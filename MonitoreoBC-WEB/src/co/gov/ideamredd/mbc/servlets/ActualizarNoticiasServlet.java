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
import co.gov.ideamredd.noticias.dao.ActualizarNoticia;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Servlet usado para creacion de noticias y eventos
 * @author drivera (lokgiova@gmail.com)
 *
 */
public class ActualizarNoticiasServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	MultipartFormDataRequest	mrequest			= null;
	UploadBean					upBean				= null;
	UploadFile					file;
	String						ubicacionArchivo	= "";

	@EJB
	Parametro					parametro;
	@EJB
	ActualizarNoticia				cn;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer idUsuario = 0;
		Integer idnotevt = 0;
		String str_idnotevt= "";
		String login_usuario = "";
		String nombreNoticia = "";
		String tipoNoticia = "";
		String descripcion = "";
		String nombreImagenPar = "";
		String carpetaImagenes = "";
		String imagenvieja = "";
		
		File archivo = null;
	

		String fechaImagen = (new Date()).toString().replace(":", "").replace(" ", "");

		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(req)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
				
				for (FileItem item : multiparts) {
					
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();
					System.out.println("ActualizarNoticiasServlet: fieldname: "+fieldname);
					if(!fieldname.equals("imgNotEvt"))
						System.out.println("ActualizarNoticiasServlet: fieldvalue: "+fieldvalue);

					if (fieldname.equals("idUsuarioNotEvt")) {
						login_usuario = Auxiliar.nz(fieldvalue, "");
						idUsuario = Integer.parseInt(Auxiliar.nz(ConexionBD.obtenerDato("select usr_consecutivo from red_usuario where usr_login='" + login_usuario + "'", "0", null), "0"));
					}
					else if (item.getFieldName().equals("nombreNotEvt")) {
						nombreNoticia = Auxiliar.nz(fieldvalue, "");
						//nombreNoticia = getStringFromInputStream(item.getInputStream());
						nombreNoticia = new String(nombreNoticia.getBytes(), "UTF-8");
						nombreNoticia = nombreNoticia.replace("&aacute;", "á").replace("&eacute;", "é").replace("&iacute;", "í").replace("&oacute;", "ó").replace("&uacute;", "ú").replace("&ntilde;", "ñ");
					}
					else if (item.getFieldName().equals("tipoNotEvt")) {
						tipoNoticia = Auxiliar.nz(fieldvalue, "0");
						//tipoNoticia = getStringFromInputStream(item.getInputStream());
					}
					else if (item.getFieldName().equals("descpNotEvt")) {
						descripcion = Auxiliar.nz(fieldvalue, "");
						//descripcion = getStringFromInputStream(item.getInputStream());
						descripcion = new String(descripcion.getBytes(), "UTF-8");
						descripcion = descripcion.replace("&aacute;", "á").replace("&eacute;", "é").replace("&iacute;", "í").replace("&oacute;", "ó").replace("&uacute;", "ú").replace("&ntilde;", "ñ");
					}else if (item.getFieldName().equals("idnotevt")) {
						str_idnotevt = Auxiliar.nz(fieldvalue, "");
						idnotevt = Integer.parseInt(str_idnotevt);
					}else if (item.getFieldName().equals("imagenvieja")) {
						imagenvieja = Auxiliar.nz(fieldvalue, "");
						imagenvieja =new String(imagenvieja.getBytes(), "UTF-8");
					}

					
					if (!item.isFormField()) {
						System.out.println("ActualizarNoticiasServlet: item.getName(): "+item.getName());
						String name = new File(item.getName()).getName();
						System.out.println("ActualizarNoticiasServlet: name: "+name);

						if (!name.equals("")) {
							if (name.contains(".jpg")) {
								nombreImagenPar = idUsuario + fechaImagen + ".jpg";
							}
							else {
								nombreImagenPar = idUsuario + fechaImagen + ".png";
							}
							System.out.println("ActualizarNoticiasServlet: nombreImagenPar: "+nombreImagenPar);
							
							if (!nombreImagenPar.equals("")) {
								carpetaImagenes = Parametro.getParametro("ruta.imagenes.noticias");
								archivo = new File(carpetaImagenes + "/" + nombreImagenPar);
								item.write(archivo);
								System.out.println("ActualizarNoticiasServlet: Imagen guardada en " + carpetaImagenes + "/" + nombreImagenPar);
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
								System.out.println("ActualizarNoticiasServlet: Imagen subida con éxito.");
							}else{
								System.out.println("ActualizarNoticiasServlet: Imagen no cargada: "+nombreImagenPar);
							}
						}
					}
					
				}

				
			}//fin for
			catch (Exception ex) {
				System.out.println("ActualizarNoticiasServlet: Error al actualizar la Noticia o Evento: ");
				ex.printStackTrace();
			}

		}
		else {
			System.out.println("ActualizarNoticiasServlet: Formulario no es multipart");
		}

		cn.actualizarDatos(idUsuario, nombreImagenPar, nombreNoticia, Integer.parseInt(tipoNoticia), descripcion, idnotevt);
		System.out.println("ActualizarNoticiasServlet: Noticia o Evento actualizado... entrando a notifiNoticiaEvento.jsp");
	
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
