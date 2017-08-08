// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete para conectarse a R
 */
package co.gov.ideamredd.R;

import java.util.LinkedList;
import java.util.Queue;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 * Clase de entidad de comunicación con R
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
class ComunicacionR implements RMainLoopCallbacks {

	/**
	 * metodos propios implementados de la api JRI para hacer conexion desde Java al software estadistico R.
	 */
	public void rWriteConsole(Rengine re, String text, int oType) {
		System.out.print(text);
	}

	/**
	 * Método vacío para determinar si r está ocupado
	 */
	public void rBusy(Rengine re, int which) {
	}

	/**
	 * Leer la consola de R
	 */
	public String rReadConsole(Rengine re, String prompt, int addToHistory) {

		return null;
	}

	/**
	 * Mostrar mensaje de r
	 */
	public void rShowMessage(Rengine re, String message) {
		System.out.println("rShowMessage \"" + message + "\"");
	}

	/**
	 * Elegir archivo
	 */
	public String rChooseFile(Rengine re, int newFile) {
		return "";
	}

	/**
	 * Descargar la consulta
	 */
	public void rFlushConsole(Rengine re) {
	}

	/**
	 * Cargar historial
	 */
	public void rLoadHistory(Rengine re, String filename) {
	}

	/**
	 * Guardar historial
	 */
	public void rSaveHistory(Rengine re, String filename) {
	}
}

/**
 * Métodos de conexión y comunicación con R
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class ConexionR {

	static ConexionR				conexionR;
	static Rengine					re;
	static private Queue<String>	procesos;
	static private String			serialActual	= null;

	/**
	 * Método que establece coneccion con R desde Java
	 */
	private ConexionR() {
		// String path = "/usr/java/jdk1.6.0_45/jre/lib/amd64/server:/usr/java/jdk1.6.0_45/jre/lib/amd64:/usr/java/jdk1.6.0_45/jre/../lib/amd64:/usr/lib64/R/lib:/usr/lib64/R/bin::/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib:";
		// path+=System.getProperty("java.library.path");
		// System.setProperty( "java.library.path",path);
		System.out.println(System.getProperty("java.library.path"));
		System.setProperty("java.library.path", System.getProperty("java.library.path") + ":/usr/lib64/R/lib:/usr/lib64/R/bin");
		System.out.println(System.getProperty("java.library.path"));
		procesos = new LinkedList<String>();
		String[] parametro = { "--no-save" };

		re = new Rengine(parametro, false, new ComunicacionR());
		System.out.println("Rengine creado, esperando respuesta de R");

		if (!re.waitForR()) {
			System.out.println("No se pudo cargar R");
			return;
		}
	}

	/**
	 * Singleton del motor Rengine de JRI que establece comunicacion con R desde Java, manteniendo una unica instancia ejecutada del Rengine
	 * 
	 * @return re instancia del Rengine
	 */
	public static Rengine getConexionR() {

		if (re == null) {
			conexionR = new ConexionR();
		}
		return re;
	}

	/**
	 * Retorna token
	 * 
	 * @param serial
	 */
	public static void retornarToken(String serial) {
		try {
			conexionR.seguir(serial);
		}
		catch (InterruptedException e) {

		}
	}

	/**
	 * Espera ejecución de R
	 * 
	 * @param serial
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	private synchronized void esperar(String serial) throws InterruptedException {
		while (!serial.equals(serialActual)) {
			wait();

		}

	}

	/**
	 * Continua ejecución de R
	 * 
	 * @param serial
	 * @throws InterruptedException
	 */
	private synchronized void seguir(String serial) throws InterruptedException {
		if (serial.equals(serialActual)) {
			serialActual = procesos.poll();
			re.eval("rm(list=ls())");
			notify();
		}
	}

	/**
	 * Método main
	 * 
	 * @param a
	 */
	public static void main(String[] a) {
		System.out.println(System.getProperty("java.library.path"));
		// procesos=new LinkedList<String>();
		String[] parametro = { "--no-save" };

		Rengine re1 = new Rengine(parametro, false, new ComunicacionR());
		System.out.println("Rengine creado, esperando respuesta de R");

		if (!re1.waitForR()) {
			System.out.println("No se pudo cargar R");
			return;
		}
	}

}
