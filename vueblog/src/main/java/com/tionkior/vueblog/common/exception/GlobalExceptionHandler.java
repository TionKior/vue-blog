package com.tionkior.vueblog.common.exception;

import com.tionkior.vueblog.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : TionKior
 * @date : 2022/1/19 17:22
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理实体校验异常 @NotBlank()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常:------------------------{}", e);
        // 简化错误信息,没有很多的异常,只有错误的信息
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return Result.fail(objectError.getDefaultMessage());
    }

    // 处理Shiro异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e) {
        log.error("运行时异常:------------------------{}", e);
        return Result.fail(401, e.getMessage(), null);
    }

    // 处理运行时异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常:------------------------{}", e);
        return Result.fail(e.getMessage());
    }

    // 断言异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalAccessError.class)
    public Result handler(IllegalAccessError e) {
        log.error("Assert时异常:------------------------{}", e);
        return Result.fail(e.getMessage());
    }

}
