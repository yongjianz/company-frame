package com.yz.learn.controller;

import com.yz.learn.constants.Constant;
import com.yz.learn.service.HomeService;
import com.yz.learn.utils.DataResult;
import com.yz.learn.utils.jwt.JwtTokenUtil;
import com.yz.learn.vo.resp.HomeRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Api(tags = "首页数据")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    @ApiOperation(value ="获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JwtTokenUtil.getUserIdFromToken(accessToken);
        return DataResult.success(homeService.getHomeInfo(userId));
    }
}
