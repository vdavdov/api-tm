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
    @PostMapping("/api/v1/task/create")
    public ResponseEntity<?> createNewTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.createNewTask(taskDto, request);
    }

    @Operation(
            summary = "Изменить существующую задачу по айди",
            description = "Изменить существующую задачу по айди, задачу может менять только автор, исполнитель или админ"
    )
    @PutMapping("api/v1/task/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.updateTask(taskDto, request);
    }

}
