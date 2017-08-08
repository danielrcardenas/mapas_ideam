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
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleTypes;

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
import com.vividsolutions.jts.io.ParseException;


/** 
 * Clase Parcela
 * Permite administrar las Parcelas.
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 */
@SuppressWarnings("serial")
public class Parcela extends HttpServlet {

	public static String yo = "Parcela.";
	public static String charset = "ISO-8859-1";
	public static String css = "css/estilos.css";
	
	private static final String EXPORT_PDF_DIRECTORY = "pdf";
    private static final String EXPORT_XML_DIRECTORY = "xml";
    private static final String UPLOAD_DIRECTORY = "imagenes_parcelas";

	String RED_username = "";
	String RED_password = "";
	String RED_host = "";
	String RED_port = "";
	String RED_sid = "";
	
	String encriptar_usuario = "";
	String llave_encripcion = "";
	
	/**
	 * Método mudo para inicializar la clase
	 * A partir de la variable config se obtienen los parámetros de conexión a las bases de datos.
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
	public Parcela() {
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
		String retorno = "";
		String target = "";
		String ultimo_error = "";

		String f_PRCL_CONSECUTIVO = ""; 
		String f_PRCL_ID_IMPORTACION = ""; 
		String f_PRCL_ID_UPM = "";
		String f_PRCL_MEDIOACCESO_POBLADO = "";
		String f_PRCL_DISTANCIA_POBLADO = "";
		String f_PRCL_TPOBLADO_H = "";
		String f_PRCL_TPOBLADO_M = "";
		String f_PRCL_MEDIOACCESO_CAMPAMENTO = "";
		String f_PRCL_DISTANCIA_CAMPAMENTO = "";
		String f_PRCL_TCAMPAMENTO_H = "";
		String f_PRCL_TCAMPAMENTO_M = "";
		String f_PRCL_MEDIOACCESO_JALON = "";
		String f_PRCL_DISTANCIA_JALON = "";
		String f_PRCL_TJALON_H = "";
		String f_PRCL_TJALON_M = "";
		String f_PRCL_DISTANCIA_CAMPAMENTOS = "";
		String f_PRCL_LATITUD = "";
		String f_PRCL_LONGITUD = "";
		String f_PRCL_ALTITUD = "";
		String f_PRCL_NOMBRE = "";
		String f_PRCL_USR_DILIGENCIA_F1 = "";
		String f_PRCL_USR_DILIGENCIA_F2 = "";
		String f_PRCL_FECHAINI_APROXIMACION = "";
		String f_PRCL_FECHAFIN_APROXIMACION = "";
		String f_PRCL_FECHAINI_LOCALIZACION = "";
		String f_PRCL_FECHAFIN_LOCALIZACION = "";
		String f_PRCL_DESCRIPCION = "";
		String f_PRCL_OBSERVACIONES = "";
		String f_PRCL_TRACKLOG_CAMPAMENTO = "";
		String f_PRCL_TRACKLOG_PARCELA = "";
		String f_PRCL_CONS_PAIS = "";
		String f_PRCL_DEPARTAMENTO = "";
		String f_PRCL_MUNICIPIO = "";
		String f_PRCL_SPF1_DILIGENCIA = "";
		String f_PRCL_SPF1_FECHAINI = "";
		String f_PRCL_SPF1_FECHAFIN = "";
		String f_PRCL_SPF1_POSIBLE = "";
		String f_PRCL_SPF1_JUSTIFICACION_NO = "";
		String f_PRCL_SPF1_OBS_FUSTALES = "";
		String f_PRCL_SPF1_OBS_LATIZALES = "";
		String f_PRCL_SPF1_OBS_BRINZALES = "";
		String f_PRCL_SPF2_DILIGENCIA = "";
		String f_PRCL_SPF2_FECHAINI = "";
		String f_PRCL_SPF2_FECHAFIN = "";
		String f_PRCL_SPF2_POSIBLE = "";
		String f_PRCL_SPF2_JUSTIFICACION_NO = "";
		String f_PRCL_SPF2_OBS_FUSTALES = "";
		String f_PRCL_SPF2_OBS_LATIZALES = "";
		String f_PRCL_SPF2_OBS_BRINZALES = "";
		String f_PRCL_SPF3_DILIGENCIA = "";
		String f_PRCL_SPF3_FECHAINI = "";
		String f_PRCL_SPF3_FECHAFIN = "";
		String f_PRCL_SPF3_POSIBLE = "";
		String f_PRCL_SPF3_JUSTIFICACION_NO = "";
		String f_PRCL_SPF3_OBS_FUSTALES = "";
		String f_PRCL_SPF3_OBS_LATIZALES = "";
		String f_PRCL_SPF3_OBS_BRINZALES = "";
		String f_PRCL_SPF4_DILIGENCIA = "";
		String f_PRCL_SPF4_FECHAINI = "";
		String f_PRCL_SPF4_FECHAFIN = "";
		String f_PRCL_SPF4_POSIBLE = "";
		String f_PRCL_SPF4_JUSTIFICACION_NO = "";
		String f_PRCL_SPF4_OBS_FUSTALES = "";
		String f_PRCL_SPF4_OBS_LATIZALES = "";
		String f_PRCL_SPF4_OBS_BRINZALES = "";
		String f_PRCL_SPF5_DILIGENCIA = "";
		String f_PRCL_SPF5_FECHAINI = "";
		String f_PRCL_SPF5_FECHAFIN = "";
		String f_PRCL_SPF5_POSIBLE = "";
		String f_PRCL_SPF5_JUSTIFICACION_NO = "";
		String f_PRCL_SPF5_OBS_FUSTALES = "";
		String f_PRCL_SPF5_OBS_LATIZALES = "";
		String f_PRCL_SPF5_OBS_BRINZALES = "";
		
		String f_PRCL_PLOT = "";;
		String f_PRCL_AREA = "";
		String f_PRCL_INCLUIR = "";;
		String f_PRCL_TEMPORALIDAD = "";
		String f_PRCL_PUBLICA = "";
		String f_PRCL_HAB = "";
		String f_PRCL_DAP = "";
		String f_PRCL_GPS = "";
		String f_PRCL_EQ = "";
		String f_PRCL_BA = "";
		String f_PRCL_BS = "";
		String f_PRCL_BT = "";
		String f_PRCL_AUTORCUSTODIOINFO = "";
		String f_PRCL_TIPOBOSQUE = "";

		// Declaración variables para valores de campos de las tablas de parcelas
		String db_PRCL_CONSECUTIVO = "";

		
	    // Instanciar auxiliar y archivo
	    //Auxiliar aux = new Auxiliar();
		Sec sec = new Sec();

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
				    		if (!Auxiliar.esEnteroMayorOIgualACero(usuario)) {
				    			String usuario_temp = usuario;
				    			usuario = "";
				    			usuario = Auxiliar.desencriptar(usuario_temp, this.llave_encripcion);
				    			session.setAttribute("usuario", usuario);
				    		}
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
			}
			else {
		    	if (sec.sesionTieneAtributo(session, "usuario")) {
		    		accion = null;
		    	}
			}
			
			if (Auxiliar.nzObjStr(session.getAttribute("login_fallido"), "1").equals("1")) {
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
			retorno = Auxiliar.mensajeImpersonal("advertencia", Auxiliar.traducir("SESION_VENCIDA", "es", "Credenciales erradas o Su sesión ha vencido"));
			try {
				sec.registrarTransaccion(request, 194, "", "", false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if (accion.equals("plantilla_parcelas"))
			{
				boolean ok = false;
				try {
					if (sec.tienePermiso(usuario, "43")) {
						ok = generarPlantillaParcelas(response, session);
						
						if (ok) {
							sec.registrarTransaccion(request, 193, "", "", true);
						}
						else {
							sec.registrarTransaccion(request, 193, "", "error", false);
						}
					}
					else {
						sec.registrarTransaccion(request, 193, "", "permisos", false);
					}
				} 
				catch (Exception e) {				
					try {
						sec.registrarTransaccion(request, 193, "", "excepcion:"+e.toString(), false);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			else if (accion.equals("visualizar_etiqueta"))
			{ 
				target = "/visualizar_etiqueta_parcela.jsp";
				response.setContentType("text/html; charset=UTF-8");
				try {
					retorno = exportarMetadatoParcela(Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), session, "etiqueta");
					
					sec.registrarTransaccion(request, 192, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "", true);

				} catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a etiquetaParcela(): " + e.toString(), usuario, metodo);
					
					try {
						sec.registrarTransaccion(request, 192, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), true);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					e.printStackTrace();
				}
				request.setAttribute("PRCL_CONSECUTIVO", Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""));
			}
			else if (accion.equals("busqueda_parcelas"))
			{ 
				target = "/busqueda_parcelas.jsp";
				response.setContentType("text/html; charset=UTF-8");
				try {
					retorno = "";
					String opciones_pais = Auxiliar.cargarOpciones("SELECT PAIS, NOMBRE FROM RED_PAIS_SHAPE ORDER BY NOMBRE", "PAIS", "NOMBRE", "", "", false, true, false);
					request.setAttribute("opciones_pais", opciones_pais);
					
					sec.registrarTransaccion(request, 191, "", "", true);

				} catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a cargarOpciones(): " + e.toString(), usuario, metodo);
					try {
						sec.registrarTransaccion(request, 191, "", "excepcion:"+e.toString(), false);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					e.printStackTrace();
				}
			}
			else if (accion.equals("encontrar"))
			{
				target = "/ajax_resultados.jsp";
				response.setContentType("text/html; charset=UTF-8");
				try {
					String resultado = listarRegistros(
							Auxiliar.nz(request.getParameter("PRCL_NOMBRE"), ""), 
							Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), 
							Auxiliar.nz(request.getParameter("PRCL_PLOT"), ""), 
							Auxiliar.nz(request.getParameter("PRCL_CONS_PAIS"), ""), 
							Auxiliar.nz(request.getParameter("departamentos_seleccionados"), ""),
							Auxiliar.nz(request.getParameter("municipios_seleccionados"), ""),
							Auxiliar.nz(request.getParameter("PRCL_FECHAINI_APROXIMACION"), ""), 
							session
							);
					
					String [] a_resultado = resultado.split("-=-");
					request.setAttribute("n", a_resultado[0]);
					retorno = a_resultado[1];
					
					request.setAttribute("PRCL_NOMBRE", Auxiliar.nz(request.getParameter("PRCL_NOMBRE"), ""));
					request.setAttribute("PRCL_PLOT", Auxiliar.nz(request.getParameter("PRCL_PLOT"), "")); 
					request.setAttribute("PRCL_CONSECUTIVO", Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "")); 
					request.setAttribute("PRCL_CONS_PAIS", Auxiliar.nz(request.getParameter("PRCL_CONS_PAIS"), ""));
					request.setAttribute("departamentos_seleccionados", Auxiliar.nz(request.getParameter("departamentos_seleccionados"), "")); 
					request.setAttribute("municipios_seleccionados", Auxiliar.nz(request.getParameter("municipios_seleccionados"), ""));
					request.setAttribute("PRCL_FECHAINI_APROXIMACION", Auxiliar.nz(request.getParameter("PRCL_FECHAINI_APROXIMACION"), ""));
					
					sec.registrarTransaccion(request, 11, "", "", true);

				} catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a listarRegistros(): " + e.toString(), usuario, metodo);
					
					try {
						sec.registrarTransaccion(request, 11, "", "excepcion:"+e.toString(), false);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					e.printStackTrace();
				}
			}
			else {
			
				if (sec.sesionVigente(session)) {
					
					if (accion.equals("visualizar_metadato"))
					{
						target = "/visualizar_metadato.jsp";
						response.setContentType("text/html; charset=UTF-8");
						try {
							retorno = exportarMetadatoParcela(Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), session, "html");
							
							sec.registrarTransaccion(request, 104, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "", true);

						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a exportarMetadatoParcela(): " + e.toString(), usuario, metodo);

							try {
								sec.registrarTransaccion(request, 104, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}

							e.printStackTrace();
						}
						request.setAttribute("PRCL_CONSECUTIVO", Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""));
						
					}
					else if (accion.equals("exportar_pdf"))
					{
						try {
							if (sec.tienePermiso(usuario, "190")) {
								if (usuarioAceptoLicencia) {
									exportarParcelaPDF(Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), false, session, false, response);
									sec.registrarTransaccion(request, 190, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "", true);
									return;
								}
								else {
									sec.registrarTransaccion(request, 190, "", "Sin licencia para INVENTARIOS FORESTALES", false);
									retorno = Auxiliar.mensaje("advertencia", "Debe aceptar la licencia de INVENTARIOS FORESTALES para poder realizar esta acción.", usuario, metodo);
								}								
							}
							else {
								sec.registrarTransaccion(request, 190, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
							}
						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a exportarParcelaPDF(): " + e.toString(), usuario, metodo);
							try {
								sec.registrarTransaccion(request, 190, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					else if (accion.equals("detalle_parcela"))
					{
						target = "/detalle_parcela.jsp";
						response.setContentType("text/html; charset=UTF-8");
						try {
							if (sec.tienePermiso(usuario, "15")) {
								
								request = establecerAtributos(request, session, "",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										""
										);
								retorno = "";
							}
							else {
								retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
								sec.registrarTransaccion(request, 15, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
							}
						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a cargarRegistro(): " + e.toString(), usuario, metodo);
							try {
								sec.registrarTransaccion(request, 15, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					else if (accion.equals("exportar_parcelas"))
					{
						boolean ok = false;
						try {							
							if (sec.tienePermiso(usuario,  "20")) { 
								if (usuarioAceptoLicencia) {
									ok = exportarParcelasExcel(
											Auxiliar.nz(request.getParameter("PRCL_NOMBRE"), ""), 
											Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), 
											Auxiliar.nz(request.getParameter("PRCL_CONS_PAIS"), ""), 
											Auxiliar.nz(request.getParameter("departamentos_seleccionados"), ""),
											Auxiliar.nz(request.getParameter("municipios_seleccionados"), ""),
											Auxiliar.nz(request.getParameter("PRCL_FECHAINI_APROXIMACION"), ""),
											response, 
											session
											);
									if (ok) {
										sec.registrarTransaccion(request, 20, "", "", true);
									}
									else {
										sec.registrarTransaccion(request, 20, "", "error", false);
									}
								}
								else {
									sec.registrarTransaccion(request, 20, "", "Sin licencia para INVENTARIOS FORESTALES", false);									
									retorno = Auxiliar.mensaje("advertencia", "Debe aceptar la licencia de INVENTARIOS FORESTALES para poder realizar esta acción.", usuario, metodo);
								}								
							}
							else {
								ok = false;
								ultimo_error = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
								sec.registrarTransaccion(request, 20, "", "permisos", false);
							}
						} catch (Exception e) {				
							try {
								sec.registrarTransaccion(request, 20, "", "excepcion:"+e.toString(), false);
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						
						
						if (!ok) {
							retorno = ultimo_error;
							
							try {
								request = establecerAtributos(request, session, "",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										""
										);
							}
							catch (Exception e) {
								
							}
							
							target = "/detalle_parcela.jsp";
						}
						
						
					}
					else
					{
						f_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""); 
						f_PRCL_ID_IMPORTACION = Auxiliar.nz(request.getParameter("PRCL_ID_IMPORTACION"), ""); 
						f_PRCL_ID_UPM = Auxiliar.nz(request.getParameter("PRCL_ID_UPM"), "");
						f_PRCL_MEDIOACCESO_POBLADO = Auxiliar.nz(request.getParameter("PRCL_MEDIOACCESO_POBLADO"), "");
						f_PRCL_DISTANCIA_POBLADO = Auxiliar.nz(request.getParameter("PRCL_DISTANCIA_POBLADO"), "");
						f_PRCL_TPOBLADO_H = Auxiliar.nz(request.getParameter("PRCL_TPOBLADO_H"), "");
						f_PRCL_TPOBLADO_M = Auxiliar.nz(request.getParameter("PRCL_TPOBLADO_M"), "");
						f_PRCL_MEDIOACCESO_CAMPAMENTO = Auxiliar.nz(request.getParameter("PRCL_MEDIOACCESO_CAMPAMENTO"), "");
						f_PRCL_DISTANCIA_CAMPAMENTO = Auxiliar.nz(request.getParameter("PRCL_DISTANCIA_CAMPAMENTO"), "");
						f_PRCL_TCAMPAMENTO_H = Auxiliar.nz(request.getParameter("PRCL_TCAMPAMENTO_H"), "");
						f_PRCL_TCAMPAMENTO_M = Auxiliar.nz(request.getParameter("PRCL_TCAMPAMENTO_M"), "");
						f_PRCL_MEDIOACCESO_JALON = Auxiliar.nz(request.getParameter("PRCL_MEDIOACCESO_JALON"), "");
						f_PRCL_DISTANCIA_JALON = Auxiliar.nz(request.getParameter("PRCL_DISTANCIA_JALON"), "");
						f_PRCL_TJALON_H = Auxiliar.nz(request.getParameter("PRCL_TJALON_H"), "");
						f_PRCL_TJALON_M = Auxiliar.nz(request.getParameter("PRCL_TJALON_M"), "");
						f_PRCL_DISTANCIA_CAMPAMENTOS = Auxiliar.nz(request.getParameter("PRCL_DISTANCIA_CAMPAMENTOS"), "");
						f_PRCL_LATITUD = Auxiliar.nz(request.getParameter("PRCL_LATITUD"), "");
						f_PRCL_LONGITUD = Auxiliar.nz(request.getParameter("PRCL_LONGITUD"), "");
						f_PRCL_ALTITUD = Auxiliar.nz(request.getParameter("PRCL_ALTITUD"), "");
						f_PRCL_NOMBRE = Auxiliar.nz(request.getParameter("PRCL_NOMBRE"), "");
						f_PRCL_USR_DILIGENCIA_F1 = Auxiliar.nz(request.getParameter("PRCL_USR_DILIGENCIA_F1"), "");
						f_PRCL_USR_DILIGENCIA_F2 = Auxiliar.nz(request.getParameter("PRCL_USR_DILIGENCIA_F2"), "");
						f_PRCL_FECHAINI_APROXIMACION = Auxiliar.nz(request.getParameter("PRCL_FECHAINI_APROXIMACION"), "");
						f_PRCL_FECHAFIN_APROXIMACION = Auxiliar.nz(request.getParameter("PRCL_FECHAFIN_APROXIMACION"), "");
						f_PRCL_FECHAINI_LOCALIZACION = Auxiliar.nz(request.getParameter("PRCL_FECHAINI_LOCALIZACION"), "");
						f_PRCL_FECHAFIN_LOCALIZACION = Auxiliar.nz(request.getParameter("PRCL_FECHAFIN_LOCALIZACION"), "");
						f_PRCL_DESCRIPCION = Auxiliar.nz(request.getParameter("PRCL_DESCRIPCION"), "");
						f_PRCL_OBSERVACIONES = Auxiliar.nz(request.getParameter("PRCL_OBSERVACIONES"), "");
						f_PRCL_TRACKLOG_CAMPAMENTO = Auxiliar.nz(request.getParameter("PRCL_TRACKLOG_CAMPAMENTO"), "");
						f_PRCL_TRACKLOG_PARCELA = Auxiliar.nz(request.getParameter("PRCL_TRACKLOG_PARCELA"), "");
						f_PRCL_CONS_PAIS = Auxiliar.nz(request.getParameter("PRCL_CONS_PAIS"), "");
						f_PRCL_DEPARTAMENTO = Auxiliar.nz(request.getParameter("PRCL_DEPARTAMENTO"), "");
						f_PRCL_MUNICIPIO= Auxiliar.nz(request.getParameter("PRCL_MUNICIPIO"), "");
						f_PRCL_SPF1_DILIGENCIA = Auxiliar.nz(request.getParameter("PRCL_SPF1_DILIGENCIA"), "");
						f_PRCL_SPF1_FECHAINI = Auxiliar.nz(request.getParameter("PRCL_SPF1_FECHAINI"), "");
						f_PRCL_SPF1_FECHAFIN = Auxiliar.nz(request.getParameter("PRCL_SPF1_FECHAFIN"), "");
						f_PRCL_SPF1_POSIBLE = Auxiliar.nz(request.getParameter("PRCL_SPF1_POSIBLE"), "");
						f_PRCL_SPF1_JUSTIFICACION_NO = Auxiliar.nz(request.getParameter("PRCL_SPF1_JUSTIFICACION_NO"), "");
						f_PRCL_SPF1_OBS_FUSTALES = Auxiliar.nz(request.getParameter("PRCL_SPF1_OBS_FUSTALES"), "");
						f_PRCL_SPF1_OBS_LATIZALES = Auxiliar.nz(request.getParameter("PRCL_SPF1_OBS_LATIZALES"), "");
						f_PRCL_SPF1_OBS_BRINZALES = Auxiliar.nz(request.getParameter("PRCL_SPF1_OBS_BRINZALES"), "");
						f_PRCL_SPF2_DILIGENCIA = Auxiliar.nz(request.getParameter("PRCL_SPF2_DILIGENCIA"), "");
						f_PRCL_SPF2_FECHAINI = Auxiliar.nz(request.getParameter("PRCL_SPF2_FECHAINI"), "");
						f_PRCL_SPF2_FECHAFIN = Auxiliar.nz(request.getParameter("PRCL_SPF2_FECHAFIN"), "");
						f_PRCL_SPF2_POSIBLE = Auxiliar.nz(request.getParameter("PRCL_SPF2_POSIBLE"), "");
						f_PRCL_SPF2_JUSTIFICACION_NO = Auxiliar.nz(request.getParameter("PRCL_SPF2_JUSTIFICACION_NO"), "");
						f_PRCL_SPF2_OBS_FUSTALES = Auxiliar.nz(request.getParameter("PRCL_SPF2_OBS_FUSTALES"), "");
						f_PRCL_SPF2_OBS_LATIZALES = Auxiliar.nz(request.getParameter("PRCL_SPF2_OBS_LATIZALES"), "");
						f_PRCL_SPF2_OBS_BRINZALES = Auxiliar.nz(request.getParameter("PRCL_SPF2_OBS_BRINZALES"), "");
						f_PRCL_SPF3_DILIGENCIA = Auxiliar.nz(request.getParameter("PRCL_SPF3_DILIGENCIA"), "");
						f_PRCL_SPF3_FECHAINI = Auxiliar.nz(request.getParameter("PRCL_SPF3_FECHAINI"), "");
						f_PRCL_SPF3_FECHAFIN = Auxiliar.nz(request.getParameter("PRCL_SPF3_FECHAFIN"), "");
						f_PRCL_SPF3_POSIBLE = Auxiliar.nz(request.getParameter("PRCL_SPF3_POSIBLE"), "");
						f_PRCL_SPF3_JUSTIFICACION_NO = Auxiliar.nz(request.getParameter("PRCL_SPF3_JUSTIFICACION_NO"), "");
						f_PRCL_SPF3_OBS_FUSTALES = Auxiliar.nz(request.getParameter("PRCL_SPF3_OBS_FUSTALES"), "");
						f_PRCL_SPF3_OBS_LATIZALES = Auxiliar.nz(request.getParameter("PRCL_SPF3_OBS_LATIZALES"), "");
						f_PRCL_SPF3_OBS_BRINZALES = Auxiliar.nz(request.getParameter("PRCL_SPF3_OBS_BRINZALES"), "");
						f_PRCL_SPF4_DILIGENCIA = Auxiliar.nz(request.getParameter("PRCL_SPF4_DILIGENCIA"), "");
						f_PRCL_SPF4_FECHAINI = Auxiliar.nz(request.getParameter("PRCL_SPF4_FECHAINI"), "");
						f_PRCL_SPF4_FECHAFIN = Auxiliar.nz(request.getParameter("PRCL_SPF4_FECHAFIN"), "");
						f_PRCL_SPF4_POSIBLE = Auxiliar.nz(request.getParameter("PRCL_SPF4_POSIBLE"), "");
						f_PRCL_SPF4_JUSTIFICACION_NO = Auxiliar.nz(request.getParameter("PRCL_SPF4_JUSTIFICACION_NO"), "");
						f_PRCL_SPF4_OBS_FUSTALES = Auxiliar.nz(request.getParameter("PRCL_SPF4_OBS_FUSTALES"), "");
						f_PRCL_SPF4_OBS_LATIZALES = Auxiliar.nz(request.getParameter("PRCL_SPF4_OBS_LATIZALES"), "");
						f_PRCL_SPF4_OBS_BRINZALES = Auxiliar.nz(request.getParameter("PRCL_SPF4_OBS_BRINZALES"), "");
						f_PRCL_SPF5_DILIGENCIA = Auxiliar.nz(request.getParameter("PRCL_SPF5_DILIGENCIA"), "");
						f_PRCL_SPF5_FECHAINI = Auxiliar.nz(request.getParameter("PRCL_SPF5_FECHAINI"), "");
						f_PRCL_SPF5_FECHAFIN = Auxiliar.nz(request.getParameter("PRCL_SPF5_FECHAFIN"), "");
						f_PRCL_SPF5_POSIBLE = Auxiliar.nz(request.getParameter("PRCL_SPF5_POSIBLE"), "");
						f_PRCL_SPF5_JUSTIFICACION_NO = Auxiliar.nz(request.getParameter("PRCL_SPF5_JUSTIFICACION_NO"), "");
						f_PRCL_SPF5_OBS_FUSTALES = Auxiliar.nz(request.getParameter("PRCL_SPF5_OBS_FUSTALES"), "");
						f_PRCL_SPF5_OBS_LATIZALES = Auxiliar.nz(request.getParameter("PRCL_SPF5_OBS_LATIZALES"), "");
						f_PRCL_SPF5_OBS_BRINZALES = Auxiliar.nz(request.getParameter("PRCL_SPF5_OBS_BRINZALES"), "");
						
						f_PRCL_PLOT = Auxiliar.nz(request.getParameter("PRCL_PLOT"), "");
						f_PRCL_AREA = Auxiliar.nz(request.getParameter("PRCL_AREA"), "");
						f_PRCL_INCLUIR = Auxiliar.nz(request.getParameter("PRCL_INCLUIR"), "");
						f_PRCL_TEMPORALIDAD = Auxiliar.nz(request.getParameter("PRCL_TEMPORALIDAD"), "");
						f_PRCL_PUBLICA = Auxiliar.nz(request.getParameter("PRCL_PUBLICA"), "");
						f_PRCL_HAB = Auxiliar.nz(request.getParameter("PRCL_HAB"), "");
						f_PRCL_DAP = Auxiliar.nz(request.getParameter("PRCL_DAP"), "");
						f_PRCL_GPS = Auxiliar.nz(request.getParameter("PRCL_GPS"), "");
						f_PRCL_EQ = Auxiliar.nz(request.getParameter("PRCL_EQ"), "");
						f_PRCL_BA = Auxiliar.nz(request.getParameter("PRCL_BA"), "");
						f_PRCL_BS = Auxiliar.nz(request.getParameter("PRCL_BS"), "");
						f_PRCL_BT = Auxiliar.nz(request.getParameter("PRCL_BT"), "");
						f_PRCL_AUTORCUSTODIOINFO = Auxiliar.nz(request.getParameter("PRCL_AUTORCUSTODIOINFO"), "");
						f_PRCL_TIPOBOSQUE = Auxiliar.nz(request.getParameter("PRCL_TIPOBOSQUE"), "");

						if (accion.equals("guardar"))
						{
							response.setContentType("text/html; charset=UTF-8");
							try {
								if (
										sec.tienePermiso(usuario,  "9") 
									||	sec.tienePermiso(usuario,  "12")
									||	sec.tienePermiso(usuario,  "13")
									) {												
						
									String resultado = guardar(
										f_PRCL_CONSECUTIVO, 
										f_PRCL_ID_IMPORTACION, 
										f_PRCL_ID_UPM,
										f_PRCL_CONS_PAIS, // PRCL_CONS_PAIS
										"", // PRCL_VEREDA
										"", // PRCL_CORREGIMIENTO
										"", // PRCL_INSPECCION_POLICIA
										"", // PRCL_CASERIO
										"", // PRCL_RANCHERIA
										"", // PRCL_FECHA_CAPTURA
										f_PRCL_MEDIOACCESO_POBLADO,
										f_PRCL_DISTANCIA_POBLADO,
										f_PRCL_TPOBLADO_H,
										f_PRCL_TPOBLADO_M,
										f_PRCL_MEDIOACCESO_CAMPAMENTO,
										f_PRCL_DISTANCIA_CAMPAMENTO,
										f_PRCL_TCAMPAMENTO_H,
										f_PRCL_TCAMPAMENTO_M,
										f_PRCL_MEDIOACCESO_JALON,
										f_PRCL_DISTANCIA_JALON,
										f_PRCL_TJALON_H,
										f_PRCL_TJALON_M,
										"", // PRCL_COORDENADAS
										f_PRCL_DISTANCIA_CAMPAMENTOS,
										f_PRCL_LATITUD,
										f_PRCL_LONGITUD,
										f_PRCL_ALTITUD,
										f_PRCL_NOMBRE,
										f_PRCL_USR_DILIGENCIA_F1,
										f_PRCL_USR_DILIGENCIA_F2,
										f_PRCL_FECHAINI_APROXIMACION,
										f_PRCL_FECHAFIN_APROXIMACION,
										f_PRCL_FECHAINI_LOCALIZACION,
										f_PRCL_FECHAFIN_LOCALIZACION,
										f_PRCL_DESCRIPCION,
										f_PRCL_OBSERVACIONES,
										f_PRCL_TRACKLOG_CAMPAMENTO,
										f_PRCL_TRACKLOG_PARCELA,
										f_PRCL_DEPARTAMENTO,
										f_PRCL_MUNICIPIO,
										f_PRCL_SPF1_DILIGENCIA,
										f_PRCL_SPF1_FECHAINI,
										f_PRCL_SPF1_FECHAFIN,
										f_PRCL_SPF1_POSIBLE,
										f_PRCL_SPF1_JUSTIFICACION_NO,
										f_PRCL_SPF1_OBS_FUSTALES,
										f_PRCL_SPF1_OBS_LATIZALES,
										f_PRCL_SPF1_OBS_BRINZALES,
										f_PRCL_SPF2_DILIGENCIA,
										f_PRCL_SPF2_FECHAINI,
										f_PRCL_SPF2_FECHAFIN,
										f_PRCL_SPF2_POSIBLE,
										f_PRCL_SPF2_JUSTIFICACION_NO,
										f_PRCL_SPF2_OBS_FUSTALES,
										f_PRCL_SPF2_OBS_LATIZALES,
										f_PRCL_SPF2_OBS_BRINZALES,
										f_PRCL_SPF3_DILIGENCIA,
										f_PRCL_SPF3_FECHAINI,
										f_PRCL_SPF3_FECHAFIN,
										f_PRCL_SPF3_POSIBLE,
										f_PRCL_SPF3_JUSTIFICACION_NO,
										f_PRCL_SPF3_OBS_FUSTALES,
										f_PRCL_SPF3_OBS_LATIZALES,
										f_PRCL_SPF3_OBS_BRINZALES,
										f_PRCL_SPF4_DILIGENCIA,
										f_PRCL_SPF4_FECHAINI,
										f_PRCL_SPF4_FECHAFIN,
										f_PRCL_SPF4_POSIBLE,
										f_PRCL_SPF4_JUSTIFICACION_NO,
										f_PRCL_SPF4_OBS_FUSTALES,
										f_PRCL_SPF4_OBS_LATIZALES,
										f_PRCL_SPF4_OBS_BRINZALES,
										f_PRCL_SPF5_DILIGENCIA,
										f_PRCL_SPF5_FECHAINI,
										f_PRCL_SPF5_FECHAFIN,
										f_PRCL_SPF5_POSIBLE,
										f_PRCL_SPF5_JUSTIFICACION_NO,
										f_PRCL_SPF5_OBS_FUSTALES,
										f_PRCL_SPF5_OBS_LATIZALES,
										f_PRCL_SPF5_OBS_BRINZALES,
										"", 
										f_PRCL_PLOT,
										f_PRCL_AREA,
										f_PRCL_INCLUIR,
										f_PRCL_TEMPORALIDAD,
										f_PRCL_PUBLICA,
										f_PRCL_HAB,
										f_PRCL_DAP,
										f_PRCL_GPS,
										f_PRCL_EQ,
										f_PRCL_BA,
										f_PRCL_BS,
										f_PRCL_BT,
										f_PRCL_AUTORCUSTODIOINFO,
										f_PRCL_TIPOBOSQUE,
										"SYSDATE",
										request,
										this.RED_username, this.RED_password, this.RED_host, this.RED_port, this.RED_sid, false
										);
								
									String [] a_resultado = resultado.split("-=-");
									if (!(Auxiliar.nzObjStr(a_resultado[0], "").equals("") || Auxiliar.nzObjStr(a_resultado[0], "").equals("0"))) {
										request.setAttribute("PRCL_CONSECUTIVO", a_resultado[0]);
										db_PRCL_CONSECUTIVO = a_resultado[0];
									}
									retorno = Auxiliar.nzObjStr(a_resultado[1], "No se pudo guardar la parcela.");
									
									request = establecerAtributos(request, session, db_PRCL_CONSECUTIVO,
									f_PRCL_CONSECUTIVO, 
									f_PRCL_ID_IMPORTACION, 
									f_PRCL_ID_UPM,
									f_PRCL_MEDIOACCESO_POBLADO,
									f_PRCL_DISTANCIA_POBLADO,
									f_PRCL_TPOBLADO_H,
									f_PRCL_TPOBLADO_M,
									f_PRCL_MEDIOACCESO_CAMPAMENTO,
									f_PRCL_DISTANCIA_CAMPAMENTO,
									f_PRCL_TCAMPAMENTO_H,
									f_PRCL_TCAMPAMENTO_M,
									f_PRCL_MEDIOACCESO_JALON,
									f_PRCL_DISTANCIA_JALON,
									f_PRCL_TJALON_H,
									f_PRCL_TJALON_M,
									f_PRCL_DISTANCIA_CAMPAMENTOS,
									f_PRCL_LATITUD,
									f_PRCL_LONGITUD,
									f_PRCL_ALTITUD,
									f_PRCL_NOMBRE,
									f_PRCL_USR_DILIGENCIA_F1,
									f_PRCL_USR_DILIGENCIA_F2,
									f_PRCL_FECHAINI_APROXIMACION,
									f_PRCL_FECHAFIN_APROXIMACION,
									f_PRCL_FECHAINI_LOCALIZACION,
									f_PRCL_FECHAFIN_LOCALIZACION,
									f_PRCL_DESCRIPCION,
									f_PRCL_OBSERVACIONES,
									f_PRCL_TRACKLOG_CAMPAMENTO,
									f_PRCL_TRACKLOG_PARCELA,
									f_PRCL_CONS_PAIS,
									f_PRCL_DEPARTAMENTO,
									f_PRCL_MUNICIPIO,
									f_PRCL_SPF1_DILIGENCIA,
									f_PRCL_SPF1_FECHAINI,
									f_PRCL_SPF1_FECHAFIN,
									f_PRCL_SPF1_POSIBLE,
									f_PRCL_SPF1_JUSTIFICACION_NO,
									f_PRCL_SPF1_OBS_FUSTALES,
									f_PRCL_SPF1_OBS_LATIZALES,
									f_PRCL_SPF1_OBS_BRINZALES,
									f_PRCL_SPF2_DILIGENCIA,
									f_PRCL_SPF2_FECHAINI,
									f_PRCL_SPF2_FECHAFIN,
									f_PRCL_SPF2_POSIBLE,
									f_PRCL_SPF2_JUSTIFICACION_NO,
									f_PRCL_SPF2_OBS_FUSTALES,
									f_PRCL_SPF2_OBS_LATIZALES,
									f_PRCL_SPF2_OBS_BRINZALES,
									f_PRCL_SPF3_DILIGENCIA,
									f_PRCL_SPF3_FECHAINI,
									f_PRCL_SPF3_FECHAFIN,
									f_PRCL_SPF3_POSIBLE,
									f_PRCL_SPF3_JUSTIFICACION_NO,
									f_PRCL_SPF3_OBS_FUSTALES,
									f_PRCL_SPF3_OBS_LATIZALES,
									f_PRCL_SPF3_OBS_BRINZALES,
									f_PRCL_SPF4_DILIGENCIA,
									f_PRCL_SPF4_FECHAINI,
									f_PRCL_SPF4_FECHAFIN,
									f_PRCL_SPF4_POSIBLE,
									f_PRCL_SPF4_JUSTIFICACION_NO,
									f_PRCL_SPF4_OBS_FUSTALES,
									f_PRCL_SPF4_OBS_LATIZALES,
									f_PRCL_SPF4_OBS_BRINZALES,
									f_PRCL_SPF5_DILIGENCIA,
									f_PRCL_SPF5_FECHAINI,
									f_PRCL_SPF5_FECHAFIN,
									f_PRCL_SPF5_POSIBLE,
									f_PRCL_SPF5_JUSTIFICACION_NO,
									f_PRCL_SPF5_OBS_FUSTALES,
									f_PRCL_SPF5_OBS_LATIZALES,
									f_PRCL_SPF5_OBS_BRINZALES,
									f_PRCL_PLOT,
									f_PRCL_AREA,
									f_PRCL_INCLUIR,
									f_PRCL_TEMPORALIDAD,
									f_PRCL_PUBLICA,
									f_PRCL_HAB,
									f_PRCL_DAP,
									f_PRCL_GPS,
									f_PRCL_EQ,
									f_PRCL_BA,
									f_PRCL_BS,
									f_PRCL_BT,
									f_PRCL_AUTORCUSTODIOINFO,
									f_PRCL_TIPOBOSQUE
									);
								}
								else {
									retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para guardar parcelas.", usuario, metodo);
									sec.registrarTransaccion(request, 9, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
									sec.registrarTransaccion(request, 12, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
									sec.registrarTransaccion(request, 13, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
								}
							} catch (Exception e) {
								retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a guardar(): " + e.toString(), usuario, metodo);
								try {
									sec.registrarTransaccion(request, 9, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
									sec.registrarTransaccion(request, 12, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
									sec.registrarTransaccion(request, 13, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
								} catch (ClassNotFoundException e1) {
									e1.printStackTrace();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								e.printStackTrace();
							}
							target = "/detalle_parcela.jsp?PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO;
						}
						else if (accion.equals("eliminar"))
						{
							response.setContentType("text/html; charset=UTF-8");
							try {
								if (
										sec.tienePermiso(usuario,  "228")
										|| sec.tienePermiso(usuario,  "227")
										) {
									
									
									String resultado = eliminar(f_PRCL_CONSECUTIVO, request);
									
									db_PRCL_CONSECUTIVO = "";
									retorno = resultado;
									
									request = establecerAtributos(request, session, "",
											f_PRCL_CONSECUTIVO, 
											f_PRCL_ID_IMPORTACION, 
											f_PRCL_ID_UPM,
											f_PRCL_MEDIOACCESO_POBLADO,
											f_PRCL_DISTANCIA_POBLADO,
											f_PRCL_TPOBLADO_H,
											f_PRCL_TPOBLADO_M,
											f_PRCL_MEDIOACCESO_CAMPAMENTO,
											f_PRCL_DISTANCIA_CAMPAMENTO,
											f_PRCL_TCAMPAMENTO_H,
											f_PRCL_TCAMPAMENTO_M,
											f_PRCL_MEDIOACCESO_JALON,
											f_PRCL_DISTANCIA_JALON,
											f_PRCL_TJALON_H,
											f_PRCL_TJALON_M,
											f_PRCL_DISTANCIA_CAMPAMENTOS,
											f_PRCL_LATITUD,
											f_PRCL_LONGITUD,
											f_PRCL_ALTITUD,
											f_PRCL_NOMBRE,
											f_PRCL_USR_DILIGENCIA_F1,
											f_PRCL_USR_DILIGENCIA_F2,
											f_PRCL_FECHAINI_APROXIMACION,
											f_PRCL_FECHAFIN_APROXIMACION,
											f_PRCL_FECHAINI_LOCALIZACION,
											f_PRCL_FECHAFIN_LOCALIZACION,
											f_PRCL_DESCRIPCION,
											f_PRCL_OBSERVACIONES,
											f_PRCL_TRACKLOG_CAMPAMENTO,
											f_PRCL_TRACKLOG_PARCELA,
											f_PRCL_CONS_PAIS,
											f_PRCL_DEPARTAMENTO,
											f_PRCL_MUNICIPIO,
											f_PRCL_SPF1_DILIGENCIA,
											f_PRCL_SPF1_FECHAINI,
											f_PRCL_SPF1_FECHAFIN,
											f_PRCL_SPF1_POSIBLE,
											f_PRCL_SPF1_JUSTIFICACION_NO,
											f_PRCL_SPF1_OBS_FUSTALES,
											f_PRCL_SPF1_OBS_LATIZALES,
											f_PRCL_SPF1_OBS_BRINZALES,
											f_PRCL_SPF2_DILIGENCIA,
											f_PRCL_SPF2_FECHAINI,
											f_PRCL_SPF2_FECHAFIN,
											f_PRCL_SPF2_POSIBLE,
											f_PRCL_SPF2_JUSTIFICACION_NO,
											f_PRCL_SPF2_OBS_FUSTALES,
											f_PRCL_SPF2_OBS_LATIZALES,
											f_PRCL_SPF2_OBS_BRINZALES,
											f_PRCL_SPF3_DILIGENCIA,
											f_PRCL_SPF3_FECHAINI,
											f_PRCL_SPF3_FECHAFIN,
											f_PRCL_SPF3_POSIBLE,
											f_PRCL_SPF3_JUSTIFICACION_NO,
											f_PRCL_SPF3_OBS_FUSTALES,
											f_PRCL_SPF3_OBS_LATIZALES,
											f_PRCL_SPF3_OBS_BRINZALES,
											f_PRCL_SPF4_DILIGENCIA,
											f_PRCL_SPF4_FECHAINI,
											f_PRCL_SPF4_FECHAFIN,
											f_PRCL_SPF4_POSIBLE,
											f_PRCL_SPF4_JUSTIFICACION_NO,
											f_PRCL_SPF4_OBS_FUSTALES,
											f_PRCL_SPF4_OBS_LATIZALES,
											f_PRCL_SPF4_OBS_BRINZALES,
											f_PRCL_SPF5_DILIGENCIA,
											f_PRCL_SPF5_FECHAINI,
											f_PRCL_SPF5_FECHAFIN,
											f_PRCL_SPF5_POSIBLE,
											f_PRCL_SPF5_JUSTIFICACION_NO,
											f_PRCL_SPF5_OBS_FUSTALES,
											f_PRCL_SPF5_OBS_LATIZALES,
											f_PRCL_SPF5_OBS_BRINZALES,
											f_PRCL_PLOT,
											f_PRCL_AREA,
											f_PRCL_INCLUIR,
											f_PRCL_TEMPORALIDAD,
											f_PRCL_PUBLICA,
											f_PRCL_HAB,
											f_PRCL_DAP,
											f_PRCL_GPS,
											f_PRCL_EQ,
											f_PRCL_BA,
											f_PRCL_BS,
											f_PRCL_BT,
											f_PRCL_AUTORCUSTODIOINFO,
											f_PRCL_TIPOBOSQUE
											);
								}
								else {
									retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
									sec.registrarTransaccion(request, 228, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
									sec.registrarTransaccion(request, 227, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "permisos", false);
								}
							} catch (Exception e) {
								retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a eliminar(): " + e.toString(), usuario, metodo);
								try {
									sec.registrarTransaccion(request, 228, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
									sec.registrarTransaccion(request, 227, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
								} catch (ClassNotFoundException e1) {
									e1.printStackTrace();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								e.printStackTrace();
							}
							target = "/detalle_parcela.jsp?PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO;
						}
						else {
							target = "/error.jsp";
							retorno = "No se encontró la información solicitada para la acción " + accion;
							try {
								sec.registrarTransaccion(request, 195, "", "Parcela", false);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				else {
					target = "/login.jsp";
					retorno = Auxiliar.mensajeImpersonal("advertencia", Auxiliar.traducir("SESION_VENCIDA", "es", "Credenciales erradas o Su sesión ha vencido"));
					try {
						sec.registrarTransaccion(request, 194, "", "", false);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}

	/**
	 * Método establecerAtributos
	 * 
	 * @param request
	 * @return request con los parámetros actualizados desde la base de datos
	 * @throws Exception 
	 */
	private HttpServletRequest establecerAtributos(HttpServletRequest request, HttpSession session, String nuevo_consecutivo,
			String f_PRCL_CONSECUTIVO, 
			String f_PRCL_ID_IMPORTACION, 
			String f_PRCL_ID_UPM,
			String f_PRCL_MEDIOACCESO_POBLADO,
			String f_PRCL_DISTANCIA_POBLADO,
			String f_PRCL_TPOBLADO_H,
			String f_PRCL_TPOBLADO_M,
			String f_PRCL_MEDIOACCESO_CAMPAMENTO,
			String f_PRCL_DISTANCIA_CAMPAMENTO,
			String f_PRCL_TCAMPAMENTO_H,
			String f_PRCL_TCAMPAMENTO_M,
			String f_PRCL_MEDIOACCESO_JALON,
			String f_PRCL_DISTANCIA_JALON,
			String f_PRCL_TJALON_H,
			String f_PRCL_TJALON_M,
			String f_PRCL_DISTANCIA_CAMPAMENTOS,
			String f_PRCL_LATITUD,
			String f_PRCL_LONGITUD,
			String f_PRCL_ALTITUD,
			String f_PRCL_NOMBRE,
			String f_PRCL_USR_DILIGENCIA_F1,
			String f_PRCL_USR_DILIGENCIA_F2,
			String f_PRCL_FECHAINI_APROXIMACION,
			String f_PRCL_FECHAFIN_APROXIMACION,
			String f_PRCL_FECHAINI_LOCALIZACION,
			String f_PRCL_FECHAFIN_LOCALIZACION,
			String f_PRCL_DESCRIPCION,
			String f_PRCL_OBSERVACIONES,
			String f_PRCL_TRACKLOG_CAMPAMENTO,
			String f_PRCL_TRACKLOG_PARCELA,
			String f_PRCL_CONS_PAIS,
			String f_PRCL_DEPARTAMENTO,
			String f_PRCL_MUNICIPIO,
			String f_PRCL_SPF1_DILIGENCIA,
			String f_PRCL_SPF1_FECHAINI,
			String f_PRCL_SPF1_FECHAFIN,
			String f_PRCL_SPF1_POSIBLE,
			String f_PRCL_SPF1_JUSTIFICACION_NO,
			String f_PRCL_SPF1_OBS_FUSTALES,
			String f_PRCL_SPF1_OBS_LATIZALES,
			String f_PRCL_SPF1_OBS_BRINZALES,
			String f_PRCL_SPF2_DILIGENCIA,
			String f_PRCL_SPF2_FECHAINI,
			String f_PRCL_SPF2_FECHAFIN,
			String f_PRCL_SPF2_POSIBLE,
			String f_PRCL_SPF2_JUSTIFICACION_NO,
			String f_PRCL_SPF2_OBS_FUSTALES,
			String f_PRCL_SPF2_OBS_LATIZALES,
			String f_PRCL_SPF2_OBS_BRINZALES,
			String f_PRCL_SPF3_DILIGENCIA,
			String f_PRCL_SPF3_FECHAINI,
			String f_PRCL_SPF3_FECHAFIN,
			String f_PRCL_SPF3_POSIBLE,
			String f_PRCL_SPF3_JUSTIFICACION_NO,
			String f_PRCL_SPF3_OBS_FUSTALES,
			String f_PRCL_SPF3_OBS_LATIZALES,
			String f_PRCL_SPF3_OBS_BRINZALES,
			String f_PRCL_SPF4_DILIGENCIA,
			String f_PRCL_SPF4_FECHAINI,
			String f_PRCL_SPF4_FECHAFIN,
			String f_PRCL_SPF4_POSIBLE,
			String f_PRCL_SPF4_JUSTIFICACION_NO,
			String f_PRCL_SPF4_OBS_FUSTALES,
			String f_PRCL_SPF4_OBS_LATIZALES,
			String f_PRCL_SPF4_OBS_BRINZALES,
			String f_PRCL_SPF5_DILIGENCIA,
			String f_PRCL_SPF5_FECHAINI,
			String f_PRCL_SPF5_FECHAFIN,
			String f_PRCL_SPF5_POSIBLE,
			String f_PRCL_SPF5_JUSTIFICACION_NO,
			String f_PRCL_SPF5_OBS_FUSTALES,
			String f_PRCL_SPF5_OBS_LATIZALES,
			String f_PRCL_SPF5_OBS_BRINZALES,
			String f_PRCL_PLOT,
			String f_PRCL_AREA,
			String f_PRCL_INCLUIR,
			String f_PRCL_TEMPORALIDAD,
			String f_PRCL_PUBLICA,
			String f_PRCL_HAB,
			String f_PRCL_DAP,
			String f_PRCL_GPS,
			String f_PRCL_EQ,
			String f_PRCL_BA,
			String f_PRCL_BS,
			String f_PRCL_BT,
			String f_PRCL_AUTORCUSTODIOINFO,
			String f_PRCL_TIPOBOSQUE
			) 
			throws Exception {
		String metodo = yo + "establecerAtributos";

		BD dbREDD = new BD();
		Sec sec = new Sec();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String PRCL_CONSECUTIVO = "";
	    
	    PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "");
	    
	    if (Auxiliar.tieneAlgo(nuevo_consecutivo)) PRCL_CONSECUTIVO = nuevo_consecutivo;
	 
		String db_PRCL_ID_IMPORTACION = "";
		String db_PRCL_CONSECUTIVO = "";
		String db_PRCL_ID_UPM = "";
		String db_PRCL_NOMBRE = "";
		String db_PRCL_USR_DILIGENCIA_F1 = "";
		String db_PRCL_USR_DILIGENCIA_F2 = "";
		String db_PRCL_FECHAINI_APROXIMACION = "";
		String db_PRCL_FECHAFIN_APROXIMACION = "";
		String db_PRCL_FECHAINI_LOCALIZACION = "";
		String db_PRCL_FECHAFIN_LOCALIZACION = "";
		String db_PRCL_DESCRIPCION = "";
		String db_PRCL_OBSERVACIONES = "";
		String db_PRCL_TRACKLOG_CAMPAMENTO = "";
		String db_PRCL_TRACKLOG_PARCELA = "";
		String db_PRCL_DISTANCIA_POBLADO = "";
		String db_PRCL_MEDIOACCESO_POBLADO = "";
		String db_PRCL_TPOBLADO_H = "";
		String db_PRCL_TPOBLADO_M = "";
		String db_PRCL_DISTANCIA_CAMPAMENTO = "";
		String db_PRCL_MEDIOACCESO_CAMPAMENTO = "";
		String db_PRCL_TCAMPAMENTO_H = "";
		String db_PRCL_TCAMPAMENTO_M = "";
		String db_PRCL_DISTANCIA_JALON = "";
		String db_PRCL_MEDIOACCESO_JALON = "";
		String db_PRCL_TJALON_H = "";
		String db_PRCL_TJALON_M = "";
		String db_PRCL_DISTANCIA_CAMPAMENTOS = "";
		String db_PRCL_LATITUD = "";
		String db_PRCL_LONGITUD = "";
		String db_PRCL_ALTITUD = "";
		String db_PRCL_CONS_PAIS = "";
		String db_PRCL_DEPARTAMENTO = "";
		String db_PRCL_MUNICIPIO = "";
		String db_PRCL_NOMBREARCHIVO = "";
		String db_PRCL_SPF1_DILIGENCIA = "";
		String db_PRCL_SPF1_FECHAINI = "";
		String db_PRCL_SPF1_FECHAFIN = "";
		String db_PRCL_SPF1_POSIBLE = "";
		String db_PRCL_SPF1_JUSTIFICACION_NO = "";
		String db_PRCL_SPF1_OBS_FUSTALES = "";
		String db_PRCL_SPF1_OBS_LATIZALES = "";
		String db_PRCL_SPF1_OBS_BRINZALES = "";
		String db_PRCL_SPF2_DILIGENCIA = "";
		String db_PRCL_SPF2_FECHAINI = "";
		String db_PRCL_SPF2_FECHAFIN = "";
		String db_PRCL_SPF2_POSIBLE = "";
		String db_PRCL_SPF2_JUSTIFICACION_NO = "";
		String db_PRCL_SPF2_OBS_FUSTALES = "";
		String db_PRCL_SPF2_OBS_LATIZALES = "";
		String db_PRCL_SPF2_OBS_BRINZALES = "";
		String db_PRCL_SPF3_DILIGENCIA = "";
		String db_PRCL_SPF3_FECHAINI = "";
		String db_PRCL_SPF3_FECHAFIN = "";
		String db_PRCL_SPF3_POSIBLE = "";
		String db_PRCL_SPF3_JUSTIFICACION_NO = "";
		String db_PRCL_SPF3_OBS_FUSTALES = "";
		String db_PRCL_SPF3_OBS_LATIZALES = "";
		String db_PRCL_SPF3_OBS_BRINZALES = "";
		String db_PRCL_SPF4_DILIGENCIA = "";
		String db_PRCL_SPF4_FECHAINI = "";
		String db_PRCL_SPF4_FECHAFIN = "";
		String db_PRCL_SPF4_POSIBLE = "";
		String db_PRCL_SPF4_JUSTIFICACION_NO = "";
		String db_PRCL_SPF4_OBS_FUSTALES = "";
		String db_PRCL_SPF4_OBS_LATIZALES = "";
		String db_PRCL_SPF4_OBS_BRINZALES = "";
		String db_PRCL_SPF5_DILIGENCIA = "";
		String db_PRCL_SPF5_FECHAINI = "";
		String db_PRCL_SPF5_FECHAFIN = "";
		String db_PRCL_SPF5_POSIBLE = "";
		String db_PRCL_SPF5_JUSTIFICACION_NO = "";
		String db_PRCL_SPF5_OBS_FUSTALES = "";
		String db_PRCL_SPF5_OBS_LATIZALES = "";
		String db_PRCL_SPF5_OBS_BRINZALES = "";
		String db_PNN = "";
		String db_TIPOBOSQUE = "";
		String db_CAR = "";
		String db_RESGUARDOINDIGENA = "";
		String db_PRCL_PLOT = "";
		String db_PRCL_AREA = "";
		String db_PRCL_INCLUIR = "";
		String db_PRCL_TEMPORALIDAD = "";
		String db_PRCL_PUBLICA = "";
		String db_PRCL_HAB = "";
		String db_PRCL_DAP = "";
		String db_PRCL_GPS = "";
		String db_PRCL_EQ = "";
		String db_PRCL_BA = "";
		String db_PRCL_BS = "";
		String db_PRCL_BT = "";
		String db_PRCL_AUTORCUSTODIOINFO = "";
		String db_PRCL_TIPOBOSQUE = "";

		String id_usuario = "";
		String id_creador = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+"", "");
		}
		boolean privilegio = false;
		
		if (id_usuario.equals(id_creador)) {
			if (sec.tienePermiso(id_usuario, "15") || sec.tienePermiso(id_usuario, "261")) {
				privilegio = true;
			}
		}
		else {
			if (sec.tienePermiso(id_usuario, "15")) {
				privilegio = true;
			}
		}
		
		if (!privilegio) { 
			request.setAttribute("retorno",Auxiliar.mensaje("advertencia", "No tiene privilegios para ver el detalle de esta parcela", id_usuario, metodo));
			dbREDD.desconectarse();
			return request;
		}

		
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			try {
			    ResultSet rset = cargarRegistro(PRCL_CONSECUTIVO, session);
			    
			    if (rset != null) {
					if (rset.next()) {
						db_PRCL_ID_IMPORTACION = rset.getString("PRCL_ID_IMPORTACION");
						db_PRCL_CONSECUTIVO = rset.getString("PRCL_CONSECUTIVO");
						db_PRCL_ID_UPM = rset.getString("PRCL_ID_UPM");
						db_PRCL_NOMBRE = rset.getString("PRCL_NOMBRE");
						db_PRCL_USR_DILIGENCIA_F1 = rset.getString("PRCL_USR_DILIGENCIA_F1");
						db_PRCL_USR_DILIGENCIA_F2 = rset.getString("PRCL_USR_DILIGENCIA_F2");
						db_PRCL_FECHAINI_APROXIMACION = rset.getString("PRCL_FECHAINI_APROXIMACION");
						db_PRCL_FECHAFIN_APROXIMACION = rset.getString("PRCL_FECHAFIN_APROXIMACION");
						db_PRCL_FECHAINI_LOCALIZACION = rset.getString("PRCL_FECHAINI_LOCALIZACION");
						db_PRCL_FECHAFIN_LOCALIZACION = rset.getString("PRCL_FECHAFIN_LOCALIZACION");
						db_PRCL_DESCRIPCION = rset.getString("PRCL_DESCRIPCION");
						db_PRCL_OBSERVACIONES = rset.getString("PRCL_OBSERVACIONES");
						db_PRCL_TRACKLOG_CAMPAMENTO = rset.getString("PRCL_TRACKLOG_CAMPAMENTO");
						db_PRCL_TRACKLOG_PARCELA = rset.getString("PRCL_TRACKLOG_PARCELA");
						db_PRCL_DISTANCIA_POBLADO = rset.getString("PRCL_DISTANCIA_POBLADO");
						db_PRCL_MEDIOACCESO_POBLADO = rset.getString("PRCL_MEDIOACCESO_POBLADO");
						db_PRCL_TPOBLADO_H = rset.getString("PRCL_TPOBLADO_H");
						db_PRCL_TPOBLADO_M = rset.getString("PRCL_TPOBLADO_M");
						db_PRCL_DISTANCIA_CAMPAMENTO = rset.getString("PRCL_DISTANCIA_CAMPAMENTO");
						db_PRCL_MEDIOACCESO_CAMPAMENTO = rset.getString("PRCL_MEDIOACCESO_CAMPAMENTO");
						db_PRCL_TCAMPAMENTO_H = rset.getString("PRCL_TCAMPAMENTO_H");
						db_PRCL_TCAMPAMENTO_M = rset.getString("PRCL_TCAMPAMENTO_M");
						db_PRCL_DISTANCIA_JALON = rset.getString("PRCL_DISTANCIA_JALON");
						db_PRCL_MEDIOACCESO_JALON = rset.getString("PRCL_MEDIOACCESO_JALON");
						db_PRCL_TJALON_H = rset.getString("PRCL_TJALON_H");
						db_PRCL_TJALON_M = rset.getString("PRCL_TJALON_M");
						db_PRCL_DISTANCIA_CAMPAMENTOS = rset.getString("PRCL_DISTANCIA_CAMPAMENTOS");
						db_PRCL_LATITUD = rset.getString("PRCL_LATITUD");
						db_PRCL_LONGITUD = rset.getString("PRCL_LONGITUD");
						db_PRCL_ALTITUD = rset.getString("PRCL_ALTITUD");
						db_PRCL_CONS_PAIS = rset.getString("PRCL_CONS_PAIS");
						db_PRCL_DEPARTAMENTO = rset.getString("PRCL_DEPARTAMENTO");
						db_PRCL_MUNICIPIO = rset.getString("PRCL_MUNICIPIO");
						db_PRCL_NOMBREARCHIVO = rset.getString("PRCL_NOMBREARCHIVO");
						db_PRCL_SPF1_DILIGENCIA = rset.getString("PRCL_SPF1_DILIGENCIA");
						db_PRCL_SPF1_FECHAINI = rset.getString("PRCL_SPF1_FECHAINI");
						db_PRCL_SPF1_FECHAFIN = rset.getString("PRCL_SPF1_FECHAFIN");
						db_PRCL_SPF1_POSIBLE = rset.getString("PRCL_SPF1_POSIBLE");
						db_PRCL_SPF1_JUSTIFICACION_NO = rset.getString("PRCL_SPF1_JUSTIFICACION_NO");
						db_PRCL_SPF1_OBS_FUSTALES = rset.getString("PRCL_SPF1_OBS_FUSTALES");
						db_PRCL_SPF1_OBS_LATIZALES = rset.getString("PRCL_SPF1_OBS_LATIZALES");
						db_PRCL_SPF1_OBS_BRINZALES = rset.getString("PRCL_SPF1_OBS_BRINZALES");
						db_PRCL_SPF2_DILIGENCIA = rset.getString("PRCL_SPF2_DILIGENCIA");
						db_PRCL_SPF2_FECHAINI = rset.getString("PRCL_SPF2_FECHAINI");
						db_PRCL_SPF2_FECHAFIN = rset.getString("PRCL_SPF2_FECHAFIN");
						db_PRCL_SPF2_POSIBLE = rset.getString("PRCL_SPF2_POSIBLE");
						db_PRCL_SPF2_JUSTIFICACION_NO = rset.getString("PRCL_SPF2_JUSTIFICACION_NO");
						db_PRCL_SPF2_OBS_FUSTALES = rset.getString("PRCL_SPF2_OBS_FUSTALES");
						db_PRCL_SPF2_OBS_LATIZALES = rset.getString("PRCL_SPF2_OBS_LATIZALES");
						db_PRCL_SPF2_OBS_BRINZALES = rset.getString("PRCL_SPF2_OBS_BRINZALES");
						db_PRCL_SPF3_DILIGENCIA = rset.getString("PRCL_SPF3_DILIGENCIA");
						db_PRCL_SPF3_FECHAINI = rset.getString("PRCL_SPF3_FECHAINI");
						db_PRCL_SPF3_FECHAFIN = rset.getString("PRCL_SPF3_FECHAFIN");
						db_PRCL_SPF3_POSIBLE = rset.getString("PRCL_SPF3_POSIBLE");
						db_PRCL_SPF3_JUSTIFICACION_NO = rset.getString("PRCL_SPF3_JUSTIFICACION_NO");
						db_PRCL_SPF3_OBS_FUSTALES = rset.getString("PRCL_SPF3_OBS_FUSTALES");
						db_PRCL_SPF3_OBS_LATIZALES = rset.getString("PRCL_SPF3_OBS_LATIZALES");
						db_PRCL_SPF3_OBS_BRINZALES = rset.getString("PRCL_SPF3_OBS_BRINZALES");
						db_PRCL_SPF4_DILIGENCIA = rset.getString("PRCL_SPF4_DILIGENCIA");
						db_PRCL_SPF4_FECHAINI = rset.getString("PRCL_SPF4_FECHAINI");
						db_PRCL_SPF4_FECHAFIN = rset.getString("PRCL_SPF4_FECHAFIN");
						db_PRCL_SPF4_POSIBLE = rset.getString("PRCL_SPF4_POSIBLE");
						db_PRCL_SPF4_JUSTIFICACION_NO = rset.getString("PRCL_SPF4_JUSTIFICACION_NO");
						db_PRCL_SPF4_OBS_FUSTALES = rset.getString("PRCL_SPF4_OBS_FUSTALES");
						db_PRCL_SPF4_OBS_LATIZALES = rset.getString("PRCL_SPF4_OBS_LATIZALES");
						db_PRCL_SPF4_OBS_BRINZALES = rset.getString("PRCL_SPF4_OBS_BRINZALES");
						db_PRCL_SPF5_DILIGENCIA = rset.getString("PRCL_SPF5_DILIGENCIA");
						db_PRCL_SPF5_FECHAINI = rset.getString("PRCL_SPF5_FECHAINI");
						db_PRCL_SPF5_FECHAFIN = rset.getString("PRCL_SPF5_FECHAFIN");
						db_PRCL_SPF5_POSIBLE = rset.getString("PRCL_SPF5_POSIBLE");
						db_PRCL_SPF5_JUSTIFICACION_NO = rset.getString("PRCL_SPF5_JUSTIFICACION_NO");
						db_PRCL_SPF5_OBS_FUSTALES = rset.getString("PRCL_SPF5_OBS_FUSTALES");
						db_PRCL_SPF5_OBS_LATIZALES = rset.getString("PRCL_SPF5_OBS_LATIZALES");
						db_PRCL_SPF5_OBS_BRINZALES = rset.getString("PRCL_SPF5_OBS_BRINZALES");
						db_PRCL_PLOT = rset.getString("PRCL_PLOT");
						db_PRCL_AREA = rset.getString("PRCL_AREA");
						db_PRCL_INCLUIR = rset.getString("PRCL_INCLUIR");
						db_PRCL_TEMPORALIDAD = rset.getString("PRCL_TEMPORALIDAD");
						db_PRCL_PUBLICA = rset.getString("PRCL_PUBLICA");
						db_PRCL_HAB = rset.getString("PRCL_HAB");
						db_PRCL_DAP = rset.getString("PRCL_DAP");
						db_PRCL_GPS = rset.getString("PRCL_GPS");
						db_PRCL_EQ = rset.getString("PRCL_EQ");
						db_PRCL_BA = rset.getString("PRCL_BA");
						db_PRCL_BS = rset.getString("PRCL_BS");
						db_PRCL_BT = rset.getString("PRCL_BT");
						db_PRCL_AUTORCUSTODIOINFO = rset.getString("PRCL_AUTORCUSTODIOINFO");
						db_PRCL_TIPOBOSQUE = rset.getString("PRCL_TIPOBOSQUE");
						db_PNN = rset.getString("PNN");
						db_TIPOBOSQUE = rset.getString("TIPOBOSQUE");
						db_CAR = rset.getString("CAR");
						db_RESGUARDOINDIGENA = rset.getString("RESGUARDOINDIGENA");
						
						rset.close();
					}
			    }
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("PRCL_CONSECUTIVO", Auxiliar.nzVacio(db_PRCL_CONSECUTIVO, f_PRCL_CONSECUTIVO));
		request.setAttribute("PRCL_ID_IMPORTACION", Auxiliar.nzVacio(db_PRCL_ID_IMPORTACION, f_PRCL_ID_IMPORTACION));
		request.setAttribute("PRCL_ID_UPM", Auxiliar.nzVacio(db_PRCL_ID_UPM, f_PRCL_ID_UPM));
		request.setAttribute("PRCL_NOMBRE", Auxiliar.nzVacio(db_PRCL_NOMBRE, f_PRCL_NOMBRE));
		request.setAttribute("PRCL_USR_DILIGENCIA_F1", Auxiliar.nzVacio(db_PRCL_USR_DILIGENCIA_F1, f_PRCL_USR_DILIGENCIA_F1));
		request.setAttribute("PRCL_USR_DILIGENCIA_F2", Auxiliar.nzVacio(db_PRCL_USR_DILIGENCIA_F2, f_PRCL_USR_DILIGENCIA_F2));
		request.setAttribute("PRCL_FECHAINI_APROXIMACION", Auxiliar.nzVacio(db_PRCL_FECHAINI_APROXIMACION, f_PRCL_FECHAINI_APROXIMACION));
		request.setAttribute("PRCL_FECHAFIN_APROXIMACION", Auxiliar.nzVacio(db_PRCL_FECHAFIN_APROXIMACION, f_PRCL_FECHAFIN_APROXIMACION));
		request.setAttribute("PRCL_FECHAINI_LOCALIZACION", Auxiliar.nzVacio(db_PRCL_FECHAINI_LOCALIZACION, f_PRCL_FECHAINI_LOCALIZACION));
		request.setAttribute("PRCL_FECHAFIN_LOCALIZACION", Auxiliar.nzVacio(db_PRCL_FECHAFIN_LOCALIZACION, f_PRCL_FECHAFIN_LOCALIZACION));
		request.setAttribute("PRCL_DESCRIPCION", Auxiliar.nzVacio(db_PRCL_DESCRIPCION, f_PRCL_DESCRIPCION));
		request.setAttribute("PRCL_OBSERVACIONES", Auxiliar.nzVacio(db_PRCL_OBSERVACIONES, f_PRCL_DESCRIPCION));
		request.setAttribute("PRCL_TRACKLOG_CAMPAMENTO", Auxiliar.nzVacio(db_PRCL_TRACKLOG_CAMPAMENTO, f_PRCL_TRACKLOG_CAMPAMENTO));
		request.setAttribute("PRCL_TRACKLOG_PARCELA", Auxiliar.nzVacio(db_PRCL_TRACKLOG_PARCELA, f_PRCL_TRACKLOG_PARCELA));
		request.setAttribute("PRCL_MEDIOACCESO_POBLADO", Auxiliar.nzVacio(db_PRCL_MEDIOACCESO_POBLADO, f_PRCL_MEDIOACCESO_POBLADO));
		request.setAttribute("PRCL_DISTANCIA_POBLADO", Auxiliar.nzVacio(db_PRCL_DISTANCIA_POBLADO, f_PRCL_DISTANCIA_POBLADO));
		request.setAttribute("PRCL_TPOBLADO_H", Auxiliar.nzVacio(db_PRCL_TPOBLADO_H, f_PRCL_TPOBLADO_H));
		request.setAttribute("PRCL_TPOBLADO_M", Auxiliar.nzVacio(db_PRCL_TPOBLADO_M, f_PRCL_TPOBLADO_M));
		request.setAttribute("PRCL_MEDIOACCESO_CAMPAMENTO", Auxiliar.nzVacio(db_PRCL_MEDIOACCESO_CAMPAMENTO, f_PRCL_MEDIOACCESO_CAMPAMENTO));
		request.setAttribute("PRCL_DISTANCIA_CAMPAMENTO", Auxiliar.nzVacio(db_PRCL_DISTANCIA_CAMPAMENTO, f_PRCL_DISTANCIA_CAMPAMENTO));
		request.setAttribute("PRCL_TCAMPAMENTO_H", Auxiliar.nzVacio(db_PRCL_TCAMPAMENTO_H, f_PRCL_TCAMPAMENTO_H));
		request.setAttribute("PRCL_TCAMPAMENTO_M", Auxiliar.nzVacio(db_PRCL_TCAMPAMENTO_M, f_PRCL_TCAMPAMENTO_M));
		request.setAttribute("PRCL_MEDIOACCESO_JALON", Auxiliar.nzVacio(db_PRCL_MEDIOACCESO_JALON, f_PRCL_MEDIOACCESO_JALON));
		request.setAttribute("PRCL_DISTANCIA_JALON", Auxiliar.nzVacio(db_PRCL_DISTANCIA_JALON, f_PRCL_DISTANCIA_JALON));
		request.setAttribute("PRCL_TJALON_H", Auxiliar.nzVacio(db_PRCL_TJALON_H, f_PRCL_TJALON_H));
		request.setAttribute("PRCL_TJALON_M", Auxiliar.nzVacio(db_PRCL_TJALON_M, f_PRCL_TJALON_M));
		request.setAttribute("PRCL_DISTANCIA_CAMPAMENTOS", Auxiliar.nzVacio(db_PRCL_DISTANCIA_CAMPAMENTOS, f_PRCL_DISTANCIA_CAMPAMENTOS));
		request.setAttribute("PRCL_LATITUD", Auxiliar.nzVacio(db_PRCL_LATITUD, f_PRCL_LATITUD));
		request.setAttribute("PRCL_LONGITUD", Auxiliar.nzVacio(db_PRCL_LONGITUD, f_PRCL_LONGITUD));
		request.setAttribute("PRCL_ALTITUD", Auxiliar.nzVacio(db_PRCL_ALTITUD, f_PRCL_ALTITUD));
		request.setAttribute("PRCL_CONS_PAIS", Auxiliar.nzVacio(db_PRCL_CONS_PAIS, f_PRCL_CONS_PAIS));
		request.setAttribute("PRCL_DEPARTAMENTO", Auxiliar.nzVacio(db_PRCL_DEPARTAMENTO, f_PRCL_DEPARTAMENTO));
		request.setAttribute("PRCL_MUNICIPIO", Auxiliar.nzVacio(db_PRCL_MUNICIPIO, f_PRCL_MUNICIPIO));
		request.setAttribute("PRCL_NOMBREARCHIVO", Auxiliar.nzVacio(db_PRCL_NOMBREARCHIVO, ""));
		request.setAttribute("PNN", Auxiliar.nzVacio(db_PNN, ""));
		request.setAttribute("CAR", Auxiliar.nzVacio(db_CAR, ""));
		request.setAttribute("RESGUARDOINDIGENA", Auxiliar.nzVacio(db_RESGUARDOINDIGENA, ""));
		
		
		request.setAttribute("PRCL_SPF1_DILIGENCIA", Auxiliar.nzVacio(db_PRCL_SPF1_DILIGENCIA, f_PRCL_SPF1_DILIGENCIA));
		request.setAttribute("PRCL_SPF1_FECHAINI", Auxiliar.nzVacio(db_PRCL_SPF1_FECHAINI, f_PRCL_SPF1_FECHAINI));
		request.setAttribute("PRCL_SPF1_FECHAFIN", Auxiliar.nzVacio(db_PRCL_SPF1_FECHAFIN, f_PRCL_SPF1_FECHAFIN));
		request.setAttribute("PRCL_SPF1_POSIBLE", Auxiliar.nzVacio(db_PRCL_SPF1_POSIBLE, f_PRCL_SPF1_POSIBLE));
		request.setAttribute("PRCL_SPF1_JUSTIFICACION_NO", Auxiliar.nzVacio(db_PRCL_SPF1_JUSTIFICACION_NO, f_PRCL_SPF1_JUSTIFICACION_NO));
		request.setAttribute("PRCL_SPF1_OBS_FUSTALES", Auxiliar.nzVacio(db_PRCL_SPF1_OBS_FUSTALES, f_PRCL_SPF1_OBS_FUSTALES));
		request.setAttribute("PRCL_SPF1_OBS_LATIZALES", Auxiliar.nzVacio(db_PRCL_SPF1_OBS_LATIZALES, f_PRCL_SPF1_OBS_LATIZALES));
		request.setAttribute("PRCL_SPF1_OBS_BRINZALES", Auxiliar.nzVacio(db_PRCL_SPF1_OBS_BRINZALES, f_PRCL_SPF1_OBS_BRINZALES));
		
		request.setAttribute("PRCL_SPF2_DILIGENCIA", Auxiliar.nzVacio(db_PRCL_SPF2_DILIGENCIA, f_PRCL_SPF2_DILIGENCIA));
		request.setAttribute("PRCL_SPF2_FECHAINI", Auxiliar.nzVacio(db_PRCL_SPF2_FECHAINI, f_PRCL_SPF2_FECHAINI));
		request.setAttribute("PRCL_SPF2_FECHAFIN", Auxiliar.nzVacio(db_PRCL_SPF2_FECHAFIN, f_PRCL_SPF2_FECHAFIN));
		request.setAttribute("PRCL_SPF2_POSIBLE", Auxiliar.nzVacio(db_PRCL_SPF2_POSIBLE, f_PRCL_SPF2_POSIBLE));
		request.setAttribute("PRCL_SPF2_JUSTIFICACION_NO", Auxiliar.nzVacio(db_PRCL_SPF2_JUSTIFICACION_NO, f_PRCL_SPF2_JUSTIFICACION_NO));
		request.setAttribute("PRCL_SPF2_OBS_FUSTALES", Auxiliar.nzVacio(db_PRCL_SPF2_OBS_FUSTALES, f_PRCL_SPF2_OBS_FUSTALES));
		request.setAttribute("PRCL_SPF2_OBS_LATIZALES", Auxiliar.nzVacio(db_PRCL_SPF2_OBS_LATIZALES, f_PRCL_SPF2_OBS_LATIZALES));
		request.setAttribute("PRCL_SPF2_OBS_BRINZALES", Auxiliar.nzVacio(db_PRCL_SPF2_OBS_BRINZALES, f_PRCL_SPF2_OBS_BRINZALES));
		
		request.setAttribute("PRCL_SPF3_DILIGENCIA", Auxiliar.nzVacio(db_PRCL_SPF3_DILIGENCIA, f_PRCL_SPF3_DILIGENCIA));
		request.setAttribute("PRCL_SPF3_FECHAINI", Auxiliar.nzVacio(db_PRCL_SPF3_FECHAINI, f_PRCL_SPF3_FECHAINI));
		request.setAttribute("PRCL_SPF3_FECHAFIN", Auxiliar.nzVacio(db_PRCL_SPF3_FECHAFIN, f_PRCL_SPF3_FECHAFIN));
		request.setAttribute("PRCL_SPF3_POSIBLE", Auxiliar.nzVacio(db_PRCL_SPF3_POSIBLE, f_PRCL_SPF3_POSIBLE));
		request.setAttribute("PRCL_SPF3_JUSTIFICACION_NO", Auxiliar.nzVacio(db_PRCL_SPF3_JUSTIFICACION_NO, f_PRCL_SPF3_JUSTIFICACION_NO));
		request.setAttribute("PRCL_SPF3_OBS_FUSTALES", Auxiliar.nzVacio(db_PRCL_SPF3_OBS_FUSTALES, f_PRCL_SPF3_OBS_FUSTALES));
		request.setAttribute("PRCL_SPF3_OBS_LATIZALES", Auxiliar.nzVacio(db_PRCL_SPF3_OBS_LATIZALES, f_PRCL_SPF3_OBS_LATIZALES));
		request.setAttribute("PRCL_SPF3_OBS_BRINZALES", Auxiliar.nzVacio(db_PRCL_SPF3_OBS_BRINZALES, f_PRCL_SPF3_OBS_BRINZALES));
		
		request.setAttribute("PRCL_SPF4_DILIGENCIA", Auxiliar.nzVacio(db_PRCL_SPF4_DILIGENCIA, f_PRCL_SPF4_DILIGENCIA));
		request.setAttribute("PRCL_SPF4_FECHAINI", Auxiliar.nzVacio(db_PRCL_SPF4_FECHAINI, f_PRCL_SPF4_FECHAINI));
		request.setAttribute("PRCL_SPF4_FECHAFIN", Auxiliar.nzVacio(db_PRCL_SPF4_FECHAFIN, f_PRCL_SPF4_FECHAFIN));
		request.setAttribute("PRCL_SPF4_POSIBLE", Auxiliar.nzVacio(db_PRCL_SPF4_POSIBLE, f_PRCL_SPF4_POSIBLE));
		request.setAttribute("PRCL_SPF4_JUSTIFICACION_NO", Auxiliar.nzVacio(db_PRCL_SPF4_JUSTIFICACION_NO, f_PRCL_SPF4_JUSTIFICACION_NO));
		request.setAttribute("PRCL_SPF4_OBS_FUSTALES", Auxiliar.nzVacio(db_PRCL_SPF4_OBS_FUSTALES, f_PRCL_SPF4_OBS_FUSTALES));
		request.setAttribute("PRCL_SPF4_OBS_LATIZALES", Auxiliar.nzVacio(db_PRCL_SPF4_OBS_LATIZALES, f_PRCL_SPF4_OBS_LATIZALES));
		request.setAttribute("PRCL_SPF4_OBS_BRINZALES", Auxiliar.nzVacio(db_PRCL_SPF4_OBS_BRINZALES, f_PRCL_SPF4_OBS_BRINZALES));
		
		request.setAttribute("PRCL_SPF5_DILIGENCIA", Auxiliar.nzVacio(db_PRCL_SPF5_DILIGENCIA, f_PRCL_SPF5_DILIGENCIA));
		request.setAttribute("PRCL_SPF5_FECHAINI", Auxiliar.nzVacio(db_PRCL_SPF5_FECHAINI, f_PRCL_SPF5_FECHAINI));
		request.setAttribute("PRCL_SPF5_FECHAFIN", Auxiliar.nzVacio(db_PRCL_SPF5_FECHAFIN, f_PRCL_SPF5_FECHAFIN));
		request.setAttribute("PRCL_SPF5_POSIBLE", Auxiliar.nzVacio(db_PRCL_SPF5_POSIBLE, f_PRCL_SPF5_POSIBLE));
		request.setAttribute("PRCL_SPF5_JUSTIFICACION_NO", Auxiliar.nzVacio(db_PRCL_SPF5_JUSTIFICACION_NO, f_PRCL_SPF5_JUSTIFICACION_NO));
		request.setAttribute("PRCL_SPF5_OBS_FUSTALES", Auxiliar.nzVacio(db_PRCL_SPF5_OBS_FUSTALES, f_PRCL_SPF5_OBS_FUSTALES));
		request.setAttribute("PRCL_SPF5_OBS_LATIZALES", Auxiliar.nzVacio(db_PRCL_SPF5_OBS_LATIZALES, f_PRCL_SPF5_OBS_LATIZALES));
		request.setAttribute("PRCL_SPF5_OBS_BRINZALES", Auxiliar.nzVacio(db_PRCL_SPF5_OBS_BRINZALES, f_PRCL_SPF5_OBS_BRINZALES));
		
		request.setAttribute("PRCL_PLOT", Auxiliar.nzVacio(db_PRCL_PLOT, f_PRCL_PLOT));
		request.setAttribute("PRCL_AREA", Auxiliar.nzVacio(db_PRCL_AREA, f_PRCL_AREA));
		request.setAttribute("PRCL_INCLUIR", Auxiliar.nzVacio(db_PRCL_INCLUIR, f_PRCL_INCLUIR));
		request.setAttribute("PRCL_TEMPORALIDAD", Auxiliar.nzVacio(db_PRCL_TEMPORALIDAD, f_PRCL_TEMPORALIDAD));
		request.setAttribute("PRCL_PUBLICA", Auxiliar.nzVacio(db_PRCL_PUBLICA, f_PRCL_PUBLICA));
		request.setAttribute("PRCL_HAB", Auxiliar.nzVacio(db_PRCL_HAB, f_PRCL_HAB));
		request.setAttribute("PRCL_DAP", Auxiliar.nzVacio(db_PRCL_DAP, f_PRCL_DAP));
		request.setAttribute("PRCL_GPS", Auxiliar.nzVacio(db_PRCL_GPS, f_PRCL_GPS));
		request.setAttribute("PRCL_EQ", Auxiliar.nzVacio(db_PRCL_EQ, f_PRCL_EQ));
		request.setAttribute("PRCL_BA", Auxiliar.nzVacio(db_PRCL_BA, f_PRCL_BA));
		request.setAttribute("PRCL_BS", Auxiliar.nzVacio(db_PRCL_BS, f_PRCL_BS));
		request.setAttribute("PRCL_BT", Auxiliar.nzVacio(db_PRCL_BT, f_PRCL_BT));
		request.setAttribute("PRCL_AUTORCUSTODIOINFO", Auxiliar.nzVacio(db_PRCL_AUTORCUSTODIOINFO, f_PRCL_AUTORCUSTODIOINFO));
		request.setAttribute("PRCL_TIPOBOSQUE", Auxiliar.nzVacio(db_PRCL_TIPOBOSQUE, f_PRCL_TIPOBOSQUE));
		
		request.setAttribute("idioma",idioma);
		
		request.setAttribute("opciones_medioacceso_poblado", Auxiliar.cargarOpciones("SELECT MACC_ID, MACC_NOMBRE FROM RED_MEDIOACCESO ORDER BY MACC_NOMBRE", "MACC_ID", "MACC_NOMBRE", Auxiliar.nzVacio(db_PRCL_MEDIOACCESO_POBLADO, f_PRCL_MEDIOACCESO_POBLADO), "", false, true, false));
		request.setAttribute("opciones_medioacceso_campamento", Auxiliar.cargarOpciones("SELECT MACC_ID, MACC_NOMBRE FROM RED_MEDIOACCESO ORDER BY MACC_NOMBRE", "MACC_ID", "MACC_NOMBRE", Auxiliar.nzVacio(db_PRCL_MEDIOACCESO_CAMPAMENTO, f_PRCL_MEDIOACCESO_CAMPAMENTO), "", false, true, false));
		request.setAttribute("opciones_medioacceso_jalon", Auxiliar.cargarOpciones("SELECT MACC_ID, MACC_NOMBRE FROM RED_MEDIOACCESO ORDER BY MACC_NOMBRE", "MACC_ID", "MACC_NOMBRE", Auxiliar.nzVacio(db_PRCL_MEDIOACCESO_JALON, f_PRCL_MEDIOACCESO_JALON), "", false, true, false));
		request.setAttribute("opciones_pais", Auxiliar.cargarOpciones("SELECT PAIS, NOMBRE FROM RED_PAIS_SHAPE ORDER BY NOMBRE", "PAIS", "NOMBRE", Auxiliar.nzVacio(db_PRCL_CONS_PAIS, f_PRCL_CONS_PAIS), "", false, true, false));
		request.setAttribute("opciones_departamento", Auxiliar.cargarOpciones("SELECT CODIGO, NOMBRE FROM RED_DEPTOS_SHAPE ORDER BY NOMBRE", "CODIGO", "NOMBRE", Auxiliar.nzVacio(db_PRCL_DEPARTAMENTO, f_PRCL_DEPARTAMENTO), "", false, true, false));
		request.setAttribute("opciones_municipio", Auxiliar.cargarOpciones("SELECT CODIGO, NOMBRE FROM RED_MUNICIPIOS_SHAPE ORDER BY NOMBRE", "CODIGO", "NOMBRE", Auxiliar.nzVacio(db_PRCL_MUNICIPIO, f_PRCL_MUNICIPIO), "", false, true, false));
		
		request.setAttribute("opciones_posible_SPF1", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_SPF1_POSIBLE, f_PRCL_SPF1_POSIBLE), "", false, true, false));
		
		request.setAttribute("opciones_posible_SPF2", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_SPF2_POSIBLE, f_PRCL_SPF2_POSIBLE), "", false, true, false));
		
		request.setAttribute("opciones_posible_SPF3", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_SPF3_POSIBLE, f_PRCL_SPF3_POSIBLE), "", false, true, false));
		
		request.setAttribute("opciones_posible_SPF4", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_SPF4_POSIBLE, f_PRCL_SPF4_POSIBLE), "", false, true, false));
		
		request.setAttribute("opciones_posible_SPF5", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_SPF5_POSIBLE, f_PRCL_SPF5_POSIBLE), "", false, true, false));

		request.setAttribute("opciones_tipobosque", Auxiliar.cargarOpciones("SELECT TPBS_CONSECUTIVO, TPBS_NOMBRE || ' ' || TPBS_DESCRIPCION AS INFO FROM RED_TIPOBOSQUE ORDER BY TPBS_NOMBRE", "TPBS_CONSECUTIVO", "INFO", Auxiliar.nzVacio(db_PRCL_TIPOBOSQUE, f_PRCL_TIPOBOSQUE), "", false, true, false));
		request.setAttribute("opciones_temporalidad", Auxiliar.cargarOpciones("SELECT TMPR_CONSECUTIVO, TMPR_NOMBRE FROM RED_TEMPORALIDAD ORDER BY TMPR_NOMBRE", "TMPR_CONSECUTIVO", "TMPR_NOMBRE", Auxiliar.nzVacio(db_PRCL_TEMPORALIDAD, f_PRCL_TEMPORALIDAD), "", false, true, false));
		request.setAttribute("opciones_incluir", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_INCLUIR, f_PRCL_INCLUIR), "", false, true, false));
		request.setAttribute("opciones_publica", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_PUBLICA, f_PRCL_PUBLICA), "", false, true, false));
		request.setAttribute("opciones_hab", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_HAB, f_PRCL_HAB), "", false, true, false));
		request.setAttribute("opciones_dap", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_DAP, f_PRCL_DAP), "", false, true, false));
		request.setAttribute("opciones_gps", Auxiliar.cargarOpciones("SELECT SINO_ID, SINO_NOMBRE FROM RED_SINO ORDER BY SINO_NOMBRE", "SINO_ID", "SINO_NOMBRE", Auxiliar.nzVacio(db_PRCL_GPS, f_PRCL_GPS), "", false, true, false));
		request.setAttribute("opciones_eq", Auxiliar.cargarOpciones("SELECT EQAL.EQAL_ID AS EQAL_ID, EQAL.EQAL_LEGIBLE || ' ' || M.MTDL_NOMBRE || ' Nr:' || EQAL.EQAL_CODIGO AS INFO FROM RED_ECUACIONALOMETRICA EQAL INNER JOIN RED_METODOLOGIA M ON EQAL.EQAL_METODOLOGIA=M.MTDL_CONSECUTIVO ORDER BY INFO", "EQAL_ID", "INFO", Auxiliar.nzVacio(db_PRCL_EQ, f_PRCL_EQ), "", false, true, false));

		if (Auxiliar.tieneAlgo(db_PRCL_CONSECUTIVO)) {
			request.setAttribute("etiqueta_SPF1",generarEtiquetaSPF("1", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF2",generarEtiquetaSPF("2", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF3",generarEtiquetaSPF("3", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF4",generarEtiquetaSPF("4", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("etiqueta_SPF5",generarEtiquetaSPF("5", db_PRCL_CONSECUTIVO, session));

			request.setAttribute("track_campamento",generarTrack("CAMPAMENTO", db_PRCL_CONSECUTIVO, session));
			request.setAttribute("track_parcela",generarTrack("PARCELA", db_PRCL_CONSECUTIVO, session));
			
			request.setAttribute("str_individuos", generarStrIndividuos(db_PRCL_CONSECUTIVO, session));

			request.setAttribute("enlace_metadato_parcela", exportarMetadatoParcela(db_PRCL_CONSECUTIVO, session, "xml"));
		}

		dbREDD.desconectarse();
		
		sec.registrarTransaccion(request, 15, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), "", true);

		return request;
	}
	
	
	/**
	 * Método imaginar
	 * 
	 * Escanea la carpeta del individuo y registra las imágenes aún no registradas
	 * 
	 * @param INDV_CONSECUTIVO
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public void imaginar(String PRCL_CONSECUTIVO, HttpSession session)
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
		String carpetaImportacion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_parcelas'", "");
		if (!Auxiliar.tieneAlgo(carpetaImportacion)) {
			carpetaImportacion = ruta_app + File.separator + "imagenes_parcelas";
		}

        String ruta_carpeta = carpetaImportacion + File.separator + PRCL_CONSECUTIVO;
        
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
	                String id_imagen_existente = dbREDD.obtenerDato("SELECT PIMG FROM RED_PARCELA_IMAGEN WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+" NOMBRE='"+nombre_archivo +"'", "");
	                if (id_imagen_existente.equals("")) {
	                	dbREDD.ejecutarSQL("INSERT INTO RED_PARCELA_IMAGEN (PRCL_CONSECUTIVO, NOMBRE) VALUES ("+PRCL_CONSECUTIVO+", '" + nombre_archivo + "')");
	                }
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dbREDD.desconectarse();
	}

	
	/**
	 * Genera un string de individuos
	 * @param PRCL_CONSECUTIVO
	 * @return
	 * @throws Exception 
	 */
	public String generarStrIndividuos(String PRCL_CONSECUTIVO, HttpSession session) 
			throws Exception {
		String metodo = yo + "generarStrIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String str_individuos = "";
		String r = "";
		
		String sql_individuos = "SELECT ";
		sql_individuos += "INDV_CONSECUTIVO,";
		sql_individuos += "INDV_NUMERO_ARBOL,";
		sql_individuos += "INDV_LATITUD,";
		sql_individuos += "INDV_LONGITUD";
		sql_individuos += " FROM RED_INDIVIDUO ";
		sql_individuos += " WHERE INDV_PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
		sql_individuos += " ORDER BY INDV_NUMERO_ARBOL ";

		try {
			ResultSet rset_individuos = dbREDD.consultarBD(sql_individuos);
			
			if (rset_individuos != null)
			{
				String INDV_CONSECUTIVO = "";
				String INDV_NUMERO_ARBOL = "";
				String INDV_LATITUD = "";
				String INDV_LONGITUD = "";

				int i = 0;
				String s = ":=:";
				
				while (rset_individuos.next())
				{
					INDV_CONSECUTIVO = rset_individuos.getString("INDV_CONSECUTIVO");
					INDV_NUMERO_ARBOL = rset_individuos.getString("INDV_NUMERO_ARBOL");
					INDV_LATITUD = rset_individuos.getString("INDV_LATITUD");
					INDV_LONGITUD = rset_individuos.getString("INDV_LONGITUD");
					
					if (i>0) {
						str_individuos += "_";
					}
					
					if (Auxiliar.esLatitud(INDV_LATITUD) && Auxiliar.esLongitud(INDV_LONGITUD)) {
						str_individuos += INDV_CONSECUTIVO +s+ INDV_NUMERO_ARBOL +s+ INDV_LATITUD +s+ INDV_LONGITUD;
					}
					
					i++;
				}
				
				rset_individuos.close();
			}
			else
			{
				r = "El conjunto de resultados retornados para la consulta ["+sql_individuos+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
			}
		} catch (SQLException e) {
			r = "Excepción de SQL ["+sql_individuos+"]: " + e.toString();
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
		} catch (Exception e) {
			r = "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql_individuos;
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
		}
					
		dbREDD.desconectarse();
		return str_individuos;
	}
	
	/**
	 * Genera track desde el tracklog de la parcela
	 * 
	 * @param tipo
	 * @param PRCL_CONSECUTIVO
	 * @param session
	 * @return String con el track
	 * @throws Exception
	 */
	public String generarTrack(String tipo, String PRCL_CONSECUTIVO, HttpSession session) 
			throws Exception {
		String metodo = yo + "generarTrack";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String [] a_segmentos_track = null;
		String track = "";

		try {
			String PRCL_TRACKLOG = dbREDD.obtenerDato("SELECT PRCL_TRACKLOG_"+tipo+" FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "");
			
			a_segmentos_track = PRCL_TRACKLOG.split("_");
			
			int n_segmentos = a_segmentos_track.length;
			
			int i=0;
			
			String [] a_vertice = null;
			
			String lat = "";
			String lon = "";
			
			for (i=0; i<n_segmentos; i++) {
				a_vertice = a_segmentos_track[i].split(",");
				
				if (a_vertice.length == 2) {
					lat = a_vertice[0];
					lon = a_vertice[1];
					
					if (i>0) {
						track += ",";
					}
					
					if (Auxiliar.esLatitud(lat) && Auxiliar.esLongitud(lon)) {
						track += "["+lat+","+lon+"]";
					}
				}
			}
			
			//track = "[" + track + "]";
			track = track;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return track;
	}
	
	/**
	 * Funcion que retorna una etiqueta de una subparcela fustal
	 * @param n
	 * @param PRCL_CONSECUTIVO
	 * @return String de la etiqueta
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String generarEtiquetaSPF(String n, String PRCL_CONSECUTIVO, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "generarEtiquetaSPF";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String s = "";
		
		String PRCL_SPF_DILIGENCIA = "";
		String PRCL_SPF_FECHAINI = "";
		String PRCL_SPF_FECHAFIN = "";
		String PRCL_SPF_POSIBLE = "";
		String PRCL_SPF_JUSTIFICACION_NO = "";
		String PRCL_SPF_OBS_FUSTALES = "";
		String PRCL_SPF_OBS_LATIZALES = "";
		String PRCL_SPF_OBS_BRINZALES = "";
		
		String sql = "SELECT PRCL_SPF" + n + "_DILIGENCIA, PRCL_SPF" + n + "_FECHAINI, PRCL_SPF" + n + "_FECHAFIN, PRCL_SPF" + n + "_POSIBLE, PRCL_SPF" + n + "_JUSTIFICACION_NO, PRCL_SPF" + n + "_OBS_FUSTALES, PRCL_SPF" + n + "_OBS_LATIZALES, PRCL_SPF" + n + "_OBS_BRINZALES FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				rset.next();
				
				PRCL_SPF_DILIGENCIA = rset.getString("PRCL_SPF" + n + "_DILIGENCIA");
				PRCL_SPF_FECHAINI = rset.getString("PRCL_SPF" + n + "_FECHAINI");
				PRCL_SPF_FECHAFIN = rset.getString("PRCL_SPF" + n + "_FECHAFIN");
				PRCL_SPF_POSIBLE = rset.getString("PRCL_SPF" + n + "_POSIBLE");
				PRCL_SPF_JUSTIFICACION_NO = rset.getString("PRCL_SPF" + n + "_JUSTIFICACION_NO");
				PRCL_SPF_OBS_FUSTALES = rset.getString("PRCL_SPF" + n + "_OBS_FUSTALES");
				PRCL_SPF_OBS_LATIZALES = rset.getString("PRCL_SPF" + n + "_OBS_LATIZALES");
				PRCL_SPF_OBS_BRINZALES = rset.getString("PRCL_SPF" + n + "_OBS_BRINZALES");
				
				s = "<div><h4>Subparcela Fustal "+n+"</h4></div>";
				s += "<div style=\"width: 200px; height: 150px; overflow: scroll;\">";
				s += "<div><b>CC Diligenció</b>:"+PRCL_SPF_DILIGENCIA+"</div>";
				s += "<div><b>Fecha de inicio</b>:"+PRCL_SPF_FECHAINI+"</div>";
				s += "<div><b>Fecha de finalización</b>:"+PRCL_SPF_FECHAFIN+"</div>";
				s += "<div><b>Levantamiento posible</b>:"+PRCL_SPF_POSIBLE+"</div>";
				s += "<div><b>Justificación de no levantamiento</b>:"+PRCL_SPF_JUSTIFICACION_NO+"</div>";
				s += "<div><b>Observaciones sobre fustales</b>:"+PRCL_SPF_OBS_FUSTALES+"</div>";
				s += "<div><b>Observaciones sobre latizales</b>:"+PRCL_SPF_OBS_LATIZALES+"</div>";
				s += "<div><b>Observaciones sobre brinzales</b>:"+PRCL_SPF_OBS_BRINZALES+"</div>";
				s += "</div>";
				
				s = s.replace("\n", "");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return s;
	}
	

	

	/**
	 * Método que inicializa los valores de los campos para un individuo especificado con f_codigo.
	 * 
	 * @param f_codigo
	 * @return String r con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public ResultSet cargarRegistro(String PRCL_CONSECUTIVO, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "cargarRegistro";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String w_codigo = "";

		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO))
		{
			w_codigo = " AND PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
		}
		else
		{
			t = Auxiliar.traducir(yo+"Codigo_Parcela_No_Especificado", idioma, "Creación de una nueva parcela:" + "..");
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
		}

		String sql = "SELECT ";
		sql += "PRCL_ID_IMPORTACION,";
		sql += "PRCL_CONSECUTIVO,";
		sql += "PRCL_ID_UPM,";
		sql += "PRCL_NOMBRE,";
		sql += "PRCL_USR_DILIGENCIA_F1,";
		sql += "PRCL_USR_DILIGENCIA_F2,";
		sql += "TO_CHAR(PRCL_FECHAINI_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_APROXIMACION,";
		sql += "TO_CHAR(PRCL_FECHAFIN_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_APROXIMACION,";
		sql += "TO_CHAR(PRCL_FECHAINI_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_LOCALIZACION,";
		sql += "TO_CHAR(PRCL_FECHAFIN_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_LOCALIZACION,";
		sql += "PRCL_DESCRIPCION,";
		sql += "PRCL_OBSERVACIONES,";
		sql += "PRCL_TRACKLOG_CAMPAMENTO,";
		sql += "PRCL_TRACKLOG_PARCELA,";
		sql += "PRCL_DISTANCIA_POBLADO,";
		sql += "PRCL_MEDIOACCESO_POBLADO,";
		sql += "PRCL_TPOBLADO_H,";
		sql += "PRCL_TPOBLADO_M,";
		sql += "PRCL_DISTANCIA_CAMPAMENTO,";
		sql += "PRCL_MEDIOACCESO_CAMPAMENTO,";
		sql += "PRCL_TCAMPAMENTO_H,";
		sql += "PRCL_TCAMPAMENTO_M,";
		sql += "PRCL_DISTANCIA_JALON,";
		sql += "PRCL_MEDIOACCESO_JALON,";
		sql += "PRCL_TJALON_H,";
		sql += "PRCL_TJALON_M,";
		sql += "PRCL_DISTANCIA_CAMPAMENTOS,";
		sql += "PRCL_LATITUD,";
		sql += "PRCL_LONGITUD,";
		sql += "PRCL_ALTITUD,";
		sql += "PRCL_CONS_PAIS,";
		sql += "PRCL_DEPARTAMENTO,";
		sql += "PRCL_MUNICIPIO,";
		sql += "PRCL_NOMBREARCHIVO,";
		sql += "PRCL_SPF1_DILIGENCIA,";
		sql += "TO_CHAR(PRCL_SPF1_FECHAINI, 'YYYY-MM-DD') AS PRCL_SPF1_FECHAINI,";
		sql += "TO_CHAR(PRCL_SPF1_FECHAFIN, 'YYYY-MM-DD') AS PRCL_SPF1_FECHAFIN,";
		sql += "PRCL_SPF1_POSIBLE,";
		sql += "PRCL_SPF1_JUSTIFICACION_NO,";
		sql += "PRCL_SPF1_OBS_FUSTALES,";
		sql += "PRCL_SPF1_OBS_LATIZALES,";
		sql += "PRCL_SPF1_OBS_BRINZALES,";
		sql += "PRCL_SPF2_DILIGENCIA,";
		sql += "TO_CHAR(PRCL_SPF2_FECHAINI, 'YYYY-MM-DD') AS PRCL_SPF2_FECHAINI,";
		sql += "TO_CHAR(PRCL_SPF2_FECHAFIN, 'YYYY-MM-DD') AS PRCL_SPF2_FECHAFIN,";
		sql += "PRCL_SPF2_POSIBLE,";
		sql += "PRCL_SPF2_JUSTIFICACION_NO,";
		sql += "PRCL_SPF2_OBS_FUSTALES,";
		sql += "PRCL_SPF2_OBS_LATIZALES,";
		sql += "PRCL_SPF2_OBS_BRINZALES,";
		sql += "PRCL_SPF3_DILIGENCIA,";
		sql += "TO_CHAR(PRCL_SPF3_FECHAINI, 'YYYY-MM-DD') AS PRCL_SPF3_FECHAINI,";
		sql += "TO_CHAR(PRCL_SPF3_FECHAFIN, 'YYYY-MM-DD') AS PRCL_SPF3_FECHAFIN,";
		sql += "PRCL_SPF3_POSIBLE,";
		sql += "PRCL_SPF3_JUSTIFICACION_NO,";
		sql += "PRCL_SPF3_OBS_FUSTALES,";
		sql += "PRCL_SPF3_OBS_LATIZALES,";
		sql += "PRCL_SPF3_OBS_BRINZALES,";
		sql += "PRCL_SPF4_DILIGENCIA,";
		sql += "TO_CHAR(PRCL_SPF4_FECHAINI, 'YYYY-MM-DD') AS PRCL_SPF4_FECHAINI,";
		sql += "TO_CHAR(PRCL_SPF4_FECHAFIN, 'YYYY-MM-DD') AS PRCL_SPF4_FECHAFIN,";
		sql += "PRCL_SPF4_POSIBLE,";
		sql += "PRCL_SPF4_JUSTIFICACION_NO,";
		sql += "PRCL_SPF4_OBS_FUSTALES,";
		sql += "PRCL_SPF4_OBS_LATIZALES,";
		sql += "PRCL_SPF4_OBS_BRINZALES,";
		sql += "PRCL_SPF5_DILIGENCIA,";
		sql += "TO_CHAR(PRCL_SPF5_FECHAINI, 'YYYY-MM-DD') AS PRCL_SPF5_FECHAINI,";
		sql += "TO_CHAR(PRCL_SPF5_FECHAFIN, 'YYYY-MM-DD') AS PRCL_SPF5_FECHAFIN,";
		sql += "PRCL_SPF5_POSIBLE,";
		sql += "PRCL_SPF5_JUSTIFICACION_NO,";
		sql += "PRCL_SPF5_OBS_FUSTALES,";
		sql += "PRCL_SPF5_OBS_LATIZALES,";
		sql += "PRCL_SPF5_OBS_BRINZALES,";
		sql += "PRCL_PLOT,";
		sql += "PRCL_AREA,";
		sql += "PRCL_INCLUIR,";
		sql += "PRCL_TEMPORALIDAD,";
		sql += "PRCL_PUBLICA,";
		sql += "PRCL_HAB,";
		sql += "PRCL_DAP,";
		sql += "PRCL_GPS,";
		sql += "PRCL_EQ,";
		sql += "PRCL_BA,";
		sql += "PRCL_BS,";
		sql += "PRCL_BT,";
		sql += "PRCL_AUTORCUSTODIOINFO,";
		sql += "PRCL_TIPOBOSQUE,";
		sql += "'Holdridge:' || TPBS_NOMBRE || '<br>=>' || TPBS_DESCRIPCION AS TIPOBOSQUE,";
		sql += "RED_CORPORACIONES.CAR AS CAR,";
		sql += "'Nombre:-' || RED_RESGUARDO_INDIGENA.RINOMBRE || '<br>Etnia:-' || RED_RESGUARDO_INDIGENA.RIETNIA AS RESGUARDOINDIGENA,";
		sql += "RED_PARQUES_NACIONALES.NOMBRE_PN AS PNN";
		sql += " FROM RED_PARCELA ";
		sql += " LEFT OUTER JOIN RED_TIPOBOSQUE ON RED_PARCELA.PRCL_CONS_TIPOBOSQUE=RED_TIPOBOSQUE.TPBS_CONSECUTIVO ";
		sql += " LEFT OUTER JOIN RED_RESGUARDO_INDIGENA ON RED_PARCELA.PRCL_CONS_RESGUARDOINDIGENA=RED_RESGUARDO_INDIGENA.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_PARQUES_NACIONALES ON RED_PARCELA.PRCL_CONS_PNN=RED_PARQUES_NACIONALES.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_CORPORACIONES ON RED_PARCELA.PRCL_CONS_CAR=RED_CORPORACIONES.ID_CAR ";
		sql += " WHERE 1=1 ";
		sql += w_codigo;

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null) {
				return rset;
			}
			else {
				t = "No se encontraron resultados.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
			}
		} catch (SQLException e) {
			t = "Excepción de SQL ["+sql+"]: " + e.toString();
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
		} catch (Exception e) {
			t = "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql;
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
		}
		
		dbREDD.desconectarse();
		return null;
	}
	
	
	/**
	 * Metodo generarMapaParcela
	 * 
	 * Genera una imagen del mapa de la parcela
	 * 
	 * @param f_PRCL_CONSECUTIVO
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	
	/*
	public boolean generarMapaParcela(String f_PRCL_CONSECUTIVO) 
			throws SQLException, IOException {
		String html = "";
		
		String CX = dbREDD.obtenerDato("SELECT AVG(t.X) AS CX FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-73");
		String CY = dbREDD.obtenerDato("SELECT AVG(t.Y) AS CY FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "4");
		String S = dbREDD.obtenerDato("SELECT MIN(t.Y) AS S FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-73");
		String W = dbREDD.obtenerDato("SELECT MIN(t.X) AS W FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "4");
		String N = dbREDD.obtenerDato("SELECT MAX(t.Y) AS N FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-73");
		String E = dbREDD.obtenerDato("SELECT MAX(t.X) AS E FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "4");
		
		try {
			html = "";
			html += "<!DOCTYPE html>";
			html += "<html>";
			html += "<head>";
			html += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
			
			//html += "<link type='text/css' rel='stylesheet' href='../css/estilos.css' media='all' />";
			
			html += "<link rel='stylesheet' href='leaflet.css' />";
			html += "<script src='jquery-1.11.0.min.js'></script>";
			html += "</head>";
			html += "<body style='text-align: center;'>";
			
			
			html += "<div id='map' style='width: 540px; height: 800px'></div>";
			html += "<div id='viewparams' style='color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;'></div>";
			
			html += "<script type='text/javascript' src='leaflet.js'></script>";
			html += "<script type='text/javascript'>";
			html += "var str_viewparams = '';";
			html += "var PRCL_CONSECUTIVO = '" + f_PRCL_CONSECUTIVO + "';";
			html += "var w_PRCL_CONSECUTIVO = '';";
			html += "if (PRCL_CONSECUTIVO.length > 0) {";
			html += "w_PRCL_CONSECUTIVO = ' AND PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO + " ';";
			html += "str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';';"; 
			html += "}";
			
			html += "if (str_viewparams.length>0) {";
			html += "str_viewparams = str_viewparams.slice(0,-1);";
			html += "}";
			
			html += "var map = L.map('map').setView(["+CY+", "+CX+"], 8);";
			html += "map.touchZoom.disable();";
			html += "map.doubleClickZoom.disable();";
			html += "map.scrollWheelZoom.disable();";
			html += "map.boxZoom.disable();";
			html += "map.keyboard.disable();";
			html += "$('.leaflet-control-zoom').css('visibility', 'hidden');";
			
			html += "L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {";
			html += "maxZoom: 18,";
			html += "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores',";
			html += "id: 'examples.map-i875mjb7'";
			html += "}).addTo(map);";
			
			html += "var parcelas = L.tileLayer.wms('http://54.172.131.5:8080/geoserver/OracleAmazon/wms', {";
			html += "layers: 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',";
			html += "format: 'image/png',";
			html += "transparent: true,";
			html += "version: '1.1.0',";
			html += "viewparams: str_viewparams,"; 
			html += "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores'";
			html += "});";
			html += "parcelas.addTo(map);";
			html += "map.fitBounds([["+S+","+W+"],["+N+","+E+"]]);";
			*/
			/*
			html += "";
			html += "var owsrootUrl = 'http://54.172.131.5:8080/geoserver/OracleAmazon/wfs';";
	
			html += "var defaultParameters = {";
			html += "service : 'WFS',";
			html += "version : '1.0.0',";
			html += "request : 'GetFeature',";
			html += "typeName : 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',";
			html += "outputFormat : 'text/javascript',";
			html += "format_options : 'callback:getJson',";
			html += "viewparams: str_viewparams, ";
			html += "srs : 'EPSG:3116'";
			html += "};";
	
			html += "var parameters = L.Util.extend(defaultParameters);";
			html += "var URL = owsrootUrl + L.Util.getParamString(parameters);";
			
			html += "var WFSLayer = null;";
			html += "var ajax = $.ajax({";
			html += "url : URL,";
			html += "dataType : 'jsonp',";
			html += "jsonpCallback : 'getJson',";
			html += "success : function (response) {";
			html += "WFSLayer = L.geoJson(response, {";
			html += "style: function (feature) {";
			html += "return {";
			html += "stroke: true,";
			html += "fillColor: '#99FF99',";
			html += "opacity: 80,";
			html += "color: '#228822'";
			html += "};";
			html += "}";
			html += "}).addTo(map);";
			html += "map.fitBounds(WFSLayer.getBounds());";
			html += "}";
			html += "});";
			 */
			/*
			html += "map.setZoom(map.getZoom()-2);";
			
			html += "setTimeout(function(){window.status='ya';}, 1000);";
			
			html += "</script>";
			
			html += "</body>";
			html += "</html>";
			
			String ruta_app = getServletContext().getRealPath(""); 
			String carpeta = ruta_app + File.separator + "mapas_parcelas/";
			
			String ruta_html = carpeta + f_PRCL_CONSECUTIVO + "-mapa.html";
			String ruta_jpg = carpeta + f_PRCL_CONSECUTIVO + "-mapa.jpg";
			
			boolean ok_crear_archivo_html = archie.escribirArchivo(ruta_html, html);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Archivo html creado en: " + ruta_html);
			
			if (ok_crear_archivo_html) {
				String commando_generacion = "wkhtmltoimage --width 555 --window-status ya -f jpg --quality 100 --encoding UTF-8 --javascript-delay 1000 --no-stop-slow-scripts " + ruta_html + " " + ruta_jpg;
				String resultado_generacion = Auxiliar.commander(commando_generacion, commando_generacion);
				
				String [] a_resultado_generacion = resultado_generacion.split("-=-");
				
				String r = "";
				
				if (!Auxiliar.nz(a_resultado_generacion[0], "").equals("0")) {
					r = "No se pudo generar la imagen: " + resultado_generacion;
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
					return false;
				}
				else {
					r = "Generación de mapa de la parcela " + f_PRCL_CONSECUTIVO + " exitosa [" + resultado_generacion + "].";
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + r);
					//archie.eliminarArchivo(ruta_html);
					return archie.existeArchivo(ruta_jpg);
				}
			}
		}
		catch (Exception e) {
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + e.toString());
		}
		
		return false;
	}
	*/
	
	
	/**
	 * Genera una imagen del mapa de la parcela
	 * 
	 * @param f_PRCL_CONSECUTIVO
	 * @return
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean generarMapaParcela(String f_PRCL_CONSECUTIVO, HttpSession session) 
			throws ClassNotFoundException, Exception {
		String metodo = yo + "generarMapaParcela";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		
		String html = "";
		
		String CX = dbREDD.obtenerDato("SELECT PRCL_LONGITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-73");
		String CY = dbREDD.obtenerDato("SELECT PRCL_LATITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "4");
		
		try {
			html = "";
			html += "<!DOCTYPE html>";
			html += "\n" + "<html>";
			html += "\n" + "<head>";
			html += "\n" + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
			
			html += "\n" + "<link rel='stylesheet' href='leaflet.css' />";
			html += "\n" + "<script src='jquery-1.11.0.min.js'></script>";
			html += "\n" + "</head>";
			html += "\n" + "<body style='text-align: center;'>";
			
			
			html += "\n" + "<div id='map' style='width: 540px; height: 800px'></div>";
			html += "\n" + "<div id='viewparams' style='color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;'></div>";
			
			html += "\n" + "<script type='text/javascript' src='leaflet.js'></script>";
			html += "\n" + "<script type='text/javascript'>";
			html += "\n" + "var str_viewparams = '';";
			html += "\n" + "var PRCL_CONSECUTIVO = '" + f_PRCL_CONSECUTIVO + "';";
			html += "\n" + "var w_PRCL_CONSECUTIVO = '';";
			html += "\n" + "if (PRCL_CONSECUTIVO.length > 0) {";
			html += "\n" + "w_PRCL_CONSECUTIVO = ' AND PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO + " ';";
			html += "\n" + "str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';';"; 
			html += "\n" + "}";
			
			html += "\n" + "if (str_viewparams.length>0) {";
			html += "\n" + "str_viewparams = str_viewparams.slice(0,-1);";
			html += "\n" + "}";
			
			html += "\n" + "var map = L.map('map').setView(["+CY+", "+CX+"], 16);";
			html += "\n" + "map.touchZoom.disable();";
			html += "\n" + "map.doubleClickZoom.disable();";
			html += "\n" + "map.scrollWheelZoom.disable();";
			html += "\n" + "map.boxZoom.disable();";
			html += "\n" + "map.keyboard.disable();";
			html += "\n" + "$('.leaflet-control-zoom').css('visibility', 'hidden');";
			
			html += "\n" + "L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {";
			html += "\n" + "maxZoom: 18,";
			html += "\n" + "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores',";
			html += "\n" + "id: 'examples.map-i875mjb7'";
			html += "\n" + "}).addTo(map);";
			
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
			
			String str_individuos = generarStrIndividuos(f_PRCL_CONSECUTIVO, session);
			
			html += "\n" + "var str_individuos = '" + str_individuos + "';";
			html += "\n" + "var a_individuos = str_individuos.split('_');";

			html += "\n" + "var n_individuos = a_individuos.length;";

			html += "\n" + "var i = 0;";

			html += "\n" + "for (i=0; i<n_individuos; i++) {";
			html += "\n" + "a_individuo = a_individuos[i].split(':=:');";
			
			html += "\n" + "if (a_individuo.length == 4) {";

			html += "\n" + "numero_arbol = a_individuo[0];";
			html += "\n" + "INDV_CONSECUTIVO = a_individuo[1];";
			html += "\n" + "lat = a_individuo[2];";
			html += "\n" + "lon = a_individuo[3];";

			html += "\n" + "etiqueta_individuo = \"<a class=boton href='Individuo?accion=detalle_individuo&INDV_PRCL_CONSECUTIVO=\"+PRCL_CONSECUTIVO+\"&INDV_CONSECUTIVO=\"+INDV_CONSECUTIVO+\"' target=_blank>\"+numero_arbol+\"</a>\";";
				
			html += "\n" + "if (lat != '' && lon != '') {";
			html += "\n" + "a_coordenadas = [lat, lon];";
			html += "\n" + "arbolito = L.icon({";
			html += "\n" + "iconUrl: 'images/marker-icon.png',";
			html += "\n" + "shadowUrl: 'images/marker-shadow.png',";
				
			html += "\n" + "iconSize:     [10, 20], // size of the icon";
			html += "\n" + "shadowSize:   [20, 20], // size of the shadow";
			html += "\n" + "iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location";
			html += "\n" + "shadowAnchor: [5, 20],  // the same for the shadow";
			html += "\n" + "popupAnchor:  [0, -20] // point from which the popup should open relative to the iconAnchor";
			html += "\n" + "});";
				
			html += "\n" + "L.marker(a_coordenadas, {icon: arbolito}).addTo(map).bindPopup(etiqueta_individuo);";
			html += "\n" + "}";
			html += "\n" + "}";
			html += "\n" + "}";

			html += "\n" + "setTimeout(function(){window.status='ya';}, 1000);";
			
			html += "\n" + "</script>";
			
			html += "\n" + "</body>";
			html += "\n" + "</html>";
			
			String ruta_app = getServletContext().getRealPath(""); 
			String carpeta = ruta_app + File.separator + "mapas_parcelas/";
			
			String ruta_html = carpeta + f_PRCL_CONSECUTIVO + "-mapa.html";
			String ruta_jpg = carpeta + f_PRCL_CONSECUTIVO + "-mapa.png";
			
			boolean ok_crear_archivo_html = archie.escribirArchivo(ruta_html, html);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Archivo html creado en: " + ruta_html);
			
			if (ok_crear_archivo_html) {
				String commando_generacion = "wkhtmltoimage --width 555 --window-status ya -f png --quality 100 --encoding UTF-8 --javascript-delay 1000 --no-stop-slow-scripts " + ruta_html + " " + ruta_jpg;
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
					r = "Generación de mapa de la parcela " + f_PRCL_CONSECUTIVO + " exitosa [" + resultado_generacion + "].";
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
	 * Genera un documento html que visualiza el tracklog de la parcela
	 * 
	 * @param f_PRCL_CONSECUTIVO
	 * @return true si lo pudo generar, false de lo contrario
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean generarMapaTrackLogParcela(String f_PRCL_CONSECUTIVO, HttpSession session) 
			throws ClassNotFoundException, Exception {
		String metodo = yo + "generarMapaTrackLogParcela";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
	    
		String html = "";
		
		String CX = dbREDD.obtenerDato("SELECT PRCL_LONGITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-73");
		String CY = dbREDD.obtenerDato("SELECT PRCL_LATITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "4");
		
		try {
			html = "";
			html += "<!DOCTYPE html>";
			html += "\n" + "<html>";
			html += "\n" + "<head>";
			html += "\n" + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
			
			html += "\n" + "<link rel='stylesheet' href='leaflet.css' />";
			html += "\n" + "<script src='jquery-1.11.0.min.js'></script>";
			html += "\n" + "</head>";
			html += "\n" + "<body style='text-align: center;'>";
			
			
			html += "\n" + "<div id='map' style='width: 540px; height: 400px'></div>";
			html += "\n" + "<div id='viewparams' style='color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;'></div>";
			
			html += "\n" + "<script type='text/javascript' src='leaflet.js'></script>";
			html += "\n" + "<script type='text/javascript'>";
			html += "\n" + "var str_viewparams = '';";
			html += "\n" + "var PRCL_CONSECUTIVO = '" + f_PRCL_CONSECUTIVO + "';";
			html += "\n" + "var w_PRCL_CONSECUTIVO = '';";
			html += "\n" + "if (PRCL_CONSECUTIVO.length > 0) {";
			html += "\n" + "w_PRCL_CONSECUTIVO = ' AND PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO + " ';";
			html += "\n" + "str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';';"; 
			html += "\n" + "}";
			
			html += "\n" + "if (str_viewparams.length>0) {";
			html += "\n" + "str_viewparams = str_viewparams.slice(0,-1);";
			html += "\n" + "}";
			
			html += "\n" + "var map = L.map('map').setView(["+CY+", "+CX+"], 16);";
			html += "\n" + "map.touchZoom.disable();";
			html += "\n" + "map.doubleClickZoom.disable();";
			html += "\n" + "map.scrollWheelZoom.disable();";
			html += "\n" + "map.boxZoom.disable();";
			html += "\n" + "map.keyboard.disable();";
			html += "\n" + "$('.leaflet-control-zoom').css('visibility', 'hidden');";
			
			html += "\n" + "L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {";
			html += "\n" + "maxZoom: 18,";
			html += "\n" + "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores',";
			html += "\n" + "id: 'examples.map-i875mjb7'";
			html += "\n" + "}).addTo(map);";
			
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
			
			String track_campamento = generarTrack("CAMPAMENTO", f_PRCL_CONSECUTIVO, session);
			html += "\n" + "var track_campamento = ["+track_campamento+"];";
			html += "\n" + "if (track_campamento.length > 2) {";
			html += "\n" + "polyline_campamento = L.polyline(track_campamento, {color: 'red'}).addTo(map);";
			html += "\n" + "}";
			
			String track_parcela = generarTrack("PARCELA", f_PRCL_CONSECUTIVO, session);
			html += "\n" + "var track_parcela = ["+track_parcela+"];";
			html += "\n" + "if (track_parcela.length > 2) {";
			html += "\n" + "polyline_parcela = L.polyline(track_parcela, {color: 'blue'}).addTo(map);";
			html += "\n" + "map.fitBounds(polyline_parcela.getBounds());";
			html += "\n" + "}";
			
			html += "\n" + "setTimeout(function(){window.status='ya';}, 1000);";
			
			html += "\n" + "</script>";

			// IMPRIMIR TRACKLOG
			String PRCL_TRACKLOG_PARCELA = dbREDD.obtenerDato("SELECT PRCL_TRACKLOG_PARCELA FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+f_PRCL_CONSECUTIVO, "");
			html += "\n" + "<h4>Coordenadas:</h4>";
			html += "\n" + "<p>"+PRCL_TRACKLOG_PARCELA.replace("_", "<br/>")+"</p>";

			html += "\n" + "</body>";
			html += "\n" + "</html>";
			
			String ruta_app = getServletContext().getRealPath(""); 
			String carpeta = ruta_app + File.separator + "mapas_parcelas/";
			
			String ruta_html = carpeta + f_PRCL_CONSECUTIVO + "-mapa-tracklog-parcela.html";
			String ruta_jpg = carpeta + f_PRCL_CONSECUTIVO + "-mapa-tracklog-parcela.png";
			
			boolean ok_crear_archivo_html = archie.escribirArchivo(ruta_html, html);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Archivo html creado en: " + ruta_html);
			
			if (ok_crear_archivo_html) {
				String commando_generacion = "wkhtmltoimage --width 555 --window-status ya -f png --quality 100 --encoding UTF-8 --javascript-delay 1000 --no-stop-slow-scripts " + ruta_html + " " + ruta_jpg;
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
					r = "Generación de mapa de tracklog a la parcela " + f_PRCL_CONSECUTIVO + " exitosa [" + resultado_generacion + "].";
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
	 * Genera un archivo html con el mapa del tracklog hacia el campamento
	 * 
	 * @param f_PRCL_CONSECUTIVO
	 * @param session
	 * @return true si lo pudo generar, false de lo contrario
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public boolean generarMapaTrackLogCampamento(String f_PRCL_CONSECUTIVO, HttpSession session) 
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarMapaTrackLogCampamento";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		
		String html = "";
		
		String CX = dbREDD.obtenerDato("SELECT PRCL_LONGITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-73");
		String CY = dbREDD.obtenerDato("SELECT PRCL_LATITUD FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "4");
		
		try {
			html = "";
			html += "<!DOCTYPE html>";
			html += "\n" + "<html>";
			html += "\n" + "<head>";
			html += "\n" + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
	
			html += "\n" + "<link rel='stylesheet' href='leaflet.css' />";
			html += "\n" + "<script src='jquery-1.11.0.min.js'></script>";
			html += "\n" + "</head>";
			html += "\n" + "<body style='text-align: center;'>";
	
	
			html += "\n" + "<div id='map' style='width: 540px; height: 400px'></div>";
			html += "\n" + "<div id='viewparams' style='color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;'></div>";
	
			html += "\n" + "<script type='text/javascript' src='leaflet.js'></script>";
			html += "\n" + "<script type='text/javascript'>";
			html += "\n" + "var str_viewparams = '';";
			html += "\n" + "var PRCL_CONSECUTIVO = '" + f_PRCL_CONSECUTIVO + "';";
			html += "\n" + "var w_PRCL_CONSECUTIVO = '';";
			html += "\n" + "if (PRCL_CONSECUTIVO.length > 0) {";
			html += "\n" + "w_PRCL_CONSECUTIVO = ' AND PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO + " ';";
			html += "\n" + "str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';';"; 
			html += "\n" + "}";
	
			html += "\n" + "if (str_viewparams.length>0) {";
			html += "\n" + "str_viewparams = str_viewparams.slice(0,-1);";
			html += "\n" + "}";
			
			html += "\n" + "var map = L.map('map').setView(["+CY+", "+CX+"], 16);";
			html += "\n" + "map.touchZoom.disable();";
			html += "\n" + "map.doubleClickZoom.disable();";
			html += "\n" + "map.scrollWheelZoom.disable();";
			html += "\n" + "map.boxZoom.disable();";
			html += "\n" + "map.keyboard.disable();";
			html += "\n" + "$('.leaflet-control-zoom').css('visibility', 'hidden');";
											
			html += "\n" + "L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {";
			html += "\n" + "maxZoom: 18,";
			html += "\n" + "attribution: 'Map data &copy; OpenStreetMap contributors, CC-BY-SA, Imagery &copy; Mapbox, Parcelas/Plots &copy; IDEAM y/o Colaboradores',";
			html += "\n" + "id: 'examples.map-i875mjb7'";
			html += "\n" + "}).addTo(map);";

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

			String track_campamento = generarTrack("CAMPAMENTO", f_PRCL_CONSECUTIVO, session);
			html += "\n" + "var track_campamento = ["+track_campamento+"];";
			html += "\n" + "if (track_campamento.length > 2) {";
			html += "\n" + "polyline_campamento = L.polyline(track_campamento, {color: 'red'}).addTo(map);";
			html += "\n" + "map.fitBounds(polyline_campamento.getBounds());";
			html += "\n" + "}";
			
			String track_parcela = generarTrack("PARCELA", f_PRCL_CONSECUTIVO, session);
			html += "\n" + "var track_parcela = ["+track_parcela+"];";
			html += "\n" + "if (track_parcela.length > 2) {";
			html += "\n" + "polyline_parcela = L.polyline(track_parcela, {color: 'blue'}).addTo(map);";
			html += "\n" + "}";
			
			
			html += "\n" + "setTimeout(function(){window.status='ya';}, 1000);";
			
			html += "\n" + "</script>";
			
			// IMPRIMIR TRACKLOG
			String PRCL_TRACKLOG_CAMPAMENTO = dbREDD.obtenerDato("SELECT PRCL_TRACKLOG_CAMPAMENTO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+f_PRCL_CONSECUTIVO, "");
			html += "\n" + "<h4>Coordenadas:</h4>";
			html += "\n" + "<p>"+PRCL_TRACKLOG_CAMPAMENTO.replace("_", "<br/>")+"</p>";
			
			html += "\n" + "</body>";
			html += "\n" + "</html>";
			
			String ruta_app = getServletContext().getRealPath(""); 
		    String carpeta = ruta_app + File.separator + "mapas_parcelas/";
		    
		    String ruta_html = carpeta + f_PRCL_CONSECUTIVO + "-mapa-tracklog-campamento.html";
		    String ruta_jpg = carpeta + f_PRCL_CONSECUTIVO + "-mapa-tracklog-campamento.png";
		    
			boolean ok_crear_archivo_html = archie.escribirArchivo(ruta_html, html);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Archivo html creado en: " + ruta_html);
			
			if (ok_crear_archivo_html) {
				String commando_generacion = "wkhtmltoimage --width 555 --window-status ya -f png --quality 100 --encoding UTF-8 --javascript-delay 1000 --no-stop-slow-scripts " + ruta_html + " " + ruta_jpg;
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
					r = "Generación de mapa de tracklog al campamento " + f_PRCL_CONSECUTIVO + " exitosa [" + resultado_generacion + "].";
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
	 * Método que genera la ficha en PDF de una parcela.
	 * 
	 * @param f_PRCL_CONSECUTIVO Consecutivo de la parcela en la bd REDDD
	 * @return String r con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String exportarMetadatoParcela(String f_PRCL_CONSECUTIVO, HttpSession session, String modo)
			throws ClassNotFoundException, Exception {
		String metodo = yo + "exportarMetadatoParcela";
		
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
		
		/*
		if (!sec.tienePermiso(usuario, "122")
				&& !siga
				) {
			r = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
			dbREDD.desconectarse();
			return r;
		}
		*/
		
		if (!Auxiliar.tieneAlgo(f_PRCL_CONSECUTIVO)) {
			r = Auxiliar.traducir(yo+"Codigo_Parcela_No_Especificado", idioma, "Código del individuo no especificado." + "..");
			dbREDD.desconectarse();
			return r;
		}
		
		String sql = "SELECT ";
		sql += "PRCL_ID_IMPORTACION,";
		sql += "PRCL_CONSECUTIVO,";
		sql += "PRCL_ID_UPM,";
		sql += "PRCL_NOMBRE,";
		sql += "PRCL_USR_DILIGENCIA_F1,";
		sql += "PRCL_USR_DILIGENCIA_F2,";
		sql += "TO_CHAR(PRCL_FECHAINI_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_APROXIMACION,";
		sql += "TO_CHAR(PRCL_FECHAFIN_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_APROXIMACION,";
		sql += "TO_CHAR(PRCL_FECHAINI_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_LOCALIZACION,";
		sql += "TO_CHAR(PRCL_FECHAFIN_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_LOCALIZACION,";
		sql += "PRCL_DESCRIPCION,";
		sql += "PRCL_OBSERVACIONES,";
		sql += "PRCL_TRACKLOG_CAMPAMENTO,";
		sql += "PRCL_TRACKLOG_PARCELA,";
		sql += "PRCL_DISTANCIA_POBLADO,";
		sql += "PRCL_MEDIOACCESO_POBLADO,";
		sql += "PRCL_TPOBLADO_H,";
		sql += "PRCL_TPOBLADO_M,";
		sql += "PRCL_DISTANCIA_CAMPAMENTO,";
		sql += "PRCL_MEDIOACCESO_CAMPAMENTO,";
		sql += "PRCL_TCAMPAMENTO_H,";
		sql += "PRCL_TCAMPAMENTO_M,";
		sql += "PRCL_DISTANCIA_JALON,";
		sql += "PRCL_MEDIOACCESO_JALON,";
		sql += "PRCL_TJALON_H,";
		sql += "PRCL_TJALON_M,";
		sql += "PRCL_DISTANCIA_CAMPAMENTOS,";
		sql += "PRCL_LATITUD,";
		sql += "PRCL_LONGITUD,";
		sql += "PRCL_ALTITUD,";
		sql += "PRCL_CONS_PAIS,";
		sql += "PRCL_DEPARTAMENTO,";
		sql += "PRCL_MUNICIPIO,";
		sql += "PRCL_NOMBREARCHIVO,";
		sql += "PRCL_SPF1_DILIGENCIA,";
		sql += "PRCL_SPF1_FECHAINI,";
		sql += "PRCL_SPF1_FECHAFIN,";
		sql += "PRCL_SPF1_POSIBLE,";
		sql += "PRCL_SPF1_JUSTIFICACION_NO,";
		sql += "PRCL_SPF1_OBS_FUSTALES,";
		sql += "PRCL_SPF1_OBS_LATIZALES,";
		sql += "PRCL_SPF1_OBS_BRINZALES,";
		sql += "PRCL_SPF2_DILIGENCIA,";
		sql += "PRCL_SPF2_FECHAINI,";
		sql += "PRCL_SPF2_FECHAFIN,";
		sql += "PRCL_SPF2_POSIBLE,";
		sql += "PRCL_SPF2_JUSTIFICACION_NO,";
		sql += "PRCL_SPF2_OBS_FUSTALES,";
		sql += "PRCL_SPF2_OBS_LATIZALES,";
		sql += "PRCL_SPF2_OBS_BRINZALES,";
		sql += "PRCL_SPF3_DILIGENCIA,";
		sql += "PRCL_SPF3_FECHAINI,";
		sql += "PRCL_SPF3_FECHAFIN,";
		sql += "PRCL_SPF3_POSIBLE,";
		sql += "PRCL_SPF3_JUSTIFICACION_NO,";
		sql += "PRCL_SPF3_OBS_FUSTALES,";
		sql += "PRCL_SPF3_OBS_LATIZALES,";
		sql += "PRCL_SPF3_OBS_BRINZALES,";
		sql += "PRCL_SPF4_DILIGENCIA,";
		sql += "PRCL_SPF4_FECHAINI,";
		sql += "PRCL_SPF4_FECHAFIN,";
		sql += "PRCL_SPF4_POSIBLE,";
		sql += "PRCL_SPF4_JUSTIFICACION_NO,";
		sql += "PRCL_SPF4_OBS_FUSTALES,";
		sql += "PRCL_SPF4_OBS_LATIZALES,";
		sql += "PRCL_SPF4_OBS_BRINZALES,";
		sql += "PRCL_SPF5_DILIGENCIA,";
		sql += "PRCL_SPF5_FECHAINI,";
		sql += "PRCL_SPF5_FECHAFIN,";
		sql += "PRCL_SPF5_POSIBLE,";
		sql += "PRCL_SPF5_JUSTIFICACION_NO,";
		sql += "PRCL_SPF5_OBS_FUSTALES,";
		sql += "PRCL_SPF5_OBS_LATIZALES,";
		sql += "PRCL_SPF5_OBS_BRINZALES,";		
		sql += "PRCL_PLOT,";
		sql += "PRCL_AREA,";
		sql += "PRCL_TEMPORALIDAD,";
		sql += "PRCL_PUBLICA,";
		sql += "PRCL_HAB,";
		sql += "PRCL_DAP,";
		sql += "PRCL_GPS,";
		sql += "PRCL_EQ,";
		sql += "PRCL_BA,";
		sql += "PRCL_BS,";
		sql += "PRCL_BT,";
		sql += "PRCL_AUTORCUSTODIOINFO,";
		sql += "PRCL_TIPOBOSQUE,";
		sql += "PRCL_INCLUIR,";
		sql += "TPBS_NOMBRE || ' -- ' || TPBS_DESCRIPCION AS TIPOBOSQUE,";
		sql += "RED_CORPORACIONES.CAR AS CAR,";
		sql += "RED_RESGUARDO_INDIGENA.RINOMBRE || ' -- ' || RED_RESGUARDO_INDIGENA.RIETNIA AS RESGUARDOINDIGENA,";
		sql += "RED_PARQUES_NACIONALES.NOMBRE_PN AS PNN";
		sql += " FROM RED_PARCELA ";
		sql += " LEFT OUTER JOIN RED_TIPOBOSQUE ON RED_PARCELA.PRCL_CONS_TIPOBOSQUE=RED_TIPOBOSQUE.TPBS_CONSECUTIVO ";
		sql += " LEFT OUTER JOIN RED_RESGUARDO_INDIGENA ON RED_PARCELA.PRCL_CONS_RESGUARDOINDIGENA=RED_RESGUARDO_INDIGENA.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_PARQUES_NACIONALES ON RED_PARCELA.PRCL_CONS_PNN=RED_PARQUES_NACIONALES.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_CORPORACIONES ON RED_PARCELA.PRCL_CONS_CAR=RED_CORPORACIONES.ID_CAR ";
		sql += " WHERE PRCL_CONSECUTIVO="+f_PRCL_CONSECUTIVO;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				rset.next();
				
				String PRCL_ID_IMPORTACION = Auxiliar.nz(rset.getString("PRCL_ID_IMPORTACION"), "NA");
				String PRCL_CONSECUTIVO = Auxiliar.nz(rset.getString("PRCL_CONSECUTIVO"), "NA");
				String PRCL_ID_UPM = Auxiliar.nz(rset.getString("PRCL_ID_UPM"), "NA");
				String PRCL_NOMBRE = Auxiliar.nz(rset.getString("PRCL_NOMBRE"), "NA");
				String PRCL_DEPARTAMENTO = Auxiliar.nz(rset.getString("PRCL_DEPARTAMENTO"), "NA");
				String PRCL_MUNICIPIO = Auxiliar.nz(rset.getString("PRCL_MUNICIPIO"), "NA");
				String PRCL_USR_DILIGENCIA_F1 = Auxiliar.nz(rset.getString("PRCL_USR_DILIGENCIA_F1"), "NA");
				String PRCL_USR_DILIGENCIA_F2 = Auxiliar.nz(rset.getString("PRCL_USR_DILIGENCIA_F2"), "NA");
				String PRCL_FECHAINI_APROXIMACION = Auxiliar.nz(rset.getString("PRCL_FECHAINI_APROXIMACION"), "NA");
				String PRCL_FECHAFIN_APROXIMACION = Auxiliar.nz(rset.getString("PRCL_FECHAFIN_APROXIMACION"), "NA");
				String PRCL_FECHAINI_LOCALIZACION = Auxiliar.nz(rset.getString("PRCL_FECHAINI_LOCALIZACION"), "NA");
				String PRCL_FECHAFIN_LOCALIZACION = Auxiliar.nz(rset.getString("PRCL_FECHAFIN_LOCALIZACION"), "NA");
				String PRCL_DESCRIPCION = Auxiliar.nz(rset.getString("PRCL_DESCRIPCION"), "NA");
				String PRCL_OBSERVACIONES = Auxiliar.nz(rset.getString("PRCL_OBSERVACIONES"), "NA");
				String PRCL_PLOT = Auxiliar.nz(rset.getString("PRCL_PLOT"), "NA");
				String PRCL_AREA = Auxiliar.nz(rset.getString("PRCL_AREA"), "NA");
				String PRCL_TEMPORALIDAD = Auxiliar.nz(rset.getString("PRCL_TEMPORALIDAD"), "NA");
				String PRCL_PUBLICA = Auxiliar.nz(rset.getString("PRCL_PUBLICA"), "NA");
				String PRCL_EQ = Auxiliar.nz(rset.getString("PRCL_EQ"), "NA");
				String PRCL_BA = Auxiliar.nz(rset.getString("PRCL_BA"), "NA");
				String PRCL_BS = Auxiliar.nz(rset.getString("PRCL_BS"), "NA");
				String PRCL_BT = Auxiliar.nz(rset.getString("PRCL_BT"), "NA");
				String PRCL_AUTORCUSTODIOINFO = Auxiliar.nz(rset.getString("PRCL_AUTORCUSTODIOINFO"), "NA");
				String PRCL_TIPOBOSQUE = Auxiliar.nz(rset.getString("PRCL_TIPOBOSQUE"), "NA");
				String PRCL_INCLUIR = Auxiliar.nz(rset.getString("PRCL_INCLUIR"), "NA");
				String PNN = Auxiliar.nz(rset.getString("PNN"), "NA");
				String TIPOBOSQUE = Auxiliar.nz(rset.getString("TIPOBOSQUE"), "NA");
				String CAR = Auxiliar.nz(rset.getString("CAR"), "NA");
				String RESGUARDOINDIGENA = Auxiliar.nz(rset.getString("RESGUARDOINDIGENA"), "NA");
				
				
				rset.close();
				
				//String carpeta = getServletContext().getRealPath("") + File.separator + EXPORT_XML_DIRECTORY;
				String carpeta = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_xml'", "");
				if (!Auxiliar.tieneAlgo(carpeta)) {
					carpeta = getServletContext().getRealPath("") + File.separator + EXPORT_XML_DIRECTORY;
				}

				
				String nombre_XML = "Parcela_"+f_PRCL_CONSECUTIVO+".xml";
				String ruta_XML = carpeta + "/" + nombre_XML;
				
				String xml = "";
				String html = "";
				String etiqueta = "";
				String nl = "\n";
				String br = "<br/>";
				String dato = "";
				String clasecontacto = "";
				String nombre_clasecontacto = "";
				
				xml += "<?xml version='1.0' encoding='UTF-8'?>";
				xml += nl+"<gmd:MD_Metadata xmlns:gts='http://www.isotc211.org/2005/gts' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:gml='http://www.opengis.net/gml' xmlns:gco='http://www.isotc211.org/2005/gco' xmlns:gmd='http://www.isotc211.org/2005/gmd'>";
				xml += nl+"	<gmd:fileIdentifier xmlns:gmx='http://www.isotc211.org/2005/gmx' xmlns:srv='http://www.isotc211.org/2005/srv'>";
				xml += nl+"		<gco:CharacterString>47612454-e9e6-47b9-82c8-c1cdb849a051</gco:CharacterString>";
				xml += nl+"	</gmd:fileIdentifier>";
				xml += nl+"	<gmd:language>";
				xml += nl+"		<gco:CharacterString>spa</gco:CharacterString>";
				xml += nl+"	</gmd:language>";
				xml += nl+"	<gmd:characterSet>";
				xml += nl+"		<gmd:MD_CharacterSetCode codeListValue='utf8' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_CharacterSetCode'/>";
				xml += nl+"	</gmd:characterSet>";
				
				xml += nl+"	<gmd:contact>";
				html += br+"<h1>Contactos/Contacts</h1>";
				etiqueta += br+"<h1>Contactos/Contacts</h1>";
				xml += nl+"		<gmd:CI_ResponsibleParty>";
				html += br+"<h2>Contacto Responsable del Metadato/Contact Responsible for this Metadata</h2>";
				etiqueta += br+"<h2>Contacto Responsable del Metadato/Contact Responsible for this Metadata</h2>";
				xml += nl+"			<gmd:individualName>";
				html += br+"<h3>Individuo/Individual</h3>";
				etiqueta += br+"<h3>Individuo/Individual</h3>";
				html += br+"<p class='metadato'>Administrador de Metadatos</p>";
				etiqueta += br+"<p class='metadato'>Administrador de Metadatos</p>";
				xml += nl+"				<gco:CharacterString>Administrador de Metadatos</gco:CharacterString>";
				xml += nl+"			</gmd:individualName>";
				xml += nl+"			<gmd:organisationName>";
				html += br+"<h3>Organización/Organization</h3>";
				etiqueta += br+"<h3>Organización/Organization</h3>";
				html += br+"<p class='metadato'>Instituto de Hidrologia, Meteorologia y Estudios Ambientales - IDEAM</p>";
				etiqueta += br+"<p class='metadato'>Instituto de Hidrologia, Meteorologia y Estudios Ambientales - IDEAM</p>";
				xml += nl+"				<gco:CharacterString>Instituto de Hidrologia, Meteorologia y Estudios Ambientales - IDEAM</gco:CharacterString>";
				xml += nl+"			</gmd:organisationName>";
				xml += nl+"			<gmd:positionName>";
				html += br+"<h3>Cargo/Position</h3>";
				etiqueta += br+"<h3>Cargo/Position</h3>";
				html += br+"<p class='metadato'>Administrador de Metadatos</p>";
				etiqueta += br+"<p class='metadato'>Administrador de Metadatos</p>";
				xml += nl+"				<gco:CharacterString>Administrador de Metadatos</gco:CharacterString>";
				xml += nl+"			</gmd:positionName>";
				xml += nl+"			<gmd:contactInfo>";
				html += br+"<h3>Información de Contacto/Contact Information</h3>";
				xml += nl+"				<gmd:CI_Contact>";
				xml += nl+"					<gmd:phone>";
				xml += nl+"						<gmd:CI_Telephone>";
				xml += nl+"							<gmd:voice>";
				html += br+"<p class='metadato'>Teléfono/Phone: 57 1 3527160 ext 1827 - 2151</p>";
				xml += nl+"								<gco:CharacterString>57 1 3527160 ext 1827 - 2151</gco:CharacterString>";
				xml += nl+"							</gmd:voice>";
				xml += nl+"							<gmd:facsimile>";
				html += br+"<p class='metadato'>Fax: 57 1 3527160 ext 1725</p>";
				xml += nl+"								<gco:CharacterString>57 1 3527160 ext 1725</gco:CharacterString>";
				xml += nl+"							</gmd:facsimile>";
				xml += nl+"						</gmd:CI_Telephone>";
				xml += nl+"					</gmd:phone>";
				xml += nl+"					<gmd:address>";
				xml += nl+"						<gmd:CI_Address>";
				xml += nl+"							<gmd:deliveryPoint>";
				html += br+"<p class='metadato'>Dirección de correspondencia/Delivery Point: Calle 25 D No. 96 B - 70</p>";
				xml += nl+"								<gco:CharacterString>Calle 25 D No. 96 B - 70</gco:CharacterString>";
				xml += nl+"							</gmd:deliveryPoint>";
				xml += nl+"							<gmd:city>";
				html += br+"<p class='metadato'>Ciudad/City: Bogota, D.C</p>";
				xml += nl+"								<gco:CharacterString>Bogota, D.C</gco:CharacterString>";
				xml += nl+"							</gmd:city>";
				xml += nl+"							<gmd:administrativeArea>";
				html += br+"<p class='metadato'>Área Administradora/Administrative Area: 11</p>";
				xml += nl+"								<gco:CharacterString>11</gco:CharacterString>";
				xml += nl+"							</gmd:administrativeArea>";
				xml += nl+"							<gmd:postalCode>";
				html += br+"<p class='metadato'>Código Postal/Postal Code: NA</p>";
				xml += nl+"								<gco:CharacterString>NA</gco:CharacterString>";
				xml += nl+"							</gmd:postalCode>";
				xml += nl+"							<gmd:country>";
				html += br+"<p class='metadato'>País/Country: Colombia</p>";
				xml += nl+"								<gco:CharacterString>Colombia</gco:CharacterString>";
				xml += nl+"							</gmd:country>";
				xml += nl+"							<gmd:electronicMailAddress>";
				html += br+"<p class='metadato'>E-Mail: metadata@ideam.gov.co</p>";
				xml += nl+"								<gco:CharacterString>metadata@ideam.gov.co</gco:CharacterString>";
				xml += nl+"							</gmd:electronicMailAddress>";
				xml += nl+"						</gmd:CI_Address>";
				xml += nl+"					</gmd:address>";
				xml += nl+"				</gmd:CI_Contact>";
				xml += nl+"			</gmd:contactInfo>";
				xml += nl+"			<gmd:role>";
				xml += nl+"				<gmd:CI_RoleCode codeListValue='' codeList='http://www.isotc211.org/2005/resources/codeList.xml#CI_RoleCode'/>";
				xml += nl+"			</gmd:role>";
				xml += nl+"		</gmd:CI_ResponsibleParty>";
				xml += nl+"	</gmd:contact>";
				xml += nl+"	<gmd:dateStamp>";
				html += br+"<h1>Fecha y Hora: "+Auxiliar.now("yyyy-MM-dd HH:mm:ss")+"</h1>";
				etiqueta += br+"<h1>Fecha y Hora: "+Auxiliar.now("yyyy-MM-dd HH:mm:ss")+"</h1>";
				xml += nl+"		<gco:DateTime xmlns:gmx='http://www.isotc211.org/2005/gmx' xmlns:srv='http://www.isotc211.org/2005/srv'>"+Auxiliar.now("yyyy-MM-dd HH:mm:ss")+"</gco:DateTime>";
				xml += nl+"	</gmd:dateStamp>";
				xml += nl+"	<gmd:metadataStandardName>";
				html += br+"<h1>Estándar del Metadato/Metadata Standard Name: ISO 19115:2003/19139</h1>";
				xml += nl+"		<gco:CharacterString xmlns:srv='http://www.isotc211.org/2005/srv'>ISO 19115:2003/19139</gco:CharacterString>";
				xml += nl+"	</gmd:metadataStandardName>";
				xml += nl+"	<gmd:metadataStandardVersion>";
				html += br+"<h1>Versión del Metadato/Metadata Standard Version: 1.0</h1>";
				xml += nl+"		<gco:CharacterString xmlns:srv='http://www.isotc211.org/2005/srv'>1.0</gco:CharacterString>";
				xml += nl+"	</gmd:metadataStandardVersion>";
				xml += nl+"	<gmd:spatialRepresentationInfo>";
				html += br+"<h1>Representación Espacial/Spatial Representation: Multipolygons, Polylines</h1>";
				xml += nl+"		<gmd:MD_VectorSpatialRepresentation>";
				xml += nl+"			<gmd:topologyLevel>";
				xml += nl+"				<gmd:MD_TopologyLevelCode codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_TopologyLevelCode' codeListValue=''/>";
				xml += nl+"			</gmd:topologyLevel>";
				xml += nl+"		</gmd:MD_VectorSpatialRepresentation>";
				xml += nl+"	</gmd:spatialRepresentationInfo>";
				xml += nl+"	<gmd:referenceSystemInfo>";
				html += br+"<h1>Sistema de Referencia/Reference System: EPSG 4326</h1>";
				xml += nl+"		<gmd:MD_ReferenceSystem>";
				xml += nl+"			<gmd:referenceSystemIdentifier>";
				xml += nl+"				<gmd:RS_Identifier>";
				xml += nl+"					<gmd:code>";
				xml += nl+"						<gco:CharacterString>4326</gco:CharacterString>";
				xml += nl+"					</gmd:code>";
				xml += nl+"				</gmd:RS_Identifier>";
				xml += nl+"			</gmd:referenceSystemIdentifier>";
				xml += nl+"		</gmd:MD_ReferenceSystem>";
				xml += nl+"	</gmd:referenceSystemInfo>";
				xml += nl+"	<gmd:identificationInfo>";
				html += br+"<h1>Identificación de los Datos/Data Identification: Plantilla de Metadatos Parcelas de Inventarios Forestales - IDEAM</h1>";
				html += br+"<p class='metadato'>E-Mail: metadata@ideam.gov.co</p>";
				xml += nl+"		<gmd:MD_DataIdentification>";
				xml += nl+"			<gmd:citation>";
				xml += nl+"				<gmd:CI_Citation>";
				xml += nl+"					<gmd:title>";
				html += br+"<h2>Citar Como/Citation: Plantilla de Metadatos Parcelas de Inventarios Forestales - IDEAM</h2>";
				html += br+"<p class='metadato'>E-Mail: metadata@ideam.gov.co</p>";
				xml += nl+"						<gco:CharacterString>Plantilla de Metadatos Parcelas de Inventarios Forestales - IDEAM</gco:CharacterString>";
				xml += nl+"					</gmd:title>";
				xml += nl+"					<gmd:date>";
				xml += nl+"						<gmd:CI_Date>";
				xml += nl+"							<gmd:date>";
				xml += nl+"								<gco:DateTime/>";
				xml += nl+"							</gmd:date>";
				xml += nl+"							<gmd:dateType>";
				xml += nl+"								<gmd:CI_DateTypeCode codeListValue='' codeList='http://www.isotc211.org/2005/resources/codeList.xml#CI_DateTypeCode'/>";
				xml += nl+"							</gmd:dateType>";
				xml += nl+"						</gmd:CI_Date>";
				xml += nl+"					</gmd:date>";
				xml += nl+"					<gmd:edition gco:nilReason='missing'>";
				xml += nl+"						<gco:CharacterString/>";
				xml += nl+"					</gmd:edition>";
				xml += nl+"					<gmd:presentationForm>";
				xml += nl+"						<gmd:CI_PresentationFormCode codeListValue='' codeList='http://www.isotc211.org/2005/resources/codeList.xml#CI_PresentationFormCode'/>";
				xml += nl+"					</gmd:presentationForm>";
				xml += nl+"				</gmd:CI_Citation>";
				xml += nl+"			</gmd:citation>";
				xml += nl+"			<gmd:abstract gco:nilReason='missing'>";
				xml += nl+"				<gco:CharacterString/>";
				xml += nl+"			</gmd:abstract>";
				xml += nl+"			<gmd:purpose>";
				html += br+"<h2>Propósito/Purpose</h2>";
				html += br+"<p class='metadato'>Parcela de un Inventario Forestal para calculo de Contenido de Biomasa</p>";
				xml += nl+"				<gco:CharacterString>Parcela de un Inventario Forestal para calculo de Contenido de Biomasa</gco:CharacterString>";
				xml += nl+"			</gmd:purpose>";
				xml += nl+"			<gmd:status>";
				xml += nl+"				<gmd:MD_ProgressCode codeListValue='' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_ProgressCode'/>";
				xml += nl+"			</gmd:status>";
				html += br+"<h2>Contactos/Contacts</h2>";
				
				clasecontacto = "3";
				xml += nl+"			<gmd:pointOfContact>";
				xml += nl+"				<gmd:CI_ResponsibleParty>";
				xml += nl+"					<gmd:individualName>";
				nombre_clasecontacto = dbREDD.obtenerDato("SELECT CLCN_DESCRIPCION FROM RED_CLASECONTACTO WHERE CLCN_CONSECUTIVO="+clasecontacto, "");
				t = "Nombre del contacto " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_NOMBRE FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+nombre_clasecontacto+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:individualName>";
				xml += nl+"					<gmd:organisationName>";
				t = "Organización del contacto " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_ORGANIZACION FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:organisationName>";
				xml += nl+"					<gmd:positionName>";
				t = "Cargo del contacto/Position of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_CARGO FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO=="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:positionName>";
				xml += nl+"					<gmd:contactInfo>";
				xml += nl+"						<gmd:CI_Contact>";
				xml += nl+"							<gmd:phone>";
				xml += nl+"								<gmd:CI_Telephone>";
				xml += nl+"									<gmd:voice>";
				t = "Teléfono del contacto/Phone of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_TELEFONO FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO=="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:voice>";
				xml += nl+"									<gmd:facsimile>";
				t = "Fax del contacto/Facsimile of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_MOVIL FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:facsimile>";
				xml += nl+"								</gmd:CI_Telephone>";
				xml += nl+"							</gmd:phone>";
				xml += nl+"							<gmd:address>";
				xml += nl+"								<gmd:CI_Address>";
				xml += nl+"									<gmd:deliveryPoint>";
				t = "Dirección de correspondencia/Delivery Point of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_DIRECCION FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:deliveryPoint>";
				xml += nl+"									<gmd:city>";
				t = "Ciudad del contacto /City of contact " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT M.NOMBRE FROM RED_MUNICIPIOS_SHAPE M, RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_MUNICIPIO=M.CODIGO AND U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:city>";
				xml += nl+"									<gmd:administrativeArea gco:nilReason='missing'>";
				xml += nl+"										<gco:CharacterString/>";
				xml += nl+"									</gmd:administrativeArea>";
				xml += nl+"									<gmd:postalCode gco:nilReason='missing'>";
				xml += nl+"										<gco:CharacterString/>";
				xml += nl+"									</gmd:postalCode>";
				xml += nl+"									<gmd:country>";
				xml += nl+"										<gco:CharacterString>Colombia</gco:CharacterString>";
				xml += nl+"									</gmd:country>";
				xml += nl+"									<gmd:electronicMailAddress>";
				t = "E-Mail del contacto /E-Mail of contact " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_CORREOELECTRONIC FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:electronicMailAddress>";
				xml += nl+"								</gmd:CI_Address>";
				xml += nl+"							</gmd:address>";
				xml += nl+"						</gmd:CI_Contact>";
				xml += nl+"					</gmd:contactInfo>";
				xml += nl+"					<gmd:role>";
				xml += nl+"						<gmd:CI_RoleCode codeList='http://www.isotc211.org/2005/resources/codeList.xml#CI_RoleCode' codeListValue='custodian'/>";
				xml += nl+"					</gmd:role>";
				xml += nl+"				</gmd:CI_ResponsibleParty>";
				xml += nl+"			</gmd:pointOfContact>";
				
				clasecontacto = "4";
				xml += nl+"			<gmd:pointOfContact>";
				xml += nl+"				<gmd:CI_ResponsibleParty>";
				xml += nl+"					<gmd:individualName>";
				nombre_clasecontacto = dbREDD.obtenerDato("SELECT CLCN_DESCRIPCION FROM RED_CLASECONTACTO WHERE CLCN_CONSECUTIVO="+clasecontacto, "");
				t = "Nombre del contacto " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_NOMBRE FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+nombre_clasecontacto+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:individualName>";
				xml += nl+"					<gmd:organisationName>";
				t = "Organización del contacto " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_ORGANIZACION FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:organisationName>";
				xml += nl+"					<gmd:positionName>";
				t = "Cargo del contacto/Position of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_CARGO FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO=="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:positionName>";
				xml += nl+"					<gmd:contactInfo>";
				xml += nl+"						<gmd:CI_Contact>";
				xml += nl+"							<gmd:phone>";
				xml += nl+"								<gmd:CI_Telephone>";
				xml += nl+"									<gmd:voice>";
				t = "Teléfono del contacto/Phone of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_TELEFONO FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO=="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:voice>";
				xml += nl+"									<gmd:facsimile>";
				t = "Fax del contacto/Facsimile of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_MOVIL FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:facsimile>";
				xml += nl+"								</gmd:CI_Telephone>";
				xml += nl+"							</gmd:phone>";
				xml += nl+"							<gmd:address>";
				xml += nl+"								<gmd:CI_Address>";
				xml += nl+"									<gmd:deliveryPoint>";
				t = "Dirección de correspondencia/Delivery Point of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_DIRECCION FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:deliveryPoint>";
				xml += nl+"									<gmd:city>";
				t = "Ciudad del contacto /City of contact " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT M.NOMBRE FROM RED_MUNICIPIOS_SHAPE M, RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_MUNICIPIO=M.CODIGO AND U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:city>";
				xml += nl+"									<gmd:administrativeArea gco:nilReason='missing'>";
				xml += nl+"										<gco:CharacterString/>";
				xml += nl+"									</gmd:administrativeArea>";
				xml += nl+"									<gmd:postalCode gco:nilReason='missing'>";
				xml += nl+"										<gco:CharacterString/>";
				xml += nl+"									</gmd:postalCode>";
				xml += nl+"									<gmd:country>";
				xml += nl+"										<gco:CharacterString>Colombia</gco:CharacterString>";
				xml += nl+"									</gmd:country>";
				xml += nl+"									<gmd:electronicMailAddress>";
				t = "E-Mail del contacto /E-Mail of contact " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_CORREOELECTRONIC FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:electronicMailAddress>";
				xml += nl+"								</gmd:CI_Address>";
				xml += nl+"							</gmd:address>";
				xml += nl+"						</gmd:CI_Contact>";
				xml += nl+"					</gmd:contactInfo>";
				xml += nl+"					<gmd:role>";
				xml += nl+"						<gmd:CI_RoleCode codeList='http://www.isotc211.org/2005/resources/codeList.xml#CI_RoleCode' codeListValue='principalInvestigator'/>";
				xml += nl+"					</gmd:role>";
				xml += nl+"				</gmd:CI_ResponsibleParty>";
				xml += nl+"			</gmd:pointOfContact>";
				
				clasecontacto = "2";
				xml += nl+"			<gmd:pointOfContact>";
				xml += nl+"				<gmd:CI_ResponsibleParty>";
				xml += nl+"					<gmd:individualName>";
				nombre_clasecontacto = dbREDD.obtenerDato("SELECT CLCN_DESCRIPCION FROM RED_CLASECONTACTO WHERE CLCN_CONSECUTIVO="+clasecontacto, "");
				t = "Nombre del contacto " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_NOMBRE FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+nombre_clasecontacto+": "+dato+"</p>";
				etiqueta += br+"<p class='metadato'>"+nombre_clasecontacto+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:individualName>";
				xml += nl+"					<gmd:organisationName>";
				t = "Organización del contacto " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_ORGANIZACION FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				etiqueta += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:organisationName>";
				xml += nl+"					<gmd:positionName>";
				t = "Cargo del contacto/Position of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_CARGO FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO=="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				etiqueta += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"						<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"					</gmd:positionName>";
				xml += nl+"					<gmd:contactInfo>";
				xml += nl+"						<gmd:CI_Contact>";
				xml += nl+"							<gmd:phone>";
				xml += nl+"								<gmd:CI_Telephone>";
				xml += nl+"									<gmd:voice>";
				t = "Teléfono del contacto/Phone of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_TELEFONO FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO=="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:voice>";
				xml += nl+"									<gmd:facsimile>";
				t = "Fax del contacto/Facsimile of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_MOVIL FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:facsimile>";
				xml += nl+"								</gmd:CI_Telephone>";
				xml += nl+"							</gmd:phone>";
				xml += nl+"							<gmd:address>";
				xml += nl+"								<gmd:CI_Address>";
				xml += nl+"									<gmd:deliveryPoint>";
				t = "Dirección de correspondencia/Delivery Point of " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_DIRECCION FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:deliveryPoint>";
				xml += nl+"									<gmd:city>";
				t = "Ciudad del contacto /City of contact " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT M.NOMBRE FROM RED_MUNICIPIOS_SHAPE M, RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_MUNICIPIO=M.CODIGO AND U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:city>";
				xml += nl+"									<gmd:administrativeArea gco:nilReason='missing'>";
				xml += nl+"										<gco:CharacterString/>";
				xml += nl+"									</gmd:administrativeArea>";
				xml += nl+"									<gmd:postalCode gco:nilReason='missing'>";
				xml += nl+"										<gco:CharacterString/>";
				xml += nl+"									</gmd:postalCode>";
				xml += nl+"									<gmd:country>";
				xml += nl+"										<gco:CharacterString>Colombia</gco:CharacterString>";
				xml += nl+"									</gmd:country>";
				xml += nl+"									<gmd:electronicMailAddress>";
				t = "E-Mail del contacto /E-Mail of contact " + nombre_clasecontacto;
				dato = dbREDD.obtenerDato("SELECT U.USR_CORREOELECTRONIC FROM RED_USUARIO U, RED_CONTACTO_PARCELA CP WHERE U.USR_CONSECUTIVO=CP.CNPR_CONS_CONTACTO AND CP.CNPR_CONS_CLASECONTACTO="+clasecontacto+" AND CP.CNPR_CONS_PARCELA="+f_PRCL_CONSECUTIVO, "");
				html += br+"<p class='metadato'>"+t+": "+dato+"</p>";
				xml += nl+"										<gco:CharacterString>"+dato+"</gco:CharacterString>";
				xml += nl+"									</gmd:electronicMailAddress>";
				xml += nl+"								</gmd:CI_Address>";
				xml += nl+"							</gmd:address>";
				xml += nl+"						</gmd:CI_Contact>";
				xml += nl+"					</gmd:contactInfo>";
				xml += nl+"					<gmd:role>";
				xml += nl+"						<gmd:CI_RoleCode codeList='http://www.isotc211.org/2005/resources/codeList.xml#CI_RoleCode' codeListValue='owner'/>";
				xml += nl+"					</gmd:role>";
				xml += nl+"				</gmd:CI_ResponsibleParty>";
				xml += nl+"			</gmd:pointOfContact>";
				xml += nl+"			<gmd:resourceMaintenance>";
				xml += nl+"				<gmd:MD_MaintenanceInformation>";
				xml += nl+"					<gmd:maintenanceAndUpdateFrequency>";
				xml += nl+"						<gmd:MD_MaintenanceFrequencyCode codeListValue='asNeeded' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_MaintenanceFrequencyCode'/>";
				xml += nl+"					</gmd:maintenanceAndUpdateFrequency>";
				xml += nl+"				</gmd:MD_MaintenanceInformation>";
				xml += nl+"			</gmd:resourceMaintenance>";
				xml += nl+"			<gmd:graphicOverview>";
				xml += nl+"				<gmd:MD_BrowseGraphic>";
				xml += nl+"					<gmd:fileName gco:nilReason='missing'>";
				xml += nl+"						<gco:CharacterString/>";
				xml += nl+"					</gmd:fileName>";
				xml += nl+"					<gmd:fileDescription>";
				xml += nl+"						<gco:CharacterString>thumbnail</gco:CharacterString>";
				xml += nl+"					</gmd:fileDescription>";
				xml += nl+"				</gmd:MD_BrowseGraphic>";
				xml += nl+"			</gmd:graphicOverview>";
				xml += nl+"			<gmd:graphicOverview>";
				xml += nl+"				<gmd:MD_BrowseGraphic>";
				xml += nl+"					<gmd:fileName gco:nilReason='missing'>";
				xml += nl+"						<gco:CharacterString/>";
				xml += nl+"					</gmd:fileName>";
				xml += nl+"					<gmd:fileDescription>";
				xml += nl+"						<gco:CharacterString>large_thumbnail</gco:CharacterString>";
				xml += nl+"					</gmd:fileDescription>";
				xml += nl+"				</gmd:MD_BrowseGraphic>";
				xml += nl+"			</gmd:graphicOverview>";
				xml += nl+"			<gmd:descriptiveKeywords>";
				xml += nl+"				<gmd:MD_Keywords>";
				xml += nl+"					<gmd:keyword>";
				xml += nl+"						<gco:CharacterString>Inventario Forestal</gco:CharacterString>";
				xml += nl+"					</gmd:keyword>";
				xml += nl+"					<gmd:keyword>";
				xml += nl+"						<gco:CharacterString>Parcelas</gco:CharacterString>";
				xml += nl+"					</gmd:keyword>";
				xml += nl+"					<gmd:keyword>";
				xml += nl+"						<gco:CharacterString>Bosque</gco:CharacterString>";
				xml += nl+"					</gmd:keyword>";
				xml += nl+"				</gmd:MD_Keywords>";
				xml += nl+"			</gmd:descriptiveKeywords>";
				xml += nl+"			<gmd:descriptiveKeywords>";
				xml += nl+"				<gmd:MD_Keywords>";
				xml += nl+"					<gmd:keyword>";
				xml += nl+"						<gco:CharacterString>Colombia</gco:CharacterString>";
				xml += nl+"					</gmd:keyword>";
				xml += nl+"					<gmd:type>";
				xml += nl+"						<gmd:MD_KeywordTypeCode codeListValue='place' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_KeywordTypeCode'/>";
				xml += nl+"					</gmd:type>";
				xml += nl+"				</gmd:MD_Keywords>";
				xml += nl+"			</gmd:descriptiveKeywords>";
				xml += nl+"			<gmd:descriptiveKeywords>";
				xml += nl+"				<gmd:MD_Keywords>";
				xml += nl+"					<gmd:keyword>";
				xml += nl+"						<gco:CharacterString>Area geográfica donde se ubica la parcela</gco:CharacterString>";
				xml += nl+"					</gmd:keyword>";
				xml += nl+"					<gmd:type>";
				xml += nl+"						<gmd:MD_KeywordTypeCode codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_KeywordTypeCode' codeListValue='place'/>";
				xml += nl+"					</gmd:type>";
				xml += nl+"				</gmd:MD_Keywords>";
				xml += nl+"			</gmd:descriptiveKeywords>";
				xml += nl+"			<gmd:resourceConstraints>";
				xml += nl+"				<gmd:MD_LegalConstraints>";
				xml += nl+"					<gmd:accessConstraints>";
				xml += nl+"						<gmd:MD_RestrictionCode codeListValue='copyright' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_RestrictionCode'/>";
				xml += nl+"					</gmd:accessConstraints>";
				xml += nl+"					<gmd:useConstraints>";
				xml += nl+"						<gmd:MD_RestrictionCode codeListValue='copyright' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_RestrictionCode'/>";
				xml += nl+"					</gmd:useConstraints>";
				xml += nl+"					<gmd:otherConstraints gco:nilReason='missing'>";
				xml += nl+"						<gco:CharacterString/>";
				xml += nl+"					</gmd:otherConstraints>";
				xml += nl+"				</gmd:MD_LegalConstraints>";
				xml += nl+"			</gmd:resourceConstraints>";
				xml += nl+"			<gmd:spatialRepresentationType>";
				xml += nl+"				<gmd:MD_SpatialRepresentationTypeCode codeListValue='vector' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_SpatialRepresentationTypeCode'/>";
				xml += nl+"			</gmd:spatialRepresentationType>";
				xml += nl+"			<gmd:spatialResolution>";
				xml += nl+"				<gmd:MD_Resolution>";
				xml += nl+"					<gmd:equivalentScale>";
				xml += nl+"						<gmd:MD_RepresentativeFraction>";
				xml += nl+"							<gmd:denominator>";
				xml += nl+"								<gco:Integer/>";
				xml += nl+"							</gmd:denominator>";
				xml += nl+"						</gmd:MD_RepresentativeFraction>";
				xml += nl+"					</gmd:equivalentScale>";
				xml += nl+"				</gmd:MD_Resolution>";
				xml += nl+"			</gmd:spatialResolution>";
				xml += nl+"			<gmd:language>";
				xml += nl+"				<gco:CharacterString>spa</gco:CharacterString>";
				xml += nl+"			</gmd:language>";
				xml += nl+"			<gmd:characterSet>";
				xml += nl+"				<gmd:MD_CharacterSetCode codeListValue='utf8' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_CharacterSetCode'/>";
				xml += nl+"			</gmd:characterSet>";
				xml += nl+"			<gmd:topicCategory>";
				xml += nl+"				<gmd:MD_TopicCategoryCode>environment</gmd:MD_TopicCategoryCode>";
				xml += nl+"			</gmd:topicCategory>";
				xml += nl+"			<gmd:extent>";
				xml += nl+"				<gmd:EX_Extent>";
				xml += nl+"					<gmd:temporalElement>";
				xml += nl+"						<gmd:EX_TemporalExtent>";
				xml += nl+"							<gmd:extent>";
				xml += nl+"								<gml:TimePeriod gml:id='N101D0'>";
				xml += nl+"										<gml:beginPosition/>";
				xml += nl+"										<gml:endPosition/>";
				xml += nl+"								</gml:TimePeriod>";
				xml += nl+"							</gmd:extent>";
				xml += nl+"						</gmd:EX_TemporalExtent>";
				xml += nl+"					</gmd:temporalElement>";
				xml += nl+"				</gmd:EX_Extent>";
				xml += nl+"			</gmd:extent>";
				xml += nl+"			<gmd:extent>";
				xml += nl+"				<gmd:EX_Extent>";
				xml += nl+"					<gmd:geographicElement>";
				xml += nl+"						<gmd:EX_GeographicBoundingBox>";
				xml += nl+"							<gmd:westBoundLongitude>";
				xml += nl+"								<gco:Decimal>"+dbREDD.obtenerDato("SELECT MIN(t.X) AS W FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-81.72015")+"</gco:Decimal>";
				xml += nl+"							</gmd:westBoundLongitude>";
				xml += nl+"							<gmd:eastBoundLongitude>";
				xml += nl+"								<gco:Decimal>"+dbREDD.obtenerDato("SELECT MAX(t.X) AS E FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-66.87045")+"</gco:Decimal>";
				xml += nl+"							</gmd:eastBoundLongitude>";
				xml += nl+"							<gmd:southBoundLatitude>";
				xml += nl+"								<gco:Decimal>"+dbREDD.obtenerDato("SELECT MIN(t.Y) AS S FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "-4.23687")+"</gco:Decimal>";
				xml += nl+"							</gmd:southBoundLatitude>";
				xml += nl+"							<gmd:northBoundLatitude>";
				xml += nl+"								<gco:Decimal>"+dbREDD.obtenerDato("SELECT MAX(t.Y) AS N FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO, "12.59028")+"</gco:Decimal>";
				xml += nl+"							</gmd:northBoundLatitude>";
				xml += nl+"						</gmd:EX_GeographicBoundingBox>";
				xml += nl+"					</gmd:geographicElement>";
				xml += nl+"				</gmd:EX_Extent>";
				xml += nl+"			</gmd:extent>";
				
				html += br+"<h2>Información Complementaria/Supplemental Information</h2>";
				html += br+"<p class='metadato'>";
				xml += nl+"			<gmd:supplementalInformation>";
				xml += nl+"				<gco:CharacterString>";
				
				xml += nl+ "Id:"+PRCL_CONSECUTIVO;
				html += br+ "Id:"+PRCL_CONSECUTIVO;
				etiqueta += br+ "Id:"+PRCL_CONSECUTIVO;
				
				xml += nl+"; UPM:"+PRCL_ID_UPM;
				html += br+"UPM:"+PRCL_ID_UPM;
				etiqueta += br+"UPM:"+PRCL_ID_UPM;
				
				xml += nl+"; Nombre:"+PRCL_NOMBRE;
				html += br+"Nombre:"+PRCL_NOMBRE;
				etiqueta += br+"Nombre:"+PRCL_NOMBRE;
				
				dato = dbREDD.obtenerDato("SELECT TMPR_NOMBRE FROM RED_TEMPORALIDAD WHERE TMPR_CONSECUTIVO=" + PRCL_TEMPORALIDAD, "NA");
				xml += nl+"; Temporalidad:"+dato;
				html += br+"Temporalidad:"+dato;
				etiqueta += br+"Temporalidad:"+dato;
				
				dato = dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_PUBLICA, "NA");
				xml += nl+"; Parcela es pública:"+dato;
				html += br+"Parcela es pública:"+dato;
				etiqueta += br+"Parcela es pública:"+dato;
				
				xml += nl+"; Incluir en Cálculos:"+PRCL_INCLUIR;
				html += br+"Incluir en Cálculos:"+PRCL_INCLUIR;
				
				dato = dbREDD.obtenerDato("SELECT EQAL.EQAL_LEGIBLE || ' ' || M.MTDL_NOMBRE || ' Nr:' || EQAL.EQAL_CODIGO AS INFO FROM RED_ECUACIONALOMETRICA EQAL INNER JOIN RED_METODOLOGIA M ON EQAL.EQAL_METODOLOGIA=M.MTDL_CONSECUTIVO WHERE EQAL.EQAL_ID=" + PRCL_EQ, "NA");
				xml += nl+"; Ecuación Alométrica:"+dato;
				html += br+"Ecuación Alométrica:"+dato;
				
				xml += nl+"; Biomasa Aérea:"+PRCL_BA;
				html += br+"Biomasa Aérea:"+PRCL_BA;
				etiqueta += br+"Biomasa Aérea:"+PRCL_BA;
				
				xml += nl+"; Biomasa Subterránea:"+PRCL_BS;
				html += br+"Biomasa Subterránea:"+PRCL_BS;
				etiqueta += br+"Biomasa Subterránea:"+PRCL_BS;
				
				xml += nl+"; Biomasa Total:"+PRCL_BT;
				html += br+"Biomasa Total:"+PRCL_BT;
				etiqueta += br+"Biomasa Total:"+PRCL_BT;
				
				xml += nl+"; Fecha inicial de aproximación:"+PRCL_FECHAINI_APROXIMACION;
				html += br+"Fecha inicial de aproximación:"+PRCL_FECHAINI_APROXIMACION;
				
				xml += nl+"; Fecha final de aproximación:"+PRCL_FECHAFIN_APROXIMACION;
				html += br+"Fecha final de aproximación:"+PRCL_FECHAFIN_APROXIMACION;
				
				xml += nl+"; Fecha inicial de localización:"+PRCL_FECHAINI_LOCALIZACION;
				html += br+"Fecha inicial de localización:"+PRCL_FECHAINI_LOCALIZACION;
				etiqueta += br+"Fecha inicial de localización:"+PRCL_FECHAINI_LOCALIZACION;
				
				xml += nl+"; Fecha final de localización:"+PRCL_FECHAFIN_LOCALIZACION;
				html += br+"Fecha final de localización:"+PRCL_FECHAFIN_LOCALIZACION;
				etiqueta += br+"Fecha final de localización:"+PRCL_FECHAFIN_LOCALIZACION;
				
				xml += nl+"; Descripción general/General description:"+PRCL_DESCRIPCION;
				html += br+"Descripción general/General description:"+PRCL_DESCRIPCION;
				etiqueta += br+"Descripción general/General description:"+PRCL_DESCRIPCION;
				
				xml += nl+"; Observaciones/Notes:"+PRCL_OBSERVACIONES;
				html += br+"Observaciones/Notes:"+PRCL_OBSERVACIONES;
				etiqueta += br+"Observaciones/Notes:"+PRCL_OBSERVACIONES;
				
				dato = dbREDD.obtenerDato("SELECT NOMBRE FROM RED_DEPTOS_SHAPE WHERE CODIGO=" + PRCL_DEPARTAMENTO, "NA");
				xml += nl+"; Departamento:" + dato; 
				html += br+"Departamento:" + dato; 
				etiqueta += br+"Departamento:" + dato; 
				
				dato = dbREDD.obtenerDato("SELECT NOMBRE FROM RED_MUNICIPIOS_SHAPE WHERE CODIGO=" + PRCL_MUNICIPIO, "NA");
				xml += nl+"; Municipio:" + dato; 
				html += br+"Municipio:" + dato; 
				etiqueta += br+"Municipio:" + dato; 
				
				xml += nl+"; Tipo de Bosque (Holdridge):" + TIPOBOSQUE; 
				html += br+"Tipo de Bosque (Holdridge):" + TIPOBOSQUE; 
				etiqueta += br+"Tipo de Bosque (Holdridge):" + TIPOBOSQUE; 
				
				xml += nl+"; Resguardo Indigena:" + RESGUARDOINDIGENA; 
				html += br+"Resguardo Indigena:" + RESGUARDOINDIGENA; 
				etiqueta += br+"Resguardo Indigena:" + RESGUARDOINDIGENA; 
				
				xml += nl+"; Parque Nacional Natural:" + PNN; 
				html += br+"Parque Nacional Natural:" + PNN; 
				etiqueta += br+"Parque Nacional Natural:" + PNN; 
				
				xml += nl+"; Corporación Autónoma Regional:" + CAR; 
				html += br+"Corporación Autónoma Regional:" + CAR; 
				etiqueta += br+"Corporación Autónoma Regional:" + CAR; 
				
				html += br+"</p>"; 
				
				xml += nl+"				</gco:CharacterString>";
				xml += nl+"			</gmd:supplementalInformation>";
				xml += nl+"		</gmd:MD_DataIdentification>";
				xml += nl+"	</gmd:identificationInfo>";
				xml += nl+"	<gmd:distributionInfo>";
				xml += nl+"		<gmd:MD_Distribution>";
				xml += nl+"			<gmd:transferOptions>";
				xml += nl+"				<gmd:MD_DigitalTransferOptions>";
				xml += nl+"					<gmd:onLine>";
				xml += nl+"						<gmd:CI_OnlineResource>";
				xml += nl+"							<gmd:linkage>";
				xml += nl+"								<gmd:URL>http://54.172.131.5/AdmIF/visor_parcelas_leaflet.jsp?PRCL_CONSECUTIVO="+f_PRCL_CONSECUTIVO+"</gmd:URL>";
				xml += nl+"							</gmd:linkage>";
				xml += nl+"							<gmd:protocol>";
				xml += nl+"								<gco:CharacterString>WWW:LINK-1.0-http--link</gco:CharacterString>";
				xml += nl+"							</gmd:protocol>";
				xml += nl+"							<gmd:name>";
				xml += nl+"								<gco:CharacterString>Mapa de la Parcela</gco:CharacterString>";
				xml += nl+"							</gmd:name>";
				xml += nl+"							<gmd:description gco:nilReason='missing'>";
				xml += nl+"								<gco:CharacterString>Forma y ubicación de la parcela en un mapa.</gco:CharacterString>";
				xml += nl+"							</gmd:description>";
				xml += nl+"						</gmd:CI_OnlineResource>";
				xml += nl+"					</gmd:onLine>";
				xml += nl+"					<gmd:onLine>";
				xml += nl+"						<gmd:CI_OnlineResource>";
				xml += nl+"							<gmd:linkage xmlns:gmx='http://www.isotc211.org/2005/gmx' xmlns:srv='http://www.isotc211.org/2005/srv'>";
				xml += nl+"								<gmd:URL>http://54.172.131.5/AdmIF/pdf/Parcela_"+f_PRCL_CONSECUTIVO+".pdf</gmd:URL>";
				xml += nl+"							</gmd:linkage>";
				xml += nl+"							<gmd:protocol>";
				xml += nl+"								<gco:CharacterString>WWW:DOWNLOAD-1.0-http--download</gco:CharacterString>";
				xml += nl+"							</gmd:protocol>";
				xml += nl+"							<gmd:name xmlns:gmx='http://www.isotc211.org/2005/gmx' xmlns:srv='http://www.isotc211.org/2005/srv'>";
				xml += nl+"								<gmx:MimeFileType type='application/pdf'/>";
				xml += nl+"							</gmd:name>";
				xml += nl+"							<gmd:description>";
				xml += nl+"								<gco:CharacterString>Ficha técnica de la parcela en PDF</gco:CharacterString>";
				xml += nl+"							</gmd:description>";
				xml += nl+"						</gmd:CI_OnlineResource>";
				xml += nl+"					</gmd:onLine>";
				
				
				xml += nl+"					<gmd:onLine>";
				xml += nl+"						<gmd:CI_OnlineResource>";
				xml += nl+"							<gmd:linkage>";
				xml += nl+"								<gmd:URL>http://54.172.131.5:8080/geoserver/OracleAmazon/wms?service=WMS&amp;version=1.1.0&amp;request=GetMap&amp;layers=OracleAmazon:C_RED_PARCELA_PARAMETRIZADA&amp;styles=&amp;bbox=-78.584364,-4.0505,-67.338111,11.293397&amp;width=375&amp;height=512&amp;srs=EPSG:4326&amp;format=application/openlayers&amp;viewparams=w_PRCL_CONSECUTIVO:%20AND%20PRCL_CONSECUTIVO%20IN%20("+f_PRCL_CONSECUTIVO+")</gmd:URL>";
				xml += nl+"							</gmd:linkage>";
				xml += nl+"							<gmd:protocol>";
				xml += nl+"								<gco:CharacterString>OGC:WFS-1.1.0-http-get-map</gco:CharacterString>";
				xml += nl+"							</gmd:protocol>";
				xml += nl+"							<gmd:name>";
				xml += nl+"								<gco:CharacterString>WFS Parcela</gco:CharacterString>";
				xml += nl+"							</gmd:name>";
				xml += nl+"							<gmd:description>";
				xml += nl+"								<gco:CharacterString>WFS de la parcela</gco:CharacterString>";
				xml += nl+"							</gmd:description>";
				xml += nl+"						</gmd:CI_OnlineResource>";
				xml += nl+"					</gmd:onLine>";
				
				
				xml += nl+"				</gmd:MD_DigitalTransferOptions>";
				xml += nl+"			</gmd:transferOptions>";
				xml += nl+"		</gmd:MD_Distribution>";
				xml += nl+"	</gmd:distributionInfo>";
				xml += nl+"	<gmd:dataQualityInfo>";
				xml += nl+"		<gmd:DQ_DataQuality>";
				xml += nl+"			<gmd:scope>";
				xml += nl+"				<gmd:DQ_Scope>";
				xml += nl+"					<gmd:level>";
				xml += nl+"						<gmd:MD_ScopeCode codeListValue='' codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_ScopeCode'/>";
				xml += nl+"					</gmd:level>";
				xml += nl+"				</gmd:DQ_Scope>";
				xml += nl+"			</gmd:scope>";
				xml += nl+"			<gmd:lineage>";
				xml += nl+"				<gmd:LI_Lineage>";
				xml += nl+"					<gmd:statement gco:nilReason='missing'>";
				xml += nl+"						<gco:CharacterString/>";
				xml += nl+"					</gmd:statement>";
				xml += nl+"				</gmd:LI_Lineage>";
				xml += nl+"			</gmd:lineage>";
				xml += nl+"		</gmd:DQ_DataQuality>";
				xml += nl+"	</gmd:dataQualityInfo>";
				xml += nl+"	<gmd:metadataMaintenance>";
				xml += nl+"		<gmd:MD_MaintenanceInformation>";
				xml += nl+"			<gmd:maintenanceAndUpdateFrequency>";
				xml += nl+"				<gmd:MD_MaintenanceFrequencyCode codeList='http://www.isotc211.org/2005/resources/codeList.xml#MD_MaintenanceFrequencyCode' codeListValue=''/>";
				xml += nl+"			</gmd:maintenanceAndUpdateFrequency>";
				xml += nl+"		</gmd:MD_MaintenanceInformation>";
				xml += nl+"	</gmd:metadataMaintenance>";
				xml += nl+"</gmd:MD_Metadata>";
				
				if (modo.equals("xml")) {
					boolean ok_crear_xml = archie.escribirArchivo(ruta_XML, xml);
					
					if (ok_crear_xml) {
						r = "<a class=boton href='" + EXPORT_XML_DIRECTORY + "/" + nombre_XML + "' target='_blank' title='Clic derecho para descargar/Right clic to download.'>";
						r += Auxiliar.traducir(yo+"Descargar_metadato_de_la_parcela_en_XML", idioma, "Descargar metadato de la parcela en XML" + "..");
						r += "</a>";
					}
				}
				else if (modo.equals("etiqueta")) {
					dbREDD.desconectarse();
					return etiqueta;
				}
				else {
					dbREDD.desconectarse();
					return html;
				}
			}
			else
			{
				r += "No se encontraron resultados.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en exportarMetadatoParcela(): " + e.toString() + " -- SQL: " + sql;
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Exporta el detalle de una parcela a formato PDF para descargar.
	 * 
	 * @param f_PRCL_CONSECUTIVO
	 * @param solo_enlace
	 * @param session
	 * @param guardar
	 * @param response
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String exportarParcelaPDF(String f_PRCL_CONSECUTIVO, boolean solo_enlace, HttpSession session, boolean guardar, HttpServletResponse response)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "exportarParcelaPDF";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		
		String ruta_archivo = "";
		
		String r = "";
		
		if (!sec.tienePermiso(usuario, "122")) {
			r = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
			dbREDD.desconectarse();
			return r;
		}
		
		if (!Auxiliar.tieneAlgo(f_PRCL_CONSECUTIVO)) {
			r = Auxiliar.traducir(yo+"Codigo_Parcela_No_Especificado", idioma, "Código del individuo no especificado." + "..");
			dbREDD.desconectarse();
			return r;
		}
		
	    //String carpeta = getServletContext().getRealPath("") + File.separator + EXPORT_PDF_DIRECTORY;
		String carpeta = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_pdf'", "");
		if (!Auxiliar.tieneAlgo(carpeta)) {
			carpeta = getServletContext().getRealPath("") + File.separator + EXPORT_PDF_DIRECTORY;
		}

	    String nombre_PDF = "Parcela_"+f_PRCL_CONSECUTIVO+".pdf";
		String ruta_PDF = carpeta + "/" + nombre_PDF;

		if (solo_enlace) {
			r = "<a class=boton href='" + EXPORT_PDF_DIRECTORY + "/" + nombre_PDF + "' target='_blank'>";
			r += Auxiliar.traducir(yo+"Descargar_ficha_de_la_parcela_en_PDF", idioma, "Descargar ficha de la parcela en PDF" + "..");
			r += "</a>"; 
			return r;
		}
		
		String sql = "SELECT ";
		sql += "PRCL_ID_IMPORTACION,";
		sql += "PRCL_CONSECUTIVO,";
		sql += "PRCL_ID_UPM,";
		sql += "PRCL_NOMBRE,";
		sql += "PRCL_USR_DILIGENCIA_F1,";
		sql += "PRCL_USR_DILIGENCIA_F2,";
		sql += "TO_CHAR(PRCL_FECHAINI_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_APROXIMACION,";
		sql += "TO_CHAR(PRCL_FECHAFIN_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_APROXIMACION,";
		sql += "TO_CHAR(PRCL_FECHAINI_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_LOCALIZACION,";
		sql += "TO_CHAR(PRCL_FECHAFIN_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_LOCALIZACION,";
		sql += "PRCL_DESCRIPCION,";
		sql += "PRCL_OBSERVACIONES,";
		sql += "PRCL_TRACKLOG_CAMPAMENTO,";
		sql += "PRCL_TRACKLOG_PARCELA,";
		sql += "PRCL_DISTANCIA_POBLADO,";
		sql += "PRCL_MEDIOACCESO_POBLADO,";
		sql += "PRCL_TPOBLADO_H,";
		sql += "PRCL_TPOBLADO_M,";
		sql += "PRCL_DISTANCIA_CAMPAMENTO,";
		sql += "PRCL_MEDIOACCESO_CAMPAMENTO,";
		sql += "PRCL_TCAMPAMENTO_H,";
		sql += "PRCL_TCAMPAMENTO_M,";
		sql += "PRCL_DISTANCIA_JALON,";
		sql += "PRCL_MEDIOACCESO_JALON,";
		sql += "PRCL_TJALON_H,";
		sql += "PRCL_TJALON_M,";
		sql += "PRCL_DISTANCIA_CAMPAMENTOS,";
		sql += "PRCL_LATITUD,";
		sql += "PRCL_LONGITUD,";
		sql += "PRCL_ALTITUD,";
		sql += "PRCL_CONS_PAIS,";
		sql += "PRCL_DEPARTAMENTO,";
		sql += "PRCL_MUNICIPIO,";
		sql += "PRCL_NOMBREARCHIVO,";
		sql += "PRCL_SPF1_DILIGENCIA,";
		sql += "PRCL_SPF1_FECHAINI,";
		sql += "PRCL_SPF1_FECHAFIN,";
		sql += "PRCL_SPF1_POSIBLE,";
		sql += "PRCL_SPF1_JUSTIFICACION_NO,";
		sql += "PRCL_SPF1_OBS_FUSTALES,";
		sql += "PRCL_SPF1_OBS_LATIZALES,";
		sql += "PRCL_SPF1_OBS_BRINZALES,";
		sql += "PRCL_SPF2_DILIGENCIA,";
		sql += "PRCL_SPF2_FECHAINI,";
		sql += "PRCL_SPF2_FECHAFIN,";
		sql += "PRCL_SPF2_POSIBLE,";
		sql += "PRCL_SPF2_JUSTIFICACION_NO,";
		sql += "PRCL_SPF2_OBS_FUSTALES,";
		sql += "PRCL_SPF2_OBS_LATIZALES,";
		sql += "PRCL_SPF2_OBS_BRINZALES,";
		sql += "PRCL_SPF3_DILIGENCIA,";
		sql += "PRCL_SPF3_FECHAINI,";
		sql += "PRCL_SPF3_FECHAFIN,";
		sql += "PRCL_SPF3_POSIBLE,";
		sql += "PRCL_SPF3_JUSTIFICACION_NO,";
		sql += "PRCL_SPF3_OBS_FUSTALES,";
		sql += "PRCL_SPF3_OBS_LATIZALES,";
		sql += "PRCL_SPF3_OBS_BRINZALES,";
		sql += "PRCL_SPF4_DILIGENCIA,";
		sql += "PRCL_SPF4_FECHAINI,";
		sql += "PRCL_SPF4_FECHAFIN,";
		sql += "PRCL_SPF4_POSIBLE,";
		sql += "PRCL_SPF4_JUSTIFICACION_NO,";
		sql += "PRCL_SPF4_OBS_FUSTALES,";
		sql += "PRCL_SPF4_OBS_LATIZALES,";
		sql += "PRCL_SPF4_OBS_BRINZALES,";
		sql += "PRCL_SPF5_DILIGENCIA,";
		sql += "PRCL_SPF5_FECHAINI,";
		sql += "PRCL_SPF5_FECHAFIN,";
		sql += "PRCL_SPF5_POSIBLE,";
		sql += "PRCL_SPF5_JUSTIFICACION_NO,";
		sql += "PRCL_SPF5_OBS_FUSTALES,";
		sql += "PRCL_SPF5_OBS_LATIZALES,";
		sql += "PRCL_SPF5_OBS_BRINZALES,";		
		sql += "PRCL_PLOT,";
		sql += "PRCL_AREA,";
		sql += "PRCL_TEMPORALIDAD,";
		sql += "PRCL_PUBLICA,";
		sql += "PRCL_HAB,";
		sql += "PRCL_DAP,";
		sql += "PRCL_GPS,";
		sql += "PRCL_EQ,";
		sql += "PRCL_BA,";
		sql += "PRCL_BS,";
		sql += "PRCL_BT,";
		sql += "PRCL_AUTORCUSTODIOINFO,";
		sql += "PRCL_TIPOBOSQUE,";
		sql += "PRCL_INCLUIR,";
		sql += "TPBS_NOMBRE || ' -- ' || TPBS_DESCRIPCION AS TIPOBOSQUE,";
		sql += "RED_CORPORACIONES.CAR AS CAR,";
		sql += "RED_RESGUARDO_INDIGENA.RINOMBRE || ' -- ' || RED_RESGUARDO_INDIGENA.RIETNIA AS RESGUARDOINDIGENA,";
		sql += "RED_PARQUES_NACIONALES.NOMBRE_PN AS PNN";
		sql += " FROM RED_PARCELA ";
		sql += " LEFT OUTER JOIN RED_TIPOBOSQUE ON RED_PARCELA.PRCL_CONS_TIPOBOSQUE=RED_TIPOBOSQUE.TPBS_CONSECUTIVO ";
		sql += " LEFT OUTER JOIN RED_RESGUARDO_INDIGENA ON RED_PARCELA.PRCL_CONS_RESGUARDOINDIGENA=RED_RESGUARDO_INDIGENA.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_PARQUES_NACIONALES ON RED_PARCELA.PRCL_CONS_PNN=RED_PARQUES_NACIONALES.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_CORPORACIONES ON RED_PARCELA.PRCL_CONS_CAR=RED_CORPORACIONES.ID_CAR ";
		sql += " WHERE PRCL_CONSECUTIVO="+f_PRCL_CONSECUTIVO;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				rset.next();
				
				String PRCL_ID_IMPORTACION = Auxiliar.nz(rset.getString("PRCL_ID_IMPORTACION"), "NA");
				String PRCL_CONSECUTIVO = Auxiliar.nz(rset.getString("PRCL_CONSECUTIVO"), "NA");
				String PRCL_ID_UPM = Auxiliar.nz(rset.getString("PRCL_ID_UPM"), "NA");
				String PRCL_NOMBRE = Auxiliar.nz(rset.getString("PRCL_NOMBRE"), "NA");
				String PRCL_USR_DILIGENCIA_F1 = Auxiliar.nz(rset.getString("PRCL_USR_DILIGENCIA_F1"), "NA");
				String PRCL_USR_DILIGENCIA_F2 = Auxiliar.nz(rset.getString("PRCL_USR_DILIGENCIA_F2"), "NA");
				String PRCL_FECHAINI_APROXIMACION = Auxiliar.nz(rset.getString("PRCL_FECHAINI_APROXIMACION"), "NA");
				String PRCL_FECHAFIN_APROXIMACION = Auxiliar.nz(rset.getString("PRCL_FECHAFIN_APROXIMACION"), "NA");
				String PRCL_FECHAINI_LOCALIZACION = Auxiliar.nz(rset.getString("PRCL_FECHAINI_LOCALIZACION"), "NA");
				String PRCL_FECHAFIN_LOCALIZACION = Auxiliar.nz(rset.getString("PRCL_FECHAFIN_LOCALIZACION"), "NA");
				String PRCL_DESCRIPCION = Auxiliar.nz(rset.getString("PRCL_DESCRIPCION"), "NA");
				String PRCL_OBSERVACIONES = Auxiliar.nz(rset.getString("PRCL_OBSERVACIONES"), "NA");
				String PRCL_TRACKLOG_CAMPAMENTO = Auxiliar.nz(rset.getString("PRCL_TRACKLOG_CAMPAMENTO"), "NA");
				String PRCL_TRACKLOG_PARCELA = Auxiliar.nz(rset.getString("PRCL_TRACKLOG_PARCELA"), "NA");
				String PRCL_DISTANCIA_CAMPAMENTOS = Auxiliar.nz(rset.getString("PRCL_DISTANCIA_CAMPAMENTOS"), "NA");
				String PRCL_LATITUD = Auxiliar.nz(rset.getString("PRCL_LATITUD"), "NA");
				String PRCL_LONGITUD = Auxiliar.nz(rset.getString("PRCL_LONGITUD"), "NA");
				String PRCL_ALTITUD = Auxiliar.nz(rset.getString("PRCL_ALTITUD"), "NA");
				String PRCL_NOMBREARCHIVO = Auxiliar.nz(rset.getString("PRCL_NOMBREARCHIVO"), "NA");
				String PRCL_SPF1_DILIGENCIA = Auxiliar.nz(rset.getString("PRCL_SPF1_DILIGENCIA"), "NA");
				String PRCL_SPF1_FECHAINI = Auxiliar.nz(rset.getString("PRCL_SPF1_FECHAINI"), "NA");
				String PRCL_SPF1_FECHAFIN = Auxiliar.nz(rset.getString("PRCL_SPF1_FECHAFIN"), "NA");
				String PRCL_SPF1_POSIBLE = Auxiliar.nz(rset.getString("PRCL_SPF1_POSIBLE"), "NA");
				String PRCL_SPF1_JUSTIFICACION_NO = Auxiliar.nz(rset.getString("PRCL_SPF1_JUSTIFICACION_NO"), "NA");
				String PRCL_SPF1_OBS_FUSTALES = Auxiliar.nz(rset.getString("PRCL_SPF1_OBS_FUSTALES"), "NA");
				String PRCL_SPF1_OBS_LATIZALES = Auxiliar.nz(rset.getString("PRCL_SPF1_OBS_LATIZALES"), "NA");
				String PRCL_SPF1_OBS_BRINZALES = Auxiliar.nz(rset.getString("PRCL_SPF1_OBS_BRINZALES"), "NA");
				String PRCL_SPF2_DILIGENCIA = Auxiliar.nz(rset.getString("PRCL_SPF2_DILIGENCIA"), "NA");
				String PRCL_SPF2_FECHAINI = Auxiliar.nz(rset.getString("PRCL_SPF2_FECHAINI"), "NA");
				String PRCL_SPF2_FECHAFIN = Auxiliar.nz(rset.getString("PRCL_SPF2_FECHAFIN"), "NA");
				String PRCL_SPF2_POSIBLE = Auxiliar.nz(rset.getString("PRCL_SPF2_POSIBLE"), "NA");
				String PRCL_SPF2_JUSTIFICACION_NO = Auxiliar.nz(rset.getString("PRCL_SPF2_JUSTIFICACION_NO"), "NA");
				String PRCL_SPF2_OBS_FUSTALES = Auxiliar.nz(rset.getString("PRCL_SPF2_OBS_FUSTALES"), "NA");
				String PRCL_SPF2_OBS_LATIZALES = Auxiliar.nz(rset.getString("PRCL_SPF2_OBS_LATIZALES"), "NA");
				String PRCL_SPF2_OBS_BRINZALES = Auxiliar.nz(rset.getString("PRCL_SPF2_OBS_BRINZALES"), "NA");
				String PRCL_SPF3_DILIGENCIA = Auxiliar.nz(rset.getString("PRCL_SPF3_DILIGENCIA"), "NA");
				String PRCL_SPF3_FECHAINI = Auxiliar.nz(rset.getString("PRCL_SPF3_FECHAINI"), "NA");
				String PRCL_SPF3_FECHAFIN = Auxiliar.nz(rset.getString("PRCL_SPF3_FECHAFIN"), "NA");
				String PRCL_SPF3_POSIBLE = Auxiliar.nz(rset.getString("PRCL_SPF3_POSIBLE"), "NA");
				String PRCL_SPF3_JUSTIFICACION_NO = Auxiliar.nz(rset.getString("PRCL_SPF3_JUSTIFICACION_NO"), "NA");
				String PRCL_SPF3_OBS_FUSTALES = Auxiliar.nz(rset.getString("PRCL_SPF3_OBS_FUSTALES"), "NA");
				String PRCL_SPF3_OBS_LATIZALES = Auxiliar.nz(rset.getString("PRCL_SPF3_OBS_LATIZALES"), "NA");
				String PRCL_SPF3_OBS_BRINZALES = Auxiliar.nz(rset.getString("PRCL_SPF3_OBS_BRINZALES"), "NA");
				String PRCL_SPF4_DILIGENCIA = Auxiliar.nz(rset.getString("PRCL_SPF4_DILIGENCIA"), "NA");
				String PRCL_SPF4_FECHAINI = Auxiliar.nz(rset.getString("PRCL_SPF4_FECHAINI"), "NA");
				String PRCL_SPF4_FECHAFIN = Auxiliar.nz(rset.getString("PRCL_SPF4_FECHAFIN"), "NA");
				String PRCL_SPF4_POSIBLE = Auxiliar.nz(rset.getString("PRCL_SPF4_POSIBLE"), "NA");
				String PRCL_SPF4_JUSTIFICACION_NO = Auxiliar.nz(rset.getString("PRCL_SPF4_JUSTIFICACION_NO"), "NA");
				String PRCL_SPF4_OBS_FUSTALES = Auxiliar.nz(rset.getString("PRCL_SPF4_OBS_FUSTALES"), "NA");
				String PRCL_SPF4_OBS_LATIZALES = Auxiliar.nz(rset.getString("PRCL_SPF4_OBS_LATIZALES"), "NA");
				String PRCL_SPF4_OBS_BRINZALES = Auxiliar.nz(rset.getString("PRCL_SPF4_OBS_BRINZALES"), "NA");
				String PRCL_SPF5_DILIGENCIA = Auxiliar.nz(rset.getString("PRCL_SPF5_DILIGENCIA"), "NA");
				String PRCL_SPF5_FECHAINI = Auxiliar.nz(rset.getString("PRCL_SPF5_FECHAINI"), "NA");
				String PRCL_SPF5_FECHAFIN = Auxiliar.nz(rset.getString("PRCL_SPF5_FECHAFIN"), "NA");
				String PRCL_SPF5_POSIBLE = Auxiliar.nz(rset.getString("PRCL_SPF5_POSIBLE"), "NA");
				String PRCL_SPF5_JUSTIFICACION_NO = Auxiliar.nz(rset.getString("PRCL_SPF5_JUSTIFICACION_NO"), "NA");
				String PRCL_SPF5_OBS_FUSTALES = Auxiliar.nz(rset.getString("PRCL_SPF5_OBS_FUSTALES"), "NA");
				String PRCL_SPF5_OBS_LATIZALES = Auxiliar.nz(rset.getString("PRCL_SPF5_OBS_LATIZALES"), "NA");
				String PRCL_SPF5_OBS_BRINZALES = Auxiliar.nz(rset.getString("PRCL_SPF5_OBS_BRINZALES"), "NA");
				String PRCL_PLOT = Auxiliar.nz(rset.getString("PRCL_PLOT"), "NA");
				String PRCL_AREA = Auxiliar.nz(rset.getString("PRCL_AREA"), "NA");
				String PRCL_TEMPORALIDAD = Auxiliar.nz(rset.getString("PRCL_TEMPORALIDAD"), "NA");
				String PRCL_PUBLICA = Auxiliar.nz(rset.getString("PRCL_PUBLICA"), "NA");
				String PRCL_HAB = Auxiliar.nz(rset.getString("PRCL_HAB"), "NA");
				String PRCL_DAP = Auxiliar.nz(rset.getString("PRCL_DAP"), "NA");
				String PRCL_GPS = Auxiliar.nz(rset.getString("PRCL_GPS"), "NA");
				String PRCL_EQ = Auxiliar.nz(rset.getString("PRCL_EQ"), "NA");
				String PRCL_BA = Auxiliar.nz(rset.getString("PRCL_BA"), "NA");
				String PRCL_BS = Auxiliar.nz(rset.getString("PRCL_BS"), "NA");
				String PRCL_BT = Auxiliar.nz(rset.getString("PRCL_BT"), "NA");
				String PRCL_AUTORCUSTODIOINFO = Auxiliar.nz(rset.getString("PRCL_AUTORCUSTODIOINFO"), "NA");
				String PRCL_TIPOBOSQUE = Auxiliar.nz(rset.getString("PRCL_TIPOBOSQUE"), "NA");
				String PRCL_INCLUIR = Auxiliar.nz(rset.getString("PRCL_INCLUIR"), "NA");
				String PNN = Auxiliar.nz(rset.getString("PNN"), "NA");
				String TIPOBOSQUE = Auxiliar.nz(rset.getString("TIPOBOSQUE"), "NA");
				String CAR = Auxiliar.nz(rset.getString("CAR"), "NA");
				String RESGUARDOINDIGENA = Auxiliar.nz(rset.getString("RESGUARDOINDIGENA"), "NA");
				
				rset.close();
				
				String str_fotos = Auxiliar.nzObjStr(PRCL_NOMBREARCHIVO, "").toString();
				String [] a_fotos = str_fotos.split(",");
				
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
		        
		        writer.setCompressionLevel(0);
		        
		        document.open();

		        evento_encabezado.setHeader("Parcela " + f_PRCL_CONSECUTIVO, idioma);
		        
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
		        
				t = Auxiliar.traducir("Ids", idioma, "IDs." + " ");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase("Consecutivo:"+PRCL_CONSECUTIVO+"; Plot:"+PRCL_PLOT+"; Import:"+PRCL_ID_IMPORTACION));
		        D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("UPM", idioma, "Unidad de Medición Primaria" + " ");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase("UPM:"+Auxiliar.nz(PRCL_ID_UPM, "-")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_NOMBRE", idioma, "Nombre" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_NOMBRE));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_AREA", idioma, "Área" + "..");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(PRCL_AREA));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_TEMPORALIDAD", idioma, "Temporalidad (Tipo) de Parcela" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT TMPR_NOMBRE FROM RED_TEMPORALIDAD WHERE TMPR_CONSECUTIVO=" + PRCL_TEMPORALIDAD, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_PUBLICA", idioma, "Parcela es Pública" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_PUBLICA, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_HAB", idioma, "Se registró una disminución >20% de la biomasa aérea al excluir individuos no-arbóreos?" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_HAB, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_DAP", idioma, "La parcela presenta una distribución diamétrica anómala?" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_DAP, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_GPS", idioma, "La diferencia entre la altitud reportada e interpolada es mayor o igual a 100 m.s.n.m.?" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_GPS, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_EQ", idioma, "Ecuación Alométrica a Utilizar" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT EQAL.EQAL_LEGIBLE || ' ' || M.MTDL_NOMBRE || ' Nr:' || EQAL.EQAL_CODIGO AS INFO FROM RED_ECUACIONALOMETRICA EQAL INNER JOIN RED_METODOLOGIA M ON EQAL.EQAL_METODOLOGIA=M.MTDL_CONSECUTIVO WHERE EQAL.EQAL_ID=" + PRCL_EQ, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_BA", idioma, "Biomasa Aérea" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_BA));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_BS", idioma, "Biomasa Subterranea" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_BS));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_BT", idioma, "Biomasa Total" + "..");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(PRCL_BT));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_AUTORCUSTODIOINFO", idioma, "Autor/Custodio Información" + "..");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_AUTORCUSTODIOINFO, "NA")));
		        D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_INCLUIR", idioma, "Incluir la Parcela en Cálculos?" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_INCLUIR, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_USR_DILIGENCIA_F1", idioma, "Quién diligenció el formulario de aproximación" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_USR_DILIGENCIA_F1, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_USR_DILIGENCIA_F2", idioma, "Quién diligenció el formulario de localización" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_USR_DILIGENCIA_F2, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_FECHAINI_APROXIMACION", idioma, "Fecha inicial de aproximación" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_FECHAINI_APROXIMACION));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_FECHAFIN_APROXIMACION", idioma, "Fecha final de aproximación" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_FECHAINI_APROXIMACION));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_FECHAINI_LOCALIZACION", idioma, "Fecha inicial de localización" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_FECHAINI_APROXIMACION));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_FECHAFIN_LOCALIZACION", idioma, "Fecha final de localización" + "..");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(PRCL_FECHAINI_APROXIMACION));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_DISTANCIA_CAMPAMENTOS", idioma, "Distancia entre campamentos" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_DISTANCIA_CAMPAMENTOS));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_LATITUD", idioma, "Latitud del jalón" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_LATITUD));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_LONGITUD", idioma, "Longitud del jalón" + "..");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(PRCL_LONGITUD));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_ALTITUD", idioma, "Altitud del jalón" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_ALTITUD));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_DESCRIPCION", idioma, "Descripción" + "..");
		        E = new PdfPCell(new Phrase(t));
		        E.setBorder(0);
		        E.setHorizontalAlignment(Element.ALIGN_LEFT);
		        E.setVerticalAlignment(Element.ALIGN_TOP);
		        E.setCellEvent(cellBackground);
		        E.setCellEvent(roundRectangle);
		        E.setPadding(3);
		        E.setBorder(PdfPCell.NO_BORDER);		        
		        tabla_datos.addCell(E);
		        D = new PdfPCell(new Phrase(PRCL_DESCRIPCION));
		        D.setBorder(0);
				tabla_datos.addCell(D);

				t = Auxiliar.traducir("PRCL_OBSERVACIONES", idioma, "Observaciones" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_OBSERVACIONES));
				D.setBorder(0);
				tabla_datos.addCell(D);

				
				// SPF1
				
				t = Auxiliar.traducir("PRCL_SPF1_DILIGENCIA", idioma, "Quién diligenció SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_SPF1_DILIGENCIA, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_POSIBLE", idioma, "Fue posible establecer SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_SPF1_POSIBLE, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_JUSTIFICACION_NO", idioma, "Justificación para no establecer SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF1_JUSTIFICACION_NO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_FECHAINI", idioma, "Fecha inicio SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF1_FECHAINI));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_FECHAFIN", idioma, "Fecha finalización SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF1_FECHAFIN));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_OBS_FUSTALES", idioma, "Observaciones sobre la parcela de fustales de SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF1_OBS_FUSTALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_OBS_LATIZALES", idioma, "Observaciones sobre la parcela de latizales de SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF1_OBS_LATIZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF1_OBS_BRINZALES", idioma, "Observaciones sobre la parcela de brinzales de SPF1" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF1_OBS_BRINZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				
				// SPF2
				
				t = Auxiliar.traducir("PRCL_SPF2_DILIGENCIA", idioma, "Quién diligenció SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_SPF2_DILIGENCIA, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_POSIBLE", idioma, "Fue posible establecer SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_SPF2_POSIBLE, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_JUSTIFICACION_NO", idioma, "Justificación para no establecer SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF2_JUSTIFICACION_NO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_FECHAINI", idioma, "Fecha inicio SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF2_FECHAINI));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_FECHAFIN", idioma, "Fecha finalización SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF2_FECHAFIN));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_OBS_FUSTALES", idioma, "Observaciones sobre la parcela de fustales de SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF2_OBS_FUSTALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_OBS_LATIZALES", idioma, "Observaciones sobre la parcela de latizales de SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF2_OBS_LATIZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF2_OBS_BRINZALES", idioma, "Observaciones sobre la parcela de brinzales de SPF2" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF2_OBS_BRINZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				
				// SPF3
				
				t = Auxiliar.traducir("PRCL_SPF3_DILIGENCIA", idioma, "Quién diligenció SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_SPF3_DILIGENCIA, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_POSIBLE", idioma, "Fue posible establecer SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_SPF3_POSIBLE, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_JUSTIFICACION_NO", idioma, "Justificación para no establecer SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF3_JUSTIFICACION_NO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_FECHAINI", idioma, "Fecha inicio SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF3_FECHAINI));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_FECHAFIN", idioma, "Fecha finalización SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF3_FECHAFIN));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_OBS_FUSTALES", idioma, "Observaciones sobre la parcela de fustales de SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF3_OBS_FUSTALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_OBS_LATIZALES", idioma, "Observaciones sobre la parcela de latizales de SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF3_OBS_LATIZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF3_OBS_BRINZALES", idioma, "Observaciones sobre la parcela de brinzales de SPF3" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF3_OBS_BRINZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				
				// SPF4
				
				t = Auxiliar.traducir("PRCL_SPF4_DILIGENCIA", idioma, "Quién diligenció SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_SPF4_DILIGENCIA, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_POSIBLE", idioma, "Fue posible establecer SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_SPF4_POSIBLE, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_JUSTIFICACION_NO", idioma, "Justificación para no establecer SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF4_JUSTIFICACION_NO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_FECHAINI", idioma, "Fecha inicio SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF4_FECHAINI));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_FECHAFIN", idioma, "Fecha finalización SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF4_FECHAFIN));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_OBS_FUSTALES", idioma, "Observaciones sobre la parcela de fustales de SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF4_OBS_FUSTALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_OBS_LATIZALES", idioma, "Observaciones sobre la parcela de latizales de SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF4_OBS_LATIZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF4_OBS_BRINZALES", idioma, "Observaciones sobre la parcela de brinzales de SPF4" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF4_OBS_BRINZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				
				// SPF5
				
				t = Auxiliar.traducir("PRCL_SPF5_DILIGENCIA", idioma, "Quién diligenció SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_ID=" + PRCL_SPF5_DILIGENCIA, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_POSIBLE", idioma, "Fue posible establecer SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(dbREDD.obtenerDato("SELECT SINO_NOMBRE FROM RED_SINO WHERE SINO_ID=" + PRCL_SPF5_POSIBLE, "NA")));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_JUSTIFICACION_NO", idioma, "Justificación para no establecer SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF5_JUSTIFICACION_NO));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_FECHAINI", idioma, "Fecha inicio SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF5_FECHAINI));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_FECHAFIN", idioma, "Fecha finalización SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF5_FECHAFIN));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_OBS_FUSTALES", idioma, "Observaciones sobre la parcela de fustales de SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF5_OBS_FUSTALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_OBS_LATIZALES", idioma, "Observaciones sobre la parcela de latizales de SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF5_OBS_LATIZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("PRCL_SPF5_OBS_BRINZALES", idioma, "Observaciones sobre la parcela de brinzales de SPF5" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PRCL_SPF5_OBS_BRINZALES));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				
				// DATOS ESPACIALES
				
				t = Auxiliar.traducir("General.PNN", idioma, "Parque Nacional Natural" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(PNN));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("General.TIPOBOSQUE", idioma, "Tipo de Bosque (Holdridge)" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(TIPOBOSQUE));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("General.CAR", idioma, "Corporación Autónoma Regional" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(CAR));
				D.setBorder(0);
				tabla_datos.addCell(D);
				
				t = Auxiliar.traducir("General.RESGUARDOINDIGENA", idioma, "Resguardo Indígena" + "..");
				E = new PdfPCell(new Phrase(t));
				E.setBorder(0);
				E.setHorizontalAlignment(Element.ALIGN_LEFT);
				E.setVerticalAlignment(Element.ALIGN_TOP);
				E.setCellEvent(cellBackground);
				E.setCellEvent(roundRectangle);
				E.setPadding(3);
				E.setBorder(PdfPCell.NO_BORDER);		        
				tabla_datos.addCell(E);
				D = new PdfPCell(new Phrase(RESGUARDOINDIGENA));
				D.setBorder(0);
				tabla_datos.addCell(D);
				

				tabla_datos.completeRow();
				document.add(tabla_datos);

				
				
				// PORCENTAJES DE COBERTURA
				
				document.newPage();

		        PdfPTable tabla_titulo_Cobertura = new PdfPTable(1);
		        tabla_titulo_Cobertura.setTotalWidth(540);
		        tabla_titulo_Cobertura.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_Cobertura.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_Cobertura.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir("General.Cobertura", idioma, "Porcentajes de Cobertura" + "..");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_Cobertura.addCell(cell);
		        tabla_titulo_Cobertura.completeRow();
		        
		        document.add(tabla_titulo_Cobertura);

		        
		        PdfPTable tabla_Cobertura = new PdfPTable(5);
		        tabla_Cobertura.setTotalWidth(540);
		        tabla_Cobertura.setLockedWidth(true);
				
		        tabla_Cobertura.setTableEvent(tableBackground);
		        tabla_Cobertura.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_Cobertura.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_Cobertura.getDefaultCell().setCellEvent(whiteRectangle);
		        
				String sql_Cobertura = "SELECT ";
				sql_Cobertura += "CBRT.CBRT_SUBPARCELA AS SUBPARCELA,";
				sql_Cobertura += "TPER.TPER_NOMBRE AS TIPOPERTURBACION,";
				sql_Cobertura += "SVPR.SVPR_DESCRIPCION AS SEVERIDADPERTURBACION,";
				sql_Cobertura += "TPCB.TPCB_DESCRIPCION AS TIPOCOBERTURA,";
				sql_Cobertura += "CBRT.CBRT_PORCENTAJE AS PORCENTAJE";
				sql_Cobertura += " FROM RED_COBERTURA CBRT ";
				sql_Cobertura += " INNER JOIN RED_TIPOPERTURBACION TPER ON CBRT.CBRT_TPER_ID=TPER.TPER_ID ";
				sql_Cobertura += " INNER JOIN RED_SEVERIDADPERTURBACION SVPR ON CBRT.CBRT_SVPR_CONSECUTIVO=SVPR.SVPR_CONSECUTIVO ";
				sql_Cobertura += " INNER JOIN RED_TIPOCOBERTURA TPCB ON CBRT.CBRT_TPCB_CONSECUTIVO=TPCB.TPCB_CONSECUTIVO ";
				sql_Cobertura += " WHERE CBRT.CBRT_PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
				sql_Cobertura += " ORDER BY CBRT.CBRT_PORCENTAJE DESC";

				try {
					ResultSet rset_Cobertura = dbREDD.consultarBD(sql_Cobertura);
					
					if (rset_Cobertura != null)
					{
						String SUBPARCELA = "";
						String TIPOPERTURBACION = "";
						String SEVERIDADPERTURBACION = "";
						String TIPOCOBERTURA = "";
						String PORCENTAJE = "";
						

				        t = Auxiliar.traducir("TPER_NOMBRE", idioma, "Perturbación" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Cobertura.addCell(E);

				        t = Auxiliar.traducir("SVPR_DESCRIPCION", idioma, "Severidad" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Cobertura.addCell(E);

				        t = Auxiliar.traducir("TPCB_DESCRIPCION", idioma, "Tipo de Cobertura" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Cobertura.addCell(E);

				        t = Auxiliar.traducir("CBRT_SUBPARCELA", idioma, "Subparcela" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Cobertura.addCell(E);

				        t = Auxiliar.traducir("CBRT_PORCENTAJE", idioma, "Porcentaje 0 a 1" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Cobertura.addCell(E);

						
						while (rset_Cobertura.next())
						{
							SUBPARCELA = rset_Cobertura.getString("SUBPARCELA");
							TIPOPERTURBACION = rset_Cobertura.getString("TIPOPERTURBACION");
							SEVERIDADPERTURBACION = rset_Cobertura.getString("SEVERIDADPERTURBACION");
							TIPOCOBERTURA = rset_Cobertura.getString("TIPOCOBERTURA");
							PORCENTAJE = rset_Cobertura.getString("PORCENTAJE");

					        
							D = new PdfPCell(new Phrase(SUBPARCELA));
							D.setBorder(0);
							tabla_Cobertura.addCell(D);
							
							D = new PdfPCell(new Phrase(TIPOPERTURBACION));
							D.setBorder(0);
							tabla_Cobertura.addCell(D);
							
							D = new PdfPCell(new Phrase(SEVERIDADPERTURBACION));
							D.setBorder(0);
							tabla_Cobertura.addCell(D);
							
							D = new PdfPCell(new Phrase(TIPOCOBERTURA));
							D.setBorder(0);
							tabla_Cobertura.addCell(D);
							
							D = new PdfPCell(new Phrase(PORCENTAJE));
					        D.setHorizontalAlignment(Element.ALIGN_RIGHT);
							D.setBorder(0);
							tabla_Cobertura.addCell(D);							
						}
						
						rset_Cobertura.close();
					}
					else
					{
						r += "El conjunto de resultados retornados para la consulta ["+sql_Cobertura+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
					}
				} catch (SQLException e) {
					r += "Excepción de SQL ["+sql_Cobertura+"]: " + e.toString();
				} catch (Exception e) {
					r += "Ocurrió la siguiente excepción: " + e.toString() + " -- SQL: " + sql_Cobertura;
				}
		        
				tabla_Cobertura.completeRow();
				document.add(tabla_Cobertura);


				
				
				// BIOMASA Y CARBONO
				
				document.newPage();
				
				PdfPTable tabla_titulo_BMC = new PdfPTable(1);
				tabla_titulo_BMC.setTotalWidth(540);
				tabla_titulo_BMC.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabla_titulo_BMC.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				tabla_titulo_BMC.getDefaultCell().setCellEvent(whiteRectangle);
				
				t = Auxiliar.traducir("General.BMC", idioma, "Registros de Biomasa y Carbono" + "..");
				
				cell = new PdfPCell(new Phrase(t + "\n"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setCellEvent(cellBackground);
				cell.setCellEvent(roundRectangle);
				cell.setPadding(3);
				tabla_titulo_BMC.addCell(cell);
				tabla_titulo_BMC.completeRow();
				
				document.add(tabla_titulo_BMC);
				
				
				PdfPTable tabla_BMC = new PdfPTable(9);
				tabla_BMC.setTotalWidth(540);
				tabla_BMC.setLockedWidth(true);
				
				tabla_BMC.setTableEvent(tableBackground);
				tabla_BMC.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabla_BMC.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				tabla_BMC.getDefaultCell().setCellEvent(whiteRectangle);
				
				String sql_BMC = "SELECT ";
				sql_BMC += "BMCR_CONSECUTIVO,";
				sql_BMC += "BMCR_BIOMASA,";
				sql_BMC += "BMCR_CARBONO,";
				sql_BMC += "BMCR_CONS_METODOLOGI,";
				sql_BMC += "BMCR_CONS_ESTADOBIOM,";
				sql_BMC += "BMCR_FECHA_INICIO,";
				sql_BMC += "BMCR_AREABASALPROME,";
				sql_BMC += "BMCR_AREABASALTOTAL,";
				sql_BMC += "BMCR_VOLUMENTOTAL,";
				sql_BMC += "BMCR_TIPOGENERA";
				sql_BMC += " FROM RED_BIOMASAYCARBONO ";
				sql_BMC += " WHERE BMCR_CONS_PARCELA="+PRCL_CONSECUTIVO;
				sql_BMC += " ORDER BY BMCR_FECHA_INICIO ";
				
				try {
					ResultSet rset_BMC = dbREDD.consultarBD(sql_BMC);
					
					if (rset_BMC != null)
					{
						String BMCR_CONSECUTIVO = "";
						String BMCR_BIOMASA = "";
						String BMCR_CARBONO = "";
						String BMCR_CONS_METODOLOGI = "";
						String BMCR_CONS_ESTADOBIOM = "";
						String BMCR_FECHA_INICIO = "";
						String BMCR_AREABASALPROME = "";
						String BMCR_AREABASALTOTAL = "";
						String BMCR_VOLUMENTOTAL = "";
						String BMCR_TIPOGENERA = "";
						String db_metodologia = "";
						String db_estado = "";
						String db_tipogeneracion = "";
						
						
						t = Auxiliar.traducir("BMCR_BIOMASA", idioma, "Biomasa" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_CARBONO", idioma, "Carbono" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_CONS_METODOLOGI", idioma, "Metodología" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_CONS_ESTADOBIOM", idioma, "Estado" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_FECHA_INICIO", idioma, "Vigente Desde" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_AREABASALPROME", idioma, "Área Basal Promedio" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_AREABASALTOTAL", idioma, "Área Basal Total" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_VOLUMENTOTAL", idioma, "Volumen Total" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						t = Auxiliar.traducir("BMCR_TIPOGENERA", idioma, "Tipo de Generación" + " ");
						E = new PdfPCell(new Phrase(t));
						E.setBorder(0);
						E.setHorizontalAlignment(Element.ALIGN_LEFT);
						E.setVerticalAlignment(Element.ALIGN_TOP);
						E.setCellEvent(cellBackground);
						E.setCellEvent(roundRectangle);
						E.setPadding(3);
						E.setBorder(PdfPCell.NO_BORDER);		        
						tabla_BMC.addCell(E);
						
						while (rset_BMC.next())
						{
							BMCR_CONSECUTIVO = rset_BMC.getString("BMCR_CONSECUTIVO");
							BMCR_BIOMASA = rset_BMC.getString("BMCR_BIOMASA");
							BMCR_CARBONO = rset_BMC.getString("BMCR_CARBONO");
							BMCR_CONS_METODOLOGI = rset_BMC.getString("BMCR_CONS_METODOLOGI");
							db_metodologia = dbREDD.obtenerDato("SELECT MTDL_NOMBRE || '(' || MTDL_ECUACION || ')' as INFO FROM RED_METODOLOGIA WHERE MTDL_CONSECUTIVO="+BMCR_CONS_METODOLOGI, "");
							BMCR_CONS_ESTADOBIOM = rset_BMC.getString("BMCR_CONS_ESTADOBIOM");
							db_estado = dbREDD.obtenerDato("SELECT 'Verificado:' || ESBI.ESBI_VERIFICADO || '; Atípico:' || ESBI.ESBI_ATIPICO || '; Datos incluidos en cálculos:' || ESBI.ESBI_INCLUIDOCALCULOS AS INFO FROM RED_ESTADOBIOMASA WHERE ESBI_CONSECUTIVO="+BMCR_CONS_ESTADOBIOM, "");
							BMCR_FECHA_INICIO = rset_BMC.getString("BMCR_FECHA_INICIO").replace(" 00:00:00.0", "");
							BMCR_AREABASALPROME = rset_BMC.getString("BMCR_AREABASALPROME");
							BMCR_AREABASALTOTAL = rset_BMC.getString("BMCR_AREABASALTOTAL");
							BMCR_VOLUMENTOTAL = rset_BMC.getString("BMCR_VOLUMENTOTAL");
							BMCR_TIPOGENERA = rset_BMC.getString("BMCR_TIPOGENERA");
							db_tipogeneracion = dbREDD.obtenerDato("SELECT BCTG_NOMBRE FROM RED_TIPOGENERACION WHERE BCTG_CONSECUTIVO="+BMCR_TIPOGENERA, "");
							
							
							D = new PdfPCell(new Phrase(BMCR_BIOMASA));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(BMCR_CARBONO));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(db_metodologia));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(db_estado));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(BMCR_FECHA_INICIO));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(BMCR_AREABASALPROME));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(BMCR_AREABASALTOTAL));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(BMCR_VOLUMENTOTAL));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
							D = new PdfPCell(new Phrase(db_tipogeneracion));
							D.setBorder(0);
							tabla_BMC.addCell(D);
							
						}
						
						rset_BMC.close();
					}
					else
					{
						r += "El conjunto de resultados retornados para la consulta ["+sql_BMC+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
					}
				} catch (SQLException e) {
					r += "Excepción de SQL ["+sql_BMC+"]: " + e.toString();
				} catch (Exception e) {
					r += "Ocurrió la siguiente excepción: " + e.toString() + " -- SQL: " + sql_BMC;
				}
				
				tabla_BMC.completeRow();
				document.add(tabla_BMC);
				
				
				// Contactos
				
				document.newPage();

		        PdfPTable tabla_titulo_Contactos = new PdfPTable(1);
		        tabla_titulo_Contactos.setTotalWidth(540);
		        tabla_titulo_Contactos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_Contactos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_Contactos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir("General.Contactos", idioma, "Contactos de la Parcela" + "..");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_Contactos.addCell(cell);
		        tabla_titulo_Contactos.completeRow();
		        
		        document.add(tabla_titulo_Contactos);

		        
		        PdfPTable tabla_Contactos = new PdfPTable(2);
		        tabla_Contactos.setTotalWidth(540);
		        tabla_Contactos.setLockedWidth(true);
				
		        tabla_Contactos.setTableEvent(tableBackground);
		        tabla_Contactos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_Contactos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_Contactos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				String sql_Contactos = "SELECT ";
				sql_Contactos += "CNPR_CONSECUTIVO,";
				sql_Contactos += "CNPR_CONS_CONTACTO,";
				sql_Contactos += "CNPR_CONS_CLASECONTACTO";
				sql_Contactos += " FROM RED_CONTACTO_PARCELA ";
				sql_Contactos += " WHERE CNPR_CONS_PARCELA="+PRCL_CONSECUTIVO;
				sql_Contactos += " ORDER BY CNPR_CONSECUTIVO ";

				try {
					ResultSet rset_Contactos = dbREDD.consultarBD(sql_Contactos);
					
					if (rset_Contactos != null)
					{
						String CNPR_CONSECUTIVO = "";
						String CNPR_CONS_CONTACTO = "";
						String CNPR_CONS_CLASECONTACTO = "";
						String info_contacto = "";
						String db_clasecontacto = "";
						

				        t = Auxiliar.traducir("CNPR_CONS_CONTACTO", idioma, "Contacto" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Contactos.addCell(E);

				        t = Auxiliar.traducir("CNPR_CONS_CLASECONTACTO", idioma, "Clase" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_Contactos.addCell(E);
						
						while (rset_Contactos.next())
						{
							CNPR_CONSECUTIVO = rset_Contactos.getString("CNPR_CONSECUTIVO");
							CNPR_CONS_CONTACTO = rset_Contactos.getString("CNPR_CONS_CONTACTO");
							CNPR_CONS_CLASECONTACTO = rset_Contactos.getString("CNPR_CONS_CLASECONTACTO");
							db_clasecontacto = dbREDD.obtenerDato("SELECT CLCN_DESCRIPCION FROM RED_CLASECONTACTO WHERE CLCN_CONSECUTIVO="+CNPR_CONS_CLASECONTACTO, "");
							
							
							info_contacto = dbREDD.obtenerDato("SELECT USR_NOMBRE AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+CNPR_CONS_CONTACTO, "");
							info_contacto += dbREDD.obtenerDato("SELECT '; E-Mail:' || USR_CORREOELECTRONIC AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+CNPR_CONS_CONTACTO, "");
							info_contacto += dbREDD.obtenerDato("SELECT '; Tel.:' || USR_TELEFONO AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+CNPR_CONS_CONTACTO, "");
							info_contacto += dbREDD.obtenerDato("SELECT '; Cel.:' || USR_MOVIL AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+CNPR_CONS_CONTACTO, "");
							info_contacto += dbREDD.obtenerDato("SELECT '; Org.:' || USR_ORGANIZACION AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+CNPR_CONS_CONTACTO, "");
							info_contacto += dbREDD.obtenerDato("SELECT '; Titl.:' || USR_CARGO AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+CNPR_CONS_CONTACTO, "");

							
							D = new PdfPCell(new Phrase(info_contacto));
							D.setBorder(0);
							tabla_Contactos.addCell(D);
							
							D = new PdfPCell(new Phrase(db_clasecontacto));
							D.setBorder(0);
							tabla_Contactos.addCell(D);
							
						}
						
						rset_Contactos.close();
					}
					else
					{
						r += "El conjunto de resultados retornados para la consulta ["+sql_Contactos+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
					}
				} catch (SQLException e) {
					r += "Excepción de SQL ["+sql_Contactos+"]: " + e.toString();
				} catch (Exception e) {
					r += "Ocurrió la siguiente excepción: " + e.toString() + " -- SQL: " + sql_Contactos;
				}
		        
				tabla_Contactos.completeRow();
				document.add(tabla_Contactos);

				
				
				document.newPage();

		        PdfPTable tabla_titulo_individuos = new PdfPTable(1);
		        tabla_titulo_individuos.setTotalWidth(540);
		        tabla_titulo_individuos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_individuos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_individuos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir(yo+"Individuos", idioma, "Individuos de la Parcela" + "..");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_individuos.addCell(cell);
		        tabla_titulo_individuos.completeRow();
		        
		        document.add(tabla_titulo_individuos);

		        
		        PdfPTable tabla_individuos = new PdfPTable(13);
		        tabla_individuos.setTotalWidth(540);
		        tabla_individuos.setLockedWidth(true);
				
		        tabla_individuos.setTableEvent(tableBackground);
		        tabla_individuos.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_individuos.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_individuos.getDefaultCell().setCellEvent(whiteRectangle);
		        
				String sql_individuos = "SELECT ";
				sql_individuos += "INDV_CONSECUTIVO,";
				sql_individuos += "INDV_SUBPARCELA,";
				sql_individuos += "INDV_INCLUIR,";
				sql_individuos += "INDV_NUMERO_ARBOL,";
				sql_individuos += "INDV_DISTANCIA,";
				sql_individuos += "INDV_AZIMUTH,";
				sql_individuos += "INDV_ESARBOLREFERENCIA,";
				sql_individuos += "INDV_CARDINALIDAD,";
				sql_individuos += "INDV_NUMERO_COLECTOR,";
				sql_individuos += "INDV_CANTIDAD_EJEMPLARES,";
				sql_individuos += "INDV_ESPECIE,";
				sql_individuos += "INDV_OBSERVACIONES,";
				sql_individuos += "INDV_TXCT_ID";
				sql_individuos += " FROM RED_INDIVIDUO ";
				sql_individuos += " WHERE INDV_PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
				sql_individuos += " ORDER BY INDV_NUMERO_ARBOL ";

				try {
					ResultSet rset_individuos = dbREDD.consultarBD(sql_individuos);
					
					if (rset_individuos != null)
					{
						String INDV_CONSECUTIVO = "";
						String INDV_SUBPARCELA = "";
						String INDV_INCLUIR = "";
						String INDV_NUMERO_ARBOL = "";
						String INDV_DISTANCIA = "";
						String INDV_AZIMUTH = "";
						String INDV_ESARBOLREFERENCIA = "";
						String INDV_CARDINALIDAD = "";
						String INDV_NUMERO_COLECTOR = "";
						String INDV_CANTIDAD_EJEMPLARES = "";
						String INDV_ESPECIE = "";
						String INDV_OBSERVACIONES = "";
						String INDV_TXCT_ID = "";
						String db_especie = "";
						String db_taxonomia = "";
						

						t = Auxiliar.traducir("INDV_CONSECUTIVO", idioma, "Consecutivo" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_NUMERO_ARBOL", idioma, "Número de árbol" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_SUBPARCELA", idioma, "Sub-Parcela" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_ESPECIE", idioma, "Especie" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("TAXONOMIA", idioma, "Taxonomía" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_DISTANCIA", idioma, "INDV_DISTANCIA" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_AZIMUTH", idioma, "Azimuth" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_ESARBOLREFERENCIA", idioma, "Es Árbol de Referencia" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_CARDINALIDAD", idioma, "Número de Brinzales de la Misma Especie" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_NUMERO_COLECTOR", idioma, "Número de Colector" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_CANTIDAD_EJEMPLARES", idioma, "Cantidad de Ejemplares Recolectados" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_OBSERVACIONES", idioma, "Observaciones" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

				        t = Auxiliar.traducir("INDV_INCLUIR", idioma, "Incluir en Cálculos" + " ");
				        E = new PdfPCell(new Phrase(t));
				        E.setBorder(0);
				        E.setHorizontalAlignment(Element.ALIGN_LEFT);
				        E.setVerticalAlignment(Element.ALIGN_TOP);
				        E.setCellEvent(cellBackground);
				        E.setCellEvent(roundRectangle);
				        E.setPadding(3);
				        E.setBorder(PdfPCell.NO_BORDER);		        
				        tabla_individuos.addCell(E);

						
						while (rset_individuos.next())
						{
							INDV_CONSECUTIVO = rset_individuos.getString("INDV_CONSECUTIVO");
							INDV_NUMERO_ARBOL = rset_individuos.getString("INDV_NUMERO_ARBOL");
							INDV_SUBPARCELA = rset_individuos.getString("INDV_SUBPARCELA");
							INDV_ESPECIE = rset_individuos.getString("INDV_ESPECIE");
							INDV_TXCT_ID = rset_individuos.getString("INDV_TXCT_ID");
							db_taxonomia = dbREDD.obtenerDato("SELECT TXCT_NME_SPC FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+INDV_TXCT_ID, "");
							INDV_DISTANCIA = rset_individuos.getString("INDV_DISTANCIA");
							INDV_AZIMUTH = rset_individuos.getString("INDV_AZIMUTH");
							INDV_ESARBOLREFERENCIA = rset_individuos.getString("INDV_ESARBOLREFERENCIA");
							INDV_CARDINALIDAD = rset_individuos.getString("INDV_CARDINALIDAD");
							INDV_NUMERO_COLECTOR = rset_individuos.getString("INDV_NUMERO_COLECTOR");
							INDV_CANTIDAD_EJEMPLARES = rset_individuos.getString("INDV_CANTIDAD_EJEMPLARES");
							INDV_OBSERVACIONES = rset_individuos.getString("INDV_OBSERVACIONES");
							INDV_INCLUIR = rset_individuos.getString("INDV_INCLUIR");

					        
							D = new PdfPCell(new Phrase(INDV_CONSECUTIVO));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_NUMERO_ARBOL));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_SUBPARCELA));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_ESPECIE));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(db_taxonomia));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_DISTANCIA));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_AZIMUTH));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_ESARBOLREFERENCIA));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_CARDINALIDAD));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_NUMERO_COLECTOR));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_CANTIDAD_EJEMPLARES));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
							D = new PdfPCell(new Phrase(INDV_OBSERVACIONES));
							D.setBorder(0);
							tabla_individuos.addCell(D);
							
					        D = new PdfPCell(new Phrase(INDV_INCLUIR));
					        D.setBorder(0);
					        tabla_individuos.addCell(D);

							
						}
						
						rset_individuos.close();
					}
					else
					{
						r += "El conjunto de resultados retornados para la consulta ["+sql_individuos+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
					}
				} catch (SQLException e) {
					r += "Excepción de SQL ["+sql_individuos+"]: " + e.toString();
				} catch (Exception e) {
					r += "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql_individuos;
				}
		        

				
				tabla_individuos.completeRow();
				document.add(tabla_individuos);
				
				
				document.newPage();
				
				
				PdfPTable tabla_titulo_imagenes = new PdfPTable(1);
				tabla_titulo_imagenes.setTotalWidth(540);
				tabla_titulo_imagenes.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabla_titulo_imagenes.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				tabla_titulo_imagenes.getDefaultCell().setCellEvent(whiteRectangle);
				
				t = Auxiliar.traducir(yo+"Imagenes_Parcela", idioma, "Imágenes de la Parcela" + "..");
				
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

				String sql_imagenes = "SELECT PIMG, PRCL_CONSECUTIVO, NOMBRE FROM RED_PARCELA_IMAGEN WHERE PRCL_CONSECUTIVO=" + f_PRCL_CONSECUTIVO;
				
				ResultSet rs_imagenes = dbREDD.consultarBD(sql_imagenes);
				
				if (rs_imagenes != null) {
					String nombre = "";

					while (rs_imagenes.next()) {
						nombre = rs_imagenes.getString("NOMBRE");
						if (!Auxiliar.nz(nombre, "").equals("")) {
							String carpeta_imagenes_parcelas = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_parcelas'", "");
							if (!Auxiliar.tieneAlgo(carpeta_imagenes_parcelas)) {
								carpeta_imagenes_parcelas = getServletContext().getRealPath("") + File.separator + "imagenes_parcelas";
							}
							ruta_archivo = carpeta_imagenes_parcelas + "/" + f_PRCL_CONSECUTIVO + "/" + nombre;
							
							if (archie.existeArchivo(ruta_archivo)) {
								img = Image.getInstance(ruta_archivo);
								img.setDpi(300, 300);
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
						if (!Auxiliar.nz(a_fotos[i], "").equals("")) {
							ruta_archivo = getServletContext().getRealPath("") + File.separator + "imagenes_parcelas/" + f_PRCL_CONSECUTIVO + "/" + a_fotos[i];
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
				}
				*/
				
				tabla_imagenes.completeRow();
				document.add(tabla_imagenes);

				document.newPage();
				
				//com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph();
				
				PdfPTable tabla_titulo_mapa = new PdfPTable(1);
				tabla_titulo_mapa.setTotalWidth(540);
				tabla_titulo_mapa.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabla_titulo_mapa.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				tabla_titulo_mapa.getDefaultCell().setCellEvent(whiteRectangle);
				
				t = Auxiliar.traducir(yo+"Mapa_Parcela", idioma, "Mapa de la Parcela" + "..");
				
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
				
				boolean ok_crear_mapa = generarMapaParcela(f_PRCL_CONSECUTIVO, session);
				
				ruta_archivo = getServletContext().getRealPath("") + File.separator + "mapas_parcelas/" + f_PRCL_CONSECUTIVO + "-mapa.png";
				if (archie.existeArchivo(ruta_archivo)) {
					img = Image.getInstance(ruta_archivo);
					img.setDpi(300, 300);
					img.scaleToFit(540, 800);
					cell = new PdfPCell(img);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					tabla_mapa.addCell(cell);
				}
				
				tabla_mapa.completeRow();
				document.add(tabla_mapa);
				
				
				// AGREGAR MAPA DEL TRACKLOG AL CAMPAMENTO
				
				document.newPage();
				
				PdfPTable tabla_titulo_tracklog_campamento = new PdfPTable(1);
				tabla_titulo_tracklog_campamento.setTotalWidth(540);
				tabla_titulo_tracklog_campamento.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
				tabla_titulo_tracklog_campamento.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				tabla_titulo_tracklog_campamento.getDefaultCell().setCellEvent(whiteRectangle);
				
				t = Auxiliar.traducir(yo+"Mapa_tracklog_campamento", idioma, "Mapa del Tracklog al Campamento" + "..");
				
				cell = new PdfPCell(new Phrase(t + "\n"));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setCellEvent(cellBackground);
				cell.setCellEvent(roundRectangle);
				cell.setPadding(3);
				tabla_titulo_tracklog_campamento.addCell(cell);
				tabla_titulo_tracklog_campamento.completeRow();
				
				document.add(tabla_titulo_tracklog_campamento);
				
				PdfPTable tabla_mapa_tracklog_campamento= new PdfPTable(1);
				tabla_titulo_tracklog_campamento.setTotalWidth(540);
				
				boolean ok_crear_mapa_tracklog_campamento = generarMapaTrackLogCampamento(f_PRCL_CONSECUTIVO, session);
				
				ruta_archivo = getServletContext().getRealPath("") + File.separator + "mapas_parcelas/" + f_PRCL_CONSECUTIVO + "-mapa-tracklog-campamento.png";
				if (archie.existeArchivo(ruta_archivo)) {
					img = Image.getInstance(ruta_archivo);
					img.setDpi(300, 300);
					img.scaleToFit(540, 800);
					cell = new PdfPCell(img);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					tabla_mapa_tracklog_campamento.addCell(cell);
				}
				
				tabla_mapa_tracklog_campamento.completeRow();
				document.add(tabla_mapa_tracklog_campamento);

				
				// AGREGAR MAPA DEL TRACKLOG A LA PARCELA
				
				document.newPage();

		        PdfPTable tabla_titulo_tracklog_parcela = new PdfPTable(1);
		        tabla_titulo_tracklog_parcela.setTotalWidth(540);
		        tabla_titulo_tracklog_parcela.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tabla_titulo_tracklog_parcela.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		        tabla_titulo_tracklog_parcela.getDefaultCell().setCellEvent(whiteRectangle);
		        
				t = Auxiliar.traducir(yo+"Mapa_tracklog_parcela", idioma, "Mapa del Tracklog a la Parcela" + "..");

		        cell = new PdfPCell(new Phrase(t + "\n"));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		        cell.setBorder(0);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_TOP);
		        cell.setCellEvent(cellBackground);
		        cell.setCellEvent(roundRectangle);
		        cell.setPadding(3);
		        tabla_titulo_tracklog_parcela.addCell(cell);
		        tabla_titulo_tracklog_parcela.completeRow();
		        
		        document.add(tabla_titulo_tracklog_parcela);
		        
		        PdfPTable tabla_mapa_tracklog_parcela= new PdfPTable(1);
		        tabla_mapa_tracklog_parcela.setTotalWidth(540);

		        boolean ok_crear_mapa_tracklog_parcela = generarMapaTrackLogParcela(f_PRCL_CONSECUTIVO, session);
		        
				ruta_archivo = getServletContext().getRealPath("") + File.separator + "mapas_parcelas/" + f_PRCL_CONSECUTIVO + "-mapa-tracklog-parcela.png";
				if (archie.existeArchivo(ruta_archivo)) {
					img = Image.getInstance(ruta_archivo);
					
					/*
					java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(ruta_archivo);
					img = Image.getInstance(writer, awtImage, 1);
					*/

					/*
					img.setCompressionLevel(100);
					
					img.setDpi(1000, 1000);
					float w = img.getWidth();
					float h = img.getHeight();
					
					float nw = w * ratio_resolucion;
					float nh = h * ratio_resolucion;
					
					img.scaleAbsolute(nw, nh);
					
					img.scaleToFit(540, 800);
					
					*/
					/*
					float w = img.getWidth();
					float h = img.getHeight();
					
					float nw = w / ratio_resolucion;
					float nh = h / ratio_resolucion;
					
					img.scaleAbsolute(nw, nh);
					*/

					//img.scaleAbsolute(555f, 713f);

					//img.scalePercent(50f);
					
					img.scaleToFit(540, 800);

			        cell = new PdfPCell(img);
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setBorder(0);
			        tabla_mapa_tracklog_parcela.addCell(cell);
				}
				
				tabla_mapa_tracklog_parcela.completeRow();
				document.add(tabla_mapa_tracklog_parcela);

		        document.close();
		        
		        if (guardar) {
					r = "<a class=boton href='" + EXPORT_PDF_DIRECTORY + "/" + nombre_PDF + "' target='_blank'>";
					r += Auxiliar.traducir(yo+"Descargar_ficha_de_la_parcela_en_PDF", idioma, "Descargar ficha de la parcela en PDF" + "..");
					r += "</a>";
		        }
		        else {
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
				r += "No se encontraron resultados.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en exportarParcelaPDF(): " + e.toString() + " -- SQL: " + sql;
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

    	String yo = "Parcela.";
    	String t = "";

        /** The header text. */
        String header;
        /** The template with the total number of pages. */
        PdfTemplate total;
        
        //Auxiliar aux = new Auxiliar();
        String idioma = "es";

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
    		Archivo archie = new Archivo();
    		
    		String ruta_archivo = "";
        	
	        PdfPTable tabla_encabezado = new PdfPTable(3);
            //PdfPTable table = new PdfPTable(3);
            try {
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
				t = Auxiliar.traducir("General.Detalle_Parcela", idioma, "Detalle de la Parcela" + "..");
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
    		
    		Archivo archie = new Archivo();

    		String ruta_archivo = "";
    		
    		PdfPTable tabla = new PdfPTable(5);
    		try {
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
	 * Método que crea un archivo de excel para un individuo especificado con f_codigo.
     * 
     * @param PRCL_NOMBRE
     * @param PRCL_CONSECUTIVO
     * @param PRCL_CONS_PAIS
     * @param departamentos_seleccionados
     * @param municipios_seleccionados
     * @param PRCL_FECHAINI_APROXIMACION
     * @param response
     * @param session
	 * @return true si hubo éxito, false de lo contrario
     * @throws ClassNotFoundException
     * @throws Exception
     */
	public boolean exportarParcelasExcel(
			String PRCL_NOMBRE,
			String PRCL_CONSECUTIVO,
			String PRCL_CONS_PAIS,
			String departamentos_seleccionados,
			String municipios_seleccionados,
			String PRCL_FECHAINI_APROXIMACION,
			HttpServletResponse response, 
			HttpSession session
			)
		throws ClassNotFoundException, Exception {
		String metodo = yo + "exportarParcelasExcel";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    Sec sec = new Sec();
	    
		boolean ok = false;
		
		String w_nombre = "";
		String w_codigo = "";
		String w_pais = "";
		String w_departamentos = "";
		String w_municipios = "";
		String w_fecha = "";
		
		if (Auxiliar.tieneAlgo(PRCL_NOMBRE)) {
			w_nombre = " AND LOWER(R.PRCL_NOMBRE) LIKE '%"+PRCL_NOMBRE.toLowerCase()+"%' ";
		}

		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			w_codigo = " AND R.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
		}
		
		if (Auxiliar.tieneAlgo(PRCL_FECHAINI_APROXIMACION)) {
			w_fecha = " AND R.PRCL_FECHAINI_APROXIMACION >= TO_DATE('"+PRCL_FECHAINI_APROXIMACION+"', 'YYYY-MM-DD HH24:MI:SS') ";
		}
		
		if (Auxiliar.tieneAlgo(PRCL_CONS_PAIS)) {
			w_pais = " AND R.PRCL_CONS_PAIS=" + PRCL_CONS_PAIS;
		}

		if (Auxiliar.tieneAlgo(departamentos_seleccionados)) {
			w_departamentos = " AND R.PRCL_CONSECUTIVO IN (SELECT DPPR_CONS_PARCELA FROM RED_DEPTO_PARCELA WHERE DPPR_CONS_DEPTO IN (" + departamentos_seleccionados + ")) ";
		}

		if (Auxiliar.tieneAlgo(municipios_seleccionados)) {
			w_municipios = " AND R.PRCL_CONSECUTIVO IN (SELECT MNPR_PARCELA FROM RED_MUNICIPIO_PARCELA WHERE MNPR_MUNICIPIO IN (" + municipios_seleccionados + ")) ";
		}
		
		int i = 0;
		
		String sql = "SELECT ";
		sql += "R.PRCL_CONSECUTIVO,";
		sql += "R.PRCL_ID_IMPORTACION,";
		sql += "R.PRCL_ID_UPM,";
		sql += "R.PRCL_CONS_PAIS,";
		sql += "R.PRCL_VEREDA,";
		sql += "R.PRCL_CORREGIMIENTO,";
		sql += "R.PRCL_INSPECCION_POLICIA,";
		sql += "R.PRCL_CASERIO,";
		sql += "R.PRCL_RANCHERIA,";
		sql += "TO_CHAR(R.PRCL_FECHA_CAPTURA, 'YYYY-MM-DD') AS PRCL_FECHA_CAPTURA,";
		sql += "R.PRCL_MEDIOACCESO_POBLADO,";
		sql += "R.PRCL_DISTANCIA_POBLADO,";
		sql += "R.PRCL_TPOBLADO_H,";
		sql += "R.PRCL_TPOBLADO_M,";
		sql += "R.PRCL_MEDIOACCESO_CAMPAMENTO,";
		sql += "R.PRCL_DISTANCIA_CAMPAMENTO,";
		sql += "R.PRCL_TCAMPAMENTO_H,";
		sql += "R.PRCL_TCAMPAMENTO_M,";
		sql += "R.PRCL_MEDIOACCESO_JALON,";
		sql += "R.PRCL_DISTANCIA_JALON,";
		sql += "R.PRCL_TJALON_H,";
		sql += "R.PRCL_TJALON_M,";
		//sql += "R.PRCL_COORDENADAS,";
		sql += "R.PRCL_DISTANCIA_CAMPAMENTOS,";
		sql += "R.PRCL_LATITUD,";
		sql += "R.PRCL_LONGITUD,";
		sql += "R.PRCL_ALTITUD,";
		sql += "R.PRCL_NOMBRE,";
		sql += "R.PRCL_USR_DILIGENCIA_F1,";
		sql += "R.PRCL_USR_DILIGENCIA_F2,";
		sql += "TO_CHAR(R.PRCL_FECHAINI_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_APROXIMACION,";
		sql += "TO_CHAR(R.PRCL_FECHAFIN_APROXIMACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_APROXIMACION,";
		sql += "TO_CHAR(R.PRCL_FECHAINI_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAINI_LOCALIZACION,";
		sql += "TO_CHAR(R.PRCL_FECHAFIN_LOCALIZACION, 'YYYY-MM-DD') AS PRCL_FECHAFIN_LOCALIZACION,";
		sql += "R.PRCL_DESCRIPCION,";
		sql += "R.PRCL_OBSERVACIONES,";
		sql += "R.PRCL_TRACKLOG_CAMPAMENTO,";
		sql += "R.PRCL_TRACKLOG_PARCELA,";
		sql += "R.PRCL_SPF1_DILIGENCIA,";
		sql += "R.PRCL_SPF1_FECHAINI,";
		sql += "R.PRCL_SPF1_FECHAFIN,";
		sql += "R.PRCL_SPF1_POSIBLE,";
		sql += "R.PRCL_SPF1_JUSTIFICACION_NO,";
		sql += "R.PRCL_SPF1_OBS_FUSTALES,";
		sql += "R.PRCL_SPF1_OBS_LATIZALES,";
		sql += "R.PRCL_SPF1_OBS_BRINZALES,";
		sql += "R.PRCL_SPF2_DILIGENCIA,";
		sql += "R.PRCL_SPF2_FECHAINI,";
		sql += "R.PRCL_SPF2_FECHAFIN,";
		sql += "R.PRCL_SPF2_POSIBLE,";
		sql += "R.PRCL_SPF2_JUSTIFICACION_NO,";
		sql += "R.PRCL_SPF2_OBS_FUSTALES,";
		sql += "R.PRCL_SPF2_OBS_LATIZALES,";
		sql += "R.PRCL_SPF2_OBS_BRINZALES,";
		sql += "R.PRCL_SPF3_DILIGENCIA,";
		sql += "R.PRCL_SPF3_FECHAINI,";
		sql += "R.PRCL_SPF3_FECHAFIN,";
		sql += "R.PRCL_SPF3_POSIBLE,";
		sql += "R.PRCL_SPF3_JUSTIFICACION_NO,";
		sql += "R.PRCL_SPF3_OBS_FUSTALES,";
		sql += "R.PRCL_SPF3_OBS_LATIZALES,";
		sql += "R.PRCL_SPF3_OBS_BRINZALES,";
		sql += "R.PRCL_SPF4_DILIGENCIA,";
		sql += "R.PRCL_SPF4_FECHAINI,";
		sql += "R.PRCL_SPF4_FECHAFIN,";
		sql += "R.PRCL_SPF4_POSIBLE,";
		sql += "R.PRCL_SPF4_JUSTIFICACION_NO,";
		sql += "R.PRCL_SPF4_OBS_FUSTALES,";
		sql += "R.PRCL_SPF4_OBS_LATIZALES,";
		sql += "R.PRCL_SPF4_OBS_BRINZALES,";
		sql += "R.PRCL_SPF5_DILIGENCIA,";
		sql += "R.PRCL_SPF5_FECHAINI,";
		sql += "R.PRCL_SPF5_FECHAFIN,";
		sql += "R.PRCL_SPF5_POSIBLE,";
		sql += "R.PRCL_SPF5_JUSTIFICACION_NO,";
		sql += "R.PRCL_SPF5_OBS_FUSTALES,";
		sql += "R.PRCL_SPF5_OBS_LATIZALES,";
		sql += "R.PRCL_SPF5_OBS_BRINZALES,";
		sql += "R.PRCL_PLOT,";
		sql += "R.PRCL_AREA,";
		sql += "R.PRCL_TEMPORALIDAD,";
		sql += "R.PRCL_PUBLICA,";
		sql += "R.PRCL_HAB,";
		sql += "R.PRCL_DAP,";
		sql += "R.PRCL_GPS,";
		sql += "R.PRCL_EQ,";
		sql += "R.PRCL_BA,";
		sql += "R.PRCL_BS,";
		sql += "R.PRCL_BT,";
		sql += "R.PRCL_AUTORCUSTODIOINFO,";
		sql += "R.PRCL_TIPOBOSQUE,";
		sql += "R.PRCL_INCLUIR,";
		sql += "TO_CHAR(R.PRCL_ACTUALIZACION, 'YYYY-MM-DD HH24:MI:SS') AS PRCL_ACTUALIZACION,";
		sql += "R.PRCL_DEPARTAMENTO,";
		sql += "R.PRCL_MUNICIPIO,";
		sql += "D.NOMBRE AS DEPARTAMENTO,";
		sql += "M.NOMBRE AS MUNICIPIO,";
		sql += "TB.TPBS_NOMBRE || ' -- ' || TB.TPBS_DESCRIPCION AS TIPOBOSQUE,";
		sql += "RI.RINOMBRE || ' -- ' || RI.RIETNIA AS RESGUARDOINDIGENA,";
		sql += "RC.CAR AS CAR,";
		sql += "PN.NOMBRE_PN AS PNN";
		sql += " FROM RED_PARCELA R ";
		sql += " LEFT OUTER JOIN RED_DEPTOS_SHAPE D ON R.PRCL_DEPARTAMENTO=D.CODIGO ";
		sql += " LEFT OUTER JOIN RED_MUNICIPIOS_SHAPE M ON R.PRCL_MUNICIPIO=M.CODIGO ";
		sql += " LEFT OUTER JOIN RED_TIPOBOSQUE TB ON R.PRCL_CONS_TIPOBOSQUE=TB.TPBS_CONSECUTIVO ";
		sql += " LEFT OUTER JOIN RED_RESGUARDO_INDIGENA RI ON R.PRCL_CONS_RESGUARDOINDIGENA=RI.OBJECTID ";
		sql += " LEFT OUTER JOIN RED_CORPORACIONES RC ON R.PRCL_CONS_CAR=RC.ID_CAR ";
		sql += " LEFT OUTER JOIN RED_PARQUES_NACIONALES PN ON R.PRCL_CONS_PNN=PN.OBJECTID ";
		sql += " WHERE 1=1 ";
		if (!sec.tienePermiso(usuario,  "121")) {
			sql += " AND R.PRCL_PUBLICA=1 ";
		}
		sql += w_nombre;
		sql += w_codigo;
		sql += w_pais;
		sql += w_departamentos;
		sql += w_municipios;
		sql += w_fecha;
		sql += " ORDER BY PRCL_CONSECUTIVO ";

		int j=0;
		
		try {
			HSSFWorkbook libro = new HSSFWorkbook();
			Sheet hoja = libro.createSheet("PARCELAS");
			
			Row fila_titulos = hoja.createRow(0);

			ResultSet rset = dbREDD.consultarBD(sql);

			j=-1;
			j++;fila_titulos.createCell(j).setCellValue("ACCION");
			j++;fila_titulos.createCell(j).setCellValue("CONSECUTIVO");
			j++;fila_titulos.createCell(j).setCellValue("ID_IMPORTACION");
			j++;fila_titulos.createCell(j).setCellValue("ID_UPM");
			j++;fila_titulos.createCell(j).setCellValue("PAIS");
			j++;fila_titulos.createCell(j).setCellValue("VEREDA");
			j++;fila_titulos.createCell(j).setCellValue("CORREGIMIENTO");
			j++;fila_titulos.createCell(j).setCellValue("INSPECCION_POLICIA");
			j++;fila_titulos.createCell(j).setCellValue("CASERIO");
			j++;fila_titulos.createCell(j).setCellValue("RANCHERIA");
			j++;fila_titulos.createCell(j).setCellValue("FECHA_CAPTURA");
			j++;fila_titulos.createCell(j).setCellValue("MEDIOACCESO_POBLADO");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_POBLADO");
			j++;fila_titulos.createCell(j).setCellValue("TPOBLADO_H");
			j++;fila_titulos.createCell(j).setCellValue("TPOBLADO_M");
			j++;fila_titulos.createCell(j).setCellValue("MEDIOACCESO_CAMPAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_CAMPAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("TCAMPAMENTO_H");
			j++;fila_titulos.createCell(j).setCellValue("TCAMPAMENTO_M");
			j++;fila_titulos.createCell(j).setCellValue("MEDIOACCESO_JALON");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_JALON");
			j++;fila_titulos.createCell(j).setCellValue("TJALON_H");
			j++;fila_titulos.createCell(j).setCellValue("TJALON_M");
			//j++;fila_titulos.createCell(j).setCellValue("COORDENADAS");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_CAMPAMENTOS");
			j++;fila_titulos.createCell(j).setCellValue("LATITUD");
			j++;fila_titulos.createCell(j).setCellValue("LONGITUD");
			j++;fila_titulos.createCell(j).setCellValue("NOMBRE");
			j++;fila_titulos.createCell(j).setCellValue("USR_DILIGENCIA_F1");
			j++;fila_titulos.createCell(j).setCellValue("USR_DILIGENCIA_F2");
			j++;fila_titulos.createCell(j).setCellValue("FECHAINI_APROXIMACION");
			j++;fila_titulos.createCell(j).setCellValue("FECHAFIN_APROXIMACION");
			j++;fila_titulos.createCell(j).setCellValue("FECHAINI_LOCALIZACION");
			j++;fila_titulos.createCell(j).setCellValue("FECHAFIN_LOCALIZACION");
			j++;fila_titulos.createCell(j).setCellValue("DESCRIPCION");
			j++;fila_titulos.createCell(j).setCellValue("OBSERVACIONES");
			j++;fila_titulos.createCell(j).setCellValue("TRACKLOG_CAMPAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("TRACKLOG_PARCELA");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF1_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF2_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF3_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF4_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("PRCL_SPF5_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("PLOT");
			j++;fila_titulos.createCell(j).setCellValue("AREA");
			j++;fila_titulos.createCell(j).setCellValue("TEMPORALIDAD");
			j++;fila_titulos.createCell(j).setCellValue("PUBLICA");
			j++;fila_titulos.createCell(j).setCellValue("HAB");
			j++;fila_titulos.createCell(j).setCellValue("DAP");
			j++;fila_titulos.createCell(j).setCellValue("GPS");
			j++;fila_titulos.createCell(j).setCellValue("EQ");
			j++;fila_titulos.createCell(j).setCellValue("BA");
			j++;fila_titulos.createCell(j).setCellValue("BS");
			j++;fila_titulos.createCell(j).setCellValue("BT");
			j++;fila_titulos.createCell(j).setCellValue("AUTORCUSTODIOINFO");
			j++;fila_titulos.createCell(j).setCellValue("TIPOBOSQUE");
			j++;fila_titulos.createCell(j).setCellValue("INCLUIR");
			j++;fila_titulos.createCell(j).setCellValue("ACTUALIZACION");
			j++;fila_titulos.createCell(j).setCellValue("DEPARTAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("MUNICIPIO");
			j++;fila_titulos.createCell(j).setCellValue("TIPOBOSQUE");
			j++;fila_titulos.createCell(j).setCellValue("RESGUARDOINDIGENA");
			j++;fila_titulos.createCell(j).setCellValue("CAR");
			j++;fila_titulos.createCell(j).setCellValue("PNN");
			
			while (rset.next()) {
			
				i++;
				Row fila_datos = hoja.createRow(i);

				j=-1;
				j++;fila_datos.createCell(j).setCellValue("");
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_CONSECUTIVO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_ID_IMPORTACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_ID_UPM"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_CONS_PAIS"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_VEREDA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_CORREGIMIENTO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_INSPECCION_POLICIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_CASERIO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_RANCHERIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_FECHA_CAPTURA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_MEDIOACCESO_POBLADO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_DISTANCIA_POBLADO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TPOBLADO_H"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TPOBLADO_M"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_MEDIOACCESO_CAMPAMENTO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_DISTANCIA_CAMPAMENTO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TCAMPAMENTO_H"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TCAMPAMENTO_M"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_MEDIOACCESO_JALON"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_DISTANCIA_JALON"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TJALON_H"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TJALON_M"),""));
				//j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_COORDENADAS"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_DISTANCIA_CAMPAMENTOS"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_LATITUD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_LONGITUD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_ALTITUD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_NOMBRE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_USR_DILIGENCIA_F1"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_USR_DILIGENCIA_F2"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_FECHAINI_APROXIMACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_FECHAFIN_APROXIMACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_FECHAINI_LOCALIZACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_FECHAFIN_LOCALIZACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_DESCRIPCION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_OBSERVACIONES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TRACKLOG_CAMPAMENTO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TRACKLOG_PARCELA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_DILIGENCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_FECHAINI"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_FECHAFIN"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_POSIBLE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_JUSTIFICACION_NO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_OBS_FUSTALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_OBS_LATIZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF1_OBS_BRINZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_DILIGENCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_FECHAINI"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_FECHAFIN"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_POSIBLE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_JUSTIFICACION_NO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_OBS_FUSTALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_OBS_LATIZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF2_OBS_BRINZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_DILIGENCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_FECHAINI"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_FECHAFIN"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_POSIBLE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_JUSTIFICACION_NO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_OBS_FUSTALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_OBS_LATIZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF3_OBS_BRINZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_DILIGENCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_FECHAINI"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_FECHAFIN"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_POSIBLE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_JUSTIFICACION_NO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_OBS_FUSTALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_OBS_LATIZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF4_OBS_BRINZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_DILIGENCIA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_FECHAINI"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_FECHAFIN"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_POSIBLE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_JUSTIFICACION_NO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_OBS_FUSTALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_OBS_LATIZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_SPF5_OBS_BRINZALES"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_PLOT"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_AREA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TEMPORALIDAD"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_PUBLICA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_HAB"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_DAP"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_GPS"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_EQ"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_BA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_BS"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_BT"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_AUTORCUSTODIOINFO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_TIPOBOSQUE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_INCLUIR"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PRCL_ACTUALIZACION"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("DEPARTAMENTO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("MUNICIPIO"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("TIPOBOSQUE"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("RESGUARDOINDIGENA"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("CAR"),""));
				j++;fila_datos.createCell(j).setCellValue(Auxiliar.nz(rset.getString("PNN"),""));
			}
			
			rset.close();
			if (i==0) {
				i++;
				Row fila_datos = hoja.createRow(i);
				for (j=0;j<=108;j++) {
					fila_datos.createCell(j).setCellValue("");
				}
			}
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=parcelas.xls");

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
	 * Metodo para generar la plantilla de cargue masivo de parcelas
	 * 
	 * @param response
	 * @return true si la pudo generar, false de lo contrario
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean generarPlantillaParcelas(HttpServletResponse response, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarPlantillaParcelas";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		boolean ok = false;
		
		int i = 0;
		int j=0;
		
		try {
			HSSFWorkbook libro = new HSSFWorkbook();
			Sheet hoja = libro.createSheet("PARCELA");
			
			Row fila_titulos = hoja.createRow(0);

			j=-1;
			j++;fila_titulos.createCell(j).setCellValue("ACCION");
			j++;fila_titulos.createCell(j).setCellValue("CONSECUTIVO");
			j++;fila_titulos.createCell(j).setCellValue("ID_IMPORTACION");
			j++;fila_titulos.createCell(j).setCellValue("ID_UPM");
			j++;fila_titulos.createCell(j).setCellValue("PAIS");
			j++;fila_titulos.createCell(j).setCellValue("VEREDA");
			j++;fila_titulos.createCell(j).setCellValue("CORREGIMIENTO");
			j++;fila_titulos.createCell(j).setCellValue("INSPECCION_POLICIA");
			j++;fila_titulos.createCell(j).setCellValue("CASERIO");
			j++;fila_titulos.createCell(j).setCellValue("RANCHERIA");
			j++;fila_titulos.createCell(j).setCellValue("FECHA_CAPTURA");
			j++;fila_titulos.createCell(j).setCellValue("MEDIOACCESO_POBLADO");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_POBLADO");
			j++;fila_titulos.createCell(j).setCellValue("TPOBLADO_H");
			j++;fila_titulos.createCell(j).setCellValue("TPOBLADO_M");
			j++;fila_titulos.createCell(j).setCellValue("MEDIOACCESO_CAMPAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_CAMPAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("TCAMPAMENTO_H");
			j++;fila_titulos.createCell(j).setCellValue("TCAMPAMENTO_M");
			j++;fila_titulos.createCell(j).setCellValue("MEDIOACCESO_JALON");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_JALON");
			j++;fila_titulos.createCell(j).setCellValue("TJALON_H");
			j++;fila_titulos.createCell(j).setCellValue("TJALON_M");
			//j++;fila_titulos.createCell(j).setCellValue("COORDENADAS");
			j++;fila_titulos.createCell(j).setCellValue("DISTANCIA_CAMPAMENTOS");
			j++;fila_titulos.createCell(j).setCellValue("LATITUD");
			j++;fila_titulos.createCell(j).setCellValue("LONGITUD");
			j++;fila_titulos.createCell(j).setCellValue("ALTITUD");
			j++;fila_titulos.createCell(j).setCellValue("NOMBRE");
			j++;fila_titulos.createCell(j).setCellValue("USR_DILIGENCIA_F1");
			j++;fila_titulos.createCell(j).setCellValue("USR_DILIGENCIA_F2");
			j++;fila_titulos.createCell(j).setCellValue("FECHAINI_APROXIMACION");
			j++;fila_titulos.createCell(j).setCellValue("FECHAFIN_APROXIMACION");
			j++;fila_titulos.createCell(j).setCellValue("FECHAINI_LOCALIZACION");
			j++;fila_titulos.createCell(j).setCellValue("FECHAFIN_LOCALIZACION");
			j++;fila_titulos.createCell(j).setCellValue("DESCRIPCION");
			j++;fila_titulos.createCell(j).setCellValue("OBSERVACIONES");
			j++;fila_titulos.createCell(j).setCellValue("TRACKLOG_CAMPAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("TRACKLOG_PARCELA");
			j++;fila_titulos.createCell(j).setCellValue("DEPARTAMENTO");
			j++;fila_titulos.createCell(j).setCellValue("MUNICIPIO");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF1_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF2_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF3_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF4_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_DILIGENCIA");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_FECHAINI");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_FECHAFIN");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_POSIBLE");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_JUSTIFICACION_NO");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_OBS_FUSTALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_OBS_LATIZALES");
			j++;fila_titulos.createCell(j).setCellValue("SPF5_OBS_BRINZALES");
			j++;fila_titulos.createCell(j).setCellValue("PLOT");
			j++;fila_titulos.createCell(j).setCellValue("AREA");
			j++;fila_titulos.createCell(j).setCellValue("TEMPORALIDAD");
			j++;fila_titulos.createCell(j).setCellValue("PUBLICA");
			j++;fila_titulos.createCell(j).setCellValue("HAB");
			j++;fila_titulos.createCell(j).setCellValue("DAP");
			j++;fila_titulos.createCell(j).setCellValue("GPS");
			j++;fila_titulos.createCell(j).setCellValue("EQ");
			j++;fila_titulos.createCell(j).setCellValue("BA");
			j++;fila_titulos.createCell(j).setCellValue("BS");
			j++;fila_titulos.createCell(j).setCellValue("BT");
			j++;fila_titulos.createCell(j).setCellValue("AUTORCUSTODIOINFO");
			j++;fila_titulos.createCell(j).setCellValue("TIPOBOSQUE");
			j++;fila_titulos.createCell(j).setCellValue("INCLUIR");
			j++;fila_titulos.createCell(j).setCellValue("ACTUALIZACION");
			
			i++;
			
			Row fila_datos = hoja.createRow(i);

			String o = "";

			t = Auxiliar.traducir("CargueMasivo.Mensaje_Opciones", idioma, "Opciones:" + "..");
			String mensaje_o = t;

			j=-1;

			t = Auxiliar.traducir("CargueMasivo.Parcelas.ACCION", idioma, "Poner ELIMINAR para eliminar la parcela existente. Requiere PRCL_CONSECUTIVO. De lo contrario dejar vacío." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.CONSECUTIVO", idioma, "Entero positivo. Consecutivo de la parcela" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.ID_IMPORTACION", idioma, "Entero positivo. Consecutivo de importación de la parcela" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.ID_UPM", idioma, "Entero positivo. Obligatorio. Identificador de la unidad primaria de medición." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.PAIS", idioma, "Entero positivo. Identificador (código internacional) del país." + "..");
			o = Auxiliar.cargarOpciones("SELECT PAIS, NOMBRE FROM RED_PAIS_SHAPE ORDER BY NOMBRE", "PAIS", "NOMBRE", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.VEREDA", idioma, "Texto. Max 255 caracteres. Nombre de la vereda." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.CORREGIMIENTO", idioma, "Texto. Max 255 caracteres. Nombre del corregimiento." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.INSPECCION_POLICIA", idioma, "Texto. Max 255 caracteres. Nombre de la inspección de policía." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.CASERIO", idioma, "Texto. Max 255 caracteres. Nombre del caserío." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.RANCHERIA", idioma, "Texto. Max 255 caracteres. Nombre de la ranchería." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.FECHA_CAPTURA", idioma, "Fecha. Obligatorio. Formato aaaa-mm-dd. Fecha de la captura de la parcela. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.MEDIOACCESO_POBLADO", idioma, "Entero positivo. Medio de acceso desde la capital del departamento al poblado más cercano de la parcela." + "..");
			o = Auxiliar.cargarOpciones("SELECT MACC_ID, MACC_NOMBRE FROM RED_MEDIOACCESO ORDER BY MACC_NOMBRE", "MACC_ID", "MACC_NOMBREMACC_NOMBRE", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.DISTANCIA_POBLADO", idioma, "Número Decimal. Distancia desde la capital del departamento al poblado más cercano de la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TPOBLADO_H", idioma, "Entero positivo. Horas de viaje desde la capital del departamento hasta el poblado más cercano a la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TPOBLADO_M", idioma, "Entero entre 0 y 59. Minutos de viaje (después de las horas) desde la capital del departamento hasta el poblado más cercano a la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.MEDIOACCESO_CAMPAMENTO", idioma, "Entero positivo. Medio de acceso desde el poblado más cercano de la parcela al campamento." + "..");
			o = Auxiliar.cargarOpciones("SELECT MACC_ID, MACC_NOMBRE FROM RED_MEDIOACCESO ORDER BY MACC_NOMBRE", "MACC_ID", "MACC_NOMBREMACC_NOMBRE", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.DISTANCIA_CAMPAMENTO", idioma, "Número Decimal. Distancia desde desde el poblado más cercano de la parcela al campamento." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TCAMPAMENTO_H", idioma, "Entero positivo. Horas de viaje desde desde el poblado más cercano de la parcela al campamento." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TCAMPAMENTO_M", idioma, "Entero entre 0 y 59. Minutos de viaje (después de las horas) desde desde el poblado más cercano de la parcela al campamento." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.MEDIOACCESO_JALON", idioma, "Entero positivo. Medio de acceso desde el campamento al jalón de la parcela." + "..");
			o = Auxiliar.cargarOpciones("SELECT MACC_ID, MACC_NOMBRE FROM RED_MEDIOACCESO ORDER BY MACC_NOMBRE", "MACC_ID", "MACC_NOMBREMACC_NOMBRE", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.DISTANCIA_JALON", idioma, "Número Decimal. Distancia en metros desde el campamento al jalón de la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TJALON_H", idioma, "Entero positivo. Horas de viaje desde el campamento al jalón de la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TJALON_M", idioma, "Entero entre 0 y 59. Minutos de viaje (después de las horas) desde el campamento al jalón de la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			/*
			t = Auxiliar.traducir("CargueMasivo.Parcelas.COORDENADAS", idioma, "Texto. Max 4000 caracteres. Opcional. Concatenación de coordenadas separadas por _.  Cada coordenada es una tripleta de secuencia, latitud, longitud.  Ejemplo: 1,4.5423,-75.4234_2,4.3243,-75.3423_3,4.2234,-75.4224" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			*/
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.DISTANCIA_CAMPAMENTOS", idioma, "Número Decimal. Distancia entre campamentos." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.LATITUD", idioma, "Número Decimal. Obligatoria. Latitud geográfica del jalón principal." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.LONGITUD", idioma, "Número Decimal. Obligatoria. Longitud geográfica del jalón principal." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.ALTITUD", idioma, "Número Decimal. Obligatoria. Altitud geográfica del jalón principal." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.NOMBRE", idioma, "Texto. Max 255 caracteres. Nombre de la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.USR_DILIGENCIA_F1", idioma, "Texto. Max 255 caracteres. Cédula de quien diligencia el formulario 1." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.USR_DILIGENCIA_F2", idioma, "Texto. Max 255 caracteres. Cédula de quien diligencia el formulario 2." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.FECHAINI_APROXIMACION", idioma, "Fecha. Obligatorio. Formato aaaa-mm-dd. Fecha inicial de aproximación. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.FECHAFIN_APROXIMACION", idioma, "Fecha. Obligatorio. Formato aaaa-mm-dd. Fecha final de aproximación. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.FECHAINI_LOCALIZACION", idioma, "Fecha. Obligatorio. Formato aaaa-mm-dd. Fecha inicial de localización. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.FECHAFIN_LOCALIZACION", idioma, "Fecha. Obligatorio. Formato aaaa-mm-dd. Fecha final de localización. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.DESCRIPCION", idioma, "Texto. Max 4000 caracteres. Descripción de la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.OBSERVACIONES", idioma, "Texto. Max 4000 caracteres. Observaciones." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TRACKLOG_CAMPAMENTO", idioma, "Texto. Max 4000 caracteres. Tracklog hacia el campamento." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TRACKLOG_PARCELA", idioma, "Texto. Max 4000 caracteres. Tracklog hacia la parcela." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.DANE_DEPARTAMENTO", idioma, "Texto. Max 255 caracteres. Obligatorio. Código DANE del departamento." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.DANE_MUNICIPIO", idioma, "Texto. Max 255 caracteres. Obligatorio. Código DANE del municipio." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			// SPF1
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_DILIGENCIA", idioma, "Entero positivo. Max 10 caracteres. CC persona diligencia SPF1." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_FECHAINI", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de inicio de SPF1. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_FECHAFIN", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de finalización de SPF1. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_POSIBLE", idioma, "Número entero. 0 si no fue posible establecer SPF1, 1 si sí fue posible." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_JUSTIFICACION_NO", idioma, "Texto. Máx 1000 caracteres. Justificación de por qué no fue posible establecer SPF1." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_OBS_FUSTALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela fustal en SPF1." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_OBS_LATIZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela latizal en SPF1." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF1_OBS_BRINZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela brinzal en SPF1." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			
			// SPF2
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_DILIGENCIA", idioma, "Entero positivo. Max 10 caracteres. CC persona diligencia SPF2." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_FECHAINI", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de inicio de SPF2. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_FECHAFIN", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de finalización de SPF2. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_POSIBLE", idioma, "Número entero. 0 si no fue posible establecer SPF2, 1 si sí fue posible." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_JUSTIFICACION_NO", idioma, "Texto. Máx 1000 caracteres. Justificación de por qué no fue posible establecer SPF2." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_OBS_FUSTALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela fustal en SPF2." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_OBS_LATIZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela latizal en SPF2." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF2_OBS_BRINZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela brinzal en SPF2." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			
			// SPF3
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_DILIGENCIA", idioma, "Entero positivo. Max 10 caracteres. CC persona diligencia SPF3." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_FECHAINI", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de inicio de SPF3. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_FECHAFIN", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de finalización de SPF3. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_POSIBLE", idioma, "Número entero. 0 si no fue posible establecer SPF3, 1 si sí fue posible." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_JUSTIFICACION_NO", idioma, "Texto. Máx 1000 caracteres. Justificación de por qué no fue posible establecer SPF3." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_OBS_FUSTALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela fustal en SPF3." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_OBS_LATIZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela latizal en SPF3." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF3_OBS_BRINZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela brinzal en SPF3." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			
			// SPF4
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_DILIGENCIA", idioma, "Entero positivo. Max 10 caracteres. CC persona diligencia SPF4." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_FECHAINI", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de inicio de SPF4. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_FECHAFIN", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de finalización de SPF4. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_POSIBLE", idioma, "Número entero. 0 si no fue posible establecer SPF4, 1 si sí fue posible." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_JUSTIFICACION_NO", idioma, "Texto. Máx 1000 caracteres. Justificación de por qué no fue posible establecer SPF4." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_OBS_FUSTALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela fustal en SPF4." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_OBS_LATIZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela latizal en SPF4." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF4_OBS_BRINZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela brinzal en SPF4." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			
			// SPF5
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_DILIGENCIA", idioma, "Entero positivo. Max 10 caracteres. CC persona diligencia SPF5." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_FECHAINI", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de inicio de SPF5. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_FECHAFIN", idioma, "Fecha. Formato aaaa-mm-dd. Fecha de finalización de SPF5. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_POSIBLE", idioma, "Número entero. 0 si no fue posible establecer SPF5, 1 si sí fue posible." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_JUSTIFICACION_NO", idioma, "Texto. Máx 1000 caracteres. Justificación de por qué no fue posible establecer SPF5." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_OBS_FUSTALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela fustal en SPF5." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_OBS_LATIZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela latizal en SPF5." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.SPF5_OBS_BRINZALES", idioma, "Texto. Max 4000 caracteres. Observaciones sobre la parcela brinzal en SPF5." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.PLOT", idioma, "Entero positivo. Opcional. Código de la parcela (plot)." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.AREA", idioma, "Decimal mayor que cero. Opcional. Área de la parcela en hectáreas.  Si no se especifica se asume el área estándar mediante la fórmula: (5 * (Pi * 15.0^2)) / 10000" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.TEMPORALIDAD", idioma, "Entero positivo. Código del tipo de parcela o temporalidad." + "..");
			o = Auxiliar.cargarOpciones("SELECT TMPR_CONSECUTIVO, TMPR_NOMBRE FROM RED_TEMPORALIDAD ORDER BY TMPR_NOMBRE", "TMPR_CONSECUTIVO", "TMPR_NOMBRE", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.PUBLICA", idioma, "Entero obligatorio. 1: el acceso a la parcela es público, 0: el acceso a la parcela es confidencial." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.HAB", idioma, "Entero obligatorio. Se registró una disminución >20% de la biomasa aérea al excluir individuos no-arbóreos? 1: sí, 0: no." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.DAP", idioma, "Entero obligatorio. La parcela presenta una distribución diamétrica anómala? 1: sí, 0: no." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.GPS", idioma, "Entero obligatorio. La diferencia entre la altitud reportada e interpolada es mayor o igual a 100 m.s.n.m.? 1: sí, 0: no." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.EQ", idioma, "Entero positivo obligatorio. Identificador de la ecuación alométrica con la que se deberá calcular la biomasa." + "..");
			o = Auxiliar.cargarOpciones("SELECT EQAL.EQAL_ID AS EQAL_ID, EQAL.EQAL_LEGIBLE || ' ' || M.MTDL_NOMBRE || ' Nr:' || EQAL.EQAL_CODIGO AS INFO FROM RED_ECUACIONALOMETRICA EQAL INNER JOIN RED_METODOLOGIA M ON EQAL.EQAL_METODOLOGIA=M.MTDL_CONSECUTIVO ORDER BY INFO", "EQAL_ID", "INFO", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.BA", idioma, "Decimal positivo. Opcional. Biomasa aérea." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.BS", idioma, "Decimal positivo. Opcional. Biomasa subterránea." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.BT", idioma, "Decimal positivo. Opcional. Biomasa total." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.AUTORCUSTODIOINFO", idioma, "Entero positivo. Max 10 caracteres. Nr documento del usuario autor y custodio de la información." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.TIPOBOSQUE", idioma, "Texto. Opcional. Código del tipo de bosque (estrato)." + "..");
			o = Auxiliar.cargarOpciones("SELECT TPBS_NOMBRE, TPBS_DESCRIPCION FROM RED_TIPOBOSQUE ORDER BY TPBS_DESCRIPCION", "TPBS_NOMBRE", "TPBS_DESCRIPCION", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Parcelas.INCLUIR", idioma, "Entero obligatorio. 1: incluir parcela en cálculos, 0: no incluir parcela en cálculos." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);

			t = Auxiliar.traducir("CargueMasivo.Parcelas.ACTUALIZACION", idioma, "Fecha y hora en formato yyyy-mm-dd hh:m:ss. Obligatorio. Fecha y hora de la última actualización de la información. Ejemplo: 2004-03-14 23:18:56. Preceder el valor con una comilla sencilla." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=Plantilla_Parcelas_Diligenciar_y_Guardar_como_Archivo_de_Texto_Separado_por_Tabulaciones.xls");

			libro.write(response.getOutputStream());
			
			ok = true;
			
		} catch (Exception e) {
			t = "Ocurrió la siguiente excepción en generarPlantillaParcelas(): " + e.toString();
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + t);
		}
		
		dbREDD.desconectarse();
		return ok;
	}
	
	
	/**
	 * Metodo que retorna una tabla de parcelas encontradas
	 * 
	 * @param PRCL_NOMBRE
	 * @param PRCL_ID_IMPORTACION
	 * @param PRCL_CONS_PAIS
	 * @param departamentos_seleccionados
	 * @param municipios_seleccionados
	 * @param PRCL_FECHAFIN_APROXIMACION
	 * @return String r con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String listarRegistros(
			String PRCL_NOMBRE,
			String PRCL_CONSECUTIVO,
			String PRCL_PLOT,
			String PRCL_CONS_PAIS,
			String departamentos_seleccionados,
			String municipios_seleccionados,
			String PRCL_FECHAFIN_APROXIMACION, 
			HttpSession session
			)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "listarRegistros";
		Codec ORACLE_CODEC = new OracleCodec();

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String r = "";
		
		long n = 0;
		
		String w_nombre = "";
		String w_codigo = "";
		String w_plot = "";
		String w_pais = "";
		String w_departamentos = "";
		String w_municipios = "";
		String w_fecha = "";

		PRCL_NOMBRE = Auxiliar.limpiarTexto(PRCL_NOMBRE);
		PRCL_NOMBRE = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_NOMBRE, ""));
		if (Auxiliar.tieneAlgo(PRCL_NOMBRE)) {
			w_nombre = " AND LOWER(PRCL_NOMBRE) LIKE '%"+PRCL_NOMBRE.toLowerCase()+"%' ";
		}

		PRCL_CONSECUTIVO = Auxiliar.limpiarTexto(PRCL_CONSECUTIVO);
		PRCL_CONSECUTIVO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_CONSECUTIVO, ""));
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			w_codigo = " AND PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
		}
		
		PRCL_PLOT = Auxiliar.limpiarTexto(PRCL_PLOT);
		PRCL_PLOT = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_PLOT, ""));
		if (Auxiliar.tieneAlgo(PRCL_PLOT)) {
			w_codigo = " AND PRCL_PLOT IN ("+PRCL_PLOT+") ";
		}
		
		PRCL_FECHAFIN_APROXIMACION = Auxiliar.limpiarTexto(PRCL_FECHAFIN_APROXIMACION);
		PRCL_FECHAFIN_APROXIMACION = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_FECHAFIN_APROXIMACION, ""));
		if (Auxiliar.tieneAlgo(PRCL_FECHAFIN_APROXIMACION)) {
			w_fecha = " AND PRCL_FECHAFIN_APROXIMACION >= TO_DATE('"+PRCL_FECHAFIN_APROXIMACION+"', 'YYYY-MM-DD HH24:MI:SS') ";
		}
		
		PRCL_CONS_PAIS = Auxiliar.limpiarTexto(PRCL_CONS_PAIS);
		PRCL_CONS_PAIS = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_CONS_PAIS, ""));
		if (Auxiliar.tieneAlgo(PRCL_CONS_PAIS)) {
			w_pais = " AND PRCL_CONS_PAIS=" + PRCL_CONS_PAIS;
		}

		if (Auxiliar.tieneAlgo(departamentos_seleccionados)) {
			w_departamentos = " AND PRCL_CONSECUTIVO IN (SELECT DPPR_CONS_PARCELA FROM RED_DEPTO_PARCELA WHERE DPPR_CONS_DEPTO IN (" + departamentos_seleccionados + ")) ";
		}

		if (Auxiliar.tieneAlgo(municipios_seleccionados)) {
			w_municipios = " AND PRCL_CONSECUTIVO IN (SELECT MNPR_PARCELA FROM RED_MUNICIPIO_PARCELA WHERE MNPR_MUNICIPIO IN (" + municipios_seleccionados + ")) ";
		}

		String sql = "SELECT ";
		sql += "PRCL_CONSECUTIVO,";
		sql += "PRCL_PLOT,";
		sql += "PRCL_NOMBRE,";
		sql += "PRCL_ID_UPM,";
		sql += "TO_CHAR (PRCL_FECHAFIN_APROXIMACION, 'YYYY-MON-DD') AS PRCL_FECHAFIN_APROXIMACION";
		sql += " FROM RED_PARCELA";
		sql += " WHERE 1=1 ";
		sql += w_nombre;
		sql += w_codigo;
		sql += w_plot;
		sql += w_pais;
		sql += w_departamentos;
		sql += w_municipios;
		sql += w_fecha;
		sql += " ORDER BY PRCL_CONSECUTIVO ";

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				n = 0; 
				
				String tabla = "";
				
				String db_PRCL_CONSECUTIVO = "";
				String db_PRCL_PLOT = "";
				String db_PRCL_NOMBRE = "";
				String db_PRCL_ID_UPM = "";
				String db_PRCL_FECHAFIN_APROXIMACION = "";
				String db_departamento = "";
				String db_municipio = "";
				
				String opciones = "";
				
				tabla = "<h1>" + Auxiliar.traducir("General.Resultados_de_la_busqueda", idioma, "Resultados de la Búsqueda") + "</h1>";
				
				tabla += "<div id='contenedor-resultados' class='tabla_resultados'>";
				
				String t_PRCL_CONSECUTIVO = "";
				String t_PRCL_PLOT = "";
				String t_PRCL_NOMBRE = "";
				String t_PRCL_ID_UPM = "";
				String t_PRCL_FECHAFIN_APROXIMACION = "";
				String t_departamento = "";
				String t_municipio = "";
				String t_Opciones = "";
				
				t_PRCL_CONSECUTIVO = Auxiliar.traducir("PRCL_CONSECUTIVO", idioma, "Consecutivo" + "..");
				t_PRCL_PLOT = Auxiliar.traducir("PRCL_PLOT", idioma, "Plot" + "..");
				t_PRCL_NOMBRE = Auxiliar.traducir("PRCL_NOMBRE", idioma, "Nombre" + "..");
				t_PRCL_ID_UPM = Auxiliar.traducir("PRCL_ID_UPM", idioma, "UPM" + "..");
				t_PRCL_FECHAFIN_APROXIMACION = Auxiliar.traducir("PRCL_FECHAFIN_APROXIMACION", idioma, "Fecha Inicial de Aproximación" + "..");
				t_departamento = Auxiliar.traducir("General.Departamento", idioma, "Departamento" + "..");
				t_municipio = Auxiliar.traducir("General.Municipio", idioma, "Municipio" + "..");
				t_Opciones = Auxiliar.traducir("General.Opciones", idioma, "Opciones" + "..");
				
				while (rset.next())
				{
					n++;
					
					opciones = "";
					
					db_PRCL_CONSECUTIVO = rset.getString("PRCL_CONSECUTIVO");
					db_PRCL_PLOT = rset.getString("PRCL_PLOT");
					db_PRCL_NOMBRE = rset.getString("PRCL_NOMBRE");
					db_PRCL_ID_UPM = rset.getString("PRCL_ID_UPM");
					db_PRCL_FECHAFIN_APROXIMACION = rset.getString("PRCL_FECHAFIN_APROXIMACION");
					db_departamento = dbREDD.obtenerDato("SELECT NOMBRE FROM RED_DEPTOS_SHAPE WHERE CODIGO=(SELECT PRCL_DEPARTAMENTO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO+")", "");
					db_municipio = dbREDD.obtenerDato("SELECT NOMBRE FROM RED_MUNICIPIOS_SHAPE WHERE CODIGO=(SELECT PRCL_MUNICIPIO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO+")", "");

					if (Auxiliar.tieneAlgo(usuario)) {
						t = Auxiliar.traducir("General.Ver_Detalle", idioma, "Ver Detalle" + "..");
						opciones += "<div class='opcionmenu'><a class=boton href='Parcela?accion=detalle_parcela&PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO+"' target='_blank'>" + t + "</a></div>";
	
						t = Auxiliar.traducir("General.Individuos", idioma, "Individuos" + "..");
						opciones += "<div class='opcionmenu'><a class=boton href='Individuo?accion=buscar&PRCL_CONSECUTIVO="+db_PRCL_CONSECUTIVO+"' target='_blank'>" + t + "</a></div>";
					}
					
					tabla += "<span>";
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_PRCL_CONSECUTIVO+":"+db_PRCL_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_PRCL_PLOT+":"+db_PRCL_PLOT+"</div>";
					tabla += "<div class='dato_resultado'>"+t_PRCL_NOMBRE+":"+db_PRCL_NOMBRE+"</div>";
					tabla += "<div class='dato_resultado'>"+t_PRCL_ID_UPM+":"+db_PRCL_ID_UPM+"</div>";
					tabla += "<div class='dato_resultado'>"+t_PRCL_FECHAFIN_APROXIMACION+":"+db_PRCL_FECHAFIN_APROXIMACION+"</div>";
					tabla += "<div class='dato_resultado'>"+t_departamento+":"+db_departamento+"</div>";
					tabla += "<div class='dato_resultado'>"+t_municipio+":"+db_municipio+"</div>";
					tabla += "<div class='botones_resultado'>"+opciones+"</div>";
					tabla += "</div>";
					tabla += "</span>";
					
				}
				
				if (n == 0) {
					tabla += Auxiliar.mensaje("nota", "No se encontraron registros.", usuario, metodo);
				}
				
				tabla += "</div>";
				
				rset.close();
				
				r = tabla + "==!!==" + String.valueOf(n);
			}
			else
			{
				r += "No se encontraron resultados.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError + "==!!==0";
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
	 * Guarda una parcela
	 * 
	 * @param PRCL_CONSECUTIVO
	 * @param PRCL_ID_IMPORTACION
	 * @param PRCL_ID_UPM
	 * @param PRCL_CONS_PAIS
	 * @param PRCL_VEREDA
	 * @param PRCL_CORREGIMIENTO
	 * @param PRCL_INSPECCION_POLICIA
	 * @param PRCL_CASERIO
	 * @param PRCL_RANCHERIA
	 * @param PRCL_FECHA_CAPTURA
	 * @param PRCL_MEDIOACCESO_POBLADO
	 * @param PRCL_DISTANCIA_POBLADO
	 * @param PRCL_TPOBLADO_H
	 * @param PRCL_TPOBLADO_M
	 * @param PRCL_MEDIOACCESO_CAMPAMENTO
	 * @param PRCL_DISTANCIA_CAMPAMENTO
	 * @param PRCL_TCAMPAMENTO_H
	 * @param PRCL_TCAMPAMENTO_M
	 * @param PRCL_MEDIOACCESO_JALON
	 * @param PRCL_DISTANCIA_JALON
	 * @param PRCL_TJALON_H
	 * @param PRCL_TJALON_M
	 * @param PRCL_COORDENADAS
	 * @param PRCL_DISTANCIA_CAMPAMENTOS
	 * @param PRCL_LATITUD
	 * @param PRCL_LONGITUD
	 * @param PRCL_ALTITUD
	 * @param PRCL_NOMBRE
	 * @param PRCL_USR_DILIGENCIA_F1
	 * @param PRCL_USR_DILIGENCIA_F2
	 * @param PRCL_FECHAINI_APROXIMACION
	 * @param PRCL_FECHAFIN_APROXIMACION
	 * @param PRCL_FECHAINI_LOCALIZACION
	 * @param PRCL_FECHAFIN_LOCALIZACION
	 * @param PRCL_DESCRIPCION
	 * @param PRCL_OBSERVACIONES
	 * @param PRCL_TRACKLOG_CAMPAMENTO
	 * @param PRCL_TRACKLOG_PARCELA
	 * @param PRCL_DEPARTAMENTO
	 * @param PRCL_MUNICIPIO
	 * @param PRCL_SPF1_DILIGENCIA
	 * @param PRCL_SPF1_FECHAINI
	 * @param PRCL_SPF1_FECHAFIN
	 * @param PRCL_SPF1_POSIBLE
	 * @param PRCL_SPF1_JUSTIFICACION_NO
	 * @param PRCL_SPF1_OBS_FUSTALES
	 * @param PRCL_SPF1_OBS_LATIZALES
	 * @param PRCL_SPF1_OBS_BRINZALES
	 * @param PRCL_SPF2_DILIGENCIA
	 * @param PRCL_SPF2_FECHAINI
	 * @param PRCL_SPF2_FECHAFIN
	 * @param PRCL_SPF2_POSIBLE
	 * @param PRCL_SPF2_JUSTIFICACION_NO
	 * @param PRCL_SPF2_OBS_FUSTALES
	 * @param PRCL_SPF2_OBS_LATIZALES
	 * @param PRCL_SPF2_OBS_BRINZALES
	 * @param PRCL_SPF3_DILIGENCIA
	 * @param PRCL_SPF3_FECHAINI
	 * @param PRCL_SPF3_FECHAFIN
	 * @param PRCL_SPF3_POSIBLE
	 * @param PRCL_SPF3_JUSTIFICACION_NO
	 * @param PRCL_SPF3_OBS_FUSTALES
	 * @param PRCL_SPF3_OBS_LATIZALES
	 * @param PRCL_SPF3_OBS_BRINZALES
	 * @param PRCL_SPF4_DILIGENCIA
	 * @param PRCL_SPF4_FECHAINI
	 * @param PRCL_SPF4_FECHAFIN
	 * @param PRCL_SPF4_POSIBLE
	 * @param PRCL_SPF4_JUSTIFICACION_NO
	 * @param PRCL_SPF4_OBS_FUSTALES
	 * @param PRCL_SPF4_OBS_LATIZALES
	 * @param PRCL_SPF4_OBS_BRINZALES
	 * @param PRCL_SPF5_DILIGENCIA
	 * @param PRCL_SPF5_FECHAINI
	 * @param PRCL_SPF5_FECHAFIN
	 * @param PRCL_SPF5_POSIBLE
	 * @param PRCL_SPF5_JUSTIFICACION_NO
	 * @param PRCL_SPF5_OBS_FUSTALES
	 * @param PRCL_SPF5_OBS_LATIZALES
	 * @param PRCL_SPF5_OBS_BRINZALES
	 * @param PRCL_ARCH_CONSECUTIVO
	 * @param PRCL_PLOT
	 * @param PRCL_AREA
	 * @param PRCL_INCLUIR
	 * @param PRCL_TEMPORALIDAD
	 * @param PRCL_PUBLICA
	 * @param PRCL_HAB
	 * @param PRCL_DAP
	 * @param PRCL_GPS
	 * @param PRCL_EQ
	 * @param PRCL_BA
	 * @param PRCL_BS
	 * @param PRCL_BT
	 * @param PRCL_AUTORCUSTODIOINFO
	 * @param PRCL_TIPOBOSQUE
	 * @param PRCL_ACTUALIZACION
	 * @param request
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * @param sid
	 * @param importacion
	 * @return String con el mensaje de resultado
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String guardar(
			String PRCL_CONSECUTIVO,
			String PRCL_ID_IMPORTACION,
			String PRCL_ID_UPM,
			String PRCL_CONS_PAIS,
			String PRCL_VEREDA,
			String PRCL_CORREGIMIENTO,
			String PRCL_INSPECCION_POLICIA,
			String PRCL_CASERIO,
			String PRCL_RANCHERIA,
			String PRCL_FECHA_CAPTURA,
			String PRCL_MEDIOACCESO_POBLADO,
			String PRCL_DISTANCIA_POBLADO,
			String PRCL_TPOBLADO_H,
			String PRCL_TPOBLADO_M,
			String PRCL_MEDIOACCESO_CAMPAMENTO,
			String PRCL_DISTANCIA_CAMPAMENTO,
			String PRCL_TCAMPAMENTO_H,
			String PRCL_TCAMPAMENTO_M,
			String PRCL_MEDIOACCESO_JALON,
			String PRCL_DISTANCIA_JALON,
			String PRCL_TJALON_H,
			String PRCL_TJALON_M,
			String PRCL_COORDENADAS,
			String PRCL_DISTANCIA_CAMPAMENTOS,
			String PRCL_LATITUD,
			String PRCL_LONGITUD,
			String PRCL_ALTITUD,
			String PRCL_NOMBRE,
			String PRCL_USR_DILIGENCIA_F1,
			String PRCL_USR_DILIGENCIA_F2,
			String PRCL_FECHAINI_APROXIMACION,
			String PRCL_FECHAFIN_APROXIMACION,
			String PRCL_FECHAINI_LOCALIZACION,
			String PRCL_FECHAFIN_LOCALIZACION,
			String PRCL_DESCRIPCION,
			String PRCL_OBSERVACIONES,
			String PRCL_TRACKLOG_CAMPAMENTO,
			String PRCL_TRACKLOG_PARCELA,
			String PRCL_DEPARTAMENTO,
			String PRCL_MUNICIPIO,			
			String PRCL_SPF1_DILIGENCIA,
			String PRCL_SPF1_FECHAINI,
			String PRCL_SPF1_FECHAFIN,
			String PRCL_SPF1_POSIBLE,
			String PRCL_SPF1_JUSTIFICACION_NO,
			String PRCL_SPF1_OBS_FUSTALES,
			String PRCL_SPF1_OBS_LATIZALES,
			String PRCL_SPF1_OBS_BRINZALES,
			String PRCL_SPF2_DILIGENCIA,
			String PRCL_SPF2_FECHAINI,
			String PRCL_SPF2_FECHAFIN,
			String PRCL_SPF2_POSIBLE,
			String PRCL_SPF2_JUSTIFICACION_NO,
			String PRCL_SPF2_OBS_FUSTALES,
			String PRCL_SPF2_OBS_LATIZALES,
			String PRCL_SPF2_OBS_BRINZALES,
			String PRCL_SPF3_DILIGENCIA,
			String PRCL_SPF3_FECHAINI,
			String PRCL_SPF3_FECHAFIN,
			String PRCL_SPF3_POSIBLE,
			String PRCL_SPF3_JUSTIFICACION_NO,
			String PRCL_SPF3_OBS_FUSTALES,
			String PRCL_SPF3_OBS_LATIZALES,
			String PRCL_SPF3_OBS_BRINZALES,
			String PRCL_SPF4_DILIGENCIA,
			String PRCL_SPF4_FECHAINI,
			String PRCL_SPF4_FECHAFIN,
			String PRCL_SPF4_POSIBLE,
			String PRCL_SPF4_JUSTIFICACION_NO,
			String PRCL_SPF4_OBS_FUSTALES,
			String PRCL_SPF4_OBS_LATIZALES,
			String PRCL_SPF4_OBS_BRINZALES,
			String PRCL_SPF5_DILIGENCIA,
			String PRCL_SPF5_FECHAINI,
			String PRCL_SPF5_FECHAFIN,
			String PRCL_SPF5_POSIBLE,
			String PRCL_SPF5_JUSTIFICACION_NO,
			String PRCL_SPF5_OBS_FUSTALES,
			String PRCL_SPF5_OBS_LATIZALES,
			String PRCL_SPF5_OBS_BRINZALES,
			String PRCL_ARCH_CONSECUTIVO,
			String PRCL_PLOT,
			String PRCL_AREA,
			String PRCL_INCLUIR,
			String PRCL_TEMPORALIDAD,
			String PRCL_PUBLICA,
			String PRCL_HAB,
			String PRCL_DAP,
			String PRCL_GPS,
			String PRCL_EQ,
			String PRCL_BA,
			String PRCL_BS,
			String PRCL_BT,
			String PRCL_AUTORCUSTODIOINFO,
			String PRCL_TIPOBOSQUE,
			String PRCL_ACTUALIZACION,
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

	    if (!Auxiliar.tieneAlgo(this.RED_username)) this.RED_username = Auxiliar.nz(username, "");
	    if (!Auxiliar.tieneAlgo(this.RED_password)) this.RED_password = Auxiliar.nz(password, "");
	    if (!Auxiliar.tieneAlgo(this.RED_host)) this.RED_host = Auxiliar.nz(host, "");
	    if (!Auxiliar.tieneAlgo(this.RED_port)) this.RED_port = Auxiliar.nz(port, "");
		if (!Auxiliar.tieneAlgo(this.RED_sid)) this.RED_sid = Auxiliar.nz(sid, "");

		BD dbREDD = new BD();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");

		String resultado = "";
		String novedades = "";
		String update_esadmin = "";
		
		String observaciones = "";
		String aviso = "";
		boolean parametros_insert_ok = true;
		boolean parametros_update_ok = true;
		boolean parametros_import_ok = true;
		
		boolean ok_guardar = false;
		boolean update = false;
		
		boolean privilegio = false;
		
		String id_creador = "";
		
		importacion = false;

		String conteo = "";
		String sql_tmp = "";
		
		String pe = "";
		
		pe = Auxiliar.traducir("General.Por_favor_especifique", idioma, "Por favor especifique" + "..");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		

		// VERIFICAR PARAMETROS
		
		// VALIDAR PRCL_ACTUALIZACION
		String fechahora = "SYSDATE";
		String tz = "UTC";
		if (Auxiliar.tieneAlgo(PRCL_ACTUALIZACION)) {
			String [] a_actualizacion = PRCL_ACTUALIZACION.split("@");
			fechahora = a_actualizacion[0].trim();
			if (a_actualizacion.length > 1) {
				tz = a_actualizacion[1];
			}
			
			if (!Auxiliar.fechaHoraEsValida(fechahora) && !fechahora.equals("SYSDATE")) {
				t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_ACTUALIZACION", idioma, "Fecha PRCL_ACTUALIZACION debe ser válida y estar en formato yyyy-mm-dd hh:m:ss@zona Ejemplo: 2013-10-13 21:29:44@-05:00.  Si importa desde excel, se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_ACTUALIZACION, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
			}
			else {
				if (!fechahora.equals("SYSDATE")) {
					if (Auxiliar.tieneAlgo(tz)) {
						PRCL_ACTUALIZACION = "CAST(FROM_TZ(TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss'), '"+tz+"') AT TIME ZONE ('UTC') AS TIMESTAMP)";
					}
					else {
						PRCL_ACTUALIZACION = "TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss')";
					}
				}
				else {
					PRCL_ACTUALIZACION = fechahora;
				}
			}
		}
		
		if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			if (Auxiliar.tieneAlgo(PRCL_PLOT)) {
				PRCL_CONSECUTIVO = dbREDD.obtenerDato("SELECT PRCL_CONSECUTIVO FROM RED_PARCELA WHERE PRCL_PLOT="+PRCL_PLOT, "");
			}
		}	
		
		// SI YA FUE ACTUALIZADA CON LA MISMA FECHA ENTONCES NO ACTUALIZAR
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			conteo = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+" AND PRCL_ACTUALIZACION>=" + Auxiliar.nzVacio(PRCL_ACTUALIZACION, "SYSDATE"), "0");
			if (!conteo.equals("0")) {
				dbREDD.desconectarse();
				return PRCL_CONSECUTIVO+"-=-" + Auxiliar.mensaje("confirmacion", "La parcela "+PRCL_CONSECUTIVO+" ya había sido actualizada en o después de:" + fechahora + "@" + tz, usuario, metodo);														
			}
		}
		

		// VALIDAR PRCL_NOMBRE
		PRCL_NOMBRE = Auxiliar.limpiarTexto(PRCL_NOMBRE);
		PRCL_NOMBRE = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_NOMBRE, ""));
		if (!Auxiliar.tieneAlgo(PRCL_NOMBRE) || PRCL_NOMBRE.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_NOMBRE", idioma, "Por favor especifique el nombre de la parcela.  Este no debe exceder los 255 caracteres." + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_NOMBRE, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_basicos', true); f.PRCL_NOMBRE.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_ID_UPM
		if (!importacion) {
			if (!Auxiliar.esEntero(PRCL_ID_UPM)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_ID_UPM", idioma, "El código de la unidad primaria de medición (UPM) debe ser un número entero:" + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_ID_UPM, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_basicos', true); f.PRCL_ID_UPM.focus(); }\"");
				observaciones += aviso;
			}
		}
		
		
		// VERIFICAR PAIS
		
		if (!Auxiliar.tieneAlgo(PRCL_CONS_PAIS)) {
			PRCL_CONS_PAIS = "57";
		}
		conteo = "0";
		sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_PAIS_SHAPE WHERE PAIS=" + PRCL_CONS_PAIS;
		try {
			conteo = dbREDD.obtenerDato(sql_tmp, "0");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_CONS_PAIS", idioma, "PRCL_CONS_PAIS no encontrado" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_CONS_PAIS, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_geograficos', true); f.PRCL_CONS_PAIS.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// VALIDAR PRCL_VEREDA
		PRCL_VEREDA = Auxiliar.limpiarTexto(PRCL_VEREDA);
		PRCL_VEREDA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_VEREDA, ""));
		if (PRCL_VEREDA.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_VEREDA", idioma, "PRCL_VEREDA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_VEREDA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_CORREGIMIENTO
		PRCL_CORREGIMIENTO = Auxiliar.limpiarTexto(PRCL_CORREGIMIENTO);
		PRCL_CORREGIMIENTO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_CORREGIMIENTO, ""));
		if (PRCL_CORREGIMIENTO.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_CORREGIMIENTO", idioma, "PRCL_CORREGIMIENTO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_CORREGIMIENTO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_INSPECCION_POLICIA
		PRCL_INSPECCION_POLICIA = Auxiliar.limpiarTexto(PRCL_INSPECCION_POLICIA);
		PRCL_INSPECCION_POLICIA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_INSPECCION_POLICIA, ""));
		if (PRCL_INSPECCION_POLICIA.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_INSPECCION_POLICIA", idioma, "PRCL_INSPECCION_POLICIA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_INSPECCION_POLICIA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_CASERIO
		PRCL_CASERIO = Auxiliar.limpiarTexto(PRCL_CASERIO);
		PRCL_CASERIO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_CASERIO, ""));
		if (PRCL_CASERIO.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_CASERIO", idioma, "PRCL_CASERIO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_CASERIO, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_RANCHERIA
		PRCL_RANCHERIA = Auxiliar.limpiarTexto(PRCL_RANCHERIA);
		PRCL_RANCHERIA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_RANCHERIA, ""));
		if (PRCL_RANCHERIA.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_RANCHERIA", idioma, "PRCL_RANCHERIA no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_RANCHERIA, usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		if (Auxiliar.tieneAlgo(PRCL_FECHA_CAPTURA)) {
			// VALIDAR PRCL_FECHA_CAPTURA
			if (!Auxiliar.fechaEsValida(PRCL_FECHA_CAPTURA)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHA_CAPTURA", idioma, "PRCL_FECHA_CAPTURA debe estar en formato aaaa-mm-dd y debe ser una fecha válida." + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_FECHA_CAPTURA, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
			}
		}
		
		// VERIFICAR PRCL_MEDIOACCESO_POBLADO
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_MEDIOACCESO_POBLADO)) {
				conteo = "0";
				sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_MEDIOACCESO WHERE MACC_ID=" + PRCL_MEDIOACCESO_POBLADO;
				try {
					conteo = dbREDD.obtenerDato(sql_tmp, "0");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_MEDIOACCESO_POBLADO", idioma, "PRCL_MEDIOACCESO_POBLADO no encontrado" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_MEDIOACCESO_POBLADO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_poblado', true); f.PRCL_MEDIOACCESO_POBLADO.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_DISTANCIA_POBLADO
		PRCL_DISTANCIA_POBLADO = PRCL_DISTANCIA_POBLADO.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_DISTANCIA_POBLADO)) {
				if (!Auxiliar.esNumeroPositivo(PRCL_DISTANCIA_POBLADO)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_DISTANCIA_POBLADO", idioma, "PRCL_DISTANCIA_POBLADO debe ser numérica y positiva" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_RANCHERIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_poblado', true); f.PRCL_DISTANCIA_POBLADO.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_TPOBLADO_H
		PRCL_TPOBLADO_H = PRCL_TPOBLADO_H.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_TPOBLADO_H)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_TPOBLADO_H)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TPOBLADO_H", idioma, "PRCL_TPOBLADO_H debe ser un entero entre 0 y 24" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_TPOBLADO_H, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_poblado', true); f.PRCL_TPOBLADO_H.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_TPOBLADO_M
		PRCL_TPOBLADO_M = PRCL_TPOBLADO_M.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_TPOBLADO_M)) {
				if (!Auxiliar.esMinuto(PRCL_TPOBLADO_M)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TPOBLADO_M", idioma, "PRCL_TPOBLADO_M debe ser un entero entre 0 y 59" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_TPOBLADO_M, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_poblado', true); f.PRCL_TPOBLADO_M.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VERIFICAR PRCL_MEDIOACCESO_CAMPAMENTO
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_MEDIOACCESO_CAMPAMENTO)) {
				conteo = "0";
				sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_MEDIOACCESO WHERE MACC_ID="
						+ PRCL_MEDIOACCESO_CAMPAMENTO;
				try {
					conteo = dbREDD.obtenerDato(sql_tmp, "0");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_MEDIOACCESO_CAMPAMENTO", idioma, "PRCL_MEDIOACCESO_CAMPAMENTO no encontrado" + "..");
					aviso = Auxiliar.mensajeJS("advertencia",
							t + ":" + PRCL_CONS_PAIS, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_MEDIOACCESO_CAMPAMENTO.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_DISTANCIA_CAMPAMENTO
		PRCL_DISTANCIA_CAMPAMENTO = PRCL_DISTANCIA_CAMPAMENTO.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_DISTANCIA_CAMPAMENTO)) {
				if (!Auxiliar.esNumeroPositivo(PRCL_DISTANCIA_CAMPAMENTO)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_DISTANCIA_CAMPAMENTO", idioma, "PRCL_DISTANCIA_CAMPAMENTO debe ser numérica y positiva" + "..");
					aviso = Auxiliar.mensajeJS("advertencia",
							t + ":" + PRCL_RANCHERIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_DISTANCIA_CAMPAMENTO.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_TCAMPAMENTO_H
		PRCL_TCAMPAMENTO_H = PRCL_TCAMPAMENTO_H.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_TCAMPAMENTO_H)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_TCAMPAMENTO_H)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TCAMPAMENTO_H", idioma, "PRCL_TCAMPAMENTO_H debe ser un entero entre 0 y 24" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_TCAMPAMENTO_H, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_TCAMPAMENTO_H.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_TCAMPAMENTO_M
		PRCL_TCAMPAMENTO_M = PRCL_TCAMPAMENTO_M.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_TCAMPAMENTO_M)) {
				if (!Auxiliar.esMinuto(PRCL_TCAMPAMENTO_M)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TCAMPAMENTO_M", idioma, "PRCL_TCAMPAMENTO_M debe ser un entero entre 0 y 59" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_TCAMPAMENTO_M, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_TCAMPAMENTO_M.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VERIFICAR PRCL_MEDIOACCESO_JALON
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_MEDIOACCESO_JALON)) {
				conteo = "0";
				sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_MEDIOACCESO WHERE MACC_ID="
						+ PRCL_MEDIOACCESO_JALON;
				try {
					conteo = dbREDD.obtenerDato(sql_tmp, "0");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_MEDIOACCESO_JALON", idioma, "PRCL_MEDIOACCESO_JALON no encontrado" + "..");
					aviso = Auxiliar.mensajeJS("advertencia",
							t + ":" + PRCL_CONS_PAIS, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_MEDIOACCESO_JALON.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_DISTANCIA_JALON
		PRCL_DISTANCIA_JALON = PRCL_DISTANCIA_JALON.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_DISTANCIA_JALON)) {
				if (!Auxiliar.esNumeroPositivo(PRCL_DISTANCIA_JALON)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_DISTANCIA_JALON", idioma, "PRCL_DISTANCIA_JALON debe ser numérica y positiva" + "..");
					aviso = Auxiliar.mensajeJS("advertencia",
							t + ":" + PRCL_RANCHERIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_DISTANCIA_JALON.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_TJALON_H
		PRCL_TJALON_H = PRCL_TJALON_H.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_TJALON_H)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_TJALON_H)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TJALON_H", idioma, "PRCL_TJALON_H debe ser un entero entre 0 y 24" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_TJALON_H,
							usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_TJALON_H.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_TJALON_M
		PRCL_TJALON_M = PRCL_TJALON_M.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_TJALON_M)) {
				if (!Auxiliar.esMinuto(PRCL_TJALON_M)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TJALON_M", idioma, "PRCL_TJALON_M debe ser un entero entre 0 y 59" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_TJALON_M,
							usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_TJALON_M.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_COORDENADAS
		/*
		PRCL_COORDENADAS = Auxiliar.limpiarTexto(PRCL_COORDENADAS);
		PRCL_COORDENADAS = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_COORDENADAS, ""));
		if (PRCL_COORDENADAS.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_COORDENADAS", idioma, "PRCL_COORDENADAS no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_COORDENADAS,
					usuario, metodo);
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		*/

		// VALIDAR PRCL_DISTANCIA_CAMPAMENTOS
		PRCL_DISTANCIA_CAMPAMENTOS = PRCL_DISTANCIA_CAMPAMENTOS.replace(",", ".");
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_DISTANCIA_CAMPAMENTOS)) {
				if (!Auxiliar.esNumeroPositivo(PRCL_DISTANCIA_CAMPAMENTOS)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_DISTANCIA_CAMPAMENTOS", idioma, "PRCL_DISTANCIA_CAMPAMENTOS debe ser numérica y positiva" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_DISTANCIA_CAMPAMENTOS, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_DISTANCIA_CAMPAMENTOS.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_LATITUD
		PRCL_LATITUD = PRCL_LATITUD.replace(",", ".");
		if (!importacion) {
			//if (Auxiliar.tieneAlgo(PRCL_LATITUD)) {
				if (!Auxiliar.esLatitud(PRCL_LATITUD)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_LATITUD", idioma, "PRCL_LATITUD debe ser un decimal entre -90 y 90" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_LATITUD,
							usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_geograficos', true); f.PRCL_LATITUD.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			//}
		}

		// VALIDAR PRCL_LONGITUD
		PRCL_LONGITUD = PRCL_LONGITUD.replace(",", ".");
		if (!importacion) {
			//if (Auxiliar.tieneAlgo(PRCL_LONGITUD)) {
				if (!Auxiliar.esLatitud(PRCL_LONGITUD)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_LONGITUD", idioma, "PRCL_LONGITUD debe ser un decimal entre -180 y 180" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_LONGITUD,
							usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_geograficos', true); f.PRCL_LONGITUD.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			//}
		}

		// VALIDAR PRCL_ALTITUD
		PRCL_ALTITUD = PRCL_ALTITUD.replace(",", ".");
		if (!importacion) {
			//if (Auxiliar.tieneAlgo(PRCL_LONGITUD)) {
			if (!Auxiliar.esAltitud(PRCL_ALTITUD)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_ALTITUD", idioma, "PRCL_ALTITUD debe ser un decimal entre 0m y 6000m" + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_LONGITUD,
						usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_geograficos', true); f.PRCL_ALTITUD.focus(); }\"");
				observaciones += aviso;
				parametros_insert_ok = false;
			}
			//}
		}
		
		// VALIDAR PRCL_USR_DILIGENCIA_F1
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_USR_DILIGENCIA_F1)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_USR_DILIGENCIA_F1)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_USR_DILIGENCIA_F1", idioma, "PRCL_USR_DILIGENCIA_F1 debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensaje("advertencia", t + ":"
							+ PRCL_USR_DILIGENCIA_F1, usuario, metodo);
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_USR_DILIGENCIA_F2
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_USR_DILIGENCIA_F2)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_USR_DILIGENCIA_F2)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_USR_DILIGENCIA_F2", idioma, "PRCL_USR_DILIGENCIA_F2 debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensaje("advertencia", t + ":"
							+ PRCL_USR_DILIGENCIA_F2, usuario, metodo);
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR FECHAS DE ESTABLECIMIENTO / APROXIMACION
		boolean fechas_aproximacion_ok = true;
		boolean fechaini_aproximacion_ok = true;
		boolean fechafin_aproximacion_ok = true;

		// VALIDAR PRCL_FECHAINI_APROXIMACION
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_FECHAINI_APROXIMACION)) {
				if (!Auxiliar.fechaEsValida(PRCL_FECHAINI_APROXIMACION)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAINI_APROXIMACION", idioma, "Fecha PRCL_FECHAINI_APROXIMACION nó válida" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_FECHAINI_APROXIMACION, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_FECHAINI_APROXIMACION.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_aproximacion_ok = false;
				}
			}
		}

		// VALIDAR PRCL_FECHAFIN_APROXIMACION
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_FECHAFIN_APROXIMACION)) {
				if (!Auxiliar.fechaEsValida(PRCL_FECHAFIN_APROXIMACION)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAFIN_APROXIMACION", idioma, "Fecha PRCL_FECHAFIN_APROXIMACION nó válida" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_FECHAFIN_APROXIMACION, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_FECHAFIN_APROXIMACION.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_aproximacion_ok = false;
				}
			}
		}
		
		fechas_aproximacion_ok = (fechaini_aproximacion_ok && fechafin_aproximacion_ok);
		if (fechas_aproximacion_ok) {
			if (Auxiliar.tieneAlgo(PRCL_FECHAINI_APROXIMACION) && Auxiliar.tieneAlgo(PRCL_FECHAFIN_APROXIMACION)) {
				try {
					Date fechaini_aproximacion = formatter.parse(PRCL_FECHAINI_APROXIMACION);
					Date fechafin_aproximacion = formatter.parse(PRCL_FECHAFIN_APROXIMACION);
					if (fechaini_aproximacion.after(fechafin_aproximacion)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_APROXIMACION", idioma, "Fecha inicial de aproximación no puede ser posterior a la fecha final de aproximación" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_FECHAINI_APROXIMACION + " vs " + PRCL_FECHAFIN_APROXIMACION, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_campamento', true); f.PRCL_FECHAFIN_APROXIMACION.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// VALIDAR FECHAS DE LOCALIZACION
		boolean fechas_localizacion_ok = true;
		boolean fechaini_localizacion_ok = true;
		boolean fechafin_localizacion_ok = true;

		// VALIDAR PRCL_FECHAINI_LOCALIZACION
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_FECHAINI_LOCALIZACION)) {
				if (!Auxiliar.fechaEsValida(PRCL_FECHAINI_LOCALIZACION)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAINI_LOCALIZACION", idioma, "Fecha PRCL_FECHAINI_LOCALIZACION nó válida" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_FECHAINI_LOCALIZACION, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_FECHAINI_LOCALIZACION.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_localizacion_ok = false;
				}
			}
		}

		// VALIDAR PRCL_FECHAFIN_LOCALIZACION
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_FECHAFIN_LOCALIZACION)) {
				if (!Auxiliar.fechaEsValida(PRCL_FECHAFIN_LOCALIZACION)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAFIN_LOCALIZACION", idioma, "Fecha PRCL_FECHAFIN_LOCALIZACION nó válida" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_FECHAFIN_LOCALIZACION, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_FECHAFIN_LOCALIZACION.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_localizacion_ok = false;
				}
			}
		}

		fechas_localizacion_ok = (fechaini_localizacion_ok && fechafin_localizacion_ok);
		if (fechas_localizacion_ok) {
			if (Auxiliar.tieneAlgo(PRCL_FECHAINI_LOCALIZACION) && Auxiliar.tieneAlgo(PRCL_FECHAFIN_LOCALIZACION)) {
				try {
					Date fechaini_localizacion = formatter.parse(PRCL_FECHAINI_LOCALIZACION);
					Date fechafin_localizacion = formatter.parse(PRCL_FECHAFIN_LOCALIZACION);
					if (fechaini_localizacion.after(fechafin_localizacion)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_LOCALIZACION", idioma, "Fecha inicial de localización no puede ser posterior a la fecha final de localización" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_FECHAINI_LOCALIZACION + " vs " + PRCL_FECHAFIN_LOCALIZACION, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__acceso_jalon', true); f.PRCL_FECHAINI_LOCALIZACION.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		// VALIDAR PRCL_DESCRIPCION
		PRCL_DESCRIPCION = Auxiliar.limpiarTexto(PRCL_DESCRIPCION);
		PRCL_DESCRIPCION = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_DESCRIPCION, ""));
		if (PRCL_DESCRIPCION.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_DESCRIPCION", idioma, "PRCL_DESCRIPCION no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_DESCRIPCION,
					usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_basicos', true); f.PRCL_DESCRIPCION.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		// VALIDAR PRCL_OBSERVACIONES
		PRCL_OBSERVACIONES = Auxiliar.limpiarTexto(PRCL_OBSERVACIONES);
		PRCL_OBSERVACIONES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_OBSERVACIONES, ""));
		if (PRCL_OBSERVACIONES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_OBSERVACIONES", idioma, "PRCL_OBSERVACIONES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_OBSERVACIONES,
					usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_basicos', true); f.PRCL_OBSERVACIONES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		// VALIDAR PRCL_TRACKLOG_CAMPAMENTO
		PRCL_TRACKLOG_CAMPAMENTO = Auxiliar.limpiarTexto(PRCL_TRACKLOG_CAMPAMENTO);
		PRCL_TRACKLOG_CAMPAMENTO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_TRACKLOG_CAMPAMENTO, ""));
		if (PRCL_TRACKLOG_CAMPAMENTO.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TRACKLOG_CAMPAMENTO", idioma, "PRCL_TRACKLOG_CAMPAMENTO no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_TRACKLOG_CAMPAMENTO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__tracklogs', true); f.PRCL_TRACKLOG_CAMPAMENTO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		// VALIDAR PRCL_TRACKLOG_PARCELA
		PRCL_TRACKLOG_PARCELA = Auxiliar.limpiarTexto(PRCL_TRACKLOG_PARCELA);
		PRCL_TRACKLOG_PARCELA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_TRACKLOG_PARCELA, ""));
		if (PRCL_TRACKLOG_PARCELA.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_TRACKLOG_PARCELA", idioma, "PRCL_TRACKLOG_PARCELA no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_TRACKLOG_PARCELA,
					usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__tracklogs', true); f.PRCL_TRACKLOG_PARCELA.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		
		// VERIFICAR DEPARTAMENTO 
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_DEPARTAMENTO)) {
				conteo = "0"; 
				sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_DEPTOS_SHAPE WHERE CODIGO='" + PRCL_DEPARTAMENTO +"'"; 
				
				try { 
					conteo = dbREDD.obtenerDato(sql_tmp, "0"); 
				} 
				catch (Exception ex) { 
					ex.printStackTrace();
				} 
				
				if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) { 
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_DEPARTAMENTO", idioma, "PRCL_DEPARTAMENTO no encontrado" + ".."); 
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_DEPARTAMENTO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_geograficos', true); f.PRCL_DEPARTAMENTO.focus(); }\""); 
					observaciones += aviso; 
				}				
			}
		}
		
		// VERIFICAR MUNICIPIO 
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_MUNICIPIO)) {
				conteo = "0"; 
				sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_MUNICIPIOS_SHAPE WHERE CODIGO='" + PRCL_MUNICIPIO +"'"; 
				try { 
					conteo = dbREDD.obtenerDato(sql_tmp, "0"); 
				} 
				catch (Exception ex) { 
					ex.printStackTrace();
				} 
				if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) { 
					t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_MUNICIPIO", idioma, "PRCL_MUNICIPIO no encontrado" + ".."); 
					aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_MUNICIPIO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__datos_geograficos', true); f.PRCL_MUNICIPIO.focus(); }\""); 
					observaciones += aviso; 
				}
			}
		}
		 

		 // SPF1
		
		// VALIDAR PRCL_SPF1_DILIGENCIA
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF1_DILIGENCIA)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_SPF1_DILIGENCIA)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_DILIGENCIA", idioma, "PRCL_SPF1_DILIGENCIA debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF1_DILIGENCIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_DILIGENCIA.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		boolean fechas_SPF1_ok = true;
		boolean fechaini_SPF1_ok = true;
		boolean fechafin_SPF1_ok = true;

		// VALIDAR PRCL_SPF1_FECHAINI
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF1_FECHAINI)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF1_FECHAINI)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_FECHAINI", idioma, "Fecha PRCL_SPF1_FECHAINI debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF1_FECHAINI, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_FECHAINI.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_SPF1_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF1_FECHAFIN
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF1_FECHAFIN)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF1_FECHAFIN)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.FECHAS_SPF1", idioma, "Fecha PRCL_SPF1_FECHAFIN debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF1_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_FECHAFIN.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_SPF1_ok = false;
				}
			}
		}
		
		fechas_SPF1_ok = (fechaini_SPF1_ok && fechafin_SPF1_ok);
		if (fechas_SPF1_ok) {
			if (Auxiliar.tieneAlgo(PRCL_SPF1_FECHAINI) && Auxiliar.tieneAlgo(PRCL_SPF1_FECHAFIN)) {
				try {
					Date fechaini_spf1 = formatter.parse(PRCL_SPF1_FECHAINI);
					Date fechafin_spf1 = formatter.parse(PRCL_SPF1_FECHAFIN);
					if (fechaini_spf1.after(fechafin_spf1)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_SPF1", idioma, "Fecha inicial de subparcela 1 no puede ser posterior a la fecha final" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_SPF1_FECHAINI + " vs " + PRCL_SPF1_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_FECHAINI.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		
		// VALIDAR PRCL_SPF1_POSIBLE
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF1_POSIBLE)) {
				if (!Auxiliar.nz(PRCL_SPF1_POSIBLE, "").equals("0")
						&& !Auxiliar.nz(PRCL_SPF1_POSIBLE, "").equals("1")) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_POSIBLE", idioma, "PRCL_SPF1_POSIBLE debe ser 0 o 1." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF1_POSIBLE, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_POSIBLE.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF1_JUSTIFICACION_NO
		PRCL_SPF1_JUSTIFICACION_NO = Auxiliar.limpiarTexto(PRCL_SPF1_JUSTIFICACION_NO);
		PRCL_SPF1_JUSTIFICACION_NO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF1_JUSTIFICACION_NO, ""));
		if (PRCL_SPF1_JUSTIFICACION_NO.length() > 1000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_JUSTIFICACION_NO", idioma, "PRCL_SPF1_JUSTIFICACION_NO no puede exceder los 1000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF1_JUSTIFICACION_NO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_JUSTIFICACION_NO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF1_OBS_FUSTALES
		PRCL_SPF1_OBS_FUSTALES = Auxiliar.limpiarTexto(PRCL_SPF1_OBS_FUSTALES);
		PRCL_SPF1_OBS_FUSTALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF1_OBS_FUSTALES, ""));
		if (PRCL_SPF1_OBS_FUSTALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_OBS_FUSTALES", idioma, "PRCL_SPF1_OBS_FUSTALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia",
					t + ":" + PRCL_SPF1_OBS_FUSTALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_OBS_FUSTALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF1_OBS_LATIZALES
		PRCL_SPF1_OBS_LATIZALES = Auxiliar.limpiarTexto(PRCL_SPF1_OBS_LATIZALES);
		PRCL_SPF1_OBS_LATIZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF1_OBS_LATIZALES, ""));
		if (PRCL_SPF1_OBS_LATIZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_OBS_LATIZALES", idioma, "PRCL_SPF1_OBS_LATIZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF1_OBS_LATIZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_OBS_LATIZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF1_OBS_BRINZALES
		PRCL_SPF1_OBS_BRINZALES = Auxiliar.limpiarTexto(PRCL_SPF1_OBS_BRINZALES);
		PRCL_SPF1_OBS_BRINZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF1_OBS_BRINZALES, ""));
		if (PRCL_SPF1_OBS_BRINZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF1_OBS_BRINZALES", idioma, "PRCL_SPF1_OBS_BRINZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF1_OBS_BRINZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF1', true); f.PRCL_SPF1_OBS_BRINZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// SPF2
		
		// VALIDAR PRCL_SPF2_DILIGENCIA
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF2_DILIGENCIA)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_SPF2_DILIGENCIA)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_DILIGENCIA", idioma, "PRCL_SPF2_DILIGENCIA debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF2_DILIGENCIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_DILIGENCIA.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		
		boolean fechas_SPF2_ok = true;
		boolean fechaini_SPF2_ok = true;
		boolean fechafin_SPF2_ok = true;

		// VALIDAR PRCL_SPF2_FECHAINI
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF2_FECHAINI)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF2_FECHAINI)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_FECHAINI", idioma, "Fecha PRCL_SPF2_FECHAINI debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF2_FECHAINI, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_FECHAINI.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_SPF2_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF2_FECHAFIN
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF2_FECHAFIN)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF2_FECHAFIN)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_FECHAFIN", idioma, "Fecha PRCL_SPF2_FECHAFIN debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF2_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_FECHAFIN.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_SPF2_ok = false;
				}
			}
		}
		
		fechas_SPF2_ok = (fechaini_SPF2_ok && fechafin_SPF2_ok);
		if (fechas_SPF2_ok) {
			if (Auxiliar.tieneAlgo(PRCL_SPF2_FECHAINI) && Auxiliar.tieneAlgo(PRCL_SPF2_FECHAFIN)) {
				try {
					Date fechaini_spf2 = formatter.parse(PRCL_SPF2_FECHAINI);
					Date fechafin_spf2 = formatter.parse(PRCL_SPF2_FECHAFIN);
					if (fechaini_spf2.after(fechafin_spf2)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_SPF2", idioma, "Fecha inicial de subparcela 2 no puede ser posterior a la fecha final" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_SPF2_FECHAINI + " vs " + PRCL_SPF2_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_FECHAINI.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		
		// VALIDAR PRCL_SPF2_POSIBLE
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF2_POSIBLE)) {
				if (!Auxiliar.nz(PRCL_SPF2_POSIBLE, "").equals("0")
						&& !Auxiliar.nz(PRCL_SPF2_POSIBLE, "").equals("1")) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_POSIBLE", idioma, "PRCL_SPF2_POSIBLE debe ser 0 o 1." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF2_POSIBLE, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_POSIBLE.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF2_JUSTIFICACION_NO
		PRCL_SPF2_JUSTIFICACION_NO = Auxiliar.limpiarTexto(PRCL_SPF2_JUSTIFICACION_NO);
		PRCL_SPF2_JUSTIFICACION_NO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF2_JUSTIFICACION_NO, ""));
		if (PRCL_SPF2_JUSTIFICACION_NO.length() > 1000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_JUSTIFICACION_NO", idioma, "PRCL_SPF2_JUSTIFICACION_NO no puede exceder los 1000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF2_JUSTIFICACION_NO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_JUSTIFICACION_NO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		

		// VALIDAR PRCL_SPF2_OBS_FUSTALES
		PRCL_SPF2_OBS_FUSTALES = Auxiliar.limpiarTexto(PRCL_SPF2_OBS_FUSTALES);
		PRCL_SPF2_OBS_FUSTALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF2_OBS_FUSTALES, ""));
		if (PRCL_SPF2_OBS_FUSTALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_OBS_FUSTALES", idioma, "PRCL_SPF2_OBS_FUSTALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia",
					t + ":" + PRCL_SPF2_OBS_FUSTALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_OBS_FUSTALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF2_OBS_LATIZALES
		PRCL_SPF2_OBS_LATIZALES = Auxiliar.limpiarTexto(PRCL_SPF2_OBS_LATIZALES);
		PRCL_SPF2_OBS_LATIZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF2_OBS_LATIZALES, ""));
		if (PRCL_SPF2_OBS_LATIZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_OBS_LATIZALES", idioma, "PRCL_SPF2_OBS_LATIZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF2_OBS_LATIZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_OBS_LATIZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF2_OBS_BRINZALES
		PRCL_SPF2_OBS_BRINZALES = Auxiliar.limpiarTexto(PRCL_SPF2_OBS_BRINZALES);
		PRCL_SPF2_OBS_BRINZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF2_OBS_BRINZALES, ""));
		if (PRCL_SPF2_OBS_BRINZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF2_OBS_BRINZALES", idioma, "PRCL_SPF2_OBS_BRINZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF2_OBS_BRINZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF2', true); f.PRCL_SPF2_OBS_BRINZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// SPF3
		
		// VALIDAR PRCL_SPF3_DILIGENCIA
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF3_DILIGENCIA)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_SPF3_DILIGENCIA)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_DILIGENCIA", idioma, "PRCL_SPF3_DILIGENCIA debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF3_DILIGENCIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_DILIGENCIA.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		
		boolean fechas_SPF3_ok = true;
		boolean fechaini_SPF3_ok = true;
		boolean fechafin_SPF3_ok = true;

		// VALIDAR PRCL_SPF3_FECHAINI
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF3_FECHAINI)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF3_FECHAINI)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_FECHAINI", idioma, "Fecha PRCL_SPF3_FECHAINI debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF3_FECHAINI, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_FECHAINI.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_SPF3_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF3_FECHAFIN
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF3_FECHAFIN)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF3_FECHAFIN)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_FECHAFIN", idioma, "Fecha PRCL_SPF3_FECHAFIN debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF3_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_FECHAFIN.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_SPF3_ok = false;
				}
			}
		}
		
		fechas_SPF3_ok = (fechaini_SPF3_ok && fechafin_SPF3_ok);
		if (fechas_SPF3_ok) {
			if (Auxiliar.tieneAlgo(PRCL_SPF3_FECHAINI) && Auxiliar.tieneAlgo(PRCL_SPF3_FECHAFIN)) {
				try {
					Date fechaini_spf2 = formatter.parse(PRCL_SPF3_FECHAINI);
					Date fechafin_spf2 = formatter.parse(PRCL_SPF3_FECHAFIN);
					if (fechaini_spf2.after(fechafin_spf2)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_SPF3", idioma, "Fecha inicial de subparcela 2 no puede ser posterior a la fecha final" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_SPF3_FECHAINI + " vs " + PRCL_SPF3_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_FECHAINI.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		
		// VALIDAR PRCL_SPF3_POSIBLE
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF3_POSIBLE)) {
				if (!Auxiliar.nz(PRCL_SPF3_POSIBLE, "").equals("0")
						&& !Auxiliar.nz(PRCL_SPF3_POSIBLE, "").equals("1")) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_POSIBLE", idioma, "PRCL_SPF3_POSIBLE debe ser 0 o 1." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF3_POSIBLE, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_POSIBLE.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF3_JUSTIFICACION_NO
		PRCL_SPF3_JUSTIFICACION_NO = Auxiliar.limpiarTexto(PRCL_SPF3_JUSTIFICACION_NO);
		PRCL_SPF3_JUSTIFICACION_NO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF3_JUSTIFICACION_NO, ""));
		if (PRCL_SPF3_JUSTIFICACION_NO.length() > 1000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_JUSTIFICACION_NO", idioma, "PRCL_SPF3_JUSTIFICACION_NO no puede exceder los 1000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF3_JUSTIFICACION_NO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_JUSTIFICACION_NO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// VALIDAR PRCL_SPF3_OBS_FUSTALES
		PRCL_SPF3_OBS_FUSTALES = Auxiliar.limpiarTexto(PRCL_SPF3_OBS_FUSTALES);
		PRCL_SPF3_OBS_FUSTALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF3_OBS_FUSTALES, ""));
		if (PRCL_SPF3_OBS_FUSTALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_OBS_FUSTALES", idioma, "PRCL_SPF3_OBS_FUSTALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia",
					t + ":" + PRCL_SPF3_OBS_FUSTALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_OBS_FUSTALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF3_OBS_LATIZALES
		PRCL_SPF3_OBS_LATIZALES = Auxiliar.limpiarTexto(PRCL_SPF3_OBS_LATIZALES);
		PRCL_SPF3_OBS_LATIZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF3_OBS_LATIZALES, ""));
		if (PRCL_SPF3_OBS_LATIZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_OBS_LATIZALES", idioma, "PRCL_SPF3_OBS_LATIZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF3_OBS_LATIZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_OBS_LATIZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF3_OBS_BRINZALES
		PRCL_SPF3_OBS_BRINZALES = Auxiliar.limpiarTexto(PRCL_SPF3_OBS_BRINZALES);
		PRCL_SPF3_OBS_BRINZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF3_OBS_BRINZALES, ""));
		if (PRCL_SPF3_OBS_BRINZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF3_OBS_BRINZALES", idioma, "PRCL_SPF3_OBS_BRINZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF3_OBS_BRINZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF3', true); f.PRCL_SPF3_OBS_BRINZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// SPF4
		
		// VALIDAR PRCL_SPF4_DILIGENCIA
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF4_DILIGENCIA)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_SPF4_DILIGENCIA)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_DILIGENCIA", idioma, "PRCL_SPF4_DILIGENCIA debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF4_DILIGENCIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_DILIGENCIA.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		boolean fechas_SPF4_ok = true;
		boolean fechaini_SPF4_ok = true;
		boolean fechafin_SPF4_ok = true;

		// VALIDAR PRCL_SPF4_FECHAINI
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF4_FECHAINI)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF4_FECHAINI)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_FECHAINI", idioma, "Fecha PRCL_SPF4_FECHAINI debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF4_FECHAINI, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_FECHAINI.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_SPF4_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF4_FECHAFIN
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF4_FECHAFIN)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF4_FECHAFIN)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_FECHAFIN", idioma, "Fecha PRCL_SPF4_FECHAFIN debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF4_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_FECHAFIN.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_SPF4_ok = false;
				}
			}
		}

		fechas_SPF4_ok = (fechaini_SPF4_ok && fechafin_SPF4_ok);
		if (fechas_SPF4_ok) {
			if (Auxiliar.tieneAlgo(PRCL_SPF4_FECHAINI) && Auxiliar.tieneAlgo(PRCL_SPF4_FECHAFIN)) {
				try {
					Date fechaini_spf2 = formatter.parse(PRCL_SPF4_FECHAINI);
					Date fechafin_spf2 = formatter.parse(PRCL_SPF4_FECHAFIN);
					if (fechaini_spf2.after(fechafin_spf2)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_SPF4", idioma, "Fecha inicial de subparcela 2 no puede ser posterior a la fecha final" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_SPF4_FECHAINI + " vs " + PRCL_SPF4_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_FECHAINI.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		// VALIDAR PRCL_SPF4_POSIBLE
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF4_POSIBLE)) {
				if (!Auxiliar.nz(PRCL_SPF4_POSIBLE, "").equals("0")
						&& !Auxiliar.nz(PRCL_SPF4_POSIBLE, "").equals("1")) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_POSIBLE", idioma, "PRCL_SPF4_POSIBLE debe ser 0 o 1." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF4_POSIBLE, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_POSIBLE.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}
		
		// VALIDAR PRCL_SPF4_JUSTIFICACION_NO
		PRCL_SPF4_JUSTIFICACION_NO = Auxiliar.limpiarTexto(PRCL_SPF4_JUSTIFICACION_NO);
		PRCL_SPF4_JUSTIFICACION_NO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF4_JUSTIFICACION_NO, ""));
		if (PRCL_SPF4_JUSTIFICACION_NO.length() > 1000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_JUSTIFICACION_NO", idioma, "PRCL_SPF4_JUSTIFICACION_NO no puede exceder los 1000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF4_JUSTIFICACION_NO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_JUSTIFICACION_NO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// VALIDAR PRCL_SPF4_OBS_FUSTALES
		PRCL_SPF4_OBS_FUSTALES = Auxiliar.limpiarTexto(PRCL_SPF4_OBS_FUSTALES);
		PRCL_SPF4_OBS_FUSTALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF4_OBS_FUSTALES, ""));
		if (PRCL_SPF4_OBS_FUSTALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_OBS_FUSTALES", idioma, "PRCL_SPF4_OBS_FUSTALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia",
					t + ":" + PRCL_SPF4_OBS_FUSTALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_OBS_FUSTALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF4_OBS_LATIZALES
		PRCL_SPF4_OBS_LATIZALES = Auxiliar.limpiarTexto(PRCL_SPF4_OBS_LATIZALES);
		PRCL_SPF4_OBS_LATIZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF4_OBS_LATIZALES, ""));
		if (PRCL_SPF4_OBS_LATIZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_OBS_LATIZALES", idioma, "PRCL_SPF4_OBS_LATIZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF4_OBS_LATIZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_OBS_LATIZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		// VALIDAR PRCL_SPF4_OBS_BRINZALES
		PRCL_SPF4_OBS_BRINZALES = Auxiliar.limpiarTexto(PRCL_SPF4_OBS_BRINZALES);
		PRCL_SPF4_OBS_BRINZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF4_OBS_BRINZALES, ""));
		if (PRCL_SPF4_OBS_BRINZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF4_OBS_BRINZALES", idioma, "PRCL_SPF4_OBS_BRINZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF4_OBS_BRINZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF4', true); f.PRCL_SPF4_OBS_BRINZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}
		
		
		// SPF5

		// VALIDAR PRCL_SPF5_DILIGENCIA
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF5_DILIGENCIA)) {
				if (!Auxiliar.esEnteroMayorOIgualACero(PRCL_SPF5_DILIGENCIA)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_DILIGENCIA", idioma, "PRCL_SPF5_DILIGENCIA debe ser un entero mayor o igual a cero" + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF5_DILIGENCIA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_DILIGENCIA.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		boolean fechas_SPF5_ok = true;
		boolean fechaini_SPF5_ok = true;
		boolean fechafin_SPF5_ok = true;

		// VALIDAR PRCL_SPF5_FECHAINI
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF5_FECHAINI)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF5_FECHAINI)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_FECHAINI", idioma, "Fecha PRCL_SPF5_FECHAINI debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF5_FECHAINI, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_FECHAINI.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechaini_SPF5_ok = false;
				}
			}
		}

		// VALIDAR PRCL_SPF5_FECHAFIN
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF5_FECHAFIN)) {
				if (!Auxiliar.fechaEsValida(PRCL_SPF5_FECHAFIN)) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_FECHAFIN", idioma, "Fecha PRCL_SPF5_FECHAFIN debe ser válida y estar en formato aaaa-mm-dd.  Se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF5_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_FECHAFIN.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
					fechafin_SPF5_ok = false;
				}
			}
		}

		fechas_SPF5_ok = (fechaini_SPF5_ok && fechafin_SPF5_ok);
		if (fechas_SPF5_ok) {
			if (Auxiliar.tieneAlgo(PRCL_SPF5_FECHAINI) && Auxiliar.tieneAlgo(PRCL_SPF5_FECHAFIN)) {
				try {
					Date fechaini_spf2 = formatter.parse(PRCL_SPF5_FECHAINI);
					Date fechafin_spf2 = formatter.parse(PRCL_SPF5_FECHAFIN);
					if (fechaini_spf2.after(fechafin_spf2)) {
						t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_FECHAS_SPF5", idioma, "Fecha inicial de subparcela 2 no puede ser posterior a la fecha final" + "..");
						aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_SPF5_FECHAINI + " vs " + PRCL_SPF5_FECHAFIN, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_FECHAINI.focus(); }\"");
						observaciones += aviso;
						parametros_insert_ok = false;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		// VALIDAR PRCL_SPF5_POSIBLE
		if (!importacion) {
			if (Auxiliar.tieneAlgo(PRCL_SPF5_POSIBLE)) {
				if (!Auxiliar.nz(PRCL_SPF5_POSIBLE, "").equals("0")
						&& !Auxiliar.nz(PRCL_SPF5_POSIBLE, "").equals("1")) {
					t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_POSIBLE", idioma, "PRCL_SPF5_POSIBLE debe ser 0 o 1." + "..");
					aviso = Auxiliar.mensajeJS("advertencia", t + ":"
							+ PRCL_SPF5_POSIBLE, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_POSIBLE.focus(); }\"");
					observaciones += aviso;
					parametros_insert_ok = false;
				}
			}
		}

		// VALIDAR PRCL_SPF5_JUSTIFICACION_NO
		PRCL_SPF5_JUSTIFICACION_NO = Auxiliar.limpiarTexto(PRCL_SPF5_JUSTIFICACION_NO);
		PRCL_SPF5_JUSTIFICACION_NO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF5_JUSTIFICACION_NO, ""));
		if (PRCL_SPF5_JUSTIFICACION_NO.length() > 1000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_JUSTIFICACION_NO", idioma, "PRCL_SPF5_JUSTIFICACION_NO no puede exceder los 1000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF5_JUSTIFICACION_NO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_JUSTIFICACION_NO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}


		// VALIDAR PRCL_SPF5_OBS_FUSTALES
		PRCL_SPF5_OBS_FUSTALES = Auxiliar.limpiarTexto(PRCL_SPF5_OBS_FUSTALES);
		PRCL_SPF5_OBS_FUSTALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF5_OBS_FUSTALES, ""));
		if (PRCL_SPF5_OBS_FUSTALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_OBS_FUSTALES", idioma, "PRCL_SPF5_OBS_FUSTALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia",
					t + ":" + PRCL_SPF5_OBS_FUSTALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_OBS_FUSTALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		// VALIDAR PRCL_SPF5_OBS_LATIZALES
		PRCL_SPF5_OBS_LATIZALES = Auxiliar.limpiarTexto(PRCL_SPF5_OBS_LATIZALES);
		PRCL_SPF5_OBS_LATIZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF5_OBS_LATIZALES, ""));
		if (PRCL_SPF5_OBS_LATIZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_OBS_LATIZALES", idioma, "PRCL_SPF5_OBS_LATIZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF5_OBS_LATIZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_OBS_LATIZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		// VALIDAR PRCL_SPF5_OBS_BRINZALES
		PRCL_SPF5_OBS_BRINZALES = Auxiliar.limpiarTexto(PRCL_SPF5_OBS_BRINZALES);
		PRCL_SPF5_OBS_BRINZALES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_SPF5_OBS_BRINZALES, ""));
		if (PRCL_SPF5_OBS_BRINZALES.length() > 4000) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_SPF5_OBS_BRINZALES", idioma, "PRCL_SPF5_OBS_BRINZALES no puede exceder los 4000 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":"
					+ PRCL_SPF5_OBS_BRINZALES, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__SPF5', true); f.PRCL_SPF5_OBS_BRINZALES.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		
		
		// VALIDAR PRCL_PLOT
		if (Auxiliar.tieneAlgo(PRCL_PLOT)) {
			if (!Auxiliar.esEntero(PRCL_PLOT)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_PLOT", idioma, "PRCL_PLOT debe ser un entero positivo." + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_PLOT, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_PLOT.focus(); }\"");
				observaciones += aviso;
				parametros_import_ok = false;
				parametros_insert_ok = false;
			}
		}

		
		// VALIDAR PRCL_AREA
		// CALCULAR ÁREA POR DEFECTO
		
		PRCL_AREA = PRCL_AREA.replace(",", ".");

		double area_defecto = (5 * (Math.PI * Math.pow(15.0, 2))) / 10000;
		
		if (!Auxiliar.tieneAlgo(PRCL_AREA)) {
			PRCL_AREA = String.valueOf(area_defecto);
		}
		else {
			if (!Auxiliar.esNumeroPositivo(PRCL_AREA)) {
				t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_AREA", idioma, "PRCL_AREA debe ser un número decimal positivo, que representa el área de la parcela en hectáreas." + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_AREA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_AREA.focus(); }\"");
				observaciones += aviso;
				parametros_import_ok = false;
				parametros_insert_ok = false;
			}
		}


		// VALIDAR PRCL_INCLUIR
		if (!Auxiliar.nz(PRCL_INCLUIR, "").equals("0") && !Auxiliar.nz(PRCL_INCLUIR, "").equals("1")) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_INCLUIR", idioma, "PRCL_INCLUIR debe ser 0 (entra en cálculos) o 1 (no es incluida en cálculos)." + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_INCLUIR, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_INCLUIR.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_import_ok = false;
		}
		
		
		// VERIFICAR PRCL_TEMPORALIDAD
		conteo = "0";
		sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_TEMPORALIDAD WHERE TMPR_CONSECUTIVO=" + PRCL_TEMPORALIDAD;
		try {
			conteo = dbREDD.obtenerDato(sql_tmp, "0");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_TEMPORALIDAD", idioma, "PRCL_TEMPORALIDAD no encontrado" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_TEMPORALIDAD, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_TEMPORALIDAD.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_import_ok = false;
		}
		
		// VALIDAR PRCL_PUBLICA
		if (!Auxiliar.nz(PRCL_PUBLICA, "").equals("0") && !Auxiliar.nz(PRCL_PUBLICA, "").equals("1")) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_PUBLICA", idioma, "PRCL_PUBLICA debe ser 0 (confidencial) o 1 (pública)." + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_PUBLICA, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_PUBLICA.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
			parametros_import_ok = false;
		}
		
		
		// VALIDAR PRCL_HAB
		if (!Auxiliar.tieneAlgo(PRCL_HAB)) {
			if (!Auxiliar.nz(PRCL_HAB, "").equals("0") && !Auxiliar.nz(PRCL_HAB, "").equals("1")) {
				t = Auxiliar.traducir("PRCL_HAB", idioma, "PRCL_HAB debe ser 0 (No) o 1 (Sí)." + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_HAB, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_HAB.focus(); }\"");
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}
		
		// VALIDAR PRCL_DAP
		if (!Auxiliar.tieneAlgo(PRCL_DAP)) {
			if (!Auxiliar.nz(PRCL_DAP, "").equals("0") && !Auxiliar.nz(PRCL_DAP, "").equals("1")) {
				t = Auxiliar.traducir("PRCL_DAP", idioma, "PRCL_DAP debe ser 0 (No) o 1 (Sí)." + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_DAP, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_DAP.focus(); }\"");
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}		
		
		// VALIDAR PRCL_GPS
		if (!Auxiliar.tieneAlgo(PRCL_GPS)) {
			if (!Auxiliar.nz(PRCL_GPS, "").equals("0") && !Auxiliar.nz(PRCL_GPS, "").equals("1")) {
				t = Auxiliar.traducir("PRCL_GPS", idioma, "PRCL_GPS debe ser 0 (No) o 1 (Sí)." + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_GPS, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_GPS.focus(); }\"");
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}		
		
		// VERIFICAR PRCL_EQ
		if (!Auxiliar.tieneAlgo(PRCL_EQ)) {
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_ECUACIONALOMETRICA WHERE EQAL_ID=" + PRCL_EQ;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("PRCL_EQ", idioma, "PRCL_EQ no encontrado" + "..");
				aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_EQ, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_EQ.focus(); }\"");
				observaciones += aviso;
				//parametros_insert_ok = false;
				//parametros_import_ok = false;
			}
		}		

		// VALIDAR PRCL_BA
		if (Auxiliar.tieneAlgo(PRCL_BA)) {
			if (!Auxiliar.esBiomasaAerea(PRCL_BA)) {
				t = Auxiliar.traducir("PRCL_BA", idioma, "PRCL_BA debe ser un decimal mayor o igual a cero" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_BA, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}
		
		// VALIDAR PRCL_BS
		if (Auxiliar.tieneAlgo(PRCL_BS)) {
			if (!Auxiliar.esBiomasaSubterranea(PRCL_BS)) {
				t = Auxiliar.traducir("PRCL_BS", idioma, "PRCL_BS debe ser un decimal mayor o igual a cero" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_BS, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}		
		
		// VALIDAR PRCL_BT
		if (Auxiliar.tieneAlgo(PRCL_BT)) {
			if (!Auxiliar.esBiomasaTotal(PRCL_BT)) {
				t = Auxiliar.traducir("PRCL_BT", idioma, "PRCL_BT debe ser un decimal mayor o igual a cero" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_BT, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}
		
		// VALIDAR PRCL_AUTORCUSTODIOINFO
		PRCL_AUTORCUSTODIOINFO = Auxiliar.limpiarTexto(PRCL_AUTORCUSTODIOINFO);
		PRCL_AUTORCUSTODIOINFO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_AUTORCUSTODIOINFO, ""));
		if (PRCL_AUTORCUSTODIOINFO.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_AUTORCUSTODIOINFO", idioma, "PRCL_AUTORCUSTODIOINFO no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensajeJS("advertencia", t + ":" + PRCL_AUTORCUSTODIOINFO, usuario, metodo, "onclick=\"javascript:if(document.f) { DIVer('div__campos_importacion', true); f.PRCL_AUTORCUSTODIOINFO.focus(); }\"");
			observaciones += aviso;
			parametros_insert_ok = false;
		}

		// VERIFICAR PRCL_TIPOBOSQUE
		if (Auxiliar.tieneAlgo(PRCL_TIPOBOSQUE)) {
			String PRCL_TEXTO_TIPOBOSQUE = PRCL_TIPOBOSQUE;
			try {
				String sql_temp = "";
				if (Auxiliar.esEnteroMayorOIgualACero(PRCL_TIPOBOSQUE)) {
					sql_temp = "SELECT TPBS_CONSECUTIVO FROM RED_TIPOBOSQUE WHERE TPBS_CONSECUTIVO=" + PRCL_TIPOBOSQUE;
				}
				else {
					PRCL_TIPOBOSQUE = Auxiliar.limpiarTexto(PRCL_TIPOBOSQUE);
					PRCL_TIPOBOSQUE = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(PRCL_TIPOBOSQUE, ""));
					sql_temp = "SELECT TPBS_CONSECUTIVO FROM RED_TIPOBOSQUE WHERE TPBS_NOMBRE='" + PRCL_TIPOBOSQUE + "' OR TPBS_DESCRIPCION='" + PRCL_TIPOBOSQUE + "'";
				}
				PRCL_TIPOBOSQUE = dbREDD.obtenerDato(sql_temp, "");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (!Auxiliar.esEntero(PRCL_TIPOBOSQUE)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.PRCL_TIPOBOSQUE", idioma, "PRCL_TIPOBOSQUE no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + PRCL_TEXTO_TIPOBOSQUE, usuario, metodo);
				observaciones += aviso;
				parametros_insert_ok = false;
				parametros_import_ok = false;
			}
		}		
		
		
		
		// REALIZAR OPERACION
		
		String sql_guardar = "";
		
		try 
		{
			if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
				if (!parametros_update_ok || !parametros_insert_ok) {
					dbREDD.desconectarse();
					return "0-=-"+observaciones;
				}
				
				sql_guardar = "UPDATE RED_PARCELA SET ";
				if (Auxiliar.noEsNulo(PRCL_CONS_PAIS)) sql_guardar += "PRCL_CONS_PAIS=" + PRCL_CONS_PAIS+ ",";
				if (!Auxiliar.tieneAlgo(PRCL_FECHA_CAPTURA)) sql_guardar += "PRCL_FECHA_CAPTURA=SYSDATE,";
				else sql_guardar += "PRCL_FECHA_CAPTURA=TO_DATE('" + PRCL_FECHA_CAPTURA + "', 'YYYY-MM-DD'),";
				sql_guardar += "PRCL_ACTUALIZACION="+Auxiliar.nzVacio(PRCL_ACTUALIZACION, "SYSDATE")+",";
				if (Auxiliar.tieneAlgo(PRCL_FECHAINI_APROXIMACION)) sql_guardar += "PRCL_FECHAINI_APROXIMACION=TO_DATE('" + PRCL_FECHAINI_APROXIMACION + "', 'YYYY-MM-DD'),"; 
				if (Auxiliar.tieneAlgo(PRCL_FECHAFIN_APROXIMACION)) sql_guardar += "PRCL_FECHAFIN_APROXIMACION=TO_DATE('" + PRCL_FECHAFIN_APROXIMACION + "', 'YYYY-MM-DD'),";
				if (Auxiliar.tieneAlgo(PRCL_FECHAINI_LOCALIZACION)) sql_guardar += "PRCL_FECHAINI_LOCALIZACION=TO_DATE('" + PRCL_FECHAINI_LOCALIZACION + "', 'YYYY-MM-DD'),";
				if (Auxiliar.tieneAlgo(PRCL_FECHAFIN_LOCALIZACION)) sql_guardar += "PRCL_FECHAFIN_LOCALIZACION=TO_DATE('" + PRCL_FECHAFIN_LOCALIZACION + "', 'YYYY-MM-DD'),";
				if (Auxiliar.noEsNulo(PRCL_NOMBRE)) sql_guardar += "PRCL_NOMBRE='" + PRCL_NOMBRE.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_DESCRIPCION)) sql_guardar += "PRCL_DESCRIPCION='" + PRCL_DESCRIPCION.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_OBSERVACIONES)) sql_guardar += "PRCL_OBSERVACIONES='" + PRCL_OBSERVACIONES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_TRACKLOG_CAMPAMENTO)) sql_guardar += "PRCL_TRACKLOG_CAMPAMENTO='" + PRCL_TRACKLOG_CAMPAMENTO.replace("'", "") + "',";
				if (Auxiliar.noEsNulo(PRCL_TRACKLOG_PARCELA)) sql_guardar += "PRCL_TRACKLOG_PARCELA='" + PRCL_TRACKLOG_PARCELA.replace("'", "") + "',";
				if (Auxiliar.noEsNulo(PRCL_ID_UPM)) sql_guardar += "PRCL_ID_UPM=" + PRCL_ID_UPM + ",";
				if (Auxiliar.noEsNulo(PRCL_MEDIOACCESO_POBLADO)) sql_guardar += "PRCL_MEDIOACCESO_POBLADO=" + Auxiliar.nzVacio(PRCL_MEDIOACCESO_POBLADO, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_DISTANCIA_POBLADO)) sql_guardar += "PRCL_DISTANCIA_POBLADO=" + Auxiliar.nzVacio(PRCL_DISTANCIA_POBLADO, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TPOBLADO_H)) sql_guardar += "PRCL_TPOBLADO_H=" + Auxiliar.nzVacio(PRCL_TPOBLADO_H, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TPOBLADO_M)) sql_guardar += "PRCL_TPOBLADO_M=" + Auxiliar.nzVacio(PRCL_TPOBLADO_M, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_MEDIOACCESO_CAMPAMENTO)) sql_guardar += "PRCL_MEDIOACCESO_CAMPAMENTO=" + Auxiliar.nzVacio(PRCL_MEDIOACCESO_CAMPAMENTO, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_DISTANCIA_CAMPAMENTO)) sql_guardar += "PRCL_DISTANCIA_CAMPAMENTO=" + Auxiliar.nzVacio(PRCL_DISTANCIA_CAMPAMENTO, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TCAMPAMENTO_H)) sql_guardar += "PRCL_TCAMPAMENTO_H=" + Auxiliar.nzVacio(PRCL_TCAMPAMENTO_H, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TCAMPAMENTO_M)) sql_guardar += "PRCL_TCAMPAMENTO_M=" + Auxiliar.nzVacio(PRCL_TCAMPAMENTO_M, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_MEDIOACCESO_JALON)) sql_guardar += "PRCL_MEDIOACCESO_JALON=" + Auxiliar.nzVacio(PRCL_MEDIOACCESO_JALON, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_DISTANCIA_JALON)) sql_guardar += "PRCL_DISTANCIA_JALON=" + Auxiliar.nzVacio(PRCL_DISTANCIA_JALON, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TJALON_H)) sql_guardar += "PRCL_TJALON_H=" + Auxiliar.nzVacio(PRCL_TJALON_H, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TJALON_M)) sql_guardar += "PRCL_TJALON_M=" + Auxiliar.nzVacio(PRCL_TJALON_M, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_DISTANCIA_CAMPAMENTOS)) sql_guardar += "PRCL_DISTANCIA_CAMPAMENTOS=" + Auxiliar.nzVacio(PRCL_DISTANCIA_CAMPAMENTOS, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_LATITUD)) sql_guardar += "PRCL_LATITUD=" + PRCL_LATITUD + ",";
				if (Auxiliar.noEsNulo(PRCL_LONGITUD)) sql_guardar += "PRCL_LONGITUD=" + PRCL_LONGITUD + ",";
				if (Auxiliar.noEsNulo(PRCL_ALTITUD)) sql_guardar += "PRCL_ALTITUD=" + PRCL_ALTITUD + ",";
				if (Auxiliar.noEsNulo(PRCL_DEPARTAMENTO)) sql_guardar += "PRCL_DEPARTAMENTO='" + PRCL_DEPARTAMENTO+ "',";
				if (Auxiliar.noEsNulo(PRCL_MUNICIPIO)) sql_guardar += "PRCL_MUNICIPIO='" + PRCL_MUNICIPIO+ "',";
				if (Auxiliar.noEsNulo(PRCL_USR_DILIGENCIA_F1)) sql_guardar += "PRCL_USR_DILIGENCIA_F1=" + Auxiliar.nzVacio(PRCL_USR_DILIGENCIA_F1, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_USR_DILIGENCIA_F2)) sql_guardar += "PRCL_USR_DILIGENCIA_F2=" + Auxiliar.nzVacio(PRCL_USR_DILIGENCIA_F2, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_SPF1_DILIGENCIA)) sql_guardar += "PRCL_SPF1_DILIGENCIA=" + Auxiliar.nzVacio(PRCL_SPF1_DILIGENCIA, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_SPF1_FECHAINI)) sql_guardar += "PRCL_SPF1_FECHAINI=TO_DATE('" + PRCL_SPF1_FECHAINI + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF1_FECHAINI=NULL,"; 
				if (Auxiliar.tieneAlgo(PRCL_SPF1_FECHAFIN)) sql_guardar += "PRCL_SPF1_FECHAFIN=TO_DATE('" + PRCL_SPF1_FECHAFIN + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF1_FECHAFIN=NULL,";
				if (Auxiliar.noEsNulo(PRCL_SPF1_POSIBLE)) sql_guardar += "PRCL_SPF1_POSIBLE=" + Auxiliar.nzVacio(PRCL_SPF1_POSIBLE, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_SPF1_JUSTIFICACION_NO)) sql_guardar += "PRCL_SPF1_JUSTIFICACION_NO='" + PRCL_SPF1_JUSTIFICACION_NO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF1_OBS_FUSTALES)) sql_guardar += "PRCL_SPF1_OBS_FUSTALES='" + PRCL_SPF1_OBS_FUSTALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF1_OBS_LATIZALES)) sql_guardar += "PRCL_SPF1_OBS_LATIZALES='" + PRCL_SPF1_OBS_LATIZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF1_OBS_BRINZALES)) sql_guardar += "PRCL_SPF1_OBS_BRINZALES='" + PRCL_SPF1_OBS_BRINZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF2_DILIGENCIA)) sql_guardar += "PRCL_SPF2_DILIGENCIA=" + Auxiliar.nzVacio(PRCL_SPF2_DILIGENCIA, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_SPF2_FECHAINI)) sql_guardar += "PRCL_SPF2_FECHAINI=TO_DATE('" + PRCL_SPF2_FECHAINI + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF2_FECHAINI=NULL,";
				if (Auxiliar.tieneAlgo(PRCL_SPF2_FECHAFIN)) sql_guardar += "PRCL_SPF2_FECHAFIN=TO_DATE('" + PRCL_SPF2_FECHAFIN + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF2_FECHAFIN=NULL,";
				if (Auxiliar.noEsNulo(PRCL_SPF2_POSIBLE)) sql_guardar += "PRCL_SPF2_POSIBLE=" + Auxiliar.nzVacio(PRCL_SPF2_POSIBLE, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_SPF2_JUSTIFICACION_NO)) sql_guardar += "PRCL_SPF2_JUSTIFICACION_NO='" + PRCL_SPF2_JUSTIFICACION_NO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF2_OBS_FUSTALES)) sql_guardar += "PRCL_SPF2_OBS_FUSTALES='" + PRCL_SPF2_OBS_FUSTALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF2_OBS_LATIZALES)) sql_guardar += "PRCL_SPF2_OBS_LATIZALES='" + PRCL_SPF2_OBS_LATIZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF2_OBS_BRINZALES)) sql_guardar += "PRCL_SPF2_OBS_BRINZALES='" + PRCL_SPF2_OBS_BRINZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF3_DILIGENCIA)) sql_guardar += "PRCL_SPF3_DILIGENCIA=" + Auxiliar.nzVacio(PRCL_SPF3_DILIGENCIA, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_SPF3_FECHAINI)) sql_guardar += "PRCL_SPF3_FECHAINI=TO_DATE('" + PRCL_SPF3_FECHAINI + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF3_FECHAINI=NULL,";
				if (Auxiliar.tieneAlgo(PRCL_SPF3_FECHAFIN)) sql_guardar += "PRCL_SPF3_FECHAFIN=TO_DATE('" + PRCL_SPF3_FECHAFIN + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF3_FECHAFIN=NULL,";
				if (Auxiliar.noEsNulo(PRCL_SPF3_POSIBLE)) sql_guardar += "PRCL_SPF3_POSIBLE=" + Auxiliar.nzVacio(PRCL_SPF3_POSIBLE, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_SPF3_JUSTIFICACION_NO)) sql_guardar += "PRCL_SPF3_JUSTIFICACION_NO='" + PRCL_SPF3_JUSTIFICACION_NO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF3_OBS_FUSTALES)) sql_guardar += "PRCL_SPF3_OBS_FUSTALES='" + PRCL_SPF3_OBS_FUSTALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF3_OBS_LATIZALES)) sql_guardar += "PRCL_SPF3_OBS_LATIZALES='" + PRCL_SPF3_OBS_LATIZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF3_OBS_BRINZALES)) sql_guardar += "PRCL_SPF3_OBS_BRINZALES='" + PRCL_SPF3_OBS_BRINZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF4_DILIGENCIA)) sql_guardar += "PRCL_SPF4_DILIGENCIA=" + Auxiliar.nzVacio(PRCL_SPF4_DILIGENCIA, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_SPF4_FECHAINI)) sql_guardar += "PRCL_SPF4_FECHAINI=TO_DATE('" + PRCL_SPF4_FECHAINI + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF4_FECHAINI=NULL,";
				if (Auxiliar.tieneAlgo(PRCL_SPF4_FECHAFIN)) sql_guardar += "PRCL_SPF4_FECHAFIN=TO_DATE('" + PRCL_SPF4_FECHAFIN + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF4_FECHAFIN=NULL,";
				if (Auxiliar.noEsNulo(PRCL_SPF4_POSIBLE)) sql_guardar += "PRCL_SPF4_POSIBLE=" + Auxiliar.nzVacio(PRCL_SPF4_POSIBLE, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_SPF4_JUSTIFICACION_NO)) sql_guardar += "PRCL_SPF4_JUSTIFICACION_NO='" + PRCL_SPF4_JUSTIFICACION_NO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF4_OBS_FUSTALES)) sql_guardar += "PRCL_SPF4_OBS_FUSTALES='" + PRCL_SPF4_OBS_FUSTALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF4_OBS_LATIZALES)) sql_guardar += "PRCL_SPF4_OBS_LATIZALES='" + PRCL_SPF4_OBS_LATIZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF4_OBS_BRINZALES)) sql_guardar += "PRCL_SPF4_OBS_BRINZALES='" + PRCL_SPF4_OBS_BRINZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF5_DILIGENCIA)) sql_guardar += "PRCL_SPF5_DILIGENCIA=" + Auxiliar.nzVacio(PRCL_SPF5_DILIGENCIA, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_SPF5_FECHAINI)) sql_guardar += "PRCL_SPF5_FECHAINI=TO_DATE('" + PRCL_SPF5_FECHAINI + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF5_FECHAINI=NULL,";
				if (Auxiliar.tieneAlgo(PRCL_SPF5_FECHAFIN)) sql_guardar += "PRCL_SPF5_FECHAFIN=TO_DATE('" + PRCL_SPF5_FECHAFIN + "', 'YYYY-MM-DD'),"; else sql_guardar += "PRCL_SPF5_FECHAFIN=NULL,";
				if (Auxiliar.noEsNulo(PRCL_SPF5_POSIBLE)) sql_guardar += "PRCL_SPF5_POSIBLE=" + Auxiliar.nzVacio(PRCL_SPF5_POSIBLE, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_SPF5_JUSTIFICACION_NO)) sql_guardar += "PRCL_SPF5_JUSTIFICACION_NO='" + PRCL_SPF5_JUSTIFICACION_NO.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF5_OBS_FUSTALES)) sql_guardar += "PRCL_SPF5_OBS_FUSTALES='" + PRCL_SPF5_OBS_FUSTALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF5_OBS_LATIZALES)) sql_guardar += "PRCL_SPF5_OBS_LATIZALES='" + PRCL_SPF5_OBS_LATIZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_SPF5_OBS_BRINZALES)) sql_guardar += "PRCL_SPF5_OBS_BRINZALES='" + PRCL_SPF5_OBS_BRINZALES.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_VEREDA)) sql_guardar += "PRCL_VEREDA='" + PRCL_VEREDA.replace("'", "`") +"',";
				if (Auxiliar.noEsNulo(PRCL_CORREGIMIENTO)) sql_guardar += "PRCL_CORREGIMIENTO='" + PRCL_CORREGIMIENTO.replace("'", "`") +"',";
				if (Auxiliar.noEsNulo(PRCL_INSPECCION_POLICIA)) sql_guardar += "PRCL_INSPECCION_POLICIA='" + PRCL_INSPECCION_POLICIA.replace("'", "`") +"',";
				if (Auxiliar.noEsNulo(PRCL_CASERIO)) sql_guardar += "PRCL_CASERIO='" + PRCL_CASERIO.replace("'", "`") +"',";
				if (Auxiliar.noEsNulo(PRCL_RANCHERIA)) sql_guardar += "PRCL_RANCHERIA='" + PRCL_RANCHERIA.replace("'", "`") + "',";
				if (Auxiliar.noEsNulo(PRCL_PLOT)) sql_guardar += "PRCL_PLOT=" + Auxiliar.nzVacio(PRCL_PLOT, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_AREA)) sql_guardar += "PRCL_AREA=" + Auxiliar.nzVacio(PRCL_AREA, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_TEMPORALIDAD)) sql_guardar += "PRCL_TEMPORALIDAD=" + Auxiliar.nzVacio(PRCL_TEMPORALIDAD, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_PUBLICA)) sql_guardar += "PRCL_PUBLICA=" + Auxiliar.nzVacio(PRCL_PUBLICA, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_HAB)) sql_guardar += "PRCL_HAB=" + Auxiliar.nzVacio(PRCL_HAB, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_DAP)) sql_guardar += "PRCL_DAP=" + Auxiliar.nzVacio(PRCL_DAP, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_GPS)) sql_guardar += "PRCL_GPS=" + Auxiliar.nzVacio(PRCL_GPS, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_EQ)) sql_guardar += "PRCL_EQ=" + Auxiliar.nzVacio(PRCL_EQ, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_BA)) sql_guardar += "PRCL_BA=" + Auxiliar.nzVacio(PRCL_BA, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_BS)) sql_guardar += "PRCL_BS=" + Auxiliar.nzVacio(PRCL_BS, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_BT)) sql_guardar += "PRCL_BT=" + Auxiliar.nzVacio(PRCL_BT, "NULL") + ",";
				if (Auxiliar.noEsNulo(PRCL_AUTORCUSTODIOINFO)) sql_guardar += "PRCL_AUTORCUSTODIOINFO='" + Auxiliar.nzVacio(PRCL_AUTORCUSTODIOINFO, "NULL") + "',";
				if (Auxiliar.noEsNulo(PRCL_TIPOBOSQUE)) sql_guardar += "PRCL_TIPOBOSQUE=" + Auxiliar.nzVacio(PRCL_TIPOBOSQUE, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_ARCH_CONSECUTIVO)) sql_guardar += "PRCL_ARCH_CONSECUTIVO=" + PRCL_ARCH_CONSECUTIVO + ",";
				sql_guardar += "PRCL_INCLUIR=" + Auxiliar.nzVacio(PRCL_INCLUIR, "NULL") + ",";
				sql_guardar += "PRCL_MODIFICADOR=" + Auxiliar.nzVacio(id_usuario, "NULL") + "";
				sql_guardar += " WHERE PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO + " AND PRCL_ACTUALIZACION<" + Auxiliar.nzVacio(PRCL_ACTUALIZACION, "SYSDATE");

				update = true;

			}
			else {
				if (!parametros_insert_ok) {
					dbREDD.desconectarse();
					return "0-=-"+observaciones;
				}
				
				PRCL_CONSECUTIVO = dbREDD.obtenerDato("SELECT red_sq_PRD_PARCELA.nextval FROM DUAL", "" );
				
				sql_guardar = "INSERT INTO RED_PARCELA ( ";
				sql_guardar += "PRCL_CONSECUTIVO, ";
				sql_guardar += "PRCL_CONS_PAIS, ";
				sql_guardar += "PRCL_FECHA_CAPTURA, ";
				sql_guardar += "PRCL_FECHAINI_APROXIMACION, ";
				sql_guardar += "PRCL_FECHAFIN_APROXIMACION, ";
				sql_guardar += "PRCL_FECHAINI_LOCALIZACION, ";
				sql_guardar += "PRCL_FECHAFIN_LOCALIZACION, ";
				sql_guardar += "PRCL_NOMBRE, ";
				sql_guardar += "PRCL_DESCRIPCION, ";
				sql_guardar += "PRCL_OBSERVACIONES, ";
				sql_guardar += "PRCL_TRACKLOG_CAMPAMENTO, ";
				sql_guardar += "PRCL_TRACKLOG_PARCELA, ";
				sql_guardar += "PRCL_ID_UPM, ";
				sql_guardar += "PRCL_MEDIOACCESO_POBLADO, ";
				sql_guardar += "PRCL_DISTANCIA_POBLADO, ";
				sql_guardar += "PRCL_TPOBLADO_H, ";
				sql_guardar += "PRCL_TPOBLADO_M, ";
				sql_guardar += "PRCL_MEDIOACCESO_CAMPAMENTO, ";
				sql_guardar += "PRCL_DISTANCIA_CAMPAMENTO, ";
				sql_guardar += "PRCL_TCAMPAMENTO_H, ";
				sql_guardar += "PRCL_TCAMPAMENTO_M, ";
				sql_guardar += "PRCL_MEDIOACCESO_JALON, ";
				sql_guardar += "PRCL_DISTANCIA_JALON, ";
				sql_guardar += "PRCL_TJALON_H, ";
				sql_guardar += "PRCL_TJALON_M, ";
				sql_guardar += "PRCL_DISTANCIA_CAMPAMENTOS, ";
				sql_guardar += "PRCL_LATITUD, ";
				sql_guardar += "PRCL_LONGITUD, ";
				sql_guardar += "PRCL_ALTITUD, ";
				if (Auxiliar.tieneAlgo(PRCL_DEPARTAMENTO)) sql_guardar += "PRCL_DEPARTAMENTO, ";
				if (Auxiliar.tieneAlgo(PRCL_MUNICIPIO)) sql_guardar += "PRCL_MUNICIPIO, ";
				sql_guardar += "PRCL_ID_IMPORTACION, ";
				sql_guardar += "PRCL_USR_DILIGENCIA_F1, ";
				sql_guardar += "PRCL_USR_DILIGENCIA_F2, ";
				sql_guardar += "PRCL_SPF1_DILIGENCIA, ";
				sql_guardar += "PRCL_SPF1_FECHAINI, ";
				sql_guardar += "PRCL_SPF1_FECHAFIN, ";
				sql_guardar += "PRCL_SPF1_POSIBLE, ";
				sql_guardar += "PRCL_SPF1_JUSTIFICACION_NO, ";
				sql_guardar += "PRCL_SPF1_OBS_FUSTALES, ";
				sql_guardar += "PRCL_SPF1_OBS_LATIZALES, ";
				sql_guardar += "PRCL_SPF1_OBS_BRINZALES, ";
				sql_guardar += "PRCL_SPF2_DILIGENCIA, ";
				sql_guardar += "PRCL_SPF2_FECHAINI, ";
				sql_guardar += "PRCL_SPF2_FECHAFIN, ";
				sql_guardar += "PRCL_SPF2_POSIBLE, ";
				sql_guardar += "PRCL_SPF2_JUSTIFICACION_NO, ";
				sql_guardar += "PRCL_SPF2_OBS_FUSTALES, ";
				sql_guardar += "PRCL_SPF2_OBS_LATIZALES, ";
				sql_guardar += "PRCL_SPF2_OBS_BRINZALES, ";
				sql_guardar += "PRCL_SPF3_DILIGENCIA, ";
				sql_guardar += "PRCL_SPF3_FECHAINI, ";
				sql_guardar += "PRCL_SPF3_FECHAFIN, ";
				sql_guardar += "PRCL_SPF3_POSIBLE, ";
				sql_guardar += "PRCL_SPF3_JUSTIFICACION_NO, ";
				sql_guardar += "PRCL_SPF3_OBS_FUSTALES, ";
				sql_guardar += "PRCL_SPF3_OBS_LATIZALES, ";
				sql_guardar += "PRCL_SPF3_OBS_BRINZALES, ";
				sql_guardar += "PRCL_SPF4_DILIGENCIA, ";
				sql_guardar += "PRCL_SPF4_FECHAINI, ";
				sql_guardar += "PRCL_SPF4_FECHAFIN, ";
				sql_guardar += "PRCL_SPF4_POSIBLE, ";
				sql_guardar += "PRCL_SPF4_JUSTIFICACION_NO, ";
				sql_guardar += "PRCL_SPF4_OBS_FUSTALES, ";
				sql_guardar += "PRCL_SPF4_OBS_LATIZALES, ";
				sql_guardar += "PRCL_SPF4_OBS_BRINZALES, ";
				sql_guardar += "PRCL_SPF5_DILIGENCIA, ";
				sql_guardar += "PRCL_SPF5_FECHAINI, ";
				sql_guardar += "PRCL_SPF5_FECHAFIN, ";
				sql_guardar += "PRCL_SPF5_POSIBLE, ";
				sql_guardar += "PRCL_SPF5_JUSTIFICACION_NO, ";
				sql_guardar += "PRCL_SPF5_OBS_FUSTALES, ";
				sql_guardar += "PRCL_SPF5_OBS_LATIZALES, ";
				sql_guardar += "PRCL_SPF5_OBS_BRINZALES, ";
				sql_guardar += "PRCL_VEREDA, ";
				sql_guardar += "PRCL_CORREGIMIENTO, ";
				sql_guardar += "PRCL_INSPECCION_POLICIA, ";
				sql_guardar += "PRCL_CASERIO, ";
				sql_guardar += "PRCL_RANCHERIA, ";
				sql_guardar += "PRCL_PLOT, ";
				sql_guardar += "PRCL_AREA, ";
				sql_guardar += "PRCL_INCLUIR, ";
				sql_guardar += "PRCL_TEMPORALIDAD, ";
				sql_guardar += "PRCL_PUBLICA, ";
				sql_guardar += "PRCL_HAB, ";
				sql_guardar += "PRCL_DAP, ";
				sql_guardar += "PRCL_GPS, ";
				sql_guardar += "PRCL_EQ, ";
				sql_guardar += "PRCL_BA, ";
				sql_guardar += "PRCL_BS, ";
				sql_guardar += "PRCL_BT, ";
				sql_guardar += "PRCL_AUTORCUSTODIOINFO, ";
				sql_guardar += "PRCL_TIPOBOSQUE, ";
				sql_guardar += "PRCL_ACTUALIZACION, ";
				sql_guardar += "PRCL_ARCH_CONSECUTIVO,";
				sql_guardar += "PRCL_CREADOR";
				sql_guardar += " ) VALUES ( ";
				sql_guardar += "" + PRCL_CONSECUTIVO+ ",";
				sql_guardar += "" + PRCL_CONS_PAIS+ ",";
				if (!Auxiliar.tieneAlgo(PRCL_FECHA_CAPTURA)) sql_guardar += "SYSDATE,";
				else sql_guardar += "TO_DATE('" + PRCL_FECHA_CAPTURA.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_FECHAINI_APROXIMACION + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_FECHAFIN_APROXIMACION + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_FECHAINI_LOCALIZACION + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_FECHAFIN_LOCALIZACION + "', 'YYYY-MM-DD'),";
				sql_guardar += "'" + PRCL_NOMBRE.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_DESCRIPCION.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_OBSERVACIONES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_TRACKLOG_CAMPAMENTO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_TRACKLOG_PARCELA.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_ID_UPM, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_MEDIOACCESO_POBLADO, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_DISTANCIA_POBLADO, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TPOBLADO_H, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TPOBLADO_M, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_MEDIOACCESO_CAMPAMENTO, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_DISTANCIA_CAMPAMENTO, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TCAMPAMENTO_H, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TCAMPAMENTO_M, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_MEDIOACCESO_JALON, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_DISTANCIA_JALON, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TJALON_H, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TJALON_M, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_DISTANCIA_CAMPAMENTOS, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_LATITUD, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_LONGITUD, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_ALTITUD, "NULL") + ",";
				if (Auxiliar.tieneAlgo(PRCL_DEPARTAMENTO)) sql_guardar +=  "'" + PRCL_DEPARTAMENTO.replace("'", "`") + "',";
				if (Auxiliar.tieneAlgo(PRCL_MUNICIPIO)) sql_guardar += "'" + PRCL_MUNICIPIO+ "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_ID_IMPORTACION, "NULL") + ",";
				sql_guardar += "'" + PRCL_USR_DILIGENCIA_F1.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_USR_DILIGENCIA_F2.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF1_DILIGENCIA, "NULL") + ",";
				sql_guardar += "TO_DATE('" + PRCL_SPF1_FECHAINI.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_SPF1_FECHAFIN.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF1_POSIBLE, "NULL") + ",";
				sql_guardar += "'" + PRCL_SPF1_JUSTIFICACION_NO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF1_OBS_FUSTALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF1_OBS_LATIZALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF1_OBS_BRINZALES.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF2_DILIGENCIA, "NULL") + ",";
				sql_guardar += "TO_DATE('" + PRCL_SPF2_FECHAINI.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_SPF2_FECHAFIN.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF2_POSIBLE, "NULL") + ",";
				sql_guardar += "'" + PRCL_SPF2_JUSTIFICACION_NO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF2_OBS_FUSTALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF2_OBS_LATIZALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF2_OBS_BRINZALES.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF3_DILIGENCIA, "NULL") + ",";
				sql_guardar += "TO_DATE('" + PRCL_SPF3_FECHAINI.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_SPF3_FECHAFIN.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF3_POSIBLE, "NULL") + ",";
				sql_guardar += "'" + PRCL_SPF3_JUSTIFICACION_NO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF3_OBS_FUSTALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF3_OBS_LATIZALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF3_OBS_BRINZALES.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF4_DILIGENCIA, "NULL") + ",";
				sql_guardar += "TO_DATE('" + PRCL_SPF4_FECHAINI.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_SPF4_FECHAFIN.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF4_POSIBLE, "NULL") + ",";
				sql_guardar += "'" + PRCL_SPF4_JUSTIFICACION_NO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF4_OBS_FUSTALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF4_OBS_LATIZALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF4_OBS_BRINZALES.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF5_DILIGENCIA, "NULL") + ",";
				sql_guardar += "TO_DATE('" + PRCL_SPF5_FECHAINI.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "TO_DATE('" + PRCL_SPF5_FECHAFIN.replace("'", "`") + "', 'YYYY-MM-DD'),";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_SPF5_POSIBLE, "NULL") + ",";
				sql_guardar += "'" + PRCL_SPF5_JUSTIFICACION_NO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF5_OBS_FUSTALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF5_OBS_LATIZALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_SPF5_OBS_BRINZALES.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_VEREDA.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_CORREGIMIENTO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_INSPECCION_POLICIA.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_CASERIO.replace("'", "`") + "',";
				sql_guardar += "'" + PRCL_RANCHERIA.replace("'", "`") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_PLOT, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_AREA, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_INCLUIR, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TEMPORALIDAD, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_PUBLICA, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_HAB, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_DAP, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_GPS, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_EQ, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_BA, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_BS, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_BT, "NULL") + ",";
				sql_guardar += "'" + Auxiliar.nzVacio(PRCL_AUTORCUSTODIOINFO, "NULL") + "',";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_TIPOBOSQUE, "NULL") + ",";
				sql_guardar += Auxiliar.nzVacio(PRCL_ACTUALIZACION, "SYSDATE") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(PRCL_ARCH_CONSECUTIVO, "NULL") + ",";
				sql_guardar += "" + Auxiliar.nzVacio(id_usuario, "NULL") + "";
				sql_guardar += ")";
			}

			if (update) {
				id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "");
				if (id_usuario.equals(id_creador)) {
					if (sec.tienePermiso(id_usuario, "12")) {
						privilegio = true;
					}
				}
				else {
					if (sec.tienePermiso(id_usuario, "13")) {
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

				try {
					ok_guardar = dbREDD.ejecutarSQL(sql_guardar);
					
					if (!ok_guardar) {
						if (update) {
							id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "");
							if (id_usuario.equals(id_creador)) {
								if (sec.tienePermiso(id_usuario, "12")) {
									sec.registrarTransaccion(request, 12, PRCL_CONSECUTIVO, sql_guardar, ok_guardar);
								}
							}
							else {
								if (sec.tienePermiso(id_usuario, "13")) {
									sec.registrarTransaccion(request, 13, PRCL_CONSECUTIVO, sql_guardar, ok_guardar);
								}
							}
						}
						else {
							if (sec.tienePermiso(id_usuario, "13")) {
								sec.registrarTransaccion(request, 9, PRCL_CONSECUTIVO, sql_guardar, ok_guardar);
							}
						}

						dbREDD.desconectarse();
						return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar la parcela [" + sql_guardar + "]:" + dbREDD.ultimoError, usuario, metodo);									
					}
					
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo una excepción al intentar guardar la parcela [" + sql_guardar + "]:" + dbREDD.ultimoError, usuario, metodo);									
				}
			
				
				boolean ok_coordenadas = true;
				String sql_coordenada = "";

				boolean ok_geometria = true;
				String sql_geometria = "";

				int c = 0;
				
				String [] a_coordenadas = {""};
				
				if (false) {
					if (Auxiliar.tieneAlgo(PRCL_COORDENADAS)) {
						a_coordenadas = PRCL_COORDENADAS.split("_");
					}
					else {
						String str_n_coordenadas = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_PARCELA_COORDENADAS WHERE PRCR_PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "0");
						int n_coordenadas = Integer.parseInt(str_n_coordenadas);
						
						if (n_coordenadas > 0) {
							a_coordenadas = new String[n_coordenadas];
							
							String sql_coordenadas = "SELECT PRCR_SECUENCIA, PRCR_LATITUD, PRCR_LONGITUD FROM RED_PARCELA_COORDENADAS WHERE PRCR_PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO + " ORDER BY PRCR_SECUENCIA";
							
							try {
								ResultSet rset = dbREDD.consultarBD(sql_coordenadas);
								
								if (rset != null) {
									
									while (rset.next()) {
										a_coordenadas[c] = rset.getString("PRCR_SECUENCIA") + "," + rset.getString("PRCR_LATITUD") + "," + rset.getString("PRCR_LONGITUD");
										c++;
									}
									rset.close();
								}
								else {
									System.out.println("Error SQL al consultar usuarios:"+dbREDD.ultimoError+"@@"+sql_coordenadas);
								}
							} catch (SQLException e) {
								System.out.println("Error SQL al consultar usuarios:"+dbREDD.ultimoError+"@@"+sql_coordenadas);
							} catch (Exception e) {
								System.out.println("Excepción: " + e.toString());
							}
						}
					}
					
					c = 0;
					
					int n_vertices = a_coordenadas.length; 
					
					if (n_vertices > 0) { 
						
						if (n_vertices > 2) {
							String [] a_vertices = new String[n_vertices+1];
							
							String verticeInicialFinal = "";
							
							for (c=0; c<a_coordenadas.length; c++) {
								String [] a_coordenada = a_coordenadas[c].split(",");
								sql_coordenada = "";
								
								if (a_coordenada.length == 3) {
									String secuencia = a_coordenada[0];
									
									if (Auxiliar.esEntero(secuencia)) {
										String latitud = a_coordenada[1];
										
										if (Auxiliar.esNumero(latitud)) {
											String longitud = a_coordenada[2];
		
											if (Auxiliar.esNumero(longitud)) {
												boolean ok_coordenada = true;
												
												if (Auxiliar.tieneAlgo(PRCL_COORDENADAS)) {
													// ELIMINAR LA COORDENADA EXISTENTE:
													dbREDD.ejecutarSQL("DELETE FROM RED_PARCELA_COORDENADAS WHERE PRCR_PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+" AND PRCR_SECUENCIA="+secuencia);
													sql_coordenada = "INSERT INTO RED_PARCELA_COORDENADAS (PRCR_PRCL_CONSECUTIVO, PRCR_SECUENCIA, PRCR_LATITUD, PRCR_LONGITUD) VALUES ("+PRCL_CONSECUTIVO+","+secuencia+","+latitud+","+longitud+")";
													ok_coordenada = dbREDD.ejecutarSQL(sql_coordenada);
												}
												
												if (!ok_coordenada) {
													dbREDD.deshacerTransaccion();
													return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar las coordenadas de la parcela: " + dbREDD.ultimoError, usuario, metodo);
												}
												
												if (c==0) {
													verticeInicialFinal = longitud + "," + latitud;
												}
												
												a_vertices[Integer.parseInt(secuencia)-1] = longitud + "," + latitud;
												
												ok_coordenadas = ok_coordenadas && ok_coordenada;
											}
										}
										else {
											ok_coordenadas = false;
										}
									}
									else {
										ok_coordenadas = false;
									}
								}
								else {
									ok_coordenadas = false;
								}
							}
							
							a_vertices[n_vertices] = verticeInicialFinal;
							
							if (ok_coordenadas) {
								
								String str_vertices = "";
		
								str_vertices = Auxiliar.implotarArregloString(a_vertices, ",");
								
								sql_geometria = "UPDATE RED_PARCELA";
								sql_geometria += " SET PRCL_GEO = SDO_GEOMETRY( ";
								sql_geometria += " 2003, ";
								sql_geometria += " 4326, ";
								sql_geometria += " NULL, ";
								sql_geometria += " SDO_ELEM_INFO_ARRAY(1,1003,1), ";
								sql_geometria += " SDO_ORDINATE_ARRAY ("+str_vertices+") ";
								sql_geometria += " ) ";
								sql_geometria += " WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
								
								ok_geometria = dbREDD.ejecutarSQL(sql_geometria);
							}
						}
						else {
							if (n_vertices == 2) {
								String [] a_coordenada_min = a_coordenadas[0].split(",");
								String [] a_coordenada_max = a_coordenadas[1].split(",");
								
								if (a_coordenada_min.length == 2 && a_coordenada_max.length == 2) {
									String latitud_min = a_coordenada_min[1];
									String longitud_min = a_coordenada_min[2];
								
									String latitud_max = a_coordenada_max[1];
									String longitud_max = a_coordenada_max[2];
									
									sql_geometria = "UPDATE RED_PARCELA";
									sql_geometria += " SET PRCL_GEO = SDO_GEOMETRY( ";
									sql_geometria += " 2003, ";
									sql_geometria += " 4326, ";
									sql_geometria += " NULL, ";
									sql_geometria += " SDO_ELEM_INFO_ARRAY(1,1003,3), ";
									sql_geometria += " SDO_ORDINATE_ARRAY ("+longitud_min+","+latitud_min+", "+longitud_max+","+latitud_max+") ";
									sql_geometria += " ) ";
									sql_geometria += " WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
		
									ok_geometria = dbREDD.ejecutarSQL(sql_geometria);
								}
							}
							/*
							else if (n_vertices == 1 && Auxiliar.tieneAlgo(PRCL_AREA)) {
								String [] a_coordenada_centroide = a_coordenadas[0].split(",");
								
								if (a_coordenada_centroide.length == 3) {
									String latitud_centroide = a_coordenada_centroide[1];
									String longitud_centroide = a_coordenada_centroide[2];
									
									sql_geometria = "UPDATE RED_PARCELA";
									sql_geometria += " SET PRCL_GEO = SDO_GEOMETRY( ";
									sql_geometria += " 2003, ";
									sql_geometria += " 4326, ";
									sql_geometria += " NULL, ";
									sql_geometria += " SDO_ELEM_INFO_ARRAY(1,1003,3), ";
									sql_geometria += " SDO_ORDINATE_ARRAY ("+longitud_centroide+"-(0.01*(PRCL_AREA/4)),"+latitud_centroide+"-(0.01*(PRCL_AREA/4)), "+longitud_centroide+"+(0.01*(PRCL_AREA/4)),"+latitud_centroide+"+(0.01*(PRCL_AREA/4))) ";
									sql_geometria += " ) ";
									sql_geometria += " WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
									
									ok_geometria = dbREDD.ejecutarSQL(sql_geometria);
								}
							}
							*/
							else if (n_vertices == 1 && (Auxiliar.tieneAlgo(PRCL_LATITUD) && Auxiliar.tieneAlgo(PRCL_LONGITUD))) {
								String latitud_centroide = PRCL_LATITUD;
								String longitud_centroide = PRCL_LONGITUD;
								String area = "36100";
								String factor = "0.0000045";
								
								sql_geometria = "UPDATE RED_PARCELA";
								sql_geometria += " SET PRCL_GEO = SDO_GEOMETRY( ";
								sql_geometria += " 2003, ";
								sql_geometria += " 4326, ";
								sql_geometria += " NULL, ";
								sql_geometria += " SDO_ELEM_INFO_ARRAY(1,1003,3), ";
								//sql_geometria += " SDO_ORDINATE_ARRAY ("+longitud_centroide+"-(0.01*("+area+"/4)),"+latitud_centroide+"-(0.01*("+area+"/4)), "+longitud_centroide+"+(0.01*("+area+"/4)),"+latitud_centroide+"+(0.01*("+area+"/4))) ";
								//sql_geometria += " SDO_ORDINATE_ARRAY ("+longitud_centroide+"-("+factor+"*("+area+"/4)),"+latitud_centroide+"-("+factor+"*("+area+"/4)), "+longitud_centroide+"+("+factor+"*("+area+"/4)),"+latitud_centroide+"+("+factor+"*("+area+"/4))) ";
								sql_geometria += " SDO_ORDINATE_ARRAY ("+longitud_centroide+"-("+factor+"*95),"+latitud_centroide+"-("+factor+"*95), "+longitud_centroide+"+("+factor+"*95),"+latitud_centroide+"+("+factor+"*95)) ";
								sql_geometria += " ) ";
								sql_geometria += " WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
	
								ok_geometria = dbREDD.ejecutarSQL(sql_geometria);
							}
						}
					}
				}
				
				// ASIGNAR TIPO DE BOSQUE
				
				/*
				String sql_tipobosque = "UPDATE RED_PARCELA T1 " 
						+ " SET T1.PRCL_CONS_TIPOBOSQUE = " 
						+ " ( "
						+ " SELECT DISTINCT T2.TPBS_CONSECUTIVO "
						+ " FROM " 
						+ " RED_PARCELA T1, " 
						+ " ( "
						+ " SELECT DISTINCT TB.TPBS_CONSECUTIVO AS TPBS_CONSECUTIVO, " 
						+ " B.GEOM AS GEOM " 
						+ " FROM RED_TIPOBOSQUE TB " 
						+ " INNER JOIN RED_BOSQUES B ON LOWER(TB.TPBS_NOMBRE)=LOWER(B.HOLDRIDGE) "
						+ " ) T2 "
						+ " WHERE 1=1 "
						+ " AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ " AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 "
						+ " ) "
						+ " WHERE T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ " AND EXISTS " 
						+ " ( "
						+ " SELECT DISTINCT T2.TPBS_CONSECUTIVO "
						+ " FROM " 
						+ " RED_PARCELA T1, " 
						+ " ( "
						+ " SELECT DISTINCT TB.TPBS_CONSECUTIVO AS TPBS_CONSECUTIVO, " 
						+ " B.GEOM AS GEOM " 
						+ " FROM RED_TIPOBOSQUE TB " 
						+ " INNER JOIN RED_BOSQUES B ON LOWER(TB.TPBS_NOMBRE)=LOWER(B.HOLDRIDGE) "
						+ " ) T2 "
						+ " WHERE 1=1 "
						+ " AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ " AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 "
						+ " )";
				boolean ok_tipobosque = dbREDD.ejecutarSQL(sql_tipobosque);
				*/
				
				String sql_tipobosque = "SELECT T2.TPBS_CONSECUTIVO AS ID "
						+ "FROM " 
						+ "RED_PARCELA T1, " 
						+ "( " 
						  + "SELECT TB.TPBS_CONSECUTIVO AS TPBS_CONSECUTIVO, B.GEOM AS GEOM " 
						  + "FROM RED_TIPOBOSQUE TB " 
						  + "INNER JOIN RED_BOSQUES B ON LOWER(TB.TPBS_NOMBRE)=LOWER(B.HOLDRIDGE) "
						+ ") T2 "
						+ "WHERE 1=1 "
						+ "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ "AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ "AND ROWNUM=1 ";
				
				try {
					ResultSet rset = dbREDD.consultarBD(sql_tipobosque);
					
					if (rset != null) {
						
						if (rset.next()) {
							dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_CONS_TIPOBOSQUE=" + Auxiliar.nz(rset.getString("ID"), "NULL"));
							dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_TIPOBOSQUE=" + Auxiliar.nz(rset.getString("ID"), "NULL"));
						}
						rset.close();
					}
					else {
						System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
					}
				} catch (SQLException e) {
					System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
				} catch (Exception e) {
					System.out.println("Excepción: " + e.toString());
				}
				
				
				// ASIGCAR

				/*
				String sql_car = "UPDATE RED_PARCELA T1 " 
						+ "SET T1.PRCL_CONS_CAR = " 
						+ "( "
						+ "SELECT DISTINCT T2.ID_CAR "
						+ "FROM RED_PARCELA T1, (SELECT * FROM RED_CORPORACIONES) T2 "
						+ "WHERE 1=1 "
						+ "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ "AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 "
						+ ") "
						+ "WHERE 1=1 "
						//+ "T1.PRCL_CONS_CAR IS NULL AND T1.CAR_CDS IS NULL "
						+ "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ "AND EXISTS " 
						+ "( "
						+ "SELECT DISTINCT T2.ID_CAR "
						+ "FROM RED_PARCELA T1, (SELECT * FROM RED_CORPORACIONES) T2 "
						+ "WHERE 1=1 "
						+ "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ "AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 "
						+ ") ";
				boolean ok_car = dbREDD.ejecutarSQL(sql_car);
				*/

				String sql_car = "SELECT DISTINCT T2.ID_CAR AS ID "
						+ " FROM RED_PARCELA T1, (SELECT * FROM RED_CORPORACIONES) T2 "
						+ " WHERE 1=1 "
						+ " AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ " AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 ";

				try {
					ResultSet rset = dbREDD.consultarBD(sql_car);
					
					if (rset != null) {
						
						if (rset.next()) {
							dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_CONS_CAR=" + Auxiliar.nz(rset.getString("ID"), "NULL"));
						}
						rset.close();
					}
					else {
						System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
					}
				} catch (SQLException e) {
					System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
				} catch (Exception e) {
					System.out.println("Excepción: " + e.toString());
				}
				
				// ASIGNAR PNN

				/*
				String sql_pnn = "UPDATE RED_PARCELA T1 " 
						+ "MERGE "
						+ "INTO RED_PARCELA T1 "
						+ "USING " 
						+ "( "
						  + "SELECT DISTINCT T1.PRCL_CONSECUTIVO AS ID_T1, T2.OBJECTID AS ID_T2 "
						  + "FROM RED_PARCELA T1, (SELECT * FROM RED_PARQUES_NACIONALES) T2 "
						  + "WHERE 1=1 "
						  + "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						  + "AND T1.PRCL_CONSECUTIVO NOT IN " 
						  + "( "
						    + "SELECT T1.PRCL_CONSECUTIVO "
						    + "FROM RED_PARCELA T1, (SELECT * FROM RED_PARQUES_NACIONALES) T2 "
						    + "WHERE SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						    + "GROUP BY T1.PRCL_CONSECUTIVO "
						    + "HAVING COUNT(*) > 1 "
						  + ") "
						  + "AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ ") "
						+ "ON (T1.PRCL_CONSECUTIVO = ID_T1) "
						+ "WHEN MATCHED THEN "
						+ "UPDATE "
						+ "SET T1.PRCL_CONS_PNN = ID_T2 ";
				boolean ok_pnn = dbREDD.ejecutarSQL(sql_pnn);
				*/
				
				String sql_pnn = "SELECT DISTINCT T2.OBJECTID AS ID "
						+ "FROM RED_PARCELA T1, (SELECT * FROM RED_PARQUES_NACIONALES) T2 "
						+ "WHERE 1=1 "
						+ "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ "AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 ";
				
				try {
					ResultSet rset = dbREDD.consultarBD(sql_pnn);
					
					if (rset != null) {
						
						if (rset.next()) {
							dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_CONS_PNN=" + Auxiliar.nz(rset.getString("ID"), "NULL"));
						}
						rset.close();
					}
					else {
						System.out.println("Error SQL al asignar el PNN:"+dbREDD.ultimoError+"@@");
					}
				} catch (SQLException e) {
					System.out.println("Error SQL al asignar el PNN:"+dbREDD.ultimoError+"@@");
				} catch (Exception e) {
					System.out.println("Excepción: " + e.toString());
				}
				

				// ASIGNAR RESGUARDO INDIGENA
				
				String sql_resguardo_indigena = "SELECT DISTINCT T2.OBJECTID AS ID "
						+ "FROM RED_PARCELA T1, (SELECT * FROM RED_RESGUARDO_INDIGENA) T2 "
						+ "WHERE 1=1 "
						+ "AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") "
						+ "AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' "
						+ " AND ROWNUM=1 ";

				try {
					ResultSet rset = dbREDD.consultarBD(sql_resguardo_indigena);
					
					if (rset != null) {
						
						if (rset.next()) {
							dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_CONS_RESGUARDOINDIGENA=" + Auxiliar.nz(rset.getString("ID"), "NULL"));
						}
						rset.close();
					}
					else {
						System.out.println("Error SQL al asignar el resguardo indigena:"+dbREDD.ultimoError+"@@");
					}
				} catch (SQLException e) {
					System.out.println("Error SQL al asignar el resguardo indigena:"+dbREDD.ultimoError+"@@");
				} catch (Exception e) {
					System.out.println("Excepción: " + e.toString());
				}
				
				
				
				// ASIGNAR DEPARTAMENTO
				
				boolean ok_departamentos = true;

				if (PRCL_DEPARTAMENTO.equals("NULL") || !Auxiliar.tieneAlgo(PRCL_DEPARTAMENTO)) {
					String sql_departamento = "SELECT DISTINCT T2.CODIGO AS ID " + 
									" FROM RED_PARCELA T1, (SELECT * FROM RED_DEPTOS_SHAPE) T2  " +
									" WHERE 1=1  " +
									" AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") " + 
									" AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' " + 
									" AND ROWNUM=1";
					try {
						ResultSet rset = dbREDD.consultarBD(sql_departamento);
						
						if (rset != null) {
							
							if (rset.next()) {
								String dato_espacial = Auxiliar.nz(rset.getString("ID"), "");
								
								if (!dato_espacial.equals("")) {
									dbREDD.ejecutarSQL("DELETE FROM RED_DEPTO_PARCELA WHERE DPPR_CONS_PARCELA="+PRCL_CONSECUTIVO);
									ok_departamentos = dbREDD.ejecutarSQL("INSERT INTO RED_DEPTO_PARCELA (DPPR_CONS_DEPTO, DPPR_CONS_PARCELA) VALUES ("+dato_espacial+", "+PRCL_CONSECUTIVO+")");
									ok_departamentos = dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_DEPARTAMENTO="+dato_espacial+" WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+")");
									PRCL_DEPARTAMENTO = dato_espacial;
								}
							}
							rset.close();
						}
						else {
							System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
						}
					} catch (SQLException e) {
						System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
					} catch (Exception e) {
						System.out.println("Excepción: " + e.toString());
					}
				}
				/*
				else {
					dbREDD.ejecutarSQL("DELETE FROM RED_DEPTO_PARCELA WHERE DPPR_CONS_PARCELA="+PRCL_CONSECUTIVO);
					ok_departamentos = dbREDD.ejecutarSQL("INSERT INTO RED_DEPTO_PARCELA (DPPR_CONS_DEPTO, DPPR_CONS_PARCELA) VALUES ("+PRCL_DEPARTAMENTO+", "+PRCL_CONSECUTIVO+")");					
				}
				
				if (!ok_departamentos) {
					dbREDD.deshacerTransaccion();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el departamento de la parcela: " + dbREDD.ultimoError, usuario, metodo);
				}
				*/
				
				// ASIGNAR MUNICIPIO
				
				boolean ok_municipios = true;
				
				if (PRCL_MUNICIPIO.equals("NULL") || !Auxiliar.tieneAlgo(PRCL_MUNICIPIO)) {
					String sql_municipio = "SELECT DISTINCT T2.CODIGO AS ID " + 
									" FROM RED_PARCELA T1, (SELECT * FROM RED_MUNICIPIOS_SHAPE) T2  " +
									" WHERE 1=1  " +
									" AND T1.PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") " + 
									" AND SDO_RELATE(T2.GEOM, T1.PRCL_GEO, 'mask=ANYINTERACT') = 'TRUE' " + 
									" AND ROWNUM=1";
					try {
						ResultSet rset = dbREDD.consultarBD(sql_municipio);
						
						if (rset != null) {
							
							if (rset.next()) {
								String dato_espacial = Auxiliar.nz(rset.getString("ID"), "");
								
								if (!dato_espacial.equals("")) {
									dbREDD.ejecutarSQL("DELETE FROM RED_MUNICIPIO_PARCELA WHERE MNPR_PARCELA="+PRCL_CONSECUTIVO);
									ok_municipios = dbREDD.ejecutarSQL("INSERT INTO RED_MUNICIPIO_PARCELA (MNPR_MUNICIPIO, MNPR_PARCELA) VALUES ("+dato_espacial+", "+PRCL_CONSECUTIVO+")");
									ok_municipios = dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_MUNICIPIO="+dato_espacial+" WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+")");
									PRCL_MUNICIPIO = dato_espacial;
								}
							}
							rset.close();
						}
						else {
							System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
						}
					} catch (SQLException e) {
						System.out.println("Error SQL al actualizar tipo de bosque:"+dbREDD.ultimoError+"@@");
					} catch (Exception e) {
						System.out.println("Excepción: " + e.toString());
					}
				}
				/*
				else {
					dbREDD.ejecutarSQL("DELETE FROM RED_MUNICIPIO_PARCELA WHERE MNPR_PARCELA="+PRCL_CONSECUTIVO);
					ok_municipios = dbREDD.ejecutarSQL("INSERT INTO RED_MUNICIPIO_PARCELA (MNPR_MUNICIPIO, MNPR_PARCELA) VALUES ("+PRCL_MUNICIPIO+", "+PRCL_CONSECUTIVO+")");
				}

				if (!ok_municipios) {
					dbREDD.deshacerTransaccion();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el municipio de la parcela: " + dbREDD.ultimoError, usuario, metodo);
				}
				*/
				
				/*
				PrecisionModel  pm = new PrecisionModel((double)Math.pow(10, 1000000));
				
				GeometryFactory gf = new GeometryFactory(pm, 4326);
				OraReader or = new OraReader(gf);
				Geometry geo1 = or.read(_geom1);
		        Geometry geo2 = or.read(_geom2);
				IntersectionMatrix im = RelateOp.relate(geo1,geo2);	
				*/			

				
				if (!ok_coordenadas) {
					dbREDD.deshacerTransaccion();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar las coordenadas:" + dbREDD.ultimoError, usuario, metodo);
				}
				
				if (!ok_geometria) {
					dbREDD.deshacerTransaccion();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar la geometría:" + dbREDD.ultimoError, usuario, metodo);
				}
				
				boolean ok_sioperan = true;
				String str_mensaje_sioperan = "";
				
				String pc_OPERADOR = "A";
				
				int pn_PUNTO_MONITOREO_ID = Integer.parseInt(dbREDD.obtenerDato("SELECT PUNTO_MONITOREO_ID FROM SIOPERAN.SIO_PUNTOS_MONITOREOS WHERE NOMBRE='PRCL_CONSECUTIVO:" + PRCL_CONSECUTIVO + "'", "0"));
				if (pn_PUNTO_MONITOREO_ID == 0) {
					pc_OPERADOR = "C";
				}
				
				String pc_NOMBRE = "PRCL_CONSECUTIVO:" + PRCL_CONSECUTIVO;
				String pc_DESCRIPCION = PRCL_NOMBRE;
				int pn_ALTURA_REFERENCIA = 0;
				String pc_TIPO = "MMC";
			    String pc_ESTADO = PRCL_INCLUIR.equals("1") ? "ACT" : "SUS";
			    //String pd_FECHA_APLICACION = "'"+PRCL_FECHAFIN_APROXIMACION+"'";
			    Date pd_FECHA_APLICACION = formatter.parse(PRCL_FECHAFIN_APROXIMACION);
			    float pn_LATITUD = Float.parseFloat(PRCL_LATITUD);
			    float pn_LONGITUD = Float.parseFloat(PRCL_LONGITUD);
			    String pc_DIRECCION_LATITUD = "";
			    pc_DIRECCION_LATITUD = (pn_LATITUD > 0) ? "N" : "S";
			    String pc_DIRECCION_LONGITUD = "";
			    pc_DIRECCION_LONGITUD = (pn_LONGITUD > 0) ? "E" : "W";
			    Integer pn_ALTITUD = !Auxiliar.nz(PRCL_ALTITUD, "").equals("") ? Integer.parseInt(PRCL_ALTITUD) : null;
			    Float pn_AREA = !Auxiliar.nz(PRCL_AREA, "").equals("") ? Float.parseFloat(PRCL_AREA) : null;
			    int pn_ZONA = 0;
			    String pn_SUBZONA = "";
			    String pc_CORRIENTE = "";
			    int pn_FGDA_ID = 3;
			    int pn_DIVIPOLA_ID = Integer.parseInt(dbREDD.obtenerDato("SELECT DIVIPOLA_ID FROM SIOPERAN.SIO_DIVIPOLAS WHERE MUNICIPIO=" + PRCL_MUNICIPIO, "0"));
			    int pn_PUNTO_MONITOREO_PADRE_ID = 0;

			    String modo_sioperan = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='modo_sioperan'", "PROCEDIMIENTO");

				String sql_sioperan = "";
				
				try {
					if (modo_sioperan.equals("PROCEDIMIENTO")) {
						CallableStatement sioperar = dbREDD.conn .prepareCall("{call SIOPERAN.SIOP$PUNTOS_MONITOREOS(" +
								"?," + //pc_OPERADOR    						VARCHAR2
								"?," + //,pn_PUNTO_MONITOREO_ID    			NUMBER
								"?," + //, pc_NOMBRE                  		VARCHAR2   	DEFAULT NULL
								"?," + //, pc_DESCRIPCION                	VARCHAR2   	DEFAULT NULL
								"?," + //, pn_ALTURA_REFERENCIA          	NUMBER   	DEFAULT NULL
								"?," + //, pc_TIPO                 			VARCHAR2   	DEFAULT NULL
							    "?," + //, pc_ESTADO                     	VARCHAR2   	DEFAULT NULL
							    "?," + //, pd_FECHA_APLICACION             	DATE   		DEFAULT NULL
							    "?," + //, pn_LATITUD             			NUMBER   	DEFAULT NULL
							    "?," + //, pn_LONGITUD             			NUMBER   	DEFAULT NULL
							    "?," + //, pn_GRADOS_LATITUD             	NUMBER   	DEFAULT NULL
							    "?," + //, pn_MINUTOS_LATITUD             	NUMBER   	DEFAULT NULL
							    "?," + //, pn_SEGUNDOS_LATITUD             	NUMBER   	DEFAULT NULL
							    "?," + //, pc_DIRECCION_LATITUD             	VARCHAR2   	DEFAULT NULL
							    "?," + //, pn_GRADOS_LONGITUD             	NUMBER   	DEFAULT NULL
							    "?," + //, pn_MINUTOS_LONGITUD             	NUMBER   	DEFAULT NULL
							    "?," + //, pn_SEGUNDOS_LONGITUD             	NUMBER   	DEFAULT NULL
							    "?," + //, pc_DIRECCION_LONGITUD             VARCHAR2   	DEFAULT NULL
							    "?," + //, pn_ALTITUD             			NUMBER   	DEFAULT NULL
							    "?," + //, pn_AREA             				NUMBER   	DEFAULT NULL
							    "?," + //, pn_ZONA             				NUMBER   	DEFAULT NULL
							    "?," + //, pn_SUBZONA             			NUMBER   	DEFAULT NULL
							    "?," + //, pc_CORRIENTE             			VARCHAR2   	DEFAULT NULL
							    "?," + //, pn_FGDA_ID             			NUMBER		DEFAULT NULL
							    "?," + //, pn_DIVIPOLA_ID             		NUMBER		DEFAULT NULL
							    "?" + //, pn_PUNTO_MONITOREO_PADRE_ID   	NUMBER		DEFAULT NULL
								")}");
						
						sioperar.setString("pc_OPERADOR", pc_OPERADOR);
						if (pn_PUNTO_MONITOREO_ID != 0) { 
							sioperar.setInt("pn_PUNTO_MONITOREO_ID", pn_PUNTO_MONITOREO_ID);
						}
						else {
							sioperar.setNull("pn_PUNTO_MONITOREO_ID", OracleTypes.NULL);
						}
						sioperar.setString("pc_NOMBRE", pc_NOMBRE);
						sioperar.setString("pc_DESCRIPCION", pc_DESCRIPCION);
						sioperar.setInt("pn_ALTURA_REFERENCIA", pn_ALTURA_REFERENCIA);
						sioperar.setString("pc_TIPO", pc_TIPO);
						sioperar.setString("pc_ESTADO", pc_ESTADO);
						sioperar.setDate("pd_FECHA_APLICACION", new java.sql.Date(pd_FECHA_APLICACION.getTime()));
						sioperar.setFloat("pn_LATITUD", pn_LATITUD);
						sioperar.setFloat("pn_LONGITUD", pn_LONGITUD);
						sioperar.setNull("pn_GRADOS_LATITUD", OracleTypes.NULL);
						sioperar.setNull("pn_MINUTOS_LATITUD", OracleTypes.NULL);
						sioperar.setNull("pn_SEGUNDOS_LATITUD", OracleTypes.NULL);
						sioperar.setString("pc_DIRECCION_LATITUD", pc_DIRECCION_LATITUD);
						sioperar.setNull("pn_GRADOS_LONGITUD", OracleTypes.NULL);
						sioperar.setNull("pn_MINUTOS_LONGITUD", OracleTypes.NULL);
						sioperar.setNull("pn_SEGUNDOS_LONGITUD", OracleTypes.NULL);
						sioperar.setString("pc_DIRECCION_LONGITUD", pc_DIRECCION_LONGITUD);
						sioperar.setFloat("pn_ALTITUD", pn_ALTITUD);
						sioperar.setFloat("pn_AREA", pn_AREA);
						sioperar.setInt("pn_ZONA", pn_ZONA);
						sioperar.setString("pn_SUBZONA", pn_SUBZONA);
						sioperar.setString("pc_CORRIENTE", pc_CORRIENTE);
						sioperar.setInt("pn_FGDA_ID", pn_FGDA_ID);
						if (pn_DIVIPOLA_ID != 0) {
							sioperar.setInt("pn_DIVIPOLA_ID", pn_DIVIPOLA_ID);
						}
						else {
							sioperar.setNull("pn_DIVIPOLA_ID", OracleTypes.NULL);							
						}
						sioperar.setNull("pn_PUNTO_MONITOREO_PADRE_ID", OracleTypes.NULL);
	
						sioperar.execute();
						sioperar.close();			
					}
					else {
						if (pn_PUNTO_MONITOREO_ID != 0) { 
							sql_sioperan = "UPDATE SIOPERAN.SIO_PUNTOS_MONITOREOS SET ";
							sql_sioperan += " NOMBRE='"+pc_NOMBRE+"',  ";
							sql_sioperan += " DESCRIPCION='"+PRCL_NOMBRE+"',  ";
							sql_sioperan += " FECHA_MODIFICACION=SYSDATE,  ";
							sql_sioperan += " FECHA_APLICACION=TO_DATE('"+PRCL_FECHAFIN_APROXIMACION+"', 'YYYY-MM-DD'),  ";
							sql_sioperan += " LATITUD="+PRCL_LATITUD+",  ";
							sql_sioperan += " LONGITUD="+PRCL_LONGITUD+",  ";
							sql_sioperan += " GRADOS_LATITUD=ABS(TRUNC("+PRCL_LATITUD+",0)),  ";
							sql_sioperan += " MINUTOS_LATITUD=TRUNC((ABS("+PRCL_LATITUD+") -ABS(TRUNC("+PRCL_LATITUD+",0))) * 60,0),  ";
							sql_sioperan += " SEGUNDOS_LATITUD=((ABS("+PRCL_LATITUD+") -ABS(TRUNC("+PRCL_LATITUD+",0))) * 60) -TRUNC((ABS("+PRCL_LATITUD+") -ABS(TRUNC("+PRCL_LATITUD+",0))) * 60,0),  ";
							sql_sioperan += " DIRECCION_LATITUD='"+pc_DIRECCION_LATITUD+"',  ";
							sql_sioperan += " GRADOS_LONGITUD=ABS(TRUNC("+PRCL_LONGITUD+",0)),  ";
							sql_sioperan += " MINUTOS_LONGITUD=TRUNC((ABS("+PRCL_LONGITUD+") -ABS(TRUNC("+PRCL_LONGITUD+",0))) * 60,0),  ";
							sql_sioperan += " SEGUNDOS_LONGITUD=((ABS("+PRCL_LONGITUD+") -ABS(TRUNC("+PRCL_LONGITUD+",0))) * 60) -TRUNC((ABS("+PRCL_LONGITUD+") -ABS(TRUNC("+PRCL_LONGITUD+",0))) * 60,0),  ";
							sql_sioperan += " DIRECCION_LONGITUD='"+pc_DIRECCION_LONGITUD+"',  ";
							
							/*
				              pb$reg.GRADOS_LATITUD := ABS(TRUNC(pb$reg.LATITUD,0));
				              VC_VALOR_PASO := (ABS(pb$reg.LATITUD) - pb$reg.GRADOS_LATITUD) * 60;
				              pb$reg.MINUTOS_LATITUD := TRUNC(VC_VALOR_PASO,0);
				              VC_VALOR_PASO := (VC_VALOR_PASO - pb$reg.MINUTOS_LATITUD) * 60;
				              pb$reg.SEGUNDOS_LATITUD := VC_VALOR_PASO;
				        --CALCULO DE LA LONGITUD
				              IF pb$reg.GRADOS_LONGITUD >=0 THEN  --pb$reg.GRADOS_LATITUD >1 THEN  CORREGIDO POR LINEA ANTERIOR
				                   --pb$reg.DIRECCION_LATITUD := 'E';  --SE CORRIGE POR LA SIGUIENTE LINEA DE CÓDIGO.
				                  -- pb$reg.DIRECCION_LONGITUD := 'E';
				              --ELSE
				                 --pb$reg.DIRECCION_LATITUD := 'E'; --SE CORRIGE POR LA SIGUIENTE LINEA DE CÓDIGO.
				                    pb$reg.DIRECCION_LONGITUD := 'W';
				              END IF;
				              pb$reg.GRADOS_LONGITUD := ABS(TRUNC(pb$reg.LONGITUD,0));
				              VC_VALOR_PASO := (ABS(pb$reg.LONGITUD) - pb$reg.GRADOS_LONGITUD) * 60;
				              pb$reg.MINUTOS_LONGITUD := TRUNC(VC_VALOR_PASO,0);
				              VC_VALOR_PASO := (VC_VALOR_PASO - pb$reg.MINUTOS_LONGITUD) * 60;
				              pb$reg.SEGUNDOS_LONGITUD := VC_VALOR_PASO;
				              */

							sql_sioperan += " ALTITUD="+PRCL_ALTITUD+",  ";
							sql_sioperan += " AREA="+PRCL_AREA+",  ";
							if (pn_DIVIPOLA_ID != 0) {
								sql_sioperan += " DIVIPOLA_ID="+pn_DIVIPOLA_ID+",  ";
							}
							else {
								sql_sioperan += " DIVIPOLA_ID=NULL,  ";
							}
							sql_sioperan += " FGDA_ID=3,  ";
							sql_sioperan += " ESTADO='"+pc_ESTADO+"'  ";
							sql_sioperan += " WHERE PUNTO_MONITOREO_ID="+pn_PUNTO_MONITOREO_ID+"  ";
						}
						else {
							sql_sioperan = "INSERT INTO SIOPERAN.SIO_PUNTOS_MONITOREOS (";
							sql_sioperan += "NOMBRE,";
							sql_sioperan += "DESCRIPCION,";
							sql_sioperan += "TIPO,";
							sql_sioperan += "ESTADO,";
							sql_sioperan += "FECHA_APLICACION,";
							sql_sioperan += "LATITUD,";
							sql_sioperan += "LONGITUD,";
							sql_sioperan += "GRADOS_LATITUD,";
							sql_sioperan += "MINUTOS_LATITUD,";
							sql_sioperan += "SEGUNDOS_LATITUD,";
							sql_sioperan += "DIRECCION_LATITUD,";
							sql_sioperan += "GRADOS_LONGITUD,";
							sql_sioperan += "MINUTOS_LONGITUD,";
							sql_sioperan += "SEGUNDOS_LONGITUD,";
							sql_sioperan += "DIRECCION_LONGITUD,";
							sql_sioperan += "ALTITUD,";
							sql_sioperan += "AREA,";
							sql_sioperan += "FGDA_ID,";
							sql_sioperan += "DIVIPOLA_ID,";
							sql_sioperan += "PUNTO_MONITOREO_PADRE_ID,";
							sql_sioperan += "FECHA_CREACION,";
							sql_sioperan += "FECHA_MODIFICACION";
							sql_sioperan += ") VALUES (";
							sql_sioperan += "'"+pc_NOMBRE+"',";
							sql_sioperan += "'"+PRCL_NOMBRE+"',";
							sql_sioperan += "'MBC',";
							sql_sioperan += "'"+pc_ESTADO+"',";
							sql_sioperan += "TO_DATE('"+PRCL_FECHAFIN_APROXIMACION+"', 'YYYY-MM-DD'),";
							sql_sioperan += ""+PRCL_LATITUD+",";
							sql_sioperan += ""+PRCL_LONGITUD+",";
							sql_sioperan += "ABS(TRUNC("+PRCL_LATITUD+",0)),";
							sql_sioperan += "TRUNC((ABS("+PRCL_LATITUD+") -ABS(TRUNC("+PRCL_LATITUD+",0))) * 60,0),";
							sql_sioperan += "((ABS("+PRCL_LATITUD+") -ABS(TRUNC("+PRCL_LATITUD+",0))) * 60) -TRUNC((ABS("+PRCL_LATITUD+") -ABS(TRUNC("+PRCL_LATITUD+",0))) * 60,0),";
							sql_sioperan += "'"+pc_DIRECCION_LATITUD+"',";
							sql_sioperan += "ABS(TRUNC("+PRCL_LONGITUD+",0)),";
							sql_sioperan += "TRUNC((ABS("+PRCL_LONGITUD+") -ABS(TRUNC("+PRCL_LONGITUD+",0))) * 60,0),";
							sql_sioperan += "((ABS("+PRCL_LONGITUD+") -ABS(TRUNC("+PRCL_LONGITUD+",0))) * 60) -TRUNC((ABS("+PRCL_LONGITUD+") -ABS(TRUNC("+PRCL_LONGITUD+",0))) * 60,0),";
							sql_sioperan += "'"+pc_DIRECCION_LONGITUD+"',";
							sql_sioperan += ""+PRCL_ALTITUD+",";
							sql_sioperan += ""+PRCL_AREA+",";
							sql_sioperan += "3,";
							if (pn_DIVIPOLA_ID != 0) {
								sql_sioperan += " "+pn_DIVIPOLA_ID+",  ";
							}
							else {
								sql_sioperan += " NULL,  ";
							}
							sql_sioperan += "NULL,";
							sql_sioperan += "SYSDATE,";
							sql_sioperan += "SYSDATE";
							sql_sioperan += ")";
						}

						ok_sioperan = dbREDD.ejecutarSQL(sql_sioperan);
					 }
					
					if (ok_sioperan) {
						str_mensaje_sioperan = Auxiliar.mensaje("confirmacion", "Parcela guardada ("+pc_OPERADOR+") en SIOPERAN (PUNTO_MONITOREO_ID:"+pn_PUNTO_MONITOREO_ID+")", id_usuario, metodo);
					}
					else {
						str_mensaje_sioperan = Auxiliar.mensaje("error", "Problemas al guardar ("+pc_OPERADOR+") la parcela en SIOPERAN (PUNTO_MONITOREO_ID:"+pn_PUNTO_MONITOREO_ID+"): ["+sql_sioperan+"]", id_usuario, metodo);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					ok_sioperan = false;
					str_mensaje_sioperan = Auxiliar.mensaje("error", "No se pudo guardar ("+pc_OPERADOR+") la parcela en SIOPERAN (PUNTO_MONITOREO_ID:"+pn_PUNTO_MONITOREO_ID+"): " + e.toString() + " ["+sql_sioperan+"]", id_usuario, metodo);
				}				
				
		        try {
					if (update) {
						id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "");
						if (id_usuario.equals(id_creador)) {
							if (sec.tienePermiso(id_usuario, "12")) {
								sec.registrarTransaccion(request, 12, PRCL_CONSECUTIVO, sql_guardar, ok_guardar);
							}
						}
						else {
							if (sec.tienePermiso(id_usuario, "13")) {
								sec.registrarTransaccion(request, 13, PRCL_CONSECUTIVO, sql_guardar, ok_guardar);
							}
						}
					}
					else {
						if (sec.tienePermiso(id_usuario, "13")) {
							sec.registrarTransaccion(request, 9, PRCL_CONSECUTIVO, sql_guardar, ok_guardar);
						}
					}

					dbREDD.cometerTransaccion();
					resultado = Auxiliar.nz(PRCL_CONSECUTIVO, "")+"-=-" + str_mensaje_sioperan + Auxiliar.mensaje("confirmacion", "Parcela "+PRCL_CONSECUTIVO+" guardada.", usuario, metodo);
					
					dbREDD.establecerAutoCometer(true);
		        }
		        catch (Exception e) {
		    		dbREDD.desconectarse();
		        	return "0-=-" + Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
		        }

			}
			catch (Exception e) {
				dbREDD.deshacerTransaccion();
				return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar la parcela:" + dbREDD.ultimoError, usuario, metodo);
			}
		} 
		catch (Exception e) {
			resultado = "0-=-" + "Problema al editar la parcela: " + e.toString();
			e.printStackTrace();
		}
		
		if (novedades.length() > 0) resultado = "0-=-" + "Novedades: " + novedades + resultado;
				
		dbREDD.desconectarse();
		
		return resultado;
	}
	
	/**
	 * Elimina una parcela
	 * 
	 * @param PRCL_CONSECUTIVO
	 * @return String r con el resultado
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public String eliminar(	String PRCL_CONSECUTIVO, HttpServletRequest request) 
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
		
		pe = Auxiliar.traducir("General.Por_favor_especifique", idioma, "Por favor especifique" + "..");
		
		
		// VERIFICAR PARAMETROS
		
		t = Auxiliar.traducir("PRCL_CONSECUTIVO", idioma, "el consecutivo REDD de la parcela" + "..");
		if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) { 
			dbREDD.desconectarse();
			return "0-=-" + pe + " " + t; 
		}
		
		// REALIZAR OPERACION
		
		String sql_eliminar = "";
		
		try 
		{
			sql_eliminar = "DELETE FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO;
			
			id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "");
			if (id_usuario.equals(id_creador)) {
				if (sec.tienePermiso(id_usuario, "227") || sec.tienePermiso(id_usuario, "228")) {
					privilegio = true;
				}
			}
			else {
				if (sec.tienePermiso(id_usuario, "228")) {
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
					if (!dbREDD.ejecutarSQL(sql_eliminar)) {
						dbREDD.desconectarse();
						return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar eliminar la parcela [" + sql_eliminar + "]:" + dbREDD.ultimoError, usuario, metodo);									
					}
					else {
						ok_eliminar = true;
						
						// ELIMINAR DE SIOPERAN
						
						boolean ok_sioperan = true;
						String str_mensaje_sioperan = "";
						
						String pc_OPERADOR = "E";
						
						int pn_PUNTO_MONITOREO_ID = 0;
						
						pn_PUNTO_MONITOREO_ID = Integer.parseInt(dbREDD.obtenerDato("SELECT PUNTO_MONITOREO_ID FROM SIOPERAN.SIO_PUNTOS_MONITOREOS WHERE NOMBRE='PRCL_CONSECUTIVO:" + PRCL_CONSECUTIVO + "'", "0"));
						if (pn_PUNTO_MONITOREO_ID != 0) {
							String pc_NOMBRE = "PRCL_CONSECUTIVO:" + PRCL_CONSECUTIVO;
							String pc_DESCRIPCION = "";
							int pn_ALTURA_REFERENCIA = 0;
							String pc_TIPO = "MBC";
						    String pc_ESTADO = "SUS";
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						    Date pd_FECHA_APLICACION = formatter.parse("'1900-01-01'");
						    float pn_LATITUD = 0;
						    float pn_LONGITUD = 0;
						    String pc_DIRECCION_LATITUD = "";
						    pc_DIRECCION_LATITUD = (pn_LATITUD > 0) ? "N" : "S";
						    String pc_DIRECCION_LONGITUD = "";
						    pc_DIRECCION_LONGITUD = (pn_LONGITUD > 0) ? "E" : "W";
						    Integer pn_ALTITUD = 0;
						    Float pn_AREA = 0f;
						    int pn_ZONA = 0;
						    String pn_SUBZONA = "";
						    String pc_CORRIENTE = "";
						    int pn_FGDA_ID = 3;
						    int pn_DIVIPOLA_ID = 0;
						    int pn_PUNTO_MONITOREO_PADRE_ID = 0;
	
							try {
								CallableStatement sioperar = dbREDD.conn .prepareCall("{call SIOPERAN.SIOP$PUNTOS_MONITOREOS(" +
										"?," + //pc_OPERADOR    						VARCHAR2
										"?," + //,pn_PUNTO_MONITOREO_ID    			NUMBER
										"?," + //, pc_NOMBRE                  		VARCHAR2   	DEFAULT NULL
										"?," + //, pc_DESCRIPCION                	VARCHAR2   	DEFAULT NULL
										"?," + //, pn_ALTURA_REFERENCIA          	NUMBER   	DEFAULT NULL
										"?," + //, pc_TIPO                 			VARCHAR2   	DEFAULT NULL
									    "?," + //, pc_ESTADO                     	VARCHAR2   	DEFAULT NULL
									    "?," + //, pd_FECHA_APLICACION             	DATE   		DEFAULT NULL
									    "?," + //, pn_LATITUD             			NUMBER   	DEFAULT NULL
									    "?," + //, pn_LONGITUD             			NUMBER   	DEFAULT NULL
									    "?," + //, pn_GRADOS_LATITUD             	NUMBER   	DEFAULT NULL
									    "?," + //, pn_MINUTOS_LATITUD             	NUMBER   	DEFAULT NULL
									    "?," + //, pn_SEGUNDOS_LATITUD             	NUMBER   	DEFAULT NULL
									    "?," + //, pc_DIRECCION_LATITUD             	VARCHAR2   	DEFAULT NULL
									    "?," + //, pn_GRADOS_LONGITUD             	NUMBER   	DEFAULT NULL
									    "?," + //, pn_MINUTOS_LONGITUD             	NUMBER   	DEFAULT NULL
									    "?," + //, pn_SEGUNDOS_LONGITUD             	NUMBER   	DEFAULT NULL
									    "?," + //, pc_DIRECCION_LONGITUD             VARCHAR2   	DEFAULT NULL
									    "?," + //, pn_ALTITUD             			NUMBER   	DEFAULT NULL
									    "?," + //, pn_AREA             				NUMBER   	DEFAULT NULL
									    "?," + //, pn_ZONA             				NUMBER   	DEFAULT NULL
									    "?," + //, pn_SUBZONA             			NUMBER   	DEFAULT NULL
									    "?," + //, pc_CORRIENTE             			VARCHAR2   	DEFAULT NULL
									    "?," + //, pn_FGDA_ID             			NUMBER		DEFAULT NULL
									    "?," + //, pn_DIVIPOLA_ID             		NUMBER		DEFAULT NULL
									    "?" + //, pn_PUNTO_MONITOREO_PADRE_ID   	NUMBER		DEFAULT NULL
										")}");
								
								sioperar.setString("pc_OPERADOR", pc_OPERADOR);
								sioperar.setInt("pn_PUNTO_MONITOREO_ID", pn_PUNTO_MONITOREO_ID);
								sioperar.setString("pc_NOMBRE", pc_NOMBRE);
								sioperar.setString("pc_DESCRIPCION", pc_DESCRIPCION);
								sioperar.setInt("pn_ALTURA_REFERENCIA", pn_ALTURA_REFERENCIA);
								sioperar.setString("pc_TIPO", pc_TIPO);
								sioperar.setString("pc_ESTADO", pc_ESTADO);
								sioperar.setDate("pd_FECHA_APLICACION", new java.sql.Date(pd_FECHA_APLICACION.getTime()));
								sioperar.setFloat("pn_LATITUD", pn_LATITUD);
								sioperar.setFloat("pn_LONGITUD", pn_LONGITUD);
								sioperar.setNull("pn_GRADOS_LATITUD", OracleTypes.NULL);
								sioperar.setNull("pn_MINUTOS_LATITUD", OracleTypes.NULL);
								sioperar.setNull("pn_SEGUNDOS_LATITUD", OracleTypes.NULL);
								sioperar.setString("pc_DIRECCION_LATITUD", pc_DIRECCION_LATITUD);
								sioperar.setNull("pn_GRADOS_LONGITUD", OracleTypes.NULL);
								sioperar.setNull("pn_MINUTOS_LONGITUD", OracleTypes.NULL);
								sioperar.setNull("pn_SEGUNDOS_LONGITUD", OracleTypes.NULL);
								sioperar.setString("pc_DIRECCION_LONGITUD", pc_DIRECCION_LONGITUD);
								sioperar.setFloat("pn_ALTITUD", pn_ALTITUD);
								sioperar.setFloat("pn_AREA", pn_AREA);
								sioperar.setInt("pn_ZONA", pn_ZONA);
								sioperar.setString("pn_SUBZONA", pn_SUBZONA);
								sioperar.setString("pc_CORRIENTE", pc_CORRIENTE);
								sioperar.setInt("pn_FGDA_ID", pn_FGDA_ID);
								if (pn_DIVIPOLA_ID != 0) {
									sioperar.setInt("pn_DIVIPOLA_ID", pn_DIVIPOLA_ID);
								}
								else {
									sioperar.setNull("pn_DIVIPOLA_ID", OracleTypes.NULL);							
								}
								sioperar.setNull("pn_PUNTO_MONITOREO_PADRE_ID", pn_PUNTO_MONITOREO_PADRE_ID);
	
								sioperar.execute();
								sioperar.close();
								str_mensaje_sioperan = Auxiliar.mensaje("confirmacion", "Parcela eliminada de SIOPERAN (PUNTO_MONITOREO_ID:"+pn_PUNTO_MONITOREO_ID+")", id_usuario, metodo);								
							}
							catch (Exception e) {
								e.printStackTrace();
								ok_sioperan = false;
								str_mensaje_sioperan = Auxiliar.mensaje("error", "No se pudo eliminar la parcela de SIOPERAN (PUNTO_MONITOREO_ID:"+pn_PUNTO_MONITOREO_ID+"): " + e.toString(), id_usuario, metodo);
							}				
						}						

						if (!ok_sioperan) {
							dbREDD.desconectarse();
							return "0-=-" + str_mensaje_sioperan;									
						}
						
						if (id_usuario.equals(id_creador)) {
							sec.registrarTransaccion(request, 227, PRCL_CONSECUTIVO, sql_eliminar, ok_eliminar);
						}
						else {
							sec.registrarTransaccion(request, 228, PRCL_CONSECUTIVO, sql_eliminar, ok_eliminar);
						}

						dbREDD.cometerTransaccion();
						resultado = "1-=-" + str_mensaje_sioperan + Auxiliar.mensaje("confirmacion", "Parcela eliminada.", usuario, metodo);
					}
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo una excepción al intentar eliminar la parcela [" + sql_eliminar + "]:" + dbREDD.ultimoError, usuario, metodo);									
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
				return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar eliminar el individuo [" + sql_eliminar + "]:" + dbREDD.ultimoError, usuario, metodo);
			}
		} 
		catch (Exception e) {
			resultado = "Problema al eliminar el Individuo: " + e.toString();
			e.printStackTrace();
		}
		
		if (novedades.length() > 0) resultado += "Novedades: " + novedades;
		
		dbREDD.desconectarse();
		
		return resultado;
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
	public String importarImagenes(String ruta_archivo, String prcl_id_importacion, HttpSession session)
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
		//String commando_descompresion = "7za e "+ruta_archivo+" -y -o"+getServletContext().getRealPath("") + File.separator + "imagenes_parcelas/" + indv_id_importacion;
		String carpeta_imagenes_parcelas = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_parcelas'", "");
		if (!Auxiliar.tieneAlgo(carpeta_imagenes_parcelas)) {
			carpeta_imagenes_parcelas = getServletContext().getRealPath("") + File.separator + "imagenes_parcelas";
		}

		String commando_descompresion = "7za e "+ruta_archivo+" -y -o"+carpeta_imagenes_parcelas + "/" + prcl_id_importacion;
		
		try {
			String str_resultado_descompresion = Auxiliar.commander(commando_descompresion, commando_descompresion, session);
	
			String[] a_resultado_descompresion = str_resultado_descompresion.split("-=-");
			
			if (a_resultado_descompresion[0] != "0")
			{
				t = Auxiliar.traducir(yo+"Imagenes_Extraidas", idioma, "Imágenes extraidas en la carpeta de imágenes de la parcela identificada con consecutivo " + "..");
				r = Auxiliar.mensaje("confirmacion", t + prcl_id_importacion, usuario, metodo);
				r += a_resultado_descompresion[1];
			}
			else {
				r = Auxiliar.mensaje("error", "No se pudieron extraer las imágenes en la carpeta de imágenes de la parcela identificada con consecutivo " + prcl_id_importacion + ". Comando de descompresión:" + commando_descompresion, usuario, metodo);
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
