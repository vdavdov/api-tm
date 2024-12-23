package by.vdavdov.apitm.repositories;

import by.vdavdov.apitm.model.entities.Task;
import by.vdavdov.apitm.model.enums.Priority;
import by.vdavdov.apitm.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
    SELECT t FROM Task t
    JOIN t.assignee u
    WHERE u.email = :userEmail
    AND (:priority IS NULL OR :priority = '' OR t.priority = :priority)
    AND (:status IS NULL OR :status = '' OR t.status = :status)
    """)
    Page<Task> findTasksByUserEmail(@Param("userEmail") String userEmail,
                                        @Param("priority") Priority priority,
                                        @Param("status") Status status,
                                        Pageable pageable);

    void deleteTaskById(Long id);

}
