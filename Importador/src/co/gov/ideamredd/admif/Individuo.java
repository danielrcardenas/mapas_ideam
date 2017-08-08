// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;


/** 
 * Clase Individuo
 * Permite administrar los Individuos.
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 */
@SuppressWarnings("serial")
public class Individuo extends HttpServlet {

	public static String yo = "Individuo.";
	public static String charset = "ISO-8859-1";
	public static String css = "css/estilos.css";

    // Definir carpeta para generar los pdf de los individuos
    private static final String EXPORT_PDF_DIRECTORY = "pdf";
    private static final String UPLOAD_DIRECTORY = "imagenes_individuos";

	String RED_username = "";
	String RED_password = "";
	String RED_host = "";
	String RED_port = "";
	String RED_sid = "";
	
	String encriptar_usuario = "";
	String llave_encripcion = "";
	
	/**
	 * Método mudo para inicializar la clase A partir de la variable config se obtienen los parámetros de conexión a las bases de datos.
	 */
	public void init(ServletConfig config)
	throws ServletException
	{
		
		super.init(config);

		String p_RED_username = "RED_username";
		this.RED_username = getServletContext().getInitParameter(p_RED_username);
		
		String p_RED_password = "RED_password";
		this.RED_password = getServletContext().getInitParameter(p_RED_password);
		
		String p_RED_host = "RED_host";
		this.RED_host = getServletContext().getInitParameter(p_RED_host);
		
		String p_RED_port = "RED_port";
		this.RED_port = getServletContext().getInitParameter(p_RED_port);
		
		String p_RED_sid = "RED_sid";
		this.RED_sid = getServletContext().getInitParameter(p_RED_sid);
		
		String p_encriptar_usuario = "encriptar_usuario";
		this.encriptar_usuario = getServletContext().getInitParameter(p_encriptar_usuario);
		
		String p_llave_encripcion = "llave_encripcion";
		this.llave_encripcion = getServletContext().getInitParameter(p_llave_encripcion);
	}
	
	
	/**
	 * Método constructor
	 */
	public Individuo() {
	}
	
	
	/**
	 * Método que traduce a post el get si recibió un request get
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		doPost(request, response);
	}

		
	/**
	 * Método que procesa el request de POST que recibió la clase
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		String metodo = yo + "doPost";
		
		String usuario_recibido = "";
		String usuario = "";
		String idioma = "";
		String clave = "";
		
		// Declaración variables para valores de campos de las tablas de individuo
	    String db_INDV_CONSECUTIVO = "";
	    String db_PRCL_CONSECUTIVO = "";
	    
		String f_INDV_CONSECUTIVO = "";
		String f_PRCL_CONSECUTIVO = "";
		String f_INDV_ID_IMPORTACION = "";
		String f_INDV_ESARBOLREFERENCIA = "";
		String f_INDV_CARDINALIDAD = "";
		String f_INDV_NUMERO_COLECTOR = "";
		String f_INDV_CANTIDAD_EJEMPLARES = "";
		String f_INDV_DISTANCIA = "";
		String f_INDV_AZIMUTH = "";
		String f_INDV_NUMERO_ARBOL = "";
		String f_INDV_ESPECIE = "";
		String f_INDV_SUBPARCELA = "";
		String f_INDV_OBSERVACIONES = "";
		String f_INDV_ETIQUETA_COLECTA = "";
		String f_INDV_FOTO_COLECTA = "";
		String f_INDV_HOMOLOGACION = "";
		//String f_INDV_ARCHIVO_FOTOS = "";
		String f_INDV_TXCT_ID = "";
		
		String f_INDV_PRCL_PLOT = "";
		String f_INDV_DENSIDAD = "";
		String f_INDV_FAMILIA = "";
		String f_INDV_AUTORFAMILIA = "";
		String f_INDV_GENERO = "";
		String f_INDV_AUTORGENERO = "";
		String f_INDV_ESTADOEPITETO = "";
		String f_INDV_EPITETO = "";
		String f_INDV_MORFOESPECIE = "";
		String f_INDV_AUTORESPECIE = "";
		String f_INDV_HABITO = "";
		String f_INDV_INCLUIR = "";
		String f_INDV_FID = "";

		String retorno = "";
		String target = "";
		String ultimo_error = "";
		
	    // Instanciar auxiliar y archivo
	    //Auxiliar aux = new Auxiliar();
		Sec sec = new Sec();

		// Variables para depurar el código de la clase 
		
		// Instanciar lenguaje
		request.setCharacterEncoding("UTF-8");
		
		String accion = request.getParameter("accion");
		
		HttpSession session = sec.iniciarSesion(request);
		
		if (session != null) {
			if (sec.sesionVigente(session)) {
				try {
				    synchronized (session) {
				    	if (sec.sesionTieneAtributo(session, "idioma")) {
				    		idioma = Auxiliar.nzObjStr(session.getAttribute("idioma").toString(), "");
				    	}
				    	if (sec.sesionTieneAtributo(session, "usuario")) {
				    		usuario = Auxiliar.nzObjStr(session.getAttribute("usuario").toString(), "");
				    		if (!Auxiliar.tieneAlgo(usuario)) {
				    			accion = null;
				    		}
				    	}
				    }
				}
				catch (Exception e) {
					Auxiliar.mensajeImpersonal("error", "Error de sesión: " + e.toString());
					accion = null;
				}
				
				if (Auxiliar.nzObjStr(session.getAttribute("login_fallido"), "1").equals("1")) {
					accion = null;
				}
			}
			else {
				accion = null;
			}
		}
		else {
			accion = null;
		}
		
		request.setAttribute("datos_sesion", Auxiliar.mensajeImpersonal("datos_sesion", "Usuario: " + Auxiliar.nz(usuario, "--") + ", Idioma: " + Auxiliar.nz(idioma, "")));

		boolean usuarioAceptoLicencia = false;
		String nombreLicencia = "INVENTARIOS FORESTALES";
		String indicacionLicencia = "";
		
		if (sec.sesionVigente(session)) {	
			
			try {
				usuarioAceptoLicencia = sec.usuarioAceptoLicencia(usuario, nombreLicencia);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			if (usuarioAceptoLicencia) {
				request.setAttribute("usuarioAceptoLicencia", "1");
				indicacionLicencia = "";
			}
			else {
				request.setAttribute("usuarioAceptoLicencia", "0");
				indicacionLicencia = "Nota: para poder exportar o descargar este recurso es necesario que acepte el acuerdo de licencia " + nombreLicencia + " en el <a href='/MonitoreoBC-WEB/reg/modificarUsuario.jsp'>módulo de datos personales</a>";
			}
			request.setAttribute("indicacionLicencia", indicacionLicencia);
		}
		
		
		if (accion == null) {
			target = "/login.jsp";
			retorno = Auxiliar.mensajeImpersonal("advertencia", Auxiliar.traducir("SESION_VENCIDA", "es", "Credenciales erradas o su sesión ha vencido"));
			try {
				sec.registrarTransaccion(request, 194, "", "Individuo", false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("opciones_individuo"))
		{
			target = "/ajax_opciones.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				retorno = opcionesIndividuos(
						Auxiliar.nz(request.getParameter("f_consecutivo"), ""),
						Auxiliar.nz(request.getParameter("f_placa"), ""),
						Auxiliar.nz(request.getParameter("f_nombre_comun"), ""),
						Auxiliar.nz(request.getParameter("f_especie"), ""),
						session
						);
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a subirArchivo(): " + e.toString(), usuario, metodo);
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("buscar"))
		{
			target = "/busqueda_individuos.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				if (
						sec.tienePermiso(usuario,  "28") 
					||	sec.tienePermiso(usuario,  "29") 
					||	sec.tienePermiso(usuario,  "30") 
					||	sec.tienePermiso(usuario,  "31") 
					||	sec.tienePermiso(usuario,  "197") 
					) {
					request.setAttribute("PRCL_CONSECUTIVO", Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""));
					retorno = "";

					sec.registrarTransaccion(request, 197, "", "", true);
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 197, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 197, "", "excepcion:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("encontrar")) 
		{
			target = "/ajax_individuos.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				if (
						sec.tienePermiso(usuario,  "28") 
					||	sec.tienePermiso(usuario,  "29") 
					||	sec.tienePermiso(usuario,  "30") 
					||	sec.tienePermiso(usuario,  "31") 
					||	sec.tienePermiso(usuario,  "197") 
					) {
					
					f_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), "");
					
					String resultado = encontrarIndividuos(
							Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), 
							Auxiliar.nz(request.getParameter("INDV_NUMERO_ARBOL"), ""), 
							f_INDV_CONSECUTIVO, 
							Auxiliar.nz(request.getParameter("INDV_ID_IMPORTACION"), ""), 
							Auxiliar.nz(request.getParameter("INDV_ESARBOLREFERENCIA"), ""), 
							Auxiliar.nz(request.getParameter("INDV_CARDINALIDAD"), ""), 
							Auxiliar.nz(request.getParameter("INDV_NUMERO_COLECTOR"), ""), 
							Auxiliar.nz(request.getParameter("INDV_CANTIDAD_EJEMPLARES"), ""), 
							Auxiliar.nz(request.getParameter("f_desde"), ""), 
							Auxiliar.nz(request.getParameter("f_hasta"), ""), 
							Auxiliar.nz(request.getParameter("f_especie"), ""),
							session
							);
					String [] a_resultado = resultado.split("-=-");
					request.setAttribute("n", a_resultado[0]);
					retorno = a_resultado[1];
					
					sec.registrarTransaccion(request, 196, "", "", true);

				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 196, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 196, "", "excepcion:"+e.toString(), true);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("encontrar_especies"))
		{
			target = "/ajax_resultados.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				String resultado = encontrarEspecies(Auxiliar.nz(request.getParameter("texto"), ""), session);
				retorno = resultado;

				sec.registrarTransaccion(request, 198, "", Auxiliar.nz(request.getParameter("texto"), ""), true);

			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarEspecies(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 198, "", Auxiliar.nz(request.getParameter("texto"), "")+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("detalle_individuo"))
		{
			target = "/detalle_individuo.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				if (
						sec.tienePermiso(usuario,  "32") 
					||	sec.tienePermiso(usuario,  "33") 
					||	sec.tienePermiso(usuario,  "34") 
					||	sec.tienePermiso(usuario,  "35") 
					) {
					f_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), "");

					f_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "");
					db_PRCL_CONSECUTIVO = f_PRCL_CONSECUTIVO;
					
					request = establecerAtributos(request, session, "");
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 199, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 199, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("guardar"))
		{
			response.setContentType("text/html; charset=UTF-8");
			try {
				if (
						sec.tienePermiso(usuario,  "184") 
						||	sec.tienePermiso(usuario,  "201")
						||	sec.tienePermiso(usuario,  "226")
						||	sec.tienePermiso(usuario,  "225")
						) {
					
					f_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""); 
					f_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""); 
					f_INDV_ID_IMPORTACION = Auxiliar.nz(request.getParameter("INDV_ID_IMPORTACION"), ""); 
					f_INDV_ESARBOLREFERENCIA = Auxiliar.nz(request.getParameter("INDV_ESARBOLREFERENCIA"), ""); 
					f_INDV_CARDINALIDAD = Auxiliar.nz(request.getParameter("INDV_CARDINALIDAD"), ""); 
					f_INDV_NUMERO_COLECTOR = Auxiliar.nz(request.getParameter("INDV_NUMERO_COLECTOR"), ""); 
					f_INDV_CANTIDAD_EJEMPLARES = Auxiliar.nz(request.getParameter("INDV_CANTIDAD_EJEMPLARES"), ""); 
					f_INDV_DISTANCIA = Auxiliar.nz(request.getParameter("INDV_DISTANCIA"), "");
					f_INDV_AZIMUTH = Auxiliar.nz(request.getParameter("INDV_AZIMUTH"), "");
					f_INDV_NUMERO_ARBOL = Auxiliar.nz(request.getParameter("INDV_NUMERO_ARBOL"), "");
					f_INDV_ESPECIE = Auxiliar.nz(request.getParameter("INDV_ESPECIE"), "");
					f_INDV_SUBPARCELA = Auxiliar.nz(request.getParameter("INDV_SUBPARCELA"), "");
					f_INDV_OBSERVACIONES = Auxiliar.nz(request.getParameter("INDV_OBSERVACIONES"), "");
					f_INDV_ETIQUETA_COLECTA = Auxiliar.nz(request.getParameter("INDV_ETIQUETA_COLECTA"), "");
					f_INDV_FOTO_COLECTA = Auxiliar.nz(request.getParameter("INDV_FOTO_COLECTA"), "");
					f_INDV_HOMOLOGACION = Auxiliar.nz(request.getParameter("INDV_HOMOLOGACION"), "");
					//f_INDV_ARCHIVO_FOTOS = Auxiliar.nz(request.getParameter("INDV_ARCHIVO_FOTOS"), "NULL");
					f_INDV_TXCT_ID = Auxiliar.nz(request.getParameter("INDV_TXCT_ID"), "NULL");

					f_INDV_PRCL_PLOT = Auxiliar.nz(request.getParameter("INDV_PRCL_PLOT"), "NULL");
					f_INDV_DENSIDAD = Auxiliar.nz(request.getParameter("INDV_DENSIDAD"), "NULL");
					f_INDV_FAMILIA = Auxiliar.nz(request.getParameter("INDV_FAMILIA"), "NULL");
					f_INDV_AUTORFAMILIA = Auxiliar.nz(request.getParameter("INDV_AUTORFAMILIA"), "NULL");
					f_INDV_GENERO = Auxiliar.nz(request.getParameter("INDV_GENERO"), "NULL");
					f_INDV_AUTORGENERO = Auxiliar.nz(request.getParameter("INDV_AUTORGENERO"), "NULL");
					f_INDV_ESTADOEPITETO = Auxiliar.nz(request.getParameter("INDV_ESTADOEPITETO"), "NULL");
					f_INDV_EPITETO = Auxiliar.nz(request.getParameter("INDV_EPITETO"), "NULL");
					f_INDV_MORFOESPECIE = Auxiliar.nz(request.getParameter("INDV_MORFOESPECIE"), "NULL");
					f_INDV_AUTORESPECIE = Auxiliar.nz(request.getParameter("INDV_AUTORESPECIE"), "NULL");
					f_INDV_HABITO = Auxiliar.nz(request.getParameter("INDV_HABITO"), "NULL");
					f_INDV_INCLUIR = Auxiliar.nz(request.getParameter("INDV_INCLUIR"), "NULL");
					f_INDV_FID = Auxiliar.nz(request.getParameter("INDV_FID"), "NULL");
					
					String resultado = guardar(
						f_INDV_CONSECUTIVO, 
						f_PRCL_CONSECUTIVO, 
						f_INDV_ID_IMPORTACION, 
						f_INDV_ESARBOLREFERENCIA, 
						f_INDV_CARDINALIDAD, 
						f_INDV_NUMERO_COLECTOR, 
						f_INDV_CANTIDAD_EJEMPLARES, 
						f_INDV_DISTANCIA,
						f_INDV_AZIMUTH,
						f_INDV_NUMERO_ARBOL,
						f_INDV_ESPECIE,
						f_INDV_SUBPARCELA,
						f_INDV_OBSERVACIONES,
						f_INDV_ETIQUETA_COLECTA,
						f_INDV_FOTO_COLECTA,
						f_INDV_HOMOLOGACION,
						//f_INDV_ARCHIVO_FOTOS,
						f_INDV_TXCT_ID,
						"",
						f_INDV_PRCL_PLOT,
						f_INDV_DENSIDAD,
						f_INDV_FAMILIA,
						f_INDV_AUTORFAMILIA,
						f_INDV_GENERO,
						f_INDV_AUTORGENERO,
						f_INDV_ESTADOEPITETO,
						f_INDV_EPITETO,
						f_INDV_MORFOESPECIE,
						f_INDV_AUTORESPECIE,
						f_INDV_HABITO,
						f_INDV_INCLUIR,
						f_INDV_FID,
						"SYSDATE",
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						"",
						request,
						this.RED_username, this.RED_password, this.RED_host, this.RED_port, this.RED_sid, false
						);
				
					retorno = "";
					
					String [] a_resultado = resultado.split("-=-");
					if (a_resultado.length == 2) {
						retorno = a_resultado[1];
					}

					if (!a_resultado[0].equals("0"))
					{
						db_INDV_CONSECUTIVO = a_resultado[0];
						
						request = establecerAtributos(request, session, db_INDV_CONSECUTIVO);
					}
					else {
						request.setAttribute("INDV_CONSECUTIVO", f_INDV_CONSECUTIVO);
						request.setAttribute("PRCL_CONSECUTIVO", f_PRCL_CONSECUTIVO);
						request.setAttribute("INDV_ID_IMPORTACION", f_INDV_ID_IMPORTACION);
						request.setAttribute("INDV_ESARBOLREFERENCIA", f_INDV_ESARBOLREFERENCIA);
						request.setAttribute("INDV_CARDINALIDAD", f_INDV_CARDINALIDAD);
						request.setAttribute("INDV_NUMERO_COLECTOR", f_INDV_NUMERO_COLECTOR);
						request.setAttribute("INDV_CANTIDAD_EJEMPLARES", f_INDV_CANTIDAD_EJEMPLARES);
						request.setAttribute("INDV_DISTANCIA", f_INDV_DISTANCIA);
						request.setAttribute("INDV_AZIMUTH", f_INDV_AZIMUTH);
						request.setAttribute("INDV_NUMERO_ARBOL", f_INDV_NUMERO_ARBOL);
						request.setAttribute("INDV_ESPECIE", f_INDV_ESPECIE);
						request.setAttribute("INDV_SUBPARCELA", f_INDV_SUBPARCELA);
						request.setAttribute("INDV_OBSERVACIONES", f_INDV_OBSERVACIONES);
						request.setAttribute("INDV_ETIQUETA_COLECTA", f_INDV_ETIQUETA_COLECTA);
						request.setAttribute("INDV_FOTO_COLECTA", f_INDV_FOTO_COLECTA);
						request.setAttribute("INDV_HOMOLOGACION", f_INDV_HOMOLOGACION);
						//request.setAttribute("INDV_ARCHIVO_FOTOS", f_INDV_ARCHIVO_FOTOS);						
						request.setAttribute("INDV_TXCT_ID", f_INDV_TXCT_ID);						
					}
				
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 184, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
					sec.registrarTransaccion(request, 201, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
					sec.registrarTransaccion(request, 225, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
					sec.registrarTransaccion(request, 226, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 184, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
					sec.registrarTransaccion(request, 201, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
					sec.registrarTransaccion(request, 225, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
					sec.registrarTransaccion(request, 226, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			target = "/detalle_individuo.jsp?usuario="+usuario_recibido+"&idioma="+idioma+"&mensaje_edicion="+retorno;
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("eliminar"))
		{
			response.setContentType("text/html; charset=UTF-8");
			try {
				if (
						sec.tienePermiso(usuario,  "230") 
						||	sec.tienePermiso(usuario,  "229")
						) {
					f_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "");
					db_PRCL_CONSECUTIVO = f_PRCL_CONSECUTIVO;
					
					String resultado = eliminar(Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), request);
					String [] a_resultado = resultado.split("-=-");
					retorno = a_resultado[1];

					request = establecerAtributos(request, session, "");
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 230, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
					sec.registrarTransaccion(request, 229, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 230, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
					sec.registrarTransaccion(request, 229, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			target = "/detalle_individuo.jsp";
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else if (accion.equals("exportar_individuos"))
		{
			boolean ok = false;
			try {
				if (sec.tienePermiso(usuario,  "121")) {
					
					f_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), "");
					
					if (usuarioAceptoLicencia) {
						ok = exportarIndividuosExcel(
							Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), 
							Auxiliar.nz(request.getParameter("INDV_NUMERO_ARBOL"), ""), 
							f_INDV_CONSECUTIVO, 
							Auxiliar.nz(request.getParameter("INDV_ID_IMPORTACION"), ""), 
							Auxiliar.nz(request.getParameter("INDV_ESARBOLREFERENCIA"), ""), 
							Auxiliar.nz(request.getParameter("INDV_CARDINALIDAD"), ""), 
							Auxiliar.nz(request.getParameter("INDV_NUMERO_COLECTOR"), ""), 
							Auxiliar.nz(request.getParameter("INDV_CANTIDAD_EJEMPLARES"), ""), 
							Auxiliar.nz(request.getParameter("f_desde"), ""), 
							Auxiliar.nz(request.getParameter("f_hasta"), ""), 
							Auxiliar.nz(request.getParameter("f_especie"), ""),
							response,
							session
							);
					
						if (ok) {
							sec.registrarTransaccion(request, 121, "", "", true);
						}
						else {
							sec.registrarTransaccion(request, 121, "", "error", false);
						}
					}
					else {
						sec.registrarTransaccion(request, 121, "", "Sin licencia para INVENTARIOS FORESTALES.", false);
						retorno = Auxiliar.mensaje("advertencia", "Debe aceptar la licencia de INVENTARIOS FORESTALES para poder realizar esta acción.", usuario, metodo);
					}
				}
				else {
					ok = false;
					ultimo_error = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 121, "", "permisos", false);
				}
			} catch (Exception e) {				
				try {
					sec.registrarTransaccion(request, 121, "", "excepcion", false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			if (!ok) {
				retorno = ultimo_error;
				
				try {
					request = establecerAtributos(request, session, "");
				}
				catch (Exception e) {
					
				}
				
				target = "/detalle_individuo.jsp";
				response.setContentType("text/html; charset=UTF-8");
				request.setAttribute("retorno", retorno);
				ServletContext context = getServletContext();
				RequestDispatcher dispatcher = context.getRequestDispatcher(target);
				dispatcher.forward(request, response);
			}
			
		}
		else if (accion.equals("plantilla_individuos"))
		{
			boolean ok = false;
			try {
				if (sec.tienePermiso(usuario, "43")) {
					ok = generarPlantillaIndividuos(response, session);
					if (ok) {
						sec.registrarTransaccion(request, 43, "", "", true);
					}
					else {
						sec.registrarTransaccion(request, 43, "", "error", false);
					}
				}
				else {
					sec.registrarTransaccion(request, 43, "", "permisos", false);
				}
			} 
			catch (Exception e) {				
				try {
					sec.registrarTransaccion(request, 43, "", "excepcion", false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		else if (accion.equals("exportar_pdf"))
		{
			try {
				if (sec.tienePermiso(usuario, "122")) {
					if (usuarioAceptoLicencia) {
						exportarIndividuoPDF(Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), false, session, false, response);
						return;
					}
					else {
						sec.registrarTransaccion(request, 121, "", "Sin licencia para INVENTARIOS FORESTALES.", false);
						retorno = Auxiliar.mensaje("advertencia", "Debe aceptar la licencia de INVENTARIOS FORESTALES para poder realizar esta acción.", usuario, metodo);
					}
				}
				else {
					sec.registrarTransaccion(request, 122, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a exportarParcelaPDF(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 122, Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		else
		{
			target = "/error.jsp";
			retorno = "No se encontró la información solicitada para la acción " + accion;
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
			try {
				sec.registrarTransaccion(request, 195, "", "Individuo", false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Establece los atributos del request como parámetros por defecto del formulario
	 * 
	 * @param request
	 * @return request con parámetros actualizados desde la base de datos o, en su defecto, desde el post del formulario
	 * @throws Exception 
	 */
	private HttpServletRequest establecerAtributos(HttpServletRequest request, HttpSession session, String nuevo_consecutivo) 
			throws Exception {
		String metodo = yo + "establecerAtributos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String INDV_CONSECUTIVO = "";
	    
	    INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), "");
	    
	    if (Auxiliar.tieneAlgo(nuevo_consecutivo)) INDV_CONSECUTIVO = nuevo_consecutivo;
	    
		String db_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), "");
		String db_INDV_ID_IMPORTACION = Auxiliar.nz(request.getParameter("INDV_ID_IMPORTACION"), "");
		String db_INDV_ESARBOLREFERENCIA = Auxiliar.nz(request.getParameter("INDV_ESARBOLREFERENCIA"), "");
		String db_INDV_CARDINALIDAD = Auxiliar.nz(request.getParameter("INDV_CARDINALIDAD"), "");
		String db_INDV_NUMERO_COLECTOR = Auxiliar.nz(request.getParameter("INDV_NUMERO_COLECTOR"), "");
		String db_INDV_CANTIDAD_EJEMPLARES = Auxiliar.nz(request.getParameter("INDV_CANTIDAD_EJEMPLARES"), "");
		String db_INDV_LATITUD = Auxiliar.nz(request.getParameter("INDV_LATITUD"), "");
		String db_INDV_LONGITUD = Auxiliar.nz(request.getParameter("INDV_LONGITUD"), "");
		String db_INDV_DISTANCIA = Auxiliar.nz(request.getParameter("INDV_DISTANCIA"), "");
		String db_INDV_AZIMUTH = Auxiliar.nz(request.getParameter("INDV_AZIMUTH"), "");
		String db_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "");
		
	    Sec sec = new Sec();
		String id_usuario = "";
		String id_creador = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		if (Auxiliar.tieneAlgo(db_PRCL_CONSECUTIVO)) {
			id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO+"", "");
		}		
		if (!id_usuario.equals(id_creador)) {
			if (!sec.tienePermiso(id_usuario, "184")) {
				sec.registrarTransaccion(request, 184, INDV_CONSECUTIVO, "permisos", false);
				return request;
			}
			else {
				sec.registrarTransaccion(request, 184, INDV_CONSECUTIVO, "", true);
			}
		}
		else {
			sec.registrarTransaccion(request, 201, INDV_CONSECUTIVO, "", true);
		}
		
		String db_INDV_NUMERO_ARBOL = Auxiliar.nz(request.getParameter("INDV_NUMERO_ARBOL"), "");
		String db_INDV_ESPECIE = Auxiliar.nz(request.getParameter("INDV_ESPECIE"), "");
		String db_INDV_SUBPARCELA = Auxiliar.nz(request.getParameter("INDV_SUBPARCELA"), "");
		String db_INDV_OBSERVACIONES = Auxiliar.nz(request.getParameter("INDV_OBSERVACIONES"), "");
		String db_INDV_ETIQUETA_COLECTA = Auxiliar.nz(request.getParameter("INDV_ETIQUETA_COLECTA"), "");
		String db_INDV_FOTO_COLECTA = Auxiliar.nz(request.getParameter("INDV_FOTO_COLECTA"), "");
		String db_INDV_HOMOLOGACION = Auxiliar.nz(request.getParameter("INDV_HOMOLOGACION"), "");
		//String db_INDV_ARCHIVO_FOTOS = Auxiliar.nz(request.getParameter("INDV_ARCHIVO_FOTOS"), "");
		String db_INDV_TXCT_ID = Auxiliar.nz(request.getParameter("INDV_TXCT_ID"), "");
		String db_INDV_FID = Auxiliar.nz(request.getParameter("INDV_FID"), "");
		String db_INDV_PRCL_PLOT = Auxiliar.nz(request.getParameter("INDV_PRCL_PLOT"), "");
		String db_INDV_DENSIDAD = Auxiliar.nz(request.getParameter("INDV_DENSIDAD"), "");
		String db_INDV_FAMILIA = Auxiliar.nz(request.getParameter("INDV_FAMILIA"), "");
		String db_INDV_AUTORFAMILIA = Auxiliar.nz(request.getParameter("INDV_AUTORFAMILIA"), "");
		String db_INDV_GENERO = Auxiliar.nz(request.getParameter("INDV_GENERO"), "");
		String db_INDV_AUTORGENERO = Auxiliar.nz(request.getParameter("INDV_AUTORGENERO"), "");
		String db_INDV_ESTADOEPITETO = Auxiliar.nz(request.getParameter("INDV_ESTADOEPITETO"), "");
		String db_INDV_EPITETO = Auxiliar.nz(request.getParameter("INDV_EPITETO"), "");
		String db_INDV_MORFOESPECIE = Auxiliar.nz(request.getParameter("INDV_MORFOESPECIE"), "");
		String db_INDV_AUTORESPECIE = Auxiliar.nz(request.getParameter("INDV_AUTORESPECIE"), "");
		String db_INDV_HABITO = Auxiliar.nz(request.getParameter("INDV_HABITO"), "");
		String db_INDV_INCLUIR = Auxiliar.nz(request.getParameter("INDV_INCLUIR"), "");
		String db_INDV_ACTUALIZACION = Auxiliar.nz(request.getParameter("INDV_ACTUALIZACION"), "");


		if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			
		    ResultSet rset = cargarRegistro(INDV_CONSECUTIVO, session);
		    
			try {
			    if (rset != null) {
					if (rset.next()) {
						db_INDV_CONSECUTIVO = rset.getString("INDV_CONSECUTIVO");
						db_INDV_ID_IMPORTACION = rset.getString("INDV_ID_IMPORTACION");
						db_INDV_ESARBOLREFERENCIA = rset.getString("INDV_ESARBOLREFERENCIA");
						db_INDV_CARDINALIDAD = rset.getString("INDV_CARDINALIDAD");
						db_INDV_NUMERO_COLECTOR = rset.getString("INDV_NUMERO_COLECTOR");
						db_INDV_CANTIDAD_EJEMPLARES = rset.getString("INDV_CANTIDAD_EJEMPLARES");
						db_INDV_LATITUD = rset.getString("INDV_LATITUD");
						db_INDV_LONGITUD = rset.getString("INDV_LONGITUD");
						db_INDV_DISTANCIA = rset.getString("INDV_DISTANCIA");
						db_INDV_AZIMUTH = rset.getString("INDV_AZIMUTH");
						db_INDV_LATITUD = rset.getString("INDV_LATITUD");
						db_INDV_LONGITUD = rset.getString("INDV_LONGITUD");
						db_PRCL_CONSECUTIVO = rset.getString("INDV_PRCL_CONSECUTIVO");
						db_INDV_NUMERO_ARBOL = rset.getString("INDV_NUMERO_ARBOL");
						db_INDV_ESPECIE = rset.getString("INDV_ESPECIE");
						db_INDV_SUBPARCELA = rset.getString("INDV_SUBPARCELA");
						db_INDV_OBSERVACIONES = rset.getString("INDV_OBSERVACIONES");
						db_INDV_ETIQUETA_COLECTA = rset.getString("INDV_ETIQUETA_COLECTA");
						db_INDV_FOTO_COLECTA = rset.getString("INDV_FOTO_COLECTA");
						db_INDV_HOMOLOGACION = rset.getString("INDV_HOMOLOGACION");
						//db_INDV_ARCHIVO_FOTOS = rset.getString("INDV_ARCHIVO_FOTOS");
						db_INDV_TXCT_ID = rset.getString("INDV_TXCT_ID");
						db_INDV_FID = rset.getString("INDV_FID");
						db_INDV_PRCL_PLOT = rset.getString("INDV_PRCL_PLOT");
						db_INDV_DENSIDAD = rset.getString("INDV_DENSIDAD");
						db_INDV_FAMILIA = rset.getString("INDV_FAMILIA");
						db_INDV_AUTORFAMILIA = rset.getString("INDV_AUTORFAMILIA");
						db_INDV_GENERO = rset.getString("INDV_GENERO");
						db_INDV_AUTORGENERO = rset.getString("INDV_AUTORGENERO");
						db_INDV_ESTADOEPITETO = rset.getString("INDV_ESTADOEPITETO");
						db_INDV_EPITETO = rset.getString("INDV_EPITETO");
						db_INDV_MORFOESPECIE = rset.getString("INDV_MORFOESPECIE");
						db_INDV_AUTORESPECIE = rset.getString("INDV_AUTORESPECIE");
						db_INDV_HABITO = rset.getString("INDV_HABITO");
						db_INDV_INCLUIR = rset.getString("INDV_INCLUIR");
						db_INDV_ACTUALIZACION = rset.getString("INDV_ACTUALIZACION");
	
						rset.close();
					}
				}
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("INDV_CONSECUTIVO", db_INDV_CONSECUTIVO);
		request.setAttribute("PRCL_CONSECUTIVO", db_PRCL_CONSECUTIVO);
		request.setAttribute("INDV_ID_IMPORTACION", db_INDV_ID_IMPORTACION);
		request.setAttribute("INDV_ESARBOLREFERENCIA", db_INDV_ESARBOLREFERENCIA);
		request.setAttribute("INDV_CARDINALIDAD", db_INDV_CARDINALIDAD);
		request.setAttribute("INDV_NUMERO_COLECTOR", db_INDV_NUMERO_COLECTOR);
		request.setAttribute("INDV_CANTIDAD_EJEMPLARES", db_INDV_CANTIDAD_EJEMPLARES);
		request.setAttribute("INDV_LATITUD", db_INDV_LATITUD);
		request.setAttribute("INDV_LONGITUD", db_INDV_LONGITUD);
		request.setAttribute("INDV_DISTANCIA", db_INDV_DISTANCIA);
		request.setAttribute("INDV_AZIMUTH", db_INDV_AZIMUTH);
		request.setAttribute("INDV_NUMERO_ARBOL", db_INDV_NUMERO_ARBOL);
		request.setAttribute("INDV_ESPECIE", db_INDV_ESPECIE);
		request.setAttribute("INDV_SUBPARCELA", db_INDV_SUBPARCELA);
		request.setAttribute("INDV_OBSERVACIONES", db_INDV_OBSERVACIONES);
		request.setAttribute("INDV_ETIQUETA_COLECTA", db_INDV_ETIQUETA_COLECTA);
		request.setAttribute("INDV_FOTO_COLECTA", db_INDV_FOTO_COLECTA);
		request.setAttribute("INDV_HOMOLOGACION", db_INDV_HOMOLOGACION);
		//request.setAttribute("INDV_ARCHIVO_FOTOS", db_INDV_ARCHIVO_FOTOS);
		request.setAttribute("INDV_TXCT_ID", db_INDV_TXCT_ID);
		request.setAttribute("INDV_FID", db_INDV_FID);
		request.setAttribute("INDV_PRCL_PLOT", db_INDV_PRCL_PLOT);
		request.setAttribute("INDV_DENSIDAD", db_INDV_DENSIDAD);
		request.setAttribute("INDV_FAMILIA", db_INDV_FAMILIA);
		request.setAttribute("INDV_AUTORFAMILIA", db_INDV_AUTORFAMILIA);
		request.setAttribute("INDV_GENERO", db_INDV_GENERO);
		request.setAttribute("INDV_AUTORGENERO", db_INDV_AUTORGENERO);
		request.setAttribute("INDV_ESTADOEPITETO", db_INDV_ESTADOEPITETO);
		request.setAttribute("INDV_EPITETO", db_INDV_EPITETO);
		request.setAttribute("INDV_MORFOESPECIE", db_INDV_MORFOESPECIE);
		request.setAttribute("INDV_AUTORESPECIE", db_INDV_AUTORESPECIE);
		request.setAttribute("INDV_HABITO", db_INDV_HABITO);
		request.setAttribute("INDV_INCLUIR", db_INDV_INCLUIR);
		request.setAttribute("INDV_ACTUALIZACION", db_INDV_ACTUALIZACION);
		
		request.setAttribute("idioma",idioma);

		request.setAttribute("opciones_habito", Auxiliar.cargarOpciones("SELECT HABI_ID, HABI_NOMBRE FROM RED_HABITO ORDER BY HABI_NOMBRE", "HABI_ID", "HABI_NOMBRE", db_INDV_HABITO, "", false, true, false));
		request.setAttribute("opciones_incluir", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", db_INDV_INCLUIR, "", false, true, false));
		request.setAttribute("o_INDV_ESARBOLREFERENCIA", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", db_INDV_ESARBOLREFERENCIA, "", false, true, false));
		
		if (Auxiliar.tieneAlgo(db_PRCL_CONSECUTIVO)) {
			
			request.setAttribute("CX", dbREDD.obtenerDato("SELECT AVG(t.X) AS CX FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO, "-73"));
			request.setAttribute("CY", dbREDD.obtenerDato("SELECT AVG(t.Y) AS CY FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO, "4"));
			
			request.setAttribute("PRCL_LONGITUD", dbREDD.obtenerDato("SELECT PRCL_LONGITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO, "-73"));
			request.setAttribute("PRCL_LATITUD", dbREDD.obtenerDato("SELECT PRCL_LATITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO, "4"));

			Parcela parce = new Parcela();
			
			request.setAttribute("etiqueta_SPF1", parce.generarEtiquetaSPF("1", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF2", parce.generarEtiquetaSPF("2", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF3", parce.generarEtiquetaSPF("3", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF4", parce.generarEtiquetaSPF("4", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF5", parce.generarEtiquetaSPF("5", db_PRCL_CONSECUTIVO, session));


		}

		dbREDD.desconectarse();
		
		return request;
	}
	
	/**
	 * Escanea la carpeta del individuo y registra las imágenes aún no registradas
	 * 
	 * @param INDV_CONSECUTIVO
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public void imaginar(String INDV_CONSECUTIVO, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "imaginar";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();

		
        //String carpetaImportacion = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

        String ruta_app = getServletContext().getRealPath("");
		//String ruta_carpeta_imagenes_parcelas = ruta_app + File.separator + "imagenes_parcelas";
		String carpetaImportacion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_individuos'", "");
		if (!Auxiliar.tieneAlgo(carpetaImportacion)) {
			carpetaImportacion = ruta_app + File.separator + "imagenes_individuos";
		}

        String ruta_carpeta = carpetaImportacion + File.separator + INDV_CONSECUTIVO;
        
        try {
        	archie.crearCarpeta(ruta_carpeta);
        }
        catch (Exception e) {}
		
		String[] a_imagenes = archie.archivosEnCarpeta(ruta_carpeta);
		
		int i = 0;
		
		for (i=0; i<a_imagenes.length; i++) {
			String nombre_archivo = a_imagenes[i];
			String ruta_archivo = ruta_carpeta + File.separator + nombre_archivo;
	
	        try {
	            if (archie.existeArchivo(ruta_archivo)) {
	                String id_imagen_existente = dbREDD.obtenerDato("SELECT IIMG_ID FROM RED_INDIVIDUO_IMAGEN WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO+" NOMBRE='"+nombre_archivo +"'", "");
	                if (id_imagen_existente.equals("")) {
	                	dbREDD.ejecutarSQL("INSERT INTO RED_INDIVIDUO_IMAGEN (INDV_CONSECUTIVO, NOMBRE) VALUES ("+INDV_CONSECUTIVO+", '" + nombre_archivo + "')");
	                }
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dbREDD.desconectarse();
	}
	
	
	/**
	 * Genera el mapa de ubicación de un individuo dentro de la parcela
	 * 	
	 * @param INDV_CONSECUTIVO
	 * @return true si lo pudo generar, false de lo contrario
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean generarMapaIndividuo(String INDV_CONSECUTIVO, HttpSession session) 
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarMapaIndividuo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		
		String nl = "\n";

		String html = "";
		
		String PRCL_CONSECUTIVO = dbREDD.obtenerDato("SELECT INDV_PRCL_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO, "");

		String longitud = dbREDD.obtenerDato("SELECT INDV_LONGITUD FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO, "");
		String latitud = dbREDD.obtenerDato("SELECT INDV_LATITUD FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO, "");
		
		if (longitud.equals("")) {
			dbREDD.desconectarse();
			return false;
		}

		if (latitud.equals("")) {
			dbREDD.desconectarse();
			return false;
		}
		
		/*
		String CX = longitud;
		String CY = latitud;
		*/
		String S = "-73";
		String W = "4";
		String N = "-73";
		String E = "4";
		
		if (!PRCL_CONSECUTIVO.equals("")) {
			/*
			CX = dbREDD.obtenerDato("SELECT t.X AS CX FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, longitud);
			CY = dbREDD.obtenerDato("SELECT t.Y AS CY FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, latitud);
			*/
			S = dbREDD.obtenerDato("SELECT MIN(t.Y) AS S FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "-73");
			W = dbREDD.obtenerDato("SELECT MIN(t.X) AS W FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "4");
			N = dbREDD.obtenerDato("SELECT MAX(t.Y) AS N FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "-73");
			E = dbREDD.obtenerDato("SELECT MAX(t.X) AS E FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "4");
		}
		
		try {
			html = "";
			html += "<!DOCTYPE html>";
			html += nl + "<html>";
			html += nl + "<head>";
			html += nl + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
	
			html += nl + "<link rel='stylesheet' href='leaflet.css' />";
			html += nl + "<script src='jquery-1.11.0.min.js'></script>";
			html += nl + "</head>";
			html += nl + "<body style='text-align: center;'>";
	
	
			html += nl + "<div id='map' style='width: 540px; height: 800px'></div>";
			html += nl + "<div id='viewparams' style='color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;'></div>";
	
			html += nl + "<script type='text/javascript' src='leaflet.js'></script>";
			html += nl + "<script type='text/javascript'>";
			html += nl + "var str_viewparams = '';";
			html += nl + "var PRCL_CONSECUTIVO = '" + PRCL_CONSECUTIVO + "';";
			html += nl + "var w_PRCL_CONSECUTIVO = '';";
			html += nl + "if (PRCL_CONSECUTIVO.length > 0) {";
			html += nl + "w_PRCL_CONSECUTIVO = ' AND PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO + " ';";
			html += nl + "str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';';"; 
			html += nl + "}";
	
			html += nl + "if (str_viewparams.length>0) {";
			html += nl + "str_viewparams = str_viewparams.slice(0,-1);";
			html += nl + "}";
			
			html += nl + "var map = L.map('map').setView(["+latitud+", "+longitud+"], 8);";
			html += nl + "map.touchZoom.disable();";
			html += nl + "map.doubleClickZoom.disable();";
			html += nl + "map.scrollWheelZoom.disable();";
			html += nl + "map.boxZoom.disable();";
			html += nl + "map.keyboard.disable();";
			html += nl + "$('.leaflet-control-zoom').css('visibility', 'hidden');";
											
			html += nl + "L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {";
			html += nl + "maxZoom: 18,";
			html += nl + "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores',";
			html += nl + "id: 'examples.map-i875mjb7'";
			html += nl + "}).addTo(map);";

			/*
			String wms_parcelas_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_parcelas_url'", "http://52.4.164.82:8080/geoserver/OracleAmazon/wms");
			String wms_parcelas_parametro_version = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_parcelas_parametro_version'", "1.1.0");
			String wms_parcelas_parametro_layers = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_parcelas_parametro_layers'", "OracleAmazon:C_RED_PARCELA_PARAMETRIZADA");

			html += nl + "var wms_parcelas_url = '"+wms_parcelas_url+"';";
			html += nl + "var wms_parcelas_parametro_version = '"+wms_parcelas_parametro_version+"';";
			html += nl + "var wms_parcelas_parametro_layers = '"+wms_parcelas_parametro_layers+"';";

			html += nl + "var owsrootUrl = wms_parcelas_url;";

			//html += "var parcelas = L.tileLayer.wms('http://54.172.131.5:8080/geoserver/OracleAmazon/wms', {";
			html += nl + "var parcelas = L.tileLayer.wms(wms_parcelas_url, {";
			//html += "layers: 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',";
			html += nl + "layers: wms_parcelas_parametro_layers,";
		    html += nl + "format: 'image/png',";
			html += nl + "transparent: true,";
			//html += "version: '1.1.0',";
			html += nl + "version : wms_parcelas_parametro_version,";

			html += nl + "viewparams: str_viewparams,"; 
			html += nl + "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores'";
			html += nl + "});";
			html += nl + "parcelas.addTo(map);";
			 */
			
			String CX = dbREDD.obtenerDato("SELECT PRCL_LONGITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "-73");
			String CY = dbREDD.obtenerDato("SELECT PRCL_LATITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "4");
			
			html += "\n" + "var dCX = "+CX+";";
			html += "\n" + "var dCY = "+CY+";";
			
			html += "\n" + "var factor = 0.0000045;";
			//html += "\n" + "var bounds = [[dCY+(95*factor), dCX-(95*factor)], [dCY-(95*factor), dCX+(95*factor)]];";
			//html += "\n" + "L.rectangle(bounds, {color: '#bbbbbb', weight: 1}).addTo(map);";
			
			html += "\n" + "var SPF1_BRINZAL = L.circle([dCY, dCX+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF1_LATIZAL = L.circle([dCY, dCX], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF1_FUSTAL = L.circle([dCY, dCX], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF1_FUSTAL_GRANDE = L.circle([dCY, dCX], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";

			html += "\n" + "var SPF2_BRINZAL = L.circle([dCY+(80*factor), dCX+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF2_LATIZAL = L.circle([dCY+(80*factor), dCX], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF2_FUSTAL = L.circle([dCY+(80*factor), dCX], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF2_FUSTAL_GRANDE = L.circle([dCY+(80*factor), dCX], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";

			html += "\n" + "var SPF3_BRINZAL = L.circle([dCY, dCX+(80*factor)+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF3_LATIZAL = L.circle([dCY, dCX+(80*factor)], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF3_FUSTAL = L.circle([dCY, dCX+(80*factor)], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF3_FUSTAL_GRANDE = L.circle([dCY, dCX+(80*factor)], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";

			html += "\n" + "var SPF4_BRINZAL = L.circle([dCY-(80*factor), dCX+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF4_LATIZAL = L.circle([dCY-(80*factor), dCX], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF4_FUSTAL = L.circle([dCY-(80*factor), dCX], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF4_FUSTAL_GRANDE = L.circle([dCY-(80*factor), dCX], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";

			html += "\n" + "var SPF5_BRINZAL = L.circle([dCY, dCX-(80*factor)+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF5_LATIZAL = L.circle([dCY, dCX-(80*factor)], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF5_FUSTAL = L.circle([dCY, dCX-(80*factor)], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";
			html += "\n" + "var SPF5_FUSTAL_GRANDE = L.circle([dCY, dCX-(80*factor)], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);";

			
			html += nl + "var a_coordenadas = ["+latitud+","+longitud+"];";

			html += nl + "L.marker(a_coordenadas).addTo(map);";
			
			html += nl + "map.fitBounds([["+S+","+W+"],["+N+","+E+"]]);";
			
			//html += "map.setZoom(map.getZoom()-2);";
			
			html += nl + "setTimeout(function(){window.status='ya';}, 1000);";
			
			html += nl + "</script>";
			
			html += nl + "</body>";
			html += nl + "</html>";
			
			String ruta_app = getServletContext().getRealPath(""); 
		    String carpeta = ruta_app + File.separator + "mapas_individuos/";
		    
		    String ruta_html = carpeta + INDV_CONSECUTIVO + "-mapa.html";
		    String ruta_jpg = carpeta + INDV_CONSECUTIVO + "-mapa.jpg";
		    
			boolean ok_crear_archivo_html = archie.escribirArchivo(ruta_html, html);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Archivo html mapa individuo creado en: " + ruta_html);
			
			if (ok_crear_archivo_html) {
				String commando_generacion = "wkhtmltoimage --width 555 --window-status ya -f jpg --quality 100 --encoding UTF-8 --javascript-delay 1000 --no-stop-slow-scripts " + ruta_html + " " + ruta_jpg;
				String resultado_generacion = Auxiliar.commander(commando_generacion, commando_generacion, session);
				
				String [] a_resultado_generacion = resultado_generacion.split("-=-");
	
				String r = "";
				
				if (!Auxiliar.nz(a_resultado_generacion[0], "").equals("0")) {
					r = "No se pudo generar la imagen: " + resultado_generacion;
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
					dbREDD.desconectarse();
					return false;
				}
				else {
					r = "Generación de mapa del individuo " + INDV_CONSECUTIVO + " exitosa [" + resultado_generacion + "].";
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
					//archie.eliminarArchivo(ruta_html);
					dbREDD.desconectarse();
					return archie.existeArchivo(ruta_jpg);
				}
			}
		}
		catch (Exception e) {
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + e.toString());
		}
		
		dbREDD.desconectarse();
		return false;
	}
	
	

	/**
	 * Método que inicializa los valores de los campos para un individuo especificado con f_codigo.
	 * 
	 * @param INDV_CONSECUTIVO
	 * @param session
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public ResultSet cargarRegistro(String INDV_CONSECUTIVO, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "cargarRegistro";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		ResultSet rset = null;
		
		try {
			imaginar(INDV_CONSECUTIVO, session);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		String sql = "SELECT ";
		sql += "INDV_CONSECUTIVO,";
		sql += "INDV_ID_IMPORTACION,";
		sql += "INDV_ESARBOLREFERENCIA,";
		sql += "INDV_CARDINALIDAD,";
		sql += "INDV_NUMERO_COLECTOR,";
		sql += "INDV_CANTIDAD_EJEMPLARES,";
		sql += "INDV_LATITUD,";
		sql += "INDV_LONGITUD,";
		sql += "INDV_DISTANCIA,";
		sql += "INDV_AZIMUTH,";
		sql += "INDV_PRCL_CONSECUTIVO,";
		sql += "INDV_NUMERO_ARBOL,";
		sql += "INDV_ESPECIE,";
		sql += "INDV_SUBPARCELA,";
		sql += "INDV_OBSERVACIONES,";
		sql += "INDV_ETIQUETA_COLECTA,";
		sql += "INDV_FOTO_COLECTA,";
		sql += "INDV_HOMOLOGACION,";
		//sql += "INDV_ARCHIVO_FOTOS,";
		sql += "INDV_TXCT_ID,";
		sql += "INDV_FID,";
		sql += "INDV_PRCL_PLOT,";
		sql += "INDV_DENSIDAD,";
		sql += "INDV_FAMILIA,";
		sql += "INDV_AUTORFAMILIA,";
		sql += "INDV_GENERO,";
		sql += "INDV_AUTORGENERO,";
		sql += "INDV_ESTADOEPITETO,";
		sql += "INDV_EPITETO,";
		sql += "INDV_MORFOESPECIE,";
		sql += "INDV_AUTORESPECIE,";
		sql += "INDV_HABITO,";
		sql += "INDV_INCLUIR,";
		sql += "INDV_ARCH_CONSECUTIVO,";
		sql += "INDV_PRCL_CONSECUTIVO,";
		sql += "TO_CHAR(INDV_ACTUALIZACION, 'yyyy-mm-dd hh24:mi:ss') AS INDV_ACTUALIZACION";
		sql += " FROM RED_INDIVIDUO ";
		sql += " WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO;

		try {
			rset = dbREDD.consultarBD(sql);

			if (rset != null)
			{
				return rset;
			}
			else
			{
				t = "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
			}
		} catch (SQLException e) {
			t += "Excepción de SQL ["+sql+"]: " + e.toString();
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
		} catch (Exception e) {
			t += "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql;
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
		}
		
		dbREDD.desconectarse();
		return rset;
	}
	
	/**
	 * Método que inicializa los valores de los campos para un individuo especificado.
	 * 
	 * @param INDV_CONSECUTIVO
	 * @param solo_enlace
	 * @param session
	 * @param guardar
	 * @param response
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String exportarIndividuoPDF(String INDV_CONSECUTIVO, boolean solo_enlace, HttpSession session, boolean guardar, HttpServletResponse response)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "exportarIndividuoPDF";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		String ruta_archivo = "";
		
		if (!sec.tienePermiso(usuario, "122")) {
			r = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
			dbREDD.desconectarse();
			return r;
		}
		
		if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			r = Auxiliar.traducir(yo+"Codigo_Individuo_No_Especificado", idioma, "Código del individuo no especificado." + " ");
			dbREDD.desconectarse();
			return r;
		}
		
	    //String carpeta = getServletContext().getRealPath("") + File.separator + EXPORT_PDF_DIRECTORY;
		String carpeta = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_pdf'", "");
		if (!Auxiliar.tieneAlgo(carpeta)) {
			carpeta = getServletContext().getRealPath("") + File.separator + EXPORT_PDF_DIRECTORY;
		}

	    
	    String nombre_PDF = "Individuo_"+INDV_CONSECUTIVO+".pdf";
		String ruta_PDF = carpeta + "/" + nombre_PDF;

		if (solo_enlace) {
			r = "<a class=boton href='" + EXPORT_PDF_DIRECTORY + "/" + nombre_PDF + "' target='_blank'>";
			r += Auxiliar.traducir(yo+"Descargar_ficha_del_individuo_en_PDF", idioma, "Descargar ficha del individuo en PDF" + "..");
			r += "</a>"; 
			return r;
		}
		
		String sql = "SELECT ";
		sql += "INDV_CONSECUTIVO,";
		sql += "INDV_ID_IMPORTACION,";
		sql += "INDV_PRCL_CONSECUTIVO,";
		sql += "INDV_SUBPARCELA,";
		sql += "INDV_NUMERO_ARBOL,";
		sql += "INDV_DISTANCIA,";
		sql += "INDV_AZIMUTH,";
		sql += "INDV_ESARBOLREFERENCIA,";
		sql += "INDV_CARDINALIDAD,";
		sql += "INDV_NUMERO_COLECTOR,";
		sql += "INDV_CANTIDAD_EJEMPLARES,";
		sql += "INDV_ESPECIE,";
		sql += "INDV_TXCT_ID,";
		sql += "INDV_OBSERVACIONES,";
		sql += "INDV_ETIQUETA_COLECTA,";
		sql += "INDV_FOTO_COLECTA,";
		sql += "INDV_HOMOLOGACION,";
		//sql += "INDV_ARCHIVO_FOTOS,";
		sql += "INDV_PRCL_PLOT,";
		sql += "INDV_DENSIDAD,";
		sql += "INDV_FAMILIA,";
		sql += "INDV_AUTORFAMILIA,";
		sql += "INDV_GENERO,";
		sql += "INDV_AUTORGENERO,";
		sql += "INDV_ESTADOEPITETO,";
		sql += "INDV_EPITETO,";
		sql += "INDV_MORFOESPECIE,";
		sql += "INDV_AUTORESPECIE,";
		sql += "INDV_HABITO,";
		sql += "INDV_INCLUIR,";
		sql += "INDV_LATITUD,";
		sql += "INDV_LONGITUD";
		sql += " FROM RED_INDIVIDUO ";
		sql += " WHERE INDV_CONSECUTIVO = "+INDV_CONSECUTIVO;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				rset.next();
				
				String db_INDV_CONSECUTIVO = Auxiliar.nz(rset.getString("INDV_CONSECUTIVO"), "NA");
				String db_INDV_ID_IMPORTACION = Auxiliar.nz(rset.getString("INDV_ID_IMPORTACION"), "NA");
				String db_PRCL_CONSECUTIVO = Auxiliar.nz(rset.getString("INDV_PRCL_CONSECUTIVO"), "NA");
				String db_INDV_SUBPARCELA = Auxiliar.nz(rset.getString("INDV_SUBPARCELA"), "NA");
				String db_INDV_NUMERO_ARBOL = Auxiliar.nz(rset.getString("INDV_NUMERO_ARBOL"), "NA");
				String db_INDV_DISTANCIA = Auxiliar.nz(rset.getString("INDV_DISTANCIA"), "NA");
				String db_INDV_AZIMUTH = Auxiliar.nz(rset.getString("INDV_AZIMUTH"), "NA");
				String db_INDV_ESARBOLREFERENCIA = Auxiliar.nz(rset.getString("INDV_ESARBOLREFERENCIA"), "NA");
				String db_INDV_CARDINALIDAD = Auxiliar.nz(rset.getString("INDV_CARDINALIDAD"), "NA");
				String db_INDV_NUMERO_COLECTOR = Auxiliar.nz(rset.getString("INDV_NUMERO_COLECTOR"), "NA");
				String db_INDV_CANTIDAD_EJEMPLARES = Auxiliar.nz(rset.getString("INDV_CANTIDAD_EJEMPLARES"), "NA");
				String db_INDV_ESPECIE = Auxiliar.nz(rset.getString("INDV_ESPECIE"), "NA");
				String db_INDV_TXCT_ID = Auxiliar.nz(rset.getString("INDV_TXCT_ID"), "NA");
				String db_INDV_OBSERVACIONES = Auxiliar.nz(rset.getString("INDV_OBSERVACIONES"), "NA");
				String db_INDV_ETIQUETA_COLECTA = Auxiliar.nz(rset.getString("INDV_ETIQUETA_COLECTA"), "NA");
				String db_INDV_FOTO_COLECTA = Auxiliar.nz(rset.getString("INDV_FOTO_COLECTA"), "NA");
				String db_INDV_HOMOLOGACION = Auxiliar.nz(rset.getString("INDV_HOMOLOGACION"), "NA");
				//String db_INDV_ARCHIVO_FOTOS = Auxiliar.nz(rset.getString("INDV_ARCHIVO_FOTOS"), "NA");
				String db_INDV_PRCL_PLOT = Auxiliar.nz(rset.getString("INDV_PRCL_PLOT"), "NA");
				String db_INDV_DENSIDAD = Auxiliar.nz(rset.getString("INDV_DENSIDAD"), "NA");
				String db_INDV_FAMILIA = Auxiliar.nz(rset.getString("INDV_FAMILIA"), "NA");
				String db_INDV_AUTORFAMILIA = Auxiliar.nz(rset.getString("INDV_AUTORFAMILIA"), "NA");
				String db_INDV_GENERO = Auxiliar.nz(rset.getString("INDV_GENERO"), "NA");
				String db_INDV_AUTORGENERO = Auxiliar.nz(rset.getString("INDV_AUTORGENERO"), "NA");
				String db_INDV_ESTADOEPITETO = Auxiliar.nz(rset.getString("INDV_ESTADOEPITETO"), "NA");
				String db_INDV_EPITETO = Auxiliar.nz(rset.getString("INDV_EPITETO"), "NA");
				String db_INDV_MORFOESPECIE = Auxiliar.nz(rset.getString("INDV_MORFOESPECIE"), "NA");
				String db_INDV_AUTORESPECIE = Auxiliar.nz(rset.getString("INDV_AUTORESPECIE"), "NA");
				String db_INDV_HABITO = Auxiliar.nz(rset.getString("INDV_HABITO"), "NA");
				String db_INDV_INCLUIR = Auxiliar.nz(rset.getString("INDV_INCLUIR"), "NA");
				String db_INDV_LATITUD = Auxiliar.nz(rset.getString("INDV_LATITUD"), "NA");
				String db_INDV_LONGITUD = Auxiliar.nz(rset.getString("INDV_LONGITUD"), "NA");
								
				rset.close();
				
				/*
				String str_fotos = Auxiliar.nzObjStr(db_INDV_ARCHIVO_FOTOS, "").toString();
				String [] a_fotos = str_fotos.split(",");
				*/
				
				com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.LETTER);
				Image img = null;
		        PdfPCell cell;
		        
		        PdfPCell E,D;
				
				document.setMargins(0, 0, 70, 60);
		        document.setMarginMirroring(false);
				
		        PdfWriter writer = null;

		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        if (guardar) {
		        	writer = PdfWriter.getInstance(document, new FileOutputStream(ruta_PDF));
		        }
		        else {
		        	writer = PdfWriter.getInstance(document, baos);
		        }
		        
		        if (writer == null) {
		        	return "";
		        }
		        
		        TableHeader evento_encabezado = new TableHeader();
		        writer.setPageEvent(evento_encabezado);		        
		        TableFooter evento_pie = new TableFooter();
		        writer.setPageEvent(evento_pie);		        
		        
		        document.open();

		        evento_encabezado.setHeader("Parcela " + db_PRCL_CONSECUTIVO + " - Individuo " + INDV_CONSECUTIVO, idioma);
		        
		        PdfPTable tabla_datos = new PdfPTable(2);
		        tabla_datos.setTotalWidth(540);
		        tabla_datos.setLockedWidth(true);
				
		        TableBackground tableBackground = new TableBackground(new int[]{ 0x03, 0x03, 0x03, 0x00 });
				CellBackground cellBackground = new CellBackground(new int[]{ 0x88, 0x02, 0xBB, 0x02 });
				RoundRectangle roundRectangle = new RoundRectangle(new int[]{ 0x55, 0x55, 0x55, 0x00 });
				RoundRectangle whiteRectangle = new RoundRectangle(new int[]{ 0x00, 0x00, 0x00, 0x00 });
				
		        tabla_datos.setTableEvent(tableBackground);
		        tabla_datos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_datos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_datos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir("Ids", idioma, "Ident." + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase("REDD:"+INDV_CONSECUTIVO+"; Imp:"+db_INDV_ID_IMPORTACION));
		        D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_CONSECUTIVO", idioma, "Consecutivo de Parcela" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(db_PRCL_CONSECUTIVO));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("INDV_ESARBOLREFERENCIA", idioma, "Es árbol de referencia" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(Auxiliar.nz(db_INDV_ESARBOLREFERENCIA, "-")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_CARDINALIDAD", idioma, "Número de brinzales de la misma especie" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(Auxiliar.nz(db_INDV_CARDINALIDAD, "-")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_NUMERO_COLECTOR", idioma, "Número del colector" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(Auxiliar.nz(db_INDV_NUMERO_COLECTOR, "-")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_CANTIDAD_EJEMPLARES", idioma, "Cantidad recolectada de ejemplares de la misma especie" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(Auxiliar.nz(db_INDV_CANTIDAD_EJEMPLARES, "-")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_NUMERO_ARBOL", idioma, "Número de Árbol" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(db_INDV_NUMERO_ARBOL));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				
				t = Auxiliar.traducir("INDV_ESPECIE", idioma, "Especie" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(db_INDV_ESPECIE));
		        D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_TXCT_ID", idioma, "Clasificación Taxonómica" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT 'K:' || T.TXCT_KNG || '>D:' || T.TXCT_DVS || '>C:' || T.TXCT_CLS || '>O:' || T.TXCT_ORD || '->F:' || T.TXCT_FML || '->G:' || T.TXCT_GNS || '->S:' || T.TXCT_SPC || '->E:' || T.TXCT_NME_SPC || ' (CF:' || TO_CHAR(T.TXCT_CODIGO_FORESTAL) || ')' AS INFO FROM IDT_TAXONOMY_CATALOGUE T WHERE T.TXCT_ID=" + db_INDV_TXCT_ID, "")));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("INDV_SUBPARCELA", idioma, "Sub-parcela" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(db_INDV_SUBPARCELA));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("INDV_OBSERVACIONES", idioma, "Observaciones" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_OBSERVACIONES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_ETIQUETA_COLECTA", idioma, "Etiqueta de la colecta" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_ETIQUETA_COLECTA));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_FOTO_COLECTA", idioma, "Foto de la colecta" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_FOTO_COLECTA));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_HOMOLOGACION", idioma, "Homologación de la colecta" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(db_INDV_HOMOLOGACION));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("General.LATLON", idioma, "Latitud,Longitud" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase("(" + Auxiliar.nz(db_INDV_LATITUD, "...") + "," + Auxiliar.nz(db_INDV_LONGITUD, "...") + ")"));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("DISTAZI", idioma, "Distancia,Azimuth" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase("(" + Auxiliar.nz(db_INDV_DISTANCIA, "...") + "," + Auxiliar.nz(db_INDV_AZIMUTH, "...") + ")"));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_PRCL_PLOT", idioma, "Plot" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_PRCL_PLOT));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_DENSIDAD", idioma, "Densidad de la Madera" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_DENSIDAD));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_FAMILIA", idioma, "Familia" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_FAMILIA));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_AUTORFAMILIA", idioma, "Autor Familia" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_AUTORFAMILIA));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_GENERO", idioma, "Género" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_GENERO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_AUTORGENERO", idioma, "Autor Género" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_AUTORGENERO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_ESTADOEPITETO", idioma, "Estado Epiteto" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_ESTADOEPITETO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_EPITETO", idioma, "Epiteto" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_EPITETO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_MORFOESPECIE", idioma, "Morfoespecie" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_MORFOESPECIE));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_AUTORESPECIE", idioma, "Autor Especie" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_AUTORESPECIE));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_HABITO", idioma, "Hábito" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(db_INDV_HABITO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("INDV_INCLUIR", idioma, "Incluir en Cálculos" + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(db_INDV_INCLUIR));
		        D.setBorder(0);
				tabla_datos.addCell(D);


				tabla_datos.completeRow();
				document.add(tabla_datos);
				
				document.newPage();

		        //com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph();
		        
		        PdfPTable tabla_titulo_imagenes = new PdfPTable(1);
		        tabla_titulo_imagenes.setTotalWidth(540);
		        tabla_titulo_imagenes.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_imagenes.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_imagenes.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir(yo+"Imagenes_Individuo", idioma, "Imágenes del Individuo" + " ");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_imagenes.addCell(cell);
		        tabla_titulo_imagenes.completeRow();
		        
		        document.add(tabla_titulo_imagenes);
		        
		        PdfPTable tabla_imagenes = new PdfPTable(1);
		        tabla_imagenes.setTotalWidth(540);

				//String sql_imagenes = "SELECT IIMG, INDV_CONSECUTIVO, NOMBRE FROM RED_INDIVIDUO_IMAGEN WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO;
				String sql_imagenes = "SELECT IIMG, INDV_CONSECUTIVO, NOMBRE, PUBLICA FROM RED_INDIVIDUO_IMAGEN " +
								"WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO + 
								" AND (" +
								"INDV_CONSECUTIVO IN (SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_CREADOR="+usuario+") " +
								"OR " +
								"(SELECT COUNT(*) FROM RED_USUARIO_ROL WHERE RLUS_CONS_USUARIO="+usuario+" AND RLUS_CONS_ROL IN (SELECT PRRL_CONS_ROL FROM RED_PERMISO_ROL WHERE PRRL_CONS_PERMISOS IN (1001,1003))) > 0" +
								")";
				
				ResultSet rs_imagenes = dbREDD.consultarBD(sql_imagenes);
				
				if (rs_imagenes != null) {
					String ruta = "";
					String nombre = "";

					while (rs_imagenes.next()) {
						nombre = rs_imagenes.getString("NOMBRE");
						if (!Auxiliar.nz(nombre, "").equals("")) {
							String carpeta_imagenes_individuos = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_individuos'", "");
							if (!Auxiliar.tieneAlgo(carpeta_imagenes_individuos)) {
								carpeta_imagenes_individuos = getServletContext().getRealPath("") + File.separator + "imagenes_individuos";
							}
							ruta_archivo = carpeta_imagenes_individuos + "/" + INDV_CONSECUTIVO + "/" + nombre;
							//ruta_archivo = getServletContext().getRealPath("") + File.separator + "imagenes_individuos/" + INDV_CONSECUTIVO + "/" + nombre;
							if (archie.existeArchivo(ruta_archivo)) {
								img = Image.getInstance(ruta_archivo);
								img.scaleToFit(400, 400);
								cell = new PdfPCell(img);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setBorder(0);
								tabla_imagenes.addCell(cell);
							}
						}
					}
					rs_imagenes.close();
				}
		        
		        /*
				if (a_fotos.length > 0) {
					int i = 0;
					for (i=0; i<a_fotos.length; i++) {
						ruta_archivo = getServletContext().getRealPath("") + File.separator + "imagenes_individuos/" + INDV_CONSECUTIVO + "/" + a_fotos[i];
						if (archie.existeArchivo(ruta_archivo)) {
							img = Image.getInstance(ruta_archivo);
							img.scaleToFit(400, 400);
					        cell = new PdfPCell(img);
					        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					        cell.setBorder(0);
							tabla_imagenes.addCell(cell);
						}
					}
				}
				*/
		        
				tabla_imagenes.completeRow();
				document.add(tabla_imagenes);

				
				// GENERAR Y AÑADIR MAPA
				
				document.newPage();

		        PdfPTable tabla_titulo_mapa = new PdfPTable(1);
		        tabla_titulo_mapa.setTotalWidth(540);
		        tabla_titulo_mapa.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_mapa.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_mapa.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir(yo+"Mapa_Individuo", idioma, "Ubicación del Individuo" + "..");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_mapa.addCell(cell);
		        tabla_titulo_mapa.completeRow();
		        
		        document.add(tabla_titulo_mapa);
		        
		        PdfPTable tabla_mapa= new PdfPTable(1);
		        tabla_mapa.setTotalWidth(540);

		        boolean ok_crear_mapa = generarMapaIndividuo(INDV_CONSECUTIVO, session);
		        
				ruta_archivo = getServletContext().getRealPath("") + File.separator + "mapas_individuos/" + INDV_CONSECUTIVO + "-mapa.jpg";
				
				if (archie.existeArchivo(ruta_archivo)) {
					img = Image.getInstance(ruta_archivo);
					img.scaleToFit(540, 800);
			        cell = new PdfPCell(img);
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setBorder(0);
			        tabla_mapa.addCell(cell);
				}
				
				tabla_mapa.completeRow();
				document.add(tabla_mapa);
				

				document.newPage();

		        PdfPTable tabla_titulo_tallos = new PdfPTable(1);
		        tabla_titulo_tallos.setTotalWidth(540);
		        tabla_titulo_tallos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_tallos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_tallos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir(yo+"Tallos", idioma, "Tallos del Individuo" + "..");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_tallos.addCell(cell);
		        tabla_titulo_tallos.completeRow();
		        
		        document.add(tabla_titulo_tallos);

		        
		        PdfPTable tabla_tallos = new PdfPTable(9);
		        tabla_tallos.setTotalWidth(540);
		        tabla_tallos.setLockedWidth(true);
				
		        tabla_tallos.setTableEvent(tableBackground);
		        tabla_tallos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_tallos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_tallos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				String sql_tallos = "SELECT ";
				sql_tallos += "TAYO_CONSECUTIVO,";
				sql_tallos += "TAYO_DAP1,";
				sql_tallos += "TAYO_DAP2,";
				sql_tallos += "TAYO_ALTURADAP,";
				sql_tallos += "TAYO_ALTURA,";
				sql_tallos += "TAYO_FORMAFUSTE,";
				sql_tallos += "TAYO_DANIO,";
				sql_tallos += "TAYO_OBSERVACIONES,";
				sql_tallos += "TAYO_ALTURATOTAL";
				sql_tallos += " FROM RED_TALLO ";
				sql_tallos += " WHERE TAYO_INDV_CONSECUTIVO=" + INDV_CONSECUTIVO;
				sql_tallos += " ORDER BY TAYO_CONSECUTIVO ";

				try {
					ResultSet rset_tallos = dbREDD.consultarBD(sql_tallos);
					
					if (rset_tallos != null)
					{
						String TAYO_CONSECUTIVO = "";
						String TAYO_DAP1 = "";
						String TAYO_DAP2 = "";
						String TAYO_ALTURADAP = "";
						String TAYO_ALTURA = "";
						String TAYO_FORMAFUSTE = "";
						String TAYO_DANIO = "";
						String TAYO_OBSERVACIONES = "";
						String TAYO_ALTURATOTAL = "";
						

						t = Auxiliar.traducir("TAYO_CONSECUTIVO", idioma, "Consecutivo" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_DAP1", idioma, "DAP 1" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_DAP2", idioma, "DAP 2" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_ALTURADAP", idioma, "Altura DAP" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_ALTURA", idioma, "Altura" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_ALTURATOTAL", idioma, "Altura Total" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_FORMAFUSTE", idioma, "Forma Fuste" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_DANIO", idioma, "Daño" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

				        t = Auxiliar.traducir("TAYO_OBSERVACIONES", idioma, "Observaciones" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_tallos.addCell(E);

						while (rset_tallos.next())
						{
							TAYO_CONSECUTIVO = rset_tallos.getString("TAYO_CONSECUTIVO");
							TAYO_DAP1 = rset_tallos.getString("TAYO_DAP1");
							TAYO_DAP2 = rset_tallos.getString("TAYO_DAP2");
							TAYO_ALTURADAP = rset_tallos.getString("TAYO_ALTURADAP");
							TAYO_ALTURA = rset_tallos.getString("TAYO_ALTURA");
							TAYO_FORMAFUSTE = rset_tallos.getString("TAYO_FORMAFUSTE");
							TAYO_DANIO = rset_tallos.getString("TAYO_DANIO");
							TAYO_OBSERVACIONES = rset_tallos.getString("TAYO_OBSERVACIONES");
							TAYO_ALTURATOTAL = rset_tallos.getString("TAYO_ALTURATOTAL");

					        
							D = new PdfPCell(new Phrase(TAYO_CONSECUTIVO));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(TAYO_DAP1));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(TAYO_DAP2));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(TAYO_ALTURADAP));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(TAYO_ALTURA));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(TAYO_ALTURATOTAL));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT FRFS_DESCRIPCION FROM RED_FORMAFUSTE WHERE FRFS_CONSECUTIVO=" + TAYO_FORMAFUSTE, "NA")));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT DANO_DESCRIPCION FROM RED_DANO WHERE DANO_CONSECUTIVO=" + TAYO_DANIO, "NA")));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
							D = new PdfPCell(new Phrase(TAYO_OBSERVACIONES));
							D.setBorder(0);
							tabla_tallos.addCell(D);
							
						}
						
						rset_tallos.close();
					}
					else
					{
						r += "El conjunto de resultados retornados para la consulta ["+sql_tallos+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
					}
				} catch (SQLException e) {
					r += "Excepción de SQL ["+sql_tallos+"]: " + e.toString();
				} catch (Exception e) {
					r += "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql_tallos;
				}
		        
				tabla_tallos.completeRow();
				document.add(tabla_tallos);
				
		        document.close();
				
		        if (guardar) {
					r = "<div class='opcionmenu'><a class=boton href='" + EXPORT_PDF_DIRECTORY + "/" + nombre_PDF + "' target='_blank'>";
					r += Auxiliar.traducir(yo+"Descargar_ficha_del_individuo_en_PDF", idioma, "Descargar ficha del individuo en PDF" + " ");
					r += "</a></div>"; 
		        }
		        else {
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename=" + nombre_PDF);
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					response.setContentType("application/pdf");
					response.setContentLength(baos.size());
					ServletOutputStream out = response.getOutputStream();
					baos.writeTo(out);
					out.flush();
		        }

			}
			else
			{
				r += "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en exportarIndividuoPDF(): " + e.toString() + " -- SQL: " + sql;
		}
		
		dbREDD.desconectarse();
		return r;
	}

	/**
     * Inner class with a table event that draws a background with rounded corners.
     */
    class TableBackground implements PdfPTableEvent {
        /** the border color described as CMYK values. */
        protected int[] color;
        /** Constructs the event using a certain color. */
        public TableBackground(int[] color) {
            this.color = color;
        }
 
        public void tableLayout(PdfPTable table, float[][] width, float[] height,
                int headerRows, int rowStart, PdfContentByte[] canvas) {
            PdfContentByte background = canvas[PdfPTable.BASECANVAS];
            background.saveState();
            background.setCMYKColorFill(color[0], color[1], color[2], color[3]);
            background.roundRectangle(
                width[0][0], height[height.length - 1] - 2,
                width[0][1] - width[0][0] + 6, height[0] - height[height.length - 1] - 4, 4);
            background.fill();
            background.restoreState();
        }
 
    }
 
    /**
     * Inner class with a cell event that draws a background with rounded corners.
     */
    class CellBackground implements PdfPCellEvent {
        /** the border color described as CMYK values. */
        protected int[] color;
        /** Constructs the event using a certain color. */
        public CellBackground(int[] color) {
            this.color = color;
        }
    	
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.roundRectangle(
                rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                rect.getHeight() - 3, 4);
            cb.setCMYKColorFill(color[0], color[1], color[2], color[3]);
            cb.fill();
        }
    }
 
    /**
     * Inner class with a cell event that draws a border with rounded corners.
     */
    class RoundRectangle implements PdfPCellEvent {
        /** the border color described as CMYK values. */
        protected int[] color;
        /** Constructs the event using a certain color. */
        public RoundRectangle(int[] color) {
            this.color = color;
        }
 
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                rect.getHeight() - 3, 4);
            cb.setLineWidth(0.0f);
            cb.setCMYKColorStrokeF(color[0], color[1], color[2], color[3]);
            cb.stroke();
        }
    }	

    /**
     * Inner class to add a table as header.
     */
    class TableHeader extends PdfPageEventHelper {

    	String yo = "Individuo.";
    	String t = "";
    	
    	//Auxiliar aux = new Auxiliar();

        /** The header text. */
        String header;
        /** The template with the total number of pages. */
        PdfTemplate total;
        String idioma = "";
        

        /**
         * Allows us to change the content of the header.
         * @param header The new header String
         */
        public void setHeader(String header, String idioma) {
        	this.header = header;
            this.idioma = idioma;
        }

        /**
         * Creates the PdfTemplate that will hold the total number of pages.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        /**
         * Adds a header to every page
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
	        PdfPTable tabla_encabezado = new PdfPTable(3);
            //PdfPTable table = new PdfPTable(3);
            try {
        		Archivo archie = new Archivo();

            	String ruta_archivo = "";
            	
		        tabla_encabezado.setTotalWidth(540);
		        tabla_encabezado.setLockedWidth(true);
                tabla_encabezado.getDefaultCell().setFixedHeight(50);
                tabla_encabezado.getDefaultCell().setBorder(Rectangle.BOTTOM);
		        
		        PdfPCell cell;
				Image img = null;
		        
		        ruta_archivo = getServletContext().getRealPath("") + File.separator + "images/Ideam.png";
				if (archie.existeArchivo(ruta_archivo)) {
					img = Image.getInstance(ruta_archivo);
					img.scaleToFit(50, 50);
				}
		        cell = new PdfPCell(img);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
				tabla_encabezado.addCell(cell);

		        com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph();
		        
		        p.clear();
				t = Auxiliar.traducir("General.Detalle_Individuo", idioma, "Detalle Individuo" + " ");
		        p.add(t);
		        p.add("\n");
		        p.add("_______");
		        p.add("\n");
		        p.add("\n");
		        p.add(header);
		        p.setAlignment(Element.ALIGN_JUSTIFIED);
		        cell = new PdfPCell(p);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_encabezado.addCell(cell);
		        
		        ruta_archivo = getServletContext().getRealPath("") + File.separator + "images/MinAmbiente.png";
		        if (archie.existeArchivo(ruta_archivo)) {
					img = Image.getInstance(ruta_archivo);
					img.scaleToFit(170, 170);
				}
		        cell = new PdfPCell(img);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
				tabla_encabezado.addCell(cell);

				tabla_encabezado.writeSelectedRows(0, -1, 34, 780, writer.getDirectContent());
            }
            catch(DocumentException de) {
                throw new ExceptionConverter(de);
            } catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        /**
         * Fills out the total number of pages before the document is closed.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                    2, 2, 0);
        }
    }
	
    /**
     * Inner class to add a table as footer.
     */
    class TableFooter extends PdfPageEventHelper {
    	/** The footer text. */
    	String footer;
    	/** The template with the total number of pages. */
    	PdfTemplate total;
    	
    	/**
    	 * Allows us to change the content of the footer.
    	 * @param footer The new footer String
    	 */
    	public void setFooter(String footer) {
    		this.footer = footer;
    	}
    	
    	/**
    	 * Creates the PdfTemplate that will hold the total number of pages.
    	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
    	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
    	 */
    	public void onOpenDocument(PdfWriter writer, Document document) {
    		total = writer.getDirectContent().createTemplate(30, 16);
    	}
    	
    	/**
    	 * Adds a footer to every page
    	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
    	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
    	 */
    	public void onEndPage(PdfWriter writer, Document document) {
    		PdfPTable tabla = new PdfPTable(5);
    		try {
    			Archivo archie = new Archivo();

    			String ruta_archivo = "";
    			
    			tabla.setTotalWidth(540);
    			tabla.setLockedWidth(true);
    			tabla.getDefaultCell().setFixedHeight(50);
    			tabla.getDefaultCell().setBorder(Rectangle.BOTTOM);
    			
    			PdfPCell cell;
    			Image img = null;
    			
    			ruta_archivo = getServletContext().getRealPath("") + File.separator + "images/patrimonioNatural.jpg";
    			if (archie.existeArchivo(ruta_archivo)) {
    				img = Image.getInstance(ruta_archivo);
    				img.scaleToFit(50, 50);
    			}
    			cell = new PdfPCell(img);
    			cell.setBorder(0);
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    			tabla.addCell(cell);
    			
    			ruta_archivo = getServletContext().getRealPath("") + File.separator + "images/Logo-Prosperidad.jpg";
    			if (archie.existeArchivo(ruta_archivo)) {
    				img = Image.getInstance(ruta_archivo);
    				img.scaleToFit(50, 50);
    			}
    			cell = new PdfPCell(img);
    			cell.setBorder(0);
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    			tabla.addCell(cell);

    			ruta_archivo = getServletContext().getRealPath("") + File.separator + "images/colombia.png";
    			if (archie.existeArchivo(ruta_archivo)) {
    				img = Image.getInstance(ruta_archivo);
    				img.scaleToFit(50, 50);
    			}
    			cell = new PdfPCell(img);
    			cell.setBorder(0);
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    			tabla.addCell(cell);
    			
    			ruta_archivo = getServletContext().getRealPath("") + File.separator + "images/moore.jpg";
    			if (archie.existeArchivo(ruta_archivo)) {
    				img = Image.getInstance(ruta_archivo);
    				img.scaleToFit(50, 50);
    			}
    			cell = new PdfPCell(img);
    			cell.setBorder(0);
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    			tabla.addCell(cell);
    			
    			cell = new PdfPCell(new Phrase(String.format("Página %d", writer.getPageNumber())));
    			cell.setBorder(0);
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    			tabla.addCell(cell);
    			
    			tabla.writeSelectedRows(0, -1, 34, 50, writer.getDirectContent());
    		}
    		catch(DocumentException de) {
    			throw new ExceptionConverter(de);
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	/**
    	 * Fills out the total number of pages before the document is closed.
    	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
    	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
    	 */
    	public void onCloseDocument(PdfWriter writer, Document document) {
    		ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
    				new Phrase(String.valueOf(writer.getPageNumber() - 1)),
    				2, 2, 0);
    	}
    }
    
    /**
	 * Método que crea un archivo de excel para un individuo especificado.
     * 
     * @param PRCL_CONSECUTIVO
     * @param INDV_NUMERO_ARBOL
     * @param INDV_CONSECUTIVO
     * @param INDV_ID_IMPORTACION
     * @param INDV_ESARBOLREFERENCIA
     * @param INDV_CARDINALIDAD
     * @param INDV_NUMERO_COLECTOR
     * @param INDV_CANTIDAD_EJEMPLARES
     * @param f_desde
     * @param f_hasta
     * @param f_especie
     * @param response
     * @param session
     * @return true si pudo, false si no
     * @throws ClassNotFoundException
     * @throws Exception
     */
	public <E> boolean exportarIndividuosExcel(
		String PRCL_CONSECUTIVO, 
		String INDV_NUMERO_ARBOL, 
		String INDV_CONSECUTIVO, 
		String INDV_ID_IMPORTACION, 
		String INDV_ESARBOLREFERENCIA, 
		String INDV_CARDINALIDAD, 
		String INDV_NUMERO_COLECTOR, 
		String INDV_CANTIDAD_EJEMPLARES, 
		String f_desde, 
		String f_hasta, 
		String f_especie,
		HttpServletResponse response,
		HttpSession session
		)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "exportarIndividuosExcel";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();
		Codec ORACLE_CODEC = new OracleCodec();

		boolean ok = false;
		
		String w_PRCL_CONSECUTIVO = "";
		String w_INDV_NUMERO_ARBOL= "";
		String w_INDV_CONSECUTIVO = "";
		String w_INDV_ID_IMPORTACION = "";
		String w_INDV_ESARBOLREFERENCIA = "";
		String w_INDV_CARDINALIDAD = "";
		String w_INDV_NUMERO_COLECTOR = "";
		String w_INDV_CANTIDAD_EJEMPLARES = "";
		String w_desde = "";
		String w_hasta = "";
		String w_especie = "";
		
		PRCL_CONSECUTIVO = Auxiliar.limpiarTexto(PRCL_CONSECUTIVO);
		PRCL_CONSECUTIVO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_CONSECUTIVO, ""));
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			w_PRCL_CONSECUTIVO = " AND INDV_PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
		}
		else {
			w_PRCL_CONSECUTIVO = "";
		}
		
		INDV_NUMERO_ARBOL = Auxiliar.limpiarTexto(INDV_NUMERO_ARBOL);
		INDV_NUMERO_ARBOL = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_NUMERO_ARBOL, ""));
		if (Auxiliar.tieneAlgo(INDV_NUMERO_ARBOL)) {
			w_INDV_NUMERO_ARBOL = " INDV_NUMERO_ARBOL IN ("+INDV_NUMERO_ARBOL+") ";
		}
		else {
			w_INDV_NUMERO_ARBOL = " 1=1 ";
		}
		
		INDV_CONSECUTIVO = Auxiliar.limpiarTexto(INDV_CONSECUTIVO);
		INDV_CONSECUTIVO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_CONSECUTIVO, ""));
		if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			w_INDV_CONSECUTIVO = " AND INDV_CONSECUTIVO IN ("+INDV_CONSECUTIVO+") ";
		}
		else {
			w_INDV_CONSECUTIVO = " AND 1=1 ";
		}
		
		INDV_ID_IMPORTACION = Auxiliar.limpiarTexto(INDV_ID_IMPORTACION);
		INDV_ID_IMPORTACION = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ID_IMPORTACION, ""));
		if (Auxiliar.tieneAlgo(INDV_ID_IMPORTACION)) {
			w_INDV_ID_IMPORTACION = " INDV_ID_IMPORTACION IN ("+INDV_ID_IMPORTACION+") ";
		}
		else {
			w_INDV_ID_IMPORTACION = " 1=1 ";
		}
		
		INDV_ESARBOLREFERENCIA = Auxiliar.limpiarTexto(INDV_ESARBOLREFERENCIA);
		INDV_ESARBOLREFERENCIA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ESARBOLREFERENCIA, ""));
		if (Auxiliar.tieneAlgo(INDV_ESARBOLREFERENCIA)) {
			w_INDV_ESARBOLREFERENCIA = " INDV_ESARBOLREFERENCIA IN ("+INDV_ESARBOLREFERENCIA+") ";
		}
		else {
			w_INDV_ESARBOLREFERENCIA = " 1=1 ";
		}
				
		INDV_CARDINALIDAD = Auxiliar.limpiarTexto(INDV_CARDINALIDAD);
		INDV_CARDINALIDAD = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_CARDINALIDAD, ""));
		if (Auxiliar.tieneAlgo(INDV_CARDINALIDAD)) {
			w_INDV_CARDINALIDAD = " INDV_CARDINALIDAD IN ("+INDV_CARDINALIDAD+") ";
		}
		else {
			w_INDV_CARDINALIDAD = " 1=1 ";
		}
		
		INDV_NUMERO_COLECTOR = Auxiliar.limpiarTexto(INDV_NUMERO_COLECTOR);
		INDV_NUMERO_COLECTOR = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_NUMERO_COLECTOR, ""));
		if (Auxiliar.tieneAlgo(INDV_NUMERO_COLECTOR)) {
			w_INDV_NUMERO_COLECTOR = " INDV_NUMERO_COLECTOR IN ("+INDV_NUMERO_COLECTOR+") ";
		}
		else {
			w_INDV_NUMERO_COLECTOR = " 1=1 ";
		}
		
		INDV_CANTIDAD_EJEMPLARES = Auxiliar.limpiarTexto(INDV_CANTIDAD_EJEMPLARES);
		INDV_CANTIDAD_EJEMPLARES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_CANTIDAD_EJEMPLARES, ""));
		if (Auxiliar.tieneAlgo(INDV_CANTIDAD_EJEMPLARES)) {
			w_INDV_CANTIDAD_EJEMPLARES = " INDV_CANTIDAD_EJEMPLARES IN ("+INDV_CANTIDAD_EJEMPLARES+") ";
		}
		else {
			w_INDV_CANTIDAD_EJEMPLARES = " 1=1 ";
		}
		
		f_especie = Auxiliar.limpiarTexto(f_especie);
		f_especie = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_especie, ""));
		if (Auxiliar.tieneAlgo(f_especie)) {
			w_especie = " AND LOWER(INDV_ESPECIE) LIKE '%"+f_especie.toLowerCase()+"%' ";
		}

		
		int i = 0;
		
		String sql = "select * from ( select /*+ FIRST_ROWS(n) */ a.*, ROWNUM rnum from (SELECT ";
		sql += "INDV_CONSECUTIVO,";
		sql += "INDV_ID_IMPORTACION,";
		sql += "INDV_PRCL_CONSECUTIVO,";
		sql += "INDV_SUBPARCELA,";
		sql += "INDV_NUMERO_ARBOL,";
		sql += "INDV_DISTANCIA,";
		sql += "INDV_AZIMUTH,";
		sql += "INDV_ESARBOLREFERENCIA,";
		sql += "INDV_CARDINALIDAD,";
		sql += "INDV_NUMERO_COLECTOR,";
		sql += "INDV_CANTIDAD_EJEMPLARES,";
		sql += "INDV_ESPECIE,";
		sql += "INDV_TXCT_ID,";
		sql += "INDV_OBSERVACIONES,";
		sql += "INDV_ETIQUETA_COLECTA,";
		sql += "INDV_FOTO_COLECTA,";
		sql += "INDV_HOMOLOGACION,";
		//sql += "INDV_ARCHIVO_FOTOS,";
		sql += "INDV_FID,";
		sql += "INDV_PRCL_PLOT,";
		sql += "INDV_DENSIDAD,";
		sql += "INDV_FAMILIA,";
		sql += "INDV_AUTORFAMILIA,";
		sql += "INDV_GENERO,";
		sql += "INDV_AUTORGENERO,";
		sql += "INDV_ESTADOEPITETO,";
		sql += "INDV_EPITETO,";
		sql += "INDV_MORFOESPECIE,";
		sql += "INDV_AUTORESPECIE,";
		sql += "INDV_HABITO,";
		sql += "INDV_INCLUIR,";
		sql += "TO_CHAR(INDV_ACTUALIZACION, 'YYYY-MM-DD HH24:MI:SS') AS INDV_ACTUALIZACION,";
		sql += "INDV_LATITUD,";
		sql += "INDV_LONGITUD";
		sql += " FROM RED_INDIVIDUO ";
		sql += " WHERE 1=1 ";
		sql += w_PRCL_CONSECUTIVO;
		sql += " AND (1=2 " + " OR " + w_INDV_NUMERO_ARBOL + " OR " + w_INDV_ID_IMPORTACION + ")";
		sql += " AND (1=2 " + " OR " + w_INDV_ESARBOLREFERENCIA + " OR " + w_INDV_CARDINALIDAD + " OR " + w_INDV_NUMERO_COLECTOR + " OR " + w_INDV_CANTIDAD_EJEMPLARES + ")";
		sql += w_INDV_CONSECUTIVO;
		sql += w_desde;
		sql += w_hasta;
		sql += w_especie;
		sql += " ORDER BY INDV_PRCL_CONSECUTIVO,INDV_NUMERO_ARBOL ) a where ROWNUM <= 1000 ) where rnum >= 0 ";

		int j=0;
		
		try {
			HSSFWorkbook libro = new HSSFWorkbook();
			Sheet hoja = libro.createSheet("INDIVIDUO");
			
			Row fila_titulos = hoja.createRow(0);

			ResultSet rset = dbREDD.consultarBD(sql);
			ResultSet rset_tallo = null;

			j=-1;
			j++;fila_titulos.createCell(j).setCellValue("ACCION");
			j++;fila_titulos.createCell(j).setCellValue("CONSECUTIVO");
			j++;fila_titulos.createCell(j).setCellValue("ID_IMPORTACION");
			j++;fila_titulos.createCell(j).setCellValue("PARCELA");
			j++;fila_titulos.createCell(j).setCellValue("SUBPARCELA");
			j++;fila_titulos.createCell(j).setCellValue("NUMERO_ARBOL");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA");
			j++;fila_titulos.createCell(j).setCellValue("AZIMUTH");
			j++;fila_titulos.createCell(j).setCellValue("ESARBOLREFERENCIA");
			j++;fila_titulos.createCell(j).setCellValue("CARDINALIDAD");
			j++;fila_titulos.createCell(j).setCellValue("NUMERO_COLECTOR");
			j++;fila_titulos.createCell(j).setCellValue("CANTIDAD_EJEMPLARES");
			j++;fila_titulos.createCell(j).setCellValue("ESPECIE");
			j++;fila_titulos.createCell(j).setCellValue("TAXONOMIA");
			j++;fila_titulos.createCell(j).setCellValue("OBSERVACIONES");
			j++;fila_titulos.createCell(j).setCellValue("ETIQUETA_COLECTA");
			j++;fila_titulos.createCell(j).setCellValue("FOTO_COLECTA");
			j++;fila_titulos.createCell(j).setCellValue("HOMOLOGACION");
			//j++;fila_titulos.createCell(j).setCellValue("ARCHIVO_FOTOS");
			j++;fila_titulos.createCell(j).setCellValue("FID");
			j++;fila_titulos.createCell(j).setCellValue("PLOT");
			j++;fila_titulos.createCell(j).setCellValue("DENSIDAD");
			j++;fila_titulos.createCell(j).setCellValue("FAMILIA");
			j++;fila_titulos.createCell(j).setCellValue("AUTORFAMILIA");
			j++;fila_titulos.createCell(j).setCellValue("GENERO");
			j++;fila_titulos.createCell(j).setCellValue("AUTORGENERO");
			j++;fila_titulos.createCell(j).setCellValue("ESTADOEPITETO");
			j++;fila_titulos.createCell(j).setCellValue("EPITETO");
			j++;fila_titulos.createCell(j).setCellValue("MORFOESPECIE");
			j++;fila_titulos.createCell(j).setCellValue("AUTORESPECIE");
			j++;fila_titulos.createCell(j).setCellValue("HABITO");
			j++;fila_titulos.createCell(j).setCellValue("INCLUIR");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_DAP1");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_DAP2");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_ALTURADAP");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_ALTURA");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_ALTURATOTAL");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_FORMAFUSTE");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_DANIO");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_OBSERVACIONES");
			j++;fila_titulos.createCell(j).setCellValue("ACTUALIZACION");
			j++;fila_titulos.createCell(j).setCellValue("LATITUD");
			j++;fila_titulos.createCell(j).setCellValue("LONGITUD");
			
			String db_INDV_CONSECUTIVO = "";
			String sql_tallo = "";
			int n_tallos = 0;
			
			String TALLOUNICO_DAP1 = "";
			String TALLOUNICO_DAP2 = "";
			String TALLOUNICO_ALTURADAP = "";
			String TALLOUNICO_ALTURA = "";
			String TALLOUNICO_ALTURATOTAL = "";
			String TALLOUNICO_FORMAFUSTE = "";
			String TALLOUNICO_DANIO = "";
			String TALLOUNICO_OBSERVACIONES = "";
			
			List<HashMap<String,Object>> listallo = null;
			
			while (rset.next()) {
				
				db_INDV_CONSECUTIVO = Auxiliar.nz(rset.getString("INDV_CONSECUTIVO"),"");
				n_tallos = 0;
				
				if (Auxiliar.tieneAlgo(db_INDV_CONSECUTIVO)) {
					// AVERIGUAR SI SOLO TIENE UN TALLO
					sql_tallo = "SELECT TAYO_DAP1,TAYO_DAP2,TAYO_ALTURADAP,TAYO_ALTURA,TAYO_ALTURATOTAL,TAYO_FORMAFUSTE,TAYO_DANIO,TAYO_OBSERVACIONES FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO = " + db_INDV_CONSECUTIVO;

					/*
					rset_tallo = dbREDD.consultarBD(sql_tallo);
					while (rset_tallo.next()) {
						n_tallos++;
						
						TALLOUNICO_DAP1 = Auxiliar.nz(rset_tallo.getString("TAYO_DAP1"),"");
						TALLOUNICO_DAP2 = Auxiliar.nz(rset_tallo.getString("TAYO_DAP2"),"");
						TALLOUNICO_ALTURADAP = Auxiliar.nz(rset_tallo.getString("TAYO_ALTURADAP"),"");
						TALLOUNICO_ALTURA = Auxiliar.nz(rset_tallo.getString("TAYO_ALTURA"),"");
						TALLOUNICO_ALTURATOTAL = Auxiliar.nz(rset_tallo.getString("TAYO_ALTURATOTAL"),"");
						TALLOUNICO_FORMAFUSTE = Auxiliar.nz(rset_tallo.getString("TAYO_FORMAFUSTE"),"");
						TALLOUNICO_DANIO = Auxiliar.nz(rset_tallo.getString("TAYO_DANIO"),"");
						TALLOUNICO_OBSERVACIONES = Auxiliar.nz(rset_tallo.getString("TAYO_OBSERVACIONES"),"");
					}
					rset_tallo.close();
					*/

					TALLOUNICO_DAP1 = "";
					TALLOUNICO_DAP2 = "";
					TALLOUNICO_ALTURADAP = "";
					TALLOUNICO_ALTURA = "";
					TALLOUNICO_ALTURATOTAL = "";
					TALLOUNICO_FORMAFUSTE = "";
					TALLOUNICO_DANIO = "";
					TALLOUNICO_OBSERVACIONES = "";

					listallo = dbREDD.ListaBD(sql_tallo);
					
					n_tallos = listallo.size();
					
					if (n_tallos == 1) {
						for (HashMap<String,Object> map : listallo) {

						    for (Entry<String, Object> entry : map.entrySet()) {
						        String key = entry.getKey();
						        Object value = entry.getValue();

						        if (Auxiliar.nz(key, "").equals("TAYO_DAP1")) { TALLOUNICO_DAP1 = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_DAP2")) { TALLOUNICO_DAP2 = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_ALTURADAP")) { TALLOUNICO_ALTURADAP = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_ALTURA")) { TALLOUNICO_ALTURA = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_ALTURATOTAL")) { TALLOUNICO_ALTURATOTAL = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_FORMAFUSTE")) { TALLOUNICO_FORMAFUSTE = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_DANIO")) { TALLOUNICO_DANIO = Auxiliar.nzObjStr(value, ""); }
						        if (Auxiliar.nz(key, "").equals("TAYO_OBSERVACIONES")) { TALLOUNICO_OBSERVACIONES = Auxiliar.nzObjStr(value, ""); }
						        
						    }
						}
						
					}
				}
				
				i++;
				Row fila_datos = hoja.createRow(i);

				j=-1;
				j++;fila_datos.createCell(j).setCellValue("");
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_CONSECUTIVO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ID_IMPORTACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_PRCL_CONSECUTIVO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_SUBPARCELA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_NUMERO_ARBOL"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_DISTANCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_AZIMUTH"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ESARBOLREFERENCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_CARDINALIDAD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_NUMERO_COLECTOR"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_CANTIDAD_EJEMPLARES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ESPECIE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_TXCT_ID"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_OBSERVACIONES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ETIQUETA_COLECTA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_FOTO_COLECTA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_HOMOLOGACION"),""));
				//j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ARCHIVO_FOTOS"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_FID"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_PRCL_PLOT"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_DENSIDAD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_FAMILIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_AUTORFAMILIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_GENERO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_AUTORGENERO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ESTADOEPITETO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_EPITETO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_MORFOESPECIE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_AUTORESPECIE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_HABITO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_INCLUIR"),""));
				if (n_tallos == 1) {
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_DAP1);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_DAP2);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_ALTURADAP);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_ALTURA);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_ALTURATOTAL);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_FORMAFUSTE);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_DANIO);
					j++;fila_datos.createCell(j).setCellValue(TALLOUNICO_OBSERVACIONES);
				}
				else {
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
					j++;fila_datos.createCell(j).setCellValue("");
				}
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_ACTUALIZACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_LATITUD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("INDV_LONGITUD"),""));
			}
			
			rset.close();
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=individuo.xls");

			libro.write(response.getOutputStream());
			
			ok = true;
			
		} catch (SQLException e) {
			t = "Excepción de SQL ["+sql+"]: " + e.toString();
			Auxiliar.mensaje("error", t, usuario, metodo);
		} catch (Exception e) {
			t = "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql;
			Auxiliar.mensaje("error", t, usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return ok;
	}
	
	
	/**
	 * Genera una plantilla en excel para poder importar masibamente individuos
	 * 
	 * @param response
	 * @return true si pudo, false si no
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean generarPlantillaIndividuos(HttpServletResponse response, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarPlantillaIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		boolean ok = false;
		
		int i = 0;
		int j=0;
		
		try {
			HSSFWorkbook libro = new HSSFWorkbook();
			Sheet hoja = libro.createSheet("INDIVIDUO");
			
			Row fila_titulos = hoja.createRow(0);

			j=-1;
			j++;fila_titulos.createCell(j).setCellValue("ACCION");
			j++;fila_titulos.createCell(j).setCellValue("CONSECUTIVO");
			j++;fila_titulos.createCell(j).setCellValue("ID_IMPORTACION");
			j++;fila_titulos.createCell(j).setCellValue("PARCELA");
			j++;fila_titulos.createCell(j).setCellValue("SUBPARCELA");
			j++;fila_titulos.createCell(j).setCellValue("NUMERO_ARBOL");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA");
			j++;fila_titulos.createCell(j).setCellValue("AZIMUTH");
			j++;fila_titulos.createCell(j).setCellValue("ESARBOLREFERENCIA");
			j++;fila_titulos.createCell(j).setCellValue("CARDINALIDAD");
			j++;fila_titulos.createCell(j).setCellValue("NUMERO_COLECTOR");
			j++;fila_titulos.createCell(j).setCellValue("CANTIDAD_EJEMPLARES");
			j++;fila_titulos.createCell(j).setCellValue("ESPECIE");
			j++;fila_titulos.createCell(j).setCellValue("TAXONOMIA");
			j++;fila_titulos.createCell(j).setCellValue("OBSERVACIONES");
			j++;fila_titulos.createCell(j).setCellValue("ETIQUETA_COLECTA");
			j++;fila_titulos.createCell(j).setCellValue("FOTO_COLECTA");
			j++;fila_titulos.createCell(j).setCellValue("HOMOLOGACION");
			//j++;fila_titulos.createCell(j).setCellValue("ARCHIVO_FOTOS");
			j++;fila_titulos.createCell(j).setCellValue("FID");
			j++;fila_titulos.createCell(j).setCellValue("PLOT");
			j++;fila_titulos.createCell(j).setCellValue("DENSIDAD");
			j++;fila_titulos.createCell(j).setCellValue("FAMILIA");
			j++;fila_titulos.createCell(j).setCellValue("AUTORFAMILIA");
			j++;fila_titulos.createCell(j).setCellValue("GENERO");
			j++;fila_titulos.createCell(j).setCellValue("AUTORGENERO");
			j++;fila_titulos.createCell(j).setCellValue("ESTADOEPITETO");
			j++;fila_titulos.createCell(j).setCellValue("EPITETO");
			j++;fila_titulos.createCell(j).setCellValue("MORFOESPECIE");
			j++;fila_titulos.createCell(j).setCellValue("AUTORESPECIE");
			j++;fila_titulos.createCell(j).setCellValue("HABITO");
			j++;fila_titulos.createCell(j).setCellValue("INCLUIR");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_DAP1");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_DAP2");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_ALTURADAP");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_ALTURA");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_ALTURATOTAL");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_FORMAFUSTE");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_DANIO");
			j++;fila_titulos.createCell(j).setCellValue("TALLOUNICO_OBSERVACIONES");
			j++;fila_titulos.createCell(j).setCellValue("ACTUALIZACION");
			
			i++;
			
			Row fila_datos = hoja.createRow(i);

			String o = "";

			t = Auxiliar.traducir("CargueMasivo.Mensaje_Opciones", idioma, "Opciones:" + "..");
			String mensaje_o = t;

			j=-1;

			t = Auxiliar.traducir("CargueMasivo.Individuos.ACCION", idioma, "Poner ELIMINAR para eliminar el individuo existente. De lo contrario dejar vacío." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.CONSECUTIVO", idioma, "Entero positivo. Consecutivo del individuo" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.ID_IMPORTACION", idioma, "Entero positivo. Consecutivo de importación del individuo" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.PARCELA", idioma, "Entero positivo. Obligatorio. Consecutivo de la parcela que contiene al individuo" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.SUBPARCELA", idioma, "Entero obligatorio entre 1 y 5.  Número de la subparcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.NUMERO_ARBOL", idioma, "Texto. Max 255 caracteres. Obligatorio. Identificador del individuo en la parcela.  Corresponde al número de placa." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.DISTANCIA", idioma, "Número Decimal obligatorio. Distancia en metros de la ubicación del individuo respecto al centro de la subparcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.AZIMUTH", idioma, "Número Decimal obligatorio. Azimuth en grados de la ubicación del individuo respecto al centro de la subparcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.ESARBOLREFERENCIA", idioma, "Entero positivo. Obligatorio. 1: Sí, 0: No." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.CARDINALIDAD", idioma, "Entero positivo. Número de individuos de la misma especie (sólo para brinzales)." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.NUMERO_COLECTOR", idioma, "Entero positivo. Número del colector." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.CANTIDAD_EJEMPLARES", idioma, "Entero positivo. Cantidad de ejemplares recolectados." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.ESPECIE", idioma, "Texto. Max 255 caracteres. Opcional. Nombre de la especie del individuo." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TAXONOMIA", idioma, "Texto. Max 255 caracteres. Obligatorio. Código taxonómico de la especie. Consúltelo en el módulo de catálogo de especies del sistema." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.OBSERVACIONES", idioma, "Texto. Max 4000 caracteres. Observaciones." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.ETIQUETA_COLECTA", idioma, "Texto. Max 255 caracteres. Etiqueta de la colecta." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.FOTO_COLECTA", idioma, "Texto. Max 255 caracteres. Foto de la colecta." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.HOMOLOGACION", idioma, "Texto. Max 255 caracteres. Homologación de la colecta." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			/*
			t = Auxiliar.traducir("CargueMasivo.Individuos.ARCHIVO_FOTOS", idioma, "Texto. Max 4000 caracteres. Concatenación de los nombres de archivo de imágen (sin incluir la ruta) del individuo." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			*/
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.FID", idioma, "Entero positivo. Código del individuo FID. Permite identificar el individuo para actualización o eliminación." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.PRCL_PLOT", idioma, "Entero positivo. Código de la parcela (o plot)." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.DENSIDAD", idioma, "Número decimal opcional. Densidad de la madera." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.FAMILIA", idioma, "Texto. Max 255 caracteres. Familia de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.AUTORFAMILIA", idioma, "Texto. Max 255 caracteres. Autor de la familia de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.GENERO", idioma, "Texto. Max 255 caracteres. Género de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.AUTORGENERO", idioma, "Texto. Max 255 caracteres. Autor del género de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.ESTADOEPITETO", idioma, "Texto. Max 255 caracteres. Estado del epiteto de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.EPITETO", idioma, "Texto. Max 255 caracteres. Epiteto de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.MORFOESPECIE", idioma, "Texto. Max 255 caracteres. Morfología de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.AUTORESPECIE", idioma, "Texto. Max 255 caracteres. Autor de la morfología de la especie." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.HABITO", idioma, "Entero positivo. Código del tipo de cobertura para SPF5." + "..");
			o = Auxiliar.cargarOpciones("SELECT HABI_ID, HABI_NOMBRE FROM RED_HABITO ORDER BY HABI_NOMBRE", "HABI_ID", "HABI_NOMBRE", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);

			t = Auxiliar.traducir("CargueMasivo.Individuos.INCLUIR", idioma, "Entero positivo. Obligatorio. 1: Incluir individuo en cálculos, 0: No incluir individuo en cálculos." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_DAP1", idioma, "Número decimal opcional. DAP1 del único tallo del individuo." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_DAP2", idioma, "Número decimal opcional. DAP2 del único tallo del individuo." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_ALTURADAP", idioma, "Número decimal opcional. Altura exacta de medición de DAP en cm sobre el tallo único." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_ALTURA", idioma, "Número decimal opcional. Altura del tallo único." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_ALTURATOTAL", idioma, "Número decimal opcional. Altura total del tallo único, incluyendo su copa.." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_FORMAFUSTE", idioma, "Entero positivo. Obligatorio. Identificador de la forma del fuste del tallo único." + "..");
			o = Auxiliar.cargarOpciones("SELECT FRFS_CONSECUTIVO, FRFS_DESCRIPCION FROM RED_FORMAFUSTE ORDER BY FRFS_DESCRIPCION", "FRFS_CONSECUTIVO", "FRFS_DESCRIPCION", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_DANIO", idioma, "Entero positivo. Obligatorio. Identificador del daño del fuste del tallo único." + "..");
			o = Auxiliar.cargarOpciones("SELECT DANO_CONSECUTIVO, DANO_DESCRIPCION FROM RED_DANO ORDER BY DANO_DESCRIPCION", "DANO_CONSECUTIVO", "DANO_DESCRIPCION", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Individuos.TALLOUNICO_OBSERVACIONES", idioma, "Texto. Max 255 caracteres. Observaciones sobre el tallo único." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.ACTUALIZACION", idioma, "Fecha y hora en formato yyyy-mm-dd hh:m:ss. Obligatorio. Fecha y hora de la última actualización de la información. Ejemplo: 2004-03-14 23:18:56.  Cambiar formato de esta columna a texto." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=Plantilla_Individuos_Diligenciar_y_Guardar_como_Archivo_de_Texto_Separado_por_Tabulaciones.xls");

			libro.write(response.getOutputStream());
			
			ok = true;
			
		} catch (Exception e) {
			t = "Ocurrió la siguiente excepción en generarPlantillaIndividuos(): " + e.toString();
		}
		
		dbREDD.desconectarse();
		return ok;
	}

	/**
	 * Metodo que retorna una tabla de individuos encontrados
	 * 
	 * @param PRCL_CONSECUTIVO
	 * @param INDV_NUMERO_ARBOL
	 * @param INDV_CONSECUTIVO
	 * @param INDV_ID_IMPORTACION
	 * @param INDV_ESARBOLREFERENCIA
	 * @param INDV_CARDINALIDAD
	 * @param INDV_NUMERO_COLECTOR
	 * @param INDV_CANTIDAD_EJEMPLARES
	 * @param f_desde
	 * @param f_hasta
	 * @param f_especie
	 * @param session
	 * @return String con los resultados de la busqueda en html
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String encontrarIndividuos(
			String PRCL_CONSECUTIVO, 
			String INDV_NUMERO_ARBOL, 
			String INDV_CONSECUTIVO, 
			String INDV_ID_IMPORTACION, 
			String INDV_ESARBOLREFERENCIA, 
			String INDV_CARDINALIDAD, 
			String INDV_NUMERO_COLECTOR, 
			String INDV_CANTIDAD_EJEMPLARES, 
			String f_desde, 
			String f_hasta, 
			String f_especie,
			HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "encontrarIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();
		Codec ORACLE_CODEC = new OracleCodec();

		String r = "";
		
		long n = 0;
		
		String w_PRCL_CONSECUTIVO = "";
		String w_INDV_NUMERO_ARBOL= "";
		String w_INDV_CONSECUTIVO = "";
		String w_INDV_ID_IMPORTACION = "";
		String w_INDV_ESARBOLREFERENCIA = "";
		String w_INDV_CARDINALIDAD = "";
		String w_INDV_NUMERO_COLECTOR = "";
		String w_INDV_CANTIDAD_EJEMPLARES = "";
		String w_desde = "";
		String w_hasta = "";
		String w_especie = "";

		PRCL_CONSECUTIVO = Auxiliar.limpiarTexto(PRCL_CONSECUTIVO);
		PRCL_CONSECUTIVO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_CONSECUTIVO, ""));
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			w_PRCL_CONSECUTIVO = " AND INDV_PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
		}
		else {
			w_PRCL_CONSECUTIVO = "";
		}
		
		INDV_CONSECUTIVO = Auxiliar.limpiarTexto(INDV_CONSECUTIVO);
		INDV_CONSECUTIVO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_CONSECUTIVO, ""));
		if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			w_INDV_CONSECUTIVO = " AND INDV_CONSECUTIVO IN ("+INDV_CONSECUTIVO+") ";
		}
		else {
			w_INDV_CONSECUTIVO = " ";
		}
		
		INDV_NUMERO_ARBOL = Auxiliar.limpiarTexto(INDV_NUMERO_ARBOL);
		INDV_NUMERO_ARBOL = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_NUMERO_ARBOL, ""));
		if (Auxiliar.tieneAlgo(INDV_NUMERO_ARBOL)) {
			w_INDV_NUMERO_ARBOL = " AND INDV_NUMERO_ARBOL IN ("+INDV_NUMERO_ARBOL+") ";
		}
		else {
			w_INDV_NUMERO_ARBOL = " ";
		}
		
		INDV_ID_IMPORTACION = Auxiliar.limpiarTexto(INDV_ID_IMPORTACION);
		INDV_ID_IMPORTACION = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ID_IMPORTACION, ""));
		if (Auxiliar.tieneAlgo(INDV_ID_IMPORTACION)) {
			w_INDV_ID_IMPORTACION = " AND INDV_ID_IMPORTACION IN ("+INDV_ID_IMPORTACION+") ";
		}
		else {
			w_INDV_ID_IMPORTACION = " ";
		}
		
		INDV_ESARBOLREFERENCIA = Auxiliar.limpiarTexto(INDV_ESARBOLREFERENCIA);
		INDV_ESARBOLREFERENCIA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ESARBOLREFERENCIA, ""));
		if (Auxiliar.tieneAlgo(INDV_ESARBOLREFERENCIA)) {
			w_INDV_ESARBOLREFERENCIA = " AND INDV_ESARBOLREFERENCIA IN ("+INDV_ESARBOLREFERENCIA+") ";
		}
		else {
			w_INDV_ESARBOLREFERENCIA = " ";
		}
				
		INDV_CARDINALIDAD = Auxiliar.limpiarTexto(INDV_CARDINALIDAD);
		INDV_CARDINALIDAD = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_CARDINALIDAD, ""));
		if (Auxiliar.tieneAlgo(INDV_CARDINALIDAD)) {
			w_INDV_CARDINALIDAD = " AND INDV_CARDINALIDAD IN ("+INDV_CARDINALIDAD+") ";
		}
		else {
			w_INDV_CARDINALIDAD = " ";
		}
		
		INDV_NUMERO_COLECTOR = Auxiliar.limpiarTexto(INDV_NUMERO_COLECTOR);
		INDV_NUMERO_COLECTOR = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_NUMERO_COLECTOR, ""));
		if (Auxiliar.tieneAlgo(INDV_NUMERO_COLECTOR)) {
			w_INDV_NUMERO_COLECTOR = " AND INDV_NUMERO_COLECTOR IN ("+INDV_NUMERO_COLECTOR+") ";
		}
		else {
			w_INDV_NUMERO_COLECTOR = " ";
		}
		
		INDV_CANTIDAD_EJEMPLARES = Auxiliar.limpiarTexto(INDV_CANTIDAD_EJEMPLARES);
		INDV_CANTIDAD_EJEMPLARES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_CANTIDAD_EJEMPLARES, ""));
		if (Auxiliar.tieneAlgo(INDV_CANTIDAD_EJEMPLARES)) {
			w_INDV_CANTIDAD_EJEMPLARES = " AND INDV_CANTIDAD_EJEMPLARES IN ("+INDV_CANTIDAD_EJEMPLARES+") ";
		}
		else {
			w_INDV_CANTIDAD_EJEMPLARES = " ";
		}
		
		f_especie = Auxiliar.limpiarTexto(f_especie);
		f_especie = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_especie, ""));
		if (Auxiliar.tieneAlgo(f_especie)) {
			w_especie = " AND LOWER(INDV_ESPECIE) LIKE '%"+f_especie.toLowerCase()+"%' ";
		}
		
		f_desde = Auxiliar.limpiarTexto(f_desde);
		f_desde = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_desde, ""));
		if (Auxiliar.tieneAlgo(f_desde)) {
			w_desde = " AND INDV_ACTUALIZACION >= TO_DATE('"+f_desde+"', 'YYYY-MM-DD HH24:MI:SS') ";
		}

		f_hasta = Auxiliar.limpiarTexto(f_hasta);
		f_hasta = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_hasta, ""));
		if (Auxiliar.tieneAlgo(f_hasta)) {
			w_hasta = " AND INDV_ACTUALIZACION <= TO_DATE('"+f_hasta+"', 'YYYY-MM-DD HH24:MI:SS') ";
		}
		
		
		String sql = "select * from ( select /*+ FIRST_ROWS(n) */ a.*, ROWNUM rnum from (SELECT ";
		sql += "INDV_CONSECUTIVO,";
		sql += "INDV_PRCL_CONSECUTIVO,";
		sql += "INDV_NUMERO_ARBOL,";
		sql += "INDV_ID_IMPORTACION,";
		sql += "INDV_ESARBOLREFERENCIA,";
		sql += "INDV_CARDINALIDAD,";
		sql += "INDV_NUMERO_COLECTOR,";
		sql += "INDV_CANTIDAD_EJEMPLARES,";
		sql += "INDV_ESPECIE";
		sql += " FROM RED_INDIVIDUO ";
		sql += " WHERE 1=1 ";
		sql += w_PRCL_CONSECUTIVO;
		sql += w_INDV_CONSECUTIVO;
		sql += w_INDV_ID_IMPORTACION;
		sql += w_INDV_NUMERO_ARBOL;
		sql += w_INDV_ESARBOLREFERENCIA; 
		sql += w_INDV_CARDINALIDAD;
		sql += w_INDV_NUMERO_COLECTOR;
		sql += w_INDV_CANTIDAD_EJEMPLARES;
		sql += w_desde;
		sql += w_hasta;
		sql += w_especie;
		sql += " ORDER BY INDV_PRCL_CONSECUTIVO,INDV_NUMERO_ARBOL ) a where ROWNUM <= 1000 ) where rnum >= 0 ";

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				n = 0; 
				
				String tabla = "";
				
				String db_INDV_CONSECUTIVO = "";
				String db_INDV_PRCL_CONSECUTIVO = "";
				String db_INDV_NUMERO_ARBOL = "";
				String db_INDV_ID_IMPORTACION = "";
				String db_INDV_ESARBOLREFERENCIA = "";
				String db_INDV_CARDINALIDAD = "";
				String db_INDV_NUMERO_COLECTOR = "";
				String db_INDV_CANTIDAD_EJEMPLARES = "";
				//String db_familia = "";
				//String db_genero = "";
				String db_especie = "";
				String db_dap = "";
				String db_fecha_toma = "";
				
				String opciones = "";
				
				t = Auxiliar.traducir(yo+"Individuos_Encontrados", idioma, "Individuos Encontrados" + " ");
				tabla = "<h3>"+t+"</h3>";
				
				tabla += "<div id='contenedor-resultados' class='tabla_resultados'>";
				
				String t_INDV_CONSECUTIVO = "";
				String t_INDV_PRCL_CONSECUTIVO = "";
				String t_INDV_NUMERO_ARBOL = "";
				String t_INDV_ID_IMPORTACION = "";
				String t_INDV_ESARBOLREFERENCIA = "";
				String t_INDV_CARDINALIDAD = "";
				String t_INDV_NUMERO_COLECTOR = "";
				String t_INDV_CANTIDAD_EJEMPLARES = "";
				String t_INDV_ESPECIE = "";
				String t_Opciones = "";
				
				tabla += "<tr>";
				t_INDV_CONSECUTIVO = Auxiliar.traducir("INDV_CONSECUTIVO", idioma, "Consecutivo del individuo en REDD" + " ");
				t_INDV_PRCL_CONSECUTIVO = Auxiliar.traducir("INDV_PRCL_CONSECUTIVO", idioma, "Consecutivo Parcela" + " ");
				t_INDV_NUMERO_ARBOL = Auxiliar.traducir("INDV_NUMERO_ARBOL", idioma, "Número de árbol" + " ");
				t_INDV_ID_IMPORTACION = Auxiliar.traducir("INDV_ID_IMPORTACION", idioma, "Consecutivo Importación" + " ");
				t_INDV_ESARBOLREFERENCIA = Auxiliar.traducir("INDV_ESARBOLREFERENCIA", idioma, "Id UPM" + " ");
				t_INDV_CARDINALIDAD = Auxiliar.traducir("INDV_CARDINALIDAD", idioma, "Id USM" + " ");
				t_INDV_NUMERO_COLECTOR = Auxiliar.traducir("INDV_NUMERO_COLECTOR", idioma, "Id UTM" + " ");
				t_INDV_CANTIDAD_EJEMPLARES = Auxiliar.traducir("INDV_CANTIDAD_EJEMPLARES", idioma, "Id UCM" + " ");
				t_INDV_ESPECIE = Auxiliar.traducir("INDV_ESPECIE", idioma, "Especie" + " ");
				t_Opciones = Auxiliar.traducir("General.Opciones", idioma, "Opciones" + " ");
				
				while (rset.next())
				{
					n++;
					
					opciones = "";
					
					db_INDV_CONSECUTIVO = rset.getString("INDV_CONSECUTIVO");
					db_INDV_PRCL_CONSECUTIVO = rset.getString("INDV_PRCL_CONSECUTIVO");
					db_INDV_NUMERO_ARBOL = rset.getString("INDV_NUMERO_ARBOL");
					db_INDV_ID_IMPORTACION = rset.getString("INDV_ID_IMPORTACION");
					db_INDV_ESARBOLREFERENCIA = rset.getString("INDV_ESARBOLREFERENCIA");
					db_INDV_CARDINALIDAD = rset.getString("INDV_CARDINALIDAD");
					db_INDV_NUMERO_COLECTOR = rset.getString("INDV_NUMERO_COLECTOR");
					db_INDV_CANTIDAD_EJEMPLARES = rset.getString("INDV_CANTIDAD_EJEMPLARES");
					db_especie = rset.getString("INDV_ESPECIE");

					t = Auxiliar.traducir("General.Ver_Detalle", idioma, "Ver Detalle" + " ");
					opciones += "<div class='opcionmenu'><a class=boton href='Individuo?accion=detalle_individuo&INDV_PRCL_CONSECUTIVO="+db_INDV_PRCL_CONSECUTIVO+"&INDV_CONSECUTIVO="+db_INDV_CONSECUTIVO+"' target='_blank'>" + t + "</a></div>";
					
					tabla += "<span>";
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_INDV_CONSECUTIVO+":"+db_INDV_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_PRCL_CONSECUTIVO+":"+db_INDV_PRCL_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_NUMERO_ARBOL+":"+db_INDV_NUMERO_ARBOL+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_ID_IMPORTACION+":"+db_INDV_ID_IMPORTACION+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_ESARBOLREFERENCIA+":"+db_INDV_ESARBOLREFERENCIA+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_CARDINALIDAD+":"+db_INDV_CARDINALIDAD+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_NUMERO_COLECTOR+":"+db_INDV_NUMERO_COLECTOR+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_CANTIDAD_EJEMPLARES+":"+db_INDV_CANTIDAD_EJEMPLARES+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INDV_ESPECIE+":"+db_especie+"</div>";
					tabla += "<div class='botones_resultado'>"+opciones+"</div>";
					tabla += "</div>";
					tabla += "</span>";

				}
				
				tabla += "</div>";
				
				rset.close();
				
				r = tabla + "==!!==" + String.valueOf(n);
			}
			else
			{
				r += "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError + "==!!==0";
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString() + "==!!==0";
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql + "==!!==0";
		}
		
		dbREDD.desconectarse();
		return String.valueOf(n) + "-=-" + r;
	}
	
	
	/**
	 * Metodo para retornar string de opciones valor@@texto para un select de especies.
	 * 
	 * @param texto
	 * @return String r con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String encontrarEspecies(String texto, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "encontrarEspecies";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();
		
		String r = "";
		
		String txt = texto.toLowerCase();
		
		if (!Auxiliar.tieneAlgo(texto)) {
			t = Auxiliar.traducir(yo+"Por_favor_especifique_algun_nombre_de_especie", idioma, "Por favor especifique algún nombre de especie" + " ");
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}
		
		/*
		String sql = "SELECT DISTINCT";
		sql += " INDV_ESPECIE";
		sql += " FROM RED_INDIVIDUO ";
		sql += " WHERE LOWER(INDV_ESPECIE) LIKE '%"+texto.toLowerCase()+"%' ";
		sql += " ORDER BY INDV_ESPECIE ";
		*/
		
		String sql = "SELECT T.TXCT_ID, ' K:' || T.TXCT_KNG || ' -D:' || T.TXCT_DVS || ' -C:' || T.TXCT_CLS || ' -O:' || T.TXCT_ORD || ' -F:' || T.TXCT_FML || ' -G:' || T.TXCT_GNS || ' -S:' || T.TXCT_SPC || ' -E:' || T.TXCT_NME_SPC || ' -CF:' || TO_CHAR(T.TXCT_CODIGO_FORESTAL) AS INFO " +
			" FROM IDT_TAXONOMY_CATALOGUE T " +
			" WHERE 1=1 " + 
			" AND " + 
			" (LOWER(TO_CHAR(T.TXCT_ID)) LIKE '%"+txt+"%' " +
			" OR LOWER(T.TXCT_KNG) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_DVS) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_CLS) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_ORD) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_FML) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_GNS) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_SPC) LIKE '%"+txt+"%' " + 
			" OR LOWER(T.TXCT_NME_SPC) LIKE '%"+txt+"%' " + 
			" OR LOWER(TO_CHAR(T.TXCT_CODIGO_FORESTAL)) LIKE '%"+txt+"%' " + 
			" ) AND ROWNUM < 10";		
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String opciones="";
				
				String db_TXCT_ID = "";
				String db_TXCT_INFO = "";
				
				while (rset.next())
				{
					db_TXCT_ID = rset.getString("TXCT_ID");
					db_TXCT_INFO = rset.getString("INFO");
					
					opciones += db_TXCT_ID+"@@"+db_TXCT_INFO+"\n";
				}
				
				rset.close();
				
				r=opciones;
			}
			else
			{
				r += "Error SQL:"+dbREDD.ultimoError+"@@"+sql;
				System.out.println("Error SQL al consultar especies:"+dbREDD.ultimoError+"@@"+sql);
			}
		} catch (SQLException e) {
			r += "Error SQL:"+dbREDD.ultimoError+"@@"+sql;
			System.out.println("Error SQL al consultar especies:"+dbREDD.ultimoError+"@@"+sql);
		} catch (Exception e) {
			r += "Excepción:@@"+e.toString();
			System.out.println("Excepción: " + e.toString());
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	
	/**
	 * Guarda un individuo (árbol)
	 * 
	 * @param INDV_CONSECUTIVO
	 * @param INDV_PRCL_CONSECUTIVO
	 * @param INDV_ID_IMPORTACION
	 * @param INDV_ESARBOLREFERENCIA
	 * @param INDV_CARDINALIDAD
	 * @param INDV_NUMERO_COLECTOR
	 * @param INDV_CANTIDAD_EJEMPLARES
	 * @param INDV_DISTANCIA
	 * @param INDV_AZIMUTH
	 * @param INDV_NUMERO_ARBOL
	 * @param INDV_ESPECIE
	 * @param INDV_SUBPARCELA
	 * @param INDV_OBSERVACIONES
	 * @param INDV_ETIQUETA_COLECTA
	 * @param INDV_FOTO_COLECTA
	 * @param INDV_HOMOLOGACION
	 * @param INDV_TXCT_ID
	 * @param INDV_ARCH_CONSECUTIVO
	 * @param INDV_PRCL_PLOT
	 * @param INDV_DENSIDAD
	 * @param INDV_FAMILIA
	 * @param INDV_AUTORFAMILIA
	 * @param INDV_GENERO
	 * @param INDV_AUTORGENERO
	 * @param INDV_ESTADOEPITETO
	 * @param INDV_EPITETO
	 * @param INDV_MORFOESPECIE
	 * @param INDV_AUTORESPECIE
	 * @param INDV_HABITO
	 * @param INDV_INCLUIR
	 * @param INDV_FID
	 * @param INDV_ACTUALIZACION
	 * @param INDV_TAYO_DAP1
	 * @param INDV_TAYO_DAP2
	 * @param INDV_TAYO_ALTURADAP
	 * @param INDV_TAYO_ALTURA
	 * @param INDV_TAYO_ALTURATOTAL
	 * @param INDV_TAYO_FORMAFUSTE
	 * @param INDV_TAYO_DANIO
	 * @param INDV_TAYO_OBSERVACIONES
	 * @param request
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * @param sid
	 * @param importacion
	 * @return String con resultado de la operación
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String guardar(
			String INDV_CONSECUTIVO,
			String INDV_PRCL_CONSECUTIVO,
			String INDV_ID_IMPORTACION, 
			String INDV_ESARBOLREFERENCIA, 
			String INDV_CARDINALIDAD, 
			String INDV_NUMERO_COLECTOR, 
			String INDV_CANTIDAD_EJEMPLARES, 
			String INDV_DISTANCIA,
			String INDV_AZIMUTH,
			String INDV_NUMERO_ARBOL,
			String INDV_ESPECIE,
			String INDV_SUBPARCELA,
			String INDV_OBSERVACIONES,
			String INDV_ETIQUETA_COLECTA,
			String INDV_FOTO_COLECTA,
			String INDV_HOMOLOGACION,
			//String INDV_ARCHIVO_FOTOS,
			String INDV_TXCT_ID,
			String INDV_ARCH_CONSECUTIVO,
			String INDV_PRCL_PLOT,
			String INDV_DENSIDAD,
			String INDV_FAMILIA,
			String INDV_AUTORFAMILIA,
			String INDV_GENERO,
			String INDV_AUTORGENERO,
			String INDV_ESTADOEPITETO,
			String INDV_EPITETO,
			String INDV_MORFOESPECIE,
			String INDV_AUTORESPECIE,
			String INDV_HABITO,
			String INDV_INCLUIR,
			String INDV_FID,
			String INDV_ACTUALIZACION,
			String INDV_TAYO_DAP1,
			String INDV_TAYO_DAP2,
			String INDV_TAYO_ALTURADAP,
			String INDV_TAYO_ALTURA,
			String INDV_TAYO_ALTURATOTAL,
			String INDV_TAYO_FORMAFUSTE,
			String INDV_TAYO_DANIO,
			String INDV_TAYO_OBSERVACIONES,
			HttpServletRequest request,
			String username, String password, String host, String port, String sid, boolean importacion
			) 
	throws Exception
	{
		String metodo = yo + "guardar";

		Sec sec = new Sec();
		Codec ORACLE_CODEC = new OracleCodec();

		//Auxiliar aux = new Auxiliar();
	    String t = "";
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();

	    if (!Auxiliar.tieneAlgo(this.RED_username)) this.RED_username = Auxiliar.nz(username, "");
	    if (!Auxiliar.tieneAlgo(this.RED_password)) this.RED_password = Auxiliar.nz(password, "");
	    if (!Auxiliar.tieneAlgo(this.RED_host)) this.RED_host = Auxiliar.nz(host, "");
	    if (!Auxiliar.tieneAlgo(this.RED_port)) this.RED_port = Auxiliar.nz(port, "");
		if (!Auxiliar.tieneAlgo(this.RED_sid)) this.RED_sid = Auxiliar.nz(sid, "");

		BD dbREDD = new BD();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");

		boolean ok_operacion = false;
		boolean update = false;
		boolean privilegio = false;
		
		String id_creador = "";

		String resultado = "";
		String novedades = "";
		String update_esadmin = "";
		

		String observaciones = "";
		String aviso = "";
		boolean parametros_insert_ok = true;
		boolean parametros_update_ok = true;
		boolean parametros_import_ok = true;

		String conteo = "";
		String sql_tmp = "";

		String pe = "";

		pe = Auxiliar.traducir("General.Por_favor_especifique", idioma, "Por favor especifique" + " ");
		
		
		// VERIFICAR PARAMETROS
		
		// VALIDAR INDV_ACTUALIZACION
		String fechahora = "SYSDATE";
		String tz = "UTC";
		if (Auxiliar.tieneAlgo(INDV_ACTUALIZACION)) {
			String [] a_actualizacion = INDV_ACTUALIZACION.split("@");
			fechahora = a_actualizacion[0].trim();
			if (a_actualizacion.length > 1) {
				tz = a_actualizacion[1];
			}
			
			if (!Auxiliar.fechaHoraEsValida(fechahora) && !fechahora.equals("SYSDATE")) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ACTUALIZACION", idioma, "Fecha INDV_ACTUALIZACION debe ser válida y estar en formato yyyy-mm-dd hh:m:ss@zona Ejemplo: 2013-10-13 21:29:44@-05:00.  Si importa desde excel, se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_ACTUALIZACION, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
			else {
				if (!fechahora.equals("SYSDATE")) {
					if (Auxiliar.tieneAlgo(tz)) {
						INDV_ACTUALIZACION = "CAST(FROM_TZ(TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss'), '"+tz+"') AT TIME ZONE ('UTC') AS TIMESTAMP)";
					}
					else {
						INDV_ACTUALIZACION = "TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss')";
					}
				}
				else {
					INDV_ACTUALIZACION = fechahora;
				}
			}
		}
		
		if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			if (Auxiliar.tieneAlgo(INDV_FID)) {
				INDV_CONSECUTIVO = dbREDD.obtenerDato("SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_FID="+INDV_FID, "");
			}
		}	

		// VALIDAR INDV_NUMERO_ARBOL
		INDV_NUMERO_ARBOL = Auxiliar.limpiarTexto(INDV_NUMERO_ARBOL);
		INDV_NUMERO_ARBOL = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_NUMERO_ARBOL, ""));
		if (!Auxiliar.tieneAlgo(INDV_NUMERO_ARBOL) || INDV_NUMERO_ARBOL.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_NUMERO_ARBOL", idioma, "Por favor especifique el INDV_NUMERO_ARBOL.  Este no debe exceder los 255 caracteres." + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_NUMERO_ARBOL, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}

		// ASOCIAR CON LA PARCELA MEDIANTE INDV_PRCL_PLOT
		if (!Auxiliar.tieneAlgo(INDV_PRCL_CONSECUTIVO)) {
			if (Auxiliar.tieneAlgo(INDV_PRCL_PLOT)) {
				INDV_PRCL_CONSECUTIVO = dbREDD.obtenerDato("SELECT PRCL_CONSECUTIVO FROM RED_PARCELA WHERE PRCL_PLOT="+INDV_PRCL_PLOT, "");
			}
		}	

		if (!Auxiliar.tieneAlgo(INDV_PRCL_CONSECUTIVO)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_PRCL_CONSECUTIVO", idioma, "Faltó especificar el consecutivo de la parcela " + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_PRCL_CONSECUTIVO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		else {
			// VERIFICAR INDV_PRCL_CONSECUTIVO
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + INDV_PRCL_CONSECUTIVO;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.INDV_PRCL_CONSECUTIVO", idioma, "Problemas al intentar determinar la existencia de INDV_PRCL_CONSECUTIVO [" + ex.toString() + "]:" + sql_tmp + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_PRCL_CONSECUTIVO, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.INDV_PRCL_CONSECUTIVO", idioma, "INDV_PRCL_CONSECUTIVO no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_PRCL_CONSECUTIVO, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
			
			if (!Auxiliar.tieneAlgo(INDV_ESPECIE)){
				if (Auxiliar.tieneAlgo(INDV_MORFOESPECIE)) {
					INDV_ESPECIE = INDV_MORFOESPECIE;
				}
				else {
					INDV_ESPECIE = "NA";
				}
			}
		}
		
		// VALIDAR INDV_ESPECIE
		INDV_ESPECIE = Auxiliar.limpiarTexto(INDV_ESPECIE);
		INDV_ESPECIE = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ESPECIE, ""));
		if (Auxiliar.tieneAlgo(INDV_ESPECIE)) {
			if (INDV_ESPECIE.length() > 4000) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ESPECIE", idioma, "ESPECIE no puede exceder los 255 caracteres" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_ESPECIE, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}
		
		// VALIDAR INDV_ESARBOLREFERENCIA
		if (Auxiliar.tieneAlgo(INDV_ESARBOLREFERENCIA)) {
			if (!Auxiliar.nz(INDV_ESARBOLREFERENCIA, "").equals("0") && !Auxiliar.nz(INDV_ESARBOLREFERENCIA, "").equals("1")) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ESARBOLREFERENCIA", idioma, "ESARBOLREFERENCIA debe ser 0 o 1." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_ESARBOLREFERENCIA, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}
		else {
			INDV_ESARBOLREFERENCIA = "0";
		}
			
		// VALIDAR INDV_CARDINALIDAD 
		if (Auxiliar.tieneAlgo(INDV_CARDINALIDAD)) {
			if (!Auxiliar.esEnteroMayorOIgualACero(INDV_CARDINALIDAD)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_CARDINALIDAD", idioma, "CARDINALIDAD debe ser un entero mayor o igual a 0." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_CARDINALIDAD, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}
		else {
			INDV_CARDINALIDAD = "1";
		}
			
		// VALIDAR INDV_NUMERO_COLECTOR 
		if (Auxiliar.tieneAlgo(INDV_NUMERO_COLECTOR)) {
			if (!Auxiliar.esEnteroMayorOIgualACero(INDV_NUMERO_COLECTOR)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_NUMERO_COLECTOR", idioma, "NUMERO_COLECTOR debe ser un entero mayor o igual a 0." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_NUMERO_COLECTOR, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}
			
		// VALIDAR INDV_CANTIDAD_EJEMPLARES 
		if (Auxiliar.tieneAlgo(INDV_CANTIDAD_EJEMPLARES)) {
			if (!Auxiliar.esEnteroMayorOIgualACero(INDV_CANTIDAD_EJEMPLARES)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_CANTIDAD_EJEMPLARES", idioma, "CANTIDAD_EJEMPLARES debe ser un entero mayor o igual a 0." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_CANTIDAD_EJEMPLARES, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}
		
		// VALIDAR INDV_DISTANCIA
		INDV_DISTANCIA = INDV_DISTANCIA.replace(",", ".");
		if (!Auxiliar.esDistancia(INDV_DISTANCIA)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_DISTANCIA", idioma, "DISTANCIA debe ser numérica y positiva" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_DISTANCIA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}

		// VALIDAR INDV_AZIMUTH
		INDV_AZIMUTH = INDV_AZIMUTH.replace(",", ".");
		if (!Auxiliar.esAzimuth(INDV_AZIMUTH)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_AZIMUTH", idioma, "AZIMUTH debe ser un número entero entre 0 y 359" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_AZIMUTH, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_SUBPARCELA
		if (!Auxiliar.esSPF(INDV_SUBPARCELA)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_SUBPARCELA", idioma, "SUBPARCELA debe ser un número entero entre 1 y 5" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_SUBPARCELA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}

		// VALIDAR INDV_OBSERVACIONES
		INDV_OBSERVACIONES = Auxiliar.limpiarTexto(INDV_OBSERVACIONES);
		INDV_OBSERVACIONES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_OBSERVACIONES, ""));
		if (INDV_OBSERVACIONES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_OBSERVACIONES", idioma, "OBSERVACIONES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_OBSERVACIONES, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_ETIQUETA_COLECTA
		INDV_ETIQUETA_COLECTA = Auxiliar.limpiarTexto(INDV_ETIQUETA_COLECTA);
		INDV_ETIQUETA_COLECTA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ETIQUETA_COLECTA, ""));
		if (INDV_ETIQUETA_COLECTA.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ETIQUETA_COLECTA", idioma, "ETIQUETA_COLECTA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_ETIQUETA_COLECTA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_FOTO_COLECTA
		INDV_FOTO_COLECTA = Auxiliar.limpiarTexto(INDV_FOTO_COLECTA);
		INDV_FOTO_COLECTA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_FOTO_COLECTA, ""));
		if (INDV_FOTO_COLECTA.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_FOTO_COLECTA", idioma, "FOTO_COLECTA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_FOTO_COLECTA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_HOMOLOGACION
		INDV_HOMOLOGACION = Auxiliar.limpiarTexto(INDV_HOMOLOGACION);
		INDV_HOMOLOGACION = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_HOMOLOGACION, ""));
		if (INDV_HOMOLOGACION.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_HOMOLOGACION", idioma, "HOMOLOGACION no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_HOMOLOGACION, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		/*
		// VALIDAR INDV_ARCHIVO_FOTOS
		INDV_ARCHIVO_FOTOS = Auxiliar.limpiarTexto(INDV_ARCHIVO_FOTOS);
		INDV_ARCHIVO_FOTOS = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ARCHIVO_FOTOS, ""));
		if (INDV_ARCHIVO_FOTOS.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ARCHIVO_FOTOS", idioma, "ARCHIVO_FOTOS no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_ARCHIVO_FOTOS, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		*/
		
		if (!importacion) {
			// VERIFICAR INDV_TXCT_ID
//			if (Auxiliar.tieneAlgo(INDV_TXCT_ID)) {
				conteo = "0";
				sql_tmp = "SELECT COUNT(*) FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID=" + INDV_TXCT_ID;
				try {
					conteo = dbREDD.obtenerDato(sql_tmp, "0");
				} catch (Exception ex) {
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.INDV_TXCT_ID", idioma, "Problemas al intentar determinar la existencia de INDV_TXCT_ID [" + ex.toString() + "]:" + sql_tmp + "..");
					aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TXCT_ID, usuario, metodo);
					observaciones += aviso;
					parametros_insert_ok = false;
					parametros_update_ok = false;
				}
				if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.INDV_TXCT_ID", idioma, "TXCT_ID no encontrado" + "..");
					aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TXCT_ID, usuario, metodo);
					observaciones += aviso;
					parametros_insert_ok = false;
					parametros_update_ok = false;
				}
//			}
		}
		
		// VALIDAR INDV_DENSIDAD
		if (Auxiliar.tieneAlgo(INDV_DENSIDAD) && !INDV_DENSIDAD.equals("NULL")) {
			INDV_DENSIDAD = INDV_DENSIDAD.replace(",", ".");
			if (!Auxiliar.esDensidad(INDV_DENSIDAD)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_DENSIDAD", idioma, "DENSIDAD debe ser numérica y positiva" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_DENSIDAD, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}
		
		// VALIDAR INDV_FAMILIA
		INDV_FAMILIA = Auxiliar.limpiarTexto(INDV_FAMILIA);
		INDV_FAMILIA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_FAMILIA, ""));
		if (INDV_FAMILIA.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_FAMILIA", idioma, "FAMILIA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_FAMILIA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_AUTORFAMILIA
		INDV_AUTORFAMILIA = Auxiliar.limpiarTexto(INDV_AUTORFAMILIA);
		INDV_AUTORFAMILIA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_AUTORFAMILIA, ""));
		if (INDV_AUTORFAMILIA.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_AUTORFAMILIA", idioma, "AUTORFAMILIA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_AUTORFAMILIA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_GENERO
		INDV_GENERO = Auxiliar.limpiarTexto(INDV_GENERO);
		INDV_GENERO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_GENERO, ""));
		if (INDV_GENERO.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_GENERO", idioma, "GENERO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_GENERO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_AUTORGENERO
		INDV_AUTORGENERO = Auxiliar.limpiarTexto(INDV_AUTORGENERO);
		INDV_AUTORGENERO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_AUTORGENERO, ""));
		if (INDV_AUTORGENERO.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_AUTORGENERO", idioma, "AUTORGENERO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_AUTORGENERO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_ESTADOEPITETO
		INDV_ESTADOEPITETO = Auxiliar.limpiarTexto(INDV_ESTADOEPITETO);
		INDV_ESTADOEPITETO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_ESTADOEPITETO, ""));
		if (INDV_ESTADOEPITETO.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ESTADOEPITETO", idioma, "ESTADOEPITETO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_ESTADOEPITETO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_EPITETO
		INDV_EPITETO = Auxiliar.limpiarTexto(INDV_EPITETO);
		INDV_EPITETO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_EPITETO, ""));
		if (INDV_EPITETO.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_EPITETO", idioma, "EPITETO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_EPITETO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_MORFOESPECIE
		INDV_MORFOESPECIE = Auxiliar.limpiarTexto(INDV_MORFOESPECIE);
		INDV_MORFOESPECIE = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_MORFOESPECIE, ""));
		if (INDV_MORFOESPECIE.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_MORFOESPECIE", idioma, "MORFOESPECIE no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_MORFOESPECIE, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}
		
		// VALIDAR INDV_AUTORESPECIE
		INDV_AUTORESPECIE = Auxiliar.limpiarTexto(INDV_AUTORESPECIE);
		INDV_AUTORESPECIE = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_AUTORESPECIE, ""));
		if (INDV_AUTORESPECIE.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_AUTORESPECIE", idioma, "AUTORESPECIE no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_AUTORESPECIE, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_update_ok = false;
		}

		// VERIFICAR INDV_HABITO
		if (Auxiliar.tieneAlgo(INDV_HABITO)) {
			String INDV_TEXTO_HABITO = INDV_HABITO;
			try {
				if (Auxiliar.esEntero(INDV_HABITO)) {
					INDV_HABITO = dbREDD.obtenerDato("SELECT HABI_ID FROM RED_HABITO WHERE HABI_ID="+INDV_HABITO, "");
				}
				else {
					INDV_HABITO = dbREDD.obtenerDato("SELECT HABI_ID FROM RED_HABITO WHERE HABI_NOMBRE='" + INDV_HABITO + "'", "");
				}
			} catch (Exception ex) {
			}
			
			if (!Auxiliar.esEntero(INDV_HABITO)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.INDV_HABITO", idioma, "HABITO no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_HABITO, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_update_ok = false;
			}
		}		
		
		// VALIDAR INDV_TAYO_DAP1
		if (Auxiliar.tieneAlgo(INDV_TAYO_DAP1)) {
			INDV_TAYO_DAP1 = INDV_TAYO_DAP1.replace(",", ".");
			if (!Auxiliar.esNumeroPositivo(INDV_TAYO_DAP1)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_TAYO_DAP1", idioma, "TAYO_DAP1 debe ser numérico y positivo" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_DAP1, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VALIDAR INDV_TAYO_DAP2
		if (Auxiliar.tieneAlgo(INDV_TAYO_DAP2)) {
			INDV_TAYO_DAP2 = INDV_TAYO_DAP2.replace(",", ".");
			if (!Auxiliar.esNumeroPositivo(INDV_TAYO_DAP2)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_TAYO_DAP2", idioma, "TAYO_DAP2 debe ser numérico y positivo" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_DAP2, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VALIDAR INDV_TAYO_ALTURADAP
		if (Auxiliar.tieneAlgo(INDV_TAYO_ALTURADAP)) {
			INDV_TAYO_ALTURADAP = INDV_TAYO_ALTURADAP.replace(",", ".");
			if (!Auxiliar.esNumeroPositivo(INDV_TAYO_ALTURADAP)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_TAYO_ALTURADAP", idioma, "TAYO_ALTURADAP debe ser numérica y positiva" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_ALTURADAP, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VALIDAR INDV_TAYO_ALTURA
		if (Auxiliar.tieneAlgo(INDV_TAYO_ALTURA)) {
			INDV_TAYO_ALTURA = INDV_TAYO_ALTURA.replace(",", ".");
			if (!Auxiliar.esNumeroPositivo(INDV_TAYO_ALTURA)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_TAYO_ALTURA", idioma, "TAYO_ALTURA debe ser numérica y positiva" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_ALTURA, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VALIDAR INDV_TAYO_ALTURATOTAL
		if (Auxiliar.tieneAlgo(INDV_TAYO_ALTURATOTAL)) {
			INDV_TAYO_ALTURATOTAL = INDV_TAYO_ALTURATOTAL.replace(",", ".");
			if (!Auxiliar.esNumeroPositivo(INDV_TAYO_ALTURATOTAL)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.INDV_TAYO_ALTURATOTAL", idioma, "TAYO_ALTURATOTAL debe ser numérica y positiva" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_ALTURATOTAL, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VERIFICAR INDV_TAYO_FORMAFUSTE
		if (Auxiliar.tieneAlgo(INDV_TAYO_FORMAFUSTE)) {
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_FORMAFUSTE WHERE FRFS_CONSECUTIVO=" + INDV_TAYO_FORMAFUSTE;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.INDV_TAYO_FORMAFUSTE", idioma, "Problemas al intentar determinar la existencia de INDV_TAYO_FORMAFUSTE [" + ex.toString() + "]:" + sql_tmp + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_FORMAFUSTE, usuario, metodo);
				observaciones += aviso;
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.INDV_TAYO_FORMAFUSTE", idioma, "TAYO_FORMAFUSTE no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_FORMAFUSTE, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VERIFICAR INDV_TAYO_DANIO
		if (Auxiliar.tieneAlgo(INDV_TAYO_DANIO)) {
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_DANO WHERE DANO_CONSECUTIVO=" + INDV_TAYO_DANIO;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.INDV_TAYO_DANIO", idioma, "Problemas al intentar determinar la existencia de INDV_TAYO_DANIO [" + ex.toString() + "]:" + sql_tmp + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_DANIO, usuario, metodo);
				observaciones += aviso;
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.INDV_TAYO_DANIO", idioma, "TAYO_DANIO no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_DANIO, usuario, metodo);
				observaciones += aviso;
			}
		}
		
		// VALIDAR INDV_TAYO_OBSERVACIONES
		INDV_TAYO_OBSERVACIONES = Auxiliar.limpiarTexto(INDV_TAYO_OBSERVACIONES);
		INDV_TAYO_OBSERVACIONES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(INDV_TAYO_OBSERVACIONES, ""));
		if (INDV_TAYO_OBSERVACIONES.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_TAYO_OBSERVACIONES", idioma, "TAYO_OBSERVACIONES no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_TAYO_OBSERVACIONES, usuario, metodo);
			observaciones += aviso;
		}

		// VALIDAR INDV_INCLUIR
		if (!Auxiliar.nz(INDV_INCLUIR, "").equals("0") && !Auxiliar.nz(INDV_INCLUIR, "").equals("1")) {
			t = Auxiliar.traducir("AVISO_VALIDACION.INDV_INCLUIR", idioma, "INDV_INCLUIR debe ser 0 (entra en cálculos) o 1 (no es incluida en cálculos)." + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + INDV_INCLUIR, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_import_ok = false;
			parametros_update_ok = false;
		}

		
		// SI EL FID ESTA DADO ACTUALIZAR SEGÚN ESE IDENTIFICADOR
		if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			if (Auxiliar.tieneAlgo(INDV_FID)) {
				INDV_CONSECUTIVO = dbREDD.obtenerDato("SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_FID="+INDV_FID, "");
			}
		}
		
		
		
		// CALCULAR COORDENADAS DEL INDIVIDUO
		
    	Double lat = 0.0;
    	Double lon = 0.0;
    	
    	
    	// OBTENER LATITUD Y LONGITUD DEL JALÓN
    	
    	Double jaLat = Double.parseDouble(dbREDD.obtenerDato("SELECT PRCL_LATITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + INDV_PRCL_CONSECUTIVO, "0.0"));
    	Double jaLon = Double.parseDouble(dbREDD.obtenerDato("SELECT PRCL_LONGITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + INDV_PRCL_CONSECUTIVO, "0.0"));
    
    	Double azimuth = Double.parseDouble(INDV_AZIMUTH);
    	Double distancia = Double.parseDouble(INDV_DISTANCIA);

    	
    	// CALCULAR INDV_LATITUD E INDV_LONGITUD

    	Double dLat = 0.0;
    	Double dLon = 0.0;

    	Double alpha = azimuth - 90;
    	Double beta = 180 - azimuth;
    	Double gamma = 90.0;
    	
    	dLat = Math.sin(alpha) / distancia;
    	dLon = Math.sqrt(Math.pow(distancia, 2) - Math.pow(dLat, 2));
    	
    	if (azimuth%360 > 180 && azimuth%360 < 360) dLon *= -1;
    	
    	Double factor = 0.0000090;
    	
    	lat = jaLat + dLat*(factor);
    	lon = jaLon + dLon*(factor);
    	
    	String INDV_LATITUD = String.valueOf(lat);
    	String INDV_LONGITUD = String.valueOf(lon);

		
    	String BA = "";
    	String CO2 = "";
    	String mensajeBMC = "";
    	
    	
    	
		// REALIZAR OPERACION
		
		String sql_guardar = "";
		String sql_indv_ifn = "";
		
		try 
		{
			if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
				if (!parametros_update_ok) {
					dbREDD.desconectarse();
					return "0-=-"+observaciones;
				}

				sql_guardar = "UPDATE RED_INDIVIDUO SET ";
				if (Auxiliar.noEsNulo(INDV_ESARBOLREFERENCIA)) sql_guardar += "INDV_ESARBOLREFERENCIA=" + INDV_ESARBOLREFERENCIA + ",";
				if (Auxiliar.noEsNulo(INDV_CARDINALIDAD)) sql_guardar += "INDV_CARDINALIDAD=" + INDV_CARDINALIDAD + ",";
				if (Auxiliar.noEsNulo(INDV_NUMERO_COLECTOR)) sql_guardar += "INDV_NUMERO_COLECTOR=" + Auxiliar.nzVacio(INDV_NUMERO_COLECTOR, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_CANTIDAD_EJEMPLARES)) sql_guardar += "INDV_CANTIDAD_EJEMPLARES=" + Auxiliar.nzVacio(INDV_CANTIDAD_EJEMPLARES, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_NUMERO_ARBOL)) sql_guardar += "INDV_NUMERO_ARBOL='" + INDV_NUMERO_ARBOL.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_DISTANCIA)) sql_guardar += "INDV_DISTANCIA=" + INDV_DISTANCIA + ",";
				if (Auxiliar.noEsNulo(INDV_AZIMUTH)) sql_guardar += "INDV_AZIMUTH=" + INDV_AZIMUTH + ",";
				if (Auxiliar.noEsNulo(INDV_LATITUD)) sql_guardar += "INDV_LATITUD=" + Auxiliar.nzVacio(INDV_LATITUD, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_LONGITUD)) sql_guardar += "INDV_LONGITUD=" + Auxiliar.nzVacio(INDV_LONGITUD, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_ESPECIE)) sql_guardar += "INDV_ESPECIE='" + INDV_ESPECIE.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_SUBPARCELA)) sql_guardar += "INDV_SUBPARCELA=" + INDV_SUBPARCELA + ",";
				if (Auxiliar.noEsNulo(INDV_OBSERVACIONES)) sql_guardar += "INDV_OBSERVACIONES='" + INDV_OBSERVACIONES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_ETIQUETA_COLECTA)) sql_guardar += "INDV_ETIQUETA_COLECTA='" + INDV_ETIQUETA_COLECTA + "',";
				if (Auxiliar.noEsNulo(INDV_FOTO_COLECTA)) sql_guardar += "INDV_FOTO_COLECTA='" + INDV_FOTO_COLECTA.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_HOMOLOGACION)) sql_guardar += "INDV_HOMOLOGACION='" + INDV_HOMOLOGACION.replace("'", "`") + "',";
				//if (Auxiliar.noEsNulo(INDV_ARCHIVO_FOTOS)) if (!INDV_ARCHIVO_FOTOS.equals("NULL")) sql_guardar += "INDV_ARCHIVO_FOTOS='" + INDV_ARCHIVO_FOTOS + "',";
				if (Auxiliar.noEsNulo(INDV_ID_IMPORTACION)) sql_guardar += "INDV_ID_IMPORTACION=" + Auxiliar.nzVacio(INDV_ID_IMPORTACION, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_FID)) sql_guardar += "INDV_FID=" + Auxiliar.nzVacio(INDV_FID, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_PRCL_PLOT)) sql_guardar += "INDV_PRCL_PLOT=" + Auxiliar.nzVacio(INDV_PRCL_PLOT, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_DENSIDAD)) sql_guardar += "INDV_DENSIDAD=" + Auxiliar.nzVacio(INDV_DENSIDAD, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_FAMILIA)) sql_guardar += "INDV_FAMILIA='" + INDV_FAMILIA.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_AUTORFAMILIA)) sql_guardar += "INDV_AUTORFAMILIA='" + INDV_AUTORFAMILIA.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_GENERO)) sql_guardar += "INDV_GENERO='" + INDV_GENERO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_AUTORGENERO)) sql_guardar += "INDV_AUTORGENERO='" + INDV_AUTORGENERO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_ESTADOEPITETO)) sql_guardar += "INDV_ESTADOEPITETO='" + INDV_ESTADOEPITETO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_EPITETO)) sql_guardar += "INDV_EPITETO='" + INDV_EPITETO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_MORFOESPECIE)) sql_guardar += "INDV_MORFOESPECIE='" + INDV_MORFOESPECIE.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_AUTORESPECIE)) sql_guardar += "INDV_AUTORESPECIE='" + INDV_AUTORESPECIE.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(INDV_HABITO)) sql_guardar += "INDV_HABITO=" + Auxiliar.nzVacio(INDV_HABITO, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_INCLUIR)) sql_guardar += "INDV_INCLUIR=" + Auxiliar.nzVacio(INDV_INCLUIR, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_DAP1)) sql_guardar += "INDV_TAYO_DAP1=" + Auxiliar.nzVacio(INDV_TAYO_DAP1, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_DAP2)) sql_guardar += "INDV_TAYO_DAP2=" + Auxiliar.nzVacio(INDV_TAYO_DAP2, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_ALTURADAP)) sql_guardar += "INDV_TAYO_ALTURADAP=" + Auxiliar.nzVacio(INDV_TAYO_ALTURADAP, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_ALTURA)) sql_guardar += "INDV_TAYO_ALTURA=" + Auxiliar.nzVacio(INDV_TAYO_ALTURA, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_ALTURATOTAL)) sql_guardar += "INDV_TAYO_ALTURATOTAL=" + Auxiliar.nzVacio(INDV_TAYO_ALTURATOTAL, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_FORMAFUSTE)) sql_guardar += "INDV_TAYO_FORMAFUSTE=" + Auxiliar.nzVacio(INDV_TAYO_FORMAFUSTE, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_DANIO)) sql_guardar += "INDV_TAYO_DANIO=" + Auxiliar.nzVacio(INDV_TAYO_DANIO, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TAYO_OBSERVACIONES)) sql_guardar += "INDV_TAYO_OBSERVACIONES='" + Auxiliar.nzVacio(INDV_TAYO_OBSERVACIONES.replace("'", "`"), "NULL") + "',";
				//if (Auxiliar.noEsNulo(BA)) sql_guardar += "INDV_BIOMASA_AEREA=" + Auxiliar.nzVacio(BA, "NULL") + ",";
				//if (Auxiliar.noEsNulo(CO2)) sql_guardar += "INDV_CARBONO=" + Auxiliar.nzVacio(CO2, "NULL") + ",";
				if (Auxiliar.noEsNulo(INDV_TXCT_ID)) sql_guardar += "INDV_TXCT_ID=" + INDV_TXCT_ID + ",";
				sql_guardar += "INDV_MODIFICADOR=" + Auxiliar.nzVacio(id_usuario, "NULL") + ",";
				if (Auxiliar.tieneAlgo(INDV_ARCH_CONSECUTIVO)) sql_guardar += "INDV_ARCH_CONSECUTIVO=" + INDV_ARCH_CONSECUTIVO + ",";
				sql_guardar += "INDV_ACTUALIZACION="+Auxiliar.nzVacio(INDV_ACTUALIZACION, "SYSDATE")+"";
				//sql_guardar += " WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO + " AND INDV_ACTUALIZACION<" + Auxiliar.nzVacio(INDV_ACTUALIZACION, "SYSDATE");
				sql_guardar += " WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO;
				update = true;
			}
			else {
				if (!parametros_insert_ok) {
					dbREDD.desconectarse();
					return "0-=-"+observaciones;
				}

				INDV_CONSECUTIVO = dbREDD.obtenerDato("SELECT RED_SQ_INDIVIDUO.nextval FROM DUAL", "");
				
				sql_guardar = "INSERT INTO RED_INDIVIDUO ";
				sql_guardar += "(";
				sql_guardar += "INDV_CONSECUTIVO,";
				sql_guardar += "INDV_PRCL_CONSECUTIVO,";
				sql_guardar += "INDV_ESARBOLREFERENCIA,";
				sql_guardar += "INDV_CARDINALIDAD,";
				sql_guardar += "INDV_NUMERO_COLECTOR,";
				sql_guardar += "INDV_CANTIDAD_EJEMPLARES,";
				sql_guardar += "INDV_ID_IMPORTACION,";
				sql_guardar += "INDV_NUMERO_ARBOL,";
				sql_guardar += "INDV_DISTANCIA,";
				sql_guardar += "INDV_AZIMUTH,";
				sql_guardar += "INDV_LATITUD,";
				sql_guardar += "INDV_LONGITUD,";
				sql_guardar += "INDV_ESPECIE,";
				sql_guardar += "INDV_SUBPARCELA,";
				sql_guardar += "INDV_TXCT_ID,";
				sql_guardar += "INDV_OBSERVACIONES,";
				sql_guardar += "INDV_ETIQUETA_COLECTA,";
				sql_guardar += "INDV_FOTO_COLECTA,";
				sql_guardar += "INDV_HOMOLOGACION,";
				sql_guardar += "INDV_PRCL_PLOT,";
				sql_guardar += "INDV_DENSIDAD,";
				sql_guardar += "INDV_FAMILIA,";
				sql_guardar += "INDV_AUTORFAMILIA,";
				sql_guardar += "INDV_GENERO,";
				sql_guardar += "INDV_AUTORGENERO,";
				sql_guardar += "INDV_ESTADOEPITETO,";
				sql_guardar += "INDV_EPITETO,";
				sql_guardar += "INDV_MORFOESPECIE,";
				sql_guardar += "INDV_AUTORESPECIE,";
				sql_guardar += "INDV_HABITO,";
				sql_guardar += "INDV_INCLUIR,";
				sql_guardar += "INDV_FID,";
				sql_guardar += "INDV_ACTUALIZACION,";
				sql_guardar += "INDV_TAYO_DAP1,";
				sql_guardar += "INDV_TAYO_DAP2,";
				sql_guardar += "INDV_TAYO_ALTURADAP,";
				sql_guardar += "INDV_TAYO_ALTURA,";
				sql_guardar += "INDV_TAYO_ALTURATOTAL,";
				sql_guardar += "INDV_TAYO_FORMAFUSTE,";
				sql_guardar += "INDV_TAYO_DANIO,";
				sql_guardar += "INDV_TAYO_OBSERVACIONES,";
				sql_guardar += "INDV_CREADOR,";
				//sql_guardar += "INDV_BIOMASA_AEREA,";
				//sql_guardar += "INDV_CARBONO,";
				sql_guardar += "INDV_ARCH_CONSECUTIVO";
				//if (!INDV_ARCHIVO_FOTOS.equals("NULL")) sql_guardar += ",INDV_ARCHIVO_FOTOS";
				sql_guardar += ") ";
				sql_guardar += " VALUES ";
				sql_guardar += "(";
				sql_guardar += "" + INDV_CONSECUTIVO+ ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_PRCL_CONSECUTIVO, "NULL") + ",";
				sql_guardar += "" + INDV_ESARBOLREFERENCIA + ",";
				sql_guardar += "" + INDV_CARDINALIDAD + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_NUMERO_COLECTOR, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_CANTIDAD_EJEMPLARES, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_ID_IMPORTACION, "NULL") + ",";
				sql_guardar += "'" + INDV_NUMERO_ARBOL.replace("'", "`") + "',";
				sql_guardar += "" + INDV_DISTANCIA + ",";
				sql_guardar += "" + INDV_AZIMUTH + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_LATITUD, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_LONGITUD, "NULL") + ",";
				sql_guardar += "'" + INDV_ESPECIE.replace("'", "`") + "',";
				sql_guardar += "" + INDV_SUBPARCELA + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TXCT_ID, "NULL") + ",";
				sql_guardar += "'" + INDV_OBSERVACIONES.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_ETIQUETA_COLECTA.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_FOTO_COLECTA.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_HOMOLOGACION.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_PRCL_PLOT, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_DENSIDAD, "NULL") + ",";
				sql_guardar += "'" + INDV_FAMILIA.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_AUTORFAMILIA.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_GENERO.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_AUTORGENERO.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_ESTADOEPITETO.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_EPITETO.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_MORFOESPECIE.replace("'", "`") + "',";
				sql_guardar += "'" + INDV_AUTORESPECIE.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_HABITO, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_INCLUIR, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_FID, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_ACTUALIZACION, "SYSDATE") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_DAP1, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_DAP2, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_ALTURADAP, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_ALTURA, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_ALTURATOTAL, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_FORMAFUSTE, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_TAYO_DANIO, "NULL") + ",";
				sql_guardar += "'" + INDV_TAYO_OBSERVACIONES.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(id_usuario, "NULL") + ",";
				//sql_guardar += "" + Auxiliar.nzVacio(BA, "NULL") + "";
				//sql_guardar += "" + Auxiliar.nzVacio(CO2, "NULL") + "";
				sql_guardar += "" + Auxiliar.nzVacio(INDV_ARCH_CONSECUTIVO, "NULL") + "";
				//if (!INDV_ARCHIVO_FOTOS.equals("NULL")) sql_guardar += ",'" + INDV_ARCHIVO_FOTOS + "'";
				sql_guardar += ")";
			}
				
			if (update) {
				id_creador = dbREDD.obtenerDato("SELECT INDV_CREADOR FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
				if (id_usuario.equals(id_creador)) {
					if (sec.tienePermiso(id_usuario, "225") || sec.tienePermiso(id_usuario, "226")) {
						privilegio = true;
					}
				}
				else {
					if (sec.tienePermiso(id_usuario, "226")) {
						privilegio = true;
					}
				}
			}
			else {
				if (sec.tienePermiso(id_usuario, "13")) {
					privilegio = true;
				}
			}

			if (!privilegio) {
				dbREDD.desconectarse();
	        	return "0-=-" + Auxiliar.mensaje("advertencia", "No tiene privilegio para realizar esta operación. Por favor, contacte al administrador.", usuario, metodo);
			}

			
			try {
		        try {
		        	dbREDD.establecerAutoCometer(false);
		        }
		        catch (Exception e) {
					dbREDD.desconectarse();
		        	return "0-=-" + Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
		        }

		        boolean ok_guardar = dbREDD.ejecutarSQL(sql_guardar + "");
				if (!ok_guardar) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el individuo:" + dbREDD.ultimoError, usuario, metodo);									
				}
		        
		        boolean ok_geometria = true;

		        /*
		        if (ok_guardar) {
		        	
		        	
			        String sql_geometria = "UPDATE RED_INDIVIDUO";
					sql_geometria += " SET INDV_GEO=SDO_GEOMETRY( ";
					sql_geometria += " 2001, ";
					sql_geometria += " 4326, ";
					sql_geometria += " SDO_POINT_TYPE("+INDV_LONGITUD+", "+INDV_LATITUD+", NULL), ";
					sql_geometria += " NULL, NULL ";
					sql_geometria += " ) ";
					sql_geometria += " WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO;
					
					ok_geometria = dbREDD.ejecutarSQL(sql_geometria);
					if (!ok_geometria) {
						dbREDD.desconectarse();
						return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar la geometria del individuo:" + dbREDD.ultimoError, usuario, metodo);									
					}
					
		        	ok_geometria = true;
		        }
		        */
				
		        boolean ok_tallounico = true;
		        
		        if (ok_guardar) {
		        	if (Auxiliar.tieneAlgo(INDV_TAYO_DAP1) && Auxiliar.tieneAlgo(INDV_TAYO_DAP2)) {
		        		// DETERMINAR CUANTOS TALLOS TIENE EL INDIVIDUO
		        		String n_tallos = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "0");
		        		
		        		String sql_tallounico = "";
		        		
		        		if (n_tallos.equals("0")) {
		        			// SI NO TIENE TALLOS
		        			
			        		sql_tallounico = "INSERT INTO RED_TALLO (TAYO_INDV_CONSECUTIVO,TAYO_DAP1,TAYO_DAP2,TAYO_ALTURADAP,TAYO_ALTURA,TAYO_FORMAFUSTE,TAYO_DANIO,TAYO_OBSERVACIONES,TAYO_ALTURATOTAL) VALUES ("+Auxiliar.nzVacio(INDV_CONSECUTIVO, "NULL")+","+Auxiliar.nzVacio(INDV_TAYO_DAP1, "NULL")+","+Auxiliar.nzVacio(INDV_TAYO_DAP2, "NULL")+","+Auxiliar.nzVacio(INDV_TAYO_ALTURADAP, "NULL")+","+Auxiliar.nzVacio(INDV_TAYO_ALTURA, "NULL")+","+Auxiliar.nzVacio(INDV_TAYO_FORMAFUSTE, "NULL")+","+Auxiliar.nzVacio(INDV_TAYO_DANIO, "NULL")+",'"+INDV_TAYO_OBSERVACIONES+"',"+Auxiliar.nzVacio(INDV_TAYO_ALTURATOTAL, "NULL")+")";
		        		}
		        		else if (n_tallos.equals("1")) {
		        			// SI TIENE UN ÚNICO TALLO
		        			String TAYO_CONSECUTIVO = dbREDD.obtenerDato("SELECT TAYO_CONSECUTIVO FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		        			
		        			if (Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
		        				sql_tallounico = "UPDATE RED_TALLO SET TAYO_DAP1="+Auxiliar.nzVacio(INDV_TAYO_DAP1, "NULL")+",TAYO_DAP2="+Auxiliar.nzVacio(INDV_TAYO_DAP2, "NULL")+",TAYO_ALTURADAP="+Auxiliar.nzVacio(INDV_TAYO_ALTURADAP, "NULL")+",TAYO_ALTURA="+Auxiliar.nzVacio(INDV_TAYO_ALTURA, "NULL")+",TAYO_FORMAFUSTE="+Auxiliar.nzVacio(INDV_TAYO_FORMAFUSTE, "NULL")+",TAYO_DANIO="+Auxiliar.nzVacio(INDV_TAYO_DANIO, "NULL")+",TAYO_OBSERVACIONES='"+INDV_TAYO_OBSERVACIONES+"',TAYO_ALTURATOTAL="+Auxiliar.nzVacio(INDV_TAYO_ALTURATOTAL, "NULL");
		        			}
		        		}
		        		
		        		if (Auxiliar.tieneAlgo(sql_tallounico)) {
		        			// GUARDAR TALLO
		        			
		    				try {
		    					ok_tallounico = dbREDD.ejecutarSQL(sql_tallounico);
		    					Auxiliar.mensaje("nota", dbREDD.ultimoError, id_usuario, metodo);
		    				}
		    				catch (Exception e) {
		    					dbREDD.desconectarse();
		    					return "0-=-" + Auxiliar.mensaje("error", "Se produjo una excepción al intentar guardar el único tallo del individuo:" + dbREDD.ultimoError, usuario, metodo);									
		    				}
		        		}
		        	}
		        }
	
		        
		        boolean ok_biomasa = true;
		        
		    	// CALCULAR BIOMASA Y CARBONO

		        // averiguar si tiene tallos
		        
		        String n_tallos = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "0");
		        
		        
		    	if (Auxiliar.tieneAlgo(INDV_PRCL_CONSECUTIVO) && !n_tallos.equals("0")) {
		    		
			    	BMC bmc = null;
			    	String [] a_bmc = null;
					try {
			    		double DAPromedio = 0.0;
						//bmc = new BMC(this.RED_username, this.RED_password, this.RED_host, this.RED_port, this.RED_sid);
						bmc = new BMC();
				    	String EQAL_ID = dbREDD.obtenerDato("SELECT PRCL_EQ FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+INDV_PRCL_CONSECUTIVO, "");
			    		//DAPromedio = dbREDD.obtenerDato("SELECT SUM((TAYO_DAP1 + TAYO_DAP2)/2) AS DAP FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, ""); 
				    	if (!Auxiliar.tieneAlgo(INDV_TAYO_DAP1)) {
				    		INDV_TAYO_DAP1 = dbREDD.obtenerDato("SELECT SUM(TAYO_DAP1/"+n_tallos+") AS DAP1 FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "0"); 
				    	}
				    	if (!Auxiliar.tieneAlgo(INDV_TAYO_DAP2)) {
				    		INDV_TAYO_DAP2 = dbREDD.obtenerDato("SELECT SUM(TAYO_DAP2/"+n_tallos+") AS DAP2 FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "0"); 
				    	}
				    	if (Auxiliar.tieneAlgo(INDV_TAYO_DAP1) && Auxiliar.tieneAlgo(INDV_TAYO_DAP2)) {
				    		DAPromedio = (Double.parseDouble(INDV_TAYO_DAP1) + Double.parseDouble(INDV_TAYO_DAP2)) / 2;
				    	}
				    	if (Auxiliar.tieneAlgo(EQAL_ID) && Auxiliar.tieneAlgo(INDV_TAYO_DAP1) && Auxiliar.tieneAlgo(INDV_TAYO_DAP2)) {
				    		a_bmc = bmc.bmcIndividuoBD(INDV_CONSECUTIVO, "", "", EQAL_ID, String.valueOf(DAPromedio), INDV_DENSIDAD, "R");
				    	}
					} catch (Exception e) {
						aviso = Auxiliar.mensaje("error", "No se pudo calcular la biomasa del individuo " + INDV_CONSECUTIVO + ":" + e.toString(), usuario, metodo);
						observaciones += aviso;
						e.printStackTrace();
					}
					
					if (a_bmc != null) {
						if (a_bmc.length == 3) {
							BA = a_bmc[0];
							CO2 = a_bmc[1];
							mensajeBMC = a_bmc[2];
							
							aviso = Auxiliar.mensaje("confirmacion", "Biomasa y carbono del individuo " + INDV_CONSECUTIVO + ": " + BA + ", CO2:" + CO2 + " Mensaje: " + mensajeBMC, usuario, metodo);
							observaciones += aviso;
							Auxiliar.mensaje("confirmacion", "Biomasa y carbono del individuo " + INDV_CONSECUTIVO + ": " + BA + ", CO2:" + CO2 + " Mensaje: " + mensajeBMC, usuario, metodo);
							
							dbREDD.ejecutarSQL("UPDATE RED_INDIVIDUO SET INDV_BIOMASA_AEREA=" + BA + ", INDV_CARBONO="+CO2+" WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO);
						}
					}
					else {
						ok_biomasa = false;
					}
		    	}
				
		        
				try {
					if (ok_guardar && ok_geometria && ok_tallounico && ok_biomasa) {
						
						if (update) {
							id_creador = dbREDD.obtenerDato("SELECT INDV_CREADOR FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
							if (id_usuario.equals(id_creador)) {
								sec.registrarTransaccion(request, 225, INDV_CONSECUTIVO, sql_guardar, ok_operacion);
							}
							else {
								sec.registrarTransaccion(request, 226, INDV_CONSECUTIVO, sql_guardar, ok_operacion);
							}
						}
						else {
							if (!importacion) {
								id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+INDV_PRCL_CONSECUTIVO, "");
								if (id_usuario.equals(id_creador)) {
									sec.registrarTransaccion(request, 201, INDV_CONSECUTIVO, sql_guardar, ok_operacion);
								}
								else {
									sec.registrarTransaccion(request, 184, INDV_CONSECUTIVO, sql_guardar, ok_operacion);
								}
							}
						}

						dbREDD.cometerTransaccion();
						resultado = INDV_CONSECUTIVO + "-=-" + Auxiliar.mensaje("confirmacion", "Individuo "+INDV_CONSECUTIVO+" guardado.", usuario, metodo);
						ok_operacion = true;
					}
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo una excepción al intentar guardar el individuo:" + dbREDD.ultimoError, usuario, metodo);									
				}
				
		        try {
		        	dbREDD.establecerAutoCometer(true);
		        }
		        catch (Exception e) {
					dbREDD.desconectarse();
		        	return "0-=-" + Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
		        }
		       
			}
			catch (Exception e) {
				dbREDD.deshacerTransaccion();
				return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el individuo:" + dbREDD.ultimoError, usuario, metodo);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			dbREDD.desconectarse();
			return "0-=-" + "Problema al guardar el Individuo: " + e.toString();
		}
		
		dbREDD.desconectarse();

		return resultado;
	}
	
	/**
	 * Elimina un individo.
	 * 
	 * @param INDV_CONSECUTIVO
	 * @return String r con el resultado
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public String eliminar(	String INDV_CONSECUTIVO, HttpServletRequest request) 
	throws Exception {
		String metodo = yo + "eliminar";

		Sec sec = new Sec();
		
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();

		BD dbREDD = new BD();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");

		String resultado = "";
		String novedades = "";
		String update_esadmin = "";
		
		boolean ok_eliminar = false;
		String id_creador = "";
		boolean privilegio = false;

		String pe = "";
		
		//try { pe = msj.getString("General.Por_favor_especifique", idioma, "Por favor especifique" + " "; }
		pe = Auxiliar.traducir("General.Por_favor_especifique", idioma, "Por favor especifique");
		
		
		// VERIFICAR PARAMETROS
		
		t = Auxiliar.traducir("INDV_CONSECUTIVO", idioma, "el código del individuo" + " ");
		if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			dbREDD.desconectarse();
			return "0-=-" + pe + " " + t; 
		}
		
		
		// REALIZAR OPERACION
		
		String sql_guardar = "";
		
		try 
		{
			sql_guardar = "DELETE FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO;
			
			id_creador = dbREDD.obtenerDato("SELECT INDV_CREADOR FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
			if (id_usuario.equals(id_creador)) {
				if (sec.tienePermiso(id_usuario, "229") || sec.tienePermiso(id_usuario, "230")) {
					privilegio = true;
				}
			}
			else {
				if (sec.tienePermiso(id_usuario, "230")) {
					privilegio = true;
				}
			}

			if (!privilegio) {
				dbREDD.desconectarse();
	        	return "0-=-" + Auxiliar.mensaje("advertencia", "No tiene privilegio para realizar esta operación. Por favor, contacte al administrador.", usuario, metodo);
			}


			try {
				try {
					dbREDD.establecerAutoCometer(false);
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
				}

				try {
					if (!dbREDD.ejecutarSQL(sql_guardar)) {
						dbREDD.desconectarse();
						return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar eliminar el individuo [" + sql_guardar + "]:" + dbREDD.ultimoError, usuario, metodo);									
					}
					else {
						ok_eliminar = true;
						
						if (id_usuario.equals(id_creador)) {
							sec.registrarTransaccion(request, 229, INDV_CONSECUTIVO, sql_guardar, ok_eliminar);
						}
						else {
							sec.registrarTransaccion(request, 230, INDV_CONSECUTIVO, sql_guardar, ok_eliminar);
						}

						dbREDD.cometerTransaccion();
						resultado = "1-=-" + Auxiliar.mensaje("confirmacion", "Individuo eliminado.", usuario, metodo);
					}
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo una excepción al intentar eliminar el individuo [" + sql_guardar + "]:" + dbREDD.ultimoError, usuario, metodo);									
				}
				
				try {
					dbREDD.establecerAutoCometer(true);
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
				}
				
			}
			catch (Exception e) {
				dbREDD.deshacerTransaccion();
				return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar eliminar el individuo [" + sql_guardar + "]:" + dbREDD.ultimoError, usuario, metodo);
			}
		} 
		catch (Exception e) {
			resultado = "0-=-" + "Problema al eliminar el Individuo: " + e.toString();
			e.printStackTrace();
		}
		
		if (novedades.length() > 0) resultado += "Novedades: " + novedades;
		
		dbREDD.desconectarse();

		return resultado;
	}
	
	
	/**
	 * Método opcionesIndividuos para retornar un listado de opciones de individuos encontrados
	 * según una consulta sql filtrada por los parámetros:
	 * 
	 * @param f_consecutivo
	 * @param f_placa
	 * @param f_nombre_comun
	 * @param f_especie
	 * @return String opciones separando identificador de etiqueta por @@
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String opcionesIndividuos(String f_consecutivo, String f_placa, String f_nombre_comun, String f_especie, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "opcionesIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		String w_consecutivo = "";
		String w_placa = "";
		String w_nombre_comun = "";
		String w_especie = "";

		if (Auxiliar.tieneAlgo(f_consecutivo))
		{
			w_consecutivo = " AND INDV_ID_IMPORTACION IN ("+f_consecutivo+") ";
		}
		
		if (Auxiliar.tieneAlgo(f_placa))
		{
			w_placa = " AND UPPER(INDV_PLACA) LIKE '%"+f_placa.toUpperCase()+"%' ";
		}

		if (Auxiliar.tieneAlgo(f_nombre_comun))
		{
			w_nombre_comun = " AND UPPER(INDV_NOMBRECOMUN) LIKE '%"+f_nombre_comun.toUpperCase()+"%' ";
		}
		
		if (Auxiliar.tieneAlgo(f_especie))
		{
			w_especie = " AND UPPER(INDV_ESPECIE) LIKE '%"+f_especie.toUpperCase()+"%' ";
		}

		
		// CREACIÓN DE LA CONSULTA SQL
		
		String sql = "SELECT * FROM RED_INDIVIDUO ";
		sql += "WHERE 1=1 ";
		sql += w_consecutivo;
		sql += w_placa;
		sql += w_nombre_comun;
		sql += w_especie;
		sql += " ORDER BY INDV_ID_IMPORTACION ";

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null) {
				String db_consecutivo = "";
				String db_placa = "";
				String db_nombre_comun = "";
				String db_especie = "";
				String db_prcl_consecutivo = "";
				String db_fechatomadatos = "";
				
				String opciones = "";

				while (rset.next())
				{
					db_consecutivo = rset.getString("INDV_ID_IMPORTACION");
					db_placa = rset.getString("INDV_PLACA");
					db_nombre_comun = rset.getString("INDV_NOMBRECOMUN");
					db_especie = rset.getString("INDV_ESPECIE");
					db_prcl_consecutivo = rset.getString("INDV_PRCL_CONSECUTIVO");
					db_fechatomadatos = rset.getString("INDV_FECHATOMADATOS");

					opciones += db_consecutivo + "@@" 
						+ "Id: "+ db_consecutivo 
						+ " Nr:" + db_placa 
						+ "--" + db_nombre_comun 
						+ " (" + db_especie + ")" 
						+ " Parc.:" + db_prcl_consecutivo 
						+ " Fecha:" + db_fechatomadatos
						+ "\n"; 
										
				}
				
				rset.close();
				
				r=opciones;
			}
			else {
				r = "@@El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
				Auxiliar.mensaje("advertencia", r, usuario, metodo);
			}
		} catch (SQLException e) {
			r = "@@Excepción de SQL ["+sql+"]: " + e.toString();
			Auxiliar.mensaje("error", r, usuario, metodo);
		} catch (Exception e) {
			r = "@@Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql;
			Auxiliar.mensaje("error", r, usuario, metodo);
		}
		
		if (r.equals(""))
		{
			r = "@@No se encontraron resultados para sql:" + sql;
			Auxiliar.mensaje("nota", r, usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Método que recibe la ruta de un archivo comprimido, lo descomprime y guarda las imagenes
	 * extraidas en la carpeta de imagenes del individuo con el consecutivo especificado.
	 * 
	 * @param ruta_archivo
	 * @param consecutivo
	 * @return r String con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String importarImagenes(String ruta_archivo, String indv_id_importacion, HttpSession session)
	throws ClassNotFoundException, Exception
	{
		String metodo = yo + "importarImagenes";
		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String r = "";
		
		// DESCOMPRIMIR ARCHIVO EN LA CARPETA DEL INDIVIDUO
		
		//String commando_descompresion = "7za e "+ruta_archivo+" -y -o"+getServletContext().getRealPath("") + File.separator + "imagenes_individuos/" + indv_id_importacion;
		String carpeta_imagenes_individuos = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_individuos'", "");
		if (!Auxiliar.tieneAlgo(carpeta_imagenes_individuos)) {
			carpeta_imagenes_individuos = getServletContext().getRealPath("") + File.separator + "imagenes_individuos";
		}
		
		String commando_descompresion = "7za e "+ruta_archivo+" -y -o"+carpeta_imagenes_individuos + "/" + indv_id_importacion;
		
		try {
			String str_resultado_descompresion = Auxiliar.commander(commando_descompresion, commando_descompresion, session);
	
			String[] a_resultado_descompresion = str_resultado_descompresion.split("-=-");
			
			if (a_resultado_descompresion[0] != "0")
			{
				t = Auxiliar.traducir(yo+"Imagenes_Extraidas", idioma, "Imágenes extraidas en la carpeta de imágenes del individuo identificado con consecutivo " + " ");
				r = Auxiliar.mensaje("confirmacion", t + indv_id_importacion, usuario, metodo);
				r += a_resultado_descompresion[1];
			}
			else {
				r = Auxiliar.mensaje("error", "No se pudieron extraer las imágenes en la carpeta de imágenes del individuo identificado con consecutivo " + indv_id_importacion + ". Comando de descompresión:" + commando_descompresion, usuario, metodo);
				r += a_resultado_descompresion[1];
			}
		}
		catch (Exception e) {
			r = Auxiliar.mensaje("error", "Error al intentar extraer las imágenes: " + e.toString() + ". Comando de descompresión:" + commando_descompresion, usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return r + Auxiliar.mensaje("nota", "Comando de descompresión:" + commando_descompresion, usuario, metodo);
	}
} //1888
