<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.entities.InventarioSuelo"%>
<%@page import="co.gov.ideamredd.entities.Metodologia"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modificar Inventario de Suelo</title>
<link rel="stylesheet" type="text/css"
	href="../css/jsDatePick_ltr.min.css" />
<link rel="stylesheet" href="../css/accordion_style.css" type="text/css" />
<script type="text/javascript" src="../js/validadores.js"></script>
<script type="text/javascript" src="../js/jquery.1.4.2.js"></script>
<script type="text/javascript" src="../js/jsDatePick.jquery.min.1.3.js"></script>
<script type="text/javascript" src="../js/accordion.js"></script>
<script type="text/javascript" src="../js/seleccion.js"></script>

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<link href="../css/PopUp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/popup.min.js"></script>
<script type="text/javascript">
	window.onload = function() {
		new JsDatePick({
			useMode : 2,
			target : "newFechaTomaDatos",
			dateFormat : "%Y-%m-%d"
		});
	};

	function verificarPDF(archivo) {
		extension = (archivo.substring(archivo.lastIndexOf("."))).toLowerCase();

		if (extension == ".pdf") {
			return 1;
		} else {
			return 0;
		}
	}

	function verifiModificacion() {
		var nombreM = document.getElementById("metodNombre");
		var archivoM = document.getElementById("fileMetodologia");
		var profToma = document.getElementById("newProfToma");
		var textura = document.getElementById("newTextura");
		var densApar = document.getElementById("newDensAparente");
		var flujo = document.getElementById("newFlujoCO2");
		var fecha = document.getElementById("newFechaTomaDatos");

		if (nombreM.value == false || archivoM.value == false
				|| profToma == false || textura.value == false
				|| densApar == false || flujo.value == false
				|| fecha.value == false) {
			alert("Debe completar los datos");
			return false;
		} else {
			if (verificarPDF(archivoM.value) == 1 || archivoM.value == "No") {
				alert("Los datos fueron actualizados exitosamente");
				return true;
			} else {
				alert("Solo puede cargar archivos PDF");
				return false;
			}
		}
	}

	function activaCampos() {
		document.modificaF.metodNombre.disabled = !document.modificaF.metodNombre.disabled;
		document.modificaF.fileMetodologia.disabled = !document.modificaF.fileMetodologia.disabled;
		document.modificaF.metodNombre.type = "text";
		document.modificaF.fileMetodologia.type = "file";
		document.modificaF.hayMetod.value = "true";
	}

	function desactivaCampos() {
		document.modificaF.metodNombre.disabled = "disabled";
		document.modificaF.fileMetodologia.disabled = "disabled";
		document.modificaF.metodNombre.type = "hidden";
		document.modificaF.fileMetodologia.type = "hidden";
		document.modificaF.hayMetod.value = "false";
	}

	function comprobarOtraMetod() {
		var lista = document.getElementById("newMetodologia");

		// Obtener el índice de la opción que se ha seleccionado
		var indiceSeleccionado = lista.selectedIndex;
		// Con el índice y el array "options", obtener la opción seleccionada
		var opcionSeleccionada = lista.options[indiceSeleccionado];

		// Obtener el valor y el texto de la opción seleccionada
		var textoSeleccionado = opcionSeleccionada.text;
		var valorSeleccionado = opcionSeleccionada.value;

		if (textoSeleccionado == "Otra") {
			activaCampos();
		} else {
			desactivaCampos();
		}
	}
</script>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	ArrayList<InventarioSuelo> invSuelos = (ArrayList<InventarioSuelo>) session
			.getAttribute("invSuelos");
	String metodologias = (String) session.getAttribute("metodologias");
	int indice = (Integer) session.getAttribute("indice");
	InventarioSuelo iSuelo;
	iSuelo = invSuelos.get(indice);
%>
</head>
<body>
	<div id="contenedor">

		<div id="head">
			<h1>Modificar Inventario de Suelo</h1>
		</div>

		<div id="menu"></div>

		<div id="slices"></div>

		<div id="cuerpo">
			<form action="<%=basePath%>modificarInventarioSueloServlet"
				method="post" name="modificaF" enctype="multipart/form-data"
				onsubmit="return verifiModificacion()">
				<input type="hidden" name="parcelaID" id="parcelaID"
					value="<%=iSuelo.getParcelaId()%>"> <input type="hidden"
					name="parcelaNombre" id="parcelaNombre"
					value="<%=iSuelo.getParcela()%>">
				<%
					out.print("<h4>Metodologia:");
					iSuelo = (InventarioSuelo) invSuelos.get(indice);
					out.print(iSuelo.getMetodologiaNombre() + "</h4>");
				%>
				<input type="hidden" name="hayMetod" value="false"> <select
					name='newMetodologia' id='newMetodologia' SIZE=1
					onchange="comprobarOtraMetod()">
					<%
						out.print(metodologias);
					%>

				</select>
				<p>
					<input title="Nombre Metodologia" type="hidden" name="metodNombre"
						id="metodNombre" value="Nombre Metodologia" disabled="disabled">
				</p>
				<input type="hidden" value="_" name="metodEcuacion"
					disabled="disabled">
				<p>
					<input title="Archivo Metodologia" type="hidden"
						name="fileMetodologia" id="fileMetodologia" value="No"
						disabled="disabled">
				</p>
				<%
					out.print("</p>");
				%>
				<h4>Profundidad Toma:</h4>
				<input type="text" value=<%=iSuelo.getProfundidadToma()%>
					name="newProfToma" id="newProfToma"
					onkeypress="return valideValNum(event)">
				<%
					out.print("</p>");
				%>
				<h4>Textura:</h4>
				<input type="text" value=<%=iSuelo.getTextura()%> name="newTextura"
					id="newTextura">
				<%
					out.print("</p>");
				%>
				<h4>Densidad Aparente:</h4>
				<input type="text" value=<%=iSuelo.getDensidadAparente()%>
					name="newDensAparente" id="newDensAparente"
					onkeypress="return valideValNum(event)">
				<%
					out.print("</p>");
				%>
				<h4>Flujo CO2:</h4>
				<input type="text" value=<%=iSuelo.getFlujoCO2()%>
					name="newFlujoCO2" id="newFlujoCO2"
					onkeypress="return valideValNum(event)">
				<%
					out.print("</p>");
				%>
				<h4>Fecha Toma Datos:</h4>
				<input id="newFechaTomaDatos" name="newFechaTomaDatos"
					onkeypress="return noKeyData(event);"
					value=<%=iSuelo.getFechaTomaDatos()%>>
				<%
					out.print("</p>");
				%>
				<input type="submit" value="Guardar Cambios"> <input
					type="button" value="Cancelar" onclick="javascript:history.back()">
			</form>
		</div>

		<div id="footer"></div>

	</div>
</body>
</html>