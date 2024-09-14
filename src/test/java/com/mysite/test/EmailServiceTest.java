//package com.mysite.test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import com.mysite.login.service.EmailService;
//
//@ExtendWith(MockitoExtension.class)
//public class EmailServiceTest {
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @InjectMocks
//    private EmailService emailService;
//
//    @Test
//    public void testSendPasswordResetEmail() {
//        // Arrange
//        String to = "test@example.com";
//        String resetToken = "dummyToken";
//
//        // Act
//        emailService.sendPasswordResetEmail(to, resetToken);
//
//        // Assert
//        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//        verify(mailSender).send(messageCaptor.capture());
//
//        SimpleMailMessage sentMessage = messageCaptor.getValue();
//        assertEquals(to, sentMessage.getTo()[0]);
//        assertEquals("Password Reset Request", sentMessage.getSubject());
//        assertEquals("To reset your password, click the link below:\n" +
//                     "http://example.com/reset?token=" + resetToken, sentMessage.getText());
//    }
//}
