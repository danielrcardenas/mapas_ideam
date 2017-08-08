// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.ActualizaPermisos;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para actualizar permisos
 */
public class ActualizarPermisosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private Parametro parametro;
	
	@EJB
	private ActualizaPermisos ap;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String[] permisos = request.getParameterValues("permisosFin");
		Integer idRol = Integer.parseInt(request.getParameter("hidRolConsecutivo"));
		
		ap.actualizar(idRol,permisos);
		
		response.sendRedirect("admin/consultaRoles.jsp");
		
	}
}
