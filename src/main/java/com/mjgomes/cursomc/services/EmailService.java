package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Pedido;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;

// Abstração de envio de e-mail (texto simples e HTML); implementada por AbstractEmailService.
public interface EmailService {

    void sendOrderConfirmationEmail(Pedido obj);

    void sendEmail(SimpleMailMessage msg);

    void sendOrderConfirmationHtmlEmail(Pedido obj);

    void sendHtmlEmail(MimeMessage msg);
}
