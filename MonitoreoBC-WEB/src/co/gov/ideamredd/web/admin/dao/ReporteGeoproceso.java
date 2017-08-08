// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.ejb.EJB;

import co.gov.ideamredd.mbc.auxiliares.Archivo;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Métodos para extraer datos de reportes de biomasa y carbono, bosque no bosque, cambio de cobertura de una imágen e importarla a la base de datos.
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class ReporteGeoproceso {

	private static String	carpeta_salida_geoprocesos	= "";
	private static String	comando_python				= "";
	private static String	carpeta_extractor			= "";
	private static String	extension_imagen_geoproceso	= "";
	private static String	carpeta_csv					= "";
	
	/**
	 * Realiza el llamado a extraer e importar la información desde un archivo CSV
	 * 
	 * @param divisionTerritorio
	 * @param tipoReporte
	 * @param periodoUno
	 * @param periodoDos
	 * @param identImagen
	 * @return String con log de ejecución
	 * @throws IOException
	 */
	public static String generar(Integer divisionTerritorio, Integer tipoReporte, Integer periodoUno, Integer periodoDos, String identImagen, String nombreDivisionTerritorio) throws IOException {

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		// VALIDAR LAS PRECONDICIONES

		boolean procesar = true;

		String retorno = "";
		Integer dif_periodo = 0;

		// divisionTerritorio = 1;
		// tipoReporte = 1;
		// periodoUno = 2016;
		// periodoDos = -1;
		// identImagen = "TEST";

		if (divisionTerritorio == -1) {
			procesar = false;
			retorno += Auxiliar.mensajeImpersonal("advertencia", "División territorial no válida.");
		}
		
		if (tipoReporte == 5) {
			if (!Auxiliar.tieneAlgo(nombreDivisionTerritorio)) {
				procesar = false;
				retorno += Auxiliar.mensajeImpersonal("advertencia", "Para el tipo de reporte de deforestación también debe especificar el nombre de la división territorial.");
			}
		}
		
		if (periodoUno == -1) {
			periodoUno = null;
		}
		if (periodoDos == 0) {
			periodoDos = null;
		}

		Integer[] periodos = new Integer[2];
		periodos[0] = periodoUno;
		periodos[1] = periodoDos;

		if (!Auxiliar.esPeriodo(periodoUno)) {
			procesar = false;
			retorno += Auxiliar.mensajeImpersonal("advertencia", "Período Uno no válido.");
		}

		if (periodoDos != null) {
			if (!Auxiliar.esPeriodo(periodoDos)) {
				retorno += Auxiliar.mensajeImpersonal("advertencia", "Período Dos no válido.");
			}
			else {
				dif_periodo = periodoDos - periodoUno;
			}
		}

		if (!Auxiliar.tieneAlgo(identImagen)) {
			procesar = false;
			retorno += Auxiliar.mensajeImpersonal("advertencia", "Identificador de geoproceso no válido.");
		}

		String existe_tiporeporte = "";

		if (tipoReporte != null) {
			existe_tiporeporte = ConexionBD.obtenerDato("SELECT TPRP_CONSECUTIVO FROM RED_TIPOREPORTE WHERE TPRP_CONSECUTIVO=" + tipoReporte, "", null);
			if (!Auxiliar.tieneAlgo(existe_tiporeporte)) {
				procesar = false;
				retorno += Auxiliar.mensajeImpersonal("advertencia", "Tipo de reporte no válido.");
			}
		}
		else {
			procesar = false;
			retorno += Auxiliar.mensajeImpersonal("advertencia", "Tipo de reporte no especificado.");
		}

		// SI TODAS LAS PRECONDICIONES SE CUMPLEN PROCESAR IMAGEN

		if (procesar) {
			try {
				retorno += importarDatosImagen(divisionTerritorio, tipoReporte, periodoUno, periodoDos, identImagen, dif_periodo, nombreDivisionTerritorio);
			}
			catch (Exception e) {
				e.printStackTrace();
				retorno += Auxiliar.mensajeImpersonal("error", "No se pudieron extraer los datos de la imagen " + identImagen + ": " + e.toString());
			}
		}

		return retorno;

	}

	/**
	 * Ejecuta un comando python para la generación de un archivo CSV y llama al método que la importa
	 * 
	 * @param divisionTerritorio
	 * @param tipoReporte
	 * @param periodoUno
	 * @param periodoDos
	 * @param identImagen
	 * @param dif_periodo
	 * @return String con log de ejecución
	 */
	private static String importarDatosImagen(Integer divisionTerritorio, Integer tipoReporte, Integer periodoUno, Integer periodoDos, String identImagen, Integer dif_periodo, String nombreDivisionTerritorio) {
		String r = "";

		String ruta_imagen_original = "";
		String ruta_imagen_procesada = "";

		// OBTENER CONSTANTES DE PROCESAMIENTO

		carpeta_salida_geoprocesos = Parametro.getParametro("carpeta_salida_geoprocesos");
		comando_python = Parametro.getParametro("comando_python");
		carpeta_extractor = Parametro.getParametro("carpeta_extractor");
		extension_imagen_geoproceso = Parametro.getParametro("extension_imagen_geoproceso");

		// ESTABLECER LA OPCIÓN DE CONSOLIDACION SEGÚN LOS FILTROS DE ENTRADA

		String opcion = "";

		switch (tipoReporte) {
			case 1:
				opcion = divisionTerritorio == 4 ? "consolidado_bnb" : "bnb";
				break;
			case 3:
				opcion = divisionTerritorio == 4 ? "consolidado_cambio" : "cambio";
				break;
			case 5:
				opcion = divisionTerritorio == 4 ? "consolidado_tipificacion" : "tipificacion";
				break;
			case 7:
				opcion = divisionTerritorio == 4 ? "consolidado_byc" : "byc";
				break;
			default:
				break;
		}

		// DETERMINAR LA RUTA DE LA IMAGEN

		String commando_ruta_imagen = "/usr/bin/sh " + carpeta_extractor + "/finder.sh " + carpeta_salida_geoprocesos + " " + identImagen + extension_imagen_geoproceso + " " + carpeta_extractor;

		try {
			String resultado_ruta_imagen = Auxiliar.commander(commando_ruta_imagen, commando_ruta_imagen);
			String[] a_resultado_ruta_imagen = resultado_ruta_imagen.split("-=-");
			if (!a_resultado_ruta_imagen[0].equals("0")) {
				r += a_resultado_ruta_imagen[1];
				return r;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			r += e.toString();
		}

		ruta_imagen_original = Archivo.leerNLineasDelArchivo(carpeta_extractor + "/ruta_imagen.txt", 1);

		if (!Archivo.existeArchivo(ruta_imagen_original)) {
			return r + Auxiliar.mensajeImpersonal("advertencia", "No encontré el archivo de imagen resultado del geoproceso en la ruta:[" + ruta_imagen_original + "].");
		}

		if (extension_imagen_geoproceso.equals(".img")) {
			// COPIAR LA IMAGEN A UNA CARPETA LOCAL
	
			String nombre_imagen_preparado = identImagen.replace("_", "") + extension_imagen_geoproceso;
			String commando_transferir = "cp " + ruta_imagen_original + " " + carpeta_extractor + "/geoprocesos/" + nombre_imagen_preparado;
			try {
				Auxiliar.commander(commando_transferir, commando_transferir);
				ruta_imagen_procesada = carpeta_extractor + "/geoprocesos/" + nombre_imagen_preparado;
			}
			catch (Exception e) {
				e.printStackTrace();
				r += Auxiliar.mensajeImpersonal("error", "No se pudo transferir la imagen " + commando_transferir);
				return r;
			}
	
			if (!Archivo.existeArchivo(ruta_imagen_procesada)) {
				return r + Auxiliar.mensajeImpersonal("error", "No se pudo transferir la imagen a la carpeta local  (" + ruta_imagen_procesada + ").");
			}
	
			// SACAR UN BACKUP DE LA IMAGEN ORIGINAL (SI AUN NO EXISTE)
	
			String ruta_imagen_original_original = ruta_imagen_original + ".original";
			if (!Archivo.existeArchivo(ruta_imagen_original_original)) {
				String commando_backup = "cp " + ruta_imagen_original + " " + ruta_imagen_original + ".original";
				try {
					Auxiliar.commander(commando_backup, commando_backup);
				}
				catch (Exception e) {
					e.printStackTrace();
					r += Auxiliar.mensajeImpersonal("error", "No se pudo sacar un backup de la imagen original" + commando_backup);
					return r;
				}
			}
		}
		else if (extension_imagen_geoproceso.equals(".tif")) {
			ruta_imagen_procesada = ruta_imagen_original; 
			String ruta_imagen_procesada_zip = carpeta_extractor + "/Mapas/" +identImagen + ".zip" ;
			//dgr
			if (Archivo.existeArchivo(ruta_imagen_procesada_zip)) {
				String commando_transferir = "rm -r " + ruta_imagen_procesada_zip;
				Auxiliar.commander(commando_transferir, commando_transferir);
				r = Auxiliar.mensajeImpersonal("existeArchivo:", "comando: "+commando_transferir);
			}
			//dgr
			String sourceFiles = carpeta_salida_geoprocesos + "/" + identImagen +".tif " + carpeta_salida_geoprocesos + "/" + identImagen +".rrd " + carpeta_salida_geoprocesos + "/" + identImagen +".aux";
			r += zipArrayFiles(ruta_imagen_procesada_zip, sourceFiles);
			
		}
		else {
			r += Auxiliar.mensajeImpersonal("error", "Tipo de extension no soportado: " + extension_imagen_geoproceso);
			return r;
		}
		
		// EJECUTAR SCRIPT DE PYTHON PARA EXTRAER LOS DATOS DE LA IMAGEN Y GUARDARLOS EN UN CSV

		// CREAR ARCHIVO DE PARÁMETROS DE EJECUCIÓN DEL SCRIPT DE PYTHON

		String nombre_archivo_ini = carpeta_extractor + "/extractor.ini";
		String ruta_archivo_ini = nombre_archivo_ini;

		boolean ok_archivo_ini = false;

		String ini = "";
		ini += "opcion=" + opcion + "\n";
		ini += "ruta_imagen_Str=" + ruta_imagen_procesada + "\n";
		ini += "dif_periodo=" + dif_periodo + "\n";

		try {
			ok_archivo_ini = Archivo.escribirArchivo(ruta_archivo_ini, ini);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}

		if (!ok_archivo_ini) {
			return r + Auxiliar.mensajeImpersonal("error", "No se pudo crear el archivo de iniciación del extractor  (" + ruta_archivo_ini + ").");
		}

		// EJECUTAR SCRIPT

		String commando = "";
		try {
			commando = comando_python + " " + carpeta_extractor + "/extractor.py " + identImagen + " " + carpeta_extractor;
			r += Auxiliar.mensajeImpersonal("nota", "Ejecutando...");
			String resultado_generacion = Auxiliar.commander(commando, commando);
			String[] a_resultado_generacion = resultado_generacion.split("-=-");
			if (!a_resultado_generacion[0].equals("0") && !a_resultado_generacion[0].equals("139")) {
				r += Auxiliar.mensajeImpersonal("error", "Error al ejecutar el comando de extracción:<br>" + a_resultado_generacion[1]);
			}
			else {
				r += Auxiliar.mensajeImpersonal("confirmacion", "Resultado de la extracción:<br>" + a_resultado_generacion[1]);

				// SI LA EXTRACCIÓN FUE EXITOSA, IMPORTAR LA INFORMACIÓN DEL CSV A LA BASE DE DATOS

				r += importarDatosCSV(divisionTerritorio, tipoReporte, periodoUno, periodoDos, identImagen, ruta_imagen_procesada, nombreDivisionTerritorio);

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			r += Auxiliar.mensajeImpersonal("error", "No se pudo ejecutar el proceso de extracción de datos con el comando " + commando);
			return r;
		}

		
		if (extension_imagen_geoproceso.equals(".img")) {
			// COPIAR LA IMAGEN PROCESADA A LA CARPETA ORIGINAL
	
			String commando_devolver = "mv " + ruta_imagen_procesada + " " + ruta_imagen_original;
			try {
				Auxiliar.commander(commando_devolver, commando_devolver);
			}
			catch (Exception e) {
				e.printStackTrace();
				r += Auxiliar.mensajeImpersonal("error", "No se pudo actualizar la imagen original" + commando_devolver);
				return r;
			}
	
			if (!Archivo.existeArchivo(ruta_imagen_original)) {
				return r + Auxiliar.mensajeImpersonal("error", "No se pudo transferir la imagen a la carpeta original (" + commando_devolver + ").");
			}
		}
	
		return r;
	}

	public static String zipArrayFiles(String rutaZip, String sourceFiles) {
		String r = "";
		try {                     
			String commando_transferir = "zip -r " + rutaZip + " " +sourceFiles;
			Auxiliar.commander(commando_transferir, commando_transferir);
			r = Auxiliar.mensajeImpersonal("zipImagenes", "comando: "+commando_transferir);
		}
		catch(Exception ioe) {
			ioe.printStackTrace();
			r = Auxiliar.mensajeImpersonal("error", "zipArrayFiles: No se pudo comprimir la imagen " + ioe.getMessage());
		}	
		return r;
	}

	
	/**
	 * Importa a la base de datos la información extraida desde el archivo CSV
	 * 
	 * @param divisionTerritorio
	 * @param tipoReporte
	 * @param periodoUno
	 * @param periodoDos
	 * @param identImagen
	 * @param ruta_imagen
	 * @return String con log de ejecución
	 */
	private static String importarDatosCSV(Integer divisionTerritorio, Integer tipoReporte, Integer periodoUno, Integer periodoDos, String identImagen, String ruta_imagen, String nombreDivisionTerritorio) {
		String r = "";

		Connection conn = ConexionBD.establecerConexion();

		// VALIDAR QUE EL CSV EXISTA

		carpeta_csv = Parametro.getParametro("carpeta_csv");
		String nombre_archivo_csv = identImagen + ".csv";
		String ruta_archivo_csv = carpeta_csv + "/" + nombre_archivo_csv;

		boolean csv_existe = Archivo.existeArchivo(ruta_archivo_csv);
		if (!csv_existe) {
			return r + Auxiliar.mensajeImpersonal("advertencia", "El archivo CSV " + ruta_archivo_csv + " no existe.");
		}

		// VALIDAR QUE LA IMAGEN EXISTA

		if (!Archivo.existeArchivo(ruta_imagen)) {
			return r + Auxiliar.mensajeImpersonal("advertencia", "No encontré el archivo de imagen resultado del geoproceso en la ruta al intentar extraer la información: [" + ruta_imagen + "].");
		}

		String fecha_generacion = Archivo.fechaModificacion(ruta_imagen);
		String sql_fecha_generacion = "";
		if (!Auxiliar.tieneAlgo(fecha_generacion)) {
			sql_fecha_generacion = "SYSDATE";
		}
		else {
			sql_fecha_generacion = "TO_DATE('" + fecha_generacion + "', 'YYYY-MM-DD HH24:MI:SS')";
		}

		String sql = "";
		String id_reporte = "";

		// AVERIGUAR SI EXISTE UN REPORTE PARA LOS FILTROS DADOS

		boolean ok_reporte = false;
		String sql_id_reporte_existente = "SELECT RPRT_CONSECUTIVO FROM RED_REPORTES WHERE  ";
		sql_id_reporte_existente += " RPRT_DIVISIONTERRITORIO=" + divisionTerritorio;
		sql_id_reporte_existente += " AND RPRT_TIPOREPORTE=" + tipoReporte;
		sql_id_reporte_existente += " AND RPRT_PERIODOUNO=" + periodoUno;
		sql_id_reporte_existente += periodoDos != null ? " AND RPRT_PERIODODOS=" + periodoDos : " AND RPRT_PERIODODOS IS NULL";
		if (tipoReporte == 5 && Auxiliar.tieneAlgo(nombreDivisionTerritorio)) {
			sql_id_reporte_existente += " AND RPRT_CONSECUTIVO IN (SELECT ID_REPORTE FROM RED_INFOREPORTES WHERE DIVISION_TERRITORIAL= '" + nombreDivisionTerritorio + "') ";
		}
		id_reporte = ConexionBD.obtenerDato(sql_id_reporte_existente, "", null);

		// INICIAR LA TRANSACCIÓN DE IMPORTACIÓN

		try {
			ConexionBD.establecerAutoCometer(false, conn);
		}
		catch (Exception e) {
			ConexionBD.desconectarse(conn);
			return r + Auxiliar.mensajeImpersonal("error", "Error al establecer el auto commit");
		}

		String identImagen_actual = "";

		try {
			boolean ok_eliminarinfo = false;

			if (!Auxiliar.tieneAlgo(id_reporte)) {
				// CREAR REPORTE SI NO EXISTE
				sql = "INSERT INTO RED_REPORTES ";
				sql += " (RPRT_FECHAGENERACION, RPRT_DIVISIONTERRITORIO, RPRT_PERIODOUNO, RPRT_PERIODODOS, RPRT_TIPOREPORTE, RPRT_PUBLICADO, RPRT_IDENTIMAGEN) ";
				sql += " VALUES (" + sql_fecha_generacion + "," + divisionTerritorio + ", " + periodoUno + ", " + periodoDos + ", " + tipoReporte + ", 1, '" + identImagen + "')";

				ok_eliminarinfo = true;
			}
			else {
				// OBTENER LA IMAGEN ACTUAL POR SI ES NECESARIO BORRARLA
				String sql_identImagen_actual = "SELECT RPRT_IDENTIMAGEN FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte;
				identImagen_actual = ConexionBD.obtenerDato(sql_identImagen_actual, "", conn);

				// SI EXISTE, ACTUALIZARLO
				sql = "UPDATE RED_REPORTES SET ";
				sql += "RPRT_FECHAGENERACION=" + sql_fecha_generacion;
				sql += ",RPRT_DIVISIONTERRITORIO=" + divisionTerritorio;
				sql += ",RPRT_PERIODOUNO=" + periodoUno;
				sql += ",RPRT_PERIODODOS=" + periodoDos;
				sql += ",RPRT_TIPOREPORTE=" + tipoReporte;
				sql += ",RPRT_IDENTIMAGEN='" + identImagen + "'";
				sql += " WHERE RPRT_CONSECUTIVO=" + id_reporte;

				// ... Y ELIMINAR LOS SUB-REGISTROS
				ok_eliminarinfo = ConexionBD.ejecutarSQL("DELETE FROM RED_INFOREPORTES WHERE ID_REPORTE=" + id_reporte, conn);
				if (!ok_eliminarinfo) {
					ConexionBD.desconectarse(conn);
					return r + Auxiliar.mensajeImpersonal("error", "Excepción al eliminar los registros del reporte.");
				}
			}

			ok_reporte = ConexionBD.ejecutarSQL(sql, conn);
			if (ok_reporte) {
				sql_id_reporte_existente = "SELECT RPRT_CONSECUTIVO FROM RED_REPORTES WHERE  ";
				sql_id_reporte_existente += " RPRT_DIVISIONTERRITORIO=" + divisionTerritorio;
				sql_id_reporte_existente += " AND RPRT_TIPOREPORTE=" + tipoReporte;
				sql_id_reporte_existente += " AND RPRT_IDENTIMAGEN='" + identImagen + "'";
				sql_id_reporte_existente += " AND RPRT_PERIODOUNO=" + periodoUno;
				sql_id_reporte_existente += periodoDos != null ? " AND RPRT_PERIODODOS=" + periodoDos : " AND RPRT_PERIODODOS IS NULL";
				id_reporte = ConexionBD.obtenerDato(sql_id_reporte_existente, "", conn);

				if (!Auxiliar.tieneAlgo(id_reporte)) {
					ConexionBD.desconectarse(conn);
					return r + Auxiliar.mensajeImpersonal("error", "Excepción al consultar el reporte existente: " + sql_id_reporte_existente);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			ConexionBD.desconectarse(conn);
			return r + Auxiliar.mensajeImpersonal("error", "Excepción al ejecutar la consulta " + sql);
		}

		if (!ok_reporte) {
			ConexionBD.desconectarse(conn);
			return r + Auxiliar.mensajeImpersonal("error", "No se pudo ejecutar la consulta para crear o actualizar la versión " + identImagen + " del reporte: " + sql);
		}

		// EJECUTAR LA IMPORTACIÓN DE LA INFORMACIÓN DEL CSV

		String s = ",";
		String sql_importacion = "";
		boolean ok_importacion = false;
		boolean ok_importaciones = true;
		BufferedReader br = null;
		int lineas_procesadas = 0;

		try {
			// INICIAR PROCESAMIENTO DEL ARCHIVO LINEA POR LINEA

			InputStream inputStream = new FileInputStream(ruta_archivo_csv);
			// Reader reader = new InputStreamReader(inputStream, "UTF-8");
			Reader reader = new InputStreamReader(inputStream, "windows-1252");
			br = new BufferedReader(reader);

			String[] a_valores = null;

			String linea = "";

			linea = br.readLine();

			String area_ha = "";
			String histogram = "";
			String clasificacion = "";
			String territorial_division = "";
			String incertidumbre = "";
			String error_estandar = "";
			String baj = "";
			String bat = "";
			String caj = "";
			String cat = "";
			String coe = "";
			String coet = "";

			linea = br.readLine();

			while (linea != null) {
				a_valores = linea.split(s);

				int n_valores = a_valores.length;

				area_ha = Auxiliar.nz(a_valores[0].trim().replace("\"", ""), "");
				histogram = Auxiliar.nz(a_valores[1].trim().replace("\"", ""), "").replace(",", ".");
				clasificacion = Auxiliar.nz(a_valores[2].trim().replace("\"", ""), "");
				if (!Auxiliar.tieneAlgo(clasificacion)) {
					linea = br.readLine();
					continue;
				}

				territorial_division = n_valores >= 4 ? Auxiliar.nz(a_valores[3].trim().replace("\"", ""), "") : "";
				if (!Auxiliar.tieneAlgo(territorial_division)) {
					linea = br.readLine();
					continue;
				}

				incertidumbre = n_valores >= 5 ? Auxiliar.nz(a_valores[4], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				error_estandar = n_valores >= 6 ? Auxiliar.nz(a_valores[5], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				baj = n_valores >= 7 ? Auxiliar.nz(a_valores[6], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				bat = n_valores >= 8 ? Auxiliar.nz(a_valores[7], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				caj = n_valores >= 9 ? Auxiliar.nz(a_valores[8], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				cat = n_valores >= 10 ? Auxiliar.nz(a_valores[9], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				coe = n_valores >= 11 ? Auxiliar.nz(a_valores[10], "NULL").trim().replace("\"", "").replace(",", ".") : "0";
				coet = n_valores >= 12 ? Auxiliar.nz(a_valores[11], "NULL").trim().replace("\"", "").replace(",", ".") : "0";

				// INSERTAR EL SUB-REGISTRO

				sql_importacion = "INSERT INTO RED_INFOREPORTES (ID_REPORTE, AREA, HISTOGRAMA, CLASIFICACION, DIVISION_TERRITORIAL, INCERTIDUMBRE, ERROR_ESTANDAR, BAJ, BAT, CAJ, CAT, COE, COET)";
				sql_importacion += " VALUES (" + id_reporte + ", " + area_ha + ", " + histogram + ", '" + clasificacion + "', '" + territorial_division + "', " + incertidumbre + ", " + error_estandar + ", " + baj + ", " + bat + ", " + caj + ", " + cat + ", " + coe + ", " + coet + ")";

				ok_importacion = ConexionBD.ejecutarSQL(sql_importacion, conn);

				ok_importaciones = (ok_importaciones && ok_importacion);
				if (!ok_importacion) {
					r += Auxiliar.mensajeImpersonal("advertencia", "No se pudo ejecutar la consulta para registrar un dato del reporte: [" + sql_importacion + "]");
				}
				lineas_procesadas++;

				linea = br.readLine();
			}

			if (ok_importaciones) {
				ConexionBD.cometerTransaccion(conn);
				r += Auxiliar.mensajeImpersonal("confirmacion", lineas_procesadas + " registros procesados!");
				ConexionBD.establecerAutoCometer(true, conn);

				if (ok_importaciones && extension_imagen_geoproceso.equals(".img")) {
					// SI CAMBIO LA IMAGEN Y SE IMPORTO BIEN ENTONCES ELIMINAR LA IMAGEN ANTERIOR
					if (Auxiliar.tieneAlgo(identImagen_actual)) {
						if (!identImagen_actual.equals(identImagen)) {
							r += Auxiliar.mensajeImpersonal("nota", "La imagen cambió.  Revisando si debe ser eliminada la versión anterior (" + identImagen_actual + ")...");
							// VERIFICAR QUE NINGUN REPORTE ESTE HACIENDO REFERENCIA A LA IMAGEN A ELIMINAR
							String conteo_referencias_imagen = ConexionBD.obtenerDato("SELECT RPRT_CONSECUTIVO FROM RED_REPORTES WHERE RPRT_IDENTIMAGEN='" + identImagen_actual + "'", "", conn);
							if (!Auxiliar.tieneAlgo(conteo_referencias_imagen)) {
								r += Auxiliar.mensajeImpersonal("nota", "No existen reportes que hagan referencia de la imagen antigua " + identImagen_actual + ". Eliminando carpeta...");
								r += eliminarCarpetaGeoproceso(identImagen_actual);
							}
						}
					}
				}
			}

			ConexionBD.desconectarse(conn);
		}
		catch (Exception e) {
			e.printStackTrace();
			ConexionBD.desconectarse(conn);
			return r + Auxiliar.mensajeImpersonal("error", "Error al procesar una línea del " + ruta_archivo_csv + ": " + e.toString());
		}
		finally {
			if (br != null) {
				try {
					br.close();
					ConexionBD.desconectarse(conn);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return r;
	}

	/**
	 * Una vez extraida la información del archivo CSV, elimina la imagen temporal desde la que python extrajo los datos
	 * 
	 * @param identImagen
	 * @return String log de ejecución
	 */
	public static String eliminarCarpetaGeoproceso(String identImagen) {
		String r = "";
		
		if (!extension_imagen_geoproceso.equals(".img")) {
			r += Auxiliar.mensajeImpersonal("advertencia", "Se impidio la eliminacion de la carpeta de geoprocesos TIFF...");
			return r;
		}
		
		// DETERMINAR LA RUTA DE LA IMAGEN

		r += Auxiliar.mensajeImpersonal("nota", "Buscando carpeta de la imagen antigua " + identImagen);
		String commando_ruta_imagen = "/usr/bin/sh " + carpeta_extractor + "/finder.sh " + carpeta_salida_geoprocesos + " " + identImagen + extension_imagen_geoproceso + " " + carpeta_extractor;

		try {
			String resultado_ruta_imagen = Auxiliar.commander(commando_ruta_imagen, commando_ruta_imagen);
			String[] a_resultado_ruta_imagen = resultado_ruta_imagen.split("-=-");
			if (!a_resultado_ruta_imagen[0].equals("0")) {
				r += a_resultado_ruta_imagen[1];
				return r;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			r += e.toString();
		}

		String ruta_imagen = Archivo.leerNLineasDelArchivo(carpeta_extractor + "/ruta_imagen.txt", 1);

		if (!Archivo.existeArchivo(ruta_imagen)) {
			return r + Auxiliar.mensajeImpersonal("advertencia", "No encontré el archivo de imagen anterior del geoproceso en la ruta:[" + ruta_imagen + "].");
		}

		String carpeta_geoproceso = ruta_imagen.replace("/" + identImagen + extension_imagen_geoproceso, "");
		r += Auxiliar.mensajeImpersonal("nota", "Carpeta de la imagen antigua " + identImagen + ": " + carpeta_geoproceso);

		// ELIMINAR LA CARPETA DEL GEOPROCESO QUE YA NO SE USA

		String parametro_eliminar_imagen_anterior = Parametro.getParametro("eliminar_version_anterior_geoproceso");
		
		if (Auxiliar.nz(parametro_eliminar_imagen_anterior, "").equals("1")) {
			String commando_eliminar_carpeta_geproceso = "rm -rf " + carpeta_geoproceso;
	
			try {
				String resultado_eliminar_carpeta_geoproceso = Auxiliar.commander(commando_eliminar_carpeta_geproceso, commando_eliminar_carpeta_geproceso);
				String[] a_resultado_eliminar_carpeta_geoproceso = resultado_eliminar_carpeta_geoproceso.split("-=-");
				r += a_resultado_eliminar_carpeta_geoproceso[1];
				r += Auxiliar.mensajeImpersonal("confirmacion", "Carpeta de la imagen antigua " + identImagen + ": " + carpeta_geoproceso + " eliminada." + a_resultado_eliminar_carpeta_geoproceso[1]);
				return r;
			}
			catch (Exception e) {
				e.printStackTrace();
				r += e.toString();
				r += Auxiliar.mensajeImpersonal("error", "Excepción al eliminar la carpeta de la imagen antigua " + identImagen + ": " + carpeta_geoproceso);
			}
		}
		else {
			r += Auxiliar.mensajeImpersonal("nota", "No eliminando carpeta de la imagen antigua " + identImagen + ": " + carpeta_geoproceso + ", por configuración.");
		}

		return r;
	}

}
