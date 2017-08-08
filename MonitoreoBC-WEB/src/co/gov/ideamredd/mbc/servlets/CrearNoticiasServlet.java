// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.servlets;

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

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.Parametro;
import co.gov.ideamredd.noticias.dao.CrearNoticia;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Servlet usado para creacion de noticias y eventos
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class CrearNoticiasServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	MultipartFormDataRequest	mrequest			= null;
	UploadBean					upBean				= null;
	UploadFile					file;
	String						ubicacionArchivo	= "";

	@EJB
	Parametro					parametro;
	@EJB
	CrearNoticia				cn;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idUsuario = 0;
		String login_usuario = "";
		String nombreNoticia = "";
		String tipoNoticia = "";
		String descripcion = "";
		String nombreImagenPar = "";
		String carpetaImagenes = "";
		
		File archivo = null;
	

		String fechaImagen = (new Date()).toString().replace(":", "").replace(" ", "");

		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(req)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);

				for (FileItem item : multiparts) {
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();

					if (fieldname.equals("idUsuario")) {
						login_usuario = Auxiliar.nz(fieldvalue, "");
						idUsuario = Integer.parseInt(Auxiliar.nz(ConexionBD.obtenerDato("select usr_consecutivo from red_usuario where usr_login='" + login_usuario + "'", "0", null), "0"));
					}
					else if (item.getFieldName().equals("nombreNoticia")) {
						nombreNoticia = Auxiliar.nz(fieldvalue, "");
						//nombreNoticia = getStringFromInputStream(item.getInputStream());
						nombreNoticia = new String(nombreNoticia.getBytes(), "UTF-8");
						nombreNoticia = nombreNoticia.replace("&aacute;", "á").replace("&eacute;", "é").replace("&iacute;", "í").replace("&oacute;", "ó").replace("&uacute;", "ú").replace("&ntilde;", "ñ");
					}
					else if (item.getFieldName().equals("tipoNoticia")) {
						tipoNoticia = Auxiliar.nz(fieldvalue, "0");
						//tipoNoticia = getStringFromInputStream(item.getInputStream());
					}
					else if (item.getFieldName().equals("descripcion")) {
						descripcion = Auxiliar.nz(fieldvalue, "");
						//descripcion = getStringFromInputStream(item.getInputStream());
						descripcion = new String(descripcion.getBytes(), "UTF-8");
						descripcion = descripcion.replace("&aacute;", "á").replace("&eacute;", "é").replace("&iacute;", "í").replace("&oacute;", "ó").replace("&uacute;", "ú").replace("&ntilde;", "ñ");
					}

					if (!item.isFormField()) {
						String name = new File(item.getName()).getName();

						if (!name.equals("")) {
							if (name.contains(".jpg")) {
								nombreImagenPar = idUsuario + fechaImagen + ".jpg";
							}
							else {
								nombreImagenPar = idUsuario + fechaImagen + ".png";
							}
							
							if (!nombreImagenPar.equals("")) {
								carpetaImagenes = Parametro.getParametro("ruta.imagenes.noticias");
								archivo = new File(carpetaImagenes + "/" + nombreImagenPar);
								item.write(archivo);
								System.out.println("Imagen guardada en " + carpetaImagenes + "/" + nombreImagenPar);
							}							
						}
					}
					else {
						nombreImagenPar = "";
					}
				}

				boolean ok_upload = false;
				
				if (!nombreImagenPar.equals("")) {
					if (archivo != null) {
						if (archivo.exists()) {
							ok_upload = true;
						}
					}					
				}
				
				if (ok_upload) {
					// File uploaded successfully
					System.out.println("Imagen subida con éxito.");
				}
				
				System.out.println("Noticia o Evento creado...");
			}
			catch (Exception ex) {
				System.out.println("Error al crear la Noticia o Evento: ");
				ex.printStackTrace();
			}

		}
		else {
			System.out.println("Formulario no es multipart");
		}

		cn.guardarDatos(idUsuario, nombreImagenPar, nombreNoticia, Integer.parseInt(tipoNoticia), descripcion);

		req.getSession().setAttribute("nombreNoticia", nombreNoticia);
		req.getSession().setAttribute("descripcion", descripcion);
		req.getSession().setAttribute("nombreImagenPar", nombreImagenPar);
		// Aca toca cambiar esto por la pagina donde se visualiza la noticia
		// creada
		resp.sendRedirect("noticias/notifiNoticiaEvento.jsp");

	}

	/**
	 * Retorna string de input stream
	 * 
	 * @param is
	 * @return string del input stream
	 */
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
