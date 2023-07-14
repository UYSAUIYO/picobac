package com.yuwen303.picobac.controller;

import com.yuwen303.picobac.entity.LoginResponse;
import com.yuwen303.picobac.entity.User;
import com.yuwen303.picobac.repository.UserRepository;
import com.yuwen303.picobac.utils.JwtUtil;
import com.yuwen303.picobac.utils.PassWordEncoderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YuWen
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final PassWordEncoderUtil passWordEncoderUtil;

    public AuthController (JwtUtil jwtUtil, UserRepository userRepository, PassWordEncoderUtil passWordEncoderUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passWordEncoderUtil = passWordEncoderUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userRequest){
        try {
            User user = userRepository.findByUsername(userRequest.getUsername());
            if (user == null) {
                return ResponseEntity.ok("用户不存在");
            }
            if (!passWordEncoderUtil.matches(userRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok("密码错误");
            }
            String token = jwtUtil.generateToken(user.getUsername ());
            return ResponseEntity.ok( new LoginResponse (token));
        }catch (Exception e){
            return ResponseEntity.ok("登录失败");
        }

    }

}
