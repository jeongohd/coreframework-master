<%
 /**
  * @Class Name : EgovIndvdlPgeUpdt.jsp
  * @Description : EgovIndvdlPgeUpdt 화면
  * @Modification Information
  * @
  * @  수정일             수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.02.01   박정규              최초 생성
  *   2016.06.13   김연호              표준프레임워크 v3.6 개선
  *  @author 공통서비스팀 
  *  @since 2009.02.01
  *  @version 1.0
  *  @see
  *  
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<c:set var="pageTitle"><spring:message code="comUssMpe.indvdlPgeVO.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle } <spring:message code="title.update" /></title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/coreframework/com/com.css' />">
<script type="text/javascript" src="<c:url value="/validator.cm"/>"></script>
<validator:javascript formName="indvdlPgeVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
/* ********************************************************
 * 초기화
 ******************************************************** */
function fn_egov_init(){
	// 첫 입력란에 포커스..
	document.getElementById("indvdlPgeVO").cntntsNm.focus();
}
/* ********************************************************
 * 저장처리화면
 ******************************************************** */
function fn_egov_updt_pge(form){
	if (!validateIndvdlPgeVO(form)) {		 			
		return false;		
	} else {
		
		if(confirm("<spring:message code="common.update.msg" />")){	
			form.submit();	
		}					
	}	
}
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_inqire_list() {
	indvdlPgeVO.action = "<c:url value='/uss/mpe/selectIndvdlPgeList.cm'/>";
	indvdlPgeVO.submit();	
}
</script>
</head>
<body onLoad="fn_egov_init();">

<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>

<!-- 상단타이틀 -->
<form:form modelAttribute="indvdlPgeVO" action="${pageContext.request.contextPath}/uss/mpe/updateIndvdlPge.cm" method="post" onSubmit="fn_egov_updt_pge(document.forms[0]); return false;">
<div class="wTableFrm">
	<h2>${pageTitle} <spring:message code="title.update" /></h2>

	<!-- 수정폼 -->
	<table class="wTable" summary="<spring:message code="common.summary.update" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.update" /></caption>
	<colgroup>
		<col style="width: 20%;"><col style="width: ;">
	</colgroup>
	<tbody>
		<!-- 입력 -->
		<c:set var="inputTxt"><spring:message code="input.input" /></c:set>
		
		<!-- 컨텐츠명 -->
		<c:set var="title"><spring:message code="comUssMpe.indvdlPgeVO.cntntsNm"/> </c:set>
		<tr>
			<th><label for="cntntsNm">${title} <span class="pilsu">*</span></label></th>
			<td class="left">
			    <form:input path="cntntsNm" title="${title} ${inputTxt}" size="70" maxlength="70" />
   				<div><form:errors path="cntntsNm" cssClass="error" /></div>     
			</td>
		</tr>
		
		<!-- 컨텐츠URL -->
		<c:set var="title"><spring:message code="comUssMpe.indvdlPgeVO.cntcUrl"/> </c:set>
		<tr>
			<th><label for="cntcUrl">${title} <span class="pilsu">*</span></label></th>
			<td class="left">
			    <form:input path="cntcUrl" title="${title} ${inputTxt}" size="70" maxlength="70" />
   				<div><form:errors path="cntcUrl" cssClass="error" /></div>     
			</td>
		</tr>
		
		<!-- 미리보기URL -->
		<c:set var="title"><spring:message code="comUssMpe.indvdlPgeVO.cntntsLinkUrl"/> </c:set>
		<tr>
			<th><label for="cntntsLinkUrl">${title} <span class="pilsu">*</span></label></th>
			<td class="left">
			    <form:input path="cntntsLinkUrl" title="${title} ${inputTxt}" size="70" maxlength="70" />
   				<div><form:errors path="cntntsLinkUrl" cssClass="error" /></div>     
			</td>
		</tr>
		
		<!-- 컨텐츠설명 -->
		<c:set var="title"><spring:message code="comUssMpe.indvdlPgeVO.cntntsDc"/> </c:set>
		<tr>
			<th><label for="cntntsDc">${title } <span class="pilsu">*</span></label></th>
			<td class="nopd">
				<form:textarea path="cntntsDc" title="${title} ${inputTxt}" cols="300" rows="20" />   
				<div><form:errors path="cntntsDc" cssClass="error" /></div>  
			</td>
		</tr>
		
		<!-- 사용여부 -->
		<c:set var="title"><spring:message code="comUssMpe.indvdlPgeVO.cntntsUseAt"/> </c:set>
		<tr>
			<th><label for="cntntsUseAt">${title } <span class="pilsu">*</span></label></th>
			<td class="left">
				<form:select path="cntntsUseAt" title="${title} ${inputTxt }" cssClass="txt">
					<form:option value="" label="--선택하세요--" />
					<form:option value="Y"  label="예" />
	  		   		<form:option value='N'>아니오</form:option>
				</form:select>
				<div><form:errors path="cntntsUseAt" cssClass="error" /></div>       
			</td>
		</tr>
		
	</tbody>
	</table>

	<!-- 하단 버튼 -->
	<div class="btn">
		<input type="submit" class="s_submit" value="<spring:message code="button.update" />" title="<spring:message code="button.update" /> <spring:message code="input.button" />" />
		<span class="btn_s"><a href="<c:url value='/uss/mpe/selectIndvdlPgeList.cm' />"  title="<spring:message code="button.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a></span>
	</div><div style="clear:both;"></div>
	
</div>


<input name="cntntsId" type="hidden" value="<c:out value='${indvdlPgeVO.cntntsId}'/>">
</form:form>

</body>
</html>
