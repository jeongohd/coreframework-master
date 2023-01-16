sap.ui.define([], function () {

    const fnList = function (url, data) {
        return $.ajax({
            method: "GET",
            url: url,
            headers: {"Content-Type": "application/json"},
            dataType: "JSON",
            data: data,
            success: function (resultData) {
                return resultData;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 에러 로그는 아래처럼 확인해볼 수 있다.
                alert("error");
            },
        });
    };
    const fnDetail = function (url, data) {
        return $.ajax({
            method: "GET",
            url: url,
            headers: {"Content-Type": "application/json"},
            dataType: "JSON",
            data: data,
            success: function (resultData) {
                return resultData;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 에러 로그는 아래처럼 확인해볼 수 있다.
                alert("error");
            },
        });
    };
    const fnCreate = function (url, data) {
        return $.ajax({
            method: "POST",
            url: url,
            headers: {"Content-Type": "application/json"},
            dataType: "JSON",
            data: data,
            success: function (resultData) {
                return resultData;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 에러 로그는 아래처럼 확인해볼 수 있다.
                alert("error");
            },
        });
    };
    const fnDelete = function (url, data) {
        return $.ajax({
            method: "POST",
            url: url,
            headers: {"Content-Type": "application/json"},
            data: data,
            dataType: "JSON",
            success: function (resultData) {
                return resultData;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 에러 로그는 아래처럼 확인해볼 수 있다.
                alert("error");
            },
        });
    };
    const fnUpdate = function (url, data) {
        return $.ajax({
            method: "PUT",
            url: url,
            headers: {"Content-Type": "application/json"},
            dataType: "JSON",
            data: data,
            success: function (resultData) {
                return resultData;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                // 에러 로그는 아래처럼 확인해볼 수 있다.
                alert("error");
            },
        });
    };

    return {
        init: function (properties) {
            this.extend(properties);
            return Promise.resolve(this.action());
        },
        extend: function (properties) {
            this.ajaxActionType = properties.actionType || "";
            this.ajaxUrl = properties.url || "";
            this.ajaxData = properties.data || "";
        },
        action: function () {
            switch (this.ajaxActionType) {
                case "list": {
                    return fnList(this.ajaxUrl, this.ajaxData);
                }
                case "detail": {
                    return fnDetail(this.ajaxUrl, this.ajaxData);
                }
                case "create": {
                    return fnCreate(this.ajaxUrl, this.ajaxData);
                }
                case "delete": {
                    return fnDelete(this.ajaxUrl, this.ajaxData);
                }
                case "update": {
                    return fnUpdate(this.ajaxUrl, this.ajaxData);
                }
            }
        },
    };
});
