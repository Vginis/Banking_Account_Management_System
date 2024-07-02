package com.bank.controller;


import com.bank.domain.User;
import com.bank.repository.UserRepository;
import com.bank.representation.UserMapper;
import com.bank.representation.UserRepresentation;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserMapper userMapper;
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/users")
    public ResponseEntity<?> findAllAccounts() {
        return new ResponseEntity<>(userMapper.toRepresentationList(userRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<?> findOneUser(@PathVariable Integer id){
        if(id==null){
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST);
        }
        if(!userRepository.existsById(id)){
            return new ResponseEntity<>("User doesn't exist", HttpStatus.NOT_FOUND);
        }
        User user = userRepository.getReferenceById(id);
        return new ResponseEntity<>(userMapper.userToRepresentation(user), HttpStatus.OK);
    }

    @GetMapping("/users/name/{username}")
    ResponseEntity<?> findUserByUsername(@PathVariable String username){
        if(username==null){
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByName1(username);
        if(user==null){
            return new ResponseEntity<>("User doesn't exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userMapper.userToRepresentation(user), HttpStatus.OK);
    }

    @PostMapping(value = "/users/new/{password}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewUser(@RequestBody UserRepresentation userRepresentation,@PathVariable String password) {
        if(userRepository.existsById(userRepresentation.userId)){
            return new ResponseEntity<>("There is another user with that id", HttpStatus.BAD_REQUEST);
        }
        String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&])[A-Za-z\\d!@#$%&]{8,20}$";
        if (!password.matches(regexPattern)) {
            return new ResponseEntity<>("Password is incompatiblie with our Password Policy!", HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userMapper.userRepresentationToModel(userRepresentation);
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
            return new ResponseEntity<>("User Created!", HttpStatus.CREATED);
        } catch (Exception p) {
            return new ResponseEntity<>("Something went wrong.Possible format error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/users/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateUser(@PathVariable("id") Integer userId,@RequestBody UserRepresentation userRepresentation) throws BadRequestException {
        if(userId ==null || userRepresentation==null){return new ResponseEntity<>("User or id is null", HttpStatus.BAD_REQUEST);}
        if(!userRepository.existsById(userId)){
            return new ResponseEntity<>("No User with that id", HttpStatus.NOT_FOUND);
        }
        User user1 = userRepository.getReferenceById(userId);
        User user = userMapper.userRepresentationToModel(userRepresentation);
        if (user1.getAccountList() == null) {
            user.setAccountList(new ArrayList<>());
        }

        user1.getAccountList().clear();
        user1.getAccountList().addAll(user.getAccountList());

        user1.setUserId(user.getUserId());
        user1.setAddress(user.getAddress());
        user1.setEmail(user.getEmail());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());

        userRepository.save(user1);

        return new ResponseEntity<>("User Updated!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/users/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteUser(@PathVariable("id") Integer userId){
        if(userId==null){return new ResponseEntity<>("userId is null", HttpStatus.BAD_REQUEST);}
        if(!userRepository.existsById(userId)){
            return new ResponseEntity<>("No User with that id", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(userId);
        return new ResponseEntity<>("User Deleted!", HttpStatus.NO_CONTENT);
    }
}
