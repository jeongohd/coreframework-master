sap.ui.define([
    './BaseController',
    'sap/m/ResponsivePopover',
    'sap/m/MessagePopover',
    'sap/m/ActionSheet',
    'sap/m/Button',
    'sap/m/Link',
    'sap/m/NotificationListItem',
    'sap/m/MessagePopoverItem',
    'sap/ui/core/CustomData',
    'sap/m/MessageToast',
    'sap/ui/Device',
    'sap/ui/core/syncStyleClass',
    'sap/m/library'
], function ( BaseController,
              ResponsivePopover,
              MessagePopover,
              ActionSheet,
              Button,
              Link,
              NotificationListItem,
              MessagePopoverItem,
              CustomData,
              MessageToast,
              Device,
              syncStyleClass,
              mobileLibrary) {
    "use strict";

    var CController = BaseController.extend("layoutRes.controller.Layout", {
        onInit : function() {
            // this.oModel = new JSONModel();
            // this.oModel.loadData(sap.ui.require.toUrl("layout/model/sideContent.json"), null, false);
            // this.getView().setModel(this.oModel);

            this.getView().addStyleClass(this.getOwnerComponent().getContentDensityClass());
            if (Device.resize.width <= 1024) {
                this.onSideNavButtonPress();
            }
            Device.media.attachHandler(this._handleWindowResize, this);
            this.getRouter().attachRouteMatched(this.onRouteChange.bind(this));
        },
        onSideNavButtonPress: function() {
            var oToolPage = this.byId("app");
            var bSideExpanded = oToolPage.getSideExpanded();
            this._setToggleButtonTooltip(bSideExpanded);
            oToolPage.setSideExpanded(!oToolPage.getSideExpanded());
        },

        _setToggleButtonTooltip : function(bSideExpanded) {
            var oToggleButton = this.byId('sideNavigationToggleButton');
            this.getBundleText(bSideExpanded ? "expandMenuButtonText" : "collpaseMenuButtonText").then(function(sTooltipText){
                oToggleButton.setTooltip(sTooltipText);
            });
        },

        onRouteChange: function (oEvent) {
            this.getModel('side').setProperty('/selectedKey', oEvent.getParameter('name'));
            if (Device.system.phone) {
                this.onSideNavButtonPress();
            }
        },



    });


    return CController;

});