// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para borrar archivos
 */
public class BorrarArchivosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
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

		resp.sendRedirect("admin/consultarUsuarios.jsp");

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String ruta = parametro.getParametro("usuarios.EstadisticasDir") + "/";

		File f = new File(parametro.getParametro("usuarios.EstadisticasDir"));
		File[] fi = f.listFiles();
		File borra = null;

		for (int i = 0; i < fi.length; i++) {
			borra = new File(ruta + fi[i].getName());
			borra.delete();
		}

		resp.sendRedirect("admin/consultarUsuarios.jsp");

	}

}
