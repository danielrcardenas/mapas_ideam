<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.entities.InventarioSuelo"%> 
<%@page import="co.gov.ideamredd.util.Util" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<%

	String archivoEncabezados = "co/gov/ideamredd/servlets/parcela";
	ResourceBundle encabezados = ResourceBundle
			.getBundle(archivoEncabezados);
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";

	String rutaImagen = Util.obtenerClave("rutaImagenesReportes", encabezados);
	rutaImagen=rutaImagen+"1.jpg";
	System.out.println(rutaImagen+"1.png");
	
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consulta Inventarios Suelos</title>
<%
	//ArrayList<InventarioSuelo> invSuelos = (ArrayList<InventarioSuelo>)session.getAttribute("invSuelos");
%>
</head>
<body>

	<div id="contenedor">

		<div id="head">
			<h1>Consulta Inventarios de suelos</h1>
		</div>

		<div id="menu"></div>

		<div id="slices"></div>

		<div id="cuerpo">
			<form action="<%=basePath%>consultaInventarioSueloServlet"
				method="post">
				<input type="text" name="parcelasIDs"> <input type="submit"
					value="Consultar">
			</form>
			<table border="1">
				<tr>
					<td>NOMBRE</td>
					<td>MUNICIPIO</td>
					<td>DEPARTAMENTO</td>
					<td>CONTACTO</td>
					<td>METODOLOGIA</td>
					<td>PROFUNDIDAD TOMA</td>
					<td>TEXTURA</td>
					<td>DENSIDAD AP.</td>
					<td>FLUJO CO2</td>
				</tr>
				<%
					/*if(invSuelos.size()>0)
					{
						InventarioSuelo iSuelo = (InventarioSuelo)invSuelos.get(0);
					    out.print("<h>"+iSuelo.getParcela()+"</h>");
					}*/
				%>
			</table>

		</div>

		<div id="footer"></div>

	</div>

</body>
</html>