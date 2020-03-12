package com.yz.learn.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean enable;

    @Bean
    public Docket createRestApi(){
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        ParameterBuilder refreshPar = new ParameterBuilder();
        tokenPar.name("authorization").
                description("wagger测试用(模拟authorization传入)非必填 header").
                modelRef(new ModelRef("string")).
                parameterType("header").
                required(false);
        refreshPar.name("refresh_token").
                description("swagger测试用(模拟刷新token传入)非必填 header").
                modelRef(new ModelRef("string")).
                parameterType("header").
                required(false);
        pars.add(tokenPar.build());
        pars.add(refreshPar.build());
        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(apiInfo()).
                select().
                apis(RequestHandlerSelectors.basePackage("com.yz.learn.controller")).
                //paths(Predicates.not(PathSelectors.regex("/error.*"))).
               // paths(PathSelectors.regex("/.*")).
                paths(PathSelectors.any())

                .build().
                globalOperationParameters(pars).
                enable(enable);



    }

    private ApiInfo apiInfo(){
       return new ApiInfoBuilder().
               title("通用后台系统").
               description("spring boot 实战系列").
               termsOfServiceUrl("").
               version("1.0").
               build();
    }
}
