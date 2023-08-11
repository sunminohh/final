package kr.co.mgv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yml")
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.google.com"); // 메인 도메인 서버 주소 -> 정확히는 smtp 서버 주소
        javaMailSender.setPort(587); // 메일 인증 서버 포트
        javaMailSender.setUsername("mgv0731@gmail.com"); // 구글 계정 아이디
        javaMailSender.setPassword("okiydcgatofxrizx"); // 구글 계정 비밀번호
        javaMailSender.setProtocol("smtp");
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true"); // smtp strattles 사용
        properties.setProperty("mail.debug", "true"); // 디버그 사용
        properties.setProperty("mail.smtp.ssl.trust","smtp.google.com"); // ssl 인증 서버는 smtp.naver.com
        properties.setProperty("mail.smtp.ssl.enable","true"); // ssl 사용

        return javaMailSender;
    }

}
