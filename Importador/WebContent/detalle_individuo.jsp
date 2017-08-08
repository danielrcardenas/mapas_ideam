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
String yo = "detalle_individuo.";
String idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "es");

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

if(es_movil) { 
	doctype = " <!DOCTYPE html PUBLIC '-//WAPFORUM//DTD XHTML Mobile 1.0//EN' 'http://www.wapforum.org/DTD/xhtml-mobile10.dtd' >"; 
	estilo = "estilos_movil.css";
} 
else {
	doctype = " <!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd' >";
	estilo = "estilos_pc.css";
} 
out.print(doctype); 

String pe = "";
String t = "";

try { pe = msj.getString("General.Por_favor_especifique"); } catch (MissingResourceException e) { t = "Por favor especifique" + " "; }

String PRCL_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("PRCL_CONSECUTIVO"), "").toString();

%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<link rel='stylesheet' type='text/css' media='all' href='js/jscalendar/calendar-win2k-cold-1.css' title='win2k-cold-1' />

<script type='text/javascript' src='js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='js/jscalendar/calendar-setup.js'></script>

<title>
<% try { out.print(msj.getString(yo+"Detalle_de_Individuo")); } catch (MissingResourceException e) { out.print("Detalle de Individuo" + " "); } %>
</title>

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/ajax.js'></script>
<script type='text/javascript' src='js/ajax_opciones.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>


<script type='text/javascript'>

$(document).ready(function(){
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-30) + 'px';
    <% } %>
});

function nueva(f) {
	url = 'Individuo?accion=detalle_individuo&PRCL_CONSECUTIVO=<%=PRCL_CONSECUTIVO%>';
	location.href = url;
}

function editar(f) {
	if (validar(f))	{
		f.submit();
	}
	else {
		alert('<% try { out.print(msj.getString("General.Formulario_Incompleto")); } catch (MissingResourceException e) { out.print("Faltaron algunos datos. Por favor verifique los datos resaltados." + " "); } %>.');
	}
}

function validar(f) {
	if (!f) f = document.f;
	
	var valido = true;
	
	if (f.PRCL_CONSECUTIVO.value == '') {
		valido = false;
	}
	
	if (f.INDV_TXCT_ID.value == '') {
		f.texto_especie.className = 'invalido';
		valido = false;
	}
	else {
		f.texto_especie.className = 'valido';
	}
	
	if (f.INDV_ESARBOLREFERENCIA.value == '') {
		f.INDV_ESARBOLREFERENCIA.className = 'invalido';
		valido = false;
	}
	else {
		f.INDV_ESARBOLREFERENCIA.className = 'valido';
	}

	if (f.INDV_NUMERO_ARBOL.value == '') {
		f.INDV_NUMERO_ARBOL.className = 'invalido';
		valido = false;
	}
	else {
		f.INDV_NUMERO_ARBOL.className = 'valido';
	}
	
	if (f.INDV_NUMERO_ARBOL.value == '') {
		f.INDV_NUMERO_ARBOL.className = 'invalido';
		valido = false;
	}
	else {
		f.INDV_NUMERO_ARBOL.className = 'valido';
	}
	
	f.INDV_DENSIDAD.className = 'valido';
	document.getElementById('INDV_DENSIDAD_MSG').style.display = 'none';
	if (f.INDV_DENSIDAD.value == '') {
		f.INDV_DENSIDAD.className = 'invalido';
		valido = false;
	}
	if (!esDensidad(f.INDV_DENSIDAD.value)) {
		f.INDV_DENSIDAD.className = 'invalido';
		document.getElementById('INDV_DENSIDAD_MSG').innerHTML = 'La densidad debe ser un número decimal entre 0 y 1';
		document.getElementById('INDV_DENSIDAD_MSG').style.display = 'block';
		valido = false;
	}
	
	if (f.INDV_INCLUIR.value == '') {
		f.INDV_INCLUIR.className = 'invalido';
		valido = false;
	}
	else {
		f.INDV_INCLUIR.className = 'valido';
	}
	
	f.INDV_SUBPARCELA.className = 'valido';
	document.getElementById('INDV_SUBPARCELA_MSG').style.display = 'none';
	if (f.INDV_SUBPARCELA.value == '') {
		f.INDV_SUBPARCELA.className = 'invalido';
		valido = false;
	}
	if (!esSubparcela(f.INDV_SUBPARCELA.value)) {
		f.INDV_SUBPARCELA.className = 'invalido';
		document.getElementById('INDV_SUBPARCELA_MSG').innerHTML = 'Las subparcelas se numeran entre 1 y 5';
		document.getElementById('INDV_SUBPARCELA_MSG').style.display = 'block';
		valido = false;
	}
	
	f.INDV_DISTANCIA.className = 'valido';
	document.getElementById('INDV_DISTANCIA_MSG').style.display = 'none';
	if (f.INDV_DISTANCIA.value == '') {
		f.INDV_DISTANCIA.className = 'invalido';
		valido = false;
	}
	if (!esDistancia(f.INDV_DISTANCIA.value)) {
		f.INDV_DISTANCIA.className = 'invalido';
		document.getElementById('INDV_DISTANCIA_MSG').innerHTML = 'La distancia debe ser un número decimal mayor o igual a cero.';
		document.getElementById('INDV_DISTANCIA_MSG').style.display = 'block';
		valido = false;
	}
	
	f.INDV_AZIMUTH.className = 'valido';
	document.getElementById('INDV_AZIMUTH_MSG').style.display = 'none';
	if (f.INDV_AZIMUTH.value == '') {
		f.INDV_AZIMUTH.className = 'invalido';
		valido = false;
	}
	if (!esAzimut(f.INDV_AZIMUTH.value)) {
		f.INDV_AZIMUTH.className = 'invalido';
		document.getElementById('INDV_AZIMUTH_MSG').innerHTML = 'El azimut va desde 0 hasta 359, siendo 0/360 norte, 90 este, 180 sur, 270 oeste';
		document.getElementById('INDV_AZIMUTH_MSG').style.display = 'block';
		valido = false;
	}

	/*
	f.INDV_LATITUD.className = 'valido';
	document.getElementById('INDV_LATITUD_MSG').style.display = 'none';
	if (f.INDV_LATITUD.value != '') {
		if (!esLatitud(f.INDV_LATITUD.value)) {
			f.INDV_LATITUD.className = 'invalido';
			document.getElementById('INDV_LATITUD_MSG').innerHTML = 'La latitud va desde -90 hasta +90.';
			document.getElementById('INDV_LATITUD_MSG').style.display = 'block';
			valido = false;
		}
	}
	
	f.INDV_LONGITUD.className = 'valido';
	document.getElementById('INDV_LONGITUD_MSG').style.display = 'none';
	if (f.INDV_LONGITUD.value != '') {
		if (!esLongitud(f.INDV_LONGITUD.value)) {
			f.INDV_LONGITUD.className = 'invalido';
			document.getElementById('INDV_LONGITUD_MSG').innerHTML = 'La longitud geográfica va desde -180 hasta +180.';
			document.getElementById('INDV_LONGITUD_MSG').style.display = 'block';
			valido = false;
		}
	}
	*/
	
	return valido;
}

function eliminar(f) {
	if (confirm('<% try { out.print(msj.getString(yo+"Confirma_la_eliminacion_del_elemento_seleccionado")); } catch (MissingResourceException e) { out.print("Confirma la eliminación del elemento seleccionado" + " "); } %>')) {
		f.accion.value = 'eliminar';
		f.submit();
	}
}

function exportar_a_excel(f) {
	url = 'Individuo?accion=exportar_individuos&INDV_CONSECUTIVO=' + f.INDV_CONSECUTIVO.value;
	window.open(url);	
}

function exportar_a_pdf(f) {
	url = 'Individuo?accion=exportar_individuo_pdf&INDV_CONSECUTIVO=' + f.INDV_CONSECUTIVO.value;
	window.open(url);	
}

function coordenadas(f) {
	var url = '';
	if (f.PRCL_CONSECUTIVO.value != '') {
		url = 'Individuo?accion=coordenadas&control=&PRCL_CONSECUTIVO='+f.PRCL_CONSECUTIVO.value;
		window.open(url); 
	}
	else {
		alert('Antes debe crear el individuo');	
	}
}

function esNumero(num){
    return (!isNaN(num) && num != null);
}

var arbolito;


function success(position)
{
	return;
	
	var INDV_LATITUD = document.getElementById('INDV_LATITUD');
	var INDV_LONGITUD = document.getElementById('INDV_LONGITUD');

	var lat = position.coords.latitude;
	var lon = position.coords.longitude;

	if (!esNumero(lat)) lat = 0;
	if (!esNumero(lon)) lon = 0;
	
	INDV_LATITUD.value = lat;
	INDV_LONGITUD.value = lon;

	arbolito = L.icon({
	    iconUrl: 'mapas_individuos/images/marker-icon.png',
	    shadowUrl: 'mapas_individuos/images/marker-shadow.png',

	    iconSize:     [10, 20], // size of the icon
	    shadowSize:   [20, 20], // size of the shadow
	    iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location
	    shadowAnchor: [5, 20],  // the same for the shadow
	    popupAnchor:  [0, -20] // point from which the popup should open relative to the iconAnchor
	});
	
	L.marker([lat, lon], {icon: arbolito}).addTo(map).bindPopup("GPS").openPopup();
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
	var INDV_LATITUD = document.getElementById('INDV_LATITUD');
	var INDV_LONGITUD = document.getElementById('INDV_LONGITUD');
		
	if (!esNumero(lat)) lat = 0;
	if (!esNumero(lon)) lon = 0;

	INDV_LATITUD.value = lat;
	INDV_LONGITUD.value = lon;

	arbolito = L.icon({
	    iconUrl: 'mapas_individuos/images/marker-icon.png',
	    shadowUrl: 'mapas_individuos/images/marker-shadow.png',

	    iconSize:     [10, 20], // size of the icon
	    shadowSize:   [20, 20], // size of the shadow
	    iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location
	    shadowAnchor: [5, 20],  // the same for the shadow
	    popupAnchor:  [0, -20] // point from which the popup should open relative to the iconAnchor
	});

	
	if (markerVertice == null) {
		markerVertice = L.marker([lat,lon], {icon: arbolito});
		markerVertice.addTo(map);
	}
	else {
		markerVertice.setLatLng(L.latLng(lat, lon));
	}
	

}

function mostrarCoordenadas(lat, lon) {
	document.getElementById('div_coordenadas').innerHTML = '('+lat+','+lon+')';	
}

function gps(f)
{
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


</script>

<!-- Put these into the <head> -->
<link rel="stylesheet" href="js/responsive-nav/responsive-nav.css">
<script src="js/responsive-nav/responsive-nav.js"></script>

</head>
<body>

<%=co.gov.ideamredd.admif.UI.getHeader() %>

<%
co.gov.ideamredd.admif.Sec sec = new co.gov.ideamredd.admif.Sec();

String menu = "";
try {
	String id_usuario = "";
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "es");
	if (!id_usuario.equals("")) {
		menu = sec.generarMenu(id_usuario, "detalle_individuo.jsp", idioma);
	}
}
catch (Exception e) {
	menu = "..." + e.toString();
}

%>
<div class="menu"><%=menu %></div>

<%
String datos_sesion = "";
try {
	String id_usuario = "";
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	String t_usuario = "";
	try { t_usuario = msj.getString("General.USUARIO_EN_SESION"); } catch (MissingResourceException e) { t_usuario = "Usuario en sesión:"; }
	if (Auxiliar.tieneAlgo(id_usuario)) datos_sesion = Auxiliar.mensajeImpersonal("sesion", t_usuario + ": " + id_usuario);
}
catch (Exception e) {
	datos_sesion = "..." + e.toString();
}
%>
<div id="div_datos_sesion"><%=datos_sesion %></div>

<div id="content">
<div class="content-inner">

<p class="titulo-derecho">
<% try { out.print(msj.getString("General.Administracion_de_inventarios_forestales")); } catch (MissingResourceException e) { out.print("Administración de Inventarios Forestales" + " "); } %>
</p>

<div id="div_titulo__detalle_de_individuo"  onclick="javascript:DIVisibilidad('div__form');">
<h1>
<% try { out.print(msj.getString(yo+"Detalle_de_Individuo")); } catch (MissingResourceException e) { out.print("Detalle de Individuo" + " "); } %>
</h1>
</div>

<% 
out.println(request.getAttribute("retorno")); 
%>

<div id="div__form" class="form">

<form name="f" action='Individuo' method='post'>


<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Consecutivo Individuo" + " "); } %>
</div>
<%
String INDV_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("INDV_CONSECUTIVO"), "").toString();
%>
<input type=hidden name="INDV_CONSECUTIVO" value="<%= INDV_CONSECUTIVO %>" />
<%= INDV_CONSECUTIVO %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_FID")); } catch (MissingResourceException e) { out.print("Consecutivo FID" + " "); } %>
</div>
<%
String INDV_FID = Auxiliar.nzObjStr(request.getAttribute("INDV_FID"), "").toString();
%>
<input type=hidden name="INDV_FID" value="<%= INDV_FID %>" />
<%= INDV_FID %>
</div>

<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("PRCL_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Consecutivo Parcela" + " "); } %>
</div>
<input type="hidden" name="PRCL_CONSECUTIVO" value="<%=PRCL_CONSECUTIVO%>" />
<%= PRCL_CONSECUTIVO %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_PRCL_PLOT")); } catch (MissingResourceException e) { out.print("Plot" + " "); } %>
</div>
<%
String INDV_PRCL_PLOT = Auxiliar.nzObjStr(request.getAttribute("INDV_PRCL_PLOT"), "").toString();
%>
<input type="hidden" name="INDV_PRCL_PLOT" value="<%=INDV_PRCL_PLOT%>" />
<%= INDV_PRCL_PLOT %>
</div>

<div style="clear: both;"></div>


<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_ESARBOLREFERENCIA")); } catch (MissingResourceException e) { out.print("Es Árbol de Referencia" + ":"); } %>
</div>
<select class="form-control" name="INDV_ESARBOLREFERENCIA" onchange="validar(this.form)">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("o_INDV_ESARBOLREFERENCIA"), "").toString()); 
%>
</select>
</div>


<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_NUMERO_ARBOL")); } catch (MissingResourceException e) { out.print("Número de Árbol" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_NUMERO_ARBOL" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_NUMERO_ARBOL"), "").toString() %>"  onchange="validar(this.form)"/>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_CARDINALIDAD")); } catch (MissingResourceException e) { out.print("Número de individuos por especie" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_CARDINALIDAD" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_CARDINALIDAD"), "").toString() %>" />
</div>

<div style="clear: both;"></div>

<div class="campo">
<div class="etiqueta">
<% 
try { out.print(msj.getString("INDV_ESPECIE")); } catch (MissingResourceException e) { out.print("Especie" + " "); } 
%>
</div>
<input type="text" name="INDV_ESPECIE" value="<%=Auxiliar.nzObjStr(request.getAttribute("INDV_ESPECIE"), "").toString()%>" />
</div>

<div class="campo" onclick="validar(document.f)">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_TXCT_ID")); } catch (MissingResourceException e) { out.print("Especie" + " "); } %>
</div>
<input type="text" name="texto_especie" id="texto_especie" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_TXCT_ID"), "").toString() %>" onblur="validar(this.form)"/>
<input type="button" onclick="ajax_opciones('Individuo','INDV_TXCT_ID','div_estado_ajax_especie','?accion=encontrar_especies&texto='+document.getElementById('texto_especie').value,'validar','No se encontraron especies.', 'div_especies', 'div_especie_seleccionada', 'INDV_ESPECIE');" value='<% try { out.print(msj.getString("General.Buscar")); } catch (MissingResourceException e) { out.print("Buscar" + " "); } %>' class="btn btn-default" />
<div id="div_estado_ajax_especie"></div>
<div id="div_especie_seleccionada" style="background: white;" onclick="document.f.texto_especie.focus();validar(document.f);">[<%=Auxiliar.nzObjStr(request.getAttribute("INDV_TXCT_ID"), "").toString() %>]<%=Auxiliar.nzObjStr(request.getAttribute("INDV_ESPECIE"), "").toString() %></div>
<div id="div_especies" style="background: #cce; position: relative; z-index: 2;"></div>
<input type="hidden" name="INDV_TXCT_ID" id="INDV_TXCT_ID" value="<%=Auxiliar.nzObjStr(request.getAttribute("INDV_TXCT_ID"), "").toString() %>"  onchange="validar(this.form)"/>
<input type="hidden" name="INDV_ESPECIE" id="INDV_ESPECIE" value="<%=Auxiliar.nzObjStr(request.getAttribute("INDV_ESPECIE"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_FAMILIA")); } catch (MissingResourceException e) { out.print("Familia" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_FAMILIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_FAMILIA"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_GENERO")); } catch (MissingResourceException e) { out.print("Género" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_GENERO" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_GENERO"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_AUTORGENERO")); } catch (MissingResourceException e) { out.print("Autor del Género" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_AUTORGENERO" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_AUTORGENERO"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_ESTADOEPITETO")); } catch (MissingResourceException e) { out.print("Estado Epiteto" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_ESTADOEPITETO" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_ESTADOEPITETO"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_EPITETO")); } catch (MissingResourceException e) { out.print("Epiteto" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_EPITETO" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_EPITETO"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_MORFOESPECIE")); } catch (MissingResourceException e) { out.print("Morfología de la Especie" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_MORFOESPECIE" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_MORFOESPECIE"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_AUTORESPECIE")); } catch (MissingResourceException e) { out.print("Autor Especie" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_AUTORESPECIE" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_AUTORESPECIE"), "").toString() %>" />
</div>


<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_HABITO")); } catch (MissingResourceException e) { out.print("Hábito" + ":"); } %>
</div>
<select class="form-control" name="INDV_HABITO" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_habito"), "").toString()); 
%>
</select>
</div>
<div style="clear: both;"></div>

<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_DENSIDAD")); } catch (MissingResourceException e) { out.print("Densidad de la Madera del Individuo" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_DENSIDAD" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_DENSIDAD"), "").toString() %>"  onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="INDV_DENSIDAD_MSG"></div>
</div>


<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_INCLUIR")); } catch (MissingResourceException e) { out.print("Incluir Individuo en Cálculos" + ":"); } %>
</div>
<select class="form-control" name="INDV_INCLUIR"  onchange="validar(this.form)">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_incluir"), "").toString()); 
%>
</select>
</div>

<div style="clear: both;"></div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_NUMERO_COLECTOR")); } catch (MissingResourceException e) { out.print("Número de Colector" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_NUMERO_COLECTOR" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_NUMERO_COLECTOR"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_CANTIDAD_EJEMPLARES")); } catch (MissingResourceException e) { out.print("Cantidad de ejemplares recolectados" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_CANTIDAD_EJEMPLARES" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_CANTIDAD_EJEMPLARES"), "").toString() %>" />
</div>

<!-- 
<div class="campo">
<div class="etiqueta">
<% //try { out.print(msj.getString("INDV_ETIQUETA_COLECTA")); } catch (MissingResourceException e) { out.print("Etiqueta Colecta" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_ETIQUETA_COLECTA" value="<%//= Auxiliar.nzObjStr(request.getAttribute("INDV_ETIQUETA_COLECTA"), "").toString() %>" />
</div>
-->
<input type="hidden" name="INDV_ETIQUETA_COLECTA" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_ETIQUETA_COLECTA"), "").toString() %>" />

<!-- 
<div class="campo">
<div class="etiqueta">
<% //try { out.print(msj.getString("INDV_FOTO_COLECTA")); } catch (MissingResourceException e) { out.print("Foto Colecta" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_FOTO_COLECTA" value="<%//= Auxiliar.nzObjStr(request.getAttribute("INDV_FOTO_COLECTA"), "").toString() %>" />
</div>
-->
<input type="hidden" name="INDV_FOTO_COLECTA" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_FOTO_COLECTA"), "").toString() %>" />

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_HOMOLOGACION")); } catch (MissingResourceException e) { out.print("Homologación" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_HOMOLOGACION" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_HOMOLOGACION"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_SUBPARCELA")); } catch (MissingResourceException e) { out.print("Código de Subparcela" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_SUBPARCELA" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_SUBPARCELA"), "").toString() %>"  onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="INDV_SUBPARCELA_MSG"></div>
</div>

<div style="clear: both;"></div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("INDV_OBSERVACIONES")); } catch (MissingResourceException e) { out.print("Observaciones" + " "); } %>
</div>
<textarea class="form-control" cols=36 rows=4 name="INDV_OBSERVACIONES"><%= Auxiliar.nzObjStr(request.getAttribute("INDV_OBSERVACIONES"), "").toString() %></textarea>
</div>

<div style="clear: both;"></div>

<!-- 
<div class="campo">
<div class="etiqueta">
-->
<% 
//try { out.print(msj.getString("INDV_LATITUD")); } catch (MissingResourceException e) { out.print("Latitud Jalón Principal" + " "); } 
%>
<!-- 
</div>
<input class="form-control" type=text name="INDV_LATITUD" id="INDV_LATITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_LATITUD"), "").toString() %>" />
<input type="button" value="GPS" onclick="gps(this.form)" />
<div class="div_mensaje_dato_invalido" id="INDV_LATITUD_MSG"></div>
</div>
-->
<input type="hidden" name="INDV_LATITUD" value="" />

<!-- 
<div class="campo">
<div class="etiqueta">
-->
<% 
//try { out.print(msj.getString("INDV_LONGITUD")); } catch (MissingResourceException e) { out.print("Longitud Jalón Principal" + " "); } 
%>
<!-- 
</div>
<input class="form-control" type=text name="INDV_LONGITUD" id="INDV_LONGITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_LONGITUD"), "").toString() %>" />
<input type="button" value="GPS" onclick="gps(this.form)" />
<div class="div_mensaje_dato_invalido" id="INDV_LONGITUD_MSG"></div>
</div>
-->
<input type="hidden" name="INDV_LONGITUD" value="" />

<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_DISTANCIA")); } catch (MissingResourceException e) { out.print("Distancia desde el jalón principal" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_DISTANCIA" id="INDV_DISTANCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_DISTANCIA"), "").toString() %>"  onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="INDV_DISTANCIA_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta_dato_obligatorio">
<% try { out.print(msj.getString("INDV_AZIMUTH")); } catch (MissingResourceException e) { out.print("Azimuth desde el jalón principal" + " "); } %>
</div>
<input class="form-control" type=text name="INDV_AZIMUTH" id="INDV_AZIMUTH" value="<%= Auxiliar.nzObjStr(request.getAttribute("INDV_AZIMUTH"), "").toString() %>"  onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="INDV_AZIMUTH_MSG"></div>
</div>


<div class="botones">
<input type="button" onclick='javascript:editar(this.form);' value='<% try { out.print(msj.getString("General.Guardar")); } catch (MissingResourceException e) { out.print("Guardar" + " "); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:exportar_a_excel(this.form);' value='<% try { out.print(msj.getString("General.Exportar_a_Excel")); } catch (MissingResourceException e) { out.print("Exportar a Excel" + " "); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:eliminar(this.form);' value='<% try { out.print(msj.getString("General.Eliminar")); } catch (MissingResourceException e) { out.print("Eliminar" + " "); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:nueva(this.form);' value='<% try { out.print(msj.getString("General.Nueva")); } catch (MissingResourceException e) { out.print("Registrar Nuevo Individuo" + ".."); } %>' class="btn btn-default" />
</div>

<input type="hidden" name="accion" value="guardar" />
</form>

</div>

<div style="display: inline-block;">
<div class="opcionmenu">
<%
String usuarioAceptoLicencia = Auxiliar.nzObjStr(request.getAttribute("usuarioAceptoLicencia"), "");
String indicacionLicencia = Auxiliar.nzObjStr(request.getAttribute("indicacionLicencia"), "");
if (!usuarioAceptoLicencia.equals("1")) { 
%>
	<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=indicacionLicencia %></div>
<% } else { %>
<a class=boton href='Individuo?accion=exportar_pdf&INDV_CONSECUTIVO=<%=INDV_CONSECUTIVO %>' target='_blank'>
<% try { out.print(msj.getString("General.EXPORTAR_INDIVIDUO_PDF")); } catch (MissingResourceException e) { out.print("Exportar a PDF" + ":"); } %> 
</a>
<% } %>
</div>
</div>

<div>

<h1>
<% try { out.print(msj.getString("General.Visor")); } catch (MissingResourceException e) { out.print("Visor" + ".."); } %>
</h1>

<div id="div_coordenadas"></div>
<div id="map" style="width: 100%; height: 400px"></div>

<script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
<script type="text/javascript">

var a_coordenadas = Array();
var a_coordenadas_secuencia = Array();


var db_PRCL_LONGITUD = '<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_LONGITUD"), "").toString() %>';
var db_PRCL_LATITUD = '<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_LATITUD"), "").toString() %>';

if (db_PRCL_LONGITUD != '') {
	PRCL_LONGITUD = db_PRCL_LONGITUD * 1.0; 
}
else {
	PRCL_LONGITUD = -73;
}
if (db_PRCL_LATITUD != '') {
	PRCL_LATITUD = db_PRCL_LATITUD * 1.0;
}
else {
	PRCL_LATITUD = 5; 
}

var db_INDV_LONGITUD = '<%= Auxiliar.nzObjStr(request.getAttribute("INDV_LONGITUD"), "").toString() %>';
var db_INDV_LATITUD = '<%= Auxiliar.nzObjStr(request.getAttribute("INDV_LATITUD"), "").toString() %>';

if (db_INDV_LONGITUD != '') {
	INDV_LONGITUD = db_INDV_LONGITUD * 1.0; 
}
else {
	INDV_LONGITUD = PRCL_LONGITUD;
}
if (db_INDV_LATITUD != '') {
	INDV_LATITUD = db_INDV_LATITUD * 1.0;
}
else {
	INDV_LATITUD = PRCL_LATITUD; 
}

var map = L.map('map', {maxZoom:30, minZoom: 5}).setView([INDV_LATITUD, INDV_LONGITUD], 19);


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


/*
var str_viewparams = "";

var PRCL_CONSECUTIVO = '<%//= Auxiliar.nzObjStr(request.getParameter("PRCL_CONSECUTIVO"),"") %>';
var w_PRCL_CONSECUTIVO = '';
if (PRCL_CONSECUTIVO.length > 0) {
	w_PRCL_CONSECUTIVO = " AND PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
	str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';'; 
}

if (str_viewparams.length>0) {
	str_viewparams = str_viewparams.slice(0,-1);
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
*/

if (db_INDV_LATITUD != '' && db_INDV_LONGITUD != '') {
	a_coordenadas = [db_INDV_LATITUD, db_INDV_LONGITUD];
	arbolito = L.icon({
	    iconUrl: 'mapas_individuos/images/marker-icon.png',
	    shadowUrl: 'mapas_individuos/images/marker-shadow.png',

	    iconSize:     [10, 20], // size of the icon
	    shadowSize:   [20, 20], // size of the shadow
	    iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location
	    shadowAnchor: [5, 20],  // the same for the shadow
	    popupAnchor:  [0, 20] // point from which the popup should open relative to the iconAnchor
	});

	L.marker(a_coordenadas, {icon: arbolito}).addTo(map);
}/*
var S = '<%//= Auxiliar.nzObjStr(request.getAttribute("S"), "").toString() %>';
var W = '<%//= Auxiliar.nzObjStr(request.getAttribute("W"), "").toString() %>';
var N = '<%//= Auxiliar.nzObjStr(request.getAttribute("N"), "").toString() %>';
var E = '<%//= Auxiliar.nzObjStr(request.getAttribute("E"), "").toString() %>';

if (S != '' && W != '' && N != '' && E != '') {
	if (S != '') dS = S * 1.0; 
	if (W != '') dW = W * 1.0; 
	if (N != '') dN = N * 1.0; 
	if (E != '') dE = E * 1.0; 
	
	map.fitBounds([[dS, dW],[dN, dE]]);
}
*/

var popup = L.popup();

map.doubleClickZoom.disable();

function ponerVertice(e) {
	//establecerCoordenadasDesdeElMapa(e.latlng.lat, e.latlng.lng);
}
map.on('click', ponerVertice);

function desplegarCoordenadas(e) {
	mostrarCoordenadas(e.latlng.lat, e.latlng.lng);
}
map.on('mousemove', desplegarCoordenadas);

var factor = 0.0000045;


//var bounds = [[dCY+(95*factor), dCX-(95*factor)], [dCY-(95*factor), dCX+(95*factor)]];
//L.rectangle(bounds, {color: "#bbbbbb", weight: 1}).addTo(map);

<%
String etiqueta_SPF1 = Auxiliar.nzObjStr(request.getAttribute("etiqueta_SPF1"), "").toString();
String etiqueta_SPF2 = Auxiliar.nzObjStr(request.getAttribute("etiqueta_SPF2"), "").toString();
String etiqueta_SPF3 = Auxiliar.nzObjStr(request.getAttribute("etiqueta_SPF3"), "").toString();
String etiqueta_SPF4 = Auxiliar.nzObjStr(request.getAttribute("etiqueta_SPF4"), "").toString();
String etiqueta_SPF5 = Auxiliar.nzObjStr(request.getAttribute("etiqueta_SPF5"), "").toString();
%>

var etiqueta_SPF1 = '<%= etiqueta_SPF1%>';
var etiqueta_SPF2 = '<%= etiqueta_SPF2%>';
var etiqueta_SPF3 = '<%= etiqueta_SPF3%>';
var etiqueta_SPF4 = '<%= etiqueta_SPF4%>';
var etiqueta_SPF5 = '<%= etiqueta_SPF5%>';

var SPF1_BRINZAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF1_LATIZAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF1_FUSTAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF1_FUSTAL_GRANDE = L.circle([PRCL_LATITUD, PRCL_LONGITUD], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map).bindPopup(etiqueta_SPF1);

var SPF2_BRINZAL = L.circle([PRCL_LATITUD+(80*factor), PRCL_LONGITUD+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF2_LATIZAL = L.circle([PRCL_LATITUD+(80*factor), PRCL_LONGITUD], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF2_FUSTAL = L.circle([PRCL_LATITUD+(80*factor), PRCL_LONGITUD], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF2_FUSTAL_GRANDE = L.circle([PRCL_LATITUD+(80*factor), PRCL_LONGITUD], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map).bindPopup(etiqueta_SPF2);

var SPF3_BRINZAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD+(80*factor)+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF3_LATIZAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD+(80*factor)], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF3_FUSTAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD+(80*factor)], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF3_FUSTAL_GRANDE = L.circle([PRCL_LATITUD, PRCL_LONGITUD+(80*factor)], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map).bindPopup(etiqueta_SPF3);

var SPF4_BRINZAL = L.circle([PRCL_LATITUD-(80*factor), PRCL_LONGITUD+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF4_LATIZAL = L.circle([PRCL_LATITUD-(80*factor), PRCL_LONGITUD], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF4_FUSTAL = L.circle([PRCL_LATITUD-(80*factor), PRCL_LONGITUD], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF4_FUSTAL_GRANDE = L.circle([PRCL_LATITUD-(80*factor), PRCL_LONGITUD], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map).bindPopup(etiqueta_SPF4);

var SPF5_BRINZAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD-(80*factor)+(7.5*factor)], 1.5, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF5_LATIZAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD-(80*factor)], 3, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF5_FUSTAL = L.circle([PRCL_LATITUD, PRCL_LONGITUD-(80*factor)], 7, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map);
var SPF5_FUSTAL_GRANDE = L.circle([PRCL_LATITUD, PRCL_LONGITUD-(80*factor)], 15, {color:'green',fillColor:'#008844',fillOpacity: 0.5,weight:1}).addTo(map).bindPopup(etiqueta_SPF5);


var usuario = '<% out.print(request.getParameter("usuario"));%>'; 
var idioma = '<% out.print(request.getParameter("idioma"));%>'; 
var PRCL_CONSECUTIVO = '<% out.print(request.getParameter("PRCL_CONSECUTIVO"));%>'; 

</script>

</div>

<%
if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
	String url_tallos = "Tallo?accion=detalle&control=&TAYO_INDV_CONSECUTIVO=" + INDV_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_tallos"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_tallos');">
<h1>
<% try { out.print(msj.getString("General.Tallos_de_Individuo")); } catch (MissingResourceException e) { out.print("Tallos del Individuo" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_tallos" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_tallos" id="iframe_tallos" src="<%= url_tallos %>"></iframe>
</div>
<% } %>

<%
if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
	String url_carguemasivo = "registro_archivo.jsp?INDV_CONSECUTIVO=" + INDV_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_carguemasivo"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_carguemasivo');">
<h1>
<% try { out.print(msj.getString(yo+"IMPORTACION_DE_TALLOS")); } catch (MissingResourceException e) { out.print("Importación de Tallos" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_carguemasivo" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_carguemasivo" id="iframe_carguemasivo" src="<%= url_carguemasivo %>"></iframe>
</div>
<% } %>

<%
if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
	String url_imagenes = "ImagenesIndividuo?accion=imagenes_individuo&control=&INDV_CONSECUTIVO=" + INDV_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_imagenes"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_imagenes');">
<h1>
<% try { out.print(msj.getString(yo+"Imagenes_de_Individuo")); } catch (MissingResourceException e) { out.print("Fotos del Individuo" + ".."); } %>
</h1>
</div>
<div id="div__iframe_subformulario_imagenes" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_imagenes" id="iframe_imagenes" src="<%= url_imagenes %>"></iframe>
</div>
<% } %>

</div>
</div>

<!-- Put this right before the </body> closing tag -->
<script>
  //var nav = responsiveNav(".nav-collapse");
  
  var nav = responsiveNav(".nav-collapse", { // Selector
  animate: true, // Boolean: Use CSS3 transitions, true or false
  transition: 284, // Integer: Speed of the transition, in milliseconds
  label: "Menu", // String: Label for the navigation toggle
  insert: "before", // String: Insert the toggle before or after the navigation
  customToggle: "", // Selector: Specify the ID of a custom toggle
  closeOnNavClick: false, // Boolean: Close the navigation when one of the links are clicked
  openPos: "relative", // String: Position of the opened nav, relative or static
  navClass: "nav-collapse", // String: Default CSS class. If changed, you need to edit the CSS too!
  navActiveClass: "js-nav-active", // String: Class that is added to  element when nav is active
  jsClass: "js", // String: 'JS enabled' class which is added to  element
  init: function(){}, // Function: Init callback
  open: function(){}, // Function: Open callback
  close: function(){} // Function: Close callback
});
</script>
</body>
</html>