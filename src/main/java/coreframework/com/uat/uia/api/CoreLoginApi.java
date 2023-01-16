package coreframework.com.uat.uia.api;


import coreframework.com.cmm.ComDefaultCodeVO;
import coreframework.com.cmm.CoreComponentChecker;
import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.annotation.IncludedInfo;
import coreframework.com.cmm.config.CoreLoginConfig;
import coreframework.com.cmm.service.CoreCmmUseService;
import coreframework.com.cmm.service.EgovProperties;
import coreframework.com.cmm.service.Globals;
import coreframework.com.cmm.util.ApiResult;
import coreframework.com.cmm.util.CoreUserDetailsHelper;
import coreframework.com.uat.uia.service.CoreLoginService;
import coreframework.com.utl.fcc.service.EgovStringUtil;
import coreframework.com.utl.sim.service.EgovClntInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/*
import com.gpki.gpkiapi.cert.X509Certificate;
import com.gpki.servlet.GPKIHttpServletRequest;
import com.gpki.servlet.GPKIHttpServletResponse;
*/

/**
 * 일반 로그인, 인증서 로그인을 처리하는 컨트롤러 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일               수정자            수정내용
 *  ----------   --------   ---------------------------
 *  2009.03.06   박지욱           최초 생성
 *  2011.08.26   정진오           IncludedInfo annotation 추가
 *  2011.09.07   서준식           스프링 시큐리티 로그인 및 SSO 인증 로직을 필터로 분리
 *  2011.09.25   서준식           사용자 관리 컴포넌트 미포함에 대한 점검 로직 추가
 *  2011.09.27   서준식           인증서 로그인시 스프링 시큐리티 사용에 대한 체크 로직 추가
 *  2011.10.27   서준식           아이디 찾기 기능에서 사용자 리름 공백 제거 기능 추가
 *  2017.07.21   장동한           로그인인증제한 작업
 *  2018.10.26   신용호           로그인 화면에 message 파라미터 전달 수정
 *  2019.10.01   정진오           로그인 인증세션 추가
 *  2020.06.25   신용호           로그인 메시지 처리 수정
 *  2021.01.15   신용호           로그아웃시 권한 초기화 추가 : session 모드 actionLogout()
 *  2021.05.30   정진오           디지털원패스 처리하기 위해 로그인 화면에 인증방식 전달
 *  
 *  </pre>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CoreLoginApi {

	/** EgovLoginService */
	@Resource(name = "CoreLoginService")
	private CoreLoginService loginService;

	/** CoreCmmUseService */
	@Resource(name = "CoreCmmUseService")
	private CoreCmmUseService cmmUseService;

	/** CoreMessageSource */
	@Resource(name = "coreMessageSource")
	CoreMessageSource coreMessageSource;

	@Resource(name = "coreLoginConfig")
	CoreLoginConfig coreLoginConfig;

	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreLoginApi.class);

	/**
	 * 로그인 화면으로 들어간다
	 * @param vo - 로그인후 이동할 URL이 담긴 LoginVO
	 * @return 로그인 페이지
	 * @exception Exception
	 */
	@IncludedInfo(name = "로그인", listUrl = "/uat/uia/coreLoginUsrApi.cm", order = 10, gid = 10)
	@RequestMapping(value = "/uat/uia/coreLoginUsrApi.cm")
	public ApiResult<Object> loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		if (CoreComponentChecker.hasComponent("mberManageService")) {
			model.addAttribute("useMemberManage", "true");
			try {
				return ApiResult.Data("true",null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
				
		//권한체크시 에러 페이지 이동
		String auth_error =  request.getParameter("auth_error") == null ? "" : (String)request.getParameter("auth_error");
		if(auth_error != null && auth_error.equals("1")){
			try {
				loginVO.setUrl("coreframework/com/cmm/error/accessDenied");

				return  ApiResult.Data(loginVO, null);
			}catch (Exception e) {
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
			//return "coreframework/com/cmm/error/accessDenied";
		}

		/*
		GPKIHttpServletResponse gpkiresponse = null;
		GPKIHttpServletRequest gpkirequest = null;

		try{

			gpkiresponse=new GPKIHttpServletResponse(response);
		    gpkirequest= new GPKIHttpServletRequest(request);
		    gpkiresponse.setRequest(gpkirequest);
		    model.addAttribute("challenge", gpkiresponse.getChallenge());
		    return "coreframework/com/uat/uia/EgovLoginUsr";

		}catch(Exception e){
		    return "coreframework/com/cmm/coreError";
		}
		*/

		// 2021.05.30, 정진오, 디지털원패스 처리하기 위해 로그인 화면에 인증방식 전달
		String authType = EgovProperties.getProperty("Globals.Auth").trim();
		model.addAttribute("authType", authType);

		String message = (String)request.getParameter("loginMessage");
		if (message!=null) model.addAttribute("loginMessage", message);

		try {
			loginVO.setUrl("coreframework/com/uat/uia/LoginUsrIdx");

			return  ApiResult.Data(loginVO, null);
		}catch (Exception e) {
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
//
//		return "coreframework/com/uat/uia/LoginUsrIdx";
	}

	/**
	 * 일반(세션) 로그인을 처리한다
	 * @param vo - 아이디, 비밀번호가 담긴 LoginVO
	 * @param request - 세션처리를 위한 HttpServletRequest
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/actionLoginApi.cm")
	public ApiResult<Object> actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {

		// 1. 로그인인증제한 활성화시 
		if( coreLoginConfig.isLock()){
		    Map<?,?> mapLockUserInfo = (EgovMap)loginService.selectLoginIncorrect(loginVO);
		    if(mapLockUserInfo != null){			
				//2.1 로그인인증제한 처리
				String sLoginIncorrectCode = loginService.processLoginIncorrect(loginVO, mapLockUserInfo);
				String msg = null;

				if(!sLoginIncorrectCode.equals("E")){
					if(sLoginIncorrectCode.equals("L")){
						model.addAttribute("loginMessage", coreMessageSource.getMessageArgs("fail.common.loginIncorrect", new Object[] {coreLoginConfig.getLockCount(),request.getLocale()}));
						msg = coreMessageSource.getMessageArgs("fail.common.loginIncorrect", new Object[] {coreLoginConfig.getLockCount(),request.getLocale()});
					}else if(sLoginIncorrectCode.equals("C")){
						model.addAttribute("loginMessage", coreMessageSource.getMessage("fail.common.login",request.getLocale()));
						msg = coreMessageSource.getMessage("fail.common.login",request.getLocale());
					}
					//return "coreframework/com/uat/uia/LoginUsrIdx";
					try {
						return ApiResult.Message(msg);
					}catch (Exception e){
						log.error(e.getMessage());
						return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
					}
				}
		    }else{
		    	model.addAttribute("loginMessage", coreMessageSource.getMessage("fail.common.login",request.getLocale()));
				try {
					return ApiResult.Message(coreMessageSource.getMessage("fail.common.login",request.getLocale()));
				}catch (Exception e){
					log.error(e.getMessage());
					return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
				}
		    	//return "coreframework/com/uat/uia/LoginUsrIdx";
		    }
		}
		
		// 2. 로그인 처리
		LoginVO resultVO = loginService.actionLogin(loginVO);
		
		// 3. 일반 로그인 처리
		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("")) {

			// 3-1. 로그인 정보를 세션에 저장
			request.getSession().setAttribute("loginVO", resultVO);
			// 2019.10.01 로그인 인증세션 추가
			request.getSession().setAttribute("accessUser", resultVO.getUserSe().concat(resultVO.getId()));

			try {
				loginVO.setUrl("/uat/uia/actionMainApi.cm");
				return ApiResult.Data(loginVO,null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "redirect:/uat/uia/actionMainApi.cm";

		} else {
			model.addAttribute("loginMessage", coreMessageSource.getMessage("fail.common.login",request.getLocale()));
			try {
				return ApiResult.Data("coreframework/com/uat/uia/LoginUsrIdx",null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "coreframework/com/uat/uia/LoginUsrIdx";
		}
	}

	/**
	 * 인증서 로그인을 처리한다
	 * @param vo - 주민번호가 담긴 LoginVO
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/actionCrtfctLoginApi.cm")
	public ApiResult<Object> actionCrtfctLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 접속IP
		String userIp = EgovClntInfo.getClntIP(request);
		LOGGER.debug("User IP : {}", userIp);

		/*
		// 1. GPKI 인증
		GPKIHttpServletResponse gpkiresponse = null;
		GPKIHttpServletRequest gpkirequest = null;
		String dn = "";
		try{
			gpkiresponse = new GPKIHttpServletResponse(response);
		    gpkirequest = new GPKIHttpServletRequest(request);
		    gpkiresponse.setRequest(gpkirequest);
		    X509Certificate cert = null;

		    byte[] signData = null;
		    byte[] privatekey_random = null;
		    String signType = "";
		    String queryString = "";

		    cert = gpkirequest.getSignerCert();
		    dn = cert.getSubjectDN();

		    java.math.BigInteger b = cert.getSerialNumber();
		    b.toString();
		    int message_type =  gpkirequest.getRequestMessageType();
		    if( message_type == gpkirequest.ENCRYPTED_SIGNDATA || message_type == gpkirequest.LOGIN_ENVELOP_SIGN_DATA ||
		        message_type == gpkirequest.ENVELOP_SIGNDATA || message_type == gpkirequest.SIGNED_DATA){
		        signData = gpkirequest.getSignedData();
		        if(privatekey_random != null) {
		            privatekey_random   = gpkirequest.getSignerRValue();
		        }
		        signType = gpkirequest.getSignType();
		    }
		    queryString = gpkirequest.getQueryString();
		}catch(Exception e){
			return "cmm/coreError";
		}

		// 2. 업무사용자 테이블에서 dn값으로 사용자의 ID, PW를 조회하여 이를 일반로그인 형태로 인증하도록 함
		if (dn != null && !dn.equals("")) {

			loginVO.setDn(dn);
			LoginVO resultVO = loginService.actionCrtfctLogin(loginVO);
		    if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("")) {

		    	//스프링 시큐리티패키지를 사용하는지 체크하는 로직
		    	if(CoreComponentChecker.hasComponent("coreAuthorManageService")){
		    		// 3-1. spring security 연동
		            return "redirect:/j_spring_security_check?j_username=" + resultVO.getUserSe() + resultVO.getId() + "&j_password=" + resultVO.getUniqId();

		    	}else{
		    		// 3-2. 로그인 정보를 세션에 저장
		        	request.getSession().setAttribute("loginVO", resultVO);
		    		return "redirect:/uat/uia/actionMainApi.cm";
		    	}


		    } else {
		    	model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
		    	return "coreframework/com/uat/uia/EgovLoginUsr";
		    }
		} else {
			model.addAttribute("message", coreMessageSource.getMessage("fail.common.login"));
			return "coreframework/com/uat/uia/EgovLoginUsr";
		}
		*/
		try {
			return ApiResult.Data("coreframework/com/uat/uia/LoginUsrIdx",null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uat/uia/LoginUsrIdx";
	}

	/**
	 * 로그인 후 메인화면으로 들어간다
	 * @param
	 * @return 로그인 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/actionMainApi.cm")
	public ApiResult<Object> actionMain(ModelMap model) throws Exception {

		// 1. Spring Security 사용자권한 처리
		Boolean isAuthenticated = CoreUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			model.addAttribute("loginMessage", coreMessageSource.getMessage("fail.common.login"));

			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.login"));
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

			//return "coreframework/com/uat/uia/CoreLoginUsr";
		}
		LoginVO user = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		
		LOGGER.debug("User Id : {}", user == null ? "" : EgovStringUtil.isNullToString(user.getId()));

		/*
		// 2. 메뉴조회
		MenuManageVO menuManageVO = new MenuManageVO();
		menuManageVO.setTmp_Id(user.getId());
		menuManageVO.setTmp_UserSe(user.getUserSe());
		menuManageVO.setTmp_Name(user.getName());
		menuManageVO.setTmp_Email(user.getEmail());
		menuManageVO.setTmp_OrgnztId(user.getOrgnztId());
		menuManageVO.setTmp_UniqId(user.getUniqId());
		List list_headmenu = menuManageService.selectMainMenuHead(menuManageVO);
		model.addAttribute("list_headmenu", list_headmenu);
		*/

		// 3. 메인 페이지 이동
		String main_page = Globals.MAIN_PAGE;

		LOGGER.debug("Globals.MAIN_PAGE > " + Globals.MAIN_PAGE);
		LOGGER.debug("main_page > {}", main_page);

		if (main_page.startsWith("/")) {
			//return "forward:" + main_page;

			user.setUrl("indexApi.cm");
			try {
				return ApiResult.Data(user,null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}

		} else {
			//return main_page;
			user.setUrl(main_page);
			try {
				return ApiResult.Data(main_page,null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}

		/*
		if (main_page != null && !main_page.equals("")) {

			// 3-1. 설정된 메인화면이 있는 경우
			return main_page;

		} else {

			// 3-2. 설정된 메인화면이 없는 경우
			if (user.getUserSe().equals("USR")) {
				return "coreframework/com/CoreMainView";
			} else {
				return "coreframework/com/CoreMainViewG";
			}
		}
		*/
	}

	/**
	 * 로그아웃한다.
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/actionLogoutApi.cm")
	public ApiResult<Object> actionLogout(HttpServletRequest request, ModelMap model) throws Exception {

		/*String userIp = EgovClntInfo.getClntIP(request);

		// 1. Security 연동
		return "redirect:/j_spring_security_logout";*/

		request.getSession().setAttribute("loginVO", null);
		// 세션모드인경우 Authority 초기화
		// List<String> authList = (List<String>)CoreUserDetailsHelper.getAuthorities();
		request.getSession().setAttribute("accessUser", null);

		//return "redirect:/egovDevIndex.jsp";
		//return "redirect:/CoreContentApi.cm";
		try {
			return ApiResult.Data("CoreContentApi.cm",null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

	/**
	 * 아이디/비밀번호 찾기 화면으로 들어간다
	 * @param
	 * @return 아이디/비밀번호 찾기 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/egovIdPasswordSearchApi.cm")
	public ApiResult<Object> idPasswordSearchView(ModelMap model) throws Exception {

		// 1. 비밀번호 힌트 공통코드 조회
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM022");
		List<?> code = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("pwhtCdList", code);

		//return "coreframework/com/uat/uia/EgovIdPasswordSearch";
		try {
			return ApiResult.Data("coreframework/com/uat/uia/EgovIdPasswordSearch",null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

	/**
	 * 인증서안내 화면으로 들어간다
	 * @return 인증서안내 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/egovGpkiIssuApi.cm")
	public ApiResult<Object> gpkiIssuView(ModelMap model) throws Exception {
		//return "coreframework/com/uat/uia/EgovGpkiIssu";
		try {
			return ApiResult.Data("coreframework/com/uat/uia/EgovGpkiIssu",null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

	/**
	 * 아이디를 찾는다.
	 * @param vo - 이름, 이메일주소, 사용자구분이 담긴 LoginVO
	 * @return result - 아이디
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/searchIdApi.cm")
	public ApiResult<Object> searchId(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model) throws Exception {

		if (loginVO == null || loginVO.getName() == null || loginVO.getName().equals("") && loginVO.getEmail() == null || loginVO.getEmail().equals("")
				&& loginVO.getUserSe() == null || loginVO.getUserSe().equals("")) {
			//return "coreframework/com/cmm/coreError";
			try {
				return ApiResult.Data("coreframework/com/cmm/coreError",null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}

		// 1. 아이디 찾기
		loginVO.setName(loginVO.getName().replaceAll(" ", ""));
		LoginVO resultVO = loginService.searchId(loginVO);

		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("")) {

			model.addAttribute("resultInfo", "아이디는 " + resultVO.getId() + " 입니다.");
			//return "coreframework/com/uat/uia/EgovIdPasswordResult";
			try {
				return ApiResult.Message("아이디는 " + resultVO.getId() + " 입니다.");
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		} else {
			model.addAttribute("resultInfo", coreMessageSource.getMessage("fail.common.idsearch"));
			//return "coreframework/com/uat/uia/EgovIdPasswordResult";
			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.idsearch"));
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
	}

	/**
	 * 비밀번호를 찾는다.
	 * @param vo - 아이디, 이름, 이메일주소, 비밀번호 힌트, 비밀번호 정답, 사용자구분이 담긴 LoginVO
	 * @return result - 임시비밀번호전송결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/searchPasswordApi.cm")
	public ApiResult<Object> searchPassword(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model) throws Exception {

		//KISA 보안약점 조치 (2018-10-29, 윤창원)
		if (loginVO == null || loginVO.getId() == null || loginVO.getId().equals("") && loginVO.getName() == null || "".equals(loginVO.getName()) && loginVO.getEmail() == null
				|| loginVO.getEmail().equals("") && loginVO.getPasswordHint() == null || "".equals(loginVO.getPasswordHint()) && loginVO.getPasswordCnsr() == null
				|| "".equals(loginVO.getPasswordCnsr()) && loginVO.getUserSe() == null || "".equals(loginVO.getUserSe())) {
			//return "coreframework/com/cmm/coreError";
			try {
				return ApiResult.Data("coreframework/com/cmm/coreError",null);
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}

		// 1. 비밀번호 찾기
		boolean result = loginService.searchPassword(loginVO);

		// 2. 결과 리턴
		if (result) {
			model.addAttribute("resultInfo", "임시 비밀번호를 발송하였습니다.");
			//return "coreframework/com/uat/uia/EgovIdPasswordResult";
			try {
				return ApiResult.Message("임시 비밀번호를 발송하였습니다.");
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		} else {
			model.addAttribute("resultInfo", coreMessageSource.getMessage("fail.common.pwsearch"));
			//return "coreframework/com/uat/uia/EgovIdPasswordResult";
			try {
				return ApiResult.Message(coreMessageSource.getMessage("fail.common.pwsearch"));
			}catch (Exception e){
				log.error(e.getMessage());
				return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
			}
		}
	}

	/**
	 * 개발 시스템 구축 시 발급된 GPKI 서버용인증서에 대한 암호화데이터를 구한다.
	 * 최초 한번만 실행하여, 암호화데이터를 EgovGpkiVariables.js의 ServerCert에 넣는다.
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/getEncodingDataApi.cm")
	public void getEncodingData() throws Exception {

		/*
		X509Certificate x509Cert = null;
		byte[] cert = null;
		String base64cert = null;
		try {
			x509Cert = Disk.readCert("/product/jeus/coreProps/gpkisecureweb/certs/SVR1311000011_env.cer");
			cert = x509Cert.getCert();
			Base64 base64 = new Base64();
			base64cert = base64.encode(cert);
			log.info("+++ Base64로 변환된 인증서는 : " + base64cert);

		} catch (GpkiApiException e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * 인증서 DN추출 팝업을 호출한다.
	 * @return 인증서 등록 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/EgovGpkiRegistApi.cm")
	public ApiResult<Object> gpkiRegistView(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		/** GPKI 인증 부분 */
		// OS에 따라 (local NT(로컬) / server Unix(서버)) 구분
		String os = System.getProperty("os.arch");
		LOGGER.debug("OS : {}", os);
		
		//String virusReturn = null;

		/*
		// 브라우저 이름을 받기위한 처리
		String webKind = EgovClntInfo.getClntWebKind(request);
		String[] ss = webKind.split(" ");
		String browser = ss[1];
		model.addAttribute("browser",browser);
		// -- 여기까지
		if (os.equalsIgnoreCase("x86")) {
		    //Local Host TEST 진행중
		} else {
		    if (browser.equalsIgnoreCase("Explorer")) {
		GPKIHttpServletResponse gpkiresponse = null;
		GPKIHttpServletRequest gpkirequest = null;

		try {
		    gpkiresponse = new GPKIHttpServletResponse(response);
		    gpkirequest = new GPKIHttpServletRequest(request);

		    gpkiresponse.setRequest(gpkirequest);
		    model.addAttribute("challenge", gpkiresponse.getChallenge());

		    return "coreframework/com/uat/uia/EgovGpkiRegist";

		} catch (Exception e) {
		    return "coreframework/com/cmm/coreError";
		}
		}
		}
		*/

		try {
			return ApiResult.Data("coreframework/com/uat/uia/EgovGpkiRegist",null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return "coreframework/com/uat/uia/EgovGpkiRegist";
	}

	/**
	 * 인증서 DN값을 추출한다
	 * @return result - dn값
	 * @exception Exception
	 */
	@RequestMapping(value = "/uat/uia/actionGpkiRegistApi.cm")
	public ApiResult<Object> actionGpkiRegist(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		/** GPKI 인증 부분 */
		// OS에 따라 (local NT(로컬) / server Unix(서버)) 구분
		String os = System.getProperty("os.arch");
		LOGGER.debug("OS : {}", os);
		
		// String virusReturn = null;
		@SuppressWarnings("unused")
		String dn = "";

		// 브라우저 이름을 받기위한 처리
		String webKind = EgovClntInfo.getClntWebKind(request);
		String[] ss = webKind.split(" ");
		String browser = ss[1];
		model.addAttribute("browser", browser);
		/*
		// -- 여기까지
		if (os.equalsIgnoreCase("x86")) {
			// Local Host TEST 진행중
		} else {
			if (browser.equalsIgnoreCase("Explorer")) {
				GPKIHttpServletResponse gpkiresponse = null;
				GPKIHttpServletRequest gpkirequest = null;
				try {
					gpkiresponse = new GPKIHttpServletResponse(response);
					gpkirequest = new GPKIHttpServletRequest(request);
					gpkiresponse.setRequest(gpkirequest);
					X509Certificate cert = null;

					// byte[] signData = null;
					// byte[] privatekey_random = null;
					// String signType = "";
					// String queryString = "";

					cert = gpkirequest.getSignerCert();
					dn = cert.getSubjectDN();

					model.addAttribute("dn", dn);
					model.addAttribute("challenge", gpkiresponse.getChallenge());

					return "coreframework/com/uat/uia/EgovGpkiRegist";
				} catch (Exception e) {
					return "coreframework/com/cmm/coreError";
				}
			}
		}
		*/
		//return "coreframework/com/uat/uia/EgovGpkiRegist";

		try {
			return ApiResult.Data(browser,null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}
	
	/**
	 * 세션타임아웃 시간을 연장한다.
	 * Cookie에 egovLatestServerTime, egovExpireSessionTime 기록하도록 한다.
	 * @return result - String
	 * @exception Exception
	 */
	@RequestMapping(value="/uat/uia/refreshSessionTimeoutApi.cm")
	public ApiResult<Object> refreshSessionTimeout(@RequestParam Map<String, Object> commandMap) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");

		modelAndView.addObject("result", "ok");
		try {
			return ApiResult.Data("ok",null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
		//return modelAndView;
	}
	
	/**
	 * 비밀번호 유효기간 팝업을 출력한다.
	 * Cookie에 egovLatestServerTime, egovExpireSessionTime 기록하도록 한다.
	 * @return result - String
	 * @exception Exception
	 */
	@RequestMapping(value="/uat/uia/noticeExpirePwdApi.cm")
	public ApiResult<Object> noticeExpirePwd(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		// 설정된 비밀번호 유효기간을 가져온다. ex) 180이면 비밀번호 변경후 만료일이 앞으로 180일 
		String propertyExpirePwdDay = EgovProperties.getProperty("Globals.ExpirePwdDay");
		int expirePwdDay = 0 ;
		try {
			expirePwdDay =  Integer.parseInt(propertyExpirePwdDay);
		} catch (NumberFormatException e) {
			LOGGER.debug("convert expirePwdDay Err : "+e.getMessage());
		} catch (Exception e) {
			LOGGER.debug("convert expirePwdDay Err : "+e.getMessage());
		}
		
		model.addAttribute("expirePwdDay", expirePwdDay);

		// 비밀번호 설정일로부터 몇일이 지났는지 확인한다. ex) 3이면 비빌번호 설정후 3일 경과
		LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("loginVO", loginVO);
		int passedDayChangePWD = 0;
		if ( loginVO != null ) {
			LOGGER.debug("===>>> loginVO.getId() = "+loginVO.getId());
			LOGGER.debug("===>>> loginVO.getUniqId() = "+loginVO.getUniqId());
			LOGGER.debug("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
			// 비밀번호 변경후 경과한 일수
			passedDayChangePWD = loginService.selectPassedDayChangePWD(loginVO);
			LOGGER.debug("===>>> passedDayChangePWD = "+passedDayChangePWD);
			model.addAttribute("passedDay", passedDayChangePWD);
		}
		
		// 만료일자로부터 경과한 일수 => ex)1이면 만료일에서 1일 경과
		model.addAttribute("elapsedTimeExpiration", passedDayChangePWD - expirePwdDay);
		
		//return "coreframework/com/uat/uia/EgovExpirePwd";

		try {
			return ApiResult.Data((passedDayChangePWD - expirePwdDay),null);
		}catch (Exception e){
			log.error(e.getMessage());
			return ApiResult.ERROR(e.getMessage(), BAD_REQUEST);
		}
	}

}