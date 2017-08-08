// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Métodos para la conexión con la base de datos
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class DatosConexion {

	private Properties	prop;
	private InputStream	inputStream;
	private String		url			= "";
	private String		usuario		= "";
	private String		password	= "";

	/**
	 * Retorna la URL
	 * 
	 * @return String de la URL
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Retorna el usuario
	 * 
	 * @return String del usuario
	 */
	public String getUsuario() {
		return this.usuario;
	}

	/**
	 * Retorna la clave
	 * 
	 * @return String de la clave
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Establece los parámetros de conexión
	 */
	public DatosConexion() {

		try {
			establecerParametrosConexionBD();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {}

	}

	/**
	 * Retorna la propiedad según un nombre
	 * 
	 * @param nombre
	 * @return
	 */
	public String getPropiedad(String nombre) {

		if (nombre.equals("url")) {
			return this.url;
		}
		if (nombre.equals("usuario")) {
			return this.usuario;
		}
		if (nombre.equals("password")) {
			return this.password;
		}

		return "";

	}

	/**
	 * Retorna un arreglo con los parámetros de conexión
	 * 
	 * @return
	 */
	public boolean establecerParametrosConexionBD() {

		String host = "";
		String port = "";
		String sid = "";

		try {

			String directorio_parametros = "/opt/SMBC/conf/DescargasAutomaticas";
			String parametros_raw = leerArchivoSeparado(directorio_parametros + File.separator + "configuracion.properties", "\n");

			String[] a_parametros_raw = parametros_raw.split("\n");

			int i = 0;
			for (i = 0; i < a_parametros_raw.length; i++) {
				String[] a_parametro = a_parametros_raw[i].split("=");
				if (a_parametro.length == 2) {
					String nombre_parametro = a_parametro[0];
					String valor_parametro = a_parametro[1];

					if (nombre_parametro.equals("username")) this.usuario = valor_parametro;
					if (nombre_parametro.equals("password")) this.password = valor_parametro;
					if (nombre_parametro.equals("host")) host = valor_parametro;
					if (nombre_parametro.equals("port")) port = valor_parametro;
					if (nombre_parametro.equals("sid")) sid = valor_parametro;

				}
			}
			this.url = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + sid;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Retorna el contenido de un archivo de texto como un String
	 * 
	 * @param archivo
	 * @return String: contenido de archivo
	 */
	public String leerArchivoSeparado(String nombre_archivo, String separador) {
		String resultado = "";

		try {
			File f = new File(nombre_archivo);

			long i = 0;

			if (f.exists()) {
				BufferedReader input = new BufferedReader(new FileReader(nombre_archivo));
				try {
					String linea = null;
					while ((linea = input.readLine()) != null) {
						if (i > 0) resultado += separador;

						resultado += linea.toString();
						i++;
					}
				}
				finally {
					input.close();
				}
			}
		}
		catch (Exception e) {
			mensajeImpersonal("error", "Error al leer del archivo " + nombre_archivo);
		}

		return resultado;
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
	public String mensajeImpersonal(String clase, String texto) {
		System.out.println("T=" + (System.currentTimeMillis() / 1000) + " -- " + clase + ": " + texto);
		return "<p class=\"" + clase + "\">" + texto + "</p>";
	}

}
