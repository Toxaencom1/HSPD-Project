package com.taxah.hspd.repository.auth;


import com.taxah.hspd.entity.auth.Role;
import com.taxah.hspd.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoles(Roles role);
}
