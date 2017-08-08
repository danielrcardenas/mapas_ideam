// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.RolesAdmin;
import co.gov.ideamredd.admin.entities.Rol;

public class RolesAdminServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@EJB
	private RolesAdmin			rolesAdmin;

	private String				texto;
	private ArrayList<Rol>		roles;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		texto = req.getParameter("texto");
		if (texto == null) texto = "";
		roles = rolesAdmin.rolesUsuarioBusqueda(texto);
		
		req.getSession().setAttribute("roles", roles);
		resp.sendRedirect("admin/consultaRoles.jsp");
	}

}
