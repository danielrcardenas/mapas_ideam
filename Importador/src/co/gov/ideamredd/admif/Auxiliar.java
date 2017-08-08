// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import co.gov.ideamredd.admif.Commando;
import co.gov.ideamredd.lenguaje.LenguajeI18N;

/**
 * Clase Auxiliar con métodos que todas las demás clases puedan utilizar
 * 
 * @author Santiago Hernández Plazas, santiago.h.plazas@gmail.com
 * 
 */
@SuppressWarnings("serial") public class Auxiliar extends HttpServlet {

	public static String		yo					= "Individuo.";
	public static String		charset				= "ISO-8859-1";
	public static String		css					= "css/estilos.css";

	public static String		depuracion			= "";

	public static final String	DATE_FORMAT_NOW		= "yyyy-MM-dd HH:mm:ss";

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
	 * Método que traduce a post el get si recibió un request get
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public static ResourceBundle setLenguaje(String p_idioma, LenguajeI18N L) {
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String metodo = yo + "doPost";

		String usuario_recibido = "";
		String usuario = "";
		String idioma = "";
		String clave = "";

		String retorno = "";
		String target = "";

		// Instanciar auxiliar y archivo
		Sec sec = new Sec();

		// Obtener parámetro de acción para saber lo que quiere el usuario
		String accion = request.getParameter("accion");

		HttpSession session = request.getSession();

		if (session != null) {
			if (sec.sesionVigente(session)) {
				try {
					synchronized (session) {
						if (sec.sesionTieneAtributo(session, "idioma")) {
							idioma = nzObjStr(session.getAttribute("idioma").toString(), "");
						}
						if (sec.sesionTieneAtributo(session, "usuario")) {
							usuario = nzObjStr(session.getAttribute("usuario").toString(), "");
						}
					}
				}
				catch (Exception e) {
					mensajeImpersonal("error", "Error de sesión: " + e.toString());
				}
			}
		}
		else {
			accion = null;
		}

		if (!nz(request.getParameter("idioma"), "").equals("")) {
			idioma = nz(request.getParameter("idioma"), "");
		}

		if (!nz(request.getParameter("usuario"), "").equals("")) {
			usuario_recibido = nz(request.getParameter("usuario"), "");

			if (!nz(request.getParameter("clave"), "").equals("")) {
				clave = nz(request.getParameter("clave"), "");
			}

			if (tieneAlgo(clave)) {
				usuario = sec.autenticarUsuario(usuario_recibido, clave, request);
			}
			else {
				if (encriptar_usuario.equals("1")) {
					usuario = desencriptar(usuario_recibido, llave_encripcion);
				}
				else {
					usuario = usuario_recibido;
				}
			}
		}

		if (accion != null) {
			if (!accion.equals("salir")) {
				if (tieneAlgo(idioma)) {
					try {
						synchronized (session) {
							session.setAttribute("idioma", idioma);
						}
					}
					catch (Exception e) {
						mensajeImpersonal("error", "Error de sesión: " + e.toString());
					}
				}
				request.setAttribute("idioma", idioma);

				if (!tieneAlgo(usuario)) {
					// accion = null;
				}
				else {
					try {
						synchronized (session) {
							session.setAttribute("usuario", usuario);
						}
					}
					catch (Exception e) {
						mensajeImpersonal("error", "Error de sesión: " + e.toString());
					}
				}
			}
		}

		request.setAttribute("datos_sesion", mensajeImpersonal("nota", "Usuario: " + nz(usuario, "") + ", Idioma: " + nz(idioma, "")));

		if (accion == null) {
			target = "/menu.jsp";
		}
		else if (accion.equals("ajax_departamentos")) {
			target = "/ajax_resultados.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				String resultado = opcionesDepartamentos(nz(request.getParameter("PRCL_CONS_PAIS"), ""), nz(request.getParameter("opcionVacia"), ""), session);
				retorno = resultado;
			}
			catch (Exception e) {
				retorno = depuracion + mensaje("error", "Excepción durante el llamado a opcionesDepartamentos(): " + e.toString(), usuario, metodo);
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
			return;
		}
		else if (accion.equals("ajax_municipios")) {
			target = "/ajax_resultados.jsp";
			response.setContentType("text/html; charset=UTF-8");
			try {
				String resultado = opcionesMunicipios(nz(request.getParameter("departamentos_seleccionados"), ""), nz(request.getParameter("opcionVacia"), ""), session);
				retorno = resultado;
			}
			catch (Exception e) {
				retorno = depuracion + mensaje("error", "Excepción durante el llamado a opcionesMunicipios(): " + e.toString(), usuario, metodo);
				e.printStackTrace();
			}
			request.setAttribute("retorno", retorno);
			ServletContext context = getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher(target);
			dispatcher.forward(request, response);
			return;
		}
		else {
			target = "/error.jsp";
			retorno = "No se encontró la información solicitada para la acción " + accion;
		}

		request.setAttribute("retorno", retorno);
		ServletContext context = getServletContext();
		response.setContentType("text/html; charset=UTF-8");
		RequestDispatcher dispatcher = context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}

	/**
	 * Metodo para retornar string de opciones valor@@texto para un select de departamentos.
	 * 
	 * @param id_pais
	 * @return String r con el resultado
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public static String opcionesDepartamentos(String id_pais, String str_opcionVacia, HttpSession session) throws ClassNotFoundException, Exception {
		BD dbREDD = new BD();
		String t = "";
		LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try {
			synchronized (session) {
				usuario = nz(session.getAttribute("usuario").toString(), "");
			}
		}
		catch (Exception e) {
			mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString());
		}
		String idioma = "";
		try {
			synchronized (session) {
				idioma = nz(session.getAttribute("idioma").toString(), "");
			}
		}
		catch (Exception e) {
			mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString());
		}
		ResourceBundle msj = setLenguaje(idioma, L);

		String r = "";

		if (!tieneAlgo(id_pais)) {
			try {
				t = msj.getString(yo + "Por_favor_especifique_algun_pais");
			}
			catch (MissingResourceException e) {
				t = "Por favor especifique algún país" + "..";
			}
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}

		boolean opcionVacia = false;
		if (str_opcionVacia.equals("true")) {
			opcionVacia = true;
		}

		String sql = "SELECT ";
		sql += " CODIGO, NOMBRE";
		sql += " FROM RED_DEPTOS_SHAPE ";
		sql += " WHERE PAIS = '" + id_pais + "' ";
		sql += " ORDER BY NOMBRE ";

		try {
			r = cargarOpciones(sql, "CODIGO", "NOMBRE", "", "", true, opcionVacia, false);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (dbREDD != null) dbREDD.desconectarse();
		return r;
	}

	/**
	 * Metodo para retornar string de opciones valor@@texto para un select de municipios.
	 * 
	 * @param id_pais
	 * @return String r con el resultado
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public static String opcionesMunicipios(String departamentos_seleccionados, String str_opcionVacia, HttpSession session) throws ClassNotFoundException, Exception {
		BD dbREDD = new BD();
		String t = "";
		LenguajeI18N L = new LenguajeI18N();
		String usuario = "";
		try {
			synchronized (session) {
				usuario = nz(session.getAttribute("usuario").toString(), "");
			}
		}
		catch (Exception e) {
			mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString());
		}
		String idioma = "";
		try {
			synchronized (session) {
				idioma = nz(session.getAttribute("idioma").toString(), "");
			}
		}
		catch (Exception e) {
			mensajeImpersonal("error", "Error de sesión reenviada: " + e.toString());
		}
		ResourceBundle msj = setLenguaje(idioma, L);

		String r = "";

		if (!tieneAlgo(departamentos_seleccionados)) {
			try {
				t = msj.getString(yo + "Por_favor_especifique_algun_departamento");
			}
			catch (MissingResourceException e) {
				t = "Por favor especifique algún departamento" + "..";
			}
			dbREDD.desconectarse();
			return "@@" + t + "...";
		}

		boolean opcionVacia = false;
		if (str_opcionVacia.equals("true")) {
			opcionVacia = true;
		}

		String sql = "SELECT ";
		sql += " CODIGO, NOMBRE";
		sql += " FROM RED_MUNICIPIOS_SHAPE ";
		sql += " WHERE DEPARTAMEN IN (" + departamentos_seleccionados + ")";
		sql += " ORDER BY NOMBRE ";

		try {
			r = cargarOpciones(sql, "CODIGO", "NOMBRE", "", "", true, opcionVacia, false);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (dbREDD != null) dbREDD.desconectarse();
		return r;
	}

	/**
	 * Implota un arreglo a un string
	 * 
	 * Devuelve un string concatenando los elementos del arreglo a, separados por separador
	 * 
	 * @param a
	 * @param separador
	 * @return String del arreglo
	 */
	public static String implotarArregloString(String[] a, String separador) {
		String r = "";

		if (a == null) return "";

		int i = 0;

		for (i = 0; i < a.length; i++) {
			r += a[i] + separador;
		}

		if (r.length() > 0) {
			r = r.substring(0, r.length() - 1);
		}

		return r;
	}

	/**
	 * Retorna un mensaje dentro de tags
	 * <p>
	 * 
	 * @param mensaje
	 * @return String
	 *         <p>
	 *         mensaje
	 *         </p>
	 */
	public static String mensaje(String clase, String texto, String usuario, String metodo) {
		// System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		mensajeLog(clase, "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		return "<p class=\"" + clase + "\">" + texto + "</p>";
	}

	/**
	 * Retorna un mensaje dentro de tags
	 * <p>
	 * incluyendo un evento de ejecución de javascript
	 * 
	 * @param mensaje
	 * @return String
	 *         <p>
	 *         mensaje
	 *         </p>
	 */
	public static String mensajeJS(String clase, String texto, String usuario, String metodo, String js) {
		// System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		mensajeLog(clase, "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		return "\n<p class=\"" + clase + "\" " + js + ">" + texto + "</p>";
	}

	/**
	 * Retorna un mensaje traducido dentro de tags
	 * <p>
	 * 
	 * @param mensaje
	 * @return String
	 *         <p>
	 *         mensaje
	 *         </p>
	 */
	public static String mensajeTraducido(String clase, String texto, String usuario, String metodo) {
		// System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- " + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		mensajeLog(clase, "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " + texto);
		return "<p class=\"" + clase + "\">" + texto + "</p>";
	}

	/**
	 * Retorna un mensaje dentro de tags
	 * <p>
	 * 
	 * @param mensaje
	 * @return String
	 *         <p>
	 *         mensaje
	 *         </p>
	 */
	public static String mensajeImpersonal(String clase, String texto) {
		System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + clase + ": " + texto);
		return "<p class=\"" + clase + "\">" + texto + "</p>";
	}

	/**
	 * Metodo para traducir según un código de etiqueta
	 * 
	 * @param codigo_traduccion
	 * @param idioma
	 * @param texto_x_defecto
	 * @return
	 */
	public static String traducir(String codigo_traduccion, String idioma, String texto_x_defecto) {
		String t = texto_x_defecto;
		LenguajeI18N L = new LenguajeI18N();
		ResourceBundle msj = setLenguaje(idioma, L);

		if (!tieneAlgo(codigo_traduccion)) {
			return t;
		}

		if (!tieneAlgo(idioma)) {
			return t;
		}

		try {
			t = msj.getString(codigo_traduccion);
		}
		catch (MissingResourceException e) {
			mensajeLog("error", "No hay traduccion para: " + codigo_traduccion);
		}

		return t;
	}

	/**
	 * Obtiene la fecha actual
	 * 
	 * @return String: fecha actual
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static String now(String formato) {

		if (nz(formato, "").equals("")) {
			formato = DATE_FORMAT_NOW;
		}

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		return sdf.format(cal.getTime());
	}

	/**
	 * Método no utilizado aún para dar formato a las columnas de una tabla retornada
	 * 
	 * @param object
	 * @param className
	 * @param displaySize
	 * @return
	 */
	public static String formatColumn(Object object, String className, int displaySize) {
		String value = "";
		if (className.equals("java.lang.String")) {
			if (object != null) {
				value = rpad((String) object, displaySize, ' ');
			}
			else {
				value = rpad(value, displaySize, ' ');
			}
		}
		else if (className.equals("java.math.BigDecimal")) {
			if (object != null) {
				BigDecimal n = (BigDecimal) object;
				value = lpad(n.toString(), 9, ' ');
			}
			else {
				value = rpad(value, 9, ' ');
			}
		}
		else if (className.equals("java.sql.Date")) {
			if (object != null) {
				Date ts = (Date) object;
				value = rpad(ts.toString(), 21, ' ');
			}
			else {
				value = rpad(value, 21, ' ');
			}
		}
		else if (className.equals("java.sql.Timestamp")) {
			if (object != null) {
				Date ts = (Date) object;
				value = rpad(ts.toString(), 21, ' ');
			}
			else {
				value = rpad(value, 21, ' ');
			}
		}
		else {
			value = "Unsupported class name: " + className;
		}
		return value + " ";
	}

	/**
	 * Método no utilizado aún para dar formato al encabezado de una tabla retornada
	 * 
	 * @param heading
	 * @param className
	 * @param displaySize
	 * @return
	 */
	public static String formatHeading(String heading, String className, int displaySize) {
		int length = displaySize;
		String value = "";
		if (heading != null) {
			value = heading;
			if (className.equals("java.lang.String")) {}
			else if (className.equals("java.math.BigDecimal")) {
				length = 9;
			}
			else if (className.equals("java.sql.Date")) {
				length = 21;
			}
			else if (className.equals("java.sql.Timestamp")) {
				length = 21;
			}
			else {
				value = "Unsupported class name: " + className;
			}
		}
		return rpad(value, length, ' ') + " ";
	}

	/**
	 * Método no utilizado aún para dar formato a una celda de una tabla
	 * 
	 * @param in
	 * @param length
	 * @param pad
	 * @return
	 */
	public static String rpad(String in, int length, char pad) {
		StringBuffer out = new StringBuffer(length);
		int least = in.length();

		if (least > length) least = length;

		out.append(in.substring(0, least));

		int fill = length - out.length();

		for (int i = 0; i < fill; i++) {
			out.append(pad);
		}
		return out.toString();
	}

	/**
	 * Método no utilizado aún para dar formato a una celda de una tabla
	 * 
	 * @param in
	 * @param length
	 * @param pad
	 * @return
	 */
	public static String lpad(String in, int length, char pad) {
		StringBuffer out = new StringBuffer(length);
		int least = in.length();

		if (least > length) least = length;

		out.append(in.substring(0, least));

		int fill = length - out.length();

		for (int i = 0; i < fill; i++) {
			out.insert(0, pad);
		}
		return out.toString();
	}

	/**
	 * Método tieneAlgo para averiguar si un string tiene algún valor definido.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean tieneAlgo(String s) {
		if (s == null) return false;
		if (s.equals("")) return false;
		if (s.trim().length() == 0) return false;

		return true;
	}

	/**
	 * Metodo noEsNulo para validar que un String no sea nulo.
	 * 
	 * @param s
	 * @return true si no es nulo, false de lo contrario
	 */
	public static boolean noEsNulo(String s) {
		if (s == null) return false;

		return true;
	}

	/**
	 * Retorna una fecha con menor nivel de precisión. Esta función se usa para poder comparar dos fechas más fácilmente.
	 * 
	 * @param fecha
	 * @return
	 */
	public static Date redondearFecha(Date fecha, int nivel) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		if (nivel > 0) cal.set(Calendar.MILLISECOND, 0);
		if (nivel > 1) cal.set(Calendar.SECOND, 0);
		if (nivel > 2) cal.set(Calendar.MINUTE, 0);
		if (nivel > 3) cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}

	/**
	 * Método para emitir un mensaje en la consola
	 * 
	 * @param str
	 * @return strfinal: mensaje emitido
	 */
	private static String mensajeLog(String clase, String str) {
		Date fecha_actual = null;
		fecha_actual = redondearFecha(new Date(), 0);
		String strfinal = "";

		strfinal = fecha_actual.toString() + " --> " + str;

		if (!clase.equals("error"))
			System.out.println(strfinal);
		else
			System.err.println(strfinal);

		return strfinal;
	}

	/**
	 * Método para registrar mensajes en un archivo específico
	 * 
	 * @param str
	 * @param nombrearchivo
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	public void loguear(String str, String nombrearchivo) throws ClassNotFoundException, Exception {
		BD dbREDD = new BD();

		String carpeta = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_logs'", "");
		if (!tieneAlgo(carpeta)) {
			carpeta = getServletContext().getRealPath("") + File.separator + "logs";
		}

		String nombre_archivo_log = carpeta + "/" + nombrearchivo;

		String mensajefinal = "\n" + mensajeLog("nota", str) + "\n";

		try {
			FileWriter fstream = new FileWriter(nombre_archivo_log, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(mensajefinal);
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que dice si el string s es realmente un número entero
	 * 
	 * @param s
	 * @return verdadero si s es un número entero, falso de lo contrario
	 */
	public static boolean esEntero(String s) {
		if (s == null) return false;

		return s.matches("((-|\\+)?[0-9]+(\\[0-9]+)?)+");
	}

	/**
	 * Método que dice si el string s es realmente un número entero
	 * 
	 * @param s
	 * @return verdadero si s es un número entero, falso de lo contrario
	 */
	public static boolean esEnteroMayorOIgualACero(String s) {
		if (s == null) return false;

		if (!esEntero(s)) return false;

		return Long.parseLong(s) >= 0;
	}

	/**
	 * Método que dice si el string s es realmente un puerto
	 * 
	 * @param s
	 * @return verdadero si s es un puerto, falso de lo contrario
	 */
	public static boolean esPuerto(String s) {
		if (s == null) return false;

		if (!esEntero(s)) return false;

		return (Long.parseLong(s) > 0 && Long.parseLong(s) <= 65535);
	}

	/**
	 * Método que dice si el string s es realmente un número
	 * 
	 * @param s
	 * @return verdadero si s es un número, falso de lo contrario
	 */
	public static boolean esNumero(String s) {
		boolean loEs1 = false;
		boolean loEs2 = false;

		if (s == null) return false;

		loEs1 = s.matches("-?\\d+(\\.\\d+)?");
		// loEs2 = s.matches("-?[\\.]\\d+(\\.\\d+)?");
		loEs2 = s.matches("-?[\\.]\\d+?");

		return (loEs1 || loEs2);
	}

	/**
	 * Método que dice si el string s es realmente un número y una latitud
	 * 
	 * @param s
	 * @return verdadero si s es un número, falso de lo contrario
	 */
	public static boolean esLatitud(String s) {
		if (s == null) return false;

		boolean es_numero = esNumero(s);

		if (!es_numero) {
			return false;
		}

		try {
			double v = Double.parseDouble(s);
			if (v >= -90 && v <= 90) {
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}

		return false;
	}

	/**
	 * Método que dice si el string s es realmente una altitud
	 * 
	 * @param s
	 * @return verdadero si s es un número, falso de lo contrario
	 */
	public static boolean esAltitud(String s) {
		if (s == null) return false;

		boolean es_numero = esNumero(s);

		if (!es_numero) {
			return false;
		}

		try {
			double v = Double.parseDouble(s);
			if (v >= 0 && v <= 6000) {
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}

		return false;
	}

	/**
	 * Método que dice si el string s es realmente un número y una longitud
	 * 
	 * @param s
	 * @return verdadero si s es un número, falso de lo contrario
	 */
	public static boolean esLongitud(String s) {
		if (s == null) return false;

		boolean es_numero = esNumero(s);

		if (!es_numero) {
			return false;
		}

		double v = Double.parseDouble(s);

		if (v >= -180 && v <= 180) {
			return true;
		}

		return false;
	}

	/**
	 * Método que dice si el string s es realmente una hora
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esHora(String s) {
		if (s == null) return false;

		if (!esEnteroMayorOIgualACero(s)) return false;

		return Long.parseLong(s) <= 24;
	}

	/**
	 * Método que dice si el string s es realmente un minuto
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esMinuto(String s) {
		if (s == null) return false;

		if (!esEnteroMayorOIgualACero(s)) return false;

		return Long.parseLong(s) <= 59;
	}

	/**
	 * Método que dice si el string s es realmente un número de subparcela fustal SPF
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esSPF(String s) {
		if (s == null) return false;

		if (!esEnteroMayorOIgualACero(s)) return false;

		long i = Long.parseLong(s);

		return i >= 1 && i <= 5;
	}

	/**
	 * Método que dice si el string s es realmente un DAP
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esDAP(String s) {
		if (s == null) return false;

		if (!esNumeroPositivo(s)) return false;

		return Double.parseDouble(s) <= 500.0;
	}

	/**
	 * Método que dice si el string s es realmente una densidad de madera
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esDensidad(String s) {
		if (s == null) return false;

		if (!esNumeroPositivo(s)) return false;

		return Double.parseDouble(s) <= 2.0;
	}

	/**
	 * Método que dice si el string s es realmente una biomasa aérea
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esBiomasaAerea(String s) {
		if (s == null) return false;

		if (!esNumeroPositivo(s)) return false;

		return Double.parseDouble(s) <= 10000.0;
	}

	/**
	 * Método que dice si el string s es realmente una biomasa subterranea
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esBiomasaSubterranea(String s) {
		if (s == null) return false;

		if (!esNumeroPositivo(s)) return false;

		return Double.parseDouble(s) <= 10000.0;
	}

	/**
	 * Método que dice si el string s es realmente una biomasa total
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esBiomasaTotal(String s) {
		if (s == null) return false;

		if (!esNumeroPositivo(s)) return false;

		return Double.parseDouble(s) <= 10000.0;
	}

	/**
	 * Método que dice si el string s es realmente un segundo
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esSegundo(String s) {
		if (s == null) return false;

		if (!esEnteroMayorOIgualACero(s)) return false;

		return Long.parseLong(s) <= 59;
	}

	/**
	 * Método que dice si el string s es realmente una subparcela
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esSubparcela(String s) {
		if (s == null) return false;

		if (!esEnteroMayorOIgualACero(s)) return false;

		return (Long.parseLong(s) <= 5 && Long.parseLong(s) >= 1);
	}

	/**
	 * Método que dice si el string s es realmente un número positivo
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esNumeroPositivo(String s) {
		if (s == null) return false;

		boolean es_numero = esNumero(s);

		if (!es_numero) {
			return false;
		}

		boolean es_positivo = false;

		try {
			double d = Double.parseDouble(s);
			es_positivo = (d > 0);
		}
		catch (NumberFormatException nfe) {
			return false;
		}

		return es_positivo;
	}

	/**
	 * Método que dice si el string s es realmente un azimuth
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esAzimuth(String s) {
		if (s == null) return false;

		boolean es_azimuth = false;

		if (!esNumero(s)) return false;

		double d = 0;

		try {
			d = Double.parseDouble(s);
			es_azimuth = (d >= 0 && d <= 359);
		}
		catch (NumberFormatException nfe) {
			return false;
		}

		return es_azimuth;
	}

	/**
	 * Método que dice si el string s es realmente una distancia
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esDistancia(String s) {
		if (s == null) return false;

		boolean loes = false;

		if (!esNumero(s)) return false;

		double d = 0;

		try {
			d = Double.parseDouble(s);
			loes = (d >= 0 && d <= 1000);
		}
		catch (NumberFormatException nfe) {
			return false;
		}

		return loes;
	}

	/**
	 * Método que dice si el string s es realmente un porcentaje
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esPorcentaje(String s) {
		if (s == null) return false;

		boolean es_porcentaje = false;

		if (!esNumeroPositivo(s)) return false;

		double d = 0;

		try {
			d = Double.parseDouble(s);
			es_porcentaje = (d >= 0 && d <= 1);
		}
		catch (NumberFormatException nfe) {
			return false;
		}

		return es_porcentaje;
	}

	/**
	 * Método que retorna la ruta de la aplicación
	 * 
	 * @return
	 */
	public String rutApp() {
		String rutaAbsoluta = "";

		try {
			rutaAbsoluta = getServletContext().getRealPath("");
		}
		catch (Exception e) {
			rutaAbsoluta = e.toString();
		}

		return rutaAbsoluta;

	}

	/**
	 * Método que retorna la ruta de la aplicación
	 * 
	 * @return ruta: ruta absoluta de la aplicación
	 */
	public String rutAplicacion() {
		String rutaAbsoluta = "";
		String ruta = "";

		String rutaRelativa = "";

		try {
			rutaAbsoluta = getServletContext().getRealPath(rutaRelativa);
			ruta = rutaAbsoluta.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ruta;
	}

	/**
	 * Método que retorna un valor especificado si el String dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public static String nz(String s, String valorSiEsNulo) {
		String resultado = "";

		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = s;

		return resultado;
	}

	/**
	 * Método que retorna un valor especificado si el String dado es null o es vacio ""
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public static String nzVacio(String s, String valorSiEsNulo) {
		if (s == null) return valorSiEsNulo;
		if (s.equals("")) return valorSiEsNulo;
		return s;
	}

	/**
	 * Método que retorna un String especificado si el Objeto dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public static String nzObjStr(Object s, String valorSiEsNulo) {
		String resultado = null;

		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = s.toString();

		return resultado;
	}

	/**
	 * Método que retorna un double especificado si el Objeto dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public static double nzObjDouble(Object s, double valorSiEsNulo) {
		double resultado = 0.0;

		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = Double.parseDouble(s.toString());

		return resultado;
	}

	/**
	 * Variable publica estatica final format para la validacion de fechas
	 */
	public static final ThreadLocal<SimpleDateFormat>	format	= new ThreadLocal<SimpleDateFormat>() {

																	@Override
																	protected SimpleDateFormat initialValue() {
																		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
																		df.setLenient(false);
																		System.out.println("created");
																		return df;
																	}
																};

	/**
	 * Método para verificar la validez de una fecha con formato yyyy-MM-dd
	 * 
	 * @param text
	 * @return
	 */
	public static boolean fechaEsValida(String text) {
		if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d")) return false;
		try {
			format.get().parse(text);
			return true;
		}
		catch (ParseException ex) {
			return false;
		}
	}

	/**
	 * Método para verificar la validez de una fecha con formato yyyy-mm-dd hh24:mi:ss
	 * 
	 * @param text
	 * @return
	 */
	public static boolean fechaHoraEsValida(String text) {
		if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d \\d{2}:\\d{2}:\\d{2}")) return false;
		try {
			format.get().parse(text);
			return true;
		}
		catch (ParseException ex) {
			return false;
		}
	}

	/**
	 * Ejecuta un comando en el sistema.
	 * 
	 * @param commando
	 * @return String con la salida del comando
	 */
	public static String commander(String commandowin, String commandolin, HttpSession session) {
		String metodo = yo + "Commando";

		String usuario = "";
		synchronized (session) {
			usuario = nz(session.getAttribute("usuario").toString(), "");
		}
		String idioma = "";
		synchronized (session) {
			idioma = nz(session.getAttribute("idioma").toString(), "");
		}

		int exitVal = 0;
		Process p = null;
		String os_name = System.getProperty("os.name");
		String mensajes = "";

		if (!tieneAlgo(os_name)) {
			mensajes += mensaje("error", "No se logro obtener el nombre del sistema operativo.", usuario, metodo);
			return "-300-=-" + mensajes;
		}

		mensajes += mensaje("nota", "Nombre del sistema operativo: " + os_name, usuario, metodo);

		try {
			if (os_name.toUpperCase().contains("WIN")) {
				// PARA WINDOWS:
				mensajes += mensaje("nota", "Ejecutando comando de windows: " + commandowin, usuario, metodo);

				p = Runtime.getRuntime().exec("cmd /c " + commandowin);

				Commando errorGobbler = new Commando(p.getErrorStream(), "ERROR");

				// any output?
				Commando outputGobbler = new Commando(p.getInputStream(), "OUTPUT");

				// kick them off
				errorGobbler.start();
				outputGobbler.start();

				// any error???
				exitVal = p.waitFor();
			}
			else {
				// PARA LINUX:
				mensajes += mensaje("nota", "Ejecutando comando de UNIX: " + commandolin, usuario, metodo);
				p = Runtime.getRuntime().exec(commandolin);

				exitVal = p.waitFor();

				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = reader.readLine();

				while (line != null) {
					System.out.println(line);
					line = reader.readLine();
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			exitVal = -100;
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			exitVal = -101;
		}
		catch (Exception e) {
			e.printStackTrace();
			exitVal = -102;
		}

		return String.valueOf(exitVal) + "-=-" + mensajes;
	}

	/**
	 * Quita comillas dobles y espacios de un titulo de columna de archivo CSV
	 * 
	 * @param titulo
	 * @return
	 */
	public static String tituloCSV(String titulo) {
		return titulo.trim().replaceAll("\"", "");
	}

	/**
	 * Formatea un decimal para poder ser guardado en la base de datos
	 * 
	 * @param v
	 * @return
	 */
	public static String decimalCSV(String v) {
		v = v.trim().replaceAll("\"", "");
		v = v.replace(",", ".");

		return v;
	}

	/**
	 * Método que devuelve las opciones en formato HTML para un elemento SELECT según una consulta SQL, y especificando, de esa consulta, cuál es el campo que tiene el valor y cuál el texto visible en el select
	 * 
	 * @param consulta
	 * @param campo_valor
	 * @param campos_texto
	 * @param id
	 * @param defecto
	 * @return String: opciones en formato HTML para un elemento SELECT
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String cargarOpciones(String consulta, String campo_valor, String campos_texto, String id, String defecto, boolean ajax, boolean opcionVacia, boolean plantilla) throws ClassNotFoundException, SQLException {
		String opciones = "";
		String valor = "";
		String texto = "";
		String selected = "";
		boolean hubo_seleccion = false;
		int i = 0;

		try {
			if (defecto == null) defecto = "";

			ResultSet r = null;

			BD dbREDD = new BD();

			r = dbREDD.consultarBD(consulta);

			while (r.next()) {
				valor = r.getString(campo_valor);
				StringTokenizer st_campos_texto = new StringTokenizer(campos_texto, ",");

				texto = "";
				int n_tokens = st_campos_texto.countTokens();
				for (i = 2; i <= n_tokens + 1; i++) {
					texto += r.getString(i);
					if (i >= 2 && i < n_tokens + 1) {
						texto += ", ";
					}
				}
				texto = texto.trim();

				if (nz(id, "").length() > 0) {
					if (valor.equals(id)) {
						selected = " selected ";
						hubo_seleccion = true;
					}
					else {
						selected = "";
					}
				}
				else if (defecto.length() > 0) {
					if (valor.equals(defecto)) {
						selected = " selected ";
						hubo_seleccion = true;
					}
					else {
						selected = "";
					}
				}

				if (!ajax) {
					if (!plantilla)
						opciones += "<option value='" + valor + "' " + selected + ">" + texto + "</option>";
					else
						opciones += " [" + valor + "=" + texto + "] ";
				}
				else
					opciones += valor + "@@" + selected + "@@" + texto + "\n";
			}

			if (opcionVacia) {
				if (!hubo_seleccion) {
					selected = " selected ";
				}
				else {
					selected = "";
				}

				if (!ajax) {
					if (!plantilla)
						opciones = "<option value='' " + selected + ">" + defecto + "</option>" + opciones;
					else
						opciones += " [" + valor + "=" + defecto + "] ";
				}
				else
					opciones = "@@" + selected + "@@" + defecto + "\n" + opciones;
			}

			r.close();

			if (dbREDD != null) dbREDD.desconectarse();
			return opciones;
		}
		catch (Exception e) {
			return "Problemas al cargar las opciones (" + e.toString() + "). Consulta: " + consulta + " Campo de valor: " + campo_valor + " Campos de texto: " + campos_texto + " índice de columna: " + i;
		}
	}

	/**
	 * Encripta string con llave usando dos caminos
	 * 
	 * @param cadena
	 * @param llave
	 * @return
	 */
	public static String encriptar(String cadena, String llave) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword(llave);
		return s.encrypt(cadena);
	}

	/**
	 * Desencripta string con llave usando dos caminos
	 * 
	 * @param cadena
	 * @param llave
	 * @return
	 */
	public static String desencriptar(String cadena, String llave) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword(llave);
		String contenido = "";
		for (int i = 0; i < cadena.length(); i++) {
			contenido += (cadena.charAt(i) == ' ') ? "+" : cadena.charAt(i);
		}// fin del for
			// cadena.replace("/\s/g","_");F
		String devuelve = "";
		try {
			devuelve = s.decrypt(contenido);
		}
		catch (Exception e) {}
		return devuelve;
	}

	/**
	 * Emite en la consola un mensaje de depuración
	 * 
	 * @param clase
	 * @param mensaje
	 */
	public static void depurar(String clase, String mensaje) {
		String depuracion = nz(mensaje, "");
		System.out.println(clase + "--T=" + (System.currentTimeMillis() / 1000) + " -- " + depuracion);
	}

	/**
	 * Limpia un texto de palabras no deseadas
	 * 
	 * @param texto
	 * @return String del texto depurado
	 */
	public static String limpiarTexto(String texto) {
		String textoLimpio = "";

		textoLimpio = nzObjStr(texto, "");

		textoLimpio = textoLimpio.replace("\t", "");
		textoLimpio = textoLimpio.replace("\n", "");
		textoLimpio = textoLimpio.replace("\r", "");
		textoLimpio = textoLimpio.replaceAll("[^a-zA-Z0-9áÁéÉíÍóÓúÚñÑß@$?¿.,!¡:_\\- ]", "");
		textoLimpio = textoLimpio.replaceAll("(?i)insert", "i n s e r t");
		textoLimpio = textoLimpio.replaceAll("(?i)update", "u p d a t e");
		textoLimpio = textoLimpio.replaceAll("(?i)delete", "d e l e t e");
		textoLimpio = textoLimpio.replaceAll("(?i)drop", "d r o p");
		textoLimpio = textoLimpio.replaceAll("(?i)alter", "a l t e r");
		textoLimpio = textoLimpio.trim();

		if (textoLimpio.length() > 4000) {
			textoLimpio = textoLimpio.substring(0, 4001);
		}

		return textoLimpio;
	}
}
