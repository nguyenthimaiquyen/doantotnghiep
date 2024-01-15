package com.quyen.hust.service.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public void sendAttachedMail(String receiver) throws MessagingException {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        // Setting multipart as true for attachments to
        // be send
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setText("Email có đính kèm file");
        mimeMessageHelper.setSubject("[DEMO MAIL] Gửi mail kèm file");

        // Adding the attachment
        FileSystemResource file = new FileSystemResource(new File("data/1.jpg"));
        mimeMessageHelper.addAttachment(file.getFilename(), file);

        // Sending the mail
        javaMailSender.send(mimeMessage);

    }

    public void otpSendingMail(String name, String receiver, String otp) throws MessagingException {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        // Setting multipart as true for attachments to be send
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setText("Bạn <b>" + name +
                "</b> thân mến,<br>Đây là OTP của bạn, OTP có giá trị sử dụng một lần duy nhất. Vui lòng truy cập website để lấy lại mật khẩu.<br>" +
                otp, true);
        mimeMessageHelper.setSubject("[Larna - Học tập] Lấy lại mật khẩu");

        // Adding the attachment
//        FileSystemResource file = new FileSystemResource(new File("data/1.jpg"));
//        mimeMessageHelper.addAttachment(file.getFilename(), file);

        // Sending the mail
        javaMailSender.send(mimeMessage);
    }


    public void verifyAccount(Long id, String name, String receiver, String role) throws MessagingException {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        // Setting multipart as true for attachments to be send
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("[Larna - Học tập] Kích hoạt tài khoản");

        String emailContent = "Xin chào <b>" + name + "</b>,<br>" +
                "Chúc mừng bạn đã đăng ký thành công tài khoản với username " + receiver +
                ".<br> Vui lòng click vào liên kết sau để kích hoạt tài khoản: <br><br>" +
                "<a class=\"btn btn-primary f-500\" target=\"_blank\" " +
                "href=\"http://localhost:8080/accounts/"+ id +"/activation\">Kích hoạt tài khoản</a>";
        mimeMessageHelper.setText(emailContent, true);

        // Sending the mail
        javaMailSender.send(mimeMessage);
    }


}
