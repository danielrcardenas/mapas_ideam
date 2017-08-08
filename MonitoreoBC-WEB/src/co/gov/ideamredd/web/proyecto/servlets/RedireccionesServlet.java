// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.proyecto.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.util.Util;

/**
 * Servlet usado para redireccionar
 */
public class RedireccionesServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idProyecto = request.getParameter("proyecto_hidden");
		String usuario = request.getParameter("usuario");

		Integer dir = Integer.valueOf(request.getParameter("dir"));
		if (dir == 1) {
			String idProyectoEncripted = Util.encriptar(idProyecto);
			request.getSession().setAttribute("idProyecto", idProyectoEncripted);
			response.sendRedirect("proyectos/detallarProyecto.jsp");
		}
		else if (dir == 4) response.sendRedirect("proyectos/consultaReporteProyecto.jsp");
	}

}
