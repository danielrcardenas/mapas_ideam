package co.gov.ideamredd.servlets;

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

import co.gov.ideamredd.conexionBD.Parametro;

public class DescargaLicenciaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String BUNDLE_NAME = "co/gov/ideamredd/ui/dao/Configuracion";
	private static final ResourceBundle properties = ResourceBundle
			.getBundle(BUNDLE_NAME);
	
	@EJB
	private Parametro parametro;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String nombreLic = request.getParameter("licenciaDescarga");
		String uriFile = parametro.getParametro("dir.licencias") + "/"
				+ nombreLic + ".pdf";

		response.setContentType("application/pdf");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-disposition", "attachment; filename="
				+ nombreLic);
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
