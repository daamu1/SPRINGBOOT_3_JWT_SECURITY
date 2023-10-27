package com.saurabh.repository;

import com.saurabh.enums.UserRole;
import com.saurabh.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);

}
