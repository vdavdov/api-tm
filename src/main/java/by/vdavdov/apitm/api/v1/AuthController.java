package by.vdavdov.apitm.api.v1;

import by.vdavdov.apitm.model.dtos.JwtRequest;
import by.vdavdov.apitm.model.dtos.RegistrationUserDto;
import by.vdavdov.apitm.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController", description = "Needed for create jwt token and new user")
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Получение токена JWT",
            description = "Позволяет получить токен jwt, необходимо предоставить емайл и пароль"
    )
    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя по данным из пост запроса"
    )
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }
}
