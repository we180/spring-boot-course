package top.cs.boot.config.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int code;
    private final String msg;
    private final T data;

    public ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String msg,T data) {
        return new ApiResponse<>(200, msg, data);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(400, msg, null);
    }
}
