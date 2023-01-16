package coreframework.com.cmm.util;

import coreframework.com.cmm.LoginVO;
import coreframework.com.cmm.exception.CoreXssException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * CoreXssChecker 클래스
 *
 * @author 장동한
 * @since 2016.10.27
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일        수정자           수정내용
 *  -------      -------------  ----------------------
 *   2016.10.17  장동한           최초 생성
 *   2017.03.03     조성원 	  시큐어코딩(ES)-오류 메시지를 통한 정보노출[CWE-209]
 * </pre>
 */

public class CoreXssChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreXssChecker.class);

	/**
	 * 사용자에 대한 크로스사이트스크립트(Xss) 확인한다.
	 * 수정, 상세조회, 삭제시 사용
	 * @param uniqId Stirng
	 * @return boolean
	 * @exception IllegalArgumentException
	 */
	public static boolean checkerUserXss(HttpServletRequest request, String sUniqId) throws Exception {

		boolean bLog = false;

		try {
			//@ 공통모듈을 이용한 권한체크
			LoginVO loginVO = (LoginVO) CoreUserDetailsHelper.getAuthenticatedUser();

			if (loginVO != null) {
				if (bLog) {
					LOGGER.debug("@Step1. XSS Check uniqId  : {}", sUniqId);
					LOGGER.debug("Step2. XSS Session uniqId  : {}", loginVO.getId());
					LOGGER.debug("Step3. XSS Session getUniqId  : {}", loginVO.getUniqId());
					LOGGER.debug("Step4. XSS Session getAuthorities  : {}", CoreUserDetailsHelper.getAuthorities());
				}

				//체크 값에 대한 무결성 체크
				//			if(sUniqId == null || (loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getUniqId())) == null){
				//				throw new CoreXssException("XSS00001", "errors.xss.checkerUser");
				//			} else if (loginVO.getUniqId().equals("")) { // KISA 보안약점 조치 (2018-12-11, 신용호)
				//				throw new CoreXssException("XSS00001", "errors.xss.checkerUser");
				//			}
				//
				//			//사용자에에 대한 Xss 체크
				//			if(!sUniqId.equals(loginVO.getUniqId())){
				//				throw new CoreXssException("XSS00002", "errors.xss.checkerUser");
				//			}

				if (sUniqId == null || loginVO.getUniqId() == null || loginVO.getUniqId().equals("")) {
					throw new CoreXssException("XSS00001", "errors.xss.checkerUser");
				}

				//사용자에에 대한 Xss 체크
				if (!sUniqId.equals(loginVO.getUniqId())) {
					throw new CoreXssException("XSS00002", "errors.xss.checkerUser");
				}
			} else {
				throw new CoreXssException("XSS00001", "errors.xss.checkerUser");
			}

			//2017.03.03 	조성원 	시큐어코딩(ES)-오류 메시지를 통한 정보노출[CWE-209]
		} catch (IllegalArgumentException e) {
			LOGGER.error("[IllegalArgumentException] Try/Catch...usingParameters Runing : " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("[" + e.getClass() + "] Try/Catch...Exception : " + e.getMessage());
		}
		return true;
	}

}
