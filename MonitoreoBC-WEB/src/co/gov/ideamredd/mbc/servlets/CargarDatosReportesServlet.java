// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para cargar datos de reporte
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class CargarDatosReportesServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@EJB
	Parametro					parametro;

	/**
	 * Sube el archivo comprimido
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(req)) {
			try {

				String archivo = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/ArchivoComprimido.zip";

				String carpeta = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp";

				String archTexto = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/datos_reporte.txt";

				String boletinOficial = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/boletin_oficial.pdf";

				ArrayList<String> imagenes_nucleo = new ArrayList<String>();

				for (int i = 0; i < 8; i++) {
					imagenes_nucleo.add(parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/imagen_nucleo" + i + 1 + ".jpg");
				}

				String carpetaBoletines = parametro.getParametro("ruta_reportes") + "/AlertasTempranas/";

				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);

				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						item.write(new File(archivo));
					}
				}

				ZipInputStream archivoZip = new ZipInputStream(new FileInputStream(archivo));

				Integer resultadoAnalisis = 0;

				ZipEntry entrada;
				String nombrearchivo = "";
				while (null != (entrada = archivoZip.getNextEntry())) {
					nombrearchivo = entrada.getName().toLowerCase().replace(" ", "").replace("_", "").replaceAll(carpeta.toLowerCase(), "");
					if (!comprobarArchivo(nombrearchivo)) {
						archivoZip.closeEntry();
						archivoZip.close();
						// borrarTodo(carpeta);
						resultadoAnalisis = 0;
						break;
					}

					FileOutputStream fos = new FileOutputStream(carpeta + "/" + entrada.getName().replace(" ", "_").toLowerCase());
					int leido;
					byte[] buffer = new byte[1024];
					while (0 < (leido = archivoZip.read(buffer))) {
						fos.write(buffer, 0, leido);
					}
					fos.close();
					archivoZip.closeEntry();
					resultadoAnalisis = 1;
				}
				archivoZip.close();

				File borrar = new File(archivo);
				borrar.delete();

				Integer numeroBoletin = leerNumero(archTexto);
				String nombreNuevoBoletin = carpetaBoletines + "Boletin" + numeroBoletin + ".pdf";
				File afile = new File(nombreNuevoBoletin);
				if (afile.exists()) {
					afile.delete();
				}
				afile = new File(boletinOficial);
				afile.renameTo(new File(nombreNuevoBoletin));

				if (resultadoAnalisis == 0) {
					req.getSession().setAttribute("cargadoCorrecto", 0);
					resp.sendRedirect("admin/cargarDatosAlertasTempranas.jsp");
				}
				else {
					req.getSession().setAttribute("cargadoCorrecto", 1);
					resp.sendRedirect("admin/cargarDatosAlertasTempranas.jsp");
				}

			}
			catch (Exception ex) {
				System.out.println("Error al subir la Licencia");
				ex.printStackTrace();
			}

		}
		else {
			System.out.println("Formulario multipart");
		}

	}

	/**
	 * Comprueba validez del archivo
	 * 
	 * @param nombreArchivo
	 * @return true si es válido
	 */
	public Boolean comprobarArchivo(String nombreArchivo) {
		Boolean resultado = false;

		if (nombreArchivo.equals("datosreporte.txt")) {
			resultado = true;
		}
		if (nombreArchivo.equals("tabladatos.xlsx")) {
			resultado = true;
		}
		if (nombreArchivo.equals("mapadepartamentos.pdf")) {
			resultado = true;
		}
		if (nombreArchivo.equals("mapanucleos.pdf")) {
			resultado = true;
		}
		if (nombreArchivo.equals("mapapersistencia.pdf")) {
			resultado = true;
		}
		if (nombreArchivo.equals("maparegionesnaturales.pdf")) {
			resultado = true;
		}
		if (nombreArchivo.equals("mapacompuesto.pdf")) {
			resultado = true;
		}
		if (nombreArchivo.equals("boletinoficial.pdf")) {
			resultado = true;
		}
		if (nombreArchivo.indexOf("imagennucleo") != -1 && nombreArchivo.indexOf(".jpg") != -1) {
			resultado = true;
		}

		return resultado;
	}

	/**
	 * Borra todos los archivos del reporte
	 * 
	 * @param carpeta
	 */
	public void borrarTodo(String carpeta) {
		String sourcePath = carpeta;
		File prueba = new File(sourcePath);
		File[] ficheros = prueba.listFiles();
		File f = null;
		if (prueba.exists()) {
			for (int x = 0; x < ficheros.length; x++) {
				f = new File(ficheros[x].toString());
				f.delete();
			}
		}
		else {
			System.out.println("No existe el directorio");
		}
	}

	/**
	 * Lee la sección del número dado
	 * 
	 * @param archivoTexto
	 * @return
	 */
	public Integer leerNumero(String archivoTexto) {
		Integer resultado = 0;
		Boolean leerNumero = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(archivoTexto));
			String curline = "";
			while ((curline = br.readLine()) != null) {
				if (leerNumero) {
					resultado = Integer.parseInt(curline.trim());
					break;
				}
				if (curline.contains("#NUMERO#")) {
					leerNumero = true;
				}
			}

			br.close();
			return resultado;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}

}
