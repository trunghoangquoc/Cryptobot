package com.ai.cryptobot.security.dto;

import com.ai.cryptobot.security.MemberCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ADMIN
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimsToken {
    private String username;
    private String fullName;
    private String email;
    private boolean active;
    private List<String> roles = new ArrayList<>();

    public ClaimsToken(MemberCustom principal){
        BeanUtils.copyProperties(principal, this);
    }
}
