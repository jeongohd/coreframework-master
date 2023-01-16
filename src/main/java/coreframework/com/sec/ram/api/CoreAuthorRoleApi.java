package coreframework.com.sec.ram.api;

import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.sec.ram.mapper.CoreAuthorRoleManageMapper;
import coreframework.com.sec.ram.vo.AuthorRoleManage;
import coreframework.com.sec.ram.vo.AuthorRoleManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 권한별 롤관리에 관한 controller 클래스를 정의한다.
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
 *   2011.09.07  서준식          롤 등록시 이미 등록된 경우 데이터 중복 에러 발생 문제 수정
 * </pre>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CoreAuthorRoleApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreAuthorRoleApi.class);
	
    @Resource(name="coreMessageSource")
	CoreMessageSource coreMessageSource;
    
    @Resource(name = "CoreAuthorRoleManageMapper'")
    private CoreAuthorRoleManageMapper coreAuthorRoleManageService;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * 권한 롤 관계 화면 이동
	 * @return "coreframework/com/sec/ram/CoreDeptAuthorList"
	 * @exception Exception
	 */
    @RequestMapping("/sec/ram/CoreAuthorRoleListViewApi.cm")
    public ApiResult<Object> selectAuthorRoleListViewApi(ComUrlVO comUrlVO) throws Exception {

		comUrlVO.setSelfUrl("coreframework/com/sec/ram/CoreAuthorRoleManage");
		try {
			return ApiResult.Data(comUrlVO,null);
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
        //return "coreframework/com/sec/ram/CoreAuthorRoleManage";
    } 

	/**
	 * 권한별 할당된 롤 목록 조회
	 * 
	 * @param authorRoleManageVO AuthorRoleManageVO
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping(value="/sec/ram/CoreAuthorRoleListApi.cm")
	public ApiResult<Object> selectAuthorRoleListApi(@ModelAttribute("authorRoleManageVO") AuthorRoleManageVO authorRoleManageVO,
			                            ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(authorRoleManageVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(authorRoleManageVO.getPageUnit());
		paginationInfo.setPageSize(authorRoleManageVO.getPageSize());
		
		authorRoleManageVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		authorRoleManageVO.setLastIndex(paginationInfo.getLastRecordIndex());
		authorRoleManageVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		authorRoleManageVO.setAuthorRoleList(coreAuthorRoleManageService.selectAuthorRoleList(authorRoleManageVO));
        model.addAttribute("authorRoleList", authorRoleManageVO.getAuthorRoleList());
        model.addAttribute("searchVO", authorRoleManageVO);
        
        int totCnt = coreAuthorRoleManageService.selectAuthorRoleListTotCnt(authorRoleManageVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.OK(authorRoleManageVO.getAuthorRoleList(),paginationInfo, coreMessageSource.getMessage("success.common.select"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
       // return "coreframework/com/sec/ram/CoreAuthorRoleManage";
	}
    
	/**
	 * 권한정보에 롤을 할당하여 데이터베이스에 등록
	 * @param authorCode String
	 * @param roleCodes String
	 * @param regYns String
	 * @param authorRoleManage AuthorRoleManage
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/sec/ram/CoreAuthorRoleInsertApi.cm")
	public ApiResult<Object> insertAuthorRoleApi(@RequestParam("authorCode") String authorCode,
			                       @RequestParam("roleCodes") String roleCodes,
			                       @RequestParam("regYns") String regYns,
			                       @RequestParam Map<?, ?> commandMap,
			                       @ModelAttribute("authorRoleManage") AuthorRoleManage authorRoleManage,
												 ComUrlVO comUrlVO,
			                         ModelMap model) throws Exception {
		
    	String [] strRoleCodes = roleCodes.split(";");
    	String [] strRegYns = regYns.split(";");
    	    	
    	authorRoleManage.setRoleCode(authorCode);
    	
    	for(int i=0; i<strRoleCodes.length;i++) {
    		
    		authorRoleManage.setRoleCode(strRoleCodes[i]);
    		authorRoleManage.setRegYn(strRegYns[i]);
    		if(strRegYns[i].equals("Y")){
    			coreAuthorRoleManageService.deleteAuthorRole(authorRoleManage);//2011.09.07
				coreAuthorRoleManageService.insertAuthorRole(authorRoleManage);
    		}else {
				coreAuthorRoleManageService.deleteAuthorRole(authorRoleManage);
    		}
    	}

		comUrlVO.setRdrUrl("/sec/ram/CoreAuthorRoleList.cm?searchKeyword="+authorCode);
		try {
			return ApiResult.Data(comUrlVO,null);
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "redirect:/sec/ram/CoreAuthorRoleList.cm?searchKeyword="+authorCode;
	}    
}