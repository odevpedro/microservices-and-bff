package br.com.odevpedro.user_service_api.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import models.expections.RessourceNotFoundExpection;;
import models.expections.StandartError;
import models.expections.ValidationException;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerExceptionHandler {

    //No caso de buscar um id e não encontrar
    @ExceptionHandler(RessourceNotFoundExpection.class)
    ResponseEntity<?> handleNotFoundException (
            final RessourceNotFoundExpection ex, final HttpServletRequest request
    ) {
        return ResponseEntity.status( NOT_FOUND ).body(
                StandartError.builder()
                        .timeStamp(now())
                        .status(NOT_FOUND.value())
                        .error("Ressource not found")
                        .message(ex.getMessage())
                .path(request.getRequestURI())
                .build()
        );
    }

    //No caso de buscar um id e não encontrar
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> handleDataIntegrityViolationException (
            final DataIntegrityViolationException ex, final HttpServletRequest request
    ) {
        return ResponseEntity.status(CONFLICT).body(
                StandartError.builder()
                        .timeStamp(now())
                        .status(BAD_REQUEST.value())
                        .error(CONFLICT.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    //Caso falte algum argumento na criação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<StandartError> handleMethodArgumentNotValidException (final MethodArgumentNotValidException ex, final HttpServletRequest request) {
        var error = ValidationException.builder()
                .timeStamp(now())
                .status(BAD_REQUEST.value()).
                error("Validation Exception").
                message("Exception in validation attributes")
                .path(request.getRequestURI())
                .errors(new ArrayList<>())
                .build();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(error);


    }
}
