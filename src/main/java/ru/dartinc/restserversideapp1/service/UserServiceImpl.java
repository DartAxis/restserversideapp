package ru.dartinc.restserversideapp1.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dartinc.restserversideapp1.model.User;
import ru.dartinc.restserversideapp1.model.UserDTO;
import ru.dartinc.restserversideapp1.repository.RoleRepository;
import ru.dartinc.restserversideapp1.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //получение всех пользователей
    @Override
    public List<User> getAllUsers() {
        log.info("IN SERVICE - get all users");
        return userRepository.findAll();
    }

    //получение пользователя по ID
    @Override
    public User getUserById(long id) {
        User result=userRepository.getById(id);
        if(result==null){
            log.warn("IN SERVICE - can`t find user with id= {}",id);
            return null;
        }
        log.info("IN SERVICE - get user by id");
        return result;
    }

    //получение пользователя по Login
    @Override
    public User getUserByLogin(String login) {
        User result=userRepository.findByLogin(login);
        if(result==null){
            log.warn("IN SERVICE - can`t find user with login= {}",login);
            return null;
        }
        log.info("IN SERVICE - get user by login");
        return result;
    }

    //Удаление пользователя
    @Override
    @Transactional
    public void deleteUser(@NotNull User user) {
        User userDelete = getUserById(user.getId());
        log.info("IN SERVICE - delete user with id= {}",user.getId());
        userRepository.delete(userDelete);
        userRepository.flush();
    }

    //Добавление пользователя
    @Override
    public boolean addUser(UserDTO userDTO) {
//создаем полноценного юзера на основании данных от пользователя для передачи
        User user = new User();
        setRolesToUser(userDTO, user);

        if (!isExistLogin(user.getUsername())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            log.info("IN SERVICE - add user - {}",user);
            userRepository.saveAndFlush(user);
            return true;
        }
        return false;
    }

    //Редактирование пользователя
    @Override
    public boolean updateUser(@NotNull UserDTO userDTO) {
        User user = getUserById(userDTO.getId());
        if (user == null) {
            log.warn("IN SERVICE - can`t find user with id= {}",userDTO.getId());
            return false;
        }
        setRolesToUser(userDTO, user);
        log.info("IN SERVICE - update user - {}",user);
        userRepository.save(user);
        return true;
    }

    //Установка ролей пользователям
    private void setRolesToUser(@NotNull UserDTO userDTO, @NotNull User user) {
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        if(!user.getRoles().isEmpty()){
            user.getRoles().clear();
        }
        if (userDTO.getRole().contains("ROLE_ADMIN")) {
            user.getRoles().add(roleRepository.findById(2L));
            System.out.println("добавили роль админа");
            System.out.println(user);
        } else if (userDTO.getRole().contains("ROLE_USER")) {
            user.getRoles().add(roleRepository.findById(1L));
            System.out.println("добавили роль юзера");
            System.out.println(user);
        }
    }


    @Override
    @Transactional
    public boolean isExistLogin(String login) {
        User byLogin = userRepository.findByLogin(login);
        log.info("IN SERVICE - check username on exist");
        return byLogin != null;
    }
}