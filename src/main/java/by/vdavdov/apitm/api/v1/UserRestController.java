package by.vdavdov.apitm.api.v1;

import by.vdavdov.apitm.model.entities.User;
import by.vdavdov.apitm.services.UserService;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "Needed for read or delete users (only for admins)")
@RestController
//@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @Operation(
            summary = "Получение всех пользователей для администратора",
            description = "Пользователь с правами администратора может получить всех пользователей"
    )
    @GetMapping("/api/v1/users")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        return userService.getAllUsers(request);
    }

    @Operation(
            summary = "Удаление пользователя по айди, доступно только для админа",
            description = "Позволяет удалить пользователя по айди из урла"
    )
    @DeleteMapping("/api/v1/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, HttpServletRequest request) {
        return userService.deleteUserById(id, request);
    }

}
