package by.vdavdov.apitm.services;

import by.vdavdov.apitm.messages.DataError;
import by.vdavdov.apitm.model.dtos.RegistrationUserDto;
import by.vdavdov.apitm.model.entities.User;
import by.vdavdov.apitm.repositories.UserRepository;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtTokenUtils jwtTokenUtils;

    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        String token = jwtTokenUtils.getTokenFromRequest(request);
        if (Objects.equals(jwtTokenUtils.getRoles(token).get(0), "ROLE_ADMIN")) {
            return ResponseEntity.ok(userRepository.findAll());
        }
        return new ResponseEntity<>(new DataError(HttpStatus.FORBIDDEN.value(), "You have not permission to access this resource"), HttpStatus.FORBIDDEN);
    }

    public User saveNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User %s not found", email)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    @Transactional
    public ResponseEntity<?> deleteUserById(Long id, HttpServletRequest request) {
        String token = jwtTokenUtils.getTokenFromRequest(request);
        if (userRepository.findById(id).isPresent()) {
            if (jwtTokenUtils.getRoles(token).get(0).contains("ROLE_ADMIN")) {
                userRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            }
        } else {
            return new ResponseEntity<>(new DataError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new DataError(HttpStatus.FORBIDDEN.value(), "You have not permission to delete this resource"), HttpStatus.FORBIDDEN);
    }
}
