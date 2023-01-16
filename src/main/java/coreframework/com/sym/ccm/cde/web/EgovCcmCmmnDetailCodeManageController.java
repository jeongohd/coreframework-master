package coreframework.com.sym.ccm.cde.web;

import java.util.List;

import javax.annotation.Resource;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.ccm.ccc.service.CcmCmmnClCodeManageService;
import coreframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import coreframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CmmnDetailCode;
import coreframework.com.sym.ccm.cca.service.CmmnCodeVO;
import coreframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
import coreframework.com.sym.ccm.ccc.vo.CmmnClCodeVO;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
*
* 공통상세코드에 관한 요청을 받아 서비스 클래스로 요청을 전달하고 서비스클래스에서 처리한 결과를 웹 화면으로 전달을 위한 Controller를 정의한다
* @author 공통서비스 개발팀 이중호
* @since 2009.04.01
* @version 1.0
* @see
*
* <pre>
* << 개정이력(Modification Information) >>
*
*   수정일      수정자           수정내용
*  -------    --------    ---------------------------
*   2009.04.01  이중호       최초 생성
*   2011.08.26	정진오	IncludedInfo annotation 추가
*   2017.08.08	이정은	표준프레임워크 v3.7 개선
*
* </pre>
*/

@Controller
public class EgovCcmCmmnDetailCodeManageController {

	@Resource(name = "CmmnDetailCodeManageService")
	private EgovCcmCmmnDetailCodeManageService cmmnDetailCodeManageService;

	@Resource(name = "CcmCmmnClCodeManageService")
	private CcmCmmnClCodeManageService cmmnClCodeManageService;

	@Resource(name = "CmmnCodeManageService")
	private EgovCcmCmmnCodeManageService cmmnCodeManageService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** CoreMessageSource */
	@Resource(name = "coreMessageSource")
    CoreMessageSource coreMessageSource;

	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 공통상세코드 목록을 조회한다.
	  * @param loginVO
	  * @param searchVO
	  * @param model
	  * @return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeList"
	  * @throws Exception
	  */
	@IncludedInfo(name = "공통상세코드", listUrl = "/sym/ccm/cde/SelectCcmCmmnDetailCodeList.cm", order = 970, gid = 60)
	@RequestMapping(value = "/sym/ccm/cde/SelectCcmCmmnDetailCodeList.cm")
	public String selectCmmnDetailCodeList(@ModelAttribute("loginVO") LoginVO loginVO,
                                           @ModelAttribute("searchVO") CmmnDetailCodeVO searchVO, ModelMap model) throws Exception {

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

		List<?> CmmnCodeList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("resultList", CmmnCodeList);

		int totCnt = cmmnDetailCodeManageService.selectCmmnDetailCodeListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeList";
	}

	/**
	 * 공통상세코드 상세항목을 조회한다.
	 * @param loginVO
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeDetail"
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/ccm/cde/SelectCcmCmmnDetailCodeDetail.cm")
	public String selectCmmnDetailCodeDetail(@ModelAttribute("loginVO") LoginVO loginVO,
		CmmnDetailCodeVO cmmnDetailCodeVO, ModelMap model) throws Exception {
		CmmnDetailCode vo = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
		model.addAttribute("result", vo);

		return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeDetail";
	}

	/**
	 * 공통상세코드를 삭제한다.
	 * @param loginVO
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return "forward:/sym/ccm/cde/EgovCcmCmmnDetailCodeList.cm"
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/ccm/cde/RemoveCcmCmmnDetailCode.cm")
	public String deleteCmmnDetailCode(@ModelAttribute("loginVO") LoginVO loginVO, CmmnDetailCodeVO cmmnDetailCodeVO,
		ModelMap model) throws Exception {
		cmmnDetailCodeManageService.deleteCmmnDetailCode(cmmnDetailCodeVO);

		return "forward:/sym/ccm/cde/SelectCcmCmmnDetailCodeList.cm";
	}

	/**
	 * 공통상세코드 등록을 위한 등록페이지로 이동한다.
	 *
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sym/ccm/cde/RegistCcmCmmnDetailCodeView.cm")
	public String insertCmmnDetailCodeView(@ModelAttribute("loginVO") LoginVO loginVO,
		@ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO,
		@ModelAttribute("cmmnDetailCodeVO") CmmnDetailCodeVO cmmnDetailCodeVO, ModelMap model) throws Exception {

		CmmnClCodeVO searchClCodeVO = new CmmnClCodeVO();
		searchClCodeVO.setFirstIndex(0);
		List<?> CmmnClCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchClCodeVO);
		model.addAttribute("clCodeList", CmmnClCodeList);

		CmmnCodeVO clCode = new CmmnCodeVO();
		clCode.setClCode(cmmnCodeVO.getClCode());

		if (!cmmnCodeVO.getClCode().equals("")) {

			CmmnCodeVO searchCodeVO = new CmmnCodeVO();
			searchCodeVO.setRecordCountPerPage(999999);
			searchCodeVO.setFirstIndex(0);
			searchCodeVO.setSearchCondition("clCode");
			searchCodeVO.setSearchKeyword(cmmnCodeVO.getClCode());

			List<?> CmmnCodeList = cmmnCodeManageService.selectCmmnCodeList(searchCodeVO);
			model.addAttribute("codeList", CmmnCodeList);
		}

		return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeRegist";
	}

	/**
	 * 공통상세코드를 등록한다.
	 *
	 * @param CmmnDetailCodeVO
	 * @param CmmnDetailCodeVO
	 * @param status
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sym/ccm/cde/RegistCcmCmmnDetailCode.cm")
	public String insertCmmnDetailCode(@ModelAttribute("cmmnDetailCodeVO") CmmnDetailCodeVO cmmnDetailCodeVO,
		BindingResult bindingResult, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		CmmnClCodeVO searchClCodeVO = new CmmnClCodeVO();

		beanValidator.validate(cmmnDetailCodeVO, bindingResult);

		if (bindingResult.hasErrors()) {

			List<?> CmmnClCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchClCodeVO);
			model.addAttribute("clCodeList", CmmnClCodeList);

			return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeRegist";
		}

		if (cmmnDetailCodeVO.getCodeId() != null) {

			CmmnDetailCode vo = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
			if (vo != null) {
				model.addAttribute("message", coreMessageSource.getMessage("comSymCcmCde.validate.codeCheck"));

				List<?> CmmnClCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchClCodeVO);
				model.addAttribute("clCodeList", CmmnClCodeList);

				return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeRegist";
			}
		}

		cmmnDetailCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnDetailCodeManageService.insertCmmnDetailCode(cmmnDetailCodeVO);

		return "forward:/sym/ccm/cde/SelectCcmCmmnDetailCodeList.cm";
	}

	/**
	 * 공통상세코드 수정을 위한 수정페이지로 이동한다.
	 *
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeUpdt"
	 * @throws Exception
	 */
	@RequestMapping("/sym/ccm/cde/UpdateCcmCmmnDetailCodeView.cm")
	public String updateCmmnDetailCodeView(@ModelAttribute("loginVO") LoginVO loginVO,
		@ModelAttribute("cmmnDetailCodeVO") CmmnDetailCodeVO cmmnDetailCodeVO, ModelMap model)
		throws Exception {

		CmmnDetailCode result = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
		model.addAttribute("cmmnDetailCodeVO", result);

		return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeUpdt";
	}

	/**
	 * 공통상세코드를 수정한다.
	 *
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeUpdt", "/sym/ccm/cde/SelectCcmCmmnDetailCodeList.cm"
	 * @throws Exception
	 */
	@RequestMapping("/sym/ccm/cde/UpdateCcmCmmnDetailCode.cm")
	public String updateCmmnDetailCode(@ModelAttribute("cmmnDetailCodeVO") CmmnDetailCodeVO cmmnDetailCodeVO,
		ModelMap model, BindingResult bindingResult)
		throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		beanValidator.validate(cmmnDetailCodeVO, bindingResult);

		if (bindingResult.hasErrors()) {
			CmmnDetailCode result = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
			model.addAttribute("cmmnDetailCodeVO", result);

			return "coreframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeUpdt";
		}

		cmmnDetailCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnDetailCodeManageService.updateCmmnDetailCode(cmmnDetailCodeVO);

		return "forward:/sym/ccm/cde/SelectCcmCmmnDetailCodeList.cm";
	}

}
