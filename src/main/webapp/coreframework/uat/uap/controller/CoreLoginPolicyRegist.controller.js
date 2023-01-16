sap.ui.define( [
	"sap/ui/core/mvc/Controller",
	"sap/ui/core/routing/History",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/format/DateFormat",
	"sap/base/Log",
	"sap/m/MessageBox"
], function (Controller, History, JSONModel, DateFormat, Log, MessageBox) {
	"use strict";

	return Controller.extend("coreLoginPolicy.controller.CoreLoginPolicyRegist", {
		onInit : function () {
			// var sUrl = "#" + this.getOwnerComponent().getRouter().getURL("coreLoginPolicyList");
			// this.byId("link").setHref(sUrl);
			console.log("onInit function called Reg");
			var oRouter = this.getOwnerComponent().getRouter();

			oRouter.getRoute("coreLoginPolicyRegist").attachMatched(this.onRouteMatched, this);

		},

		onBeforeRendering: function() {
			console.log("onBeforeRendering function called Reg");
		},

		onAfterRendering: function() {
			console.log("onAfterRendering function called Reg");
		},

		onToList : function () {
			this.getOwnerComponent().getRouter().navTo("coreLoginPolicyList");
		},

		onBack : function () {
			var sPreviousHash = History.getInstance().getPreviousHash();

			//The history contains a previous entry
			if (sPreviousHash !== undefined) {
				window.history.go(-1);
			} else {
				// There is no history!
				// replace the current hash with page 1 (will not add an history entry)
				this.getOwnerComponent().getRouter().navTo("coreLoginPolicyList", null, true);
			}
		},

		onRouteMatched : function (oEvent) {
			var oArgs, oView;
			oArgs = oEvent.getParameter("arguments");
			oView = this.getView();

			var oJSONModel = this.initSampleDataModel(oEvent);

			this.getView().setModel(oJSONModel);

			//this.getView().setModel(oJSONModel.getData().data);

			oView.bindElement({
				path : "/coreLoginPolicy(" + oArgs.emplyrId + ")",
				events : {
					change: this._onBindingChange.bind(this),
					dataRequested: function (oEvent) {

						oView.setBusy(true);
					},
					dataReceived: function (oEvent) {
						oView.setBusy(false);
					}
				}
			});
		},

		_onBindingChange : function (oEvent) {
			console.log(oEvent);
			// No data for the binding
			if (!this.getView().getBindingContext()) {
				this.getOwnerComponent().getTargets().display("notFound");
			}
		},
		initSampleDataModel : function(oEvent) {
			// var vSearchCondition = this.byId("searchCondition");
			// var vSearchKeyword = this.byId("searchKeyword");

			var oModel = new JSONModel();
			var oDateFormat = DateFormat.getDateInstance({source: {pattern: "timestamp"}, pattern: "yyyy-MM-dd"});
			var oData = oEvent.getParameter("arguments")


			jQuery.ajax({
				url: "/uat/uap/coreLoginPolicyApi.cm",
				data:oData,
				dataType: "json",
				method: "POST",
				async: false,	//중요
				success: function(oData) {

					oModel.setData(oData.data);
					console.log(oModel);
				},
				error: function() {
					Log.error("failed to load json");
				}
			});

			return oModel;
		},

		onEdit : function (oEvent) {
			var o = this.byId("SimpleFormToolbar");
			MessageBox.confirm("수정하시겠습니까?",{
				onClose: function (sAction){
					if(sAction === "OK") {

						//console.log(o);

						var oData = o.getBindingContext().oModel.oData;
						console.log(oData);
						jQuery.ajax({
							url: "/uat/uap/coreLoginPolicyModifyApi.cm",
							data:oData,
							dataType: "json",
							method: "POST",
							async: false,	//중요
							success: function(oData) {



								MessageBox.alert(oData.message);

							},
							error: function() {
								Log.error("failed to load json");
							}
						});

					}
				}
			});

		},

		onDelete : function (oEvent){
			console.log(oEvent);
		}

	});

});
