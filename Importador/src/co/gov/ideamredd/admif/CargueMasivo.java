// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

import co.gov.ideamredd.lenguaje.LenguajeI18N;

/**
 * Importar masivamente información sobre parcelas, sus individuos y sus imágenes.
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com 
 *
 */
@SuppressWarnings("serial")
public class CargueMasivo extends HttpServlet {

	String s = "\t";
	
	private static final int MAX_VARCHAR2 = 3999;

	public static String yo = "CargueMasivo.";
	public static String charset = "ISO-8859-1";
	public static String css = "css/estilos.css";

	String GLOBAL_CONSECUTIVO_ARCHIVO = "";
	String GLOBAL_PRCL_CONSECUTIVO = "";
	String GLOBAL_INDV_CONSECUTIVO = "";
	
    // Definir carpeta para subir los archivos de importación
    private static String UPLOAD_DIRECTORY = "upload";

	
	String RED_username = "";
	String RED_password = "";
	String RED_host = "";
	String RED_port = "";
	String RED_sid = "";
	
	String encriptar_usuario = "";
	String llave_encripcion = "";
	
	/**
	 * Método mudo para inicializar la clase
	 * A partir de la variable config se obtienen los parámetros de conexión
	 * a las bases de datos.
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
	 * Constructor 
	 */
	public CargueMasivo() {
	}


	/**
	 * Método que traduce a post el get si recibió un request get
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		doPost(request, response);
	}
	
	public ResourceBundle setLenguaje(String p_idioma, LenguajeI18N L) {
		String metodo = yo + "setLenguaje";

		if (p_idioma.equals("es")) {
			L.setLenguaje("es");
			L.setPais("CO");
		}
		else {
			L.setLenguaje("en");
			L.setPais("US");
		}

		// Obtener diccionario del idioma elegido
		return L.obtenerMensajeIdioma();
	}
	

	/**
	 * Método que procesa el request de POST que recibió la clase
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		String metodo = yo + "doPost";

		String retorno = "";
		String target = "";

		String usuario_recibido = "";
		String usuario = "";
		String idioma = "";
		String clave = "";
		
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
		
		request.setAttribute("datos_sesion", Auxiliar.mensajeImpersonal("nota", "Usuario: " + Auxiliar.nz(usuario, "") + ", Idioma: " + Auxiliar.nz(idioma, "")));

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
		else if (accion.equals("subir_archivo"))
		{
			target = "/registro_archivo.jsp";
			String [] a_resultado = null;
			String str_resultado = "";
			String consecutivo_archivo = "";

			try {
				if (sec.tienePermiso(usuario, "38")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Subiendo archivo...");
					str_resultado = subirArchivo(request, session);
					a_resultado = str_resultado.split("-=-");
				
					if (a_resultado.length > 1) {
						consecutivo_archivo = a_resultado[0];
						retorno = a_resultado[1];
					}
				}
				else {
					sec.registrarTransaccion(request, 38, "", "permisos", false);
				}
			} catch (SQLException e) {
				retorno = Auxiliar.mensaje("error", "Excepción de SQL durante el llamado a subirArchivo(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 38, "", "excepcion SQL:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción general durante el llamado a subirArchivo(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 38, "", "excepcion:"+e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
			if (Auxiliar.tieneAlgo(consecutivo_archivo)) {
				if (Auxiliar.tieneAlgo(GLOBAL_PRCL_CONSECUTIVO)) {
					target = "/procesar_archivo.jsp";
					
					try {
						if (sec.tienePermiso(usuario, "44")) {
							System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Procesando archivo...");
							retorno = procesarArchivo(consecutivo_archivo, GLOBAL_PRCL_CONSECUTIVO, "", request);
						}
						else {
							retorno += Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 44, "", "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a procesarArchivo(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 44, "", "excepcion:" + e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
					
					request.setAttribute("PRCL_CONSECUTIVO", GLOBAL_PRCL_CONSECUTIVO);
				}
				else if (Auxiliar.tieneAlgo(GLOBAL_INDV_CONSECUTIVO)) {
					target = "/procesar_archivo.jsp";
					try {
						if (sec.tienePermiso(usuario, "44")) {
							System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Procesando archivo...");
							retorno += procesarArchivo(consecutivo_archivo, "", GLOBAL_INDV_CONSECUTIVO, request);
						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 44, "", "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a procesarArchivo(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 44, "", "excepcion:" + e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
					
					request.setAttribute("INDV_CONSECUTIVO", GLOBAL_INDV_CONSECUTIVO);
				}
				else {	
				}
			}
		}
		else if (accion.equals("listar_archivos"))
		{
			target = "/consulta_archivos.jsp";
			
			try {
				if (sec.tienePermiso(usuario, "39") || sec.tienePermiso(usuario, "40")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Consultando archivos...");
					String resultado = consultarArchivos(Auxiliar.nz(request.getParameter("f_consecutivo"), ""), 
							Auxiliar.nz(request.getParameter("f_nombre"), ""), 
							Auxiliar.nz(request.getParameter("f_tipo"), ""), 
							Auxiliar.nz(request.getParameter("f_descripcion"), ""), 
							Auxiliar.nz(request.getParameter("f_estado"), ""), 
							Auxiliar.nz(request.getParameter("f_usuario"), ""), request);
					String [] a_resultado = resultado.split("-=-");
					request.setAttribute("n_archivos", a_resultado[0]);
					retorno = a_resultado[1];
				}
				else {
					request.setAttribute("n_archivos", "0");
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 39, "", "permisos", false);
					sec.registrarTransaccion(request, 40, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a consultarArchivos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 39, "", "excepcion:" + e.toString(), false);
					sec.registrarTransaccion(request, 40, "", "excepcion:" + e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		else if (accion.equals("log_importacion_archivo_individuos"))
		{
			target = "/log_importacion_archivo_individuos.jsp";
			try {
				if (sec.tienePermiso(usuario, "44")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Consultando log de importación de individuos...");
					retorno = logImportacionArchivoIndividuos(Auxiliar.nz(request.getParameter("arch_consecutivo"), ""), session);
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 44, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a logImportacionArchivoIndividuos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 44, "", "excepcion:" + e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		else if (accion.equals("log_importacion_archivo_tallos"))
		{
			target = "/log_importacion_archivo_tallos.jsp";
			try {
				if (sec.tienePermiso(usuario, "44")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Consultando log de importación de tallo...");
					retorno = logImportacionArchivoTallos(Auxiliar.nz(request.getParameter("arch_consecutivo"), ""), session);
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 44, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a logImportacionArchivoTallos(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 44, "", "excepcion:" + e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		else if (accion.equals("log_importacion_archivo_parcelas"))
		{
			target = "/log_importacion_archivo_parcelas.jsp";
			try {
				if (sec.tienePermiso(usuario, "44")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Consultando log de importación de parcelas...");
					retorno = logImportacionArchivoParcelas(Auxiliar.nz(request.getParameter("arch_consecutivo"), ""), session);
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 44, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a logImportacionArchivoParcelas(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 44, "", "excepcion:" + e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		else if (accion.equals("eliminar_archivo"))
		{
			target = "/eliminar_archivo.jsp";
			target = "/consulta_archivos.jsp";
			try {
				if (sec.tienePermiso(usuario, "39") || sec.tienePermiso(usuario, "40")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Eliminando archivo...");
					retorno = eliminarArchivo(Auxiliar.nz(request.getParameter("arch_consecutivo"), ""), request);
					
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Consultando archivos después de eliminar uno..." );
					String resultado = consultarArchivos(Auxiliar.nz(request.getParameter("f_consecutivo"), ""), 
							Auxiliar.nz(request.getParameter("f_nombre"), ""), 
							Auxiliar.nz(request.getParameter("f_tipo"), ""), 
							Auxiliar.nz(request.getParameter("f_descripcion"), ""), 
							Auxiliar.nz(request.getParameter("f_estado"), ""), 
							Auxiliar.nz(request.getParameter("f_usuario"), ""), request);
					String [] a_resultado = resultado.split("-=-");
					request.setAttribute("n_archivos", a_resultado[0]);
					retorno += a_resultado[1];
				}
				else {
					request.setAttribute("n_archivos", "0");
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 39, "", "permisos", false);
					sec.registrarTransaccion(request, 40, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a eliminarArchivo(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 39, "", "excepcion:" + e.toString(), false);
					sec.registrarTransaccion(request, 40, "", "excepcion:" + e.toString(), false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		else if (accion.equals("procesar_archivo"))
		{
			target = "/procesar_archivo.jsp";
			try {
				if (sec.tienePermiso(usuario, "44")) {
					System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Procesando archivo...");
					retorno = procesarArchivo(Auxiliar.nz(request.getParameter("arch_consecutivo"), ""),Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""),Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), request);
				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
					sec.registrarTransaccion(request, 44, "", "permisos", false);
				}
			} catch (Exception e) {
				retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a procesarArchivo(): " + e.toString(), usuario, metodo);
				try {
					sec.registrarTransaccion(request, 44, "", "excepcion:" + e.toString(), false);
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
			retorno = Auxiliar.mensaje("confirmacion", "Su sesión ha terminado, muchas gracias.", usuario, metodo);
			retorno += Auxiliar.mensaje("nota", "Clic <a class=boton href='menu.jsp'>acá</a> para volver a ingresar.", usuario, metodo);
			try {
				sec.registrarTransaccion(request, 195, "", "CargueMasivo", false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Generando response...");
		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		response.setContentType("text/html; charset=UTF-8");
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}
	

	/**
	 * Método que retorna el código html de un formulario para generar un listado especificado
	 *  
	 * @param tipo String: tipo de listado a retornar
	 * @return String: código html
	 * @throws Exception 
	 */
	private String subirArchivo(HttpServletRequest request, HttpSession session) 
	throws Exception {
		String metodo = yo + "subirArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();
		Sec sec = new Sec();


	    String consecutivo_archivo = "";
	    
		String resultado = "";
		
	    // DEFINIR CARPETA EN LA QUE SE GUARDARA EL ARCHIVO
		String carpetaImportacion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_importacion'", "");
		if (!Auxiliar.tieneAlgo(carpetaImportacion)) {
		    carpetaImportacion = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
		}
		

		// VERIFICAR QUE EL CAPTCHA ESTÉ OKAY

		Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
		String f_respuestacaptcha = "";
		String f_tipo = "";
		String f_descripcion = "";
		String nombre_archivo = "";
		String ruta_archivo = "";
		
		String PRCL_CONSECUTIVO = "";
		String INDV_CONSECUTIVO = "";
		
		List<FileItem> items;
		
		try {
			items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		}
	    catch (FileUploadException e) {
	    	resultado += e.toString();
	        throw new ServletException("Problemas al subir el archivo.", e);
	    }
		
	    try {
	        for (FileItem item : items) {
	            if (item.isFormField()) {
	                String fieldname = item.getFieldName();
	                String fieldvalue = item.getString();
	               
	                if (fieldname.equals("f_respuestacaptcha")) {
	                	f_respuestacaptcha = fieldvalue;
	                }
	            }
	        }
	    }
	    catch (Exception e) {
	    	resultado += e.toString();
	        throw new ServletException("Problemas al leer el código captcha.", e);
	    }
		
	    // SI EL CAPTCHA ES CORRECTO
	    
	    boolean ok_captcha = true;
	    
	    if (captcha != null) {
	    	ok_captcha = captcha.isCorrect(f_respuestacaptcha);
	    }
	    
	    if (ok_captcha) {
		    try {
		        for (FileItem item : items) {
		            if (item.isFormField()) {
		                String fieldname = item.getFieldName();
		                String fieldvalue = item.getString();
		               
		                //resultado += "<br>Revisando " + fieldname + " con valor " + fieldvalue;
		                
		                if (fieldname.equals("f_respuestacaptcha"))
		                {
		                	f_respuestacaptcha = fieldvalue;
		                }
		                
		                if (fieldname.equals("f_descripcion"))
		                {
		                	f_descripcion = fieldvalue;
		                }
		                
		                if (fieldname.equals("f_tipo"))
		                {
		                	f_tipo = fieldvalue;
		                }
		                
		                if (fieldname.equals("f_indv_consecutivo"))
		                {
		                	//f_indv_consecutivo = fieldvalue;
		                }
		                
		                if (fieldname.equals("PRCL_CONSECUTIVO"))
		                {
		                	PRCL_CONSECUTIVO = fieldvalue;
		                	GLOBAL_PRCL_CONSECUTIVO = PRCL_CONSECUTIVO;
		                }
		                
		                if (fieldname.equals("INDV_CONSECUTIVO"))
		                {
		                	INDV_CONSECUTIVO = fieldvalue;
		                	GLOBAL_INDV_CONSECUTIVO = INDV_CONSECUTIVO;
		                }
		            }
		        }
		        
		        for (FileItem item : items) {
		            if (!item.isFormField()) {
	                    nombre_archivo = new File(item.getName()).getName();
	    	    		//resultado += Auxiliar.mensaje("nota", "Se procesará el archivo " + nombre_archivo, usuario, metodo);
	                    ruta_archivo = carpetaImportacion + File.separator + nombre_archivo;
	                    File archivo_a_guardar = new File(ruta_archivo);
	    				// SUBIR EL ARCHIVO
	    	    		//resultado += Auxiliar.mensaje("nota", "Subiendo el archivo " + ruta_archivo + "...", usuario, metodo);
	                    
	        		    if (f_tipo.equals("INDIVIDUOS") || f_tipo.equals("TALLOS") || f_tipo.equals("PARCELAS")) {
		                    if (archie.existeArchivo(ruta_archivo)) {
		                        resultado = "-=-" + Auxiliar.mensaje("advertencia", "Un archivo con el mismo nombre ya existe: " + ruta_archivo + ".", usuario, metodo);
		                		dbREDD.desconectarse();
		                        return resultado;
		                    }
	        		    }
	        		    
	    	    		try {
							item.write(archivo_a_guardar);

	                        if (archie.existeArchivo(ruta_archivo)) {
	                            //resultado += Auxiliar.mensaje("confirmacion", "Archivo " + ruta_archivo + " guardado.", usuario, metodo);                        	
	                        }
	                        else {
	                            resultado += Auxiliar.mensaje("error", "Inconvenientes al guardar el archivo " + ruta_archivo + ".", usuario, metodo);
	                    		dbREDD.desconectarse();
	                            return resultado;
	                	    }
						} catch (Exception e) {
	                        resultado += Auxiliar.mensaje("error", "Inconvenientes al guardar el archivo " + ruta_archivo + " en el disco: " + e.toString(), usuario, metodo);
							e.printStackTrace();
						}
	                }
		            else {
		            	
		            }
		        }
		    } 
		    catch (Exception e) {
		    	e.printStackTrace();
		    }
		    
		    
		    // VERIFICAR QUE SEA .txt/.csv SI ES DE INDIVIDUOS O PARCELAS, O .zip SI ES DE IMAGENES DE INDIVIDUOS
		    
		    if (f_tipo.equals("INDIVIDUOS") || f_tipo.equals("TALLOS") || f_tipo.equals("PARCELAS")) {
		    	if (!nombre_archivo.contains(".txt") && !nombre_archivo.contains(".csv")) {
		    		dbREDD.desconectarse();
		    		return "-=-" + Auxiliar.mensaje("error", "Un archivo de individuos, tallos o parcelas debe tener extension .txt o .csv (archivo de valores separados por tabulación).", usuario, metodo);
		    	}
		    }
		    
		    if (f_tipo.equals("IMAGENES_PARCELAS") || f_tipo.equals("IMAGENES_INDIVIDUOS")) {
		    	if (!nombre_archivo.contains(".zip") && !nombre_archivo.contains(".7z")) {
		    		dbREDD.desconectarse();
		    		return "-=-" + Auxiliar.mensaje("error", "Un archivo de imágenes debe ser un .zip o un .7z", usuario, metodo);
		    	}
		    }
		    


			resultado += "<p>Resultado de Subida de Archivo:</p>";
					
			if (f_tipo.equals("INDIVIDUOS") || f_tipo.equals("TALLOS") || f_tipo.equals("PARCELAS")) {
				if ((f_tipo.equals("INDIVIDUOS") || f_tipo.equals("TALLOS")) && !sec.tienePermiso(usuario, "38")) {
					dbREDD.desconectarse();
					sec.registrarTransaccion(request, 38, "", "permisos", false);
		    		return "0-=-" + Auxiliar.mensaje("advertencia", "No tiene permiso para registrar archivos de inventario de individuos o tallos.", usuario, metodo);
				}

				if (f_tipo.equals("PARCELAS") && !sec.tienePermiso(usuario, "9")) {
					dbREDD.desconectarse();
					sec.registrarTransaccion(request, 9, "", "permisos por cargue masivo", false);
		    		return "-=-" + Auxiliar.mensaje("advertencia", "No tiene permiso para registrar datos de parcela.", usuario, metodo);
				}
				
			    // REGISTRAR EL ARCHIVO EN LA BASE DE DATOS

		    	try {
		    		//resultado += Auxiliar.mensaje("nota", "Registrando archivo en la base de datos...", usuario, metodo);
					consecutivo_archivo = registrarArchivo(carpetaImportacion, f_tipo, f_descripcion, nombre_archivo, PRCL_CONSECUTIVO, INDV_CONSECUTIVO, session);
					if (Auxiliar.esEntero(consecutivo_archivo)) {
						sec.registrarTransaccion(request, 38, consecutivo_archivo, "archivo registrado", true);
						//resultado += Auxiliar.mensaje("confirmacion", "Archivo registrado en la base de datos con consecutivo: " + consecutivo_archivo, usuario, metodo);
					}
					else {
						sec.registrarTransaccion(request, 38, "", "error:"+nombre_archivo, false);
					}
		    	} catch (Exception e) {
		    		resultado += "Excepción al registrar el archivo. Resultado obtenido: " 
		    					+ consecutivo_archivo + " Código de error: " + e.toString();
					sec.registrarTransaccion(request, 38, "", "excepcion:"+e.toString(), false);
			        throw new ServletException("Problemas al registrar el archivo (" + resultado + ").", e);
		    	}
		    	
		    	
				// SI EL ARCHIVO SE LOGRO REGISTRAR
				if (Auxiliar.esEntero(consecutivo_archivo)) {
					String resultado_subir_archivo = "";
					
					//resultado += Auxiliar.mensaje("confirmacion", "Archivo guardado en " + carpetaImportacion + "... " + resultado_subir_archivo, usuario, metodo);

					// VALIDAR ARCHIVO
						
		    		//resultado += Auxiliar.mensaje("nota", "Realizando la validacion general del archivo...", usuario, metodo);
					String archivo_es_valido = ""; 

					try {
						archivo_es_valido = validarArchivo(consecutivo_archivo, session);
						sec.registrarTransaccion(request, 38, consecutivo_archivo, "archivo es valido", true);
					}
					catch (Exception e) {
				    	resultado += e.toString();
						sec.registrarTransaccion(request, 38, consecutivo_archivo, "no se pudo validar el archivo", false);
				        throw new ServletException("Problemas al validar el archivo.", e);
					}
					

					Cartero miCartero = new Cartero();
					
			        String fromname_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='fromname'", "Sistema de Inventarios Forestales del IDEAM");
			        String fromaddress_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='fromaddress'", "avisosREDD@localhost.localdomain");
			        String servidor_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='servidor_smtp'", "localhost");
			        String protocolo_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='protocolo_autenticacion_smtp'", "PLAIN");
			        String autenticar_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='autenticar_smtp'", "false");
			        String usuario_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='usuario_smtp'", "");
			        String clave_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='clave_smtp'", "");
			        int puerto_smtp = Integer.parseInt(dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='puerto_smtp'", "25"));
					
			        miCartero.establecerFromname(fromname_smtp);
			        miCartero.establecerFromaddress(fromaddress_smtp);
			        miCartero.establecerServidor(servidor_smtp);
			        miCartero.establecerProtocolo(protocolo_smtp);
			        miCartero.establecerAutenticar(autenticar_smtp);
			        miCartero.establecerUsuario(usuario_smtp);
			        miCartero.establecerClave(clave_smtp);
					miCartero.establecerPuerto(puerto_smtp);
			        
					// obtener destinatario usuario
					
					String nombre_usuario = dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_CONSECUTIVO=" + usuario, "");
					String email_usuario = dbREDD.obtenerDato("SELECT USR_CORREOELECTRONIC FROM RED_USUARIO WHERE USR_CONSECUTIVO=" + usuario, "");
					
					String destinatario_usuario = nombre_usuario + "·" + email_usuario;
					String[] a_destinatarios_cc = new String[] {destinatario_usuario};
					ArrayList<String> a_destinatarios_to = new ArrayList<String>();
					

					// obtener destinatarios admisn temáticos
					
					String sql_admins_tematicos = "SELECT USR_NOMBRE, USR_CORREOELECTRONIC FROM RED_USUARIO WHERE USR_CONSECUTIVO IN (SELECT RLUS_CONS_USUARIO FROM RED_USUARIO_ROL WHERE RLUS_CONS_ROL IN (2))";
					
					try {
						ResultSet rset = dbREDD.consultarBD(sql_admins_tematicos);
						
						if (rset != null)
						{
							String USR_NOMBRE = "";
							String USR_CORREOELECTRONIC = "";
							
							String destinatario_usuario_admin_tematico = "";
							
							while (rset.next())
							{
								USR_NOMBRE = rset.getString("USR_NOMBRE");
								USR_CORREOELECTRONIC = rset.getString("USR_CORREOELECTRONIC");

								destinatario_usuario_admin_tematico = USR_NOMBRE + "·" + USR_CORREOELECTRONIC;
								a_destinatarios_to.add(destinatario_usuario_admin_tematico);
							}
							
							rset.close();
						}
						else
						{
							t = Auxiliar.mensaje("advertencia", "El conjunto de resultados retornados para la consulta ["+sql_admins_tematicos+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError, usuario, metodo);
						}
					} catch (SQLException e) {
						t = Auxiliar.mensaje("error", "Excepción de SQL ["+sql_admins_tematicos+"]: " + e.toString(), usuario, metodo);
					} catch (Exception e) {
						t = Auxiliar.mensaje("error", "Ocurrió la siguiente excepción en subirArchivo(): " + e.toString() + " -- SQL: " + sql_admins_tematicos, usuario, metodo);
					}

					String[] a_destinatarios_bcc = new String[] {};

					// SI EL ARCHIVO ES VALIDO
					if (archivo_es_valido.equals("1")) {
				    	String base_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='base_url_admif'", "http://172.16.1.125/AdmIF");
						String link_ver_archivo = "<a href='"+base_url+"/consulta_archivos.jsp?id=" + consecutivo_archivo + "'>Ver Archivo</a>";
						resultado += Auxiliar.mensaje("confirmacion", "Archivo subido. " + link_ver_archivo, usuario, metodo);

						// ENVIAR E-MAIL DE AVISO
						
						String asunto_email = "REDD - Nuevo archivo de " + f_tipo;
						String mensaje_email = "Se subió un nuevo archivo de " + f_tipo + " con nombre: " + nombre_archivo + "\n";
						mensaje_email += "<p>" + resultado + "</p>";
						
						String respuesta_cartero = "";
						
						respuesta_cartero = miCartero.enviarMail_CodeMonkey(asunto_email, a_destinatarios_to, a_destinatarios_cc, a_destinatarios_bcc, mensaje_email, true);
						
						//resultado += Auxiliar.mensaje("nota", "Resultado del envío del email: " + respuesta_cartero, usuario, metodo);
						
						sec.registrarTransaccion(request, 215, consecutivo_archivo, mensaje_email, true);

					}
					else {

						// ENVIAR E-MAIL DE AVISO
						
						String asunto_email = "REDD - Nuevo archivo de " + f_tipo;
						String mensaje_email = "Se subió sin éxito un nuevo archivo de " + f_tipo + " con nombre: " + nombre_archivo + "\n";
						mensaje_email += "\n" + "Resultado de la operación: \n" + resultado;
						
						String respuesta_cartero = "";
						
						respuesta_cartero = miCartero.enviarMail_CodeMonkey(asunto_email, a_destinatarios_to, a_destinatarios_cc, a_destinatarios_bcc, mensaje_email, true);
						
						//resultado += Auxiliar.mensaje("nota", "Resultado del envío del email: " + respuesta_cartero, usuario, metodo);
						
						sec.registrarTransaccion(request, 215, consecutivo_archivo, mensaje_email, true);

						/*
						// ELIMINAR ARCHIVO
			    		resultado += Auxiliar.mensaje("nota", "Eliminando archivo no válido (" + archivo_es_valido + ")...", usuario, metodo);
						String resultado_eliminar_archivo = eliminarArchivo(consecutivo_archivo, request);
						if (resultado_eliminar_archivo.equals("1"))
						{
							resultado += Auxiliar.mensaje("advertencia", "El archivo tuvo que ser eliminado por no ser válido.", usuario, metodo);
						}
						*/
					}
				}
				else
				{
					resultado += Auxiliar.mensaje("advertencia", "El archivo no pudo ser registrado en la base de datos: " + consecutivo_archivo, usuario, metodo);
				}
		    }
			
		    if (f_tipo.equals("IMAGENES_PARCELAS") || f_tipo.equals("IMAGENES_INDIVIDUOS")) {
				if ((f_tipo.equals("IMAGENES_PARCELAS") || f_tipo.equals("IMAGENES_INDIVIDUOS")) && !(sec.tienePermiso(usuario, "17") || sec.tienePermiso(usuario, "18"))) {
					dbREDD.desconectarse();
					sec.registrarTransaccion(request, 38, "", "no tiene permisos para registrar imágenes", false);
		    		return "-=-" + Auxiliar.mensaje("advertencia", "No tiene permiso para registrar imágenes.", usuario, metodo);
				}

		    	if (archie.existeArchivo(ruta_archivo)) {
				    // REGISTRAR EL ARCHIVO EN LA BASE DE DATOS
				    consecutivo_archivo = "";
			    	try {
			    		resultado += Auxiliar.mensaje("nota", "Registrando archivo en la base de datos...", usuario, metodo);
						consecutivo_archivo = registrarArchivo(carpetaImportacion, f_tipo, f_descripcion, nombre_archivo, Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), ""), Auxiliar.nz(request.getParameter("INDV_CONSECUTIVO"), ""), session);
						if (Auxiliar.esEntero(consecutivo_archivo)) {
							resultado += Auxiliar.mensaje("confirmacion", "Archivo registrado en la base de datos con consecutivo: " + consecutivo_archivo, usuario, metodo);
							sec.registrarTransaccion(request, 38, "", "archivo de imágenes registrado", true);
						}
			    	} catch (Exception e) {
			    		resultado += "Excepción al registrar el archivo. Resultado obtenido: " 
			    					+ consecutivo_archivo + " Código de error: " + e.toString();
						sec.registrarTransaccion(request, 38, "", "excepcion al registrar archivo de imágenes:"+e.toString(), false);
				        throw new ServletException("Problemas al registrar el archivo (" + resultado + ").", e);
			    	}
			    	
			    	
					// SI EL ARCHIVO SE LOGRO REGISTRAR
			    	if (f_tipo.equals("IMAGENES_INDIVIDUOS")) {
						if (Auxiliar.esEntero(consecutivo_archivo)) {
							try {
								resultado += importarImagenesIndividuos(consecutivo_archivo, request);
							}
							catch (Exception e) {
					    		resultado += "Excepción al importar las imágenes del archivo. Resultado obtenido:  " 
				    					+ consecutivo_archivo + " Código de error: " + e.toString();
								sec.registrarTransaccion(request, 214, "", "excepcion al importar archivo de imágenes de individuos:"+e.toString(), false);
						        throw new ServletException("Problemas al importar las imágenes del archivo (" + resultado + ").", e);
							}
						}
			    	}
			    	if (f_tipo.equals("IMAGENES_PARCELAS")) {
						if (Auxiliar.esEntero(consecutivo_archivo)) {
							try {
								resultado += importarImagenesParcelas(consecutivo_archivo, request);
							}
							catch (Exception e) {
					    		resultado += "Excepción al importar las imágenes del archivo. Resultado obtenido:  " 
				    					+ consecutivo_archivo + " Código de error: " + e.toString();
								sec.registrarTransaccion(request, 213, "", "excepcion al importar archivo de imágenes de parcela:"+e.toString(), false);
						        throw new ServletException("Problemas al importar las imágenes del archivo (" + resultado + ").", e);
							}
						}
			    	}
		    	}
		    }
	    }
	    else
	    {
	    	resultado = Auxiliar.mensaje("advertencia", "Código de imagen incorrecto. <a href='javascript:history.go(-1);'>Por favor vuelva a intentarlo</a>", usuario, metodo);
	    	session.setAttribute(Captcha.NAME, null);
	    }
	    
		dbREDD.desconectarse();
		return Auxiliar.nzVacio(consecutivo_archivo, "") + "-=-" + resultado;
	}
	
	
	/**
	 * Método para crear el registro de un nuevo archivo que será subido
	 * 
	 * @param carpetaImportacion
	 * @param f_tipo
	 * @param f_descripcion
	 * @return max_id: el consecutivo del nuevo archivo en la tabla
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String registrarArchivo(String carpetaImportacion, String f_tipo, String f_descripcion, String nombre_archivo, String PRCL_CONSECUTIVO, String INDV_CONSECUTIVO, HttpSession session) 
	throws ClassNotFoundException, Exception {
		String metodo = yo + "registrarArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();
		Codec ORACLE_CODEC = new OracleCodec();

		
		String resultado = "";
		
		if (!Auxiliar.tieneAlgo(carpetaImportacion)) {
			dbREDD.desconectarse();
			return " La carpeta destino es un dato obligatorio";
		}
		if (!Auxiliar.tieneAlgo(f_tipo)) {
			dbREDD.desconectarse();
			return " Se debe especificar el tipo del archivo";
		}
		if (!Auxiliar.tieneAlgo(f_descripcion)) {
			dbREDD.desconectarse();
			return " Se debe especificar la descripción del archivo";
		}
		if (!Auxiliar.tieneAlgo(nombre_archivo)) {
			dbREDD.desconectarse();
			return " Se debe especificar el nombre del archivo";
		}

		String ruta_archivo = carpetaImportacion + "/" + nombre_archivo;
			
		if (!archie.existeArchivo(ruta_archivo)) {
			dbREDD.desconectarse();
			return "El archivo " + ruta_archivo + " no existe y no puede ser registrado.";
		}
		
		try {
			String tmpsql = "SELECT ARCH_CONSECUTIVO FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CARPETA='"+Auxiliar.nz(carpetaImportacion, "")+"' AND ARCH_NOMBRE='"+Auxiliar.nz(nombre_archivo, "")+"'"; 
			String archivo_ya_registrado = dbREDD.obtenerDato(tmpsql, "-1");
			
			if (!Auxiliar.esEntero(archivo_ya_registrado)) {
				dbREDD.desconectarse();
				return resultado + "Problemas al verificar si el archivo " + nombre_archivo + " ya fue registrado anteriormente.  Mensaje devuelto: "+archivo_ya_registrado+". [" + tmpsql + "]";
			}
			
			if (!archivo_ya_registrado.equals("-1")) {
				dbREDD.desconectarse();
				return archivo_ya_registrado;
			}
		} 
		catch (Exception e) {
			dbREDD.desconectarse();
			return resultado + e.toString() + dbREDD.ultimoError;
		}

		
		// CALCULAR EL CONSECUTIVO
		String max_consecutivo = "";
		
		try {
			max_consecutivo = dbREDD.obtenerDato("SELECT COALESCE(MAX(ARCH_CONSECUTIVO),0)+1 AS MAXIMO FROM RED_ARCHIVO_IMPORTACION", "");
		} 
		catch (Exception e) {
			dbREDD.desconectarse();
			return resultado + e.toString() + dbREDD.ultimoError;
		}
		
		if (max_consecutivo.equals("")) {
			max_consecutivo = "1";
		}
		
		GLOBAL_CONSECUTIVO_ARCHIVO = max_consecutivo;
		
		long arch_registros = 0;
		
	    if (!(f_tipo.equals("IMAGENES_PARCELAS") || f_tipo.equals("IMAGENES_INDIVIDUOS"))) {
	    	//arch_registros = archie.filasArchivo(ruta_archivo);
	    	arch_registros = archie.filasArchivoImportacion(ruta_archivo); 
	    }
		
	    String estado_inicial = "NUEVO";
	    
	    if (f_tipo.equals("IMAGENES_PARCELAS") || f_tipo.equals("IMAGENES_INDIVIDUOS")) {
	    	estado_inicial = "ARCHIVO SUBIDO. PENDIENTE POR VALIDAR.";
	    }

	    f_descripcion = Auxiliar.limpiarTexto(f_descripcion);
	    f_descripcion = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_descripcion, ""));
	    
	    String USR_CONSECUTIVO = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "NULL");
	    
		String sql = "INSERT INTO RED_ARCHIVO_IMPORTACION ";
		sql += "(ARCH_CONSECUTIVO, ARCH_CARPETA, ARCH_ESTADO, ARCH_REGISTROS, ARCH_ERRORES, ARCH_PROCESADOS, ARCH_NOMBRE, ARCH_DESCRIPCION, ARCH_TIPO, ARCH_FECHA, ARCH_USUARIO, PRCL_CONSECUTIVO, INDV_CONSECUTIVO) ";
		sql += "VALUES ";
		sql += "("+max_consecutivo+", '"+carpetaImportacion+"', '"+estado_inicial+"', "+String.valueOf(arch_registros)+", 0, 0, '"+nombre_archivo+"', '"+f_descripcion+"', '"+f_tipo+"', CURRENT_TIMESTAMP, "+USR_CONSECUTIVO+", "+Auxiliar.nzVacio(PRCL_CONSECUTIVO, "NULL")+", "+Auxiliar.nzVacio(INDV_CONSECUTIVO, "NULL")+") ";

		boolean resultado_insertar_archivo = false;
		
		try {
			resultado_insertar_archivo = dbREDD.ejecutarSQL(sql);
			
			if (resultado_insertar_archivo)	{
				dbREDD.desconectarse();
				return max_consecutivo;
			}
			else {
				dbREDD.desconectarse();
				return "No se pudo registrar el archivo " + nombre_archivo + " - " + resultado_insertar_archivo;
			}
		} 
		catch (Exception e) {
			resultado += "Excepción en registrarArchivo: " + e.toString() + dbREDD.ultimoError + " - SQL:" + sql;
		}

		dbREDD.desconectarse();
		return resultado;
	}

	
	/**
	 * Método para validar un archivo
	 * 
	 * @param consecutivo: consecutivo del archivo
	 * @return r: resultado de tipo string. Retorna "1" si el archivo es válido.
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String validarArchivo(String consecutivo_archivo, HttpSession session) 
	throws ClassNotFoundException, Exception {
		String metodo = yo + "validarArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		boolean valido = true;
		String resultado = "";
		String estado = "PENDIENTE POR VALIDAR. ";

		if (consecutivo_archivo == null) {
			dbREDD.desconectarse();
			return "El archivo no tiene consecutivo";
		}
		
		String carpeta = "";
		String nombre_archivo = "";
		String tipo = "";
	
		
	    try {
			carpeta = dbREDD.obtenerDato("SELECT ARCH_CARPETA FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
			nombre_archivo = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
			tipo = dbREDD.obtenerDato("SELECT ARCH_TIPO FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
	    }
	    catch (Exception e) {
	    	resultado += Auxiliar.mensaje("error", "Problemas al obtener informacion sobre el archivo:" + e.toString(), usuario, metodo);
	    }

	    if (tipo.equals("INDIVIDUOS") || tipo.equals("TALLOS") || tipo.equals("PARCELAS"))
	    {
	    	if (!nombre_archivo.contains(".txt") && !nombre_archivo.contains(".csv"))
	    	{
	    		dbREDD.desconectarse();
	    		estado += "Extensión de archivo incorrecta. Un archivo de individuos, tallos o parcelas debe ser un archivo con extension .txt o .csv (archivo de valores separados por TABULACIÓN). ";
	    		valido = false;
	    	}
	    }
	    
	    if (tipo.equals("IMAGENES_PARCELAS") || tipo.equals("IMAGENES_INDIVIDUOS"))
	    {
	    	if (!nombre_archivo.contains(".zip") && !nombre_archivo.contains(".7z"))
	    	{
	    		dbREDD.desconectarse();
	    		estado += "Un archivo de imágenes debe ser un .zip o un .7z";
	    		valido = false;
	    	}
	    }
		
	    String ruta = carpeta + "/" + nombre_archivo;
	    
	    try {
	    
		    File f = new File(ruta);
		    
		    if (!f.exists()) {
				dbREDD.desconectarse();
	    		estado += "El archivo de " + tipo + " " + ruta + " no existe.";
	    		valido = false;
		    }
		    
		    long tam = f.length();
		    
		    if (tam == 0) {
				dbREDD.desconectarse();
				estado += "El archivo de " + tipo + " " + ruta + " está vacío.";
				valido = false;
		    }
		    
		    if (valido) {
		    	estado = "LISTO PARA PROCESAR";
		    }
		    
		    boolean resultado_registrar_validez = false;
		    
		    String sql_registro_validez = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='"+estado+"' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
		    
		    try {
		    	resultado_registrar_validez = dbREDD.ejecutarSQL(sql_registro_validez);
		    }
		    catch (Exception e) {
		    	resultado += Auxiliar.mensaje("error", "Problemas al registrar la validez del archivo:" + e.toString(), usuario, metodo);
		    }
		    
		    if (!resultado_registrar_validez) {
		    	resultado = "No se pudo registrar la validez del archivo. [" + sql_registro_validez + "]{" + dbREDD.ultimoError + "}";
		    }
		    else {
		    	resultado = "1";
		    }
		    
	    }
	    catch (Exception e) {
	    	resultado += e.toString();
	    }
	    
		dbREDD.desconectarse();
		return resultado;
	}

	
	/**
	 * Método para retornar una tabla html con la lista de archivos de 
	 * acuerdo con los siguientes filtros:
	 * 
	 * @param f_consecutivo
	 * @param f_nombre
	 * @param f_tipo
	 * @param f_descripcion
	 * @param f_estado
	 * @param f_usuario
	 * @return r: lista de archivos (en un resultset)
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String consultarArchivos(String f_consecutivo, String f_nombre, 
			String f_tipo, String f_descripcion, 
			String f_estado, String f_usuario,
			HttpServletRequest request
			)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "consultarArchivos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();
		Codec ORACLE_CODEC = new OracleCodec();

		String r = "";
		
		long n_archivos = 0;
		
		String w_consecutivo = "";
		String w_nombre = "";
		String w_tipo = "";
		String w_descripcion = "";
		String w_estado = "";
		String w_usuario = "";
		String w_usuario_propio = "";

		f_consecutivo = Auxiliar.limpiarTexto(f_consecutivo);
		f_consecutivo = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_consecutivo, ""));
		if (Auxiliar.tieneAlgo(f_consecutivo)) {
			w_consecutivo = " AND ARCH_CONSECUTIVO IN ("+f_consecutivo+") ";
		}
		
		f_nombre = Auxiliar.limpiarTexto(f_nombre);
		f_nombre = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_nombre, ""));
		if (Auxiliar.tieneAlgo(f_nombre)) {
			w_nombre = " AND ARCH_NOMBRE LIKE '%"+f_nombre+"%' ";
		}

		f_tipo = Auxiliar.limpiarTexto(f_tipo);
		f_tipo = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_tipo, ""));
		if (Auxiliar.tieneAlgo(f_tipo)) {
			w_tipo = " AND ARCH_TIPO = '"+f_tipo+"' ";
		}
		
		f_descripcion = Auxiliar.limpiarTexto(f_descripcion);
		f_descripcion = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_descripcion, ""));
		if (Auxiliar.tieneAlgo(f_descripcion)) {
			w_descripcion = " AND ARCH_DESCRIPCION LIKE '%"+f_descripcion+"%' ";
		}

		f_estado = Auxiliar.limpiarTexto(f_estado);
		f_estado = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_estado, ""));
		if (Auxiliar.tieneAlgo(f_estado))
		{
			w_estado = " AND ARCH_ESTADO = '"+f_estado+"' ";
		}
		
		f_usuario = Auxiliar.limpiarTexto(f_usuario);
		f_usuario = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(f_usuario, ""));
		if (Auxiliar.tieneAlgo(f_usuario))
		{
		    String USR_CONSECUTIVO = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+f_usuario+"'", "NULL");

			w_usuario = " AND ARCH_USUARIO = "+USR_CONSECUTIVO+" ";
		}
		
		if (!sec.tienePermiso(usuario, "40")) {
			w_usuario_propio = " AND ARCH_USUARIO = "+usuario+" ";
			sec.registrarTransaccion(request, 39, Auxiliar.nz(f_consecutivo, ""), "", true);
		}
		else {
			sec.registrarTransaccion(request, 40, Auxiliar.nz(f_consecutivo, ""), "", true);
		}
		
		String sql = "SELECT ";
		sql += "ARCH_CONSECUTIVO,";
		sql += "ARCH_CARPETA,";
		sql += "ARCH_ESTADO,";
		sql += "ARCH_REGISTROS,";
		sql += "ARCH_ERRORES,";
		sql += "ARCH_PROCESADOS,";
		sql += "ARCH_NOMBRE,";
		sql += "ARCH_DESCRIPCION,";
		sql += "ARCH_TIPO,";
		sql += "TO_CHAR (ARCH_FECHA, 'YYYY-MON-DD HH24:MI:SS') AS FECHA,";
		//sql += "ARCH_FECHA AS FECHA,";
		sql += "ARCH_USUARIO,";
		sql += "PRCL_CONSECUTIVO,";
		sql += "INDV_CONSECUTIVO";
		sql += " FROM RED_ARCHIVO_IMPORTACION ";
		sql += " WHERE 1=1 ";
		sql += w_consecutivo;
		sql += w_nombre;
		sql += w_tipo;
		sql += w_descripcion;
		sql += w_estado;
		sql += w_usuario;
		sql += w_usuario_propio;
		sql += " ORDER BY ARCH_CONSECUTIVO desc";

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				n_archivos = 0; 
				
				String tabla = "";
				String db_consecutivo = "";
				String db_nombre = "";
				//String db_carpeta = "";
				String db_tipo = "";
				String db_descripcion = "";
				String db_estado = "";
				String db_usuario = "";
				String db_fecha = "";
				String db_registros = "";
				String db_errores = "";
				String db_procesados = "";
				String db_PRCL_CONSECUTIVO = "";
				String db_INDV_CONSECUTIVO = "";
				
				String t_Opciones = "";
				String t_Consecutivo = "";
				String t_Tipo = "";
				String t_Estado = "";
				String t_Archivo = "";
				String t_Descripcion = "";
				String t_Estadisticas = "";
				String t_Parcela = "";
				String t_Individuo = "";
				String t_Usuario = "";
				String t_Fecha = "";

				String opciones = "";
				
				tabla = "<div class='listado'>";
				tabla = "<h1>"+Auxiliar.traducir("resultados_de_la_busqueda", idioma, "Resultados de la Búsqueda" + "..")+"</h1>";

				tabla += "<div class='tabla_resultados'>";

				t_Opciones = Auxiliar.traducir("General.Opciones", idioma, "Opciones" + "..");
				t_Consecutivo = Auxiliar.traducir(yo+"Consecutivo", idioma, "Consecutivo" + "..");
				t_Tipo = Auxiliar.traducir(yo+"Tipo", idioma, "Tipo" + "..");
				t_Estado = Auxiliar.traducir(yo+"Estado", idioma, "Estado" + "..");
				t_Archivo = Auxiliar.traducir(yo+"Archivo", idioma, "Archivo" + "..");
				t_Descripcion = Auxiliar.traducir(yo+"Descripcion", idioma, "Descripcion" + "..");
				t_Estadisticas = Auxiliar.traducir(yo+"Estadisticas", idioma, "Estadisticas" + "..");
				t_Parcela = Auxiliar.traducir(yo+"Parcela", idioma, "Parcela" + "..");
				t_Individuo = Auxiliar.traducir(yo+"Individuo", idioma, "Individuo" + "..");
				t_Usuario = Auxiliar.traducir(yo+"Usuario", idioma, "Usuario" + "..");
				t_Fecha = Auxiliar.traducir(yo+"Fecha", idioma, "Fecha" + "..");
				
				long [] a_estadisticas = null;
				
		        String recalcular_estadisticas_importacion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='recalcular_estadisticas_importacion'", "0");

				
				while (rset.next())
				{
					n_archivos++;
					
					opciones = "";
					
					db_consecutivo = rset.getString("ARCH_CONSECUTIVO");
					//db_carpeta = rset.getString("ARCH_CARPETA");
					db_nombre = rset.getString("ARCH_NOMBRE");
					db_tipo = rset.getString("ARCH_TIPO");
					db_descripcion = rset.getString("ARCH_DESCRIPCION");
					db_estado = rset.getString("ARCH_ESTADO");
					String arch_usuario = String.valueOf(rset.getInt("ARCH_USUARIO"));
					db_usuario = dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_CONSECUTIVO="+arch_usuario, arch_usuario);
					db_registros = rset.getString("ARCH_REGISTROS");
					db_errores = rset.getString("ARCH_ERRORES");
					db_procesados = rset.getString("ARCH_PROCESADOS");
					db_PRCL_CONSECUTIVO = rset.getString("PRCL_CONSECUTIVO");
					db_INDV_CONSECUTIVO = rset.getString("INDV_CONSECUTIVO");

					
					if (!db_tipo.equals("IMAGENES") && recalcular_estadisticas_importacion.equals("1")) {
					
						a_estadisticas = recalcularEstadisticasArchivo(db_consecutivo, session);
						db_registros = String.valueOf(a_estadisticas[0]);
						db_procesados = String.valueOf(a_estadisticas[1]);
						db_errores = String.valueOf(a_estadisticas[2]);
						
					}
					
					
					db_fecha = rset.getString("FECHA");
					
					if (db_estado.equals("LISTO PARA PROCESAR"))
					{
						if (!db_tipo.equals("IMAGENES_PARCELAS") && !db_tipo.equals("IMAGENES_INDIVIDUOS")) { 
							opciones += "<br><div class='opcionmenu'><a class=boton href='?accion=procesar_archivo&arch_consecutivo="+db_consecutivo+"' >";
							try { opciones += msj.getString(yo+"Procesar"); } catch (MissingResourceException e) { opciones += "Procesar" + ".."; }
							opciones += "</a></div>";
						}
					}

					String tipolog = "";
					
					if (db_tipo.equals("INDIVIDUOS")) { 
						tipolog = "log_importacion_archivo_individuos";
					}
					
					if (db_tipo.equals("TALLOS")) { 
						tipolog = "log_importacion_archivo_tallos";
					}
					
					if (db_tipo.equals("PARCELAS")) { 
						tipolog = "log_importacion_archivo_parcelas";
					}
					
					if (!db_tipo.equals("IMAGENES_PARCELAS") && !db_tipo.equals("IMAGENES_INDIVIDUOS")) { 
						opciones += "<div class='opcionmenu'><a class=boton href='?accion="+tipolog+"&arch_consecutivo="+db_consecutivo+"' >";
						try { opciones += msj.getString(yo+"Ver_Log_de_Errores"); } catch (MissingResourceException e) { opciones += "Ver Log de Importación" + ".."; }
						opciones += "</a></div>";
					}					

					if (!db_estado.equals("ARCHIVO PROCESADO"))
					{
						opciones += "<div class='opcionmenu'><a class=boton href='?accion=eliminar_archivo&arch_consecutivo="+db_consecutivo+"' >";
						try { opciones += msj.getString(yo+"Eliminar"); } catch (MissingResourceException e) { opciones += "Eliminar" + ".."; }
						opciones += "</a></div>";
					}
					
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_Archivo+":<a class=boton href='upload/"+db_nombre+"' target='_blank'>"+db_nombre+"</a></div>";
					tabla += "<div class='dato_resultado' style='clear:both;'>"+t_Estadisticas+":";
					if (!db_tipo.equals("IMAGENES")) {
						tabla += "<table>";
						tabla += "<tr><th>";
						try { tabla += msj.getString(yo+"Registros"); } catch (MissingResourceException e) { tabla += "Registros" + ".."; }
						tabla += ":</th><td>"+db_registros+"</td></tr>";
						tabla += "<tr><th>";
						try { tabla += msj.getString(yo+"Procesados"); } catch (MissingResourceException e) { tabla += "Procesados" + ".."; }
						tabla += ":</th><td>"+db_procesados+"</td></tr>";
						tabla += "<tr><th>";
						try { tabla += msj.getString(yo+"Errores"); } catch (MissingResourceException e) { tabla += "Errores" + ".."; }
						tabla += ":</th><td>"+db_errores+"</td></tr>";
						tabla += "</table>";
					}
					tabla += "</div>";
					tabla += "<div class='dato_resultado' style='clear:both;'>"+t_Consecutivo+":"+db_consecutivo+"</div>";
					tabla += "<div class='dato_resultado'>"+t_Tipo+":"+db_tipo+"</div>";
					tabla += "<div class='dato_resultado'>"+t_Estado+":"+db_estado+"</div>";
					tabla += "<div class='dato_resultado'>"+t_Descripcion+":"+db_descripcion+"</div>";
					//tabla += "<div class='dato_resultado'>"+t_Parcela+":"+db_PRCL_CONSECUTIVO+"</div>";
					//tabla += "<div class='dato_resultado'>"+t_Individuo+":"+db_INDV_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_Usuario+":"+db_usuario+"</div>";
					tabla += "<div class='dato_resultado'>"+t_Fecha+":"+db_fecha+"</div>";
					tabla += "<div class='dato_resultado' style='clear:both;'>"+t_Opciones+":"+opciones+"</div>";
					tabla += "</div>";
					
				}
				
				if (n_archivos == 0) {
					tabla += Auxiliar.mensaje("nota", Auxiliar.traducir("no_se_encontraron_registros", idioma, "No se encontraron registros" + ".."), usuario, metodo);
				}

				tabla += "</div>";
				tabla += "</div>";

				rset.close();
				
				r=tabla;
			}
			else
			{
				r += "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql;
		}
		
		dbREDD.desconectarse();
				
		return String.valueOf(n_archivos) + "-=-" + r;
		
	}
	
	/**
	 * Recalcula las estadísticas de procesamiento de un archivo
	 * 
	 * @param consecutivo_archivo
	 * @return long [] arreglo de estadísticas
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public long [] recalcularEstadisticasArchivo(String consecutivo_archivo, HttpSession session) 
	throws ClassNotFoundException, Exception {
		String metodo = yo + "recalcularEstadisticasArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		long[] a = {0,0,0};
		
	    try {
			long arch_registros = 0;
			
			String nombre_archivo = "";
			String tipo = "";
			int procesados = 0;
			int errores = 0;
			
			String ruta_app = getServletContext().getRealPath("");
			String ruta_carpeta_upload = ruta_app + File.separator + "upload";

			nombre_archivo = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
			tipo = dbREDD.obtenerDato("SELECT ARCH_TIPO FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		    if (!tipo.equals("IMAGENES_PARCELAS") && !tipo.equals("IMAGENES_INDIVIDUOS")) {
		    	if (tipo.equals("INDIVIDUOS")) {
		    		procesados = Integer.parseInt(dbREDD.obtenerDato("SELECT COUNT(*) FROM RED_INDIVIDUO WHERE INDV_ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"));
		    		errores = Integer.parseInt(dbREDD.obtenerDato("SELECT COUNT(*) FROM RED_INDIVIDUO_IMPORTACION WHERE IDIM_ESTADOIMPORTACION=0 AND IDIM_ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"));
		    		dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_PROCESADOS=" + procesados + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
		    		dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ERRORES=" + errores + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
		    	}
		    	else if (tipo.equals("TALLOS")) {
					procesados = Integer.parseInt(dbREDD.obtenerDato("SELECT COUNT(*) FROM RED_TALLO WHERE TAYO_ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"));
				    errores = Integer.parseInt(dbREDD.obtenerDato("SELECT COUNT(*) FROM RED_TALLO_IMPORTACION WHERE TAIM_ESTADOIMPORTACION=0 AND TAIM_ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"));
				    dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_PROCESADOS=" + procesados + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
				    dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ERRORES=" + errores + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
				}
				else if (tipo.equals("PARCELAS")) {
					procesados = Integer.parseInt(dbREDD.obtenerDato("SELECT COUNT(*) FROM RED_PARCELA WHERE PRCL_ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"));
				    errores = Integer.parseInt(dbREDD.obtenerDato("SELECT COUNT(*) FROM RED_PARCELA_IMPORTACION WHERE PRIM_ESTADOIMPORTACION=0 AND PRIM_ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"));
				    dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_PROCESADOS=" + procesados + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
				    dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ERRORES=" + errores + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
				}
	
			    String ruta = ruta_carpeta_upload + "/" + nombre_archivo;
		    
			    if (archie.existeArchivo(ruta)) {
			    	arch_registros = archie.filasArchivoImportacion(ruta);
			    	
				    dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_REGISTROS=" + arch_registros + " WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
				    
				    a[0] = arch_registros;
			    }
			    else {
			    	String str_arch_registros = dbREDD.obtenerDato("SELECT ARCH_REGISTROS FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "0"); 
				    a[0] = Integer.parseInt(Auxiliar.nz(str_arch_registros, "0"));
			    }
			    a[1] = procesados;
			    a[2] = errores;
		    }
	    }
	    catch (Exception e) {
	    	t = "Problemas al generar las estadísticas del archivo " + consecutivo_archivo + ":" + e.toString();
	    }

		dbREDD.desconectarse();
		return a;
	}

	/**
	 * Genera log de importación de archivo de individuos
	 * 
	 * @param f_consecutivo
	 * @param session
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String logImportacionArchivoIndividuos(String f_consecutivo, HttpSession session)
			throws ClassNotFoundException, Exception {
		String metodo = yo + "logImportacionArchivoIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		String sql = "SELECT * FROM RED_INDIVIDUO_IMPORTACION WHERE IDIM_ARCH_CONSECUTIVO = "+f_consecutivo;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String tabla = "";
				
				String ARCH_NOMBRE = "";
				
				try {
					ARCH_NOMBRE = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + f_consecutivo, "");
					tabla = "<h2>Archivo: " + ARCH_NOMBRE + "</h2>";
				}
				catch (Exception e) {
					r += Auxiliar.mensaje("error", "Error de al consultar el nombre del archivo a desplegar.", usuario, metodo);
				}
				
				String IDIM_ID = "";
				
				String IDIM_ARCH_CONSECUTIVO = f_consecutivo;
				String IDIM_OBSERVACIONES_IMPORTACION = "";
				
				String IDIM_CONSECUTIVO = "";
				String IDIM_PRCL_CONSECUTIVO = "";
				String IDIM_ID_IMPORTACION = ""; 
				String IDIM_ESARBOLREFERENCIA = ""; 
				String IDIM_CARDINALIDAD = ""; 
				String IDIM_NUMERO_COLECTOR = ""; 
				String IDIM_CANTIDAD_EJEMPLARES = ""; 
				String IDIM_DISTANCIA = "";
				String IDIM_AZIMUTH = "";
				String IDIM_NUMERO_ARBOL = "";
				String IDIM_ESPECIE = "";
				String IDIM_SUBPARCELA = "";
				String IDIM_OBSERVACIONES = "";
				String IDIM_ETIQUETA_COLECTA = "";
				String IDIM_FOTO_COLECTA = "";
				String IDIM_HOMOLOGACION = "";
				//String IDIM_ARCHIVO_FOTOS = "";
				String IDIM_TXCT_ID = "";
				
				tabla += "<div class='log_importacion'>";
				tabla += "<table>";
				
				tabla += "<tr>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"ID"); } catch (MissingResourceException e) { tabla += "Id" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"ARCH_CONSECUTIVO"); } catch (MissingResourceException e) { tabla += "Id Archivo" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"OBSERVACIONES_IMPORTACION"); } catch (MissingResourceException e) { tabla += "Observaciones Importación" + ".."; }
				tabla += "</th>";
				tabla += "<th>INDV_CONSECUTIVO</th>";
				tabla += "<th>INDV_ID_IMPORTACION</th>";
				tabla += "<th>INDV_PRCL_CONSECUTIVO</th>";
				tabla += "<th>INDV_ESARBOLREFERENCIA</th>";
				tabla += "<th>INDV_CARDINALIDAD</th>";
				tabla += "<th>INDV_NUMERO_COLECTOR</th>";
				tabla += "<th>INDV_CANTIDAD_EJEMPLARES</th>";
				tabla += "<th>INDV_DISTANCIA</th>";
				tabla += "<th>INDV_AZIMUTH</th>";
				tabla += "<th>INDV_NUMERO_ARBOL</th>";
				tabla += "<th>INDV_ESPECIE</th>";
				tabla += "<th>INDV_TXCT_ID</th>";
				tabla += "<th>INDV_SUBPARCELA</th>";
				tabla += "<th>INDV_OBSERVACIONES</th>";
				tabla += "<th>INDV_ETIQUETA_COLECTA</th>";
				tabla += "<th>INDV_FOTO_COLECTA</th>";
				tabla += "<th>INDV_HOMOLOGACION</th>";
				//tabla += "<th>INDV_ARCHIVO_FOTOS</th>";
				tabla += "</tr>";
				
				while (rset.next())
				{
					IDIM_ID = rset.getString("IDIM_ID");
					IDIM_ARCH_CONSECUTIVO = rset.getString("IDIM_ARCH_CONSECUTIVO");
					IDIM_OBSERVACIONES_IMPORTACION = rset.getString("IDIM_OBSERVACIONES_IMPORTACION");
					
					IDIM_CONSECUTIVO = rset.getString("IDIM_CONSECUTIVO");
					IDIM_PRCL_CONSECUTIVO = rset.getString("IDIM_PRCL_CONSECUTIVO");
					IDIM_ID_IMPORTACION = rset.getString("IDIM_ID_IMPORTACION"); 
					IDIM_ESARBOLREFERENCIA = rset.getString("IDIM_ESARBOLREFERENCIA"); 
					IDIM_CARDINALIDAD = rset.getString("IDIM_CARDINALIDAD"); 
					IDIM_NUMERO_COLECTOR = rset.getString("IDIM_NUMERO_COLECTOR"); 
					IDIM_CANTIDAD_EJEMPLARES = rset.getString("IDIM_CANTIDAD_EJEMPLARES"); 
					IDIM_DISTANCIA = rset.getString("IDIM_DISTANCIA");
					IDIM_AZIMUTH = rset.getString("IDIM_AZIMUTH");
					IDIM_NUMERO_ARBOL = rset.getString("IDIM_NUMERO_ARBOL");
					IDIM_ESPECIE = rset.getString("IDIM_ESPECIE");
					IDIM_SUBPARCELA = rset.getString("IDIM_SUBPARCELA");
					IDIM_OBSERVACIONES = rset.getString("IDIM_OBSERVACIONES");
					IDIM_ETIQUETA_COLECTA = rset.getString("IDIM_ETIQUETA_COLECTA");
					IDIM_FOTO_COLECTA = rset.getString("IDIM_FOTO_COLECTA");
					IDIM_HOMOLOGACION = rset.getString("IDIM_HOMOLOGACION");
					//IDIM_ARCHIVO_FOTOS = rset.getString("IDIM_ARCHIVO_FOTOS");
					IDIM_TXCT_ID = rset.getString("IDIM_TXCT_ID");
					
					tabla += "<tr>";
					tabla += "<td>"+IDIM_ID+"</td>";
					tabla += "<td>"+IDIM_ARCH_CONSECUTIVO+"</td>";
					tabla += "<td>"+IDIM_OBSERVACIONES_IMPORTACION+"</td>";
					tabla += "<th>" + IDIM_CONSECUTIVO + "</th>"; 
					tabla += "<th>" + IDIM_ID_IMPORTACION + "</th>"; 
					tabla += "<th>" + IDIM_PRCL_CONSECUTIVO + "</th>"; 
					tabla += "<th>" + IDIM_ESARBOLREFERENCIA + "</th>"; 
					tabla += "<th>" + IDIM_CARDINALIDAD + "</th>"; 
					tabla += "<th>" + IDIM_NUMERO_COLECTOR + "</th>"; 
					tabla += "<th>" + IDIM_CANTIDAD_EJEMPLARES + "</th>"; 
					tabla += "<th>" + IDIM_DISTANCIA + "</th>"; 
					tabla += "<th>" + IDIM_AZIMUTH + "</th>"; 
					tabla += "<th>" + IDIM_NUMERO_ARBOL + "</th>"; 
					tabla += "<th>" + IDIM_ESPECIE + "</th>"; 
					tabla += "<th>" + IDIM_TXCT_ID + "</th>"; 
					tabla += "<th>" + IDIM_SUBPARCELA + "</th>"; 
					tabla += "<th>" + IDIM_OBSERVACIONES + "</th>"; 
					tabla += "<th>" + IDIM_ETIQUETA_COLECTA + "</th>"; 
					tabla += "<th>" + IDIM_FOTO_COLECTA + "</th>"; 
					tabla += "<th>" + IDIM_HOMOLOGACION + "</th>"; 
					//tabla += "<th>" + IDIM_ARCHIVO_FOTOS + "</th>"; 
					tabla += "</tr>";
					
				}
				
				tabla += "</table>";
				tabla += "</div>";
				
				rset.close();
				
				r=tabla;
			}
			else
			{
				r += "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en detalleArchivo(): " + e.toString() + " -- SQL: " + sql;
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Genera el log de importación de archivos de tallos
	 * 
	 * @param f_consecutivo
	 * @param session
	 * @return String con el log
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String logImportacionArchivoTallos(String f_consecutivo, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "logImportacionArchivoTallos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		String sql = "SELECT * FROM RED_TALLO_IMPORTACION WHERE TAIM_ARCH_CONSECUTIVO = "+f_consecutivo;

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String tabla = "";
				
				String ARCH_NOMBRE = "";
				
				try {
					ARCH_NOMBRE = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + f_consecutivo, "");
					tabla = "<h2>Archivo: " + ARCH_NOMBRE + "</h2>";
				}
				catch (Exception e) {
					r += Auxiliar.mensaje("error", "Error de al consultar el nombre del archivo a desplegar.", usuario, metodo);
				}
				
				String TAIM_ID = "";
				
				String TAIM_ARCH_CONSECUTIVO = f_consecutivo;
				String TAIM_OBSERVACIONES_IMPORTACION = "";
				
				String TAIM_CONSECUTIVO = "";
				String TAIM_INDV_CONSECUTIVO = "";
				String TAIM_ID_IMPORTACION = "";
				String TAIM_DAP1 = ""; 
				String TAIM_DAP2 = ""; 
				String TAIM_ALTURADAP = ""; 
				String TAIM_ALTURA = ""; 
				String TAIM_ALTURATOTAL = ""; 
				String TAIM_FORMAFUSTE = "";
				String TAIM_DANIO = "";
				String TAIM_OBSERVACIONES = "";

				tabla += "<div class='log_importacion'>";
				tabla += "<table>";
				
				tabla += "<tr>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"ID"); } catch (MissingResourceException e) { tabla += "Id" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"ARCH_CONSECUTIVO"); } catch (MissingResourceException e) { tabla += "Id Archivo" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"OBSERVACIONES_IMPORTACION"); } catch (MissingResourceException e) { tabla += "Observaciones Importación" + ".."; }
				tabla += "</th>";
				tabla += "<th>TAIM_CONSECUTIVO</th>";
				tabla += "<th>TAIM_ID_IMPORTACION</th>";
				tabla += "<th>TAIM_INDV_CONSECUTIVO</th>";
				tabla += "<th>TAIM_DAP1</th>";
				tabla += "<th>TAIM_DAP2</th>";
				tabla += "<th>TAIM_ALTURADAP</th>";
				tabla += "<th>TAIM_ALTURA</th>";
				tabla += "<th>TAIM_ALTURATOTAL</th>";
				tabla += "<th>TAIM_FORMAFUSTE</th>";
				tabla += "<th>TAIM_DANIO</th>";
				tabla += "<th>TAIM_OBSERVACIONES</th>";
				tabla += "</tr>";
				
				while (rset.next())
				{
					TAIM_ID = rset.getString("TAIM_ID");
					TAIM_ARCH_CONSECUTIVO = rset.getString("TAIM_ARCH_CONSECUTIVO");
					TAIM_OBSERVACIONES_IMPORTACION = rset.getString("TAIM_OBSERVACIONES_IMPORTACION");
					
					TAIM_CONSECUTIVO = rset.getString("TAIM_CONSECUTIVO");
					TAIM_INDV_CONSECUTIVO = rset.getString("TAIM_INDV_CONSECUTIVO");
					TAIM_ID_IMPORTACION = rset.getString("TAIM_ID_IMPORTACION"); 
					TAIM_DAP1 = rset.getString("TAIM_DAP1"); 
					TAIM_DAP2 = rset.getString("TAIM_DAP2"); 
					TAIM_ALTURADAP = rset.getString("TAIM_ALTURADAP"); 
					TAIM_ALTURA = rset.getString("TAIM_ALTURA");
					TAIM_ALTURATOTAL = rset.getString("TAIM_ALTURATOTAL");
					TAIM_FORMAFUSTE = rset.getString("TAIM_FORMAFUSTE");
					TAIM_DANIO = rset.getString("TAIM_DANIO");
					TAIM_OBSERVACIONES = rset.getString("TAIM_OBSERVACIONES");
					
					tabla += "<tr>";
					tabla += "<td>"+TAIM_ID+"</td>";
					tabla += "<td>"+TAIM_ARCH_CONSECUTIVO+"</td>";
					tabla += "<td>"+TAIM_OBSERVACIONES_IMPORTACION+"</td>";
					tabla += "<th>" + TAIM_CONSECUTIVO + "</th>"; 
					tabla += "<th>" + TAIM_ID_IMPORTACION + "</th>"; 
					tabla += "<th>" + TAIM_INDV_CONSECUTIVO + "</th>"; 
					tabla += "<th>" + TAIM_DAP1 + "</th>"; 
					tabla += "<th>" + TAIM_DAP2 + "</th>"; 
					tabla += "<th>" + TAIM_ALTURADAP + "</th>"; 
					tabla += "<th>" + TAIM_ALTURA + "</th>"; 
					tabla += "<th>" + TAIM_ALTURATOTAL + "</th>"; 
					tabla += "<th>" + TAIM_FORMAFUSTE + "</th>"; 
					tabla += "<th>" + TAIM_DANIO + "</th>"; 
					tabla += "<th>" + TAIM_OBSERVACIONES + "</th>"; 
					tabla += "</tr>";
					
				}
				
				tabla += "</table>";
				tabla += "</div>";
				
				rset.close();
				
				r=tabla;
			}
			else
			{
				r += "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en detalleArchivo(): " + e.toString() + " -- SQL: " + sql;
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Genera log de importación de parcelas
	 * 
	 * @param f_consecutivo
	 * @param session
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String logImportacionArchivoParcelas(String f_consecutivo, HttpSession session)
			throws ClassNotFoundException, Exception {
		String metodo = yo + "logImportacionArchivoParcelas";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		String sql = "SELECT * FROM RED_PARCELA_IMPORTACION WHERE PRIM_ARCH_CONSECUTIVO = "+f_consecutivo;

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String tabla = "";
				
				String ARCH_NOMBRE = "";
				
				try {
					ARCH_NOMBRE = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + f_consecutivo, "");
					tabla = "<h2>Archivo: " + ARCH_NOMBRE + "</h2>";
				}
				catch (Exception e) {
					r += Auxiliar.mensaje("error", "Error de al consultar el nombre del archivo a desplegar.", usuario, metodo);
				}
				
				String PRIM_ID = "";
				String PRIM_ARCH_CONSECUTIVO = "";
				String PRIM_OBSERVACIONES_IMPORTACION = "";
				String PRIM_FECHAIMPORTACION = "";
				
				String PRIM_CONSECUTIVO = "";
				String PRIM_ID_IMPORTACION = "";
				String PRIM_ID_UPM = "";
				String PRIM_CONS_PAIS = "";
				String PRIM_VEREDA = "";
				String PRIM_CORREGIMIENTO = "";
				String PRIM_INSPECCION_POLICIA = "";
				String PRIM_CASERIO = "";
				String PRIM_RANCHERIA = "";
				String PRIM_FECHA_CAPTURA = "";
				String PRIM_MEDIOACCESO_POBLADO = "";
				String PRIM_DISTANCIA_POBLADO = "";
				String PRIM_TPOBLADO_H = "";
				String PRIM_TPOBLADO_M = "";
				String PRIM_MEDIOACCESO_CAMPAMENTO = "";
				String PRIM_DISTANCIA_CAMPAMENTO = "";
				String PRIM_TCAMPAMENTO_H = "";
				String PRIM_TCAMPAMENTO_M = "";
				String PRIM_MEDIOACCESO_JALON = "";
				String PRIM_DISTANCIA_JALON = "";
				String PRIM_TJALON_H = "";
				String PRIM_TJALON_M = "";
				String PRIM_COORDENADAS = "";
				String PRIM_DISTANCIA_CAMPAMENTOS = "";
				String PRIM_LATITUD = "";
				String PRIM_LONGITUD = "";
				String PRIM_ALTITUD = "";
				String PRIM_NOMBRE = "";
				String PRIM_USR_DILIGENCIA_F1 = "";
				String PRIM_USR_DILIGENCIA_F2 = "";
				String PRIM_FECHAINI_APROXIMACION = "";
				String PRIM_FECHAFIN_APROXIMACION = "";
				String PRIM_FECHAINI_LOCALIZACION = "";
				String PRIM_FECHAFIN_LOCALIZACION = "";
				String PRIM_DESCRIPCION = "";
				String PRIM_OBSERVACIONES = "";
				String PRIM_TRACKLOG_CAMPAMENTO = "";
				String PRIM_TRACKLOG_PARCELA = "";
				String PRIM_DEPARTAMENTO = "";
				String PRIM_MUNICIPIO = "";			
				String PRIM_SPF1_DILIGENCIA = "";
				String PRIM_SPF1_FECHAINI = "";
				String PRIM_SPF1_FECHAFIN = "";
				String PRIM_SPF1_POSIBLE = "";
				String PRIM_SPF1_JUSTIFICACION_NO = "";
				String PRIM_SPF1_OBS_FUSTALES = "";
				String PRIM_SPF1_OBS_LATIZALES = "";
				String PRIM_SPF1_OBS_BRINZALES = "";
				String PRIM_SPF2_DILIGENCIA = "";
				String PRIM_SPF2_FECHAINI = "";
				String PRIM_SPF2_FECHAFIN = "";
				String PRIM_SPF2_POSIBLE = "";
				String PRIM_SPF2_JUSTIFICACION_NO = "";
				String PRIM_SPF2_OBS_FUSTALES = "";
				String PRIM_SPF2_OBS_LATIZALES = "";
				String PRIM_SPF2_OBS_BRINZALES = "";
				String PRIM_SPF3_DILIGENCIA = "";
				String PRIM_SPF3_FECHAINI = "";
				String PRIM_SPF3_FECHAFIN = "";
				String PRIM_SPF3_POSIBLE = "";
				String PRIM_SPF3_JUSTIFICACION_NO = "";
				String PRIM_SPF3_OBS_FUSTALES = "";
				String PRIM_SPF3_OBS_LATIZALES = "";
				String PRIM_SPF3_OBS_BRINZALES = "";
				String PRIM_SPF4_DILIGENCIA = "";
				String PRIM_SPF4_FECHAINI = "";
				String PRIM_SPF4_FECHAFIN = "";
				String PRIM_SPF4_POSIBLE = "";
				String PRIM_SPF4_JUSTIFICACION_NO = "";
				String PRIM_SPF4_OBS_FUSTALES = "";
				String PRIM_SPF4_OBS_LATIZALES = "";
				String PRIM_SPF4_OBS_BRINZALES = "";
				String PRIM_SPF5_DILIGENCIA = "";
				String PRIM_SPF5_FECHAINI = "";
				String PRIM_SPF5_FECHAFIN = "";
				String PRIM_SPF5_POSIBLE = "";
				String PRIM_SPF5_JUSTIFICACION_NO = "";
				String PRIM_SPF5_OBS_FUSTALES = "";
				String PRIM_SPF5_OBS_LATIZALES = "";
				String PRIM_SPF5_OBS_BRINZALES = "";
				
				tabla += "<div class='log_importacion'>";
				tabla += "<table>";
				
				tabla += "<tr>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"ID"); } catch (MissingResourceException e) { tabla += "Id" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"ARCH_CONSECUTIVO"); } catch (MissingResourceException e) { tabla += "Id Archivo" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"OBSERVACIONES_IMPORTACION"); } catch (MissingResourceException e) { tabla += "Observaciones Importación" + ".."; }
				tabla += "</th>";
				tabla += "<th>";
				try { tabla += msj.getString(yo+"FECHAIMPORTACION"); } catch (MissingResourceException e) { tabla += "Fecha Importación" + ".."; }
				tabla += "</th>";
				tabla += "<th>PRCL_CONSECUTIVO</th>";
 				tabla += "<th>PRCL_ID_IMPORTACION</th>";
				tabla += "<th>PRCL_ID_UPM</th>";
				tabla += "<th>PRCL_CONS_PAIS</th>";
				tabla += "<th>PRCL_VEREDA</th>";
				tabla += "<th>PRCL_CORREGIMIENTO</th>";
				tabla += "<th>PRCL_INSPECCION_POLICIA</th>";
				tabla += "<th>PRCL_CASERIO</th>";
				tabla += "<th>PRCL_RANCHERIA</th>";
				tabla += "<th>PRCL_FECHA_CAPTURA</th>";
				tabla += "<th>PRCL_MEDIOACCESO_POBLADO</th>";
				tabla += "<th>PRCL_DISTANCIA_POBLADO</th>";
				tabla += "<th>PRCL_TPOBLADO_H</th>";
				tabla += "<th>PRCL_TPOBLADO_M</th>";
				tabla += "<th>PRCL_MEDIOACCESO_CAMPAMENTO</th>";
				tabla += "<th>PRCL_DISTANCIA_CAMPAMENTO</th>";
				tabla += "<th>PRCL_TCAMPAMENTO_H</th>";
				tabla += "<th>PRCL_TCAMPAMENTO_M</th>";
				tabla += "<th>PRCL_MEDIOACCESO_JALON</th>";
				tabla += "<th>PRCL_DISTANCIA_JALON</th>";
				tabla += "<th>PRCL_TJALON_H</th>";
				tabla += "<th>PRCL_TJALON_M</th>";
				//tabla += "<th>PRCL_COORDENADAS</th>";
				tabla += "<th>PRCL_DISTANCIA_CAMPAMENTOS</th>";
				tabla += "<th>PRCL_LATITUD</th>";
				tabla += "<th>PRCL_LONGITUD</th>";
				tabla += "<th>PRCL_ALTITUD</th>";
				tabla += "<th>PRCL_NOMBRE</th>";
				tabla += "<th>PRCL_USR_DILIGENCIA_F1</th>";
				tabla += "<th>PRCL_USR_DILIGENCIA_F2</th>";
				tabla += "<th>PRCL_FECHAINI_APROXIMACION</th>";
				tabla += "<th>PRCL_FECHAFIN_APROXIMACION</th>";
				tabla += "<th>PRCL_FECHAINI_LOCALIZACION</th>";
				tabla += "<th>PRCL_FECHAFIN_LOCALIZACION</th>";
				tabla += "<th>PRCL_DESCRIPCION</th>";
				tabla += "<th>PRCL_OBSERVACIONES</th>";
				tabla += "<th>PRCL_TRACKLOG_CAMPAMENTO</th>";
				tabla += "<th>PRCL_TRACKLOG_PARCELA</th>";
				tabla += "<th>PRCL_DEPARTAMENTO</th>";
				tabla += "<th>PRCL_MUNICIPIO</th>";			
				tabla += "<th>PRCL_SPF1_DILIGENCIA</th>";
				tabla += "<th>PRCL_SPF1_FECHAINI</th>";
				tabla += "<th>PRCL_SPF1_FECHAFIN</th>";
				tabla += "<th>PRCL_SPF1_POSIBLE</th>";
				tabla += "<th>PRCL_SPF1_JUSTIFICACION_NO</th>";
				tabla += "<th>PRCL_SPF1_OBS_FUSTALES</th>";
				tabla += "<th>PRCL_SPF1_OBS_LATIZALES</th>";
				tabla += "<th>PRCL_SPF1_OBS_BRINZALES</th>";
				tabla += "<th>PRCL_SPF2_DILIGENCIA</th>";
				tabla += "<th>PRCL_SPF2_FECHAINI</th>";
				tabla += "<th>PRCL_SPF2_FECHAFIN</th>";
				tabla += "<th>PRCL_SPF2_POSIBLE</th>";
				tabla += "<th>PRCL_SPF2_JUSTIFICACION_NO</th>";
				tabla += "<th>PRCL_SPF2_OBS_FUSTALES</th>";
				tabla += "<th>PRCL_SPF2_OBS_LATIZALES</th>";
				tabla += "<th>PRCL_SPF2_OBS_BRINZALES</th>";
				tabla += "<th>PRCL_SPF3_DILIGENCIA</th>";
				tabla += "<th>PRCL_SPF3_FECHAINI</th>";
				tabla += "<th>PRCL_SPF3_FECHAFIN</th>";
				tabla += "<th>PRCL_SPF3_POSIBLE</th>";
				tabla += "<th>PRCL_SPF3_JUSTIFICACION_NO</th>";
				tabla += "<th>PRCL_SPF3_OBS_FUSTALES</th>";
				tabla += "<th>PRCL_SPF3_OBS_LATIZALES</th>";
				tabla += "<th>PRCL_SPF3_OBS_BRINZALES</th>";
				tabla += "<th>PRCL_SPF4_DILIGENCIA</th>";
				tabla += "<th>PRCL_SPF4_FECHAINI</th>";
				tabla += "<th>PRCL_SPF4_FECHAFIN</th>";
				tabla += "<th>PRCL_SPF4_POSIBLE</th>";
				tabla += "<th>PRCL_SPF4_JUSTIFICACION_NO</th>";
				tabla += "<th>PRCL_SPF4_OBS_FUSTALES</th>";
				tabla += "<th>PRCL_SPF4_OBS_LATIZALES</th>";
				tabla += "<th>PRCL_SPF4_OBS_BRINZALES</th>";
				tabla += "<th>PRCL_SPF5_DILIGENCIA</th>";
				tabla += "<th>PRCL_SPF5_FECHAINI</th>";
				tabla += "<th>PRCL_SPF5_FECHAFIN</th>";
				tabla += "<th>PRCL_SPF5_POSIBLE</th>";
				tabla += "<th>PRCL_SPF5_JUSTIFICACION_NO</th>";
				tabla += "<th>PRCL_SPF5_OBS_FUSTALES</th>";
				tabla += "<th>PRCL_SPF5_OBS_LATIZALES</th>";
				tabla += "<th>PRCL_SPF5_OBS_BRINZALES</th>";
				tabla += "</tr>";

				while (rset.next())
				{
					PRIM_ID = rset.getString("PRIM_ID");
					PRIM_ARCH_CONSECUTIVO = rset.getString("PRIM_ARCH_CONSECUTIVO");
					PRIM_OBSERVACIONES_IMPORTACION = rset.getString("PRIM_OBSERVACIONES_IMPORTACION");
					PRIM_FECHAIMPORTACION = rset.getString("PRIM_FECHAIMPORTACION");
					
					PRIM_CONSECUTIVO = rset.getString("PRIM_CONSECUTIVO");
					PRIM_ID_IMPORTACION = rset.getString("PRIM_ID_IMPORTACION");
					PRIM_ID_UPM = rset.getString("PRIM_ID_UPM");
					PRIM_CONS_PAIS = rset.getString("PRIM_CONS_PAIS");
					PRIM_VEREDA = rset.getString("PRIM_VEREDA");
					PRIM_CORREGIMIENTO = rset.getString("PRIM_CORREGIMIENTO");
					PRIM_INSPECCION_POLICIA = rset.getString("PRIM_INSPECCION_POLICIA");
					PRIM_CASERIO = rset.getString("PRIM_CASERIO");
					PRIM_RANCHERIA = rset.getString("PRIM_RANCHERIA");
					PRIM_FECHA_CAPTURA = rset.getString("PRIM_FECHA_CAPTURA");
					PRIM_MEDIOACCESO_POBLADO = rset.getString("PRIM_MEDIOACCESO_POBLADO");
					PRIM_DISTANCIA_POBLADO = rset.getString("PRIM_DISTANCIA_POBLADO");
					PRIM_TPOBLADO_H = rset.getString("PRIM_TPOBLADO_H");
					PRIM_TPOBLADO_M = rset.getString("PRIM_TPOBLADO_M");
					PRIM_MEDIOACCESO_CAMPAMENTO = rset.getString("PRIM_MEDIOACCESO_CAMPAMENTO");
					PRIM_DISTANCIA_CAMPAMENTO = rset.getString("PRIM_DISTANCIA_CAMPAMENTO");
					PRIM_TCAMPAMENTO_H = rset.getString("PRIM_TCAMPAMENTO_H");
					PRIM_TCAMPAMENTO_M = rset.getString("PRIM_TCAMPAMENTO_M");
					PRIM_MEDIOACCESO_JALON = rset.getString("PRIM_MEDIOACCESO_JALON");
					PRIM_DISTANCIA_JALON = rset.getString("PRIM_DISTANCIA_JALON");
					PRIM_TJALON_H = rset.getString("PRIM_TJALON_H");
					PRIM_TJALON_M = rset.getString("PRIM_TJALON_M");
					PRIM_COORDENADAS = rset.getString("PRIM_COORDENADAS");
					PRIM_DISTANCIA_CAMPAMENTOS = rset.getString("PRIM_DISTANCIA_CAMPAMENTOS");
					PRIM_LATITUD = rset.getString("PRIM_LATITUD");
					PRIM_LONGITUD = rset.getString("PRIM_LONGITUD");
					PRIM_ALTITUD = rset.getString("PRIM_ALTITUD");
					PRIM_NOMBRE = rset.getString("PRIM_NOMBRE");
					PRIM_USR_DILIGENCIA_F1 = rset.getString("PRIM_USR_DILIGENCIA_F1");
					PRIM_USR_DILIGENCIA_F2 = rset.getString("PRIM_USR_DILIGENCIA_F2");
					PRIM_FECHAINI_APROXIMACION = rset.getString("PRIM_FECHAINI_APROXIMACION");
					PRIM_FECHAFIN_APROXIMACION = rset.getString("PRIM_FECHAFIN_APROXIMACION");
					PRIM_FECHAINI_LOCALIZACION = rset.getString("PRIM_FECHAINI_LOCALIZACION");
					PRIM_FECHAFIN_LOCALIZACION = rset.getString("PRIM_FECHAFIN_LOCALIZACION");
					PRIM_DESCRIPCION = rset.getString("PRIM_DESCRIPCION");
					PRIM_OBSERVACIONES = rset.getString("PRIM_OBSERVACIONES");
					PRIM_TRACKLOG_CAMPAMENTO = rset.getString("PRIM_TRACKLOG_CAMPAMENTO");
					PRIM_TRACKLOG_PARCELA = rset.getString("PRIM_TRACKLOG_PARCELA");
					PRIM_DEPARTAMENTO = rset.getString("PRIM_DEPARTAMENTO");
					PRIM_MUNICIPIO = rset.getString("PRIM_DEPARTAMENTO");			
					PRIM_SPF1_DILIGENCIA = rset.getString("PRIM_SPF1_DILIGENCIA");
					PRIM_SPF1_FECHAINI = rset.getString("PRIM_SPF1_FECHAINI");
					PRIM_SPF1_FECHAFIN = rset.getString("PRIM_SPF1_FECHAFIN");
					PRIM_SPF1_POSIBLE = rset.getString("PRIM_SPF1_POSIBLE");
					PRIM_SPF1_JUSTIFICACION_NO = rset.getString("PRIM_SPF1_JUSTIFICACION_NO");
					PRIM_SPF1_OBS_FUSTALES = rset.getString("PRIM_SPF1_OBS_FUSTALES");
					PRIM_SPF1_OBS_LATIZALES = rset.getString("PRIM_SPF1_OBS_FUSTALES");
					PRIM_SPF1_OBS_BRINZALES = rset.getString("PRIM_SPF1_OBS_BRINZALES");
					PRIM_SPF2_DILIGENCIA = rset.getString("PRIM_SPF2_DILIGENCIA");
					PRIM_SPF2_FECHAINI = rset.getString("PRIM_SPF2_FECHAINI");
					PRIM_SPF2_FECHAFIN = rset.getString("PRIM_SPF2_FECHAFIN");
					PRIM_SPF2_POSIBLE = rset.getString("PRIM_SPF2_POSIBLE");
					PRIM_SPF2_JUSTIFICACION_NO = rset.getString("PRIM_SPF2_JUSTIFICACION_NO");
					PRIM_SPF2_OBS_FUSTALES = rset.getString("PRIM_SPF2_OBS_FUSTALES");
					PRIM_SPF2_OBS_LATIZALES = rset.getString("PRIM_SPF2_OBS_LATIZALES");
					PRIM_SPF2_OBS_BRINZALES = rset.getString("PRIM_SPF2_OBS_LATIZALES");
					PRIM_SPF3_DILIGENCIA = rset.getString("PRIM_SPF3_DILIGENCIA");
					PRIM_SPF3_FECHAINI = rset.getString("PRIM_SPF3_FECHAINI");
					PRIM_SPF3_FECHAFIN = rset.getString("PRIM_SPF3_FECHAFIN");
					PRIM_SPF3_POSIBLE = rset.getString("PRIM_SPF3_POSIBLE");
					PRIM_SPF3_JUSTIFICACION_NO = rset.getString("PRIM_SPF3_JUSTIFICACION_NO");
					PRIM_SPF3_OBS_FUSTALES = rset.getString("PRIM_SPF3_OBS_FUSTALES");
					PRIM_SPF3_OBS_LATIZALES = rset.getString("PRIM_SPF3_OBS_LATIZALES");
					PRIM_SPF3_OBS_BRINZALES = rset.getString("PRIM_SPF3_OBS_BRINZALES");
					PRIM_SPF4_DILIGENCIA = rset.getString("PRIM_SPF4_DILIGENCIA");
					PRIM_SPF4_FECHAINI = rset.getString("PRIM_SPF4_DILIGENCIA");
					PRIM_SPF4_FECHAFIN = rset.getString("PRIM_SPF4_FECHAFIN");
					PRIM_SPF4_POSIBLE = rset.getString("PRIM_SPF4_POSIBLE");
					PRIM_SPF4_JUSTIFICACION_NO = rset.getString("PRIM_SPF4_JUSTIFICACION_NO");
					PRIM_SPF4_OBS_FUSTALES = rset.getString("PRIM_SPF4_OBS_FUSTALES");
					PRIM_SPF4_OBS_LATIZALES = rset.getString("PRIM_SPF4_OBS_LATIZALES");
					PRIM_SPF4_OBS_BRINZALES = rset.getString("PRIM_SPF4_OBS_BRINZALES");
					PRIM_SPF5_DILIGENCIA = rset.getString("PRIM_SPF5_DILIGENCIA");
					PRIM_SPF5_FECHAINI = rset.getString("PRIM_SPF5_FECHAINI");
					PRIM_SPF5_FECHAFIN = rset.getString("PRIM_SPF5_FECHAFIN");
					PRIM_SPF5_POSIBLE = rset.getString("PRIM_SPF5_POSIBLE");
					PRIM_SPF5_JUSTIFICACION_NO = rset.getString("PRIM_SPF5_JUSTIFICACION_NO");
					PRIM_SPF5_OBS_FUSTALES = rset.getString("PRIM_SPF5_OBS_FUSTALES");
					PRIM_SPF5_OBS_LATIZALES = rset.getString("PRIM_SPF5_OBS_LATIZALES");
					PRIM_SPF5_OBS_BRINZALES = rset.getString("PRIM_SPF5_OBS_BRINZALES");
					
					tabla += "<tr>";
					tabla += "<td>"+PRIM_ID+"</td>";
					tabla += "<td>"+PRIM_ARCH_CONSECUTIVO+"</td>";
					tabla += "<td>"+PRIM_OBSERVACIONES_IMPORTACION+"</td>";
					tabla += "<td>"+PRIM_FECHAIMPORTACION+"</td>";
					tabla += "<td>"+PRIM_CONSECUTIVO+"</td>";
					tabla += "<td>"+PRIM_ID_IMPORTACION+"</td>";
					tabla += "<td>"+PRIM_ID_UPM+"</td>";
					tabla += "<td>"+PRIM_CONS_PAIS+"</td>";
					tabla += "<td>"+PRIM_VEREDA+"</td>";
					tabla += "<td>"+PRIM_CORREGIMIENTO+"</td>";
					tabla += "<td>"+PRIM_INSPECCION_POLICIA+"</td>";
					tabla += "<td>"+PRIM_CASERIO+"</td>";
					tabla += "<td>"+PRIM_RANCHERIA+"</td>";
					tabla += "<td>"+PRIM_FECHA_CAPTURA+"</td>";
					tabla += "<td>"+PRIM_MEDIOACCESO_POBLADO+"</td>";
					tabla += "<td>"+PRIM_DISTANCIA_POBLADO+"</td>";
					tabla += "<td>"+PRIM_TPOBLADO_H+"</td>";
					tabla += "<td>"+PRIM_TPOBLADO_M+"</td>";
					tabla += "<td>"+PRIM_MEDIOACCESO_CAMPAMENTO+"</td>";
					tabla += "<td>"+PRIM_DISTANCIA_CAMPAMENTO+"</td>";
					tabla += "<td>"+PRIM_TCAMPAMENTO_H+"</td>";
					tabla += "<td>"+PRIM_TCAMPAMENTO_M+"</td>";
					tabla += "<td>"+PRIM_MEDIOACCESO_JALON+"</td>";
					tabla += "<td>"+PRIM_DISTANCIA_JALON+"</td>";
					tabla += "<td>"+PRIM_TJALON_H+"</td>";
					tabla += "<td>"+PRIM_TJALON_M+"</td>";
					//tabla += "<td>"+PRIM_COORDENADAS+"</td>";
					tabla += "<td>"+PRIM_DISTANCIA_CAMPAMENTOS+"</td>";
					tabla += "<td>"+PRIM_LATITUD+"</td>";
					tabla += "<td>"+PRIM_LONGITUD+"</td>";
					tabla += "<td>"+PRIM_ALTITUD+"</td>";
					tabla += "<td>"+PRIM_NOMBRE+"</td>";
					tabla += "<td>"+PRIM_USR_DILIGENCIA_F1+"</td>";
					tabla += "<td>"+PRIM_USR_DILIGENCIA_F2+"</td>";
					tabla += "<td>"+PRIM_FECHAINI_APROXIMACION+"</td>";
					tabla += "<td>"+PRIM_FECHAFIN_APROXIMACION+"</td>";
					tabla += "<td>"+PRIM_FECHAINI_LOCALIZACION+"</td>";
					tabla += "<td>"+PRIM_FECHAFIN_LOCALIZACION+"</td>";
					tabla += "<td>"+PRIM_DESCRIPCION+"</td>";
					tabla += "<td>"+PRIM_OBSERVACIONES+"</td>";
					tabla += "<td>"+PRIM_TRACKLOG_CAMPAMENTO+"</td>";
					tabla += "<td>"+PRIM_TRACKLOG_PARCELA+"</td>";
					tabla += "<td>"+PRIM_DEPARTAMENTO+"</td>";
					tabla += "<td>"+PRIM_MUNICIPIO+"</td>";			
					tabla += "<td>"+PRIM_SPF1_DILIGENCIA+"</td>";
					tabla += "<td>"+PRIM_SPF1_FECHAINI+"</td>";
					tabla += "<td>"+PRIM_SPF1_FECHAFIN+"</td>";
					tabla += "<td>"+PRIM_SPF1_POSIBLE+"</td>";
					tabla += "<td>"+PRIM_SPF1_JUSTIFICACION_NO+"</td>";
					tabla += "<td>"+PRIM_SPF1_OBS_FUSTALES+"</td>";
					tabla += "<td>"+PRIM_SPF1_OBS_LATIZALES+"</td>";
					tabla += "<td>"+PRIM_SPF1_OBS_BRINZALES+"</td>";
					tabla += "<td>"+PRIM_SPF2_DILIGENCIA+"</td>";
					tabla += "<td>"+PRIM_SPF2_FECHAINI+"</td>";
					tabla += "<td>"+PRIM_SPF2_FECHAFIN+"</td>";
					tabla += "<td>"+PRIM_SPF2_POSIBLE+"</td>";
					tabla += "<td>"+PRIM_SPF2_JUSTIFICACION_NO+"</td>";
					tabla += "<td>"+PRIM_SPF2_OBS_FUSTALES+"</td>";
					tabla += "<td>"+PRIM_SPF2_OBS_LATIZALES+"</td>";
					tabla += "<td>"+PRIM_SPF2_OBS_BRINZALES+"</td>";
					tabla += "<td>"+PRIM_SPF3_DILIGENCIA+"</td>";
					tabla += "<td>"+PRIM_SPF3_FECHAINI+"</td>";
					tabla += "<td>"+PRIM_SPF3_FECHAFIN+"</td>";
					tabla += "<td>"+PRIM_SPF3_POSIBLE+"</td>";
					tabla += "<td>"+PRIM_SPF3_JUSTIFICACION_NO+"</td>";
					tabla += "<td>"+PRIM_SPF3_OBS_FUSTALES+"</td>";
					tabla += "<td>"+PRIM_SPF3_OBS_LATIZALES+"</td>";
					tabla += "<td>"+PRIM_SPF3_OBS_BRINZALES+"</td>";
					tabla += "<td>"+PRIM_SPF4_DILIGENCIA+"</td>";
					tabla += "<td>"+PRIM_SPF4_FECHAINI+"</td>";
					tabla += "<td>"+PRIM_SPF4_FECHAFIN+"</td>";
					tabla += "<td>"+PRIM_SPF4_POSIBLE+"</td>";
					tabla += "<td>"+PRIM_SPF4_JUSTIFICACION_NO+"</td>";
					tabla += "<td>"+PRIM_SPF4_OBS_FUSTALES+"</td>";
					tabla += "<td>"+PRIM_SPF4_OBS_LATIZALES+"</td>";
					tabla += "<td>"+PRIM_SPF4_OBS_BRINZALES+"</td>";
					tabla += "<td>"+PRIM_SPF5_DILIGENCIA+"</td>";
					tabla += "<td>"+PRIM_SPF5_FECHAINI+"</td>";
					tabla += "<td>"+PRIM_SPF5_FECHAFIN+"</td>";
					tabla += "<td>"+PRIM_SPF5_POSIBLE+"</td>";
					tabla += "<td>"+PRIM_SPF5_JUSTIFICACION_NO+"</td>";
					tabla += "<td>"+PRIM_SPF5_OBS_FUSTALES+"</td>";
					tabla += "<td>"+PRIM_SPF5_OBS_LATIZALES+"</td>";
					tabla += "<td>"+PRIM_SPF5_OBS_BRINZALES+"</td>";
					tabla += "</tr>";
					
				}
				
				tabla += "</table>";
				tabla += "</div>";
				
				rset.close();
				
				r=tabla;
			}
			else
			{
				r += "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
			}
		} catch (SQLException e) {
			r += "Excepción de SQL ["+sql+"]: " + e.toString();
		} catch (Exception e) {
			r += "Ocurrió la siguiente excepción en detalleArchivo(): " + e.toString() + " -- SQL: " + sql;
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	
	/**
	 * Método para eliminar un archivo registrado
	 * 
	 * @param carpetaImportacion
	 * @return String con resultado de la operación
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String eliminarArchivo(String consecutivo_archivo, HttpServletRequest request)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "eliminarArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String resultado = "";
		String ARCH_CARPETA = dbREDD.obtenerDato("SELECT ARCH_CARPETA FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		String ARCH_NOMBRE = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		String nombre_archivo = ARCH_CARPETA + "/" + ARCH_NOMBRE;
		
		if (nombre_archivo.length()>0) {
			boolean resultado_eliminar_registro = dbREDD.ejecutarSQL("DELETE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);
			if (resultado_eliminar_registro) {
				resultado += Auxiliar.mensaje("confirmacion", "Registro eliminado de la base de datos", usuario, metodo);
			} 
			else {
				resultado += Auxiliar.mensaje("error", "No se pudo eliminar el registro de la base de datos", usuario, metodo);
			}

			File f = new File(nombre_archivo);
			if (f.exists()) {
				if (f.delete()) {
					resultado += Auxiliar.mensaje("confirmacion", "Archivo eliminado exitosamente.", usuario, metodo);
					sec.registrarTransaccion(request, 216, consecutivo_archivo, nombre_archivo, true);
				}
				else {
					resultado += Auxiliar.mensaje("error", "No se pudo eliminar el archivo", usuario, metodo);
					sec.registrarTransaccion(request, 216, consecutivo_archivo, nombre_archivo, false);
				}
			}
			else {
				resultado += Auxiliar.mensaje("advertencia", "El archivo no fue eliminado puesto que no existe.", usuario, metodo);
			}
		}
		else {
			resultado += Auxiliar.mensaje("advertencia", "No se especificó la carpeta!", usuario, metodo);
		}
		
		dbREDD.desconectarse();
		
		return resultado;
	}
	
	
	/**
	 * Procesa un archivo de importación
	 * 
	 * @param consecutivo_archivo
	 * @return String con el resultado de la operación
	 * @throws Exception 
	 */
	private String procesarArchivo(String consecutivo_archivo, String PRCL_CONSECUTIVO, String INDV_CONSECUTIVO, HttpServletRequest request)
	throws Exception {
		String metodo = yo + "procesarArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();

		HttpSession session = request.getSession();
	    
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		String sql = "";
		
		boolean procesar = true;
		
		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Obteniendo la ruta del archivo...");
		// Obtener la ruta del archivo
		String ruta_archivo = "";
		try {
			ruta_archivo = obtenerRutaArchivo(consecutivo_archivo, request);
		} catch (Exception e) {
			r += Auxiliar.mensaje("error", "Inconveniente al obtener la ruta del archivo. " + e.toString() + ". Ultimo error:" + dbREDD.ultimoError, usuario, metodo);
			procesar = false;
		}

		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Validando si el archivo está registrado...");
		// Validar si el archivo esta registrado
		if (ruta_archivo.equals("")) {
			r += Auxiliar.mensaje("error", "El archivo de consecutivo " + consecutivo_archivo + " no esta registrado en la base de datos.", usuario, metodo);
			procesar = false;
		}	
		
		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Validando si el archivo existe...");
		// Validar si el archivo existe
		if (!archie.existeArchivo(ruta_archivo)) {
			r += Auxiliar.mensaje("error", "El archivo " + ruta_archivo + " no fue encontrado.", usuario, metodo);
			procesar = false;
		}		
		
		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Consultando el tipo de archivo...");
		// Obtener tipo de archivo
		
		String tipo = "";
		
		try {
			sql = "SELECT ARCH_TIPO FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO="+consecutivo_archivo;
			tipo = dbREDD.obtenerDato(sql, "");
		} catch (Exception e) {
			r += Auxiliar.mensaje("error", "Inconveniente al obtener el tipo del archivo. ["+sql+"]: " + e.toString() + ". Ultimo error:" + dbREDD.ultimoError, usuario, metodo);
			procesar = false;
		}

		if (!procesar) {
		    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='"+r+"' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
		    
		    try {
		    	dbREDD.ejecutarSQL(sql_registro_estado);
		    }
		    catch (Exception e) {
		    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
		    }

			dbREDD.desconectarse();
			return r;
		}
		
		// Si es un archivo de individuos llamar a importarIndividuos

		if (tipo.equals("INDIVIDUOS")) {
			try {
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Llamando a importarIndividuos...");
				r += importarIndividuos(consecutivo_archivo, PRCL_CONSECUTIVO, request);
				recalcularEstadisticasArchivo(consecutivo_archivo, session);
				sec.registrarTransaccion(request, 211, consecutivo_archivo, "", true);
			} catch (Exception e) {
				r += Auxiliar.mensaje("error", "Inconveniente al importar los individuos del archivo " + ruta_archivo + ".  Depuracion:" + ". Excepcion: " + e.toString() + ". Ultimo error de la bd: " + dbREDD.ultimoError, usuario, metodo);
				//return r;
			}
		}
		
		if (tipo.equals("TALLOS")) {
			try {
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Llamando a importarTallos...");
				r += importarTallos(consecutivo_archivo, INDV_CONSECUTIVO, request);
				recalcularEstadisticasArchivo(consecutivo_archivo, session);
				sec.registrarTransaccion(request, 212, consecutivo_archivo, "", true);
			} catch (Exception e) {
				r += Auxiliar.mensaje("error", "Inconveniente al importar los tallos del archivo " + ruta_archivo + ".  Depuracion:" + ". Excepcion: " + e.toString() + ". Ultimo error de la bd: " + dbREDD.ultimoError, usuario, metodo);
				//return r;
			}
		}
		
		// Si es un archivo de parcelas llamar a importarParcelas
		
		if (tipo.equals("PARCELAS")) {
			try {
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Llamando a importarParcelas...");
				r += importarParcelas(consecutivo_archivo, request);
				recalcularEstadisticasArchivo(consecutivo_archivo, session);
				sec.registrarTransaccion(request, 210, consecutivo_archivo, "", true);
			} catch (Exception e) {
				r += Auxiliar.mensaje("error", "Inconveniente al importar las parcelas del archivo " + ruta_archivo + ".  Depuracion:" + ". Excepcion: " + e.toString() + ". Ultimo error de la bd: " + dbREDD.ultimoError, usuario, metodo);
				//return r;
			}
		}
		
		// Si es un archivo de imagenes de parcelas llamar a importarImagenesParcelas
		if (tipo.equals("IMAGENES_PARCELAS")) {
			try {
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Llamando a importarImagenesParcelas...");
				r += importarImagenesParcelas(consecutivo_archivo, request);
			} catch (Exception e) {
				r += Auxiliar.mensaje("error", "Inconveniente al importar las imagenes de las parcelas desde el archivo " + ruta_archivo + ": " + e.toString(), usuario, metodo);
				dbREDD.desconectarse();
				sec.registrarTransaccion(request, 213, "", "excepcion al importar archivo de imágenes de parcela:"+e.toString(), false);
				return r;
			}
		}
		
		// Si es un archivo de imagenes de inviduos llamar a importarImagenesIndividuos
		if (tipo.equals("IMAGENES_INDIVIDUOS")) {
			try {
				System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Llamando a importarImagenesIndividuo...");
				r += importarImagenesIndividuos(consecutivo_archivo, request);
			} catch (Exception e) {
				r += Auxiliar.mensaje("error", "Inconveniente al importar las imagenes de los individuos desde el archivo " + ruta_archivo + ": " + e.toString(), usuario, metodo);
				dbREDD.desconectarse();
				sec.registrarTransaccion(request, 214, "", "excepcion al importar archivo de imágenes de individuos:"+e.toString(), false);
				return r;
			}
		}
		
		
		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Comenzando a enviar e-mail de aviso...");
		// ENVIAR E-MAIL DE AVISO
		
		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Inicializando cartero...");
		Cartero miCartero = new Cartero();
		
        String fromname_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='fromname'", "Sistema de Inventarios Forestales del IDEAM");
        String fromaddress_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='fromaddress'", "avisosREDD@localhost.localdomain");
        String servidor_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='servidor_smtp'", "localhost");
        String protocolo_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='protocolo_autenticacion_smtp'", "PLAIN");
        String autenticar_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='autenticar_smtp'", "false");
        String usuario_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='usuario_smtp'", "");
        String clave_smtp = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='clave_smtp'", "");
        int puerto_smtp = Integer.parseInt(dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='puerto_smtp'", "25"));
		
        miCartero.establecerFromname(fromname_smtp);
        miCartero.establecerFromaddress(fromaddress_smtp);
        miCartero.establecerServidor(servidor_smtp);
        miCartero.establecerProtocolo(protocolo_smtp);
        miCartero.establecerAutenticar(autenticar_smtp);
        miCartero.establecerUsuario(usuario_smtp);
        miCartero.establecerClave(clave_smtp);
		miCartero.establecerPuerto(puerto_smtp);
        
		// obtener destinatario usuario
		
		String nombre_usuario = dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_CONSECUTIVO=" + usuario, "");
		String email_usuario = dbREDD.obtenerDato("SELECT USR_CORREOELECTRONIC FROM RED_USUARIO WHERE USR_CONSECUTIVO=" + usuario, "");
		
		String destinatario_usuario = nombre_usuario + "·" + email_usuario;
		String[] a_destinatarios_cc = new String[] {destinatario_usuario};
		ArrayList<String> a_destinatarios_to = new ArrayList<String>();
		

		// obtener destinatarios admisn temáticos
		
		String sql_admins_tematicos = "SELECT USR_NOMBRE, USR_CORREOELECTRONIC FROM RED_USUARIO WHERE USR_CONSECUTIVO IN (SELECT RLUS_CONS_USUARIO FROM RED_USUARIO_ROL WHERE RLUS_CONS_ROL IN (2))";
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql_admins_tematicos);
			
			if (rset != null)
			{
				String USR_NOMBRE = "";
				String USR_CORREOELECTRONIC = "";
				
				String destinatario_usuario_admin_tematico = "";
				
				while (rset.next())
				{
					USR_NOMBRE = rset.getString("USR_NOMBRE");
					USR_CORREOELECTRONIC = rset.getString("USR_CORREOELECTRONIC");

					destinatario_usuario_admin_tematico = USR_NOMBRE + "·" + USR_CORREOELECTRONIC;
					a_destinatarios_to.add(destinatario_usuario_admin_tematico);
				}
				
				rset.close();
			}
			else
			{
				t = Auxiliar.mensaje("advertencia", "El conjunto de resultados retornados para la consulta ["+sql_admins_tematicos+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError, usuario, metodo);
			}
		} catch (SQLException e) {
			t = Auxiliar.mensaje("error", "Excepción de SQL ["+sql_admins_tematicos+"]: " + e.toString(), usuario, metodo);
		} catch (Exception e) {
			t = Auxiliar.mensaje("error", "Ocurrió la siguiente excepción en subirArchivo(): " + e.toString() + " -- SQL: " + sql_admins_tematicos, usuario, metodo);
		}

		String[] a_destinatarios_bcc = new String[] {};

		String nombre_archivo = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO="+consecutivo_archivo, "");
		
		String asunto_email = "REDD - Archivo de " + tipo + " procesado.";
		String mensaje_email = "Se procesó un archivo de " + tipo + " con nombre: " + nombre_archivo + "<br>El resultado del procesamiento fue: <br>" + r;
		
		String respuesta_cartero = "";
		
		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Llamando enviarMail_CodeMonkey...");
		respuesta_cartero = miCartero.enviarMail_CodeMonkey(asunto_email, a_destinatarios_to, a_destinatarios_cc, a_destinatarios_bcc, mensaje_email, true);
		
		r += Auxiliar.mensaje("nota", "Resultado del envío del email: " + respuesta_cartero, usuario, metodo);

		dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO=ARCH_ESTADO || " + r.replace("'", "`") + " WHERE ARCH_CONSECUTIVO="+consecutivo_archivo);
		
		dbREDD.desconectarse();
		return r;
	}	
	
	/**
	 * Importa parcelas desde un archivo
	 * 
	 * @param consecutivo_archivo
	 * @return String resultado de la operación
	 * @throws Exception 
	 */
	public String importarParcelas(String consecutivo_archivo, HttpServletRequest request)
	throws Exception {
		String metodo = yo + "importarParcelas";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();

	    HttpSession session = request.getSession();

		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";

		// Obtener la ruta del archivo
		String ruta_archivo = ""+"";
		boolean procesar = true;

		try {
			ruta_archivo = obtenerRutaArchivo(consecutivo_archivo, request);
		} catch (Exception e) {
			r += Auxiliar.mensaje("error", "Inconveniente al obtener la ruta del archivo. " + e.toString() + ". Último error en la bd:" + dbREDD.ultimoError, usuario, metodo);
			procesar = false;
			
		}
		

		// VERIFICAR QUE EL ARCHIVO EXISTA
		if (!archie.existeArchivo(ruta_archivo)) {
			r += Auxiliar.mensaje("error", "No existe el archivo de parcelas a importar (" + ruta_archivo + "). ", usuario, metodo);
			procesar = false;
			
		}
	
		
		// INICIAR PROCESAMIENTO DEL ARCHIVO LINEA POR LINEA

		String [] a_campos = null;
		String [] a_valores = null;
		
		String ACCION = "";
		
		//String IDIM_ARCHIVO = consecutivo_archivo;
		
		// CAMPOS DE LA TABLA DE IMPORTACIÓN DE PARCELAS
		String PRCL_ARCH_CONSECUTIVO = consecutivo_archivo;
		String PRCL_OBSERVACIONES_IMPORTACION = "";
		
		String PRCL_CONSECUTIVO = "";
		String PRCL_ID_IMPORTACION = "";
		String PRCL_ID_UPM = "";
		String PRCL_CONS_PAIS = "";
		String PRCL_VEREDA = "";
		String PRCL_CORREGIMIENTO = "";
		String PRCL_INSPECCION_POLICIA = "";
		String PRCL_CASERIO = "";
		String PRCL_RANCHERIA = "";
		String PRCL_FECHA_CAPTURA = "";
		String PRCL_MEDIOACCESO_POBLADO = "";
		String PRCL_DISTANCIA_POBLADO = "";
		String PRCL_TPOBLADO_H = "";
		String PRCL_TPOBLADO_M = "";
		String PRCL_MEDIOACCESO_CAMPAMENTO = "";
		String PRCL_DISTANCIA_CAMPAMENTO = "";
		String PRCL_TCAMPAMENTO_H = "";
		String PRCL_TCAMPAMENTO_M = "";
		String PRCL_MEDIOACCESO_JALON = "";
		String PRCL_DISTANCIA_JALON = "";
		String PRCL_TJALON_H = "";
		String PRCL_TJALON_M = "";
		String PRCL_COORDENADAS = "";
		String PRCL_DISTANCIA_CAMPAMENTOS = "";
		String PRCL_LATITUD = "";
		String PRCL_LONGITUD = "";
		String PRCL_ALTITUD = "";
		String PRCL_NOMBRE = "";
		String PRCL_USR_DILIGENCIA_F1 = "";
		String PRCL_USR_DILIGENCIA_F2 = "";
		String PRCL_FECHAINI_APROXIMACION = "";
		String PRCL_FECHAFIN_APROXIMACION = "";
		String PRCL_FECHAINI_LOCALIZACION = "";
		String PRCL_FECHAFIN_LOCALIZACION = "";
		String PRCL_DESCRIPCION = "";
		String PRCL_OBSERVACIONES = "";
		String PRCL_TRACKLOG_CAMPAMENTO = "";
		String PRCL_TRACKLOG_PARCELA = "";
		String PRCL_DEPARTAMENTO = "";
		String PRCL_MUNICIPIO = "";			
		String PRCL_SPF1_DILIGENCIA = "";
		String PRCL_SPF1_FECHAINI = "";
		String PRCL_SPF1_FECHAFIN = "";
		String PRCL_SPF1_POSIBLE = "";
		String PRCL_SPF1_JUSTIFICACION_NO = "";
		String PRCL_SPF1_OBS_FUSTALES = "";
		String PRCL_SPF1_OBS_LATIZALES = "";
		String PRCL_SPF1_OBS_BRINZALES = "";
		String PRCL_SPF2_DILIGENCIA = "";
		String PRCL_SPF2_FECHAINI = "";
		String PRCL_SPF2_FECHAFIN = "";
		String PRCL_SPF2_POSIBLE = "";
		String PRCL_SPF2_JUSTIFICACION_NO = "";
		String PRCL_SPF2_OBS_FUSTALES = "";
		String PRCL_SPF2_OBS_LATIZALES = "";
		String PRCL_SPF2_OBS_BRINZALES = "";
		String PRCL_SPF3_DILIGENCIA = "";
		String PRCL_SPF3_FECHAINI = "";
		String PRCL_SPF3_FECHAFIN = "";
		String PRCL_SPF3_POSIBLE = "";
		String PRCL_SPF3_JUSTIFICACION_NO = "";
		String PRCL_SPF3_OBS_FUSTALES = "";
		String PRCL_SPF3_OBS_LATIZALES = "";
		String PRCL_SPF3_OBS_BRINZALES = "";
		String PRCL_SPF4_DILIGENCIA = "";
		String PRCL_SPF4_FECHAINI = "";
		String PRCL_SPF4_FECHAFIN = "";
		String PRCL_SPF4_POSIBLE = "";
		String PRCL_SPF4_JUSTIFICACION_NO = "";
		String PRCL_SPF4_OBS_FUSTALES = "";
		String PRCL_SPF4_OBS_LATIZALES = "";
		String PRCL_SPF4_OBS_BRINZALES = "";
		String PRCL_SPF5_DILIGENCIA = "";
		String PRCL_SPF5_FECHAINI = "";
		String PRCL_SPF5_FECHAFIN = "";
		String PRCL_SPF5_POSIBLE = "";
		String PRCL_SPF5_JUSTIFICACION_NO = "";
		String PRCL_SPF5_OBS_FUSTALES = "";
		String PRCL_SPF5_OBS_LATIZALES = "";
		String PRCL_SPF5_OBS_BRINZALES = "";
		
		String PRCL_PLOT = "";
		String PRCL_AREA = "";
		String PRCL_TEMPORALIDAD = "";
		String PRCL_PUBLICA = "";
		String PRCL_HAB = "";
		String PRCL_DAP = "";
		String PRCL_GPS = "";
		String PRCL_EQ = "";
		String PRCL_BA = "";
		String PRCL_BS = "";
		String PRCL_BT = "";
		String PRCL_AUTORCUSTODIOINFO = "";
		String PRCL_TIPOBOSQUE = "";
		String PRCL_INCLUIR = "";
		String PRCL_ACTUALIZACION = "";
		
		String sql_prim = "";
		String sql_tmp = "";
		String conteo = "0";
		
		String sql_eliminar = "";
		

		
		// ABRIMOS EL ARCHIVO PARA PROCESARLO LINEA POR LINEA UTILIZANDO UN BUFFEREDREADER
		
		InputStream inputStream = new FileInputStream(ruta_archivo);
		Reader reader = new InputStreamReader(inputStream, Charset.forName("windows-1252"));
		BufferedReader br = new BufferedReader(reader);

		int c = -1;
		int v = -1;
		
		try {
	        String linea = br.readLine();
	        
	        if (Auxiliar.nz(linea, "").equals("")) {
				r += Auxiliar.mensaje("advertencia", "Línea de títulos de campos no encontrada.", usuario, metodo);
				procesar = false;
	        }

	        int n_campos = 95;
	        
	        a_campos = linea.split(s);
			int a_campos_length = a_campos.length;

	        if (a_campos.length != n_campos) {
				r += Auxiliar.mensaje("advertencia", "Línea de títulos de campos no válida. Número de campos encontrados:" + a_campos.length, usuario, metodo);
				procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ACCION")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título ACCION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CONSECUTIVO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título CONSECUTIVO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ID_IMPORTACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título ID_IMPORTACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ID_UPM")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título ID_UPM. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("PAIS")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título PAIS. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("VEREDA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título VEREDA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CORREGIMIENTO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título CORREGIMIENTO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("INSPECCION_POLICIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título INSPECCION_POLICIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CASERIO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título CASERIO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("RANCHERIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título RANCHERIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FECHA_CAPTURA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título FECHA_CAPTURA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("MEDIOACCESO_POBLADO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título MEDIOACCESO_POBLADO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DISTANCIA_POBLADO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DISTANCIA_POBLADO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TPOBLADO_H")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TPOBLADO_H. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TPOBLADO_M")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TPOBLADO_M. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("MEDIOACCESO_CAMPAMENTO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título MEDIOACCESO_CAMPAMENTO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DISTANCIA_CAMPAMENTO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DISTANCIA_CAMPAMENTO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TCAMPAMENTO_H")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TCAMPAMENTO_H. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TCAMPAMENTO_M")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TCAMPAMENTO_M. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("MEDIOACCESO_JALON")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título MEDIOACCESO_JALON. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DISTANCIA_JALON")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DISTANCIA_JALON. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TJALON_H")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TJALON_H. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TJALON_M")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TJALON_M. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        /*
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("COORDENADAS")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título COORDENADAS. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        */

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DISTANCIA_CAMPAMENTOS")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DISTANCIA_CAMPAMENTOS. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("LATITUD")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título LATITUD. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("LONGITUD")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título LONGITUD. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
        	if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ALTITUD")) {
        		r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título ALTITUD. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
        		procesar = false;
        	}
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("NOMBRE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título NOMBRE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("USR_DILIGENCIA_F1")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título USR_DILIGENCIA_F1. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("USR_DILIGENCIA_F2")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título USR_DILIGENCIA_F2. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FECHAINI_APROXIMACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título FECHAINI_APROXIMACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FECHAFIN_APROXIMACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título FECHAFIN_APROXIMACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FECHAINI_LOCALIZACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título FECHAINI_LOCALIZACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FECHAFIN_LOCALIZACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título FECHAFIN_LOCALIZACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DESCRIPCION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DESCRIPCION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("OBSERVACIONES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título OBSERVACIONES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TRACKLOG_CAMPAMENTO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TRACKLOG_CAMPAMENTO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TRACKLOG_PARCELA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TRACKLOG_PARCELA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DEPARTAMENTO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DEPARTAMENTO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
        		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("MUNICIPIO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título MUNICIPIO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	        	procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_DILIGENCIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_DILIGENCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_FECHAINI")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_FECHAINI. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_FECHAFIN")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_FECHAFIN. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_POSIBLE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_POSIBLE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_JUSTIFICACION_NO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_JUSTIFICACION_NO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_OBS_FUSTALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_OBS_FUSTALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_OBS_LATIZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_OBS_LATIZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF1_OBS_BRINZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF1_OBS_BRINZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_DILIGENCIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_DILIGENCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_FECHAINI")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_FECHAINI. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_FECHAFIN")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_FECHAFIN. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_POSIBLE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_POSIBLE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_JUSTIFICACION_NO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_JUSTIFICACION_NO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_OBS_FUSTALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_OBS_FUSTALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_OBS_LATIZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_OBS_LATIZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF2_OBS_BRINZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF2_OBS_BRINZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_DILIGENCIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_DILIGENCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_FECHAINI")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_FECHAINI. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_FECHAFIN")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_FECHAFIN. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_POSIBLE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_POSIBLE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_JUSTIFICACION_NO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_JUSTIFICACION_NO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_OBS_FUSTALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_OBS_FUSTALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_OBS_LATIZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_OBS_LATIZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF3_OBS_BRINZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF3_OBS_BRINZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_DILIGENCIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_DILIGENCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_FECHAINI")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_FECHAINI. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_FECHAFIN")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_FECHAFIN. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_POSIBLE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_POSIBLE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_JUSTIFICACION_NO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_JUSTIFICACION_NO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_OBS_FUSTALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_OBS_FUSTALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_OBS_LATIZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_OBS_LATIZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF4_OBS_BRINZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF4_OBS_BRINZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_DILIGENCIA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_DILIGENCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_FECHAINI")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_FECHAINI. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_FECHAFIN")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_FECHAFIN. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_POSIBLE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_POSIBLE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_JUSTIFICACION_NO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_JUSTIFICACION_NO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_OBS_FUSTALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_OBS_FUSTALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_OBS_LATIZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_OBS_LATIZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SPF5_OBS_BRINZALES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título SPF5_OBS_BRINZALES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("PLOT")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título PLOT. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("AREA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título AREA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TEMPORALIDAD")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TEMPORALIDAD. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("PUBLICA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título PUBLICA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("HAB")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título HAB. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DAP")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título DAP. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("GPS")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título GPS. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("EQ")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título EQ. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("BA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título BA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("BS")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título BS. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("BT")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título BT. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("AUTORCUSTODIOINFO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título AUTORCUSTODIOINFO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TIPOBOSQUE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título TIPOBOSQUE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("INCLUIR")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título INCLUIR. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ACTUALIZACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el título ACTUALIZACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
			if (!procesar) {
			    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='"+r+"' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
			    
			    try {
			    	dbREDD.ejecutarSQL(sql_registro_estado);
			    }
			    catch (Exception e) {
			    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
			    }

				dbREDD.desconectarse();
				return r;
			}
			else {
			    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='ARCHIVO VALIDADO EN SU FORMATO. PROCESANDO...' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
			    
			    try {
			    	dbREDD.ejecutarSQL(sql_registro_estado);
			    }
			    catch (Exception e) {
			    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
			    }
			}

	        long i = 0;
	        
			String str_ok_transaccion = "";
			String ok_transaccion = "";
			String [] a_ok_transaccion = null;
			boolean ok_validacion = false;
	        
	        String tmplinea = "";

		    dbREDD.ejecutarSQL("DELETE FROM RED_PARCELA_IMPORTACION WHERE PRIM_ARCH_CONSECUTIVO=" + PRCL_ARCH_CONSECUTIVO);
		    
		    boolean fin = false;

	        while (linea != null) {
	        	i++;
	        	v = -1;
	        	
	        	ok_validacion = true;
	        	
				tmplinea = br.readLine();

				linea = null;
				
				if (tmplinea != null) {
					try {
						linea = tmplinea;
					} catch (Exception e1){
						r += Auxiliar.mensaje("error", "Error al convertir linea a UTF-8:" + e1.toString(), usuario, metodo);
						break;
					}
				}
				
	            if (linea == null) {
	            	fin = true;
	            	break;
	            }
	            
	            String lini = linea.substring(0, 5);
	            
	            if (lini.equals("Poner") || lini.equals("Put E")) {
	            	continue;
	            }
	            
	    		PRCL_OBSERVACIONES_IMPORTACION = "";
	    		
	    		PRCL_CONSECUTIVO = "";
	    		PRCL_ID_IMPORTACION = "";
	    		PRCL_ID_UPM = "";
	    		PRCL_CONS_PAIS = "";
	    		PRCL_VEREDA = "";
	    		PRCL_CORREGIMIENTO = "";
	    		PRCL_INSPECCION_POLICIA = "";
	    		PRCL_CASERIO = "";
	    		PRCL_RANCHERIA = "";
	    		PRCL_FECHA_CAPTURA = "";
	    		PRCL_MEDIOACCESO_POBLADO = "";
	    		PRCL_DISTANCIA_POBLADO = "";
	    		PRCL_TPOBLADO_H = "";
	    		PRCL_TPOBLADO_M = "";
	    		PRCL_MEDIOACCESO_CAMPAMENTO = "";
	    		PRCL_DISTANCIA_CAMPAMENTO = "";
	    		PRCL_TCAMPAMENTO_H = "";
	    		PRCL_TCAMPAMENTO_M = "";
	    		PRCL_MEDIOACCESO_JALON = "";
	    		PRCL_DISTANCIA_JALON = "";
	    		PRCL_TJALON_H = "";
	    		PRCL_TJALON_M = "";
	    		PRCL_COORDENADAS = "";
	    		PRCL_DISTANCIA_CAMPAMENTOS = "";
	    		PRCL_LATITUD = "";
	    		PRCL_LONGITUD = "";
	    		PRCL_ALTITUD = "";
	    		PRCL_NOMBRE = "";
	    		PRCL_USR_DILIGENCIA_F1 = "";
	    		PRCL_USR_DILIGENCIA_F2 = "";
	    		PRCL_FECHAINI_APROXIMACION = "";
	    		PRCL_FECHAFIN_APROXIMACION = "";
	    		PRCL_FECHAINI_LOCALIZACION = "";
	    		PRCL_FECHAFIN_LOCALIZACION = "";
	    		PRCL_DESCRIPCION = "";
	    		PRCL_OBSERVACIONES = "";
	    		PRCL_TRACKLOG_CAMPAMENTO = "";
	    		PRCL_TRACKLOG_PARCELA = "";
	    		PRCL_DEPARTAMENTO = "";
	    		PRCL_MUNICIPIO = "";			
	    		PRCL_SPF1_DILIGENCIA = "";
	    		PRCL_SPF1_FECHAINI = "";
	    		PRCL_SPF1_FECHAFIN = "";
	    		PRCL_SPF1_POSIBLE = "";
	    		PRCL_SPF1_JUSTIFICACION_NO = "";
	    		PRCL_SPF1_OBS_FUSTALES = "";
	    		PRCL_SPF1_OBS_LATIZALES = "";
	    		PRCL_SPF1_OBS_BRINZALES = "";
	    		PRCL_SPF2_DILIGENCIA = "";
	    		PRCL_SPF2_FECHAINI = "";
	    		PRCL_SPF2_FECHAFIN = "";
	    		PRCL_SPF2_POSIBLE = "";
	    		PRCL_SPF2_JUSTIFICACION_NO = "";
	    		PRCL_SPF2_OBS_FUSTALES = "";
	    		PRCL_SPF2_OBS_LATIZALES = "";
	    		PRCL_SPF2_OBS_BRINZALES = "";
	    		PRCL_SPF3_DILIGENCIA = "";
	    		PRCL_SPF3_FECHAINI = "";
	    		PRCL_SPF3_FECHAFIN = "";
	    		PRCL_SPF3_POSIBLE = "";
	    		PRCL_SPF3_JUSTIFICACION_NO = "";
	    		PRCL_SPF3_OBS_FUSTALES = "";
	    		PRCL_SPF3_OBS_LATIZALES = "";
	    		PRCL_SPF3_OBS_BRINZALES = "";
	    		PRCL_SPF4_DILIGENCIA = "";
	    		PRCL_SPF4_FECHAINI = "";
	    		PRCL_SPF4_FECHAFIN = "";
	    		PRCL_SPF4_POSIBLE = "";
	    		PRCL_SPF4_JUSTIFICACION_NO = "";
	    		PRCL_SPF4_OBS_FUSTALES = "";
	    		PRCL_SPF4_OBS_LATIZALES = "";
	    		PRCL_SPF4_OBS_BRINZALES = "";
	    		PRCL_SPF5_DILIGENCIA = "";
	    		PRCL_SPF5_FECHAINI = "";
	    		PRCL_SPF5_FECHAFIN = "";
	    		PRCL_SPF5_POSIBLE = "";
	    		PRCL_SPF5_JUSTIFICACION_NO = "";
	    		PRCL_SPF5_OBS_FUSTALES = "";
	    		PRCL_SPF5_OBS_LATIZALES = "";
	    		PRCL_SPF5_OBS_BRINZALES = "";
	    		PRCL_PLOT = "";
	    		PRCL_AREA = "";
	    		PRCL_TEMPORALIDAD = "";
	    		PRCL_PUBLICA = "";
	    		PRCL_HAB = "";
	    		PRCL_DAP = "";
	    		PRCL_GPS = "";
	    		PRCL_EQ = "";
	    		PRCL_BA = "";
	    		PRCL_BS = "";
	    		PRCL_BT = "";
	    		PRCL_AUTORCUSTODIOINFO = "";
	    		PRCL_TIPOBOSQUE = "";
	    		PRCL_INCLUIR = "";
	    		PRCL_ACTUALIZACION = "";
	            

				PRCL_ID_IMPORTACION = dbREDD.obtenerDato("SELECT COALESCE(MAX(PRIM_ID_IMPORTACION),0)+1 AS CONSECUTIVO FROM RED_PARCELA_IMPORTACION","");

				a_valores = linea.split(s);
		        
		        if (a_valores.length != n_campos) {
		        	PRCL_OBSERVACIONES_IMPORTACION += Auxiliar.mensaje("advertencia", "Linea "+i+" no válida. Numero de campos encontrados:" + a_valores.length, usuario, metodo);
		        	ok_validacion = false;
		        }
		        else {
					v++; ACCION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_CONSECUTIVO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_ID_IMPORTACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_ID_UPM = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_CONS_PAIS = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_VEREDA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_CORREGIMIENTO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_INSPECCION_POLICIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_CASERIO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_RANCHERIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_FECHA_CAPTURA = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_MEDIOACCESO_POBLADO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_DISTANCIA_POBLADO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_TPOBLADO_H = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_TPOBLADO_M = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_MEDIOACCESO_CAMPAMENTO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_DISTANCIA_CAMPAMENTO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_TCAMPAMENTO_H = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_TCAMPAMENTO_M = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_MEDIOACCESO_JALON = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_DISTANCIA_JALON = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_TJALON_H = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_TJALON_M = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					//v++; PRCL_COORDENADAS = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_DISTANCIA_CAMPAMENTOS = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_LATITUD = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_LONGITUD = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_ALTITUD = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_NOMBRE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_USR_DILIGENCIA_F1 = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_USR_DILIGENCIA_F2 = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_FECHAINI_APROXIMACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_FECHAFIN_APROXIMACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_FECHAINI_LOCALIZACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_FECHAFIN_LOCALIZACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_DESCRIPCION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_OBSERVACIONES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_TRACKLOG_CAMPAMENTO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_TRACKLOG_PARCELA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_DEPARTAMENTO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_MUNICIPIO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF1_DILIGENCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF1_FECHAINI = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF1_FECHAFIN = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF1_POSIBLE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF1_JUSTIFICACION_NO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF1_OBS_FUSTALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF1_OBS_LATIZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF1_OBS_BRINZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF2_DILIGENCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF2_FECHAINI = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF2_FECHAFIN = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF2_POSIBLE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF2_JUSTIFICACION_NO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF2_OBS_FUSTALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF2_OBS_LATIZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF2_OBS_BRINZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF3_DILIGENCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF3_FECHAINI = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF3_FECHAFIN = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF3_POSIBLE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF3_JUSTIFICACION_NO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF3_OBS_FUSTALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF3_OBS_LATIZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF3_OBS_BRINZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF4_DILIGENCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF4_FECHAINI = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF4_FECHAFIN = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF4_POSIBLE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF4_JUSTIFICACION_NO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF4_OBS_FUSTALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF4_OBS_LATIZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF4_OBS_BRINZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF5_DILIGENCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF5_FECHAINI = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF5_FECHAFIN = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");
					v++; PRCL_SPF5_POSIBLE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "NULL");
					v++; PRCL_SPF5_JUSTIFICACION_NO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF5_OBS_FUSTALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF5_OBS_LATIZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_SPF5_OBS_BRINZALES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_PLOT = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_AREA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_TEMPORALIDAD = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_PUBLICA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_HAB = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_DAP = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_GPS = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_EQ = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_BA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; PRCL_BS = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; PRCL_BT = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; PRCL_AUTORCUSTODIOINFO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_TIPOBOSQUE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_INCLUIR = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; PRCL_ACTUALIZACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  "").replace("'", ""), "");

					Parcela parce = new Parcela();
					
					if (ACCION.equals("ELIMINAR")) {
						if (!PRCL_ID_IMPORTACION.equals("")) {
							// VERIFICAR QUE LA PARCELA EXISTA
							try {
								sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_PARCELA WHERE PRCL_ID_IMPORTACION=" + PRCL_ID_IMPORTACION;
								conteo = dbREDD.obtenerDato(sql_tmp, "0");
							}
							catch (Exception e) {
								r += Auxiliar.mensaje("error", "Error al intentar encontrar individuo con el ID_IMPORTACION " + PRCL_ID_IMPORTACION + " para eliminarlo. SQL: [" + sql_tmp + "]. Último error en la bd de RED: " + dbREDD.ultimoError + ". Excepción: " + e.toString(), usuario, metodo);								
							}
							
							if (conteo.equals("0")) {
								r += Auxiliar.mensaje("advertencia", "No se encontró individuo alguno con el ID_IMPORTACION " + PRCL_ID_IMPORTACION, usuario, metodo);
							}
							else {
								try {
									
									str_ok_transaccion = parce.eliminar(PRCL_CONSECUTIVO, request);
									a_ok_transaccion = str_ok_transaccion.split("-=-");
									ok_transaccion = a_ok_transaccion[0];
									
									// CONFIRMAR QUE SE PUDO ELIMINAR
									dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_PROCESADOS=ARCH_PROCESADOS+1 WHERE ARCH_CONSECUTIVO="+consecutivo_archivo);
								}
								catch (SQLException e) {
									r += Auxiliar.mensaje("error", "Error al intentar eliminar la parcela en RED con el ID_IMPORTACION " + PRCL_ID_IMPORTACION + ". SQL: [" + sql_eliminar + "]. Último error en la bd: " + dbREDD.ultimoError + ". Excepción: " + e.toString(), usuario, metodo);
								}
							}
						} 
						else {
							r += Auxiliar.mensaje("advertencia", "Para poder eliminar una parcela es necesario especificar el ID_IMPORTACION.", usuario, metodo);
						}						
					}
					else {
						PRCL_OBSERVACIONES_IMPORTACION = "";
						
						if (ok_validacion) {
							if (PRCL_CONSECUTIVO.equals("")) {
								if (Auxiliar.tieneAlgo(PRCL_NOMBRE) && Auxiliar.tieneAlgo(PRCL_ID_UPM)) {
									sql_tmp = "SELECT PRCL_CONSECUTIVO FROM RED_PARCELA WHERE ";
									sql_tmp += " PRCL_NOMBRE='" + PRCL_NOMBRE + "'";
									sql_tmp += " AND PRCL_ID_UPM=" + PRCL_ID_UPM;
								}
								else if (Auxiliar.tieneAlgo(PRCL_ID_IMPORTACION)) {
									sql_tmp = "SELECT PRCL_CONSECUTIVO FROM RED_PARCELA WHERE ";
									sql_tmp += " PRCL_ID_IMPORTACION=" + PRCL_ID_IMPORTACION;
								}
								else {
									sql_tmp = "";
								}
								
								String PRCL_CONSECUTIVO_EXISTENTE = "";
								
								if (Auxiliar.tieneAlgo(sql_tmp)) {
									try {
										PRCL_CONSECUTIVO_EXISTENTE = dbREDD.obtenerDato(sql_tmp, "");
									} catch (Exception e) {
									}
								}
								
								if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO_EXISTENTE)) {
									PRCL_CONSECUTIVO = PRCL_CONSECUTIVO_EXISTENTE;
								}
							}
							
							if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
								PRCL_ID_IMPORTACION = dbREDD.obtenerDato("SELECT PRCL_ID_IMPORTACION FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "");
							}
							
							if (!Auxiliar.tieneAlgo(PRCL_ID_IMPORTACION)) {
								PRCL_ID_IMPORTACION = dbREDD.obtenerDato("SELECT COALESCE(MAX(PRCL_ID_IMPORTACION),0)+1 AS CONSECUTIVO FROM RED_PARCELA", "");
							}
							
							
							// VALIDAR PRCL_ACTUALIZACION
							String fechahora = "";
							String tz = "UTC";
							String fecha_actualizacion = "";
							if (Auxiliar.tieneAlgo(PRCL_ACTUALIZACION)) {
								String [] a_actualizacion = PRCL_ACTUALIZACION.split("@");
								fechahora = a_actualizacion[0].trim();
								if (a_actualizacion.length > 1) {
									tz = a_actualizacion[1];
								}
								
								if (!Auxiliar.fechaHoraEsValida(fechahora)) {
									t = Auxiliar.traducir("AVISO_VALIDACION.PRCL_ACTUALIZACION", idioma, "Fecha PRCL_ACTUALIZACION debe ser válida y estar en formato aaaa-mm-dd hh24:mi:ss@zona Ejemplo: 2013-10-13 21:29:44@-05:00.  Si importa desde excel, se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
								}
								else {
									if (Auxiliar.tieneAlgo(tz)) {
										fecha_actualizacion = "CAST(FROM_TZ(TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss'), '"+tz+"') AT TIME ZONE ('UTC') AS TIMESTAMP)";
									}
									else {
										fecha_actualizacion = "TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss')";
									}
								}
							}
							
							if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
								if (Auxiliar.tieneAlgo(PRCL_PLOT)) {
									PRCL_CONSECUTIVO = dbREDD.obtenerDato("SELECT PRCL_CONSECUTIVO FROM RED_PARCELA WHERE PRCL_PLOT="+PRCL_PLOT, "");
								}
							}	
							
							boolean guardar = true;
							// SI YA FUE ACTUALIZADA CON LA MISMA FECHA ENTONCES NO ACTUALIZAR
							if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
								String mensaje_conteo = "";
								conteo = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+" AND PRCL_ACTUALIZACION>=" + Auxiliar.nzVacio(fecha_actualizacion, "SYSDATE"), "0");
								if (!conteo.equals("0")) {
									mensaje_conteo = Auxiliar.mensaje("confirmacion", "Fila "+i+": obviando la parcela "+PRCL_CONSECUTIVO+" (Plot "+PRCL_PLOT+") que ya había sido actualizada en o después de:" + fechahora + "@" + tz, usuario, metodo);
									r += mensaje_conteo;
									guardar = false;
									str_ok_transaccion = "0-=-" + mensaje_conteo;
								}
								conteo = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO, "0");
								if (conteo.equals("0")) {
									mensaje_conteo = Auxiliar.mensaje("advertencia", "Fila "+i+": obviando la parcela "+PRCL_CONSECUTIVO+" (Plot "+PRCL_PLOT+") ya que no existe una parcela con ese código en la base de datos.", usuario, metodo);
									r += mensaje_conteo;
									guardar = false;
									str_ok_transaccion = "0-=-" + mensaje_conteo;
								}
							}
							
							if (guardar) {
								str_ok_transaccion = parce.guardar(
									PRCL_CONSECUTIVO,
									PRCL_ID_IMPORTACION,
									PRCL_ID_UPM,
									PRCL_CONS_PAIS,
									PRCL_VEREDA,
									PRCL_CORREGIMIENTO,
									PRCL_INSPECCION_POLICIA,
									PRCL_CASERIO,
									PRCL_RANCHERIA,
									PRCL_FECHA_CAPTURA,
									PRCL_MEDIOACCESO_POBLADO,
									PRCL_DISTANCIA_POBLADO,
									PRCL_TPOBLADO_H,
									PRCL_TPOBLADO_M,
									PRCL_MEDIOACCESO_CAMPAMENTO,
									PRCL_DISTANCIA_CAMPAMENTO,
									PRCL_TCAMPAMENTO_H,
									PRCL_TCAMPAMENTO_M,
									PRCL_MEDIOACCESO_JALON,
									PRCL_DISTANCIA_JALON,
									PRCL_TJALON_H,
									PRCL_TJALON_M,
									PRCL_COORDENADAS,
									PRCL_DISTANCIA_CAMPAMENTOS,
									PRCL_LATITUD,
									PRCL_LONGITUD,
									PRCL_ALTITUD,
									PRCL_NOMBRE,
									PRCL_USR_DILIGENCIA_F1,
									PRCL_USR_DILIGENCIA_F2,
									PRCL_FECHAINI_APROXIMACION,
									PRCL_FECHAFIN_APROXIMACION,
									PRCL_FECHAINI_LOCALIZACION,
									PRCL_FECHAFIN_LOCALIZACION,
									PRCL_DESCRIPCION,
									PRCL_OBSERVACIONES,
									PRCL_TRACKLOG_CAMPAMENTO,
									PRCL_TRACKLOG_PARCELA,
									PRCL_DEPARTAMENTO,
									PRCL_MUNICIPIO,			
									PRCL_SPF1_DILIGENCIA,
									PRCL_SPF1_FECHAINI,
									PRCL_SPF1_FECHAFIN,
									PRCL_SPF1_POSIBLE,
									PRCL_SPF1_JUSTIFICACION_NO,
									PRCL_SPF1_OBS_FUSTALES,
									PRCL_SPF1_OBS_LATIZALES,
									PRCL_SPF1_OBS_BRINZALES,
									PRCL_SPF2_DILIGENCIA,
									PRCL_SPF2_FECHAINI,
									PRCL_SPF2_FECHAFIN,
									PRCL_SPF2_POSIBLE,
									PRCL_SPF2_JUSTIFICACION_NO,
									PRCL_SPF2_OBS_FUSTALES,
									PRCL_SPF2_OBS_LATIZALES,
									PRCL_SPF2_OBS_BRINZALES,
									PRCL_SPF3_DILIGENCIA,
									PRCL_SPF3_FECHAINI,
									PRCL_SPF3_FECHAFIN,
									PRCL_SPF3_POSIBLE,
									PRCL_SPF3_JUSTIFICACION_NO,
									PRCL_SPF3_OBS_FUSTALES,
									PRCL_SPF3_OBS_LATIZALES,
									PRCL_SPF3_OBS_BRINZALES,
									PRCL_SPF4_DILIGENCIA,
									PRCL_SPF4_FECHAINI,
									PRCL_SPF4_FECHAFIN,
									PRCL_SPF4_POSIBLE,
									PRCL_SPF4_JUSTIFICACION_NO,
									PRCL_SPF4_OBS_FUSTALES,
									PRCL_SPF4_OBS_LATIZALES,
									PRCL_SPF4_OBS_BRINZALES,
									PRCL_SPF5_DILIGENCIA,
									PRCL_SPF5_FECHAINI,
									PRCL_SPF5_FECHAFIN,
									PRCL_SPF5_POSIBLE,
									PRCL_SPF5_JUSTIFICACION_NO,
									PRCL_SPF5_OBS_FUSTALES,
									PRCL_SPF5_OBS_LATIZALES,
									PRCL_SPF5_OBS_BRINZALES,
									consecutivo_archivo,
									PRCL_PLOT,
									PRCL_AREA,
									PRCL_INCLUIR,
									PRCL_TEMPORALIDAD,
									PRCL_PUBLICA,
									PRCL_HAB,
									PRCL_DAP,
									PRCL_GPS,
									PRCL_EQ,
									PRCL_BA,
									PRCL_BS,
									PRCL_BT,
									PRCL_AUTORCUSTODIOINFO,
									PRCL_TIPOBOSQUE,
									PRCL_ACTUALIZACION,
									request,
									this.RED_username, this.RED_password, this.RED_host, this.RED_port, this.RED_sid, true
									);
							}
							
							a_ok_transaccion = str_ok_transaccion.split("-=-");
							ok_transaccion = a_ok_transaccion[0];

							if (a_ok_transaccion.length == 2) {
								PRCL_OBSERVACIONES_IMPORTACION = a_ok_transaccion[1].toString();
							}

						}
						
						sql_prim = "INSERT INTO RED_PARCELA_IMPORTACION ";
						sql_prim += "(";
						sql_prim += "PRIM_ESTADOIMPORTACION,";
						sql_prim += "PRIM_OBSERVACIONES_IMPORTACION,";
						sql_prim += "PRIM_CONSECUTIVO,";
						sql_prim += "PRIM_ID_IMPORTACION,";
						sql_prim += "PRIM_ID_UPM,";
						sql_prim += "PRIM_CONS_PAIS,";
						sql_prim += "PRIM_VEREDA,";
						sql_prim += "PRIM_CORREGIMIENTO,";
						sql_prim += "PRIM_INSPECCION_POLICIA,";
						sql_prim += "PRIM_CASERIO,";
						sql_prim += "PRIM_RANCHERIA,";
						sql_prim += "PRIM_FECHA_CAPTURA,";
						sql_prim += "PRIM_MEDIOACCESO_POBLADO,";
						sql_prim += "PRIM_DISTANCIA_POBLADO,";
						sql_prim += "PRIM_TPOBLADO_H,";
						sql_prim += "PRIM_TPOBLADO_M,";
						sql_prim += "PRIM_MEDIOACCESO_CAMPAMENTO,";
						sql_prim += "PRIM_DISTANCIA_CAMPAMENTO,";
						sql_prim += "PRIM_TCAMPAMENTO_H,";
						sql_prim += "PRIM_TCAMPAMENTO_M,";
						sql_prim += "PRIM_MEDIOACCESO_JALON,";
						sql_prim += "PRIM_DISTANCIA_JALON,";
						sql_prim += "PRIM_TJALON_H,";
						sql_prim += "PRIM_TJALON_M,";
						sql_prim += "PRIM_COORDENADAS,";
						sql_prim += "PRIM_DISTANCIA_CAMPAMENTOS,";
						sql_prim += "PRIM_LATITUD,";
						sql_prim += "PRIM_LONGITUD,";
						sql_prim += "PRIM_ALTITUD,";
						sql_prim += "PRIM_NOMBRE,";
						sql_prim += "PRIM_USR_DILIGENCIA_F1,";
						sql_prim += "PRIM_USR_DILIGENCIA_F2,";
						sql_prim += "PRIM_FECHAINI_APROXIMACION,";
						sql_prim += "PRIM_FECHAFIN_APROXIMACION,";
						sql_prim += "PRIM_FECHAINI_LOCALIZACION,";
						sql_prim += "PRIM_FECHAFIN_LOCALIZACION,";
						sql_prim += "PRIM_DESCRIPCION,";
						sql_prim += "PRIM_OBSERVACIONES,";
						sql_prim += "PRIM_TRACKLOG_CAMPAMENTO,";
						sql_prim += "PRIM_TRACKLOG_PARCELA,";
						sql_prim += "PRIM_DEPARTAMENTO,";
						sql_prim += "PRIM_MUNICIPIO,";			
						sql_prim += "PRIM_SPF1_DILIGENCIA,";
						sql_prim += "PRIM_SPF1_FECHAINI,";
						sql_prim += "PRIM_SPF1_FECHAFIN,";
						sql_prim += "PRIM_SPF1_POSIBLE,";
						sql_prim += "PRIM_SPF1_JUSTIFICACION_NO,";
						sql_prim += "PRIM_SPF1_OBS_FUSTALES,";
						sql_prim += "PRIM_SPF1_OBS_LATIZALES,";
						sql_prim += "PRIM_SPF1_OBS_BRINZALES,";
						sql_prim += "PRIM_SPF2_DILIGENCIA,";
						sql_prim += "PRIM_SPF2_FECHAINI,";
						sql_prim += "PRIM_SPF2_FECHAFIN,";
						sql_prim += "PRIM_SPF2_POSIBLE,";
						sql_prim += "PRIM_SPF2_JUSTIFICACION_NO,";
						sql_prim += "PRIM_SPF2_OBS_FUSTALES,";
						sql_prim += "PRIM_SPF2_OBS_LATIZALES,";
						sql_prim += "PRIM_SPF2_OBS_BRINZALES,";
						sql_prim += "PRIM_SPF3_DILIGENCIA,";
						sql_prim += "PRIM_SPF3_FECHAINI,";
						sql_prim += "PRIM_SPF3_FECHAFIN,";
						sql_prim += "PRIM_SPF3_POSIBLE,";
						sql_prim += "PRIM_SPF3_JUSTIFICACION_NO,";
						sql_prim += "PRIM_SPF3_OBS_FUSTALES,";
						sql_prim += "PRIM_SPF3_OBS_LATIZALES,";
						sql_prim += "PRIM_SPF3_OBS_BRINZALES,";
						sql_prim += "PRIM_SPF4_DILIGENCIA,";
						sql_prim += "PRIM_SPF4_FECHAINI,";
						sql_prim += "PRIM_SPF4_FECHAFIN,";
						sql_prim += "PRIM_SPF4_POSIBLE,";
						sql_prim += "PRIM_SPF4_JUSTIFICACION_NO,";
						sql_prim += "PRIM_SPF4_OBS_FUSTALES,";
						sql_prim += "PRIM_SPF4_OBS_LATIZALES,";
						sql_prim += "PRIM_SPF4_OBS_BRINZALES,";
						sql_prim += "PRIM_SPF5_DILIGENCIA,";
						sql_prim += "PRIM_SPF5_FECHAINI,";
						sql_prim += "PRIM_SPF5_FECHAFIN,";
						sql_prim += "PRIM_SPF5_POSIBLE,";
						sql_prim += "PRIM_SPF5_JUSTIFICACION_NO,";
						sql_prim += "PRIM_SPF5_OBS_FUSTALES,";
						sql_prim += "PRIM_SPF5_OBS_LATIZALES,";
						sql_prim += "PRIM_SPF5_OBS_BRINZALES,";
						sql_prim += "PRIM_PLOT,";
						sql_prim += "PRIM_AREA,";
						sql_prim += "PRIM_TEMPORALIDAD,";
						sql_prim += "PRIM_PUBLICA,";
						sql_prim += "PRIM_HAB,";
						sql_prim += "PRIM_DAP,";
						sql_prim += "PRIM_GPS,";
						sql_prim += "PRIM_EQ,";
						sql_prim += "PRIM_BA,";
						sql_prim += "PRIM_BS,";
						sql_prim += "PRIM_BT,";
						sql_prim += "PRIM_AUTORCUSTODIOINFO,";
						sql_prim += "PRIM_TIPOBOSQUE,";
						sql_prim += "PRIM_INCLUIR,";
						sql_prim += "PRIM_ACTUALIZACION,";
						sql_prim += "PRIM_ARCH_CONSECUTIVO";
						sql_prim += ")";
						sql_prim += " VALUES ";
						sql_prim += "(";
						sql_prim += Auxiliar.nzVacio(a_ok_transaccion[0], "NULL") + ",";
						int longitud = (PRCL_OBSERVACIONES_IMPORTACION.length() < MAX_VARCHAR2) ? PRCL_OBSERVACIONES_IMPORTACION.length() : MAX_VARCHAR2;
						sql_prim += "'" + PRCL_OBSERVACIONES_IMPORTACION.substring(0, longitud).replace("'",  "`") + "',";
						sql_prim += Auxiliar.nzVacio(PRCL_CONSECUTIVO, "NULL") + ",";
						sql_prim += Auxiliar.nzVacio(PRCL_ID_IMPORTACION, "NULL") + ",";
						sql_prim += "'" + PRCL_ID_UPM + "',";
						sql_prim += "'" + PRCL_CONS_PAIS + "',";
						sql_prim += "'" + PRCL_VEREDA + "',";
						sql_prim += "'" + PRCL_CORREGIMIENTO + "',";
						sql_prim += "'" + PRCL_INSPECCION_POLICIA + "',";
						sql_prim += "'" + PRCL_CASERIO + "',";
						sql_prim += "'" + PRCL_RANCHERIA + "',";
						sql_prim += "'" + PRCL_FECHA_CAPTURA + "',";
						sql_prim += "'" + PRCL_MEDIOACCESO_POBLADO + "',";
						sql_prim += "'" + PRCL_DISTANCIA_POBLADO + "',";
						sql_prim += "'" + PRCL_TPOBLADO_H + "',";
						sql_prim += "'" + PRCL_TPOBLADO_M + "',";
						sql_prim += "'" + PRCL_MEDIOACCESO_CAMPAMENTO + "',";
						sql_prim += "'" + PRCL_DISTANCIA_CAMPAMENTO + "',";
						sql_prim += "'" + PRCL_TCAMPAMENTO_H + "',";
						sql_prim += "'" + PRCL_TCAMPAMENTO_M + "',";
						sql_prim += "'" + PRCL_MEDIOACCESO_JALON + "',";
						sql_prim += "'" + PRCL_DISTANCIA_JALON + "',";
						sql_prim += "'" + PRCL_TJALON_H + "',";
						sql_prim += "'" + PRCL_TJALON_M + "',";
						sql_prim += "'" + PRCL_COORDENADAS + "',";
						sql_prim += "'" + PRCL_DISTANCIA_CAMPAMENTOS + "',";
						sql_prim += "'" + PRCL_LATITUD + "',";
						sql_prim += "'" + PRCL_LONGITUD + "',";
						sql_prim += "'" + PRCL_ALTITUD + "',";
						sql_prim += "'" + PRCL_NOMBRE + "',";
						sql_prim += "'" + PRCL_USR_DILIGENCIA_F1 + "',";
						sql_prim += "'" + PRCL_USR_DILIGENCIA_F2 + "',";
						sql_prim += "'" + PRCL_FECHAINI_APROXIMACION + "',";
						sql_prim += "'" + PRCL_FECHAFIN_APROXIMACION + "',";
						sql_prim += "'" + PRCL_FECHAINI_LOCALIZACION + "',";
						sql_prim += "'" + PRCL_FECHAFIN_LOCALIZACION + "',";
						sql_prim += "'" + PRCL_DESCRIPCION + "',";
						sql_prim += "'" + PRCL_OBSERVACIONES + "',";
						sql_prim += "'" + PRCL_TRACKLOG_CAMPAMENTO + "',";
						sql_prim += "'" + PRCL_TRACKLOG_PARCELA + "',";
						sql_prim += "'" + PRCL_DEPARTAMENTO + "',";
						sql_prim += "'" + PRCL_MUNICIPIO + "',";			
						sql_prim += "'" + PRCL_SPF1_DILIGENCIA + "',";
						sql_prim += "'" + PRCL_SPF1_FECHAINI + "',";
						sql_prim += "'" + PRCL_SPF1_FECHAFIN + "',";
						sql_prim += "'" + PRCL_SPF1_POSIBLE + "',";
						sql_prim += "'" + PRCL_SPF1_JUSTIFICACION_NO + "',";
						sql_prim += "'" + PRCL_SPF1_OBS_FUSTALES + "',";
						sql_prim += "'" + PRCL_SPF1_OBS_LATIZALES + "',";
						sql_prim += "'" + PRCL_SPF1_OBS_BRINZALES + "',";
						sql_prim += "'" + PRCL_SPF2_DILIGENCIA + "',";
						sql_prim += "'" + PRCL_SPF2_FECHAINI + "',";
						sql_prim += "'" + PRCL_SPF2_FECHAFIN + "',";
						sql_prim += "'" + PRCL_SPF2_POSIBLE + "',";
						sql_prim += "'" + PRCL_SPF2_JUSTIFICACION_NO + "',";
						sql_prim += "'" + PRCL_SPF2_OBS_FUSTALES + "',";
						sql_prim += "'" + PRCL_SPF2_OBS_LATIZALES + "',";
						sql_prim += "'" + PRCL_SPF2_OBS_BRINZALES + "',";
						sql_prim += "'" + PRCL_SPF3_DILIGENCIA + "',";
						sql_prim += "'" + PRCL_SPF3_FECHAINI + "',";
						sql_prim += "'" + PRCL_SPF3_FECHAFIN + "',";
						sql_prim += "'" + PRCL_SPF3_POSIBLE + "',";
						sql_prim += "'" + PRCL_SPF3_JUSTIFICACION_NO + "',";
						sql_prim += "'" + PRCL_SPF3_OBS_FUSTALES + "',";
						sql_prim += "'" + PRCL_SPF3_OBS_LATIZALES + "',";
						sql_prim += "'" + PRCL_SPF3_OBS_BRINZALES + "',";
						sql_prim += "'" + PRCL_SPF4_DILIGENCIA + "',";
						sql_prim += "'" + PRCL_SPF4_FECHAINI + "',";
						sql_prim += "'" + PRCL_SPF4_FECHAFIN + "',";
						sql_prim += "'" + PRCL_SPF4_POSIBLE + "',";
						sql_prim += "'" + PRCL_SPF4_JUSTIFICACION_NO + "',";
						sql_prim += "'" + PRCL_SPF4_OBS_FUSTALES + "',";
						sql_prim += "'" + PRCL_SPF4_OBS_LATIZALES + "',";
						sql_prim += "'" + PRCL_SPF4_OBS_BRINZALES + "',";
						sql_prim += "'" + PRCL_SPF5_DILIGENCIA + "',";
						sql_prim += "'" + PRCL_SPF5_FECHAINI + "',";
						sql_prim += "'" + PRCL_SPF5_FECHAFIN + "',";
						sql_prim += "'" + PRCL_SPF5_POSIBLE + "',";
						sql_prim += "'" + PRCL_SPF5_JUSTIFICACION_NO + "',";
						sql_prim += "'" + PRCL_SPF5_OBS_FUSTALES + "',";
						sql_prim += "'" + PRCL_SPF5_OBS_LATIZALES + "',";
						sql_prim += "'" + PRCL_SPF5_OBS_BRINZALES + "',";
						sql_prim += "'" + PRCL_PLOT + "',";
						sql_prim += "'" + PRCL_AREA + "',";
						sql_prim += "'" + PRCL_TEMPORALIDAD + "',";
						sql_prim += "'" + PRCL_PUBLICA + "',";
						sql_prim += "'" + PRCL_HAB + "',";
						sql_prim += "'" + PRCL_DAP + "',";
						sql_prim += "'" + PRCL_GPS + "',";
						sql_prim += "'" + PRCL_EQ + "',";
						sql_prim += "'" + PRCL_BA + "',";
						sql_prim += "'" + PRCL_BS + "',";
						sql_prim += "'" + PRCL_BT + "',";
						sql_prim += "'" + PRCL_AUTORCUSTODIOINFO + "',";
						sql_prim += "'" + PRCL_TIPOBOSQUE + "',";
						sql_prim += "'" + PRCL_INCLUIR + "',";
						sql_prim += "'" + PRCL_ACTUALIZACION + "',";
						sql_prim += "" + consecutivo_archivo + "";
						sql_prim += ")"; 
						
						try {
						    r += Auxiliar.mensaje("nota", "Insertando observación para linea " + i + ".", usuario, metodo);
							if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
								dbREDD.ejecutarSQL("DELETE FROM RED_PARCELA_IMPORTACION WHERE PRIM_CONSECUTIVO="+PRCL_CONSECUTIVO);
							}
						    if (!dbREDD.ejecutarSQL(sql_prim)) {
							    Auxiliar.mensaje("advertencia", "Error de SQL al insertar las observaciones para linea " + i + "["+sql_prim+"]: " + dbREDD.ultimoError, usuario, metodo);
						    }
						    dbREDD.cometerTransaccion();
						}
						catch (SQLException e) {
							r += Auxiliar.mensaje("error", "Inconveniente al ejecutar consulta SQL [" + sql_prim + "]: " + e.toString() + ". Ultimo error:" + dbREDD.ultimoError, usuario, metodo);
						}
					}
		        }
		        //r += Auxiliar.mensaje("nota", "Linea " + i + " [" + linea + "] procesada.", usuario, metodo);
			    r += Auxiliar.mensaje("confirmacion", "Linea " + i + " procesada.", usuario, metodo);

			    try {
		        	dbREDD.establecerAutoCometer(true);
		        }
		        catch (Exception e) {
		        	r += Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
		        }
	        }
	        
	        
	        // ACTUALIZAR ESTADISTICAS DE IMPORTACIÓN
	        recalcularEstadisticasArchivo(consecutivo_archivo, session);	        
	    	dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='ARCHIVO PROCESADO' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);

	    	String base_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='base_url_admif'", "http://172.16.1.125/AdmIF");
	    	
		    if (fin) {
		        r += Auxiliar.mensaje("nota", "<div class='opcionmenu'><a class=boton href='"+base_url+"/CargueMasivo?accion=log_importacion_archivo_parcelas&arch_consecutivo="+consecutivo_archivo+"'>Log</a></div>", usuario, metodo);
	        }
	    }
	    catch (Exception e) {
	    	r += Auxiliar.mensaje("error", "Error al procesar una linea del archivo.  Detalle del error:" + e.toString() + dbREDD.ultimoError, usuario, metodo);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Excepción en importarParcelas: " + e.toString() + dbREDD.ultimoError);
	    }
	    finally {
	        br.close();
	    }
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Importa individuos desde un archivo
	 * 
	 * @param consecutivo_archivo
	 * @return String con resultado de la importación
	 * @throws Exception 
	 */
	public String importarIndividuos(String consecutivo_archivo, String PRCL_CONSECUTIVO, HttpServletRequest request)
			throws Exception {
		String metodo = yo + "importarIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		// Obtener la ruta del archivo
		String ruta_archivo = "";
		boolean procesar = true;
		
		try {
			ruta_archivo = obtenerRutaArchivo(consecutivo_archivo, request);
		} catch (Exception e) {
			r += Auxiliar.mensaje("error", "Inconveniente al obtener la ruta del archivo. " + e.toString() + ". Ultimo error en la bd:" + dbREDD.ultimoError, usuario, metodo);
			procesar = false;
		}
		
		
		// VERIFICAR QUE EL ARCHIVO EXISTA
		if (!archie.existeArchivo(ruta_archivo)) {
			r += Auxiliar.mensaje("error", "No existe el archivo de individuos a importar (" + ruta_archivo + "). ", usuario, metodo);
			procesar = false;
		}
		
		
		// INICIAR PROCESAMIENTO DEL ARCHIVO LINEA POR LINEA
		
		String [] a_campos = null;
		String [] a_valores = null;
		
		String ACCION = "";
		
		
		// CAMPOS DE LA TABLA DE IMPORTACIÓN DE INDIVIDUOS
		String INDV_ID = "";
		
		String INDV_ARCH_CONSECUTIVO = consecutivo_archivo;
		String INDV_OBSERVACIONES_IMPORTACION = "";
		
		String INDV_CONSECUTIVO = "";
		String INDV_PRCL_CONSECUTIVO = "";
		String INDV_ID_IMPORTACION = ""; 
		String INDV_ESARBOLREFERENCIA = ""; 
		String INDV_CARDINALIDAD = ""; 
		String INDV_NUMERO_COLECTOR = ""; 
		String INDV_CANTIDAD_EJEMPLARES = ""; 
		String INDV_DISTANCIA = "";
		String INDV_AZIMUTH = "";
		String INDV_NUMERO_ARBOL = "";
		String INDV_ESPECIE = "";
		String INDV_SUBPARCELA = "";
		String INDV_OBSERVACIONES = "";
		String INDV_ETIQUETA_COLECTA = "";
		String INDV_FOTO_COLECTA = "";
		String INDV_HOMOLOGACION = "";
		//String INDV_ARCHIVO_FOTOS = "";
		String INDV_TXCT_ID = "";
		String INDV_FID = "";
		String INDV_PRCL_PLOT = "";
		String INDV_DENSIDAD = "";
		String INDV_FAMILIA = "";
		String INDV_AUTORFAMILIA = "";
		String INDV_GENERO = "";
		String INDV_AUTORGENERO = "";
		String INDV_ESTADOEPITETO = "";
		String INDV_EPITETO = "";
		String INDV_MORFOESPECIE = "";
		String INDV_AUTORESPECIE = "";
		String INDV_HABITO = "";
		String INDV_INCLUIR = "";
		String INDV_TAYO_DAP1 = "";
		String INDV_TAYO_DAP2 = "";
		String INDV_TAYO_ALTURADAP = "";
		String INDV_TAYO_ALTURA = "";
		String INDV_TAYO_ALTURATOTAL = "";
		String INDV_TAYO_FORMAFUSTE = "";
		String INDV_TAYO_DANIO = "";
		String INDV_TAYO_OBSERVACIONES = "";
		String INDV_ACTUALIZACION = "";

		
		String sql_idim = "";
		String sql_tmp = "";
		String sql_indv_red = "";
		String sql_indv_where_red = "";
		String conteo = "0";
		
		String sql_eliminar = "";
		

		
		// ABRIMOS EL ARCHIVO PARA PROCESARLO LINEA POR LINEA UTILIZANDO UN BUFFEREDREADER
		
		InputStream inputStream = new FileInputStream(ruta_archivo);
		Reader reader = new InputStreamReader(inputStream, Charset.forName("windows-1252"));
		BufferedReader br = new BufferedReader(reader);
		
		int c = -1;
		int v = -1;
		
		try {
			String linea = br.readLine();
			
			if (Auxiliar.nz(linea, "").equals("")) {
				r += Auxiliar.mensaje("advertencia", "Linea de titulos de campos no encontrada.", usuario, metodo);
				procesar = false;
			}
			
			
			int n_campos = 40;
			
			a_campos = linea.split(s);
			int a_campos_length = a_campos.length;
			
			if (a_campos.length != n_campos) {
				r += Auxiliar.mensaje("advertencia", "Linea de titulos de campos no válida. Numero de campos encontrados:" + a_campos.length + " Recuerde que el caracter de separación es la tabulación ("+s+")", usuario, metodo);
				procesar = false;
			}

			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ACCION")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ACCION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CONSECUTIVO")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo CONSECUTIVO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ID_IMPORTACION")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ID_IMPORTACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("PARCELA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo PARCELA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("SUBPARCELA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo SUBPARCELA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("NUMERO_ARBOL")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo NUMERO_ARBOL. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DISTANCIA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+"debe tener el titulo DISTANCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("AZIMUTH")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo AZIMUTH. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ESARBOLREFERENCIA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ESARBOLREFERENCIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CARDINALIDAD")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo CARDINALIDAD. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("NUMERO_COLECTOR")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo NUMERO_COLECTOR. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CANTIDAD_EJEMPLARES")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo CANTIDAD_EJEMPLARES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ESPECIE")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ESPECIE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TAXONOMIA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo TAXONOMIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("OBSERVACIONES")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo OBSERVACIONES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ETIQUETA_COLECTA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ETIQUETA_COLECTA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim(), "").equals("FOTO_COLECTA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo FOTO_COLECTA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("HOMOLOGACION")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo HOMOLOGACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			/*
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ARCHIVO_FOTOS")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ARCHIVO_FOTOS. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			*/
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FID")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo PRCL_PLOT. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("PLOT")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo PLOT. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DENSIDAD")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DENSIDAD. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FAMILIA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo FAMILIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("AUTORFAMILIA")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo AUTORFAMILIA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("GENERO")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo GENERO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("AUTORGENERO")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo AUTORGENERO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ESTADOEPITETO")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ESTADOEPITETO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("EPITETO")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo EPITETO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("MORFOESPECIE")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo MORFOESPECIE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("AUTORESPECIE")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo AUTORESPECIE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("HABITO")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo HABITO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("INCLUIR")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo INCLUIR. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_DAP1")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DAP1. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_DAP2")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DAP2. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_ALTURADAP")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ALTURADAP. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_ALTURA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ALTURA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_ALTURATOTAL")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+"debe tener el titulo ALTURATOTAL. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_FORMAFUSTE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo FORMAFUSTE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_DANIO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DANIO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("TALLOUNICO_OBSERVACIONES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo OBSERVACIONES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }
	        
			c++; if (c < a_campos_length)	        
			if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ACTUALIZACION")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ACTUALIZACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
			}
			
			if (!procesar) {
			    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='"+r+"' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
			    
			    try {
			    	dbREDD.ejecutarSQL(sql_registro_estado);
			    }
			    catch (Exception e) {
			    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
			    }

				dbREDD.desconectarse();
				return r;
			}
			else {
			    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='ARCHIVO VALIDADO EN SU FORMATO. PROCESANDO...' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
			    
			    try {
			    	dbREDD.ejecutarSQL(sql_registro_estado);
			    }
			    catch (Exception e) {
			    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
			    }
			}
			
			
			long i = 0;
			
			String str_ok_transaccion = "";
			String ok_transaccion = "";
			String [] a_ok_transaccion = null;
			boolean ok_validacion = false;
			
	        String tmplinea = "";
			
			
			String max_fid = dbREDD.obtenerDato("SELECT MAX(INDV_FID) AS MAX_FID FROM RED_INDIVIDUO WHERE INDV_BIOMASA_AEREA IS NOT NULL AND INDV_CONSECUTIVO IN (SELECT TAYO_INDV_CONSECUTIVO FROM RED_TALLO)", "");
			String min_fid_nE = dbREDD.obtenerDato("SELECT MIN(IDIM_FID) AS MIN_FID FROM RED_INDIVIDUO_IMPORTACION", max_fid);
			String min_fid_BA = dbREDD.obtenerDato("SELECT MIN(INDV_FID) AS MIN_FID FROM RED_INDIVIDUO WHERE INDV_BIOMASA_AEREA IS NULL", max_fid);
			String min_fid_TAYO = dbREDD.obtenerDato("SELECT MIN(INDV_FID) AS MIN_FID FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO NOT IN (SELECT TAYO_INDV_CONSECUTIVO FROM RED_TALLO)", max_fid);
			
			long min_fid = 1;
			
			if (Auxiliar.tieneAlgo(min_fid_nE) && Auxiliar.tieneAlgo(min_fid_BA) && Auxiliar.tieneAlgo(min_fid_TAYO))
				min_fid = Math.min(Math.min(Long.parseLong(min_fid_BA), Long.parseLong(min_fid_TAYO)), Long.parseLong(min_fid_nE));

			while (linea != null) {
				
				i++;
				v = -1;
				
				ok_validacion = true;
				
				linea = br.readLine();
				
				if (linea == null) {
					break;
				}
				
				String lini = linea.substring(0, 5);
				
				if (lini.equals("Poner") || lini.equals("Put E")) {
					continue;
				}
				
				INDV_OBSERVACIONES_IMPORTACION = "";
				
				
				INDV_CONSECUTIVO = "";
				if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
					INDV_PRCL_CONSECUTIVO = PRCL_CONSECUTIVO;
				}
				INDV_ID_IMPORTACION = ""; 
				INDV_ESARBOLREFERENCIA = ""; 
				INDV_CARDINALIDAD = ""; 
				INDV_NUMERO_COLECTOR = ""; 
				INDV_CANTIDAD_EJEMPLARES = ""; 
				INDV_DISTANCIA = "";
				INDV_AZIMUTH = "";
				INDV_NUMERO_ARBOL = "";
				INDV_ESPECIE = "";
				INDV_SUBPARCELA = "";
				INDV_OBSERVACIONES = "";
				INDV_ETIQUETA_COLECTA = "";
				INDV_FOTO_COLECTA = "";
				INDV_HOMOLOGACION = "";
				//INDV_ARCHIVO_FOTOS = "";
				INDV_TXCT_ID = "";
				INDV_FID = "";
				INDV_PRCL_PLOT = "";
				INDV_DENSIDAD = "";
				INDV_FAMILIA = "";
				INDV_AUTORFAMILIA = "";
				INDV_GENERO = "";
				INDV_AUTORGENERO = "";
				INDV_ESTADOEPITETO = "";
				INDV_EPITETO = "";
				INDV_MORFOESPECIE = "";
				INDV_AUTORESPECIE = "";
				INDV_HABITO = "";
				INDV_INCLUIR = "";
				INDV_ACTUALIZACION = "";
				INDV_TAYO_DAP1 = "";
				INDV_TAYO_DAP2 = "";
				INDV_TAYO_ALTURADAP = "";
				INDV_TAYO_ALTURA = "";
				INDV_TAYO_ALTURATOTAL = "";
				INDV_TAYO_FORMAFUSTE = "";
				INDV_TAYO_DANIO = "";
				INDV_TAYO_OBSERVACIONES = "";

				a_valores = linea.split(s);
				
				int n_valores = a_valores.length;
				
				
				if (n_valores > n_campos) {
					INDV_OBSERVACIONES_IMPORTACION += Auxiliar.mensaje("advertencia", "Linea "+i+" no válida. Numero de campos encontrados:" + a_valores.length, usuario, metodo);
					ok_validacion = false;
				}
				else {
					v++; ACCION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_CONSECUTIVO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_ID_IMPORTACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_PRCL_CONSECUTIVO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_SUBPARCELA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_NUMERO_ARBOL = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_DISTANCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; INDV_AZIMUTH = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; INDV_ESARBOLREFERENCIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_CARDINALIDAD = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_NUMERO_COLECTOR = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_CANTIDAD_EJEMPLARES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_ESPECIE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TXCT_ID = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_OBSERVACIONES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_ETIQUETA_COLECTA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_FOTO_COLECTA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_HOMOLOGACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					//v++; INDV_ARCHIVO_FOTOS = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_FID = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_PRCL_PLOT = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_DENSIDAD = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; INDV_FAMILIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_AUTORFAMILIA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_GENERO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_AUTORGENERO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_ESTADOEPITETO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_EPITETO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_MORFOESPECIE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_AUTORESPECIE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_HABITO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_INCLUIR = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_DAP1 = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_DAP2 = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_ALTURADAP = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_ALTURA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_ALTURATOTAL = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_FORMAFUSTE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_DANIO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_TAYO_OBSERVACIONES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; INDV_ACTUALIZACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");

					if (Auxiliar.tieneAlgo(INDV_FID)) {
						if (Long.parseLong(INDV_FID) < min_fid) {
							continue;
						}
					}
					
					//INDV_ID_IMPORTACION = dbREDD.obtenerDato("SELECT COALESCE(MAX(IDIM_ID_IMPORTACION),0)+1 AS CONSECUTIVO FROM RED_INDIVIDUO_IMPORTACION","");
					
					if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
						INDV_PRCL_CONSECUTIVO = PRCL_CONSECUTIVO;
					}

					// DEFINIR WHERE PARA IDENTIFICACIÓN DEL INDIVIDUO TANTO EN RED COMO EN IFN
					sql_indv_where_red = " INDV_ID_IMPORTACION=" + INDV_ID_IMPORTACION;
					
					Individuo indy = new Individuo();
					
					if (ACCION.equals("ELIMINAR")) {
						if (!INDV_ID_IMPORTACION.equals("")) {
							// VERIFICAR QUE EL INDIVIDUO EXISTA
							try {
								sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_INDIVIDUO WHERE INDV_ID_IMPORTACION=" + INDV_ID_IMPORTACION;
								conteo = dbREDD.obtenerDato(sql_tmp, "0");
							}
							catch (Exception e) {
								r += Auxiliar.mensaje("error", "Error al intentar encontrar individuo con el ID_IMPORTACION " + INDV_ID_IMPORTACION + " para eliminarlo. SQL: [" + sql_tmp + "]. Último error en la bd de RED: " + dbREDD.ultimoError + ". Excepción: " + e.toString(), usuario, metodo);								
							}
							
							if (conteo.equals("0")) {
								r += Auxiliar.mensaje("advertencia", "No se encontró individuo alguno con el ID_IMPORTACION " + INDV_ID_IMPORTACION, usuario, metodo);
							}
							else {
								try {
									INDV_CONSECUTIVO = dbREDD.obtenerDato("SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_ID_IMPORTACION="+INDV_ID_IMPORTACION, "");
									
									if (Auxiliar.tieneAlgo(INDV_ID_IMPORTACION)) {
										str_ok_transaccion = indy.eliminar(INDV_CONSECUTIVO, request);
										a_ok_transaccion = str_ok_transaccion.split("-=-");
										ok_transaccion = a_ok_transaccion[0];
										
										// CONFIRMAR QUE SE PUDO ELIMINAR
										dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_PROCESADOS=ARCH_PROCESADOS+1 WHERE ARCH_CONSECUTIVO="+consecutivo_archivo);
									}									
								}
								catch (SQLException e) {
									r += Auxiliar.mensaje("error", "Error al intentar eliminar individuo en RED con el consecutivo " + INDV_ID_IMPORTACION + ". SQL: [" + sql_eliminar + "]. Último error en la bd: " + dbREDD.ultimoError + ". Excepción: " + e.toString(), usuario, metodo);
								}
							}
						} 
						else {
							r += Auxiliar.mensaje("advertencia", "Para poder eliminar un individuo es necesario especificar el consecutivo.", usuario, metodo);
						}						
					}
					else {
						INDV_OBSERVACIONES_IMPORTACION += "";
						
						if (ok_validacion) {
							if (INDV_CONSECUTIVO.equals("")) {
								if (Auxiliar.tieneAlgo(INDV_NUMERO_ARBOL) && Auxiliar.tieneAlgo(INDV_PRCL_CONSECUTIVO)) {
									sql_tmp = "SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE ";
									sql_tmp += " INDV_NUMERO_ARBOL='" + INDV_NUMERO_ARBOL + "'";
									sql_tmp += " AND INDV_PRCL_CONSECUTIVO=" + INDV_PRCL_CONSECUTIVO;
								}
								else if (Auxiliar.tieneAlgo(INDV_ID_IMPORTACION)) {
									sql_tmp = "SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE ";
									sql_tmp += " INDV_ID_IMPORTACION=" + INDV_ID_IMPORTACION;
								}
								else {
									sql_tmp = "";
								}
								
								String INDV_CONSECUTIVO_EXISTENTE = "";
								
								if (Auxiliar.tieneAlgo(sql_tmp)) {
									try {
										INDV_CONSECUTIVO_EXISTENTE = dbREDD.obtenerDato(sql_tmp, "");
									} catch (Exception e) {
									}
								}
								
								if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO_EXISTENTE)) {
									INDV_CONSECUTIVO = INDV_CONSECUTIVO_EXISTENTE;
								}
							}
							
							if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
								INDV_ID_IMPORTACION = dbREDD.obtenerDato("SELECT INDV_ID_IMPORTACION FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
							}
							
							if (!Auxiliar.tieneAlgo(INDV_ID_IMPORTACION)) {
								INDV_ID_IMPORTACION = dbREDD.obtenerDato("SELECT COALESCE(MAX(INDV_ID_IMPORTACION),0)+1 AS CONSECUTIVO FROM RED_INDIVIDUO", "");
							}
							
							
							// VALIDAR INDV_ACTUALIZACION
							String fechahora = "";
							String tz = "UTC";
							String fecha_actualizacion = "";
							if (Auxiliar.tieneAlgo(INDV_ACTUALIZACION)) {
								String [] a_actualizacion = INDV_ACTUALIZACION.split("@");
								fechahora = a_actualizacion[0].trim();
								if (a_actualizacion.length > 1) {
									tz = a_actualizacion[1];
								}
								
								if (!Auxiliar.fechaHoraEsValida(fechahora)) {
									t = Auxiliar.traducir("AVISO_VALIDACION.INDV_ACTUALIZACION", idioma, "Fecha INDV_ACTUALIZACION debe ser válida y estar en formato aaaa-mm-dd hh24:mi:ss@zona Ejemplo: 2013-10-13 21:29:44@-05:00.  Si importa desde excel, se recomienda trabajar los campos de fecha con formato de texto en su hoja de cálculo." + "..");
								}
								else {
									if (Auxiliar.tieneAlgo(tz)) {
										fecha_actualizacion = "CAST(FROM_TZ(TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss'), '"+tz+"') AT TIME ZONE ('UTC') AS TIMESTAMP)";
									}
									else {
										fecha_actualizacion = "TO_TIMESTAMP('"+fechahora+"', 'yyyy-mm-dd hh24:mi:ss')";
									}
								}
							}
							
							if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
								if (Auxiliar.tieneAlgo(INDV_FID)) {
									INDV_CONSECUTIVO = dbREDD.obtenerDato("SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_FID="+INDV_FID, "");
								}
							}	
							
							
							boolean repetido = false;
							boolean guardar = true;
							String mensaje_repetido = "";
							String mensaje_conteo = "";
							
							// SI YA FUE ACTUALIZADA CON LA MISMA FECHA ENTONCES NO ACTUALIZAR
							
							if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
								conteo = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO+" AND INDV_ACTUALIZACION>=" + Auxiliar.nzVacio(fecha_actualizacion, "SYSDATE") + " AND INDV_BIOMASA_AEREA IS NOT NULL AND INDV_CONSECUTIVO IN (SELECT TAYO_INDV_CONSECUTIVO FROM RED_TALLO)", "");
								if (!conteo.equals("")) {
									mensaje_repetido = Auxiliar.mensaje("confirmacion", "Fila "+i+": el individuo "+INDV_CONSECUTIVO+" (FID "+INDV_FID+") ya está actualizado.", usuario, metodo);
									if (Auxiliar.tieneAlgo(INDV_FID)) {
										min_fid_nE = dbREDD.obtenerDato("SELECT MIN(IDIM_FID) AS MIN_FID FROM RED_INDIVIDUO_IMPORTACION WHERE IDIM_FID>"+Math.max(min_fid, Long.parseLong(INDV_FID)), max_fid);
										min_fid_BA = dbREDD.obtenerDato("SELECT MIN(INDV_FID) AS MIN_FID FROM RED_INDIVIDUO WHERE INDV_BIOMASA_AEREA IS NULL AND INDV_FID>"+Math.max(min_fid, Long.parseLong(INDV_FID)), max_fid);
										min_fid_TAYO = dbREDD.obtenerDato("SELECT MIN(INDV_FID) AS MIN_FID FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO NOT IN (SELECT TAYO_INDV_CONSECUTIVO FROM RED_TALLO) AND INDV_FID>"+Math.max(min_fid, Long.parseLong(INDV_FID)), max_fid);
										
										if (Auxiliar.tieneAlgo(min_fid_nE) && Auxiliar.tieneAlgo(min_fid_BA) && Auxiliar.tieneAlgo(min_fid_TAYO)) {
											min_fid = Math.min(Math.min(Long.parseLong(min_fid_BA), Long.parseLong(min_fid_TAYO)), Long.parseLong(min_fid_nE));
											mensaje_repetido += Auxiliar.mensaje("confirmacion", "Siguiente FID obviando:" + min_fid + " por ("+min_fid_nE+","+min_fid_BA+","+min_fid_TAYO+").", usuario, metodo);
										}
									}	
									repetido = true;
									str_ok_transaccion = "0-=-" + mensaje_repetido;
									guardar = false;
								}
								conteo = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "0");
								if (conteo.equals("0")) {
									mensaje_conteo = Auxiliar.mensaje("advertencia", "Fila "+i+": obviando el individuo "+INDV_CONSECUTIVO+" ya que no existe un individuo con ese código en la base de datos.", usuario, metodo);
									r += mensaje_conteo;
									guardar = false;
									str_ok_transaccion = "0-=-" + mensaje_conteo;
								}
							}
							
							if (!repetido && guardar) {
								str_ok_transaccion = indy.guardar(
										INDV_CONSECUTIVO,
										INDV_PRCL_CONSECUTIVO,
										INDV_ID_IMPORTACION, 
										INDV_ESARBOLREFERENCIA, 
										INDV_CARDINALIDAD, 
										INDV_NUMERO_COLECTOR, 
										INDV_CANTIDAD_EJEMPLARES, 
										INDV_DISTANCIA,
										INDV_AZIMUTH,
										INDV_NUMERO_ARBOL,
										INDV_ESPECIE,
										INDV_SUBPARCELA,
										INDV_OBSERVACIONES,
										INDV_ETIQUETA_COLECTA,
										INDV_FOTO_COLECTA,
										INDV_HOMOLOGACION,
										//INDV_ARCHIVO_FOTOS,
										INDV_TXCT_ID,
										consecutivo_archivo,
										INDV_PRCL_PLOT,
										INDV_DENSIDAD,
										INDV_FAMILIA,
										INDV_AUTORFAMILIA,
										INDV_GENERO,
										INDV_AUTORGENERO,
										INDV_ESTADOEPITETO,
										INDV_EPITETO,
										INDV_MORFOESPECIE,
										INDV_AUTORESPECIE,
										INDV_HABITO,
										INDV_INCLUIR,
										INDV_FID,
										INDV_ACTUALIZACION,
										INDV_TAYO_DAP1,
										INDV_TAYO_DAP2,
										INDV_TAYO_ALTURADAP,
										INDV_TAYO_ALTURA,
										INDV_TAYO_ALTURATOTAL,
										INDV_TAYO_FORMAFUSTE,
										INDV_TAYO_DANIO,
										INDV_TAYO_OBSERVACIONES,
										request,
										this.RED_username, this.RED_password, this.RED_host, this.RED_port, this.RED_sid, true
										);
							}
								
							a_ok_transaccion = str_ok_transaccion.split("-=-");
							ok_transaccion = a_ok_transaccion[0];
							
							if (a_ok_transaccion.length == 2) {
								INDV_OBSERVACIONES_IMPORTACION = a_ok_transaccion[1].toString();
							}
						}

						if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO) && Auxiliar.tieneAlgo(INDV_ID_IMPORTACION)) {
							dbREDD.ejecutarSQL("DELETE FROM RED_INDIVIDUO_IMPORTACION WHERE IDIM_ID_IMPORTACION="+INDV_ID_IMPORTACION + " AND IDIM_CONSECUTIVO=" + INDV_CONSECUTIVO);
						}
						
						sql_idim = "INSERT INTO RED_INDIVIDUO_IMPORTACION ";
						sql_idim += "(";
						sql_idim += "IDIM_ESTADOIMPORTACION,";
						sql_idim += "IDIM_OBSERVACIONES_IMPORTACION,";
						sql_idim += "IDIM_CONSECUTIVO,";
						sql_idim += "IDIM_PRCL_CONSECUTIVO,";
						sql_idim += "IDIM_ID_IMPORTACION,"; 
						sql_idim += "IDIM_ESARBOLREFERENCIA,"; 
						sql_idim += "IDIM_CARDINALIDAD,"; 
						sql_idim += "IDIM_NUMERO_COLECTOR,"; 
						sql_idim += "IDIM_CANTIDAD_EJEMPLARES,"; 
						sql_idim += "IDIM_DISTANCIA,";
						sql_idim += "IDIM_AZIMUTH,";
						sql_idim += "IDIM_NUMERO_ARBOL,";
						sql_idim += "IDIM_ESPECIE,";
						sql_idim += "IDIM_SUBPARCELA,";
						sql_idim += "IDIM_OBSERVACIONES,";
						sql_idim += "IDIM_ETIQUETA_COLECTA,";
						sql_idim += "IDIM_FOTO_COLECTA,";
						sql_idim += "IDIM_HOMOLOGACION,";
						//sql_idim += "IDIM_ARCHIVO_FOTOS,";
						sql_idim += "IDIM_TXCT_ID,";
						sql_idim += "IDIM_FID,";
						sql_idim += "IDIM_PRCL_PLOT,";
						sql_idim += "IDIM_DENSIDAD,";
						sql_idim += "IDIM_FAMILIA,";
						sql_idim += "IDIM_AUTORFAMILIA,";
						sql_idim += "IDIM_GENERO,";
						sql_idim += "IDIM_AUTORGENERO,";
						sql_idim += "IDIM_ESTADOEPITETO,";
						sql_idim += "IDIM_EPITETO,";
						sql_idim += "IDIM_MORFOESPECIE,";
						sql_idim += "IDIM_AUTORESPECIE,";
						sql_idim += "IDIM_HABITO,";
						sql_idim += "IDIM_INCLUIR,";
						sql_idim += "IDIM_TAYO_DAP1,";
						sql_idim += "IDIM_TAYO_DAP2,";
						sql_idim += "IDIM_TAYO_ALTURADAP,";
						sql_idim += "IDIM_TAYO_ALTURA,";
						sql_idim += "IDIM_TAYO_ALTURATOTAL,";
						sql_idim += "IDIM_TAYO_FORMAFUSTE,";
						sql_idim += "IDIM_TAYO_DANIO,";
						sql_idim += "IDIM_TAYO_OBSERVACIONES,";
						sql_idim += "IDIM_ACTUALIZACION,";
						sql_idim += "IDIM_ARCH_CONSECUTIVO";
						sql_idim += ")";
						sql_idim += " VALUES ";
						sql_idim += "(";
						sql_idim += Auxiliar.nzVacio(a_ok_transaccion[0], "NULL") + ",";
						int longitud = (INDV_OBSERVACIONES_IMPORTACION.length() < MAX_VARCHAR2) ? INDV_OBSERVACIONES_IMPORTACION.length() : MAX_VARCHAR2;
						sql_idim += "'" + INDV_OBSERVACIONES_IMPORTACION.substring(0, longitud).replace("'", "`") + "',";
						sql_idim += Auxiliar.nzVacio(INDV_CONSECUTIVO, "NULL") + ",";
						sql_idim += "'" + INDV_PRCL_CONSECUTIVO + "',";
						sql_idim += "" + Auxiliar.nzVacio(INDV_ID_IMPORTACION, "NULL") + ","; 
						sql_idim += "'" + INDV_ESARBOLREFERENCIA + "',"; 
						sql_idim += "'" + INDV_CARDINALIDAD + "',"; 
						sql_idim += "'" + INDV_NUMERO_COLECTOR + "',"; 
						sql_idim += "'" + INDV_CANTIDAD_EJEMPLARES + "',"; 
						sql_idim += "'" + INDV_DISTANCIA + "',";
						sql_idim += "'" + INDV_AZIMUTH + "',";
						sql_idim += "'" + INDV_NUMERO_ARBOL + "',";
						sql_idim += "'" + INDV_ESPECIE + "',";
						sql_idim += "'" + INDV_SUBPARCELA + "',";
						sql_idim += "'" + INDV_OBSERVACIONES + "',";
						sql_idim += "'" + INDV_ETIQUETA_COLECTA + "',";
						sql_idim += "'" + INDV_FOTO_COLECTA + "',";
						sql_idim += "'" + INDV_HOMOLOGACION + "',";
						//sql_idim += "'" + INDV_ARCHIVO_FOTOS + "',";
						sql_idim += "'" + INDV_TXCT_ID + "',";
						sql_idim += "" + Auxiliar.nzVacio(INDV_FID, "NULL") + ",";
						sql_idim += "'" + INDV_PRCL_PLOT + "',";
						sql_idim += "'" + INDV_DENSIDAD + "',";
						sql_idim += "'" + INDV_FAMILIA + "',";
						sql_idim += "'" + INDV_AUTORFAMILIA + "',";
						sql_idim += "'" + INDV_GENERO + "',";
						sql_idim += "'" + INDV_AUTORGENERO + "',";
						sql_idim += "'" + INDV_ESTADOEPITETO + "',";
						sql_idim += "'" + INDV_EPITETO + "',";
						sql_idim += "'" + INDV_MORFOESPECIE + "',";
						sql_idim += "'" + INDV_AUTORESPECIE + "',";
						sql_idim += "'" + INDV_HABITO + "',";
						sql_idim += "'" + INDV_INCLUIR + "',";
						sql_idim += "'" + INDV_TAYO_DAP1 + "',";
						sql_idim += "'" + INDV_TAYO_DAP2 + "',";
						sql_idim += "'" + INDV_TAYO_ALTURADAP + "',";
						sql_idim += "'" + INDV_TAYO_ALTURA + "',";
						sql_idim += "'" + INDV_TAYO_ALTURATOTAL + "',";
						sql_idim += "'" + INDV_TAYO_FORMAFUSTE + "',";
						sql_idim += "'" + INDV_TAYO_DANIO + "',";
						sql_idim += "'" + INDV_TAYO_OBSERVACIONES + "',";
						sql_idim += "'" + INDV_ACTUALIZACION + "',";
						sql_idim += INDV_ARCH_CONSECUTIVO + "";
						sql_idim += ")";
						
						try {
							r += Auxiliar.mensaje("nota", "Observaciones para la fila " + i, usuario, metodo);
							if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
								dbREDD.ejecutarSQL("DELETE FROM RED_INDIVIDUO_IMPORTACION WHERE IDIM_CONSECUTIVO="+INDV_CONSECUTIVO);
							}
							if (!dbREDD.ejecutarSQL(sql_idim)) {
								Auxiliar.mensaje("error", "No pude registrar la importación fallida de un individuo: " + dbREDD.ultimoError, usuario, metodo);
							}
							dbREDD.cometerTransaccion();
						}
						catch (SQLException e) {
							r += Auxiliar.mensaje("error", "Inconveniente al ejecutar consulta SQL [" + sql_idim + "]: " + e.toString() + ". Ultimo error:" + dbREDD.ultimoError, usuario, metodo);
						}
						

						if (Auxiliar.tieneAlgo(INDV_FID)) {
							min_fid_nE = dbREDD.obtenerDato("SELECT MIN(IDIM_FID) AS MIN_FID FROM RED_INDIVIDUO_IMPORTACION WHERE IDIM_FID>"+Math.max(min_fid, Long.parseLong(INDV_FID)), max_fid);
							min_fid_BA = dbREDD.obtenerDato("SELECT MIN(INDV_FID) AS MIN_FID FROM RED_INDIVIDUO WHERE INDV_BIOMASA_AEREA IS NULL AND INDV_FID>"+Math.max(min_fid, Long.parseLong(INDV_FID)), max_fid);
							min_fid_TAYO = dbREDD.obtenerDato("SELECT MIN(INDV_FID) AS MIN_FID FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO NOT IN (SELECT TAYO_INDV_CONSECUTIVO FROM RED_TALLO) AND INDV_FID>"+Math.max(min_fid, Long.parseLong(INDV_FID)), max_fid);
							
							if (Auxiliar.tieneAlgo(min_fid_nE) && Auxiliar.tieneAlgo(min_fid_BA) && Auxiliar.tieneAlgo(min_fid_TAYO)) {
								min_fid = Math.min(Math.min(Long.parseLong(min_fid_BA), Long.parseLong(min_fid_TAYO)), Long.parseLong(min_fid_nE));
								Auxiliar.mensaje("confirmacion", "Siguiente FID:" + min_fid + " por ("+min_fid_nE+","+min_fid_BA+","+min_fid_TAYO+").", usuario, metodo);
							}
						}	
					}
				}
				
				//r += Auxiliar.mensaje("confirmacion", "Fila " + i + " procesada. Siguiente FID:." + min_fid, usuario, metodo);
				//Auxiliar.mensaje("confirmacion", "Fila " + i + " procesada. Siguiente FID:." + min_fid, usuario, metodo);
				
				try {
					dbREDD.establecerAutoCometer(true);
				}
				catch (Exception e) {
					r += Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
				}
				
			}
			
			// ACTUALIZAR ESTADISTICAS DE IMPORTACIÓN
	        recalcularEstadisticasArchivo(consecutivo_archivo, session);			
	    	dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='ARCHIVO PROCESADO' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);

	    	String base_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='base_url_admif'", "http://172.16.1.125/AdmIF");

			r += Auxiliar.mensaje("nota", "<a class=boton href='"+base_url+"/CargueMasivo?accion=log_importacion_archivo_individuos&arch_consecutivo="+consecutivo_archivo+"'>Log</a>", usuario, metodo);
			
		}
		catch (Exception e) {
			r += Auxiliar.mensaje("error", "Error al procesar una línea del archivo.  Detalle del error:" + e.toString() + dbREDD.ultimoError, usuario, metodo);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Excepción en importarIndividuos: " + e.toString() + dbREDD.ultimoError);
		}
		finally {
			br.close();
		}
		
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Importa tallos desde un archivo
	 * 
	 * @param consecutivo_archivo
	 * @return String con resultado de la importación
	 * @throws Exception 
	 */
	public String importarTallos(String consecutivo_archivo, String INDV_CONSECUTIVO, HttpServletRequest request)
	throws Exception {
		String metodo = yo + "importarTallos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();

		String r = "";

		// Obtener la ruta del archivo
		String ruta_archivo = "";
		boolean procesar = true;
		
		try {
			ruta_archivo = obtenerRutaArchivo(consecutivo_archivo, request);
		} catch (Exception e) {
			r += Auxiliar.mensaje("error", "Inconveniente al obtener la ruta del archivo. " + e.toString() + ". Ultimo error en la bd:" + dbREDD.ultimoError, usuario, metodo);
			procesar = false;
		}
		

		// VERIFICAR QUE EL ARCHIVO EXISTA
		if (!archie.existeArchivo(ruta_archivo)) {
			r += Auxiliar.mensaje("error", "No existe el archivo de tallos a importar (" + ruta_archivo + "). ", usuario, metodo);
			procesar = false;
		}
	
		
		// INICIAR PROCESAMIENTO DEL ARCHIVO LINEA POR LINEA

		String [] a_campos = null;
		String [] a_valores = null;
		
		String ACCION = "";
		
		
		// CAMPOS DE LA TABLA DE TALLOS
		String TAYO_ID = "";
		
		String TAYO_ARCH_CONSECUTIVO = consecutivo_archivo;
		String TAYO_OBSERVACIONES_IMPORTACION = "";
		
		String TAYO_CONSECUTIVO = "";
		String TAYO_INDV_CONSECUTIVO = "";
		String TAYO_ID_IMPORTACION = "";
		String TAYO_DAP1 = ""; 
		String TAYO_DAP2 = ""; 
		String TAYO_ALTURADAP = ""; 
		String TAYO_ALTURA = ""; 
		String TAYO_ALTURATOTAL = ""; 
		String TAYO_FORMAFUSTE = "";
		String TAYO_DANIO = "";
		String TAYO_OBSERVACIONES = "";

				
		String sql_idim = "";
		String sql_tmp = "";
		String sql_indv_red = "";
		String sql_indv_where_red = "";
		String conteo = "0";
		
		String sql_eliminar = "";
		
		String indv_consecutivo_existente = "";

		
		// ABRIMOS EL ARCHIVO PARA PROCESARLO LINEA POR LINEA UTILIZANDO UN BUFFEREDREADER
		
		InputStream inputStream = new FileInputStream(ruta_archivo);
		Reader reader = new InputStreamReader(inputStream, Charset.forName("windows-1252"));
		BufferedReader br = new BufferedReader(reader);
		
		int c = -1;
		int v = -1;
		
	    try {
	        String linea = br.readLine();
	        
	        if (Auxiliar.nz(linea, "").equals("")) {
				r += Auxiliar.mensaje("advertencia", "Linea de titulos de campos no encontrada.", usuario, metodo);
				procesar = false;
	        }

	        
	        int n_campos = 12;
	        
	        a_campos = linea.split(s);
			int a_campos_length = a_campos.length;

	        if (a_campos.length != n_campos) {
				r += Auxiliar.mensaje("advertencia", "Linea de titulos de campos no válida. Numero de campos encontrados:" + a_campos.length, usuario, metodo);
				procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ACCION")) {
				r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ACCION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
				procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("CONSECUTIVO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo CONSECUTIVO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ID_IMPORTACION")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ID_IMPORTACION. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("INDV_CONSECUTIVO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo INDV_CONSECUTIVO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DAP1")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DAP1. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DAP2")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DAP2. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ALTURADAP")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ALTURADAP. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ALTURA")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo ALTURA. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("ALTURATOTAL")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+"debe tener el titulo ALTURATOTAL. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("FORMAFUSTE")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo FORMAFUSTE. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("DANIO")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo DANIO. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }

	        c++; if (c < a_campos_length)	        
	        if (!Auxiliar.nz(a_campos[c].trim().replace("\"",  ""), "").equals("OBSERVACIONES")) {
	        	r += Auxiliar.mensaje("advertencia", "La columna "+String.valueOf(c)+" debe tener el titulo OBSERVACIONES. En lugar de eso tiene:" + a_campos[c].trim(), usuario, metodo);
	    		procesar = false;
	        }
	        
			if (!procesar) {
			    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='"+r+"' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
			    
			    try {
			    	dbREDD.ejecutarSQL(sql_registro_estado);
			    }
			    catch (Exception e) {
			    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
			    }

				dbREDD.desconectarse();
				return r;
			}
			else {
			    String sql_registro_estado = "UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='ARCHIVO VALIDADO EN SU FORMATO. PROCESANDO...' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo;
			    
			    try {
			    	dbREDD.ejecutarSQL(sql_registro_estado);
			    }
			    catch (Exception e) {
			    	r += Auxiliar.mensaje("error", "Problemas al registrar el estado del archivo:" + e.toString(), usuario, metodo);
			    }
			}

	        
	        // DEPURAR LOG		System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "ProcesarArchivo: Inicializando cartero...");

			dbREDD.ejecutarSQL("DELETE FROM RED_TALLO_IMPORTACION WHERE TAYO_ARCH_CONSECUTIVO=" + TAYO_ARCH_CONSECUTIVO);

	        long i = 0;
	        
			String str_ok_transaccion = "";
			String ok_transaccion = "";
			String [] a_ok_transaccion = null;
			boolean ok_validacion = false;
	        
	        while (linea != null) {
	        	i++;
	        	v = -1;
	        	
	        	ok_validacion = true;
	        	
				linea = br.readLine();

	            if (linea == null) {
	            	break;
	            }
	            
	            String lini = linea.substring(0, 5);
	            
	            if (lini.equals("Poner") || lini.equals("Put E")) {
	            	continue;
	            }
	            
	    		
	    		// CAMPOS DE LA TABLA DE INDIVIDUOS
	    		TAYO_ID = "";
	    		
	    		TAYO_ARCH_CONSECUTIVO = consecutivo_archivo;
	    		TAYO_OBSERVACIONES_IMPORTACION = "";
	    		
	    		TAYO_CONSECUTIVO = "";
	    		TAYO_INDV_CONSECUTIVO = "";
				if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
					TAYO_INDV_CONSECUTIVO = INDV_CONSECUTIVO;
				}
	    		TAYO_ID_IMPORTACION = "";
	    		TAYO_DAP1 = ""; 
	    		TAYO_DAP2 = ""; 
	    		TAYO_ALTURADAP = ""; 
	    		TAYO_ALTURA = ""; 
	    		TAYO_ALTURATOTAL = ""; 
	    		TAYO_FORMAFUSTE = "";
	    		TAYO_DANIO = "";
	    		TAYO_OBSERVACIONES = "";
	            
	    		TAYO_ID_IMPORTACION = dbREDD.obtenerDato("SELECT COALESCE(MAX(TAIM_ID_IMPORTACION),0)+1 AS CONSECUTIVO FROM RED_TALLO_IMPORTACION","");

				a_valores = linea.split(s);

		        
		        if (a_valores.length != n_campos) {
		        	TAYO_OBSERVACIONES_IMPORTACION += Auxiliar.mensaje("advertencia", "Linea no válida. Numero de campos encontrados:" + a_valores.length, usuario, metodo);
					ok_validacion = false;
		        }
		        else {
		        	v++; ACCION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; TAYO_CONSECUTIVO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; TAYO_ID_IMPORTACION = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; TAYO_INDV_CONSECUTIVO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; TAYO_DAP1 = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; TAYO_DAP2 = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; TAYO_ALTURADAP = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; TAYO_ALTURA = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; TAYO_ALTURATOTAL = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "").replace(",", ".");
					v++; TAYO_FORMAFUSTE = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; TAYO_DANIO = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");
					v++; TAYO_OBSERVACIONES = Auxiliar.nz(a_valores[v].trim().replace("\"",  ""), "");

					if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
						TAYO_INDV_CONSECUTIVO = INDV_CONSECUTIVO;
					}

					// DEFINIR WHERE PARA IDENTIFICACIÓN DEL TALLO
					sql_indv_where_red = " TAYO_ID_IMPORTACION=" + TAYO_ID_IMPORTACION;

					Tallo tayo = new Tallo();
					
					if (ACCION.equals("ELIMINAR")) {
						if (!TAYO_ID_IMPORTACION.equals("")) {
							// VERIFICAR QUE EL TALLO EXISTA
							try {
								sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_TALLO WHERE TAYO_ID_IMPORTACION=" + TAYO_ID_IMPORTACION;
								conteo = dbREDD.obtenerDato(sql_tmp, "0");
							}
							catch (Exception e) {
								r += Auxiliar.mensaje("error", "Error al intentar encontrar tallo con el ID_IMPORTACION " + TAYO_ID_IMPORTACION + " para eliminarlo. SQL: [" + sql_tmp + "]. Último error en la bd de RED: " + dbREDD.ultimoError + ". Excepción: " + e.toString(), usuario, metodo);								
							}
							
							if (conteo.equals("0")) {
								r += Auxiliar.mensaje("advertencia", "No se encontró tallo alguno con el ID_IMPORTACION " + TAYO_ID_IMPORTACION, usuario, metodo);
							}
							else {
								try {
									TAYO_CONSECUTIVO = dbREDD.obtenerDato("SELECT TAYO_CONSECUTIVO FROM RED_INDIVIDUO WHERE TAYO_ID_IMPORTACION="+TAYO_ID_IMPORTACION, "");
									
									if (Auxiliar.tieneAlgo(TAYO_ID_IMPORTACION)) {
										str_ok_transaccion = tayo.eliminar(TAYO_CONSECUTIVO, request);
										a_ok_transaccion = str_ok_transaccion.split("-=-");
										ok_transaccion = a_ok_transaccion[0];
										
										// CONFIRMAR QUE SE PUDO ELIMINAR
										dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_PROCESADOS=ARCH_PROCESADOS+1 WHERE ARCH_CONSECUTIVO="+consecutivo_archivo);
									}									
								}
								catch (SQLException e) {
									r += Auxiliar.mensaje("error", "Error al intentar eliminar tallo en RED con el consecutivo " + TAYO_ID_IMPORTACION + ". SQL: [" + sql_eliminar + "]. Último error en la bd: " + dbREDD.ultimoError + ". Excepción: " + e.toString(), usuario, metodo);
								}
							}
						} 
						else {
							r += Auxiliar.mensaje("advertencia", "Para poder eliminar un individuo es necesario especificar el consecutivo.", usuario, metodo);
						}						
					}
					else {
						TAYO_OBSERVACIONES_IMPORTACION += "";

						
						if (ok_validacion) {
							if (TAYO_CONSECUTIVO.equals("")) {
								if (Auxiliar.tieneAlgo(TAYO_ID_IMPORTACION)) {
									sql_tmp = "SELECT TAYO_CONSECUTIVO FROM RED_TALLO WHERE ";
									sql_tmp += " TAYO_ID_IMPORTACION=" + TAYO_ID_IMPORTACION;
								}
								else {
									sql_tmp = "";
								}
									
								String TAYO_CONSECUTIVO_EXISTENTE = "";

								if (Auxiliar.tieneAlgo(sql_tmp)) {
									try {
										TAYO_CONSECUTIVO_EXISTENTE = dbREDD.obtenerDato(sql_tmp, "");
									} catch (Exception e) {
									}
								}
								
								if (Auxiliar.tieneAlgo(TAYO_CONSECUTIVO_EXISTENTE)) {
									TAYO_CONSECUTIVO = TAYO_CONSECUTIVO_EXISTENTE;
								}
							}
							
							if (Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
								TAYO_ID_IMPORTACION = dbREDD.obtenerDato("SELECT TAYO_ID_IMPORTACION FROM RED_TALLO WHERE TAYO_CONSECUTIVO="+TAYO_CONSECUTIVO, "");
							}
							
							if (!Auxiliar.tieneAlgo(TAYO_ID_IMPORTACION)) {
								TAYO_ID_IMPORTACION = dbREDD.obtenerDato("SELECT COALESCE(MAX(TAYO_ID_IMPORTACION),0)+1 AS CONSECUTIVO FROM RED_TALLO", "");
							}

							session.setAttribute("RED_username", this.RED_username);
							session.setAttribute("RED_password", this.RED_password);
							session.setAttribute("RED_host", this.RED_host);
							session.setAttribute("RED_port", this.RED_port);
							session.setAttribute("RED_sid", this.RED_sid);
							
							str_ok_transaccion = tayo.guardar(
									TAYO_CONSECUTIVO,
									TAYO_INDV_CONSECUTIVO,
									TAYO_DAP1,
									TAYO_DAP2,
									TAYO_ALTURADAP,
									TAYO_ALTURA,
									TAYO_ALTURATOTAL,
									TAYO_FORMAFUSTE,
									TAYO_DANIO,
									TAYO_OBSERVACIONES,
									TAYO_ID_IMPORTACION,
									TAYO_ARCH_CONSECUTIVO,
									request,
									true
									);
							
							a_ok_transaccion = str_ok_transaccion.split("-=-");
							ok_transaccion = a_ok_transaccion[0];
							
							if (a_ok_transaccion.length == 2) {
								TAYO_OBSERVACIONES_IMPORTACION = a_ok_transaccion[1].toString();
							}
						}
						
						sql_idim = "INSERT INTO RED_TALLO_IMPORTACION ";
						sql_idim += "(";
						sql_idim += "TAIM_ESTADOIMPORTACION,";
						sql_idim += "TAIM_OBSERVACIONES_IMPORTACION,";
						sql_idim += "TAIM_CONSECUTIVO,";
						sql_idim += "TAIM_INDV_CONSECUTIVO,";
						sql_idim += "TAIM_ID_IMPORTACION,"; 
						sql_idim += "TAIM_DAP1,"; 
						sql_idim += "TAIM_DAP2,"; 
						sql_idim += "TAIM_ALTURADAP,"; 
						sql_idim += "TAIM_ALTURA,"; 
						sql_idim += "TAIM_ALTURATOTAL,";
						sql_idim += "TAIM_FORMAFUSTE,";
						sql_idim += "TAIM_DANIO,";
						sql_idim += "TAIM_OBSERVACIONES,";
						sql_idim += "TAIM_ARCH_CONSECUTIVO";
						sql_idim += ")";
						sql_idim += " VALUES ";
						sql_idim += "(";
						sql_idim += Auxiliar.nzVacio(a_ok_transaccion[0], "NULL") + ",";
						int longitud = (TAYO_OBSERVACIONES_IMPORTACION.length() < MAX_VARCHAR2) ? TAYO_OBSERVACIONES_IMPORTACION.length() : MAX_VARCHAR2;
						sql_idim += "'" + TAYO_OBSERVACIONES_IMPORTACION.substring(0, longitud).replace("'", "`") + "',";
						sql_idim += Auxiliar.nzVacio(TAYO_CONSECUTIVO, "NULL") + ",";
						sql_idim += "'" + TAYO_INDV_CONSECUTIVO + "',";
						sql_idim += "" + TAYO_ID_IMPORTACION + ","; 
						sql_idim += "'" + TAYO_DAP1 + "',"; 
						sql_idim += "'" + TAYO_DAP2 + "',"; 
						sql_idim += "'" + TAYO_ALTURADAP + "',"; 
						sql_idim += "'" + TAYO_ALTURA + "',"; 
						sql_idim += "'" + TAYO_ALTURATOTAL + "',";
						sql_idim += "'" + TAYO_FORMAFUSTE + "',";
						sql_idim += "'" + TAYO_DANIO + "',";
						sql_idim += "'" + TAYO_OBSERVACIONES + "',";
						sql_idim += TAYO_ARCH_CONSECUTIVO + "";
						sql_idim += ")";
						
						try {
						    r += Auxiliar.mensaje("nota", "Observaciones para la fila " + i, usuario, metodo);
							if (Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
								dbREDD.ejecutarSQL("DELETE FROM RED_TAYO_IMPORTACION WHERE TAIM_CONSECUTIVO="+TAYO_CONSECUTIVO);
							}
						    if (!dbREDD.ejecutarSQL(sql_idim)) {
					    		Auxiliar.mensaje("error", "No pude registrar la importación de un tallo: " + dbREDD.ultimoError, usuario, metodo);
						    }
						    dbREDD.cometerTransaccion();
						}
						catch (SQLException e) {
							r += Auxiliar.mensaje("error", "Inconveniente al ejecutar consulta SQL [" + sql_idim + "]: " + e.toString() + ". Ultimo error:" + dbREDD.ultimoError, usuario, metodo);
						}
					}
		        }
		        
			    r += Auxiliar.mensaje("confirmacion", "Fila " + i + " procesada.", usuario, metodo);

			    try {
		        	dbREDD.establecerAutoCometer(true);
		        }
		        catch (Exception e) {
		        	r += Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
		        }

	        }
	        
	        // ACTUALIZAR ESTADISTICAS DE IMPORTACIÓN
	        recalcularEstadisticasArchivo(consecutivo_archivo, session);
	    	dbREDD.ejecutarSQL("UPDATE RED_ARCHIVO_IMPORTACION SET ARCH_ESTADO='ARCHIVO PROCESADO' WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo);

	    	String base_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='base_url_admif'", "http://172.16.1.125/AdmIF");

	        r += Auxiliar.mensaje("nota", "<a class=boton href='"+base_url+"/CargueMasivo?accion=log_importacion_archivo_tallos&arch_consecutivo="+consecutivo_archivo+"'>Log</a>", usuario, metodo);
	        
	    }
	    catch (Exception e) {
	    	r += Auxiliar.mensaje("error", "Error al procesar una línea del archivo.  Detalle del error:" + e.toString() + dbREDD.ultimoError, usuario, metodo);
			System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "Excepción en importarTallos: " + e.toString() + dbREDD.ultimoError);
	    }
	    finally {
	        br.close();
	    }

		
		dbREDD.desconectarse();
		return r;
	}

	
	/**
	 * Metodo para obtener la ruta de un archivo
	 * 
	 * @param consecutivo_archivo
	 * @return String con la ruta
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String obtenerRutaArchivo(String consecutivo_archivo, HttpServletRequest request)
	throws ClassNotFoundException, Exception
	{
		String metodo = yo + "obtenerRutaArchivo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		String sql = "";
		
		if (consecutivo_archivo == null) {
			dbREDD.desconectarse();
			return "";
		}
		
		sql = "SELECT ARCH_CARPETA || '/' || ARCH_NOMBRE AS RUTA FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO="+consecutivo_archivo;
		
		try {
			r = dbREDD.obtenerDato(sql, "");
		} catch (Exception e) {
			r += Auxiliar.mensaje("error", "Inconveniente al obtener la ruta del archivo. ["+sql+"]: " + e.toString() + ". Ultimo error:" + dbREDD.ultimoError, usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return r;
	}

	
	
	/**
	 * Metodo para importar imagenes de parcelas masivamente.
	 * 
	 * @param consecutivo_archivo
	 * @return String con el resultado de la importación
	 * @throws Exception 
	 */
	public String importarImagenesParcelas(String consecutivo_archivo, HttpServletRequest request)
			throws Exception {
		String metodo = yo + "importarImagenesParcelas";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		
		// OBTENER RUTA DEL ARCHIVO
		
		String carpeta_archivo = dbREDD.obtenerDato("SELECT ARCH_CARPETA FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		String nombre_archivo = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		String ruta_archivo = carpeta_archivo + "/" + nombre_archivo;
		
		
		// VERIFICAR QUE EL ARCHIVO EXISTE
		
		if (!archie.existeArchivo(ruta_archivo)) {
			r += Auxiliar.mensaje("advertencia", "No pude encontrar el archivo comprimido de imágenes " + ruta_archivo, usuario, metodo);
			sec.registrarTransaccion(request, 213, consecutivo_archivo, "No pude encontrar el archivo comprimido de imágenes " + ruta_archivo, false);
			dbREDD.desconectarse();
			return r;
		}
		
		
		// VERIFICAR QUE SEA ZIP O 7Z
		
		String extension = archie.extensionArchivo(nombre_archivo);
		
		if (!extension.toLowerCase().equals("zip") && !extension.toLowerCase().equals("7z")) {
			r += Auxiliar.mensaje("advertencia", "Archivo comprimido de imágenes " + ruta_archivo + " ha de ser un .zip o un .7z", usuario, metodo);
			sec.registrarTransaccion(request, 213, consecutivo_archivo, "Archivo comprimido de imágenes " + ruta_archivo + " ha de ser un .zip o un .7z", false);
			dbREDD.desconectarse();
			return r;
		}
		
		
		// GARANTIZAR QUE EXISTA LA PARCELA
		
		String PRCL_CONSECUTIVO = archie.nombreBaseArchivo(nombre_archivo);
		String existe = dbREDD.obtenerDato("SELECT PRCL_CONSECUTIVO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO, "");
		if (existe.equals("")) {
			r += Auxiliar.mensaje("advertencia", "No se pudieron importar las imágenes de la parcela " + PRCL_CONSECUTIVO + " puesto que no existe en la base de datos.", usuario, metodo);
			sec.registrarTransaccion(request, 213, consecutivo_archivo, "No se pudieron importar las imágenes de la parcela " + PRCL_CONSECUTIVO + " puesto que no existe en la base de datos.", false);
			dbREDD.desconectarse();
			return r;
		}
		
		
		// OBTENER RUTA DE LA APLICACIÓN
		
		//String ruta_app = Auxiliar.rutApp();
		String ruta_app = getServletContext().getRealPath("");
		//String ruta_carpeta_imagenes_parcelas = ruta_app + File.separator + "imagenes_parcelas";
		String ruta_carpeta_imagenes_parcelas = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_parcelas'", "");
		if (!Auxiliar.tieneAlgo(ruta_carpeta_imagenes_parcelas)) {
			ruta_carpeta_imagenes_parcelas = ruta_app + File.separator + "imagenes_parcelas";
		}
		
		
		// CREAR LA CARPETA DE IMAGENES DE PARCELAS
		
		if (!archie.crearCarpeta(ruta_carpeta_imagenes_parcelas)) {
			r += Auxiliar.mensaje("error", "No se pudo crear o acceder la carpeta de imágenes de parcelas " + ruta_carpeta_imagenes_parcelas, usuario, metodo);
			sec.registrarTransaccion(request, 213, consecutivo_archivo, "No se pudo crear o acceder la carpeta de imágenes de parcelas " + ruta_carpeta_imagenes_parcelas, false);
			dbREDD.desconectarse();
			return r;
		}
		
		
		// DESCOMPRIMIR ARCHIVO EN LA CARPETA DE IMAGENES DE PARCELAS
		
		String commando_extraccion_win = ruta_app + File.separator + "7za x -y -o" + ruta_carpeta_imagenes_parcelas + " " + ruta_archivo	+ "";
		String commando_extraccion_lin = "7za x -y -o" + ruta_carpeta_imagenes_parcelas + " " + ruta_archivo	+ "";
		String resultado_extraccion = Auxiliar.commander(commando_extraccion_win, commando_extraccion_lin, session);
		
		String [] a_resultado_extraccion = resultado_extraccion.split("-=-");
		
		if (!Auxiliar.nz(a_resultado_extraccion[0], "").equals("0")) {
			r += Auxiliar.mensaje("error", "No se pudieron extraer las imágenes de las parcelas del archivo " + ruta_archivo + " (comando utilizado: " + commando_extraccion_win + " o " + commando_extraccion_lin + ") : " + resultado_extraccion, usuario, metodo);
			sec.registrarTransaccion(request, 213, consecutivo_archivo, "No se pudieron extraer las imágenes de las parcelas del archivo " + ruta_archivo + " (comando utilizado: " + commando_extraccion_win + " o " + commando_extraccion_lin + ") : " + resultado_extraccion, false);
			dbREDD.desconectarse();
			return r;
		}
		else {
			r += Auxiliar.mensaje("confirmacion", "Extracción exitosa, las imágenes de las parcelas del archivo " + ruta_archivo + " fueron extraidas con el comando " + commando_extraccion_win + " o " + commando_extraccion_lin + ". Se obtuvo el siguiente resultado: " + Auxiliar.nz(a_resultado_extraccion[1], ""), usuario, metodo);
			sec.registrarTransaccion(request, 213, consecutivo_archivo, "Extracción exitosa, las imágenes de las parcelas del archivo " + ruta_archivo + " fueron extraidas con el comando " + commando_extraccion_win + " o " + commando_extraccion_lin + ". Se obtuvo el siguiente resultado: " + Auxiliar.nz(a_resultado_extraccion[1], ""), true);
			dbREDD.desconectarse();
			return r;
		}
	}	
	
	/**
	 * Metodo para importar imagenes de inviduos masivamente.
	 * 
	 * @param consecutivo_archivo
	 * @return String con el resultado de la importación
	 * @throws Exception 
	 */
	public String importarImagenesIndividuos(String consecutivo_archivo, HttpServletRequest request)
	throws Exception {
		String metodo = yo + "importarImagenesIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
	    LenguajeI18N L = new LenguajeI18N();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    ResourceBundle msj = setLenguaje(idioma, L);
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		String r = "";
		
		
		// OBTENER RUTA DEL ARCHIVO
		
		String carpeta_archivo = dbREDD.obtenerDato("SELECT ARCH_CARPETA FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		String nombre_archivo = dbREDD.obtenerDato("SELECT ARCH_NOMBRE FROM RED_ARCHIVO_IMPORTACION WHERE ARCH_CONSECUTIVO=" + consecutivo_archivo, "");
		String ruta_archivo = carpeta_archivo + "/" + nombre_archivo;
		
		
		// VERIFICAR QUE EL ARCHIVO EXISTE
		
		if (!archie.existeArchivo(ruta_archivo)) {
			r += Auxiliar.mensaje("advertencia", "No pude encontrar el archivo comprimido de imágenes " + ruta_archivo, usuario, metodo);
			sec.registrarTransaccion(request, 214, consecutivo_archivo, "No pude encontrar el archivo comprimido de imágenes " + ruta_archivo, false);
			dbREDD.desconectarse();
			return r;
		}
		
		
		// VERIFICAR QUE SEA ZIP O 7Z
		
		String extension = archie.extensionArchivo(nombre_archivo);
		
		if (!extension.toLowerCase().equals("zip") && !extension.toLowerCase().equals("7z")) {
			r += Auxiliar.mensaje("advertencia", "Archivo comprimido de imágenes " + ruta_archivo + " ha de ser un .zip o un .7z", usuario, metodo);
			sec.registrarTransaccion(request, 214, consecutivo_archivo, "Archivo comprimido de imágenes " + ruta_archivo + " ha de ser un .zip o un .7z", false);
			dbREDD.desconectarse();
			return r;
		}

		
		// GARANTIZAR QUE EXISTA EL INDIVIDUO
		
		String INDV_CONSECUTIVO = archie.nombreBaseArchivo(nombre_archivo);
		String existe = dbREDD.obtenerDato("SELECT INDV_CONSECUTIVO FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO=" + INDV_CONSECUTIVO, "");
		if (existe.equals("")) {
			r += Auxiliar.mensaje("advertencia", "No se pudieron importar las imágenes del individuo " + INDV_CONSECUTIVO + " puesto que no existe en la base de datos.", usuario, metodo);
			sec.registrarTransaccion(request, 214, consecutivo_archivo, "No se pudieron importar las imágenes del individuo " + INDV_CONSECUTIVO + " puesto que no existe en la base de datos.", false);
			dbREDD.desconectarse();
			return r;
		}
		
		
		// OBTENER RUTA DE LA APLICACIÓN
		
		//String ruta_app = Auxiliar.rutApp();
		String ruta_app = getServletContext().getRealPath("");
		//String ruta_carpeta_imagenes_individuos = ruta_app + File.separator + "imagenes_individuos";
		String ruta_carpeta_imagenes_individuos = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_individuos'", "");
		if (!Auxiliar.tieneAlgo(ruta_carpeta_imagenes_individuos)) {
			ruta_carpeta_imagenes_individuos = ruta_app + File.separator + "imagenes_individuos";
		}

		
		
		// CREAR LA CARPETA DE IMAGENES DE INDIVIDUOS
		
		if (!archie.crearCarpeta(ruta_carpeta_imagenes_individuos)) {
			r += Auxiliar.mensaje("error", "No se pudo crear o acceder la carpeta de imágenes de individuos " + ruta_carpeta_imagenes_individuos, usuario, metodo);
			sec.registrarTransaccion(request, 214, consecutivo_archivo, "No se pudo crear o acceder la carpeta de imágenes de individuos " + ruta_carpeta_imagenes_individuos, false);
			dbREDD.desconectarse();
			return r;
		}

		
		// DESCOMPRIMIR ARCHIVO EN LA CARPETA DE IMAGENES DE INDIVIDUOS
		
		String commando_extraccion_win = ruta_app + File.separator + "7za x -y -o" + ruta_carpeta_imagenes_individuos + " " + ruta_archivo	+ "";
		String commando_extraccion_lin = "7za x -y -o" + ruta_carpeta_imagenes_individuos + " " + ruta_archivo	+ "";
		String resultado_extraccion = Auxiliar.commander(commando_extraccion_win, commando_extraccion_lin, session);
		
		String [] a_resultado_extraccion = resultado_extraccion.split("-=-");

		if (!Auxiliar.nz(a_resultado_extraccion[0], "").equals("0")) {
			r += Auxiliar.mensaje("error", "No se pudieron extraer las imágenes de los individuos del archivo " + ruta_archivo + " (comando utilizado: " + commando_extraccion_win + " o " + commando_extraccion_lin + ") : " + resultado_extraccion, usuario, metodo);
			sec.registrarTransaccion(request, 214, consecutivo_archivo, "No se pudieron extraer las imágenes de los individuos del archivo " + ruta_archivo + " (comando utilizado: " + commando_extraccion_win + " o " + commando_extraccion_lin + ") : " + resultado_extraccion, false);
			dbREDD.desconectarse();
			return r;
		}
		else {
			r += Auxiliar.mensaje("confirmacion", "Extracción exitosa, las imágenes de los individuos del archivo " + ruta_archivo + " fueron extraidas con el comando " + commando_extraccion_win + " o " + commando_extraccion_lin + ". Se obtuvo el siguiente resultado: " + Auxiliar.nz(a_resultado_extraccion[1], ""), usuario, metodo);
			sec.registrarTransaccion(request, 214, consecutivo_archivo, "Extracción exitosa, las imágenes de los individuos del archivo " + ruta_archivo + " fueron extraidas con el comando " + commando_extraccion_win + " o " + commando_extraccion_lin + ". Se obtuvo el siguiente resultado: " + Auxiliar.nz(a_resultado_extraccion[1], ""), true);
			dbREDD.desconectarse();
			return r;
		}
	}	
	
}
//3935