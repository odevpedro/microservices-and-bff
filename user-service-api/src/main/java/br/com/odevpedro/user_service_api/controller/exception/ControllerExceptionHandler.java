package br.com.odevpedro.user_service_api.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import models.expections.RessourceNotFoundExpection;;
import models.expections.StandartError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RessourceNotFoundExpection.class)
    ResponseEntity<?> handleNotFoundException (
            final RessourceNotFoundExpection ex, final HttpServletRequest request
    ) {
        return ResponseEntity.status( NOT_FOUND ).body(
                StandartError.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(NOT_FOUND.value())
                        .error("Ressource not found")
                        .message(ex.getMessage())
                .path(request.getRequestURI())
                .build()
        );
    }
}
