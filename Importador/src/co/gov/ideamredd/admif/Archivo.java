// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Provee métodos para manejar los archivos y las carpetas
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Archivo extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// private static BD db = new BD();
	// private static Auxiliar aux = new Auxiliar();

	/**
	 * Método para subir un archivo al servidor
	 * 
	 * @param request
	 *            : request del post
	 * @param rutaCarpetaFinal
	 *            : ruta final de almacenamiento
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	protected String subirArchivos(HttpServletRequest request, String rutaCarpetaFinal) throws ServletException, IOException {

		// CONFIGURACIONES DE SUBIDA DE ARCHIVOS
		final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
		final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
		final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

		boolean ok = true;

		String resultado = "";

		// VERIFICAR QUE EL FORMULARIO TENGA UN ARCHIVO

		if (!ServletFileUpload.isMultipartContent(request)) {
			return "0-=-El formulario no contenía ningún archivo.";
		}

		// PARA CONFIGURAR LOS DETALLES DEL UPLOAD

		DiskFileItemFactory factory = new DiskFileItemFactory();

		// ESTABLECER LIMITE DE MEMORIA RAM

		factory.setSizeThreshold(MEMORY_THRESHOLD);

		// ESTABLECER DIRECTORIO TEMPORAL DE SUBIDA

		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// DEFINIR LA SUBIDA

		ServletFileUpload subida = new ServletFileUpload(factory);

		// ESTABLECER EL TAMAÑO MÁXIMO DEL ARCHIVO

		subida.setFileSizeMax(MAX_FILE_SIZE);

		// ESTABLECER EL TAMAÑO MÁXIMO DE TO DO EL POST

		subida.setSizeMax(MAX_REQUEST_SIZE);

		// SI EL DIRECTORIO NO EXISTE LO CREA (OJO CON LOS PERMISOS)

		resultado += Auxiliar.mensajeImpersonal("nota", "Revisando si la carpeta destino (" + rutaCarpetaFinal + ") existe o debe ser creada...");
		File carpetaFinal = new File(rutaCarpetaFinal);
		if (!carpetaFinal.exists()) {
			resultado += Auxiliar.mensajeImpersonal("nota", "Creando la carpeta destino (" + rutaCarpetaFinal + ")...");
			if (carpetaFinal.mkdir()) {
				resultado += Auxiliar.mensajeImpersonal("nota", "Carpeta destino (" + rutaCarpetaFinal + ") creada.");
			}
			else {
				return "0-=-" + Auxiliar.mensajeImpersonal("error", "No se pudo crear la carpeta destino (" + rutaCarpetaFinal + ").");
			}
		}

		// INTENTA SUBIR LOS ARCHIVOS DEL FORMULARIO EN LA CARPETA FINAL
		try {

			// OBTENER EL ARCHIVO DEL POST REQUEST

			List<FileItem> items_formulario = subida.parseRequest(request);

			// SI EL FORMULARIO TIENE ITEMS

			if ((items_formulario != null && items_formulario.size() > 0)) {

				// ITERAR LOS CAMPOS DEL FORMULARIO POST

				for (FileItem item : items_formulario) {

					// SOLO MIRAR LOS CAMPOS DE TIPO ARCHIVO

					if (!item.isFormField()) {

						String nombre_archivo = new File(item.getName()).getName();
						String ruta_archivo = rutaCarpetaFinal + File.separator + nombre_archivo;

						File archivo_a_guardar = new File(ruta_archivo);

						resultado += Auxiliar.mensajeImpersonal("nota", "Revisando si el archivo " + ruta_archivo + " ya existe...");
						if (archivo_a_guardar.exists()) {
							ok = false;
							resultado += Auxiliar.mensajeImpersonal("advertencia", "El archivo " + ruta_archivo + " ya existe.");
						}
						else {

							// GUARDAR EL ARCHIVO EN EL DISCO

							resultado += Auxiliar.mensajeImpersonal("nota", "Guardando el archivo " + ruta_archivo + " en el disco...");
							try {
								item.write(archivo_a_guardar);

								resultado += Auxiliar.mensajeImpersonal("confirmacion", "Archivo " + ruta_archivo + " guardado.");
							}
							catch (Exception e) {
								ok = false;
								resultado += Auxiliar.mensajeImpersonal("error", "Inconvenientes al guardar el archivo " + ruta_archivo + " en el disco: " + e.toString());
							}
						}
					}
					else {
						resultado += Auxiliar.mensajeImpersonal("nota", "Obviando el campo del formulario " + item.getFieldName() + " con valor " + item.getString());
					}
				}
			}
			else {
				ok = false;
				resultado += Auxiliar.mensajeImpersonal("error", "El formulario no tiene items");
			}
		}
		catch (Exception e) {
			ok = false;
			resultado += Auxiliar.mensajeImpersonal("error", "Ocurrió un error al subir el archivo: " + e.toString());
		}

		// SI NO HAY MENSAJES DE ERROR RETORNAR 1

		if (ok) {
			resultado = "1-=-" + resultado;
		}
		else {
			resultado = "0-=-" + resultado;
		}

		return resultado;
	}

	/**
	 * Método que dice si un archivo ya existe o no en la ruta especificada.
	 * 
	 * @param ruta_archivo
	 * @return boolean: verdadero si ya existe, falso si no
	 */
	public boolean existeArchivo(String ruta_archivo) {
		File arch = new File(ruta_archivo);

		return arch.exists();
	}

	/**
	 * Método recursivo que responde si un archivo existe en una carpeta o sus subcarpetas
	 * 
	 * @param nombre_archivo
	 * @param carpeta
	 * @return boolean: existe o no el archivo en la carpeta o sus subcarpetas?
	 */
	@SuppressWarnings("unused")
	private static boolean existeArchivoEnSubdirectorios(String nombre_archivo, File carpeta) {
		boolean resultado = false;

		if (new File(carpeta, nombre_archivo).exists()) {
			resultado = true;
		}
		else {
			File[] subcarpetas = carpeta.listFiles();

			int i = 0;
			int n = (subcarpetas == null) ? 0 : subcarpetas.length;

			while ((i < n) && !resultado) {
				File subcarpeta = subcarpetas[i];

				if (subcarpeta.isDirectory()) {
					resultado = existeArchivoEnSubdirectorios(nombre_archivo, subcarpeta);
				}

				i++;
			}
		}

		return resultado;
	}

	/**
	 * Retorna el contenido de un archivo de texto como un String
	 * 
	 * @param archivo
	 * @return String: contenido de archivo
	 */
	public String leerArchivo(String nombre_archivo) {
		String resultado = "";

		try {
			// StringBuilder contents = new StringBuilder();
			StringBuffer contents = new StringBuffer();
			File f = new File(nombre_archivo);

			if (f.exists()) {
				BufferedReader input = new BufferedReader(new FileReader(nombre_archivo));
				try {
					String linea = null;
					while ((linea = input.readLine()) != null) {
						contents.append(linea);
						// contents.append(System.getProperty("line.separator"));
					}

					resultado = contents.toString();
				}
				finally {
					input.close();
				}
			}
			else {
				resultado = "ERROR - El archivo " + nombre_archivo + " no existe... ";
			}
		}
		catch (Exception e) {
			resultado = "Problemas al leer del archivo " + nombre_archivo;
		}

		return resultado;
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
			Auxiliar.mensajeImpersonal("error", "Error al leer del archivo " + nombre_archivo);
		}

		return resultado;
	}

	/**
	 * Método que retorna la extensión de un archivo.
	 * 
	 * @param nombre_archivo
	 *            : nombre del archivo
	 * @return extensión del archivo
	 */
	public String extensionArchivo(String nombre_archivo) {
		String r = "";

		if (nombre_archivo == null) return "";

		if (nombre_archivo.equals("")) return "";

		String[] a_partes = null;

		a_partes = nombre_archivo.split("\\.");

		int n_partes = a_partes.length;

		if (n_partes > 0) {
			r = a_partes[n_partes - 1];
		}

		return r;
	}

	/**
	 * Método que retorna el nombre base de un archivo.
	 * 
	 * @param nombre_archivo
	 *            : nombre del archivo
	 * @return extensión del archivo
	 */
	public String nombreBaseArchivo(String ruta) {
		String r = "";

		if (ruta == null) return "";

		if (ruta.equals("")) return "";

		String[] a_partes_ruta = null;
		a_partes_ruta = ruta.split("/");
		int n_partes_ruta = a_partes_ruta.length;
		String nombre_archivo = a_partes_ruta[n_partes_ruta - 1];

		String[] a_partes_nombre = null;
		a_partes_nombre = nombre_archivo.split("\\.");
		int n_partes_nombre = a_partes_nombre.length;
		if (n_partes_nombre >= 0) {
			r = a_partes_nombre[0];
		}

		return r;
	}

	/**
	 * Metodo para crear una carpeta. Retorna verdadero si ya existe.
	 * 
	 * @param ruta_carpeta
	 * @throws IOException
	 */
	public boolean crearCarpeta(String ruta_carpeta) throws IOException {
		// SI LA CARPETA YA EXISTE RETORNA VERDADERO

		if (existeArchivo(ruta_carpeta)) return true;

		return (new File(ruta_carpeta)).mkdirs();
	}

	/**
	 * Método para eliminar un archivo
	 * 
	 * @param ruta_archivo
	 * @return
	 * @throws IOException
	 */
	public boolean eliminarArchivo(String ruta_archivo) throws IOException {
		// SI LA CARPETA YA EXISTE RETORNA VERDADERO

		if (!existeArchivo(ruta_archivo)) return true;

		return (new File(ruta_archivo)).delete();
	}

	/**
	 * Cuenta las líneas de un archivo
	 * 
	 * @param ruta_archivo
	 * @return numero de filas de un archivo de texto
	 * @throws IOException
	 */
	public int filasArchivo(String ruta_archivo) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(ruta_archivo));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count - 1;
		}
		finally {
			is.close();
		}
	}

	/**
	 * Retorna el número de registros a importar.
	 * 
	 * @param ruta_archivo
	 *            : debe existir.
	 * @return lineas: número de registros a importar.
	 */
	public int filasArchivoImportacion(String ruta_archivo) {
		int lineas = 0;

		try {
			InputStream inputStream = new FileInputStream(ruta_archivo);
			Reader reader = new InputStreamReader(inputStream, Charset.forName("windows-1252"));
			BufferedReader br = new BufferedReader(reader);
			String linea = "";

			while ((linea = br.readLine()) != null) {
				String lini = linea.substring(0, 5);
				if (lini.equals("Poner") || lini.equals("Put E") || lini.equals("ACCIO")) {
					continue;
				}
				lineas++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return lineas;

	}

	/**
	 * Método escribirArchivo. Crea un archivo de texto con el contenido en la ruta.
	 * 
	 * @param ruta
	 * @param contenido
	 * @return
	 * @throws IOException
	 */
	public boolean escribirArchivo(String ruta, String contenido) throws IOException {
		BufferedWriter writer = null;
		try {
			// String instante = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			File archivo = new File(ruta);
			System.out.println(archivo.getCanonicalPath());
			writer = new BufferedWriter(new FileWriter(archivo));
			writer.write(contenido);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				writer.close();
			}
			catch (Exception e) {}
		}

		return existeArchivo(ruta);
	}

	/**
	 * Devuelve arreglo de archivos en carpeta
	 * 
	 * @param ruta
	 * @return
	 * @throws IOException
	 */
	public String[] archivosEnCarpeta(String ruta) throws IOException {
		List<String> a_lista_archivos = new ArrayList<String>();

		File carpeta = new File(ruta);

		try {
			if (carpeta != null) {
				for (File archivo : carpeta.listFiles()) {
					if (archivo != null) {
						if (archivo.isFile()) {
							a_lista_archivos.add(archivo.getName());
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String[] a_archivos = new String[a_lista_archivos.size()];
		a_lista_archivos.toArray(a_archivos);

		return a_archivos;
	}

	/**
	 * Retorna un arreglo con los parámetros de conexión
	 * 
	 * @return arreglo de string de parámetros
	 */
	public String[] aParametrosConexionBD() {
		String[] a = { "", "", "", "", "" };

		String username = "";
		String password = "";
		String host = "";
		String port = "";
		String sid = "";

		String mi_ruta = Archivo.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		// String directorio_parametros = mi_ruta.replace("WEB-INF/classes/", "Parametros");
		String directorio_parametros = "/opt/SMBC/conf/AdmIF";

		// String parametros_raw = leerArchivo(getServletContext().getRealPath("") + File.separator + "Parametros/.parametros");
		String parametros_raw = leerArchivoSeparado(directorio_parametros + File.separator + "bd.conf", "\n");

		String[] a_parametros_raw = parametros_raw.split("\n");

		int i = 0;
		for (i = 0; i < a_parametros_raw.length; i++) {
			String[] a_parametro = a_parametros_raw[i].split("=");
			if (a_parametro.length == 2) {
				String nombre_parametro = a_parametro[0];
				String valor_parametro = a_parametro[1];

				if (nombre_parametro.equals("username")) username = valor_parametro;
				if (nombre_parametro.equals("password")) password = valor_parametro;
				if (nombre_parametro.equals("host")) host = valor_parametro;
				if (nombre_parametro.equals("port")) port = valor_parametro;
				if (nombre_parametro.equals("sid")) sid = valor_parametro;
			}
		}

		a = new String[] { username, password, host, port, sid };

		return a;
	}

	/**
	 * Método que obtiene los parámetros de encripción de un archivo
	 * 
	 * @return arreglo de string de parámetros de encripción
	 */
	public String[] aParametrosEncripcion() {
		String[] a = { "", "" };

		String encriptar_usuario = "";
		String llave_encripcion = "";

		String mi_ruta = Archivo.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		// String directorio_parametros = mi_ruta.replace("WEB-INF/classes/", "Parametros");
		String directorio_parametros = "/opt/SMBC/conf/AdmIF";

		// String parametros_raw = leerArchivo(getServletContext().getRealPath("") + File.separator + "Parametros/.parametros");
		String parametros_raw = leerArchivoSeparado(directorio_parametros + File.separator + ".crypt", "\n");

		String[] a_parametros_raw = parametros_raw.split("\n");

		int i = 0;
		for (i = 0; i < a_parametros_raw.length; i++) {
			String[] a_parametro = a_parametros_raw[i].split("=");
			if (a_parametro.length == 2) {
				String nombre_parametro = a_parametro[0];
				String valor_parametro = a_parametro[1];

				if (nombre_parametro.equals("encriptar_usuario")) encriptar_usuario = valor_parametro;
				if (nombre_parametro.equals("llave_encripcion")) llave_encripcion = valor_parametro;
			}
		}

		a = new String[] { encriptar_usuario, llave_encripcion };

		return a;
	}

}
// 338
