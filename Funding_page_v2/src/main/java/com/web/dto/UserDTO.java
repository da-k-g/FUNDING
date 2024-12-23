package com.web.dto;

import com.web.domain.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    private String username;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이어야 합니다.")
    private String password;

    
    
    // UserDTO -> User 변환
    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    // User -> UserDTO 변환
    public static UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword()) // 비밀번호는 보통 DTO에 포함되지 않음 (보안 문제)
                .build();
    }
    
}
