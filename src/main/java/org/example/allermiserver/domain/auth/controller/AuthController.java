package org.example.allermiserver.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.allermiserver.Member;
import org.example.allermiserver.domain.auth.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/member")
    public String addMember(
            String email,
            String password
    ) {
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return "redirect:/login";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login.html";
    }


}
