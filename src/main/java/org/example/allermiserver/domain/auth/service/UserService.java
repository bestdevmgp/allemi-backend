package org.example.allermiserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.allermiserver.domain.auth.dto.UserDTO;
import org.example.allermiserver.domain.auth.entity.UserEntity;
import org.example.allermiserver.domain.auth.repository.UserRepository;
import org.example.allermiserver.global.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Value("${spring.jwt.secret}")
    private String key;

    public ResponseEntity<?> register(UserDTO user) throws NoSuchAlgorithmException {
        if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email Already Exists", HttpStatus.CONFLICT);
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(HashUtil.sha256(user.getPassword()));
        return new ResponseEntity<>(userRepository.save(userEntity), HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(UserDTO user) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity == null) {
            return null;
        }
        String header_key;
        try {
            if (!HashUtil.matches(user.getPassword(), userEntity.getPassword())) {
                return new ResponseEntity<>("비밀번호 틀림", HttpStatus.BAD_REQUEST);
            }
            header_key = HashUtil.sha256(key);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(header_key, HttpStatus.OK);
    }
}
