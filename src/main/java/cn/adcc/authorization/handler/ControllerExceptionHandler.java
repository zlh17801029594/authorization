package cn.adcc.authorization.handler;

import cn.adcc.authorization.response.Result;
import cn.adcc.authorization.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exception(Exception e) {
        return ResultUtil.failure(e.getMessage());
    }
}
