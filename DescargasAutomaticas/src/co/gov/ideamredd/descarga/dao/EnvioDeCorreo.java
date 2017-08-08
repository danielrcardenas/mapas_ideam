// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Métodos para enviar mail con reporte de descargas
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class EnvioDeCorreo {

	Parametro	parametro;

	/**
	 * Envía un mail con adjunto
	 * 
	 * @param tipodato
	 * @param autor
	 */
	public void enviarCorreoConAdjuntos(String tipodato, String autor) {

		parametro = new Parametro();

		// final String username = "ideam.redd.emails.sending@gmail.com";
		// final String password = "ideam.redd2015";

		final String username = parametro.getParametro("correo.envio.mails");
		final String password = parametro.getParametro("correo.mails.pass");
		final String mailTo = parametro.getParametro("correo.reportes.auto");
		final String smtpHost = parametro.getParametro("correo.host.smtp");

		Properties props = new Properties();
		// props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.starttls.enable", "true");
		// props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		// props.put("mail.smtp.host", "smtp.gmail.com");
		// props.put("mail.smtp.port", "587");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHost);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));

			String str_tipodato = "";
			if (tipodato.length() > 0) {
				str_tipodato = "(Tipo:" + tipodato + ")";
			}
			String str_autor = "";
			if (autor.length() > 0) {
				str_autor = "(Autor:" + autor + ")";
			}

			message.setSubject("Reporte: Descargas de usuarios " + str_tipodato + " " + str_autor + " " + (new Date()).toString());

			BodyPart part1 = new MimeBodyPart();
			part1.setText("Reporte de Descargas de usuarios, generado automaticamente el " + (new Date()).toString());

			File file = new File(parametro.getParametro("REPORTES_TEMP") + "/TmpConsultaDetalleDesc.xlsx");
			BodyPart part2 = new MimeBodyPart();
			DataSource attachment = new FileDataSource(file);
			part2.setDataHandler(new DataHandler(attachment));
			part2.setFileName(file.getName());

			file = new File(parametro.getParametro("REPORTES_TEMP") + "/TmpConsultaImg.pdf");
			BodyPart part3 = new MimeBodyPart();
			attachment = new FileDataSource(file);
			part3.setDataHandler(new DataHandler(attachment));
			part3.setFileName(file.getName());

			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(part1);
			multiPart.addBodyPart(part2);
			multiPart.addBodyPart(part3);

			message.setContent(multiPart);

			Transport.send(message);

		}
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
