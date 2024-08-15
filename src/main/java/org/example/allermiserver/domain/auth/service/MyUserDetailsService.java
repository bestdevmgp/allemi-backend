package org.example.allermiserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.allermiserver.domain.auth.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var result = memberRepository.findByEmail(email);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("회원 정보가 일치하지 않습니다");
        }
        var user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getEmail().equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("관리자"));
        } else {
            authorities.add(new SimpleGrantedAuthority("일반유저"));
        }
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
