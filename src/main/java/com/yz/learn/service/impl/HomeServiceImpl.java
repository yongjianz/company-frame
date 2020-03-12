package com.yz.learn.service.impl;

import com.alibaba.fastjson.JSON;
import com.yz.learn.entity.SysUser;
import com.yz.learn.mapper.SysUserMapper;
import com.yz.learn.service.HomeService;
import com.yz.learn.service.PermissionService;
import com.yz.learn.vo.resp.HomeRespVO;
import com.yz.learn.vo.resp.PermissionRespNode;
import com.yz.learn.vo.resp.UserInfoRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PermissionService permissionService;
    @Override
    public HomeRespVO getHomeInfo(String userId) {
        HomeRespVO homeRespVo = new HomeRespVO();
        //String home = "[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[],\"id\":\"6\",\"title\":\"五级类目5-6\",\"url\":\"string\"}],\"id\":\"5\",\"title\":\"四级类目 4-5\",\"url\":\"string\"}],\"id\":\"4\",\"title\":\"三级类目3- 4\",\"url\":\"string\"}],\"id\":\"3\",\"title\":\"二级类目2- 3\",\"url\":\"string\"}],\"id\":\"1\",\"title\":\"类目1\",\"url\":\"string\"},{\"children\":[],\"id\":\"2\",\"title\":\"类目2\",\"url\":\"string\"}]";
//        String home="[\n" +
//                " {\n" +
//                " \"children\": [\n" +
//                " {\n" +
//                " \"children\": [],\n" +
//                " \"id\": \"3\",\n" +
//                " \"title\": \"菜单权限管理\",\n" +
//                " \"url\": \"/index/menus\"\n" +
//                " }\n" +
//                " ],\n" +
//                " \"id\": \"1\",\n" +
//                " \"title\": \"组织管理\",\n" +
//                " \"url\": \"string\"\n" +
//                " },\n" +
//                " {\n" +
//                " \"children\": [],\n" +
//                " \"id\": \"2\",\n" +
//                " \"title\": \"类目2\",\n" +
//                " \"url\": \"string\"\n" +
//                " }\n" +
//                "]";

        List<PermissionRespNode> list =permissionService.premissionTreeList(userId);
        homeRespVo.setMenus(list);
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        UserInfoRespVO respVO = new UserInfoRespVO();
        if(sysUser!=null){
            respVO.setUsername(sysUser.getUsername());
            respVO.setDeptName("");
            respVO.setId(sysUser.getId());
        }
        homeRespVo.setUserInfo(respVO);
        return homeRespVo;
    }
}
