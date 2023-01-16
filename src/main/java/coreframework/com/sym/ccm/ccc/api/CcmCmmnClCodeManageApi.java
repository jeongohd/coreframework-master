package coreframework.com.sym.ccm.ccc.api;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.ccm.ccc.service.CcmCmmnClCodeManageService;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCode;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCodeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * 공통분류코드에 관한 요청을 받아 서비스 클래스로 요청을 전달하고 서비스클래스에서 처리한 결과를 웹 화면으로 전달을 위한 Controller를 정의한다
 * 
 * @author 공통서비스 개발팀 이중호
 * @since 2009.04.01
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *	     수정일		수정자          			 수정내용
 *  ---------  -------   ---------------------------
 *  2009.04.01	이중호         최초 생성
 *  2011.8.26	정진오	 IncludedInfo annotation 추가
 *  2017.06.08	이정은	 표준프레임워크 v3.7 개선
 *
 *      </pre>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("*") // 모든 요청에 접근 허용
public class CcmCmmnClCodeManageApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CcmCmmnClCodeManageApi.class);

	@Resource(name = "CcmCmmnClCodeManageService")
	private CcmCmmnClCodeManageService cmmnClCodeManageService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** CoreMessageSource */
	@Resource(name = "coreMessageSource")
    CoreMessageSource coreMessageSource;

	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 공통분류코드 목록을 조회한다.
	 * 
	 * @param loginVO
	 * @param searchVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/ccc/SelectCcmCmmnClCodeList"
	 * @throws Exception
	 */
	@IncludedInfo(name = "공통분류코드", listUrl = "/sym/ccm/ccc/SelectCcmCmmnClCodeListApi.cm", order = 960, gid = 60)
	@RequestMapping(value = "/sym/ccm/ccc/SelectCcmCmmnClCodeListApi.cm")
	public ApiResult<Object> selectCmmnClCodeListApi(@ModelAttribute("searchVO") CmmnClCodeVO searchVO, ModelMap model)  {
		
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

		try{
			List<?> CmmnCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchVO);
//			model.addAttribute("resultList", CmmnCodeList);

			int totCnt = cmmnClCodeManageService.selectCmmnClCodeListTotCnt(searchVO);
			paginationInfo.setTotalRecordCount(totCnt);
//			model.addAttribute("paginationInfo", paginationInfo);

			return ApiResult.OK(CmmnCodeList,paginationInfo,null);
		} catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

	}

	/**
	 * 공통분류코드 상세항목을 조회한다.
	 * 
	 * @param loginVO
	 * @param cmmnClCode
	 * @param model
	 * @return "coreframework/com/sym/ccm/ccc/SelectCcmCmmnClCodeDetail.cm"
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/ccm/ccc/SelectCcmCmmnClCodeDetailApi.cm")
	public ApiResult<Object> selectCmmnClCodeDetailApi(@ModelAttribute("loginVO") LoginVO loginVO, CmmnClCodeVO cmmnClCodeVO,
			ModelMap model)  {
		try {
			CmmnClCode vo = cmmnClCodeManageService.selectCmmnClCodeDetail(cmmnClCodeVO);

			model.addAttribute("result", vo);
			return ApiResult.Data(vo, null);
		} catch (Exception e){

			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

	}
	
	/**
	 * 공통분류코드 등록을 위한 등록페이지로 이동한다.
	 * 
	 * @param cmmnClCodeVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/ccc/EgovCcmCmmnClCodeRegist";
	 * @throws Exception
	 */
	@RequestMapping("/sym/ccm/ccc/RegistCcmCmmnClCodeViewApi.cm")
	public ApiResult<Object> insertCmmnClCodeViewApi(@ModelAttribute("searchVO")CmmnClCodeVO cmmnClCodeVO, ModelMap model)  {
		try {
			model.addAttribute("cmmnClCodeVO", new CmmnClCodeVO());
			return ApiResult.Data(new CmmnClCodeVO(), null);

		} catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}
	
	 /**
     * 공통분류코드를 등록한다.
     * 
     * @param CmmnClCodeVO
     * @param CmmnClCodeVO
     * @param status
     * @param model
     * @return /sym/ccm/ccc/SelectCcmCmmnClCodeList.cm";
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/ccc/RegistCcmCmmnClCodeApi.cm")
    public ApiResult<Object> insertCmmnClCodeApi(@ModelAttribute("cmmnClCodeVO") CmmnClCodeVO cmmnClCodeVO,
	    BindingResult bindingResult, ModelMap model)  {
		try {
			// 로그인VO에서  사용자 정보 가져오기
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

			beanValidator.validate(cmmnClCodeVO, bindingResult);

			if (bindingResult.hasErrors()) {
				return ApiResult.Message(null);
			}

			if (cmmnClCodeVO.getClCode() != null) {
				CmmnClCode vo = cmmnClCodeManageService.selectCmmnClCodeDetail(cmmnClCodeVO);
				if (vo != null) {
					model.addAttribute("message", coreMessageSource.getMessage("comSymCcmCcc.validate.codeCheck"));
					return ApiResult.Message(coreMessageSource.getMessage("comSymCcmCcc.validate.codeCheck"));
				}
			}

			cmmnClCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
			cmmnClCodeManageService.insertCmmnClCode(cmmnClCodeVO);

			return ApiResult.Message( null);

		} catch(Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
    }

    /**
     * 공통분류코드를 삭제한다.
     * 
     * @param cmmnClCodeVO
     * @param status
     * @param model
     * @return /sym/ccm/ccc/SelectCcmCmmnClCodeList.cm";
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/ccc/RemoveCcmCmmnClCodeApi.cm")
    public ApiResult<Object> deleteCmmnClCodeApi(@ModelAttribute("searchVO") CmmnClCodeVO cmmnClCode, @ModelAttribute("cmmnClCodeVO") CmmnClCodeVO cmmnClCodeVO,
	    BindingResult bindingResult, ModelMap model)  {

    	LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		try {
			cmmnClCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
			cmmnClCodeManageService.deleteCmmnClCode(cmmnClCodeVO);
			String forward = "forward:/sym/ccm/ccc/SelectCcmCmmnClCodeList.cm";
			return ApiResult.Data(forward, null);

		} catch(Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}
    
    /**
     * 공통분류코드 수정을 위한 수정페이지로 이동한다.
     * 
     * @param cmmnClCodeVO
     * @param model
     * @return "coreframework/com/sym/ccm/ccc/EgovCcmCmmnClCodeUpdt";
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/ccc/UpdateCcmCmmnClCodeViewApi.cm")
    public ApiResult<Object> updateCmmnClCodeViewApi(@ModelAttribute("searchVO") CmmnClCodeVO cmmnClCodeVO, ModelMap model) {
		try {

			CmmnClCode result = cmmnClCodeManageService.selectCmmnClCodeDetail(cmmnClCodeVO);

			model.addAttribute("cmmnClCodeVO", result);

//			return "coreframework/com/sym/ccm/ccc/EgovCcmCmmnClCodeUpdt";
			return ApiResult.Data(result, null);

		} catch(Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
    }
    
    /**
     * 공통분류코드를 수정한다.
     * 
     * @param cmmnClCodeVO
     * @param status
     * @param model
     * @return /sym/ccm/ccc/SelectCcmCmmnClCodeList.cm"
     * @throws Exception
     */
    @RequestMapping("/sym/ccm/ccc/UpdateCcmCmmnClCodeApi.cm")
    public ApiResult<Object> updateCmmnClCodeApi(@ModelAttribute("searchVO") CmmnClCodeVO cmmnClCode, @ModelAttribute("cmmnClCodeVO") CmmnClCodeVO cmmnClCodeVO,
	    BindingResult bindingResult, ModelMap model)  {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		try {
			beanValidator.validate(cmmnClCodeVO, bindingResult);
			if (bindingResult.hasErrors()) {

				CmmnClCode result = cmmnClCodeManageService.selectCmmnClCodeDetail(cmmnClCode);
				model.addAttribute("cmmnClCodeVO", result);

//				return "coreframework/com/sym/ccm/ccc/EgovCcmCmmnClCodeUpdt";
				return ApiResult.Data(result, null);
			}

			cmmnClCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
			cmmnClCodeManageService.updateCmmnClCode(cmmnClCodeVO);
			String forward = "forward:/sym/ccm/ccc/SelectCcmCmmnClCodeList.cm";
//			return "forward:/sym/ccm/ccc/SelectCcmCmmnClCodeList.cm";
			return ApiResult.Data(forward, null);

		} catch(Exception e) {
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
    }

}