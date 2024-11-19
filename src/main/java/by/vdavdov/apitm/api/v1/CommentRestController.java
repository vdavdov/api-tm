package by.vdavdov.apitm.api.v1;

import by.vdavdov.apitm.model.dtos.CommentDto;
import by.vdavdov.apitm.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CommentController", description = "Needed for CRUD comment")
@RestController
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @Operation(
            summary = "Позволяет добавлять комментарии к задаче",
            description = "Метод позволяет комментировать задачи, в которых пользователь назначен автором или исполнителем(админу можно)"
    )
    @PostMapping("/api/v1/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        return commentService.createComment(commentDto, request);
    }

}
