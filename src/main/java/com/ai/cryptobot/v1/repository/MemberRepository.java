package com.ai.cryptobot.v1.repository;

import com.ai.cryptobot.v1.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ADMIN
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    MemberEntity getMemberEntitiesByUserNameAndActiveIsTrue(String userName);

    MemberEntity findByUserName(String userName);

}
