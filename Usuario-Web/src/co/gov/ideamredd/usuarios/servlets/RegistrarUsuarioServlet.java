package co.gov.ideamredd.usuarios.servlets;

import java.io.IOException;
import java.sql.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import co.gov.ideamredd.dao.RegistroUsuarios;
import co.gov.ideamredd.ui.dao.ManejoCorreo;
import co.gov.ideamredd.ui.entities.Usuario;
import co.gov.ideamredd.util.Util;

public class RegistrarUsuarioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private RegistroUsuarios registroUsuarios;

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

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

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
		email = req.getParameter("email");
		password = req.getParameter("password");
		dpto = req.getParameter("dpto");
		mcpio = req.getParameter("mcpio");
		datLicencias = req.getParameter("licSeleccionadas");

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
			
			req.getSession().setAttribute("usr_tmp", usr_tmp);
			req.getSession().setAttribute("errorCaptcha", "Yes");
			resp.sendRedirect("pub/registroUsuario.jsp");
		} else {
			req.getSession().setAttribute("errorCaptcha", "No");
			if (datLicencias != "") {
				licencias = datLicencias.split(",");
				registroUsuarios.setLicencia(Util
						.obtenerArregloInteger(licencias));
			} else {
				registroUsuarios.setLicencia(null);
			}
			registroUsuarios.setUn_Nombre(nombre);
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
			registroUsuarios.setUn_Departamento(Integer
					.valueOf(dpto != null ? dpto : "-1"));
			registroUsuarios.setUn_Municipio(Integer
					.valueOf(mcpio != null ? mcpio : "-1"));

			boolean registrado = registroUsuarios.registrarUsuario();

			if (registrado) {
				req.getSession().setAttribute("errorRegistro", "No");
				req.getSession().setAttribute("usr_tmp", null);
				ManejoCorreo.enviarCorreo(req, licencias);
				req.getSession().invalidate();
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
				usr_tmp.setMunicipio(Integer.valueOf(mcpio));
				usr_tmp.setLicencias(licencias);

				req.getSession().setAttribute("usr_tmp", usr_tmp);
				req.getSession().setAttribute("errorRegistro", "Yes");
				resp.sendRedirect("pub/registroUsuario.jsp");
			}

		}

	}

}
