<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="coreframework.com.cmm.LoginVO" %>
<%@ page import="coreframework.com.cmm.util.CoreUserDetailsHelper" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Basic</title>

	<script id="sap-ui-bootstrap"
			src="https://openui5.hana.ondemand.com/resources/sap-ui-core.js"
			data-sap-ui-theme="sap_belize_plus"
			data-sap-ui-resourceroots='{
			"coreLoginPolicy": "/coreframework/uat/uap/"
		}'
			data-sap-ui-compatVersion="edge"
			data-sap-ui-async="true"
			data-sap-ui-frameOptions="trusted"
			data-sap-ui-xx-componentPreload=off
			data-sap-ui-oninit="module:sap/ui/core/ComponentSupport">
	</script>

</head>

<body style="height: 98%">
<div data-sap-ui-component
	 data-name="coreLoginPolicy"
	 data-height="100%"
	 data-id="container"
	 data-settings='{"id" : "nav"}'
	 style="height: 100%">
</div>

</body>
</html>