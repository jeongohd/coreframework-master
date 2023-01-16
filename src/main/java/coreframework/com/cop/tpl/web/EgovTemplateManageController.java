package coreframework.com.cop.tpl.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import coreframework.com.cop.tpl.service.EgovTemplateManageService;
import coreframework.com.cop.tpl.service.TemplateInf;
import coreframework.com.cop.tpl.service.TemplateInfVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 템플릿 관리를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------        --------    ---------------------------
 *   2009. 3.18  이삼섭          최초 생성
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * </pre>
 */

@Controller
public class EgovTemplateManageController {

	@Resource(name = "EgovTemplateManageService")
	private EgovTemplateManageService tmplatService;

	@Resource(name = "CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	@Autowired
	private DefaultBeanValidator beanValidator;

	//Logger log = Logger.getLogger(this.getClass());

	/**
	 * 템플릿 목록을 조회한다.
	 *
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name = "템플릿관리", order = 200, gid = 40)
	@RequestMapping("/cop/tpl/selectTemplateInfs.cm")
	public String selectTemplateInfs(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, ModelMap model) throws Exception {
		tmplatInfVO.setPageUnit(propertyService.getInt("pageUnit"));
		tmplatInfVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(tmplatInfVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(tmplatInfVO.getPageUnit());
		paginationInfo.setPageSize(tmplatInfVO.getPageSize());

		tmplatInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		tmplatInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
		tmplatInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = tmplatService.selectTemplateInfs(tmplatInfVO);
		int totCnt = Integer.parseInt((String) map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/cop/tpl/EgovTemplateList";
	}

	/**
	 * 템플릿에 대한 상세정보를 조회한다.
	 *
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/tpl/selectTemplateInf.cm")
	public String selectTemplateInf(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, ModelMap model) throws Exception {

		ComDefaultCodeVO codeVO = new ComDefaultCodeVO();

		codeVO.setCodeId("COM005");
		List<?> result = cmmUseService.selectCmmCodeDetail(codeVO);

		TemplateInfVO vo = tmplatService.selectTemplateInf(tmplatInfVO);

		model.addAttribute("TemplateInfVO", vo);
		model.addAttribute("resultList", result);

		return "coreframework/com/cop/tpl/EgovTemplateUpdt";
	}

	/**
	 * 템플릿 정보를 등록한다.
	 *
	 * @param searchVO
	 * @param tmplatInfo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/tpl/insertTemplateInf.cm")
	public String insertTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, @ModelAttribute("templateInf") TemplateInf templateInf, BindingResult bindingResult,
                                    SessionStatus status, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

		beanValidator.validate(templateInf, bindingResult);

		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO vo = new ComDefaultCodeVO();

			vo.setCodeId("COM005");

			List<?> result = cmmUseService.selectCmmCodeDetail(vo);

			model.addAttribute("resultList", result);

			return "coreframework/com/cop/tpl/EgovTemplateRegist";
		}

		templateInf.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));

		if (isAuthenticated) {
			tmplatService.insertTemplateInf(templateInf);
		}

		return "forward:/cop/tpl/selectTemplateInfs.cm";
	}

	/**
	 * 템플릿 등록을 위한 등록페이지로 이동한다.
	 *
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/tpl/addTemplateInf.cm")
	public String addTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, ModelMap model) throws Exception {
		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		vo.setCodeId("COM005");

		List<?> result = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("resultList", result);

		return "coreframework/com/cop/tpl/EgovTemplateRegist";
	}

	/**
	 * 템플릿 정보를 수정한다.
	 *
	 * @param searchVO
	 * @param tmplatInfo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/tpl/updateTemplateInf.cm")
	public String updateTemplateInf(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, @ModelAttribute("templateInf") TemplateInf templateInf, BindingResult bindingResult,
			SessionStatus status, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

		beanValidator.validate(templateInf, bindingResult);

		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO codeVO = new ComDefaultCodeVO();

			codeVO.setCodeId("COM005");

			List<?> result = cmmUseService.selectCmmCodeDetail(codeVO);

			TemplateInfVO vo = tmplatService.selectTemplateInf(tmplatInfVO);

			model.addAttribute("TemplateInfVO", vo);
			model.addAttribute("resultList", result);

			return "coreframework/com/cop/tpl/EgovTemplateUpdt";
		}

		templateInf.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));

		if (isAuthenticated) {
			tmplatService.updateTemplateInf(templateInf);
		}

		return "forward:/cop/tpl/selectTemplateInfs.cm";
	}

	/**
	 * 템플릿 정보를 삭제한다.
	 *
	 * @param searchVO
	 * @param tmplatInfo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/bbs/deleteTemplateInf.cm")
	public String deleteTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, @ModelAttribute("tmplatInf") TemplateInf tmplatInf, SessionStatus status, ModelMap model)
			throws Exception {

		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

		tmplatInf.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));

		if (isAuthenticated) {
			tmplatService.deleteTemplateInf(tmplatInf);
		}

		return "forward:/cop/tpl/selectTemplateInfs.cm";
	}

	/**
	 * 팝업을 위한 템플릿 목록을 조회한다.
	 *
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cop/tpl/selectTemplateInfsPop.cm")
	public String selectTemplateInfsPop(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {

		String typeFlag = (String) commandMap.get("typeFlag");

		if ("CLB".equals(typeFlag)) {
			tmplatInfVO.setTypeFlag(typeFlag);
			tmplatInfVO.setTmplatSeCode("TMPT03");
		} else if ("CMY".equals(typeFlag)) {
			tmplatInfVO.setTypeFlag(typeFlag);
			tmplatInfVO.setTmplatSeCode("TMPT02");
		} else {
			tmplatInfVO.setTypeFlag(typeFlag);
			tmplatInfVO.setTmplatSeCode("TMPT01");
		}

		tmplatInfVO.setPageUnit(propertyService.getInt("pageUnit"));
		tmplatInfVO.setPageSize(propertyService.getInt("pageSize"));
		//CMY, CLB

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(tmplatInfVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(tmplatInfVO.getPageUnit());
		paginationInfo.setPageSize(tmplatInfVO.getPageSize());

		tmplatInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		tmplatInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
		tmplatInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = tmplatService.selectTemplateInfs(tmplatInfVO);
		int totCnt = Integer.parseInt((String) map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("typeFlag", typeFlag);
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/cop/tpl/EgovTemplateInqirePopup";
	}
}
