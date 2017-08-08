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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;



/** 
 * Permite administrar los Coberturas de los Individuos.
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com
 */
@SuppressWarnings("serial")
public class Cobertura extends HttpServlet {

	
	public static String yo = "Cobertura.";
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
	public Cobertura() {
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
		
	    String f_CBRT_CONSECUTIVO = "";
	    String f_CBRT_PRCL_CONSECUTIVO = "";
	    String f_CBRT_TPCB_CONSECUTIVO = "";
	    String f_CBRT_TPER_ID = "";
	    String f_CBRT_SVPR_CONSECUTIVO = "";
	    String f_CBRT_PORCENTAJE = "";
	    String f_CBRT_SUBPARCELA = "";

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
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
		}
		else {
			retorno = "";
			target = "/cobertura.jsp";
			response.setContentType("text/html; charset=UTF-8");

			f_CBRT_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""); 
			f_CBRT_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_PRCL_CONSECUTIVO"), ""); 
			f_CBRT_TPCB_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_TPCB_CONSECUTIVO"), "");
			f_CBRT_PORCENTAJE = Auxiliar.nz(request.getParameter("CBRT_PORCENTAJE"), "");
			f_CBRT_SUBPARCELA = Auxiliar.nz(request.getParameter("CBRT_SUBPARCELA"), "");
			f_CBRT_TPER_ID = Auxiliar.nz(request.getParameter("CBRT_TPER_ID"), "");
			f_CBRT_SVPR_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_SVPR_CONSECUTIVO"), "");
		
			if (Auxiliar.tieneAlgo(f_CBRT_PRCL_CONSECUTIVO)) {
				if (accion.equals("guardar"))
				{
					try {
						if (
								sec.tienePermiso(usuario,  "224") 
								||	sec.tienePermiso(usuario,  "223")
								||	sec.tienePermiso(usuario,  "222")
								||	sec.tienePermiso(usuario,  "221")
								) {
							String resultado = guardar(
								f_CBRT_CONSECUTIVO, 
								f_CBRT_PRCL_CONSECUTIVO, 
								f_CBRT_TPCB_CONSECUTIVO,
								f_CBRT_PORCENTAJE,
								f_CBRT_SUBPARCELA,
								f_CBRT_TPER_ID,
								f_CBRT_SVPR_CONSECUTIVO,
								"",
								request,
								false
								);
						
							String [] a_resultado = resultado.split("-=-");
							retorno = a_resultado[1];
							
							request = establecerAtributos(request, session, a_resultado[0]);
						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 224, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 223, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 222, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 221, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 224, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 223, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 222, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 221, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
				else if (accion.equals("eliminar"))
				{
					try {
						if (
								sec.tienePermiso(usuario,  "232")
								|| sec.tienePermiso(usuario,  "231")
								) {
							String resultado = eliminar(Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), request);
							String [] a_resultado = resultado.split("-=-");
							retorno = a_resultado[1];
		
							request.setAttribute("CBRT_PRCL_CONSECUTIVO", f_CBRT_PRCL_CONSECUTIVO);

						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 232, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 231, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 232, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 231, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
				
				try {
					if (sec.tienePermiso(usuario,  "189")) {
						request = establecerAtributos(request, session, "");
					}
					else {
						retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
						sec.registrarTransaccion(request, 189, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "permisos", false);
					}
				} catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a listarRegistros(): " + e.toString(), usuario, metodo);
					try {
						sec.registrarTransaccion(request, 189, Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}

			}
			else {
				retorno = Auxiliar.mensaje("advertencia", "Identificador del parcela no especificado", usuario, metodo);
			}
		}
		
		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);

	}

	
	
	/**
	 * Inicializa atributos del formulario
	 * 
	 * @param request
	 * @return request con los atributos inicializados
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

	    String CBRT_CONSECUTIVO = "";
	    
	    CBRT_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""); 
	    
	    if (Auxiliar.tieneAlgo(nuevo_consecutivo)) CBRT_CONSECUTIVO = nuevo_consecutivo;

		String db_CBRT_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_CONSECUTIVO"), ""); 
		String db_CBRT_PRCL_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_PRCL_CONSECUTIVO"), ""); 
		String db_CBRT_TPCB_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_TPCB_CONSECUTIVO"), "");
		String db_CBRT_PORCENTAJE = Auxiliar.nz(request.getParameter("CBRT_PORCENTAJE"), "");
		String db_CBRT_SUBPARCELA = Auxiliar.nz(request.getParameter("CBRT_SUBPARCELA"), "");
		String db_CBRT_TPER_ID = Auxiliar.nz(request.getParameter("CBRT_TPER_ID"), "");
		String db_CBRT_SVPR_CONSECUTIVO = Auxiliar.nz(request.getParameter("CBRT_SVPR_CONSECUTIVO"), "");
		
		if (Auxiliar.tieneAlgo(CBRT_CONSECUTIVO)) {
			try {
			    ResultSet rset = cargarRegistro(CBRT_CONSECUTIVO, session);
			    
			    if (rset != null) {
					if (rset.next()) {
						
						db_CBRT_CONSECUTIVO = rset.getString("CBRT_CONSECUTIVO");
						db_CBRT_PRCL_CONSECUTIVO = rset.getString("CBRT_PRCL_CONSECUTIVO");
						db_CBRT_TPCB_CONSECUTIVO = rset.getString("CBRT_TPCB_CONSECUTIVO");
						db_CBRT_PORCENTAJE = rset.getString("CBRT_PORCENTAJE");
						db_CBRT_SUBPARCELA = rset.getString("CBRT_SUBPARCELA");
						db_CBRT_TPER_ID = rset.getString("CBRT_TPER_ID");
						db_CBRT_SVPR_CONSECUTIVO = rset.getString("CBRT_SVPR_CONSECUTIVO");
						
						rset.close();
					}
			    }
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("CBRT_CONSECUTIVO", db_CBRT_CONSECUTIVO);
		request.setAttribute("CBRT_PRCL_CONSECUTIVO", db_CBRT_PRCL_CONSECUTIVO);
		request.setAttribute("CBRT_TPCB_CONSECUTIVO", db_CBRT_TPCB_CONSECUTIVO);
		request.setAttribute("CBRT_PORCENTAJE", db_CBRT_PORCENTAJE);
		request.setAttribute("CBRT_SUBPARCELA", db_CBRT_SUBPARCELA);
		request.setAttribute("CBRT_TPER_ID", db_CBRT_TPER_ID);
		request.setAttribute("CBRT_SVPR_CONSECUTIVO", db_CBRT_SVPR_CONSECUTIVO);
		
		request.setAttribute("idioma",idioma);

		request.setAttribute("opciones_tipocobertura",Auxiliar.cargarOpciones("SELECT TPCB_CONSECUTIVO, TPCB_DESCRIPCION FROM RED_TIPOCOBERTURA ORDER BY TPCB_DESCRIPCION", "TPCB_CONSECUTIVO", "TPCB_DESCRIPCION", db_CBRT_TPER_ID, "", false, true, false));
		request.setAttribute("opciones_tipoperturbacion",Auxiliar.cargarOpciones("SELECT TPER_ID, TPER_NOMBRE FROM RED_TIPOPERTURBACION ORDER BY TPER_NOMBRE", "TPER_ID", "TPER_NOMBRE", db_CBRT_SVPR_CONSECUTIVO, "", false, true, false));
		request.setAttribute("opciones_severidad",Auxiliar.cargarOpciones("SELECT SVPR_CONSECUTIVO, SVPR_DESCRIPCION FROM RED_SEVERIDADPERTURBACION ORDER BY SVPR_DESCRIPCION", "SVPR_CONSECUTIVO", "SVPR_DESCRIPCION", db_CBRT_SVPR_CONSECUTIVO, "", false, true, false));
		
		String[] a_registros = listarRegistros(db_CBRT_PRCL_CONSECUTIVO, session);
		
		request.setAttribute("tabla_registros", a_registros[0]);
		request.setAttribute("n_registros", a_registros[1]);
		
		dbREDD.desconectarse();
		
		Sec sec = new Sec();
		sec.registrarTransaccion(request, 189, CBRT_CONSECUTIVO, "", true);

		return request;
	}
	
	/**
	 * Método que inicializa los valores de los campos para un parcela especificado.
	 * 
	 * @param CBRT_CONSECUTIVO
	 * @param session
	 * @return registros encontrados
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public ResultSet cargarRegistro(String CBRT_CONSECUTIVO, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "cobertura";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		ResultSet rset = null;
		
		String sql = "SELECT ";
		sql += "CBRT_CONSECUTIVO,";
		sql += "CBRT_PRCL_CONSECUTIVO,";
		sql += "CBRT_TPCB_CONSECUTIVO,";
		sql += "CBRT_PORCENTAJE,";
		sql += "CBRT_SUBPARCELA,";
		sql += "CBRT_TPER_ID,";
		sql += "CBRT_SVPR_CONSECUTIVO";
		sql += " FROM RED_COBERTURA ";
		sql += " WHERE CBRT_CONSECUTIVO="+CBRT_CONSECUTIVO;

		try {
			rset = dbREDD.consultarBD(sql);

			if (rset != null)
			{
				return rset;
			}
			else
			{
				t = Auxiliar.mensaje("advertencia", "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError, usuario, metodo);
			}
		} catch (SQLException e) {
			t = Auxiliar.mensaje("error", "Excepción de SQL ["+sql+"]: " + e.toString(), usuario, metodo);
		} catch (Exception e) {
			t = Auxiliar.mensaje("error", "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql, usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return rset;
	}
	
	
	/**
	 * Metodo que retorna una tabla de porcentajes de cobertura encontrados
	 * 
	 * @param CBRT_PRCL_CONSECUTIVO
	 * @param session
	 * @return String de porcentajes de cobertura
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] listarRegistros(String CBRT_PRCL_CONSECUTIVO, HttpSession session) 
	throws ClassNotFoundException, Exception {
		String metodo = yo + "listarRegistros";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		int n = 0;
		
	    String tabla = "No se encontraron registros.";

		String [] a_tabla = {tabla, "0"};

		String w_CBRT_PRCL_CONSECUTIVO = "";

		if (Auxiliar.tieneAlgo(CBRT_PRCL_CONSECUTIVO)) {
			w_CBRT_PRCL_CONSECUTIVO = " AND CBRT_PRCL_CONSECUTIVO IN ("+CBRT_PRCL_CONSECUTIVO+") ";
		}
		else {
			dbREDD.desconectarse();
			return a_tabla;
		}
		
		String sql = "SELECT ";
		sql += "CBRT.CBRT_CONSECUTIVO AS CBRT_CONSECUTIVO,";
		sql += "CBRT.CBRT_SUBPARCELA AS CBRT_SUBPARCELA,";
		sql += "TPER.TPER_NOMBRE AS TIPOPERTURBACION,";
		sql += "SVPR.SVPR_DESCRIPCION AS SEVERIDADPERTURBACION,";
		sql += "TPCB.TPCB_DESCRIPCION AS TIPOCOBERTURA,";
		sql += "CBRT.CBRT_PORCENTAJE AS CBRT_PORCENTAJE";
		sql += " FROM RED_COBERTURA CBRT ";
		sql += " INNER JOIN RED_TIPOPERTURBACION TPER ON CBRT.CBRT_TPER_ID=TPER.TPER_ID ";
		sql += " INNER JOIN RED_SEVERIDADPERTURBACION SVPR ON CBRT.CBRT_SVPR_CONSECUTIVO=SVPR.SVPR_CONSECUTIVO ";
		sql += " INNER JOIN RED_TIPOCOBERTURA TPCB ON CBRT.CBRT_TPCB_CONSECUTIVO=TPCB.TPCB_CONSECUTIVO ";
		sql += " WHERE CBRT.CBRT_PRCL_CONSECUTIVO="+CBRT_PRCL_CONSECUTIVO;
		sql += " ORDER BY CBRT.CBRT_PORCENTAJE DESC";
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				n = 0; 
				
				String db_CBRT_CONSECUTIVO = "";
				String db_CBRT_TPCB_CONSECUTIVO = "";
				String db_CBRT_TPER_ID = "";
				String db_CBRT_SVPR_CONSECUTIVO = "";
				String db_CBRT_PORCENTAJE = "";
				String db_CBRT_SUBPARCELA = "";
				
				String opciones = "";
				
				tabla = "<div id='contenedor-resultados' class='tabla_resultados'>";
				
				String t_CBRT_CONSECUTIVO = "";
				String t_CBRT_TPCB_CONSECUTIVO = "";
				String t_CBRT_TPER_ID = "";
				String t_CBRT_SVPR_CONSECUTIVO = "";
				String t_CBRT_PORCENTAJE = "";
				String t_CBRT_SUBPARCELA = "";
				
				t_CBRT_CONSECUTIVO = Auxiliar.traducir("CBRT_CONSECUTIVO", idioma, "Consecutivo del registro" + " ");
				t_CBRT_TPCB_CONSECUTIVO = Auxiliar.traducir("CBRT_TPCB_CONSECUTIVO", idioma, "Tipo Cobertura" + " ");
				t_CBRT_PORCENTAJE = Auxiliar.traducir("CBRT_PORCENTAJE", idioma, "Porcentaje Parcela Bajo Cobertura" + " ");
				t_CBRT_SUBPARCELA = Auxiliar.traducir("CBRT_SUBPARCELA", idioma, "Subparcela" + " ");
				t_CBRT_TPER_ID = Auxiliar.traducir("CBRT_TPER_ID", idioma, "Perturbación" + " ");
				t_CBRT_SVPR_CONSECUTIVO = Auxiliar.traducir("CBRT_SVPR_CONSECUTIVO", idioma, "Severidad" + " ");
				
				while (rset.next())
				{
					n++;
					
					opciones = "";
					
					db_CBRT_CONSECUTIVO = rset.getString("CBRT_CONSECUTIVO");
					db_CBRT_TPCB_CONSECUTIVO = rset.getString("TIPOCOBERTURA");
					db_CBRT_TPER_ID = rset.getString("TIPOPERTURBACION");
					db_CBRT_SVPR_CONSECUTIVO = rset.getString("SEVERIDADPERTURBACION");
					db_CBRT_PORCENTAJE = rset.getString("CBRT_PORCENTAJE");
					db_CBRT_SUBPARCELA = rset.getString("CBRT_SUBPARCELA");

					t = Auxiliar.traducir("General.Ver_Detalle", idioma, "Ver Detalle" + " ");
					opciones += "<div class='opcionmenu'><a class=boton href='Cobertura?accion=detalle&CBRT_PRCL_CONSECUTIVO="+CBRT_PRCL_CONSECUTIVO+"&CBRT_CONSECUTIVO="+db_CBRT_CONSECUTIVO+"'>" + t + "</a></div>";
					
					tabla += "<span>";
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_CBRT_CONSECUTIVO+":"+db_CBRT_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_CBRT_SUBPARCELA+":"+db_CBRT_SUBPARCELA+"</div>";
					tabla += "<div class='dato_resultado'>"+t_CBRT_TPER_ID+":"+db_CBRT_TPER_ID+"</div>";
					tabla += "<div class='dato_resultado'>"+t_CBRT_SVPR_CONSECUTIVO+":"+db_CBRT_SVPR_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_CBRT_TPCB_CONSECUTIVO+":"+db_CBRT_TPCB_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_CBRT_PORCENTAJE+":"+db_CBRT_PORCENTAJE+"</div>";
					tabla += "<div class='botones_resultado'>"+opciones+"</div>";
					tabla += "</div>";
					tabla += "</span>";

				}
				
				if (n == 0) {
					tabla += "<div class='resultado'>Esta parcela aún no tiene porcentajes de cobertura registrados.</div>";					
				}
				
				tabla += "</div>";
				
				a_tabla[0] = tabla;
				a_tabla[1] = String.valueOf(n);

				rset.close();
			}
			else
			{
				t = Auxiliar.mensaje("advertencia", "El conjunto de resultados retornados para la consulta ["+sql+"] es nulo.  Último error de la interacción con la base de datos: " + dbREDD.ultimoError, usuario, metodo);
			}
		} catch (SQLException e) {
			t = Auxiliar.mensaje("error", "Excepción de SQL ["+sql+"]: " + e.toString(), usuario, metodo);
		} catch (Exception e) {
			t = Auxiliar.mensaje("error", "Ocurrió la siguiente excepción en consultarArchivos(): " + e.toString() + " -- SQL: " + sql, usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return a_tabla;
	}
	
	
	/**
	 * Guarda un porcentaje de cobertura
	 * 
	 * @param CBRT_CONSECUTIVO
	 * @param CBRT_PRCL_CONSECUTIVO
	 * @param CBRT_TPCB_CONSECUTIVO
	 * @param CBRT_PORCENTAJE
	 * @param CBRT_SUBPARCELA
	 * @param CBRT_TPER_ID
	 * @param CBRT_SVPR_CONSECUTIVO
	 * @param CBRT_ID_IMPORTACION
	 * @param request
	 * @param importacion
	 * @return String con resultado de la operación
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String guardar(
			String CBRT_CONSECUTIVO,
			String CBRT_PRCL_CONSECUTIVO,
			String CBRT_TPCB_CONSECUTIVO,
			String CBRT_PORCENTAJE,
			String CBRT_SUBPARCELA,
			String CBRT_TPER_ID,
			String CBRT_SVPR_CONSECUTIVO,
			String CBRT_ID_IMPORTACION,
			HttpServletRequest request,
			boolean importacion
			) 
	throws Exception
	{
		String metodo = yo + "guardar";
		
		Sec sec = new Sec();
		Codec ORACLE_CODEC = new OracleCodec();

		//Auxiliar aux = new Auxiliar();
		HttpSession session = request.getSession();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    if (!Auxiliar.tieneAlgo(this.RED_username)) this.RED_username = session.getAttribute("RED_username").toString();
	    if (!Auxiliar.tieneAlgo(this.RED_password)) this.RED_password = session.getAttribute("RED_password").toString();
	    if (!Auxiliar.tieneAlgo(this.RED_host)) this.RED_host = session.getAttribute("RED_host").toString();
	    if (!Auxiliar.tieneAlgo(this.RED_port)) this.RED_port = session.getAttribute("RED_port").toString();
		if (!Auxiliar.tieneAlgo(this.RED_sid)) this.RED_sid = session.getAttribute("RED_sid").toString();

		BD dbREDD = new BD();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");

		String resultado = "";
		String novedades = "";
		String update_esadmin = "";
		
		String observaciones = "";
		String aviso = "";
		boolean parametros_ok = true;

		String conteo = "";
		String sql_tmp = "";
		
		boolean ok_guardar = false;
		boolean update = false;
		boolean privilegio = false;
		
		String id_creador = "";

		String pe = "";
		
		pe = Auxiliar.traducir("General.Por_favor_especifique", idioma, "Por favor especifique" + " ");
		
		
		// VERIFICAR CBRT_PRCL_CONSECUTIVO
		conteo = "0";
		sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_PARCELA WHERE PRCL_CONSECUTIVO=" + CBRT_PRCL_CONSECUTIVO;
		try {
			conteo = dbREDD.obtenerDato(sql_tmp, "0");
		} catch (Exception ex) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.CBRT_PRCL_CONSECUTIVO", idioma, "Problemas al intentar determinar la existencia de CBRT_PRCL_CONSECUTIVO [" + ex.toString() + "]:" + sql_tmp + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_PRCL_CONSECUTIVO, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.CBRT_PRCL_CONSECUTIVO", idioma, "CBRT_PRCL_CONSECUTIVO no encontrado" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_PRCL_CONSECUTIVO, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		
		// VALIDAR CBRT_SUBPARCELA
		CBRT_SUBPARCELA = CBRT_SUBPARCELA.replace(",", ".");
		if (!Auxiliar.esSubparcela(CBRT_SUBPARCELA)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.CBRT_SUBPARCELA", idioma, "CBRT_SUBPARCELA debe ser numérico, entero de 1 a 5" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_SUBPARCELA, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VERIFICAR CBRT_TPER_ID
		conteo = "0";
		sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_TIPOPERTURBACION WHERE TPER_ID=" + CBRT_TPER_ID;
		try {
			conteo = dbREDD.obtenerDato(sql_tmp, "0");
		} catch (Exception ex) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.CBRT_TPER_ID", idioma, "Problemas al intentar determinar la existencia de CBRT_TPER_ID [" + ex.toString() + "]:" + sql_tmp + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_TPER_ID, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.CBRT_TPER_ID", idioma, "CBRT_TPER_ID no encontrado" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_TPER_ID, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VERIFICAR CBRT_SVPR_CONSECUTIVO
		if (Auxiliar.tieneAlgo(CBRT_SVPR_CONSECUTIVO)) {
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_SEVERIDADPERTURBACION WHERE SVPR_CONSECUTIVO=" + CBRT_SVPR_CONSECUTIVO;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.CBRT_SVPR_CONSECUTIVO", idioma, "Problemas al intentar determinar la existencia de CBRT_SVPR_CONSECUTIVO [" + ex.toString() + "]:" + sql_tmp + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_SVPR_CONSECUTIVO, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.CBRT_SVPR_CONSECUTIVO", idioma, "CBRT_SVPR_CONSECUTIVO no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_SVPR_CONSECUTIVO, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
		}
		
		// VERIFICAR CBRT_TPCB_CONSECUTIVO
		if (Auxiliar.tieneAlgo(CBRT_TPCB_CONSECUTIVO)) {
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_TIPOCOBERTURA WHERE TPCB_CONSECUTIVO=" + CBRT_TPCB_CONSECUTIVO;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.CBRT_TPCB_CONSECUTIVO", idioma, "Problemas al intentar determinar la existencia de CBRT_TPCB_CONSECUTIVO [" + ex.toString() + "]:" + sql_tmp + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_TPCB_CONSECUTIVO, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.CBRT_TPCB_CONSECUTIVO", idioma, "CBRT_TPCB_CONSECUTIVO no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_TPCB_CONSECUTIVO, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
		}

		// VALIDAR CBRT_PORCENTAJE
		CBRT_PORCENTAJE = CBRT_PORCENTAJE.replace(",", ".");
		if (!Auxiliar.esPorcentaje(CBRT_PORCENTAJE)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.CBRT_PORCENTAJE", idioma, "CBRT_PORCENTAJE debe ser numérico, positivo y menor a 1" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_PORCENTAJE, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		else {
			double sumaporcentajes = 0.0;
			String w_CBRT_CONSECUTIVO = "";
			if (Auxiliar.tieneAlgo(CBRT_CONSECUTIVO)) {
				w_CBRT_CONSECUTIVO = " AND CBRT_CONSECUTIVO <> " + CBRT_CONSECUTIVO;
			}
			String sql_sumaporcentajes = "SELECT SUM(CBRT_PORCENTAJE) AS SUMAPORCENTAJES FROM RED_COBERTURA WHERE CBRT_PRCL_CONSECUTIVO=" + CBRT_PRCL_CONSECUTIVO + w_CBRT_CONSECUTIVO;
			String str_suma = dbREDD.obtenerDato(sql_sumaporcentajes, "0.0");
			sumaporcentajes = Double.parseDouble(str_suma);
			double nuevototal = sumaporcentajes + Double.parseDouble(CBRT_PORCENTAJE); 
			if (nuevototal > 1.0) {
				String str_nuevototal = String.valueOf(nuevototal);
				t = Auxiliar.traducir("AVISO_VALIDACION.CBRT_PORCENTAJE_suma_reboso_la_copa", idioma, "La suma de porcentajes de esta parcela no puede exceder 1.0 y " + str_suma + " + " + CBRT_PORCENTAJE + " daría " + str_nuevototal + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ". Valor suministrado:" + CBRT_PORCENTAJE, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
		}


		if (!parametros_ok) {
			dbREDD.desconectarse();
			return "0-=-"+observaciones;
		}
		

		// VERIFICAR PARAMETROS

		t = Auxiliar.traducir("CBRT_TPCB_CONSECUTIVO", idioma, "el tipo de cobertura" + " ");
		//if (!Auxiliar.tieneAlgo(CBRT_TPCB_CONSECUTIVO)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("CBRT_PORCENTAJE", idioma, "el porcentaje de cobertura" + " ");
		//if (!Auxiliar.tieneAlgo(CBRT_PORCENTAJE)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("CBRT_SUBPARCELA", idioma, "el número de subparcela" + " ");
		//if (!Auxiliar.tieneAlgo(CBRT_SUBPARCELA)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("CBRT_TPER_ID", idioma, "el tipo de perturbación" + " ");
		//if (!Auxiliar.tieneAlgo(CBRT_TPER_ID)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("CBRT_SVPR_CONSECUTIVO", idioma, "la severidad de la perturbación" + " ");
		//if (!Auxiliar.tieneAlgo(CBRT_SVPR_CONSECUTIVO)) return "0-=-" + pe + " " + t;

		if (!Auxiliar.tieneAlgo(CBRT_TPCB_CONSECUTIVO)) CBRT_TPCB_CONSECUTIVO = "NULL";
		if (!Auxiliar.tieneAlgo(CBRT_PORCENTAJE)) CBRT_PORCENTAJE = "NULL";
		if (!Auxiliar.tieneAlgo(CBRT_SUBPARCELA)) CBRT_SUBPARCELA = "NULL";
		if (!Auxiliar.tieneAlgo(CBRT_TPER_ID)) CBRT_TPER_ID = "NULL";
		if (!Auxiliar.tieneAlgo(CBRT_SVPR_CONSECUTIVO)) CBRT_SVPR_CONSECUTIVO = "NULL";
		
		// REALIZAR OPERACION
		
		String sql_guardar = "";
		
		String CBRT_CONSECUTIVO_NUEVO = "";
		
		try 
		{
			if (!Auxiliar.tieneAlgo(CBRT_CONSECUTIVO)) {
				CBRT_CONSECUTIVO_NUEVO = dbREDD.obtenerDato("SELECT RED_COBERTURA_SEQ.nextval FROM DUAL", "");
			}
			
			if (Auxiliar.tieneAlgo(CBRT_CONSECUTIVO)) {
				sql_guardar = "UPDATE RED_COBERTURA SET ";
				sql_guardar += "CBRT_TPCB_CONSECUTIVO=" + CBRT_TPCB_CONSECUTIVO + ",";
				sql_guardar += "CBRT_PORCENTAJE=" + CBRT_PORCENTAJE + ",";
				sql_guardar += "CBRT_SUBPARCELA=" + CBRT_SUBPARCELA + ",";
				sql_guardar += "CBRT_TPER_ID=" + CBRT_TPER_ID + ",";
				sql_guardar += "CBRT_SVPR_CONSECUTIVO=" + CBRT_SVPR_CONSECUTIVO + ",";
				sql_guardar += "CBRT_MODIFICADOR=" + Auxiliar.nzVacio(id_usuario, "NULL") + "";
				sql_guardar += " WHERE CBRT_CONSECUTIVO=" + CBRT_CONSECUTIVO;
				update = true;
			}
			else {
				CBRT_CONSECUTIVO = CBRT_CONSECUTIVO_NUEVO;
				
				sql_guardar = "INSERT INTO RED_COBERTURA ";
				sql_guardar += "(";
				sql_guardar += "CBRT_CONSECUTIVO,";
				sql_guardar += "CBRT_PRCL_CONSECUTIVO,";
				sql_guardar += "CBRT_TPCB_CONSECUTIVO,";
				sql_guardar += "CBRT_PORCENTAJE,";
				sql_guardar += "CBRT_SUBPARCELA,";
				sql_guardar += "CBRT_TPER_ID,";
				sql_guardar += "CBRT_SVPR_CONSECUTIVO,";
				sql_guardar += "CBRT_CREADOR";
				sql_guardar += ") ";
				sql_guardar += " VALUES ";
				sql_guardar += "(";
				sql_guardar += "" + CBRT_CONSECUTIVO+ ",";
				sql_guardar += "" + CBRT_PRCL_CONSECUTIVO+ ",";
				sql_guardar += "" + CBRT_TPCB_CONSECUTIVO + ",";
				sql_guardar += "" + CBRT_PORCENTAJE + ",";
				sql_guardar += "" + CBRT_SUBPARCELA + ",";
				sql_guardar += "" + CBRT_TPER_ID + ",";
				sql_guardar += "" + CBRT_SVPR_CONSECUTIVO + ",";
				sql_guardar += "" + Auxiliar.nzVacio(id_usuario, "NULL") + "";
				sql_guardar += ")";
			}
				
			if (update) {
				id_creador = dbREDD.obtenerDato("SELECT CBRT_CREADOR FROM RED_COBERTURA WHERE CBRT_CONSECUTIVO="+CBRT_CONSECUTIVO, "");
				if (id_usuario.equals(id_creador)) {
					if (sec.tienePermiso(id_usuario, "221") || sec.tienePermiso(id_usuario, "222")) {
						privilegio = true;
					}
				}
				else {
					if (sec.tienePermiso(id_usuario, "222")) {
						privilegio = true;
					}
				}
			}
			else {
				id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+CBRT_PRCL_CONSECUTIVO, "");
				if (id_usuario.equals(id_creador)) {
					if (sec.tienePermiso(id_usuario, "223") || sec.tienePermiso(id_usuario, "224")) {
						privilegio = true;
					}
				}
				else {
					if (sec.tienePermiso(id_usuario, "224")) {
						privilegio = true;
					}
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

		        ok_guardar = dbREDD.ejecutarSQL(sql_guardar);
				if (!ok_guardar) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el registro:" + dbREDD.ultimoError, usuario, metodo);									
				}
		        
		        boolean ok_geometria = false;

				try {
					if (ok_guardar) {

						if (update) {
							id_creador = dbREDD.obtenerDato("SELECT CBRT_CREADOR FROM RED_COBERTURA WHERE CBRT_CONSECUTIVO="+CBRT_CONSECUTIVO, "");
							if (id_usuario.equals(id_creador)) {
								sec.registrarTransaccion(request, 221, CBRT_CONSECUTIVO, sql_guardar, ok_guardar);
							}
							else {
								sec.registrarTransaccion(request, 222, CBRT_CONSECUTIVO, sql_guardar, ok_guardar);
							}
						}
						else {
							if (!importacion) {
								id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+CBRT_PRCL_CONSECUTIVO, "");
								if (id_usuario.equals(id_creador)) {
									sec.registrarTransaccion(request, 223, CBRT_CONSECUTIVO, sql_guardar, ok_guardar);
								}
								else {
									sec.registrarTransaccion(request, 224, CBRT_CONSECUTIVO, sql_guardar, ok_guardar);
								}
							}
						}
						
						dbREDD.cometerTransaccion();
						resultado = CBRT_CONSECUTIVO + "-=-" + Auxiliar.mensaje("confirmacion", "Registro "+CBRT_CONSECUTIVO+" guardado.", usuario, metodo);
					}
				}
				catch (Exception e) {
					dbREDD.desconectarse();
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo una excepción al intentar guardar el registro:" + dbREDD.ultimoError, usuario, metodo);									
				}
				
		        try {
		        	dbREDD.establecerAutoCometer(true);
		        }
		        catch (Exception e) {
		        	return "0-=-" + Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
		        }
		        
			}
			catch (Exception e) {
				dbREDD.deshacerTransaccion();
				return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el registro:" + dbREDD.ultimoError, usuario, metodo);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			dbREDD.desconectarse();
			return "0-=-" + "Problema al guardar el registro: " + e.toString();
		}
		
		
		dbREDD.desconectarse();

		return resultado;
	}
	
	/**
	 * Elimina un registro de porcentaje de cobertura.
	 * 
	 * @param CBRT_CONSECUTIVO
	 * @return String t con el resultado
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public String eliminar(String CBRT_CONSECUTIVO, HttpServletRequest request) 
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

		String r = "";
		String novedades = "";
		String update_esadmin = "";
		
		boolean ok_eliminar = false;
		String id_creador = "";
		boolean privilegio = false;


		String pe = "";
		
		pe = Auxiliar.traducir("General.Por_favor_especifique", idioma, "Por favor especifique" + " ");
		
		
		// VERIFICAR PARAMETROS
		
		t = Auxiliar.traducir("CBRT_CONSECUTIVO", idioma, "el código del tallo" + " ");
		if (!Auxiliar.tieneAlgo(CBRT_CONSECUTIVO)) {
			t = Auxiliar.mensaje("advertencia", pe + " " + t, usuario, metodo);
			dbREDD.desconectarse();
			return "0-=-" + t; 
		}
		
		
		// REALIZAR OPERACION
		
		String sql_eliminar = "";
		
		try 
		{
			sql_eliminar = "DELETE FROM RED_COBERTURA WHERE CBRT_CONSECUTIVO="+CBRT_CONSECUTIVO;
			
			id_creador = dbREDD.obtenerDato("SELECT CBRT_CREADOR FROM RED_COBERTURA WHERE CBRT_CONSECUTIVO="+CBRT_CONSECUTIVO, "");
			if (id_usuario.equals(id_creador)) {
				if (sec.tienePermiso(id_usuario, "231") || sec.tienePermiso(id_usuario, "232")) {
					privilegio = true;
				}
			}
			else {
				if (sec.tienePermiso(id_usuario, "232")) {
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
					t= Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
					dbREDD.desconectarse();
					return "0-=-" + t; 
				}

				try {
					if (!dbREDD.ejecutarSQL(sql_eliminar)) {
						t = Auxiliar.mensaje("error", "Se produjo un error al intentar eliminar el registro [" + sql_eliminar + "]:" + dbREDD.ultimoError, usuario, metodo);
						r = "0-=-" + t;									
					}
					else {
						ok_eliminar = true;
						
						if (id_usuario.equals(id_creador)) {
							sec.registrarTransaccion(request, 231, CBRT_CONSECUTIVO, sql_eliminar, ok_eliminar);
						}
						else {
							sec.registrarTransaccion(request, 232, CBRT_CONSECUTIVO, sql_eliminar, ok_eliminar);
						}

						dbREDD.cometerTransaccion();
						t = Auxiliar.mensaje("confirmacion", "Registro eliminado.", usuario, metodo);
						r = "1-=-" + t;
					}
				}
				catch (Exception e) {
					t = Auxiliar.mensaje("error", "Se produjo una excepción al intentar eliminar el registro [" + sql_eliminar + "]:" + dbREDD.ultimoError, usuario, metodo);									
					r = "0-=-" + t;
				}
				
				try {
					dbREDD.establecerAutoCometer(true);
				}
				catch (Exception e) {
					t = Auxiliar.mensaje("error", "Error al establecer el auto commit sobre RED: " + dbREDD.ultimoError, usuario, metodo);
					dbREDD.desconectarse();
					return "0-=-" + t;
				}

			}
			catch (Exception e) {
				dbREDD.deshacerTransaccion();
				t = Auxiliar.mensaje("error", "Se produjo un error al intentar eliminar el registro [" + sql_eliminar + "]:" + dbREDD.ultimoError, usuario, metodo);
				r = "0-=-" + t;
			}
		} 
		catch (Exception e) {
			t = "Problema al eliminar el Individuo: " + e.toString();
			r = "0-=-" + t;
		}
		
		dbREDD.desconectarse();
		
		return r;
	}
	
	
} //953
