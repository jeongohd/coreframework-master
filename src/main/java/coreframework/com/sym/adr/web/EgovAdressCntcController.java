package coreframework.com.sym.adr.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coreframework.com.cmm.CoreWebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import coreframework.com.cmm.annotation.IncludedInfo;

/**
 * 도로명 주소 연계를 위한 Controller
 *
 *  수정일                수정자             수정내용
 *  ----------   ---------   -------------------
 *  2014.10.21   표준프레임워크    최초생성
 *  2015.04.01   전여철              Test용 Open API confmKey encode추가
 *  2020.10.29   신용호              KISA 보안약점 조치 (경로 조작 및 자원 삽입, 크로스사이트 스크립트)
 *
 * @author 표준프레임워크
 * @since 2014.10.21
 * @version 3.5
 */

@Controller
public class EgovAdressCntcController {

	/**
	 * 도로명주소 안내시스템에서 제공하는 Open API를 호출하여 주소 정보를 얻어온다.
	 *
	 * @param req
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/adr/getAdressCntcApi.cm")
	public void getAddrApi(HttpServletRequest req, ModelMap model, HttpServletResponse response) throws Exception {

		String currentPage = req.getParameter("currentPage");
		String countPerPage = req.getParameter("countPerPage");
		String confmKey = req.getParameter("confmKey");
		String keyword = req.getParameter("keyword");
		String apiUrl = "http://www.juso.go.kr/addrlink/addrLinkApi.cm?currentPage=" + currentPage + "&countPerPage="
			+ countPerPage + "&keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&confmKey=" + confmKey;
		URL url = new URL(CoreWebUtil.filePathBlackList(apiUrl));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));) {//2022.01 Resources should be closed
			StringBuffer sb = new StringBuffer();
			String tempStr = null;
			while (true) {
				tempStr = br.readLine();
				if (tempStr == null) {
					break;
				}
				sb.append(tempStr);
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.getWriter().write(CoreWebUtil.clearXSSMinimum(sb.toString()));
		}
	}

	/**
	 * 도로명주소 안내시스템에서 제공하는 Test용 Open API를 호출하여 주소 정보를 얻어온다.
	 * @param req
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/sym/adr/getAdressCntcTestApi.cm")
	public void getAddrApiTest(HttpServletRequest req, ModelMap model, HttpServletResponse response) throws Exception {

		String currentPage = req.getParameter("currentPage");
		String countPerPage = req.getParameter("countPerPage");
		String confmKey = req.getParameter("confmKey");
		String keyword = req.getParameter("keyword");
		String apiUrl = "http://www.juso.go.kr/addrlink/addrLinkApiTest.cm?currentPage=" + currentPage
			+ "&countPerPage=" + countPerPage + "&keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&confmKey="
			+ URLEncoder.encode(confmKey, "UTF-8");
		URL url = new URL(CoreWebUtil.filePathBlackList(apiUrl));
		try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));){//2022.01 Resources should be closed
			StringBuffer sb = new StringBuffer();
			String tempStr = null;
			while (true) {
				tempStr = br.readLine();
				if (tempStr == null) {
					break;
				}
				sb.append(tempStr);
			}
			br.close();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.getWriter().write(CoreWebUtil.clearXSSMinimum(sb.toString()));
		}

	}

	/**
	 * 주소정보연계를 위한 입력 페이지를 호출한다.
	 *
	 * @return
	 */
	@IncludedInfo(name = "주소정보연계", listUrl = "/sym/adr/getAdressCntcInitPage.cm", order = 2180, gid = 90)
	@RequestMapping(value = "/sym/adr/getAdressCntcInitPage.cm")
	public String selectMainMenuHome() {

		return "coreframework/com/sym/adr/EgovAdressCntc";
	}

}
