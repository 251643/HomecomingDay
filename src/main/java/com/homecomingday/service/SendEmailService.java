package com.homecomingday.service;

import com.homecomingday.controller.request.EmailRequestDto;
import com.homecomingday.controller.request.MailDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class SendEmailService {
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;


    public MailDto createMail( EmailRequestDto.EmailSendRequestDto emailSendRequestDto) {
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(999999) + 1);
        MailDto dto = new MailDto();
        dto.setAddress(emailSendRequestDto.getSendEmail());
        dto.setTitle("Homecoming Day 인증번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Homecoming Day입니다. 인증번호는 " + authKey + " 입니다. ");
        // 유효 시간(5분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(authKey, emailSendRequestDto.getSendEmail(), 60 * 3L);

        return dto;
    }

    public void mailSend(MailDto mailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }

    public ResponseDto<?> checkEmail(EmailRequestDto.AuthRequestDto authRequestDto) {
        String redisEmail = redisUtil.getData(authRequestDto.getAuthKey());
        if(!authRequestDto.getAuthEmail().equals(redisEmail)){
            return ResponseDto.fail("UNREGISTERED_KEY", "인증번호가 일치하지 않습니다.");
           // throw new RuntimeException("인증코드가 일치하지 않습니다.");
        }
        return ResponseDto.success("인증번호가 일치합니다.");
    }
}