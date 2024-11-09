package com.ceos20.instagram.domain.user.controller;

import com.ceos20.instagram.domain.user.dto.UserLoginRequestDto;
import com.ceos20.instagram.domain.user.dto.UserLoginResponseDto;
import com.ceos20.instagram.domain.user.dto.UserRegisterRequestDto;
import com.ceos20.instagram.domain.user.dto.UserRegisterResponseDto;
import com.ceos20.instagram.domain.user.service.UserService;
import com.ceos20.instagram.global.ResponseTemplate;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Controller", description = "유저 컨트롤러 \n 가입, 로그인, 조회 기능을 포함합니다.")
public class UserController {
    private final UserService userService;

    // 회원가입
    @Operation(summary = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<ResponseTemplate<UserRegisterResponseDto>> register(@Valid @RequestBody UserRegisterRequestDto requestDto,
                                                                              BindingResult bindingResult) {
        // DTO 필드 검증
        if (bindingResult.hasErrors()) {
            // 필드와 기본 메시지를 CustomException으로 던짐
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            // CustomException으로 필드 오류 전달
            throw new CustomException(ErrorCode.BAD_REQUEST, "잘못된 필드 형식입니다.", fieldErrors);
        }

        // 회원 정보 저장
        UserRegisterResponseDto responseDto = userService.register(requestDto);
        // ResponseTemplate을 사용해 응답 생성
        return ResponseTemplate.createTemplate(HttpStatus.CREATED, "회원가입 성공", responseDto);
    }


    // 로그인
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseTemplate<UserLoginResponseDto>> login(@Valid @RequestBody UserLoginRequestDto requestDto,
                                                                        BindingResult bindingResult) {
        // DTO 필드 검증
        if (bindingResult.hasErrors()) {
            // 필드와 기본 메시지를 CustomException으로 던짐
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            // CustomException으로 필드 오류 전달
            throw new CustomException(ErrorCode.BAD_REQUEST, "잘못된 필드 형식입니다.", fieldErrors);
        }
        // 로그인
        UserLoginResponseDto responseDto = userService.login(requestDto);

        // ResponseTemplate을 사용해 응답 생성
        return ResponseTemplate.createTemplate(HttpStatus.OK, "로그인 성공", responseDto);
    }

    // 아이디 중복 확인
    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/id/{username}")
    public ResponseEntity<ResponseTemplate<UserLoginResponseDto>> login(@PathVariable String username) {
        if(username.length() >=50){
            return ResponseTemplate.createTemplate(HttpStatus.OK, "50자가 넘는 아이디는 존재하지 않습니다.", null);
        }
        // 아이디 중복 확인
        if(userService.checkUsername(username)){
            return ResponseTemplate.createTemplate(HttpStatus.OK, "해당 아이디를 사용할 수 있습니다.", null);
        }

        return ResponseTemplate.createTemplate(HttpStatus.OK, "이미 사용 중인 아이디입니다.", null);
    }
}
