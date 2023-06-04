package wang.ultra.my_utilities.common.filter;

import org.springframework.http.HttpStatus;
import wang.ultra.my_utilities.common.exception.CommonException;

public class CurrentLimitingException extends CommonException {

    public CurrentLimitingException() {
        super("当前访问过多, 请稍后再试! ", HttpStatus.TOO_MANY_REQUESTS);
    }
}
