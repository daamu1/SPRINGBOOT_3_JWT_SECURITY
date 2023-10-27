package com.saurabh.config;

import com.saurabh.enums.UserRole;
import com.saurabh.model.Role;
import com.saurabh.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class DataInitialization {
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitialization(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    @Transactional
    public void dataInitialization() {
        Role studentRole = new Role(UserRole.USER);
        Role parentRole = new Role(UserRole.ADMIN);
        Role teacherRole = new Role(UserRole.MANAGER);
        List<Role> roleList = new ArrayList<>();
        roleList.add(studentRole);
        roleList.add(teacherRole);
        roleList.add(parentRole);
        roleRepository.saveAll(roleList);

    }
}