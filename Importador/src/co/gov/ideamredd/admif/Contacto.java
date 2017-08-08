// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;


/** 
 * Permite administrar los contactos de una parcela
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com 
 *
 */
@SuppressWarnings("serial")
public class Contacto extends HttpServlet {

	public static String yo = "Contacto.";
	public static String charset = "ISO-8859-1";
	public static String css = "css/estilos.css";
	
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
	 * Método constructor
	 */
	public Contacto() {
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
	    
	    String f_CNPR_CONSECUTIVO = "";
		String f_CNPR_CONS_PARCELA = "";
		String f_CNPR_CONS_CONTACTO = "";
		String f_CNPR_CONS_CLASECONTACTO = "";

		
		String retorno = "";
		String target = "";
		
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
			}
			else {
				accion = null;
			}
		}
		else {
			accion = null;
		}
		
		request.setAttribute("datos_sesion", Auxiliar.mensajeImpersonal("datos_sesion", "Usuario: " + Auxiliar.nz(usuario, "--") + ", Idioma: " + Auxiliar.nz(idioma, "")));

		if (accion == null) {
			target = "/login.jsp";
			retorno = Auxiliar.mensajeImpersonal("advertencia", Auxiliar.traducir("SESION_VENCIDA", "es", "Credenciales erradas o Su sesión ha vencido"));
			response.setContentType("text/html; charset=UTF-8");
			try {
				sec.registrarTransaccion(request, 194, "", "Contacto", false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if (accion.equals("ajax_encontrar")) {
				target = "/ajax_resultados.jsp";
				response.setContentType("text/html; charset=UTF-8");
				try {
					String resultado = ajax_encontrar(Auxiliar.nz(request.getParameter("USR_CONS_TIPOIDENT"), ""), Auxiliar.nz(request.getParameter("USR_ID"), ""), session);
					retorno = resultado;
				} catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarContactos(): " + e.toString(), usuario, metodo);
					e.printStackTrace();
				}
			}
			else {
				response.setContentType("text/html; charset=UTF-8");
				target = "/contactos_parcela.jsp?CNPR_CONS_PARCELA="+Auxiliar.nz(request.getParameter("CNPR_CONS_PARCELA"), "");
	
				f_CNPR_CONSECUTIVO = Auxiliar.nz(request.getParameter("CNPR_CONSECUTIVO"), "");
				f_CNPR_CONS_PARCELA = Auxiliar.nz(request.getParameter("CNPR_CONS_PARCELA"), "");
				f_CNPR_CONS_CONTACTO = Auxiliar.nz(request.getParameter("CNPR_CONS_CONTACTO"), "");
				f_CNPR_CONS_CLASECONTACTO = Auxiliar.nz(request.getParameter("CNPR_CONS_CLASECONTACTO"), "");
				
				if (Auxiliar.tieneAlgo(f_CNPR_CONS_PARCELA)) {
					if (accion.equals("guardar")) {
						response.setContentType("text/html; charset=UTF-8");
						try {
							if (
									sec.tienePermiso(usuario,  "233") 
									||	sec.tienePermiso(usuario,  "234")
									||	sec.tienePermiso(usuario,  "235")
									||	sec.tienePermiso(usuario,  "236")
									) {
								String resultado = guardar(
										f_CNPR_CONSECUTIVO, 
										f_CNPR_CONS_PARCELA, 
										f_CNPR_CONS_CONTACTO, 
										f_CNPR_CONS_CLASECONTACTO, 
										request
										);
								
								String [] a_resultado = resultado.split("-=-");
								retorno = a_resultado[1];
							}
							else {
								retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							}
						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a contactos: " + e.toString(), usuario, metodo);
							e.printStackTrace();
						}
					}
					else if (accion.equals("eliminar")) {
						response.setContentType("text/html; charset=UTF-8");
						try {
							if (
									sec.tienePermiso(usuario,  "36") 
									||	sec.tienePermiso(usuario,  "37")
									) {
								String resultado = eliminar(f_CNPR_CONSECUTIVO, session);
								String [] a_resultado = resultado.split("-=-");
								retorno = a_resultado[1];
							}
							else {
								retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							}
						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a contactos: " + e.toString(), usuario, metodo);
							e.printStackTrace();
						}
					}
					
					try {
						if (
								sec.tienePermiso(usuario,  "245") 
								||	sec.tienePermiso(usuario,  "246")
								) {
							retorno = "";
							request = establecerAtributos(request, session, "");
							retorno = Auxiliar.nz(request.getAttribute("retorno").toString(), "");
							request.setAttribute("CNPR_CONS_PARCELA", f_CNPR_CONS_PARCELA);

						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a contactos: " + e.toString(), usuario, metodo);
						e.printStackTrace();
					}

				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Identificador de la parcela no especificado", usuario, metodo);
				}
			}
		}

		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}

	
	/**
	 * Inicializa los atributos del request para llenar el formulario con los valores por defecto
	 * 
	 * @param request
	 * @return request con atributos inicializados
	 * @throws Exception 
	 */
	private HttpServletRequest establecerAtributos(HttpServletRequest request, HttpSession session, String nuevo_consecutivo) 
			throws Exception {
		String metodo = yo + "establecerAtributos";
		
		request.setAttribute("retorno","");

		BD dbREDD = new BD();
		Sec sec = new Sec();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String CNPR_CONSECUTIVO = "";
	    
	    CNPR_CONSECUTIVO = Auxiliar.nz(request.getParameter("CNPR_CONSECUTIVO"), "");
	    
	    if (Auxiliar.tieneAlgo(nuevo_consecutivo)) CNPR_CONSECUTIVO = nuevo_consecutivo;

		String db_CNPR_CONS_PARCELA = Auxiliar.nz(request.getParameter("CNPR_CONS_PARCELA"), "");
		String db_CNPR_CONS_CONTACTO = Auxiliar.nz(request.getParameter("CNPR_CONS_CONTACTO"), "");
		String db_NOMBRE_CONTACTO = "";
		String db_CNPR_CONS_CLASECONTACTO = Auxiliar.nz(request.getParameter("CNPR_CONS_CLASECONTACTO"), "");

		String id_usuario = "";
		String id_creador = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		if (Auxiliar.tieneAlgo(db_CNPR_CONS_PARCELA)) {
			id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+db_CNPR_CONS_PARCELA+"", "");
		}
		boolean privilegio = false;
		
		if (id_usuario.equals(id_creador)) {
			if (sec.tienePermiso(id_usuario, "247") || sec.tienePermiso(id_usuario, "248")) {
				privilegio = true;
			}
		}
		else {
			if (sec.tienePermiso(id_usuario, "248")) {
				privilegio = true;
			}
		}
		
		if (!privilegio) { 
			request.setAttribute("retorno",Auxiliar.mensaje("advertencia", "No tiene privilegios para detallar contactos de parcela", id_usuario, metodo));
			dbREDD.desconectarse();
			return request;
		}
		
		if (Auxiliar.tieneAlgo(CNPR_CONSECUTIVO)) {
			try {
			    ResultSet rset = cargarRegistro(CNPR_CONSECUTIVO, session);
			    
			    if (rset != null) {
					if (rset.next()) {
						
						db_CNPR_CONS_PARCELA = rset.getString("CNPR_CONS_PARCELA");
						db_CNPR_CONS_CONTACTO = rset.getString("CNPR_CONS_CONTACTO");
						
						if (Auxiliar.tieneAlgo(db_CNPR_CONS_CONTACTO)) {
							db_NOMBRE_CONTACTO = dbREDD.obtenerDato("SELECT USR_NOMBRE FROM RED_USUARIO WHERE USR_CONSECUTIVO=" + db_CNPR_CONS_CONTACTO, "");
						}
						db_CNPR_CONS_CLASECONTACTO = rset.getString("CNPR_CONS_CLASECONTACTO");
						
						rset.close();				
					}
			    }
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("CNPR_CONSECUTIVO", CNPR_CONSECUTIVO);
		request.setAttribute("CNPR_CONS_CONTACTO", db_CNPR_CONS_CONTACTO);
		request.setAttribute("NOMBRE_CONTACTO", db_NOMBRE_CONTACTO);
		request.setAttribute("CNPR_CONS_CLASECONTACTO", db_CNPR_CONS_CLASECONTACTO);
		
		request.setAttribute("idioma",idioma);

		request.setAttribute("opciones_tipodoc", Auxiliar.cargarOpciones("SELECT TPID_CONSECUTIVO, TPID_NOMBRE FROM RED_TIPOIDENTIFICACION ORDER BY TPID_NOMBRE", "TPID_CONSECUTIVO", "TPID_NOMBRE", "", "", false, true, false));
		request.setAttribute("opciones_clase_contacto", Auxiliar.cargarOpciones("SELECT CLCN_CONSECUTIVO, CLCN_DESCRIPCION FROM RED_CLASECONTACTO ORDER BY CLCN_DESCRIPCION", "CLCN_CONSECUTIVO", "CLCN_DESCRIPCION", db_CNPR_CONS_CLASECONTACTO, "", false, true, false));
		
		String[] a_registros = listarRegistros(db_CNPR_CONS_PARCELA, session);
		
		request.setAttribute("tabla_registros", a_registros[0]);
		request.setAttribute("n_registros", a_registros[1]);

		dbREDD.desconectarse();
		return request;
	}

	/**
	 * Metodo para retornar string de opciones valor@@texto para un select de contactos.
	 * 
	 * @param texto
	 * @return String r con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String ajax_encontrar(
			String USR_CONS_TIPOIDENT,
			String USR_ID, 
			HttpSession session
			)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarStrIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String r = "";
		
		if (!Auxiliar.tieneAlgo(USR_CONS_TIPOIDENT)) {
			t = Auxiliar.traducir(yo+"Por_favor_especifique_tipo_documento", idioma, "Por favor especifique el tipo de documento" + "..");
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}
		
		if (!Auxiliar.tieneAlgo(USR_ID)) {
			t = Auxiliar.traducir(yo+"Por_favor_especifique_numero_documento", idioma, "Por favor especifique el número de documento" + "..");
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}
		
		String sql = "SELECT DISTINCT";
		sql += " USR_CONSECUTIVO,";
		sql += " USR_NOMBRE";
		sql += " FROM RED_USUARIO ";
		sql += " WHERE USR_CONS_TIPOIDENT="+USR_CONS_TIPOIDENT+" AND USR_ID='"+USR_ID+"' ";
		sql += " ORDER BY USR_NOMBRE ";
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String opciones="";
				String db_USR_CONSECUTIVO = "";
				String db_USR_NOMBRE = "";
				
				while (rset.next())
				{
					db_USR_CONSECUTIVO = rset.getString("USR_CONSECUTIVO");
					db_USR_NOMBRE = rset.getString("USR_NOMBRE");
					
					opciones += db_USR_CONSECUTIVO+"@@"+db_USR_NOMBRE+"\n";
				}
				
				rset.close();
				
				r=opciones;
			}
			else
			{
				r += "Error SQL:"+dbREDD.ultimoError+"@@"+sql;
				System.out.println("Error SQL al consultar usuarios:"+dbREDD.ultimoError+"@@"+sql);
			}
		} catch (SQLException e) {
			r += "Error SQL:"+dbREDD.ultimoError+"@@"+sql;
			System.out.println("Error SQL al consultar usuarios:"+dbREDD.ultimoError+"@@"+sql);
		} catch (Exception e) {
			r += "Excepción:@@"+e.toString();
			System.out.println("Excepción: " + e.toString());
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Carga registro de contacto
	 * 
	 * @param CNPR_CONSECUTIVO
	 * @param session
	 * @return contactos
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private ResultSet cargarRegistro(String CNPR_CONSECUTIVO, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "generarStrIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		ResultSet rset = null;
	    
		if (!Auxiliar.tieneAlgo(CNPR_CONSECUTIVO)) {
			dbREDD.desconectarse();
			return null;
		}
		
		String sql = "SELECT * FROM RED_CONTACTO_PARCELA WHERE CNPR_CONSECUTIVO=" + CNPR_CONSECUTIVO;
		
		try {
			rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				//dbREDD.desconectarse();
				return rset;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return rset;
	}


	/**
	 * Genera una tabla con los contactos de una parcela
	 * 
	 * @param CNPR_CONS_PARCELA
	 * @param session
	 * @return html de tabla de contactos
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] listarRegistros(String CNPR_CONS_PARCELA, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "generarStrIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String tabla = Auxiliar.traducir("General.No_se_encontraron_registros", idioma, "No se encontraron registros");

		String [] a_tabla = {tabla, "0"};

		
		if (!Auxiliar.tieneAlgo(CNPR_CONS_PARCELA)) {
			dbREDD.desconectarse();
			return a_tabla;
		}
		
		String sql = "SELECT ";
		sql += "CP.CNPR_CONSECUTIVO AS CNPR_CONSECUTIVO,";
		sql += "CP.CNPR_CONS_PARCELA AS CNPR_CONS_PARCELA,";
		sql += "CP.CNPR_CONS_CONTACTO AS CNPR_CONS_CONTACTO,";
		sql += "U.USR_NOMBRE || '(' || TI.TPID_NOMBRE || ':' || U.USR_ID || ')' AS USUARIO,";
		sql += "CC.CLCN_DESCRIPCION AS CLASECONTACTO";
		sql += " FROM RED_CONTACTO_PARCELA CP ";
		sql += " INNER JOIN RED_CLASECONTACTO CC ON CP.CNPR_CONS_CLASECONTACTO=CC.CLCN_CONSECUTIVO ";
		sql += " INNER JOIN RED_USUARIO U ON CP.CNPR_CONS_CONTACTO=U.USR_CONSECUTIVO ";
		sql += " INNER JOIN RED_TIPOIDENTIFICACION TI ON U.USR_CONS_TIPOIDENT=TI.TPID_CONSECUTIVO ";
		sql += " WHERE CP.CNPR_CONS_PARCELA = ("+CNPR_CONS_PARCELA+") ";
		sql += " ORDER BY CLASECONTACTO, USUARIO ";
		
		int n = 0;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String db_CNPR_CONSECUTIVO = "";
				String db_CNPR_CONS_PARCELA = "";
				String db_CNPR_CONS_CONTACTO = "";
				String db_USUARIO = "";
				String db_CLASECONTACTO = "";
				
				String opciones = "";
				
				tabla = "<div id='contenedor-resultados' class='tabla_resultados'>";
				
				String t_CNPR_CONS_CLASECONTACTO = "";
				String t_CNPR_CONS_CONTACTO = "";
				String t_Opciones = "";

				t_CNPR_CONS_CLASECONTACTO = Auxiliar.traducir("CNPR_CONS_CLASECONTACTO", idioma, "Clase de contacto" + "..");
				t_CNPR_CONS_CONTACTO = Auxiliar.traducir("CNPR_CONS_CONTACTO", idioma, "Contacto" + "..");
				t_Opciones = Auxiliar.traducir("General.Opciones", idioma, "Opciones" + "..");
				
				while (rset.next())
				{
					opciones = "";
					
					db_CNPR_CONSECUTIVO = rset.getString("CNPR_CONSECUTIVO");
					db_CNPR_CONS_PARCELA = rset.getString("CNPR_CONS_PARCELA");
					db_CNPR_CONS_CONTACTO = rset.getString("CNPR_CONS_CONTACTO");
					db_USUARIO = rset.getString("USUARIO");
					db_CLASECONTACTO = rset.getString("CLASECONTACTO");
					
					t = Auxiliar.traducir("General.Editar", idioma, "Editar" + "..");
					opciones += "<div class='opcionmenu'><a class=boton href='Contacto?accion=detalle&USUARIO="+db_USUARIO+"&CNPR_CONS_PARCELA="+db_CNPR_CONS_PARCELA+"&CNPR_CONSECUTIVO="+db_CNPR_CONSECUTIVO+"' >" + t + "</a></div>";
					
					t = Auxiliar.traducir("General.Eliminar", idioma, "Eliminar" + "..");
					opciones += "<div class='opcionmenu'><a class=boton href='Contacto?accion=eliminar&CNPR_CONS_PARCELA="+db_CNPR_CONS_PARCELA+"&CNPR_CONSECUTIVO="+db_CNPR_CONSECUTIVO+"' >" + t + "</a></div>";
					
					String info_contacto = "<div>";
					
					info_contacto += "<div>" + db_USUARIO + "</div>";
					info_contacto += dbREDD.obtenerDato("SELECT '<br/>E-Mail:' || USR_CORREOELECTRONIC AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+db_CNPR_CONS_CONTACTO, "");
					info_contacto += dbREDD.obtenerDato("SELECT '<br/>Tel.:' || USR_TELEFONO AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+db_CNPR_CONS_CONTACTO, "");
					info_contacto += dbREDD.obtenerDato("SELECT '<br/>Cel.:' || USR_MOVIL AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+db_CNPR_CONS_CONTACTO, "");
					info_contacto += dbREDD.obtenerDato("SELECT '<br/>Org.:' || USR_ORGANIZACION AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+db_CNPR_CONS_CONTACTO, "");
					info_contacto += dbREDD.obtenerDato("SELECT '<br/>Titl.:' || USR_CARGO AS INFO FROM RED_USUARIO WHERE USR_CONSECUTIVO="+db_CNPR_CONS_CONTACTO, "");
					
					info_contacto += "</div>";
					
					tabla += "<span>";
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_CNPR_CONS_CLASECONTACTO+":"+db_CLASECONTACTO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_CNPR_CONS_CONTACTO+":"+info_contacto+"</div>";
					tabla += "<div class='dato_resultado'>"+opciones+"</div>";
					tabla += "</div>";
					tabla += "</span>";
					
					n++;
				}
				
				tabla += "</div>";

				rset.close();
				
				dbREDD.desconectarse();
				
				a_tabla[0] = tabla;
				a_tabla[1] = String.valueOf(n);
				

				return a_tabla;
			}
			else {
				dbREDD.desconectarse();
				//return Auxiliar.mensaje("ERROR", "No se pudieron consultar los contactos [" + sql + "]", usuario, metodo);
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		}
		
		dbREDD.desconectarse();
		return a_tabla;
	}


	/**
	 * Guarda el contacto de una parcela
	 * 
	 * @param CNPR_CONSECUTIVO
	 * @param CNPR_CONS_PARCELA
	 * @param CNPR_CONS_CONTACTO
	 * @param CNPR_CONS_CLASECONTACTO
	 * @param request
	 * @return String resultado de la operación
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private String guardar(
			String CNPR_CONSECUTIVO,
			String CNPR_CONS_PARCELA, 
			String CNPR_CONS_CONTACTO, 
			String CNPR_CONS_CLASECONTACTO, 
			HttpServletRequest request 
			) throws ClassNotFoundException, Exception {
		String metodo = yo + "generarStrIndividuos";

		Sec sec = new Sec();

		//Auxiliar aux = new Auxiliar();
	    String t = "";
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		Archivo archie = new Archivo();

		String id_creador = "";

		BD dbREDD = new BD();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+CNPR_CONS_PARCELA+"", "");

		boolean ok_operacion = false;
		boolean update = false;
		boolean privilegio = false;
		

		String r = "";
		String sql = "";
		
		if (!Auxiliar.tieneAlgo(CNPR_CONSECUTIVO)) {
			sql = "INSERT INTO RED_CONTACTO_PARCELA ( ";
			sql += " CNPR_CONS_PARCELA,";
			sql += " CNPR_CONS_CONTACTO,";
			sql += " CNPR_CONS_CLASECONTACTO";
			sql += " ) VALUES ( ";
			sql += " "+CNPR_CONS_PARCELA+",";
			sql += " "+CNPR_CONS_CONTACTO+",";
			sql += " "+CNPR_CONS_CLASECONTACTO+"";
			sql += " )";
		}
		else {
			sql = "UPDATE RED_CONTACTO_PARCELA SET ";
			sql += " CNPR_CONS_CLASECONTACTO="+CNPR_CONS_CLASECONTACTO+"";
			sql += " WHERE CNPR_CONSECUTIVO="+CNPR_CONSECUTIVO;
			
			update = true;
		}
		
		try {
			if (update) {
				if (id_usuario.equals(id_creador)) {
					if (sec.tienePermiso(id_usuario, "235") || sec.tienePermiso(id_usuario, "236")) {
						privilegio = true;
					}
				}
				else {
					if (sec.tienePermiso(id_usuario, "236")) {
						privilegio = true;
					}
				}
			}
			else {
				if (id_usuario.equals(id_creador)) {
					if (sec.tienePermiso(id_usuario, "233") || sec.tienePermiso(id_usuario, "234")) {
						privilegio = true;
					}
				}
				else {
					if (sec.tienePermiso(id_usuario, "234")) {
						privilegio = true;
					}
				}
			}
			
			if (!privilegio) {
				dbREDD.desconectarse();
	        	return "0-=-" + Auxiliar.mensaje("advertencia", "No tiene privilegio para realizar esta operación. Por favor, contacte al administrador.", usuario, metodo);
			}

			ok_operacion = dbREDD.ejecutarSQL(sql);
			
			if (ok_operacion) {
				if (update) {
					if (id_usuario.equals(id_creador)) {
						sec.registrarTransaccion(request, 235, CNPR_CONSECUTIVO, sql, ok_operacion);
					}
					else {
						sec.registrarTransaccion(request, 236, CNPR_CONSECUTIVO, sql, ok_operacion);
					}
				}
				else {
					if (id_usuario.equals(id_creador)) {
						sec.registrarTransaccion(request, 233, "", sql, ok_operacion);
					}
					else {
						sec.registrarTransaccion(request, 234, "", sql, ok_operacion);
					}
				}
				r = "1-=-" + Auxiliar.mensaje("confirmacion", "Contacto registrado.", usuario, metodo);
			}
			else {
				r = "0-=-" + Auxiliar.mensaje("error", "El contacto no se pudo registrar:" + dbREDD.ultimoError, usuario, metodo);				
			}
		} catch (SQLException e) {
			String aviso_error = dbREDD.ultimoError;
			if (dbREDD.ultimoError.indexOf("ORA-00001") > -1) {
				aviso_error = "No se pudo guardar el registro puesto que ya existe uno con los mismos datos.";
			}
			else {
				aviso_error = "Surgió un error al guardar el registro:" + dbREDD.ultimoError;
			}
			r = "0-=-" + r + Auxiliar.mensaje("error", aviso_error, usuario, metodo);
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return r;
	}


	/**
	 * Elimina un contacto de una parcela.
	 * 
	 * @param CNPR_CONSECUTIVO
	 * @param session
	 * @return String con el resultado de la operación
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private String eliminar(String CNPR_CONSECUTIVO, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "generarStrIndividuos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String r = "0-=-No se pudo eliminar el contacto";
		String sql = "";
		
		sql = "DELETE FROM RED_CONTACTO_PARCELA WHERE CNPR_CONSECUTIVO="+CNPR_CONSECUTIVO;
		try {
			if (dbREDD.ejecutarSQL(sql)) {
				r = "1-=-" + Auxiliar.mensaje("confirmacion", "Contacto eliminado.", usuario, metodo);
			}
			else {
				r = "0-=-" + Auxiliar.mensaje("error", "El contacto no se pudo eliminar.", usuario, metodo);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
} //953
