package com.ai.cryptobot.security.service;

import com.ai.cryptobot.v1.entity.MemberEntity;
import com.ai.cryptobot.v1.entity.RolesEntity;

import java.util.List;

public interface MemberService {

    MemberEntity saveUser(MemberEntity user);

    RolesEntity saveRole(RolesEntity role);

    void addRoleToUser(String userName, String roleName);

    MemberEntity getAllUser(String userName);

    List<MemberEntity> getAllUser();
}
