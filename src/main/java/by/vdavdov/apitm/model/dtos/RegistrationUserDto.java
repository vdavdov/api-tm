package by.vdavdov.apitm.model.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class RegistrationUserDto {
    private String username;
    private String email;
    private Date creationTime;
    private String password;

}
