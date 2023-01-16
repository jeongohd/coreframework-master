sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageBox"
], function(Controller, JSONModel, MessageBox) {
    "use strict";

    return Controller.extend("uiaRes.controller.LoginUsr", {
        onInit : function () {

            var oData = {
                id: "",
                password: "",
                userSe: ""
            };

            var oModel = new JSONModel(oData);

            this.getView().setModel(oModel);

            //this가 controller를 바라볼 수 있게
            var that = this;

            //oResult 배열 데이터
            //setModel시 모델명 만들어줌
            // $.ajax({
            // 	url: "./json/account.json",
            // 	type: "get",
            // 	success: function (oResult) {
            // 		var oAccountModel = new JSONModel(oResult);
            // 		that.getView().setModel(oAccountModel, "account");
            // 		// that.getView().setModel(new JSONModel(oResult, "account"));
            // 	}
            // });
        },

        onLogin : function (oEvent) {
            var oRadioButtonGroup = this.byId("typeGroup");
            var sSelectedStatus = oRadioButtonGroup.getSelectedButton().getText();
            var sSelectedText = oRadioButtonGroup.getSelectedButton().data("codeValue");

            var oModel = this.getView().getModel();
            oModel.setProperty("/userSe", sSelectedText)

            var sId = oModel.getProperty("/id");
            var sPassword = oModel.getProperty("/password");
            var sUserSe = oModel.getProperty("/userSe");
            //getData() 모델에 바인딩된 전체 데이터, 배열데이터 리턴
            // var aAccount = oAccountModel.getData();
            var bLoginCheck = false;

            //ERP에서 데이터를 가져오는 것과 비슷한 형식. 클릭 할 때마다 데이터 가져옴
            //변수 생성 없이 사용 가능
            var sData = $('loginForm').serialize();



            $.ajax({
                url: "/uat/uia/actionLoginApi.cm",
                type: "post",
                data: oModel.oData,
                success: function (oResult) {
                    if(oResult.success){
                        MessageBox.success("success login");

                        goResult(oResult);


                    } else{
                        MessageBox.error("fail login");
                    }

                },

            });


            function goResult(result) {
                $.ajax({
                    url: result.data.url,
                    type: "post",
                    success: function (oResult) {
                        if(oResult.success){
                           location.href = oResult.data.url;

                        } else{
                            MessageBox.error("fail login");
                        }

                    },

                });
            }

        },

        onSelect : function (oEvent) {
            var oSelectedIndex = oEvent.getParameter("selectedIndex");
            var oRadioButtonSrc = oEvent.getSource().getAggregation("buttons");
            var oSelectedRadioText = oRadioButtonSrc[oSelectedIndex].getText();
            console.log(oSelectedRadioText);
        }
    });
});