// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.BufferedInputStream;

import java.io.FileInputStream;
import java.io.IOException;
// import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.mbc.auxiliares.Archivo;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para descargar documentos
 */
public class DescargaDocumentosServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombreDoc = request.getParameter("hidNomDoc");
		String tipoDocumento = request.getParameter("hidTipoDocRef");

		String uriFile = "";
		
		if (tipoDocumento == null || tipoDocumento.equals("PDF") || !Auxiliar.tieneAlgo(tipoDocumento)) {
			uriFile = Parametro.getParametro("dir.documentosUs") + "/" + nombreDoc + ".pdf";
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=" + nombreDoc + ".pdf");
		}
		else {
			uriFile = Parametro.getParametro("dir.documentosUs") + "/" + nombreDoc + ".docx";
			response.setContentType("application/docx");
			response.setHeader("Content-disposition", "attachment; filename=" + nombreDoc + ".docx");
		}

		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");

		if (Archivo.existeArchivo(uriFile)) {
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
			String mensaje = "Archivo no encontrado/File not found.";
			response.sendRedirect("/MonitoreoBC-WEB/error.jsp?mensaje=" + mensaje);
		}

	}
}
