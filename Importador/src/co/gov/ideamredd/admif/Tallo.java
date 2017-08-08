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
 * Clase Tallo
 * Permite administrar los Tallos de los Individuos.
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 */
@SuppressWarnings("serial")
public class Tallo extends HttpServlet {

	
	public static String yo = "Tallo.";
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
	public Tallo() {
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
		
	    String f_TAYO_CONSECUTIVO = "";
	    String f_TAYO_INDV_CONSECUTIVO = "";
	    String f_TAYO_DAP1 = "";
	    String f_TAYO_DAP2 = "";
	    String f_TAYO_ALTURADAP = "";
	    String f_TAYO_ALTURA = "";
	    String f_TAYO_FORMAFUSTE = "";
	    String f_TAYO_DANIO = "";
	    String f_TAYO_OBSERVACIONES = "";
	    String f_TAYO_ALTURATOTAL = "";

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
		else if (accion.equals("plantilla_tallos"))
		{
			boolean ok = false;
			try {
				if (sec.tienePermiso(usuario, "202")) {
					ok = generarPlantillaTallos(response, session);
					if (ok) {
						sec.registrarTransaccion(request, 202, "", "", true);
					}
					else {
						sec.registrarTransaccion(request, 202, "", "error", false);
					}
				}
				else {
					sec.registrarTransaccion(request, 202, "", "permisos", false);
				}
			} 
			catch (Exception e) {				
				try {
					sec.registrarTransaccion(request, 202, "", "excepcion", false);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		else {
			target = "/detallo.jsp";
			response.setContentType("text/html; charset=UTF-8");

			f_TAYO_CONSECUTIVO = Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""); 
			f_TAYO_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("TAYO_INDV_CONSECUTIVO"), ""); 
			f_TAYO_DAP1 = Auxiliar.nz(request.getParameter("TAYO_DAP1"), "");
			f_TAYO_DAP2 = Auxiliar.nz(request.getParameter("TAYO_DAP2"), "");
			f_TAYO_ALTURADAP = Auxiliar.nz(request.getParameter("TAYO_ALTURADAP"), "");
			f_TAYO_ALTURA = Auxiliar.nz(request.getParameter("TAYO_ALTURA"), "");
			f_TAYO_ALTURATOTAL = Auxiliar.nz(request.getParameter("TAYO_ALTURATOTAL"), "");
			f_TAYO_FORMAFUSTE = Auxiliar.nz(request.getParameter("TAYO_FORMAFUSTE"), "");
			f_TAYO_DANIO = Auxiliar.nz(request.getParameter("TAYO_DANIO"), "");
			f_TAYO_OBSERVACIONES = Auxiliar.nz(request.getParameter("TAYO_OBSERVACIONES"), "");
		
			if (Auxiliar.tieneAlgo(f_TAYO_INDV_CONSECUTIVO)) {
				if (accion.equals("detalle"))
				{
					try {
						if (sec.tienePermiso(usuario,  "189")) {
							retorno = "";
							
							request = establecerAtributos(request, session, "");
						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 189, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a listarRegistros(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 189, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
				else if (accion.equals("guardar"))
				{
					try {
						if (
								sec.tienePermiso(usuario,  "224") 
								||	sec.tienePermiso(usuario,  "223")
								||	sec.tienePermiso(usuario,  "222")
								||	sec.tienePermiso(usuario,  "221")
								) {
							String resultado = guardar(
								f_TAYO_CONSECUTIVO, 
								f_TAYO_INDV_CONSECUTIVO, 
								f_TAYO_DAP1,
								f_TAYO_DAP2,
								f_TAYO_ALTURADAP,
								f_TAYO_ALTURA,
								f_TAYO_ALTURATOTAL,
								f_TAYO_FORMAFUSTE,
								f_TAYO_DANIO,
								f_TAYO_OBSERVACIONES,
								"",
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
							sec.registrarTransaccion(request, 224, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 223, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 222, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 221, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 224, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 223, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 222, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 221, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
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
							String resultado = eliminar(Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), request);
							String [] a_resultado = resultado.split("-=-");
							retorno = a_resultado[1];
		
							request = establecerAtributos(request, session, "");
							request.setAttribute("TAYO_INDV_CONSECUTIVO", f_TAYO_INDV_CONSECUTIVO);

						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 232, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
							sec.registrarTransaccion(request, 231, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "permisos", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a encontrarIndividuos(): " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 232, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
							sec.registrarTransaccion(request, 231, Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""), "excepcion:"+e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
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

	
    public String RserVersion() 
    		throws RserveException, REXPMismatchException {
    	//Auxiliar aux = new Auxiliar();
    	
        String r = "";
    	
    	try {
            RConnection c = new RConnection();// make a new local connection on default port (6311)
            double d[] = c.eval("rnorm(10)").asDoubles();
            org.rosuda.REngine.REXP x0 = c.eval("R.version.string");
            r = Auxiliar.mensajeImpersonal("nota", "Versión R: " + x0.asString());
        } catch (REngineException e) {
            //manipulation
        }       

    	return r;
    }
	
	
	
	/**
	 * Método establecerAtributos
	 * 
	 * @param request
	 * @return
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

	    String TAYO_CONSECUTIVO = "";
	    
	    TAYO_CONSECUTIVO = Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""); 
	    
	    if (Auxiliar.tieneAlgo(nuevo_consecutivo)) TAYO_CONSECUTIVO = nuevo_consecutivo;

		String db_TAYO_CONSECUTIVO = Auxiliar.nz(request.getParameter("TAYO_CONSECUTIVO"), ""); 
		String db_TAYO_INDV_CONSECUTIVO = Auxiliar.nz(request.getParameter("TAYO_INDV_CONSECUTIVO"), ""); 
		String db_TAYO_DAP1 = Auxiliar.nz(request.getParameter("TAYO_DAP1"), "");
		String db_TAYO_DAP2 = Auxiliar.nz(request.getParameter("TAYO_DAP2"), "");
		String db_TAYO_ALTURADAP = Auxiliar.nz(request.getParameter("TAYO_ALTURADAP"), "");
		String db_TAYO_ALTURA = Auxiliar.nz(request.getParameter("TAYO_ALTURA"), "");
		String db_TAYO_ALTURATOTAL = Auxiliar.nz(request.getParameter("TAYO_ALTURATOTAL"), "");
		String db_TAYO_FORMAFUSTE = Auxiliar.nz(request.getParameter("TAYO_FORMAFUSTE"), "");
		String db_TAYO_DANIO = Auxiliar.nz(request.getParameter("TAYO_DANIO"), "");
		String db_TAYO_OBSERVACIONES = Auxiliar.nz(request.getParameter("TAYO_OBSERVACIONES"), "");
		
		if (Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
			try {
			    ResultSet rset = cargarRegistro(TAYO_CONSECUTIVO, session);
			    
			    if (rset != null) {
					if (rset.next()) {
						
						db_TAYO_CONSECUTIVO = rset.getString("TAYO_CONSECUTIVO");
						db_TAYO_INDV_CONSECUTIVO = rset.getString("TAYO_INDV_CONSECUTIVO");
						db_TAYO_DAP1 = rset.getString("TAYO_DAP1");
						db_TAYO_DAP2 = rset.getString("TAYO_DAP2");
						db_TAYO_ALTURADAP = rset.getString("TAYO_ALTURADAP");
						db_TAYO_ALTURA = rset.getString("TAYO_ALTURA");
						db_TAYO_ALTURATOTAL = rset.getString("TAYO_ALTURATOTAL");
						db_TAYO_FORMAFUSTE = rset.getString("TAYO_FORMAFUSTE");
						db_TAYO_DANIO = rset.getString("TAYO_DANIO");
						db_TAYO_OBSERVACIONES = rset.getString("TAYO_OBSERVACIONES");
						
						rset.close();
					}
			    }
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("TAYO_CONSECUTIVO", db_TAYO_CONSECUTIVO);
		request.setAttribute("TAYO_INDV_CONSECUTIVO", db_TAYO_INDV_CONSECUTIVO);
		request.setAttribute("TAYO_DAP1", db_TAYO_DAP1);
		request.setAttribute("TAYO_DAP2", db_TAYO_DAP2);
		request.setAttribute("TAYO_ALTURADAP", db_TAYO_ALTURADAP);
		request.setAttribute("TAYO_ALTURA", db_TAYO_ALTURA);
		request.setAttribute("TAYO_ALTURATOTAL", db_TAYO_ALTURATOTAL);
		request.setAttribute("TAYO_FORMAFUSTE", db_TAYO_FORMAFUSTE);
		request.setAttribute("TAYO_DANIO", db_TAYO_DANIO);
		request.setAttribute("TAYO_OBSERVACIONES", db_TAYO_OBSERVACIONES);
		
		request.setAttribute("idioma",idioma);

		request.setAttribute("opciones_formafuste",Auxiliar.cargarOpciones("SELECT FRFS_CONSECUTIVO, FRFS_DESCRIPCION FROM RED_FORMAFUSTE ORDER BY FRFS_DESCRIPCION", "FRFS_CONSECUTIVO", "FRFS_DESCRIPCION", db_TAYO_FORMAFUSTE, "", false, true, false));
		request.setAttribute("opciones_danoarbol",Auxiliar.cargarOpciones("SELECT DANO_CONSECUTIVO, DANO_DESCRIPCION FROM RED_DANO ORDER BY DANO_DESCRIPCION", "DANO_CONSECUTIVO", "DANO_DESCRIPCION", db_TAYO_DANIO, "", false, true, false));
		
		String[] a_registros = listarRegistros(db_TAYO_INDV_CONSECUTIVO, session);
		
		request.setAttribute("tabla_registros", a_registros[0]);
		request.setAttribute("n_registros", a_registros[1]);
		
		request.setAttribute("RserVersion", RserVersion());

		dbREDD.desconectarse();
		
		Sec sec = new Sec();
		sec.registrarTransaccion(request, 189, TAYO_CONSECUTIVO, "", true);

		return request;
	}
	
	/**
	 * Método que inicializa los valores de los campos para un individuo especificado con f_codigo.
	 * 
	 * @param f_codigo
	 * @return String t con el resultado
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public ResultSet cargarRegistro(String TAYO_CONSECUTIVO, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "detallo";

		BD dbREDD = new BD();
		//Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		ResultSet rset = null;
		
		String sql = "SELECT ";
		sql += "TAYO_CONSECUTIVO,";
		sql += "TAYO_INDV_CONSECUTIVO,";
		sql += "TAYO_DAP1,";
		sql += "TAYO_DAP2,";
		sql += "TAYO_ALTURADAP,";
		sql += "TAYO_ALTURA,";
		sql += "TAYO_ALTURATOTAL,";
		sql += "TAYO_FORMAFUSTE,";
		sql += "TAYO_DANIO,";
		sql += "TAYO_OBSERVACIONES";
		sql += " FROM RED_TALLO ";
		sql += " WHERE TAYO_CONSECUTIVO="+TAYO_CONSECUTIVO;

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
	 * Método que retorna una tabla de tayos encontrados
	 * 
	 * @param TAYO_INDV_CONSECUTIVO
	 * @param session
	 * @return String[] Arreglo de cadenas de registros
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] listarRegistros(String TAYO_INDV_CONSECUTIVO, HttpSession session) 
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

		String w_TAYO_INDV_CONSECUTIVO = "";

		if (Auxiliar.tieneAlgo(TAYO_INDV_CONSECUTIVO)) {
			w_TAYO_INDV_CONSECUTIVO = " AND TAYO_INDV_CONSECUTIVO IN ("+TAYO_INDV_CONSECUTIVO+") ";
		}
		else {
			dbREDD.desconectarse();
			return a_tabla;
		}
		
		String sql = "SELECT ";
		sql += "TAYO_CONSECUTIVO,";
		sql += "TAYO_DAP1,";
		sql += "TAYO_DAP2,";
		sql += "TAYO_ALTURADAP,";
		sql += "TAYO_ALTURA,";
		sql += "TAYO_FORMAFUSTE,";
		sql += "TAYO_DANIO,";
		sql += "TAYO_OBSERVACIONES,";
		sql += "TAYO_ALTURATOTAL";
		sql += " FROM RED_TALLO ";
		sql += " WHERE 1=1 ";
		sql += w_TAYO_INDV_CONSECUTIVO;
		sql += " ORDER BY TAYO_CONSECUTIVO ";

		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				n = 0; 
				
				String db_TAYO_CONSECUTIVO = "";
				String db_TAYO_DAP1 = "";
				String db_TAYO_DAP2 = "";
				String db_TAYO_ALTURADAP = "";
				String db_TAYO_ALTURA = "";
				String db_TAYO_FORMAFUSTE = "";
				String db_TAYO_DANIO = "";
				String db_TAYO_OBSERVACIONES = "";
				String db_TAYO_ALTURATOTAL = "";
				
				String opciones = "";
				
				tabla = "<div id='contenedor-resultados' class='tabla_resultados'>";
				
				String t_TAYO_CONSECUTIVO = "";
				String t_TAYO_DAP1 = "";
				String t_TAYO_DAP2 = "";
				String t_TAYO_ALTURADAP = "";
				String t_TAYO_ALTURA = "";
				String t_TAYO_FORMAFUSTE = "";
				String t_TAYO_DANIO = "";
				String t_TAYO_OBSERVACIONES = "";
				String t_TAYO_ALTURATOTAL = "";
				
				t_TAYO_CONSECUTIVO = Auxiliar.traducir("TAYO_CONSECUTIVO", idioma, "Consecutivo del tallo" + " ");
				t_TAYO_DAP1 = Auxiliar.traducir("TAYO_DAP1", idioma, "DAP 1" + " ");
				t_TAYO_DAP2 = Auxiliar.traducir("TAYO_DAP2", idioma, "DAP 2" + " ");
				t_TAYO_ALTURADAP = Auxiliar.traducir("TAYO_ALTURADAP", idioma, "Altura de medición de DAP" + " ");
				t_TAYO_ALTURA = Auxiliar.traducir("TAYO_ALTURA", idioma, "Altura" + " ");
				t_TAYO_ALTURATOTAL = Auxiliar.traducir("TAYO_ALTURATOTAL", idioma, "Altura Total" + " ");
				t_TAYO_FORMAFUSTE = Auxiliar.traducir("TAYO_FORMAFUSTE", idioma, "Forma de Fuste" + " ");
				t_TAYO_DANIO = Auxiliar.traducir("TAYO_DANIO", idioma, "Daño Fuste" + " ");
				t_TAYO_FORMAFUSTE = Auxiliar.traducir("TAYO_FORMAFUSTE", idioma, "Forma de Fuste" + " ");
				t_TAYO_OBSERVACIONES = Auxiliar.traducir("TAYO_OBSERVACIONES", idioma, "Observaciones" + " ");
				
				while (rset.next())
				{
					n++;
					
					opciones = "";
					
					db_TAYO_CONSECUTIVO = rset.getString("TAYO_CONSECUTIVO");
					db_TAYO_DAP1 = rset.getString("TAYO_DAP1");
					db_TAYO_DAP2 = rset.getString("TAYO_DAP2");
					db_TAYO_ALTURADAP = rset.getString("TAYO_ALTURADAP");
					db_TAYO_ALTURA = rset.getString("TAYO_ALTURA");
					db_TAYO_FORMAFUSTE = rset.getString("TAYO_FORMAFUSTE");
					db_TAYO_DANIO = rset.getString("TAYO_DANIO");
					db_TAYO_OBSERVACIONES = rset.getString("TAYO_OBSERVACIONES");
					db_TAYO_ALTURATOTAL = rset.getString("TAYO_ALTURATOTAL");

					t = Auxiliar.traducir("General.Ver_Detalle", idioma, "Ver Detalle" + " ");
					opciones += "<div class='opcionmenu'><a class=boton href='Tallo?accion=detalle&TAYO_INDV_CONSECUTIVO="+TAYO_INDV_CONSECUTIVO+"&TAYO_CONSECUTIVO="+db_TAYO_CONSECUTIVO+"'>" + t + "</a></div>";
					
					tabla += "<span>";
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_CONSECUTIVO+":"+db_TAYO_CONSECUTIVO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_DAP1+":"+db_TAYO_DAP1+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_DAP2+":"+db_TAYO_DAP2+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_ALTURADAP+":"+db_TAYO_ALTURADAP+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_ALTURA+":"+db_TAYO_ALTURA+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_ALTURATOTAL+":"+db_TAYO_ALTURATOTAL+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_FORMAFUSTE+":"+db_TAYO_FORMAFUSTE+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_DANIO+":"+db_TAYO_DANIO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_TAYO_OBSERVACIONES+":"+db_TAYO_OBSERVACIONES+"</div>";
					tabla += "<div class='botones_resultado'>"+opciones+"</div>";
					tabla += "</div>";
					tabla += "</span>";

				}
				
				if (n == 0) {
					tabla += "<div class='resultado'>Este individuo aún no tiene tallos registrados.</div>";					
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
	 * Guarda un tallo
	 * 
	 * @param TAYO_CONSECUTIVO
	 * @param TAYO_INDV_CONSECUTIVO
	 * @param TAYO_DAP1
	 * @param TAYO_DAP2
	 * @param TAYO_ALTURADAP
	 * @param TAYO_ALTURA
	 * @param TAYO_ALTURATOTAL
	 * @param TAYO_FORMAFUSTE
	 * @param TAYO_DANIO
	 * @param TAYO_OBSERVACIONES
	 * @param TAYO_ID_IMPORTACION
	 * @param TAYO_ARCH_CONSECUTIVO
	 * @param request
	 * @param importacion
	 * @return String de resultado
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String guardar(
			String TAYO_CONSECUTIVO,
			String TAYO_INDV_CONSECUTIVO,
			String TAYO_DAP1,
			String TAYO_DAP2,
			String TAYO_ALTURADAP,
			String TAYO_ALTURA,
			String TAYO_ALTURATOTAL,
			String TAYO_FORMAFUSTE,
			String TAYO_DANIO,
			String TAYO_OBSERVACIONES,
			String TAYO_ID_IMPORTACION,
			String TAYO_ARCH_CONSECUTIVO,
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
		
		
		// VERIFICAR TAYO_INDV_CONSECUTIVO
		conteo = "0";
		sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO=" + TAYO_INDV_CONSECUTIVO;
		try {
			conteo = dbREDD.obtenerDato(sql_tmp, "0");
		} catch (Exception ex) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.TAYO_INDV_CONSECUTIVO", idioma, "Problemas al intentar determinar la existencia de TAYO_INDV_CONSECUTIVO [" + ex.toString() + "]:" + sql_tmp + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_INDV_CONSECUTIVO, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.TAYO_INDV_CONSECUTIVO", idioma, "TAYO_INDV_CONSECUTIVO no encontrado" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_INDV_CONSECUTIVO, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		
		// VALIDAR TAYO_DAP1
		TAYO_DAP1 = TAYO_DAP1.replace(",", ".");
		if (!Auxiliar.esNumeroPositivo(TAYO_DAP1)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.TAYO_DAP1", idioma, "TAYO_DAP1 debe ser numérico y positivo" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_DAP1, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VALIDAR TAYO_DAP2
		TAYO_DAP2 = TAYO_DAP2.replace(",", ".");
		if (!Auxiliar.esNumeroPositivo(TAYO_DAP2)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.TAYO_DAP2", idioma, "TAYO_DAP2 debe ser numérico y positivo" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_DAP2, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VALIDAR TAYO_ALTURADAP
		TAYO_ALTURADAP = TAYO_ALTURADAP.replace(",", ".");
		if (!Auxiliar.esNumeroPositivo(TAYO_ALTURADAP)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.TAYO_ALTURADAP", idioma, "TAYO_ALTURADAP debe ser numérica y positiva" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_ALTURADAP, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VALIDAR TAYO_ALTURA
		TAYO_ALTURA = TAYO_ALTURA.replace(",", ".");
		if (!Auxiliar.esNumeroPositivo(TAYO_ALTURA)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.TAYO_ALTURA", idioma, "TAYO_ALTURA debe ser numérica y positiva" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_ALTURA, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VALIDAR TAYO_ALTURATOTAL
		TAYO_ALTURATOTAL = TAYO_ALTURATOTAL.replace(",", ".");
		if (!Auxiliar.esNumeroPositivo(TAYO_ALTURATOTAL)) {
			t = Auxiliar.traducir("AVISO_VALIDACION.TAYO_ALTURATOTAL", idioma, "TAYO_ALTURATOTAL debe ser numérica y positiva" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_ALTURATOTAL, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}

		// VERIFICAR TAYO_FORMAFUSTE
		conteo = "0";
		sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_FORMAFUSTE WHERE FRFS_CONSECUTIVO=" + TAYO_FORMAFUSTE;
		try {
			conteo = dbREDD.obtenerDato(sql_tmp, "0");
		} catch (Exception ex) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.TAYO_FORMAFUSTE", idioma, "Problemas al intentar determinar la existencia de TAYO_FORMAFUSTE [" + ex.toString() + "]:" + sql_tmp + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_FORMAFUSTE, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
			t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.TAYO_FORMAFUSTE", idioma, "TAYO_FORMAFUSTE no encontrado" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_FORMAFUSTE, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}
		
		// VERIFICAR TAYO_DANIO
		if (Auxiliar.tieneAlgo(TAYO_DANIO)) {
			conteo = "0";
			sql_tmp = "SELECT COUNT(*) AS CONTEO FROM RED_DANO WHERE DANO_CONSECUTIVO=" + TAYO_DANIO;
			try {
				conteo = dbREDD.obtenerDato(sql_tmp, "0");
			} catch (Exception ex) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA_BD.TAYO_DANIO", idioma, "Problemas al intentar determinar la existencia de TAYO_DANIO [" + ex.toString() + "]:" + sql_tmp + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_DANIO, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
			if (conteo.equals("0") || !Auxiliar.esEntero(conteo)) {
				t = Auxiliar.traducir("AVISO_VALIDACION_EXISTENCIA.TAYO_DANIO", idioma, "TAYO_DANIO no encontrado" + "..");
				aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_DANIO, usuario, metodo);
				observaciones += aviso;
				parametros_ok = false;
			}
		}
		
		// VALIDAR TAYO_OBSERVACIONES
		TAYO_OBSERVACIONES = Auxiliar.limpiarTexto(TAYO_OBSERVACIONES);
		TAYO_OBSERVACIONES = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(TAYO_OBSERVACIONES, ""));
		if (TAYO_OBSERVACIONES.length() > 255) {
			t = Auxiliar.traducir("AVISO_VALIDACION.TAYO_OBSERVACIONES", idioma, "TAYO_OBSERVACIONES no puede exceder los 255 caracteres" + "..");
			aviso = Auxiliar.mensaje("advertencia", t + ":" + TAYO_OBSERVACIONES, usuario, metodo);
			observaciones += aviso;
			parametros_ok = false;
		}

		
		if (!parametros_ok) {
			dbREDD.desconectarse();
			return "0-=-"+observaciones;
		}
		

		// VERIFICAR PARAMETROS

		t = Auxiliar.traducir("TAYO_DAP1", idioma, "el diámetro a la altura del pecho" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_DAP1)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_DAP2", idioma, "el segundo diámetro a la altura del pecho" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_DAP2)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_ALTURADAP", idioma, "la primera altura del fuste" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_ALTURADAP)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_ALTURA", idioma, "la segunda altura del fuste" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_ALTURA)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_ALTURATOTAL", idioma, "la altura final del fuste" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_ALTURATOTAL)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_FORMAFUSTE", idioma, "la forma de fuste" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_FORMAFUSTE)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_DANIO", idioma, "el daño del árbol" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_DANIO)) return "0-=-" + pe + " " + t;
		t = Auxiliar.traducir("TAYO_OBSERVACIONES", idioma, "las observaciones" + " ");
		//if (!Auxiliar.tieneAlgo(TAYO_OBSERVACIONES)) return "0-=-" + pe + " " + t;

		if (!Auxiliar.tieneAlgo(TAYO_DAP1)) TAYO_DAP1 = "NULL";
		if (!Auxiliar.tieneAlgo(TAYO_DAP2)) TAYO_DAP2 = "NULL";
		if (!Auxiliar.tieneAlgo(TAYO_ALTURADAP)) TAYO_ALTURADAP = "NULL";
		if (!Auxiliar.tieneAlgo(TAYO_ALTURA)) TAYO_ALTURA = "NULL";
		if (!Auxiliar.tieneAlgo(TAYO_ALTURATOTAL)) TAYO_ALTURATOTAL = "NULL";
		if (!Auxiliar.tieneAlgo(TAYO_FORMAFUSTE)) TAYO_FORMAFUSTE = "NULL";
		if (!Auxiliar.tieneAlgo(TAYO_DANIO)) TAYO_DANIO = "NULL";
		
		// REALIZAR OPERACION
		
		String sql_guardar = "";
		
		String TAYO_CONSECUTIVO_NUEVO = "";
		
		try 
		{
			if (!Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
				TAYO_CONSECUTIVO_NUEVO = dbREDD.obtenerDato("SELECT RED_TALLO_SEQ.nextval FROM DUAL", "");
			}
			
			if (Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
				sql_guardar = "UPDATE RED_TALLO SET ";
				sql_guardar += "TAYO_DAP1=" + TAYO_DAP1 + ",";
				sql_guardar += "TAYO_DAP2=" + TAYO_DAP2 + ",";
				sql_guardar += "TAYO_ALTURADAP=" + TAYO_ALTURADAP + ",";
				sql_guardar += "TAYO_ALTURA=" + TAYO_ALTURA + ",";
				sql_guardar += "TAYO_ALTURATOTAL=" + TAYO_ALTURATOTAL + ",";
				sql_guardar += "TAYO_FORMAFUSTE=" + TAYO_FORMAFUSTE + ",";
				sql_guardar += "TAYO_DANIO=" + TAYO_DANIO + ",";
				sql_guardar += "TAYO_MODIFICADOR=" + Auxiliar.nzVacio(id_usuario, "NULL") + ",";
				if (Auxiliar.tieneAlgo(TAYO_ARCH_CONSECUTIVO)) sql_guardar += "TAYO_ARCH_CONSECUTIVO=" + TAYO_ARCH_CONSECUTIVO + ",";
				sql_guardar += "TAYO_OBSERVACIONES='" + TAYO_OBSERVACIONES + "'";
				sql_guardar += " WHERE TAYO_CONSECUTIVO=" + TAYO_CONSECUTIVO;
				update = true;
			}
			else {
				TAYO_CONSECUTIVO = TAYO_CONSECUTIVO_NUEVO;
				
				sql_guardar = "INSERT INTO RED_TALLO ";
				sql_guardar += "(";
				sql_guardar += "TAYO_CONSECUTIVO,";
				sql_guardar += "TAYO_INDV_CONSECUTIVO,";
				sql_guardar += "TAYO_DAP1,";
				sql_guardar += "TAYO_DAP2,";
				sql_guardar += "TAYO_ALTURADAP,";
				sql_guardar += "TAYO_ALTURA,";
				sql_guardar += "TAYO_ALTURATOTAL,";
				sql_guardar += "TAYO_FORMAFUSTE,";
				sql_guardar += "TAYO_DANIO,";
				sql_guardar += "TAYO_CREADOR,";
				sql_guardar += "TAYO_OBSERVACIONES,";
				sql_guardar += "TAYO_ARCH_CONSECUTIVO";
				sql_guardar += ") ";
				sql_guardar += " VALUES ";
				sql_guardar += "(";
				sql_guardar += "" + TAYO_CONSECUTIVO+ ",";
				sql_guardar += "" + TAYO_INDV_CONSECUTIVO+ ",";
				sql_guardar += "" + TAYO_DAP1 + ",";
				sql_guardar += "" + TAYO_DAP2 + ",";
				sql_guardar += "" + TAYO_ALTURADAP + ",";
				sql_guardar += "" + TAYO_ALTURA + ",";
				sql_guardar += "" + TAYO_ALTURATOTAL + ",";
				sql_guardar += "" + TAYO_FORMAFUSTE + ",";
				sql_guardar += "" + TAYO_DANIO + ",";
				sql_guardar += "" + Auxiliar.nzVacio(id_usuario, "NULL") + ",";
				sql_guardar += "'" + TAYO_OBSERVACIONES + "',";
				sql_guardar += "" + Auxiliar.nzVacio(TAYO_ARCH_CONSECUTIVO, "NULL") + "";
				sql_guardar += ")";
			}
				
			if (update) {
				id_creador = dbREDD.obtenerDato("SELECT TAYO_CREADOR FROM RED_TALLO WHERE TAYO_CONSECUTIVO="+TAYO_CONSECUTIVO, "");
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
				id_creador = dbREDD.obtenerDato("SELECT INDV_CREADOR FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+TAYO_INDV_CONSECUTIVO, "");
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
					return "0-=-" + Auxiliar.mensaje("error", "Se produjo un error al intentar guardar el individuo:" + dbREDD.ultimoError, usuario, metodo);									
				}
		        
		        boolean ok_geometria = false;

				try {
					if (ok_guardar) {

						if (update) {
							id_creador = dbREDD.obtenerDato("SELECT TAYO_CREADOR FROM RED_TALLO WHERE TAYO_CONSECUTIVO="+TAYO_CONSECUTIVO, "");
							if (id_usuario.equals(id_creador)) {
								sec.registrarTransaccion(request, 221, TAYO_CONSECUTIVO, sql_guardar, ok_guardar);
							}
							else {
								sec.registrarTransaccion(request, 222, TAYO_CONSECUTIVO, sql_guardar, ok_guardar);
							}
						}
						else {
							if (!importacion) {
								id_creador = dbREDD.obtenerDato("SELECT INDV_CREADOR FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+TAYO_INDV_CONSECUTIVO, "");
								if (id_usuario.equals(id_creador)) {
									sec.registrarTransaccion(request, 223, TAYO_CONSECUTIVO, sql_guardar, ok_guardar);
								}
								else {
									sec.registrarTransaccion(request, 224, TAYO_CONSECUTIVO, sql_guardar, ok_guardar);
								}
							}
						}
						
						dbREDD.cometerTransaccion();
						resultado = TAYO_CONSECUTIVO + "-=-" + Auxiliar.mensaje("confirmacion", "Registro "+TAYO_CONSECUTIVO+" guardado.", usuario, metodo);
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
	 * Eliminar un tallo
	 * 
	 * @param TAYO_CONSECUTIVO
	 * @return String con el resultado
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public String eliminar(String TAYO_CONSECUTIVO, HttpServletRequest request) 
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
		
		t = Auxiliar.traducir("TAYO_CONSECUTIVO", idioma, "el código del tallo" + " ");
		if (!Auxiliar.tieneAlgo(TAYO_CONSECUTIVO)) {
			t = Auxiliar.mensaje("advertencia", pe + " " + t, usuario, metodo);
			dbREDD.desconectarse();
			return "0-=-" + t; 
		}
		
		
		// REALIZAR OPERACION
		
		String sql_eliminar = "";
		
		try 
		{
			sql_eliminar = "DELETE FROM RED_TALLO WHERE TAYO_CONSECUTIVO="+TAYO_CONSECUTIVO;
			
			id_creador = dbREDD.obtenerDato("SELECT TAYO_CREADOR FROM RED_TALLO WHERE TAYO_CONSECUTIVO="+TAYO_CONSECUTIVO, "");
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
							sec.registrarTransaccion(request, 231, TAYO_CONSECUTIVO, sql_eliminar, ok_eliminar);
						}
						else {
							sec.registrarTransaccion(request, 232, TAYO_CONSECUTIVO, sql_eliminar, ok_eliminar);
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
	
	/**
	 * Genera y guarda en un archivo la plantilla para poder importar tallos
	 * 
	 * @param response
	 * @return boolean si pudo o no generar la plantilla
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public boolean generarPlantillaTallos(HttpServletResponse response, HttpSession session)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarPlantillaTallos";

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
			Sheet hoja = libro.createSheet("INDIVIDUO");
			
			Row fila_titulos = hoja.createRow(0);

			j=-1;
			j++;fila_titulos.createCell(j).setCellValue("ACCION");
			j++;fila_titulos.createCell(j).setCellValue("CONSECUTIVO");
			j++;fila_titulos.createCell(j).setCellValue("ID_IMPORTACION");
			j++;fila_titulos.createCell(j).setCellValue("INDV_CONSECUTIVO");
			j++;fila_titulos.createCell(j).setCellValue("DAP1");
			j++;fila_titulos.createCell(j).setCellValue("DAP2");
			j++;fila_titulos.createCell(j).setCellValue("ALTURADAP");
			j++;fila_titulos.createCell(j).setCellValue("ALTURA");
			j++;fila_titulos.createCell(j).setCellValue("ALTURATOTAL");
			j++;fila_titulos.createCell(j).setCellValue("FORMAFUSTE");
			j++;fila_titulos.createCell(j).setCellValue("DANIO");
			j++;fila_titulos.createCell(j).setCellValue("OBSERVACIONES");
			
			i++;
			
			Row fila_datos = hoja.createRow(i);

			String o = "";

			t = Auxiliar.traducir("CargueMasivo.Mensaje_Opciones", idioma, "Opciones:" + "..");

			j=-1;

			t = Auxiliar.traducir("CargueMasivo.Tallos.ACCION", idioma, "Poner ELIMINAR para eliminar el tallo existente. De lo contrario dejar vacío." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.CONSECUTIVO", idioma, "Entero positivo. Consecutivo del tallo" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.ID_IMPORTACION", idioma, "Entero positivo. Consecutivo de importación del tallo" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.INDIVIDUO", idioma, "Entero positivo. Obligatorio. Consecutivo del individuo al que pertenece el tallo" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.DAP1", idioma, "Número Decimal. Diámetro en centímetros a la altura del pecho (medición 1)" + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.DAP2", idioma, "Número Decimal. Diámetro en centímetros a la altura del pecho (medición 2)." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.ALTURADAP", idioma, "Número Decimal. Altura en metros al punto de medición del diámetro en centímetros a la altura del pecho." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.ALTURA", idioma, "Número Decimal. Altura en metros del tallo." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.ALTURATOTAL", idioma, "Número Decimal. Altura en metros del tallo hasta la copa." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.FORMAFUSTE", idioma, "Entero positivo. Obligatorio. Identificador de la forma del fuste." + "..");
			o = Auxiliar.cargarOpciones("SELECT FRFS_CONSECUTIVO, FRFS_DESCRIPCION FROM RED_FORMAFUSTE ORDER BY FRFS_DESCRIPCION", "FRFS_CONSECUTIVO", "FRFS_DESCRIPCION", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.DANIO", idioma, "Entero positivo. Obligatorio. Identificador del daño del fuste." + "..");
			o = Auxiliar.cargarOpciones("SELECT DANO_CONSECUTIVO, DANO_DESCRIPCION FROM RED_DANO ORDER BY DANO_DESCRIPCION", "DANO_CONSECUTIVO", "DANO_DESCRIPCION", "", "", false, false, true);
			j++;fila_datos.createCell(j).setCellValue(t+o);
			
			t = Auxiliar.traducir("CargueMasivo.Tallos.OBSERVACIONES", idioma, "Texto. Max 255 caracteres. Observaciones." + "..");
			j++;fila_datos.createCell(j).setCellValue(t);
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=Plantilla_Tallos_Diligenciar_y_Guardar_como_Archivo_de_Texto_Separado_por_Tabulaciones.xls");

			libro.write(response.getOutputStream());
			
			ok = true;
			
		} catch (Exception e) {
			t = Auxiliar.mensaje("error", "Ocurrió la siguiente excepción en generarPlantillaIndividuos(): " + e.toString(), usuario, metodo);
		}
		
		dbREDD.desconectarse();
		return ok;
	}

	
} //953
