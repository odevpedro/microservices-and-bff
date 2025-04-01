package br.com.odevpedro.user_service_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import models.expections.StandartError;
import models.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Tag(name = "UserController", description = "Controller responsible for user operations")
@RequestMapping("/api/users")
public interface UserController {



    @Operation(summary = "Find user by id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(
                    responseCode = "404", description = "User not found",
                    content = @Content
                                (mediaType = "application/json",
                                schema = @Schema(implementation = StandartError.class)
            )),
            @ApiResponse(
                    responseCode = "500", description = "Internal Server Error",
                    content = @Content
                            (mediaType = "application/json",
                                    schema = @Schema(implementation = StandartError.class)
            ))
    })


    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(
            @Parameter(description = "User id", required = true,example = "67d9cdef62d0f253a6adb1ac")
            @PathVariable(name = "id") final String id);
}