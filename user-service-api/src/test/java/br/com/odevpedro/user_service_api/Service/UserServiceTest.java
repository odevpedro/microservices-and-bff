package br.com.odevpedro.user_service_api.Service;

import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import models.expections.RessourceNotFoundExpection;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;


    @Test
    void whenCallFindByIdWithValidIdThenReturnUserResponse(){

        when(repository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));
        final var response = service.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        verify(repository, Mockito.times(1)).findById(anyString());
        verify(mapper, times(1)).fromEntity(any(User.class));

    }


    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        try {
            service.findById("1");
        } catch (Exception e) {
            assertEquals(RessourceNotFoundExpection.class, e.getClass());
            assertEquals("Objects not found. Id: 1. Type: UserResponse", e.getMessage());
        }
        verify(repository, times(1)).findByEmail(anyString());
        verify(mapper, times(0)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {

        when(repository.findAll()).thenReturn(List.of(new User(), new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));
        
        final var response = service.findAll();

        assertNotNull(response);
        assertEquals(2, response.size()); // Esperado que size seja 1, como no teste original
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }


}