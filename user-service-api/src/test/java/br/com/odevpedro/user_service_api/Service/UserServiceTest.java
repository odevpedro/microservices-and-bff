package br.com.odevpedro.user_service_api.Service;

import br.com.odevpedro.user_service_api.creator.CreatorUtils;
import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import models.expections.RessourceNotFoundExpection;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static br.com.odevpedro.user_service_api.creator.CreatorUtils.generateMock;
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
        when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));
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
        assertEquals(2, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    void whenCallSaveThenSuccess() {
        // Arrange
        final var request = generateMock(CreateUserRequest.class);

        when(mapper.fromRequest(any())).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any(User.class))).thenReturn(new User());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        service.save(request);

        // Assert
        verify(mapper).fromRequest(request);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
        verify(repository).findByEmail(request.email());
    }

    @Test
    void whenCallSaveWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final var request = CreatorUtils.generateMock(CreateUserRequest.class);
        final var entity = CreatorUtils.generateMock(User.class);

        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.of(entity));

        var exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> service.save(request)
        );

        assertEquals("Email already exists: " + request.email(), exception.getMessage());

        verify(repository).findByEmail(request.email());
        verify(mapper, times(0)).fromRequest(request);
        verify(encoder, times(0)).encode(request.password());
        verify(repository, times(0)).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
        // Arrange
        final var request = CreatorUtils.generateMock(UpdateUserRequest.class);

        when(repository.findById(anyString()))
                .thenReturn(Optional.empty());

        // Act + Assert
        try {
            service.update("1", request);
            fail("Deveria lançar ResourceNotFoundException");
        } catch (Exception e) {
            assertEquals(RessourceNotFoundExpection.class, e.getClass());
            assertEquals("Object not found. Id: 1, Type: UserResponse", e.getMessage());
        }

        // Verificações
        verify(repository).findById(anyString());
        verify(mapper, times(0)).update(any(), any());
        verify(encoder, times(0)).encode(request.password());
        verify(repository, times(0)).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidEmailThenThrowDataIntegrityViolationException() {
        // Arrange
        final var request = CreatorUtils.generateMock(UpdateUserRequest.class);
        final var entity = CreatorUtils.generateMock(User.class);

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));

        when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        // Act + Assert
        try {
            service.update("1", request);
            fail("Deveria lançar DataIntegrityViolationException");
        } catch (Exception e) {
            assertEquals(DataIntegrityViolationException.class, e.getClass());
            assertEquals("Email already exists: " + request.email() , e.getMessage());
        }

        verify(repository).findById(anyString());
        verify(repository).findByEmail(request.email());
        verify(mapper, times(0)).update(any(), any());
        verify(encoder, times(0)).encode(request.password());
        verify(repository, times(0)).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithValidParamsThenGetSuccess() {

        final var id = "1";
        final var request = CreatorUtils.generateMock(UpdateUserRequest.class);
        final var entity = CreatorUtils.generateMock(User.class).withId(id);

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));
        when(mapper.update(any(), any())).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(entity);


        service.update("1", request);

        verify(repository).findById(anyString());
        verify(repository).findByEmail(request.email());
        verify(mapper).update(request, entity);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
    }


}