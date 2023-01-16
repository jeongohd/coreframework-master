package coreframework.com.sym.bat.web;

import java.util.List;

import javax.annotation.Resource;

import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.bat.service.BatchOpert;
import coreframework.com.sym.bat.service.EgovBatchOpertService;
import coreframework.com.sym.bat.validation.BatchOpertValidator;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 배치작업관리에 대한 controller 클래스를 정의한다.
 *
 * 배치작업관리에 대한 등록, 수정, 삭제, 조회 기능을 제공한다.
 * 배치작업관리의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author 김진만
 * @since 2010.06.17
 * @version 1.0
 * @updated 17-6-2010 오전 10:27:13
 * @see
 * <pre>
 * == 개정이력(Modification Information) ==
 *
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *  2010.06.17   김진만     최초 생성
 *  2011.8.26	정진오			IncludedInfo annotation 추가
 * </pre>
 */

@Controller
public class EgovBatchOpertController {

	/** egovBatchOpertService */
	@Resource(name = "egovBatchOpertService")
	private EgovBatchOpertService egovBatchOpertService;

	/* Property 서비스 */
	@Resource(name = "propertiesService")
	private EgovPropertyService propertyService;

	/* 메세지 서비스 */
	@Resource(name = "coreMessageSource")
	private CoreMessageSource coreMessageSource;

	/* common validator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/* batchOpert bean validator */
	@Resource(name = "batchOpertValidator")
	private BatchOpertValidator batchOpertValidator;

	/** ID Generation */
	@Resource(name = "egovBatchOpertIdGnrService")
	private EgovIdGnrService idgenService;

	/** logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovBatchOpertController.class);

	/**
	 * 배치작업을 삭제한다.
	 * @return 리턴URL
	 *
	 * @param batchOpert 삭제대상 배치작업model
	 * @param model		ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/deleteBatchOpert.cm")
	public String deleteBatchOpert(BatchOpert batchOpert, ModelMap model) throws Exception {
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		egovBatchOpertService.deleteBatchOpert(batchOpert);

		return "forward:/sym/bat/getBatchOpertList.cm";
	}

	/**
	 * 배치작업을 등록한다.
	 * @return 리턴URL
	 *
	 * @param batchOpert 등록대상 배치작업model
	 * @param bindingResult	BindingResult
	 * @param model			ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/addBatchOpert.cm")
	public String insertBatchOpert(BatchOpert batchOpert, BindingResult bindingResult, ModelMap model) throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}

		//로그인 객체 선언
		LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		beanValidator.validate(batchOpert, bindingResult);
		batchOpertValidator.validate(batchOpert, bindingResult);
		if (bindingResult.hasErrors()) {
			return "coreframework/com/sym/bat/EgovBatchOpertRegist";
		} else {
			batchOpert.setBatchOpertId(idgenService.getNextStringId());
			//아이디 설정
			batchOpert.setLastUpdusrId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));
			batchOpert.setFrstRegisterId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));

			egovBatchOpertService.insertBatchOpert(batchOpert);
			//Exception 없이 진행시 등록성공메시지
			model.addAttribute("resultMsg", "success.common.insert");
		}
		return "forward:/sym/bat/getBatchOpertList.cm";
	}

	/**
	 * 배치작업정보을 상세조회한다.
	 * @return 리턴URL
	 *
	 * @param batchOpert 조회대상 배치작업model
	 * @param model		ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/getBatchOpert.cm")
	public String selectBatchOpert(@ModelAttribute("searchVO") BatchOpert batchOpert, ModelMap model) throws Exception {
		LOGGER.debug(" 조회조건 : {}", batchOpert);
		BatchOpert result = egovBatchOpertService.selectBatchOpert(batchOpert);
		model.addAttribute("resultInfo", result);
		LOGGER.debug(" 결과값 : {}", result);

		return "coreframework/com/sym/bat/EgovBatchOpertDetail";
	}

	/**
	 * 등록화면을 위한 배치작업정보을 조회한다.
	 * @return 리턴URL
	 *
	 * @param batchOpert 조회대상 배치작업model
	 * @param model		ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/getBatchOpertForRegist.cm")
	public String selectBatchOpertForRegist(@ModelAttribute("searchVO") BatchOpert batchOpert, ModelMap model) throws Exception {
		model.addAttribute("batchOpert", batchOpert);

		return "coreframework/com/sym/bat/EgovBatchOpertRegist";
	}

	/**
	 * 수정화면을 위한 배치작업정보을 조회한다.
	 * @return 리턴URL
	 *
	 * @param batchOpert 조회대상 배치작업model
	 * @param model		ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/getBatchOpertForUpdate.cm")
	public String selectBatchOpertForUpdate(@ModelAttribute("searchVO") BatchOpert batchOpert, ModelMap model) throws Exception {
		LOGGER.debug(" 조회조건 : {}", batchOpert);
		BatchOpert result = egovBatchOpertService.selectBatchOpert(batchOpert);
		model.addAttribute("batchOpert", result);
		LOGGER.debug(" 결과값 : {}", result);

		return "coreframework/com/sym/bat/EgovBatchOpertUpdt";
	}

	/**
	 * 배치작업 목록을 조회한다.
	 * @return 리턴URL
	 *
	 * @param searchVO 목록조회조건VO
	 * @param model		ModelMap
	 * @param popupAt	팝업여부
	 * @exception Exception Exception
	 */
	@SuppressWarnings("unchecked")
	@IncludedInfo(name = "배치작업관리", listUrl = "/sym/bat/getBatchOpertList.cm", order = 1120, gid = 60)
	@RequestMapping("/sym/bat/getBatchOpertList.cm")
	public String selectBatchOpertList(@ModelAttribute("searchVO") BatchOpert searchVO, ModelMap model, @RequestParam(value = "popupAt", required = false) String popupAt)
			throws Exception {
		searchVO.setPageUnit(propertyService.getInt("pageUnit"));
		searchVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<BatchOpert> resultList = (List<BatchOpert>) egovBatchOpertService.selectBatchOpertList(searchVO);
		int totCnt = egovBatchOpertService.selectBatchOpertListCnt(searchVO);

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", resultList);
		model.addAttribute("resultCnt", totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		if ("Y".equals(popupAt)) {
			// Popup 화면이면
			return "coreframework/com/sym/bat/EgovBatchOpertListPopup";
		} else {
			// 메인화면 호출이면
			return "coreframework/com/sym/bat/EgovBatchOpertList";
		}

	}

	/**
	 * 배치작업을 수정한다.
	 * @return 리턴URL
	 *
	 * @param batchOpert 수정대상 배치작업model
	 * @param bindingResult		BindingResult
	 * @param model				ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/updateBatchOpert.cm")
	public String updateBatchOpert(BatchOpert batchOpert, BindingResult bindingResult, ModelMap model) throws Exception {
		// 0. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		//로그인 객체 선언
		LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		beanValidator.validate(batchOpert, bindingResult);
		batchOpertValidator.validate(batchOpert, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("batchOpert", batchOpert);
			return "coreframework/com/sym/bat/EgovBatchOpertUpdt";
		}

		// 정보 업데이트
		batchOpert.setLastUpdusrId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));
		egovBatchOpertService.updateBatchOpert(batchOpert);

		return "forward:/sym/bat/getBatchOpertList.cm";

	}

	/**
	 * 배치작업 조회팝업을 실행한다.
	 * @return 리턴URL
	 *
	 * @param searchVO 목록조회조건VO
	 * @param model		ModelMap
	 * @exception Exception Exception
	 */
	@RequestMapping("/sym/bat/getBatchOpertListPopup.cm")
	public String openPopupWindow(@ModelAttribute("searchVO") BatchOpert searchVO, ModelMap model) throws Exception {
		return "coreframework/com/sym/bat/EgovBatchOpertListPopupFrame";
	}

}