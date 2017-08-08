// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.EliminarRol;

/**
 * Servlet usado para borrar un rol
 */
@SuppressWarnings("serial")
public class EliminarRolServlet extends HttpServlet {

	@EJB
	private EliminarRol eliminarRol;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer id = Integer.valueOf(request.getParameter("rol"));
		eliminarRol.setConsecutivo(id);
		eliminarRol.eliminaRol();
	}

}
