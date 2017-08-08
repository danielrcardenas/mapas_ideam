// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.dao;

import java.util.ArrayList;
import java.util.Properties;
//import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.ParametroNoBean;
//import co.gov.ideamredd.usuario.entities.Usuario;
import co.gov.ideamredd.util.entities.Usuario;
import co.gov.ideamredd.util.Util;

/**
 * clase usada para manejo de correos.
 */
public class ManejoCorreo {

	private static ArrayList<String> nombresLicencias;
	private static ArrayList<String> idsLicencias;
	private static ArrayList<String> licencias;

	public static void enviarCorreo(HttpServletRequest req, String[] lic) {

		Properties props = new Properties();
		ParametroNoBean parametro = new ParametroNoBean();

		final String username = parametro.getParametro("correo.envio.mails");
		final String password = parametro.getParametro("correo.mails.pass");
		final String smtpHost = parametro.getParametro("correo.host.smtp");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		nombresLicencias = null;
		idsLicencias = null;

		try {
			String htmlBody;

			nombresLicencias = Util.obtenerArregloString(req.getParameter("nombresLicencias").split(","));
			idsLicencias = Util.obtenerArregloString(req.getParameter("idsLicencias").split(","));
			if (lic != null) {
				if (!lic.equals("")) {
					licencias = Util.obtenerArregloString(lic);
				}
			}

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta creada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + req.getParameter("nombre").toString() + " Su cuenta a sido creada</h3>" + "<h4>Se encuentran adjuntos los acuerdos<br>" + "de licencia que acepto al momento de registrarse(si es el caso).<br>" + "Puede modificar sus datos ingresando A la pagina " + parametro.getParametro("paginaIdeam") + " con los siguientes datos: <br>" + "Usuario: " + req.getParameter("documento").toString() + "<br>" + "Contrase&ntilde;a: " + req.getParameter("password").toString() + "</h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);

			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(req.getParameter("email").toString()));

			// msg.setFrom(new InternetAddress(
			// parametro.getParametro("correoIdeam"),
			// parametro.getParametro("paginaIdeam")));
			// msg.addRecipient(Message.RecipientType.TO, new
			// InternetAddress(req
			// .getParameter("email").toString(), "Se&ntilde;or(a) "
			// + req.getParameter("nombre").toString()));
			msg.setSubject(parametro.getParametro("asunto.registro"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			if (lic != null) {
				if (!lic.equals("")) {
					int indice = 0;
					for (int i = 0; i < licencias.size(); i++) {
						indice = idsLicencias.indexOf(licencias.get(i));
						MimeBodyPart attachment = new MimeBodyPart();
						attachment.setFileName(nombresLicencias.get(indice) + ".pdf");
						attachment.setDataHandler(new DataHandler(new FileDataSource(parametro.getParametro("dir.licencias") + nombresLicencias.get(indice) + ".pdf")));
						mp.addBodyPart(attachment);
					}
				}
			}

			msg.setContent(mp);
			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * Envía correo de actualización de usuario, incluyendo adjuntos sobre licencias aceptadas.
	 * 
	 * @param req
	 * @param lic
	 * @return boolean true si pudo enviar el correo, false de lo contrario
	 */
	public static boolean enviarCorreoActualizacion(HttpServletRequest req, String[] lic) {

		boolean resultado = false;
		
		Properties props = new Properties();
		ParametroNoBean parametro = new ParametroNoBean();

		final String username = parametro.getParametro("correo.envio.mails");
		final String password = parametro.getParametro("correo.mails.pass");
		final String smtpHost = parametro.getParametro("correo.host.smtp");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		nombresLicencias = null;
		idsLicencias = null;

		try {
			String htmlBody;

			nombresLicencias = Util.obtenerArregloString(req.getParameter("nombresLicencias").split(","));
			idsLicencias = Util.obtenerArregloString(req.getParameter("idsLicencias").split(","));
			if (lic != null) {
				if (!lic.equals("")) {
					licencias = Util.obtenerArregloString(lic);
					ArrayList<String> datos = new ArrayList<String>();
					for (int i = 0; i < lic.length; i++) {
						datos.add(lic[i]);
					}
					licencias = datos;
				}
			}

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta Actualizada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + req.getParameter("nombre").toString() + " Su cuenta ha sido Actualizada</h3>" + "<h4>Se adjuntan los acuerdos de licencia<br>" + "que ha aceptado.<br>" + "Por favor, no responda este mensaje Informativo.";
			if (!req.getParameter("password").toString().equals("")) {
				htmlBody = htmlBody + "Puede modificar sus datos ingresando a la página " + parametro.getParametro("paginaIdeam") + " con los siguientes datos: <br>" + "Usuario: " + req.getSession().getAttribute("documento").toString() + "<br>" + "Contrase&ntilde;a: " + req.getParameter("password").toString() + "</h4>" + "</body>" + "</html>";
			}

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(username));

			String emailUsuario = Auxiliar.nzObjStr(req.getParameter("emailUsuario"), "").toString();

			if (Auxiliar.tieneAlgo(emailUsuario)) {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailUsuario));
	
				// msg.setFrom(new InternetAddress(
				// parametro.getParametro("correoIdeam"),
				// parametro.getParametro("paginaIdeam")));
				// msg.addRecipient(Message.RecipientType.TO, new
				// InternetAddress(req
				// .getParameter("email").toString(), "Se&ntilde;or(a) "
				// + req.getParameter("nombre").toString()));
				msg.setSubject(parametro.getParametro("asunto.actualiza"));
	
				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(htmlBody, "text/html");
				mp.addBodyPart(htmlPart);
	
				int indice = 0;
				if (lic != null) {
					if (!lic.equals("")) {
						for (int i = 0; i < licencias.size(); i++) {
							indice = idsLicencias.indexOf(licencias.get(i));
							MimeBodyPart attachment = new MimeBodyPart();
							attachment.setFileName(nombresLicencias.get(indice) + ".pdf");
							attachment.setDataHandler(new DataHandler(new FileDataSource(parametro.getParametro("dir.licencias") + nombresLicencias.get(indice) + ".pdf")));
							mp.addBodyPart(attachment);
						}
					}
				}
	
				msg.setContent(mp);
				Transport.send(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultado = false;
		}
		
		return resultado;

	}

	public static void enviarCorreoRecordatorio(String clave, String correo, Usuario usuario) {

		Properties props = new Properties();
		ParametroNoBean parametro = new ParametroNoBean();

		final String username = parametro.getParametro("correo.envio.mails");
		final String password = parametro.getParametro("correo.mails.pass");
		final String smtpHost = parametro.getParametro("correo.host.smtp");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		nombresLicencias = null;
		idsLicencias = null;

		try {
			String htmlBody;

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta creada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + usuario.getNombre() + "<br>" + " Su clave a sido reestablecida:</h3>" + "<h4>Su nueva clave es: " + clave + "<br>" + "Puede modificarla ingresando a la pagina " + parametro.getParametro("paginaIdeam") + "<br>" + "Con los siguientes datos:" + "<br>" + "Logín de usuario: " + usuario.getLogin() + "<br>Contrase&ntilde;a: " + clave + "<br></h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));

			// msg.setFrom(new InternetAddress(
			// parametro.getParametro("correoIdeam"),
			// parametro.getParametro("paginaIdeam")));
			// msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
			// correo, "Se&ntilde;or(a)"));
			msg.setSubject(parametro.getParametro("asunto.recordatorio"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);
			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void enviarCorreoAceptaImagen(String nombreImagen, co.gov.ideamredd.util.entities.Usuario usuario) {

		Properties props = new Properties();
		ParametroNoBean parametro = new ParametroNoBean();

		final String username = parametro.getParametro("correo.envio.mails");
		final String password = parametro.getParametro("correo.mails.pass");
		final String smtpHost = parametro.getParametro("correo.host.smtp");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			String htmlBody;

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Imagen Aceptada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + usuario.getNombre() + "<br>" + " Su imagen" + nombreImagen + "a sido Aceptada y Publicada.</h3>" + "<h4>Gracias.<br></h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usuario.getCorreoElectronico()));

			// msg.setFrom(new InternetAddress(
			// parametro.getParametro("correoIdeam"),
			// parametro.getParametro("paginaIdeam")));
			// msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
			// usuario.getCorreoElectronico(), "Se&ntilde;or(a)"));
			msg.setSubject(parametro.getParametro("asunto.imagen.aceptada"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);
			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void enviarCorreoRechazoImagen(String nombreImagen, co.gov.ideamredd.util.entities.Usuario usuario) {

		Properties props = new Properties();
		ParametroNoBean parametro = new ParametroNoBean();

		final String username = parametro.getParametro("correo.envio.mails");
		final String password = parametro.getParametro("correo.mails.pass");
		final String smtpHost = parametro.getParametro("correo.host.smtp");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			String htmlBody;

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Imagen Rechazada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + usuario.getNombre() + "<br>" + " Su imagen" + nombreImagen + "a sido Rechazada.</h3>" + "<h4>Verifique que la imagen es correcta e intentelo nuevamente.<br>" + "Gracias.</h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usuario.getCorreoElectronico()));

			// msg.setFrom(new InternetAddress(
			// parametro.getParametro("correoIdeam"),
			// parametro.getParametro("paginaIdeam")));
			// msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
			// usuario.getCorreoElectronico(), "Se&ntilde;or(a)"));
			msg.setSubject(parametro.getParametro("asunto.imagen.rechazada"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);
			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
