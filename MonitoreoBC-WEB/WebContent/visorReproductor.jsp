<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.mbc.conexion.ParametroNoBean"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.Auxiliar"%>
<%@page import="co.gov.ideamredd.bosqueencifras.entities.*"%>
<%@page import="co.gov.ideamredd.bosqueencifras.dao.BosqueEnCifras"%>
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
out.print(doctype); 
%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<%
ArrayList<CapaWMS> capasBNB = BosqueEnCifras.consultarCapasBNB();

String opciones_capas = "";
String a_nombres_capas = "";

if (capasBNB != null) {
	if (capasBNB.size() > 0) {
		for (int i=0; i<capasBNB.size(); i++) {
			CapaWMS capaWMS = capasBNB.get(i);
			opciones_capas += "\n<option value='"+i+"'>" + capaWMS.getPeriodo() + "</option>";
			a_nombres_capas += "\na_nombres_capas["+i+"] = '" + capaWMS.getPeriodo() + "';";  
		}
	}
}


%>

<%
ParametroNoBean parametro;
parametro = new ParametroNoBean();
 	
String identImagen = Auxiliar.nz(request.getParameter("identImagen"), "");

String atribucion_defecto = "Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors. <a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, Imagery © <a href='http://mapbox.com'>Mapbox</a>, Parcelas/Plots © <a href='http://www.ideam.gov.co'>IDEAM</a> y/o Colaboradores."; 

String wms_base = Auxiliar.nzObjStr(parametro.getParametro("wms_base"), "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
String wms_atribucion = Auxiliar.nzObjStr(parametro.getParametro("wms_atribucion"), atribucion_defecto);
String wms_pixeles_srs = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_srs"), "EPSG:4326");
String wms_pixeles_crs = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_crs"), "L.CRS.EPSG4326");
String wms_pixeles_url = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_url"), "http://seinekan.ideam.gov.co/ApolloCatalogWMSPublic/service.svc/get?service=WMS&request=getLayer");

String wms_pixeles_format = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_format"), "image/png");
String wms_pixeles_version = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_version"), "1.3.0");
String wms_pixeles_latitud = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_latitud"), "4");
String wms_pixeles_longitud = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_longitud"), "-73");
String wms_pixeles_zoom = Auxiliar.nzObjStr(parametro.getParametro("wms_pixeles_zoom"), "5");

String zoom = Auxiliar.nz(request.getParameter("zoom"), wms_pixeles_zoom);
String cx = Auxiliar.nz(request.getParameter("cx"), wms_pixeles_longitud);
String cy = Auxiliar.nz(request.getParameter("cy"), wms_pixeles_latitud);

%>

<head>
<title>Visor/Viewer</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/<% out.print(estilo); %>" media="all" />

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>
<script type="text/javascript" src="/MonitoreoBC-WEB/js/leaflet.js"></script>

<script type="text/javascript">

var CX = '<%=cx %>';
var CY = '<%=cy %>';

if (CX != '') {
	dCX = CX * 1.0; 
}
else {
	dCX = -73;
}
if (CY != '') {
	dCY = CY * 1.0;
}
else {
	dCY = 5; 
}

var zoom = '<%=zoom %>';
if (zoom != '') {
	dZoom = zoom * 1.0; 
}
else {
	dZoom = 5;
}

dCY = 2.5096;
dCX = -72.1637;
dZoom = 5; 

var wms_base = '<%=wms_base %>';
var wms_atribucion = '<%=wms_atribucion %>';
var wms_pixeles_crs = '<%=wms_pixeles_crs %>';
var wms_pixeles_srs = '<%=wms_pixeles_srs %>';
var wms_pixeles_format = '<%=wms_pixeles_format %>';
var wms_pixeles_version = '<%=wms_pixeles_version %>';
var wms_pixeles_url = '<%=wms_pixeles_url %>';

var map;

var a_capas = new Array();
var a_nombres_capas = new Array();

<%
out.println(a_nombres_capas);
%>

function initmap() {
	map = L.map('map', {crs: <%=wms_pixeles_crs %>}).setView([dCY, dCX], dZoom);

	L.tileLayer(wms_base, {srs: wms_pixeles_srs, attribution: wms_atribucion}).addTo(map);

	<%
	for (int i=0; i<capasBNB.size(); i++) {
		out.println("a_capas["+i+"] = L.tileLayer.wms(wms_pixeles_url, {layers: '" + capasBNB.get(i).getIdentimagen() + "',format: 'image/png', srs: wms_pixeles_srs,transparent: true, version: '1.3.0', attribution: wms_atribucion});");
		out.println("a_capas["+i+"].addTo(map);");
		out.println("opacidadCapa("+i+", 0.75)");
	}
	%>
}

function opacidadCapa(id_capa, opacidad) {
	if (a_capas[id_capa]) {
		a_capas[id_capa].setOpacity(opacidad);
	}
}

function mostrarCapa(id_capa) {
	ocultarCapas();
	opacidadCapa(id_capa, 0.75);
	document.getElementById('etiqueta_capa').innerHTML = a_nombres_capas[id_capa];
}

function ocultarCapas() {
	for (var i=0; i<a_capas.length; i++) {
		if (a_capas[i]) {
			opacidadCapa(i, 0.0);
		}
	}
}

var tiempo_intervalo_capas = 1000;
var intervalo_capas;
var reproducir_capas = true;
var id_capa_actual = 0;
var waitforit = 0;

function posteriorCapa() {
	var n_capas = a_capas.length;

	if (id_capa_actual < n_capas-1) {
		id_capa_actual++;
	}
	else {
		waitforit++;
		console.log("waitforit..."+waitforit);

		if (waitforit == 5) {
			id_capa_actual = 0;
			waitforit = 0;
		}
		else {
			return;
		}
	}
	
	console.log("id_capa_actual: " + id_capa_actual); 
	mostrarCapa(id_capa_actual);
}

function reproducirCapas() {
	intervalo_capas = setInterval(
		function() {
			if (reproducir_capas) {
				posteriorCapa();
			}
		},
		tiempo_intervalo_capas
	);
}


</script>


<script type='text/javascript'>

$(document).ready(function(){
	<% if (es_movil) { %>
    	document.body.style.width = (getWidth()-0) + 'px';
    <% } %>

    range = document.getElementById('range');
    
    initmap();
    reproducirCapas();
    
});
</script>

</head>


<body>

<div id="map" class="map"></div>

<div id="etiqueta_capa"></div>

</body>
</html>

