<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registrar Datos de Especies</title>
<script type="text/javascript" src="../js/suggest.js"></script>
<script type="text/javascript" src="../js/prototype.js"></script>
<link rel="stylesheet" href="../css/estiloSuggest.css" type="text/css" />

<script type="text/javascript" language="javascript">
	
<%String dat = "'Java', 'JavaScript', 'Perl', 'Ruby', 'PHP', 'Python', 'C',"
					+ "'C++', '.NET', 'MySQL', 'Oracle', 'PostgreSQL'";%>
	var list = [
<%=dat%>
	];

	var start = function() {
		new Suggest.Local("Division", "sugDivision", list);
		new Suggest.Local("Clase", "sugClase", list);
		new Suggest.Local("Orden", "sugOrden", list);
		new Suggest.Local("Familia", "sugFamilia", list);
		new Suggest.Local("Genero", "sugGenero", list);
	};
	window.addEventListener ? window.addEventListener('load', start, false)
			: window.attachEvent('onload', start);

	function limpiarForm() {
		document.getElementById("Division").value = "";
		document.getElementById("Clase").value = "";
		document.getElementById("Orden").value = "";
		document.getElementById("Familia").value = "";
		document.getElementById("Genero").value = "";
		document.getElementById("Especie").value = "";
	}
</script>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
</head>
<body>
	<h1>Registrar Datos de Especies</h1>
	<form action="<%=basePath%>registrarEspecieCatalogoServlet"
		method="post">
		<input type="hidden" name="Reino" value="Plantae">
		<div style="margin-left: 30px; margin-top: 4px;">
			Division<input id="Division" type="text" name="Division" value=""
				autocomplete="off" size="20" style="display: block">
			<div id="sugDivision" style="display: none;"></div>

			Clase<input id="Clase" type="text" name="Clase" value=""
				autocomplete="off" size="20" style="display: block">
			<div id="sugClase" style="display: none;"></div>

			Orden<input id="Orden" type="text" name="Orden" value=""
				autocomplete="off" size="20" style="display: block">
			<div id="sugOrden" style="display: none;"></div>

			Familia<input id="Familia" type="text" name="Familia" value=""
				autocomplete="off" size="20" style="display: block">
			<div id="sugFamilia" style="display: none;"></div>

			Genero<input id="Genero" type="text" name="Genero" value=""
				autocomplete="off" size="20" style="display: block">
			<div id="sugGenero" style="display: none;"></div>
			Especie<br /> <input id="Especie" type="text" name="Especie"
				size="35" value=""><br /> Clasificacion Segun UICN<br /> <select
				id="Especie" name="Especie">
				<option value="extinto" id="extinto">Extinto(EX)</option>
				<option value="extintoSilvestre" id="extintoSilvestre">Extinto
					en estado silvestre (EW)</option>
				<option value="critico" id="critico">Criticamente amenazado
					(CR)</option>
				<option value="peligro" id="peligro">En peligro (EN)</option>
				<option value="vulnerable" id="vulnerable">Vulnerable (VU)</option>
				<option value="amenazado" id="amenazado">Casi amenazado
					(NT)</option>
				<option value="preocupacionMenor" id="preocupacionMenor">Preocupacion
					menor (LC)</option>
				<option value="sinDatos" id="sinDatos">Datos insuficientes
					(DD)</option>
				<option value="noEvaluado" id="noEvaluado">No evaluado (NE)</option>
			</select>
		</div>
		<input type="submit" id="Guardar" name="Guardar" value="Guardar">
	</form>
	<input type="button" value="Registrar Otra Especie"
		name="RegistrarOtra" onclick="limpiarForm()">
	<input type="button" value="Cancelar" name="Cancelar"
		onclick="javascript:history.back()">
</body>
</html>