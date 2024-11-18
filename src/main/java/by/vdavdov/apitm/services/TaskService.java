package by.vdavdov.apitm.services;

import by.vdavdov.apitm.exceptions.DataError;
import by.vdavdov.apitm.model.constants.Priority;
import by.vdavdov.apitm.model.constants.Status;
import by.vdavdov.apitm.model.dtos.NewTaskDto;
import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.model.entities.Task;
import by.vdavdov.apitm.repositories.TaskRepository;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

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
                        .getEmail(getTokenFromRequest(request))).get());
        Task save = taskRepository.save(task);
        return ResponseEntity.ok(new NewTaskDto(
                String.valueOf(save.getId()),
                taskDto.getTitle(),
                taskDto.getDescription(),
                save.getAuthor().getEmail(),
                taskDto.getAssigneeEmail()
                ));
    }

    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String emailFromRequest = jwtTokenUtils.getEmail(token);
        String roleFromRequest = jwtTokenUtils.getRoles(token).get(0);

        Optional<Task> task = taskRepository.findById(taskDto.getId());
        if (task.isPresent()) {
            Task taskToUpdate = task.get();
            String assigneeEmailFromTask = task.get().getAssignee().getEmail();
            String authorEmailFromTask = task.get().getAuthor().getEmail();

            if (Objects.equals(emailFromRequest, assigneeEmailFromTask) || Objects.equals(emailFromRequest, authorEmailFromTask) || Objects.equals(roleFromRequest, "ROLE_ADMIN")) {
                taskToUpdate.setDescription(taskDto.getDescription());
                taskToUpdate.setTitle(taskDto.getTitle());
                taskToUpdate.setPriority(Priority.valueOf(taskDto.getPriority()));
                taskToUpdate.setStatus(Status.valueOf(taskDto.getStatus()));

                if (userService.findByEmail(assigneeEmailFromTask).isPresent()) {
                    taskToUpdate.setAssignee(userService.findByEmail(taskDto.getAssigneeEmail()).get());
                    taskRepository.save(taskToUpdate);
                } else {
                    return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), "Assignee not found"), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new DataError(HttpStatus.FORBIDDEN.value(), "You can't change strangers tasks"), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), "Task not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new NewTaskDto(String.valueOf(taskDto.getId()),
                taskDto.getTitle(),
                taskDto.getDescription(),
                task.get().getAuthor().getEmail(),
                taskDto.getAssigneeEmail()));
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

}
