<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Usuario Registrado</title>
<%
	int esRegistrado = (Integer) session.getAttribute("esRegistrado");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<script type="text/javascript">
	window.onload = function() {
		if (
<%=esRegistrado%>
	== 0) {
			document.getElementById("submRegistrar").type = "hidden";
			document.getElementById("submReportes").type = "hidden";
		}
	};
</script>
</head>
<body>
	<h1>
		Menu usuarios :
		<%
		if (esRegistrado == 1) {
			out.print("Administrador Tematico");
		} else {
			out.print("Usuario Publico");
		}
	%>
	</h1>
	<form action="<%=basePath%>irARegistrarEspecieCatalogoServlet"
		method="post">
		<input type="submit" id="submRegistrar"
			value="REGISTRAR DATOS DE ESPECIE">
	</form>
	<form action="<%=basePath%>irAConsultarEspecieServlet"
		method="post">
		<input type="submit" value="CONSULTAR ESPECIES FORESTALES">
	</form>
	<form action="<%=basePath%>consultarRangoDistribucionServlet"
		method="post">
		<input type="submit"
			value="CONSULTAR RANGO FAMILIAS, GENEROS Y ESPECIES">
	</form>
	<form action="<%=basePath%>consultarReportesEstadisticosServlet"
		method="post">
		<input type="submit" id="submReportes"
			value="CONSULTAR REPORTES ESTADISTICOS">
	</form>
</body>
</html>