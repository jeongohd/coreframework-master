package coreframework.com.utl.sys.prm.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import coreframework.com.utl.sys.prm.service.EgovProcessMonService;
import coreframework.com.utl.sys.prm.service.ProcessMon;
import coreframework.com.utl.sys.prm.service.ProcessMonChecker;
import coreframework.com.utl.sys.prm.service.ProcessMonLogVO;
import coreframework.com.utl.sys.prm.service.ProcessMonVO;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * 개요
 * - PROCESS모니터링에 대한 controller 클래스를 정의한다.
 *
 * 상세내용
 * - PROCESS모니터링에 대한 등록, 수정, 삭제, 조회 기능을 제공한다.
 * - PROCESS모니터링에 조회기능은 목록조회, 상세조회로 구분된다.
 * @author 박종선
 * @version 1.0
 * @created 08-9-2010 오후 3:54:45
 *  <pre>
 * == 개정이력(Modification Information) ==
 *
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *  2010.9.8   박종선     최초 생성
 *  2011.8.26	정진오			IncludedInfo annotation 추가
 * </pre>
 */

@Controller
public class EgovProcessMonController {

	@Resource(name="EgovProcessMonService")
    protected EgovProcessMonService processMonService;

	@Resource(name="propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name="coreMessageSource")
    CoreMessageSource coreMessageSource;

    @Autowired
    private DefaultBeanValidator beanValidator;

	/**
	 * 등록된 PROCESS모니터링 정보를 조회 한다.
	 * @param processMonVO- PROCESS모니터링 VO
	 * @return String - 리턴 Url
	 *
	 * @param processMonVO
	 */
    @SuppressWarnings("unused")
	@IncludedInfo(name="프로세스모니터링", order = 2110 ,gid = 90)
    @RequestMapping("/utl/sys/prm/EgovComUtlProcessMonList.cm")
	public String selectProcessMonList(
			@ModelAttribute("searchVO") ProcessMonVO processMonVO
			, ModelMap model
			) throws Exception {

    	//로그인 객체 선언
		LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		processMonVO.setPageUnit(propertyService.getInt("pageUnit"));
		processMonVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(processMonVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(processMonVO.getPageUnit());
		paginationInfo.setPageSize(processMonVO.getPageSize());

		processMonVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		processMonVO.setLastIndex(paginationInfo.getLastRecordIndex());
		processMonVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

        List<?> ProcessMonList = processMonService.selectProcessMonList(processMonVO);
        model.addAttribute("resultList", ProcessMonList);

        int totCnt = processMonService.selectProcessMonTotCnt(processMonVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonList";
	}

	/**
	 * PROCESS모니터링상세 정보를 조회 한다.
	 * @param ProcessMonVO - PROCESS모니터링 VO
	 * @return String - 리턴 Url
	 *
	 * @param processMonVO
	 */
    @RequestMapping("/utl/sys/prm/EgovComUtlProcessMon.cm")
	public String selectProcessMon(
			@ModelAttribute("processMonVO") ProcessMonVO processMonVO
			, ModelMap model
			) throws Exception {

    	ProcessMon vo = processMonService.selectProcessMon(processMonVO);
    	model.addAttribute("result", vo);
    	//model.addAttribute("processNm", ProcessMonChecker.getProcessId(vo.getProcessNm()));

		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonDetail";
	}

	/**
	 * PROCESS모니터링 정보를 신규로 등록한다.
	 * @param processNm - PROCESS모니터링 model
	 * @return String - 리턴 Url
	 *
	 * @param processNm
	 */
    @RequestMapping(value="/utl/sys/prm/EgovComUtlProcessMonRegist.cm")
	public String insertProcessMon(
			@ModelAttribute("processMonVO") ProcessMonVO processMonVO
			, BindingResult bindingResult
			, ModelMap model
			) throws Exception {

    	// 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
        	return "coreframework/com/uat/uia/CoreLoginUsr";
    	}

		//로그인 객체 선언
		LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

    	if (processMonVO.getProcessNm() == null
            ||processMonVO.getProcessNm().equals("")) {
        	return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonRegist";
        }

		//서버  validate 체크
        beanValidator.validate(processMonVO, bindingResult);
		if(bindingResult.hasErrors()){
    		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonRegist";
		}

		//아이디 설정
		processMonVO.setFrstRegisterId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));
		processMonVO.setLastUpdusrId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));

		processMonService.insertProcessMon(processMonVO);
        return "forward:/utl/sys/prm/EgovComUtlProcessMonList.cm";

	}

	/**
	 * 기 등록 된 PROCESS모니터링 정보를 수정 한다.
	 * @param processNm - PROCESS모니터링 model
	 * @return String - 리턴 Url
	 *
	 * @param processNm
	 */
    @RequestMapping(value="/utl/sys/prm/EgovComUtlProcessMonModify.cm")
	public String updateProcessMon(
			@ModelAttribute("processMonVO") ProcessMonVO processMonVO
			, BindingResult bindingResult
			, @RequestParam Map<?, ?> commandMap
			, ModelMap model
			) throws Exception {

    	//로그인 객체 선언
    	LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
    	
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();	//KISA 보안취약점 조치 (2018-12-10, 이정은)

        if(!isAuthenticated) {
            return "coreframework/com/uat/uia/CoreLoginUsr";
        }

    	String sCmd = commandMap.get("cmd") == null ? "": (String)commandMap.get("cmd");
    	if (sCmd.equals("")) {
    		ProcessMonVO vo = processMonService.selectProcessMon(processMonVO);
    		model.addAttribute("processMonVO", vo);
    		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonModify";
    	} else if (sCmd.equals("Modify")) {
    		beanValidator.validate(processMonVO, bindingResult);
    		if (bindingResult.hasErrors()){
    			ProcessMonVO vo = processMonService.selectProcessMon(processMonVO);
    			model.addAttribute("processMonVO", vo);
    			return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonModify";
    		}

        	// 아이디 설정
    		processMonVO.setFrstRegisterId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));
    		processMonVO.setLastUpdusrId(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId()));

    		processMonService.updateProcessMon(processMonVO);
    		return "forward:/utl/sys/prm/EgovComUtlProcessMonList.cm";
    	} else {
    		return "forward:/utl/sys/prm/EgovComUtlProcessMonList.cm";
    	}
    }

	/**
	 * 기 등록된 PROCESS모니터링 정보를 삭제한다.
	 * @param processNm - PROCESS모니터링 model
	 * @return String - 리턴 Url
	 *
	 * @param processNm
	 */
    @RequestMapping(value="/utl/sys/prm/EgovComUtlProcessMonRemove.cm")
	public String deleteProcessMon(
			@ModelAttribute("processMonVO") ProcessMonVO processMonVO
			, ModelMap model
			) throws Exception {

    	// 0. Spring Security 사용자권한 처리
    	Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
    	if(!isAuthenticated) {
    		model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
        	return "coreframework/com/uat/uia/CoreLoginUsr";
    	}

    	processMonService.deleteProcessMon(processMonVO);
		return "forward:/utl/sys/prm/EgovComUtlProcessMonList.cm";
	}

	/**
	 * 프로세스의 상태를 조회한다.
	 * @param processMon
	 * @return  String
	 *
	 * @param processSttus
	 */
    @RequestMapping("/utl/sys/prm/selectProcessSttus.cm")
	public String selectProcessSttus(
			@ModelAttribute("processMonVO") ProcessMonVO processMonVO
			, ModelMap model
			) throws Exception {

		//System.out.println("FileSysNm" + fileSysMntrngVO.getFileSysNm());
    	//KISA 보안약점 조치 (2018-10-29, 윤창원)
    	model.addAttribute("processSttus", ProcessMonChecker.getProcessId(EgovStringUtil.isNullToString(processMonVO.getProcessNm())));
    	model.addAttribute("processMonVO", processMonVO);


		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonRegist";
	}

    /**
	 * 프로세스 모니터링로그 정보에 대한 목록을 조회한다.
	 * @param ProcessMonVO - 프로세스 모니터링로그 VO
	 * @return  String - 리턴 URL
	 *
	 * @param processMonVO
	 */
    @SuppressWarnings("unused")
	@RequestMapping("/utl/sys/prm/EgovComUtlProcessMonLogList.cm")
	public String selectProcessMonLogList(
			@ModelAttribute("searchVO") ProcessMonLogVO processMonLogVO
			, ModelMap model
			) throws Exception {

		//로그인 객체 선언
		LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

		processMonLogVO.setPageUnit(propertyService.getInt("pageUnit"));
		processMonLogVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(processMonLogVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(processMonLogVO.getPageUnit());
		paginationInfo.setPageSize(processMonLogVO.getPageSize());

		processMonLogVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		processMonLogVO.setLastIndex(paginationInfo.getLastRecordIndex());
		processMonLogVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		// 조회기간설정
		if(processMonLogVO.getSearchBgnDe() != null && processMonLogVO.getSearchEndDe() != null){
			if(!processMonLogVO.getSearchBgnDe().equals("") && !processMonLogVO.getSearchEndDe().equals("")){
				processMonLogVO.setSearchBgnDt(processMonLogVO.getSearchBgnDe() + " " + processMonLogVO.getSearchBgnHour());
				processMonLogVO.setSearchEndDt(processMonLogVO.getSearchEndDe() + " " + processMonLogVO.getSearchEndHour());
			}
		}

		Map<String, Object> map = processMonService.selectProcessMonLogList(processMonLogVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
		paginationInfo.setTotalRecordCount(totCnt);

		// 조회시작시
    	model.addAttribute("searchBgnHour", getTimeHH());
    	// 조회종료시
    	model.addAttribute("searchEndHour", getTimeHH());

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);

		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonLogList";
	}

	/**
	 * 프로세스 모니터링로그 상세정보를 조회한다.
	 * @param FileSysMntrngLogVO - 프로세스 모니터링로그 VO
	 * @return  String - 리턴 URL
	 *
	 * @param fileSysMntrngLogVO
	 */
    @RequestMapping("/utl/sys/prm/EgovComUtlProcessMonLog.cm")
	public String selectProcessMonLog(
			@ModelAttribute("processMonLogVO") ProcessMonLogVO processMonLogVO
			, ModelMap model
			) throws Exception {

		ProcessMonLogVO vo = processMonService.selectProcessMonLog(processMonLogVO);

		if(vo.getCreatDt() != null && !vo.getCreatDt().equals("")){
			if(vo.getCreatDt().length() > 18){
				vo.setCreatDt(vo.getCreatDt().substring(0, 19));
			}
		}

    	model.addAttribute("result", vo);

		return "coreframework/com/utl/sys/prm/EgovComUtlProcessMonLogDetail";
	}

	/**
	 * 시간의 LIST를 반환한다.
	 * @return  List
	 * @throws
	 */
	private List<ComDefaultCodeVO> getTimeHH (){
    	ArrayList<ComDefaultCodeVO> listHH = new ArrayList<ComDefaultCodeVO>();
    	//HashMap hmHHMM;
    	for(int i=0;i < 24; i++){
    		String sHH = "";
    		String strI = String.valueOf(i);
    		if(i<10){
    			sHH = "0" + strI;
    		}else{
    			sHH = strI;
    		}

    		ComDefaultCodeVO codeVO = new ComDefaultCodeVO();
    		codeVO.setCode(sHH);
    		codeVO.setCodeNm(sHH + ":00");

    		listHH.add(codeVO);
    	}

    	return listHH;
	}

}