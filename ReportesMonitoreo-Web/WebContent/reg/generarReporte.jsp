<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
	CargaDatosSelect cds = null;
%>
<head>

<link rel="stylesheet" href="../css/ideam/datum-custom.css"
	type="text/css"></link>
<link rel="stylesheet" type="text/css"
	href="../custom/calendar/css/jscal2.css" />
<link rel="stylesheet" type="text/css"
	href="../custom/calendar/css/border-radius.css" />
<link rel="stylesheet" type="text/css"
	href="../custom/calendar/css/steel/steel.css" />
<link rel="stylesheet" href="../css/ideam/jqModal.css" type="text/css"></link>

<script type="text/javascript" src="../custom/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="../custom/jquery.organictabs.js"></script>
<script src="../custom/calendar/js/jscal2.js"></script>
<script src="../custom/calendar/js/lang/es.js"></script>
<script type="text/javascript" src="../custom/jqModal.js"></script>
<script type="text/javascript" src="../custom/seleccion.js"></script>
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>

<script type='text/javascript'>
	$(function() {
		$("#form-tabs").organicTabs({
			"speed" : 300
		});
	});

	function valoresTipoReporte() {
		var sel = document.forms[0].divterritorio;
		if (document.forms[0].treporte.value == 7) {
			sel.disabled = false;
			sel.value = -1;
			sel.options[1].style.display = 'none';
			sel.options[2].style.display = 'none';
			document.getElementById('l2').style.display = 'none';
			document.forms[0].periodo2.style.display = 'none';
		} else if (document.forms[0].treporte.value > 4
				&& document.forms[0].treporte.value < 7) {
			sel.disabled = false;
			sel.value = -1;
			sel.options[1].style.display = 'none';
			sel.options[2].style.display = 'none';
			document.getElementById('l2').style.display = 'block';
			document.forms[0].periodo2.style.display = 'block';
		} else if (document.forms[0].treporte.value > 0
				&& document.forms[0].treporte.value < 5) {
			sel.disabled = false;
			sel.value = -1;
			sel.options[1].style.display = 'block';
			sel.options[2].style.display = 'block';
			if (document.forms[0].treporte.value > 2
					&& document.forms[0].treporte.value < 5) {
				document.getElementById('l2').style.display = 'block';
				document.forms[0].periodo2.style.display = 'block';
			} else {
				document.getElementById('l2').style.display = 'none';
				document.forms[0].periodo2.style.display = 'none';
			}
		} else {
			sel.options[1].style.display = 'block';
			sel.options[2].style.display = 'block';
			document.getElementById('l2').style.display = 'none';
			document.forms[0].periodo2.style.display = 'none';
		}
		sel.value = -1;
	}

	function validar() {
		var passed = true;
		var mensaje = "Los siguientes campos son obligatorios:<br>";
		if (document.forms[0].treporte.value < 1) {
			mensaje = mensaje + "- Tipo de Reporte";
			passed = false;
		}
		if (!passed) {
			alerta(mensaje);
		} else {
			document.forms[0].submit();
		}

	}
</script>
<script type="text/javascript" src="custom/datum-validation.js"></script>
</head>

<body style="background-color: #FFFFFF">
	<div id="page-wrap">
		<div id="site-tree" align="left"><%=UbicacionActual.getArbol(request.getRequestURL()
					.toString())%></div>
		<h3 align="center">GENERACI&Oacute;N DE REPORTES</h3>
		<form action="../generarreporte" method="post">
			<fieldset style="margin: 10px; padding: 3px;">
				<input type="button" value="GENERAR" class="boton"
					onclick="validar();">
			</fieldset>
			<div id="form-tabs">
				<ul class="nav">
					<li class="nav last"><a href="#datos" class="current">Datos
							Generaci&oacute;n</a></li>
				</ul>
				<div class="list-wrap">
					<ul id="datos">
						<li>
							<div align="left" style="height: 450px; overflow: auto;">
								<fieldset style="padding: 10px;">
									<table>
										<tbody>
											<tr>
												<td><label>Tipo de Reporte:<span
														class="obligatorio">*</span></label></td>
												<td colspan="3"><select name="treporte" id="treporte"
													style="width: 420px;" title="Tipo de reporte a consultar"
													onchange="valoresTipoReporte();">
														<option value="-1">Seleccionar</option>
<%-- 														<%=cds.getTipoReporte()%> --%>
												</select></td>
											</tr>
											<tr>
												<td><label>Divisi&oacute;n del Territorio:</label></td>
												<td colspan="3"><select name="divterritorio"
													id="divterritorio" disabled="disabled" size="1"
													style="width: 420px;"
													title="Division territorial disponible de acuerdo al tipo de reporte">
														<option value="-1">Seleccionar</option>
														<%=cds.getDivisionTerritorio()%>
												</select></td>
											</tr>
											<tr>
												<td><label id="l1">Per&iacute;odo inicial:<label></td>
												<td><select name="periodo1" id="periodo1" size="1"
													style="width: 100px;"
													title="Año inicial para el periodo del reporte">
														<option value="-1">Seleccionar</option>
														<%=cds.getPeriodos2()%>
												</select></td>
												<td><label id="l2" style="display: none;">Per&iacute;odo
														final:<label></td>
												<td><select name="periodo2" id="periodo2" size="1"
													style="width: 100px; display: none;"
													title="Año final para el periodo del reporte">
														<option value="-1">Seleccionar</option>
														<%=cds.getPeriodos2()%>
												</select></td>
											</tr>
										</tbody>
									</table>
								</fieldset>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</form>
	</div>
</body>
</html>