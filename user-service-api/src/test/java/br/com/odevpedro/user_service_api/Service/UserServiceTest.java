package br.com.odevpedro.user_service_api.Service;

import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    //criando componentes de mentira:
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;


    //cenário feliz
    @Test
    void whenCallFindByIdWithValidIdThenReturnUserResponse(){

        when(repository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));
        final var response = service.findById("1");

        assertNotNull(response);
        assertEquals(UserResponse.class, response.getClass());

        //É importante especificar quantas vezes o repository esperava ser chamado
        verify(repository, Mockito.times(1)).findById(anyString());
        verify(mapper, times(1)).fromEntity(any(User.class));

    }


}