package models.expections;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class StandartError {
    private LocalDateTime timeStamp;
    private Integer status;
    private String error;
    private String path;
    private String message;

}
