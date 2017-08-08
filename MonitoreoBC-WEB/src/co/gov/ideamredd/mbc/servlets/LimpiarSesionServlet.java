// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet usado para limpiar la sesion de la plataforma
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class LimpiarSesionServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";

		req.getSession().invalidate();

		resp.sendRedirect(basePath + "MonitoreoBC-WEB/logout.jsp");
	}

}
