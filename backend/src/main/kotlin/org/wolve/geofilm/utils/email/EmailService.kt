package org.wolve.geofilm.utils.email

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class EmailService(
    private val emailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) {

    // Enviar correo de bienvenida al registrarse
    fun sendWelcomeEmail(to: String, name: String, email: String) {
        val context = Context().apply {
            setVariable("name", name)
            setVariable("email", email)
        }
        val htmlContent = templateEngine.process("welcome-email", context)

        sendEmail(to, "¡Bienvenido a nuestra aplicación!", htmlContent)
    }

    fun sendLoginAlertEmail(
        to: String,
        name: String,
        ip: String,
        deviceType: String,
        os: String,
        browser: String,
        loginTime: LocalDateTime = LocalDateTime.now()
    ) {
        val context = Context().apply {
            setVariable("name", name)
            setVariable("ip", formatIp(ip))
            setVariable("deviceType", deviceType)
            setVariable("os", os)
            setVariable("browser", browser)
            setVariable("loginTime", formatDateTime(loginTime))
            setVariable("currentYear", LocalDate.now().year)
        }

        val htmlContent = templateEngine.process("login-alert", context)
        sendEmail(to, "🔒 Nuevo inicio de sesión en tu cuenta MovieTrip", htmlContent)
    }

    private fun formatDateTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm")
        return dateTime.format(formatter)
    }

    private fun formatIp(ip: String): String {
        return if (ip == "0:0:0:0:0:0:0:1") "Localhost" else ip
    }

    // Método común para enviar correos electrónicos
    private fun sendEmail(to: String, subject: String, htmlContent: String) {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)  // 'true' para que sea HTML
        emailSender.send(message)
    }

    fun sendPasswordResetPinEmail(to: String, pin: String) {
        val context = Context().apply {
            setVariable("pin", pin)
            setVariable("currentYear", LocalDate.now().year)
        }

        val htmlContent = templateEngine.process("password-reset-pin", context)
        sendEmail(to, "🔐 Tu PIN para restablecer la contraseña", htmlContent)
    }
}
