<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.web.reportes.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<%
String ua=request.getHeader("User-Agent").toLowerCase();
boolean es_movil = ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-");
String doctype = "";
String estilo = "estilos.css";

//es_movil = true;

if(es_movil) { 
	doctype = " <!DOCTYPE html PUBLIC '-//WAPFORUM//DTD XHTML Mobile 1.0//EN' 'http://www.wapforum.org/DTD/xhtml-mobile10.dtd' >"; 
	estilo = "estilos_movil.css";
} 
else {
	doctype = " <!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd' >";
	estilo = "estilos_pc.css";
} 
%>

<% out.print(doctype); %>
<html >
<%
	CargaDatosSelect cds = null;
%>
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>

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