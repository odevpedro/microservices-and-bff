package br.com.odevpedro.user_service_api;

import br.com.odevpedro.user_service_api.entity.User;
import br.com.odevpedro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.enums.ProfileEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class UserServiceApiApplication implements CommandLineRunner {

	private final UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(UserServiceApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	userRepository.save(new User(null, "odev pedro", "odevpedro@mail.com", "12345", Set.of(ProfileEnum.ROLE_TECHNICIAN)));
	}
}
