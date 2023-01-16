<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="coreframework.com.cmm.LoginVO" %>
<%@ page import="coreframework.com.cmm.util.CoreUserDetailsHelper" %>
<%
/**
 * @Class Name : CoreLoginPolicyList.java
 * @Description : CoreLoginPolicyList jsp
 * @Modification Information
 * @
 * @  수정일             수정자              수정내용
 * @ ---------    --------    ---------------------------
 *
 *  @author cha
 *  @since 2022-12-02
 *  @version 1.0
 *  @see
 *
 *  Copyright (C) 2009 by coremeathod  All right reserved.
 */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="comUatUap.LoginPolicyList.title" /></title><!-- 로그인정책 목록조회 -->
<link href="<c:url value='/css/coreframework/com/com.css' />" rel="stylesheet" type="text/css">
<link href="<c:url value='/css/coreframework/com/button.css' />" rel="stylesheet" type="text/css">
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.table.Table.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.table.Column.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Label.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.SearchField.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Select.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.core.Item.js' />"></script>
	<script type="text/javaScript" language="javascript" defer="defer">
<!--

function fncCheckAll() {
    var checkField = document.listForm.delYn;
    if(document.listForm.checkAll.checked) {
        if(checkField) {
            if(checkField.length > 1) {
                for(var i=0; i < checkField.length; i++) {
                    checkField[i].checked = true;
                }
            } else {
                checkField.checked = true;
            }
        }
    } else {
        if(checkField) {
            if(checkField.length > 1) {
                for(var j=0; j < checkField.length; j++) {
                    checkField[j].checked = false;
                }
            } else {
                checkField.checked = false;
            }
        }
    }
}

function fncManageChecked() {

    var checkField = document.listForm.delYn;
    var checkId = document.listForm.checkId;
    var returnValue = "";
    var returnBoolean = false;
    var checkCount = 0;

    if(checkField) {
        if(checkField.length > 1) {
            for(var i=0; i<checkField.length; i++) {
                if(checkField[i].checked) {
                	checkCount++;
                    checkField[i].value = checkId[i].value;
                    if(returnValue == "")
                        returnValue = checkField[i].value;
                    else
                        returnValue = returnValue + ";" + checkField[i].value;
                }
            }
            if(checkCount > 0)
                returnBoolean = true;
            else {
                alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkCount"/>");//선택된  로그인정책이 없습니다.
                returnBoolean = false;
            }
        } else {
            if(document.listForm.delYn.checked == false) {
                alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkCount"/>");//선택된  로그인정책이 없습니다.
                returnBoolean = false;
            }
            else {
                returnValue = checkId.value;
                returnBoolean = true;
            }
        }
    } else {
    	alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkField"/>");//조회된 결과가 없습니다.
    }

    document.listForm.emplyrIds.value = returnValue;
    return returnBoolean;
}

function fncSelectLoginPolicyList(pageNo){
    //document.listForm.searchCondition.value = "1";
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/uat/uap/coreLoginPolicyList.cm'/>";
    document.listForm.submit();
}

function fncSelectLoginPolicy(emplyrId) {
    document.listForm.emplyrId.value = emplyrId;
    document.listForm.action = "<c:url value='/uat/uap/coreLoginPolicy.cm'/>";
    document.listForm.submit();
}

function fncInsertCheckId() {

    var checkedCounter = 0;
    var checkIds = document.listForm.delYn;
    var checkIdv = document.listForm.checkId;
    var checkReg = document.listForm.regYn;

    if(checkIds == null) {
        alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkIds"/>");//조회 후 등록하시기 바랍니다.
        return;
    }
    else {

        for(var i=0; i<checkIds.length; i++) {
            if(checkIds[i].checked) {
                if(checkReg[i].value == 'Y' ) {
                    alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkReg"/>");//이미 로그인정책이 등록되어 있습니다.
                    return;
                }
                checkedCounter++;
                document.listForm.emplyrId.value = checkIdv[i].value;
            }
        }

        if(checkedCounter > 1) {
            alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkedCounter.onlyOne"/>");//등록대상 하나만 선택하십시오.
            return false;
        } else if(checkedCounter < 1) {
            alert("<spring:message code="comUatUap.LoginPolicyList.validate.checkedCounter.none"/>");//선택된 등록대상이  없습니다.
            return false;
        }

        return true;
    }
}

function fncAddLoginPolicyInsert() {

    if(fncInsertCheckId()) {
        document.listForm.action = "<c:url value='/uat/uap/coreLoginPolicyRegist.cm'/>";
        document.listForm.submit();
    }
}

function fncLoginPolicyListDelete() {
	if(fncManageChecked()) {
        if(confirm("<spring:message code="comUatUap.LoginPolicyList.validate.delete"/>")) {//삭제하시겠습니까?
            document.listForm.action = "<c:url value='/uat/uap/coreLoginPolicyRemoveList.cm'/>";
            document.listForm.submit();
        }
    }
}

function linkPage(pageNo){
    document.listForm.searchCondition.value = "1";
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/uat/uap/coreLoginPolicyList.cm'/>";
    document.listForm.submit();
}

function press() {

    if (event.keyCode==13) {
    	fncSelectLoginPolicyList('1');
    }
}
-->
</script>


</head>

<body>
	<!--wrap-->
	<div id="wrap">
		<!--container -->
		<div id="container">
			<!--lm-->

			<!--//lm -->
			<!--contents-->
			<div id="contents">
				<!--cont-wrap-->
				<form name="listForm" action="<c:url value='/uat/uap/coreLoginPolicyList.cm'/>" method="post" onSubmit="return false;">
					<input name="searchKeyword" id="searchKeyword" type="hidden" value="<c:out value="${loginPolicyVO.searchKeyword}"/>" size="25" onkeypress="press();"  /><!-- 사용자명검색 -->
					<input type="hidden" name="emplyrId">
					<input type="hidden" name="pageIndex" value="<c:if test="${empty loginPolicyVO.pageIndex }">1</c:if><c:if test="${!empty loginPolicyVO.pageIndex }"><c:out value='${loginPolicyVO.pageIndex}'/></c:if>">

				<div class="cont-wrap">
					<!--header -->
					<!-- /header -->
					<!--conts-->

					<div class="conts">
						<div class="btnTop menu_n">
							<div class="tit-area_n">
								<h3 class="sub-tit" style="font-size: 20px; font-family: none;"><spring:message code="comUatUap.LoginPolicyList.caption" /> / <spring:message code="title.list" /></h3>
							</div>
<%--							<div class="btn-wrap r">--%>
<%--								<a href="#" class="btn btn-blue"><i class="fas fa-save"></i>저장</a>--%>
<%--								<a href="#" class="btn btn-yellow"><i class="fas fa-plus-circle"></i>신규입력</a>--%>
<%--							</div>--%>
						</div>

						<!--search-->
						<div class="hd-sch" style="height:80px">
							<div id="idSearchField" style="float: right"></div>
							<div id="idSelect" style="float: right; margin-right: 10px;"></div>

						</div>
						<!--//search-->
						<div class="cont_tbbox">
							<div class="btn-wrap r">

								<b class="double_data">total: <strong class="seahch_num_txt">${paginationInfo.totalRecordCount}</strong></b>
								<!--<select class="select bgGray" style="width:74px;">
									<option>100</option>
								</select>
								<label class="ml5">개씩 보기</label>//-->
							</div>
							<div class="table_box">
								<div id="idTable"></div>
								<div class="paging-wrap mt20">
									<!-- paging navigation -->
									<c:if test="${!empty loginPolicyVO.pageIndex }">
										<div class="pagination">
											<ul>
												<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/>
											</ul>

										</div>
									</c:if>

								</div>
								<div id="idSelect2" style="margin-top: -40px;float: right;"></div>
							</div>
						</div>
					</div>
					<!--//conts-->

				</div>
				<!--//cont-wrap-->
				</form>
				<!--footer-->

				<!--//footer-->
			</div><!-- //contents-->
		</div><!-- //container -->
	</div>

</body>
<script>


	// Prepare static data set
	const oData = ${loginPolicyListJson};


	const oSearchField = sapMSearchField.clone();
	oSearchField.setPlaceholder('검색');
	oSearchField.setValue('<c:out value="${loginPolicyVO.searchKeyword}"/>');
	oSearchField.attachLiveChange(function (sId, oEvent){
		onSearch(sId, oEvent);
	});
	oSearchField.attachSearch(function(sId, oEvent){
		$('#searchKeyword').val(this.getValue());
		//$('#searchCondition').val(oSelect.getSelectedKey());
		fncSelectLoginPolicyList('1');
	});
	oSearchField.setWidth('15rem');
	//NameSet(oSearchField,'searchKeyword');
	//oSearchField.setProperty("name","searchKeyword");

	const a = $('#searchCondition').val();


	const oItem1 = sapUiCoreItem.clone();
	oItem1.setText('사용자 ID');
	oItem1.setKey('1');
	const oItem2 = sapUiCoreItem.clone();
	oItem2.setText('사용자 명');
	oItem2.setKey('2');

	const oSelect = sapMSelect.clone();
	oSelect.setEnabled(true);
	oSelect.setEditable(true);
	oSelect.setForceSelection(true);
	oSelect.setWidth('120px');
	oSelect.setSelectedKey(1);
	oSelect.attachChange(function(oEvent){
		onSearch(oEvent);
	});
	oSelect.addItem(oItem1);
	oSelect.addItem(oItem2);

	const oTable = sapUiTableTable.clone();
	oTable.setColumnHeaderHeight(50);
	oTable.setVisibleRowCountMode('Auto');
	oTable.setEnableBusyIndicator(true);
	oTable.setSelectionMode("Single");
	oTable.setSelectionBehavior("RowOnly");
	oTable.setMinAutoRowCount(10);

	const oCol1 =   sapUiTableColumn.clone();
	oCol1.setLabel(new sap.m.Label({text: '<spring:message code="comUatUap.LoginPolicyList.userId"/>', design: "Bold"}));
	oCol1.setTemplate(new sap.m.Label({text:"{emplyrId}", width: '100%', textAlign: 'Begin'}));
	oCol1.setSortProperty("emplyrId");



	const oCol2 =  sapUiTableColumn.clone();
	const oLabel2 = sapMLabel.clone();
	oLabel2.setText('<spring:message code="comUatUap.LoginPolicyList.userName"/>');
	oLabel2.setDesign("Bold");

	const oTemplate2 = sapMLabel.clone();
	oTemplate2.setText("{emplyrNm}");
	oTemplate2.setWidth("100%");
	oTemplate2.setTextAlign("Begin");

	oCol2.setLabel(oLabel2);
	oCol2.setTemplate(oTemplate2);

	const oCol3 =  sapUiTableColumn.clone();
	oCol3.setLabel(new sap.m.Label({text: '<spring:message code="comUatUap.LoginPolicyList.ipInfo" />', design: "Bold"}));
	oCol3.setTemplate(new sap.m.Label({text:"{ipInfo}", width: '100%', textAlign: 'Begin'}))


	const oCol4 =  sapUiTableColumn.clone();
	oCol4.setLabel(new sap.m.Label({text: '<spring:message code="comUatUap.LoginPolicyList.restricted" />', design: "Bold"}));
	oCol4.setTemplate(new sap.m.Label({text:"{lmttAt}", width: '100%', textAlign: 'Begin'}))



	const oCol5 =  sapUiTableColumn.clone();
	oCol5.setLabel(new sap.m.Label({text: '<spring:message code="comUatUap.loginPolicyUpdt.regDate" />', design: "Bold"}));
	oCol5.setTemplate(new sap.m.Label({text:"{regDate}", width: '100%', textAlign: 'Begin'}))


	oTable.addColumn(oCol1);
	oTable.addColumn(oCol2);
	oTable.addColumn(oCol3);
	oTable.addColumn(oCol4);
	oTable.addColumn(oCol5);

	// Instantitae JSON Model
	const oModel = new sap.ui.model.json.JSONModel();
	oModel.setData({modelData: oData});
	// Add model to the table
	oTable.setModel(oModel);
	oTable.bindRows("/modelData");

	oTable.placeAt("idTable");

	oTable.attachRowSelectionChange(function (oEvent){
		var s = this.getModel().getProperty("emplyrId", oEvent.getParameters().rowContext);
		var a = oEvent.getParameters().rowContext.getProperty('emplyrId');
		fncSelectLoginPolicy(a);
	});

	oSearchField.placeAt("idSearchField");

	<c:if test="${empty loginPolicyVO.searchCondition }">
	oSelect.setSelectedKey('1');
	//sap.ui.getCore().byId("vSelect").setSelectedKey('1');
	</c:if>
	<c:if test="${!empty loginPolicyVO.searchCondition }">
	oSelect.setSelectedKey('<c:out value='${loginPolicyVO.searchCondition}'/>');
	//sap.ui.getCore().byId("vSelect").setSelectedKey('<c:out value='${loginPolicyVO.searchCondition}'/>');
	</c:if>
	oSelect.setName("searchCondition");
	oSelect.placeAt("idSelect");





	const oItemA = sapUiCoreItem.clone();
	oItemA.setText('10');
	oItemA.setKey('10');
	const oItemB = sapUiCoreItem.clone();
	oItemB.setText('30');
	oItemB.setKey('30');
	const oItemC = sapUiCoreItem.clone();
	oItemC.setText('50');
	oItemC.setKey('50');

	const oSelect2 = sapMSelect.clone();
	oSelect2.setEnabled(true);
	oSelect2.setEditable(true);
	oSelect2.setForceSelection(false);
	oSelect2.setWidth('100px');
	oSelect2.setSelectedKey(10);
	oSelect2.attachChange(function(oEvent){
		onSearch(oEvent);
	});
	oSelect2.addItem(oItemA);
	oSelect2.addItem(oItemB);
	oSelect2.addItem(oItemC);

	oSelect2.placeAt("idSelect2");

	var onSearch = function (oEvent) {
		// add filter for search
		var aFilters = [];
		var sQuery;
		if(oEvent.sId === "change") {sQuery = oSearchField.getValue()}
		if(oEvent.sId === "liveChange") {sQuery = oEvent.getSource().getValue();}

		if (sQuery && sQuery.length > 0) {
			var searchCondition = oSelect.getSelectedKey();
			if(searchCondition === "1")  searchCondition = "emplyrId";
			if(searchCondition === "2")  searchCondition = "emplyrNm";
			var filterOperator = sap.ui.model.FilterOperator.Contains;
			var filter = new sap.ui.model.Filter(searchCondition, filterOperator, sQuery);
			aFilters.push(filter);
		}

		// update list binding
		var oList = oTable;
		var oBinding = oList.getBinding("rows");
		oBinding.filter(aFilters, "Application");
	}
</script>
</html>
