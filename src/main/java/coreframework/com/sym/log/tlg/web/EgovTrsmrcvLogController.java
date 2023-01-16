package coreframework.com.sym.log.tlg.web;

import java.util.HashMap;
import java.util.List;

import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.sym.log.tlg.service.EgovTrsmrcvLogService;
import coreframework.com.sym.log.tlg.service.TrsmrcvLog;
import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

/**
 * @Class Name : EgovTrsmrcvLogController.java
 * @Description : 송수신 로그정보를 관리하기 위한 컨트롤러 클래스
 * @Modification Information
 *
 *    수정일         수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.   이삼섭         최초생성
 *    2011. 7. 01.   이기하         패키지 분리(sym.log -> sym.log.tlg)
 *    2011.8.26	정진오			IncludedInfo annotation 추가
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */

@Controller
public class EgovTrsmrcvLogController {

	@Resource(name = "EgovTrsmrcvLogService")
	private EgovTrsmrcvLogService trsmrcvLogService;

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	@Resource(name = "CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	/**
	 * 송수신 로그 목록 조회
	 *
	 * @param trsmrcvLog
	 * @return sym/log/tlg/EgovTrsmrcvLogList
	 * @throws Exception
	 */
	@IncludedInfo(name = "송/수신로그관리", listUrl = "/sym/log/tlg/SelectTrsmrcvLogList.cm", order = 1050, gid = 60)
	@RequestMapping(value = "/sym/log/tlg/SelectTrsmrcvLogList.cm")
	public String selectTrsmrcvLogInf(@ModelAttribute("searchVO") TrsmrcvLog trsmrcvLog, ModelMap model) throws Exception {

		trsmrcvLog.setPageUnit(propertyService.getInt("pageUnit"));
		trsmrcvLog.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(trsmrcvLog.getPageIndex());
		paginationInfo.setRecordCountPerPage(trsmrcvLog.getPageUnit());
		paginationInfo.setPageSize(trsmrcvLog.getPageSize());

		trsmrcvLog.setFirstIndex(paginationInfo.getFirstRecordIndex());
		trsmrcvLog.setLastIndex(paginationInfo.getLastRecordIndex());
		trsmrcvLog.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		HashMap<?, ?> _map = (HashMap<?, ?>) trsmrcvLogService.selectTrsmrcvLogInf(trsmrcvLog);
		int totCnt = Integer.parseInt((String) _map.get("resultCnt"));

		model.addAttribute("resultList", _map.get("resultList"));
		model.addAttribute("resultCnt", _map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/sym/log/tlg/EgovTrsmrcvLogList";
	}

	/**
	 * 송수신 로그 상세 조회
	 *
	 * @param trsmrcvLog
	 * @param model
	 * @return sym/log/tlg/EgovTrsmrcvLogInqire
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/log/tlg/InqireTrsmrcvLog.cm")
	public String selectTrsmrcvLog(@ModelAttribute("searchVO") TrsmrcvLog trsmrcvLog, @RequestParam("requstId") String requstId, ModelMap model) throws Exception {

		trsmrcvLog.setRequstId(requstId.trim());

		TrsmrcvLog vo = trsmrcvLogService.selectTrsmrcvLog(trsmrcvLog);
		model.addAttribute("result", vo);
		return "coreframework/com/sym/log/tlg/EgovTrsmrcvLogInqire";
	}

	/**
	 * 송수신 로그 테스트 화면
	 *
	 * @param trsmrcvLog
	 * @return sym/log/slg/EgovSysHistRegist
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/log/tlg/AddTrsmrcvLog.cm")
	public String addTrsmrcvLog(@ModelAttribute("searchVO") TrsmrcvLog trsmrcvLog, ModelMap model) throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM002");
		List<?> _result = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("resultList", _result);
		return "coreframework/com/sym/log/tlg/EgovTrsmrcvLogRegist";
	}

	/**
	 * 송수신 로그 테스트
	 *
	 * @param trsmrcvLog
	 * @return forward:/sym/log/tlg/SelectTrsmrcvLogList.cm
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/log/tlg/InsertTrsmrcvLog.cm")
	public String insertTrsmrcvLog(@ModelAttribute("searchVO") TrsmrcvLog trsmrcvLog, SessionStatus status) throws Exception {

		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

		if (isAuthenticated) {
			LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
			trsmrcvLog.setRqesterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
			trsmrcvLogService.logInsertTrsmrcvLog(trsmrcvLog);
		}

		return "forward:/sym/log/tlg/SelectTrsmrcvLogList.cm";
	}

}
