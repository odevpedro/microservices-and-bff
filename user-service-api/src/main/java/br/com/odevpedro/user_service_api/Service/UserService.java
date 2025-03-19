package br.com.odevpedro.user_service_api.Service;


import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findById(final String id) {
        return repository.findById(id).orElse(null);
    }
}