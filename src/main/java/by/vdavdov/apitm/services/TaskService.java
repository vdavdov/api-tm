package by.vdavdov.apitm.services;

import by.vdavdov.apitm.messages.DataError;
import by.vdavdov.apitm.model.enums.Priority;
import by.vdavdov.apitm.model.enums.Status;
import by.vdavdov.apitm.model.dtos.NewTaskDto;
import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.model.entities.Task;
import by.vdavdov.apitm.repositories.TaskRepository;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import by.vdavdov.apitm.utils.ConverterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

import static by.vdavdov.apitm.constants.RestConstants.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final ConverterDto converterDto;

    public ResponseEntity<?> createNewTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        Task task = new Task();
        try {
            task.setAssignee(userService
                    .findByEmail(taskDto.getAssigneeEmail())
                    .orElseThrow(() -> new RuntimeException(
                            String.format("Assignee with email %s not found", taskDto.getAssigneeEmail())
                    )));
        } catch (RuntimeException e) {
            return  new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        task.setDescription(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setStatus(Status.valueOf(taskDto.getStatus()));
        if (userService.findByEmail(jwtTokenUtils.getEmail(jwtTokenUtils.getTokenFromRequest(request))).isPresent()) {
            task.setAuthor(userService
                    .findByEmail(jwtTokenUtils
                            .getEmail(jwtTokenUtils.getTokenFromRequest(request))).get());
        } else {
            return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        Task save = taskRepository.save(task);
        return ResponseEntity.ok(new NewTaskDto(
                String.valueOf(save.getId()),
                taskDto.getTitle(),
                taskDto.getDescription(),
                save.getAuthor().getEmail(),
                taskDto.getAssigneeEmail()
                ));
    }

    public ResponseEntity<?> updateTask(TaskDto taskDto, HttpServletRequest request) {
        String token = jwtTokenUtils.getTokenFromRequest(request);
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
                    return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), ASSIGNEE_NOT_FOUND), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new DataError(HttpStatus.FORBIDDEN.value(), IMPOSSIBLE_UPDATE_STRANGERS_TASKS), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), TASK_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new NewTaskDto(String.valueOf(taskDto.getId()),
                taskDto.getTitle(),
                taskDto.getDescription(),
                task.get().getAuthor().getEmail(),
                taskDto.getAssigneeEmail()));
    }

    public ResponseEntity<?> getTasksByAssignee(String userEmail,
                                                int page,
                                                int size,
                                                String priority,
                                                String status) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Priority priorityEnum = (priority == null || priority.isEmpty()) ? null : Priority.valueOf(priority.toUpperCase());
        Status statusEnum = (status == null || status.isEmpty()) ? null : Status.valueOf(status.toUpperCase());

        Page<Task> tasks = taskRepository.findTasksByUserEmail(userEmail, priorityEnum, statusEnum, pageRequest);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), TASK_NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(converterDto.convertToDtoPage(tasks).getContent());
    }

    @Transactional
    public ResponseEntity<?> deleteTask(Long id, HttpServletRequest request) {
        String token = jwtTokenUtils.getTokenFromRequest(request);

        if(jwtTokenUtils.getRoles(token).contains(ADMIN)) {
            if(taskRepository.findById(id).isPresent()) {
                Task task = taskRepository.findById(id).get();
                taskRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } else {
                return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), TASK_NOT_FOUND), HttpStatus.NOT_FOUND);
            }
        } return new ResponseEntity<>(new DataError(HttpStatus.FORBIDDEN.value(), HAVE_NOT_PERMISSION), HttpStatus.FORBIDDEN);
    }



    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

}
