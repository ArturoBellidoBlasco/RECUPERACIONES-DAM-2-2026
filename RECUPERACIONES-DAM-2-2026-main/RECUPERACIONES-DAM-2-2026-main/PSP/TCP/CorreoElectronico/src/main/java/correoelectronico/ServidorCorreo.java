package correoelectronico;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class ServidorCorreo {
    public static void main(String[] args) {

        //REGISTRAMOS LA ENTRADA DE DATOS POR PARTE DEL USUARIO
        //ESTOS DATOS TAMBIÉN PUEDEN SER FORZADOS SIN NECESIDAD DE TENER UN USUARIO

        Scanner sc = new Scanner(System.in);

        System.out.println("Correo del emisor: ");
        String emisor = sc.nextLine();

        System.out.println("Contraseña (o token de aplicación): ");
        char[] password = System.console().readPassword();

        System.out.println("Correo del destinatario: ");
        String destinatario = sc.nextLine();

        System.out.println("Asunto: ");
        String asunto = sc.nextLine();

        System.out.print("Cuerpo del mensaje: ");
        String cuerpo = sc.nextLine();

        System.out.print("Ruta del archivo adjunto: ");
        String rutaAdjunto = sc.nextLine();


        //CONFIGURACIÓN DE PROPIEDADES SMTP

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //CAMBIAR SEGÚN EL TIPO DE SERVIDOR DE CORREO, EN ESTE CASO GMAIL
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");

        //CREAR LA SESIÓN CON AUTENTICACIÓN

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emisor, new String(password));
            }
        });

        try {
            //CREAR EL MENSAJE

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emisor));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            //MANEJAR EL CUERPO Y EL ADJUNTO (MimeMultipart)
            Multipart multipart = new MimeMultipart();

            // PARTE DE TEXTO
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(cuerpo);
            multipart.addBodyPart(textPart);

            // PARTE DEL ADJUNTO (Opcional)
            if (!rutaAdjunto.isEmpty()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(new File(rutaAdjunto));
                    multipart.addBodyPart(attachPart);
                } catch (Exception ex) {
                    System.out.println("No se pudo adjuntar el archivo: " + ex.getMessage());
                }
            }

            message.setContent(multipart);

            //ENVIAR
            System.out.println("Enviando correo...");
            Transport.send(message);
            Arrays.fill(password, ' ');
            System.out.println("¡Correo enviado con éxito!");


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
