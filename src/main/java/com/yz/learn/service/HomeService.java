package com.yz.learn.service;

import com.yz.learn.vo.resp.HomeRespVO;

public interface HomeService {

    HomeRespVO getHomeInfo(String userId);
}
