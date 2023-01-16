<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="coreframework.com.cmm.util.CoreUserDetailsHelper" %>
<script language="text/javascript" src="<c:url value='/js/coreframework/com/main.js' />"></script>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/coreframework/com/main_portal.css' />">
<style type="text/css">
.headTitleClass .ui-widget-header
{
       background-color: #F9A7AE;
       background-image: none;
       color: Black;
}
</style>
<link type="text/css" rel="stylesheet" href="/css/coreframework/com/cmm/jqueryui.css">
<script src="<c:url value='/js/coreframework/com/cmm/jquery.js' />"></script>
<script src="<c:url value='/js/coreframework/com/cmm/jqueryui.js' />"></script>
<script type="text/javascript">

	var $dialog;

    $(document).ready(function () {
		// To Do
    });
    
    function fn_openMenuPopup(menuNo) {
    	var pagetitle = "<spring:message code="comCmm.unitContent.20"/>"; // 비밀번호 유효기간 만료 안내
    	var page = "${pageContext.request.contextPath}/sym/mnu/mpm/CoreMainMenuLeft.cm?vStartP="+menuNo;
    	
        $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
				        .html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
				        .dialog({
				        	autoOpen: false,
				            modal: true,
				            width: 600,
				            height: 550,
				            title: pagetitle,
				            dialogClass: 'headTitleClass'
				    	});
    	
    	$dialog.dialog('open');
    }
    
</script>
<script type="text/javascript">
	function fn_main_headPageMove(menuNo, url){
		document.selectOne.menuNo.value=menuNo;
		document.selectOne.vStartP.value=menuNo;
		//document.selectOne.chkURL.value=url;
	    //document.selectOne.action = "<c:url value='/sym/mnu/mpm/CoreMainMenuIndex.cm'/>";
	    document.selectOne.action = "<c:url value='/sym/mnu/mpm/CoreMainMenuLeft.cm'/>";
	    document.selectOne.target = "frmSubMemnuList";
	    document.selectOne.submit();
	    //fn_openMenuPopup(menuNo);
	}

	function actionLogout()
	{
		document.selectOne.action = "<c:url value='/uat/uia/actionLogout.cm'/>";
		document.selectOne.submit();
		//top.cmcument.location.href = "<c:url value='/j_spring_security_logout'/>";
	}
</script>

<form name="selectOne">
<input name="menuNo" type="hidden" />
<input name="chkURL" type="hidden" />
<input name="vStartP" type="hidden" />
    <div id="gnb">
    <div id="top_logo"><a href="<c:url value='/sym/mnu/mpm/CoreMainMenuHome.cm' />" target=_top><img src="<c:url value='/images/coreframework/com/cmm/main/logo_01.gif' />" alt="egovframe" /></a></div>
    <div id="use_descri">
            <ul>
                <li><spring:message code="comSymMnuMpm.head.head"/></li><!-- 공통서비스 테스트 사이트 -->
                <li><a href="javascript:actionLogout()"><img src="<c:url value='/images/coreframework/com/cmm/main/logout_btn.gif' />" alt="로그아웃" /></a></li>
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

</form>
