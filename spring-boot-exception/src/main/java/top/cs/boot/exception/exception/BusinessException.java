package top.cs.boot.exception.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cs.boot.exception.enums.ErrorCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    private int code;
    private String msg;

    public BusinessException(String msg) {
        this.code= ErrorCode.SERVER_ERROR.getCode();
        this.msg= msg;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code= errorCode.getCode();
        this.msg= errorCode.getMsg();
    }

    public BusinessException(String msg, Throwable e) {
        this.code= ErrorCode.SERVER_ERROR.getCode();
        this.msg= msg;
    }
}
