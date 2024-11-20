package by.vdavdov.apitm.services;

import by.vdavdov.apitm.constants.RestConstants;
import by.vdavdov.apitm.messages.DataError;
import by.vdavdov.apitm.messages.DataSuccess;
import by.vdavdov.apitm.model.dtos.RegistrationUserDto;
import by.vdavdov.apitm.model.dtos.UserDto;
import by.vdavdov.apitm.model.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;

class UserServiceTestIT extends BaseTest {

    private final String jwtTokenAdmin = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiZXhwIjoxODE4NDY4MzY3LCJpYXQiOjE3MzIwNjgzNjd9.95NmVFGt_eK18LEU8_S3XshPcNaoNLq9niLpC-t11Lg";
    private final String jwtTokenUser = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdXRob3IiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiZXhwIjoxODE4NDY4NzY0LCJpYXQiOjE3MzIwNjg3NjR9.mbtH8RT50O0rLLOb0vHo4G4ng2sghyzW_mCWKQDid6M";

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    RegistrationUserDto registrationUserDto;
    RegistrationUserDto registrationUserDto1;

    @BeforeEach
    void setUp() {
        registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setEmail("1");
        registrationUserDto.setPassword("1");
        registrationUserDto.setConfirmPassword("1");
        registrationUserDto1 = new RegistrationUserDto();
        registrationUserDto1.setEmail("2");
        registrationUserDto1.setPassword("2");
        registrationUserDto1.setConfirmPassword("2");
    }

    @Test
    @Rollback
    void getAllUsers_ifRequestFromAdmin_thenReturnAllUsers() {
        //given
        authService.createNewUser(registrationUserDto);
        authService.createNewUser(registrationUserDto1);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(jwtTokenAdmin);
        //when
        ResponseEntity<?> allUsers = userService.getAllUsers(request);
        List<User> listOfUsers = (List<User>) allUsers.getBody();
        //then
        Assertions.assertNotNull(listOfUsers);
        Assertions.assertEquals(HttpStatus.OK, allUsers.getStatusCode());
        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");

    }

    @Test
    @Rollback
    void getAllUsers_ifRequestNotFromAdmin_thenReturnForbidden() {
        //given
        authService.createNewUser(registrationUserDto);
        authService.createNewUser(registrationUserDto1);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(jwtTokenUser);
        //when
        ResponseEntity<?> allUsers = userService.getAllUsers(request);
        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, allUsers.getStatusCode());
        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
    }

    @Test
    @Rollback
    void deleteUserById_whenAdminDeleteAndUserExists_thenReturnRemoveUser() {
        //given
        registrationUserDto.setEmail("test1");
        ResponseEntity<?> newUser = authService.createNewUser(registrationUserDto);
        DataSuccess dataSuccess = (DataSuccess) newUser.getBody();
        UserDto userDto = (UserDto) dataSuccess.getMessage();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(jwtTokenAdmin);
        //when
        ResponseEntity<?> response = userService.deleteUserById(userDto.getId(), request);
        //then
        Assertions.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Assertions.assertNotNull(userDto);
    }

    @Test
    @Rollback
    void deleteUserById_whenAdminDeleteAndUserNotExists_thenReturnNotFound() {
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(jwtTokenAdmin);
        //when
        ResponseEntity<?> response = userService.deleteUserById(4L, request);
        DataError dataError = (DataError) response.getBody();
        //then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(dataError);
        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Assertions.assertEquals(RestConstants.USER_NOT_FOUND, dataError.getMessage());
    }

    @Test
    @Rollback
    void deleteUserById_whenNotAdminDeleteUser_thenReturnForbidden() {
        //given
        registrationUserDto.setEmail("test2");
        ResponseEntity<?> newUser = authService.createNewUser(registrationUserDto);
        DataSuccess dataSuccess = (DataSuccess) newUser.getBody();
        UserDto userDto = (UserDto) dataSuccess.getMessage();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(jwtTokenUser);
        //when
        ResponseEntity<?> response = userService.deleteUserById(userDto.getId(), request);
        DataError dataError = (DataError) response.getBody();
        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Mockito.verify(request, Mockito.times(1)).getHeader("Authorization");
        Assertions.assertNotNull(dataError);
        Assertions.assertEquals(RestConstants.HAVE_NOT_PERMISSION, dataError.getMessage());
    }
}