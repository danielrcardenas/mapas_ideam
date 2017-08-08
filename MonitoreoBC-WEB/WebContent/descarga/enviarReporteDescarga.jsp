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
<%@page import="co.gov.ideamredd.web.descarga.servlets.EnviarReporteDescarga"%>
<%
Usuario usuario = UtilWeb.consultarUsuarioPorLogin(request.getUserPrincipal().getName());
usuario.setRolNombre(UtilWeb.consultarRolesUsuarioPorLogin(request.getUserPrincipal().getName()));
request.getSession().setAttribute("usuarioSesion", usuario);

Map<Integer,String> diccionarioPermisos = null;
if(usuario !=null) {
	diccionarioPermisos = ControlPermisos.consultaPermisos(usuario.getRolId());
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 75)) {
		String resultado = EnviarReporteDescarga.enviar();
		out.print(resultado);
	}
}
%>