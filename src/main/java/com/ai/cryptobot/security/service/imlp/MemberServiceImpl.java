package com.ai.cryptobot.security.service.imlp;

import com.ai.cryptobot.security.service.MemberService;
import com.ai.cryptobot.v1.entity.MemberEntity;
import com.ai.cryptobot.v1.entity.RolesEntity;
import com.ai.cryptobot.v1.repository.MemberRepository;
import com.ai.cryptobot.v1.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberEntity saveUser(MemberEntity member) {
        log.info("save user {} to database", member.getUserName());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    @Override
    public RolesEntity saveRole(RolesEntity role) {
        log.info("save new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        log.info("add role {} to user {}", roleName, userName);
        MemberEntity memberEntity = memberRepository.findByUserName(userName);
        RolesEntity rolesEntity = roleRepository.findByName(roleName);
        memberEntity.getRoles().add(rolesEntity);
    }

    @Override
    public MemberEntity getAllUser(String userName) {
        return memberRepository.findByUserName(userName);
    }

    @Override
    public List<MemberEntity> getAllUser() {
        return memberRepository.findAll();
    }

}
