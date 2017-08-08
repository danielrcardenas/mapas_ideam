// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.CrearRol;

/**
 * Servlet usado para crear un rol
 */
@SuppressWarnings("serial")
public class CrearRolServlet extends HttpServlet {

	@EJB
	private CrearRol crearRol;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath()
				+ "/";
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		String permisos = request.getParameter("permisos");
		crearRol.setNombre(nombre);
		crearRol.setDescripcion(descripcion);
		Integer idRol = crearRol.registarRol();
		if (permisos.equals("1"))
			response.sendRedirect(basePath + "permisosRol.jsp?id=" + idRol);
	}

}
