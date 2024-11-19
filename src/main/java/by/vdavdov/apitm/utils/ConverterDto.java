package by.vdavdov.apitm.utils;

import by.vdavdov.apitm.model.dtos.CommentDto;
import by.vdavdov.apitm.model.dtos.TaskDto;
import by.vdavdov.apitm.model.entities.Comment;
import by.vdavdov.apitm.model.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConverterDto {

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(comment.getUser().getEmail());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    private TaskDto mapToDto(Task task) {
        List<CommentDto> commentDtos = task.getComments().stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList());

        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setAssigneeEmail(task.getAssignee().getEmail());
        taskDto.setAuthorEmail(task.getAuthor().getEmail());
        taskDto.setStatus(task.getStatus().name());
        taskDto.setPriority(task.getPriority().name());
        taskDto.setComments(commentDtos);

        return  taskDto;
    }

    public Page<TaskDto> convertToDtoPage(Page<Task> taskPage) {
        List<TaskDto> taskDtos = taskPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(taskDtos, taskPage.getPageable(), taskPage.getTotalElements());
    }

}
