package co.gov.ideamredd.usuarios.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.conexionBD.Parametro;

public class BorrarArchivosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String BUNDLE_NAME = "co/gov/ideamredd/ui/dao/Configuracion";
	private static final ResourceBundle properties = ResourceBundle
			.getBundle(BUNDLE_NAME);
	
	@EJB
	private Parametro parametro;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String ruta = parametro.getParametro("usuarios.EstadisticasDir") + "/";

		File f = new File(parametro.getParametro("usuarios.EstadisticasDir"));
		File[] fi = f.listFiles();
		File borra = null;

		for (int i = 0; i < fi.length; i++) {
			borra = new File(ruta + fi[i].getName());
			borra.delete();
		}

		resp.sendRedirect("reg/home.jsp");

	}

}