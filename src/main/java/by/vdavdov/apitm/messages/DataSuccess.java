package by.vdavdov.apitm.messages;

import lombok.Data;

import java.util.Date;

@Data
public class DataSuccess {
    private int status;
    private Object message;
    private Date date;

    public DataSuccess(int status, Object message) {
        this.status = status;
        this.message = message;
        this.date = new Date();
    }
}
