package by.vdavdov.apitm.repositories;

import by.vdavdov.apitm.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByAssigneeId(Long assigneeId);
    Optional<Task> findTaskByAuthorId(Long authorId);
}
