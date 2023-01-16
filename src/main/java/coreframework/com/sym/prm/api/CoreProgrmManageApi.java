package coreframework.com.sym.prm.api;

import coreframework.com.cmm.ComDefaultVO;
import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.cop.ems.service.EgovSndngMailRegistService;
import coreframework.com.sym.prm.service.CoreProgrmManageService;
import coreframework.com.sym.prm.vo.ProgrmManageDtlVO;
import coreframework.com.sym.prm.vo.ProgrmManageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 프로그램목록 관리및 변경을 처리하는 비즈니스 구현 클래스
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이  용          최초 생성
 *   2011.08.22  서준식          selectProgrmChangRequstProcess() 메서드 처리일자 trim 처리
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*") // 모든 요청에 접근 허용

public class CoreProgrmManageApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreProgrmManageApi.class);

	/** Validator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** CoreProgrmManageMapper */
	@Resource(name = "CoreProgrmManageService")
    private CoreProgrmManageService progrmManageService;

	/** CoreMessageSource */
    @Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

    /** EgovSndngMailRegistService */
	@Resource(name = "sndngMailRegistService")
    private EgovSndngMailRegistService sndngMailRegistService;

    /**
     * 프로그램목록을 상세화면 호출 및 상세조회한다.
     * @param tmp_progrmNm  String
     * @return 출력페이지정보 "sym/prm/EgovProgramListDetailSelectUpdt"
     * @exception Exception
     */
    @RequestMapping(value="/sym/prm/coreProgrmApi.cm")
    public ApiResult<Object> coreProgrm(
    		@RequestParam("tmp_progrmNm") String tmp_progrmNm ,
   		    @ModelAttribute("searchVO") ComDefaultVO searchVO,
    		ModelMap model)
            throws Exception {
        // 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}
    	
    	ProgrmManageVO vo = new ProgrmManageVO();
    	vo.setProgrmFileNm(tmp_progrmNm);
    	ProgrmManageVO progrmManageVO = progrmManageService.selectProgrm(vo);
        model.addAttribute("progrmManageVO", progrmManageVO);

		try {
			return ApiResult.Data(progrmManageVO, null);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
        //return "coreframework/com/sym/prm/CoreProgramListDetailSelectUpdt";
    }

    /**
     * 프로그램목록 리스트조회한다.
     * @param searchVO ComDefaultVO
     * @return 출력페이지정보 "sym/prm/EgovProgramListManage"
     * @exception Exception
     */
    @IncludedInfo(name="프로그램관리",order = 1111 ,gid = 60)
    @RequestMapping(value="/sym/prm/coreProgrmListApi.cm")
    public ApiResult<Object> coreProgramList(
    		@ModelAttribute("searchVO") ComDefaultVO searchVO,
    		ModelMap model)
            throws Exception {
        // 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}
    	// 내역 조회
    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        List<?> list_progrmmanage = progrmManageService.selectProgrmList(searchVO);
        model.addAttribute("list_progrmmanage", list_progrmmanage);
        model.addAttribute("searchVO", searchVO);

        int totCnt = progrmManageService.selectProgrmListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

		try {
			return ApiResult.OK(list_progrmmanage, searchVO , null);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
      	//return "coreframework/com/sym/prm/CoreProgramListManage";

    }

    /**
     * 프로그램목록 멀티 삭제한다.
     * @param checkedProgrmFileNmForDel String
     * @return 출력페이지정보 "forward:/sym/prm/coreProgrmListApi.cm"
     * @exception Exception
     */
    @RequestMapping("/sym/prm/coreProgrmRemovesApi.cm")
    public ApiResult<Object> deleteProgrmManageList(
            @RequestParam("checkedProgrmFileNmForDel") String checkedProgrmFileNmForDel ,
            @ModelAttribute("progrmManageVO") ProgrmManageVO progrmManageVO,
            ModelMap model)
            throws Exception {
		String sLocationUrl = null;
    	String resultMsg    = "";
        // 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}
    	String [] delProgrmFileNm = checkedProgrmFileNmForDel.split(",");
		if (delProgrmFileNm == null || (delProgrmFileNm.length ==0)){
    		resultMsg = coreMessageSource.getMessage("fail.common.delete");
    		sLocationUrl = "forward:/sym/prm/coreProgrmListApi.cm";
		}else{
    	   progrmManageService.deleteProgrmManageList(checkedProgrmFileNmForDel);
    	   resultMsg = coreMessageSource.getMessage("success.common.delete");
    	   sLocationUrl ="forward:/sym/prm/coreProgrmListApi.cm";
		}
		model.addAttribute("resultMsg", resultMsg);
        //status.setComplete();
        //return sLocationUrl ;

		try {
			return ApiResult.Message(resultMsg);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
    }

    /**
     * 프로그램목록을 등록화면으로 이동 및 등록 한다.
     * @param progrmManageVO ProgrmManageVO
     * @param commandMap     Map
     * @return 출력페이지정보 등록화면 호출시 "sym/prm/EgovProgramListRegist",
     *         출력페이지정보 등록처리시 "forward:/sym/prm/coreProgrmListApi.cm"
     * @exception Exception
     */
    @RequestMapping(value="/sym/prm/coreProgrmRegistApi.cm")
    public ApiResult<Object> coreProgrmRegist(
    		@RequestParam Map<?, ?> commandMap,
    		@ModelAttribute("progrmManageVO") ProgrmManageVO progrmManageVO,
			BindingResult bindingResult, ComUrlVO comUrlVO,
			ModelMap model)
            throws Exception {
        String resultMsg = "";
        String sLocationUrl = null;
    	// 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}

        String sCmd = commandMap.get("cmd") == null ? "" : (String)commandMap.get("cmd");
        if(sCmd.equals("insert")){
	        beanValidator.validate(progrmManageVO, bindingResult);
			if (bindingResult.hasErrors()){
				sLocationUrl = "coreframework/com/sym/prm/CoreProgramListRegist";
				comUrlVO.setSelfUrl(sLocationUrl);
				try {
					return ApiResult.Data(comUrlVO, null);
				}catch (Exception e){
					LOGGER.error("ERROR::", e);
					return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
				}
				//return sLocationUrl;
			}
			if(progrmManageVO.getProgrmDc()==null || progrmManageVO.getProgrmDc().equals("")){progrmManageVO.setProgrmDc(" ");}
	    	progrmManageService.insertProgrm(progrmManageVO);
			resultMsg = coreMessageSource.getMessage("success.common.insert");
	        sLocationUrl = "forward:/sym/prm/coreProgrmListApi.cm";

			try {
				return ApiResult.Message(resultMsg);
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}


		}else{
            sLocationUrl = "coreframework/com/sym/prm/CoreProgramListRegist";
        }

    	model.addAttribute("resultMsg", resultMsg);

		try {
			return ApiResult.Message(resultMsg);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return sLocationUrl;
    }

    /**
     * 프로그램목록을 수정 한다.
     * @param progrmManageVO ProgrmManageVO
     * @return 출력페이지정보 "forward:/sym/prm/EgovProgramListManageSelectApi.cm"
     * @exception Exception
     */
    /*프로그램목록수정*/
    @RequestMapping(value="/sym/prm/coreProgrmUpdtApi.cm")
    public ApiResult<Object> updateProgrmList(
    		@ModelAttribute("progrmManageVO") ProgrmManageVO progrmManageVO,
    		BindingResult bindingResult,ComUrlVO comUrlVO,
    		ModelMap model)
            throws Exception {
		String resultMsg = "";
        String sLocationUrl = null;
    	// 0. Spring Security 사용자권한 처리
   	    Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
        	//return "coreframework/com/uat/uia/CoreLoginUsr";
			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
    	}

        beanValidator.validate(progrmManageVO, bindingResult);
		if (bindingResult.hasErrors()){
			//sLocationUrl = "forward:/sym/prm/coreProgrmApi.cm";
			//return sLocationUrl;
			comUrlVO.setFwdUrl("/sym/prm/coreProgrmApi.cm");
			try {
				return ApiResult.Data(comUrlVO, null);
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
		if(progrmManageVO.getProgrmDc()==null || progrmManageVO.getProgrmDc().equals("")){progrmManageVO.setProgrmDc(" ");}
		progrmManageService.updateProgrm(progrmManageVO);
		resultMsg = coreMessageSource.getMessage("success.common.update");
        sLocationUrl = "forward:/sym/prm/EgovProgramListManageSelectApi.cm";
    	model.addAttribute("resultMsg", resultMsg);

		try {
			return ApiResult.Message(resultMsg);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return sLocationUrl;
    }

    /**
     * 프로그램목록을 삭제 한다.
     * @param progrmManageVO ProgrmManageVO
     * @return 출력페이지정보 "forward:/sym/prm/EgovProgramListManageSelectApi.cm"
     * @exception Exception
     */
    @RequestMapping(value="/sym/prm/coreProgrmRemoveApi.cm")
    public ApiResult<Object> deleteProgrmList(
    		@ModelAttribute("progrmManageVO")
    		ProgrmManageVO progrmManageVO,
    		ModelMap model)
            throws Exception {
    	String resultMsg = "";
        // 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}
        progrmManageService.deleteProgrm(progrmManageVO);
        resultMsg = coreMessageSource.getMessage("success.common.delete");
    	model.addAttribute("resultMsg", resultMsg);

		try {
			return ApiResult.Message(resultMsg);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

        //return "forward:/sym/prm/EgovProgramListManageSelectApi.cm";
    }



    /**
     * 프로그램변경이력리스트를 조회한다.
     * @param searchVO ComDefaultVO
     * @return 출력페이지정보 "sym/prm/EgovProgramChgHst"
     * @exception Exception
     */
    @IncludedInfo(name="프로그램변경이력",order = 1114 ,gid = 60)
    @RequestMapping(value="/sym/prm/coreProgrmCngHstListApi.cm")
    public ApiResult<Object> coreProgrmCngHstList(
    		@ModelAttribute("searchVO") ComDefaultVO searchVO,
    		ModelMap model)
            throws Exception {
         // 0. Spring Security 사용자권한 처리
    	 Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	 if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			 try {
				 return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			 }catch (Exception e){
				 LOGGER.error("ERROR::", e);
				 return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			 }

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	 }
     	 // 내역 조회
     	 /** EgovPropertyService.sample */
     	 searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
     	 searchVO.setPageSize(propertiesService.getInt("pageSize"));

     	 /** pageing */
     	 PaginationInfo paginationInfo = new PaginationInfo();
 		 paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
 		 paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
 		 paginationInfo.setPageSize(searchVO.getPageSize());

 		 searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
 		 searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
 		 searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

         List<?> list_changerequst = progrmManageService.selectProgrmChangeRequstList(searchVO);
         model.addAttribute("list_changerequst", list_changerequst);

         int totCnt = progrmManageService.selectProgrmChangeRequstListTotCnt(searchVO);
  		 paginationInfo.setTotalRecordCount(totCnt);
         model.addAttribute("paginationInfo", paginationInfo);
		try {
			return ApiResult.OK(list_changerequst, paginationInfo, null);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
         //return "coreframework/com/sym/prm/CoreProgramChgHst";
    }

    /*프로그램변경이력상세조회*/
    /**
     * 프로그램변경이력을 상세조회한다.
     * @param progrmManageDtlVO ProgrmManageDtlVO
     * @return 출력페이지정보 "sym/prm/EgovProgramChgHstDetail"
     * @exception Exception
     */
    @RequestMapping(value="/sym/prm/coreProgrmCngHstApi.cm")
    public ApiResult<Object> coreProgrmCngHst(
    		@ModelAttribute("progrmManageDtlVO") ProgrmManageDtlVO progrmManageDtlVO,
    		ModelMap model)
            throws Exception {
         // 0. Spring Security 사용자권한 처리
    	 Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	 if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			 try {
				 return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			 }catch (Exception e){
				 LOGGER.error("ERROR::", e);
				 return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			 }

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	 }
         String _FileNm = progrmManageDtlVO.getTmpProgrmNm();
         progrmManageDtlVO.setProgrmFileNm(_FileNm);
         int _tmp_no = progrmManageDtlVO.getTmpRqesterNo();
         progrmManageDtlVO.setRqesterNo(_tmp_no);

         ProgrmManageDtlVO resultVO = progrmManageService.selectProgrmChangeRequst(progrmManageDtlVO);
         model.addAttribute("resultVO", resultVO);
        // return "coreframework/com/sym/prm/CoreProgramChgHstDetail";
		try {
			return ApiResult.Data(resultVO, null);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

    }

    /**
     * 프로그램파일명을 조회한다.
     * @param searchVO ComDefaultVO
     * @return 출력페이지정보 "sym/prm/EgovFileNmSearch"
     * @exception Exception
     */
    @RequestMapping(value="/sym/prm/coreProgrmListSearchApi.cm")
    public ApiResult<Object> coreProgrmListSearch(
    		@ModelAttribute("searchVO") ComDefaultVO searchVO,
    		ModelMap model)
            throws Exception {
        // 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}
    	// 내역 조회
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        List<?> list_progrmmanage = progrmManageService.selectProgrmList(searchVO);
        model.addAttribute("list_progrmmanage", list_progrmmanage);

        int totCnt = progrmManageService.selectProgrmListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

		try {
			return ApiResult.OK(list_progrmmanage, paginationInfo, null);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}


      	//return "coreframework/com/sym/prm/CoreFileNmSearch";

    }

    /**
     * 프로그램파일명을 조회한다. (New)
     * @param searchVO ComDefaultVO
     * @return 출력페이지정보 "sym/prm/EgovFileNmSearch"
     * @exception Exception
     */
    @RequestMapping(value="/sym/prm/coreProgrmListSearchNewApi.cm")
    public ApiResult<Object> coreProgrmListSearchNew(
    		@ModelAttribute("searchVO") ComDefaultVO searchVO,
    		ModelMap model)
            throws Exception {
        // 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

        	//return "coreframework/com/uat/uia/CoreLoginUsr";
    	}
    	// 내역 조회
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        List<?> list_progrmmanage = progrmManageService.selectProgrmList(searchVO);
        model.addAttribute("list_progrmmanage", list_progrmmanage);

        int totCnt = progrmManageService.selectProgrmListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
		try {
			return ApiResult.OK(list_progrmmanage, paginationInfo, null);
		}catch (Exception e){
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
      	//return "coreframework/com/sym/prm/CoreFileNmSearchNew";

    }
}