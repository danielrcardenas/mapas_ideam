<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.*"%>
<%@page import="co.gov.ideamredd.web.admin.dao.ReporteGeoproceso"%>
<%
	Usuario usuario = UtilWeb.consultarUsuarioPorLogin(request.getUserPrincipal().getName());
	usuario.setRolNombre(UtilWeb.consultarRolesUsuarioPorLogin(request.getUserPrincipal().getName()));
	request.getSession().setAttribute("usuarioSesion", usuario);

	Map<Integer, String> diccionarioPermisos = null;
	if (usuario != null) {
		diccionarioPermisos = ControlPermisos.consultaPermisos(usuario.getRolId());
		if (ControlPermisos.tienePermiso(diccionarioPermisos, 75) || true) {
			
			Integer tipoReporte = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("tipoReporte"), "-1"));
			Integer divisionTerritorio = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("divisionTerritorio"), "-1"));
			String nombreDivisionTerritorio = Auxiliar.nzObjStr(request.getParameter("nombreDivisionTerritorio"), "");
			Integer periodoUno = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("periodoUno"), "-1"));
			Integer periodoDos = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("periodoDos"), "-1"));
			String identImagen = Auxiliar.nzObjStr(request.getParameter("identImagen"), "").trim();

			String resultado = ReporteGeoproceso.generar(divisionTerritorio, tipoReporte, periodoUno, periodoDos, identImagen, nombreDivisionTerritorio);
			out.print("<div class='div_resultado_commando'>"+resultado+"</div>");
		}
	}
%>