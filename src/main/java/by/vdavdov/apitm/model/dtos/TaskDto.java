package by.vdavdov.apitm.model.dtos;

import by.vdavdov.apitm.model.entities.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assigneeEmail;
    private String authorEmail;
    private Collection<CommentDto> comments;
}
