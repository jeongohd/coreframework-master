<%--
  Created by IntelliJ IDEA.
  User: Cha
  Date: 2022/12/12
  Time: 3:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop Administration Tool</title>
    <script id="sap-ui-bootstrap"
            src="https://sdk.openui5.org/resources/sap-ui-core.js"
            data-sap-ui-theme="sap_fiori_3"
            data-sap-ui-resourceroots='{
			"layoutRes": "../coreframework/layout/",
			"coreRes": "../coreframework/",
			"sampleRes": "../coreframework/sample/",
			"symRes": "../coreframework/sym/",
			"uatRes": "../coreframework/uat/",
			"secRes":"../coreframework/sec/"
		}'
            data-sap-ui-oninit="module:sap/ui/core/ComponentSupport"
            data-sap-ui-compatVersion="edge"
            data-sap-ui-xx-componentPreload=off
            data-sap-ui-async="true">
    </script>
</head>

<body class="sapUiBody sapUiSizeCompact" id="content">
<div data-sap-ui-component data-name="coreRes" data-id="container" data-settings='{"id" : "core"}'></div>
</body>
</html>
