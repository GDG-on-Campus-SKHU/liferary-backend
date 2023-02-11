package gdsc.skhu.liferary.configure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("gdsc-skhu-liferary")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI liferaryOpenAPI() {
        return new OpenAPI().info(
                new Info().title("Liferary API")
                        .description("GDSC SKHU Solution Challenge Liferary API Docs")
                        .version("v0.0.1")
        );
    }
}
