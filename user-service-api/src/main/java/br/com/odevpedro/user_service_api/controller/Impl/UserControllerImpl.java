package br.com.odevpedro.user_service_api.controller.Impl;

import br.com.odevpedro.user_service_api.Service.UserService;
import br.com.odevpedro.user_service_api.controller.UserController;
import br.com.odevpedro.user_service_api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<User> findById(String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    
}