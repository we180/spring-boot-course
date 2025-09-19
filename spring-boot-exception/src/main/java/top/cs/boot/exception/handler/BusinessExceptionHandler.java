package top.cs.boot.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.cs.boot.exception.common.Result;
import top.cs.boot.exception.exception.BusinessException;

//AOP 切面处理
@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error(e.getMessage());
    }
}
