package coreframework.com.sec.rgm.api;

import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.SessionVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.sec.ram.service.CoreAuthorManageService;
import coreframework.com.sec.ram.vo.AuthorManageVO;
import coreframework.com.sec.rgm.service.CoreAuthorGroupService;
import coreframework.com.sec.rgm.vo.AuthorGroup;
import coreframework.com.sec.rgm.vo.AuthorGroupVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 권한그룹에 관한 controller 클래스를 정의한다.
 * @author 공통서비스 개발팀 이문준
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.11  이문준          최초 생성
 *   2011.08.04  서준식          mberTyCodes 구분자 부분 추가
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 * </pre>
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@SessionAttributes(types=SessionVO.class)
public class CoreAuthorGroupApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreAuthorGroupApi.class);
    @Resource(name="coreMessageSource")
	CoreMessageSource coreMessageSource;
    
    @Resource(name = "CoreAuthorGroupService")
    private CoreAuthorGroupService coreAuthorGroupService;
    
    @Resource(name = "CoreAuthorManageService")
    private CoreAuthorManageService coreAuthorManageService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * 권한 목록화면 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sec/rgm/CoreAuthorGroupListViewApi.cm")
    public ApiResult<Object> selectAuthorGroupListViewApi(ComUrlVO comUrlVO) throws Exception {
		comUrlVO.setSelfUrl("coreframework/com/sec/rgm/CoreAuthorGroupManage");

		try {
			return ApiResult.Data(comUrlVO,null);
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
       // return "coreframework/com/sec/rgm/CoreAuthorGroupManage";
    }    

	/**
	 * 그룹별 할당된 권한 목록 조회
	 * @param authorGroupVO AuthorGroupVO
	 * @param authorManageVO AuthorManageVO
	 * @return String
	 * @exception Exception
	 */
    @IncludedInfo(name="권한그룹관리", listUrl="/sec/rgm/CoreAuthorGroupListApi.cm", order = 70,gid = 20)
    @RequestMapping(value="/sec/rgm/CoreAuthorGroupListApi.cm")
	public ApiResult<Object> selectAuthorGroupListApi(@ModelAttribute("authorGroupVO") AuthorGroupVO authorGroupVO,
												   @ModelAttribute("authorManageVO") AuthorManageVO authorManageVO,
												   ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(authorGroupVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(authorGroupVO.getPageUnit());
		paginationInfo.setPageSize(authorGroupVO.getPageSize());
		
		authorGroupVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		authorGroupVO.setLastIndex(paginationInfo.getLastRecordIndex());
		authorGroupVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		authorGroupVO.setAuthorGroupList(coreAuthorGroupService.selectAuthorGroupList(authorGroupVO));
        model.addAttribute("authorGroupList", authorGroupVO.getAuthorGroupList());
        
        int totCnt = coreAuthorGroupService.selectAuthorGroupListTotCnt(authorGroupVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

    	authorManageVO.setAuthorManageList(coreAuthorManageService.selectAuthorAllList(authorManageVO));
        model.addAttribute("authorManageList", authorManageVO.getAuthorManageList());

        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.OK(authorGroupVO.getAuthorGroupList(),paginationInfo, coreMessageSource.getMessage("success.common.select"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

        //return "coreframework/com/sec/rgm/CoreAuthorGroupManage";
	}

	/**
	 * 그룹에 권한정보를 할당하여 데이터베이스에 등록
	 * @param userIds String
	 * @param authorCodes String
	 * @param regYns String
	 * @param authorGroup AuthorGroup
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/sec/rgm/CoreAuthorGroupInsertApi.cm")
	public ApiResult<Object> insertAuthorGroupApi(@RequestParam("userIds") String userIds,
			                        @RequestParam("authorCodes") String authorCodes,
			                        @RequestParam("regYns") String regYns,
			                        @RequestParam("mberTyCodes") String mberTyCodes,// 2011.08.04 수정 부분
			                        @ModelAttribute("authorGroup") AuthorGroup authorGroup,
			                         ModelMap model) throws Exception {
		
    	String [] strUserIds = userIds.split(";");
    	String [] strAuthorCodes = authorCodes.split(";");
    	String [] strRegYns = regYns.split(";");
    	String [] strMberTyCodes = mberTyCodes.split(";");// 2011.08.04 수정 부분
    	
    	for(int i=0; i<strUserIds.length;i++) {
    		authorGroup.setUniqId(strUserIds[i]);
    		authorGroup.setAuthorCode(strAuthorCodes[i]);
    		authorGroup.setMberTyCode(strMberTyCodes[i]);// 2011.08.04 수정 부분
    		if(strRegYns[i].equals("N"))
    		    coreAuthorGroupService.insertAuthorGroup(authorGroup);
    		else 
    		    coreAuthorGroupService.updateAuthorGroup(authorGroup);
    	}

        model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));

		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/sec/rgm/CoreAuthorGroupListApi.cm";
	}

	/**
	 * 그룹별 할당된 시스템 메뉴 접근권한을 삭제
	 * @param userIds String
	 * @param authorGroup AuthorGroup
	 * @return String
	 * @exception Exception
	 */ 
	@RequestMapping(value="/sec/rgm/CoreAuthorGroupDeleteApi.cm")
	public ApiResult<Object> deleteAuthorGroupApi(@RequestParam("userIds") String userIds,
											   @ModelAttribute("authorGroup") AuthorGroup authorGroup,
											   ModelMap model) throws Exception {
		
    	String [] strUserIds = userIds.split(";");
    	for(int i=0; i<strUserIds.length;i++) {
    		authorGroup.setUniqId(strUserIds[i]);
    		coreAuthorGroupService.deleteAuthorGroup(authorGroup);
    	}
    	
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));

		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "forward:/sec/rgm/CoreAuthorGroupListApi.cm";
	}

}