// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.usuario.dao.ConsultaUsuario_Usuario;
import co.gov.ideamredd.usuario.entities.Usuario;
import co.gov.ideamredd.web.usuario.dao.RegistrarAccesoUsuario_UsuarioWeb;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Servlet usado para registrar el ingreso de un usuario
 */
public class RegistrarAccesoServlet extends HttpServlet {

	private static final long					serialVersionUID	= 54652693606335170L;

	ConexionBD									conexion;

	@EJB
	private ConsultaUsuario_Usuario				consultaUsuario;
	@EJB
	private RegistrarAccesoUsuario_UsuarioWeb	registraAcceso;

	// private int documento;
	private String								documento;
	private String								pass;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// documento = Integer.valueOf(req.getParameter("hidUsername"));
			documento = req.getParameter("hidUsername");
			pass = req.getParameter("hidPassword");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Usuario usuario = null;
		//
		// if (usuario != null) {
		// registraAcceso.registrarIngresoUsuario(usuario.getIdUsuario());
		// }

		resp.sendRedirect("pub/closePag.jsp");

	}

}
