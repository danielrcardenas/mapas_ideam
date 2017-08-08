// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.imgusuarios.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.imgusuarios.dao.ActualizarImagenUsuario;
import co.gov.ideamredd.util.UtilWeb;
import co.gov.ideamredd.util.entities.Usuario;
import co.gov.ideamredd.web.usuario.dao.ManejoCorreo;

/**
 * Servlet usado para procesar una imagen de usuario
 */
public class ProcesarImagenServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@EJB
	ActualizarImagenUsuario aui;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer imagenId = Integer.parseInt(req.getParameter("hidImagenId"));
		String nombreImagen = req.getParameter("hidNombreImagen");
		Integer usuarioId = Integer.parseInt(req.getParameter("hidUsuarioId"));
		String accion = req.getParameter("hidSeleccion");
		
		Usuario usuario= UtilWeb.consultarUsuarioPorDoc(String.valueOf(usuarioId));
		
		aui.procesarImagen(imagenId, accion);
		
		if(accion.equals("acepta")){
			ManejoCorreo.enviarCorreoAceptaImagen(nombreImagen, usuario);
		}
        else{
        	ManejoCorreo.enviarCorreoRechazoImagen(nombreImagen, usuario);
        }
		
		resp.sendRedirect("imageus/consultarImagenesUsuarios.jsp");
		
	}

}
