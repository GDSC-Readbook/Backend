package com.example.demo.service;

import com.example.demo.unit.RedisUtil;
import jakarta.mail.Message;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil; //redis 관련

    @Autowired
    public EmailService(JavaMailSender javaMailSender,RedisUtil redisUtil )
    {
        this.javaMailSender = javaMailSender;
        this.redisUtil = redisUtil;
    }

    public String createMessage(String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        String code = sendCertificationMail(email); // 인증번호 생성 및 redis에 저장.

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("인증 링크입니다.");

        String url = "http://152.69.225.60/v1/email/certification/check?email="+email+"&code="+code;
        message.setText(url);

        message.setFrom("daehyuh@gmail.com");

        sendMail(message);
        return code;
    }

    public void sendNewPassword(String email, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();


        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("임시 비밀번호 입니다, 로그인 후 비밀번호를 변경해주세요.");
        message.setText("임시 비밀번호 : "+code);

        message.setFrom("daehyuh@gmail.com"); //보내는사람.

        sendMail(message);
    }

    private void sendMail(MimeMessage message) throws Exception{
        try{
            javaMailSender.send(message);
        }catch (MailException mailException){
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    private String sendCertificationMail(String email)  throws Exception {
        try{
            String code = UUID.randomUUID().toString().substring(0, 6); //랜덤 인증번호 uuid를 이용!
            redisUtil.setDataExpire(code, email,60*5L); // {key,value} 5분동안 저장.
            return code;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    public String getData(String code){
        return redisUtil.getData(code);
    }

    public void deleteData(String code){
        redisUtil.deleteData(code);
    }

}