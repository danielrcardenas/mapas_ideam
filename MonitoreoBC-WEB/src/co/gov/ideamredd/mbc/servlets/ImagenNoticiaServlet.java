// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.mbc.auxiliares.Archivo;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para obtener la imagen de una noticia
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class ImagenNoticiaServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String nombreImagenUs = req.getParameter("nomImagenParam");

		if (nombreImagenUs != null) {
			if (nombreImagenUs.contains(".jpg")) resp.setContentType("image/jpeg");
			if (nombreImagenUs.contains(".png")) resp.setContentType("image/png");

			ServletOutputStream out;
			out = resp.getOutputStream();

			String ruta_archivo = Parametro.getParametro("ruta.imagenes.noticias") + "/" + nombreImagenUs;
			if (Archivo.existeArchivo(ruta_archivo)) {
				FileInputStream fin = new FileInputStream(ruta_archivo);
				BufferedInputStream bin = new BufferedInputStream(fin);
				BufferedOutputStream bout = new BufferedOutputStream(out);
				int ch = 0;
				while ((ch = bin.read()) != -1) {
					bout.write(ch);
				}
				bin.close();
				fin.close();
				bout.close();
			}
			out.close();
		}
	}

}
