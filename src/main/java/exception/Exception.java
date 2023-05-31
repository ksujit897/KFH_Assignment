package exception;

import org.springframework.http.HttpStatus;

public class Exception extends RuntimeException {

    public Exception(String message) {
        super(message);
    }

}
