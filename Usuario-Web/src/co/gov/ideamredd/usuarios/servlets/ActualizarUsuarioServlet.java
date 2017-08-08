package co.gov.ideamredd.usuarios.servlets;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.gov.ideamredd.dao.ActualizarUsuario;
import co.gov.ideamredd.entities.Perfil;
import co.gov.ideamredd.ui.dao.ManejoCorreo;
import co.gov.ideamredd.util.Util;

public class ActualizarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	ActualizarUsuario actualizarUsuario;

	private Integer idUsuario;
	private String nombre;
	private String tipodoc;
	private String documento;
	private String tipopersona;
	private String pais;
	private String dir;
	private String tel;
	private String cel;
	private String email;
	private String password;
	private String es_Activo = "1";
	private Date una_FechaCreacion = new Date(System.currentTimeMillis());
	private String dpto;
	private String mcpio;
	private String datLicencias;
	private String[] licencias;
	ArrayList<Perfil> perfiles;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		idUsuario = (Integer) req.getSession().getAttribute("usr_seq");
		tipopersona = req.getSession().getAttribute("tipPersona").toString();
		documento = req.getSession().getAttribute("documento").toString();
		tipodoc = req.getSession().getAttribute("tipodoc").toString();

		nombre = req.getParameter("nombre");
		pais = req.getParameter("pais") != "" ? req.getParameter("pais") : "-1";
		dir = req.getParameter("dir");
		tel = req.getParameter("tel") != "" ? req.getParameter("tel") : " ";
		cel = req.getParameter("cel") != "" ? req.getParameter("cel") : " ";
		email = req.getParameter("email");
		password = req.getParameter("password");
		dpto = req.getParameter("dpto");
		mcpio = req.getParameter("mcpio");
		datLicencias = req.getParameter("licSeleccionadas");

		actualizarUsuario.setIdUsuario(idUsuario);
		actualizarUsuario.setUn_Nombre(nombre);
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
		actualizarUsuario.setUn_Departamento(Integer.valueOf(dpto));
		actualizarUsuario.setUn_Municipio(Integer.valueOf(mcpio));

		if (!datLicencias.equals("")) {
			licencias = datLicencias.split(",");
			actualizarUsuario
					.setLicencia(Util.obtenerArregloInteger(licencias));
		}

		boolean actualizado = actualizarUsuario.modificarUsuario();

		if (!actualizado) {
			req.getSession().setAttribute("errorRegistro", "Yes");
			resp.sendRedirect("reg/modificarUsuario.jsp");
		} else {
			ManejoCorreo.enviarCorreoActualizacion(req, licencias);
			req.getSession().setAttribute("errorRegistro", "No");
			
			String basePath = req.getScheme() + "://"
					+ req.getServerName() + ":" + req.getServerPort() + "/";

			resp.sendRedirect(basePath+"MonitoreoBiomasaCarbono/home.jsp");
		}

	}

}