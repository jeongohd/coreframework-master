<%
 /**
  * @Class Name : EgovAuthorManage.java
  * @Description : EgovAuthorManage List 화면
  * @Modification Information
  * @
  * @  수정일                     수정자                    수정내용
  * @ -------       --------    ---------------------------
  * @ 2009.03.01    Lee.m.j       최초 생성
  *   2016.06.13    장동한          표준프레임워크 v3.6 개선
  *
  *  @author 실행환경 개발팀 홍길동
  *  @since 2009.02.01
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
<c:set var="pageTitle"><spring:message code="comCopSecRam.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.list" /></title><!-- 권한관리 목록 -->
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/coreframework/com/com.css' />">
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.js' />"></script>
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.table.Table.js' />"></script>
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.table.Column.js' />"></script>
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Label.js' />"></script>
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.SearchField.js' />"></script>
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Select.js' />"></script>
	<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.core.Item.js' />"></script>


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
                    checkField[i].value = checkId[i].value;
                    if(returnValue == "")
                        returnValue = checkField[i].value;
                    else
                	    returnValue = returnValue + ";" + checkField[i].value;
                    checkCount++;
                }
            }
            if(checkCount > 0)
                returnBoolean = true;
            else {
                alert("<spring:message code="comCopSecRam.validate.authorSelect" />"); //선택된 권한이 없습니다."
                returnBoolean = false;
            }
        } else {
            if(document.listForm.delYn.checked == false) {
                alert("<spring:message code="comCopSecRam.validate.authorSelect" />"); //선택된 권한이 없습니다."
                returnBoolean = false;
            }
            else {
                returnValue = checkId.value;
                returnBoolean = true;
            }
        }
    } else {
        alert("<spring:message code="comCopSecRam.validate.authorSelectResult" />"); //조회된 결과가 없습니다.
    }

    document.listForm.authorCodes.value = returnValue;

    return returnBoolean;
}

function fncSelectAuthorList(pageNo){
    document.listForm.searchCondition.value = "1";
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/sec/ram/CoreAuthorList.cm'/>";
    document.listForm.submit();
}

function fncSelectAuthor(author) {
    document.listForm.authorCode.value = author;
    document.listForm.action = "<c:url value='/sec/ram/CoreAuthor.cm'/>";
    document.listForm.submit();
}

function fncAddAuthorInsert() {
    location.replace("<c:url value='/sec/ram/CoreAuthorInsertView.cm'/>");
}

function fncAuthorDeleteList() {

    if(fncManageChecked()) {
    	if(confirm("<spring:message code="common.delete.msg" />")){	//삭제하시겠습니까?
            document.listForm.action = "<c:url value='/sec/ram/CoreAuthorListDelete.cm'/>";
            document.listForm.submit();
        }
    }
}

function fncAddAuthorView() {
    document.listForm.action = "<c:url value='/sec/ram/CoreAuthorUpdate.cm'/>";
    document.listForm.submit();
}

function fncSelectAuthorRole(author) {
    document.listForm.searchKeyword.value = author;
    document.listForm.action = "<c:url value='/sec/ram/CoreAuthorRoleList.cm'/>";
    document.listForm.submit();
}

function linkPage(pageNo){
    document.listForm.searchCondition.value = "1";
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/sec/ram/CoreAuthorList.cm'/>";
    document.listForm.submit();
}


function press() {

    if (event.keyCode==13) {
    	fncSelectAuthorList('1');
    }
}

</script>
<script>

</script>
</head>
<body>

<!--wrap-->
<div id="wrap">
	<!--container -->
	<div id="container">

		<!--contents-->
		<div id="contents">
<form:form name="listForm" action="${pageContext.request.contextPath}/sec/ram/CoreAuthorList.cm" method="post">
	<input type="hidden" name="authorCode"/>
	<input type="hidden" name="authorCodes"/>
	<input type="hidden" name="pageIndex" value="<c:out value='${authorManageVO.pageIndex}'/>"/>
	<input type="hidden" name="searchCondition" value="1"/>
			<!--cont-wrap-->
			<div class="cont-wrap">
				<div class="conts">
				<div class="btnTop menu_n">
					<div class="tit-area_n">
						<h3 class="sub-tit" style="font-size: 20px; font-family: none;"> ${pageTitle}  /<spring:message code="title.list" /></h3>
					</div>
													<div class="btn-wrap r">
														<a href="#" class="btn btn-blue"><i class="fas fa-save"></i>저장</a>
														<a href="#" class="btn btn-yellow"><i class="fas fa-plus-circle"></i>신규입력</a>
													</div>
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
								<c:if test="${!empty authorManageVO.pageIndex }">
									<!-- paging navigation -->
									<div class="pagination">
										<ul><ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/></ul>
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
		</form:form>
		</div><!-- //contents-->
	</div><!-- //container -->
</div>
<!--//wrap-->

</body>
<script>
const oData = ${authorListJson};
const oTable = sapUiTableTable.clone();
oTable.setColumnHeaderHeight(50);
oTable.setVisibleRowCountMode('Auto');
oTable.setEnableBusyIndicator(true);
oTable.setSelectionMode("Single");
oTable.setSelectionBehavior("RowOnly");
oTable.setMinAutoRowCount(10);

const oCol1 =   sapUiTableColumn.clone();
oCol1.setSortProperty("authorCode");

const oLabel1 = sapMLabel.clone();
oLabel1.setText('<spring:message code="comCopSecRam.list.authorRollId"/>');
oLabel1.setDesign("Bold");

const oTemplate1 = sapMLabel.clone();
oTemplate1.setText("{authorCode}");
oTemplate1.setWidth("100%");
oTemplate1.setTextAlign("Begin");

oCol1.setLabel(oLabel1);
oCol1.setTemplate(oTemplate1);

const oCol2 =   sapUiTableColumn.clone();
oCol2.setSortProperty("authorNm");

const oLabel2 = sapMLabel.clone();
oLabel2.setText('<spring:message code="comCopSecRam.list.authorNm"/>');
oLabel2.setDesign("Bold");

const oTemplate2 = sapMLabel.clone();
oTemplate2.setText("{authorNm}");
oTemplate2.setWidth("100%");
oTemplate2.setTextAlign("Begin");

oCol2.setLabel(oLabel2);
oCol2.setTemplate(oTemplate2);


const oCol3 =   sapUiTableColumn.clone();
oCol3.setSortProperty("authorNm");

const oLabel3 = sapMLabel.clone();
oLabel3.setText('<spring:message code="comCopSecRam.list.authorDc"/>');
oLabel3.setDesign("Bold");

const oTemplate3 = sapMLabel.clone();
oTemplate3.setText("{authorDc}");
oTemplate3.setWidth("100%");
oTemplate3.setTextAlign("Begin");

oCol3.setLabel(oLabel3);
oCol3.setTemplate(oTemplate3);

const oCol4 =   sapUiTableColumn.clone();
oCol4.setSortProperty("authorNm");

const oLabel4 = sapMLabel.clone();
oLabel4.setText('<spring:message code="table.regdate"/>');
oLabel4.setDesign("Bold");

const oTemplate4 = sapMLabel.clone();
oTemplate4.setText("{authorCreatDe}");
oTemplate4.setWidth("100%");
oTemplate4.setTextAlign("Begin");

oCol4.setLabel(oLabel4);
oCol4.setTemplate(oTemplate4);


const oCol5 =   sapUiTableColumn.clone();
oCol5.setSortProperty("authorNm");

const oLabel5 = sapMLabel.clone();
oLabel5.setText('<spring:message code="comCopSecRam.list.authorRoll"/>');
oLabel5.setDesign("Bold");

const oTemplate5 = sapMLabel.clone();
oTemplate5.setText("");
oTemplate5.setWidth("100%");
oTemplate5.setTextAlign("Begin");

oCol5.setLabel(oLabel5);
oCol5.setTemplate(oTemplate5);

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


<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>

	<div class="board">
		<h1>${pageTitle} <spring:message code="title.list" /></h1><!-- 권한관리 목록 -->
		<!-- 검색영역 -->
		<div class="search_box" title="<spring:message code="common.searchCondition.msg" />">
			<ul>
				<li><div style="line-height:4px;">&nbsp;</div><div><spring:message code="comCopSecRam.list.searchKeywordText" /> : </div></li><!-- 권한명 -->
				<!-- 검색키워드 및 조회버튼 -->
				<li style="border: 0px solid #d2d2d2;">
					<input class="s_input" name="searchKeyword" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${authorManageVO.searchKeyword}"/>'  maxlength="155" >
					<input type="submit" class="s_btn" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
					<input type="button" class="s_btn" onClick="fncAuthorDeleteList()" value="<spring:message code="title.delete" />" title="<spring:message code="title.delete" /> <spring:message code="input.button" />" />
					<span class="btn_b"><a href="<c:url value='/sec/ram/CoreAuthorInsertView.cm'/>" onClick="javascript:fncAddAuthorInsert();"  title="<spring:message code="button.create" /> <spring:message code="input.button" />"><spring:message code="button.create" /></a></span>
				</li>
			</ul>
		</div>

		<!-- 목록영역 -->
		<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
			<caption>${pageTitle} <spring:message code="title.list" /></caption>
			<colgroup>
				<col style="width: 9%;">
				<col style="width: 33%;">
				<col style="width: 30%;">
				<col style="width: ;">
				<col style="width: 10%;">
				<col style="width: 7%;">
			</colgroup>
			<thead>
			<tr>
				<th><input type="checkbox" name="checkAll" class="check2" onclick="javascript:fncCheckAll()" title="<spring:message code="input.selectAll.title" />"></th><!-- 번호 -->
				<th class="board_th_link"><spring:message code="comCopSecRam.list.authorRollId" /></th><!-- 권한 ID -->
				<th><spring:message code="comCopSecRam.list.authorNm" /></th><!-- 권한 명 -->
				<th><spring:message code="comCopSecRam.list.authorDc" /></th><!-- 설명 -->
				<th><spring:message code="table.regdate" /></th><!-- 등록일자 -->
				<th><spring:message code="comCopSecRam.list.authorRoll" /></th><!-- 롤 정보 -->
			</tr>
			</thead>
			<tbody class="ov">
			<c:if test="${fn:length(authorList) == 0}">
				<tr>
					<td colspan="6"><spring:message code="common.nodata.msg" /></td>
				</tr>
			</c:if>
			<c:forEach var="author" items="${authorList}" varStatus="status">
				<tr>
					<td><input type="checkbox" name="delYn" class="check2" title="선택"><input type="hidden" name="checkId" value="<c:out value="${author.authorCode}"/>" /></td>
					<td><a href="#LINK" onclick="javascript:fncSelectAuthor('<c:out value="${author.authorCode}"/>')"><c:out value="${author.authorCode}"/></a></td>
					<td><c:out value="${author.authorNm}"/></td>
					<td><c:out value="${author.authorDc}"/></td>
					<td><c:out value="${fn:substring(author.authorCreatDe,0,10)}"/></td>
					<td><a href="<c:url value='/sec/ram/CoreAuthorRoleList.cm'/>?searchKeyword=<c:out value="${author.authorCode}"/>" onclick="javascript:fncSelectAuthorRole('<c:out value="${author.authorCode}"/>')"><img src="<c:url value='/images/coreframework/com/cmm/btn/btn_search.gif'/>" width="15" height="15" align="middle" alt="롤 정보"></a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>

		<c:if test="${!empty authorManageVO.pageIndex }">
			<!-- paging navigation -->
			<div class="pagination">
				<ul><ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/></ul>
			</div>
		</c:if>


	</div><!-- end div board -->



