<%
/**
 * @Class Name : CoreRoleManage.java
 * @Description : CoreRoleManage jsp
 * @Modification Information
 * @
 * @  수정일                    수정자                수정내용
 * @ ---------     --------    ---------------------------
 * @ 2009.02.01    lee.m.j     최초 생성
 *   2016.06.13    장동한        표준프레임워크 v3.6 개선
 *
 *  @author lee.m.j
 *  @since 2009.03.21
 *  @version 1.0
 *  @see
 *
 */
%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle"><spring:message code="comCopSecRmt.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.list" /></title><!-- 롤관리 목록 -->
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/coreframework/com/com.css' />">
<link href="<c:url value='/css/coreframework/com/button.css' />" rel="stylesheet" type="text/css">
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.table.Table.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.table.Column.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Label.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.SearchField.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Select.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.core.Item.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Button.js' />"></script>

	<script type="text/javaScript" language="javascript" defer="defer">
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
                alert("<spring:message code="comCopSecRmt.validate.groupSelect"/>"); //선택된  롤이 없습니다.
                returnBoolean = false;
            }
        } else {
            if(document.listForm.delYn.checked == false) {
                alert("<spring:message code="comCopSecRmt.validate.groupSelect"/>"); //선택된  롤이 없습니다.
                returnBoolean = false;
            }
            else {
                returnValue = checkId.value;
                returnBoolean = true;
            }
        }
    } else {
    	alert("<spring:message code="comCopSecRmt.validate.groupSelectResult"/>"); //조회된 결과가 없습니다.
    }

    document.listForm.roleCodes.value = returnValue;
    return returnBoolean;
}

function fncSelectRoleList(pageNo, pageUnit){
    // document.listForm.searchCondition.value = "1";
    document.listForm.pageIndex.value = pageNo;
	document.listForm.pageUnit.value = pageUnit;

    document.listForm.action = "<c:url value='/sec/rmt/CoreRoleList.cm'/>";
    document.listForm.submit();
}

// 상세조회
function fncSelectRole(roleCode, searchKeyword) {
    document.listForm.roleCode.value = roleCode;
    document.listForm.searchKeyword.value = searchKeyword;
	console.log("roleCode : " + roleCode);
	console.log("searchKeyword : " + searchKeyword);
    document.listForm.action = "<c:url value='/sec/rmt/CoreRole.cm'/>";
    document.listForm.submit();
}

function fncAddRoleInsert() {
	document.listForm.action = "<c:url value='/sec/rmt/CoreRoleInsertView.cm'/>";
	document.listForm.submit();
}

function fncRoleListDelete() {
	if(fncManageChecked()) {
        if(confirm("삭제하시겠습니까?")) { //삭제하시겠습니까?
            document.listForm.action = "<c:url value='/sec/rmt/CoreRoleListDelete.cm'/>";
            document.listForm.submit();
        }
    }
}

function fncAddRoleView() {
    document.listForm.action = "<c:url value='/sec/rmt/CoreRoleUpdate.cm'/>";
    document.listForm.submit();
}

function linkPage(pageNo){
    document.listForm.searchCondition.value = "1";
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/sec/rmt/CoreRoleList.cm'/>";
    document.listForm.submit();
}

function press() {

    if (event.keyCode==13) {
    	fncSelectRoleList('1');
    }
}
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
			<form name="listForm" action="<c:url value='/sec/rmt/CoreRoleList.cm'/>" method="post" onSubmit="return false;">
				<input name="searchKeyword" id="searchKeyword" type="hidden" value="<c:out value="${roleManageVO.searchKeyword}"/>" size="25" onkeypress="press();"  /><!-- 롤 명검색 -->
<%--				<input type="hidden" name="searchCondition" value="<c:out value="${roleManageVO.searchKeyword}"/>" />--%>
				<input type="hidden" name="pageIndex" value="<c:if test="${empty roleManageVO.pageIndex }">1</c:if><c:if test="${!empty roleManageVO.pageIndex }"><c:out value='${roleManageVO.pageIndex}'/></c:if>">
				<input type="hidden" name="pageUnit" id="pageUnit" value="<c:if test="${!empty roleManageVO.pageUnit }"><c:out value='${roleManageVO.pageUnit}'/></c:if>">
				<input type="hidden" name="roleCode">


				<div class="cont-wrap">
					<!--header -->
					<!-- /header -->
					<!--conts-->

					<div class="conts">
						<div class="btnTop menu_n">
							<div class="tit-area_n">
								<h3 class="sub-tit" style="font-size: 20px; font-family: none;"><spring:message code="comCopSecRmt.title" /> / <spring:message code="title.list" /></h3>
							</div>
<%--							<div class="btn-wrap r">--%>
<%--								<a href="#" class="btn btn-blue"><i class="fas fa-save"></i>저장</a>--%>
<%--								<a href="#" class="btn btn-yellow"><i class="fas fa-plus-circle"></i>신규입력</a>--%>
<%--							</div>--%>
						</div>

						<!--search-->
						<div class="hd-sch" style="height:80px">
							<div id="roleSearchKeyword" style="float: right"></div>
							<div id="roleSearchCondition" style="float: right; margin-right: 10px;"></div>
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
									<c:if test="${!empty roleManageVO.pageIndex }">
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
				<div class="btnTop">
					<div class="btn-wrap r" id="idInsertButton" style="float: right; margin-right: 10px"></div>
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


	// 컨트롤러에서 넘긴 json을 변수에 담고
	const oData = ${roleListJson};
	console.log(oData);

	/*----------------------------------- button -----------------------------------*/

	const oButton = sapMButton.clone();
	oButton.setText('<spring:message code="button.create" />');
	oButton.attachPress(function(oEvent){ /* btn.attachPress() 버튼 클릭시 */
		fncAddRoleInsert();
	});
	oButton.placeAt('idInsertButton');


	/*----------------------------------- select box -----------------------------------*/

	const oItem1 = sapUiCoreItem.clone();
	oItem1.setText('롤 ID');
	oItem1.setKey('1');
	const oItem2 = sapUiCoreItem.clone();
	oItem2.setText('롤 명');
	oItem2.setKey('2');
	const oItem3 = sapUiCoreItem.clone();
	oItem3.setText('롤 설명');
	oItem3.setKey('3');

	const oSelect = sapMSelect.clone();
	oSelect.setEnabled(true);
	oSelect.setEditable(true);
	oSelect.setForceSelection(true);
	oSelect.setWidth('120px');
	oSelect.setSelectedKey(1); // 선택된 셀렉트 박스
	oSelect.attachChange(function(oEvent){
		onSearch(oEvent);
	});

	<c:if test="${empty roleManageVO.searchCondition }">
	oSelect.setSelectedKey('1');
	//sap.ui.getCore().byId("vSelect").setSelectedKey('1');
	</c:if>
	<c:if test="${!empty roleManageVO.searchCondition }">
	oSelect.setSelectedKey('<c:out value='${roleManageVO.searchCondition}'/>');
	//sap.ui.getCore().byId("vSelect").setSelectedKey('<c:out value='${roleManageVO.searchCondition}'/>');
	</c:if>

	oSelect.setName("searchCondition"); // 필드에 맞게 설정.
	oSelect.placeAt("roleSearchCondition");
	oSelect.addItem(oItem1);
	oSelect.addItem(oItem2);
	oSelect.addItem(oItem3);

	/*----------------------------------- search -----------------------------------*/

	// sap.m.searchfield 검색
	const oSearchField = sapMSearchField.clone();
	oSearchField.setPlaceholder('검색');
	oSearchField.setValue('<c:out value="${roleManageVO.searchKeyword}"/>');

	// attachLiveChange 메소드 (실시간 검색)
	oSearchField.attachLiveChange(function (sId, oEvent){
		onSearch(sId, oEvent);
	});


	// attachSearch 메소드 (일반 검색??)
	oSearchField.attachSearch(function(sId, oEvent){
		$('#searchKeyword').val(this.getValue());
		// $('#searchCondition').val(oSelect.getSelectedKey());
		fncSelectRoleList('1');
	});
	oSearchField.setWidth('15rem');
	oSearchField.placeAt("roleSearchKeyword");
	//NameSet(oSearchField,'searchKeyword');
	//oSearchField.setProperty("name","searchKeyword");

	const a = $('#searchCondition').val();


	/*----------------------------------- Set values in table -----------------------------------*/

	const oTable = sapUiTableTable.clone();
	oTable.setColumnHeaderHeight(50);
	oTable.setVisibleRowCountMode('Auto');
	oTable.setEnableBusyIndicator(true);
	oTable.setSelectionMode("Single");
	oTable.setSelectionBehavior("RowOnly");
	oTable.setMinAutoRowCount(10);


	// 컬럼 추가 다른 방식.

	<%--const oCol1 =   sapUiTableColumn.clone();--%>
	<%--oCol1.setLabel(new sap.m.Label({text: '<spring:message code="comUatUap.LoginPolicyList.userId"/>', design: "Bold"}));--%>
	<%--oCol1.setTemplate(new sap.m.Label({text:"{emplyrId}", width: '100%', textAlign: 'Begin'}));--%>
	<%--oCol1.setSortProperty("emplyrId");--%>

	<%--const oCol3 =  sapUiTableColumn.clone();--%>
	<%--oCol3.setLabel(new sap.m.Label({text: '<spring:message code="comUatUap.LoginPolicyList.ipInfo" />', design: "Bold"}));--%>
	<%--oCol3.setTemplate(new sap.m.Label({text:"{ipInfo}", width: '100%', textAlign: 'Begin'}))--%>

	const oCol1 =  sapUiTableColumn.clone();
	const oLabel1 = sapMLabel.clone();
	oLabel1.setText('<spring:message code="comCopSecRam.list.rollId"/>');
	oLabel1.setDesign("Bold");
	const oTemplate1 = sapMLabel.clone();
	oTemplate1.setText("{roleCode}");
	oTemplate1.setWidth("100%");
	oTemplate1.setTextAlign("Begin");
	oCol1.setLabel(oLabel1);
	oCol1.setTemplate(oTemplate1);
	oCol1.setSortProperty("roleCode");


	const oCol2 =  sapUiTableColumn.clone();
	const oLabel2 = sapMLabel.clone();
	oLabel2.setText('<spring:message code="comCopSecRam.list.rollNm"/>');
	oLabel2.setDesign("Bold");
	const oTemplate2 = sapMLabel.clone();
	oTemplate2.setText("{roleNm}");
	oTemplate2.setWidth("100%");
	oTemplate2.setTextAlign("Begin");
	oCol2.setLabel(oLabel2);
	oCol2.setTemplate(oTemplate2);
	oCol2.setSortProperty("roleNm");

	const oCol3 =  sapUiTableColumn.clone();
	const oLabel3 = sapMLabel.clone();
	oLabel3.setText('<spring:message code="comCopSecRam.list.rollType"/>');
	oLabel3.setDesign("Bold");
	const oTemplate3 = sapMLabel.clone();
	oTemplate3.setText("{roleTyp}");
	oTemplate3.setWidth("100%");
	oTemplate3.setTextAlign("Begin");
	oCol3.setLabel(oLabel3);
	oCol3.setTemplate(oTemplate3);
	oCol3.setSortProperty("roleTyp");

	const oCol4 =  sapUiTableColumn.clone();
	const oLabel4 = sapMLabel.clone();
	oLabel4.setText('<spring:message code="comCopSecRam.list.rollSort"/>');
	oLabel4.setDesign("Bold");
	const oTemplate4 = sapMLabel.clone();
	oTemplate4.setText("{roleSort}");
	oTemplate4.setWidth("100%");
	oTemplate4.setTextAlign("Begin");
	oCol4.setLabel(oLabel4);
	oCol4.setTemplate(oTemplate4)
	oCol4.setSortProperty("roleSort");

	const oCol5 =  sapUiTableColumn.clone();
	const oLabel5 = sapMLabel.clone();
	oLabel5.setText('<spring:message code="comCopSecRam.list.rollDc"/>');
	oLabel5.setDesign("Bold");
	const oTemplate5 = sapMLabel.clone();
	oTemplate5.setText("{roleDc}");
	oTemplate5.setWidth("100%");
	oTemplate5.setTextAlign("Begin");
	oCol5.setLabel(oLabel5);
	oCol5.setTemplate(oTemplate5);
	oCol5.setSortProperty("roleDc");

	const oCol6 =  sapUiTableColumn.clone();
	const oLabel6 = sapMLabel.clone();
	oLabel6.setText('<spring:message code="test"/>');
	oLabel6.setDesign("Bold");
	const oTemplate6 = sapMLabel.clone();
	oTemplate6.setText("{roleCreatDe}");
	oTemplate6.setWidth("100%");
	oTemplate6.setTextAlign("Begin");
	oCol6.setLabel(oLabel6);
	oCol6.setTemplate(oTemplate6);
	oCol6.setSortProperty("roleCreatDe");

	oTable.addColumn(oCol1);
	oTable.addColumn(oCol2);
	oTable.addColumn(oCol3);
	oTable.addColumn(oCol4);
	oTable.addColumn(oCol5);
	oTable.addColumn(oCol6);


	// Instantitae JSON Model
	const oModel = new sap.ui.model.json.JSONModel();
	oModel.setData({modelData: oData});
	// Add model to the table
	oTable.setModel(oModel);
	oTable.bindRows("/modelData");
	oTable.placeAt("idTable");

	// attachRowSelectionChange 행 클릭 시
	// oEvent 매개변수는 ui5 내 이벤트 걸어주는 역할?
	oTable.attachRowSelectionChange(function(oEvent){
		// var s = this.getModel().getProperty("roleCode", oEvent.getParameters().rowContext);
		var a = oEvent.getParameters().rowContext.getProperty('roleCode'); // 함수에서 활용 hidden으로 값을 숨겨서 넘김.
		var searchKeyword = oSearchField.getValue();
		fncSelectRole(a, searchKeyword);
	});

	/*----------------------------------- lower select box -----------------------------------*/

	const oItemA = sapUiCoreItem.clone();
	oItemA.setText('10');
	oItemA.setKey('10');
	const oItemB = sapUiCoreItem.clone();
	oItemB.setText('15');
	oItemB.setKey('15');
	const oItemC = sapUiCoreItem.clone();
	oItemC.setText('20');
	oItemC.setKey('20');

	const oSelect2 = sapMSelect.clone();
	oSelect2.setEnabled(true);
	oSelect2.setEditable(true);
	oSelect2.setForceSelection(false);
	oSelect2.setWidth('100px');
	oSelect2.setSelectedKey(${roleManageVO.pageUnit});
	oSelect2.attachChange(function(oEvent){
		// onSearch(oEvent);
		var searchCondition2 = oSelect2.getSelectedKey();
		fncSelectRoleList(1, searchCondition2);
	});
	oSelect2.addItem(oItemA);
	oSelect2.addItem(oItemB);
	oSelect2.addItem(oItemC);

	oSelect2.placeAt("idSelect2");

	// 하단 셀렉트 박스 적용 시도..
	<%--var searchCondition = oSelect2.getSelectedKey();--%>
	<%--if(searchCondition === "30") {--%>
	<%--	oTable.setMinAutoRowCount(30);--%>
	<%--	${roleManageVO.getPageunit}--%>
	<%--	fncSelectRoleList('1');--%>
	<%--}--%>
	<%--if(searchCondition === "50") {--%>
	<%--	oTable.setMinAutoRowCount(50);--%>
	<%--	fncSelectRoleList('1');--%>
	<%--}--%>

	/*---------------------------------------------------------------------------*/

	// onSearch는 selectbox 2개, searchbox에 쓰였음.
	var onSearch = function (oEvent) {
		// add filter for search
		var aFilters = [];
		var sQuery;
		if(oEvent.sId === "change") {sQuery = oSearchField.getValue()}
		if(oEvent.sId === "liveChange") {sQuery = oEvent.getSource().getValue()}

		// if (sQuery && sQuery.length > 0) {
			var searchCondition = oSelect.getSelectedKey();
			var searchKeyword = oSearchField.getValue();
			var searchCondition2 = oSelect2.getSelectedKey();
			var pageUnit = document.querySelector('#pageUnit').value;

			if(searchCondition === "1")  searchCondition = "roleCode";
			if(searchCondition === "2")  searchCondition = "roleNm";
			if(searchCondition === "3")  searchCondition = "roleDc";
			if(searchCondition2 === "10") pageUnit = 10;
			if(searchCondition2 === "30") pageUnit = 30;
			if(searchCondition2 === "50") pageUnit = 50;
			var filterOperator = sap.ui.model.FilterOperator.Contains;
			var filter = new sap.ui.model.Filter(searchCondition, filterOperator, sQuery, searchKeyword, pageUnit);
			console.log("searchCondition : " + searchCondition);
			console.log("searchKeyword : " + searchKeyword);
			console.log("searchCondition2 : " + searchCondition2);
			console.log("pageUnit : " + pageUnit);

			aFilters.push(filter);

			// aFilters는 라이브검색이 될 수 있게만 보여주는 역할. 값을 넘기는 것은 아님.
		// }

		// update list binding
		var oList = oTable;
		var oBinding = oList.getBinding("rows");
		oBinding.filter(aFilters, "Application");

		/*---------------------------------------------------------------------------*/



	}
</script>
</html>
