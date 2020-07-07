package com.reddit.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.reddit"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(metaData())
				.securitySchemes(Arrays.asList(apiKey()));
		
	}
	
	private ApiInfo metaData() {
		return new ApiInfoBuilder()
                .title("Reddit Clone REST APIs Documentation")
                .version("1.0")
                .description("API for Reddit Clone Application - Developed using spring framework and MySQL")
                .contact(new Contact("Abhishek Yadav", "https://www.linkedin.com/in/abhishek-yadav-b6b64b17a/", "xyz@email.com"))
                .license("Apache License Version 2.0")
                .build();
    }
	
	private ApiKey apiKey() {
	    return new ApiKey("jwtToken", "Authorization", "header");
	}

}
