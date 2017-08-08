<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Generar reporte</title>
</head>
<body>
<form action="reporteServlet" method="post">
	<label>Ingrese su nombre</label>
	
	<input type="text" name="nom" id="nom"></input>
	
	<label>Ingrese su telefono</label>
	
	<input type="text" name="tel" id="tel"></input>
	
	<input type="submit" value="enviar"></input>
	
</form>


</body>
</html>