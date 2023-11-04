package com.javaproject.nfe114v17.user;

import com.javaproject.nfe114v17.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(int userId);
}
