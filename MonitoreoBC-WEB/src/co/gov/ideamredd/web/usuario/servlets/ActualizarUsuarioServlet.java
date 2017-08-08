// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.IOException;
import java.sql.Date;
//import java.util.ArrayList;

//import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.usuario.dao.ActualizarUsuario;
//import co.gov.ideamredd.usuario.entities.Perfil;
import co.gov.ideamredd.web.usuario.dao.ManejoCorreo;
import co.gov.ideamredd.util.Util;

/**
 * Servlet usado para actualizar un usuario
 */
public class ActualizarUsuarioServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//		@EJB
		ActualizarUsuario actualizarUsuario = new ActualizarUsuario();

		Integer idUsuario;
		String nombre;
		String login;
		String tipodoc;
		String documento;
		String tipopersona;
		String pais;
		String dir;
		String tel;
		String cel;
		String email;
		String password;
		String es_Activo = "1";
		Date una_FechaCreacion = new Date(System.currentTimeMillis());
		String dpto;
		String mcpio;
		String datLicencias;
		String[] licencias = null;
//		ArrayList<Perfil> perfiles;

		idUsuario = (Integer) req.getSession().getAttribute("usr_seq");
		tipopersona = req.getSession().getAttribute("tipPersona").toString();
		documento = req.getSession().getAttribute("documento").toString();
		tipodoc = req.getSession().getAttribute("tipodoc").toString();

		nombre = req.getParameter("nombre");
		login = req.getParameter("login");
		pais = req.getParameter("pais") != "" ? req.getParameter("pais") : "-1";
		dir = req.getParameter("dir");
		tel = req.getParameter("tel") != "" ? req.getParameter("tel") : " ";
		cel = req.getParameter("cel") != "" ? req.getParameter("cel") : " ";
		email = req.getParameter("emailUsuario");
		password = req.getParameter("password");
		dpto = req.getParameter("dpto");
		mcpio = req.getParameter("mcpio");
		datLicencias = req.getParameter("licSeleccionadas");

		actualizarUsuario.setIdUsuario(idUsuario);
		actualizarUsuario.setUn_Nombre(nombre);
		actualizarUsuario.setUn_login(login);
		actualizarUsuario.setUn_ApellidoDos(null);
		actualizarUsuario.setUn_TipoIdentificacion(Integer.valueOf(tipodoc));
		actualizarUsuario.setUna_Identificacion(documento);
		actualizarUsuario.setUn_TipoPersona(Integer.valueOf(tipopersona));
		actualizarUsuario.setUn_Pais(Integer.valueOf(pais));
		actualizarUsuario.setUna_Direccion(dir);
		actualizarUsuario.setUn_TelefonoOficina(tel);
		actualizarUsuario.setUn_Celular(cel);
		actualizarUsuario.setUn_CorreoElectronico(email);
		actualizarUsuario.setUna_Clave(password);
		actualizarUsuario.setEs_Activo(es_Activo);
		actualizarUsuario.setUna_FechaCreacion(una_FechaCreacion);
		actualizarUsuario.setUn_Departamento(Integer.valueOf(dpto != null && !dpto.equals("") ? dpto : "-1"));
		actualizarUsuario.setUn_Municipio(Integer.valueOf(mcpio != null && !mcpio.equals("") ? mcpio : "-1"));

		if (!datLicencias.equals("")) {
			licencias = datLicencias.split(",");
			actualizarUsuario.setLicencia(Util.obtenerArregloInteger(licencias));
		}

		boolean actualizado = actualizarUsuario.modificarUsuario();

		req.getSession().setAttribute("errorEnvio", "No");

		if (!actualizado) {
			req.getSession().setAttribute("errorRegistro", "Yes");
			resp.sendRedirect("reg/modificarUsuario.jsp");
		}
		else {
			req.getSession().setAttribute("errorRegistro", "No");

			boolean ok_notificacion = ManejoCorreo.enviarCorreoActualizacion(req, licencias);
			if (!ok_notificacion) {
				req.getSession().setAttribute("errorEnvio", "Yes");
			}

//			String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";
//			resp.sendRedirect(basePath + "MonitoreoBC-WEB/reg/notificacionActualizacion.jsp");
			resp.sendRedirect("/MonitoreoBC-WEB/reg/notificacionActualizacion.jsp");
		}

	}

}