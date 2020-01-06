package ru.dartinc.restserversideapp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dartinc.restserversideapp1.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select user from User user left join fetch user.roles where user.login=:login")
    User findByLogin(@Param("login") String login);

    @Query("select user from User user left join fetch user.roles where user.id=:id")
    User getById(@Param("id") Long id);

    //@Query("from User as user left join fetch user.roles order by user.id")
    @Query("select distinct u from User u join fetch u.roles order by u.id")
    List<User> findAll();
}