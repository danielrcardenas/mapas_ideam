// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.servlets;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
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
 * Servlet usado para descargar boletines de alertas tempranas
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class DescargaBoletinesAlertasServlet extends HttpServlet {

	@EJB
	private Parametro			parametro;

	private static final long	serialVersionUID	= 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombreDoc = request.getParameter("hidNomBoletin");
		String uriFile = Parametro.getParametro("ruta_reportes") + "/AlertasTempranas/" + nombreDoc;

		response.setContentType("application/pdf");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment; filename=" + nombreDoc);
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		try {
			String nombreDoc = request.getParameter("nomDoc");
			String uriFile = Parametro.getParametro("ruta_reportes") + "/AlertasTempranas/Temp/" + nombreDoc;

			response.setContentType("application/pdf");
			ServletOutputStream out;
			out = response.getOutputStream();
			FileInputStream fin = new FileInputStream(uriFile);

			BufferedInputStream bin = new BufferedInputStream(fin);
			BufferedOutputStream bout = new BufferedOutputStream(out);
			int ch = 0;
			while ((ch = bin.read()) != -1) {
				bout.write(ch);
			}

			bin.close();
			fin.close();
			bout.close();
			out.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
