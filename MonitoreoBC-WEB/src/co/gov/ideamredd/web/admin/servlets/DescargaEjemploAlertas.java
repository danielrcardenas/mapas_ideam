// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

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

import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para descargar un archivo ejemplo de alertas tempranas.
 */
public class DescargaEjemploAlertas extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private Parametro parametro;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String uriFile = parametro.getParametro("dir.documentosUs") + "/EjemploAlertas.zip";

		response.setContentType("application/zip");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment; filename=EjemploAlertas.zip");
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
}
