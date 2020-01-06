package ru.dartinc.restserversideapp1.service;

import ru.dartinc.restserversideapp1.model.User;
import ru.dartinc.restserversideapp1.model.UserDTO;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByLogin(String login);

    void deleteUser(User user);

    boolean updateUser(UserDTO userDTO);

    boolean addUser(UserDTO userDTO);

    boolean isExistLogin(String login);

}