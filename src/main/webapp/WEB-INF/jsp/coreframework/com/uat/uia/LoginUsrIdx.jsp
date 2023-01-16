<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    /**
     * @Class Name : CoreLoginUsr.jsp
     * @Description : Login 인증 화면
     * @Modification Information
     *
     * @수정일               수정자            수정내용
     *  ----------   --------   ---------------------------
     *  2009.03.03   박지욱            최초 생성
     *  2011.09.25   서준식            사용자 관리 패키지가 미포함 되었을때에 회원가입 오류 메시지 표시
     *  2011.10.27   서준식            사용자 입력 탭 순서 변경
     *  2017.07.21   장동한            로그인인증제한 작업
     *  2019.12.11   신용호            KISA 보안약점 조치 (크로스사이트 스크립트)
     *  2020.06.23   신용호            세션만료시간 보여주기
     *  2021.05.30   정진오            디지털원패스 로그인 추가
     *
     *  @author 공통서비스 개발팀 박지욱
     *  @since 2009.03.03
     *  @version 1.0
     *  @see
     *
     *  Copyright (C) 2009 by MOPAS  All right reserved.
     */
%>

<html>
<head>
    <title>Title</title>

    <script id="sap-ui-bootstrap"
            src="https://openui5.hana.ondemand.com/resources/sap-ui-core.js"
            data-sap-ui-theme="sap_fiori_3"
            data-sap-ui-resourceroots='{
			"uiaRes": "../coreframework/uat/uia/"
		}'
            data-sap-ui-compatVersion="edge"
            data-sap-ui-async="true"
            data-sap-ui-frameOptions="trusted"
            data-sap-ui-xx-componentPreload=off
            data-sap-ui-oninit="module:sap/ui/core/ComponentSupport">
    </script>
    <style>
        html, body { height: 100%; }
    </style>
</head>
<body class="sapUiBody sapUiSizeCozy" >

<div data-sap-ui-component
     data-name="uiaRes"
     data-height="100%"
     data-id="container"
     data-settings='{"id" : "uiaRes"}'
     style="height: 100%">
</div>

</body>
</html>