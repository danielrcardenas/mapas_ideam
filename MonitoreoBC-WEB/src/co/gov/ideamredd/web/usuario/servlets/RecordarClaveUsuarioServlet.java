// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.web.usuario.dao.ManejoCorreo;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
//import co.gov.ideamredd.usuario.entities.Usuario;
import co.gov.ideamredd.util.entities.Usuario;
import co.gov.ideamredd.usuario.security.Encript;
import co.gov.ideamredd.usuario.dao.ActualizarUsuario;
import co.gov.ideamredd.usuario.dao.ConsultaUsuario_Usuario;

/**
 * Servlet usado para recordar la clave de un usuario
 */
public class RecordarClaveUsuarioServlet extends HttpServlet {

	private static final long		serialVersionUID	= 1L;

	@EJB
	private ConsultaUsuario_Usuario	consultaUsuario;
	@EJB
	private ActualizarUsuario		actualizaUsuario;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = request.getParameter("email");
		consultaUsuario.setUn_CorreoElectronico(email);

		Usuario usuario = consultaUsuario.consultarUsuarioRecordatorio();

		if (usuario == null) {
			request.getSession().setAttribute("errorRegistro", "Yes");
			response.sendRedirect("pub/recordarClave.jsp");
		}
		else {
			if (Auxiliar.nz(usuario.getOrganizacion(), "").equals("IDEAM")) {
				request.getSession().setAttribute("errorUsuarioIDEAM", "Yes");
				response.sendRedirect("pub/recordarClave.jsp");
			}
			else {
				// if (consultaUsuario.consultarIdRol(usuario.getIdUsuario()) == 1) {
				if (usuario.getActivo().equals("1") && usuario.getIdUsuario() != 1) {
					String pass = Encript.getNewPassword();
					usuario.setClave(Encript.getEncodedPassword(pass));
					actualizaUsuario.setIdUsuario(usuario.getIdUsuario());
					actualizaUsuario.setUna_Clave(usuario.getClave());
					actualizaUsuario.modificarClaveUsuario(pass, usuario.getIdentificacion());
					usuario.setClave(pass);
					ManejoCorreo.enviarCorreoRecordatorio(pass, email, usuario);
					request.getSession().setAttribute("mensaje", "recordatorio enviado");
					response.sendRedirect("pub/notifiRecordatorio.jsp");
				}
				else {
					request.getSession().setAttribute("errorRol", "Yes");
					response.sendRedirect("pub/recordarClave.jsp");
				}
			}
		}

	}
}
