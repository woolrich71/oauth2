package se.woolrich.oauth2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.woolrich.mongo.User;
import se.woolrich.oauth2.services.UserService;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('REGISTER')")
    @PostMapping("/api/user/register")
    public ResponseEntity<User> registerAccount(@RequestBody User user) {
        user = userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @DeleteMapping("/api/user/remove")
    public ResponseEntity<GeneralController.RestMsg> removeAccount(Principal principal){
        boolean isDeleted = userService.removeAuthenticatedUser();
        if ( isDeleted ) {
            return new ResponseEntity<>(
                    new GeneralController.RestMsg(String.format("[%s] removed.", principal.getName())),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<GeneralController.RestMsg>(
                    new GeneralController.RestMsg(String.format("An error ocurred while delete [%s]", principal.getName())),
                    HttpStatus.BAD_REQUEST
            );
        }
    }



}
