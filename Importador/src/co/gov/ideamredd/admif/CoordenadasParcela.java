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


/** 
 * Permite administrar las coordenadas de una parcela
 * No se usa, posiblemente sirva en un futuro.
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com
 *
 */
@SuppressWarnings("serial")
public class CoordenadasParcela extends HttpServlet {

	
	public static String yo = "Tallo.";
	public static String charset = "ISO-8859-1";
	public static String css = "css/estilos.css";
	boolean siga = false;

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
	public CoordenadasParcela() {
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
		
		String f_PRCR_ID = "";
		String f_PRCR_PRCL_CONSECUTIVO = "";
		String f_PRCR_SECUENCIA = "";
		String f_PRCR_LATITUD = "";
		String f_PRCR_LONGITUD = "";

		String retorno = "";
		String target = "";
		
	    // Instanciar auxiliar y archivo
	    //Auxiliar aux = new Auxiliar();
		Sec sec = new Sec();

		request.setCharacterEncoding("UTF-8");
		
		// Obtener parámetro de acción para saber lo que quiere el usuario
		String accion = request.getParameter("accion");

		HttpSession session = request.getSession();

		if (session != null) {
			if (sec.sesionVigente(session)) {
				try {
				    synchronized (session) {
				    	if (sec.sesionTieneAtributo(session, "idioma")) {
				    		idioma = Auxiliar.nzObjStr(session.getAttribute("idioma").toString(), "");
				    	}
				    	if (sec.sesionTieneAtributo(session, "usuario")) {
				    		usuario = Auxiliar.nzObjStr(session.getAttribute("usuario").toString(), "");
				    	}
				    }
				}
				catch (Exception e) {
					Auxiliar.mensajeImpersonal("error", "Error de sesión: " + e.toString());
				}
			}
		}
		else {
			accion = null;
		}

		if (!Auxiliar.nz(request.getParameter("idioma"), "").equals("")) {
			idioma = Auxiliar.nz(request.getParameter("idioma"), "");
		}

		if (!Auxiliar.nz(request.getParameter("usuario"), "").equals("")) {
			usuario_recibido = Auxiliar.nz(request.getParameter("usuario"), "");
			
			if (!Auxiliar.nz(request.getParameter("clave"), "").equals("")) {
				clave = Auxiliar.nz(request.getParameter("clave"), "");
			}
	
			if (Auxiliar.tieneAlgo(clave)) {
				usuario = sec.autenticarUsuario(usuario_recibido, clave, request);
		}
			else {
				if (encriptar_usuario.equals("1")) {
					usuario = Auxiliar.desencriptar(usuario_recibido, llave_encripcion);
				}
				else {
					usuario = usuario_recibido;
				}
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
		request.setAttribute("idioma",idioma);

		if (!Auxiliar.tieneAlgo(usuario)) {
			accion = null;
		}
		else {
			try {
				synchronized (session) { 
					session.setAttribute("usuario", usuario);
					accion = "menu";
				}
			}
			catch (Exception e) {
				Auxiliar.mensajeImpersonal("error", "Error de sesión: " + e.toString());
			}
		}
		
		request.setAttribute("datos_sesion", Auxiliar.mensajeImpersonal("datos_sesion", "Usuario: " + Auxiliar.nz(usuario, "--") + ", Idioma: " + Auxiliar.nz(idioma, "")));

		if (accion == null) {
			target = "/login.jsp";
			retorno = Auxiliar.mensajeImpersonal("advertencia", Auxiliar.traducir("SESION_VENCIDA", "es", "Credenciales erradas o Su sesión ha vencido"));
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else {
			target = "/coordenadas_parcela.jsp?PRCL_CONSECUTIVO="+Auxiliar.nz(request.getParameter("PRCL_CONSECUTIVO"), "");
			response.setContentType("text/html; charset=UTF-8");

			f_PRCR_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCR_PRCL_CONSECUTIVO"), "");
			f_PRCR_ID = Auxiliar.nz(request.getParameter("PRCR_ID"), "");
			f_PRCR_SECUENCIA = Auxiliar.nz(request.getParameter("PRCR_SECUENCIA"), "");
			f_PRCR_LATITUD = Auxiliar.nz(request.getParameter("PRCR_LATITUD"), "");
			f_PRCR_LONGITUD = Auxiliar.nz(request.getParameter("PRCR_LONGITUD"), "");

			if (Auxiliar.tieneAlgo(f_PRCR_PRCL_CONSECUTIVO)) {
				if (accion.equals("detalle"))
				{
					try {
						if (
								sec.tienePermiso(usuario,  "32") 
							||	sec.tienePermiso(usuario,  "33") 
							||	sec.tienePermiso(usuario,  "34") 
							||	sec.tienePermiso(usuario,  "35") 
							|| siga
							) {
							retorno = "";
							
							request = establecerAtributos(request, session);
						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a listarRegistros(): " + e.toString(), usuario, metodo);
						e.printStackTrace();
					}
				}
				else if (accion.equals("guardar"))
				{
					try {
						if (
								sec.tienePermiso(usuario,  "36") 
								||	sec.tienePermiso(usuario,  "37")
								|| siga
								) {
							String resultado = guardar(
								f_PRCR_ID, 
								f_PRCR_PRCL_CONSECUTIVO, 
								f_PRCR_SECUENCIA,
								f_PRCR_LATITUD,
								f_PRCR_LONGITUD,
								session
								);
						
							String [] a_resultado = resultado.split("-=-");
							retorno = a_resultado[1];
							
							request = establecerAtributos(request, session);
						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
						e.printStackTrace();
					}
				}
				else if (accion.equals("eliminar"))
				{
					try {
						if (
							sec.tienePermiso(usuario,  "36") 
							||	sec.tienePermiso(usuario,  "37")
							|| siga
							) {
							String resultado = eliminar(Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), session);
							String [] a_resultado = resultado.split("-=-");
							retorno = a_resultado[1];
		
							request = establecerAtributos(request, session);
						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
						e.printStackTrace();
					}
					//target = "/Individuo?accion=detalle_individuo&TAYO_CONSECUTIVO="+Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), "")+"&mensaje_edicion="+retorno;
				}
				
				request.setAttribute("retorno", retorno);
				ServletContext context = getServletContext();
				RequestDispatcher dispatcher = context.getRequestDispatcher(target);
				dispatcher.forward(request, response);
			}
			else {
				retorno = Auxiliar.mensaje("advertencia", "Identificador del individuo no especificado", usuario, metodo);
			}
		}
	}

	/**
	 * Establece los atributos del formulario con los de la base de datos o, por defectos con los enviados en el formulario.
	 * 
	 * @param request
	 * @return request con los parámetros actualizados
	 * @throws Exception 
	 */
	private HttpServletRequest establecerAtributos(HttpServletRequest request, HttpSession session) 
			throws Exception {
		String metodo = yo + "establecerAtributos";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String PRCR_ID = "";
	    
	    PRCR_ID = Auxiliar.nz(request.getParameter("PRCR_ID"), ""); 

	    String db_PRCR_ID = Auxiliar.nz(request.getParameter("PRCR_ID"), "");
		String db_PRCR_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("PRCR_PRCL_CONSECUTIVO"), "");
		String db_PRCR_SECUENCIA = Auxiliar.nz(request.getParameter("PRCR_SECUENCIA"), "");
		String db_PRCR_LATITUD = Auxiliar.nz(request.getParameter("PRCR_LATITUD"), "");
		String db_PRCR_LONGITUD = Auxiliar.nz(request.getParameter("PRCR_LONGITUD"), "");
		
		if (Auxiliar.tieneAlgo(PRCR_ID)) {
			try {
			    ResultSet rset = cargarRegistro(PRCR_ID, session);
			    
			    if (rset != null) {
					if (rset.next()) {
						
						db_PRCR_ID = rset.getString("PRCR_ID");
						db_PRCR_PRCL_CONSECUTIVO = rset.getString("PRCR_PRCL_CONSECUTIVO");
						db_PRCR_SECUENCIA = rset.getString("PRCR_SECUENCIA");
						db_PRCR_LATITUD = rset.getString("PRCR_LATITUD");
						db_PRCR_LONGITUD = rset.getString("PRCR_LONGITUD");
						
						rset.close();
					}
			    }
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("PRCR_ID", db_PRCR_ID);
		request.setAttribute("PRCR_PRCL_CONSECUTIVO", db_PRCR_PRCL_CONSECUTIVO);
		request.setAttribute("PRCR_SECUENCIA", db_PRCR_SECUENCIA);
		request.setAttribute("PRCR_LATITUD", db_PRCR_LATITUD);
		request.setAttribute("PRCR_LONGITUD", db_PRCR_LONGITUD);
		
		if (Auxiliar.tieneAlgo(db_PRCR_PRCL_CONSECUTIVO)) {
			String [] a = listarRegistros(db_PRCR_PRCL_CONSECUTIVO, session);
			request.setAttribute("tabla_coordenadas", a[0]);
			request.setAttribute("str_coordenadas", a[1]);
			request.setAttribute("str_coordenadas_secuencia", a[2]);
			
			request.setAttribute("CX", dbREDD.obtenerDato("SELECT AVG(t.X) AS CX FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCR_PRCL_CONSECUTIVO, "-73"));
			request.setAttribute("CY", dbREDD.obtenerDato("SELECT AVG(t.Y) AS CY FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCR_PRCL_CONSECUTIVO, "4"));
			request.setAttribute("S", dbREDD.obtenerDato("SELECT MIN(t.Y) AS S FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCR_PRCL_CONSECUTIVO, "-73"));
			request.setAttribute("W", dbREDD.obtenerDato("SELECT MIN(t.X) AS W FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCR_PRCL_CONSECUTIVO, "4"));
			request.setAttribute("N", dbREDD.obtenerDato("SELECT MAX(t.Y) AS N FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCR_PRCL_CONSECUTIVO, "-73"));
			request.setAttribute("E", dbREDD.obtenerDato("SELECT MAX(t.X) AS E FROM RED_PARCELA c, TABLE(SDO_UTIL.GETVERTICES(c.PRCL_GEO)) t WHERE c.PRCL_CONSECUTIVO=" + db_PRCR_PRCL_CONSECUTIVO, "4"));
		}
		else {
			request.setAttribute("CX", "");
			request.setAttribute("CY", "");
			request.setAttribute("S", "");
			request.setAttribute("W", "");
			request.setAttribute("N", "");
			request.setAttribute("E", "");
		}

		dbREDD.desconectarse();
		return request;
	}
	
	/**
	 * Método que carga los registros de coordenadas de una parcela
	 * 
	 * @param PRCR_ID
	 * @param session
	 * @return String t con el resultado
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public ResultSet cargarRegistro(
			String PRCR_ID,
			HttpSession session
			) throws ClassNotFoundException, Exception {
		String metodo = yo + "cargarRegistro";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    ResultSet rset = null;
	    
		String w_codigo = "";
		
		if (Auxiliar.tieneAlgo(PRCR_ID)) {
			w_codigo = " AND PRCR_ID=" + PRCR_ID;
		}
		else {
			dbREDD.desconectarse();
			return rset;
		}
		
		String sql = "SELECT * FROM RED_PARCELA_COORDENADAS WHERE 1=1 " + w_codigo;
		
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
	 * Metodo que retorna una tabla de coordenadas encontrados
	 * 
	 * @param PRCL_CONSECUTIVO
	 * @param session
	 * @return String[] de tallos
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] listarRegistros(String PRCL_CONSECUTIVO, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "listarRegistros";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String [] a = new String[3];
	    
		String w_PRCL_CONSECUTIVO = "";

		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			w_PRCL_CONSECUTIVO = " AND PRCR_PRCL_CONSECUTIVO = ("+PRCL_CONSECUTIVO+") ";
		}
		
		String sql = "SELECT ";
		sql += "PRCR_ID,";
		sql += "PRCR_SECUENCIA,";
		sql += "PRCR_LATITUD,";
		sql += "PRCR_LONGITUD";
		sql += " FROM RED_PARCELA_COORDENADAS";
		sql += " WHERE 1=1 ";
		sql += w_PRCL_CONSECUTIVO;
		sql += " ORDER BY PRCR_SECUENCIA ";

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String tabla_coordenadas = "";
				String str_coordenadas = "";
				String str_coordenadas_secuencia = "";

				String tabla = "";
				
				String db_PRCR_ID = "";
				String db_PRCR_SECUENCIA = "";
				String db_PRCR_LATITUD = "";
				String db_PRCR_LONGITUD = "";
				
				String opciones = "";
				
				tabla = "<table>";
				
				tabla += "<tr>";
				//t = Auxiliar.traducir("PRCR_ID", idioma, "Id" + "..");
				//tabla += "<th>" + t + "</th>";
				t = Auxiliar.traducir("PRCR_SECUENCIA", idioma, "Secuencia" + "..");
				tabla += "<th>" + t + "</th>";
				t = Auxiliar.traducir("PRCR_LATITUD", idioma, "Latitud" + "..");
				tabla += "<th>" + t + "</th>";
				t = Auxiliar.traducir("PRCR_LONGITUD", idioma, "Longitud" + "..");
				tabla += "<th>" + t + "</th>";
				t = Auxiliar.traducir("General.Opciones", idioma, "Opciones" + "..");
				tabla += "<th>" + t + "</th>";
				tabla += "</tr>";
				
				while (rset.next())
				{
					opciones = "";
					
					db_PRCR_ID = rset.getString("PRCR_ID");
					db_PRCR_SECUENCIA = rset.getString("PRCR_SECUENCIA");
					db_PRCR_LATITUD = rset.getString("PRCR_LATITUD");
					db_PRCR_LONGITUD = rset.getString("PRCR_LONGITUD");

					t = Auxiliar.traducir("General.Editar", idioma, "Editar" + "..");
					opciones += "<div class='opcionmenu'><a class=boton href='Parcela?accion=coordenadas&control=cargar&PRCR_ID="+db_PRCR_ID+"&PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+"' >" + t + "</a></div>";

					t = Auxiliar.traducir("General.Eliminar", idioma, "Eliminar" + "..");
					opciones += "<div class='opcionmenu'><a class=boton href='Parcela?accion=coordenadas&control=eliminar&PRCR_ID="+db_PRCR_ID+"&PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+"' >" + t + "</a></div>";
					
					tabla += "<tr>";
					//tabla += "<td>"+db_PRCR_ID+"</td>";
					tabla += "<td>"+db_PRCR_SECUENCIA+"</td>";
					tabla += "<td>"+db_PRCR_LATITUD+"</td>";
					tabla += "<td>"+db_PRCR_LONGITUD+"</td>";
					tabla += "<td>"+opciones+"</td>";
					tabla += "</tr>";
					
					str_coordenadas += "[" + db_PRCR_LATITUD + "," + db_PRCR_LONGITUD + "],";
					str_coordenadas_secuencia += "[" + db_PRCR_SECUENCIA + "],";
				}
				
				if (str_coordenadas.length() > 0) str_coordenadas = str_coordenadas.substring(0, str_coordenadas.length()-1);
				if (str_coordenadas_secuencia.length() > 0) str_coordenadas_secuencia = str_coordenadas_secuencia.substring(0, str_coordenadas_secuencia.length()-1);
				
				tabla += "</table>";
				
				rset.close();
				
				tabla_coordenadas=tabla;
				
				a[0] = tabla_coordenadas;
				a[1] = str_coordenadas;
				a[2] = str_coordenadas_secuencia;
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		}
		
		dbREDD.desconectarse();
		return a;
	}
	
	/**
	 * Guarda una coordenada de vértice de parcela
	 * 
	 * @param PRCR_ID
	 * @param PRCR_PRCL_CONSECUTIVO
	 * @param PRCR_SECUENCIA
	 * @param PRCR_LATITUD
	 * @param PRCR_LONGITUD
	 * @param session
	 * @return String de resultado de la operación.
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String guardar(
			String PRCR_ID,
			String PRCR_PRCL_CONSECUTIVO,
			String PRCR_SECUENCIA, 
			String PRCR_LATITUD,
			String PRCR_LONGITUD, 
			HttpSession session
			) throws ClassNotFoundException, Exception {
		String metodo = yo + "guardar";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String r = "";
		String sql = "";
		
		if (Auxiliar.tieneAlgo(PRCR_ID)) {
			sql = "UPDATE RED_PARCELA_COORDENADAS SET ";
			sql += " PRCR_SECUENCIA="+PRCR_SECUENCIA+",";
			sql += " PRCR_LATITUD="+PRCR_LATITUD+",";
			sql += " PRCR_LONGITUD="+PRCR_LONGITUD+"";
			sql += " WHERE PRCR_ID="+PRCR_ID+" AND PRCR_PRCL_CONSECUTIVO="+PRCR_PRCL_CONSECUTIVO;
		}
		else {
			try {
				// ELIMINAR LA COORDENADA EXISTENTE:
				dbREDD.ejecutarSQL("DELETE FROM RED_PARCELA_COORDENADAS WHERE PRCR_PRCL_CONSECUTIVO="+PRCR_PRCL_CONSECUTIVO+" AND PRCR_SECUENCIA="+PRCR_SECUENCIA);
			}
			catch (Exception e) {
				
			}
			
			sql = "INSERT INTO RED_PARCELA_COORDENADAS ( ";
			sql += " PRCR_SECUENCIA,";
			sql += " PRCR_PRCL_CONSECUTIVO,";
			sql += " PRCR_LATITUD,";
			sql += " PRCR_LONGITUD";
			sql += " ) VALUES ( ";
			sql += " "+PRCR_SECUENCIA+",";
			sql += " "+PRCR_PRCL_CONSECUTIVO+",";
			sql += " "+PRCR_LATITUD+",";
			sql += " "+PRCR_LONGITUD+"";
			sql += " )";
		}
			
		try {
			if (dbREDD.ejecutarSQL(sql)) {
				r = "1-=-" + Auxiliar.mensaje("confirmacion", "Coordenadas registradas.", usuario, metodo);
			}
			else {
				r = "0-=-" + Auxiliar.mensaje("error", "Las coordenadas no se pudieron registrar:" + dbREDD.ultimoError, usuario, metodo);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return r;
	}
	
	/**
	 * Elimina las coordenadas de una parcela 
	 * 
	 * @param PRCR_ID
	 * @param session
	 * @return String con resultado de la operación
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String eliminar(String PRCR_ID, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "eliminar";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		String r = "";
		String sql = "";
		
		sql = "DELETE FROM RED_PARCELA_COORDENADAS WHERE PRCR_ID="+PRCR_ID;
		try {
			if (dbREDD.ejecutarSQL(sql)) {
				r = "1-=-" + Auxiliar.mensaje("confirmacion", "Coordenadas eliminadas.", usuario, metodo);
			}
			else {
				r = "0-=-" + Auxiliar.mensaje("error", "Las coordenadas no se pudieron eliminar.", usuario, metodo);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		return null;
	}
	
} //953
