package com.bank.manager;

import com.bank.constant.ErrorMessages;
import com.bank.domain.User;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.IllegalArgumentException;
import com.bank.exception.UnauthorizedException;
import com.bank.mapper.UserMapper;
import com.bank.repository.UserRepository;
import com.bank.representation.ChangePasswordDTO;
import com.bank.representation.user.CreateUserRepresentation;
import com.bank.representation.user.UpdateUserRepresentation;
import com.bank.representation.user.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserManager {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserMapper userMapper;
    @Autowired
    private PasswordEncoder encoder;

    public List<UserRepresentation> findAllUsers(){
        return userMapper.toRepresentationList(userRepository.findAll());
    }

    public UserRepresentation findUserById(Integer id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return userMapper.userToRepresentation(userOptional.get());
    }

    public UserRepresentation findUserByUsername(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return userMapper.userToRepresentation(userOptional.get());
    }

    public void createUser(CreateUserRepresentation createUserRepresentation){
        Optional<User> userOptional = userRepository.findByUsername(createUserRepresentation.username);
        if(userOptional.isPresent()){
            throw new IllegalArgumentException("user_already_exists");
        }

        String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&])[A-Za-z\\d!@#$%&]{8,20}$";
        if(!createUserRepresentation.password.matches(regexPattern)){
            throw new IllegalArgumentException(ErrorMessages.INVALID_PASSWORD);
        }

        User user = new User(createUserRepresentation);
        user.setPassword(encoder.encode(createUserRepresentation.password));
        userRepository.save(user);
    }

    public void updateUser(Integer userId, UpdateUserRepresentation updateUserRepresentation){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        User user = userOptional.get();
        user.updateUserDetails(updateUserRepresentation);
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordDTO changePasswordDTO){
        Optional<User> userOptional = userRepository.findByUsername(changePasswordDTO.username);
        if(userOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        User user = userOptional.get();
        String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&])[A-Za-z\\d!@#$%&]{8,20}$";

        if(!encoder.matches(changePasswordDTO.oldPassword, user.getPassword())){
            throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED);
        }

        if(!changePasswordDTO.newPassword.matches(regexPattern) ||
                Objects.equals(changePasswordDTO.newPassword, changePasswordDTO.oldPassword)){
            throw new IllegalArgumentException(ErrorMessages.INVALID_PASSWORD);
        }
        user.setPassword(encoder.encode(changePasswordDTO.newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Integer userId){
        if(!userRepository.existsById(userId)){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }
}
