package co.gov.ideamredd.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ActualizaPermisosRol;

@SuppressWarnings("serial")
public class PermisosRolServlet extends HttpServlet {
	
	@EJB
	private ActualizaPermisosRol actualizaPermisosRol;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] permisos = request.getParameterValues("permisos");
		String idRol = request.getParameter("idRol");
		
		actualizaPermisosRol.setIdRol(Integer.valueOf(idRol));
		actualizaPermisosRol.setIdPermisos(permisos);
		actualizaPermisosRol.actualizarPermisosRol();
		
	}
}
