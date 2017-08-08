// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.activation.FileDataSource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Clase Tallo
 * 
 * Permite administrar los Tallos de los Individuos.
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com
 * 
 */
@SuppressWarnings("serial") public class ImagenesParcela extends HttpServlet {

	public static String		yo					= "ImagenesParcela.";
	public static String		charset				= "ISO-8859-1";
	public static String		css					= "css/estilos.css";

	private static final String	UPLOAD_DIRECTORY	= "imagenes_parcelas";

	String						RED_username		= "";
	String						RED_password		= "";
	String						RED_host			= "";
	String						RED_port			= "";
	String						RED_sid				= "";

	String						encriptar_usuario	= "";
	String						llave_encripcion	= "";

	/**
	 * Método mudo para inicializar la clase A partir de la variable config se obtienen los parámetros de conexión a las bases de datos.
	 */
	public void init(ServletConfig config) throws ServletException {

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
	public ImagenesParcela() {
	}

	/**
	 * Método que traduce a post el get si recibió un request get
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Método que procesa el request de POST que recibió la clase
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String metodo = yo + "doPost";

		String usuario_recibido = "";
		String usuario = "";
		String idioma = "";
		String clave = "";

		String ruta_archivo = "";

		String db_PRCL_CONSECUTIVO = "";
		String db_PRCL_NOMBREARCHIVO = "";

		String retorno = "";
		String target = "";

		// Instanciar auxiliar y archivo
		// Auxiliar aux = new Auxiliar();
		Archivo archie = new Archivo();
		Sec sec = new Sec();

		request.setCharacterEncoding("UTF-8");

		BD dbREDD = null;
		try {
			dbREDD = new BD();
		}
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}

		String accion = "";
		boolean autenticado = true;

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

		if (!autenticado) {
			target = "/login.jsp";
			retorno = Auxiliar.mensajeImpersonal("advertencia", Auxiliar.traducir("SESION_VENCIDA", "es", "Credenciales erradas o Su sesión ha vencido"));
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else {
			String resultado = "";
			resultado = "1-=- ";

			db_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "");

			String ruta_carpeta_parcela = "";
			String carpetaImportacion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_parcelas'", "");
			if (!Auxiliar.tieneAlgo(carpetaImportacion) || !archie.existeArchivo(carpetaImportacion)) {
				carpetaImportacion = getServletContext().getRealPath("") + File.separator + "imagenes_parcelas";
			}
			ruta_carpeta_parcela = carpetaImportacion + File.separator + db_PRCL_CONSECUTIVO;

			String id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO + "", "");

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				response.setContentType("text/html; charset=UTF-8");
				try {
					String id_usuario = "";
					if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='" + usuario + "'", "");

					boolean privilegio = false;

					db_PRCL_NOMBREARCHIVO = "";

					String f_imagen = "";

					try {
						DiskFileItemFactory factory = new DiskFileItemFactory();

						int max_kb_imagen = 300;
						String parametro_tamanio_imagen = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='max_kb_imagen'", "300");
						if (Auxiliar.esEnteroMayorOIgualACero(parametro_tamanio_imagen)) {
							max_kb_imagen = Integer.parseInt(parametro_tamanio_imagen);
						}

						long max_bytes_imagen = max_kb_imagen * 1024;

						ServletFileUpload upload = new ServletFileUpload(factory);

						List<FileItem> items = upload.parseRequest(request);

						for (FileItem item : items) {
							if (item.isFormField()) {
								String fieldname = item.getFieldName();
								String fieldvalue = item.getString();

								if (fieldname.equals("PRCL_CONSECUTIVO")) {
									db_PRCL_CONSECUTIVO = Auxiliar.nz(fieldvalue, "");
								}

								if (fieldname.equals("nombre_imagen")) {
									f_imagen = Auxiliar.nz(fieldvalue, "");
								}

								if (fieldname.equals("accion")) {
									accion = Auxiliar.nz(fieldvalue, "");
								}
							}
						}

						ruta_carpeta_parcela = carpetaImportacion + File.separator + db_PRCL_CONSECUTIVO;
						id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO + "", "");

						try {
							archie.crearCarpeta(ruta_carpeta_parcela);
						}
						catch (Exception e) {}

						if (accion.equals("registrar")) {
							if (sec.tienePermiso(usuario, "239") || sec.tienePermiso(usuario, "240")) {
								for (FileItem item : items) {
									if (!item.isFormField()) {
										String contentType = item.getContentType();
										if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
											resultado = "0-=-" + Auxiliar.mensaje("advertencia", "Sólo se aceptan imágenes en los formatos .png o .jpg.", usuario, metodo);
											break;
										}

										long sizeInBytes = item.getSize();
										if (sizeInBytes > max_bytes_imagen) {
											resultado = "0-=-" + Auxiliar.mensaje("advertencia", "El tamaño del archivo no debe superar " + parametro_tamanio_imagen + "KB", usuario, metodo);
											break;
										}

										String nombre_archivo = new File(item.getName()).getName();

										String extension_imagen = "";

										if (nombre_archivo.toLowerCase().contains(".png")) {
											extension_imagen = ".png";
										}
										else if (nombre_archivo.toLowerCase().contains(".jp")) {
											extension_imagen = ".jpg";
										}
										else {
											extension_imagen = "";
										}

										if (!extension_imagen.equals("")) {
											ruta_archivo = ruta_carpeta_parcela + File.separator + nombre_archivo;
											File archivo_a_guardar = new File(ruta_archivo);

											try {
												if (id_usuario.equals(id_creador)) {
													if (sec.tienePermiso(id_usuario, "239") || sec.tienePermiso(id_usuario, "240")) {
														privilegio = true;
													}
												}
												else {
													if (sec.tienePermiso(id_usuario, "240")) {
														privilegio = true;
													}
												}

												if (!privilegio) {
													dbREDD.desconectarse();
													resultado = "0-=-" + Auxiliar.mensaje("advertencia", "No tiene privilegio para realizar esta operación. Por favor, contacte al administrador.", usuario, metodo);
												}
												else {
													item.write(archivo_a_guardar);

													if (archie.existeArchivo(ruta_archivo)) {
														resultado = "1-=-" + Auxiliar.mensaje("confirmacion", "Imágen de parcela " + ruta_archivo + " guardada.", usuario, metodo);

														String id_imagen_existente = dbREDD.obtenerDato("SELECT PIMG_ID FROM RED_PARCELA_IMAGEN WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO + " NOMBRE='" + nombre_archivo + "'", "");
														if (id_imagen_existente.equals("")) {
															String sql_insertar = "INSERT INTO RED_PARCELA_IMAGEN (PRCL_CONSECUTIVO, NOMBRE) VALUES (" + db_PRCL_CONSECUTIVO + ", '" + nombre_archivo + "')";
															dbREDD.ejecutarSQL(sql_insertar);

															if (id_usuario.equals(id_creador)) {
																sec.registrarTransaccion(request, 239, "", sql_insertar, true);
															}
															else {
																sec.registrarTransaccion(request, 240, "", sql_insertar, true);
															}
														}
													}
													else {
														resultado = "0-=-" + Auxiliar.mensaje("error", "Inconvenientes al guardar la imagen de la parcela " + ruta_archivo + ".", usuario, metodo);
													}
												}
											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
							}
							else {
								resultado = "0-=-" + Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							}
						}
						else if (accion.equals("eliminar")) {
							if (sec.tienePermiso(usuario, "241") || sec.tienePermiso(usuario, "242")) {
								if (id_usuario.equals(id_creador)) {
									if (sec.tienePermiso(id_usuario, "241") || sec.tienePermiso(id_usuario, "242")) {
										privilegio = true;
									}
								}
								else {
									if (sec.tienePermiso(id_usuario, "242")) {
										privilegio = true;
									}
								}

								if (!privilegio) {
									dbREDD.desconectarse();
									resultado = "0-=-" + Auxiliar.mensaje("advertencia", "No tiene privilegio para realizar esta operación. Por favor, contacte al administrador.", usuario, metodo);
								}
								else {
									boolean ok_eliminar_imagen_bd = false;

									String nombre_imagen = f_imagen.replace(UPLOAD_DIRECTORY + File.separator + db_PRCL_CONSECUTIVO + File.separator, "");

									if (!nombre_imagen.equals("")) {
										String sql_eliminar = "DELETE FROM RED_PARCELA_IMAGEN WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO + " AND NOMBRE='" + nombre_imagen + "'";
										ok_eliminar_imagen_bd = dbREDD.ejecutarSQL(sql_eliminar);
										if (ok_eliminar_imagen_bd) {
											boolean ok_eliminar = archie.eliminarArchivo(ruta_carpeta_parcela + File.separator + nombre_imagen);

											if (id_usuario.equals(id_creador)) {
												sec.registrarTransaccion(request, 241, "", sql_eliminar, ok_eliminar);
											}
											else {
												sec.registrarTransaccion(request, 242, "", sql_eliminar, ok_eliminar);
											}
										}
									}
								}
							}
							else {
								resultado = "0-=-" + Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							}
						}
					}
					catch (FileUploadException e) {
						e.printStackTrace();
					}
				}
				catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el cargue de imágenes: " + e.toString(), usuario, metodo);
					e.printStackTrace();
				}
			}

			if (!Auxiliar.tieneAlgo(resultado)) {
				resultado = "1-=-";
			}
			
			// DETECTAR NUEVAS IMAGENES QUE ESTÉN EN LA CARPETA Y REGISTRARLAS EN LA BD SI AÚN NO ESTÁN REGISTRADAS
			
			try {
				if (archie.existeArchivo(ruta_carpeta_parcela)) {
					String[] a_nombres_archivos_en_carpeta = archie.archivosEnCarpeta(ruta_carpeta_parcela);
					
					for (int a=0; a<a_nombres_archivos_en_carpeta.length; a++) {
						String nombre_archivo_en_carpeta = a_nombres_archivos_en_carpeta[a];
						String ruta_archivo_en_carpeta = ruta_carpeta_parcela + File.separator + nombre_archivo_en_carpeta;
						File archivo_en_carpeta = new File(ruta_archivo_en_carpeta);
						FileDataSource fds = new FileDataSource(archivo_en_carpeta);
						String contentType = fds.getContentType();
						if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
							resultado += Auxiliar.mensaje("advertencia", "Se encontró un archivo que no es imagen ("+nombre_archivo_en_carpeta+") en los formatos .png o .jpg en la carpeta de la parcela "+db_PRCL_CONSECUTIVO+".", usuario, metodo);
						}
						else {
							// OBTENER ID EXISTENTE DE LA BD
							String id_imagen_existente = dbREDD.obtenerDato("SELECT ID FROM RED_PARCELA_IMAGEN WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO + " AND NOMBRE='"+nombre_archivo_en_carpeta+"'", "");
							if (!Auxiliar.tieneAlgo(id_imagen_existente)) {
								// CREAR
								boolean ok_agregar_imagen_de_carpeta = dbREDD.ejecutarSQL("INSERT INTO RED_PARCELA_IMAGEN (PRCL_CONSECUTIVO, NOMBRE) VALUES ("+db_PRCL_CONSECUTIVO+",'"+nombre_archivo_en_carpeta+"')");
								if (ok_agregar_imagen_de_carpeta) {
									resultado +=  Auxiliar.mensaje("confirmacion", "Imagen importada ("+nombre_archivo_en_carpeta+") desde la carpeta de la parcela "+db_PRCL_CONSECUTIVO+".", usuario, metodo);
								}
							}
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			db_PRCL_NOMBREARCHIVO = "";

			try {
				String sql_imagenes = "SELECT PIMG, PRCL_CONSECUTIVO, NOMBRE FROM RED_PARCELA_IMAGEN WHERE PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO;
	
				ResultSet rs_imagenes = dbREDD.consultarBD(sql_imagenes);
	
				if (rs_imagenes != null) {
					String ruta = "";
					String nombre = "";
	
					while (rs_imagenes.next()) {
						nombre = rs_imagenes.getString("NOMBRE");
						ruta = ruta_carpeta_parcela + File.separator + nombre;
						if (archie.existeArchivo(ruta)) {
							db_PRCL_NOMBREARCHIVO += nombre + ",";
						}
					}
					rs_imagenes.close();
				}
	
				if (db_PRCL_NOMBREARCHIVO.length() > 0) {
					db_PRCL_NOMBREARCHIVO = db_PRCL_NOMBREARCHIVO.substring(0, db_PRCL_NOMBREARCHIVO.length() - 1);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			request.setAttribute("PRCL_CONSECUTIVO", db_PRCL_CONSECUTIVO);
			request.setAttribute("PRCL_NOMBREARCHIVO", db_PRCL_NOMBREARCHIVO);
			request.setAttribute("solovisor", Auxiliar.nz(request.getParameter("solovisor"), ""));

			String[] a_resultado = resultado.split("-=-");
			retorno = a_resultado[1];

			target = "/imagenes_parcela.jsp?PRCL_CONSECUTIVO=" + db_PRCL_CONSECUTIVO;
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		
		if (dbREDD != null) {
			dbREDD.desconectarse();
		}
	}

} // 953
