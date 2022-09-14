package com.homecomingday.service;

import com.homecomingday.controller.request.EmailRequestDto;
import com.homecomingday.controller.request.MailDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.util.MailHandler;
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
        dto.setAddress(emailSendRequestDto.getEmail());
        dto.setTitle("Homecoming Day 인증번호 안내 이메일 입니다.");
        dto.setMessage(authKey);
        // 유효 시간(5분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(authKey, emailSendRequestDto.getEmail(), 60 * 3L);

        return dto;
    }

    public void mailSend(MailDto mailDto){
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            SimpleMailMessage message = new SimpleMailMessage();
            mailHandler.setTo(mailDto.getAddress());
            mailHandler.setFrom(FROM_ADDRESS);
            mailHandler.setSubject(mailDto.getTitle());
            String htmlContent = "<p>안녕하세요. Homecoming Day입니다.</p> <p>인증번호는 " + mailDto.getMessage() + " 입니다. </p> <img style='width:800px;' src='cid:sample-img'>";
            mailHandler.setText(htmlContent, true);
            // 이미지 삽입
            mailHandler.setInline("sample-img", "static/background.png");

            mailHandler.send();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public ResponseDto<?> checkEmail(EmailRequestDto.AuthRequestDto authRequestDto) {
        String redisEmail = redisUtil.getData(authRequestDto.getAuthKey());
        if(!authRequestDto.getEmail().equals(redisEmail)){
            return ResponseDto.fail("UNREGISTERED_KEY", "인증번호가 일치하지 않습니다.");
           // throw new RuntimeException("인증코드가 일치하지 않습니다.");
        }
        return ResponseDto.success("인증번호가 일치합니다.");
    }
}