package br.com.odevpedro.user_service_api.Service;


import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.expections.RessourceNotFoundExpection;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;

    public UserResponse findById(final String id) {
        return  userMapper.fromEntity(find(id));

    }

    public void save(CreateUserRequest createUserRequest) {
        verifyIfEmailAlreadyExists(createUserRequest.email(), null);
        userRepository.save(userMapper.fromRequest(createUserRequest)
                .withPassword(encoder.encode(createUserRequest.password())));

    }

    public UserResponse update(final String id, final UpdateUserRequest updateUserRequest) {

        User entity = find(id);
        verifyIfEmailAlreadyExists(updateUserRequest.email(), id);

        String password = updateUserRequest.password() != null ? encoder.encode(updateUserRequest.password()): entity.getPassword();

        User userUpdated =  userMapper.update(updateUserRequest, entity).withPassword(password);

        return userMapper.fromEntity(userRepository.save(userUpdated));

    }

    private void verifyIfEmailAlreadyExists(final String email, final String id){
        userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(id)).
                ifPresent(user -> { throw new DataIntegrityViolationException("Email already exists: " + email);
    });
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::fromEntity).toList();
                //vamos mapear cada usuário para dto
    }

    private User find(String id){
        return userRepository.findById(id).orElseThrow(() -> new RessourceNotFoundExpection(
                "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
        ) );
    }
}