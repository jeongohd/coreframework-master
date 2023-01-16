<%--
/**
 * @Class Name  : CoreLoginPolicyRegist.java
 * @Description : CoreLoginPolicyRegist jsp
 * @Modification Information
 * @
 * @  수정일             수정자            수정내용
 * @ -------      --------   ---------------------------
 * @ 2009.02.01   lee.m.j    최초 생성
 * @ 2018.09.03   신용호            공통컴포넌트 3.8 개선
 *
 *  @author lee.m.j
 *  @since 2009.03.11
 *  @version 1.0
 *  @see
 *
 *  Copyright (C) 2009 by MOPAS  All right reserved.
 */
 --%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<c:url value='/css/coreframework/com/com.css' />" rel="stylesheet" type="text/css">
<link href="<c:url value='/css/coreframework/com/button.css' />" rel="stylesheet" type="text/css">
<title><spring:message code="comUatUap.loginPolicyRegist.title"/></title><!-- 로그인정책 등록 -->
<%-- <script type="text/javascript" src="<c:url value='/js/coreframework/com/cmm/fms/CoreMultiFile.js'/>" ></script> --%>
<script type="text/javascript" src="<c:url value='/js/coreframework/com/cmm/fms/CoreMultiFiles.js'/>" ></script>
<script type="text/javascript" src="<c:url value="/validator.cm"/>"></script>
<validator:javascript formName="loginPolicy" staticJavascript="false" xhtml="true" cdata="false"/>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Input.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.Select.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.ui.core.Item.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.ToggleButton.js' />"></script>
<script language="javascript" src="<c:url value='/js/coreframework/ui5/sap.m.MessageBox.js' />"></script>

<script type="text/javaScript" language="javascript">

function fncSelectLoginPolicyList() {
    var varFrom = document.getElementById("loginPolicy");
    varFrom.action = "<c:url value='/uat/uap/coreLoginPolicyList.cm'/>";
    varFrom.submit();
}

function fncLoginPolicyInsert() {

    var varFrom = document.getElementById("loginPolicy");
    varFrom.action = "<c:url value='/uat/uap/coreLoginPolicyAdd.cm'/>";

    <%--if(confirm("<spring:message code="comUatUap.loginPolicyRegist.validate.confirm.save"/>")){ //저장 하시겠습니까?--%>
    <%--    if(!validateLoginPolicy(varFrom)){--%>
    <%--        return;--%>
    <%--    }else{--%>
    <%--        if(ipValidate())--%>
    <%--            varFrom.submit();--%>
    <%--        else--%>
    <%--            return;--%>
    <%--    }--%>
    <%--}--%>
	MessageBoxShow('confirm', '<spring:message code="comUatUap.loginPolicyRegist.validate.confirm.save"/>', function(oAction){
			if (oAction === "OK") {
				if(!validateLoginPolicy(varFrom)){
					return;
				}else{
					if(ipValidate()){
						varFrom.submit();
					}else{
						return;
					}
				}

		}
	});

   <%--const oMessageBox = new sap.m.MessageBox.confirm('<spring:message code="comUatUap.loginPolicyRegist.validate.confirm.save"/>', {--%>
	<%--   onClose : function(sAction) {--%>
	<%--	   if (sAction === "OK") {--%>
	<%--		   if(!validateLoginPolicy(varFrom)){--%>
	<%--			   return;--%>
	<%--		   }else{--%>
	<%--			   if(ipValidate()){--%>
	<%--				   varFrom.submit();--%>
	<%--			   }else{--%>
	<%--				   return;--%>
	<%--			   }--%>
	<%--		   }--%>

	<%--	   }--%>
	<%--   }--%>
   <%--});--%>

}

function ipValidate() {

    var varFrom = document.getElementById("loginPolicy");
    var IPvalue = varFrom.ipInfo.value;

    var ipPattern = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
    var ipArray = IPvalue.match(ipPattern);

    var result = "";
    var thisSegment;

    if(IPvalue === "0.0.0.0" || IPvalue === "255.255.255.255") {
        //alert(IPvalue + " : <spring:message code="comUatUap.loginPolicyRegist.validate.info.exceptionIP"/>"); //예외 아이피 입니다.

		MessageBoxShow('alert', IPvalue + " : <spring:message code="comUatUap.loginPolicyRegist.validate.info.exceptionIP"/>");
		result = false;

    } else {
        result = true;
    }

    if(ipArray == null) {
       // alert("<spring:message code="comUatUap.loginPolicyRegist.validate.info.invalidForm"/>"); //형식이 일치 하지않습니다.
		MessageBoxShow('alert', '<spring:message code="comUatUap.loginPolicyRegist.validate.info.invalidForm"/>');
        result = false;
    } else {
        for (var i=1; i<5; i++) {

            thisSegment = ipArray[i];

            if (thisSegment > 255) {
                //alert("<spring:message code="comUatUap.loginPolicyRegist.validate.info.invalidForm"/>"); //형식이 일치 하지않습니다.
				MessageBoxShow('alert', '<spring:message code="comUatUap.loginPolicyRegist.validate.info.invalidForm"/>');
                result = false;
            }

            if ((i == 0) && (thisSegment > 255)) {
                //alert("<spring:message code="comUatUap.loginPolicyRegist.validate.info.invalidForm"/>"); //형식이 일치 하지않습니다.
				MessageBoxShow('alert', '<spring:message code="comUatUap.loginPolicyRegist.validate.info.invalidForm"/>');
                result = false;
            }
        }
    }

    return result;
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
			<form:form modelAttribute="loginPolicy" method="post" action="${pageContext.request.contextPath}/uat/uap/coreLoginPolicyAdd.cm' />" onSubmit="return false;">
				<input type="hidden" name="dplctPermAt" value="Y" >
				<input type="hidden" name="searchCondition" value="<c:out value='${loginPolicyVO.searchCondition}'/>" >
				<input type="hidden" name="searchKeyword" value="<c:out value='${loginPolicyVO.searchKeyword}'/>" >
				<input type="hidden" name="pageIndex" value="<c:out value='${loginPolicyVO.pageIndex}'/>" >
				<div id="contents">
					<div class="cont-wrap">
						<!--header -->
						<!-- /header -->
						<!--conts-->

						<div class="conts">
							<div class="btnTop menu_n">
								<div class="tit-area_n">
									<h3 class="sub-tit" style="font-size: 20px; font-family: none;"><spring:message code="comUatUap.LoginPolicyList.caption" /> / <spring:message code="title.create" /></h3>
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
											<th><spring:message code="comUatUap.loginPolicyRegist.emplyrId"/><i class="cRed">*</i></th>
											<td>
													<div  id="idEmplyrId"></div>
											</td>
											<th><spring:message code="comUatUap.loginPolicyRegist.emplyrNm"/><i class="cRed">*</i></th>
											<td>
													<div id="idEmplyrNm"></div>
											</td>
										</tr>
										<tr>
											<th><spring:message code="comUatUap.loginPolicyRegist.ipInfo"/><i class="cRed">*</i></th>
											<td>
												<div id="idIpInfo"></div>
											</td>
											<th><spring:message code="comUatUap.loginPolicyRegist.lmttAt"/><i class="cRed">*</i></th>
											<td>
												<div id="idLmttAt"</td>
											</td>
										</tr>

										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="btnTop">
							<div class="btn-wrap r" id="idListButton" style="float: right;">
<%--								<a href="#" class="btn btn-green"><i class="fas fa-save"></i>저장</a><a href="#none" class="btn"><i class="fas fa-list-alt"></i>목록</a><a href="#none" class="btn btn-gray"><i class="fas fa-times-circle"></i>취소</a><a href="#none" class="btn btn-pink"><i class="fas fa-trash-alt"></i>삭제</a>--%>
							</div>
							<div class="btn-wrap r" id="idSaveButton" style="float: right; margin-right: 10px">
							</div>
						</div>
					</div>
				</div>
			</form:form>
			<!--footer-->


			<!-- 하단 버튼 -->

			<!--//footer-->
		</div><!-- //contents-->
	</div><!-- //container -->
</body>
<script>

	const oButton = sapMToggleButton.clone();
	oButton.setText('<spring:message code="button.list" />');
	oButton.setPressed(true);
	oButton.attachPress(function(oEvent){

		fncSelectLoginPolicyList();
	});

	oButton.placeAt('idListButton');

	const oButton2 = sapMToggleButton.clone();
	oButton2.setText('<spring:message code="button.save" />');
	oButton2.setPressed(true);
	oButton2.setType('Accept');
	oButton2.attachPress(function(oEvent){

		fncLoginPolicyInsert();
	});
	oButton2.placeAt('idSaveButton');

	oInput1 = sapMinput.clone();
	//oInput1.setId('vEmplyrId');
	oInput1.setValue('<c:out value='${loginPolicy.emplyrId}'/>');
	oInput1.setEnabled(true);
	oInput1.setEditable(false);
	oInput1.setRequired(true);
	NameSet(oInput1,"emplyrId");
	oInput1.placeAt('idEmplyrId');

	oInput2 = sapMinput.clone();
	//oInput1.setId('vEmplyrId');
	oInput2.setValue('<c:out value='${loginPolicy.emplyrNm}'/>');
	oInput2.setEnabled(true);
	oInput2.setEditable(false);
	oInput2.setRequired(true);
	NameSet(oInput2,"emplyrNm");
	oInput2.placeAt('idEmplyrNm');

	oInput3 = sapMinput.clone();
	//oInput1.setId('vEmplyrId');
	oInput3.setValue('<c:out value='${loginPolicy.ipInfo}'/>');
	oInput3.setEnabled(true);
	oInput3.setEditable(true);
	oInput3.setRequired(true);
	//oInput3.setProperty("name", "ipInfo");
	NameSet(oInput3, "ipInfo");
	oInput3.placeAt('idIpInfo');

	const oItem1 = sapUiCoreItem.clone();
	oItem1.setText('Y');
	oItem1.setKey('Y');
	const oItem2 = sapUiCoreItem.clone();
	oItem2.setText('N');
	oItem2.setKey('N');

	const oSelect = sapMSelect.clone();
	oSelect.setEnabled(true);
	oSelect.setEditable(true);
	oSelect.setForceSelection(true);
	oSelect.setWidth('120px');
	oSelect.setSelectedKey('<c:out value='${loginPolicy.lmttAt}'/>');
	oSelect.attachChange(function(oEvent){
		onSearch(oEvent);
	});
	oSelect.addItem(oItem1);
	oSelect.addItem(oItem2);
	NameSet(oSelect,"lmttAt");
	//oInput1.setProperty("name","lmttAt");

	oSelect.placeAt('idLmttAt');
</script>
</html>




