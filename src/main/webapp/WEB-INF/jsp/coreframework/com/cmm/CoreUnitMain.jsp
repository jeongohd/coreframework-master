<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>coreFramework</title>


</head>
<script id="sap-ui-bootstrap"
		src="https://sdk.openui5.org/resources/sap-ui-core.js"
		data-sap-ui-theme="sap_fiori_3"
		data-sap-ui-libs="sap.m, sap.ui.commons, sap.f, sap.tnt, sap.ui.core"
		>
</script>
<%--<script>--%>
<%--	 oToolPage = new sap.tnt.ToolPage({--%>

<%--	});--%>

<%--	 const oShellBar = new sap.f.ShellBar({--%>
<%--		 title:"coreframework",--%>
<%--		 showMenuButton:true,--%>
<%--		 homeIcon:"./resources/sap/ui/documentation/sdk/images/logo_ui5.png",--%>
<%--		 menuButtonPressed:function(){--%>
<%--			 &lt;%&ndash;var iframe = document.createElement('iframe');&ndash;%&gt;--%>
<%--			 &lt;%&ndash;iframe.setAttribute('src', '${pageContext.request.contextPath}/CoreTop.cm');&ndash;%&gt;--%>
<%--			 &lt;%&ndash;document.getElementById('_content').appendChild(iframe);&ndash;%&gt;--%>


<%--			 oToolPage.setSideExpanded(!oToolPage.getSideExpanded());--%>

<%--		 },--%>
<%--		 showNotifications:true,--%>
<%--		 showProductSwitcher:true,--%>
<%--		 notificationsNumber:"2"--%>
<%--	 });--%>




<%--	 const oSideNavigation = new sap.tnt.SideNavigation({--%>
<%--		 expanded:true,--%>
<%--		 selectedKey:"root1",--%>
<%--		 itemSelect:function(){--%>

<%--		 }--%>
<%--	 })--%>

<%--	 const oNavigationList = new sap.tnt.NavigationList({--%>
<%--		 expanded:true,--%>
<%--		 selectedKey:"root1"--%>
<%--	 })--%>



<%--	 const oNavigationListItem = new sap.tnt.NavigationListItem({--%>
<%--		 text:"Root Item",--%>
<%--		 icon:"sap-icon://employee",--%>
<%--		 enabled:true,--%>
<%--		 expanded:true,--%>

<%--		 key:"root1"--%>
<%--	 })--%>


<%--	 oNavigationList.addItem(oNavigationListItem);--%>
<%--	 oSideNavigation.setItem(oNavigationList);--%>

<%--	 oToolPage.setHeader(oShellBar);--%>
<%--	 oToolPage.setSideContent(oSideNavigation);--%>
<%--	 oToolPage.setContent();--%>
<%--	 oToolPage.placeAt("_top");--%>
<%--</script>--%>

<%--<div >--%>
<%--	<div id="_top"></div>--%>
<%--	<div id="_side"></div>--%>
<%--	<div id="_content"></div>--%>
<%--	</div>--%>
<%--</div>--%>

<frameset frameborder="0" framespacing="0" rows="75, *, 45">
	<frame name="_top" src="${pageContext.request.contextPath}/CoreTop.cm" scrolling="no" title="헤더" >
	<frameset frameborder="0" framespacing="0" cols="15%, 85%">
		<frame name="_left" src="${pageContext.request.contextPath}/coreLeft.cm" scrolling="yes" title="메뉴페이지">
		<frame name="_content" src="${pageContext.request.contextPath}/CoreContent.cm" title="메인페이지">
	</frameset>
	<frame name="_bottom" src="${pageContext.request.contextPath}/CoreBottom.cm" scrolling="no" title="푸터">
</frameset>



