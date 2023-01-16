package coreframework.com.sym.tbm.tbr.web;
import java.util.List;

import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.tbm.tbr.service.EgovTroblReqstService;
import coreframework.com.sym.tbm.tbr.service.TroblReqst;
import coreframework.com.sym.tbm.tbr.service.TroblReqstVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

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

/**
 * 개요
 * -장애신청정보에 대한 controller 클래스를 정의한다.
 *
 * 상세내용
 * - 장애신청정보에 대한 등록, 수정, 삭제, 조회 등의 기능을 제공한다.
 * - 장애신청정보의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author lee.m.j
 * @version 1.0
 * @created 28-6-2010 오전 10:44:35
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
public class EgovTroblReqstController {




	@Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

	@Resource(name = "egovTroblReqstService")
	private EgovTroblReqstService egovTroblReqstService;

    /** ID Generation */
    @Resource(name="egovTroblIdGnrService")
    private EgovIdGnrService egovTroblIdGnrService;

    @Autowired
	private DefaultBeanValidator beanValidator;

    @Resource(name = "CoreCmmUseService")
	CoreCmmUseService CoreCmmUseService;

	/**
	 * 장애요청관리 목록화면으로 이동
	 * @return String
	 */
    @RequestMapping(value="/sym/tbm/tbr/selectTroblReqstListView.cm")
	public String selectTroblReqstListView() throws Exception {
		return "coreframework/com/sym/tbm/tbr/EgovTroblReqstList";
	}

	/**
	 * 장애요청을 관리하기 위해 등록된 장애요청목록을 조회한다.
	 * @param troblReqstVO - 장애신청관리 Vo
	 * @return String - 리턴 Url
	 */
    @IncludedInfo(name="장애신청관리", order = 1180 ,gid = 60)
    @RequestMapping(value="/sym/tbm/tbr/selectTroblReqstList.cm")
	public String selectTroblReqstList(@ModelAttribute("troblReqstVO") TroblReqstVO troblReqstVO,
                                        ModelMap model) throws Exception {

		/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(troblReqstVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(troblReqstVO.getPageUnit());
		paginationInfo.setPageSize(troblReqstVO.getPageSize());

		troblReqstVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		troblReqstVO.setLastIndex(paginationInfo.getLastRecordIndex());
		troblReqstVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		if(troblReqstVO.getStrTroblKnd() == null) troblReqstVO.setStrTroblKnd("00");
		if(troblReqstVO.getStrProcessSttus() == null) troblReqstVO.setStrProcessSttus("00");

		troblReqstVO.setTroblReqstList(egovTroblReqstService.selectTroblReqstList(troblReqstVO));

		model.addAttribute("troblReqstList", troblReqstVO.getTroblReqstList());

        int totCnt = egovTroblReqstService.selectTroblReqstListTotCnt(troblReqstVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("cmmCodeDetailList1", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM065"));
        model.addAttribute("cmmCodeDetailList2", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM068"));
        model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		return "coreframework/com/sym/tbm/tbr/EgovTroblReqstList";
	}

	/**
	 * 등록된 장애요청의 상세정보를 조회한다.
	 * @param troblReqstVO - 장애신청관리 Vo
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/getTroblReqst.cm")
	public String selectTroblReqst(@RequestParam("troblId") String troblId,
			                       @ModelAttribute("troblReqstVO") TroblReqstVO troblReqstVO,
					                Model model) throws Exception {

    	troblReqstVO.setTroblId(troblId);
		model.addAttribute("troblReqst", egovTroblReqstService.selectTroblReqst(troblReqstVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		return "coreframework/com/sym/tbm/tbr/EgovTroblReqstDetail";
	}

	/**
	 * 장애요청정보  등록 화면으로 이동한다.
	 * @param troblReqstVO - 장애신청관리 Vo
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/addViewTroblReqst.cm")
	public String insertViewTroblReqst(@ModelAttribute("troblReqstVO") TroblReqstVO troblReqstVO,
                                        ModelMap model) throws Exception {

    	model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM065"));
		model.addAttribute("troblReqst", troblReqstVO);
		return "coreframework/com/sym/tbm/tbr/EgovTroblReqstRegist";
	}

	/**
	 * 장애요청정보를 신규로 등록한다.
	 * @param troblReqst - 장애신청관리 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/addTroblReqst.cm")
	public String insertTroblReqst(@ModelAttribute("troblReqstVO") TroblReqstVO troblReqstVO,
			                       @ModelAttribute("troblReqst") TroblReqst troblReqst,
				                    BindingResult bindingResult,
			                        ModelMap model) throws Exception {

		beanValidator.validate(troblReqst, bindingResult); //validation 수행

		if (bindingResult.hasErrors()) {
    		model.addAttribute("troblReqstVO", troblReqstVO);
			return "coreframework/com/sym/tbm/tbr/EgovTroblReqstRegist";
		} else {
	   	    LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	   	    troblReqst.setTroblOccrrncTime(EgovStringUtil.removeMinusChar(troblReqst.getTroblOccrrncTime()));
	   	    troblReqst.setTroblRequstTime(EgovStringUtil.removeMinusChar(troblReqst.getTroblRequstTime()));
	   	    troblReqst.setFrstRegisterId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
	   	    troblReqst.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
	   	    troblReqst.setProcessSttus("A");
	   	    troblReqst.setTroblId(egovTroblIdGnrService.getNextStringId());

	   	    model.addAttribute("troblReqst", egovTroblReqstService.insertTroblReqst(troblReqst, troblReqstVO));
			model.addAttribute("message", coreMessageSource.getMessage("success.common.insert"));
			return "coreframework/com/sym/tbm/tbr/EgovTroblReqstDetail";
		}
	}

	/**
	 * 장애요청정보 수정 화면으로 이동한다.
	 * @param troblReqstVO - 장애신청관리 Vo
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/updtViewTroblReqst.cm")
    public String updateViewTroblReqst(@RequestParam("troblId") String troblId,
    		                           @ModelAttribute("troblReqstVO") TroblReqstVO troblReqstVO,
                                        Model model) throws Exception {

    	troblReqstVO.setTroblId(troblId);
    	model.addAttribute("troblReqst", egovTroblReqstService.selectTroblReqst(troblReqstVO));
    	model.addAttribute("cmmCodeDetailList", getCmmCodeDetailList(new ComDefaultCodeVO(),"COM065"));
    	model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));
		return "coreframework/com/sym/tbm/tbr/EgovTroblReqstUpdt";
	}

	/**
	 * 기 등록된 장애요청정보를 수정한다.
	 * @param troblReqst - 장애신청관리 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/updtTroblReqst.cm")
	public String updateTroblReqst(@ModelAttribute("troblReqst") TroblReqst troblReqst,
			                        BindingResult bindingResult,
                                    SessionStatus status,
                                    ModelMap model) throws Exception {

    	beanValidator.validate(troblReqst, bindingResult); //validation 수행

    	if (bindingResult.hasErrors()) {
    		model.addAttribute("troblReqstVO", troblReqst);
    		return "coreframework/com/sym/tbm/EgovTroblReqstUpdt";
    	} else {
    		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
	   	    troblReqst.setTroblOccrrncTime(EgovStringUtil.removeMinusChar(troblReqst.getTroblOccrrncTime()));
	   	    troblReqst.setTroblRequstTime(EgovStringUtil.removeMinusChar(troblReqst.getTroblRequstTime()));
    		troblReqst.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
    		egovTroblReqstService.updateTroblReqst(troblReqst);
	   	    status.setComplete();
			model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
			return "forward:/sym/tbm/tbr/getTroblReqst.cm";
    	}
	}

	/**
	 * 기 등록된 장애요청정보를 삭제한다.
	 * @param troblReqst - 장애신청관리 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/removeTroblReqst.cm")
	public String deleteTroblReqst(@RequestParam("troblId") String troblId,
			                       @ModelAttribute("troblReqst") TroblReqst troblReqst,
	                                ModelMap model) throws Exception {

    	troblReqst.setTroblId(troblId);
    	egovTroblReqstService.deleteTroblReqst(troblReqst);
		model.addAttribute("message", coreMessageSource.getMessage("success.common.delete"));
		return "forward:/sym/tbm/tbr/selectTroblReqstList.cm";
	}

	/**
	 * 장애처리를 요청한다.
	 * @param troblReqst - 장애신청관리 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/requstTroblReqst.cm")
	public String requstTroblReqst(@RequestParam("troblId") String troblId,
		                           @ModelAttribute("troblReqst") TroblReqst troblReqst,
			                        SessionStatus status,
	                                ModelMap model) throws Exception {

    	troblReqst.setTroblId(troblId);
    	troblReqst.setProcessSttus("R");
		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		troblReqst.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
		egovTroblReqstService.requstTroblReqst(troblReqst);
   	    status.setComplete();
		model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
		return "forward:/sym/tbm/tbr/getTroblReqst.cm";
	}

	/**
	 * 장애처리취소를 요청한다.
	 * @param troblReqst - 장애신청관리 model
	 * @return String - 리턴 Url
	 */
    @RequestMapping(value="/sym/tbm/tbr/requstTroblReqstCancl.cm")
	public String requstTroblReqstCancl(@RequestParam("troblId") String troblId,
                                        @ModelAttribute("troblReqst") TroblReqst troblReqst,
			                             SessionStatus status,
	                                     ModelMap model) throws Exception {

    	troblReqst.setTroblId(troblId);
    	troblReqst.setProcessSttus("A");
		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		troblReqst.setLastUpdusrId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
		egovTroblReqstService.requstTroblReqst(troblReqst);
   	    status.setComplete();
		model.addAttribute("message", coreMessageSource.getMessage("success.common.update"));
		return "forward:/sym/tbm/tbr/getTroblReqst.cm";
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