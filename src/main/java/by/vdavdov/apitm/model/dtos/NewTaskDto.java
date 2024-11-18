package by.vdavdov.apitm.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewTaskDto {
    private String id;
    private String tittle;
    private String description;
    private String authorEmail;
    private String assigneeEmail;
}
