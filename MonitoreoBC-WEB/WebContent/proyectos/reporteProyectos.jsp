<%@ page language="java" import="java.util.*" import="java.math.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.web.reportes.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<%@page import="co.gov.ideamredd.reportes.dao.LecturaArchivo"%> 
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.util.Util"%> 
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.reportes.entities.InformacionReporteBiomasa"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.proyecto.entities.ConteoProyecto"%>
<%@page import="co.gov.ideamredd.proyecto.dao.ConsultaProyecto"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.Auxiliar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta charset="utf-8" />  
<%
HttpSession sesion = request.getSession(false); 

String modoPDF = Auxiliar.nzObjStr(request.getParameter("modoPDF"), "0");

if (modoPDF.equals("0")) {
	if (!request.isRequestedSessionIdValid() || sesion == null) { 
		response.sendRedirect("/MonitoreoBC-WEB"); 
		return;
	}
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
if (request.getUserPrincipal() !=null) {
	usuario = UtilWeb.consultarUsuarioPorLogin(request.getUserPrincipal().getName());
	usuario.setRolNombre(UtilWeb.consultarRolesUsuarioPorLogin(request.getUserPrincipal().getName()));
}

Map<Integer,String> diccionarioPermisos = null;
if (usuario !=null) {
	diccionarioPermisos = ControlPermisos.consultaPermisos(usuario.getRolId());
}

String id_usuario = "";
if (usuario != null) {
	id_usuario = String.valueOf(usuario.getIdUsuario());
}
String nombreLicencia = "REPORTES";

%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<script src="../js/jquery-1.10.2.js"></script>

<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script>

google.load("visualization", "1", {packages : [ "corechart" ]});

function conteoProyectos(conteo, dimension1, dimension2) {
	this.conteo = conteo;
	this.dimension1 = dimension1;
	this.dimension2 = dimension2;
}

var a_conteoProyectos_TOTAL = new Array();
var a_conteoProyectos_ESTADO = new Array();
var a_conteoProyectos_ACTIVIDAD = new Array();
var a_conteoProyectos_TENENCIA = new Array();

$(document).ready(function() {

	// OBTENER LOS DATOS PARA EL TIPO DE INFORME
	
	var div_tabla_TOTAL = document.getElementById('div_tabla_TOTAL');
	var div_tabla_ESTADO = document.getElementById('div_tabla_ESTADO');
	var div_tabla_TENENCIA = document.getElementById('div_tabla_TENENCIA');
	var div_tabla_ACTIVIDAD = document.getElementById('div_tabla_ACTIVIDAD');
	
	var div_grafico_TOTAL = document.getElementById('div_grafico_TOTAL');
	var div_grafico_ESTADO = document.getElementById('div_grafico_ESTADO');
	var div_grafico_TENENCIA = document.getElementById('div_grafico_TENENCIA');
	var div_grafico_ACTIVIDAD = document.getElementById('div_grafico_ACTIVIDAD');
	
	<%

	String titulo = msj.getString("reportes.proyectos.titulo");

	out.println("document.getElementById('div_titulo').innerHTML = '<p class=\"p_titulo\">"+titulo+"</p>'");

	String tipoconsolidado = msj.getString("reportes.proyectos.tipoconsolidado");
	out.println("document.getElementById('div_subtitulo').innerHTML = '<p class=\"p_subtitulo\">"+tipoconsolidado+":</p>'");

	String tipo = Auxiliar.nzObjStr(request.getParameter("tipo"), "");
	String str_tipo = msj.getString("reportes.proyectos."+tipo);  
	out.println("document.getElementById('div_tipo').innerHTML = '<p class=\"p_tipo\">"+str_tipo+"</p>'");
			
	String desde = Auxiliar.nzObjStr(request.getParameter("desde"), "");
	String str_desde = "";
	if (Auxiliar.tieneAlgo(desde)) {
		str_desde = msj.getString("reportes.proyectos.desde") + ": " + desde;		
		out.println("document.getElementById('div_desde').innerHTML = '<p class=\"p_desde\">"+str_desde+"</p>'");
	}
	String hasta = Auxiliar.nzObjStr(request.getParameter("hasta"), "");
	String str_hasta = "";
	if (Auxiliar.tieneAlgo(desde)) {
		str_hasta = msj.getString("reportes.proyectos.hasta") + ": " + hasta;		
		out.println("document.getElementById('div_hasta').innerHTML = '<p class=\"p_hasta\">"+str_hasta+"</p>'");
	}

	String str_conteo = msj.getString("conteo");
	String NAL = msj.getString("reportes.proyectos.NAL");
	String DEP = msj.getString("reportes.proyectos.DEP");
	String MUN = msj.getString("reportes.proyectos.MUN");
	String CAR = msj.getString("reportes.proyectos.CAR");
	String BOS = msj.getString("reportes.proyectos.BOS");

	String[] agregaciones = {"TOTAL", "ESTADO", "TENENCIA", "ACTIVIDAD"};
	String agregacion = "";
	
	for (int a=0; a<agregaciones.length; a++) {
		agregacion = agregaciones[a];
		
		ArrayList<ConteoProyecto> conteoProyectos = ConsultaProyecto.contarProyectos(tipo, agregacion, desde, hasta);		
		out.println("var a_tabla = new Array();");
		
		ConteoProyecto conteoProyecto = new ConteoProyecto();
		int conteo = 0;
		String dimension1 = "";
		String dimension2 = "";
		
		String tabla = "<div>";
		tabla += "<h3>"+msj.getString("reportes.proyectos.titulo");
		tabla += " "+msj.getString("reportes.proyectos.a_nivel");
		tabla += " "+msj.getString("reportes.proyectos."+tipo);
		tabla += " "+msj.getString("reportes.proyectos.nivel");
		tabla += agregacion.equals("TOTAL") ? " "+msj.getString("reportes.proyectos.en") : " "+msj.getString("reportes.proyectos.por");
		tabla += " "+msj.getString("reportes.proyectos."+agregacion)+":</h3>";
		tabla += "<table>";
		tabla += "<tr class=\"trh\">";
		tabla += "<th>"+str_conteo+"</th>";
		String titulo_serie = "";
		out.println("a_tabla.push([");
		if (!tipo.equals("NAL")) {
			tabla += "<th>"+msj.getString("reportes.proyectos."+tipo)+"</th>";
			titulo_serie = msj.getString("reportes.proyectos."+tipo);
		}
		if (!agregacion.equals("TOTAL")) {
			tabla += "<th>"+msj.getString("reportes.proyectos."+agregacion)+"</th>";
			titulo_serie += msj.getString("reportes.proyectos."+agregacion);
		}
		out.print("'"+titulo_serie+"',");
		out.print("'"+msj.getString("conteo")+"'");
		tabla += "</tr>";
		out.println("]);");

		String serie = "";
		
		int n_proyectos = conteoProyectos.size();
		
		for (int i=0; i<n_proyectos; i++) {
			conteoProyecto = conteoProyectos.get(i);	
			conteo = conteoProyecto.getConteo();
			tabla += "<tr>";
			tabla += "<td class=\"dato-numerico\">"+ conteo + "</td>";
			out.println("a_tabla.push([");
			serie = "";
			if (!tipo.equals("NAL")) {
				dimension1 = Auxiliar.nz(conteoProyecto.getDimension1(), "");
				tabla += "<td class=\"dato-numerico\">"+dimension1+"</td>";
				serie = dimension1;
			}
			if (!agregacion.equals("TOTAL")) {
				dimension2 = Auxiliar.nz(conteoProyecto.getDimension2(), "");
				tabla += "<td class=\"dato-numerico\">"+dimension2+"</td>";
				serie += " - " + dimension2;
			}
			out.print("'"+serie+"',");
			out.println(conteo);
			tabla += "</tr>";
			out.print("]);");
			
			if (!(tipo.equals("NAL") && agregacion.equals("TOTAL"))) {
				out.println("graficarConteoProyectos(a_tabla, '"+msj.getString("reportes.proyectos.titulo")+"', '"+msj.getString("conteo")+"', 'div_grafico_"+agregacion+"')");
			}
		}
		tabla += "</table>";	
		tabla += "</div>";
		out.println("div_tabla_"+agregacion+".innerHTML = '"+tabla+ "'");
		
	}

	%>
	
});

var gWidth = "100%";
var gHeight = "550";
var gBarChartWidth = "100%";
var gBarChartHeight = "80%";

function graficarConteoProyectos(a_tabla, titulo_grafico, titulo_hAxis, div_grafico) {
	var data = null;
	var options = null;
	var chart = null;

	var n_registros = a_tabla.length;
	console.log("n_registros = " + n_registros);
	var alto_titulo = 20;
	var alto_subtitulo = 10;
	var alto_barra = 60;
	var alto_total = alto_titulo + alto_subtitulo + (n_registros * alto_barra) ;
	console.log("alto_total = " + alto_total);
	
	data = google.visualization.arrayToDataTable(a_tabla);

	options = {
			title : titulo_grafico,
			width : gWidth,
			height : alto_total,
            chartArea: {  width: gBarChartWidth, height: gBarChartHeight },
			backgroundColor: 'white',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				textPosition: "in",
				title : '',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : titulo_hAxis,
				titleTextStyle : {
					color : 'black'
				},
		        direction: 1, 
			},
			seriesType: 'bars'
	};

	var n_fields = a_tabla[0].length; 
	
	chart = new google.visualization.BarChart(document.getElementById(div_grafico));
	chart.draw(data, options);
	
}


</script>
</head>

<body class='sidebarlast front body_iframe'">

<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String fechaConFormato = sdf.format(new Date());

String banner = "<div class='bannerpdf'>";
banner += "<a href='/MonitoreoBC-WEB/reg/indexLogOn.jsp'><img class='logopdf' src='../img/logo.png' alt=''></a>";
banner += "<a href='http://www.minambiente.gov.co/web/index.html'><img class='logopdf' src='../img/img-min.png' alt=''></a>"; 
banner += "<a href='http://wsp.presidencia.gov.co/portal/Paginas/default.aspx'><img class='logopdf' src='../img/img-prosperidad.png' alt=''></a>"; 
banner += "<a href='http://www.moore.org/'><img class='logopdf' src='../img/img-moore.png' alt=''></a>"; 
banner += "<a href='http://www.patrimonionatural.org.co/'><img class='logopdf' src='../img/img-patrimonio.png' alt=''></a>";
banner += "</div>";
banner += "<div class='fechareporte'>"+fechaConFormato+"</div>";
%>

<% 
if (modoPDF.equals("1")) { 
	out.print(banner);		
} 
%>

	<div class="div_tablas">
		<div id="div_titulo"></div>
		<div id="div_subtitulo"></div>
		<div id="div_tipo"></div>
		<div id="div_desde"></div>
		<div id="div_hasta"></div>
		
	
		<div id="div_tabla_TOTAL" class="div_tabla"></div>
		<div id="div_grafico_TOTAL"></div>
	</div>
	<div style="page-break-before:always;"></div>
<% 
if (modoPDF.equals("1")) { 
	out.print(banner);		
} 
%>
	<div id="div_tabla_ESTADO" class="div_tabla"></div>
	<div id="div_grafico_ESTADO"></div>

	<div style="page-break-before:always;"></div>
<% 
if (modoPDF.equals("1")) { 
	out.print(banner);		
} 
%>
	<div id="div_tabla_TENENCIA" class="div_tabla"></div>
	<div id="div_grafico_TENENCIA"></div>

	<div style="page-break-before:always;"></div>
<% 
if (modoPDF.equals("1")) { 
	out.print(banner);		
} 
%>
	<div id="div_tabla_ACTIVIDAD" class="div_tabla"></div>
	<div id="div_grafico_ACTIVIDAD"></div>

</body>

</html>

