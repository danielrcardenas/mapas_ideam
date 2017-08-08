// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.BufferedInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.mbc.auxiliares.Archivo;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para descargar licencias del usuario
 */
public class DescargaLicenciaServlet extends HttpServlet {

	@EJB
	private Parametro			parametro;

	private static final long	serialVersionUID	= 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombreLic = request.getParameter("licenciaDescarga");
		String uriFile = Parametro.getParametro("dir.licencias") + "/" + nombreLic + ".pdf";

		if (Archivo.existeArchivo(uriFile)) {
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-disposition", "attachment; filename=" + nombreLic + ".pdf");
			ServletOutputStream stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(uriFile);
			BufferedInputStream buf = new BufferedInputStream(input);
			int readBytes = 0;
	
			while ((readBytes = buf.read()) != -1) {
				stream.write(readBytes);
			}
			stream.flush();
			buf.close();
			stream.close();
		}
		else {
//			String html = "";
//			
//			html = "<html><body><div>Archivo no encontrado. File not found.</div></body></html>";
//			
//			response.setContentType("text/html");
//			response.setCharacterEncoding("UTF-8");
//			response.getWriter().write(html);
			
			String mensaje = "Archivo no encontrado/File not found.";
			response.sendRedirect("/MonitoreoBC-WEB/error.jsp?mensaje=" + mensaje);
		}
	}
}
