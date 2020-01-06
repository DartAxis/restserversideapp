package ru.dartinc.restserversideapp1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private String login;
    private String address;
    private String role;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public User toUser() {
        User result=new User();
        result.setId(id);
        result.setName(name);
        result.setAddress(address);
        result.setLogin(login);
        result.setPassword(password);
        return result;
    }

    public static UserDTO fromUser(User user){
        UserDTO userDto=new UserDTO();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setAddress(user.getAddress());
        String roles=user.getRoles().toString().replace("[]", "");
        roles=roles.replaceAll("\\[", "");
        roles=roles.replaceAll("\\]","");
        userDto.setRole(roles);
        return userDto;
    }
}
