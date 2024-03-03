package com.ai.cryptobot.security.controller;

import com.ai.cryptobot.security.service.MemberService;
import com.ai.cryptobot.v1.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getAllUser() {
        List<MemberEntity> lstUser = memberService.getAllUser().stream().limit(3).collect(Collectors.toList());
        return ResponseEntity.ok().body(lstUser);
    }
}
