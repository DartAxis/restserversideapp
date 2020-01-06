package ru.dartinc.restserversideapp1.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.dartinc.restserversideapp1.dto.AuthenticationRequestDto;
import ru.dartinc.restserversideapp1.model.User;
import ru.dartinc.restserversideapp1.model.UserDTO;
import ru.dartinc.restserversideapp1.security.JwtTokenProvider;
import ru.dartinc.restserversideapp1.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value="/restapi/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto){
        try{
            String username=requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,requestDto.getPassword()));
            User user=userService.getUserByLogin(username);

            if(user==null){
                throw new UsernameNotFoundException("User with username-"+username+" not found!!!");
            }
            String token=jwtTokenProvider.createToken(username,user.getRoles().stream().collect(Collectors.toList()));
            System.out.println("Выдан токен:");
            System.out.println(token);
            Map<Object,Object> response=new HashMap<>();
            response.put("username",username);
            response.put("token",token);
            UserDTO userDTO= UserDTO.fromUser(user);
            response.put("roles",userDTO.getRole());

            return ResponseEntity.ok(response);

        }catch(AuthenticationException e){
            throw new BadCredentialsException("Inalid username or password");
        }
    }
}
