package com.bank.controller;


import com.bank.constant.SuccessMessages;
import com.bank.manager.UserManager;
import com.bank.representation.ChangePasswordDTO;
import com.bank.representation.user.CreateUserRepresentation;
import com.bank.representation.user.UpdateUserRepresentation;
import com.bank.representation.user.UserRepresentation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserManager userManager;

    @GetMapping("/users")
    public ResponseEntity<List<UserRepresentation>> findAllUsers() {
        return new ResponseEntity<>(userManager.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<UserRepresentation> findOneUser(@PathVariable @NotNull Integer id){
        return new ResponseEntity<>(userManager.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/users/name/{username}")
    ResponseEntity<UserRepresentation> findUserByUsername(@PathVariable @NotBlank String username){
        return new ResponseEntity<>(userManager.findUserByUsername(username), HttpStatus.OK);
    }

    @PostMapping(value = "/users/new", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createNewUser(@RequestBody @Valid CreateUserRepresentation createUserRepresentation) {
        userManager.createUser(createUserRepresentation);
        return new ResponseEntity<>(SuccessMessages.USER_CREATED, HttpStatus.OK);
    }

    @PutMapping(value = "/users/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateUser(@PathVariable("id") @NotNull Integer userId,@RequestBody @Valid UpdateUserRepresentation userRepresentation) throws BadRequestException {
        userManager.updateUser(userId, userRepresentation);
        return new ResponseEntity<>(SuccessMessages.USER_UPDATED, HttpStatus.NO_CONTENT);
    }


    @PutMapping("/users/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO){
        userManager.changePassword(changePasswordDTO);
        return new ResponseEntity<>("Password Updated!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/users/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUser(@PathVariable("id") Integer userId){
        userManager.deleteUser(userId);
        return new ResponseEntity<>(SuccessMessages.USER_DELETED, HttpStatus.NO_CONTENT);
    }
}
