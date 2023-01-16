<%--
  Created by IntelliJ IDEA.
  User: Cha
  Date: 2022/12/12
  Time: 11:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <script id="sap-ui-bootstrap"
            src="https://openui5.hana.ondemand.com/resources/sap-ui-core.js"
            data-sap-ui-theme="sap_fiori_3"
            data-sap-ui-resourceroots='{
			"sampleRes": "../coreframework/sample/"
		}'
            data-sap-ui-compatVersion="edge"
            data-sap-ui-async="true"
            data-sap-ui-frameOptions="trusted"
            data-sap-ui-xx-componentPreload=off
            data-sap-ui-oninit="module:sap/ui/core/ComponentSupport">
    </script>
    <style>
        html, body { height: 100%; }
    </style>
</head>
<body class="sapUiBody sapUiSizeCozy" id="content">
<div data-sap-ui-component
     data-name="sampleRes"
     data-height="100%"
     data-id="container"
     data-settings='{"id" : "sampleRes"}'
     style="height: 100%">
</div>
</body>
</html>
