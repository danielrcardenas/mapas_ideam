<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Monitoreo de Bosques y Carbono</title>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	LenguajeI18N i18n = new LenguajeI18N();
	i18n.setLenguaje("es");
	i18n.setPais("CO");
	ResourceBundle msj = i18n.obtenerMensajeIdioma();
	request.getSession().setAttribute("i18n", msj);
%>
<script type="text/javascript">
function redireccion(path){
	var dir="";
	dir = path+'pub/recordarClave.jsp';	
	window.location.href = dir;
}
</script>
</head>
<body onload="redireccion('<%=basePath%>');">
</body>
</html>