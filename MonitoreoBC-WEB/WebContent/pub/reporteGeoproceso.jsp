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
<%@page import="co.gov.ideamredd.reportes.entities.AgregadoReporte"%>
<%@page import="co.gov.ideamredd.reportes.dao.ConsultaReportes"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.Auxiliar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
String ua=request.getHeader("User-Agent").toLowerCase();
boolean es_movil = ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-");
String doctype = "";
String estilo = "estilos.css";

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
String id_reporte_parameter = "";

id_reporte_parameter = Auxiliar.nzObjStr(request.getParameter("id_reporte"), "");
id_reporte = id_reporte_parameter;
if (sesion != null) {
	if (Auxiliar.tieneAlgo(id_reporte_parameter)) {
		sesion.setAttribute("id_reporte", id_reporte_parameter);
		id_reporte = id_reporte_parameter;
	}
	else {
		id_reporte = Auxiliar.nzObjStr((String) sesion.getAttribute("id_reporte"), ""); 
	}
}

String idTipoReporte = ConsultaReportes.idTipoReporte(id_reporte);
String nombreTipoReporte = ConsultaReportes.nombreTipoReporte(idTipoReporte, i18n.getLenguaje());
String idDivisionTerritorial = ConsultaReportes.idDivisionTerritorial(id_reporte);
String nombreDivisionTerritorial = ConsultaReportes.nombreDivisionTerritorial(idDivisionTerritorial, i18n.getLenguaje());
String identImagen = ConsultaReportes.identImagen(id_reporte); 
String periodoS = ConsultaReportes.periodos(id_reporte);

ArrayList<AgregadoReporte> agregadosReporte = ConsultaReportes.consolidarReporte(id_reporte);

AgregadoReporte agregadoReporte = new AgregadoReporte();

String clasificacion = ""; 
String division_territorial = ""; 
String areastr = ""; 
String area = ""; 
String incertidumbre = ""; 
String error = "";
String baj = "";
String batstr = "";
String bat = "";
String caj = "";
String catstr = "";
String cat = "";
String coe = "";
String coetstr = "";
String coet = "";


String html_tablas = "";
String html_tablasgraficos = "";

String division_territorial_anterior = "";

String label_areaha = msj.getString("areaha");
String label_incertidumbre = msj.getString("incertidumbre"); 
String label_error = msj.getString("error_estandar"); 
String label_baj = msj.getString("baj");
String label_bat = msj.getString("bat");
String label_caj = msj.getString("caj");
String label_cat = msj.getString("cat");
String label_coe = msj.getString("coe");
String label_coet = msj.getString("coet");

ArrayList<String> options_graficas = new ArrayList<String>();
ArrayList<String> divs_graficas = new ArrayList<String>();
String js_graficas = "\nvar a_data = new Array();";
String js_graficar = "";

ArrayList<String> divisiones_territoriales = new ArrayList<String>();
ArrayList<String> clasificaciones = new ArrayList<String>();
ArrayList<String> colores = new ArrayList<String>();
colores.add("green");
colores.add("red");
colores.add("blue");
colores.add("yellow");
colores.add("magenta");
colores.add("cyan");
colores.add("brown");
colores.add("orange");
colores.add("olive");
colores.add("teal");
colores.add("navy");
int c = 0;
String color = "green";

int j = 0;

if (agregadosReporte != null) {
	if (agregadosReporte.size() > 0) {
		for (int r=0; r<agregadosReporte.size(); r++) {
			agregadoReporte = agregadosReporte.get(r);

			division_territorial = agregadoReporte.getDivisionTerritorial();
			
			if (division_territorial.indexOf("consolidado") >= 0) {
				if (idioma.equals("es")) {
					division_territorial = "Consolidado Nacional";
				}
				else {
					division_territorial = "Consolidated National";
				}
			}

			if (!division_territorial.equals(division_territorial_anterior)) {
				if (r>0) {
					html_tablas += "\n</table>";
					html_tablasgraficos += "\n</table></div>";
					if (idTipoReporte.equals("7")) { 
						html_tablasgraficos += "\n<div style='page-break-before:always;'></div>";
					}
					html_tablasgraficos += "\n<div id='div_grafica_"+j+"' class='grafico_pdf'></div>";
					html_tablasgraficos += "\n";
					js_graficas += "\ndata.addRows(a_tabla);";
					js_graficas += "\na_data.push(data);";
					j++;
				}
				if (idTipoReporte.equals("1")) {
					if ((j%2) == 0) {
						html_tablasgraficos += "\n<div style='page-break-before:always;'></div>";
					}
				}
				else {
					html_tablasgraficos += "\n<div style='page-break-before:always;'></div>";
				}
				html_tablasgraficos += "\n<div class='div_tablagraficos'>";
				html_tablas += "\n<h3>" +division_territorial+ "</h3>";
				html_tablasgraficos += "\n<div class='titulo_naranja'><h3>" +division_territorial+ "</h3></div>";

				divisiones_territoriales.add(division_territorial);

				options_graficas.add("<option value='"+j+"'>"+division_territorial+"</option>");
				divs_graficas.add("<div id='div_grafica_"+j+"' class='grafico_pdf'></div>");
				js_graficar += "\ngraficarReporte("+j+", 'div_grafica_"+j+"', '"+division_territorial+"');";
				
				js_graficas += "\n// " + division_territorial + " ("+j+"):";
				js_graficas += "\nvar data = new google.visualization.DataTable();";
				js_graficas += "\ndata.addColumn('string','"+nombreTipoReporte+"');";
				js_graficas += "\ndata.addColumn('number','"+label_areaha+"');";

				html_tablas += "\n<table>";
				html_tablasgraficos += "\n<table>";
				html_tablas += "\n<tr class='titulo_datos'>";
				html_tablasgraficos += "\n<tr>";
				html_tablas += "<th>"+nombreTipoReporte+"</th>";
				html_tablasgraficos += "<th>"+nombreTipoReporte+"</th>";
				html_tablas += "<th>"+label_areaha+"</th>";
				html_tablasgraficos += "<th>"+label_areaha+"</th>";
				if (idTipoReporte.equals("7")) {
					html_tablas += "<th>"+label_incertidumbre+"</th>";
					html_tablasgraficos += "<th>"+label_incertidumbre+"</th>";
					html_tablas += "<th>"+label_error+"</th>";
					html_tablasgraficos += "<th>"+label_error+"</th>";
					html_tablas += "<th>"+label_baj+"</th>";
					html_tablasgraficos += "<th>"+label_baj+"</th>";
					html_tablas += "<th>"+label_bat+"</th>";
					html_tablasgraficos += "<th>"+label_bat+"</th>";
					html_tablas += "<th>"+label_caj+"</th>";
					html_tablasgraficos += "<th>"+label_caj+"</th>";
					html_tablas += "<th>"+label_cat+"</th>";
					html_tablasgraficos += "<th>"+label_cat+"</th>";
					html_tablas += "<th>"+label_coe+"</th>";
					html_tablasgraficos += "<th>"+label_coe+"</th>";
					html_tablas += "<th>"+label_coet+"</th>";
					html_tablasgraficos += "<th>"+label_coet+"</th>";
					
					js_graficas += "\ndata.addColumn('number','"+label_incertidumbre+"');";
					js_graficas += "\ndata.addColumn('number','"+label_error+"');";
					js_graficas += "\ndata.addColumn('number','"+label_baj+"');";
					js_graficas += "\ndata.addColumn('number','"+label_bat+"');";
					js_graficas += "\ndata.addColumn('number','"+label_caj+"');";
					js_graficas += "\ndata.addColumn('number','"+label_cat+"');";
					js_graficas += "\ndata.addColumn('number','"+label_coe+"');";
					js_graficas += "\ndata.addColumn('number','"+label_coet+"');";

				}
				else {
	 				js_graficas += "\ndata.addColumn({type: 'string', role: 'style'})";
				}
				html_tablas += "</tr>";
				html_tablasgraficos += "</tr>";
				js_graficas += "\nvar a_tabla = new Array();";

			}

			html_tablas += "\n<tr>";				
			html_tablasgraficos += "\n<tr>";				

			division_territorial_anterior = division_territorial;

			clasificacion = agregadoReporte.getClasificacion();
			
			if (!Auxiliar.inStringArrayList(clasificacion, clasificaciones)) {
				clasificaciones.add(clasificacion);
			}

			areastr = String.format("%,f", agregadoReporte.getArea()).replace(".000000", "");
			area = String.format("%.0f", agregadoReporte.getArea());
			incertidumbre = String.valueOf(agregadoReporte.getIncertidumbre());
			error = String.valueOf(agregadoReporte.getError());
			baj = String.valueOf(agregadoReporte.getBaj());
			batstr = String.format("%,f", agregadoReporte.getBat()).replace(".000000", "");
			bat = String.format("%.0f", agregadoReporte.getBat());
			caj = String.valueOf(agregadoReporte.getCaj());
			catstr = String.format("%,f", agregadoReporte.getCat()).replace(".000000", "");
			cat = String.format("%.0f", agregadoReporte.getCat());
			coe = String.valueOf(agregadoReporte.getCoe());
			coetstr = String.format("%,f", agregadoReporte.getCoet()).replace(".000000", "");
			coet = String.format("%.0f", agregadoReporte.getCoet());

			js_graficas += "\na_tabla.push(['"+clasificacion+"'";

			html_tablas += "<td>"+clasificacion+"</td>";
			html_tablasgraficos += "<td>"+clasificacion+"</td>";
			html_tablas += "<td class='numero'>"+areastr+"</td>";
			html_tablasgraficos += "<td class='numero'>"+areastr+"</td>";
			js_graficas += ","+area;
			if (idTipoReporte.equals("7")) {
				html_tablas += "<td class='numero'>"+incertidumbre+"</td>";
				html_tablasgraficos += "<td class='numero'>"+incertidumbre+"</td>";
				js_graficas += ","+incertidumbre;
				html_tablas += "<td class='numero'>"+error+"</td>";
				html_tablasgraficos += "<td class='numero'>"+error+"</td>";
				js_graficas += ","+error;
				html_tablas += "<td class='numero'>"+baj+"</td>";
				html_tablasgraficos += "<td class='numero'>"+baj+"</td>";
				js_graficas += ","+baj;
				html_tablas += "<td class='numero'>"+batstr+"</td>";
				html_tablasgraficos += "<td class='numero'>"+batstr+"</td>";
				js_graficas += ","+bat;
				html_tablas += "<td class='numero'>"+caj+"</td>";
				html_tablasgraficos += "<td class='numero'>"+caj+"</td>";
				js_graficas += ","+caj;
				html_tablas += "<td class='numero'>"+catstr+"</td>";
				html_tablasgraficos += "<td class='numero'>"+catstr+"</td>";
				js_graficas += ","+cat;
				html_tablas += "<td class='numero'>"+coe+"</td>";
				html_tablasgraficos += "<td class='numero'>"+coe+"</td>";
				js_graficas += ","+coe;
				html_tablas += "<td class='numero'>"+coetstr+"</td>";
				html_tablasgraficos += "<td class='numero'>"+coetstr+"</td>";
				js_graficas += ","+coet;
			}
			else {
				if (idTipoReporte.equals("1")) {
					if (clasificacion.equals("Bosque")) {
						js_graficas += ",'color: #3D8944'";
					}
					else if (clasificacion.equals("No Bosque")) {
						js_graficas += ",'color: #F4F4D8'";
					}
					else {
						js_graficas += ",'color: #FFA500'";
					}
				}
				else if (idTipoReporte.equals("3")) {
					if (clasificacion.equals("Bosque Estable") || clasificacion.equals("Bosque")) {
						js_graficas += ",'color: #3D8944'";
					}
					else if (clasificacion.equals("Deforestación")) {
						js_graficas += ",'color: #FF0000'";
					}
					else if (clasificacion.equals("Deforestación Anual (ha)")) {
						js_graficas += ",'color: #880000'";
					}
					else if (clasificacion.equals("No Bosque Estable")) {
						js_graficas += ",'color: #F4F4D8'";
					}
					else if (clasificacion.equals("Regeneración")) {
						js_graficas += ",'color: #0000FF'";
					}
					else {
						js_graficas += ",'color: #FFA500'";
					}
				}
				else {
	 				c = Auxiliar.enteroAlAzar(0, colores.size()-1);
					if (colores.size()>c) {
						color = colores.get(c);
					}
					js_graficas += ",'color: "+color+"'";
				}
			}

			html_tablas += "</tr>";
			html_tablasgraficos += "</tr>";
			js_graficas += "]);";
		}
		
		html_tablas += "\n</table>";
		html_tablasgraficos += "\n</table>";
		html_tablasgraficos += "\n<div id='div_grafica_"+j+"' class='grafico_pdf'></div>";
		js_graficas += "\ndata.addRows(a_tabla);";
		js_graficas += "\na_data.push(data);";

	}
}

String leyenda = "";

if (idTipoReporte.equals("1")) {
	leyenda = "<div class='leyenda'><table class='leyenda'><tr><td class='leyenda' style='background-color:#3D8944; width:33%; color: white !important;'>Bosque/Forest</td><td class='leyenda' style='background-color:#F4F4D8; width:33%;'>No Bosque/No Forest</td><td class='leyenda' style='background-color:#FFA500; width:33%;'>Sin Información/No Data</td></tr></table></div>";
}
else if (idTipoReporte.equals("3") || idTipoReporte.equals("5")) {
	leyenda = "<div class='leyenda'><table class='leyenda'><tr><td class='leyenda' style='background-color:#3D8944; width:20%; color: white !important;'>Bosque Estable/Stable Forest</td><td class='leyenda' style='background-color:#FF0000; width:20%; color: white !important;'>Deforestación/Deforestation</td><td class='leyenda' style='background-color:#F4F4D8; width:20%;'>No Bosque Estable/No Stable Forest</td><td class='leyenda' style='background-color:#0000FF; width:20%; color: white !important;'>Regeneración/Regeneration</td><td class='leyenda' style='background-color:#FFA500; width: 20%;'>Sin Información/No Data</td></tr></table></div>";
}


%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
<script type="text/javascript" src="../js/leaflet.js"></script>

<style type="text/css">
.mapa_wms {
    width: 100%;
    height: 520px;
    border: none;
}
</style>


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

var titulo_grafico = '<%=nombreTipoReporte%>';
var titulo_hAxis = '<%=nombreTipoReporte%>';

<%

out.print(js_graficas);

%>


var gWidth = "100%";
var gHeight = "550";
var gBarChartWidth = "90%";
var gBarChartHeight = "50%";

function graficarReporte(r, divid, titulo_grafico) {

	if (r.length == 0) {
		document.getElementById(divid).style.display = 'none'; 
		return;
	}

	document.getElementById(divid).style.display = 'block'; 

	if (document.getElementById('tabs-2')) {
		document.getElementById('tabs-2').style.display = 'block';
	}
	
	var options = null;
	var chart = null;

	var n_registros = a_tabla.length;
	console.log("n_registros = " + n_registros);
	var alto_titulo = 40;
	var alto_subtitulo = 20;
	var alto_leyenda = 50;
	var alto_barra = 60;
	var alto_total = alto_titulo + alto_leyenda + alto_subtitulo + (n_registros * alto_barra) ;
	console.log("alto_total = " + alto_total);
	
	options = {
			title : titulo_grafico,
			width : gWidth,
			height : alto_total,
            chartArea: {  width: gBarChartWidth, height: gBarChartHeight },
			backgroundColor: 'FFFFFF',
			legend : {
				position : 'top',
				maxLines : 4
			},
			vAxis : {
				textPosition: "in",
				title : 'Total',
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
 			seriesType: 'bars',
 			isStacked: true
	};

	chart = new google.visualization.ColumnChart(document.getElementById(divid));
	chart.draw(a_data[r], options);
	
}

$(function() {
	var tabs = $( "#tabs" ).tabs();
	tabs.find( ".ui-tabs-nav" ).sortable({
		axis: "x",
		stop: function() {
			tabs.tabs( "refresh" );
		}
	});
});

$(document).ready(function() {
	inicializarNavegador();

<%
if (modoPDF.equals("1")) {
	out.println(js_graficar);
}
%>
	
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
}

function descargarReporte(tipoArchivo) {
	document.getElementById("tipoArchivo").value = tipoArchivo;
	document.getElementById("formDescargaDoc").submit();
}

</script>

</head>
<% if (!modoPDF.equals("1")) { %>
<body class='sidebarlast front'   onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()+"?id_reporte="+id_reporte) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
						
						<h2 class="titulo_naranja"><%=msj.getString("titulo_reporte_" + idTipoReporte) %></h2>


						<div id="block-resultados-reporte" class="block-gray  block">
	                        <div class="content">
						  	  <h2><%=nombreTipoReporte %> <%=msj.getString("por")%> <%=nombreDivisionTerritorial %> <%=msj.getString("periodos")%>: <%=periodoS %> (Id: <%=id_reporte %>)</h2>
	                          <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" id="tabs">
	                            <ul role="tablist" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
	                              <li aria-selected="true" aria-labelledby="ui-id-1" aria-controls="tabs-1" tabindex="0" role="tab" class="tab-tabla ui-state-default ui-corner-top ui-tabs-active ui-state-active"><a id="ui-id-1" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-1"><%=msj.getString("ver_tablas") %></a></li>
	                              <li aria-selected="false" aria-labelledby="ui-id-2" aria-controls="tabs-2" tabindex="-1" role="tab" class="tab-graficas ui-state-default ui-corner-top"><a id="ui-id-2" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-2"><%=msj.getString("ver_graficas") %></a></li>
	                              <li aria-selected="false" aria-labelledby="ui-id-3" aria-controls="tabs-3" tabindex="-1" role="tab" class="tab-mapa ui-state-default ui-corner-top"><a id="ui-id-3" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-3"><%=msj.getString("ver_mapa") %></a></li>
	                            </ul>
	                            <div aria-hidden="false" aria-expanded="true" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-1" id="tabs-1">
	
	                              <div class="div_tablas"><%=html_tablas %></div>
	                              
	                              <div class="block-dark-gray block-aclaracion">
									<div class="titulo_aclaracion"><h3><%=msj.getString("aclaracion") %></h3></div>
									<div class="block-dark-gray">
										<p style="text-align: justify;"><%=msj.getString("texto1_aclaracion") %></p>		
										<p style="text-align: justify;"><%=msj.getString("texto2_aclaracion") %></p>		
									</div>
	                              </div>
	                              <div class="form-actions">
	                                  <input class="btn btn-default btn-back" onclick="window.location='/MonitoreoBC-WEB'" value="<%=msj.getString("volver") %>" type="button">
									<% if (!ConsultaWebUsuario.usuarioAceptoLicencia(Integer.parseInt(id_usuario), nombreLicencia)) { %>
										<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=msj.getString("nota_licencia_descarga_recurso_prefijo")%> "<%=nombreLicencia %>" <%=msj.getString("nota_licencia_descarga_recurso_mediofijo")%> <a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp"><%=msj.getString("nota_licencia_descarga_recurso_postfijo")%></a></div>
									<% } else { %>
	                                  <input class="btn btn-default" value= "<%=msj.getString("descargas.descargarXLS") %>"  onclick="descargarReporte('xlsx')" type="button">
	                                  <input class="btn btn-default" value= "<%=msj.getString("descargas.descargarPDF") %>"  onclick="descargarReporte('pdf')" type="button">
									  <input class="btn btn-default" value= "<%=msj.getString("descargas.descargarMapa") %>"  onclick="descargarReporte('mapa')" type="button"> 
									<% } %>
									<% if (Auxiliar.nz(idTipoReporte, "").equals("7")) { %>
	                                  <input class="btn btn-default" onclick="window.location='/MonitoreoBC-WEB/pdf/aportes_monitoreo_de_bosques_y_carbono_web.pdf'" value="<%=msj.getString("ver_pdf_aportes_monitoreo") %>" type="button">
	                                <% } %>                                  
	                              </div>
	                            </div>
	
	                            <div aria-hidden="true" aria-expanded="false" style="display: inherit;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-2" id="tabs-2">
	
								  <div class="form-group" style="padding-left: 20px; padding-right: 20px">
									<label for="exampleInputEmail1"><%=msj.getString("titulo_division_territorial") %></label>
									<div class="select-wrapper" style="max-width: 400px;">
										<select name="divisionTerritorio" id="divisionTerritorio" onchange="graficarReporte(this.value, 'div_grafica', this.options[this.selectedIndex].innerHTML);">
											<option value=""></option>
	<%
	
	for (int i=0; i<options_graficas.size(); i++) {
		out.println(options_graficas.get(i));
	}
	
	%>
										</select>
									</div>
								  </div>
								  								
	
								  <div id="div_grafica" class="graficas block-dark-gray">
	                              
	<%
	                      
	// for (int i=0; i<divs_graficas.size(); i++) {
	// 	out.println(divs_graficas.get(i));
	// }
	                      
	%>                              
	 							  </div>                             
	                              
	                              <div class="block-dark-gray block-aclaracion">
									<div class="titulo_aclaracion"><h3><%=msj.getString("aclaracion") %></h3></div>
									<div class="block-dark-gray">
										<p style="text-align: justify;"><%=msj.getString("texto1_aclaracion") %></p>		
										<p style="text-align: justify;"><%=msj.getString("texto2_aclaracion") %></p>		
									</div>
	                              </div>
	                              <div class="form-actions">
	                                  <input class="btn btn-default btn-back" onclick="window.location='/MonitoreoBC-WEB'" value="<%=msj.getString("volver") %>"  type="button">
									<% if (!ConsultaWebUsuario.usuarioAceptoLicencia(Integer.parseInt(id_usuario), nombreLicencia)) { %>
										<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=msj.getString("nota_licencia_descarga_recurso_prefijo")%> "<%=nombreLicencia %>" <%=msj.getString("nota_licencia_descarga_recurso_mediofijo")%> <a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp"><%=msj.getString("nota_licencia_descarga_recurso_postfijo")%></a></div>
									<% } else { %>
	                                  <input class="btn btn-default" value= "<%=msj.getString("descargas.descargarXLS") %>" onclick="descargarReporte('xlsx')" type="button">
	                                  <input class="btn btn-default" value= "<%=msj.getString("descargas.descargarPDF") %>" onclick="descargarReporte('pdf')" type="button">
									  <input class="btn btn-default" value= "<%=msj.getString("descargas.descargarMapa") %>" onclick="descargarReporte('mapa')" type="button"> 
									<% } %>
									<% if (Auxiliar.nz(idTipoReporte, "").equals("7")) { %>
	                                  <input class="btn btn-default" onclick="window.location='/MonitoreoBC-WEB/pdf/aportes_monitoreo_de_bosques_y_carbono_web.pdf'" value="<%=msj.getString("ver_pdf_aportes_monitoreo") %>" type="button">
	                                <% } %>                                  
	                              </div>  
	
	                            </div>
	
	                            
	                            <div aria-hidden="true" aria-expanded="false" style="display:inherit;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-3" id="tabs-3">
	                                 
	                            
	                            <div class="block-dark-gray block-aclaracion">
	
								<!--<iframe id="iframe_mapa_wms" class="mapa_wms" src="/AdmIF/mapaWMS.jsp"></iframe>-->                            
								<iframe id="iframe_mapa_wms" class="mapa_wms" src="/MonitoreoBC-WEB/mapaWMSReporteGeoproceso.jsp?id_reporte=<%=id_reporte %>&identImagen=<%=identImagen %>"></iframe>                            
	                            <%=leyenda %>
	                            
									<div class="titulo_aclaracion"><h3><%=msj.getString("aclaracion") %></h3></div>
									<div class="block-dark-gray">
										<p style="text-align: justify;"><%=msj.getString("texto1_aclaracion") %></p>		
										<p style="text-align: justify;"><%=msj.getString("texto2_aclaracion") %></p>		
									</div>
	                              </div>
	                              <div class="form-actions">
	                                  <input class="btn btn-default btn-back" onclick="window.location='/MonitoreoBC-WEB'" value="Regresar" type="button">
									<% if (!ConsultaWebUsuario.usuarioAceptoLicencia(Integer.parseInt(id_usuario), nombreLicencia)) { %>
										<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=msj.getString("nota_licencia_descarga_recurso_prefijo")%> "<%=nombreLicencia %>" <%=msj.getString("nota_licencia_descarga_recurso_mediofijo")%> <a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp"><%=msj.getString("nota_licencia_descarga_recurso_postfijo")%></a></div>
									<% } else { %>
	                                  <input class="btn btn-default" value="<%=msj.getString("descargas.descargarXLS") %>"  onclick="descargarReporte('xlsx')" type="button">
	                                  <input class="btn btn-default" value="<%=msj.getString("descargas.descargarPDF") %>"  onclick="descargarReporte('pdf')" type="button">
									  <input class="btn btn-default" value="<%=msj.getString("descargas.descargarMapa") %>" onclick="descargarReporte('mapa')" type="button"> 
									<% } %>
									<% if (Auxiliar.nz(idTipoReporte, "").equals("7")) { %>
	                                  <input class="btn btn-default" onclick="window.location='/MonitoreoBC-WEB/pdf/aportes_monitoreo_de_bosques_y_carbono_web.pdf'" value="<%=msj.getString("ver_pdf_aportes_monitoreo") %>" type="button">
	                                <% } %>                                  
	                              </div>
	                            </div>
	                          </div>
	 
	                        </div>
	                      </div>

							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>
						</div>
					</div>
				</div>

			</div>

			<%=UI.getFooter(msj) %>									

		</div>
	</form>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										

	<form action="/MonitoreoBC-WEB/descargaDocumentosReportesServlet" method="post" id="formDescargaDoc" target="_blank">
		<input type="hidden" value="<%=id_reporte%>" name="id_reporte" id="id_reporte"></input> 
		<input type="hidden" value="<%=idTipoReporte%>" name="treporte" id="treporte"></input> 
		<input type="hidden" name="tipoArchivo" id="tipoArchivo"></input> 
		<input type="hidden" name="cx" id="cx" value="-73"></input> 
		<input type="hidden" name="cy" id="cy" value="5"></input> 
		<input type="hidden" name="zoom" id="zoom" value="10"></input> 
		<input type="hidden" name="idiomia" id="idioma" value="<%=idioma%>"></input> 
		<input type="hidden" name="identImagen" id="identImagen" value="<%=request.getSession().getAttribute("identImagen")%>"></input> 
	</form>
	
</body>
<% 


}
else { 

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String fechaConFormato = sdf.format(new Date());
	

%>
<body class='pdf'">
  <div id="marco_pdf">		
	<div>
		<a href="/MonitoreoBC-WEB/reg/indexLogOn.jsp"><img src="../img/logo.png" alt=""></a>
		<a href="http://www.minambiente.gov.co/web/index.html"><img src="../img/img-min.png" alt=""></a> 
		<a href="http://wsp.presidencia.gov.co/portal/Paginas/default.aspx"><img src="../img/img-prosperidad.png" alt=""></a> 
		<a href="http://www.moore.org/"><img src="../img/img-moore.png" alt=""></a> 
		<a href="http://www.patrimonionatural.org.co/"><img src="../img/img-patrimonio.png" alt=""></a>
	</div>

	<div class='titulo_portada'>
	<p class='titulo_portada'><%=nombreTipoReporte %></p>
	<p class='titulo_portada'><%=msj.getString("por")%> <%=nombreDivisionTerritorial %></p>
	<p class='titulo_portada'><%=msj.getString("periodos")%>: <%=periodoS %></p>
	<p class='subtitulo_portada'><%=fechaConFormato %></p>
	</div>

<!-- 	<div style="page-break-before:always;"></div> -->
<%-- 	<div class="titulo_naranja"><h2><%=msj.getString("tablasygraficos") %></h2></div> --%>
	<div class="div_tabla_pdf">
		<div class="div_tablas"><%=html_tablasgraficos %></div>
	</div>


<!-- 	<div style="page-break-before:always;"></div> -->
<%-- 	<div class="titulo_naranja"><h2><%=msj.getString("graficos") %></h2></div> --%>
<!-- 	<div class="block-dark-gray"> -->
<!-- 		<div id="div_graficas"> -->

<%
                      
// for (int i=0; i<divs_graficas.size(); i++) {
// 	out.println(divs_graficas.get(i));
// }

%>                              
		
<!-- 		</div> -->
<!-- 	</div> -->
			
	<div style="page-break-before:always;"></div>							
	<div class="titulo_naranja"><h2><%=msj.getString("mapa") %></h2></div>                            
	<div class="block-dark-gray">
		<iframe id="iframe_mapa_wms" style="width: 100%; height: 600px;" class="mapa_wms" src="/MonitoreoBC-WEB/mapaWMSReporteGeoproceso.jsp?id_reporte=<%=id_reporte %>&identImagen=<%=identImagen %>"></iframe>                            
	</div>

	<div style="page-break-before:always;"></div>
	<div class="titulo_aclaracion"><h3><%=msj.getString("aclaracion")%></h3></div>
	<div class="block-dark-gray">
		<p style="text-align: justify;"><%=msj.getString("texto1_aclaracion")%></p>		
		<p style="text-align: justify;"><%=msj.getString("texto2_aclaracion")%></p>		
	</div>

	<%=UI.getFooter(msj) %>									

	</div>
</body>

<% } %>

</html>

