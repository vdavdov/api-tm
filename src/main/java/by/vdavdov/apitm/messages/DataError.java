package by.vdavdov.apitm.messages;

import lombok.Data;

import java.util.Date;

@Data
public class DataError {
    private int status;
    private String message;
    private Date date;

    public DataError(int status, String message) {
        this.status = status;
        this.message = message;
        this.date = new Date();
    }
}
