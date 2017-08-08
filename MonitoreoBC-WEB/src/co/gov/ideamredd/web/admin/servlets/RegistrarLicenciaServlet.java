// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import co.gov.ideamredd.admin.dao.CrearLicencia;
import co.gov.ideamredd.mbc.conexion.Parametro;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;

/**
 * Servlet usado registrar una licencia
 */
public class RegistrarLicenciaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	MultipartFormDataRequest mrequest = null;
	UploadBean upBean = null;
	UploadFile file;
	String ubicacionArchivo = "";

	@EJB
	Parametro parametro;
	@EJB
	CrearLicencia cli;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nombreLicencia = "";
		String descripcion = "";
		Integer activa = 0;

		String cosa = "";

		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(req)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(
						new DiskFileItemFactory()).parseRequest(req);

				for (FileItem item : multiparts) {

					if (item.getFieldName().equals("nombreLicencia")) {
						nombreLicencia = getStringFromInputStream(item
								.getInputStream());
						nombreLicencia = new String(nombreLicencia.getBytes(),
								"UTF-8");
						nombreLicencia = nombreLicencia
								.replace("&aacute;", "�")
								.replace("&eacute;", "�")
								.replace("&iacute;", "�")
								.replace("&oacute;", "�")
								.replace("&uacute;", "�")
								.replace("&ntilde;", "�");
						nombreLicencia = nombreLicencia.replace(" ", "_")
								.toUpperCase();

					} else if (item.getFieldName().equals("descripcion")) {
						descripcion = getStringFromInputStream(item
								.getInputStream());
						descripcion = new String(descripcion.getBytes(),
								"UTF-8");
						descripcion = descripcion.replace("&aacute;", "�")
								.replace("&eacute;", "�")
								.replace("&iacute;", "�")
								.replace("&oacute;", "�")
								.replace("&uacute;", "�")
								.replace("&ntilde;", "�");
					} else if (item.getFieldName().equals("licenciaActiva")) {
						cosa = getStringFromInputStream(item.getInputStream());
						if(cosa.equals("on")){activa=1;}
						if(cosa.equals("off")){activa=0;}
					}

					if (!item.isFormField()) {

						item.write(new File(parametro
								.getParametro("dir.licencias")
								+ "/"
								+ nombreLicencia + ".pdf"));
					}
				}

				// File uploaded successfully
				System.out.println("Licencia guardada correctamente");
			} catch (Exception ex) {
				System.out.println("Error al subir la Licencia");
			}

		} else {
			System.out.println("Formulario multipart");
		}

		cli.guardarDatos(nombreLicencia, descripcion, activa);

		req.getSession().setAttribute("nombreLicencia", nombreLicencia);
		resp.sendRedirect("admin/notifiRegLicenciaUso.jsp");

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
