package co.gov.ideamredd.usuarios.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ActualizarUsuario;
import co.gov.ideamredd.ui.entities.Usuario;

public class ModificarRolUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private ActualizarUsuario actualizaUs;

	private int idUsuario;
	private int idRol;
	private ArrayList<Usuario> usuarios;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		idUsuario = Integer.valueOf(req.getParameter("idUsuario"));
		idRol = Integer.valueOf(req.getParameter("idRol"));
		usuarios = (ArrayList<Usuario>) req.getSession().getAttribute(
				"usuarios");
		
		actualizaUs.modificarRolUsuario(idRol, idUsuario);
		
		for (int i = 0; i < usuarios.size(); i++) {
			if (usuarios.get(i).getIdUsuario() == idUsuario) {
				usuarios.get(i).setRolId(idRol);
			}
		}
		req.getSession().setAttribute("usuarios",usuarios);
		resp.sendRedirect("admin/consultarUsuarios.jsp");

	}

}
