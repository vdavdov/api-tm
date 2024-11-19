package by.vdavdov.apitm.model.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Long taskId;
    private String author;
}

