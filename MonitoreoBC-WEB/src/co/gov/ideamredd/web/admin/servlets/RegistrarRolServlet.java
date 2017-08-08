// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.CrearRol;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para registrar un rol
 */
public class RegistrarRolServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private Parametro parametro;
	
	@EJB
	private CrearRol cro;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String nombreRol = request.getParameter("nombreRol");
		String descripcion = request.getParameter("descripcion");
		
		cro.registrar(nombreRol,descripcion);
		
		request.getSession().setAttribute("nombreRol",nombreRol);
		response.sendRedirect("admin/notifiRolCreado.jsp");
		
	}
}
