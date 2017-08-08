<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.entities.InventarioSuelo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Detallar Inventario suelo</title>
<%
	ArrayList<InventarioSuelo> invSuelos = (ArrayList<InventarioSuelo>) session
			.getAttribute("invSuelos");
	ArrayList<Integer> indicesInvSuelos = (ArrayList<Integer>) session
			.getAttribute("indicesInvSuelos");
	int indice = (Integer) session.getAttribute("indice");
	InventarioSuelo iSuelo;
%>
</head>
<body>
	<h1>Detallar Inventario de suelo</h1>
	<%
		for (int i = 0; i < indicesInvSuelos.size(); i++) {
			iSuelo = (InventarioSuelo) invSuelos.get(indicesInvSuelos
					.get(i));
			out.print("<table border=\"1\"><tr><td colspan=\"2\">Nombre Parcela:"
					+ iSuelo.getParcela() + "</td></tr>");
			out.print("<tr><td colspan=\"2\">Nombre Metodologia:"
					+ iSuelo.getMetodologiaNombre() + "</td></tr>");
			if (iSuelo.getMetodologiaEcuacion() != null) {
				if (iSuelo.getMetodologiaEcuacion().toString() != "No") {
					out.print("<tr><td>Ecuacion Metodologia:Si</td>");
				} else {
					out.print("<tr><td>Ecuacion Metodologia:No</td>");
				}
			} else {
				out.print("<tr><td>Ecuacion Metodologia:No</td>");
			}
			if (iSuelo.getMetodologiaArchivo() != null) {
				if (iSuelo.getMetodologiaArchivo().toString() != "No") {
					out.print("<td>Archivo Metodologia: Si </td></tr>");
				} else {
					out.print("<td>Archivo Metodologia: No </td></tr>");
				}
			} else {
				out.print("<td>Archivo Metodologia: No </td></tr>");
			}
			out.print("<tr><td colspan=\"2\">Nombre Contacto:"
					+ iSuelo.getContactoNombre() + "</td></tr>");
			out.print("<tr><td>Pais Contacto:" + iSuelo.getContactoPais()
					+ "</td>");
			out.print("<td>Correo Contacto:" + iSuelo.getContactoCorreo()
					+ "</td></tr>");
			out.print("<tr><td>Telefono Contacto:"
					+ iSuelo.getContactoTelefono() + "</td>");
			out.print("<td>Municipio Contacto:"
					+ iSuelo.getContactoMunicipio() + "</td></tr>");
			out.print("<tr><td>Movil Contacto:" + iSuelo.getContactoMovil()
					+ "</td><td></td></tr>");
			out.print("<tr><td colspan=\"2\">Profundidad Toma:"
					+ iSuelo.getProfundidadToma() + "</td></tr>");
			out.print("<tr><td colspan=\"2\">Textura:"
					+ iSuelo.getTextura() + "</td></tr>");
			out.print("<tr><td colspan=\"2\">Densidad Aparente:"
					+ iSuelo.getDensidadAparente() + "</td></tr>");
			out.print("<tr><td colspan=\"2\">Flujo CO2:"
					+ iSuelo.getFlujoCO2() + "</td></tr>");
			out.print("<tr><td colspan=\"2\">Fecha toma de datos:"
					+ iSuelo.getFechaTomaDatos()
					+ "</td></tr></table><br/>");
		}
	%>
	<input type="button" value="Volver" onclick="javascript:history.back()">
</body>
</html>