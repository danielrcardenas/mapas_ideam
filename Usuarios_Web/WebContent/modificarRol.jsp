<%@page import="co.gov.ideamredd.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.entities.Rol"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modificar Rol</title>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	String rol = request.getParameter("id");
	Rol rolModificar = CargaDatosInicial.consultaRol(Integer.valueOf(rol));  
%> 
</head>
<body>
	<form id="modificarRol" name="modificarRol" action="<%=basePath%>modificarRol" method="post">
		<div id="header"></div>
		<div id="left"></div>
		<div id="body">
			<div>
				<div>
					<div>
						<label>Nombre</label>
					</div>
					<div>
						<input type="hidden" id="consecutivo" name="consecutivo" value="<%=rolModificar.getConsecutivo()%>">  
						<input type="text" id="nombre" name="nombre" value="<%=rolModificar.getNombre()%>">
					</div>
				</div>
				<div>
					<div>
						<label>Descripción</label>
					</div>
					<div>
						<textarea rows="5" cols="20" id="descripcion" name="descripcion"><%=rolModificar.getDescripcion() %></textarea>
					</div>
				</div>
			</div>
			<div>
				<div>
					<input type="submit" value="Modificar">
				</div>
				<div>
					<input type="button" value="Cancelar" onclick="javascript:window.history.go(-1);">
				</div>
			</div>
		</div>
		<div id="footer"></div>
	</form>
</body>
</html>