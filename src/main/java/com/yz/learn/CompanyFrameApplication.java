package com.yz.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.image.ImageProducer;
import java.util.HashMap;

@SpringBootApplication
@MapperScan("com.yz.learn.mapper")
public class CompanyFrameApplication {

    public static void main(String[] args) {

       SpringApplication.run(CompanyFrameApplication.class, args);
    }

}
