// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.admin.dao.ConsultaUsuarioAdmin;
import co.gov.ideamredd.util.entities.Usuario;

/**
 * Servlet usado para consultar a un usuario
 */
public class ConsultarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ConsultaUsuarioAdmin consultaUsuario;
	
	private String nombre;
	private String tipodoc="-1";
	private String documento="-1";
	private String rol="-1";
	private String activo="-1";
	private String tipo="-1";
	private ArrayList<Usuario> usuarios;
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		nombre = req.getParameter("nombre");
		tipodoc = req.getParameter("tipodoc");
		documento = req.getParameter("documento");
		rol = req.getParameter("rol");
		activo = req.getParameter("activo");
		tipo = req.getParameter("tipo");
		
		
		consultaUsuario.setUn_Nombre(nombre);
		consultaUsuario.setUn_TipoDocumento(Integer.valueOf(tipodoc));
		consultaUsuario.setUn_idRol(Integer.valueOf(rol));
		consultaUsuario.setUn_activo(Integer.valueOf(activo));
		consultaUsuario.setUn_tipo(Integer.valueOf(tipo));
		consultaUsuario.setUna_Identificacion(documento=="" ? "-1" : documento);
		usuarios=consultaUsuario.consultaExtendidaUsuarios();
		
		req.getSession().setAttribute("usuarios", usuarios);
		resp.sendRedirect("admin/consultarUsuarios.jsp");
	}

}
