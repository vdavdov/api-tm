package by.vdavdov.apitm.services;

import by.vdavdov.apitm.constants.RestConstants;
import by.vdavdov.apitm.messages.DataError;
import by.vdavdov.apitm.messages.DataSuccess;
import by.vdavdov.apitm.model.dtos.JwtRequest;
import by.vdavdov.apitm.model.dtos.JwtResponse;
import by.vdavdov.apitm.model.dtos.RegistrationUserDto;
import by.vdavdov.apitm.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

@Transactional
@SpringBootTest
class AuthServiceTestIT extends BaseTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    private RegistrationUserDto registrationUserDto;

    @BeforeEach
    void setUp() {
        registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setPassword("password");
        registrationUserDto.setConfirmPassword("password");
    }

    @Test
    @Rollback
    void createNewUser_passwordNotMatchWithConfirmPassword_thenReturnBadRequest() {
        // given
        registrationUserDto.setConfirmPassword("notPassword");
        registrationUserDto.setEmail("email@mail.ru");

        // when
        ResponseEntity<?> response = authService.createNewUser(registrationUserDto);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertInstanceOf(DataError.class, response.getBody());
        DataError errorResponse = (DataError) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        Assertions.assertEquals(RestConstants.PASSWORD_NOT_MATCH, errorResponse.getMessage());
    }
    @Test
    @Rollback
    void createNewUser_butUserWithThisEmailAlreadyExists_thenReturnConflict() {
        // given
        registrationUserDto.setEmail("email1@mail.ru");
        userService.saveNewUser(registrationUserDto);

        // when
        ResponseEntity<?> response = authService.createNewUser(registrationUserDto);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertInstanceOf(DataError.class, response.getBody());
        DataError errorResponse = (DataError) response.getBody();
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatus());
    }

    @Test
    @Rollback
    void createNewUser_whenAllIsOk_returnCreated() {
        // given
        registrationUserDto.setEmail("email2@mail.ru");

        // when
        ResponseEntity<?> response = authService.createNewUser(registrationUserDto);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertInstanceOf(DataSuccess.class, response.getBody());
        DataSuccess success = (DataSuccess) response.getBody();
        Assertions.assertEquals(HttpStatus.CREATED.value(), success.getStatus());
    }

    @Test
    void createAuthToken_WithValidCredentials_ReturnsToken() throws Exception {
        // given
        registrationUserDto.setEmail("email4@mail.ru");
        userService.saveNewUser(registrationUserDto);
        JwtRequest creds = new JwtRequest("email4@mail.ru", "password");

        //when
        ResponseEntity<?> authToken = authService.createAuthToken(creds);
        JwtResponse jwtResponse = (JwtResponse) authToken.getBody();
        //then
        Assertions.assertNotNull(authToken);
        Assertions.assertEquals(HttpStatus.OK, authToken.getStatusCode());
        Assertions.assertNotNull(jwtResponse);
        Assertions.assertEquals(jwtTokenUtils.getEmail(jwtResponse.getToken()), "email4@mail.ru");
    }

    @Test
    void createAuthToken_WithInvalidCredentials_returnBadRequest() {
        //given
        registrationUserDto.setEmail("email5@mail.ru");
        userService.saveNewUser(registrationUserDto);
        JwtRequest creds = new JwtRequest("email5@mail.ru", "incorrectPassword");

        //when
        ResponseEntity<?> response = authService.createAuthToken(creds);
        DataError errorResponse = (DataError) response.getBody();
        //then
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals(RestConstants.WRONG_CREDENTIALS, errorResponse.getMessage());
    }
}