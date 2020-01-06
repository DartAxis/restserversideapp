package ru.dartinc.restserversideapp1.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dartinc.restserversideapp1.model.User;
import ru.dartinc.restserversideapp1.service.UserService;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User result=userService.getUserByLogin(username);
        if(result==null){
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        log.info("IN JWTUserDetailsService - user found");
        return result;
    }
}
