package top.cs.boot.exception.service;

import org.springframework.stereotype.Service;
import top.cs.boot.exception.exception.BusinessException;

@Service
public class ExceptionService {
    public void unAuthorizedError()
    {
        throw new BusinessException("权限不足");
    }

    public void systemError()
    {
        throw new BusinessException("系统异常");
    }
}
