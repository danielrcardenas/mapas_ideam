// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.util;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import oracle.jdbc.OracleTypes;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import co.gov.ideamredd.conexion.Conexion;

/**
 * Métodos de utilidad para todo el sistema
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Util {

	static double	a	= 349789321.0;

	// private static final int BUFFER_SIZE = 1024;

	/**
	 * Concatena un string de consulta
	 * 
	 * @param valores
	 * @return string de consulta construido
	 */
	public static String construirStringConsulta(ArrayList<Integer> valores) {
		String datos = "";
		for (int i = 0; i < valores.size(); i++) {
			if (!datos.equals("")) {
				datos += ",";
			}
			datos += valores.get(i).toString();
		}
		if (datos != "")
			return datos;
		else
			return null;
	}

	/**
	 * Retorna la diferencia en días entre dos fechas
	 * 
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return días de diferencia
	 */
	public static int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
			fechaInicial = (Date) df.parse(fechaInicioString);
		}
		catch (ParseException ex) {}
		String fechaFinalString = df.format(fechaFinal);
		try {
			fechaFinal = (Date) df.parse(fechaFinalString);
		}
		catch (ParseException ex) {}
		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return (int) dias;
	}

	/**
	 * Convierte un arreglo de integer a un arraylist de integer
	 * 
	 * @param arreglo
	 * @return arraylist del arreglo de integer
	 */
	public static ArrayList<Integer> obtenerArregloInteger(String[] arreglo) {
		ArrayList<Integer> datos = new ArrayList<Integer>();
		for (int i = 0; i < arreglo.length; i++) {
			datos.add(Integer.valueOf(arreglo[i]));
		}
		return datos;
	}

	/**
	 * Convierte un arreglo de string a un arraylist de string
	 * 
	 * @param arreglo
	 * @return arraylist del arreglo de string
	 */
	public static ArrayList<String> obtenerArregloString(String[] arreglo) {
		ArrayList<String> datos = new ArrayList<String>();
		for (int i = 0; i < arreglo.length; i++) {
			datos.add(arreglo[i]);
		}
		return datos;
	}

	/**
	 * Retorna string de una geometría
	 * 
	 * @param opcion
	 * @param coordenadas
	 * @return string de geometría
	 */
	public static String crearCadenaGeometria(Integer opcion, String coordenadas) {
		String[] puntos = {};
		String cadena = "";
		switch (opcion) {
			case 1:
				puntos = coordenadas.split(",");
				cadena = "POINT (" + puntosCadena(puntos, opcion) + ")";
				break;
			case 2:
				puntos = coordenadas.split(",");
				cadena = "LINESTRING (" + puntosCadena(puntos, opcion) + ")";
				break;
			case 3:
				puntos = coordenadas.split(",");
				cadena = "POLYGON ((" + puntosCadena(puntos, opcion) + "))";
				break;
		}
		return cadena;
	}

	/**
	 * Añade puntos a una cadena, para dar formato de separador de decimales a un string
	 * 
	 * @param puntos
	 * @param opcion
	 * @return string con separador de miles
	 */
	private static String puntosCadena(String[] puntos, Integer opcion) {
		String cad = "";
		int cont = 0;
		while (cont < puntos.length) {
			if (cont == 0)
				cad += puntos[cont] + " " + puntos[cont + 1];
			else
				cad += "," + puntos[cont] + " " + puntos[cont + 1];
			cont += 2;
		}
		if (opcion == 3) cad += "," + puntos[0] + " " + puntos[1];
		return cad;
	}

	/**
	 * Obtiene propiedad de string de propiedades para un key
	 * 
	 * @param clazz
	 * @param properties
	 * @param key
	 * @return string con valor de la propiedad
	 */
	@SuppressWarnings("rawtypes")
	public static String getProperty(Class clazz, String properties, String key) {
		Properties prop = new Properties();
		InputStream is = null;

		try {
			URL g = clazz.getResource(properties);
			is = new FileInputStream(g.getFile().replace("%20", " "));
			prop.load(is);
		}
		catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return prop.getProperty(key);
	}

	/**
	 * Determina si string es númerico
	 * 
	 * @param stringCellValue
	 * @return true si sí
	 */
	public static boolean isNumber(String stringCellValue) {
		if (stringCellValue.startsWith("-")) stringCellValue = stringCellValue.substring(1);
		Pattern p = Pattern.compile("^\\d+\\.?\\d*$");
		Matcher m = p.matcher(stringCellValue);
		return m.matches();
	}

	/**
	 * Retorna arreglo de string de la geometría
	 * 
	 * @param geo
	 * @return arreglo de string de geometría
	 */
	public static String[] obtenerDatosGeometria(String geo) {
		String[] datos = { "", "" };
		StringTokenizer informacion = null;
		if (geo.startsWith("POINT")) {
			datos[0] = "1";
			informacion = new StringTokenizer(geo.substring(5), "()");
		}
		else if (geo.startsWith("LINESTRING")) {
			datos[0] = "2";
			informacion = new StringTokenizer(geo.substring(10), "()");
		}
		else {
			datos[0] = "3";
			informacion = new StringTokenizer(geo.substring(7), "(())");
		}
		while (informacion.hasMoreTokens()) {
			String a = informacion.nextToken();
			if (!a.equals(" ")) {
				String coodenadas[] = a.split(" ");
				int i = 0;
				while (i < coodenadas.length) {
					if (datos[1].equals(""))
						datos[1] = coodenadas[i] + "," + coodenadas[i + 1].replace(",", " ");
					else
						datos[1] = datos[1] + coodenadas[i] + "," + coodenadas[i + 1].replace(",", " ");// + ";";
					i += 2;
				}
			}
		}
		return datos;
	}

	/**
	 * Convierte clob a string
	 * 
	 * @param clb
	 * @return string de clob
	 * @throws Exception
	 */
	public static String clobStringConversion(Clob clb) throws Exception {
		if (clb == null) return "";
		StringBuffer str = new StringBuffer();
		String strng;
		BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());
		while ((strng = bufferRead.readLine()) != null)
			str.append(strng);
		bufferRead.close();
		return str.toString();
	}

	/**
	 * Convierte date de fecha a timestamp
	 * 
	 * @param convert
	 * @return timestamp de fecha
	 */
	@SuppressWarnings("deprecation")
	public static Timestamp convertToDate(Date convert) {
		if (convert.toString().startsWith("00")) convert.setYear(2014 - 1900);
		Calendar c = Calendar.getInstance();
		c.setTime(convert);
		Timestamp t = new Timestamp(c.getTimeInMillis());
		return t;
	}

	/**
	 * Convierte timestamp a date
	 * 
	 * @param convert
	 * @return date de fecha
	 */
	public static java.sql.Date convertToTimestamp(Timestamp convert) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(convert.getTime());
		java.sql.Date d = new java.sql.Date(c.getTimeInMillis());
		return d;
	}

	/**
	 * Retorna la fecha actual
	 * 
	 * @return string de fecha actual
	 */
	public static String obtenerFechaActual() {
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.DATE) + "_" + (date.get(Calendar.MONTH) < 10 ? "0" + (date.get(Calendar.MONTH) + 1) : (date.get(Calendar.MONTH) + 1) + "") + "_" + date.get(Calendar.YEAR);
	}

	/**
	 * Retorna la clave
	 * 
	 * @param key
	 * @param RESOURCE_BUNDLE
	 * @return clave del key
	 */
	public static String obtenerClave(String key, ResourceBundle RESOURCE_BUNDLE) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Retorna los titulos de las parcelas
	 * 
	 * @param key
	 * @return titulo
	 */
	public static String obtenerTitulosParcelas(String key) {
		String archivoEncabezados = "co/gov/ideamredd/recursos/encabezados";
		ResourceBundle encabezados = ResourceBundle.getBundle(archivoEncabezados);
		try {
			return encabezados.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Obtiene reporte WPS
	 * 
	 * @param key
	 * @return string reporte
	 */
	public static String obtenerReportesWPS(String key) {
		String archivoReportes = "co/gov/ideamredd/recursos/reportesWPS";
		ResourceBundle infoReportes = ResourceBundle.getBundle(archivoReportes);
		try {
			return infoReportes.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Calcula el valor de hectareas según los pixeles
	 * 
	 * @param pixeles
	 * @param factor
	 * @return hectareas
	 */
	public static Double hectariasPixeles(Integer pixeles, BigDecimal factor) {
		BigDecimal m = (new BigDecimal(pixeles).multiply(factor));
		return m.doubleValue();
	}

	/**
	 * Calcula el porcentaje
	 * 
	 * @param areaTotal
	 * @param areaDivision
	 * @return porcentaje
	 */
	public static Double calcularPorcentaje(Integer areaTotal, Integer areaDivision) {
		DecimalFormat df = new DecimalFormat("##.####");
		String ad = areaDivision.toString() + "00";
		Double porcentaje = new Double(Double.valueOf(ad) / areaTotal);
		if (porcentaje.isNaN() || porcentaje.isInfinite())
			return Double.valueOf(0);
		else
			return Double.valueOf(df.format(porcentaje).replace(",", "."));
	}

	/**
	 * Retorna la información sobre el porcentaje
	 * 
	 * @param totalInfo
	 * @param porcentaje
	 * @return Double porcentaje
	 */
	public static Double obtenerInfoProcentaje(Double totalInfo, Double porcentaje) {
		double p = (new BigDecimal(totalInfo).multiply(new BigDecimal(porcentaje))).doubleValue();
		String c = String.valueOf(p);
		String d = "";
		if (c.contains(".")) {
			int i = c.indexOf(".");
			d = c.substring(i + 1);
			c = c.substring(0, i);

		}
		String f = "";
		String f1 = "";
		if (c.length() == 1) {
			return Double.valueOf("0.0" + c);
		}
		else if (c.length() == 2) {
			return Double.valueOf("0." + c);
		}
		else {
			f = c.substring(0, c.length() - 2);
			f1 = c.substring(c.length() - 2);
			if (d.equals(""))
				return Double.valueOf(f + "." + f1);
			else
				return Double.valueOf(f + "." + f1 + d);
		}
	}

	/**
	 * Define el estilo del titulo de un reporte
	 * 
	 * @param libro
	 * @return estilo de celda de titulo
	 */
	@SuppressWarnings("static-access")
	public static XSSFCellStyle estiloTituloReporte(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		estiloCelda.setFillForegroundColor((short) 44);
		estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 10);
		fuente.setFontName(fuente.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fuente.setColor((short) 9);
		estiloCelda.setFont(fuente);
		estiloCelda.setBorderBottom((short) 1);
		estiloCelda.setBottomBorderColor((short) 30);
		estiloCelda.setBorderLeft((short) 1);
		estiloCelda.setLeftBorderColor((short) 30);
		estiloCelda.setBorderRight((short) 1);
		estiloCelda.setRightBorderColor((short) 30);
		estiloCelda.setBorderTop((short) 1);
		estiloCelda.setTopBorderColor((short) 30);
		return estiloCelda;
	}

	/**
	 * Define el estilo del tipo de un reporte
	 * 
	 * @param libro
	 * @return estilo de celda de titulo
	 */
	@SuppressWarnings("static-access")
	public static XSSFCellStyle estiloTituloReporte2(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		estiloCelda.setFillForegroundColor((short) 31);
		estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 8);
		fuente.setFontName(fuente.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fuente.setColor((short) 9);
		estiloCelda.setFont(fuente);
		estiloCelda.setBorderBottom((short) 1);
		estiloCelda.setBottomBorderColor((short) 30);
		estiloCelda.setBorderLeft((short) 1);
		estiloCelda.setLeftBorderColor((short) 30);
		estiloCelda.setBorderRight((short) 1);
		estiloCelda.setRightBorderColor((short) 30);
		estiloCelda.setBorderTop((short) 1);
		estiloCelda.setTopBorderColor((short) 30);
		return estiloCelda;
	}

	/**
	 * Define el estilo de división de un reporte
	 * 
	 * @param libro
	 * @return estilo de celda
	 */
	public static XSSFCellStyle estiloDivisionReporte(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// estiloCelda.setFillForegroundColor((short) 31);
		// estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 8);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		fuente.setColor((short) 8);
		estiloCelda.setFont(fuente);
		estiloCelda.setBorderBottom((short) 1);
		estiloCelda.setBottomBorderColor((short) 30);
		estiloCelda.setBorderLeft((short) 1);
		estiloCelda.setLeftBorderColor((short) 30);
		estiloCelda.setBorderRight((short) 1);
		estiloCelda.setRightBorderColor((short) 30);
		estiloCelda.setBorderTop((short) 1);
		estiloCelda.setTopBorderColor((short) 30);
		return estiloCelda;
	}

	/**
	 * Calcula diferencia entre años
	 * 
	 * @param periodo
	 * @return años de diferencia
	 */
	public static Integer obtenerDiferenciaAnos(String periodo) {
		String[] anho = periodo.split("-");
		Integer difPeriodo = Integer.valueOf(anho[1]) - Integer.valueOf(anho[0]);
		return difPeriodo;
	}

	/**
	 * Escribe reportes
	 * 
	 * @param ruta
	 * @param nombreArchivo
	 * @param libro
	 * @return true si lo logró
	 */
	public static boolean escribirReportes(String ruta, String nombreArchivo, XSSFWorkbook libro) {
		boolean esEscrito = true;
		try {
			String path = ruta + "/" + nombreArchivo + ".xlsx";
			FileOutputStream elFichero = new FileOutputStream(path);
			libro.write(elFichero);
			elFichero.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			esEscrito = false;
		}
		return esEscrito;
	}

	/**
	 * Convierte coordenadas a grados
	 * 
	 * @param dato
	 * @return string de coordenadas en grados
	 */
	public static String convertirAGrados(String dato) {
		String g = "";
		String[] info = dato.replace(".", ",").split(",");
		Double min = Double.valueOf("0." + info[1]) * 60;
		String[] i = min.toString().replace(".", ",").split(",");
		Double seg = Double.valueOf("0." + i[1]) * 60;
		g = info[0] + "," + i[0] + "," + String.valueOf(seg.intValue());
		return g;
	}

	/**
	 * Calcula las coordenadas mínimas y máximas
	 * 
	 * @param coor
	 * @return arreglo con coordenadas mínimas y máximas
	 */
	public static String[] obtenerMaxMinCoordenadas(String coor) {
		String maxX = "";
		String maxY = "";
		String minX = "";
		String minY = "";
		String[] coordenadas = new String[4];
		String[] datosp = coor.split(" ");
		for (int j = 0; j < datosp.length; j++) {
			String[] puntos = datosp[j].split(",");
			if (maxX.equals("")) {
				maxX = puntos[0];
				maxY = puntos[1];
				minX = puntos[0];
				minY = puntos[1];
			}
			for (int i = 0; i < puntos.length; i += 2) {
				if (Double.valueOf(puntos[i]) > Double.valueOf(maxX)) {
					maxX = puntos[i];
				}
				if (Double.valueOf(puntos[i + 1]) > Double.valueOf(maxY)) {
					maxY = puntos[i + 1];
				}
				if (Double.valueOf(puntos[i]) < Double.valueOf(minX)) {
					minX = puntos[i];
				}
				if (Double.valueOf(puntos[i + 1]) < Double.valueOf(minY)) {
					minY = puntos[i + 1];
				}
			}
		}
		coordenadas[0] = maxX;
		coordenadas[1] = maxY;
		coordenadas[2] = minX;
		coordenadas[3] = minY;
		return coordenadas;
	}

	/**
	 * Obtiene el nombre de un archivo de reporte
	 * 
	 * @param reporte
	 * @param division
	 * @param periodo
	 * @param resolucion
	 * @return nombre del archivo de reporte
	 * @throws Exception
	 */
	public static String obtenerNombreArchivo(String reporte, String division, String periodo, String resolucion) throws Exception {
		Connection conn = Conexion.establecerConexion();
		String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
		ResourceBundle infoReportes = ResourceBundle.getBundle(archivoReportes);
		String descripcion = "";
		String ruta = null;
		try {
			descripcion = infoReportes.getString(reporte + ".wps." + division + "." + periodo + "." + resolucion);
		}
		catch (Exception e) {
			System.out.println("No se ha ingresado la clave " + reporte + ".wps." + division + "." + periodo + "." + resolucion + " en el archivo de propiedades");
		}
		if (!descripcion.equals("")) {
			try {
				CallableStatement obtenerNombreArchivo = conn.prepareCall("{call RED_PK_APOLLO.ConsultarRutaWPS(?,?,?)}");
				obtenerNombreArchivo.setString("una_descipcion", descripcion);
				obtenerNombreArchivo.registerOutParameter("un_resultado", OracleTypes.CURSOR);
				obtenerNombreArchivo.registerOutParameter("sentencia", OracleTypes.VARCHAR);
				obtenerNombreArchivo.execute();
				System.out.println(obtenerNombreArchivo.getString("sentencia"));
				ResultSet resultSet = (ResultSet) obtenerNombreArchivo.getObject("un_resultado");
				while (resultSet.next()) {
					ruta = resultSet.getString(1);
				}
				obtenerNombreArchivo.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		conn.close();
		return ruta;
	}

	/**
	 * Da formato a un número
	 * 
	 * @param o
	 *            Objeto cualquiera(BigDecimal, Double,Float, String)
	 * @return el valor recibido(Numero) como un numero con 4 decimales y formato de miles(.) y millones(')
	 */
	public static String formatoValores(Object o) {
		if (o != null) {
			String base = o.toString().replace(".", ",");
			String[] partes = base.split(",");
			if (partes.length > 1) {
				String ent = "";
				int count = 0;
				for (int i = 0; i < partes[0].length(); i += 3) {
					if (count % 2 == 1 && i > 0)
						ent = "." + partes[0].substring(partes[0].length() - i, partes[0].length() - (i - 3)) + ent;
					else if (i > 0) ent = "'" + partes[0].substring(partes[0].length() - i, partes[0].length() - (i - 3)) + ent;
					count++;
				}
				if (ent.length() < (partes[0].length() + (count - 1))) ent = partes[0].substring(0, (partes[0].length() + (count - 1)) - ent.length()) + ent;
				return ent + "," + (partes[1].length() > 4 ? partes[1].substring(0, 4) : partes[1]);
			}
			else
				return base;
		}
		return "";
	}

	/**
	 * Convierte arreglo a string
	 * 
	 * @param ao
	 * @return string implotado del arreglo
	 */
	public static String arregloACadena(Object[] ao) {
		if (ao != null) {
			String[] as = (String[]) ao;
			String retorno = "";
			for (String tmp : as) {
				retorno += "," + tmp;
			}
			return retorno.substring(1);
		}
		else
			return "";
	}

	/**
	 * Carga clob de oracle
	 * 
	 * @throws Exception
	 */
	public void cargaClob() throws Exception {
		// Suponiendo que tenemos una conexion de tipo java.sql.Connection que
		// apunta a un H2 o a un Oracle.
		Connection conn = Conexion.establecerConexion();
		String texto = "<h4 align=\"center\">Licencia de productos satelitales LANDSAT - Created Commons V 3.0<br>\nLa persona que firma esta licencia, acepta haber leIdo el acuerdo de licenciamiento que a continuaci�n se detalla y entiende que el incumplimiento de este acuerdo con el cual se protegen los derechos de autor, puede acarrear sanciones disciplinarias, legales y penales vigentes en la Republica de Colombia<//h4>\n<ol>\n<li><p>Acuerdo de Licenciamiento\nEste Acuerdo publica los procedimientos aplicables a los datos de sensores remotos tomados por NASA (Administraci�n Nacional de Aeron�utica y del Espacio del Gobierno) Agencia del Gobierno de Los Estados Unidos de America - USA, quienes distribuyen a traves del EROS DATA CENTER de forma gratuita los datos de IMAGENES MODIS y para los cuales los usuarios debe conocer que estos datos se distribuyen bajo el acuerdo de licenciamiento de Created Commons V 3.0, es decir La obra (como se define a continuacion) segun las condiciones de esta licencia publica creative commons (\"CCPL\" o \"licencia\"). La obra esta protegida por derechos de autor  y//u otras leyes aplicables, cualquier uso de la obra diferente a lo autorizado abajo esta prohibido.\n\nAl ejercer cualquier derecho a la obra, usted acepta y se compromete a respetar los terminos de esta licencia, en la medida en esta licencia puede ser considerado un contrato, el licenciante le concede los derechos contenidos en consideracion de su aceptaci�n de los terminos y condiciones\n.<//li><//p>\n<li><p>Parte 1: DEFINICIONES\n\"Adaptacion\" significa una obra basada en el trabajo, o sobre la Obra y otras obras preexistentes tales como una traducci�n, adaptaci�n, trabajo derivado, el arreglo de la musica u otras alteraciones de una obra literaria o artistica, o de un fonograma o de rendimiento y incluye adaptaciones cinematograficas o cualquier otra forma en la cual la Obra puede ser reformulada, transformada o adaptada incluyendo cualquier forma reconocible derivada del original, excepto que una obra que constituye una Coleccion no sera considerada una Obra Derivada a los efectos de esta Licencia. Para evitar dudas, cuando la Obra es una obra musical o fonograma, la sincronizacion de la Obra en una relaci�n temporal con una imagen en movimiento (\"sincronizacion\") sera considerado como una adaptacion a los efectos de esta Licencia.\n    \"Coleccion\" significa un conjunto de obras literarias o art�sticas, tales como enciclopedias y antologias, o interpretaciones o ejecuciones, fonogramas o emisiones, u otras obras o prestaciones distintas de las obras que figuran en la Seccion 1 (f) a continuaci�n y que, por razon de la seleccion y la disposicion de las materias, constituyan creaciones de caracter intelectual, en los que se incluye el trabajo en su totalidad, sin modificaci�n, junto con uno o mas de otras contribuciones que constituyen obras, cada una separadas e independientes en si mismas, que en conjunto estan reunidos en un todo colectivo. Una obra que constituye una Coleccion no sera considerada una Obra Derivada (como se define mas arriba) a los efectos de esta Licencia.\n    \"Distribuir\" significa poner a disposicion del publico en general. Original y las copias de la obra o adaptacion, en su caso, a traves de la venta u otra transferencia de propiedad.\n    \"Licenciante\" es la persona, personas, entidad o entidades que la oferta(n) la Obra bajo los terminos de esta Licencia.\n    \"Autor Original\", en el caso de una obra literaria o artistica, el individuo, las personas, entidad o entidades que crearon el trabajo o si ninguna persona o entidad puede ser identificado, el editor, y adem�s (i) en el caso de una interpretacion de los actores, cantantes, misicos, bailarines y otras personas que representen, canten, reciten, declamen, interpreten o ejecuten en cualquier forma obras literarias o artisticas o expresiones del folclore, (ii) en el caso de un fonograma, el productor es la persona o entidad juridica que fija por primera vez los sonidos de una ejecucion u otros sonidos, y, (iii) en el caso de las emisiones, la organizaci�n que transmite la emision.\n    \"Obra\" significa la obra literaria y // o artisticas ofrece bajo los terminos de esta licencia incluyendo, sin limitacion, cualquier produccion en el campo literario, cientifico y artistico, cualquiera que sea el modo o forma de expresi�n, incluido el formato digital, como por ejemplo un libro , un folleto y otras por escrito, una conferencia de trabajo, direccion, sermon u otra de la misma naturaleza, una obra dramatica o dramatico-musicales, una obra coreografica o de entretenimiento en un espectaculo mudo, una composici�n musical con letra o sin ella, la obra cinematografica a la que se asimilan las obras expresadas por procedimiento analogo a la cinematografia, una obra de dibujo, pintura, arquitectura, escultura, grabado o litografia; una obra fotografica a la que se asimilan las obras expresadas por procedimiento analogo a la fotografia, una obra de arte aplicado; una ilustraci�n, mapa, plano, croquis o en tres dimensiones de trabajo relativas a la geografia, topografia, arquitectura o las ciencias; una actuacion, una emision, un fonograma, una recopilacion de los datos en la medida en que esta protegida como una obra de derechos de autor, o un trabajo realizado por una variedad o un artista de circo en la medida en que no sea considerado una obra literaria o artistica.\n    \"Usted\" significa un individuo o entidad ejerciendo los derechos bajo esta Licencia quien previamente no ha violado los t�rminos de esta Licencia con respecto a la Obra, o que ha recibido el permiso expreso del Licenciante para ejercer derechos bajo esta Licencia pese a una violacion anterior.\n    \"Comunicar publicamente\" significa realizar el rezo publico de la obra y comunicar al publico los relatos publicos, por cualquier medio o procedimiento, incluso por medios al�mbricos o inalambricos o la ejecucion digital p�blica, poner a disposicion de las obras publicas, de tal manera que los miembros del publico puedan acceder a estas obras desde el lugar y en un lugar que ellos elijan, para llevar a cabo la obra al publico por cualquier medio o procedimiento y la comunicaci�n al publico de las actuaciones de la Obra, incluyendo la publica digital rendimiento, para transmitir y retransmitir la obra por cualquier medio, incluyendo signos, sonidos o imagenes.\n    \"Reproducir\" significa hacer copias de la obra por cualquier medio, incluyendo sin limitaci�n, mediante grabaciones sonoras o visuales, y el derecho de la fijaci�n y reproducci�n de las fijaciones de la Obra, incluido el almacenamiento de una interpretacion o ejecucion protegida o de un fonograma en forma digital o cualquier otro medio electronico.\n. <//li><//p>\n<li><p>Parte 2: Feria de los Derechos Autor. Nada en esta licencia tiene por objeto reducir, limitar, o restringir los usos libres de derechos de autor o los derechos derivados de limitaciones o excepciones que se preven en relacion con la proteccion de los derechos de autor bajo la ley de derechos de autor u otras leyes aplicables.<//li><//p>\n<li><p>Parte 3: Concesion de licencia. Sujeto a los terminos y condiciones de esta Licencia, el Licenciante otorga a Usted una licencia mundial, libre de regalias, licencia no exclusiva, perpetua (por la duracion de los derechos de autor) para ejercer los derechos en el trabajo como se indica a continuacion:\n    - para reproducir la Obra, para incorporar la Obra en una o mas colecciones, ya reproducir la Obra incorporada en las colecciones;\n    -  para crear y reproducir adaptaciones a condicion de que cualquier adaptacion tales, incluida cualquier traduccion en cualquier medio, toma medidas razonables para etiquetar claramente, delimitar o identificar que los cambios se hicieron para la obra original. Por ejemplo, una traduccion debe marcarse como \"La obra original fue traducida del Ingles al Espa�ol\", o una modificacion podria indicar \"La obra original ha sido modificado.\";\n    -  para distribuir y comunicar publicamente la obra, incluyendo las incorporadas en las colecciones, y,\n    -  para distribuir y ejecutar publicamente adaptaciones.\n   -  Para evitar dudas:\n	Irrenunciable Esquemas licencia obligatoria. En aquellas jurisdicciones en las que el derecho a cobrar regalias a traves de cualquier sistema de licencias legales u obligatorio no se puede renunciar, el licenciador se reserva el derecho exclusivo de recaudar las regalias para cualquier ejercicio de su de los derechos garantizados por esta Licencia;\n       	Renunciable Esquemas licencia obligatoria. En aquellas jurisdicciones en las que se puede el derecho a cobrar regalias a traves de cualquier sistema de licencias legales u obligatorias renunciado, el Licenciante renuncia al derecho exclusivo de recaudar las regalias de cualquier ejercicio de su de los derechos concedidos bajo esta licencia, y,\n       	Planes voluntarios de licencia. El Licenciante renuncia al derecho a cobrar regalias, ya sea individualmente o, en caso de que el Licenciante sea miembro de una sociedad de gestion colectiva que administra los regimenes voluntarios de concesion de licencias, a traves de esa sociedad, de cualquier ejercicio, por su parte de los derechos concedidos bajo esta Licencia.\nLos derechos mencionados anteriormente pueden ser ejercidos en todos los medios y formatos, actualmente conocidos o por conocer. Los derechos antes mencionados incluyen el derecho a efectuar las modificaciones que sean tecnicamente necesarias para ejercer los derechos en otros medios y formatos. Sujeto a la Seccion 8 (f), todos los derechos no concedidos expresamente por el licenciador quedan reservados.\n.<//li><//p>\n<li><p>Parte 4: Restricciones.   La licencia otorgada en la anterior Seccion 3 esta expresamente sujeta y limitada por las siguientes restricciones:\na. Usted puede distribuir o comunicar publicamente la Obra solo bajo los terminos de esta Licencia. Usted debe incluir una copia de, o el Uniform Resource Identifier (URI), esta Licencia con cada copia de la Obra que Usted distribuya o ejecute publicamente. Usted no podra ofrecer o imponer ninguna condicion sobre la Obra que pueda restringir los terminos de esta licencia o la capacidad del beneficiario de la Obra para ejercer los derechos otorgados al receptor bajo los terminos de la licencia.Usted no puede sublicenciar la Obra. Usted debe mantener intactos todos los anuncios que se refieran a esta Licencia ya la limitacion de las garantias con cada copia de la Obra que Usted distribuya o ejecute publicamente. Cuando Usted distribuya o ejecute publicamente la Obra, Usted no puede imponer cualquier medida tecnologica efectiva sobre el trabajo que restringen la capacidad de un receptor de la Obra por el ejercicio de los derechos otorgados al receptor bajo los terminos de la licencia. Esta seccion 4 (a) se aplica a la Obra cuando es incorporada en una coleccion, pero esto no requiere que la coleccion, aparte de la obra misma quede sujeta a los terminos de esta Licencia. Si Usted crea una coleccion, previo aviso de cualquier Licenciante debe, en la medida de lo posible, quitar de la coleccion de cualquier credito requerido en la Seccion 4 (b), conforme a lo solicitado. Si Usted crea una Obra Derivada, previo aviso de cualquier Licenciante Usted debe, en la medida de lo posible, eliminar de la adaptacion de cualquier credito requerido en la Seccion 4 (b), conforme a lo solicitado.\nb.    Si usted distribuye o ejecuta publicamente la Obra o las adaptaciones o colecciones, para que, a menos que una solicitud ha sido hecha de conformidad con la Seccion 4 (a), mantenga intactos todos los avisos de derechos de autor para la Obra y proporcionar, razonable segun el medio o medios Usted este utilizando: (i) el nombre del autor original (o seudonimo, en su caso) si fue suministrado, y // o si el autor original y // o el Licenciante designa otra parte o partes (por ejemplo, un instituto patrocinador, editorial, una revista) para la atribucion (\"Partes del Reconocimiento\") en la nota de derechos de autor del Licenciante, terminos de servicio o por cualquier otro medio, el nombre de dicha parte o partes, (ii) el titulo de la obra si fue suministrado, (iii) en la medida en que sea posible, de la URI, en su caso, que el Licenciante especifica para ser asociado con la Obra, a menos que tal URI no se refiere a la nota de copyright o de la informacion de licencia para el trabajo, y (iv), de conformidad con la Seccion 3 (b), en el caso de una obra derivada, un aviso que identifique el uso de la Obra en la adaptacion (por ejemplo, \"Traduccion Francesa de la Obra del Autor Original,\" o \"guion basado en obra original de Autor Original\"). El credito requerido por esta Seccion 4 (b) puede ser implementado de cualquier forma razonable, siempre que, sin embargo, que en el caso de una adaptacion o una coleccion, a un minimo tal credito aparecera, si un credito para todos los autores que contribuyeron a la adaptacion o Coleccion aparece, a continuacion, como parte de estos creditos y de una manera al menos tan destacada como los creditos para los demas autores que contribuyeron. Para evitar dudas, usted solo podra utilizar el credito requerido por esta Seccion con el proposito de reconocimiento en la forma prevista anteriormente y, al ejercer sus derechos bajo esta Licencia, no podra implicita o explicitamente afirmar o implicar la conexion con, patrocinio o respaldo por parte del Autor Licenciante y // o partes de atribucion, en su caso, de usted o su uso de la obra, sin el permiso independiente, expreso, previo y por escrito de, el autor original Licenciante y // o partes de atribucion.\nc.    Salvo acuerdo en contrario por escrito por el Concedente o como puede ser permitido por la ley aplicable, en caso de que se reproduzca, distribuya o ejecute publicamente la Obra, ya sea por si mismo o como parte de las adaptaciones o colecciones, no deben modificarse, mutilarse, modificar o tomar cualquier otra accion despectiva en relacion con el trabajo que seria perjudicial para el honor del autor original o la reputacion. Licenciante de acuerdo en que en esas jurisdicciones (por ejemplo, Japon), en el que cualquier ejercicio del derecho otorgado en la Seccion 3 (b) de esta licencia (el derecho a hacer adaptaciones) se considera una deformacion, mutilacion, modificacion o accion que atente otra perjudicial para el honor del autor original y la reputacion, el Licenciante renuncia o afirmar que no, segun proceda, la presente seccion, en la maxima medida permitida por la legislacion nacional aplicable, para que pueda ejercer razonablemente su derecho en virtud de la Seccion 3 (b) de esteLicencia (derecho a hacer adaptaciones) pero por lo demas no.\n<//li><//p>\n<li><p>Parte 5: Representaciones, Garantias y Limitacion de Responsabilidad.   A MENOS QUE SE ACUERDE MUTUAMENTE POR ESCRITO ENTRE LAS PARTES, EL LICENCIANTE OFRECE LA OBRA TAL COMO ESTA Y NO HACE NINGUNA REPRESENTACION O GARANTIA DE NINGUN TIPO RESPECTO DE LA OBRA, EXPRESA, IMPLICITA, LEGAL O DE OTRO TIPO, INCLUYENDO, SIN LIMITACION, GARANTIAS DE TITULO, COMERCIALIZACION, IDONEIDAD PARA UN PROPOSITO PARTICULAR, NO INFRACCION, O LA AUSENCIA DE DEFECTOS LATENTES O DE OTRO TIPO, EXACTITUD, O LA PRESENCIA O AUSENCIA DE ERRORES, SEAN O NO SER DESCUBIERTOS. ALGUNAS JURISDICCIONES NO PERMITEN LA EXCLUSION DE GARANTIAS IMPLICITAS, LA EXCLUSION PUEDE NO APLICARSE EN SU CASO.<//li><//p>\n<li><p>Parte  6:. Limitacion de la responsabilidad. A MENOS QUE LO REQUERIDO POR LAS LEYES APLICABLES, EN NINGUN CASO EL LICENCIANTE SERA RESPONSABLE ANTE USTED POR CUALQUIER OTRA TEORIA LEGAL POR CUALQUIER DA�O ESPECIAL, INCIDENTAL, CONSECUENTE, punitivos o ejemplares, PROVENIENTE DE ESTA LICENCIA O DEL USO DE LA OBRA, AUN CUANDO EL LICENCIANTE HAYA SIDO ADVERTIDO DE LA POSIBILIDAD DE TALES DA�OS.<//li><//p>\n<li><p>Parte 7: Terminacion.     Esta Licencia y los derechos de las concedidas en virtud de ella terminaran automaticamente si Usted incumple cualquiera de los terminos de esta Licencia. Las personas o entidades que hayan recibido obras o colecciones de usted bajo esta Licencia, sin embargo, no veran sus licencias finalizadas, siempre que estos individuos o entidades sigan cumpliendo integramente las condiciones de estas licencias. Las secciones 1, 2, 5, 6, 7, y 8 subsistiran a cualquier terminacion de esta Licencia.\n    Sujeto a los terminos y condiciones anteriores, la licencia otorgada aqui es perpetua (por la duracion de los derechos de autor de la obra). No obstante lo anterior, el Licenciante se reserva el derecho de difundir la Obra bajo diferentes terminos de Licencia o de detener la distribucion de la Obra en cualquier momento, siempre que, sin embargo, que ninguna de tales elecciones servira para revocar esta Licencia (o cualquier otra licencia que haya sido, o se requiere para ser concedida bajo los terminos de esta licencia), y esta licencia continuara en pleno vigor y efecto a menos que termine como se indico anteriormente.\n<//li><//p>\n<li><p>Parte 8:  Miscelaneo.     Cada vez que Usted distribuya o ejecute publicamente la Obra o una Coleccion, el Licenciante ofrecera al destinatario una licencia para la Obra en los mismos terminos y condiciones que la licencia concedida a Usted bajo esta Licencia.\n    Cada vez que Usted distribuya o ejecute publicamente una Obra Derivada, el Licenciante ofrecera al destinatario una licencia para la Obra originaria en los mismos terminos y condiciones que la licencia concedida a Usted bajo esta Licencia.\n    Si alguna disposicion de esta Licencia es invalida o no exigible bajo la ley aplicable, no afectara la validez o exigibilidad del resto de los terminos de esta Licencia, y sin mas accion de las partes de este acuerdo, tal disposicion sera reformada en la lo minimo necesario para que sea valida y exigible.\n    Ningun termino o disposicion de esta Licencia se estimara renunciada y ninguna violacion consentida a menos que tal renuncia o consentimiento figure por escrito y firmado por las partes que seran afectadas por tal renuncia o consentimiento.\n    Esta Licencia constituye el acuerdo completo entre las partes con respecto a la Obra licenciada aqui.No hay entendimientos, acuerdos o representaciones con respecto a la Obra que no esten especificados aqui. El Licenciante no sera obligado por ninguna disposicion adicional que pueda aparecer en cualquier comunicacion proveniente de Usted. Esta Licencia no puede ser modificada sin el mutuo acuerdo por escrito entre el Licenciante y Usted.\n    Los derechos concedidos en virtud de, y hace referencia a la materia, en la presente Licencia se elaboraron utilizando la terminologia de la Convencion de Berna para la Proteccion de las Obras Literarias y Artisticas (en su forma enmendada el 28 de septiembre de 1979), el Convenio de Roma de 1961, el Derecho de Autor de la OMPI Tratado de 1996, la OMPI sobre Interpretacion o Ejecucion y Fonogramas de 1996 y la Convencion Universal sobre Derecho (en su version revisada el 24 de julio de 1971). Estos derechos y prestaciones surtira efecto en la jurisdiccion correspondiente en el que los terminos de licencia se pide que se ejecuten de acuerdo a las disposiciones correspondientes de la aplicacion de las disposiciones de los tratados en la legislacion nacional aplicable. Si el paquete estandar de los derechos concedidos en virtud de la ley derechos de autor incluyen los derechos adicionales no concedidos en virtud de esta licencia, esos derechos adicionales se consideraran incluidos en la licencia, esta licencia no se pretende restringir la licencia de los derechos bajo la ley aplicable.\n<//li><//p>\n<//ol>";

		StringReader lector = new StringReader(texto); // aquí se abre un
		// InputStream con
		// la cadena que esta en memoria
		try {
			PreparedStatement ps = conn.prepareStatement("update red_licencias set LCNC_DESCRIPCION= ? where LCNC_CONSECUTIVO=9");
			ps.setCharacterStream(1, lector, texto.length());
			ps.execute();
			conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Encripta string
	 * 
	 * @param cadena
	 * @return string encriptado
	 */
	public static String encriptar(String cadena) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
		return s.encrypt(cadena);
	}

	/**
	 * Desencripta string
	 * 
	 * @param cadena
	 * @return string desencriptado
	 */
	public static String desencriptar(String cadena) {
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
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
	 * Encripta password con md5
	 * 
	 * @param clearTextPassword
	 * @return clave encriptada
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncodedPassword(String clearTextPassword) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(clearTextPassword.getBytes());
		// byte[] theDigest = md.digest();
		// return new BASE64Encoder().encode(theDigest);
		return clearTextPassword;
	}

	/**
	 * Genera nuevo password
	 * 
	 * @return password generado
	 */
	public static String getNewPassword() {
		// 48 a 57 numeros
		// 65 a 90 mayusculas
		// 97 a 122 minusculas
		char tmp;
		int ascii, cont = 0;
		String pass = "";
		while (cont < 7) {
			ascii = 48 + (int) (Math.random() * 74);
			if (ascii < 58 || (ascii > 64 && ascii < 91) || ascii > 96) {
				tmp = (char) ascii;
				pass = pass + tmp;
				cont++;
			}
		}
		return pass;
	}

	/**
	 * Prueba escritura en archivo
	 */
	public void pruebaEscritura() {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("//172.16.1.141/Modelo_Raster/Varios/Monitoreo_Forestal/REDD/Metadatos/test.xml");
			pw = new PrintWriter(fichero);

			for (int i = 0; i < 10; i++)
				pw.println("Linea " + i);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				// Nuevamente aprovechamos el finally para
				// asegurarnos que se cierra el fichero.
				if (null != fichero) fichero.close();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Mueve archivo
	 * 
	 * @param sourceFile
	 * @param destinationFile
	 */
	public static void fileMove(String sourceFile, String destinationFile) {
		try {
			File inFile = new File(sourceFile);
			File outFile = new File(destinationFile);

			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(outFile);

			int c;
			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();

			// File file = new File(sourceFile);
			// if (file.exists()) {
			// file.delete();
			// }

		}
		catch (Exception e) {
			System.err.println("Hubo un error de entrada/salida!!!");
			e.printStackTrace();
		}
	}

	/**
	 * Borra directorio
	 * 
	 * @param directorio
	 */
	public static void borrarDirectorio(File directorio) {

		File[] ficheros = directorio.listFiles();

		for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				borrarDirectorio(ficheros[x]);
			}
			ficheros[x].delete();
		}
	}

	/**
	 * Retorna info metadato
	 * 
	 * @param k
	 * @return info metadato
	 */
	public static String infoMetadato(String k) {
		String configLog = "co/gov/ideamredd/recursos/infoMetadato";
		ResourceBundle prop = ResourceBundle.getBundle(configLog);
		return prop.getString(k);
	}

	/**
	 * Extrae zip
	 * 
	 * @param rutaZip
	 * @param rutaSalida
	 * @throws Exception
	 */
	public static void UnZip(String rutaZip, String rutaSalida) throws Exception {

		ZipInputStream zis = new ZipInputStream(new FileInputStream(rutaZip));
		ZipEntry entrada;
		while (null != (entrada = zis.getNextEntry())) {
			System.out.println(entrada.getName());

			FileOutputStream fos = new FileOutputStream(rutaSalida + entrada.getName());
			int leido;
			byte[] buffer = new byte[1024];
			while (0 < (leido = zis.read(buffer))) {
				fos.write(buffer, 0, leido);
			}
			fos.close();
			zis.closeEntry();
		}
		zis.close();
	}

}
