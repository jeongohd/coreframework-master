
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<c:set var="pageTitle"><spring:message code="comCopSecRmt.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.update" /></title><!-- 롤관리 등록 -->
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/coreframework/com/com.css' />">

<script type="text/javascript" src="<c:url value="/validator.cm"/>"></script>
<validator:javascript formName="role" staticJavascript="false" xhtml="true" cdata="false"/>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Input.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Select.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.core.Item.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Button.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.MessageBox.js' />"></script>

<script type="text/javaScript" language="javascript" defer="defer">

	function fncSelectRoleList() {
		document.role12.action = "<c:url value='/sec/rmt/CoreRoleList.cm'/>";
		document.role12.submit();
	}

	function fncRoleUpdate() {
		if(confirm("<spring:message code="common.save.msg" />")){ //저장하시겠습니까?
			// if(!validateRoleManage(form)){
			// 	return false;
			// }else{
			// var varFrom = document.getElementById("role");
			// varFrom.submit();
			document.role12.submit();
			// }
		}

	}

	function fncRoleDelete() {
		document.role12.action = "<c:url value='/sec/rmt/CoreRoleDelete.cm'/>";
		if(confirm("<spring:message code="common.delete.msg" />")){	//삭제하시겠습니까?
			document.role12.submit();
		}else{
			return false;
		}
	}
</script>
</head>
<body>

<!--wrap-->
<div id="wrap">
	<!--container -->
	<div id="container">
		<!--contents-->
		<form:form name="role12" method="post" action="${pageContext.request.contextPath}/sec/rmt/CoreRoleUpdate.cm" onSubmit="return false;">

<%--		<input type="hidden" name="roleCode" >--%>
			<%-- 함수에 roleCode를 활용하지 않아서 넘길 필요 없음. hidden으로 값을 넘기면 오류.--%>

			<!-- 검색조건 유지 -->
			<input type="hidden" name="searchCondition" value="<c:out value='${roleManageVO.searchCondition}'/>" >
			<input type="hidden" name="searchKeyword" value="<c:out value='${roleManageVO.searchKeyword}'/>" >
			<input type="hidden" name="pageIndex" value="<c:out value='${roleManageVO.pageIndex}'/>" >
			<div id="contents">
				<div class="cont-wrap">
					<!--header -->
					<!-- /header -->
					<!--conts-->

					<div class="conts">
						<div class="btnTop menu_n">
							<div class="tit-area_n">
								<h3 class="sub-tit" style="font-size: 20px; font-family: none;"><spring:message code="comCopSecRmt.title" /> / <spring:message code="title.update" /></h3>
							</div>
						</div>
						<div class="cont_tbbox">
							<div class="table_box">
								<table class="view_n">
									<colgroup>
										<col style="width: 100px;">
										<col>
										<col style="width: 100px;">
										<col>
									</colgroup>
									<tbody>
									<tr>
										<th><spring:message code="comCopSecRam.list.rollId"/><i class="cRed">*</i></th>
										<td>
											<div id="roleCode"></div>
										</td>
										<th><spring:message code="comCopSecRam.list.rollNm"/><i class="cRed">*</i></th>
										<td>
											<div id="roleNm"></div>
										</td>
									</tr>
									<tr>
										<th><spring:message code="comCopSecRam.regist.rollPtn"/><i class="cRed">*</i></th>
										<td>
											<div id="rolePtn"></div>
										</td>
										<th><spring:message code="comCopSecRam.list.rollDc"/><i class="cRed">*</i></th>
										<td>
											<div id="roleDc"></div>
										</td>
									</tr>
									<tr>
										<th><spring:message code="comCopSecRam.list.rollType"/></th>
										<td>
											<div id="roleType"></div>
										</td>
										<th><spring:message code="comCopSecRam.list.rollSort"/></th>
										<td>
											<div id="roleSort"></div>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="btnTop">

						<div class="btn-wrap r" id="roleListButton" style="float: right;">
								<%--								<a href="#" class="btn btn-green"><i class="fas fa-save"></i>저장</a><a href="#none" class="btn"><i class="fas fa-list-alt"></i>목록</a><a href="#none" class="btn btn-gray"><i class="fas fa-times-circle"></i>취소</a><a href="#none" class="btn btn-pink"><i class="fas fa-trash-alt"></i>삭제</a>--%>
						</div>
						<div class="btn-wrap r" id="roleSaveButton" style="float: right; margin-right: 10px">
						</div>
						<div class="btn-wrap r" id="roleDeleteButton" style="float: right; margin-right: 10px">
						</div>

					</div>
				</div>
			</div>
		</form:form>
	</div><!-- //contents-->
</div><!-- //container -->
</body>
<script>

	/* --------------------------- 버튼 --------------------------- */
	const oButton = sapMButton.clone();
	oButton.setText('<spring:message code="button.list" />');
	oButton.attachPress(function(oEvent){
		fncSelectRoleList();
	});
	oButton.placeAt('roleListButton');

	const oButton2 = sapMButton.clone();
	oButton2.setText('<spring:message code="button.save" />');
	oButton2.setType('Accept');
	oButton2.attachPress(function(oEvent){
		fncRoleUpdate();
	});
	oButton2.placeAt('roleSaveButton');

	const oButton3 = sapMButton.clone();
	oButton3.setText('<spring:message code="button.delete" />');
	oButton3.setType('Attention');
	oButton3.attachPress(function(oEvent){
		fncRoleDelete();
	});
	oButton3.placeAt('roleDeleteButton');

	/* --------------------------- input 텍스트 --------------------------- */

	const oInput1 = sapMinput.clone();
	oInput1.setValue('<c:out value='${role.roleCode}'/>');
	oInput1.setEnabled(true);
	oInput1.setEditable(false);
	oInput1.setRequired(true);
	NameSet(oInput1,"roleCode");
	oInput1.placeAt('roleCode'); /* BODY안의 DIV ID와 동일하게 적용해야 함. */

	const oInput2 = sapMinput.clone();
	oInput2.setValue('<c:out value='${role.roleNm}'/>');
	oInput2.setEnabled(true);
	oInput2.setEditable(true);
	oInput2.setRequired(true);
	NameSet(oInput2,"roleNm");
	oInput2.placeAt('roleNm');

	const oInput3 = sapMinput.clone();
	oInput3.setValue('<c:out value='${role.rolePtn}'/>');
	oInput3.setEnabled(true);
	oInput3.setEditable(true);
	oInput3.setRequired(true);
	//oInput3.setProperty("name", "ipInfo");
	NameSet(oInput3, "rolePtn");
	oInput3.placeAt('rolePtn');

	const oInput4 = sapMinput.clone();
	oInput4.setValue('<c:out value='${role.roleDc}'/>');
	oInput4.setEnabled(true);
	oInput4.setEditable(true);
	oInput4.setRequired(true);
	NameSet(oInput4, "roleDc");
	oInput4.placeAt('roleDc');

	const oInput5 = sapMinput.clone();
	oInput5.setValue('<c:out value='${role.roleSort}'/>');
	oInput5.setEnabled(true);
	oInput5.setEditable(true);
	oInput5.setRequired(true);
	NameSet(oInput5, "roleSort");
	oInput5.placeAt('roleSort');

	/* --------------------------- select box --------------------------- */

	const oItem1 = sapUiCoreItem.clone();
	oItem1.setText('METHOD');
	oItem1.setKey('METHOD');
	const oItem2 = sapUiCoreItem.clone();
	oItem2.setText('POINTCUT');
	oItem2.setKey('POINTCUT');
	const oItem3 = sapUiCoreItem.clone();
	oItem3.setText('URL');
	oItem3.setKey('URL');

	const oSelect = sapMSelect.clone();
	oSelect.setEnabled(true);
	oSelect.setEditable(true);
	oSelect.setForceSelection(true);
	oSelect.setWidth('200px');
	oSelect.setSelectedKey('<c:out value='${role.roleTyp}'/>');
	// oSelect.attachChange(function(oEvent){
	// 	onSearch(oEvent);
	// });
	oSelect.addItem(oItem1);
	oSelect.addItem(oItem2);
	oSelect.addItem(oItem3);

	NameSet(oSelect,"roleTyp");
	//oInput1.setProperty("name","lmttAt");

	oSelect.placeAt('roleType');
</script>

</html>

