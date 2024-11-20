package by.vdavdov.apitm.services;

import by.vdavdov.apitm.messages.DataError;
import by.vdavdov.apitm.messages.DataSuccess;
import by.vdavdov.apitm.model.dtos.JwtRequest;
import by.vdavdov.apitm.model.dtos.JwtResponse;
import by.vdavdov.apitm.model.dtos.RegistrationUserDto;
import by.vdavdov.apitm.model.dtos.UserDto;
import by.vdavdov.apitm.model.entities.User;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static by.vdavdov.apitm.constants.RestConstants.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new DataError(HttpStatus.UNAUTHORIZED.value(), WRONG_CREDENTIALS), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new DataError(HttpStatus.BAD_REQUEST.value(), PASSWORD_NOT_MATCH), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new DataError(HttpStatus.CONFLICT.value(), USER_ALREADY_EXISTS), HttpStatus.CONFLICT);
        }
        User user = userService.saveNewUser(registrationUserDto);
        return new ResponseEntity<>(new DataSuccess(HttpStatus.CREATED.value(), new UserDto(user.getId(), user.getEmail())), HttpStatus.CREATED);
    }
}
