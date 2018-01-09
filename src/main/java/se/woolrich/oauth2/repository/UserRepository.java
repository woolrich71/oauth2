package se.woolrich.oauth2.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import se.woolrich.mongo.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

     Optional<User> findByUsername(String username);
     int deleteUserById(String id);

}
