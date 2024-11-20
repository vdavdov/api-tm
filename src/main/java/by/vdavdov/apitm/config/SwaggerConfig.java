package by.vdavdov.apitm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Manager API",
                description = "API для тестового задания",
                version = "1.0.0",
                contact = @Contact(
                        name = "Davydov Vsevolod",
                        email = "vdavdov.work@mail.ru"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearer-token")
        }
)
@SecurityScheme(
        name = "bearer-token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {


}