package by.vdavdov.apitm.api.v1;

import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "TaskController", description = "Needed for create/update tasks")
@RestController
@RequiredArgsConstructor
public class TaskRestController {
    private final TaskService taskService;

    @Operation(
            summary = "Создает новую задачу",
            description = "В качестве email автора использует емайл создателя"
    )
    @PostMapping("/api/v1/tasks")
    public ResponseEntity<?> createNewTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.createNewTask(taskDto, request);
    }

    @Operation(
            summary = "Изменить существующую задачу по айди",
            description = "Изменить существующую задачу по айди, задачу может менять только автор, исполнитель или админ"
    )
    @PutMapping("api/v1/tasks")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.updateTask(taskDto, request);
    }

    @Operation(
            summary = "Получить все задачи исполнителя/автора",
            description = "Получить все задачи исполнителя/автора по его емейлу, с пагинацией"
    )
    @GetMapping("api/v1/tasks")
    public ResponseEntity<?> getTasksByAssignee(@RequestParam String userEmail,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int limit,
                                                @RequestParam(required = false) String priority,
                                                @RequestParam(required = false) String status) {
        return taskService.getTasksByAssignee(userEmail, page, limit, priority, status);
    }


}
