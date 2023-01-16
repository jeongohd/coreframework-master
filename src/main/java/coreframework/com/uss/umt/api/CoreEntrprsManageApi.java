package coreframework.com.uss.umt.api;

import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.ComUrlVO;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uss.umt.service.CoreEntrprsManageService;
import coreframework.com.uss.umt.vo.EntrprsManageVO;
import coreframework.com.uss.umt.vo.UserDefaultVO;
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
 * 기업회원관련 요청을  비지니스 클래스로 전달하고 처리된결과를  해당   웹 화면으로 전달하는  Controller를 정의한다
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
 *   2011.08.26	 정진오         IncludedInfo annotation 추가
 *   2014.12.08  이기하         암호화방식 변경(CoreFileScrty.encryptPassword)
 *   2015.06.16  조정국         수정시 유효성체크 후 에러발생 시 목록으로 이동하여 에러메시지 표시
 *   2015.06.19  조정국         미인증 사용자에 대한 보안처리 기준 수정 (!isAuthenticated)
 *   2017.07.21  장동한         로그인인증제한 작업
 *   2020.07.18  윤주호         암호 설정 규칙 강화 및 버그 수정
 *   2021.05.30  정진오         디지털원패스 정보 조회
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class CoreEntrprsManageApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreEntrprsManageApi.class);

	@Resource(name = "coreMessageSource")
	CoreMessageSource coreMessageSource;

	/**
	 * entrprsManageService
	 */
	@Resource(name = "CoreEntrprsManageService")
	private CoreEntrprsManageService coreEntrprsManageService;

	/**
	 * cmmUseService
	 */
	@Resource(name = "CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	/**
	 * CorePropertyService
	 */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/**
	 * DefaultBeanValidator beanValidator
	 */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * 비밀번호 힌트 조회 목록
	 */
	@ModelAttribute("passwordHint_result")
	private List<?> getPasswordHintResult(ComDefaultCodeVO vo) throws Exception {
		vo.setCodeId("COM022");
		return cmmUseService.selectCmmCodeDetail(vo);
	}

	/**
	 * 성별 조회 목록
	 */
	@ModelAttribute("sexdstnCode_result")
	private List<?> getSexdstnCode_result(ComDefaultCodeVO vo) throws Exception {
		vo.setCodeId("COM014");
		return cmmUseService.selectCmmCodeDetail(vo);
	}

	/**
	 * 사용자 상태 조회 목록
	 */
	@ModelAttribute("entrprsMberSttus_result")
	private List<?> getEntrprsMberSttus_result(ComDefaultCodeVO vo) throws Exception {
		vo.setCodeId("COM013");
		return cmmUseService.selectCmmCodeDetail(vo);
	}

	/**
	 * 그룹 정보 조회 목록
	 */
	@ModelAttribute("groupId_result")
	private List<?> getGroupId_result(ComDefaultCodeVO vo) throws Exception {
		vo.setTableNm("COMTNORGNZTINFO");
		return cmmUseService.selectGroupIdDetail(vo);
	}

	/**
	 * 기업 구분 조회 목록
	 */
	@ModelAttribute("entrprsSeCode_result")
	private List<?> getEntrprsSeCode_result(ComDefaultCodeVO vo) throws Exception {
		vo.setCodeId("COM026");
		return cmmUseService.selectCmmCodeDetail(vo);
	}

	/**
	 * 업종 구분 조회 목록
	 */
	@ModelAttribute("indutyCode_result")
	private List<?> getIndutyCode_result(ComDefaultCodeVO vo) throws Exception {
		vo.setCodeId("COM027");
		return cmmUseService.selectCmmCodeDetail(vo);
	}

	/**
	 * 기업회원 등록화면으로 이동한다.
	 *
	 * @param userSearchVO    검색조건정보
	 * @param entrprsManageVO 기업회원 초기화정보
	 * @param model           화면모델
	 * @return uss/umt/CoreEntrprsMberInsert
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberInsertViewApi.cm")
	public ApiResult<Object> insertEntrprsMberViewApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO,
												   ComUrlVO comUrlVO,
												   @ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO, Model model)
			throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			//return "index";
			comUrlVO.setSelfUrl("index");
			try {
				return ApiResult.Data(comUrlVO, null);
			} catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}

		//		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		//패스워드힌트목록을 코드정보로부터 조회
		//		vo.setCodeId("COM022");
		//		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
		//성별구분코드를 코드정보로부터 조회
		//		vo.setCodeId("COM014");
		//		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//사용자상태코드를 코드정보로부터 조회
		//		vo.setCodeId("COM013");
		//		List<?> entrprsMberSttus_result = cmmUseService.selectCmmCodeDetail(vo);
		//그룹정보를 조회 - GROUP_ID정보
		//		vo.setTableNm("COMTNORGNZTINFO");
		//		List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);
		//기업구분코드를 코드정보로부터 조회 - COM026
		//		vo.setCodeId("COM026");
		//		List<?> entrprsSeCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//업종코드를 코드정보로부터 조회 - COM027
		//		vo.setCodeId("COM027");
		//		List<?> indutyCode_result = cmmUseService.selectCmmCodeDetail(vo);

		//		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		//		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		//		model.addAttribute("entrprsMberSttus_result", entrprsMberSttus_result);//사용자상태코드목록
		//		model.addAttribute("groupId_result", groupId_result); //그룹정보 목록
		//		model.addAttribute("entrprsSeCode_result", entrprsSeCode_result); //기업구분코드 목록
		//		model.addAttribute("indutyCode_result", indutyCode_result); //업종코드목록

		comUrlVO.setSelfUrl("coreframework/com/uss/umt/CoreEntrprsMberInsert");
		try {
			return ApiResult.Data(comUrlVO, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreEntrprsMberInsert";
	}

	/**
	 * 기업회원등록처리후 목록화면으로 이동한다.
	 *
	 * @param entrprsManageVO 신규기업회원정보
	 * @param bindingResult   입력값검증용  bindingResult
	 * @param model           화면모델
	 * @return forward:/uss/umt/CoreEntrprsMberManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberInsertApi.cm")
	public ApiResult<Object> insertEntrprsMberApi(@ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO,
											   ComUrlVO comUrlVO,
											   BindingResult bindingResult, Model model) throws Exception {

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
			//return "index";
		}

		beanValidator.validate(entrprsManageVO, bindingResult);

		if (bindingResult.hasErrors()) {
			//return "coreframework/com/uss/umt/CoreEntrprsMberInsert";
			comUrlVO.setSelfUrl("coreframework/com/uss/umt/CoreEntrprsMberInsert");
			try {
				return ApiResult.Data(comUrlVO, null);
			} catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		} else {
			if (entrprsManageVO.getGroupId() != null && entrprsManageVO.getGroupId().equals("")) {//2022.01 Null pointers should not be dereferenced
				entrprsManageVO.setGroupId(null);
			}
			coreEntrprsManageService.insertEntrprsmber(entrprsManageVO);
			//Exception 없이 진행시 등록성공메시지
			model.addAttribute("resultMsg", "success.common.insert");

			try {
				return ApiResult.Message(coreMessageSource.getMessage("success.common.insert"));
			} catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}


		//return "forward:/uss/umt/CoreEntrprsMberManageApi.cm";

	}

	/**
	 * 기업회원정보 수정을 위해기업회원정보를 상세조회한다.
	 *
	 * @param entrprsmberId 상세조회 대상 기업회원아이디
	 * @param userSearchVO  조회조건정보
	 * @param model         화면모델
	 * @return uss/umt/CoreEntrprsMberSelectUpdt
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm")
	public ApiResult<Object> updateEntrprsMberViewApi(@RequestParam("selectedId") String entrprsmberId,
												   @ModelAttribute("searchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO, HttpServletRequest request, Model model)
			throws Exception {

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

			//return "index";
		}

		EntrprsManageVO entrprsManageVO = new EntrprsManageVO();
		entrprsManageVO = coreEntrprsManageService.selectEntrprsmber(entrprsmberId);
		model.addAttribute("entrprsManageVO", entrprsManageVO);
		model.addAttribute("userSearchVO", userSearchVO);

		//		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		//패스워드힌트목록을 코드정보로부터 조회
		//		vo.setCodeId("COM022");
		//		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//성별구분코드를 코드정보로부터 조회
		//		vo.setCodeId("COM014");
		//		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//사용자상태코드를 코드정보로부터 조회
		//		vo.setCodeId("COM013");
		//		List<?> entrprsMberSttus_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//그룹정보를 조회 - GROUP_ID정보
		//		vo.setTableNm("COMTNORGNZTINFO");
		//		List<?> groupId_result = cmmUseService.selectGroupIdDetail(vo);
		//		//기업구분코드를 코드정보로부터 조회 - COM026
		//		vo.setCodeId("COM026");
		//		List<?> entrprsSeCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//업종코드를 코드정보로부터 조회 - COM027
		//		vo.setCodeId("COM027");
		//		List<?> indutyCode_result = cmmUseService.selectCmmCodeDetail(vo);

		//		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		//		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		//		model.addAttribute("entrprsMberSttus_result", entrprsMberSttus_result);//사용자상태코드목록
		//		model.addAttribute("groupId_result", groupId_result); //그룹정보 목록
		//		model.addAttribute("entrprsSeCode_result", entrprsSeCode_result); //기업구분코드 목록
		//		model.addAttribute("indutyCode_result", indutyCode_result); //업종코드목록

		// 2021.05.30, 정진오, 디지털원패스 정보 조회
		LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
		String onepassUserId = loginVO.getUniqId();
		String onepassUserkey = loginVO.getOnepassUserkey();
		String onepassIntfToken = loginVO.getOnepassIntfToken();
		if (entrprsmberId.equals(onepassUserId)) {
			model.addAttribute("onepassUserkey", onepassUserkey); //디지털원패스 사용자키
			model.addAttribute("onepassIntfToken", onepassIntfToken); //디지털원패스 사용자세션값
		} else {
			model.addAttribute("onepassUserkey", "");
			model.addAttribute("onepassIntfToken", "");
		}

		try {
			return ApiResult.Data(loginVO, null);
		} catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreEntrprsMberSelectUpdt";
	}

	/**
	 * 로그인인증제한 해제
	 *
	 * @param entrprsManageVO 기업회원정보
	 * @param model           화면모델
	 * @return uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberLockIncorrectApi.cm")
	public ApiResult<Object> updateLockIncorrect(EntrprsManageVO entrprsManageVO, ComUrlVO comUrlVO, Model model) throws Exception {

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
			//return "index";
		}

		coreEntrprsManageService.updateLockIncorrect(entrprsManageVO);
		try {
			comUrlVO.setFwdUrl("/uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm");
			return ApiResult.Data(comUrlVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "forward:/uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm";
	}



	/**
	 * 기업회원정보 수정후 목록조회 화면으로 이동한다.
	 * @param entrprsManageVO 수정할 기업회원정보
	 * @param bindingResult 입력값 검증용 bindingResult
	 * @param model 화면모델
	 * @return forward:/uss/umt/CoreEntrprsMberManageApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberSelectUpdtApi.cm")
	public ApiResult<Object> updateEntrprsMberApi(@ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO, ComUrlVO comUrlVO,
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
			//return "index";
		}

		beanValidator.validate(entrprsManageVO, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("resultMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			try {
				return ApiResult.Message(bindingResult.getAllErrors().get(0).getDefaultMessage());
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "forward:/uss/umt/CoreEntrprsMberSelectUpdtViewApi.cm";
		} else {
			if ("".equals(entrprsManageVO.getGroupId())) {
				entrprsManageVO.setGroupId(null);
			}
			coreEntrprsManageService.updateEntrprsmber(entrprsManageVO);
			//Exception 없이 진행시 수정성공메시지
			model.addAttribute("resultMsg", coreMessageSource.getMessage("success.common.update"));

			try {
				return ApiResult.Message(bindingResult.getAllErrors().get(0).getDefaultMessage());
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "forward:/uss/umt/CoreEntrprsMberManageApi.cm";
		}
	}

	/**
	 * 기업회원정보삭제후 목록조회 화면으로 이동한다.
	 * @param checkedIdForDel 삭제대상아이디 정보
	 * @param userSearchVO 조회조건정보
	 * @param model 화면모델
	 * @return "forward:/uss/umt/CoreUserManageApi.cm"
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberDeleteApi.cm")
	public ApiResult<Object> deleteEntrprsMberApi(@RequestParam("checkedIdForDel") String checkedIdForDel,
		@ModelAttribute("searchVO") UserDefaultVO userSearchVO, HttpServletRequest request, ComUrlVO comUrlVO,  Model model)
		throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			try {
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
			coreEntrprsManageService.deleteEntrprsmber(checkedIdForDel);
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

		//return "forward:/uss/umt/CoreEntrprsMberManageApi.cm";
	}

	/**
	 * 기업회원목록을 조회한다. (pageing)
	 * @param userSearchVO 검색조건정보
	 * @param model 화면모델
	 * @return uss/umt/CoreEntrprsMberManage
	 * @throws Exception
	 */
	@IncludedInfo(name = "기업회원관리", order = 450, gid = 50)
	@RequestMapping(value = "/uss/umt/CoreEntrprsMberManageApi.cm")
	public ApiResult<Object> selectEntrprsMberListApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO, ModelMap model)
		throws Exception {

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

		/** CorePropertyService.sample */
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

		List<?> entrprsList = coreEntrprsManageService.selectEntrprsMberList(userSearchVO);
		model.addAttribute("resultList", entrprsList);

		int totCnt = coreEntrprsManageService.selectEntrprsMberListTotCnt(userSearchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		//사용자상태코드를 코드정보로부터 조회
		//		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		//		vo.setCodeId("COM013");
		//		List<?> entrprsMberSttus_result = cmmUseService.selectCmmCodeDetail(vo);
		//		model.addAttribute("entrprsMberSttus_result", entrprsMberSttus_result);//기업회원상태코드목록

		try {
			return ApiResult.OK(entrprsList, paginationInfo, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreEntrprsMberManage";
	}

	/**
	 * 기업회원가입신청 등록화면으로 이동한다.
	 * @param userSearchVO 검색조건정보
	 * @param entrprsManageVO 기업회원초기화정보
	 * @param commandMap 파라메터전송 commandMap
	 * @param model 화면모델
	 * @return uss/umt/CoreEntrprsMberSbscrb
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberSbscrbViewApi.cm")
	public ApiResult<Object> sbscrbEntrprsMberViewApi(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO,
		@ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO,
		@RequestParam Map<String, Object> commandMap, Model model) throws Exception {

		//		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		//		//패스워드힌트목록을 코드정보로부터 조회
		//		vo.setCodeId("COM022");
		//		List<?> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//성별구분코드를 코드정보로부터 조회
		//		vo.setCodeId("COM014");
		//		List<?> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//기업구분코드를 코드정보로부터 조회 - COM026
		//		vo.setCodeId("COM026");
		//		List<?> entrprsSeCode_result = cmmUseService.selectCmmCodeDetail(vo);
		//		//업종코드를 코드정보로부터 조회 - COM027
		//		vo.setCodeId("COM027");
		//		List<?> indutyCode_result = cmmUseService.selectCmmCodeDetail(vo);

		//		model.addAttribute("passwordHint_result", passwordHint_result); //패스워트힌트목록
		//		model.addAttribute("sexdstnCode_result", sexdstnCode_result); //성별구분코드목록
		//		model.addAttribute("entrprsSeCode_result", entrprsSeCode_result); //기업구분코드 목록
		//		model.addAttribute("indutyCode_result", indutyCode_result); //업종코드목록
		if (!"".equals(commandMap.get("realname"))) {
			model.addAttribute("applcntNm", commandMap.get("realname")); //실명인증된 이름 - 주민번호인증
			model.addAttribute("applcntIhidnum", commandMap.get("ihidnum")); //실명인증된 주민등록번호 - 주민번호 인증
			try {
				return ApiResult.Data(commandMap, null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
		if (!"".equals(commandMap.get("realName"))) {
			model.addAttribute("applcntNm", commandMap.get("realName")); //실명인증된 이름 - ipin인증
			try {
				return ApiResult.Data(commandMap, null);
			}catch (Exception e) {
				LOGGER.error("ERROR::", e);
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
		entrprsManageVO.setEntrprsMberSttus("DEFAULT");
		try {

			return ApiResult.Data(entrprsManageVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreEntrprsMberSbscrb";
	}

	/**
	 * 기업회원가입신청 등록처리후 로그인화면으로 이동한다.
	 * @param entrprsManageVO 기업회원가입신청정보
	 * @return forward:/uat/uia/coreLoginUsrApi.cm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreEntrprsMberSbscrbApi.cm")
	public ApiResult<Object> sbscrbEntrprsMberApi(@ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO)
		throws Exception {

		//가입상태 초기화
		entrprsManageVO.setEntrprsMberSttus("A");
		//그룹정보 초기화
		//entrprsManageVO.setGroupId("1");
		//기업회원가입신청 등록시 기업회원등록기능을 사용하여 등록한다.
		coreEntrprsManageService.insertEntrprsmber(entrprsManageVO);
		try {
			return ApiResult.Message(coreMessageSource.getMessage("success.common.delete"));
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "forward:/uat/uia/coreLoginUsr.cm";
	}

	/**
	 * 기업회원 약관확인 화면을 조회한다.
	 * @param model 화면모델
	 * @return uss/umt/CoreStplatCnfirm
	 * @throws Exception
	 */
	@RequestMapping("/uss/umt/CoreStplatCnfirmEntrprsApi.cm")
	public ApiResult<Object> sbscrbEntrprsMberApi(Model model) throws Exception {

		//기업회원용 약관 아이디 설정
		String stplatId = "STPLAT_0000000000002";
		//회원가입유형 설정-기업회원
		String sbscrbTy = "USR02";
		//약관정보 조회
		List<?> stplatList = coreEntrprsManageService.selectStplat(stplatId);
		model.addAttribute("stplatList", stplatList); //약관정보포함
		model.addAttribute("sbscrbTy", sbscrbTy); //회원가입유형포함

		try {
			return ApiResult.OK(stplatList, sbscrbTy, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreStplatCnfirm";
	}

	/**
	 * 기업회원 암호 수정처리 후 화면 이동한다.
	 * @param model 화면모델
	 * @param commandMap 파라메터전달용 commandMap
	 * @param userSearchVO 검색조건정보
	 * @param entrprsManageVO 기업회원수정정보
	 * @return uss/umt/CoreEntrprsPasswordUpdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreEntrprsPasswordUpdtApi.cm")
	public ApiResult<Object> updatePasswordApi(ModelMap model, @RequestParam Map<String, Object> commandMap,
		@ModelAttribute("searchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO,
		@ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO) throws Exception {

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

		String oldPassword = (String)commandMap.get("oldPassword");
		String newPassword = (String)commandMap.get("newPassword");
		String newPassword2 = (String)commandMap.get("newPassword2");
		String uniqId = (String)commandMap.get("uniqId");

		boolean isCorrectPassword = false;
		EntrprsManageVO resultVO = new EntrprsManageVO();
		entrprsManageVO.setEntrprsMberPassword(newPassword);
		entrprsManageVO.setOldPassword(oldPassword);
		entrprsManageVO.setUniqId(uniqId);

		String resultMsg = "";
		resultVO = coreEntrprsManageService.selectPassword(entrprsManageVO);
		//패스워드 암호화
		String encryptPass = EgovFileScrty.encryptPassword(oldPassword, entrprsManageVO.getEntrprsmberId());
		if (encryptPass.equals(resultVO.getEntrprsMberPassword())) {
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
			entrprsManageVO
				.setEntrprsMberPassword(EgovFileScrty.encryptPassword(newPassword, entrprsManageVO.getEntrprsmberId()));
			coreEntrprsManageService.updatePassword(entrprsManageVO);
			model.addAttribute("entrprsManageVO", entrprsManageVO);
			resultMsg = "success.common.update";
		} else {
			model.addAttribute("entrprsManageVO", entrprsManageVO);
		}
		model.addAttribute("userSearchVO", userSearchVO);
		model.addAttribute("resultMsg", coreMessageSource.getMessage(resultMsg));

		try {
			return ApiResult.Data(entrprsManageVO, coreMessageSource.getMessage(resultMsg));
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uss/umt/CoreEntrprsPasswordUpdt";
	}

	/**
	 * 기업회원암호 수정 화면 이동
	 * @param model 화면모델
	 * @param commandMap 파라메터전송용 commandMap
	 * @param userSearchVO 검색조건정보
	 * @param entrprsManageVO 기업회원수정정보
	 * @return uss/umt/CoreEntrprsPasswordUpdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/uss/umt/CoreEntrprsPasswordUpdtView.cm")
	public ApiResult<Object> updatePasswordView(ModelMap model, @RequestParam Map<ApiResult<Object>, Object> commandMap,
		@ModelAttribute("searchVO") UserDefaultVO userSearchVO, ComUrlVO comUrlVO,
		@ModelAttribute("entrprsManageVO") EntrprsManageVO entrprsManageVO) throws Exception {

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

		String userTyForPassword = (String)commandMap.get("userTyForPassword");
		entrprsManageVO.setUserTy(userTyForPassword);

		model.addAttribute("userSearchVO", userSearchVO);
		model.addAttribute("entrprsManageVO", entrprsManageVO);

		try {
			return ApiResult.Data(entrprsManageVO, null);
		}catch (Exception e) {
			LOGGER.error("ERROR::", e);
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}

		//return "coreframework/com/uss/umt/CoreEntrprsPasswordUpdt";
	}

}