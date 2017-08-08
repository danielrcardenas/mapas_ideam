<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.entities.Especie"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consulta especies</title>
<%
	int esRegistrado = (Integer) session.getAttribute("esRegistrado");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	ArrayList<Especie> listaEspecies = (ArrayList<Especie>) session
			.getAttribute("listaEspecies");
%>
</head>
<body>
	<h2>
		Consulta especies del catalogo:
		<%
		if (esRegistrado == 1) {
			out.print("Administrador Tematico");
		} else {
			out.print("Usuario Publico");
		}
	%>
	</h2>
	<h3>Busqueda Basica</h3>
	<form action="<%=basePath%>consultarEspecieCatalogoServlet"
		method="post">
		Criterio de busqueda:<input type="text" name="palabraClave"
			id="palabraClave"> <input type="hidden" value="Basica"
			name="tipoBusqueda" id="tipoBusqueda"> <input type="submit"
			value="Buscar">
	</form>
	<h3>Busqueda Avanzada</h3>
	<p>En construccion.</p>
	<table border="1">
		<tr>
			<td></td>
			<td>ID ESPECIE</td>
			<td>NOMBRE</td>
			<td>DENSIDAD MADERA</td>
			<td>DENSIDAD MADERA GENERO</td>
			<td>FAMILIA</td>
			<td>DENSIDAD MADERA FAMILIA</td>
			<td>CLASIFICACION UICN</td>
			<td>CLASIFICACION CITES</td>
		</tr>
		<%
			for (int i = 0; i < listaEspecies.size(); i++) {
				Especie especie = listaEspecies.get(i);
				out.print("<tr>");
				out.print("<td></td>");
				out.print("<td>" + especie.getEspecieId() + "</td>");
				out.print("<td>" + especie.getEspecie() + "</td>");
				out.print("<td></td>");
				out.print("<td></td>");
				out.print("<td>" + especie.getFamilia() + "</td>");
				out.print("<td></td>");
				out.print("<td>" + especie.getUICN() + "</td>");
				out.print("<td>" + especie.getCITES() + "</td>");
			}
		%>
	</table>
</body>
</html>