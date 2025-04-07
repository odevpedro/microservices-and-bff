package br.com.odevpedro.user_service_api.controller.Impl;

import br.com.odevpedro.user_service_api.Service.UserService;
import br.com.odevpedro.user_service_api.controller.UserController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> findById(final String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    //Implementação funcional
    public ResponseEntity<UserResponse> findByIdo(final String id) {
        return Optional.ofNullable(userService.findById(id))
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Override
    public ResponseEntity<Void> save( @Valid final CreateUserRequest createUserRequest) {
        userService.save(createUserRequest);
        return ResponseEntity.status(CREATED.value()).build();
    }


}