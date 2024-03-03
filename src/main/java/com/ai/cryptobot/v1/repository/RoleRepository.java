package com.ai.cryptobot.v1.repository;

import com.ai.cryptobot.v1.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, String> {
    RolesEntity findByName(String name);
}
