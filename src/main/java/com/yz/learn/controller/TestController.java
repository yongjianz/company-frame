package com.yz.learn.controller;

import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
@Api(value = "测试接口")
public class TestController {

    @GetMapping("/index")
    @ApiOperation("引导页接口")
    public String testResult(){
        return "hello swagger1";
    }

    @GetMapping("/ssy")
    public String he(){
        return "sss";
    }

    @GetMapping("/home")
    public DataResult<String> getHome(){
        int i = 9/0;
        return DataResult.success("hahaha");
    }

    @GetMapping("/business/error")
    public DataResult<String> testBusinessError(@RequestParam String type ){
        if (type.equals("1")||type.equals("2")) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        return DataResult.success();
    }


}
