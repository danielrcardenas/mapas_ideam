
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Monitoreo de Bosques y Carbono</title>
<script type="text/javascript" src="js/redireccion.js"></script>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";

String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	LenguajeI18N i18n = new LenguajeI18N();
	i18n.setLenguaje("es");
	i18n.setPais("CO");
	ResourceBundle msj = i18n.obtenerMensajeIdioma();
	request.getSession().setAttribute("i18n", msj); 
	request.getSession().setAttribute("i18nAux", i18n);
%>
<meta http-equiv="Refresh" content="2;url=<%=basePath2%>MonitoreoBiomasaCarbono/reg/indexLogOn.jsp">
</head>

<body>
Espere unos segundos mientras se redirecciona a la pagina de inicio, si no es asi pulse <a href="<%=basePath2%>MonitoreoBiomasaCarbono/reg/indexLogOn.jsp">aqui</a>.
</body>
</html>