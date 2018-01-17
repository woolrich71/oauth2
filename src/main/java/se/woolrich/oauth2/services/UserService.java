package se.woolrich.oauth2.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.woolrich.mongo.Role;
import se.woolrich.mongo.User;
import se.woolrich.oauth2.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername( s );
        if ( user.isPresent() ) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", s));
        }
    }
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername( username );
        if ( user.isPresent() ) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
        }
    }



    public User registerUser(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.grantAuthority(Role.ROLE_USER);
            return userRepository.save( user );
    }

    @Transactional // To successfully remove the date @Transactional annotation must be added
    public boolean removeAuthenticatedUser() throws UsernameNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findUserByUsername(username);
        int del = userRepository.deleteUserById(user.getId());
        return del > 0;
    }
}
