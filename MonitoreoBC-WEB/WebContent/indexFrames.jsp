<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="java.util.ResourceBundle"%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sistema de Monitoreo de Biomasa y Carbono</title>
<%
String path = request.getContextPath();
LenguajeI18N i18n = new LenguajeI18N();
i18n.setLenguaje("es");
i18n.setPais("CO");
ResourceBundle msj = i18n.obtenerMensajeIdioma();
request.getSession().setAttribute("i18n", msj); 
request.getSession().setAttribute("i18nAux", i18n);
%>
</head>
<frameset rows="0%,*" border="0" bordercolor="white" style="background: white;">
 <frame name="deathFrame" src="/MonitoreoBC-WEB/closePag.jsp">
 <frame name="allFrame" src="/MonitoreoBC-WEB/index.jsp">
</frameset>
</html>