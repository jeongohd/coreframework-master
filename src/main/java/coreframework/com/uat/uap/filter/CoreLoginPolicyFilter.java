package coreframework.com.uat.uap.filter;

import coreframework.com.cmm.CoreMessageSource;
import coreframework.com.uat.uap.service.CoreLoginPolicyService;
import coreframework.com.uat.uap.vo.LoginPolicyVO;
import coreframework.com.utl.sim.service.EgovClntInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 로그인 정책 체크 필터
 * @author 공통서비스 개발팀 서준식
 * @since 2011.07.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.07.01  서준식          최초 생성
 *  2017-02-14  이정은          시큐어코딩(ES) - 시큐어코딩 부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
 *
 *  </pre>
 */

public class CoreLoginPolicyFilter implements Filter {

	private FilterConfig config;

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreLoginPolicyFilter.class);

	public void destroy() {
	}

	/**
	 * IP를 이용해 로그인을 제한하는 메서든
	 * @param request
	 * @param response
	 * @param chain
	 * @return void
	 * @exception IOException, ServletException
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		CoreLoginPolicyService coreLoginPolicyService = (CoreLoginPolicyService) act.getBean("CoreLoginPolicyService");
		CoreMessageSource coreMessageSource = (CoreMessageSource) act.getBean("coreMessageSource");

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String id = request.getParameter("id");
		//String password = request.getParameter("password");
		String userSe = request.getParameter("userSe");
		String userIp = "";

		if (id == null || userSe == null) {
			((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/uat/uia/coreLoginUsr.cm");
		}

		// 1. LoginVO를 DB로 부터 가져오는 과정

		try {
			// 접속IP
			userIp = EgovClntInfo.getClntIP((HttpServletRequest) request);

			boolean loginPolicyYn = true;

			LoginPolicyVO loginPolicyVO = new LoginPolicyVO();
			loginPolicyVO.setEmplyrId(id);
			loginPolicyVO = coreLoginPolicyService.selectLoginPolicy(loginPolicyVO);

			if (loginPolicyVO == null) {
				loginPolicyYn = true;
			} else {
				if (loginPolicyVO.getLmttAt().equals("Y")) {
					if (!userIp.equals(loginPolicyVO.getIpInfo())) {
						loginPolicyYn = false;
					}
				}
			}

			if (loginPolicyYn) {
				chain.doFilter(request, response);

			} else {
				String message = URLEncoder.encode(coreMessageSource.getMessage("fail.common.login.ip"),"UTF-8");
				((HttpServletRequest) request).setAttribute("loginMessage", message);
				((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/uat/uia/coreLoginUsr.cm?loginMessage="+message);
			}

		} catch (IOException e) {//KISA 보안약점 조치 (2018-10-29, 윤창원)
			LOGGER.error("["+ e.getClass() +"] : ", e.getMessage());
			((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/uat/uia/coreLoginUsr.cm?login_error=1");
		} catch (Exception e) {
//			LOGGER.error("Exception: {}", e.getClass().getName());
//			LOGGER.error("Exception  Message: {}", e.getMessage());
			// 2017-02-14  이정은          시큐어코딩(ES) - 시큐어코딩 부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
			LOGGER.error("["+ e.getClass() +"] : ", e.getMessage());
			
			((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/uat/uia/coreLoginUsr.cm?login_error=1");
		}
	}

	public void init(FilterConfig config) throws ServletException {

		this.config = config;

	}

}
