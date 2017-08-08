// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.usuario.dao.ConsultaUsuario_Usuario;
//import co.gov.ideamredd.usuario.entities.Usuario;
import co.gov.ideamredd.util.entities.Usuario;


/**
 * Servlet usado para consultar un usuario
 */
public class ConsultarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		ConsultaUsuario_Usuario consultaUsuario = new ConsultaUsuario_Usuario();
		
		String nombre;
		String tipodoc="-1";
		String documento="-1";
		ArrayList<Usuario> usuarios;
		
		nombre = Auxiliar.nzObjStr(req.getParameter("nombre"), "");
		tipodoc = Auxiliar.nzObjStr(req.getParameter("tipodoc"), "-1");
		documento = Auxiliar.nzObjStr(req.getParameter("documento"), "-1");
		
		
		consultaUsuario.setUn_Nombre(nombre);
		consultaUsuario.setUn_TipoDocumento(Integer.valueOf(tipodoc));
//		consultaUsuario.setUna_Identificacion(documento==""?Double.valueOf(Double.valueOf("-1")):Double.valueOf(Double.valueOf(documento)));
		consultaUsuario.setUna_Identificacion(documento);
		usuarios=consultaUsuario.consultarUsuario();
		
		req.getSession().setAttribute("usuarios", usuarios);
		resp.sendRedirect("admin/consultarUsuarios.jsp");
	}

}
