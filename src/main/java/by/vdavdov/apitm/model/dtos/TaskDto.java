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
    private String authorEmail;

    public TaskDto(Long id, String title, String description, String status, String priority, String assigneeEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assigneeEmail = assigneeEmail;
    }
}
