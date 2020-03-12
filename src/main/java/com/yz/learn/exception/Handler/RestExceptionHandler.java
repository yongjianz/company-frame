package com.yz.learn.exception.Handler;

import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public <T> DataResult<T> handlerBusinessException(BusinessException e){
        log.error("Exception,exception:{}", e);
        return DataResult.getResult(e.getCode(), e.getDefaultMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) 
        <T> DataResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e); 
        List<ObjectError> errors = e.getBindingResult().getAllErrors(); 
        return createValidExceptionResp(errors);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public <T> DataResult<T> handlerUnauthorizedException(UnauthorizedException e){
        log.error("Exception,exception:{}", e);
        return DataResult.getResult(BaseResponseCode.AUTHORIZED_ERROR);
    }
    //@ExceptionHandler({Exception.class, DataAccessException.class})
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> DataResult<T> handlerException(Exception e){
        log.error("Exception,exception11:{}", e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_BUSY);
    }

    private <T> DataResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        for (int i = 0; i < errors.size(); i++) {
            msgs[i] = errors.get(i).getDefaultMessage();
        }
        return DataResult.getResult(BaseResponseCode.METHOD_IDENTITY_ERROR, msgs);
    }
}
