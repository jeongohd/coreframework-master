<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>eGovFrame 공통 컴포넌트</title>
<link href="<c:url value='/css/coreframework/com/cmm/main.css' />" rel="stylesheet" type="text/css">
<style type="text/css">
link { color: #666666; text-decoration: none; }
link:hover { color: #000000; text-decoration: none; }
</style>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.js' />"></script>
<script>

	const oData = ${resultListJson};
	const oData0 = [];
	const oData10 = [];
	const oData20 = [];
	const oData30 = [];
	const oData40 = [];
	const oData50 = [];
	const oData60 = [];
	const oData70 = [];
	const oData80 = [];
	const oData90 = [];
	const oData100 = [];

	$.each(oData, function(idx, row){
		if(oData[idx].gid === 0){
			oData0.push(oData[idx]);
		}else if(oData[idx].gid === 10){
			oData10.push(oData[idx]);
		}else if(oData[idx].gid === 20){
			oData20.push(oData[idx]);
		}else if(oData[idx].gid === 30){
			oData30.push(oData[idx]);
		}else if(oData[idx].gid === 40){
			oData40.push(oData[idx]);
		}else if(oData[idx].gid === 50){
			oData50.push(oData[idx]);
		}else if(oData[idx].gid === 60){
			oData60.push(oData[idx]);
		}else if(oData[idx].gid === 70){
			oData70.push(oData[idx]);
		}else if(oData[idx].gid === 80){
			oData80.push(oData[idx]);
		}else if(oData[idx].gid === 90){
			oData90.push(oData[idx]);
		}else if(oData[idx].gid === 100){
			oData100.push(oData[idx]);
		}
	});

	//console.log(oData10);
	const oToolPage = new sap.tnt.ToolPage({
		busy: true,
	});
	const oSideNavigation = new sap.tnt.SideNavigation({

	});

	const oNavigationList = new sap.tnt.NavigationList({
		width: '225px',
		expanded: true
	});



	const oNavigationListItem = new sap.tnt.NavigationListItem({

				text: "{name}",
				href:"{listUrl}",
				target: "_content",
				key: "{order}"

	});


	const oNavigationListItem1 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.uat.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: true,
		key: "uat"
	});

	const oNavigationListItem2 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.sec.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "sec"
	});

	const oNavigationListItem3 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.sts.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "sts"
	});

	const oNavigationListItem4 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.cop.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "cop"
	});

	const oNavigationListItem5 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.uss.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "uss"
	});

	const oNavigationListItem6 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.sym.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "sym"
	});

	const oNavigationListItem7 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.ssi.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "ssi"
	});

	const oNavigationListItem8 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.dam.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "dam"
	});

	const oNavigationListItem9 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.com.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "com"
	});

	const oNavigationListItem10 = new sap.tnt.NavigationListItem({
		text: "<spring:message code="comCmm.ext.title"/>",
		icon: "sap-icon://employee",
		enabled: true,
		expanded: false,
		key: "ext"
	});

	oSideNavigation.setItem(oNavigationList);

	oToolPage.setSideContent(oSideNavigation);

	const oModel = new sap.ui.model.json.JSONModel();

	oModel.setData({modelData0: oData0});
	oNavigationList.setModel(oModel);
	oNavigationList.bindAggregation("items", "/modelData0", oNavigationListItem) ;

	oModel.setData({modelData1: oData10},{modelData0: oData0});
	oNavigationList.setModel(oModel);
	oNavigationListItem1.bindAggregation("items", "/modelData1", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem1);

	oModel.setData({modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	 oNavigationListItem2.bindAggregation("items", "/modelData2", oNavigationListItem) ;
	 oNavigationList.addItem(oNavigationListItem2);

	oModel.setData({modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem3.bindAggregation("items", "/modelData3", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem3);

	oModel.setData({modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem4.bindAggregation("items", "/modelData4", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem4);

	oModel.setData({modelData5: oData50}, {modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem5.bindAggregation("items", "/modelData5", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem5);

	oModel.setData({modelData6: oData60}, {modelData5: oData50}, {modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem6.bindAggregation("items", "/modelData6", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem6);

	oModel.setData({modelData7: oData70}, {modelData6: oData60}, {modelData5: oData50}, {modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem7.bindAggregation("items", "/modelData7", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem7);

	oModel.setData({modelData8: oData80}, {modelData7: oData70}, {modelData6: oData60}, {modelData5: oData50}, {modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem8.bindAggregation("items", "/modelData8", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem8);

	oModel.setData({modelData9: oData90},{modelData8: oData80},{modelData7: oData70}, {modelData6: oData60}, {modelData5: oData50}, {modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem9.bindAggregation("items", "/modelData9", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem9);

	oModel.setData({modelData10: oData100}, {modelData9: oData90}, {modelData8: oData80},{modelData7: oData70}, {modelData6: oData60}, {modelData5: oData50}, {modelData4: oData40}, {modelData3: oData30},{modelData2: oData20},{modelData1: oData10},{modelData0: oData0});	//기존 data 당연히 삭제 하면 안됨
	oNavigationList.setModel(oModel);
	oNavigationListItem10.bindAggregation("items", "/modelData10", oNavigationListItem) ;
	oNavigationList.addItem(oNavigationListItem10);

	oToolPage.placeAt("content");
	oToolPage.setBusy(false);

</script>
	<script>
		var jsonData = ${resultListJson};
		//console.log(jsonData);
	</script>

</head>
<body>
<div id="content"></div>
<%--<div id="lnb">--%>
<%--<c:set var="isMai" value="false"/>--%>
<%--<c:set var="isUat" value="false"/>--%>
<%--<c:set var="isSec" value="false"/>--%>
<%--<c:set var="isSts" value="false"/>--%>
<%--<c:set var="isCop" value="false"/>--%>
<%--<c:set var="isUss" value="false"/>--%>
<%--<c:set var="isSym" value="false"/>--%>
<%--<c:set var="isSsi" value="false"/>--%>
<%--<c:set var="isDam" value="false"/>--%>
<%--<c:set var="isCom" value="false"/>--%>
<%--<c:set var="isExt" value="false"/>--%>
<%--<ul class="lnb_title">--%>
<%--	<c:forEach var="result" items="${resultList}" varStatus="status">--%>
<%--	--%>
<%--		<c:if test="${isMai == 'false' && result.gid == '0'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.mai.title"/></strong></strong><!-- 포털(예제) 메인화면 -->--%>
<%--			</li>--%>
<%--			<c:set var="isMai" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isUat == 'false' && result.gid == '10'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.uat.title"/></strong></strong><!-- 사용자디렉토리/통합인증 -->--%>
<%--			</li>--%>
<%--			<c:set var="isUat" value="true"/>			--%>
<%--		</c:if>--%>
<%--		--%>
<%--		<c:if test="${isSec == 'false' && result.gid == '20'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.sec.title"/></strong></strong><!-- 보안 -->--%>
<%--			</li>--%>
<%--			<c:set var="isSec" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isSts == 'false' && result.gid == '30'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.sts.title"/></strong></strong><!-- 통계/리포팅 -->--%>
<%--			</li>--%>
<%--			<c:set var="isSts" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isCop == 'false' && result.gid == '40'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.cop.title"/></strong></strong><!-- 협업 -->--%>
<%--			</li>--%>
<%--			<c:set var="isCop" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isUss == 'false' && result.gid == '50'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.uss.title"/></strong></strong><!-- 사용자지원 -->--%>
<%--			</li>--%>
<%--			<c:set var="isUss" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isSym == 'false' && result.gid == '60'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.sym.title"/></strong></strong><!-- 시스템관리 -->--%>
<%--			</li>--%>
<%--			<c:set var="isSym" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isSsi == 'false' && result.gid == '70'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.ssi.title"/></strong></strong><!-- 시스템/서비스연계  -->--%>
<%--			</li>--%>
<%--			<c:set var="isSsi" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isDam == 'false' && result.gid == '80'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.dam.title"/></strong></strong><!-- 디지털 자산 관리 -->--%>
<%--			</li>--%>
<%--			<c:set var="isDam" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isCom == 'false' && result.gid == '90'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.com.title"/></strong></strong> <!-- 요소기술 -->--%>
<%--			</li>--%>
<%--			<c:set var="isCom" value="true"/>--%>
<%--		</c:if>--%>
<%--		<c:if test="${isExt == 'false' && result.gid == '100'}">--%>
<%--			<li>--%>
<%--				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.ext.title"/></strong></strong><!-- 외부 추가 컴포넌트 -->--%>
<%--			</li>--%>
<%--			<c:set var="isExt" value="true"/>--%>
<%--		</c:if>--%>
<%--	--%>
<%--		<c:set var="componentMsgKey">comCmm.left.${result.order}</c:set>--%>
<%--		<ul class="2depth">--%>
<%--		<li><a href="${pageContext.request.contextPath}<c:out value="${result.listUrl}"/>" target="_content" class="link"> <c:out value="${result.order}"/>. <spring:message code="${componentMsgKey}"/><!-- <c:out value="${result.name}"/> --></a></li>--%>
<%--		</ul>--%>
<%--	</c:forEach>--%>
<%--</ul>--%>

</body>
</html>
