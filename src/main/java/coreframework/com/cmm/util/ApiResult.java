package coreframework.com.cmm.util;


import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

/**
 * description    :
 * packageName    : com.core.corework.projectname.util
 * fileName       : ApiResult
 * author         : yoonsikcha
 * date           : 2022/10/31
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/31        yoonsikcha       최초 생성
 */
@Getter
public class ApiResult<T> {

    private final boolean success;

    private final T list;

    private final T data;

    private final String  message;

    private final int statusCode;

    private final String errorMessage;

    public ApiResult(boolean success, T responseList, T responseData,  String responseMessage, int statusCode) {
        this(success, responseList, responseData, responseMessage, statusCode, null);

    }

    public ApiResult(boolean success, T responseList, T responseData, String responseMessage, int statusCode, String errorMessage) {

        this.success = success;
        this.list = responseList;
        this.data = responseData;
        this.message = responseMessage;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;

    }

    public static <T> ApiResult<T> List(T responseList, String responseMessage) {

        return List(responseList, responseMessage, 200);
    }

    public static <T> ApiResult<T> List(T responseList, String responseMessage, int statusCode) {
        return new ApiResult<>(true, responseList, null, responseMessage, 200);
    }


    public static <T> ApiResult<T> Data(T responseData, String responseMessage) {

        return Data(responseData, responseMessage, 200);
    }

    public static <T> ApiResult<T> Data(T responseData, String responseMessage, int statusCode) {
        return new ApiResult<>(true, null, responseData, responseMessage, 200);
    }

    public static <T> ApiResult<T> Message(String responseMessage) {

        return Message( responseMessage, 200);
    }

    public static <T> ApiResult<T> Message(String responseMessage, int statusCode) {
        return new ApiResult<>(true, null, null, responseMessage, 200);
    }

    public static <T> ApiResult<T> OK(T responseList, T responseData, String responseMessage) {

        return OK(responseList, responseData,responseMessage, 200);
    }

    public static <T> ApiResult<T> OK(T responseList, T responseData, String responseMessage, int statusCode) {
        return new ApiResult<>(true, responseList, responseData, responseMessage, statusCode);
    }


    public static<T> ApiResult<T> ERROR(String errorMessage, HttpStatus status) {
        return new ApiResult<>(false, null, null, "", status.value(), errorMessage);
    }

//    public static <T> ApiResult<T> OK2(T responseList, T responseList2, T responseData, String responseMessage) {
//        return OK(responseList, responseData,responseMessage, 200);
//    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("success", success)
                .append("responseList", list)
                .append("responseData", data)
                .append("responseMessage", message)
                .append("statusCode", statusCode)
                .append("errorMessage", errorMessage)
                .toString();

    }
}
