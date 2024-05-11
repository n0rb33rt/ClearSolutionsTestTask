package com.norbert.clearsolutions.repository;

import com.norbert.clearsolutions.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
