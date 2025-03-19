package br.com.odevpedro.user_service_api.controller;

import br.com.odevpedro.user_service_api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/users")
public interface UserController {

    @GetMapping("/{id}")
    ResponseEntity<User> findById(@PathVariable(name = "id") final String id);
}