package com.example.demo.controller;

import com.example.demo.dto.SignRequest;
import com.example.demo.dto.SignResponse;
import com.example.demo.entity.Member;
import com.example.demo.service.EmailService;
import com.example.demo.service.SignService;
import com.example.demo.unit.OAuthService;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService memberService;
    private final OAuthService oAuthService;
    private final EmailService emailService;

    @Operation(operationId = "login", summary = "로그인", description = "요청을 검토한뒤 로그인", tags = "SignController")
    @PostMapping(value = "/login")
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @Operation(operationId = "register", summary = "회원가입", description = "요청을 검토한뒤 회원가입", tags = "SignController")
    @PostMapping(value = "/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @Operation(operationId = "myinfo", summary = "내정보 보기", description = "요청을 검토한뒤 회원수정", tags = "SignController")
    @GetMapping("/myinfo")
    public ResponseEntity<SignResponse> myinfo(Authentication authentication) throws Exception {
        System.out.println(authentication.getName());
        return new ResponseEntity<>(memberService.getMember(authentication.getName()), HttpStatus.OK);
    }

    @Operation(operationId = "updatemyinfo", summary = "회원 정보 수정", description = "요청을 검토한뒤 회원 정보 수정", tags = "SignController")
    @PostMapping("/updatemyinfo")
    public ResponseEntity<Boolean> updatemyinfo(@RequestBody SignRequest request, Authentication authentication) throws Exception {
        System.out.println(authentication.getName());
        System.out.println(request.toString());
        return new ResponseEntity<>(memberService.update(request, authentication.getName()), HttpStatus.OK);
    }

    @Operation(operationId = "새로운 비밀번호 이메일 전송", summary = "새로운 비밀번호 이메일 전송", description = "요청을 검토한뒤 새로운 비밀번호)", tags = "SignController")
    @PostMapping("/sendNewPassword")
    public ResponseEntity<Boolean> sendNewPassword(@RequestParam String email) throws Exception {
        return new ResponseEntity<>(memberService.sendNewPassword(email), HttpStatus.OK);
    }

    @GetMapping("/api/oauth2/callback/google")
    @ResponseBody
    public ResponseEntity<String> successGoogleLogin(@RequestParam("code") String accessCode) {
        return oAuthService.getGoogleAccessToken(accessCode);
    }

    @Operation(operationId = "checkCertificationMail", summary = "이메일 인증번호 확인", description = "요청을 검토한뒤 이메일 인증번호 확인", tags = "EmailController")
    @GetMapping("/v1/email/certification/check")
    @ResponseBody
    public ResponseEntity<Boolean> checkCertificationMail(@RequestParam String code, @RequestParam String email) {
        try {
            if (emailService.getData(code).equals(email)) {
                emailService.deleteData(code);

                memberService.updateActivated(email);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

}
