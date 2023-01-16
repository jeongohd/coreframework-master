package coreframework.com.sec.drm.api;

import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.SessionVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.sec.drm.service.CoreDeptAuthorService;
import coreframework.com.sec.drm.vo.DeptAuthor;
import coreframework.com.sec.drm.vo.DeptAuthorVO;
import coreframework.com.sec.ram.service.CoreAuthorManageService;
import coreframework.com.sec.ram.vo.AuthorManageVO;
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
 * 부서권한에 관한 controller 클래스를 정의한다.
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
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * </pre>
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@SessionAttributes(types=SessionVO.class)
public class CoreDeptAuthorApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreDeptAuthorApi.class);

    @Resource(name="coreMessageSource")
	CoreMessageSource coreMessageSource;
    
    @Resource(name = "CoreDeptAuthorService")
    private CoreDeptAuthorService coreDeptAuthorService;
    
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
    @RequestMapping("/sec/drm/CoreDeptAuthorListViewApi.cm")
    public ApiResult<Object> selectDeptAuthorListViewApi(ComUrlVO comUrlVO) throws Exception {

		comUrlVO.setSelfUrl("coreframework/com/sec/drm/CoreDeptAuthorManage");
		try {
			return ApiResult.Data(comUrlVO,null);
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
       // return "coreframework/com/sec/drm/CoreDeptAuthorManage";
    }    

	/**
	 * 부서별 할당된 권한목록 조회
	 * 
	 * @param deptAuthorVO DeptAuthorVO
	 * @param authorManageVO AuthorManageVO
	 * @return String
	 * @exception Exception
	 */
    @IncludedInfo(name="부서권한관리", listUrl="/sec/drm/CoreDeptAuthorListApi.cm", order = 100,gid = 20)
    @RequestMapping(value="/sec/drm/CoreDeptAuthorListApi.cm")
	public ApiResult<Object> selectDeptAuthorListApi(@ModelAttribute("deptAuthorVO") DeptAuthorVO deptAuthorVO,
										  @ModelAttribute("authorManageVO") AuthorManageVO authorManageVO,
										  ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(deptAuthorVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(deptAuthorVO.getPageUnit());
		paginationInfo.setPageSize(deptAuthorVO.getPageSize());
		
		deptAuthorVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		deptAuthorVO.setLastIndex(paginationInfo.getLastRecordIndex());
		deptAuthorVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		deptAuthorVO.setDeptAuthorList(coreDeptAuthorService.selectDeptAuthorList(deptAuthorVO));
        model.addAttribute("deptAuthorList", deptAuthorVO.getDeptAuthorList());
        
        int totCnt = coreDeptAuthorService.selectDeptAuthorListTotCnt(deptAuthorVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

    	authorManageVO.setAuthorManageList(coreAuthorManageService.selectAuthorAllList(authorManageVO));
        model.addAttribute("authorManageList", authorManageVO.getAuthorManageList());

        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.OK(deptAuthorVO.getDeptAuthorList(), paginationInfo, coreMessageSource.getMessage("success.common.select"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

        //return "coreframework/com/sec/drm/CoreDeptAuthorManage";
	}

	@IncludedInfo(name="권한관리", listUrl="/sec/drm/CoreAuthorManageListApi.cm", order = 100,gid = 20)
	@RequestMapping(value="/sec/drm/CoreAuthorManageListApi.cm")
	public ApiResult<Object> selectAuthorManageListApi(@ModelAttribute("deptAuthorVO") DeptAuthorVO deptAuthorVO,
												  @ModelAttribute("authorManageVO") AuthorManageVO authorManageVO,
												  ModelMap model) throws Exception {



		authorManageVO.setAuthorManageList(coreAuthorManageService.selectAuthorAllList(authorManageVO));
		model.addAttribute("authorManageList", authorManageVO.getAuthorManageList());

		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.List(authorManageVO.getAuthorManageList(), coreMessageSource.getMessage("success.common.select"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/sec/drm/CoreDeptAuthorManage";
	}
	
	/**
	 * 부서에 권한정보를 할당하여 데이터베이스에 등록
	 * 
	 * @param userIds String
	 * @param authorCodes String
	 * @param regYns String
	 * @param deptAuthor DeptAuthor
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value="/sec/drm/CoreDeptAuthorInsertApi.cm")
	public ApiResult<Object> insertDeptAuthorApi(@RequestParam("userIds") String userIds,
			                       @RequestParam("authorCodes") String authorCodes,
			                       @RequestParam("regYns") String regYns,
			                       @ModelAttribute("deptAuthor") DeptAuthor deptAuthor,
			                         ModelMap model) throws Exception {
		
    	String [] strUserIds = userIds.split(";");
    	String [] strAuthorCodes = authorCodes.split(";");
    	String [] strRegYns = regYns.split(";");
    	
    	for(int i=0; i<strUserIds.length;i++) {
    		deptAuthor.setUniqId(strUserIds[i]);
    		deptAuthor.setAuthorCode(strAuthorCodes[i]);
    		if(strRegYns[i].equals("N"))
    			coreDeptAuthorService.insertDeptAuthor(deptAuthor);
    		else 
    			coreDeptAuthorService.updateDeptAuthor(deptAuthor);
    	}

        model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));

		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/sec/drm/CoreDeptAuthorListApi.cm";
	}

	/**
	 * 부서별 할당된 시스템 메뉴 접근권한을 삭제
	 * @param userIds String
	 * @param deptAuthor DeptAuthor
	 * @return String
	 * @exception Exception
	 */ 
	@RequestMapping(value="/sec/drm/CoreDeptAuthorDeleteApi.cm")
	public ApiResult<Object> deleteDeptAuthorApi (@RequestParam("userIds") String userIds,
			                        @ModelAttribute("deptAuthor") DeptAuthor deptAuthor,
                                     ModelMap model) throws Exception {
		
    	String [] strUserIds = userIds.split(";");
    	for(int i=0; i<strUserIds.length;i++) {
    		deptAuthor.setUniqId(strUserIds[i]);
    		coreDeptAuthorService.deleteDeptAuthor(deptAuthor);
    	}
    	
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/sec/drm/CoreDeptAuthorListApi.cm";
	}
	
    /**
	 * 부서조회 팝업 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sec/drm/CoreDeptSearchViewApi.cm")
    public ApiResult<Object> selectDeptListViewApi(ComUrlVO comUrlVO) throws Exception {
        //return "coreframework/com/sec/drm/CoreDeptSearch";

		comUrlVO.setSelfUrl("coreframework/com/sec/drm/CoreDeptSearch");
		try {
			return ApiResult.Data(comUrlVO,null);
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

    }    	
	
	/**
	 * 부서별 할당된 권한목록 조회
	 * @param deptAuthorVO DeptAuthorVO
	 * @return String
	 * @exception Exception
	 */
    //@IncludedInfo(name="부서목록관리", order = 101)
    @RequestMapping(value="/sec/drm/CoreDeptSearchListApi.cm")
	public ApiResult<Object> selectDeptListApi(@ModelAttribute("deptAuthorVO") DeptAuthorVO deptAuthorVO,
			                             ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(deptAuthorVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(deptAuthorVO.getPageUnit());
		paginationInfo.setPageSize(deptAuthorVO.getPageSize());
		
		deptAuthorVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		deptAuthorVO.setLastIndex(paginationInfo.getLastRecordIndex());
		deptAuthorVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		deptAuthorVO.setDeptList(coreDeptAuthorService.selectDeptList(deptAuthorVO));
        model.addAttribute("deptList", deptAuthorVO.getDeptList());
        
        int totCnt = coreDeptAuthorService.selectDeptListTotCnt(deptAuthorVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		try {
			return ApiResult.OK(deptAuthorVO.getDeptList(), paginationInfo, coreMessageSource.getMessage("success.common.select"));
		}catch (Exception e){
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

       // return "coreframework/com/sec/drm/CoreDeptSearch";
	}
}