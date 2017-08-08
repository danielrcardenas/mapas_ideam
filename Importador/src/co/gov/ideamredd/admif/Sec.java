// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;

/**
 * Maneja la seguridad de usuario
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 */
@SuppressWarnings("serial")
public class Sec extends HttpServlet {
	
	public static String depuracion = "";

	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	boolean siga = false;

	public String ultimoerror = "";
	public String clase = "Sec";
	
	//Auxiliar aux = new Auxiliar();
	
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
		
		String p_encriptar_usuario = "encriptar_usuario";
		this.encriptar_usuario = getServletContext().getInitParameter(p_encriptar_usuario);
		
		String p_llave_encripcion = "llave_encripcion";
		this.llave_encripcion = getServletContext().getInitParameter(p_llave_encripcion);

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
		String usuario = "";
		String idioma = "";
		
		String retorno = "";
		String target = "";
		
		String accion = request.getParameter("accion");
		
		HttpSession session = iniciarSesion(request);
		
		if (session != null) {
			if (sesionVigente(session)) {
				try {
				    synchronized (session) {
				    	if (sesionTieneAtributo(session, "idioma")) {
				    		idioma = Auxiliar.nzObjStr(session.getAttribute("idioma").toString(), "");
				    	}
				    	if (sesionTieneAtributo(session, "usuario")) {
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
			}
			else {
				accion = null;
			}
		}
		else {
			//accion = null;
		}
		
		request.setAttribute("datos_sesion", Auxiliar.mensajeImpersonal("nota", "Usuario: " + Auxiliar.nz(usuario, "") + ", Idioma: " + Auxiliar.nz(idioma, "")));

		
		if (accion == null) {
			target = "/login.jsp";
		}
		else if (accion.equals("menu")) {
			target = "/menu.jsp";
		}
		else if (accion.equals("salir")) {
			destruirSesion(request);
			target = "/login.jsp";
		}
		else
		{
			target = "/error.jsp";
			retorno = "No se encontró la información solicitada para la acción: " + accion;
		}

		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		response.setContentType("text/html; charset=UTF-8");
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}
	
	/**
	 * Inicia la sesión del usuario
	 * 
	 * @param request
	 * @return HttpSession sesión del usuario
	 */
	public HttpSession iniciarSesion(HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (session != null) {
			session.setAttribute("login_fallido", "0");
		}
		
		String usuario_recibido = "";
		String usuario = "";
		String idioma = "";
		String clave = "";
		
		if (!Auxiliar.nz(request.getParameter("idioma"), "").equals("")) {
			idioma = Auxiliar.nz(request.getParameter("idioma"), "");
		}

		if (!Auxiliar.nz(request.getParameter("usuario"), "").equals("")) {
			usuario_recibido = Auxiliar.nz(request.getParameter("usuario"), "");
			
			if (!Auxiliar.nz(request.getParameter("clave"), "").equals("")) {
				clave = Auxiliar.nz(request.getParameter("clave"), "");
			}

			Archivo archie = new Archivo();
			
			String [] a_parametros_bd = archie.aParametrosEncripcion();
			
			this.encriptar_usuario = a_parametros_bd[0];		
			this.llave_encripcion = a_parametros_bd[1];		

			if (encriptar_usuario.equals("1") && usuario_recibido.indexOf("==") >= 0) {
				usuario = Auxiliar.desencriptar(usuario_recibido, llave_encripcion);
			}
			else {
				usuario = usuario_recibido;
			}
			
			if (Auxiliar.tieneAlgo(clave)) {
				usuario = autenticarUsuario(usuario, clave, request);
				if (!Auxiliar.tieneAlgo(usuario)) {
					session.setAttribute("login_fallido", "1");
				}
			}
			else {
				usuario = "";
				session.setAttribute("login_fallido", "1");
			}
		}

		if (Auxiliar.tieneAlgo(idioma)) {
			try {
				synchronized (session) { 
					session.setAttribute("idioma", idioma); 
				}
			}
			catch (Exception e) {
				Auxiliar.mensajeImpersonal("error", "Error de sesión: " + e.toString());
			}
		}

		if (Auxiliar.tieneAlgo(usuario)) {
			try {
				synchronized (session) { 
					session.setAttribute("usuario", usuario);
				}
			}
			catch (Exception e) {
				Auxiliar.mensajeImpersonal("error", "Error de sesión: " + e.toString());
			}
		}
		
		return session;
	}
	
	/**
	 * Funcion rolesUsuario
	 * Retorna un string de roles concatenados del usuario
	 * @param usuario
	 * @return String de roles concatenados del usuario, separados por coma
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String rolesUsuario(String usuario) throws ClassNotFoundException, Exception {
		String r = "";
		
		if (!Auxiliar.tieneAlgo(usuario)) {
			return "";
		}
		this.init();
		
		BD dbREDD = new BD();

	    String USR_CONSECUTIVO = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "NULL");

		String sql = "SELECT DISTINCT RLUS_CONS_ROL FROM RED_USUARIO_ROL ";
		sql += " WHERE RLUS_CONS_USUARIO=" + USR_CONSECUTIVO;
		sql += " ORDER BY RLUS_CONS_ROL DESC ";
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null) {
				while (rset.next())	{
					r += rset.getString("RLUS_CONS_ROL") + ",";
				}
				
				rset.close();
				
				if (Auxiliar.tieneAlgo(r)) {
					if (r.endsWith(",")) {
						r = r.substring(0, r.length()-1);
					}
				}
			}
			else
			{
				ultimoerror = " El conjunto de resultados retornados para la consulta ["+sql+"] es nix.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError;
				Auxiliar.depurar(clase, ultimoerror);
			}
		} catch (SQLException e) {
			ultimoerror = "Excepción de SQL ["+sql+"]: " + e.toString();
			Auxiliar.depurar(clase, ultimoerror);
		} catch (Exception e) {
			ultimoerror = "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql; 
			Auxiliar.depurar(clase, ultimoerror);
		}


		if (dbREDD != null) dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Función tienePermiso
	 * Permite determinar si un usuario tiene algún rol que le permita realizar cierta operación
	 * 
	 * @param id_usuario
	 * @param id_permiso
	 * @return true si tiene permiso, false de lo contrario
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean tienePermiso(String id_usuario, String id_permiso) throws ClassNotFoundException, Exception {
		boolean puede = false;
		
		if (!Auxiliar.tieneAlgo(id_usuario)) {
			return false;
		}
		
		String roles = rolesUsuario(id_usuario);
		
		if (!Auxiliar.tieneAlgo(roles)) {
			return false;
		}

		BD dbREDD = new BD();

		String sql = "SELECT COUNT(*) FROM RED_PERMISO_ROL ";
		sql += " WHERE PRRL_CONS_ROL IN (" + Auxiliar.nz(roles, "") + ")";
		sql += " AND PRRL_CONS_PERMISOS=" + Auxiliar.nz(id_permiso, "");

		String tiene = dbREDD.obtenerDato(sql, "0");

		if (!Auxiliar.nz(tiene, "0").equals("0")) {
			puede = true;
		}
		
		if (dbREDD != null) dbREDD.desconectarse();
		return puede;
	}
	
	/**
	 * Revisa si la sesión tiene un atributo
	 * @param session
	 * @return true si sí lo tiene false de lo contrario
	 */
	public boolean sesionTieneAtributo(HttpSession session, String atributo) {
		if (session == null) return false;
		if (Auxiliar.nz(atributo, "").equals("")) return false;
		
		boolean lo_tiene = false;
		
		try {
			Enumeration<String> atributos_sesion = session.getAttributeNames();
			
			
			while (atributos_sesion.hasMoreElements()) {
				String param = (String) atributos_sesion.nextElement();
				
				if (param.equals(atributo)) {
					lo_tiene = true;
					break;
				}
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return lo_tiene;
	}
	
	/**
	 * Revisa si la sesión esta vigente
	 * @param session
	 * @return true si la sesión está vigente false de lo contrario
	 */
	public boolean sesionVigente(HttpSession session) {
		if (session == null) return false;

		boolean idioma_encontrado = false;
		boolean usuario_encontrado = false;

		try {
			Enumeration<String> atributos_sesion = session.getAttributeNames();
			
			
			while (atributos_sesion.hasMoreElements()) {
				String param = (String) atributos_sesion.nextElement();
				
				if (param.equals("idioma")) idioma_encontrado = true;
				if (param.equals("usuario")) usuario_encontrado = true;
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return usuario_encontrado;
	}
	
	/**
	 * Genera menú para un usuario específico.
	 * @param id_usuario
	 * @param modulo_actual
	 * @return String con el html del menú
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String generarMenu(String id_usuario, String modulo_actual, String idioma) throws ClassNotFoundException, Exception {
		String menu = "";

		if (!Auxiliar.tieneAlgo(id_usuario)) {
			menu = "<nav class='nav-collapse'>";
			menu += "<ul>";
		    menu += "<li><a href='/AdmIF/Sec?accion=salir'>" + Auxiliar.traducir("Menu.Ingresar", idioma, "Ingresar") + "</a></li>";
			menu += "</ul>";
			menu += "</nav>";

			return menu;
		}

		
		// AVERIGUAR ROLES
		
		String roles = rolesUsuario(id_usuario);

		
		// CONSULTAR MODULOS
		
		BD dbREDD = new BD();

		menu = "<nav class='nav-collapse'>";
		menu += "<ul>";
		
		
		// ARMAR MENU

		if (Auxiliar.tieneAlgo(roles)) {
			ResultSet rset = dbREDD.consultarBD("SELECT MDLO_CONSECUTIVO, MDLO_DESCRIPCION, URL FROM RED_MODULO WHERE EN_MENU=1 AND ADMIF=1 AND MDLO_CONSECUTIVO IN (SELECT ROMO_MODULO FROM RED_ROL_MODULO WHERE ROMO_ROL IN ("+roles+"))  ORDER BY ORDEN");

			String consecutivo = "";
			String url = "";
			String modulo = "";
			String clase_activa = "";
			
			if (rset != null) {
				while (rset.next()) {
					consecutivo = Auxiliar.nz(rset.getString("MDLO_CONSECUTIVO"), "");
					url = Auxiliar.nz(rset.getString("URL"), "#");
					modulo = Auxiliar.nz(rset.getString("MDLO_DESCRIPCION"), "");
					
					if (modulo_actual.equals(url)) {
						clase_activa = " class='active' ";
					}
					else {
						clase_activa = "";
					}
					
				    menu += "<li "+clase_activa+"><a href='"+url+"'>" + Auxiliar.traducir("Menu." + consecutivo, idioma, modulo) + "</a></li>";
				}
				rset.close();
			}
		}
		
	    menu += "<li><a href='/AdmIF/Sec?accion=salir'>" + Auxiliar.traducir("Menu.Cerrar_Session", idioma, "Salir") + "</a></li>";
		menu += "</ul>";
		menu += "</nav>";
		
		if (dbREDD != null) dbREDD.desconectarse();
		
		return menu;
	}
	
	/**
	 * Destruye la sesión del usuario
	 * 
	 * @param request
	 * @return true si logró destruir (invalidar) la sesión, false de lo contrario
	 */
	public boolean destruirSesion(HttpServletRequest request) {
		boolean ok = false;
		
		HttpSession session = request.getSession();

		try {
			session.invalidate();
			ok = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * Retorna id de usuario si es auténtico.
	 * @param usuario
	 * @param clave
	 * @return String con el id del usuario
	 */
	public String autenticarUsuario(String usuario, String clave, HttpServletRequest request) {
		String id_usuario = "";
		String id_usuario_bd = "";
		String id_usuario_autenticado_bd = "";
		String id_organizacion = "";
		boolean conexion_ldap_ok = false;
		
		String atributoNombreLDAP = "";
		String nombreLDAP = "";
		String atributoMailLDAP = "";
		String mailLDAP = "";

		String principal_id = "";
		String sql_sincronizar_bd = "";
		String sql_sincronizar_apollo = "";
		boolean ok_sincronizar_bd = false; 
		boolean ok_sincronizar_apollo = false; 

		
		String clave_encriptada = encriptarClave(clave);
		
		boolean LDAPconsultado = false;
		
		String codigoResultadoLDAP = "";
		
		Codec ORACLE_CODEC = new OracleCodec();

		try {
			usuario = Auxiliar.limpiarTexto(usuario);
			usuario = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(usuario, ""));

			//clave = Auxiliar.limpiarTexto(clave);
			clave = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(clave, ""));

			
			BD dbREDD = new BD();
			if (dbREDD != null) {
				
				// AVERIGUAR SI EL USUARIO EXISTE EN LA BASE DE DATOS
				id_usuario_bd = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='" + usuario + "'", "");
				
				if (Auxiliar.tieneAlgo(id_usuario_bd)) {
					// SI EXISTE
					// AVERIGUAR SI ES UN USUARIO DE IDEAM (LDAP)
					id_organizacion = dbREDD.obtenerDato("SELECT USR_CONS_ORGANIZACION FROM RED_USUARIO WHERE USR_CONSECUTIVO=" + id_usuario_bd, "");
				}
				
				// INTENTAR AUTENTICAR CONTRA LDAP
				
				atributoNombreLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='atributoNombreLDAP'", "displayName");
				atributoMailLDAP = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='atributoMailLDAP'", "displayName");
				
				Auxiliar.mensajeImpersonal("nota", "Revisando si existe en LDAP");
				LDAP ldap = new LDAP();
				try {
					String [] autenticadoLDAP = ldap.autenticar(usuario, clave);
					codigoResultadoLDAP = autenticadoLDAP[0];
					
					if (Auxiliar.tieneAlgo(id_usuario_bd)) {
						if (codigoResultadoLDAP.equals("1")) {
							id_usuario = id_usuario_bd;
							
							// SI PUDO SER AUTENTICADO POR LDAP
							nombreLDAP = ldap.dato(usuario, clave, atributoNombreLDAP);
							mailLDAP = ldap.dato(usuario, clave, atributoMailLDAP);
							
							if (!Auxiliar.tieneAlgo(mailLDAP)) {
								mailLDAP = usuario + "@ideam.gov.co";
							}
		
							Auxiliar.mensajeImpersonal("confirmacion", "Usuario existente "+usuario+" ("+nombreLDAP+") autenticado ante LDAP.");

							// SINCRONIZAR ACTUALIZANDO EN LA BD
							sql_sincronizar_bd = "UPDATE RED_USUARIO SET USR_CLAVE='"+clave_encriptada+"', USR_NOMBRE='"+nombreLDAP+"', USR_CORREOELECTRONIC='"+mailLDAP+"' WHERE USR_CONSECUTIVO="+id_usuario_bd;
							ok_sincronizar_bd = dbREDD.ejecutarSQL(sql_sincronizar_bd);
							if (ok_sincronizar_bd) {
								Auxiliar.mensajeImpersonal("confirmacion", "Usuario "+usuario+" actualizado en SMBC...");
								id_usuario = id_usuario_bd;
							}
							else {
								Auxiliar.mensajeImpersonal("error", "No se pudo actualizar el usuario "+usuario+" en SMBC...");
							}

							
							// SINCRONIZACIÓN CON ERDAS APOLLO
							// VERIFICAR SI EL USUARIO EXISTE EN EAPOLLO
							principal_id = dbREDD.obtenerDato("SELECT PRINCIPAL_ID FROM PRINCIPALS WHERE PRINCIPAL_ID='"+usuario+"'", "");
							if (Auxiliar.tieneAlgo(principal_id)) {
								// ACTUALIZAR EL USUARIO EN EAPOLLO
								sql_sincronizar_apollo = "UPDATE PRINCIPALS SET PASSWORD='"+clave+"', EMAIL='"+mailLDAP+"' WHERE PRINCIPAL_ID='"+usuario+"'";
							}
							else {
								// CREAR EL USUARIO EN EAPOLLO
								//sql_sincronizar_apollo = "INSERT INTO PRINCIPALS (PRINCIPAL_TYPE, PRINCIPAL_ID, NAME, PASSWORD, EMAIL, COMPANY_NAME) VALUES (2, '"+usuario+"', '"+nombreLDAP+"', '"+clave+"', '"+mailLDAP+"', 'IDEAM')";
								sql_sincronizar_apollo = "INSERT INTO PRINCIPALS (PRINCIPAL_TYPE, PRINCIPAL_ID, NAME, PASSWORD, EMAIL, COMPANY_NAME) VALUES (2, '"+usuario+"', '"+usuario+"', '"+clave+"', '"+mailLDAP+"', 'IDEAM')";
							}
							ok_sincronizar_apollo = dbREDD.ejecutarSQL(sql_sincronizar_apollo);
		
							if (ok_sincronizar_apollo) {
								Auxiliar.mensajeImpersonal("confirmacion", "Usuario "+usuario+" sincronizado en Apollo...");
							}
							else {
								Auxiliar.mensajeImpersonal("error", "No se pudo sincronizar el usuario "+usuario+" en Apollo...");
							}
						}
						else if (codigoResultadoLDAP.equals("2")) {
							Auxiliar.mensajeImpersonal("advertencia", "Usuario no autenticado.");
							id_usuario = "";
						}
						else {
							if (autenticadoLDAP.length > 0) {
								Auxiliar.mensajeImpersonal("error", autenticadoLDAP[1] + "(" + codigoResultadoLDAP + ")");
								
								LDAPconsultado = true;
								if (Integer.parseInt(codigoResultadoLDAP) < 0) {
									LDAPconsultado = false;
								}
		
								if (!LDAPconsultado || codigoResultadoLDAP.equals("3")) {
									// AUTENTICAR CONTRA BD
									id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='" + usuario + "' AND USR_CLAVE='" + clave_encriptada + "'", "");
								}
							}
						}						
					}
					else {
						// SI EL USUARIO AÚN NO EXISTE EN LA BD
						Auxiliar.mensajeImpersonal("nota", "El usuario "+usuario+" aun no existe en la BD");
	
						if (codigoResultadoLDAP.equals("2")) {
							Auxiliar.mensajeImpersonal("advertencia", "Usuario o clave inválida.");
							id_usuario = "";
						}
						else {
							if (codigoResultadoLDAP.equals("1")) {
								// SI PUDO SER AUTENTICADO POR LDAP
								nombreLDAP = ldap.dato(usuario, clave, atributoNombreLDAP);
								mailLDAP = ldap.dato(usuario, clave, atributoMailLDAP);
								
								if (!Auxiliar.tieneAlgo(mailLDAP)) {
									mailLDAP = usuario + "@ideam.gov.co";
								}
			
								Auxiliar.mensajeImpersonal("confirmacion", "Usuario existente "+usuario+" ("+nombreLDAP+") autenticado ante LDAP.");

								// SINCRONIZAR CREANDO USUARIO EN LA BD
								String sql_crear_usuario = "INSERT INTO RED_USUARIO (USR_NOMBRE, USR_LOGIN, USR_ID, USR_CORREOELECTRONIC, USR_ACTIVO, USR_CONS_PAIS, USR_CONS_TIPOPERSONA, USR_ORGANIZACION, USR_CONS_ORGANIZACION, USR_CLAVE) VALUES ('"+nombreLDAP+"','"+usuario+"','"+usuario+"','"+mailLDAP+"',1,57,1,'IDEAM',1,'"+clave_encriptada+"')";
								boolean ok_crear_usuario = dbREDD.ejecutarSQL(sql_crear_usuario);
								if (ok_crear_usuario) {
									id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='" + usuario + "' AND USR_CLAVE='" + clave_encriptada + "'", "");
									Auxiliar.mensajeImpersonal("confirmacion", "Usuario "+usuario+" creado en SMBC...");
								}
								else {
									Auxiliar.mensajeImpersonal("error", "No se pudo crear el usuario "+usuario+" en SMBC... ");
								}
							}
						}
					}
				}
				catch (Exception e) {
					Auxiliar.mensajeImpersonal("error", "No fue posible conectarse al servidor LDAP: " + e.toString());					
				}				
			}
			
			if (Auxiliar.tieneAlgo(id_usuario)) {
				registrarTransaccion(request, 69, "", "", true);
			}
			else {
				registrarTransaccion(request, 69, "", "", false);
			}
			
			if (dbREDD != null) dbREDD.desconectarse();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			String error = Auxiliar.mensajeImpersonal("error", e1.toString());
			e1.printStackTrace();
		}
		
		return id_usuario;
	}
	
	/**
	 * Encripta una clave y la retorna encriptada
	 * @param clave no encriptada
	 * @return String con la clave encriptada
	 */
	public String encriptarClave(String clave) {
		String clave_encriptada = "";
		
		clave_encriptada = Encript.getEncodedPassword(clave);
		
		return clave_encriptada;
	}
	
	
	/**
	 * Registra transacción de usuario
	 * 
	 * @param request
	 * @param TRNS_CONS_PERMISO
	 * @param TRNS_REGISTRO
	 * @param TRNS_DESCRIPCION
	 * @param EXITOSA
	 * @return true si logró registrar la transacción, false de lo contrario
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public boolean registrarTransaccion(HttpServletRequest request, int TRNS_CONS_PERMISO, String TRNS_REGISTRO, String TRNS_DESCRIPCION, boolean EXITOSA) 
			throws ClassNotFoundException, Exception {
		String sql = "";
		boolean ok_registrar_transaccion = false;

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
		String usuario = "";
		HttpSession session = request.getSession();
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Usuario aún sin sesión activa."); }

		String TRNS_CONS_USUARIO = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "null");
		
		String TRNS_IP = Auxiliar.nz(request.getRemoteAddr(), "");
		
		int TRNS_EXITOSA = 1;
		
		if (EXITOSA) {
			TRNS_EXITOSA = 1;
		}
		else {
			TRNS_EXITOSA = 0;
		}
		
		try
		{
			TRNS_DESCRIPCION = TRNS_DESCRIPCION.replace("'", "`");
			
			if (TRNS_REGISTRO.equals("")) {
				TRNS_REGISTRO = "null";
			}
			
			sql = "INSERT INTO RED_TRANSACCIONES (TRNS_IP, TRNS_CONS_USUARIO, TRNS_CONS_PERMISO, TRNS_REGISTRO, TRNS_DESCRIPCION, TRNS_EXITOSA) VALUES ('"+TRNS_IP+"', "+TRNS_CONS_USUARIO+", "+TRNS_CONS_PERMISO+", "+TRNS_REGISTRO+", '"+TRNS_DESCRIPCION+"', "+TRNS_EXITOSA+")";
			
			ok_registrar_transaccion = dbREDD.ejecutarSQL(sql);
		}
		catch (SQLException e)
		{
			ok_registrar_transaccion = false;
		}
		catch (Exception e)
		{
			ok_registrar_transaccion = false;
		}

		return ok_registrar_transaccion;
	}
	
	/**
	 * Dice si el usuario especificado aceptó la licencia con cierto nombre
	 * 
	 * @param usuario
	 * @param nombreLicencia
	 * @return true si sí, false si no
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public boolean usuarioAceptoLicencia(String usuario, String nombreLicencia) 
			throws ClassNotFoundException, Exception {

		boolean usuarioAceptoLicencia = false;
		
		if (!Auxiliar.esEnteroMayorOIgualACero(usuario)) {
			return false;
		}

		try {
			BD dbREDD = new BD();
			String USR_CONSECUTIVO = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "NULL");
			String sql = "SELECT COUNT(*) AS CONTEO FROM RED_LICENCIAS L INNER JOIN RED_USUARIO_LICENCIA UL ON L.LCNC_CONSECUTIVO=UL.USLC_CONS_LICENCIA WHERE UL.USLC_CONS_USUARIO = " + USR_CONSECUTIVO + " AND L.LCNC_NOMBRE = '" + nombreLicencia + "'";

			String str_usuarioAceptoLicencia = dbREDD.obtenerDato(sql, "");
			if (!Auxiliar.nz(str_usuarioAceptoLicencia, "0").equals("0")) {
				usuarioAceptoLicencia = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return usuarioAceptoLicencia;
	}
	


}
