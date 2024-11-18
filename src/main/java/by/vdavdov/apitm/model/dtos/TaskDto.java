package by.vdavdov.apitm.model.dtos;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assigneeEmail;
}
