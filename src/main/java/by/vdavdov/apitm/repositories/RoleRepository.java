package by.vdavdov.apitm.repositories;

import by.vdavdov.apitm.model.entities.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository {
    Optional<Role> findByName(String name);
}
