package by.vdavdov.apitm.services;

import by.vdavdov.apitm.model.dtos.CommentDto;
import by.vdavdov.apitm.model.entities.Comment;
import by.vdavdov.apitm.model.entities.Role;
import by.vdavdov.apitm.model.entities.Task;
import by.vdavdov.apitm.model.entities.User;
import by.vdavdov.apitm.model.enums.Priority;
import by.vdavdov.apitm.model.enums.Status;
import by.vdavdov.apitm.repositories.TaskRepository;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Collections;

@Transactional
@AutoConfigureMockMvc
class CommentServiceTestIT extends BaseTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    private CommentService commentService;

    private RoleService roleService;

    private CommentDto commentDto;
    private String token;
    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("email@mail.com");
        user.setPassword("password");
        Collection<Role> roles = Collections.singleton(new Role());
        user.setRoles(roles);

        commentDto = new CommentDto();
        commentDto.setTaskId(1L);
        commentDto.setAuthor("email@mail.ru");
        commentDto.setText("text");

        task = new Task();
        task.setId(1L);
        task.setAssignee(user);
        Collection<Comment> comments = Collections.EMPTY_LIST;
        task.setComments(comments);
        task.setStatus(Status.COMPLETED);
        task.setPriority(Priority.HIGH);
        task.setDescription("description");
        task.setTitle("tittle");
        token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiZXhwIjoxNzMyMDU2NzIzLCJpYXQiOjE3MzIwNTM3MjN9.hFRtW_FkEyWvKJdcw7OEixXYs0YBdxc3yWACm8LuP8g";
    }

    @Test
    void createComment_whenTaskWithThisIdNotFound_thenReturnNotFound() throws Exception {

    }
}