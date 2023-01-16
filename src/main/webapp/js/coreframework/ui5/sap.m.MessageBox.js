//window.onload=function() {

    jQuery.sap.require("sap.m.MessageBox"); //sap.ui.require("sap/m/MessageBox")
    //const mMessageBox = new sap.m.MessageBox();
    //jQuery.sap.require("sap.m.MessageToast"); //sap.ui.require("sap/m/MessageBox")
    //const mMessageToast = new sap.m.MessageToast();

    let MessageBoxShow = function (o, s, f) {
        if (o === 'confirm') {
            new sap.m.MessageBox.confirm(s, {
                title: "확인",                                    // default
                onClose: f,                                       // default
                styleClass: "",                                      // default
                actions: [sap.m.MessageBox.Action.OK,
                    sap.m.MessageBox.Action.CANCEL],                 // default
                emphasizedAction: sap.m.MessageBox.Action.OK,        // default
                initialFocus: null,                                  // default
                textDirection: sap.ui.core.TextDirection.Inherit     // default
            });
        } else if (o === 'alert') {
            new sap.m.MessageBox.alert(s, {
                title: "경고",                                      // default
                onClose: f,                                       // default
                styleClass: "",                                      // default
                actions: sap.m.MessageBox.Action.OK,                 // default
                emphasizedAction: sap.m.MessageBox.Action.OK,        // default
                initialFocus: null,                                  // default
                textDirection: sap.ui.core.TextDirection.Inherit     // default
            });
        } else if (o === 'error') {
            new sap.m.MessageBox.error(s, {
                title: "에러",                                      // default
                onClose: f,                                       // default
                styleClass: "",                                      // default
                actions: sap.m.MessageBox.Action.CLOSE,              // default
                emphasizedAction: null,                              // default
                initialFocus: null,                                  // default
                textDirection: sap.ui.core.TextDirection.Inherit     // default
            });
        } else if (o === 'information') {
            new sap.m.MessageBox.information(s, {
                title: "정보",                                // default
                onClose: f,                                       // default
                styleClass: "",                                      // default
                actions: sap.m.MessageBox.Action.OK,                 // default
                emphasizedAction: sap.m.MessageBox.Action.OK,        // default
                initialFocus: null,                                  // default
                textDirection: sap.ui.core.TextDirection.Inherit     // default
            });
        } else if (o === 'success') {
            new sap.m.MessageBox.success(s, {
                title: "성공",                                    // default
                onClose: f,                                       // default
                styleClass: "",                                      // default
                actions: sap.m.MessageBox.Action.OK,                 // default
                emphasizedAction: sap.m.MessageBox.Action.OK,        // default
                initialFocus: null,                                  // default
                textDirection: sap.ui.core.TextDirection.Inherit     // default
            });
        } else if (o === 'warning') {
            new sap.m.MessageBox.warning(s, {
                title: "경고",                                    // default
                onClose: f,                                       // default
                styleClass: "",                                      // default
                actions: sap.m.MessageBox.Action.OK,                 // default
                emphasizedAction: sap.m.MessageBox.Action.OK,        // default
                initialFocus: null,                                  // default
                textDirection: sap.ui.core.TextDirection.Inherit     // default
            });
        }
    //}
}





