package by.vdavdov.apitm.services;

import by.vdavdov.apitm.constants.RestConstants;
import by.vdavdov.apitm.messages.DataError;
import by.vdavdov.apitm.model.dtos.CommentDto;
import by.vdavdov.apitm.model.entities.Comment;
import by.vdavdov.apitm.model.entities.Task;
import by.vdavdov.apitm.repositories.CommentRepository;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static by.vdavdov.apitm.constants.RestConstants.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final TaskService taskService;

    public ResponseEntity<?> createComment(CommentDto commentDto, HttpServletRequest request) {
        String token = jwtTokenUtils.getTokenFromRequest(request);
        String authorEmail = jwtTokenUtils.getEmail(token);
        if (taskService.findTaskById(commentDto.getTaskId()).isPresent()) {
            Task task = taskService.findTaskById(commentDto.getTaskId()).get();
            if (userService.findByEmail(authorEmail).isPresent()) {
                if (jwtTokenUtils.getRoles(token).get(0).contains(ADMIN)) {
                    return saveComment(commentDto, authorEmail, task);
                } else if (authorEmail.equals(task.getAuthor().getEmail()) || authorEmail.equals(task.getAssignee().getEmail())) {
                    return saveComment(commentDto, authorEmail, task);
                } else {
                    return new ResponseEntity<>(new DataError(HttpStatus.FORBIDDEN.value(), IMPOSSIBLE_UPDATE_STRANGERS_TASKS), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), TASK_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<?> saveComment(CommentDto commentDto, String authorEmail, Task task) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUser(userService.findByEmail(authorEmail).get());

        task.getComments().add(comment);
        commentRepository.save(comment);
        taskService.save(task);
        return ResponseEntity.ok(commentDto);
    }

}
