package ru.dartinc.restserversideapp1.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dartinc.restserversideapp1.model.User;
import ru.dartinc.restserversideapp1.model.UserDTO;
import ru.dartinc.restserversideapp1.service.UserService;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restapi/users/")
@CrossOrigin
public class UsersRestController {

    private UserService userService;
    @Autowired
    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    // Получение пользователя по ID

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long userId) {
        //Если ID пользователя не передано возвращаем статус ошибки
        if (userId == null) {
            System.out.println("Айди пользователя не передано");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Если пользователь с таким ID не существует сообщаем что не найден
        User user = this.userService.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь с таким айди не найден");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //Если пользователь есть возвращаем его в JSON
        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.OK);
    }

    //Добавление пользователя

    @PostMapping("")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        //Если пользователь для передачи, со страницы не собран то возвращаем статус ошибки
        if (userDTO == null) {
            System.out.println("пользователь не собрнан из JSON");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println("Собрали нового юзера из JSON");
        System.out.println(userDTO);
        System.out.println("Добавляем пользователя");
        System.out.println(userDTO);
        this.userService.addUser(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    //Редактирование пользователя

    @PutMapping("")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println("Собрали нового юзера со страницы");
        System.out.println(userDTO);
        this.userService.updateUser(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Удаление пользователя

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {
        // Если ID пользователя пустой сообщаем что плохой запрос
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = this.userService.getUserById(userId);
        //Если пользователь с таким ID не найден возвращаем статус не найден
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //Если пользователь найден то удаляем его и возвращаем статус ОК
        this.userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Получение списка всех пользователей

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        System.out.println("Запрос списка всех юзеров");

        List<User> users = this.userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<UserDTO> usersDto=new ArrayList<>();
        for(User user:users){
           usersDto.add(UserDTO.fromUser(user));
        }
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
}
