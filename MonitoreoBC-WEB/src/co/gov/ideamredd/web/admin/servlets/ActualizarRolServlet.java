// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.ActualizarRol;
import co.gov.ideamredd.mbc.conexion.Parametro;

/**
 * Servlet usado para actualizar un rol
 */
public class ActualizarRolServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private Parametro parametro;

	@EJB
	private ActualizarRol aro;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Integer consecutivo = Integer.parseInt(request
				.getParameter("hidConsecutivo"));
		String nombreRol = request.getParameter("nombreRol").replace(" ", "_");
		String descripcion = request.getParameter("descripcion");
		String flag = request.getParameter("rolActivo");
		Integer activo = 0;

		try {
			if (flag.equals("on")) {
				activo = 1;
			}
		} catch (Exception e) {
			activo = 0;
		}

		aro.registrar(consecutivo, nombreRol, descripcion, activo);

		response.sendRedirect("admin/consultaRoles.jsp");

	}
}
