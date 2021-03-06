<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.web.reportes.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.reportes.entities.Reportes"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.web.reportes.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.util.Util"%> 
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
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

<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

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
 	
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script>

    
    $(document).ready(function() {
		inicializarNavegador();
	});
    


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
	}	function postEdit() {
		document.getElementById("logName").value = "";
		document.getElementById("logPassword").value = "";
	}
</script>
<script type='text/javascript'>
var reporte;
var reportes = new Array();

function cargarPeriodos() {
	<%
	ArrayList<Reportes> reportes = CargaDatosSelect.getPeriodosBC(); 
	for (int i = 0; i < reportes.size(); i++) {
		Reportes reporte = (Reportes) reportes.get(i);
		String r = reporte.getConsecutivo() + "," + reporte.getTipoReporte() + "," + reporte.getDivision()+"," + reporte.getPeriodoUno();
		if(reporte.getPeriodoDos()>0) { 
			r += "-" + reporte.getPeriodoDos();
		}
	%>
		reporte ='<%=r%>';
		reportes[<%=i%>] = reporte;
	<%}%>
	return reportes;
}

function valoresTipoReporte() {
	var sel = document.getElementById("divterritorioAux");
	if (document.getElementById("treporteAux").value > 4) {
		sel.disabled = false;
		sel.value = -1;
		sel.options[1].style.display = 'none';
		sel.options[2].style.display = 'none';
		document.getElementById('l1').style.display = 'block';
		document.getElementById("periodo1Aux").style.display = 'block';
		document.getElementById("periodoIniAux").style.display = 'none';
		document.getElementById("periodoFinAux").style.display = 'none';
		document.getElementById('l3').style.display = 'none';
		document.getElementById("b1").style.display = 'none';
		document.getElementById("b2").style.display = 'none';
		document.getElementById("b3").style.display = 'none';
		document.getElementById("b4").style.display = 'none';
	} else if (document.getElementById("treporteAux").value > 0 && document.getElementById("treporteAux").value < 5) {
		sel.disabled = false;
		sel.value = -1;
		sel.options[1].style.display = 'block';
		sel.options[2].style.display = 'block';
		document.getElementById('l1').style.display = 'none';
		document.getElementById("periodo1Aux").style.display = 'none';
		document.getElementById("periodoIniAux").style.display = 'block';
		document.getElementById("periodoFinAux").style.display = 'block';
		document.getElementById('l3').style.display = 'block';
		document.getElementById("b1").style.display = 'block';
		document.getElementById("b2").style.display = 'block';
		document.getElementById("b3").style.display = 'block';
		document.getElementById("b4").style.display = 'block';
	} else if (document.getElementById("treporteAux").value < 0) {
		sel.value = -1;
		sel.disabled = true;
		sel.options[1].style.display = 'block';
		sel.options[2].style.display = 'block';
		document.getElementById("periodoIniAux").style.display = 'none';
		document.getElementById("periodoFinAux").style.display = 'none';
		document.getElementById('l1').style.display = 'none';
		document.getElementById('l3').style.display = 'none';
		document.getElementById("periodo1Aux").style.display = 'none';
		document.getElementById("b1").style.display = 'none';
		document.getElementById("b2").style.display = 'none';
		document.getElementById("b3").style.display = 'none';
		document.getElementById("b4").style.display = 'none';
	} else {
		sel.disabled = false;
		sel.options[1].style.display = 'block';
		sel.options[2].style.display = 'block';
		document.getElementById("periodoIniAux").style.display = 'none';
		document.getElementById("periodoFinAux").style.display = 'none';
		document.getElementById('l1').style.display = 'none';
		document.getElementById('l3').style.display = 'none';
		document.getElementById("periodo1Aux").style.display = 'none';
		document.getElementById("b1").style.display = 'none';
		document.getElementById("b2").style.display = 'none';
		document.getElementById("b3").style.display = 'none';
		document.getElementById("b4").style.display = 'none';
	}
	sel.value = -1;
}

function selEnvio() {
	var sel = document.getElementById('periodoFinAux');
	var j = 0;
	for (j = 0; j < sel.options.length; j++) {
		sel.options[j].selected = true;
	}
}

function validarConsultaReporte() {
	selEnvio();
	var passed = true;
	var mensaje = "Los siguientes campos son obligatorios:";
	if (document.getElementById("treporteAux").value < 1) {
		mensaje = mensaje + "- Tipo de Reporte";
		passed = false;
	} else if (document.getElementById("divterritorioAux").value < 1) {
		mensaje = mensaje + "- División territorial";
		passed = false;
	} else if (document.getElementById("periodoFinAux").length == 0
			&& document.getElementById("treporteAux").value > 0
			&& document.getElementById("treporteAux").value < 5) {
		mensaje = "Se necesita minimo un dato de periodo para la busqueda.\n";
		passed = false;
	}
	if (!passed) {
		alert(mensaje);
	}
	return passed;
}


function enviarFormularioReporte()
{
	document.getElementById("treporte").value= "";
	document.getElementById("divterritorio").value= "";
	document.getElementById("periodo1").value= "";
	document.getElementById("periodoFin").value="";
	
	var b=0;
	if(validarConsultaReporte()==true) {
		document.getElementById("treporte").value= document.getElementById("treporteAux").value;
		document.getElementById("divterritorio").value= document.getElementById("divterritorioAux").value;
		document.getElementById("periodo1").value= document.getElementById("periodo1Aux").value;

		for(b=0;b<document.getElementById("periodoFinAux").length;b++) {
			document.getElementById("periodoFin").value=document.getElementById("periodoFin").value+","+ document.getElementById("periodoFinAux")[b].value;
		}

		var valorcito= document.getElementById("periodo1").value;
		
		if((document.getElementById("periodo1").value=="-1" || valorcito=="") && document.getElementById("periodoFinAux").length==0) {
			setTimeout(function(){document.location.href = '/'+"MonitoreoBC-WEB/pub/consultarReporteCarbono.jsp"},500);
			<%--location.href = '/'+"MonitoreoBC-WEB/pub/consultarReporteCarbono.jsp"; --%>
			return;
		}
		
		document.getElementById("formConsultaReporte").submit();
	}
}
</script>

</head>
<body class='sidebarlast front'  
	onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
						
							<div id="content">
								<div class="content-inner">

									<div id="block-consulta-reportes" class="block-gray block">

										<h2>Consulta de reportes</h2>
											<input type="hidden" name="treporteAux" id="treporteAux" value="7" />
											<div class="form-group"
												style="padding-left: 20px; padding-right: 20px">
												<label for="exampleInputEmail1"> Tipo de reporte: </label>
												<div class="select-wrapper" style="max-width: 400px;">
													<select name="treporteAux" id="treporteAux"
														onchange="eliminarlista(periodoFinAux);eliminarlista(periodoIniAux);valoresTipoReporte()">
														<option value="-1">Seleccionar</option>
														<%=CargaDatosSelect.getTipoReporte("carbono")%>
													</select>
												</div>
											</div>
											<div class="form-group"
												style="padding-left: 20px; padding-right: 20px">
												<label for="exampleInputEmail1">Division del territorio</label>
												<div class="select-wrapper" style="max-width: 400px;">
													<select name="divterritorioAux" id="divterritorioAux" disabled="disabled" onchange="eliminarlista(periodoFinAux);eliminarlista(periodoIniAux);completarListaPeriodos(treporteAux,divterritorioAux,periodoIniAux);eliminarlista(periodo1Aux);completarListaPeriodos(treporteAux,divterritorioAux,periodo1Aux)">
														<option value="-1">Seleccionar</option>
														<%=CargaDatosSelect.getDivisionTerritorio()%>
													</select>
												</div>
											</div>
											<div class="form-group item-periodos"
												style="padding-left: 20px; padding-right: 20px">

												<label id="l1" for="exampleInputEmail1"
													style="display: none;">Periodos:</label> 
													<span class="item-periodo-left" style="max-width: 300px"> <select class="select-wrapper" name="periodo1Aux" id="periodo1Aux" size="1" style="width: 200px; display: none;" title="Año inicial para el periodo del reporte">
														<option value="-1">Seleccionar</option>
												</select>
												</span> 
												
												<label id="l3" for="exampleInputEmail1" style="display: none; ">Desde el periódo:</label> 
												<span class="item-periodo-left"> 
												<select class="select-wrapper" name="periodoIniAux" id="periodoIniAux" multiple="multiple" style="display: none; height: 150px; width:260px"> </select>
												</span>
												
												 <span class="pager item-periodo-middle" >
													<ul>
													
													<li class="item-pager-controls item-pager-last" 
													onclick="moverTodosElementos(periodoIniAux, periodoFinAux);"
													id="b1" style="display: none;"><a></a></li>
															
													<li class="item-pager-controls item-pager-next"
														onclick="moverElementoSeleccionado(periodoIniAux, periodoFinAux);"
														id="b2" style="display: none;"><a></a></li>
													
													<li class="item-pager-controls item-pager-previous" 
													onclick="moverElementoSeleccionado(periodoFinAux,periodoIniAux);"
													id="b3" style="display: none;"><a></a></li>
															
													<li class="item-pager-controls item-pager-first" 
													onclick="moverTodosElementos(periodoFinAux, periodoIniAux);"
													id="b4" style="display: none;"><a></a></li>
														
													</ul>
												</span>
												 
												<label id="l3" for="exampleInputEmail1" style="display: none; ">Hasta el periódo:</label> 
												<span class="item-periodo-right"> 
												<select class="select-wrapper" name="periodoFinAux" id="periodoFinAux" multiple="multiple" style="display: none;height: 150px; width:260px"></select>
												</span>
											</div>
											<div class="form-actions">
												<input class="btn btn-default btn-ir" type="button"
													value="<%=msj.getString("consultar")%>" onclick="enviarFormularioReporte()"></input>
													<input type="button"
												value=" <%=msj.getString("recordarClave.Atras")%> "
												class="btn btn-default"
												onclick="javascript:history.back(1);">
											</div>

									</div>

								</div>

								<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									

							</div>

						</div>
					</div>
				</div>

			</div>

					<%=UI.getFooter(msj) %>									

		</div>
	</form>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										
	
	<form action="/MonitoreoBC-WEB/consultarReporteServlet" method="post" name="formConsultaReporte" id="formConsultaReporte">
			<input type="hidden" name="treporte" id="treporte" value="">
			<input type="hidden" name="divterritorio" id="divterritorio" value="">
			<input type="hidden" name="periodo1" id="periodo1" value="">
			<input type="hidden" name="periodoFin" id="periodoFin" value="">
	</form>
	
</body>

</html>

