package coreframework.com.sym.sym.nwk.web;
import java.util.List;

import javax.annotation.Resource;

import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.sym.nwk.service.EgovNtwrkService;
import coreframework.com.sym.sym.nwk.service.Ntwrk;
import coreframework.com.sym.sym.nwk.service.NtwrkVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 개요
 * - 네트워크관리에 대한 controller 클래스를 정의한다.
 *
 * 상세내용
 * - 네트워크관리에 대한 등록, 수정, 삭제, 조회 기능을 제공한다.
 * - 네트워크관리의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author 이문준
 * @version 1.0
 * @created 28-6-2010 오전 10:44:24
 * <pre>
 * == 개정이력(Modification Information) ==
 *
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *  2010.06.28   이문준     최초 생성
 *  2011.8.26	정진오			IncludedInfo annotation 추가
 * </pre>
 */

@Controller
public class EgovNtwrkController {




    @Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

    @Resource(name = "egovNtwrkService")
    private EgovNtwrkService egovNtwrkService;

    /** Message ID Generation */
    @Resource(name="egovNtwrkIdGnrService")
    private EgovIdGnrService egovNtwrkIdGnrService;

    @Autowired
	private DefaultBeanValidator beanValidator;

    @Resource(name = "CoreCmmUseService")
	CoreCmmUseService CoreCmmUseService;

	/**
	 * 네트워크관리 목록화면 이동
	 * @return String
	 */
	@RequestMapping(value="/sym/sym/nwk/selectNtwrkListView.cm")
	public String selectNtwrkListView(ModelMap model) throws Exception {
		model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM067"));
		return "coreframework/com/sym/sym/nwk/EgovNtwrkList";
	}

	/**
	 * 네트워크정보를 관리하기 위해 등록된 네트워크목록을 조회한다.
	 * @param ntwrkVO - 네트워크 Vo
	 * @return String - 리턴 Url
	 *
	 * @param ntwrkVO
	 */
	@IncludedInfo(name="네트워크관리",order = 1160 ,gid = 60)
	@RequestMapping(value="/sym/sym/nwk/selectNtwrkList.cm")
	public String selectNtwrkList(@ModelAttribute("ntwrkVO") NtwrkVO ntwrkVO,
			                       ModelMap model) throws Exception {

    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(ntwrkVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(ntwrkVO.getPageUnit());
		paginationInfo.setPageSize(ntwrkVO.getPageSize());

		ntwrkVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		ntwrkVO.setLastIndex(paginationInfo.getLastRecordIndex());
		ntwrkVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		if(ntwrkVO.getStrManageIem() == null) ntwrkVO.setStrManageIem("00");

		ntwrkVO.setNtwrkList(egovNtwrkService.selectNtwrkList(ntwrkVO));

		model.addAttribute("ntwrkList", ntwrkVO.getNtwrkList());

        int totCnt = egovNtwrkService.selectNtwrkListTotCnt(ntwrkVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM067"));
        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		return "coreframework/com/sym/sym/nwk/EgovNtwrkList";
	}

	/**
	 * 등록된 네트워크의 상세정보를 조회한다.
	 * @param ntwrkVO - 네트워크 Vo
	 * @return String - 리턴 Url
	 *
	 * @param ntwrkVO
	 */
	@RequestMapping(value="/sym/sym/nwk/getNtwrk.cm")
	public String selectNtwrk(@RequestParam("ntwrkId") String ntwrkId,
			                  @ModelAttribute("ntwrkVO") NtwrkVO ntwrkVO,
			                   Model model) throws Exception {

		ntwrkVO.setNtwrkId(ntwrkId);
		model.addAttribute("ntwrk", egovNtwrkService.selectNtwrk(ntwrkVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		return "coreframework/com/sym/sym/nwk/EgovNtwrkDetail";
	}

	/**
	 * 네트워크정보 등록 화면으로 이동한다.
	 * @param ntwrkVO - 네트워크 Vo
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/sym/nwk/addViewNtwrk.cm")
	public String insertViewNtwrk(@ModelAttribute("ntwrkVO") NtwrkVO ntwrkVO,
			                        ModelMap model) throws Exception {

    	model.addAttribute("ntwrk", ntwrkVO);
    	model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM067"));
    	return "coreframework/com/sym/sym/nwk/EgovNtwrkRegist";
	}

	/**
	 * 네트워크정보를 신규로 등록한다.
	 * @param ntwrk - 네트워크 model
	 * @return String - 리턴 Url
	 *
	 * @param ntwrk
	 */
	@RequestMapping(value="/sym/sym/nwk/addNtwrk.cm")
	public String insertNtwrk(@ModelAttribute("ntwrkVO") NtwrkVO ntwrkVO,
			                  @ModelAttribute("ntwrk") Ntwrk ntwrk,
			                   BindingResult bindingResult,
		                       ModelMap model) throws Exception {

		beanValidator.validate(ntwrk, bindingResult); //validation 수행

		if (bindingResult.hasErrors()) {
    		model.addAttribute("ntwrkVO", ntwrkVO);
			return "coreframework/com/sym/sym/nwk/EgovNtwrkRegist";
		} else {
	   	    LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	   	    ntwrk.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
	   	    ntwrk.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
	   	    ntwrk.setNtwrkId(egovNtwrkIdGnrService.getNextStringId());
	   	    model.addAttribute("ntwrk", egovNtwrkService.insertNtwrk(ntwrk, ntwrkVO));
			model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));
			return "coreframework/com/sym/sym/nwk/EgovNtwrkDetail";
		}

	}

	/**
	 * 네트워크정보 수정 화면으로 이동한다.
	 * @param ntwrkVO - 네트워크 Vo
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/sym/nwk/updtViewNtwrk.cm")
	public String updateViewNtwrk(@RequestParam("ntwrkId") String ntwrkId,
			                      @ModelAttribute("ntwrkVO") NtwrkVO ntwrkVO,
			                       ModelMap model) throws Exception {

		ntwrkVO.setNtwrkId(ntwrkId);
		model.addAttribute("ntwrk", egovNtwrkService.selectNtwrk(ntwrkVO));
		model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM067"));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));
    	return "coreframework/com/sym/sym/nwk/EgovNtwrkUpdt";
	}

	/**
	 * 기 등록된 네트워크정보를 수정한다.
	 * @param ntwrk - 네트워크 model
	 * @return String - 리턴 Url
	 *
	 * @param ntwrk
	 */
	@RequestMapping(value="/sym/sym/nwk/updtNtwrk.cm")
	public String updateNtwrk(@ModelAttribute("ntwrk") Ntwrk ntwrk,
                               BindingResult bindingResult,
                               SessionStatus status,
                               ModelMap model) throws Exception {

		beanValidator.validate(ntwrk, bindingResult); //validation 수행

		if (bindingResult.hasErrors()) {
    		model.addAttribute("ntwrkVO", ntwrk);
			return "coreframework/com/sym/sym/nwk/EgovNtwrkUpdt";
		} else {
	   	    LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	   	    ntwrk.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
	   	    egovNtwrkService.updateNtwrk(ntwrk);
	   	    status.setComplete();
			model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
			return "forward:/sym/sym/nwk/getNtwrk.cm";
		}
	}

	/**
	 * 기 등록된 네트워크정보를 삭제한다.
	 * @param ntwrk - 네트워크 model
	 * @return String - 리턴 Url
	 *
	 * @param ntwrk
	 */
	@RequestMapping(value="/sym/sym/nwk/removeNtwrk.cm")
	public String deleteNtwrk(@RequestParam("ntwrkId") String ntwrkId,
			                  @ModelAttribute("ntwrk") Ntwrk ntwrk,
			                   ModelMap model) throws Exception {
		ntwrk.setNtwrkId(ntwrkId);
		egovNtwrkService.deleteNtwrk(ntwrk);
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/sym/sym/nwk/selectNtwrkList.cm";
	}

    /**
	 * 공통코드 호출
	 * @param comDefaultCodeVO ComDefaultCodeVO
	 * @param codeId String
	 * @return List
	 * @exception Exception
	 */
    public List<?> getCmmCodeDetailList(ComDefaultCodeVO comDefaultCodeVO, String codeId)  throws Exception {
    	comDefaultCodeVO.setCodeId(codeId);
    	return CoreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
    }
}