package by.vdavdov.apitm.services;

import by.vdavdov.apitm.model.constants.Priority;
import by.vdavdov.apitm.model.constants.Status;
import by.vdavdov.apitm.model.dtos.NewTaskDto;
import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.model.entities.Task;
import by.vdavdov.apitm.repositories.TaskRepository;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    public ResponseEntity<?> createNewTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        Task task = new Task();
        task.setAssignee(userService
                .findByEmail(taskDto.getAssigneeEmail())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Assignee with email %s not found", taskDto.getAssigneeEmail())
                )));
        task.setDescription(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setStatus(Status.valueOf(taskDto.getStatus()));
        task.setAuthor(userService
                .findByEmail(jwtTokenUtils
                        .getEmail(request
                                .getHeader("Authorization")
                                .substring(7))).get());
        Task save = taskRepository.save(task);
        return ResponseEntity.ok(new NewTaskDto(
                String.valueOf(save.getId()),
                taskDto.getTitle(),
                taskDto.getDescription(),
                save.getAuthor().getEmail(),
                taskDto.getAssigneeEmail()
                ));
    }

}
