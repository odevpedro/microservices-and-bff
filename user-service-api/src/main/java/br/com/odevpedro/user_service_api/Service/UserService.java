package br.com.odevpedro.user_service_api.Service;


import br.com.odevpedro.user_service_api.mapper.UserMapper;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.expections.RessourceNotFoundExpection;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return  userMapper.fromEntity(userRepository.findById(id).orElseThrow( () -> new RessourceNotFoundExpection("Object not found" + id + ", Type: " +
                UserResponse.class.getSimpleName())));}

    public void save(CreateUserRequest createUserRequest) {
        userRepository.save(userMapper.fromRequest(createUserRequest));

    }
}