package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ActualizarUsuario;
import co.gov.ideamredd.entities.Usuario;
import co.gov.ideamredd.ui.dao.ManejoCorreo;

public class ActivacionUsuarioServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ActualizarUsuario actualizaUs;
	
	private int idUsuario;
	private int tipoOperacion;
	private ArrayList<Usuario> usuarios;
	private String correo;
	private Usuario usr;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		idUsuario = Integer.valueOf(req.getParameter("idUsuario"));
		tipoOperacion = Integer.valueOf(req.getParameter("tipoOperacion"));
		usuarios = (ArrayList<Usuario>)req.getSession().getAttribute("usuarios");
		
		if(tipoOperacion == 0)
		{
			actualizaUs.desactivarUsuario(idUsuario);
			for(int i=0;i<usuarios.size();i++)
			{
				if(usuarios.get(i).getIdUsuario() == idUsuario)
				{
					usuarios.get(i).setActivo("0");
					usr=usuarios.get(i);
					correo=usuarios.get(i).getCorreoElectronico();
				}
			}
			ManejoCorreo.enviarCorreoDesactivacion(correo, usr);
		}
		if(tipoOperacion == 1)
		{
			actualizaUs.activarUsuario(idUsuario);
			for(int i=0;i<usuarios.size();i++)
			{
				if(usuarios.get(i).getIdUsuario() == idUsuario)
				{
					usuarios.get(i).setActivo("1");
					usr=usuarios.get(i);
					correo=usuarios.get(i).getCorreoElectronico();
				}
			}	
			ManejoCorreo.enviarCorreoActivacion(correo, usr);
		}
		req.getSession().setAttribute("usuarios",usuarios);
		resp.sendRedirect("admin/consultarUsuarios.jsp");
		
	}
}
