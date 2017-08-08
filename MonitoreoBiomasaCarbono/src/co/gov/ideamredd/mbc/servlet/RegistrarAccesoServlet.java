package co.gov.ideamredd.mbc.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.mbc.dao.ConsultaUsuario;
import co.gov.ideamredd.mbc.dao.RegistrarAccesoUsuario;
import co.gov.ideamredd.mbc.entities.Usuario;

public class RegistrarAccesoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ConsultaUsuario consultaUsuario;
	@EJB
	private RegistrarAccesoUsuario registraAcceso;
	
	private int documento;
	private String pass;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
		documento =  Integer.valueOf(req.getParameter("hidUsername"));
		pass = req.getParameter("hidPassword");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Usuario usuario=consultaUsuario.usuarioEnLogOnCorrecto(documento,pass);
		
		if(usuario != null){
			registraAcceso.registrarIngresoUsuario(usuario.getIdUsuario());
		}
		
		resp.sendRedirect("pub/closePag.jsp");
		
	}

}
