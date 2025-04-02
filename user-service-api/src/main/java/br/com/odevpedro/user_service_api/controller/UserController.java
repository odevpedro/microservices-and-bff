package br.com.odevpedro.user_service_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import models.expections.StandartError;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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



    @Operation(summary = "Save new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
            @Content(mediaType = APPLICATION_JSON_VALUE,schema = @Schema(implementation = StandartError.class)
                    )),

            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = StandartError.class)
            ))
    })
    @PostMapping
    ResponseEntity<Void> save(
            @RequestBody final CreateUserRequest createUserRequest
    );

}