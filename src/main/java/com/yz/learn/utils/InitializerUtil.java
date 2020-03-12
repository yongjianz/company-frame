package com.yz.learn.utils;

import com.yz.learn.utils.jwt.JwtProperties;
import com.yz.learn.utils.jwt.JwtTokenUtil;
import org.springframework.stereotype.Component;

/**
 * 工具类初始化类
 * */

@Component
public class InitializerUtil {

    public InitializerUtil(JwtProperties jwtProperties) {
        JwtTokenUtil.setTokenSettings(jwtProperties);
    }
}
