package by.vdavdov.apitm.api.v1;

import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TaskController", description = "Needed for create/update tasks")
@RestController
@RequiredArgsConstructor
public class TaskRestController {
    private final TaskService taskService;

    @Operation(
            summary = "Создает новую задачу",
            description = "В качестве email автора использует емайл создателя"
    )
    @PostMapping("/api/v1/createTask")
    public ResponseEntity<?> createNewTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        return taskService.createNewTask(taskDto, request);
    }

}
