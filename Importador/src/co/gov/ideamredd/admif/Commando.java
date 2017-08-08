// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Métodos para ejecutar un comando a nivel del sistema y obtener el resultado
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class Commando extends Thread {

	InputStream	is;
	String		type;

	/**
	 * Constructor
	 * @param is
	 * @param type
	 */
	public Commando(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	/**
	 * Ejecuta el comando
	 */
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(type + "> " + line);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}