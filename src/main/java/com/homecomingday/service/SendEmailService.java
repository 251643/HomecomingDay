package com.homecomingday.service;

import com.homecomingday.controller.request.EmailRequestDto;
import com.homecomingday.controller.request.MailDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.util.ClassPathResourceReader;
import com.homecomingday.util.RedisUtil;
import com.homecomingday.util.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SendEmailService {
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    private final String NUMBER_PATH = "static/logo.png";


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
          //  String htmlContent = "<p>안녕하세요. Homecoming Day입니다.</p> <p>인증번호는 " + mailDto.getMessage() + " 입니다. </p> <img style='width:800px;' src='cid:sample-img'>";
            String htmlContent = "<div style='width:50%; display: inline-block; margin: 0 25% 0 25%;'>"
                                 //  + "<img style='width:200px;' float:left; src='cid:sample-img'>"
                                   + "<p style='font-weight:600;font-size:25px;'>Homecoming Day</p>"
                                   + "<hr style='border: 0; height: 1px; background: #ccc; >"
                                   + "<div style='margin: 0 10% 0 10%;'>"
                                       + "<div style='margin: 10% 0 10% 0;'>"
                                           + "<h2 style='text-align:center;color:#232323;margin:0.1em 0 !important;'>요청하신 인증번호를 발송해드립니다.</h2>"
                                           + "<h2 style='text-align:center;color:#232323;margin:0.1em 0 !important;'>아래의 인증번호를 인증번호 입력창에 입력해주세요.</h2>"
                                           + "<h3 style='text-align:center;color: #aaa;margin-top:5%;'>인증번호</h3>"
                                           + "<h1 style='text-align:center;color:#E99439;'> " + mailDto.getMessage() + "</h1>"
                                       + "</div>"
                                       + "<hr style='border: 0; height: 1px; background: #ccc; >"
                                       + "<h2 style='text-align:center;'></h2>"
                                       + "<h4 style='text-align:center;color: #787878;'>본 이메일은 발신전용 이메일입니다. 궁금하신 사항은 홈커밍데이로 문의하시기 바랍니다.</h4>"
                                   + "</div>"
                                   +
                                 "</div>";
            mailHandler.setText(htmlContent, true);
            // 이미지 삽입
           // mailHandler.setInline("sample-img", new ClassPathResourceReader("logo.png").getContent());

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