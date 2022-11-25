package co.edu.iudigital.sendelapa.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

public class SwaggerConfig {
    //http://localhost:8082/api/v1/v2/api-docs

    //http://localhost:8082/api/v1/swagger-ui.html
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("co.edu.iudigital.app.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                ;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "API Envios El apa",
                "API To management sends and transports from sends el apa",
                "1.0",
                "https://www.iudigital.edu.co/",
                new Contact("Daniel Lopera", "https://www.iudigital.edu.co/", "daniel.lopera@est.iudigital.edu.co"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );
    }
}
