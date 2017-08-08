package co.gov.ideamredd.R;

import java.util.LinkedList;
import java.util.Queue;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

class ComunicacionR implements RMainLoopCallbacks {

	/**
	 * metodos propios implementados de la api JRI para hacer conexion desde
	 * Java al software estadistico R.
	 */
	public void rWriteConsole(Rengine re, String text, int oType) {
		System.out.print(text);
	}

	public void rBusy(Rengine re, int which) {
	}

	public String rReadConsole(Rengine re, String prompt, int addToHistory) {
		
		return null;
	}

	public void rShowMessage(Rengine re, String message) {
		System.out.println("rShowMessage \"" + message + "\"");
	}

	public String rChooseFile(Rengine re, int newFile) {
		return "";
	}

	public void rFlushConsole(Rengine re) {
	}

	public void rLoadHistory(Rengine re, String filename) {
	}

	public void rSaveHistory(Rengine re, String filename) {
	}
}

public class ConexionR {

	static ConexionR conexionR;
	static Rengine re;
	static private Queue<String> procesos;
	static private String serialActual=null;
	
	/**
	 * metodo que establece coneccion con R desde Java
	 */
	private ConexionR() {
//		String path = "/usr/java/jdk1.6.0_45/jre/lib/amd64/server:/usr/java/jdk1.6.0_45/jre/lib/amd64:/usr/java/jdk1.6.0_45/jre/../lib/amd64:/usr/lib64/R/lib:/usr/lib64/R/bin::/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib:";
//		path+=System.getProperty("java.library.path");
//		System.setProperty( "java.library.path",path);
		System.out.println(System.getProperty("java.library.path"));
		System.setProperty( "java.library.path",System.getProperty("java.library.path")+":/usr/lib64/R/lib:/usr/lib64/R/bin");
		System.out.println(System.getProperty("java.library.path"));
		procesos=new LinkedList<String>();
		String[] parametro =  {"--no-save"};
		
		re = new Rengine(parametro, false, new ComunicacionR());
		System.out.println("Rengine creado, esperando respuesta de R");

		if (!re.waitForR()) {
			System.out.println("No se pudo cargar R");
			return;
		}
	}

	/**
	 * Singleton del motor Rengine de JRI que establece comunicacion con R desde
	 * Java, manteniendo una unica instancia ejecutada del Rengine
	 * 
	 * @return re instancia del Rengine
	 */
	public static Rengine getConexionR() {

		if (re == null) {
			conexionR = new ConexionR();
		}
		return re;
	}

	public static void retornarToken(String serial){
		try{
			conexionR.seguir(serial);
		}catch (InterruptedException e) {

		}
	}
	@SuppressWarnings("unused")
	private synchronized void esperar(String serial) throws InterruptedException{
		while(!serial.equals(serialActual)){
			wait();
			
		}		
		
	}
	private synchronized void seguir(String serial) throws InterruptedException{
		if(serial.equals(serialActual)){
			serialActual=procesos.poll();
			re.eval("rm(list=ls())");
			notify();
		}
	}
	
	public static void main(String[] a){
		System.out.println(System.getProperty("java.library.path"));
//		procesos=new LinkedList<String>();
		String[] parametro =  {"--no-save"};
		
		Rengine re1 = new Rengine(parametro, false, new ComunicacionR());
		System.out.println("Rengine creado, esperando respuesta de R");

		if (!re1.waitForR()) {
			System.out.println("No se pudo cargar R");
			return;
		}
	}
	
}
