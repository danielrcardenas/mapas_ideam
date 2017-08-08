<%@page import="co.gov.ideamredd.web.admin.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.admin.entities.Depto"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.web.admin.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.web.admin.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.admin.entities.Municipios"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<html class='no-js'>
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta charset="utf-8" />
<%
HttpSession sesion = request.getSession(false); 
if (!request.isRequestedSessionIdValid() || sesion == null) { 
	response.sendRedirect("/MonitoreoBC-WEB"); 
	return;
}

ResourceBundle msj = (ResourceBundle)sesion.getAttribute("i18n");

try {
	if (msj == null) {
		sesion = request.getSession(true);
	    LenguajeI18N i18n = new LenguajeI18N();
		i18n.setLenguaje("es");
		i18n.setPais("CO");
		msj = i18n.obtenerMensajeIdioma();
		sesion.setAttribute("i18n", msj); 
		sesion.setAttribute("i18nAux", i18n);
	}
}
catch (Exception e) {
	response.sendRedirect("..");
	//return;		
}

ArrayList<Noticias> noticias = CargaNoticiasYEventos.cargaNoticias();
ArrayList<Noticias> eventos = CargaNoticiasYEventos.cargaEventos();

sesion.setAttribute("noticia", noticias);
sesion.setAttribute("eventos", eventos);

Usuario usuario = null;
LenguajeI18N i18n = (LenguajeI18N)sesion.getAttribute("i18nAux");
if(request.getUserPrincipal() !=null) {
	usuario = UtilWeb.consultarUsuarioPorLogin(request.getUserPrincipal().getName());
	usuario.setRolNombre(UtilWeb.consultarRolesUsuarioPorLogin(request.getUserPrincipal().getName()));
}

Map<Integer,String> diccionarioPermisos = null;
if(usuario !=null) {
	diccionarioPermisos = ControlPermisos.consultaPermisos(usuario.getRolId());
}
	
ArrayList tipopersona = null;
ArrayList departamento = null;
ArrayList totales = null;

String fechaIni="",fechaFin="";

try{
	fechaIni = (String)session.getAttribute("fechaIni");
	fechaFin = (String)session.getAttribute("fechaFin");
	tipopersona = (ArrayList)session.getAttribute("estadistica1");
	departamento = (ArrayList)session.getAttribute("estadistica2");
	totales = (ArrayList)session.getAttribute("estadistica3");
}catch(Exception e){
	//Control variables nulas
}
		
%>

<%if(usuario != null)
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 143))
	{
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />

<link rel='stylesheet' type='text/css' media='all' href='../js/jscalendar/calendar-green.css' title='green' /> 
<script type='text/javascript' src='../js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='../js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='../js/jscalendar/calendar-setup.js'></script>
<script type="text/javascript" src="../js/general.js"></script>

<script>
$(function() {
	$( "#accordion" ).accordion({
		heightStyle: "content"
	});
});

$(document).ready(function() {
	inicializarNavegador();
});

</script>

<script>

function validar() {
	var passed = true;
	var mensaje = "Los siguientes campos son obligatorios:\n";
	if (document.getElementById("email").value == "") {
		mensaje = mensaje + "- Correo electronico";
		passed = false;
	} else if (!valideMail(document.getElementById("email").value)) {
		mensaje = "El Correo electronico ingresado no es valido";
		passed = false;
	}
	if (!passed) {
		alert(mensaje);
	}
	return passed;
}

//*************************USUARIOS**************************************
var tipoPersonaLista = null;
var nombreLista = null;
var tipoIdenLista = null;
var documentoLista = null;
var paisLista = null;
var departamentoLista = null;
var ciudadLista = null;
var direccionLista = null;
var telefonoLista = null;
var movilLista = null;
var emailLista = null;
var licenciaLista = null;
var datUsuarios = null;
var esActivo = null;

function todos(chkbox) {
	for ( var i = 0; i < document.forms[0].elements.length; i++) {
		var elemento = document.forms[0].elements[i];
		if (elemento.type == "checkbox")
			elemento.checked = chkbox.checked;
	}
}

function detallar(docUsuario) {
	window.scrollTo(0, 0);
	document.getElementById("fondoBloqueo").style.display = "block";
	document.getElementById("popUpDetallar").style.display = "block";

	if (documentoLista != null) {
		for (i = 0; i < documentoLista.length; i++) {
			if (documentoLista[i] == docUsuario) {
				document.getElementById("detTipoPersona").value = tipoPersonaLista[i];
				document.getElementById("detNombre").value = nombreLista[i];
				document.getElementById("detTipoIdentificacion").value = tipoIdenLista[i];
				document.getElementById("detNumeroIdentificacion").value = documentoLista[i];
				document.getElementById("detPais").value = paisLista[i];
				document.getElementById("detDepartamento").value = departamentoLista[i];
				document.getElementById("detCiudad").value = ciudadLista[i];
				document.getElementById("detDireccion").value = direccionLista[i];
				document.getElementById("detTelefono").value = telefonoLista[i];
				document.getElementById("detMovil").value = movilLista[i];
				document.getElementById("detEmail").value = emailLista[i];
				document.getElementById("detLicencias").value = licenciaLista[i];
				break;
			}
		}
	}
}

function desactivaDetallar() {
	document.getElementById("fondoBloqueo").style.display = "none";
	document.getElementById("popUpDetallar").style.display = "none";
}

function asignarRol(docUsuario) {
	window.scrollTo(0, 0);
	document.getElementById("fondoBloqueo").style.display = "block";
	document.getElementById("popUpAsignarRol").style.display = "block";
	document.getElementById("guardaRol").title = docUsuario;

	if (documentoLista != null) {
		for (i = 0; i < documentoLista.length; i++) {
			if (documentoLista[i] == docUsuario) {
				document.getElementById("asrolNombre").value = nombreLista[i];
				break;
			}
		}
	}

	var aux1 = document.getElementById("rolHid" + docUsuario).value;
	document.getElementById("chkRol" + aux1).selected = true;
}

function guardaAsignarRol(docUsuario) {
	var idUsAux = document.getElementById("userHid" + docUsuario).value;
	var selectedRol = document.getElementById("selectRol").selectedIndex + 1;

	document.getElementById("idRolCambioRol").value = selectedRol;
	document.getElementById("idUsuarioCambioRol").value = idUsAux;

	document.getElementById("formCambioRol").submit();
}

function desactivaAsignarRol() {
	document.getElementById("fondoBloqueo").style.display = "none";
	document.getElementById("popUpAsignarRol").style.display = "none";
}

function activarUs(idUsuario) {
	document.getElementById("idUsuario").value = idUsuario;
	document.getElementById("tipoOperacion").value = 1;
	document.getElementById("formActivacion").submit();
}

function desactivarUs(idUsuario) {
	document.getElementById("idUsuario").value = idUsuario;
	document.getElementById("tipoOperacion").value = 0;
	document.getElementById("formActivacion").submit();
}

function validarModificar() {
	var isChecked = false;
	if (document.getElementById("selusuarios").checked)
		isChecked = true;
	if (isChecked)
		document.forms[2].submit();
	else
		alert('Seleccione un resultado para modificar');
}

function validarDesactivar() {
	var isChecked = false;
	if (document.getElementById("selusuarios").checked)
		isChecked = true;
	if (isChecked)
		document.forms[3].submit();
	else
		alert('Seleccione un resultado para desactivar/activar');
}

var parFiltradas = new Array();
var parce;
var parametros;
var codigos;
var ind;
var idForm = 0;
var idActual;

$(document).ready(
	function() {
		idActual = 1;
		if (datUsuarios != null) {
			if (datUsuarios.length != 0) {//cambiar para los filtros 
				resultadosContactos(datUsuarios.length, 'resultados', 'consultas');
				cargarDatos(datUsuarios, 'consultas', 3, titulos, null, 0, 5);
				crearPaginas(datUsuarios.length, 'consultas', 1);
			} else {
				busquedaVacia('resultados', 'consultas');
			}
		}
	}
);

function borrarArchivos() {
	document.getElementById('formBorrar').submit();
}

function popUpUsuariosAux() {
	var coords = getAbsoluteElementPosition(document
			.getElementById("icoUsuarios"));

	document.getElementById("popUpUsuarios").style.left = coords.left
			+ "px";
	document.getElementById("popUpUsuarios").style.top = coords.top + 15
			+ "px";
}

function popUpUsuariosOpen() {
	var coords = getAbsoluteElementPosition(document
			.getElementById("icoUsuarios"));

	document.getElementById("popUpUsuarios").style.left = coords.left
			+ "px";
	document.getElementById("popUpUsuarios").style.top = coords.top + 15
			+ "px";
	document.getElementById("popUpUsuarios").style.display = "block";
}

function popUpUsuariosClose() {
	document.getElementById("popUpUsuarios").style.display = "none";
}

function formConsultarUsuario() {
	document.getElementById("documento").value = document
			.getElementById("documentoAux").value;
	document.getElementById("tipodoc").value = document
			.getElementById("tipodocAux").value;
	document.getElementById("nombre").value = document
			.getElementById("nombreAux").value;

	document.getElementById("formConsulta").submit();
}

function descargaXLS() {
	document.getElementById("tipoDescarga").value = "XLS";
	document.getElementById("formDescarga").submit();
}
function descargaPDF() {
	document.getElementById("tipoDescarga").value = "PDF";
	document.getElementById("formDescarga").submit();
}

function validarFechas() {
	document.getElementById("fini").value = document.getElementById("finiAux").value;
	document.getElementById("ffin").value = document.getElementById("ffinAux").value;

	var fechaAux=new Array(3);
	if (document.getElementById("fini").value != '') {
		fechaAux=(document.getElementById("fini").value).split('/');
		document.getElementById("fini").value=fechaAux[2]+"/"+fechaAux[0]+"/"+fechaAux[1];
	}
	if (document.getElementById("ffin").value != '') {
		fechaAux=(document.getElementById("ffin").value).split('/');
		document.getElementById("ffin").value=fechaAux[2]+"/"+fechaAux[0]+"/"+fechaAux[1];
	}
	
	if (document.getElementById("fini").value == ''
			&& document.getElementById("ffin").value != '') {
		alert("Por favor, especifique una fecha inicial");
		return false;
	}

	var fini = document.getElementById("finiAux").value;
	var ffin = document.getElementById("ffinAux").value;

	if (fini > ffin) {
		alert("La fecha inicial no puede ser posterior a la final.");
		return false;
	}
	
	document.getElementById("formFechas").submit();
	return true;
}
</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja"><%=msj.getString("Generar_Reporte_de_Usuarios") %></h2>

							<div id="content">
								<div class="content-inner" style="font-size: 14px">

									<div id="block-accordeon-registro-parcela" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("reportesUsuarios.reporteUsuarios")%></h3>
												<div id="form-datos-parcela">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("reportesUsuarios.fechaIni")%>:</label>
														<input type="text" id="finiAux" style="width: 300px" readonly>
														<script type='text/javascript'>
														Calendar.setup({
															inputField     :    'finiAux',				// id of the input field
															ifFormat       :    '%m/%d/%Y',	// format of the input field
															showsTime      :    false,					// will display a time selector
															button         :    'finiAux',				// trigger for the calendar (button ID)
															singleClick    :    false,					// double-click mode
															step           :    1						// show all years in drop-down boxes (instead of every other year as default)
														});
														</script>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("reportesUsuarios.fechaFin")%>:</label> 
														<input type="text" id="ffinAux" style="width: 300px" readonly>
														<script type='text/javascript'>
														Calendar.setup({
															inputField     :    'ffinAux',				// id of the input field
															ifFormat       :    '%m/%d/%Y',	// format of the input field
															showsTime      :    false,					// will display a time selector
															button         :    'ffinAux',				// trigger for the calendar (button ID)
															singleClick    :    false,					// double-click mode
															step           :    1						// show all years in drop-down boxes (instead of every other year as default)
														});
														</script>
													</div>
													<div class="form-actions">
														<input type="button" class="btn btn-default"
															value="<%=msj.getString("reportesUsuarios.Consultar")%>" onclick="validarFechas()"
															style="font-size: 13px; margin-left: -16px"> <input
															type="button"
															value="<%=msj.getString("consultaUsuarios.volver")%>"
															style="font-size: 13px;" class="btn btn-default"
															onclick="javascript:history.back(1);">
													</div>
													<div></div>
													<div>
														<%
															try {
																if (tipopersona != null) {
																	if ((Integer) totales.get(0) > 0) {
														%>
														<input type="button" value="<%=msj.getString("reportesUsuarios.descarga")%> XLSX"
															onclick="descargaXLS()"> <input type="button"
															value="<%=msj.getString("reportesUsuarios.descarga")%> PDF" onclick="descargaPDF()">
														<%
															}
														%>
														<div class="list-wrap" style="overflow: auto;">
															<%
																if (fechaIni != null) {
																			if (!fechaIni.equals("")) {
															%>
															<h3 align="left">
																<%=msj.getString("reportesUsuarios.desde")%>:&nbsp;&nbsp;&nbsp;<%=fechaIni%></h3>
															<%
																}
																		}
																		if (fechaFin != null) {
																			if (!fechaFin.equals("")) {
															%>
															<h3 align="left">
																<%=msj.getString("reportesUsuarios.hasta")%>:&nbsp;&nbsp;&nbsp;<%=fechaFin%></h3>
															<%
																}
																		}
															%>
															<h3 align="left"><%=msj.getString("reportesUsuarios.totalUsuarios")%>:&nbsp;&nbsp;&nbsp;<%=totales.get(0)%></h3>
															<h3 align="left"><%=msj.getString("Total_usuarios_publicos_registrados")%>:&nbsp;&nbsp;&nbsp;<%=totales.get(1)%></h3>
															<br />
															<table width="99%" border="1">
																<thead>
																	<tr>
																		<th colspan="2" class="odd"><%=msj.getString("reportesUsuarios.usuariosPorTipoPersona")%></th>
																	</tr>
																	<tr >
																		<th class="even"><%=msj.getString("reportesUsuarios.tipoPersona")%></th>
																		<th class="even"><%=msj.getString("reportesUsuarios.cantidadUsuarios")%></th>
																	</tr>
																</thead>
																<tbody>

																	<%
																		for (int i = 0; i < tipopersona.size(); i++) {
																					String[] tp = (String[]) tipopersona.get(i);
																	%>
																	<tr  class="even">
																		<td><%=tp[1]%></td>
																		<td><%=tp[0]%></td>
																	</tr>
																	<%
																		}
																				if (tipopersona.size() < 1) {
																	%>
																	<tr>
																		<td colspan="2" class="odd"><%=msj.getString("consultaUsuarios.noResultados")%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>
															<br />
															<table width="99%" border="1">
																<thead>
																	<tr>
																		<th colspan="2" class="odd"><%=msj.getString("reportesUsuarios.usuariosPorDepto")%></th>
																	</tr>
																	<tr>
																		<th class="even"><%=msj.getString("reportesUsuarios.departamento")%></th>
																		<th class="even"><%=msj.getString("reportesUsuarios.cantidadUsuarios")%></th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (int i = 0; i < departamento.size(); i++) {
																					String[] depto = (String[]) departamento.get(i);
																	%>
																	<tr  class="even">
																		<td><%=depto[1]%></td>
																		<td><%=depto[0]%></td>
																	</tr>
																	<%
																		}
																				if (departamento.size() < 1) {
																	%>
																	<tr>
																	<td colspan="2"><%=msj.getString("consultaUsuarios.noResultados")%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>
														</div>
														<%
															}
															} catch (Exception e) {
																//control vars no inicializadas
															}
														%>
													</div>

												</div>
											</div>

										</div>
												<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
			
									</div>
		


								</div>
									</div>
								</div>
								</div>
					</div>
		

					<%=UI.getFooter(msj) %>									

					
				</div>
						</div>
	</form>
	
	<form action="/MonitoreoBC-WEB/consultarUsuarioServletAdmin" method="post"
		name="formConsulta" id="formConsulta">
		<input type="hidden" name="documento" id="documento"> <input
			type="hidden" name="tipodoc" id="tipodoc"> <input
			type="hidden" name="nombre" id="nombre">
	</form>
	
	<form action="/MonitoreoBC-WEB/consultarEstadisticasUsuarioServletAdmin" method="post"
			name="registro" id="formFechas">
			<input type="hidden" id="fini" name="fini" />
		    <input type="hidden" id="ffin" name="ffin"/>
	</form>
	
	<form action="/MonitoreoBC-WEB/descargaEstadisticasServletAdmin" method="post" id="formDescarga">
			<input type="hidden" id="tipoDescarga" name="tipoDescarga" value=""> 
	</form>
	
	<form id="formActivacion"
		action="/MonitoreoBC-WEB/activacionUsuarioServletAdmin" method="post">
		<input type="hidden" id="idUsuario" name="idUsuario"> <input
			type="hidden" id="tipoOperacion" name="tipoOperacion">
	</form>
	<form id="formCambioRol"
		action="/MonitoreoBC-WEB/modificarRolUsuarioServletAdmin" method="post">
		<input type="hidden" id="idUsuarioCambioRol" name="idUsuario">
		<input type="hidden" id="idRolCambioRol" name="idRol">
	</form>
</body>
</html> 

<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>