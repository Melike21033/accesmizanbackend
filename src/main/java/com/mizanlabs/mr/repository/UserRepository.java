package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
