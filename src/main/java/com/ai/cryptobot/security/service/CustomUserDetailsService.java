package com.ai.cryptobot.security.service;

import com.ai.cryptobot.cryptobotUtils.ObjectsUtil;
import com.ai.cryptobot.security.MemberCustom;
import com.ai.cryptobot.v1.entity.MemberEntity;
import com.ai.cryptobot.v1.entity.RolesEntity;
import com.ai.cryptobot.v1.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberEntity memberEntity = memberRepository.getMemberEntitiesByUserNameAndActiveIsTrue(username);
        if (Objects.isNull(memberEntity)) {
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!ObjectsUtil.isNullOrEmpty(memberEntity.getRoles())) {
            for (RolesEntity role : memberEntity.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        }
        MemberCustom myMember = new MemberCustom(memberEntity.getUserName(), memberEntity.getPassword(),
                true, true, true, true, authorities);
        myMember.setFullName(memberEntity.getFullName());
        myMember.setEmail(memberEntity.getEmail());
        myMember.setActive(memberEntity.isActive());
        return myMember;
    }

}
