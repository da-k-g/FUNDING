package com.web;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodingTest {
    public static void main(String[] args) {
    	 PasswordEncoder encoder = new BCryptPasswordEncoder();
    	    System.out.println(encoder.encode("admin123")); // 암호화된 비밀번호 출력
    }
}
