<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/coreframework/com/main_portal.css' />">
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>HeadMenu</title>
<script type="text/javascript">
var getContextPath = "${pageContext.request.contextPath}";
</script>
<script language="javascript" src="<c:url value='/js/coreframework/com/main.js' />"></script>
<script type="text/javascript">
	function fn_main_headPageMove(menuNo, url){
		document.selectOne.vStartP.value=menuNo;
		document.selectOne.chkURL.value=url;
	    document.selectOne.action = "<c:url value='/sym/mnu/mpm/CoreMainMenuLeft.cm'/>";
	    document.selectOne.target = "main_left";
	    document.selectOne.submit();
 	    document.selectOne.action = "<c:url value='/sym/mnu/mpm/CoreMainMenuRight.cm'/>";
	    document.selectOne.target = "main_right";
	    document.selectOne.submit();
	}

	function actionLogout()
	{
		document.selectOne.action = "<c:url value='/uat/uia/actionLogout.cm'/>";
		document.selectOne.target = "_top";
		document.selectOne.submit();
		//top.cmcument.location.href = "<c:url value='/j_spring_security_logout'/>";
	}
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight= "0">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/coreframework/com/common.css' />" />
<link rel="stylesheet" href="<c:url value='/css/coreframework/com/com.css' />" type="text/css" />
<form name="selectOne">
<input name="vStartP" type="hidden" />
<input name="chkURL" type="hidden" />
</form>

    <div id="gnb">
    <div id="top_logo"><a href="<c:url value='/sym/mnu/mpm/CoreMainMenuHome.cm' />" target=_top><img src="<c:url value='/images/coreframework/com/cmm/main/logo_01.gif' />" alt="egovframe" /></a></div>
     <div id="use_descri">
            <ul>
                <li>??????????????? ????????? ?????????</li>
                 <li><a href="javascript:actionLogout()"><img src="<c:url value='/images/coreframework/com/cmm/main/logout_btn.gif' />" alt="????????????" /></a></li>
            </ul>
    </div>
    </div>
    <div id="new_topnavi">
        <ul>
			<li><a href="<c:url value='/sym/mnu/mpm/CoreMainMenuHome.cm' />" target="_top">HOME</a></li>
			<c:forEach var="result" items="${list_headmenu}" varStatus="status">
			   <li class="gap"> l </li>
			   <li><a href="javascript:fn_main_headPageMove('<c:out value="${result.menuNo}"/>','<c:out value="${result.chkURL}"/>')"><c:out value="${result.menuNm}"/></a></li>
			</c:forEach>
        </ul>
    </div>


</body>
</html>
