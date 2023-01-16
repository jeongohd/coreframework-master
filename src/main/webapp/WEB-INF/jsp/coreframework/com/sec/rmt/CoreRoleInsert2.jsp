
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
<title>${pageTitle} <spring:message code="title.create" /></title><!-- 롤관리 등록 -->
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
    // location.href = "/sec/rmt/CoreRoleList.cm";

    var varFrom = document.getElementById("role");
    varFrom.action = "<c:url value='/sec/rmt/CoreRoleList.cm'/>";
    varFrom.submit();
}

function fncRoleInsert() {
    var varFrom = document.getElementById("role");
    if(confirm("<spring:message code="common.save.msg" />")){ //저장하시겠습니까?
        // if(!validateRoleManage(form)){
        //     return false;
        // }else{
        varFrom.submit();
        // }
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
        <form:form modelAttribute="role" method="post" action="/sec/rmt/CoreRoleInsert.cm" onSubmit="return false;">

            <%-- <input type="hidden" name="roleCode" >--%>
            <%-- 함수에 roleCode를 활용하지 않아서 넘길 필요 없음. hidden으로 값을 넘기면 오류.--%>

            <!-- 검색조건 유지 -->
            <input type="hidden" name="searchCondition" value="<c:out value='${roleManage.searchCondition}'/>" >
            <input type="hidden" name="searchKeyword" value="<c:out value='${roleManage.searchKeyword}'/>" >
            <input type="hidden" name="pageIndex" value="<c:out value='${roleManage.pageIndex}'/>" >
            <div id="contents">
                <div class="cont-wrap">
                    <!--header -->
                    <!-- /header -->
                    <!--conts-->

                    <div class="conts">
                        <div class="btnTop menu_n">
                            <div class="tit-area_n">
                                <h3 class="sub-tit" style="font-size: 20px; font-family: none;"><spring:message code="comCopSecRmt.title" /> / <spring:message code="title.create" /></h3>
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
                                    </tr>
                                    <tr>
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
                                    </tr>
                                    <tr>
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
                        <div class="btn-wrap r" id="roleListButton" style="float: right;"></div>
                        <div class="btn-wrap r" id="roleCreateButton" style="float: right; margin-right: 10px"></div>
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
    oButton2.setText('<spring:message code="button.create" />');
    oButton2.setType('Accept');
    oButton2.attachPress(function(oEvent){
        fncRoleInsert();
    });
    oButton2.placeAt('roleCreateButton');



    /* --------------------------- input 텍스트 --------------------------- */

    const oInput1 = sapMinput.clone();
    oInput1.setValue('<c:out value='${role.roleNm}'/>');
    oInput1.setEnabled(true);
    oInput1.setEditable(true);
    oInput1.setRequired(true);
    NameSet(oInput1,"roleNm");
    oInput1.placeAt('roleNm'); /* BODY안의 DIV ID와 동일하게 적용해야 함. */

    const oInput2 = sapMinput.clone();
    oInput2.setValue('<c:out value='${role.rolePtn}'/>');
    oInput2.setEnabled(true);
    oInput2.setEditable(true);
    oInput2.setRequired(true);
    NameSet(oInput2,"rolePtn");
    oInput2.placeAt('rolePtn');

    const oInput3 = sapMinput.clone();
    oInput3.setValue('<c:out value='${role.roleDc}'/>');
    oInput3.setEnabled(true);
    oInput3.setEditable(true);
    oInput3.setRequired(true);
    //oInput3.setProperty("name", "ipInfo");
    NameSet(oInput3, "roleDc");
    oInput3.placeAt('roleDc');

    const oInput4 = sapMinput.clone();
    oInput4.setValue('<c:out value='${role.roleSort}'/>');
    oInput4.setEnabled(true);
    oInput4.setEditable(true);
    oInput4.setRequired(true);
    NameSet(oInput4, "roleSort");
    oInput4.placeAt('roleSort');



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

