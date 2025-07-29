package br.com.odevpedro.user_service_api.Service;

import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import models.enums.ProfileEnum;
import models.expections.RessourceNotFoundExpection;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    void whenCallFindByIdWithValidIdThenReturnUserResponse() {
        User user = new User("1", "User Test", "user@test.com", "pass", Set.of(ProfileEnum.ROLE_CUSTOMER));
        UserResponse response = new UserResponse("1", "User Test", "user@test.com", "pass", Set.of(ProfileEnum.ROLE_CUSTOMER));

        when(repository.findById(anyString())).thenReturn(Optional.of(user));
        when(mapper.fromEntity(any(User.class))).thenReturn(response);

        final var result = service.findById("1");

        assertNotNull(result);
        assertEquals(UserResponse.class, result.getClass());

        verify(repository).findById(anyString());
        verify(mapper).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        Exception e = assertThrows(RessourceNotFoundExpection.class, () -> service.findById("1"));
        assertEquals("Object not found. Id: 1, Type: UserResponse", e.getMessage());

        verify(repository).findById(anyString());
        verify(mapper, times(0)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {
        User user1 = new User("1", "User 1", "user1@email.com", "pass", Set.of(ProfileEnum.ROLE_CUSTOMER));
        User user2 = new User("2", "User 2", "user2@email.com", "pass", Set.of(ProfileEnum.ROLE_ADMIN));

        UserResponse response1 = new UserResponse("1", "User 1", "user1@email.com", "pass", Set.of(ProfileEnum.ROLE_CUSTOMER));
        UserResponse response2 = new UserResponse("2", "User 2", "user2@email.com", "pass", Set.of(ProfileEnum.ROLE_ADMIN));

        when(repository.findAll()).thenReturn(List.of(user1, user2));
        when(mapper.fromEntity(user1)).thenReturn(response1);
        when(mapper.fromEntity(user2)).thenReturn(response2);

        final var response = service.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());

        verify(repository).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    void whenCallSaveThenSuccess() {
        final var request = generateMock(CreateUserRequest.class);

        when(mapper.fromRequest(any())).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any(User.class))).thenReturn(new User());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        service.save(request);

        verify(mapper).fromRequest(request);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
        verify(repository).findByEmail(request.email());
    }

    @Test
    void whenCallSaveWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final var request = generateMock(CreateUserRequest.class);
        final var entity = generateMock(User.class);

        when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        Exception e = assertThrows(DataIntegrityViolationException.class, () -> service.save(request));
        assertEquals("Email already exists: " + request.email(), e.getMessage());

        verify(repository).findByEmail(request.email());
        verify(mapper, never()).fromRequest(request);
        verify(encoder, never()).encode(request.password());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
        final var request = generateMock(UpdateUserRequest.class);

        when(repository.findById(anyString())).thenReturn(Optional.empty());

        Exception e = assertThrows(RessourceNotFoundExpection.class, () -> service.update("1", request));
        assertEquals("Object not found. Id: 1, Type: UserResponse", e.getMessage());

        verify(repository).findById(anyString());
        verify(mapper, never()).update(any(), any());
        verify(encoder, never()).encode(request.password());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithInvalidEmailThenThrowDataIntegrityViolationException() {
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class);

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));

        Exception e = assertThrows(DataIntegrityViolationException.class, () -> service.update("1", request));
        assertEquals("Email already exists: " + request.email(), e.getMessage());

        verify(repository).findById(anyString());
        verify(repository).findByEmail(request.email());
        verify(mapper, never()).update(any(), any());
        verify(encoder, never()).encode(request.password());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithValidParamsThenGetSuccess() {
        final var id = "1";
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class).withId(id);

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));
        when(mapper.update(any(), any())).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(entity);

        UserResponse response = new UserResponse(
                id, "Updated Name", "updated@email.com", "encoded",
                Set.of(ProfileEnum.ROLE_CUSTOMER, ProfileEnum.ROLE_ADMIN)
        );
        when(mapper.fromEntity(entity)).thenReturn(response);

        final var updated = service.update(id, request);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.name());
        assertEquals("updated@email.com", updated.email());

        verify(repository).findById(id);
        verify(repository).findByEmail(request.email());
        verify(mapper).update(request, entity);
        verify(encoder).encode(request.password());
        verify(repository).save(entity);
        verify(mapper).fromEntity(entity);
    }
}
