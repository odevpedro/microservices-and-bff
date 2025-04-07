package models.expections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@SuperBuilder
public class ValidationException extends StandartError {

    @Getter
    private List<FieldError> errors;

    @AllArgsConstructor
    @Getter
    private class FieldError {
        private String fieldName;
        private String message;

    }

    public void addError(String fieldname, final String message){
        this.errors.add(new FieldError(fieldname, message)); //criar uma lista de erro conforme não há conformidade
    }

}
