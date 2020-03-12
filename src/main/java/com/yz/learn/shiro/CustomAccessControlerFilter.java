package com.yz.learn.shiro;

import com.alibaba.fastjson.JSON;
import com.yz.learn.constants.Constant;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomAccessControlerFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)  {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info(request.getMethod());
        log.info(request.getRequestURI());
        log.info(request.getRequestURL().toString());
        try {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken)){
            throw new BusinessException(BaseResponseCode.TOKEN_IS_NLL);
        }
        CustomPasswordToken customPasswordToken = new CustomPasswordToken(accessToken);
            getSubject(servletRequest, servletResponse).login(customPasswordToken);
        }catch (BusinessException e){
            customRsponse(e.getCode(),e.getDefaultMessage(),servletResponse);
            return false;
        }catch (AuthenticationException e){
            if(e.getCause() instanceof BusinessException){
                BusinessException exception= (BusinessException) e.getCause();
                customRsponse(exception.getCode(),exception.getDefaultMessage(),servletResponse);
            }else {
                customRsponse(BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getCode(),BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getMsg(),servletResponse);
            }
            return false;
        }

        return true;
    }

    private void customRsponse(int code, String defaultMessage, ServletResponse response) {
        DataResult result = DataResult.getResult(code, defaultMessage);
        response.setContentType("application/json;charset=utf-8");
        String json = JSON.toJSONString(result);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(json.getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            log.error("eror={}",e);
        }


    }
}
