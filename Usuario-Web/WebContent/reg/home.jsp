<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.ui.entities.Usuario"%>
<%@page import="co.gov.ideamredd.ui.dao.ConsultaWebUsuario"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Monitoreo de Bosques y Carbono</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";

	Usuario usuario = ConsultaWebUsuario.consultarUsuarioPorDoc(Integer
			.valueOf(request.getUserPrincipal().getName())); 

	request.getSession().setAttribute("usuario", usuario);
%>
</head>
<body>
	<h2>Menu Usuario</h2>
	<h4>
		Bienvenido
		<%=usuario.getNombre()%></h4>
	<%
		if (request.isUserInRole("USUARIO_PUBLICO_REGISTRADO")) {
	%>
	<input type="button" value="Editar Datos" class="boton"
		onclick="location.href='<%=basePath%>reg/modificarUsuario.jsp';">
	<%
		}
		if (request.isUserInRole("ADMINISTRADOR_GENERAL")) {
	%>
	<input type="button" value="Generar reporte" class="boton"
		onclick="location.href='<%=basePath%>admin/generarReportesUsuarios.jsp';">
	<input type="button" value="Consultar Usuarios" class="boton"
		onclick="location.href='<%=basePath%>admin/consultarUsuarios.jsp';">
	<form action="<%=basePath%>borrarArchivosServlet" method="post">
		<input type="submit" value="Borrar Temporales">
	</form>
	<%
		}
	%>
	<input type="button" value="Cerrar Sesion" class="boton"
		onclick="location.href='<%=basePath%>limpiarSesionServlet';">

</body>
</html>