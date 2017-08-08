<%@page import="com.sun.org.apache.xml.internal.security.encryption.AgreementMethod"%>
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
<%@page import="co.gov.ideamredd.web.usuario.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.Auxiliar"%>
<%@page import="co.gov.ideamredd.bosqueencifras.entities.*"%>
<%@page import="co.gov.ideamredd.bosqueencifras.dao.BosqueEnCifras"%>
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

<%
	out.print(doctype);
%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<%
	if (es_movil) {
%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<%
	}
%>
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

String idioma = i18n.getLenguaje(); 

Map<Integer,String> diccionarioPermisos = null;
if (usuario !=null) {
	diccionarioPermisos = ControlPermisos.consultaPermisos(usuario.getRolId());
}

String id_usuario = "0";
if (usuario != null) {
	id_usuario = String.valueOf(usuario.getIdUsuario());
}

String nombreLicencia = "REPORTES";


String id_reporte = "";

String titulo_grafico_proporcionbosque = "Proporcion de Bosque Natural por Período";
String titulo_grafico_tasadeforestacion = "Tasa de Deforestación entre Períodos";
String titulo_hAxis_proporcionbosque = "%";

ArrayList<ProporcionBosque> proporcionesBosque = BosqueEnCifras.consultarProporcionesBosque();

ProporcionBosque proporcionBosque = new ProporcionBosque();

String periodo = ""; 
String areaBosqueStr = ""; 
String areaBosque = ""; 
String areaTotalStr = "";
String areaTotal = "";
String porcentajeStr = "";
String porcentaje = "";

String html_tabla_proporcionbosque = "";
String html_tablasgraficos_proporcionbosque = "";

String label_periodo = msj.getString("periodo"); 
String label_areabosque = msj.getString("areabosque");
String label_areatotal = msj.getString("areatotal");
String label_porcentaje = msj.getString("porcentaje");

ArrayList<String> options_graficas = new ArrayList<String>();
ArrayList<String> divs_graficas = new ArrayList<String>();
String js_grafica_proporcionbosque = "";
String js_graficar_proporcionbosque = "";

int j = 0;

if (proporcionesBosque != null) {
	if (proporcionesBosque.size() > 0) {

		html_tablasgraficos_proporcionbosque += "\n<div style='page-break-before:always;'></div>";
		html_tablasgraficos_proporcionbosque += "\n<div class='div_tablagraficos'>";
		html_tablasgraficos_proporcionbosque += "\n<div class='titulo_naranja'><h3>Proporción de Bosque Natural por Período</h3></div>";

		html_tabla_proporcionbosque += "\n<table>";
		html_tablasgraficos_proporcionbosque += "\n<table>";
		
		html_tabla_proporcionbosque += "\n<tr class='titulo_datos'>";
		html_tablasgraficos_proporcionbosque += "\n<tr>";

		html_tabla_proporcionbosque += "<th>"+label_periodo+"</th>";
		html_tablasgraficos_proporcionbosque += "<th>"+label_periodo+"</th>";
		html_tabla_proporcionbosque += "<th>"+label_areabosque+"</th>";
		html_tablasgraficos_proporcionbosque += "<th>"+label_areabosque+"</th>";
		html_tabla_proporcionbosque += "<th>"+label_areatotal+"</th>";
		html_tablasgraficos_proporcionbosque += "<th>"+label_areatotal+"</th>";
		html_tabla_proporcionbosque += "<th>"+label_porcentaje+"</th>";
		html_tablasgraficos_proporcionbosque += "<th>"+label_porcentaje+"</th>";

		html_tabla_proporcionbosque += "</tr>";
		html_tablasgraficos_proporcionbosque += "</tr>";

		js_grafica_proporcionbosque += "\nvar data_proporcionbosque = new google.visualization.DataTable();";
		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn('string','"+label_periodo+"');";
// 		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn('number','"+label_areabosque+"');";
// 		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn('number','"+label_areatotal+"');";
		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn('number','"+label_porcentaje+"');";
		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn('number','"+label_porcentaje+"');";
		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn({type: 'string', role: 'annotation'});";
// 		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addColumn({type: 'string', role: 'style'})";

		js_grafica_proporcionbosque += "\nvar a_tabla_proporcionbosque = new Array();";

		for (int r=0; r<proporcionesBosque.size(); r++) {
			proporcionBosque = proporcionesBosque.get(r);
		
			periodo = proporcionBosque.getPeriodo();
			areaBosqueStr = String.format("%,f", proporcionBosque.getAreaBosque()).replace(".000000", "");
			areaBosque = String.format("%.0f", proporcionBosque.getAreaBosque());
			areaTotalStr = String.format("%,f", proporcionBosque.getAreaTotal()).replace(".000000", "");
			areaTotal = String.format("%.0f", proporcionBosque.getAreaTotal());
			porcentajeStr = String.format("%.2f", proporcionBosque.getPorcentaje())+"%";
			porcentaje = String.format("%.2f", proporcionBosque.getPorcentaje());
		
			js_grafica_proporcionbosque += "\na_tabla_proporcionbosque.push(['"+periodo+"'";
		
			html_tabla_proporcionbosque += "<td><a href='/MonitoreoBC-WEB/pub/reporteGeoproceso.jsp?id_reporte="+proporcionBosque.getId_reporte()+"'>"+periodo+"</a></td>";
			html_tablasgraficos_proporcionbosque += "<td>"+periodo+"</td>";
			html_tabla_proporcionbosque += "<td class='numero_bajita'>"+areaBosqueStr+"</td>";
			html_tablasgraficos_proporcionbosque += "<td class='numero_bajita'>"+areaBosqueStr+"</td>";
// 			js_grafica_proporcionbosque += ","+areaBosque;
			html_tabla_proporcionbosque += "<td class='numero_bajita'>"+areaTotalStr+"</td>";
			html_tablasgraficos_proporcionbosque += "<td class='numero_bajita'>"+areaTotalStr+"</td>";
// 			js_grafica_proporcionbosque += ","+areaTotal;
			html_tabla_proporcionbosque += "<td class='numero_bajita'>"+porcentajeStr+"</td>";
			html_tablasgraficos_proporcionbosque += "<td class='numero_bajita'>"+porcentajeStr+"</td>";
			js_grafica_proporcionbosque += ","+porcentaje;
			js_grafica_proporcionbosque += ","+porcentaje;
			js_grafica_proporcionbosque += ",'"+porcentajeStr+"'";
// 			js_grafica_proporcionbosque += ",'color: #3D8944'";

			html_tabla_proporcionbosque += "</tr>";
			html_tablasgraficos_proporcionbosque += "</tr>";
			js_grafica_proporcionbosque += "]);";
		}
		
		html_tabla_proporcionbosque += "\n</table>";
		html_tabla_proporcionbosque += "\n<p>La metodología empleada para la medición de la superficioe cubierta por bosque natural utiliza imágenes en formato raster, sin embargo, la superficie total del país hallada al agregar estas imágenes es diferente a la superficie continental e insular oficial del país (114.174.800 ha) que fue determinada mediante un formato vector.  La proporción que se registra en el cuadro de datos toma como referencia la superficie oficial continental del país, sin incluir la insular (San Andrés y Providencia).</p>";
		html_tablasgraficos_proporcionbosque += "\n</table>";
		html_tablasgraficos_proporcionbosque += "\n<div id='div_grafica_proporcionbosque' class='grafico_pdf'></div>";
		js_grafica_proporcionbosque += "\ndata_proporcionbosque.addRows(a_tabla_proporcionbosque);";
		js_graficar_proporcionbosque += "\ngraficarReporteProporcionBosque('div_grafica_proporcionbosque');";
	}
}


ArrayList<TasaDeforestacion> tasasDeforestacion = BosqueEnCifras.consultarTasasDeforestacion();

TasaDeforestacion tasaDeforestacion = new TasaDeforestacion();

String areaBosqueInicialStr = ""; 
String areaBosqueInicial = ""; 
String areaDeforestadaStr = "";
String areaDeforestada = "";
String areaBosqueFinalStr = "";
String areaBosqueFinal = "";
String tasaStr = "";
String tasa = "";

String html_tabla_tasadeforestacion = "";
String html_tablasgraficos_tasadeforestacion = "";

String label_areabosqueinicial = msj.getString("areabosqueinicial");
String label_areabosquefinal = msj.getString("areabosquefinal");
String label_areadeforestada = msj.getString("areadeforestada");
String label_tasadeforestacion = msj.getString("tasaDeforestacion");

String js_grafica_tasadeforestacion = "";
String js_graficar_tasadeforestacion = "";

if (tasasDeforestacion != null) {
	if (tasasDeforestacion.size() > 0) {

		html_tablasgraficos_tasadeforestacion += "\n<div style='page-break-before:always;'></div>";
		html_tablasgraficos_tasadeforestacion += "\n<div class='div_tablagraficos'>";
		html_tablasgraficos_tasadeforestacion += "\n<div class='titulo_naranja'><h3>Tasa de Deforestación de Bosque Natural Entre Períodos</h3></div>";

		html_tabla_tasadeforestacion += "\n<table>";
		html_tablasgraficos_tasadeforestacion += "\n<table>";
		
		html_tabla_tasadeforestacion += "\n<tr class='titulo_datos'>";
		html_tablasgraficos_tasadeforestacion += "\n<tr>";

		html_tabla_tasadeforestacion += "<th>"+label_periodo+"</th>";
		html_tablasgraficos_tasadeforestacion += "<th>"+label_periodo+"</th>";
		html_tabla_tasadeforestacion += "<th>"+label_areabosqueinicial+"</th>";
		html_tablasgraficos_tasadeforestacion += "<th>"+label_areabosqueinicial+"</th>";
		html_tabla_tasadeforestacion += "<th>"+label_areadeforestada+"</th>";
		html_tablasgraficos_tasadeforestacion += "<th>"+label_areadeforestada+"</th>";
		html_tabla_tasadeforestacion += "<th>"+label_areabosquefinal+"</th>";
		html_tablasgraficos_tasadeforestacion += "<th>"+label_areabosquefinal+"</th>";
		html_tabla_tasadeforestacion += "<th>"+label_tasadeforestacion+"</th>";
		html_tablasgraficos_tasadeforestacion += "<th>"+label_tasadeforestacion+"</th>";

		html_tabla_tasadeforestacion += "</tr>";
		html_tablasgraficos_tasadeforestacion += "</tr>";

		js_grafica_tasadeforestacion += "\nvar data_tasadeforestacion = new google.visualization.DataTable();";
		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn('string','"+label_periodo+"');";
// 		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn('number','"+label_areabosqueinicial+"');";
// 		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn('number','"+label_areadeforestada+"');";
// 		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn('number','"+label_areabosquefinal+"');";
		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn('number','"+label_tasadeforestacion+"');";
		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn('number','"+label_tasadeforestacion+"');";
		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn({type: 'string', role: 'annotation'});";
// 		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addColumn({type: 'string', role: 'style'})";

		js_grafica_tasadeforestacion += "\nvar a_tabla_tasadeforestacion = new Array();";

		for (int r=0; r<tasasDeforestacion.size(); r++) {
			tasaDeforestacion = tasasDeforestacion.get(r);
		
			periodo = tasaDeforestacion.getPeriodo();
			areaBosqueInicialStr = String.format("%,f", tasaDeforestacion.getAreaBosqueInicial()).replace(".000000", "");
			areaBosqueInicial = String.format("%.0f", tasaDeforestacion.getAreaBosqueInicial());
			areaDeforestadaStr = String.format("%,f", tasaDeforestacion.getAreaDeforestada()).replace(".000000", "");
			areaDeforestada = String.format("%.0f", tasaDeforestacion.getAreaDeforestada());
			areaBosqueFinalStr = String.format("%,f", tasaDeforestacion.getAreaBosqueFinal()).replace(".000000", "");
			areaBosqueFinal = String.format("%.0f", tasaDeforestacion.getAreaBosqueFinal());
			tasaStr = String.format("%.2f", tasaDeforestacion.getTasaDeforestacion());
			tasa = String.format("%.2f", tasaDeforestacion.getTasaDeforestacion());
		
			js_grafica_tasadeforestacion += "\na_tabla_tasadeforestacion.push(['"+periodo+"'";
		
			html_tabla_tasadeforestacion += "<td><a href='/MonitoreoBC-WEB/pub/reporteGeoproceso.jsp?id_reporte="+tasaDeforestacion.getId_reporte()+"'>"+periodo+"</a></td>";
			html_tablasgraficos_tasadeforestacion += "<td>"+periodo+"</td>";
			html_tabla_tasadeforestacion += "<td class='numero_bajita'>"+areaBosqueInicialStr+"</td>";
			html_tablasgraficos_tasadeforestacion += "<td class='numero_bajita'>"+areaBosqueInicialStr+"</td>";
// 			js_grafica_tasadeforestacion += ","+areaBosqueInicial;
			html_tabla_tasadeforestacion += "<td class='numero_bajita'>"+areaDeforestadaStr+"</td>";
			html_tablasgraficos_tasadeforestacion += "<td class='numero_bajita'>"+areaDeforestadaStr+"</td>";
// 			js_grafica_tasadeforestacion += ","+areaDeforestada;
			html_tabla_tasadeforestacion += "<td class='numero_bajita'>"+areaBosqueFinalStr+"</td>";
			html_tablasgraficos_tasadeforestacion += "<td class='numero_bajita'>"+areaBosqueFinalStr+"</td>";
// 			js_grafica_tasadeforestacion += ","+areaBosqueFinal;
			html_tabla_tasadeforestacion += "<td class='numero_bajita'>"+tasaStr+"</td>";
			html_tablasgraficos_tasadeforestacion += "<td class='numero_bajita'>"+tasa+"</td>";
			js_grafica_tasadeforestacion += ","+tasa;
			js_grafica_tasadeforestacion += ","+tasa;
			js_grafica_tasadeforestacion += ",'"+tasaStr+"'";
// 			js_grafica_tasadeforestacion += ",'color: #3D8944'";

			html_tabla_tasadeforestacion += "</tr>";
			html_tablasgraficos_tasadeforestacion += "</tr>";
			js_grafica_tasadeforestacion += "]);";
		}
		
		html_tabla_tasadeforestacion += "\n</table>";
		html_tablasgraficos_tasadeforestacion += "\n</table>";
		html_tablasgraficos_tasadeforestacion += "\n<div id='div_grafica_tasadeforestacion' class='grafico_pdf'></div>";
		js_grafica_tasadeforestacion += "\ndata_tasadeforestacion.addRows(a_tabla_tasadeforestacion);";
		js_graficar_tasadeforestacion += "\ngraficarReporteTasaDeforestacion('div_grafica_tasadeforestacion');";

	}
}
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
<script type="text/javascript" src="../js/leaflet.js"></script>

<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
google.load('visualization', '1.1', {packages: ['corechart', 'controls']});
</script>

<script src="../js/jquery-1.10.2.js"></script>
<script src="../js/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

<script>

google.load("visualization", "1", {packages:["corechart"]});

var titulo_grafico_proporcionbosque = '<%=titulo_grafico_proporcionbosque%>';
var titulo_grafico_tasadeforestacion = '<%=titulo_grafico_tasadeforestacion%>';
var titulo_hAxis_proporcionbosque = '<%=titulo_hAxis_proporcionbosque%>';
<%
out.print(js_grafica_proporcionbosque);
out.print(js_grafica_tasadeforestacion);
%>

var gWidth = "100%";
var gHeight = "550";
var gBarChartWidth = "90%";
var gBarChartHeight = "50%";

function graficarReporteProporcionBosque(divid) {
	document.getElementById(divid).style.display = 'block';

	var options = null;
	var chart = null;

	var n_registros = a_tabla_proporcionbosque.length;
	console.log("n_registros = " + n_registros);
	var alto_titulo = 40;
	var alto_subtitulo = 20;
	var alto_leyenda = 50;
	var alto_barra = 60;
	var alto_total = alto_titulo + alto_leyenda + alto_subtitulo + (n_registros * alto_barra);
	console.log("alto_total = " + alto_total);

	options = {
		title : titulo_grafico_proporcionbosque,
		width : gWidth,
		height : alto_total,
		chartArea : {
			width : gBarChartWidth,
			height : gBarChartHeight
		},
		backgroundColor : 'FFFFFF',
		legend : {
			position : 'top',
			maxLines : 4
		},
		vAxis : {
			textPosition : "in",
			title : 'Total',
			titleTextStyle : {
				color : 'black'
			}
		},
		hAxis : {
			title : '',
			titleTextStyle : {
				color : 'black'
			},
			direction : 1,
		},
 		seriesType : 'bars',
 		series: {0: {type: 'bar', color: '#3D8944'}, 1: {type: 'line', color: 'red'}},
 		isStacked : true
	};

	chart = new google.visualization.ComboChart(document.getElementById(divid));
	chart.draw(data_proporcionbosque, options);

}

function graficarReporteTasaDeforestacion(divid) {
	document.getElementById(divid).style.display = 'block';

	var options = null;
	var chart = null;

	var n_registros = a_tabla_tasadeforestacion.length;
	console.log("n_registros = " + n_registros);
	var alto_titulo = 40;
	var alto_subtitulo = 20;
	var alto_leyenda = 50;
	var alto_barra = 60;
	var alto_total = alto_titulo + alto_leyenda + alto_subtitulo + (n_registros * alto_barra);
	console.log("alto_total = " + alto_total);

	options = {
		title : titulo_grafico_tasadeforestacion,
		width : gWidth,
		height : alto_total,
		chartArea : {
			width : gBarChartWidth,
			height : gBarChartHeight
		},
		backgroundColor : 'FFFFFF',
		legend : {
			position : 'top',
			maxLines : 4
		},
		vAxis : {
			textPosition : "in",
			title : 'Total',
			titleTextStyle : {
				color : 'black'
			}
		},
		hAxis : {
			title : '',
			titleTextStyle : {
				color : 'black'
			},
			direction : 1,
		},
 		seriesType : 'bars',
 		series: {0: {type: 'bar', color: '#3D8944'}, 1: {type: 'line', color: 'red'}},
// 		isStacked : true
	};

	chart = new google.visualization.ComboChart(document.getElementById(divid));
	chart.draw(data_tasadeforestacion, options);
}

$(function() {
	var tabs = $("#tabs").tabs();
	tabs.find(".ui-tabs-nav").sortable({
		axis : "x",
		stop : function() {
			tabs.tabs("refresh");
		}
	});
});

$(document).ready(function() {
	inicializarNavegador();
	<%
	out.println(js_graficar_proporcionbosque);
	out.println(js_graficar_tasadeforestacion);
	%>
});

function descargarReporte(tipoArchivo) {
	document.getElementById("tipoArchivo").value = tipoArchivo;
	document.getElementById("formDescargaDoc").submit();
}
</script>

</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI() + "?id_reporte=" + id_reporte)%>

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">

							<h2 class="titulo_naranja"><%=msj.getString("El_Bosque_en_Cifras")%></h2>

							<div id="block-resultados-reporte" class="block-gray  block">
								<div class="content bosqueencifras">
									<h2 class="titulo_naranja"><%=msj.getString("titulo_proporcionbosque") %></h2>
									<div>
										<div id="div_grafica_proporcionbosque" class="graficas block-dark-gray"></div>
										<div class="div_tablas"><%=html_tabla_proporcionbosque%></div>
										<div class="div_explicacion_proporcion_bosque">
											<p>La metodología empleada para la medición de la superficioe cubierta por bosque natural utiliza imágenes en formato raster, sin embargo, la superficie total del país hallada al agregar estas imágenes es diferente a la superficie continental e insular oficial del país (114.174.800 ha) que fue determinada mediante un formato vector.  La proporción que se registra en el cuadro de datos toma como referencia la superficie oficial continental del país, sin incluir la insular (San Andrés y Providencia).</p>;
										</div>
									</div>

									<div class="puntoaparte"></div>									
									<h2 class="titulo_naranja"><%=msj.getString("titulo_tasadeforestacion") %></h2>
									<div>
										<div id="div_grafica_tasadeforestacion" class="graficas block-dark-gray"></div>
										<div class="div_tablas"><%=html_tabla_tasadeforestacion%></div>
										<div class="div_explicacion_tasa_deforestacion"><img class="img_explicacion_tasa_deforestacion" src="/MonitoreoBC-WEB/img/explicacion-tasa-deforestacion.png" /></div>
									</div>

									<div class="puntoaparte"></div>
									<h2 class="titulo_naranja"><%=msj.getString("titulo_visorbosqueencifras") %></h2>
									<div>
										<iframe id="iframe_mapa_wms" class="mapa_wms" src="/MonitoreoBC-WEB/visorBosqueEnCifras.jsp"></iframe>
									</div>
								</div>
							</div>

							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n)%>
						</div>
					</div>
				</div>

			</div>

			<%=UI.getFooter(msj)%>

		</div>
	</form>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form action="/MonitoreoBC-WEB/descargaDocumentosReportesServlet" method="post" id="formDescargaDoc" target="_blank">
		<input type="hidden" value="<%=id_reporte%>" name="id_reporte" id="id_reporte"></input> <input type="hidden" name="tipoArchivo" id="tipoArchivo"></input> <input type="hidden" name="cx" id="cx" value="-73"></input> <input type="hidden" name="cy" id="cy" value="5"></input> <input type="hidden" name="zoom" id="zoom" value="10"></input> <input type="hidden" name="idiomia" id="idioma" value="<%=idioma%>"></input> <input type="hidden" name="identImagen" id="identImagen"
			value="<%=request.getSession().getAttribute("identImagen")%>"></input>
	</form>

</body>
</html>

