package com.sinosoft.hanlin.common;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerAutoConfiguration {

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.OAS_30)
			.apiInfo(apiInfo())
			.securitySchemes(securitySchemes())
			.securityContexts(securityContext())
			.select().apis(RequestHandlerSelectors.basePackage("com.sinosoft.hanlin"))
			.build()
		;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(applicationName + " OpenAPI Document")
			.version("0.0.1-SNAPSHOT")
			.build()
		;
	}

	private List<SecurityScheme> securitySchemes() {
		return Collections.singletonList(
			HttpAuthenticationScheme.JWT_BEARER_BUILDER
				.name("Authorization")
				.scheme("bearer")
				.build())
		;
	}

	private List<SecurityContext> securityContext() {
		return Collections.singletonList(
			SecurityContext.builder()
				.securityReferences(defaultAuth())
				.build())
		;
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope
			= new AuthorizationScope("all", "accessEverything");
		return Collections.singletonList(
			SecurityReference.builder()
				.scopes(
					Collections.singletonList(authorizationScope).toArray(new AuthorizationScope[1]))
				.reference("Authorization")
				.build())
		;
	}

}
