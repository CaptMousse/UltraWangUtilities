package wang.ultra.my_utilities.common.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;
    public CommonException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
