package coreframework.com.uss.umt.api;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uss.umt.service.CoreUserManageService;
import coreframework.com.uss.umt.vo.UserDefaultVO;
import coreframework.com.uss.umt.vo.UserManageVO;
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
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 업무사용자관련 요청을  비지니스 클래스로 전달하고 처리된결과를  해당   웹 화면으로 전달하는  Controller를 정의한다
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
 *   2009.04.10  조재영          최초 생성
 *   2011.08.26	 정진오			IncludedInfo annotation 추가
 *   2014.12.08	 이기하			암호화방식 변경(CoreFileScrty.encryptPassword)
 *   2015.06.16	 조정국			수정시 유효성체크 후 에러발생 시 목록으로 이동하여 에러메시지 표시
 *   2015.06.19	 조정국			미인증 사용자에 대한 보안처리 기준 수정 (!isAuthenticated)
 *   2017.07.21  장동한 			로그인인증제한 작업
 *
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CoreUserManageApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreUserManageApi.class);

	@Resource(name = "coreMessageSource")
	CoreMessageSource coreMessageSource;

	/** userManageService */
	@Resource(name = "CoreUserManageService")
	private CoreUserManageService coreUserManageService;

	/** cmmUseService */
	@Resource(name = "CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	/** CorePropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 사용자목록을 조회한다. (pageing)
	 * @param userSearchVO 검색조건정보
	 * @param model 화면모델
	 * @return cmm/uss/umt/CoreUserManage
	 * @throws Exception
	 */
	@IncludedInfo(name = "업무사용자관리", order = 460, gid = 50)
	@RequestMapping(value = "/uss/umt/CoreUserManageApi.cm")
	public ApiResult<Object> selectUserListApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO,  ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			comUrlVO.setSelfUrl("index");
			try {
				return ApiResult.Data(comUrlVO, null);
			} catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
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

		List<?> userList = coreUserManageService.selectUserList(userSearchVO);
		model.addAttribute("resultList", userList);

		int totCnt = coreUserManageService.selectUserListTotCnt(userSearchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		//사용자상태코드를 코드정보로부터 조회
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM013");
		List<?> emplyrSttusCode_result = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("emplyrSttusCode_result", emplyrSttusCode_result);//사용자상태코드목록

		try {
			return ApiResult.OK(userList, paginationInfo, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreUserManage";
	}

	/**
	 * 사용자등록화면으로 이동한다.
	 * @param userSearchVO 검색조건정보
	 * @param userManageVO 사용자초기화정보
	 * @param model 화면모델
	 * @return cmm/uss/umt/CoreUserInsert
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreUserInsertViewApi.cm")
	public ApiResult<Object> insertUserViewApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO,
											ComUrlVO comUrlVO,
											@ModelAttribute("userManageVO") UserManageVO userManageVO, Model model)
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
		List<?> emplyrSttusCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//소속기관코드를 코드정보로부터 조회 - COM025
		vo.setCodeId("COM025");
		List<?> insttCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//조직정보를 조회 - ORGNZT_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<?> orgnztId_result = cmmUseService.selectOgrnztIdDetail(vo);
		//그룹정보를 조회 - GROUP_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);

		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		model.addAttribute("emplyrSttusCode_result", emplyrSttusCode_result);//사용자상태코드목록
		model.addAttribute("insttCode_result", insttCode_result); //소속기관코드목록
		model.addAttribute("orgnztId_result", orgnztId_result); //조직정보 목록
		model.addAttribute("groupId_result", groupId_result); //그룹정보 목록

		try {
			return ApiResult.Data(vo,  null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreUserInsert";
	}

	/**
	 * 사용자등록처리후 목록화면으로 이동한다.
	 * @param userManageVO 사용자등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreUserManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreUserInsertApi.cm")
	public ApiResult<Object> insertUserApi(@ModelAttribute("userManageVO") UserManageVO userManageVO,
										ComUrlVO comUrlVO,
										BindingResult bindingResult, Model model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			comUrlVO.setSelfUrl("index");
			try {
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}

		beanValidator.validate(userManageVO, bindingResult);
		if (bindingResult.hasErrors()) {

			comUrlVO.setFwdUrl("coreframework/com/uss/umt/CoreUserInsert");
			try {
				return ApiResult.Data(comUrlVO,  null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "coreframework/com/uss/umt/CoreUserInsert";

		} else {
			if ("".equals(userManageVO.getOrgnztId())) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
				userManageVO.setOrgnztId(null);
			}
			if ("".equals(userManageVO.getGroupId())) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
				userManageVO.setGroupId(null);
			}
			coreUserManageService.insertUser(userManageVO);
			//Exception 없이 진행시 등록성공메시지
			model.addAttribute("resultMsg", "success.common.insert");
			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

		}
		//return "forward:/uss/umt/CoreUserManageApi.cm";
	}

	/**
	 * 사용자정보 수정을 위해 사용자정보를 상세조회한다.
	 * @param uniqId 상세조회대상 사용자아이디
	 * @param userSearchVO 검색조건
	 * @param model 화면모델
	 * @return uss/umt/CoreUserSelectUpdt
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreUserSelectUpdtViewApi.cm")
	public ApiResult<Object> updateUserViewApi(@RequestParam("selectedId") String uniqId,
											ComUrlVO comUrlVO,
											@ModelAttribute("searchVO") UserDefaultVO userSearchVO, Model model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			comUrlVO.setSelfUrl("index");
			try {
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
		List<?> emplyrSttusCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//소속기관코드를 코드정보로부터 조회 - COM025
		vo.setCodeId("COM025");
		List<?> insttCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//조직정보를 조회 - ORGNZT_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<?> orgnztId_result = cmmUseService.selectOgrnztIdDetail(vo);
		//그룹정보를 조회 - GROUP_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);

		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		model.addAttribute("emplyrSttusCode_result", emplyrSttusCode_result);//사용자상태코드목록
		model.addAttribute("insttCode_result", insttCode_result); //소속기관코드목록
		model.addAttribute("orgnztId_result", orgnztId_result); //조직정보 목록
		model.addAttribute("groupId_result", groupId_result); //그룹정보 목록

		UserManageVO userManageVO = new UserManageVO();
		userManageVO = coreUserManageService.selectUser(uniqId);
		model.addAttribute("userSearchVO", userSearchVO);
		model.addAttribute("userManageVO", userManageVO);

		try {
			return ApiResult.Data(userManageVO,  null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreUserSelectUpdt";
	}
	
	/**
	 * 로그인인증제한 해제 
	 * @param userManageVO 사용자정보
	 * @param model 화면모델
	 * @return uss/umt/CoreUserSelectUpdtViewApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreUserLockIncorrectApi.cm")
	public ApiResult<Object> updateLockIncorrect(UserManageVO userManageVO, ComUrlVO comUrlVO, Model model)
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
		
		coreUserManageService.updateLockIncorrect(userManageVO);

		try {
			comUrlVO.setFwdUrl("/uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm");
			return ApiResult.Data(comUrlVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/uss/umt/CoreUserSelectUpdtViewApi.cm";
	}

	/**
	 * 사용자정보 수정후 목록조회 화면으로 이동한다.
	 * @param userManageVO 사용자수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreUserManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreUserSelectUpdtApi.cm")
	public ApiResult<Object> updateUserApi(@ModelAttribute("userManageVO") UserManageVO userManageVO, ComUrlVO comUrlVO,
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

		beanValidator.validate(userManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("resultMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			try {
				return ApiResult.Message(bindingResult.getAllErrors().get(0).getDefaultMessage());
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "forward:/uss/umt/CoreUserManageApi.cm";
		} else {
			//업무사용자 수정시 히스토리 정보를 등록한다.
			coreUserManageService.insertUserHistory(userManageVO);
			if ("".equals(userManageVO.getOrgnztId())) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
				userManageVO.setOrgnztId(null);
			}
			if ("".equals(userManageVO.getGroupId())) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
				userManageVO.setGroupId(null);
			}
			coreUserManageService.updateUser(userManageVO);
			//Exception 없이 진행시 수정성공메시지
			model.addAttribute("resultMsg", "success.common.update");

			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.update"));
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "forward:/uss/umt/CoreUserManageApi.cm";
		}
	}

	/**
	 * 사용자정보삭제후 목록조회 화면으로 이동한다.
	 * @param checkedIdForDel 삭제대상아이디 정보
	 * @param userSearchVO 검색조건
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreUserManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreUserDeleteApi.cm")
	public ApiResult<Object> deleteUserApi(@RequestParam("checkedIdForDel") String checkedIdForDel, ComUrlVO comUrlVO,
										@ModelAttribute("searchVO") UserDefaultVO userSearchVO, Model model) throws Exception {

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

		coreUserManageService.deleteUser(checkedIdForDel);
		//Exception 없이 진행시 등록성공메시지
		model.addAttribute("resultMsg", "success.common.delete");

		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/uss/umt/CoreUserManageApi.cm";
	}

	/**
	 * 입력한 사용자아이디의 중복확인화면 이동
	 * @param model 화면모델
	 * @return uss/umt/CoreIdDplctCnfirm
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreIdDplctCnfirmViewApi.cm")
	public ApiResult<Object> checkIdDplct(ModelMap model, ComUrlVO comUrlVO) throws Exception {

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

		model.addAttribute("checkId", "");
		model.addAttribute("usedCnt", "-1");

		try {
			return ApiResult.Data(comUrlVO, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreIdDplctCnfirm";
	}

	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param commandMap 파라메터전달용 commandMap
	 * @param model 화면모델
	 * @return uss/umt/CoreIdDplctCnfirm
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreIdDplctCnfirmApi.cm")
	public ApiResult<Object> checkIdDplctApi(@RequestParam Map<String, Object> commandMap, ComUrlVO comUrlVO,
										  ModelMap model) throws Exception {

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

		String checkId = (String) commandMap.get("checkId");
		checkId = new String(checkId.getBytes("ISO-8859-1"), "UTF-8");

		if (checkId == null || checkId.equals("")) {
			//return "forward:/uss/umt/coreIdDplctCnfirm.cm";
			comUrlVO.setFwdUrl("/uss/umt/coreIdDplctCnfirm.cm");
			try {
				return ApiResult.Data(comUrlVO, null);
			} catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
		int usedCnt = coreUserManageService.checkIdDplct(checkId);
		model.addAttribute("usedCnt", usedCnt);
		model.addAttribute("checkId", checkId);

		try {
			return ApiResult.Data(comUrlVO, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreIdDplctCnfirm";
	}
	
	
	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param commandMap 파라메터전달용 commandMap
	 * @param model 화면모델
	 * @return uss/umt/CoreIdDplctCnfirm
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreIdDplctCnfirmAjaxApi.cm")
	public ApiResult<Object> checkIdDplctAjaxApi(@RequestParam Map<String, Object> commandMap) throws Exception {

    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("jsonView");

		String checkId = (String) commandMap.get("checkId");
		//checkId = new String(checkId.getBytes("ISO-8859-1"), "UTF-8");

		int usedCnt = coreUserManageService.checkIdDplct(checkId);
		modelAndView.addObject("usedCnt", usedCnt);
		modelAndView.addObject("checkId", checkId);
		try {
			return ApiResult.Data(usedCnt, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return modelAndView;
	}

	/**
	 * 업무사용자 암호 수정처리 후 화면 이동
	 * @param model 화면모델
	 * @param commandMap 파라메터전달용 commandMap
	 * @param userSearchVO 검색조 건
	 * @param userManageVO 사용자수정정보(비밀번호)
	 * @return uss/umt/CoreUserPasswordUpdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreUserPasswordUpdtApi.cm")
	public ApiResult<Object> updatePasswordApi(ModelMap model, @RequestParam Map<String, Object> commandMap, ComUrlVO comUrlVO,
											@ModelAttribute("searchVO") UserDefaultVO userSearchVO,
			@ModelAttribute("userManageVO") UserManageVO userManageVO) throws Exception {

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
		UserManageVO resultVO = new UserManageVO();
		userManageVO.setPassword(newPassword);
		userManageVO.setOldPassword(oldPassword);
		userManageVO.setUniqId(uniqId);

		String resultMsg = "";
		resultVO = coreUserManageService.selectPassword(userManageVO);
		//패스워드 암호화
		String encryptPass = EgovFileScrty.encryptPassword(oldPassword, userManageVO.getEmplyrId());
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
			userManageVO.setPassword(EgovFileScrty.encryptPassword(newPassword, userManageVO.getEmplyrId()));
			coreUserManageService.updatePassword(userManageVO);
			model.addAttribute("userManageVO", userManageVO);
			resultMsg = "success.common.update";
		} else {
			model.addAttribute("userManageVO", userManageVO);
		}
		model.addAttribute("userSearchVO", userSearchVO);
		model.addAttribute("resultMsg", resultMsg);

		try {
			return ApiResult.Data(userManageVO, coreMessageSource.getMessage(resultMsg));
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreUserPasswordUpdt";
	}

	/**
	 * 업무사용자 암호 수정  화면 이동
	 * @param model 화면모델
	 * @param commandMap 파라메터전달용 commandMap
	 * @param userSearchVO 검색조건
	 * @param userManageVO 사용자수정정보(비밀번호)
	 * @return uss/umt/CoreUserPasswordUpdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreUserPasswordUpdtViewApi.cm")
	public ApiResult<Object> updatePasswordViewApi(ModelMap model, @RequestParam Map<String, Object> commandMap,
												ComUrlVO comUrlVO,
												@ModelAttribute("searchVO") UserDefaultVO userSearchVO,
			@ModelAttribute("userManageVO") UserManageVO userManageVO) throws Exception {

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
		userManageVO.setUserTy(userTyForPassword);

		model.addAttribute("userManageVO", userManageVO);
		model.addAttribute("userSearchVO", userSearchVO);
		try {
			return ApiResult.Data(userManageVO, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreUserPasswordUpdt";
	}

}
