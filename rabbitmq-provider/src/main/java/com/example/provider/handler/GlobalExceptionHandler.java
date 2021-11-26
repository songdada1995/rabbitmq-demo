package com.example.provider.handler;

import com.example.provider.constants.ResultCode;
import com.example.provider.exception.BasicException;
import com.example.provider.utils.Responses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = BasicException.class)
    public Responses basicExceptionHandler(HttpServletRequest req, BasicException e) {
        Responses result = Responses.newInstance();
        result.failed(e.getMessage(), e.getCode());
        result.setData(e.getData());

        if (e.getOriginException() != null) {
            result.setExceptionStackTrace(BasicException.exceptionTrace(e.getOriginException()));
        }

        log.info(e.getMessage());
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Responses noHandlerFoundExceptionHandler(HttpServletRequest req, BasicException e) {
        Responses result = Responses.newInstance();
        String msg = "接口 [" + req.getRequestURI() + "] 不存在";
        result.failed(msg, ResultCode.Common.NOT_FOUND.code);

        log.warn(msg);
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Responses exceptionHandler(HttpServletRequest req, HandlerMethod handlerMethod, Exception e) {
        Responses result = Responses.newInstance();
        result.failed("接口 [" + req.getRequestURI() + "] 内部错误，请联系管理员", ResultCode.Common.INTERNAL_SERVER_ERROR.code);
        result.setExceptionStackTrace(BasicException.exceptionTrace(e));

        String message;
        if (handlerMethod instanceof HandlerMethod) {
            message = String.format(
                    "接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                    req.getRequestURI(),
                    handlerMethod.getBean().getClass().getName(),
                    handlerMethod.getMethod().getName(),
                    e.getMessage()
            );
        } else {
            message = e.getMessage();
        }

        log.error(message, e);
        return result;
    }

}
