package se.woolrich.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import se.woolrich.mongo.Position;
import se.woolrich.mongo.Role;
import se.woolrich.mongo.User;
import se.woolrich.oauth2.repository.UserRepository;
import se.woolrich.oauth2.services.UserService;

import java.net.InetAddress;

@SpringBootApplication
@EnableAutoConfiguration
public class Oauth2Application implements CommandLineRunner  {

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(Oauth2Application.class, args);
	}


	public void run(String... args) throws Exception {

		if(!userRepository.findByUsername( "woolrich" ).isPresent()) {
			User user = new User();
			user.setUsername("woolrich");
			user.setPassword("abc123");
			Position position = new Position();
			position.setLat(57.707145f);
			position.setLng(11.967824f);
			user.setPosition(position);
			user.grantAuthority(Role.ROLE_ADMIN);
			userService.registerUser(user);
		}

		if(!userRepository.findByUsername( "user" ).isPresent()) {
			User user = new User();
			user.setUsername("user");
			user.setPassword("123xyz");
			userService.registerUser(user);
		}
		userRepository.findAll();
	}
}
