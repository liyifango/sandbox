package com.sinosoft.hanlin.sandbox.common;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
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
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

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


	@Bean
	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider) {
					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
				}
				return bean;
			}

			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
				List<T> copy = mappings.stream()
						.filter(mapping -> mapping.getPatternParser() == null)
						.collect(Collectors.toList());
				mappings.clear();
				mappings.addAll(copy);
			}

			@SuppressWarnings("unchecked")
			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
				try {
					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
					field.setAccessible(true);
					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}

}
