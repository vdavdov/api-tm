package by.vdavdov.apitm.model.dtos;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String email;
    private String confirmPassword;
    private String password;
}
