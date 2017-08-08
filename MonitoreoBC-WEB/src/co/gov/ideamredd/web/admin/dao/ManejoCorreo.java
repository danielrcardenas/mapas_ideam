// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.dao;

import java.util.ArrayList;
import java.util.Properties;
// import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import co.gov.ideamredd.util.entities.Usuario;
import co.gov.ideamredd.mbc.conexion.ParametroNoBean;
import co.gov.ideamredd.util.Util;

/**
 * Clase para el maneo de correos, como el envio de estos.
 */
public class ManejoCorreo {

	private static ArrayList<String>	nombresLicencias;
	private static ArrayList<String>	idsLicencias;
	private static ArrayList<String>	licencias;

	public static void enviarCorreo(HttpServletRequest req, String[] lic) {

		ParametroNoBean parametro = new ParametroNoBean();

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
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

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta creada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + req.getParameter("nombre").toString() + " Su cuenta a sido creada</h3>" + "<h4>Se encuentran adjuntos los acuerdos<br>" + "de licencia que acepto al momento de registrarse(si es el caso).<br>" + "Puede modificar sus datos ingresando A la pagina " + parametro.getParametro("paginaIdeam") + " con los siguientes datos: <br>" + "Usuario: " + req.getParameter(
					"documento").toString() + "<br>" + "Contrase&ntilde;a: " + req.getParameter("password").toString() + "</h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(parametro.getParametro("correoIdeam"), parametro.getParametro("paginaIdeam")));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getParameter("email").toString(), "Señor(a) " + req.getParameter("nombre").toString()));
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

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void enviarCorreoActualizacion(HttpServletRequest req, String[] lic) {

		ParametroNoBean parametro = new ParametroNoBean();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
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

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta Actualizada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + req.getParameter("nombre").toString() + " Su cuenta a sido actualizada</h3>" + "<h4>Se encuentran adjuntos los acuerdos<br>" + "de licencia que acepto(si es el caso).<br>";
			if (!req.getParameter("password").toString().equals("")) {
				htmlBody = htmlBody + "Puede modificar sus datos ingresando A la pagina " + parametro.getParametro("paginaIdeam") + " con los siguientes datos: <br>" + "Usuario: " + req.getSession().getAttribute("documento").toString() + "<br>" + "Contrase&ntilde;a: " + req.getParameter("password").toString() + "</h4>" + "</body>" + "</html>";
			}

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(parametro.getParametro("correoIdeam"), parametro.getParametro("paginaIdeam")));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getParameter("email").toString(), "Señor(a) " + req.getParameter("nombre").toString()));
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
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void enviarCorreoRecordatorio(String clave, String correo, Usuario usuario) {

		ParametroNoBean parametro = new ParametroNoBean();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		nombresLicencias = null;
		idsLicencias = null;

		try {
			String htmlBody;

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta creada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + usuario.getNombre() + "<br>" + " Su clave a sido reestablecida:</h3>" + "<h4>Su nueva clave es: " + clave + "<br>" + "Puede modificarla ingresando A la pagina " + parametro.getParametro("paginaIdeam") + "<br>" + "Con los siguientes datos:<br>" + "Usuario: " + usuario.getIdentificacion() + "<br>Contrase&ntilde;a: " + clave + "<br></h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(parametro.getParametro("correoIdeam"), parametro.getParametro("paginaIdeam")));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(correo, "Señor(a)"));
			msg.setSubject(parametro.getParametro("asunto.recordatorio"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);
			Transport.send(msg);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void enviarCorreoActivacion(String correo, Usuario usuario) {

		ParametroNoBean parametro = new ParametroNoBean();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			String htmlBody;

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta Activada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + usuario.getNombre() + "<br>" + " Su cuenta a sido Activada</h3>" + "<h4>Por favor no conteste este correo informativo." + "</h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(parametro.getParametro("correoIdeam"), parametro.getParametro("paginaIdeam")));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(correo, "Señor(a)"));
			msg.setSubject(parametro.getParametro("asunto.cambioEstado"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);
			Transport.send(msg);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void enviarCorreoDesactivacion(String correo, Usuario usuario) {

		ParametroNoBean parametro = new ParametroNoBean();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			String htmlBody;

			htmlBody = "<html>" + "<head><meta charset=\"UTF-8\">" + "<title>Cuenta Desactivada</title>" + "</head>" + "<body>" + "<h3>Se&ntilde;or(a) " + usuario.getNombre() + "<br>" + " Su cuenta a sido Desactivada</h3>" + "<h4>Para Mayor informacion comuniquese a las lineas de atencion.<br>" + "Por favor no conteste este correo informativo." + "</h4>" + "</body>" + "</html>";

			Message msg = new MimeMessage(session);
			Multipart mp = new MimeMultipart();

			msg.setFrom(new InternetAddress(parametro.getParametro("correoIdeam"), parametro.getParametro("paginaIdeam")));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(correo, "Señor(a)"));
			msg.setSubject(parametro.getParametro("asunto.cambioEstado"));

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);
			Transport.send(msg);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
