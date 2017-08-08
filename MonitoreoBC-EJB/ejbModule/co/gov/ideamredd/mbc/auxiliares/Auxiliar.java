// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.auxiliares;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Random;
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

import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Clase Auxiliar con métodos que todas las demás clases puedan utilizar
 * 
 * @author Santiago Hernández para Datum Ingeniería SAS Proyecto REDD - IDEAM
 * 
 */
@SuppressWarnings("serial")
public class Auxiliar {

	public static String		depuracion		= "";
	private static String		yo				= "Auxiliar.";

	public static final String	DATE_FORMAT_NOW	= "yyyy-MM-dd HH:mm:ss";

	/**
	 * Método implotarArregloString
	 * 
	 * Devuelve un string concatenando los elementos del arreglo a, separados
	 * por separador
	 * 
	 * @param a
	 * @param separador
	 * @return
	 */
	public static String implotarArregloString(String[] a, String separador) {

		String metodo = yo + "implotarArregloString";

		String r = "";

		if (a == null)
			return "";

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

		// System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- "
		// + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " +
		// texto);
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
	public static String mensajeJS(String clase, String texto, String usuario, String metodo, String js) {

		// System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- "
		// + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " +
		// texto);
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

		// System.out.println("T=" + (System.currentTimeMillis()/1000) + " -- "
		// + "U:" + usuario + " -- M:" + metodo + " -- " + clase + ": " +
		// texto);
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
	 * Método tieneAlgo para averiguar si un string tiene algún valor definido.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean tieneAlgo(String s) {

		if (s == null)
			return false;
		if (s.equals(""))
			return false;
		if (s.trim().length() == 0)
			return false;

		return true;
	}

	/**
	 * Metodo noEsNulo para validar que un String no sea nulo.
	 * 
	 * @param s
	 * @return true si no es nulo, false de lo contrario
	 */
	public static boolean noEsNulo(String s) {

		if (s == null)
			return false;

		return true;
	}

	/**
	 * Funcion redondearFecha
	 * 
	 * Retorna una fecha con menor nivel de precisión. Esta función se usa para
	 * poder comparar dos fechas más fácilmente.
	 * 
	 * @param fecha
	 * @return
	 */
	public static Date redondearFecha(Date fecha, int nivel) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		if (nivel > 0)
			cal.set(Calendar.MILLISECOND, 0);
		if (nivel > 1)
			cal.set(Calendar.SECOND, 0);
		if (nivel > 2)
			cal.set(Calendar.MINUTE, 0);
		if (nivel > 3)
			cal.set(Calendar.HOUR_OF_DAY, 0);
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
	 * Método que dice si el string s es realmente un número entero
	 * 
	 * @param s
	 * @return verdadero si s es un número entero, falso de lo contrario
	 */
	public static boolean esEntero(String s) {

		if (s == null)
			return false;

		return s.matches("((-|\\+)?[0-9]+(\\[0-9]+)?)+");
	}

	/**
	 * Método que dice si el string s es realmente un número entero
	 * 
	 * @param s
	 * @return verdadero si s es un número entero, falso de lo contrario
	 */
	public static boolean esEnteroMayorOIgualACero(String s) {

		if (s == null)
			return false;

		if (!esEntero(s))
			return false;

		return Long.parseLong(s) >= 0;
	}

	/**
	 * Método que dice si el string s es realmente un puerto
	 * 
	 * @param s
	 * @return verdadero si s es un puerto, falso de lo contrario
	 */
	public static boolean esPuerto(String s) {

		if (s == null)
			return false;

		if (!esEntero(s))
			return false;

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

		if (s == null)
			return false;

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

		if (s == null)
			return false;

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

		if (s == null)
			return false;

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

		if (s == null)
			return false;

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

		if (s == null)
			return false;

		if (!esEnteroMayorOIgualACero(s))
			return false;

		return Long.parseLong(s) <= 24;
	}

	/**
	 * Método que dice si el string s es realmente un minuto
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esMinuto(String s) {

		if (s == null)
			return false;

		if (!esEnteroMayorOIgualACero(s))
			return false;

		return Long.parseLong(s) <= 59;
	}

	/**
	 * Método que dice si el string s es realmente un número de subparcela
	 * fustal SPF
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esSPF(String s) {

		if (s == null)
			return false;

		if (!esEnteroMayorOIgualACero(s))
			return false;

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

		if (s == null)
			return false;

		if (!esNumeroPositivo(s))
			return false;

		return Double.parseDouble(s) <= 500.0;
	}

	/**
	 * Método que dice si el string s es realmente una densidad de madera
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esDensidad(String s) {

		if (s == null)
			return false;

		if (!esNumeroPositivo(s))
			return false;

		return Double.parseDouble(s) <= 2.0;
	}

	/**
	 * Método que dice si el string s es realmente una biomasa aérea
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esBiomasaAerea(String s) {

		if (s == null)
			return false;

		if (!esNumeroPositivo(s))
			return false;

		return Double.parseDouble(s) <= 10000.0;
	}

	/**
	 * Método que dice si el string s es realmente una biomasa subterranea
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esBiomasaSubterranea(String s) {

		if (s == null)
			return false;

		if (!esNumeroPositivo(s))
			return false;

		return Double.parseDouble(s) <= 10000.0;
	}

	/**
	 * Método que dice si el string s es realmente una biomasa total
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esBiomasaTotal(String s) {

		if (s == null)
			return false;

		if (!esNumeroPositivo(s))
			return false;

		return Double.parseDouble(s) <= 10000.0;
	}

	/**
	 * Método que dice si el string s es realmente un segundo
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esSegundo(String s) {

		if (s == null)
			return false;

		if (!esEnteroMayorOIgualACero(s))
			return false;

		return Long.parseLong(s) <= 59;
	}

	/**
	 * Método que dice si el string s es realmente una subparcela
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esSubparcela(String s) {

		if (s == null)
			return false;

		if (!esEnteroMayorOIgualACero(s))
			return false;

		return (Long.parseLong(s) <= 5 && Long.parseLong(s) >= 1);
	}

	/**
	 * Método que dice si el string s es realmente un número positivo
	 * 
	 * @param s
	 * @return verdadero si s es un número positivo, falso de lo contrario
	 */
	public static boolean esNumeroPositivo(String s) {

		if (s == null)
			return false;

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

		if (s == null)
			return false;

		boolean es_azimuth = false;

		if (!esNumero(s))
			return false;

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

		if (s == null)
			return false;

		boolean loes = false;

		if (!esNumero(s))
			return false;

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

		if (s == null)
			return false;

		boolean es_porcentaje = false;

		if (!esNumeroPositivo(s))
			return false;

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
	 * Método que retorna un valor especificado si el String dado es null o es
	 * vacio ""
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public static String nzVacio(String s, String valorSiEsNulo) {

		if (s == null)
			return valorSiEsNulo;
		if (s.equals(""))
			return valorSiEsNulo;
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
	 * Método que retorna un int especificado si el Objeto dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public static int nzObjInt(Object s, int valorSiEsNulo) {
		
		int resultado = 0;
		
		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = Integer.parseInt(s.toString());
		
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

		if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
			return false;
		try {
			format.get().parse(text);
			return true;
		}
		catch (ParseException ex) {
			return false;
		}
	}

	/**
	 * Método para verificar la validez de una fecha con formato yyyy-mm-dd
	 * hh24:mi:ss
	 * 
	 * @param text
	 * @return
	 */
	public static boolean fechaHoraEsValida(String text) {

		if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d \\d{2}:\\d{2}:\\d{2}"))
			return false;
		try {
			format.get().parse(text);
			return true;
		}
		catch (ParseException ex) {
			return false;
		}
	}
	
	/**
	 * Método esPeriodo
	 * 
	 * Dice si el entero provisto es un período (año) válido entre 1800 y el año actual
	 * 
	 * @param p Integer período
	 * @return true si es válido, falseo de lo contrario
	 */
	public static boolean esPeriodo(Integer p) {
		if (p == null)
			return false;
		
		if (p < 1800) 
			return false;
		
		int anioactual = Calendar.getInstance().get(Calendar.YEAR);
		if (p > anioactual) 
			return false;
		
		return true;
	}

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

	public static String encriptar(String cadena, String llave) {

		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword(llave);
		return s.encrypt(cadena);
	}

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
		catch (Exception e) {
		}
		return devuelve;
	}

	public static void depurar(String clase, String mensaje) {

		String depuracion = nz(mensaje, "");
		System.out.println(clase + "--T=" + (System.currentTimeMillis() / 1000) + " -- " + depuracion);
	}

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
	
	
	public static Integer getStringArrayListIndexByValue(ArrayList<String> a, String v) {
		
		Integer i = null;
		
		return i;
		
	}

	
	/**
	 * Función Commando
	 * 
	 * Ejecuta un comando en el sistema.
	 * 
	 * @param commando
	 * @return
	 */
	public static String commander(String commandowin, String commandolin) {
		int exitVal = 0;
		Process p = null;
		String os_name = System.getProperty("os.name");
		String mensajes = "";
		String output = "";

		if (!Auxiliar.tieneAlgo(os_name)) {
			mensajes += Auxiliar.mensajeImpersonal("error", "No se logro obtener el nombre del sistema operativo.");
			return "-300-=-" + mensajes;
		}

		mensajes += Auxiliar.mensajeImpersonal("nota", "Nombre del sistema operativo: " + os_name);

		try {
			if (os_name.toUpperCase().contains("WIN")) {
				// PARA WINDOWS:
				mensajes += Auxiliar.mensajeImpersonal("nota", "Ejecutando comando de windows: " + commandowin);

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
				mensajes += Auxiliar.mensajeImpersonal("nota", "Ejecutando comando de UNIX: " + commandolin);
				p = Runtime.getRuntime().exec(commandolin);

				BufferedReader reader;
				String line;
				
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					mensajes += "<br>" + line;
				}
				
				reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					mensajes += "<br>" + line;
				}
				
				exitVal = p.waitFor();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			mensajes += e.toString();
			exitVal = -100;
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			mensajes += e.toString();
			exitVal = -101;
		} 
		catch (Exception e) {
			e.printStackTrace();
			mensajes += e.toString();
			exitVal = -102;
		}

		return String.valueOf(exitVal) +  "-=-" + mensajes + "-=-" + output;
	}
	
	
	public static String[] shcript(String script, String outfile, String errfile) {
		String out = "";
		String err = "";
		int exitValue = -1;
		String [] r = {"", out, err};
		
		ProcessBuilder builder = new ProcessBuilder("/usr/bin/sh", script);
		builder.redirectOutput(new File(outfile));
		builder.redirectError(new File(errfile));
		try {
			Process p = builder.start();
			exitValue = p.exitValue();
			
			BufferedReader reader;
			String line;
			
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				out += "\n" + line;
			}
			
			reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				err += "\n" + line;
			}
			
			r[0] = String.valueOf(exitValue);
			r[1] = out;
			r[2] = err;

		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		return r;
	}


	/**
	 * Método convertirCharset
	 * 
	 * Convierte texto del charset de al charset a
	 * 
	 * @param texto
	 * @param de
	 * @param a
	 * @return String texto convertido de de a a 
	 */
	public static String convertirCharset(String texto, String de, String a) {
		String resultado = "";
		
		Charset charsetE = Charset.forName(de);
		CharsetEncoder encoder = charsetE.newEncoder();

		Charset charsetD = Charset.forName(a);
		CharsetDecoder decoder = charsetD.newDecoder();

		try {
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(texto));
			CharBuffer cbuf = decoder.decode(bbuf);
			resultado = cbuf.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Convertí " + texto + " a " + resultado);

		return resultado;
	}
	
	/**
	 * Método inStringArrayList
	 * dice si v está en a
	 * @param v
	 * @param a
	 * @return
	 */
	public static boolean inStringArrayList(String v, ArrayList<String> a) {
		if (v == null) {
			return false;
		}
		
		if (a == null) {
			return false;
		}
		
		String s = "";
		
		for (int i=0; i<a.size(); i++) {
			s = a.get(i).toString();
			if (tieneAlgo(s)) {
				if (v.equals(s)) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Método enteroAlAzar
	 * devuelve un entero entre min y max
	 * @param min
	 * @param max
	 * @return
	 */
	public static int enteroAlAzar(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) +1) + min;
		return randomNum;
	}
}
