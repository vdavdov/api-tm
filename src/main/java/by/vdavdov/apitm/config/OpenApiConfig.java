package by.vdavdov.apitm.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Manager API",
                description = "API для тестового задания",
                version = "1.0.0",
                contact = @Contact(
                        name = "Davydov Vsevolod",
                        email = "vdavdov.work@mail.ru"
                )
        )
)
public class OpenApiConfig {
    // Конфигурация для Swagger
}