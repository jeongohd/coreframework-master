sap.ui.define([
    "sap/ui/Device",
    "sap/m/Button"
], function (Device, Button) {
    // pagination Number Button create function.
    const addNumber = function (firstPage, lastPage, container) {
        for (let i = firstPage; i <= lastPage; i++) {
            let oButton = new Button({
                text: i,
            });
            container.addItem(oButton);
        }
    }
    // pagination arrow Button create function.
    const setFixedButtons = function (firstPage, lastPage, totalPageCount, container) {
        if (firstPage - 1 > 0) {
            let oButton = new Button({
                icon: "sap-icon://close-command-field"
            });
            container.insertItem(oButton, 0);

            oButton = new Button({
                icon: "sap-icon://navigation-left-arrow"
            });
            container.insertItem(oButton, 1);
        }
        if (lastPage < totalPageCount) {
            oButton = new Button({
                icon: "sap-icon://navigation-right-arrow"
            });
            container.insertItem(oButton, lastPage + 2);

            oButton = new Button({
                icon: "sap-icon://open-command-field"
            });
            container.insertItem(oButton, lastPage + 3);
        }
    }

    return {
        init: function (properties, oView) {
            if (this.Extend(properties, oView)) this.addPaginator()
        },
        Extend: function (properties, oView) {
            properties = properties || {};
            // Device Check
            this.devicePhone = Device.system.phone;
            this.deviceTablet = Device.system.tablet && !Device.system.desktop;
            // 총 데이터의 갯수.
            this.totalRecordCount = properties.totalRecordCount || 0;
            // 한 페이지 당 나타낼 데이터의 갯수.
            this.recordCountPerPage = properties.recordCountPerPage || 0;
            // 총 페이지 갯수.
            this.totalPageCount = properties.totalPageCount || 0;
            // 첫 번째 페이지.
            this.firstPageNoOnPageList = properties.firstPageNoOnPageList || 0;
            // 마지막 페이지.
            this.lastPageNoOnPageList = properties.lastPageNoOnPageList || 0;
            // 현재 페이지.
            this.currentPageNo = properties.currentPageNo || 0;
            // Hbox container.
            this.container = oView.byId("hBoxPagination");
            // isPagination 체크.
            const isPagination = (this.totalRecordCount <= this.recordCountPerPage) ? false : true;

            return isPagination;
        },
        addPaginator: function () {
            this.container.destroyItems();
            // Number Button create
            addNumber(this.firstPageNoOnPageList, this.lastPageNoOnPageList, this.container);
            // Arrow Button create
            setFixedButtons(this.firstPageNoOnPageList, this.lastPageNoOnPageList
                , this.totalPageCount, this.container);

            const aButtons = this.container.getItems();
            for (let i = 0; i < aButtons.length; i++) {
                let oButton = aButtons[i];

                if (oButton.getText() === this.currentPageNo.toString()) {
                    oButton.setType("Emphasized");
                }
            }

        }
    }
});