package kr.co.mgv.user.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    // 메일 내용 작성
    MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;
    MimeMessage qnaMessage(String to, String question, String answer) throws MessagingException, UnsupportedEncodingException;

    // 임시비밀번호 메일 내용 작성
    MimeMessage tempPwdMessage(String to) throws MessagingException, UnsupportedEncodingException;

    // 인증번호 전송
    String createKey();
    
    // 임시비밀번호 전송
    String createPwdKey();

    // 메일 발송
    String sendSimpleMessage(String to) throws Exception;
    String sendTempPwdMessage(String to) throws Exception;
    String sendTempqnaMessage(String to, String question, String answer) throws Exception;

}
