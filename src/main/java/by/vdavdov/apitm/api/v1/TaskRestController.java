package by.vdavdov.apitm.api.v1;

import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "TaskController", description = "Needed for create/update tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskRestController {
    private final TaskService taskService;

    @Operation(
            summary = "Создает новую задачу",
            description = "В качестве email автора использует емайл создателя \n " +
                    "PRIORITY {LOW, MEDIUM, HIGH} \n " +
                    "STATUS {PENDING, IN_PROGRESS, COMPLETED} \n" +
                    " Пример запроса: \n" +
                    "{\n" +
                    "  \"title\": \"tittle\",\n" +
                    "  \"description\": \"description\",\n" +
                    "  \"status\": \"PENDING\",\n" +
                    "  \"priority\": \"HIGH\",\n" +
                    "  \"assigneeEmail\": \"assignee@gmail.com\"\n" +
                    "}",
            security = @SecurityRequirement(name = "bearer-token")
    )
    @PostMapping("/api/v1/tasks")
    public ResponseEntity<?> createNewTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.createNewTask(taskDto, request);
    }

    @Operation(
            summary = "Изменить существующую задачу по айди",
            description = "Изменить существующую задачу по айди, задачу может менять только автор, исполнитель или админ." +
                    "\n Пример запроса \n" +
                    "{\n" +
                    "  \"id\": 9,\n" +
                    "  \"title\": \"string\",\n" +
                    "  \"description\": \"string\",\n" +
                    "  \"status\": \"IN_PROGRESS\",\n" +
                    "  \"priority\": \"MEDIUM\",\n" +
                    "  \"assigneeEmail\": \"admin\",\n" +
                    "}",
            security = @SecurityRequirement(name = "bearer-token")
    )
    @PutMapping("api/v1/tasks")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.updateTask(taskDto, request);
    }

    @Operation(
            summary = "Получить все задачи исполнителя/автора",
            description = "Получить все задачи исполнителя/автора по его емейлу, с пагинацией",
            security = @SecurityRequirement(name = "bearer-token")
    )
    @GetMapping("api/v1/tasks")
    public ResponseEntity<?> getTasksByAssignee(@RequestParam String userEmail,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int limit,
                                                @RequestParam(required = false) String priority,
                                                @RequestParam(required = false) String status) {
        return taskService.getTasksByAssignee(userEmail, page, limit, priority, status);
    }

    @Operation(
            summary = "Удаляет задачу по айди из урла",
            description = "Позволяет удалять задачу по айди из урла, удалить может только админ",
            security = @SecurityRequirement(name = "bearer-token")
    )
    @DeleteMapping("api/v1/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, HttpServletRequest request) {
        return taskService.deleteTask(id, request);
    }

}
