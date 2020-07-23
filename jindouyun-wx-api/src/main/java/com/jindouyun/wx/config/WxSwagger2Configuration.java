package com.jindouyun.wx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName WxSwagger2Configuration
 * @Description swagger在线文档配置<br>
 * 项目启动后可通过地址：http://host:port/swagger-ui.html 查看在线文档
 * @Author Bruce
 * @Date 2020/7/21 3:36 下午
 */
@Configuration
@EnableSwagger2
public class WxSwagger2Configuration {
    @Bean
    public Docket wxDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("wx")
                .apiInfo(wxApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jindouyun.wx.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo wxApiInfo() {
        return new ApiInfoBuilder()
                .title("jindouyun-wx API")
                .description("jindouyun小商场API")
                .build();
    }
}
