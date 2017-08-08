// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.ModificaRol;

/**
 * Servlet usado para modificar un rol
 */
public class ModificarRolServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB
	private ModificaRol modificarRol;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cons = request.getParameter("consecutivo");
		Integer id = Integer.valueOf(cons);
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		modificarRol.setConsecutivo(id);
		modificarRol.setNombre(nombre);
		modificarRol.setDescripcion(descripcion);
		modificarRol.modicarRol();
	}

}