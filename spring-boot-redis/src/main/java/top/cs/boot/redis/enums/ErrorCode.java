package top.cs.boot.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

//异常类型枚举
@AllArgsConstructor
@Getter
public enum ErrorCode {


    PARAMS_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "登录失败，请重新登录"),
    NOT_PERMISSION(403, "权限不足"),
    NOT_FOUND(404, "未找到该资源"),
    METHOD_ERROR(405, "方法不允许"),
    INTERNAL_SERVER_ERROR(500, "服务器异常，请稍后再试"),
    CODE_SEND_ERROR(3001, "验证码发送失败"),
    SMS_CODE_ERROR(3002, "验证码错误"),
    PHONE_ERROR(3003, "手机号格式错误");

    private final int code;
    private final String msg;

}
