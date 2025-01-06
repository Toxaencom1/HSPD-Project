package com.taxah.hspd.repository;


import com.taxah.hspd.entity.Role;
import com.taxah.hspd.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoles(Roles role);
}
