package bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Webclient1Application {

	public static void main(String[] args) {
		SpringApplication.run(Webclient1Application.class, args);
	}

	 @Bean
	    public Docket swaggerPersonApi10() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("bank.controller"))
	                .paths(PathSelectors.any())
	                .build()
	                .apiInfo(new ApiInfoBuilder().version("1.0").title("Room Service API").description("Documentation User API v1.0").build());
	    }
}
