package br.com.odevpedro.user_service_api.Service;


import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.expections.RessourceNotFoundExpection;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return  userMapper.fromEntity(userRepository.findById(id).orElseThrow( () -> new RessourceNotFoundExpection("Object not found" + id + ", Type: " +
                UserResponse.class.getSimpleName())));}

    public void save(CreateUserRequest createUserRequest) {
        verifyIfEmailAlreadyExists(createUserRequest.email(), null);
        userRepository.save(userMapper.fromRequest(createUserRequest));

    }

    private void verifyIfEmailAlreadyExists(final String email, final String id){
        userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(id)).
                ifPresent(user -> { throw new DataIntegrityViolationException("Email already exists: " + email);
    });
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.
                        fromEntity(user)).toList();
                //vamos mapear cada usuário para dto
    }
}