package exception;

import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class ExceptionHandler {
    public static EntityNotFoundException handleEntityNotFoundException(String entityName, Long entityId) {
        String errorMessage = entityName + " not found with id: " + entityId;
        return new EntityNotFoundException(errorMessage);
    }
}
