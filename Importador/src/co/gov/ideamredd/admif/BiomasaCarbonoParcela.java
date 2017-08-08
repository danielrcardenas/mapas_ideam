// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.File;
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

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;


/** 
 * Permite administrar los registros de biomasa y carbono de las parcelas.
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com 
 *
 */
@SuppressWarnings("serial")
public class BiomasaCarbonoParcela extends HttpServlet {

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
	public BiomasaCarbonoParcela() {
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
		
		String db_BMCR_CONSECUTIVO = "";
		String db_BMCR_CONS_PARCELA = "";
		String db_BMCR_BIOMASA = "";
		String db_BMCR_CARBONO = "";
		String db_BMCR_AREABASALPROME = "";
		String db_BMCR_AREABASALTOTAL = "";
		String db_BMCR_VOLUMENTOTAL = "";
		String db_BMCR_FECHA_INICIO = "";
		String db_BMCR_CONS_METODOLOGI = "";
		String db_BMCR_CONS_ESTADOBIOM = "";
		String db_BMCR_TIPOGENERA = "";
		
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
			response.setContentType("text/html; charset=UTF-8");
		}
		else {
			if (accion.equals("ajax_encontrar")) {
				target = "/ajax_resultados.jsp";
				response.setContentType("text/html; charset=UTF-8");
				try {
					String resultado = ajax_encontrar(Auxiliar.nz(request.getParameter("USR_CONS_TIPOIDENT"), ""), Auxiliar.nz(request.getParameter("USR_ID"), ""), session);
					retorno = resultado;
				} catch (Exception e) {
					retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a ajax_encontrar(): " + e.toString(), usuario, metodo);
					e.printStackTrace();
				}
			}
			else {
				response.setContentType("text/html; charset=UTF-8");
				target = "/bmc_parcela.jsp?BMCR_CONS_PARCELA="+Auxiliar.nz(request.getParameter("BMCR_CONS_PARCELA"), "");
				
				String nuevo_consecutivo = "";
	
				db_BMCR_CONSECUTIVO = Auxiliar.nz(request.getParameter("BMCR_CONSECUTIVO"), "");
				db_BMCR_CONS_PARCELA = Auxiliar.nz(request.getParameter("BMCR_CONS_PARCELA"), "");
				db_BMCR_BIOMASA = Auxiliar.nz(request.getParameter("BMCR_BIOMASA"), db_BMCR_BIOMASA);
				db_BMCR_CARBONO = Auxiliar.nz(request.getParameter("BMCR_CARBONO"), db_BMCR_CARBONO);
				db_BMCR_AREABASALPROME = Auxiliar.nz(request.getParameter("BMCR_AREABASALPROME"), db_BMCR_AREABASALPROME);
				db_BMCR_AREABASALTOTAL = Auxiliar.nz(request.getParameter("BMCR_AREABASALTOTAL"), db_BMCR_AREABASALTOTAL);
				db_BMCR_VOLUMENTOTAL = Auxiliar.nz(request.getParameter("BMCR_VOLUMENTOTAL"), db_BMCR_VOLUMENTOTAL);
				db_BMCR_FECHA_INICIO = Auxiliar.nz(request.getParameter("BMCR_FECHA_INICIO"), db_BMCR_FECHA_INICIO);
				db_BMCR_CONS_METODOLOGI = Auxiliar.nz(request.getParameter("BMCR_CONS_METODOLOGI"), db_BMCR_CONS_METODOLOGI);
				db_BMCR_CONS_ESTADOBIOM = Auxiliar.nz(request.getParameter("BMCR_CONS_ESTADOBIOM"), db_BMCR_CONS_ESTADOBIOM);
				db_BMCR_TIPOGENERA = Auxiliar.nz(request.getParameter("BMCR_TIPOGENERA"), db_BMCR_TIPOGENERA);
				String calcular = Auxiliar.nz(request.getParameter("calcular"), "");
				
				if (Auxiliar.tieneAlgo(db_BMCR_CONS_PARCELA)) {
					if (accion.equals("guardar")) {
						response.setContentType("text/html; charset=UTF-8");
						try {
							if (
									sec.tienePermiso(usuario,  "24") 
									||	sec.tienePermiso(usuario,  "23")
									) {
								
								String resultado = guardar(
										db_BMCR_CONSECUTIVO, 
										db_BMCR_CONS_PARCELA, 
										db_BMCR_BIOMASA,
										db_BMCR_CARBONO,
										db_BMCR_AREABASALPROME,
										db_BMCR_AREABASALTOTAL,
										db_BMCR_VOLUMENTOTAL,
										db_BMCR_CONS_METODOLOGI, 
										db_BMCR_TIPOGENERA,
										db_BMCR_CONS_ESTADOBIOM,
										db_BMCR_FECHA_INICIO, 
										calcular,
										request
										);
								
								String [] a_resultado = resultado.split("-=-");
								nuevo_consecutivo = a_resultado[0];
								retorno = a_resultado[1];
							}
							else {
								retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
								sec.registrarTransaccion(request, 24, db_BMCR_CONSECUTIVO, "sin permisos para guardar", false);
								sec.registrarTransaccion(request, 23, db_BMCR_CONSECUTIVO, "sin permisos para guardar", false);
							}
						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a contactos: " + e.toString(), usuario, metodo);
							try {
								sec.registrarTransaccion(request, 24, db_BMCR_CONSECUTIVO, "excepcion para guardar:"+e.toString(), false);
								sec.registrarTransaccion(request, 23, db_BMCR_CONSECUTIVO, "excepcion para guardar:"+e.toString(), false);
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					else if (accion.equals("eliminar")) {
						response.setContentType("text/html; charset=UTF-8");
						try {
							if (
									sec.tienePermiso(usuario,  "24") 
									||	sec.tienePermiso(usuario,  "23")
									) {
								
								String resultado = eliminar(db_BMCR_CONSECUTIVO, request);
								
								String [] a_resultado = resultado.split("-=-");
								retorno = a_resultado[1];
							}
							else {
								retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
								sec.registrarTransaccion(request, 24, db_BMCR_CONSECUTIVO, "sin permisos para eliminar", false);
								sec.registrarTransaccion(request, 23, db_BMCR_CONSECUTIVO, "sin permisos para eliminar", false);
							}
						} catch (Exception e) {
							retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a contactos: " + e.toString(), usuario, metodo);
							try {
								sec.registrarTransaccion(request, 24, db_BMCR_CONSECUTIVO, "excepcion para eliminar:"+e.toString(), false);
								sec.registrarTransaccion(request, 23, db_BMCR_CONSECUTIVO, "excepcion para eliminar:"+e.toString(), false);
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					
					try {
						if (
								sec.tienePermiso(usuario,  "22") 
								||	sec.tienePermiso(usuario,  "21")
								) {
							//retorno = "";
							request = establecerAtributos(request, session, nuevo_consecutivo);
							request.setAttribute("BMCR_CONS_PARCELA", db_BMCR_CONS_PARCELA);

						}
						else {
							retorno = Auxiliar.mensaje("advertencia", "Carece de permisos para llevar a cabo esta acción", usuario, metodo);
							sec.registrarTransaccion(request, 21, db_BMCR_CONSECUTIVO, "sin permisos para consultar", false);
							sec.registrarTransaccion(request, 22, db_BMCR_CONSECUTIVO, "sin permisos para consultar", false);
						}
					} catch (Exception e) {
						retorno = Auxiliar.mensaje("error", "Excepción durante el llamado a contactos: " + e.toString(), usuario, metodo);
						try {
							sec.registrarTransaccion(request, 21, db_BMCR_CONSECUTIVO, "excepcion para consultar:"+e.toString(), false);
							sec.registrarTransaccion(request, 22, db_BMCR_CONSECUTIVO, "excepcion para consultar:"+e.toString(), false);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}

				}
				else {
					retorno = Auxiliar.mensaje("advertencia", "Código de parcela no especificado.", usuario, metodo);
				}
			}
		}

		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}

	
	/**
	 * Inicializa los parámetros del formulario
	 * 
	 * @param request
	 * @return request con parámetros inicializados
	 * @throws Exception 
	 */
	private HttpServletRequest establecerAtributos(HttpServletRequest request, HttpSession session, String nuevo_consecutivo) 
			throws Exception {
		String metodo = yo + "establecerAtributos";

		BD dbREDD = new BD();
		Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String BMCR_CONSECUTIVO = "";
	    
	    BMCR_CONSECUTIVO = Auxiliar.nz(request.getParameter("BMCR_CONSECUTIVO"), "");
	    
	    if (Auxiliar.tieneAlgo(nuevo_consecutivo)) BMCR_CONSECUTIVO = nuevo_consecutivo;

	    String db_BMCR_CONSECUTIVO = Auxiliar.nz(request.getParameter("BMCR_CONSECUTIVO"), "");
	    String db_BMCR_CONS_PARCELA = Auxiliar.nz(request.getParameter("BMCR_CONS_PARCELA"), "");

	    Sec sec = new Sec();
		String id_usuario = "";
		String id_creador = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		if (Auxiliar.tieneAlgo(db_BMCR_CONS_PARCELA)) {
			id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+db_BMCR_CONS_PARCELA+"", "");
		}
		if (!id_usuario.equals(id_creador)) {
			if (!sec.tienePermiso(id_usuario, "22")) {
				sec.registrarTransaccion(request, 22, BMCR_CONSECUTIVO, "permisos", false);
				return request;
			}
			else {
				sec.registrarTransaccion(request, 22, BMCR_CONSECUTIVO, "", true);
			}
		}
		else {
			sec.registrarTransaccion(request, 21, BMCR_CONSECUTIVO, "", true);
		}
		
	    String db_BMCR_BIOMASA = Auxiliar.nz(request.getParameter("BMCR_BIOMASA"), "");
	    String db_BMCR_CARBONO = Auxiliar.nz(request.getParameter("BMCR_CARBONO"), "");
	    String db_BMCR_AREABASALPROME = Auxiliar.nz(request.getParameter("BMCR_AREABASALPROME"), "");
	    String db_BMCR_AREABASALTOTAL = Auxiliar.nz(request.getParameter("BMCR_AREABASALTOTAL"), "");
	    String db_BMCR_VOLUMENTOTAL = Auxiliar.nz(request.getParameter("BMCR_VOLUMENTOTAL"), "");
	    String db_BMCR_FECHA_INICIO = Auxiliar.nz(request.getParameter("BMCR_FECHA_INICIO"), "");
	    String db_BMCR_CONS_METODOLOGI = Auxiliar.nz(request.getParameter("BMCR_CONS_METODOLOGI"), "");
	    String db_BMCR_CONS_ESTADOBIOM = Auxiliar.nz(request.getParameter("BMCR_CONS_ESTADOBIOM"), "");
	    String db_BMCR_TIPOGENERA = Auxiliar.nz(request.getParameter("BMCR_TIPOGENERA"), "");

		if (Auxiliar.tieneAlgo(BMCR_CONSECUTIVO)) {
			try {
				ResultSet rset = cargarRegistro(BMCR_CONSECUTIVO, session);
				
			    if (rset != null) {
					if (rset.next()) {
						db_BMCR_CONSECUTIVO = Auxiliar.nz(rset.getString("BMCR_CONSECUTIVO"), "");
						db_BMCR_CONS_PARCELA = Auxiliar.nz(rset.getString("BMCR_CONS_PARCELA"), "");
						db_BMCR_BIOMASA = Auxiliar.nz(rset.getString("BMCR_BIOMASA"), "");
						db_BMCR_CARBONO = Auxiliar.nz(rset.getString("BMCR_CARBONO"), "");
						db_BMCR_AREABASALPROME = Auxiliar.nz(rset.getString("BMCR_AREABASALPROME"), "");
						db_BMCR_AREABASALTOTAL = Auxiliar.nz(rset.getString("BMCR_AREABASALTOTAL"), "");
						db_BMCR_VOLUMENTOTAL = Auxiliar.nz(rset.getString("BMCR_VOLUMENTOTAL"), "");
						db_BMCR_FECHA_INICIO = Auxiliar.nz(rset.getString("BMCR_FECHA_INICIO"), "").replace(" 00:00:00.0", "");
						db_BMCR_CONS_METODOLOGI = Auxiliar.nz(rset.getString("BMCR_CONS_METODOLOGI"), "");
						db_BMCR_CONS_ESTADOBIOM = Auxiliar.nz(rset.getString("BMCR_CONS_ESTADOBIOM"), "");
						db_BMCR_TIPOGENERA = Auxiliar.nz(rset.getString("BMCR_TIPOGENERA"), "");
				
						rset.close();
					}
			    }
			}
		    catch (Exception e) {
		    	Auxiliar.mensaje("error", "Error al consutar el rset" , usuario, metodo);
		    }
		}
		
		request.setAttribute("BMCR_CONSECUTIVO", db_BMCR_CONSECUTIVO);
		request.setAttribute("BMCR_BIOMASA", db_BMCR_BIOMASA);
		request.setAttribute("BMCR_CARBONO", db_BMCR_CARBONO);
		request.setAttribute("BMCR_AREABASALPROME", db_BMCR_AREABASALPROME);
		request.setAttribute("BMCR_AREABASALTOTAL", db_BMCR_AREABASALTOTAL);
		request.setAttribute("BMCR_VOLUMENTOTAL", db_BMCR_VOLUMENTOTAL);
		request.setAttribute("BMCR_FECHA_INICIO", db_BMCR_FECHA_INICIO);
		request.setAttribute("BMCR_CONS_METODOLOGI", db_BMCR_CONS_METODOLOGI);
		request.setAttribute("BMCR_CONS_ESTADOBIOM", db_BMCR_CONS_ESTADOBIOM);
		request.setAttribute("BMCR_TIPOGENERA", db_BMCR_TIPOGENERA);
		
		request.setAttribute("idioma",idioma);

		request.setAttribute("opciones_metodologia",Auxiliar.cargarOpciones("SELECT MTDL_CONSECUTIVO, MTDL_NOMBRE FROM RED_METODOLOGIA ORDER BY MTDL_NOMBRE", "MTDL_CONSECUTIVO", "MTDL_NOMBRE", db_BMCR_CONS_METODOLOGI, "", false, true, false));
		request.setAttribute("opciones_tipogeneracion",Auxiliar.cargarOpciones("SELECT BCTG_CONSECUTIVO, BCTG_NOMBRE FROM RED_TIPOGENERACION ORDER BY BCTG_NOMBRE", "BCTG_CONSECUTIVO", "BCTG_NOMBRE", db_BMCR_TIPOGENERA, "", false, true, false));
		request.setAttribute("opciones_estadobiomasa",Auxiliar.cargarOpciones("SELECT ESBI_CONSECUTIVO, 'Verificado:' || ESBI_VERIFICADO || ' Atípico:' || ESBI_ATIPICO || ' Datos incluidos en cálculos:' || ESBI_INCLUIDOCALCULOS AS INFO FROM RED_ESTADOBIOMASA ORDER BY INFO", "ESBI_CONSECUTIVO", "INFO", db_BMCR_CONS_ESTADOBIOM, "", false, true, false));

		String[] a_registros = listarRegistros(db_BMCR_CONS_PARCELA, session);
		
		request.setAttribute("tabla_registros", a_registros[0]);
		request.setAttribute("n_registros", a_registros[1]);
		
		dbREDD.desconectarse();
		
		return request;
	}

	
	/**
	 * Lista los registros de biomasa y carbono de una parcela
	 * 
	 * @param BMCR_CONS_PARCELA
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public String [] listarRegistros(String BMCR_CONS_PARCELA, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "cargarBiomasasCarbonos";

		BD dbREDD = new BD();
		Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
		
	    String tabla = Auxiliar.traducir("General.No_se_encontraron_registros", idioma, "No se encontraron registros");

		String [] a_tabla = {tabla, "0"};

		if (!Auxiliar.tieneAlgo(BMCR_CONS_PARCELA)) {
			dbREDD.desconectarse();
			return a_tabla;
		}
		
		String sql = "SELECT ";
		sql += "BMCR.BMCR_CONSECUTIVO AS BMCR_CONSECUTIVO,";
		sql += "BMCR.BMCR_CONS_PARCELA AS BMCR_CONS_PARCELA,";
		sql += "BMCR.BMCR_FECHA_INICIO AS BMCR_FECHA_INICIO,";
		sql += "BMCR.BMCR_BIOMASA AS BMCR_BIOMASA,";
		sql += "BMCR.BMCR_CARBONO AS BMCR_CARBONO,";
		sql += "BMCR.BMCR_CONS_METODOLOGI AS BMCR_CONS_METODOLOGI,";
		sql += "BMCR.BMCR_TIPOGENERA AS BMCR_TIPOGENERA,";
		sql += "'Verificado:' || ESBI.ESBI_VERIFICADO || '; Atípico:' || ESBI.ESBI_ATIPICO || '; Datos incluidos en cálculos:' || ESBI.ESBI_INCLUIDOCALCULOS AS INFO";
		sql += " FROM RED_BIOMASAYCARBONO BMCR ";
		sql += " INNER JOIN RED_METODOLOGIA MDTL ON BMCR.BMCR_CONS_METODOLOGI=MDTL.MTDL_CONSECUTIVO ";
		sql += " INNER JOIN RED_TIPOGENERACION BCTG ON BMCR.BMCR_TIPOGENERA=BCTG.BCTG_CONSECUTIVO ";
		sql += " INNER JOIN RED_ESTADOBIOMASA ESBI ON BMCR.BMCR_CONS_ESTADOBIOM=ESBI.ESBI_CONSECUTIVO ";
		sql += " WHERE BMCR.BMCR_CONS_PARCELA = "+BMCR_CONS_PARCELA+" ";
		sql += " ORDER BY BMCR_FECHA_INICIO DESC ";
		
		int n = 0;
				
		try {
			ResultSet rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
				String db_BMCR_CONSECUTIVO = "";
				String db_BMCR_CONS_PARCELA = "";
				String db_BMCR_FECHA_INICIO = "";
				String db_BMCR_BIOMASA = "";
				String db_BMCR_CARBONO = "";
				String db_INFO = "";
				String db_BMCR_CONS_METODOLOGI = "";
				String db_BMCR_TIPOGENERA = "";
				
				String opciones = "";
				
				tabla = "<div id='contenedor-resultados' class='tabla_resultados'>";

				String t_BMCR_FECHA_INICIO = "";
				String t_BMCR_BIOMASA = "";
				String t_BMCR_CARBONO = "";
				String t_INFO = "";
				String t_BMCR_CONS_METODOLOGI = "";
				String t_BMCR_TIPOGENERA = "";

				t_BMCR_FECHA_INICIO = Auxiliar.traducir("BMCR_FECHA_INICIO", idioma, "Vigente desde" + ":");
				t_BMCR_BIOMASA = Auxiliar.traducir("BMCR_BIOMASA", idioma, "Biomasa(t)" + ":");
				t_BMCR_CARBONO = Auxiliar.traducir("BMCR_CARBONO", idioma, "Carbono(t)" + ":");
				t_INFO = Auxiliar.traducir("BMCR_ESTADO", idioma, "Estado" + ":");
				t_BMCR_CONS_METODOLOGI = Auxiliar.traducir("BMCR_CONS_METODOLOGI", idioma, "Metodología" + ":");
				t_BMCR_TIPOGENERA = Auxiliar.traducir("BMCR_TIPOGENERA", idioma, "Tipo de Generación" + ":");
				t = Auxiliar.traducir("General.Opciones", idioma, "Opciones" + ":");
				
				while (rset.next())
				{
					opciones = "";
					
					db_BMCR_CONS_PARCELA = rset.getString("BMCR_CONS_PARCELA");
					db_BMCR_CONSECUTIVO = rset.getString("BMCR_CONSECUTIVO");
					db_BMCR_FECHA_INICIO = Auxiliar.nz(rset.getString("BMCR_FECHA_INICIO"), "").replace(" 00:00:00.0", "");
					db_BMCR_BIOMASA = rset.getString("BMCR_BIOMASA");
					db_BMCR_CARBONO = rset.getString("BMCR_CARBONO");
					db_BMCR_CONS_METODOLOGI = rset.getString("BMCR_CONS_METODOLOGI");
					String metodologia = "";
					if (Auxiliar.tieneAlgo(db_BMCR_CONS_METODOLOGI)) {
						metodologia = dbREDD.obtenerDato("SELECT MTDL_NOMBRE FROM RED_METODOLOGIA WHERE MTDL_CONSECUTIVO="+db_BMCR_CONS_METODOLOGI, "");
					}
					db_BMCR_TIPOGENERA = rset.getString("BMCR_TIPOGENERA");
					String tipogeneracion = "";
					if (Auxiliar.tieneAlgo(db_BMCR_TIPOGENERA)) {
						tipogeneracion = dbREDD.obtenerDato("SELECT BCTG_NOMBRE FROM RED_TIPOGENERACION WHERE BCTG_CONSECUTIVO="+db_BMCR_TIPOGENERA, "");
					}
					db_INFO = rset.getString("INFO");
					db_INFO = db_INFO.replace("0", "No");
					db_INFO = db_INFO.replace("1", "Sí");
					
					t = Auxiliar.traducir("General.Editar", idioma, "Editar" + ":");
					opciones += "<div class='opcionmenu'><a class=boton href='BiomasaCarbonoParcela?accion=detalle&BMCR_CONSECUTIVO="+db_BMCR_CONSECUTIVO+"&BMCR_CONS_PARCELA="+db_BMCR_CONS_PARCELA+"' >" + t + "</a></div>";
					
					t = Auxiliar.traducir("General.Eliminar", idioma, "Eliminar" + ":");
					opciones += "<div class='opcionmenu'><a class=boton href='BiomasaCarbonoParcela?accion=eliminar&BMCR_CONSECUTIVO="+db_BMCR_CONSECUTIVO+"&BMCR_CONS_PARCELA="+db_BMCR_CONS_PARCELA+"' >" + t + "</a></div>";
					
					tabla += "<span>";
					tabla += "<div class='resultado'>";
					tabla += "<div class='dato_resultado'>"+t_BMCR_FECHA_INICIO+":"+db_BMCR_FECHA_INICIO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_BMCR_BIOMASA+":"+db_BMCR_BIOMASA+"</div>";
					tabla += "<div class='dato_resultado'>"+t_BMCR_CARBONO+":"+db_BMCR_CARBONO+"</div>";
					tabla += "<div class='dato_resultado'>"+t_BMCR_CONS_METODOLOGI+":"+metodologia+"</div>";
					tabla += "<div class='dato_resultado'>"+t_BMCR_TIPOGENERA+":"+tipogeneracion+"</div>";
					tabla += "<div class='dato_resultado'>"+t_INFO+":"+db_INFO+"</div>";
					tabla += "<div class='dato_resultado'>"+opciones+"</div>";
					tabla += "</div>";
					tabla += "</span>";
					
					n++;

				}
				
				tabla += "</div>";
						
				a_tabla[0] = tabla;
				a_tabla[1] = String.valueOf(n);
				
				rset.close();
			}
			else 
			{
				System.out.println("No pude consultar registros de biomasa carbono:" + dbREDD.ultimoError);
			}
		} catch (SQLException e) {
		} catch (Exception e) {
		}
		
		dbREDD.desconectarse();
		return a_tabla;
	}
	

	/**
	 * Encuentra un usuario en la base de datos por tipo y número de documento 
	 * 
	 * @param USR_CONS_TIPOIDENT
	 * @param USR_ID
	 * @param session
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String ajax_encontrar(
			String USR_CONS_TIPOIDENT,
			String USR_ID, 
			HttpSession session
			)
	throws ClassNotFoundException, Exception {
		String metodo = yo + "generarStrIndividuos";

		BD dbREDD = new BD();
		Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    String r = "";
		
		if (!Auxiliar.tieneAlgo(USR_CONS_TIPOIDENT)) {
			t = Auxiliar.traducir(yo+"Por_favor_especifique_tipo_documento", idioma, "Por favor especifique el tipo de documento" + ":");
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}
		
		if (!Auxiliar.tieneAlgo(USR_ID)) {
			t = Auxiliar.traducir(yo+"Por_favor_especifique_numero_documento", idioma, "Por favor especifique el número de documento" + ":");
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}
		
		String sql = "SELECT DISTINCT";
		sql += " USR_CONSECUTIVO,";
		sql += " USR_NOMBRE";
		sql += " FROM RED_USUARIO ";
		sql += " WHERE USR_CONS_TIPOIDENT="+USR_CONS_TIPOIDENT+" AND USR_ID="+USR_ID+" ";
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
	 * Carga registro de biomasa y carbono
	 * 
	 * @param BMCR_CONS_PARCELA
	 * @param BMCR_FECHA_INICIO
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	private ResultSet cargarRegistro(String BMCR_CONSECUTIVO, HttpSession session) throws ClassNotFoundException, Exception {
		String metodo = yo + "cargarBiomasaCarbono";

		BD dbREDD = new BD();
		Auxiliar aux = new Auxiliar();
	    String t = "";
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    ResultSet rset = null;
	    
		if (!Auxiliar.tieneAlgo(BMCR_CONSECUTIVO)) {
			dbREDD.desconectarse();
			return null;
		}
		
		String sql = "SELECT * FROM RED_BIOMASAYCARBONO WHERE BMCR_CONSECUTIVO=" + BMCR_CONSECUTIVO;
		
		try {
			rset = dbREDD.consultarBD(sql);
			
			if (rset != null)
			{
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
	 * Guarda registro de biomasa y carbono
	 * 
	 * 
	 * @param BMCR_CONS_PARCELA
	 * @param BMCR_BIOMASA
	 * @param BMCR_CARBONO
	 * @param BMCR_AREABASALPROME
	 * @param BMCR_AREABASALTOTAL
	 * @param BMCR_VOLUMENTOTAL
	 * @param BMCR_CONS_METODOLOGI
	 * @param BMCR_TIPOGENERA
	 * @param BMCR_CONS_ESTADOBIOM
	 * @param BMCR_FECHA_INICIO
	 * @param BMCR_FECHA_FIN
	 * @return String con resultado de la operación
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	private String guardar(
			String BMCR_CONSECUTIVO, 
			String BMCR_CONS_PARCELA, 
			String BMCR_BIOMASA,
			String BMCR_CARBONO,
			String BMCR_AREABASALPROME,
			String BMCR_AREABASALTOTAL,
			String BMCR_VOLUMENTOTAL,
			String BMCR_CONS_METODOLOGI, 
			String BMCR_TIPOGENERA,
			String BMCR_CONS_ESTADOBIOM,
			String BMCR_FECHA_INICIO,
			String calcular,
			HttpServletRequest request
			) throws ClassNotFoundException, Exception {
		String metodo = yo + "guardar";

		Auxiliar aux = new Auxiliar();
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

		BD dbREDD = new BD();
		Codec ORACLE_CODEC = new OracleCodec();

		String r = "";
		String sql = "";
		
		boolean ok_guardar = false;
		boolean update = false;
		boolean privilegio = false;

		BMCR_CONS_PARCELA = Auxiliar.nz(BMCR_CONS_PARCELA, "");
		BMCR_FECHA_INICIO = Auxiliar.nz(BMCR_FECHA_INICIO, "");
		
		if (BMCR_CONS_PARCELA.equals("")) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Faltó especificar el consecutivo de la parcela");
		}
		
		Sec sec = new Sec();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		String id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+BMCR_CONS_PARCELA+"", "");
		if (id_usuario.equals(id_creador)) {
			if (sec.tienePermiso(id_usuario, "23") || sec.tienePermiso(id_usuario, "24")) {
				privilegio = true;
			}
		}
		else {
			if (sec.tienePermiso(id_usuario, "24")) {
				privilegio = true;
			}			
		}
		
		if (!privilegio) {
			dbREDD.desconectarse();
        	return "0-=-" + Auxiliar.mensaje("advertencia", "No tiene privilegio para realizar esta operación. Por favor, contacte al administrador.", usuario, metodo);
		}


		BMCR_FECHA_INICIO = Auxiliar.limpiarTexto(BMCR_FECHA_INICIO);
		BMCR_FECHA_INICIO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(BMCR_FECHA_INICIO, ""));
		if (Auxiliar.tieneAlgo(BMCR_FECHA_INICIO)) {
			BMCR_FECHA_INICIO = " TO_DATE('" + BMCR_FECHA_INICIO.replace(" 00:00:00.0", "") + "', 'YYYY-MM-DD')";
		}
		
		if (!Auxiliar.tieneAlgo(BMCR_CONS_METODOLOGI)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Faltó especificar la metodología");
		}
		
		if (!Auxiliar.tieneAlgo(BMCR_TIPOGENERA)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Faltó especificar el tipo de generación");
		}
		
		if (!Auxiliar.tieneAlgo(BMCR_CONSECUTIVO)) {
			BMCR_CONSECUTIVO = dbREDD.obtenerDato("SELECT RED_BIOMASAYCARBONO_SEQ.nextval FROM DUAL", "");
		}
		
		if (!Auxiliar.tieneAlgo(BMCR_CONSECUTIVO)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Falta el consecutivo.");
		}
		
		Archivo archie = new Archivo();
		
		if (calcular.equals("1")) {
			if (BMCR_TIPOGENERA.equals("2")) {
				String carpeta = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_CalculosBMC'", "");
				if (!Auxiliar.tieneAlgo(carpeta) || !archie.existeArchivo(carpeta)) {
					carpeta = getServletContext().getRealPath("") + File.separator + "CalculosBMC";
				}

				String ruta_parcela = carpeta + File.separator + BMCR_CONS_PARCELA;
				
				String [] a_bmc = new String[3];
				
				BMC bemece = new BMC();
				
				a_bmc = bemece.bmcParcelaBD_R(BMCR_CONS_PARCELA, Auxiliar.nz(BMCR_CONS_METODOLOGI, ""), ruta_parcela, BMCR_CONSECUTIVO);
				
				String BA = a_bmc[1];
				
				if (Auxiliar.tieneAlgo(BA)) {
					BMCR_BIOMASA = BA;
					double carbono = Double.parseDouble(BA)/2.0;
					BMCR_CARBONO = String.valueOf(carbono);
					BMCR_FECHA_INICIO = "SYSDATE";
					r += a_bmc[2];
					sec.registrarTransaccion(request, 217, BMCR_CONSECUTIVO, BA, true);
				}
				else {
					sec.registrarTransaccion(request, 217, BMCR_CONSECUTIVO, Auxiliar.nzVacio(a_bmc[2], "error"), false);
				}
			}
		}

		if (!Auxiliar.tieneAlgo(BMCR_FECHA_INICIO)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Si no desea calcular la biomasa, falta especificar también la fecha de inicio");
		}
		
		BMCR_FECHA_INICIO = BMCR_FECHA_INICIO.replace(".0", "");
		
		BMCR_BIOMASA = Auxiliar.limpiarTexto(BMCR_BIOMASA);
		BMCR_BIOMASA = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(BMCR_BIOMASA, ""));
		if (!Auxiliar.tieneAlgo(BMCR_BIOMASA)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Faltó especificar la biomasa");
		}
		
		BMCR_CARBONO = Auxiliar.limpiarTexto(BMCR_CARBONO);
		BMCR_CARBONO = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(BMCR_CARBONO, ""));
		if (!Auxiliar.tieneAlgo(BMCR_CARBONO)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Faltó especificar el carbono");
		}
		
		if (!Auxiliar.tieneAlgo(BMCR_CONS_ESTADOBIOM)) {
			dbREDD.desconectarse();
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "Faltó especificar el estado");
		}

		BMCR_AREABASALPROME = Auxiliar.limpiarTexto(BMCR_AREABASALPROME);
		BMCR_AREABASALPROME = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(BMCR_AREABASALPROME, ""));
		BMCR_AREABASALTOTAL = Auxiliar.limpiarTexto(BMCR_AREABASALTOTAL);
		BMCR_AREABASALTOTAL = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(BMCR_AREABASALTOTAL, ""));
		BMCR_VOLUMENTOTAL = Auxiliar.limpiarTexto(BMCR_VOLUMENTOTAL);
		BMCR_VOLUMENTOTAL = ESAPI.encoder().encodeForSQL( ORACLE_CODEC, Auxiliar.nzObjStr(BMCR_VOLUMENTOTAL, ""));

		String existente = dbREDD.obtenerDato("SELECT COUNT(*) AS CONTEO FROM RED_BIOMASAYCARBONO WHERE BMCR_CONSECUTIVO="+BMCR_CONSECUTIVO, "0");
		
		if (existente.equals("0"))
		{
			sql = "INSERT INTO RED_BIOMASAYCARBONO ( ";
			sql += " BMCR_CONSECUTIVO,";
			sql += " BMCR_CONS_PARCELA,";
			sql += " BMCR_BIOMASA,";
			sql += " BMCR_CARBONO,";
			sql += " BMCR_AREABASALPROME,";
			sql += " BMCR_AREABASALTOTAL,";
			sql += " BMCR_VOLUMENTOTAL,";
			sql += " BMCR_CONS_METODOLOGI,";
			sql += " BMCR_TIPOGENERA,";
			sql += " BMCR_CONS_ESTADOBIOM,";
			sql += " BMCR_FECHA_INICIO";
			sql += " ) VALUES ( ";
			sql += " "+BMCR_CONSECUTIVO+",";
			sql += " "+BMCR_CONS_PARCELA+",";
			sql += " "+BMCR_BIOMASA+",";
			sql += " "+BMCR_CARBONO+",";
			sql += " "+Auxiliar.nzVacio(BMCR_AREABASALPROME, "NULL")+",";
			sql += " "+Auxiliar.nzVacio(BMCR_AREABASALTOTAL, "NULL")+",";
			sql += " "+Auxiliar.nzVacio(BMCR_VOLUMENTOTAL, "NULL")+",";
			sql += " "+BMCR_CONS_METODOLOGI+",";
			sql += " "+BMCR_TIPOGENERA+",";
			sql += " "+BMCR_CONS_ESTADOBIOM+",";
			sql += BMCR_FECHA_INICIO;
			sql += " )";
		}
		else {
			sql = "UPDATE RED_BIOMASAYCARBONO SET ";
			sql += " BMCR_CONS_PARCELA="+BMCR_CONS_PARCELA+",";
			sql += " BMCR_BIOMASA="+BMCR_BIOMASA+",";
			sql += " BMCR_CARBONO="+BMCR_CARBONO+",";
			sql += " BMCR_AREABASALPROME="+BMCR_AREABASALPROME+",";
			sql += " BMCR_AREABASALTOTAL="+BMCR_AREABASALTOTAL+",";
			sql += " BMCR_VOLUMENTOTAL="+BMCR_VOLUMENTOTAL+",";
			sql += " BMCR_CONS_METODOLOGI="+BMCR_CONS_METODOLOGI+",";
			sql += " BMCR_TIPOGENERA="+BMCR_TIPOGENERA+",";
			sql += " BMCR_CONS_ESTADOBIOM="+BMCR_CONS_ESTADOBIOM+",";
			sql += " BMCR_FECHA_INICIO="+BMCR_FECHA_INICIO;
			sql += " WHERE BMCR_CONSECUTIVO="+BMCR_CONSECUTIVO;
			
			update = true;
		}
		
		try {
			ok_guardar = dbREDD.ejecutarSQL(sql);
			
			if (ok_guardar) {
				if (update) {
					if (id_usuario.equals(id_creador)) {
						sec.registrarTransaccion(request, 23, BMCR_CONSECUTIVO, sql, ok_guardar);
					}
					else {
						sec.registrarTransaccion(request, 24, BMCR_CONSECUTIVO, sql, ok_guardar);
					}
				}
				else {
					sec.registrarTransaccion(request, 23, "", sql, ok_guardar);
				}

				r = "1-=-" + r + Auxiliar.mensaje("confirmacion", "Biomasa y carbono registrados.", usuario, metodo);
			}
			else {
				String aviso_error = dbREDD.ultimoError;
				if (dbREDD.ultimoError.indexOf("ORA-00001") > -1) {
					aviso_error = "No se pudo guardar el registro puesto que ya existe uno con los mismos datos.";
				}
				else {
					aviso_error = "Surgió un error al guardar el registro:" + dbREDD.ultimoError;
				}
				r = "0-=-" + r + Auxiliar.mensaje("error", aviso_error, usuario, metodo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();
		
		return r;
	}
	

	/**
	 * Elimina registro de biomasa y carbono
	 * 
	 * @param BMCR_CONS_PARCELA
	 * @param BMCR_FECHA_INICIO
	 * @return String resultado de la operación
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	private String eliminar(String BMCR_CONSECUTIVO, HttpServletRequest request) 
			throws ClassNotFoundException, Exception {
		String metodo = yo + "eliminar";

		BD dbREDD = new BD();
		Auxiliar aux = new Auxiliar();
	    String t = "";
		HttpSession session = request.getSession();
		String usuario = "";
		try { synchronized (session) { usuario = Auxiliar.nz(session.getAttribute("usuario").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }
	    String idioma = "";
	    try { synchronized (session) { idioma = Auxiliar.nz(session.getAttribute("idioma").toString(), ""); } } catch (Exception e) { Auxiliar.mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString()); }

	    boolean ok_eliminar = false;
	    
	    String BMCR_CONS_PARCELA = "";
	    if (Auxiliar.tieneAlgo(BMCR_CONSECUTIVO)) {
	    	BMCR_CONS_PARCELA = dbREDD.obtenerDato("SELECT BMCR_CONS_PARCELA FROM RED_BIOMASAYCARBONO WHERE BMCR_CONSECUTIVO=" + BMCR_CONSECUTIVO, "");
	    }

	    if (!Auxiliar.tieneAlgo(BMCR_CONS_PARCELA)) {
			return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "No se pudo determinar la parcela");
	    }
	    
		Sec sec = new Sec();
		String id_usuario = "";
		if (Auxiliar.tieneAlgo(usuario)) id_usuario = dbREDD.obtenerDato("SELECT USR_CONSECUTIVO FROM RED_USUARIO WHERE USR_LOGIN='"+usuario+"'", "");
		String id_creador = dbREDD.obtenerDato("SELECT PRCL_CREADOR FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+BMCR_CONS_PARCELA+"", "");
		if (!id_usuario.equals(id_creador)) {
			if (!sec.tienePermiso(id_usuario, "24")) {
				return "0-=-" + Auxiliar.mensajeImpersonal("advertencia", "El usuario " + usuario + " no tiene permiso para eliminar la biomasa de parcelas que no creó");
			}
		}
	    
		String r = "0-=-No se pudo eliminar el contacto";
		String sql = "";
		
		sql = "DELETE FROM RED_BIOMASAYCARBONO WHERE BMCR_CONSECUTIVO="+BMCR_CONSECUTIVO;
		
		try {
			if (dbREDD.ejecutarSQL(sql)) {
				r = "1-=-" + Auxiliar.mensaje("confirmacion", "Registro de biomasa y carbono eliminado.", usuario, metodo);
				ok_eliminar = true;
			}
			else {
				r = "0-=-" + Auxiliar.mensaje("error", "El registro de biomasa y carbono no se pudo eliminar.", usuario, metodo);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbREDD.desconectarse();

		if (id_usuario.equals(id_creador)) {
			sec.registrarTransaccion(request, 23, BMCR_CONSECUTIVO, sql, ok_eliminar);
		}
		else {
			sec.registrarTransaccion(request, 24, BMCR_CONSECUTIVO, sql, ok_eliminar);
		}
		
		return r;
	}
	
	
	
} //953
