package kr.co.mgv.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    private String authNumber; // 인증번호
    private String tempPwd;

    // 메일 내용 작성
    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("MGV 회원가입 이메일 인증");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요</h1>";
        msgg += "<h1> 즐거움을 드리는 MGV 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>즐거운 하루되세요. 감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "인증번호 : <strong>";
        msgg += authNumber + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("mgv0731@gmail.com", "MGV_Admin"));// 보내는 사람

        return message;
    }
    
	// 인증 코드
    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        //숫자 6자리 인증번호
        for (int i = 0; i < 6; i++) {
            int num = rnd.nextInt(10); // 0~9 중 랜덤한 숫자
            key.append(num);
        }

        return key.toString();
    }
    
    @Override
    public String sendSimpleMessage(String to) throws Exception {

        authNumber = createKey(); // 랜덤 인증번호 생성

        MimeMessage message = createMessage(to); // 메일 발송
        try {// 예외처리
            javaMailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }
        return authNumber; // 메일로 보냈던 인증 코드를 서버로 반환
    }

    // 임시비밀번호
    @Override
    public MimeMessage tempPwdMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("MGV 임시 비밀번호 발급");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요</h1>";
        msgg += "<h1> 즐거움을 드리는 MGV 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>아래 임시 비밀번호로 로그인하세요.<p>";
        msgg += "<br>";
        msgg += "<p>즐거운 하루되세요. 감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>임시 비밀번호입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "인증번호 : <strong>";
        msgg += tempPwd + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("mgv0731@gmail.com", "MGV_Admin"));// 보내는 사람

        return message;
    }

    @Override
    public String createPwdKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 임시비밀번호 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }

    @Override
    public String sendTempPwdMessage(String to) throws Exception {

        tempPwd = createPwdKey(); // 랜덤 인증번호 생성

        MimeMessage message = createMessage(to); // 메일 발송
        try {
            javaMailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
        }
        return tempPwd; // 메일로 보냈던 인증 코드를 서버로 반환
    }
    
    // 메일 발송
    // sendSimpleMessage 의 매개변수로 들어온 to 는 곧 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
    // 그리고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send!!
    
    // 고객센터 내용 작성
    @Override
    public MimeMessage qnaMessage(String to, String question, String answer) throws MessagingException, UnsupportedEncodingException {
    	
    	MimeMessage message = javaMailSender.createMimeMessage();
    	
    	message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
    	message.setSubject("MGV \"고객의 문의\" 답변 메일입니다. ");// 제목
    	
    	String msgg = ""; 
    	msgg += "<div style='margin:100px;'>";
    	msgg += "<h1> 안녕하세요</h1>";
    	msgg += "<h1> 즐거움을 드리는 MGV 입니다</h1>";
    	msgg += "<br>";
    	msgg += "<h2>Q. 고객님께서 문의하신 내용입니다.</h2>";
    	msgg += "<div style='background-color:#eee; padding:10px;'>"
    			+ question
    			+ "</div>"
    			+ "<br>";
    	
    	msgg += "<br>";
    	msgg += "<h2>A. MGV의 답변 내용입니다.</h2>";
    	msgg += "<div style='background-color:#eee; padding:10px;'>"
    			+ "<p>안녕하세요.<p>"
    			+ "<p>MGV 드림센터입니다.<p>"
    			+ "<br>"
    			+ "<p>고객님께서 보내주신 글 잘 받아보았습니다.<p>"
    			+ "<p>먼저 저희 MGV를 이용해주셔서 감사드립니다.<p>"
    			+ "<br>"
    			+ answer
    			+ "<br><br>"
    			+ "<p>문의주신 내용에 적절한 답변이 되었길 바라며,<p>"
    			+ "<p>추후 문의사항은 홈페이지 & 어플 내 1:1문의 또는 상담톡(운영시간 10:00~19:00)을<p>"
    			+ "<p>통해 문의주시면 안내 도와드리겠습니다.<p>"
    			+ "<br/>"
    			+ "<p>감사합니다.<p>"
    			+ "<p>MGV 고객센터 드림.<p>"
    			+ "</div>";
    	
    	
    	


    	message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
    	// 보내는 사람의 이메일 주소, 보내는 사람 이름
    	message.setFrom(new InternetAddress("mgv0731@gmail.com", "MGV_qna"));// 보내는 사람
    	
    	return message;
    }
    
    // 고객센터 답변완료 메일 발송
    @Override
    public String sendTempqnaMessage(String to, String question, String answer) throws Exception {
    	
    	MimeMessage message = qnaMessage(to, question, answer); // 메일 발송
    	try {
    		javaMailSender.send(message);
    	} catch (MailException es) {
    		es.printStackTrace();
    		throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
    	}
    	return tempPwd; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}
