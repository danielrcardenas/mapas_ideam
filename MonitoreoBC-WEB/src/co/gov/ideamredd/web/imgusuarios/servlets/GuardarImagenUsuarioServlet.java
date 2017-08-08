// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.imgusuarios.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import co.gov.ideamredd.imgusuarios.dao.GuardarImagenUsuario;
import co.gov.ideamredd.mbc.conexion.Parametro;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;

/**
 * Servlet usado para guardar una imagen de usuario
 */
public class GuardarImagenUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	MultipartFormDataRequest mrequest = null;
	UploadBean upBean = null;
	UploadFile file;
	String ubicacionArchivo = "";

	@EJB
	Parametro parametro;
	@EJB
	GuardarImagenUsuario giu;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer idUsuario = 0;
		String nombreImagen = "";
		String tipoLicencia = "";
		String descripcion = "";
		String autorImagen = "";
		Double latitud = 0.0;
		Double longitud = 0.0;
		String nombreImagenPar = "";

		String fechaImagen = (new Date()).toString().replace(":", "")
				.replace(" ", "");

		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(req)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(
						new DiskFileItemFactory()).parseRequest(req);

				for (FileItem item : multiparts) {

					if (item.getFieldName().equals("idUsuario")) {
						idUsuario = Integer
								.parseInt(getStringFromInputStream(item
										.getInputStream()));
					} else if (item.getFieldName().equals("nombreImagen")) {
						nombreImagen = getStringFromInputStream(item
								.getInputStream());
						nombreImagen=new String (nombreImagen.getBytes(), "UTF-8");
						nombreImagen = nombreImagen.replace("&aacute;", "�")
								.replace("&eacute;", "�")
								.replace("&iacute;", "�")
								.replace("&oacute;", "�")
								.replace("&uacute;", "�")
								.replace("&ntilde;", "�");
					} else if (item.getFieldName().equals("tipoLicencia")) {
						tipoLicencia = getStringFromInputStream(item
								.getInputStream());
					} else if (item.getFieldName().equals("descripcion")) {
						descripcion = getStringFromInputStream(item
								.getInputStream());
						descripcion=new String (descripcion.getBytes(), "UTF-8");
						descripcion = descripcion.replace("&aacute;", "�")
								.replace("&eacute;", "�")
								.replace("&iacute;", "�")
								.replace("&oacute;", "�")
								.replace("&uacute;", "�")
								.replace("&ntilde;", "�");
					} else if (item.getFieldName().equals("autorImagen")) {
						autorImagen = getStringFromInputStream(item
								.getInputStream());
						autorImagen=new String (autorImagen.getBytes(), "UTF-8");
						autorImagen = autorImagen.replace("&aacute;", "�")
								.replace("&eacute;", "�")
								.replace("&iacute;", "�")
								.replace("&oacute;", "�")
								.replace("&uacute;", "�")
								.replace("&ntilde;", "�");
					} else if (item.getFieldName().equals("latitud")) {
						if (!getStringFromInputStream(item.getInputStream())
								.equals(""))
							latitud = Double
									.parseDouble(getStringFromInputStream(item
											.getInputStream()));
					} else if (item.getFieldName().equals("longitud")) {
						if (!getStringFromInputStream(item.getInputStream())
								.equals(""))
							longitud = Double
									.parseDouble(getStringFromInputStream(item
											.getInputStream()));
					}

					if (!item.isFormField()) {
						String name = new File(item.getName()).getName();

						if (name.contains(".jpg")) {

							item.write(new File(parametro
									.getParametro("ruta.imagenes.usuario")
									+ "/" + idUsuario + fechaImagen + ".jpg"));

							nombreImagenPar = idUsuario + fechaImagen + ".jpg";
						} else {
							item.write(new File(parametro
									.getParametro("ruta.imagenes.usuario")
									+ "/" + idUsuario + fechaImagen + ".png"));

							nombreImagenPar = idUsuario + fechaImagen + ".png";
						}
					}
				}

				// File uploaded successfully
				System.out.println("Imagen guardada correctamente");
			} catch (Exception ex) {
				System.out.println("Error al subir la imagen");
			}

		} else {
			System.out.println("Formulario multipart");
		}

		giu.guardarDatos(idUsuario, nombreImagenPar, nombreImagen,
				tipoLicencia, descripcion, autorImagen, latitud, longitud);

		req.getSession().setAttribute("nombreImagenPar", nombreImagenPar);
		resp.sendRedirect("reg/notifiRegImagen.jsp");

	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
