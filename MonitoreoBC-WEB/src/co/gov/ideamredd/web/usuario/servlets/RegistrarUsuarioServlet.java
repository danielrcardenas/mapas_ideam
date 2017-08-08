// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.servlets;

import java.io.IOException;
import java.sql.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import co.gov.ideamredd.usuario.dao.RegistroUsuarios;
import co.gov.ideamredd.web.usuario.dao.ManejoCorreo;
import co.gov.ideamredd.util.entities.Usuario;
import co.gov.ideamredd.util.Util;

/**
 * Servlet usado para registrar a un usuario
 */
public class RegistrarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//		@EJB
		RegistroUsuarios registroUsuarios = new RegistroUsuarios();

		String nombre;
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
		String organizacion;
		String cargo;
		String login;

		String datLicencias;
		String[] licencias = null;

		Captcha captcha = (Captcha) req.getSession().getAttribute(Captcha.NAME);
		Usuario usr_tmp = new Usuario();
		req.setCharacterEncoding("UTF-8");
		String answer = req.getParameter("captcha");
		tipopersona = req.getParameter("tipopersona");
		nombre = req.getParameter("nombre");
		tipodoc = req.getParameter("tipodoc");
		documento = req.getParameter("documento");
		pais = req.getParameter("pais") != "" ? req.getParameter("pais") : "-1";
		dir = req.getParameter("dir");
		tel = req.getParameter("tel") != "" ? req.getParameter("tel") : " ";
		cel = req.getParameter("cel") != "" ? req.getParameter("cel") : " ";
		email = req.getParameter("correoe");
		password = req.getParameter("password");
		dpto = req.getParameter("dpto");
		mcpio = req.getParameter("mcpio");
		datLicencias = req.getParameter("licSeleccionadas");
		organizacion = req.getParameter("organizacion");
		cargo = req.getParameter("cargo");
		login = req.getParameter("login");

		if (!captcha.isCorrect(answer)) {
			if (datLicencias != "") {
				licencias = datLicencias.split(",");
				usr_tmp.setLicencias(licencias);
			} else {
				usr_tmp.setLicencias(null);
			}
			usr_tmp.setNombre(nombre);
			usr_tmp.setTipoIdentificacion(Integer.valueOf(tipodoc));
			usr_tmp.setIdentificacion(documento);
			usr_tmp.setTipoPersona(Integer.valueOf(tipopersona));
			usr_tmp.setPais(Integer.valueOf(pais));
			usr_tmp.setDireccion(dir);
			usr_tmp.setTelefonoOficina(tel);
			usr_tmp.setCelular(cel);
			usr_tmp.setCorreoElectronico(email);
			usr_tmp.setActivo(es_Activo);
			usr_tmp.setDepto(Integer.valueOf(dpto));
			usr_tmp.setMunicipio(Integer.valueOf(mcpio));
			usr_tmp.setLicencias(licencias);
			usr_tmp.setOrganizacion(organizacion);
			usr_tmp.setCargo(cargo);
			usr_tmp.setLogin(login);

			req.getSession().setAttribute("usr_tmp", usr_tmp);
			req.getSession().setAttribute("errorCaptcha", "Yes");
			resp.sendRedirect("pub/registroUsuario.jsp");
		} else {
			req.getSession().setAttribute("errorCaptcha", "No");
			if (datLicencias != "") {
				licencias = datLicencias.split(",");
				registroUsuarios.setLicencia(Util.obtenerArregloInteger(licencias));
			} else {
				registroUsuarios.setLicencia(null);
			}
			registroUsuarios.setUn_Nombre(nombre);
			registroUsuarios.setUn_login(login);
			registroUsuarios.setUna_Organizacion(organizacion);
			registroUsuarios.setUn_Cargo(cargo);
			registroUsuarios.setUn_TipoIdentificacion(Integer.valueOf(tipodoc));
			registroUsuarios.setUna_Identificacion(documento);
			registroUsuarios.setUn_TipoPersona(Integer.valueOf(tipopersona));
			registroUsuarios.setUn_Pais(Integer.valueOf(pais));
			registroUsuarios.setUna_Direccion(dir);
			registroUsuarios.setUn_TelefonoOficina(tel);
			registroUsuarios.setUn_Celular(cel);
			registroUsuarios.setUn_CorreoElectronico(email);
			registroUsuarios.setUna_Clave(password);
			registroUsuarios.setEs_Activo(es_Activo);
			registroUsuarios.setUna_FechaCreacion(una_FechaCreacion);
			registroUsuarios.setUn_Departamento(Integer.valueOf(dpto != null && !dpto.equals("") ? dpto : "-1"));
			registroUsuarios.setUn_Municipio(Integer.valueOf(mcpio != null && !mcpio.equals("") ? mcpio : "-1"));

			boolean registrado = registroUsuarios.registrarUsuario();

			if (registrado) {
				req.getSession().setAttribute("errorRegistro", "No");
				req.getSession().setAttribute("usr_tmp", null);
				ManejoCorreo.enviarCorreo(req, licencias);
				// req.getSession().invalidate();
				resp.sendRedirect("pub/notificacion.jsp");
			} else {
				usr_tmp.setNombre(nombre);
				usr_tmp.setTipoIdentificacion(Integer.valueOf(tipodoc));
				usr_tmp.setIdentificacion(documento);
				usr_tmp.setTipoPersona(Integer.valueOf(tipopersona));
				usr_tmp.setPais(Integer.valueOf(pais));
				usr_tmp.setDireccion(dir);
				usr_tmp.setTelefonoOficina(tel);
				usr_tmp.setCelular(cel);
				usr_tmp.setCorreoElectronico(email);
				usr_tmp.setActivo(es_Activo);
				usr_tmp.setDepto(Integer.valueOf(dpto));
				usr_tmp.setMunicipio(Integer.valueOf(!mcpio.equals("") ? mcpio : "-1"));
				usr_tmp.setLicencias(licencias);
				usr_tmp.setOrganizacion(organizacion);
				usr_tmp.setCargo(cargo);
				usr_tmp.setLogin(login);

				req.getSession().setAttribute("usr_tmp", usr_tmp);
				req.getSession().setAttribute("errorRegistro", "Yes");
				resp.sendRedirect("pub/registroUsuario.jsp");
			}

		}

	}

}
