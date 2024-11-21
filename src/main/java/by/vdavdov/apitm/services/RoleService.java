package by.vdavdov.apitm.services;

import by.vdavdov.apitm.model.entities.Role;
import by.vdavdov.apitm.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static by.vdavdov.apitm.constants.RestConstants.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName(USER).get();
    }
}
