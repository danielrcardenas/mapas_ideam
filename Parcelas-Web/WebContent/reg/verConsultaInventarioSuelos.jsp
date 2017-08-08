<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.entities.InventarioSuelo"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consulta Inventarios Suelos</title>
<%
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/";

	ArrayList<InventarioSuelo> invSuelos = (ArrayList<InventarioSuelo>) session
	.getAttribute("invSuelos");
	
	ArrayList<Integer> indicesChBox = new ArrayList<Integer>();
	
	request.setAttribute("lista", invSuelos);
	String iSuelosSelect="";
	int totalCHBox=0;
	InventarioSuelo iSuelo;
%>
<script type="text/javascript">
	function VerificaSubmit(totalCHBox, tipo) {
		var iSuelosSelec = document.getElementById("ISseleccionados");
		var aux = "";
		var totalC = 0;
		var checkBox;
		var hayArch;
		var j;

		if (tipo == "Detallar") {

			for (j = 0; j < totalCHBox; j++) {
				checkBox = document.getElementById("invSuelo" + j);
				if (checkBox.checked) {
					totalC++;
					aux = aux + checkBox.value + ",";
				}
			}
			if (totalC == 0) {
				alert("Debe seleccionar almenos un registro");
				return 0;
			} else {
				iSuelosSelec.value = aux;
				return 1;
			}
		} else if (tipo == "DescargarMetodologia") {

			for (j = 0; j < totalCHBox; j++) {
				checkBox = document.getElementById("invSuelo" + j);
				hayArch = document.getElementById("hayArch" + j);
				if (checkBox.checked) {
					if (hayArch.value == "0") {
						alert("Algunas Metodologianas no\nContienen archivos relacionados."
								+ "\nPor favor verifique si existe(n) en detallar.");
						return 0;
					}
					totalC++;
					aux = aux + checkBox.value + ",";
				}
			}
			if (totalC == 0) {
				alert("Debe seleccionar almenos un registro");
				return 0;
			} else {
				iSuelosSelec.value = aux;
				return 1;
			}
		} else {
			for (j = 0; j < totalCHBox; j++) {
				checkBox = document.getElementById("invSuelo" + j);
				if (checkBox.checked) {
					totalC++;
					aux = aux + checkBox.value;
				}
			}
			if (totalC == 0 || totalC > 1) {
				alert("Solo Puede seleccionar un registro para modificar,\n"
						+ "o posiblemente no selecciono ninguno.");
				return 0;
			} else {
				iSuelosSelec.value = aux;
				return 1;
			}
		}

	}

	function VerificaRegistro(totalCHBox, valor) {
		var iSuelosSelec = document.getElementById("ISseleccionados");
		iSuelosSelec.value = valor;
		return 1;

	}

	function enviarADetallar(basePath, totalCHBox, tipo) {
		var form = document.getElementById("FormAEnviar");
		form.action = basePath + "detallarInventarioSueloServlet";
		var result = VerificaSubmit(totalCHBox, tipo);
		if (result != 1) {
			return;
		}
		form.submit();
	}

	function enviarAModificar(basePath, totalCHBox, tipo) {
		var form = document.getElementById("FormAEnviar");
		form.action = basePath + "datosAModificarISueloServlet";
		var result = VerificaSubmit(totalCHBox, tipo);
		if (result != 1) {
			return;
		}
		form.submit();
	}

	function enviarARegistrar(basePath, totalCHBox, valor) {
		var form = document.getElementById("FormAEnviar");
		form.action = basePath + "datosARegistrarISueloServlet";
		var result = VerificaRegistro(totalCHBox, valor);
		if (result != 1) {
			return;
		}
		form.submit();
	}

	function enviarADescargarMetodologia(basePath, totalCHBox, valor) {
		var form = document.getElementById("FormAEnviar");
		form.action = basePath + "descargaMetodologiaServlet";
		var result = VerificaSubmit(totalCHBox, valor);
		if (result != 1) {
			return;
		}
		form.submit();
	}
</script>
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

			<form action="para validar..." method="post" name="FormAEnviar"
				id="FormAEnviar">
				<input type="hidden" title="" value="<%=invSuelos.size()%>"
					name="totalISuelos">
				<table border="1">
					<tr>
						<td></td>
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
						iSuelosSelect = "";
						indicesChBox.clear();
						if (invSuelos.size() > 0) {
							int cantidadParcelas = invSuelos.size();
							totalCHBox = 0;
							for (int i = 0; i < invSuelos.size(); i++) {
								iSuelo = (InventarioSuelo) invSuelos.get(i);
								if (iSuelo.getMetodologiaNombre() == null) {
									out.print("<tr><td>"
											+ "<input type=\"button\" value=\"Registrar\""
											+ "title=\""
											+ i
											+ "\""
											+ "onclick=\"enviarARegistrar('"
											+ basePath
											+ "','"
											+ totalCHBox
											+ "',this.title)\"></td>"
											+ "<td>"
											+ iSuelo.getParcela()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getMunicipioP()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getDepartamentoP()
											+ "</td>"
											+ "<td> No registra </td>"
											+ "<td> No registra </td>"
											+ "<td>No registra</td>"
											+ "<td>No registra</td>"
											+ "<td>No registra</td>"
											+ "<td>No registra</td></tr>");
								} else {
									indicesChBox.add(i);
									if (iSuelo.getMetodologiaArchivo() == null) {
										out.print("<input type=\"hidden\" name=\"hayArch"
												+ i + "\" id=\"hayArch" + i
												+ "\" value=\"0\">");
									} else {
										out.print("<input type=\"hidden\" name=\"hayArch"
												+ i + "\" id=\"hayArch" + i
												+ "\" value=\"1\">");
									}
									out.print("<tr><td> <input type=\"checkbox\" id=\"invSuelo"
											+ totalCHBox
											+ "\""
											+ "name=\"invSuelo"
											+ i
											+ "\""
											+ "value=\""
											+ i
											+ "\"></td>"
											+ "<td>"
											+ iSuelo.getParcela()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getMunicipioP()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getDepartamentoP()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getContactoNombre()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getMetodologiaNombre()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getProfundidadToma()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getTextura()
											+ "</td>"
											+ "<td>"
											+ iSuelo.getDensidadAparente()
											+ "</td>"
											+ "<td>" + iSuelo.getFlujoCO2() + "</td></tr>");
									totalCHBox++;
								}
							}

						} else {
							out.print("<h2>NO SE ENCONTRARON DATOS</h2>");
						}
					%>
				</table>
				<input type="hidden" value="" name="ISseleccionados"
					id="ISseleccionados"> <input type="button" value="Detallar"
					onclick="enviarADetallar('<%=basePath%>','<%=totalCHBox%>','Detallar')">
				<input type="button" value="Modificar"
					onclick="enviarAModificar('<%=basePath%>','<%=totalCHBox%>','Modificar')">
				<input type="button" value="Descargar Metodologia(s)"
					onclick="enviarADescargarMetodologia('<%=basePath%>','<%=totalCHBox%>','DescargarMetodologia')">
			</form>
		</div>

		<div id="footer"></div>

	</div>

</body>
</html>