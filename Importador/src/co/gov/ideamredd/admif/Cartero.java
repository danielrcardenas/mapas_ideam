// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.MailException;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

import com.sun.mail.smtp.SMTPTransport;

import java.util.*; 


 
/**
 * Se encarga de enviar mails
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 */
public class Cartero {
	
	//private Auxiliar aux = new Auxiliar();
	
	private String fromname = "";
	private String fromaddress = "";
	private String servidor = "";
	private Integer puerto = 25;
	private String usuario = "";
	private String clave = "";
	private String protocolo = "";
	private String autenticar = "";
	
	
	/**
	 * Constructor de la instancia de clase
	 * 
	 */
    Cartero() {
        fromname = "Sistema de Inventarios Forestales del IDEAM";
        fromaddress = "avisosREDD@localhost.localdomain";
        servidor = "localhost";
        protocolo = "PLAIN";
        autenticar = "false";
        usuario = "";
        clave = "";
        puerto = 25;
    }

    /**
     * Metodo para establecerFromname
     * 
     * @param valor
     */
    public void establecerFromname (String valor)
    {
    	this.fromname = valor;
    }
    
    /**
     * Metodo para establecerAutenticar
     * 
     * @param valor
     */
    public void establecerAutenticar (String valor)
    {
    	this.autenticar = valor;
    }
    
    /**
     * Metodo para establecerFromaddress
     * 
     * @param valor
     */
    public void establecerFromaddress (String valor)
    {
    	this.fromaddress = valor;
    }
    
    /**
     * Metodo para establecerServidor
     * 
     * @param valor
     */
    public void establecerServidor (String valor)
    {
    	this.servidor = valor;
    }
    
    /**
     * Metodo para establecerPuerto
     * 
     * @param valor
     */
    public void establecerPuerto (int valor)
    {
    	this.puerto = valor;
    }
    
    /**
     * Metodo para establecerUsuario
     * 
     * @param valor
     */
    public void establecerUsuario (String valor)
    {
    	this.usuario = valor;
    }
    
    /**
     * Metodo para establecerClave
     * 
     * @param valor
     */
    public void establecerClave (String valor)
    {
    	this.clave = valor;
    }
    
    /**
     * Metodo para establecerProtocolo
     * 
     * @param valor
     */
    public void establecerProtocolo (String valor)
    {
    	this.protocolo = valor;
    }
    
    /**
     * Metodo para enviar mail
     * 
     * @param fromname
     * @param fromaddress
     * @param asunto
     * @param a_destinatarios_to
     * @param a_destinatarios_cc
     * @param a_destinatarios_bcc
     * @param mensaje
     * @param html
     * @param servidor
     * @param puerto
     * @param usuario
     * @param clave
     * @return String con mensaje de resultado
     */
    public String enviarMail_CodeMonkey(String asunto, ArrayList<String> a_destinatarios_to, String[] a_destinatarios_cc, String[] a_destinatarios_bcc, String mensaje, boolean html)
    {
    	String resultado = "";
    	String[] tupla_nombre_email = null;
    	String nombre = "";
    	String mail = "";
    	String debugstr = "";
    	
    	try
    	{
    		debugstr += "Instanciando Email...";
	    	final Email email = new Email();
			
    		debugstr += "<br>Estableciendo Fromaddress...";
			email.setFromAddress(this.fromname, this.fromaddress);

    		debugstr += "<br>Estableciendo asunto...";
			email.setSubject(asunto);
	
			// AÑADIR DESTINATARIOS TO
			
    		debugstr += "<br>Añadiendo destinatarios to...";
			if (a_destinatarios_to != null)
			{
				for (int i=0; i<a_destinatarios_to.size(); i++)
				{
					if (Auxiliar.tieneAlgo(a_destinatarios_to.get(i))) 
					{
						tupla_nombre_email = a_destinatarios_to.get(i).split("·");
						if (tupla_nombre_email.length == 2)
						{
							nombre = tupla_nombre_email[0];
							mail = tupla_nombre_email[1];
							email.addRecipient(nombre, mail, RecipientType.TO);
						}
					}
				}
			}
			else
			{
				return "Debe especificar los destinatarios del mail";
			}
			
			
			// AÑADIR DESTINATARIOS CC
			
    		debugstr += "<br>Añadiendo destinatarios cc...";
			if (a_destinatarios_cc != null)
			{
				for (int i=0; i<a_destinatarios_cc.length; i++)
				{
					if (Auxiliar.tieneAlgo(a_destinatarios_cc[i])) 
					{
						tupla_nombre_email = a_destinatarios_cc[i].split("·");
						if (tupla_nombre_email.length == 2)
						{
							nombre = tupla_nombre_email[0];
							mail = tupla_nombre_email[1];
							email.addRecipient(nombre, mail, RecipientType.CC);
						}
					}
				}
			}		
			
			// AÑADIR DESTINATARIOS BCC
			
    		debugstr += "<br>Añadiendo destinatarios bcc...";
			if (a_destinatarios_bcc != null)
			{
				for (int i=0; i<a_destinatarios_bcc.length; i++)
				{
					if (Auxiliar.tieneAlgo(a_destinatarios_bcc[i])) 
					{
						tupla_nombre_email = a_destinatarios_bcc[i].split("·");
						if (tupla_nombre_email.length == 2)
						{
							nombre = tupla_nombre_email[0];
							mail = tupla_nombre_email[1];
							email.addRecipient(nombre, mail, RecipientType.BCC);
						}
					}
				}
			}
			
    		debugstr += "<br>Validando html...";
			if (html)
			{
	    		debugstr += "Estableciendo mensaje html...";
				email.setTextHTML(mensaje);
			}
			
    		debugstr += "<br>Estableciendo mensaje plano...";
			email.setText(mensaje);
			
			// embed images and include downloadable attachments
			/*
			email.addEmbeddedImage("wink1", imageByteArray, "image/png");
			email.addEmbeddedImage("wink2", imageDatesource);
			email.addAttachment("invitation", pdfByteArray, "application/pdf");
			email.addAttachment("dresscode", odfDatasource);
			*/
			
    		debugstr += "<br>Enviando mail...";
			if (this.protocolo.equals("TLS"))
			{
				new Mailer(this.servidor, this.puerto, this.usuario, this.clave, TransportStrategy.SMTP_TLS).sendMail(email);
			}
			else if (protocolo.equals("SSL"))
			{
				new Mailer(this.servidor, this.puerto, this.usuario, this.clave, TransportStrategy.SMTP_SSL).sendMail(email);				
			}
			else
			{
				new Mailer(this.servidor, this.puerto, this.usuario, this.clave, TransportStrategy.SMTP_PLAIN).sendMail(email);				
			}
			
			resultado = debugstr + "<p class='confirmacion'>E-Mail enviado exitosamente.</p>";
    	}
    	catch (MailException e)
    	{
    		resultado = "<p class='error'>Error del agente de mensajería: " + e.toString() + "</p>";
    	}
    	catch (Exception e)
    	{
    		resultado = "<p class='error'>Error del cartero: " + e.toString() + "</p>";
    	}
    	
		return resultado;
    }
    
	/**
	 * Otro método diferente para enviar mail
	 * no se usa
	 * 
	 * @param recipientes
	 * @param asunto
	 * @param mensaje
	 * @throws Exception
	 */
    public void enviarMail_sendMessage(String recipientes[], String asunto, String mensaje) 
    	throws Exception {
        Properties props = System.getProperties();
        
        props.put("mail.smtps.host", this.servidor);
        props.put("mail.smtps.auth", this.autenticar);
        
        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(this.fromaddress));;

        InternetAddress[] addressTo = new InternetAddress[recipientes.length]; 
        for (int i = 0; i < recipientes.length; i++) {
            addressTo[i] = new InternetAddress(recipientes[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        //msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipiente, false));
        
        msg.setSubject(asunto);
        msg.setText(mensaje);
        
        msg.setHeader("X-Mailer", "Cartero");
        msg.setSentDate(new Date());
        
        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
        t.connect(this.servidor, this.usuario, this.clave);
        t.sendMessage(msg, msg.getAllRecipients());
        System.out.println("Response: " + t.getLastServerResponse());
        t.close();
    }

    /**
     * Otro metodo diferente para enviar mail
     * no se usa.
     * 
     * @param recipientes
     * @param asunto
     * @param mensaje
     * @param de
     * @throws MessagingException
     */
    public void enviarMail_send(String recipientes[], String asunto,
        String mensaje , String de) throws MessagingException {

        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", this.servidor);

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(this.fromaddress);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipientes.length]; 
        for (int i = 0; i < recipientes.length; i++) {
            addressTo[i] = new InternetAddress(recipientes[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Optional : You can also set your custom headers in the Email if you Want
        msg.addHeader("X-Mailer", "Cartero");

        // Setting the Subject and Content Type
        msg.setSubject(asunto);
        msg.setContent(mensaje, "text/plain");
        Transport.send(msg);
    }    
}