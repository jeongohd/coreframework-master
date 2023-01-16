/**
 * 개요
 * - 보고서통계에 대한 controller 클래스를 정의한다.
 * 
 * 상세내용
 * - 보고서통계에 대한 등록, 조회 기능을 제공한다.
 * - 보고서통계의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author lee.m.j
 * @version 1.0
 * @created 03-8-2009 오후 2:09:15
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.8.3   lee.m.j          최초 생성 *  
 *  2011.8.26	정진오			IncludedInfo annotation 추가
 *
 *  </pre>
 */

package coreframework.com.sts.rst.web;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sts.rst.service.EgovReprtStatsService;
import coreframework.com.sts.rst.service.ReprtStats;
import coreframework.com.sts.rst.service.ReprtStatsVO;
import coreframework.com.utl.fcc.service.EgovDateUtil;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;


@Controller
public class EgovReprtStatsController {
	
	 
	 

	@Resource(name="coreMessageSource")
	CoreMessageSource coreMessageSource;

    @Resource(name = "CoreCmmUseService")
	CoreCmmUseService coreCmmUseService;

	@Resource(name = "egovReprtStatsService")
    EgovReprtStatsService egovReprtStatsService;

    /** Message ID Generation */
    @Resource(name="egovReprtStatsIdGnrService")    
    private EgovIdGnrService egovReprtStatsIdGnrService;
	
    @Autowired
	private DefaultBeanValidator beanValidator;    
    
    /**
	 * 보고서 통계 목록화면 이동
	 * @return String
	 * @exception Exception
	 */
    @RequestMapping("/sts/rst/selectReprtStatsListView.cm")
    public String selectReprtStatsListView(@ModelAttribute("comDefaultCodeVO") ComDefaultCodeVO comDefaultCodeVO,
    		                               @ModelAttribute("pmReprtStats") ReprtStatsVO reprtStatsVO,
    		                                ModelMap model) throws Exception {

    	comDefaultCodeVO.setCodeId("COM040");
    	model.addAttribute("cmmCode040List", coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO));
    	comDefaultCodeVO.setCodeId("COM042");
    	model.addAttribute("cmmCode042List", coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO));
    	
    	reprtStatsVO.setPmFromDate(EgovDateUtil.addMonth(EgovDateUtil.getToday(), -1));
    	reprtStatsVO.setPmToDate(EgovDateUtil.getToday());
    	model.addAttribute("pmReprtStats", reprtStatsVO);

        return "coreframework/com/sts/rst/EgovReprtStatsList";
    }

	/**
	 * 보고서 통계정보의 대상목록을 조회한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return String - 리턴 Url
	 */
    @IncludedInfo(name="보고서통계", listUrl="/sts/rst/selectReprtStatsListView.cm", order = 160 ,gid = 30)
	@RequestMapping("/sts/rst/selectReprtStatsList.cm")
	public String selectReprtStatsList(@RequestParam("pmReprtTy") String pmReprtTy,
			                           @RequestParam("pmDateTy") String pmDateTy,
			                           @RequestParam("pmFromDate") String pmFromDate, 
			                           @RequestParam("pmToDate") String pmToDate, 
			                           @ModelAttribute("comDefaultCodeVO") ComDefaultCodeVO comDefaultCodeVO,
			                           @ModelAttribute("reprtStatsVO") ReprtStatsVO reprtStatsVO,
			                            ModelMap model) throws Exception {
		
		/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(reprtStatsVO.getPageIndex());
	 // paginationInfo.setRecordCountPerPage(reprtStatsVO.getPageUnit());
	    paginationInfo.setRecordCountPerPage(5);
		paginationInfo.setPageSize(reprtStatsVO.getPageSize());

		reprtStatsVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		reprtStatsVO.setLastIndex(paginationInfo.getLastRecordIndex());
		reprtStatsVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
				
		reprtStatsVO.setPmReprtTy(pmReprtTy);
		reprtStatsVO.setPmDateTy(pmDateTy);
		reprtStatsVO.setPmFromDate(pmFromDate);
		reprtStatsVO.setPmToDate(pmToDate);

		reprtStatsVO.setReprtStatsList(egovReprtStatsService.selectReprtStatsList(reprtStatsVO));
		model.addAttribute("reprtStatsList", reprtStatsVO.getReprtStatsList());

		int totPageCnt = egovReprtStatsService.selectReprtStatsListTotCnt(reprtStatsVO);
		paginationInfo.setTotalRecordCount(totPageCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		
		int totCnt = egovReprtStatsService.selectReprtStatsListBarTotCnt(reprtStatsVO);

		if (totCnt > 10 && totCnt <= 100) {
			if (reprtStatsVO.getMaxUnit() > 5.0f) {
				reprtStatsVO.setMaxUnit(5.0f);
			}
		} else if (totCnt > 100 && totCnt <= 1000) {
			if (reprtStatsVO.getMaxUnit() > 0.5f) {
				reprtStatsVO.setMaxUnit(0.5f);
			}
		} else if (reprtStatsVO.getMaxUnit() > 1000) {
			if (reprtStatsVO.getMaxUnit() > 0.05f) {
				reprtStatsVO.setMaxUnit(0.05f);
			}
		}

		reprtStatsVO.setReprtStatsBarList(egovReprtStatsService.selectReprtStatsBarList(reprtStatsVO));
		model.addAttribute("reprtStatsBarList", reprtStatsVO.getReprtStatsBarList());

		reprtStatsVO.setReprtStatsByReprtTyList(egovReprtStatsService.selectReprtStatsByReprtTyList(reprtStatsVO));
		model.addAttribute("reprtStatsByReprtTyList", reprtStatsVO.getReprtStatsByReprtTyList());
		
		reprtStatsVO.setReprtStatsByReprtSttusList(egovReprtStatsService.selectReprtStatsByReprtSttusList(reprtStatsVO));
		model.addAttribute("reprtStatsByReprtSttusList", reprtStatsVO.getReprtStatsByReprtSttusList());
		
    	comDefaultCodeVO.setCodeId("COM040");
    	model.addAttribute("cmmCode040List", coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO));
    	comDefaultCodeVO.setCodeId("COM042");
    	model.addAttribute("cmmCode042List", coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO));

		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));

		return "coreframework/com/sts/rst/EgovReprtStatsList";
	}

	/**
	 * 보고서 통계정보의 상세정보를 조회한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/sts/rst/getReprtStats.cm")
	public String selectReprtStats(@ModelAttribute("reprtStatsVO") ReprtStatsVO reprtStatsVO,
			                       @RequestParam("reprtTy") String reprtTy, 
			                       @RequestParam("reprtSttus") String reprtSttus, 
			                        ModelMap model) throws Exception {
		
		reprtStatsVO.setReprtTy(reprtTy);
		reprtStatsVO.setReprtSttus(reprtSttus);

		model.addAttribute("reprtStats", egovReprtStatsService.selectReprtStats(reprtStatsVO));
		model.addAttribute("message", coreMessageSource.getMessage("success.common.select"));
		
		return "coreframework/com/sts/rst/EgovReprtStatsDetail";
	}

	/**
	 * 보고서 통계정보의 등록화면으로 이동한다.
	 * @param reprtStats - 보고서통계 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/sts/rst/addViewReprtStats.cm")
	public String insertViewReprtStats(@ModelAttribute("reprtStatsVO") ReprtStatsVO reprtStatsVO,
			                           @ModelAttribute("comDefaultCodeVO") ComDefaultCodeVO comDefaultCodeVO, 
			                            ModelMap model) throws Exception {
		
    	comDefaultCodeVO.setCodeId("COM036");
    	model.addAttribute("cmmCode036List", coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO));
    	comDefaultCodeVO.setCodeId("COM040");
    	model.addAttribute("cmmCode040List", coreCmmUseService.selectCmmCodeDetail(comDefaultCodeVO));
		
		return "coreframework/com/sts/rst/EgovReprtStatsRegis";
	}

	/**
	 * 보고서 통계정보를 등록한다.
	 * @param reprtStats - 보고서통계 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/sts/rst/addReprtStats.cm")	
	public String insertReprtStats(@ModelAttribute("reprtStats") ReprtStats reprtStats,
			                       @ModelAttribute("reprtStatsVO") ReprtStatsVO reprtStatsVO,
			                        BindingResult bindingResult,
			                        ModelMap model)
	                                throws Exception {
		
		beanValidator.validate(reprtStats, bindingResult); //validation 수행
		
    	if (bindingResult.hasErrors()) { 
    		model.addAttribute("reprtStats", reprtStatsVO);
			return "forward:/sts/rst/addViewReprtStats.cm";
		} else {
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

			reprtStats.setReprtId(egovReprtStatsIdGnrService.getNextStringId());
			reprtStats.setUserId(user == null ? "" : EgovStringUtil.isNullToString(user.getId()));
			
			egovReprtStatsService.insertReprtStats(reprtStats);
			
			reprtStatsVO.setReprtId(reprtStats.getReprtId());
			reprtStatsVO.setReprtTy(reprtStats.getReprtTy());
			reprtStatsVO.setReprtSttus(reprtStats.getReprtSttus());
					
			return "forward:/sts/rst/getReprtStats.cm";			
		}
	}

}
