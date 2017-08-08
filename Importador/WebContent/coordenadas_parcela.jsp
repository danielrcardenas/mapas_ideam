<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="co.gov.ideamredd.admif.BD" %>
<%@ page import="co.gov.ideamredd.admif.Auxiliar" %>
<%
//co.gov.ideamredd.admif.Auxiliar aux = new co.gov.ideamredd.admif.Auxiliar();
LenguajeI18N L = new LenguajeI18N();
ResourceBundle msj = null;
String yo = "coordenadas_parcela.";
String idioma = Auxiliar.nz(request.getParameter("idioma"), "es");

if (idioma.equals("es")) {
	L.setLenguaje("es");
	L.setPais("CO");
}
else {
	L.setLenguaje("en");
	L.setPais("US");
}
msj = L.obtenerMensajeIdioma();
%>
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
out.print(doctype); 
%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<title>
<% try { out.print(msj.getString(yo+"Coordenadas_de_Parcela")); } catch (MissingResourceException e) { out.print("Coordenadas de la Parcela" + ".."); } %>
</title>

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>

<script type='text/javascript'>

function guardar(f) {
	if (validar(f))	{
		f.control.value = 'guardar';
		f.submit();
	}
}

function validar(f) {
	var go = true;
	
	if (f.PRCR_PRCL_CONSECUTIVO.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_el_consecutivo_de_la_parcela")); } catch (MissingResourceException e) { out.print("Por favor especifique el consecutivo de la parcela" + ".."); } %>.');
		f.PRCR_PRCL_CONSECUTIVO.focus();
		go = false;
	}
	
	if (f.PRCR_SECUENCIA.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_la_secuencia")); } catch (MissingResourceException e) { out.print("Por favor especifique la secuencia" + ".."); } %>.');
		f.PRCR_SECUENCIA.focus();
		go = false;
	}
	
	if (f.PRCR_LATITUD.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_la_latitud")); } catch (MissingResourceException e) { out.print("Por favor especifique la latitud" + ".."); } %>.');
		f.PRCR_LATITUD.focus();
		go = false;
	}
	
	if (f.PRCR_LONGITUD.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_la_longitud")); } catch (MissingResourceException e) { out.print("Por favor especifique la longitud" + ".."); } %>.');
		f.PRCR_LONGITUD.focus();
		go = false;
	}

	return go;
}

function eliminar(f) {
	if (confirm('<% try { out.print(msj.getString(yo+"Confirma_la_eliminacion_del_elemento_seleccionado")); } catch (MissingResourceException e) { out.print("Confirma la eliminación del elemento seleccionado" + ".."); } %>')) {
		f.control.value = 'eliminar';
		f.submit();
	}
}

function esNumero(num){
    return (!isNaN(num) && num != null);
}

function success(position)
{
	var PRCR_LATITUD = document.getElementById('PRCR_LATITUD');
	var PRCR_LONGITUD = document.getElementById('PRCR_LONGITUD');

	var lat = position.coords.latitude;
	var lon = position.coords.longitude;

	if (!esNumero(lat)) lat = 0;
	if (!esNumero(lon)) lon = 0;
	
	PRCR_LATITUD.value = lat;
	PRCR_LONGITUD.value = lon;

	var secuencia = document.getElementById('PRCR_SECUENCIA').value;
	
	L.marker([lat, lon]).addTo(map).bindPopup(secuencia).openPopup();
};

function fail(error)
{
	switch(error.code) // Returns 0-3
	{
		case 0:
			// Unknown error alert error message
			alert(error.message);
			break;

		case 1:
			// Permission denied alert error message
			alert(error.message);
			break;
	}
};

var markerVertice = null;

function establecerCoordenadasDesdeElMapa(lat, lon) {
	var PRCR_SECUENCIA = document.getElementById('PRCR_SECUENCIA');
	var PRCR_LATITUD = document.getElementById('PRCR_LATITUD');
	var PRCR_LONGITUD = document.getElementById('PRCR_LONGITUD');
		
	if (!esNumero(lat)) lat = 0;
	if (!esNumero(lon)) lon = 0;

	//PRCR_SECUENCIA.value = '';
	PRCR_LATITUD.value = lat;
	PRCR_LONGITUD.value = lon;

	if (markerVertice == null) {
		markerVertice = L.marker([lat,lon]);
		markerVertice.addTo(map);
	}
	else {
		markerVertice.setLatLng(L.latLng(lat, lon));
	}
	
}

function gps(f)
{
	if (f.PRCR_SECUENCIA.value == '') {
		alert('Antes debe especificar la secuencia del vértice.');
		return;
	}
	
	//navigator.geolocation.getCurrentPosition(success, fail);

	if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function(position) {
                success(position);
            },
            function(error) {
                fail(error);
            }, {
                enableHighAccuracy: true,
                timeout: 10000,
                maximumAge: 10000
            });
    } else {
        alert('No encontre soporte para geoubicación en su dispositivo...');
        // no support for geolocation
    }	
}

function inicializar() {
	var ancho = window.screen.availWidth;

	/*
	if (ancho < 800) {
		document.getElementById('body').style.width = '400px';
	}
	*/

	$(document).ready(function(){
		<% if (es_movil) { %>
	    document.body.style.width = (getWidth()-30) + 'px';
	    <% } %>
	});	
}

</script>

</head>
<body onload='inicializar()' id="body" style='background: white;'>

<%
String datos_sesion = "";
try {
	String id_usuario = "";
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "");
	
	datos_sesion = Auxiliar.mensajeImpersonal("sesion", "Usuario: " + id_usuario + ", Idioma: " + idioma);
}
catch (Exception e) {
	datos_sesion = "..." + e.toString();
}
%>
<div id="div_datos_sesion"><%=datos_sesion %></div>

<% 
out.println(request.getAttribute("retorno")); 
%>

<form action='Parcela?accion=coordenadas&usuario=<% out.print(request.getParameter("usuario"));%>&idioma=<% out.print(request.getParameter("idioma"));%>' method='post'>

<div>
	<%
	String PRCR_PRCL_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("PRCL_CONSECUTIVO"), "").toString();
	%>
	<input type=hidden name="PRCR_PRCL_CONSECUTIVO" value="<%= PRCR_PRCL_CONSECUTIVO %>" />
	<%
	String PRCL_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("PRCL_CONSECUTIVO"), "").toString();
	%>
	<input type=hidden name="PRCL_CONSECUTIVO" value="<%= PRCL_CONSECUTIVO %>" />
	<% try { out.print(msj.getString("PRCR_PRCL_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Polígono de parcela" + ".."); } %>
	: <%= PRCR_PRCL_CONSECUTIVO %>
</div>

<div>
	<h4><% try { out.print(msj.getString("General.Editar_Coordenada")); } catch (MissingResourceException e) { out.print("Editar coordenada" + ".."); } %></h4>
</div>


<div style="display: inline-block;">

<div class="flotante">
	<% try { out.print(msj.getString("PRCR_SECUENCIA")); } catch (MissingResourceException e) { out.print("Secuencia" + ".."); } %>
	<br/>
	<input class="form-control" type=text id="PRCR_SECUENCIA" name="PRCR_SECUENCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCR_SECUENCIA"), "").toString() %>" />
</div>

<div class="flotante">
	<% try { out.print(msj.getString("PRCR_LATITUD")); } catch (MissingResourceException e) { out.print("Latitud" + ".."); } %>
	<br/>
	<input class="form-control" type=text id="PRCR_LATITUD" name="PRCR_LATITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCR_LATITUD"), "").toString() %>" />
</div>

<div class="flotante">
	<% try { out.print(msj.getString("PRCR_LONGITUD")); } catch (MissingResourceException e) { out.print("Longitud" + ".."); } %>
	<br/>
	<input class="form-control" type=text id="PRCR_LONGITUD" name="PRCR_LONGITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCR_LONGITUD"), "").toString() %>" />
</div>

<div class="flotante">
<input type="button" value="GPS" onclick="gps(this.form)" />

<input type=hidden name="PRCR_ID" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCR_ID"), "").toString() %>" />
<input type="hidden" name="control" value="" />
<input type="button" onclick='javascript:guardar(this.form);' value='<% try { out.print(msj.getString("General.Guardar")); } catch (MissingResourceException e) { out.print("Guardar" + ".."); } %>' class="btn btn-default" />
</div>

</div>

<div>
<h4>
<% try { out.print(msj.getString("General.Visor")); } catch (MissingResourceException e) { out.print("Visor" + ".."); } %>
</h4>

<div id="map" style="width: 100%; height: 400px"></div>

<script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
<script type="text/javascript">

var a_coordenadas = Array();
var a_coordenadas_secuencia = Array();

var CX = '<%= Auxiliar.nzObjStr(request.getAttribute("CX"), "").toString() %>';
var CY = '<%= Auxiliar.nzObjStr(request.getAttribute("CY"), "").toString() %>';

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

var map = L.map('map').setView([dCY, dCX], 6);

<%
BD dbREDD = new BD();

String atribucion_defecto = "Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors. <a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, Parcelas/Plots © <a href='http://www.ideam.gov.co'>IDEAM</a> y/o Colaboradores."; 

String wms_base = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_base'", "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
String wms_atribucion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_atribucion'", atribucion_defecto);

%>

var wms_base = '<%=wms_base %>';
var wms_atribucion = '<%=wms_atribucion %>';

//MAPA BASE
L.tileLayer(wms_base, {attribution: wms_atribucion}).addTo(map);

var str_viewparams = "";

var PRCL_CONSECUTIVO = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_CONSECUTIVO"),"") %>';
var w_PRCL_CONSECUTIVO = '';
if (PRCL_CONSECUTIVO.length > 0) {
	w_PRCL_CONSECUTIVO = " AND PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
	str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';'; 
}

if (str_viewparams.length>0) {
	str_viewparams = str_viewparams.slice(0,-1);
	//document.getElementById('viewparams').innerHTML = 'Filtro actual:['+str_viewparams+']';
}

var parcelas = L.tileLayer.wms("http://54.172.131.5:8080/geoserver/OracleAmazon/wms", {
    layers: 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',
    format: 'image/png',
    transparent: true,
    version: '1.1.0',
	viewparams: str_viewparams, 
    attribution: myAtribucion
});
parcelas.addTo(map);

<%
String str_coordenadas = Auxiliar.nzObjStr(request.getAttribute("str_coordenadas"), "").toString();
String str_coordenadas_secuencia = Auxiliar.nzObjStr(request.getAttribute("str_coordenadas_secuencia"), "").toString();
%>

a_coordenadas = [<%= str_coordenadas %>];
a_coordenadas_secuencia = [<%= str_coordenadas_secuencia %>];

var usuario = '<% out.print(request.getParameter("usuario"));%>'; 
var idioma = '<% out.print(request.getParameter("idioma"));%>'; 
var PRCL_CONSECUTIVO = '<% out.print(request.getParameter("PRCL_CONSECUTIVO"));%>'; 

var min_lat = 90;
var min_lon = 180;
var max_lat = -90;
var max_lon = -180;

var n_coordenadas = a_coordenadas.length;

if (n_coordenadas > 0) {
	L.polygon(a_coordenadas).addTo(map).bindPopup("Polígono de la parcela.");

	for (var i=0; i<a_coordenadas.length; i++) {
		L.marker(a_coordenadas[i]).addTo(map).bindPopup("<a href='Parcela?accion=coordenadas&control=cargar&PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+"&usuario="+usuario+"&idioma="+idioma+"&PRCR_SECUENCIA="+a_coordenadas_secuencia[i]+"'>" + a_coordenadas_secuencia[i] + "</a>").openPopup();
		var latitud = a_coordenadas[i][0];
		var longitud = a_coordenadas[i][1];

		if (latitud < min_lat) {
			min_lat = latitud;
		}

		if (longitud < min_lon) {
			min_lon = longitud;
		}

		if (latitud > max_lat) {
			max_lat = latitud;
		}

		if (longitud > max_lon) {
			max_lon = longitud;
		}
	}

	map.fitBounds([[min_lat, min_lon],[max_lat, max_lon]]);
}
else {
	var S = '<%= Auxiliar.nzObjStr(request.getAttribute("S"), "").toString() %>';
	var W = '<%= Auxiliar.nzObjStr(request.getAttribute("W"), "").toString() %>';
	var N = '<%= Auxiliar.nzObjStr(request.getAttribute("N"), "").toString() %>';
	var E = '<%= Auxiliar.nzObjStr(request.getAttribute("E"), "").toString() %>';

	if (S != '' && W != '' && N != '' && E != '') {
		if (S != '') dS = S * 1.0; 
		if (W != '') dW = W * 1.0; 
		if (N != '') dN = N * 1.0; 
		if (E != '') dE = E * 1.0; 
		
		map.fitBounds([[dS, dW],[dN, dE]]);
	}
}

var popup = L.popup();

map.doubleClickZoom.disable();

function ponerVertice(e) {
	establecerCoordenadasDesdeElMapa(e.latlng.lat, e.latlng.lng);
}

map.on('click', ponerVertice);
</script>

</div>

<div>

<h4>
<% try { out.print(msj.getString("General.Coordenadas")); } catch (MissingResourceException e) { out.print("Coordenadas" + ".."); } %>
</h4>

<div class="tabla_coordenadas" id="div_tabla_coordenadas">

<% 
out.println(request.getAttribute("tabla_coordenadas")); 
%>

</div>

</div>

</form>

</body>
</html>