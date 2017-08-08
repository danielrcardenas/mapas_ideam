// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.util;

import java.text.SimpleDateFormat;
import java.util.Date;
// import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import co.gov.ideamredd.conexion.Parametro;

/**
 * SMBC_Log.java Clase donde esta la configuración para el log de la aplicacion SMBC Proyecto: Sistema de Monitoreo de Biomasa y Carbono
 * 
 * @author Harry Alexis Sánchez Norato Oct 16 de 2013
 */
public class SMBC_Log {

	// private static final String configLog = "co/gov/ideamredd/recursos/Log";
	// private static final ResourceBundle prop = ResourceBundle
	// .getBundle(configLog);
	private static Date		fecha	= new Date();
	private static Logger	log;

	/**
	 * Metodo de configuración del archivo de Log de la aplicación.
	 * 
	 * @param clazz
	 *            Clase que invoca el log
	 * @return Logger Retorno a la clase que lo invoca para realizar la escritura de sucesos.
	 * @throws Exception
	 *             Fallos de conexion con el archivo de escritura.
	 */

	public static Logger log() {
		try {
			// log = Logger.getLogger(clazz);
			// Formato de la hora
			// Parametro parametro= new Parametro();
			SimpleDateFormat formato = new SimpleDateFormat("dd.MM.yyyy");
			String fechaAc = formato.format(fecha);
			// Patrón que seguirá las lineas del log
			PatternLayout defaultLayout = new PatternLayout("%p: %d{HH:mm:ss}  --> %m%n");
			RollingFileAppender rollingFileAppender = new RollingFileAppender();
			// Definimos el archivo dónde irá el log (la ruta)
			rollingFileAppender.setFile(Parametro.getParametro("ruta.log") + Parametro.getParametro("nombre.log") + fechaAc + ".log", true, false, 0);
			rollingFileAppender.setLayout(defaultLayout);

			log.removeAllAppenders();
			log.addAppender(rollingFileAppender);
			log.setAdditivity(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}

	public static Logger Log(@SuppressWarnings("rawtypes") Class clazz) {
		try {
			log = Logger.getLogger(clazz);
			// Formato de la hora
			// Parametro parametro= new Parametro();
			SimpleDateFormat formato = new SimpleDateFormat("dd.MM.yyyy");
			String fechaAc = formato.format(fecha);
			// Patrón que seguirá las lineas del log
			PatternLayout defaultLayout = new PatternLayout("%p: %d{HH:mm:ss}  --> %m%n");
			RollingFileAppender rollingFileAppender = new RollingFileAppender();
			// Definimos el archivo dónde irá el log (la ruta)
			rollingFileAppender.setFile(Parametro.getParametro("ruta.log") + Parametro.getParametro("nombre.log") + fechaAc + ".log", true, false, 0);
			rollingFileAppender.setLayout(defaultLayout);

			log.removeAllAppenders();
			log.addAppender(rollingFileAppender);
			log.setAdditivity(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}

	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
		try {
			Logger log = SMBC_Log.log();
			log.error("prueba de log");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		SMBC_Log.log = log;
	}
}
