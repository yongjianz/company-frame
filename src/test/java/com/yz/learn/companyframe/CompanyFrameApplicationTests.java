package com.yz.learn.companyframe;

import com.yz.learn.constants.Constant;
import com.yz.learn.entity.SysUser;
import com.yz.learn.mapper.SysUserMapper;
import com.yz.learn.service.RedisService;
import com.yz.learn.utils.jwt.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyFrameApplicationTests {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void contextLoads() {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey("9a26f5f1-cbd2-473d-82db-1d6dcf4598f4");
        redisService.set(sysUser.getUsername(),sysUser);
    }

    @Test
    public void testJwt(){
        HashMap<String,Object> map = new HashMap<>();
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add("user:list");
        permissionList.add("user:add");
        map.put(Constant.JWT_USER_NAME,"yongjianz");
        map.put("permission",permissionList);
        String token = JwtTokenUtil.getAccessToken("87876056",map);
        System.out.println(token);
    }

    @Test
    public void testLocker() throws InterruptedException {


        Thread threadA = new Thread(() -> {
            System.out.println("线程A "+redisService.lock("code"));
            System.out.println("线程A "+redisService.lock("code"));
        });
        Thread threadB = new Thread(() -> {
            while(!redisService.lock("code")){
                System.out.println("线程B 获取不到"+redisService.lock("code"));
            }
            System.out.println("线程B "+redisService.lock("code"));
        });
        threadA.start();
        Thread.sleep(500);
        threadB.start();
        Thread.currentThread().join();
        //System.out.println(redisService.lock("code"));



    }

}
