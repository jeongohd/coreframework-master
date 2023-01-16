package coreframework.com.uss.umt.api;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uss.umt.service.CoreMberManageService;
import coreframework.com.uss.umt.vo.MberManageVO;
import coreframework.com.uss.umt.vo.UserDefaultVO;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import coreframework.com.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 일반회원관련 요청을  비지니스 클래스로 전달하고 처리된결과를  해당   웹 화면으로 전달하는  Controller를 정의한다
 * @author 공통서비스 개발팀 조재영
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  조재영         최초 생성
 *   2011.08.26  정진오         IncludedInfo annotation 추가
 *   2014.12.08  이기하         암호화방식 변경(CoreFileScrty.encryptPassword)
 *   2015.06.16  조정국         수정시 유효성체크 후 에러발생 시 목록으로 이동하여 에러메시지 표시
 *   2015.06.19  조정국         미인증 사용자에 대한 보안처리 기준 수정 (!isAuthenticated)
 *   2017.07.21  장동한         로그인인증제한 작업
 *   2021.05.30  정진오         디지털원패스 정보 조회
 *
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CoreMberManageApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreMberManageApi.class);

	@Resource(name = "coreMessageSource")
	CoreMessageSource coreMessageSource;

	/** mberManageService */
	@Resource(name = "CoreMberManageService")
	private CoreMberManageService coreMberManageService;

	/** cmmUseService */
	@Resource(name = "CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 일반회원목록을 조회한다. (pageing)
	 * @param userSearchVO 검색조건정보
	 * @param model 화면모델
	 * @return uss/umt/CoreMberManage
	 * @throws Exception
	 */
	@IncludedInfo(name = "일반회원관리", order = 470, gid = 50)
	@RequestMapping(value = "/uss/umt/CoreMberManageApi.cm")
	public ApiResult<Object> selectMberListApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO, ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		/** CorePropertyService */
		userSearchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		userSearchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userSearchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(userSearchVO.getPageUnit());
		paginationInfo.setPageSize(userSearchVO.getPageSize());

		userSearchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userSearchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		userSearchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> mberList = coreMberManageService.selectMberList(userSearchVO);
		model.addAttribute("resultList", mberList);

		int totCnt = coreMberManageService.selectMberListTotCnt(userSearchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		//일반회원 상태코드를 코드정보로부터 조회
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM013");
		List<?> mberSttus_result = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("entrprsMberSttus_result", mberSttus_result);//기업회원상태코드목록

		try {
			return ApiResult.OK(mberList, paginationInfo, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreMberManage";
	}

	/**
	 * 일반회원등록화면으로 이동한다.
	 * @param userSearchVO 검색조건정보
	 * @param mberManageVO 일반회원초기화정보
	 * @param model 화면모델
	 * @return uss/umt/CoreMberInsert
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberInsertViewApi.cm")
	public ApiResult<Object> insertMberViewApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO,
								 @ModelAttribute("mberManageVO") MberManageVO mberManageVO, Model model)
			throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		//패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
		//성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//사용자상태코드를 코드정보로부터 조회
		vo.setCodeId("COM013");
		List<?> mberSttus_result = cmmUseService.selectCmmCodeDetail(vo);
		//그룹정보를 조회 - GROUP_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);

		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		model.addAttribute("mberSttus_result", mberSttus_result); //사용자상태코드목록
		model.addAttribute("groupId_result", groupId_result); //그룹정보 목록

		try {
			return ApiResult.Data(vo,  null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreMberInsert";
	}

	/**
	 * 일반회원등록처리후 목록화면으로 이동한다.
	 * @param mberManageVO 일반회원등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreMberManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberInsertApi.cm")
	public ApiResult<Object> insertMberApi(@ModelAttribute("mberManageVO") MberManageVO mberManageVO, ComUrlVO comUrlVO,
							 BindingResult bindingResult, Model model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		beanValidator.validate(mberManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			
			ComDefaultCodeVO vo = new ComDefaultCodeVO();

			//패스워드힌트목록을 코드정보로부터 조회
			vo.setCodeId("COM022");
			List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
			//성별구분코드를 코드정보로부터 조회
			vo.setCodeId("COM014");
			List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);
			//사용자상태코드를 코드정보로부터 조회
			vo.setCodeId("COM013");
			List<?> mberSttus_result = cmmUseService.selectCmmCodeDetail(vo);
			//그룹정보를 조회 - GROUP_ID정보
			vo.setTableNm("COMTNORGNZTINFO");
			List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);
			
			model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
			model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
			model.addAttribute("mberSttus_result", mberSttus_result); //사용자상태코드목록
			model.addAttribute("groupId_result", groupId_result); //그룹정보 목록

			try {
				return ApiResult.Data(vo,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "coreframework/com/uss/umt/CoreMberInsert";
		} else {
			if ("".equals(mberManageVO.getGroupId())) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
				mberManageVO.setGroupId(null);
			}
			coreMberManageService.insertMber(mberManageVO);
			//Exception 없이 진행시 등록 성공메시지
			model.addAttribute("resultMsg", "success.common.insert");

			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
		//return "forward:/uss/umt/CoreMberManageApi.cm";
	}

	/**
	 * 일반회원정보 수정을 위해 일반회원정보를 상세조회한다.
	 * @param mberId 상세조회대상 일반회원아이디
	 * @param userSearchVO 검색조건
	 * @param model 화면모델
	 * @return uss/umt/CoreMberSelectUpdt
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberSelectUpdtViewApi.cm")
	public ApiResult<Object> updateMberViewApi(@RequestParam("selectedId") String mberId, @ModelAttribute("searchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO,
								 HttpServletRequest request, Model model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		//패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);

		//성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);

		//사용자상태코드를 코드정보로부터 조회
		vo.setCodeId("COM013");
		List<?> mberSttus_result = cmmUseService.selectCmmCodeDetail(vo);

		//그룹정보를 조회 - GROUP_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);

		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		model.addAttribute("mberSttus_result", mberSttus_result); //사용자상태코드목록
		model.addAttribute("groupId_result", groupId_result); //그룹정보 목록

		MberManageVO mberManageVO = coreMberManageService.selectMber(mberId);
		model.addAttribute("mberManageVO", mberManageVO);
		model.addAttribute("userSearchVO", userSearchVO);

		// 2021.05.30, 정진오, 디지털원패스 정보 조회
		LoginVO loginVO = (LoginVO)request.getSession().getAttribute("loginVO");
		String onepassUserId = loginVO.getUniqId();
		String onepassUserkey = loginVO.getOnepassUserkey();
		String onepassIntfToken = loginVO.getOnepassIntfToken();
		if (mberId.equals(onepassUserId)) {
			model.addAttribute("onepassUserkey", onepassUserkey); //디지털원패스 사용자키
			model.addAttribute("onepassIntfToken", onepassIntfToken); //디지털원패스 사용자세션값
		} else {
			model.addAttribute("onepassUserkey", "");
			model.addAttribute("onepassIntfToken", "");
		}
		try {
			return ApiResult.Data(mberManageVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreMberSelectUpdt";
	}
	
	/**
	 * 로그인인증제한 해제 
	 * @param mberManageVO 일반회원등록정보
	 * @param model 화면모델
	 * @return uss/umt/CoreMberSelectUpdtViewApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberLockIncorrectApi.cm")
	public ApiResult<Object> updateLockIncorrect(MberManageVO mberManageVO, ComUrlVO comUrlVO, Model model) throws Exception {

	    
	    // 미인증 사용자에 대한 보안처리
	    Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
	    if (!isAuthenticated) {
	        try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
	    }
	    
	    coreMberManageService.updateLockIncorrect(mberManageVO);
		try {
			comUrlVO.setFwdUrl("/uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm");
			return ApiResult.Data(comUrlVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

	    //return "forward:/uss/umt/CoreMberSelectUpdtViewApi.cm";
	}

	/**
	 * 일반회원정보 수정후 목록조회 화면으로 이동한다.
	 * @param mberManageVO 일반회원수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreMberManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberSelectUpdtApi.cm")
	public ApiResult<Object> updateMberApi(@ModelAttribute("mberManageVO") MberManageVO mberManageVO, ComUrlVO comUrlVO, BindingResult bindingResult, Model model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		beanValidator.validate(mberManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("resultMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			try {
				return ApiResult.Message(bindingResult.getAllErrors().get(0).getDefaultMessage());
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "forward:/uss/umt/CoreMberManageApi.cm";
		} else {
			if ("".equals(mberManageVO.getGroupId())) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
				mberManageVO.setGroupId(null);
			}
			coreMberManageService.updateMber(mberManageVO);
			//Exception 없이 진행시 수정성공메시지
			model.addAttribute("resultMsg", "success.common.update");
			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.update"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "forward:/uss/umt/CoreMberManageApi.cm";
		}
	}

	/**
	 * 일반회원정보삭제후 목록조회 화면으로 이동한다.
	 * @param checkedIdForDel 삭제대상 아이디 정보
	 * @param userSearchVO 검색조건정보
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreMberManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberDeleteApi.cm")
	public ApiResult<Object> deleteMberApi(@RequestParam("checkedIdForDel") String checkedIdForDel, @ModelAttribute("searchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO,
							 HttpServletRequest request, Model model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		// 2021.05.30, 정진오, 디지털원패스 정보 조회
		LoginVO loginVO = (LoginVO)request.getSession().getAttribute("loginVO");
		String onepassUserkey = loginVO.getOnepassUserkey();
		String onepassIntfToken = loginVO.getOnepassIntfToken();
		if (onepassUserkey.isEmpty() && onepassIntfToken.isEmpty()) {
			coreMberManageService.deleteMber(checkedIdForDel);
			model.addAttribute("resultMsg", "success.common.delete");

			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

		} else {
			model.addAttribute("resultMsg", "digital.onepass.delete.alert");

			try {
				return ApiResult.Message(coreMessageSource.getMessage("digital.onepass.delete.alert"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}

		//return "forward:/uss/umt/CoreMberManageApi.cm";
	}

	// 탈퇴 처리 기능에 대한 예시
	@RequestMapping("/uss/umt/CoreMberWithdrawApi.cm")
	public ApiResult<Object> withdrawMber(Model model, ComUrlVO comUrlVO) throws Exception {
		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();

		String returnPage = "/"; // 탈퇴 처리 후 화면 지정

		if (!isAuthenticated) {
			model.addAttribute("resultMsg", "fail.common.delete");

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.delete"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "redirect:" + returnPage;
		}

		coreMberManageService.deleteMber(user == null ? "" : EgovStringUtil.isNullToString(user.getUniqId()));
		//Exception 없이 진행시 삭제성공메시지
		model.addAttribute("resultMsg", "success.common.delete");
		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "redirect:" + returnPage;
	}

	/**
	 * 일반회원가입신청 등록화면으로 이동한다.
	 * @param userSearchVO 검색조건
	 * @param mberManageVO 일반회원가입신청정보
	 * @param commandMap 파라메터전달용 commandMap
	 * @param model 화면모델
	 * @return uss/umt/CoreMberSbscrb
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberSbscrbViewApi.cm")
	public ApiResult<Object> sbscrbMberViewApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, @ModelAttribute("mberManageVO") MberManageVO mberManageVO,
			@RequestParam Map<String, Object> commandMap, Model model) throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		//패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
		//성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);

		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		if (!"".equals(commandMap.get("realname"))) {
			model.addAttribute("mberNm", commandMap.get("realname")); //실명인증된 이름 - 주민번호 인증
			model.addAttribute("ihidnum", commandMap.get("ihidnum")); //실명인증된 주민등록번호 - 주민번호 인증
		}
		if (!"".equals(commandMap.get("realName"))) {
			model.addAttribute("mberNm", commandMap.get("realName")); //실명인증된 이름 - ipin인증
		}

		mberManageVO.setMberSttus("DEFAULT");

		try {
			return ApiResult.Data(mberManageVO,  null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreMberSbscrb";
	}

	/**
	 * 일반회원가입신청등록처리후로그인화면으로 이동한다.
	 * @param mberManageVO 일반회원가입신청정보
	 * @return forward:/uat/uia/coreLoginUsrApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreMberSbscrbApi.cm")
	public ApiResult<Object> sbscrbMberApi(@ModelAttribute("mberManageVO") MberManageVO mberManageVO, ComUrlVO comUrlVO) throws Exception {

		//가입상태 초기화
		mberManageVO.setMberSttus("A");
		//그룹정보 초기화
		//mberManageVO.setGroupId("1");
		//일반회원가입신청 등록시 일반회원등록기능을 사용하여 등록한다.
		coreMberManageService.insertMber(mberManageVO);
		comUrlVO.setFwdUrl("/uat/uia/coreLoginUsr.cm");
		try {
			return ApiResult.Data(comUrlVO, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}


		//return "forward:/uat/uia/coreLoginUsr.cm";
	}

	/**
	 * 일반회원 약관확인
	 * @param model 화면모델
	 * @return uss/umt/CoreStplatCnfirm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreStplatCnfirmMberApi.cm")
	public ApiResult<Object> sbscrbEntrprsMberApi(Model model, ComUrlVO comUrlVO) throws Exception {

		// 미인증 사용자에 대한 보안처리
		//Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		//if (!isAuthenticated) {
		//	try {

			//return "index";
		//}

		//일반회원용 약관 아이디 설정
		String stplatId = "STPLAT_0000000000001";
		//회원가입유형 설정-일반회원
		String sbscrbTy = "USR01";
		//약관정보 조회
		List<?> stplatList = coreMberManageService.selectStplat(stplatId);
		model.addAttribute("stplatList", stplatList); //약관정보 포함
		model.addAttribute("sbscrbTy", sbscrbTy); //회원가입유형 포함

		try {
			return ApiResult.OK(stplatList, sbscrbTy, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreStplatCnfirm";
	}

	/**
	 * @param model 화면모델
	 * @param commandMap 파라메터전달용 commandMap
	 * @param userSearchVO 검색조건
	 * @param mberManageVO 일반회원수정정보(비밀번호)
	 * @return uss/umt/CoreMberPasswordUpdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreMberPasswordUpdt.cm")
	public ApiResult<Object> updatePassword(ModelMap model, @RequestParam Map<String, Object> commandMap, ComUrlVO comUrlVO, @ModelAttribute("searchVO") UserDefaultVO userSearchVO,
			@ModelAttribute("mberManageVO") MberManageVO mberManageVO) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		String oldPassword = (String) commandMap.get("oldPassword");
		String newPassword = (String) commandMap.get("newPassword");
		String newPassword2 = (String) commandMap.get("newPassword2");
		String uniqId = (String) commandMap.get("uniqId");

		boolean isCorrectPassword = false;
		MberManageVO resultVO = new MberManageVO();
		mberManageVO.setPassword(newPassword);
		mberManageVO.setOldPassword(oldPassword);
		mberManageVO.setUniqId(uniqId);

		String resultMsg = "";
		resultVO = coreMberManageService.selectPassword(mberManageVO);
		//패스워드 암호화
		String encryptPass = EgovFileScrty.encryptPassword(oldPassword, mberManageVO.getMberId());
		if (encryptPass.equals(resultVO.getPassword())) {
			if (newPassword.equals(newPassword2)) {
				isCorrectPassword = true;
			} else {
				isCorrectPassword = false;
				resultMsg = "fail.user.passwordUpdate2";
			}
		} else {
			isCorrectPassword = false;
			resultMsg = "fail.user.passwordUpdate1";
		}

		if (isCorrectPassword) {
			mberManageVO.setPassword(EgovFileScrty.encryptPassword(newPassword, mberManageVO.getMberId()));
			coreMberManageService.updatePassword(mberManageVO);
			model.addAttribute("mberManageVO", mberManageVO);
			resultMsg = "success.common.update";
		} else {
			model.addAttribute("mberManageVO", mberManageVO);
		}
		model.addAttribute("userSearchVO", userSearchVO);
		model.addAttribute("resultMsg", resultMsg);

		try {
			return ApiResult.Data(mberManageVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreMberPasswordUpdt";
	}

	/**
	 * 일반회원 암호 수정 화면 이동
	 * @param model 화면모델
	 * @param commandMap 파라메터전달용 commandMap
	 * @param userSearchVO 검색조건
	 * @param mberManageVO 일반회원수정정보(비밀번호)
	 * @return uss/umt/CoreMberPasswordUpdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreMberPasswordUpdtView.cm")
	public ApiResult<Object> updatePasswordView(ModelMap model, @RequestParam Map<String, Object> commandMap, ComUrlVO comUrlVO, @ModelAttribute("searchVO") UserDefaultVO userSearchVO,
			@ModelAttribute("mberManageVO") MberManageVO mberManageVO) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
				comUrlVO.setSelfUrl("index");
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "index";
		}

		String userTyForPassword = (String) commandMap.get("userTyForPassword");
		mberManageVO.setUserTy(userTyForPassword);

		model.addAttribute("userSearchVO", userSearchVO);
		model.addAttribute("mberManageVO", mberManageVO);

		try {
			return ApiResult.Data(mberManageVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreMberPasswordUpdt";
	}

}