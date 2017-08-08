package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ConsultaUsuario;
import co.gov.ideamredd.entities.Usuario;


public class ConsultarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ConsultaUsuario consultaUsuario;
	
	private String nombre;
	private String tipodoc="-1";
	private String documento="-1";
	private ArrayList<Usuario> usuarios;
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		nombre = req.getParameter("nombre");
		tipodoc = req.getParameter("tipodoc");
		documento = req.getParameter("documento");
		
		
		consultaUsuario.setUn_Nombre(nombre);
		consultaUsuario.setUn_TipoDocumento(Integer.valueOf(tipodoc));
		consultaUsuario.setUna_Identificacion(documento==""?Double.valueOf(Double.valueOf("-1")):Double.valueOf(Double.valueOf(documento)));
		usuarios=consultaUsuario.consultarUsuario();
		
		req.getSession().setAttribute("usuarios", usuarios);
		resp.sendRedirect("admin/consultarUsuarios.jsp");
	}

}
