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
String yo = "visor_parcelas_leaflet.";
String usuario = Auxiliar.nz(request.getParameter("usuario"), "");
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
<title>
<% try { out.print(msj.getString(yo+"Visor_Parcelas")); } catch (MissingResourceException e) { out.print("Visor de Parcelas" + ".."); } %>
</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>

<script type='text/javascript'>

$(document).ready(function(){
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-0) + 'px';
    <% } %>
});
</script>

</head>

<body class="iframe">

<div id="map" class="mapa_parcelas"></div>
<div id="viewparams" style="color: #999; width: 100%; height: 40px; border: 1px solid white; display:none;"></div>

<!--<script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>-->

<script type="text/javascript" src="js/leaflet.js"></script>

<script type="text/javascript">

var str_viewparams = "";

var PRCL_NOMBRE = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_NOMBRE"),"") %>';
var w_PRCL_NOMBRE = '';
if (PRCL_NOMBRE.length > 0) {
	//w_PRCL_NOMBRE = " AND LOWER(PRCL_NOMBRE) LIKE '%25"+PRCL_NOMBRE+"%25' ";
	w_PRCL_NOMBRE = " AND LOWER(PRCL_NOMBRE) LIKE '%"+PRCL_NOMBRE.toLowerCase()+"%' ";
	str_viewparams += 'w_PRCL_NOMBRE:'+w_PRCL_NOMBRE+';'; 
}

var PRCL_CONSECUTIVO = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_CONSECUTIVO"),"") %>';
var w_PRCL_CONSECUTIVO = '';
if (PRCL_CONSECUTIVO.length > 0) {
	w_PRCL_CONSECUTIVO = " AND PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
	str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';'; 
}

var PRCL_CONS_PAIS = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_CONS_PAIS"),"") %>';
var w_PRCL_CONS_PAIS = '';
if (PRCL_CONS_PAIS.length > 0) {
	w_PRCL_CONS_PAIS = " AND PRCL_CONS_PAIS IN ("+PRCL_CONS_PAIS+") ";
	str_viewparams += 'w_PRCL_CONS_PAIS:'+w_PRCL_CONS_PAIS+';'; 
}

var departamentos_seleccionados = '<%= Auxiliar.nzObjStr(request.getParameter("departamentos_seleccionados"),"") %>';
var w_departamentos_seleccionados = '';
if (departamentos_seleccionados.length > 0) {
	w_departamentos_seleccionados = " AND PRCL_CONSECUTIVO IN (SELECT DPPR_CONS_PARCELA FROM RED_DEPTO_PARCELA WHERE DPPR_CONS_DEPTO IN (" + departamentos_seleccionados + ")) ";
	str_viewparams += 'w_departamentos_seleccionados:'+w_departamentos_seleccionados+';'; 
}

var municipios_seleccionados = '<%= Auxiliar.nzObjStr(request.getParameter("municipios_seleccionados"),"") %>';
var w_municipios_seleccionados = '';
if (municipios_seleccionados.length > 0) {
	w_municipios_seleccionados = " AND PRCL_CONSECUTIVO IN (SELECT MNPR_PARCELA FROM RED_MUNICIPIO_PARCELA WHERE MNPR_MUNICIPIO IN (" + municipios_seleccionados + ")) ";
	str_viewparams += 'w_municipios_seleccionados:'+w_municipios_seleccionados+';'; 
}

var PRCL_FECHAINI_APROXIMACION = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_FECHAESTABPARCE"),"") %>';
var w_PRCL_FECHAINI_APROXIMACION = '';
if (PRCL_FECHAINI_APROXIMACION.length > 0) {
	w_PRCL_FECHAINI_APROXIMACION = " AND PRCL_FECHAINI_APROXIMACION >= TO_DATE('"+PRCL_FECHAINI_APROXIMACION+"', 'YYYY-MM-DD HH24:MI:SS') ";
	str_viewparams += 'w_PRCL_FECHAINI_APROXIMACION:'+w_PRCL_FECHAINI_APROXIMACION+';'; 
}

if (str_viewparams.length>0) {
	str_viewparams = str_viewparams.slice(0,-1);
	document.getElementById('viewparams').innerHTML = 'Filtro actual:['+str_viewparams+']';
}

var CX = '<%= Auxiliar.nzObjStr(request.getAttribute("CX"),"").toString() %>';
var CY = '<%= Auxiliar.nzObjStr(request.getAttribute("CY"),"").toString() %>';
var S = '<%= Auxiliar.nzObjStr(request.getAttribute("S"), "").toString() %>';
var W = '<%= Auxiliar.nzObjStr(request.getAttribute("W"), "").toString() %>';
var N = '<%= Auxiliar.nzObjStr(request.getAttribute("N"), "").toString() %>';
var E = '<%= Auxiliar.nzObjStr(request.getAttribute("E"), "").toString() %>';

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

var map = L.map('map').setView([dCY, dCX], 5);/*
var parcelas = L.tileLayer.wms("http://54.172.131.5:8080/geoserver/OracleAmazon/wms", {
    layers: 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',
    format: 'image/png',
    transparent: true,
    version: '1.1.0',
	viewparams: str_viewparams, 
    attribution: myAtribucion
});
parcelas.addTo(map);

if (S != '' && W != '' && N != '' && E != '') {
	if (S != '') dS = S * 1.0; 
	if (W != '') dW = W * 1.0; 
	if (N != '') dN = N * 1.0; 
	if (E != '') dE = E * 1.0; 
	
	map.fitBounds([[dS, dW],[dN, dE]]);
}

*/


<%
BD dbREDD = new BD();

String atribucion_defecto = "Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors. <a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, Imagery © <a href='http://mapbox.com'>Mapbox</a>, Parcelas/Plots © <a href='http://www.ideam.gov.co'>IDEAM</a> y/o Colaboradores."; 

String wms_base = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_base'", "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
String wms_atribucion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_atribucion'", atribucion_defecto);

String wfs_parcelas_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_parcelas_url'", "http://52.4.164.82:8080/geoserver/OracleAmazon/wfs");
String wfs_parcelas_parametro_version = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_parcelas_parametro_version'", "1.0.0");
String wfs_parcelas_parametro_typeName = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_parcelas_parametro_typeName'", "OracleAmazon:C_RED_PARCELA_PARAMETRIZADA");
String wfs_parcelas_parametro_srs = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_parcelas_parametro_srs'", "EPSG:3116");

%>
var wms_base = '<%=wms_base %>';
var wms_atribucion = '<%=wms_atribucion %>';

var wfs_parcelas_url = '<%=wfs_parcelas_url %>';
var wfs_parcelas_parametro_version = '<%=wfs_parcelas_parametro_version %>';
var wfs_parcelas_parametro_typeName = '<%=wfs_parcelas_parametro_typeName %>';
var wfs_parcelas_parametro_srs = '<%=wfs_parcelas_parametro_srs %>';

// MAPA BASE
L.tileLayer(wms_base, {attribution: wms_atribucion}).addTo(map);


//var owsrootUrl = 'http://54.172.131.5:8080/geoserver/OracleAmazon/wfs';
var owsrootUrl = wfs_parcelas_url;

var defaultParameters = {
    service : 'WFS',
    //version : '1.0.0',
    version : wfs_parcelas_parametro_version,
    request : 'GetFeature',
    //typeName : 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',
    typeName : wfs_parcelas_parametro_typeName,
    outputFormat : 'text/javascript',
    format_options : 'callback:getJson',
	viewparams: str_viewparams, 
    //srs : 'EPSG:3116'
    srs : wfs_parcelas_parametro_srs
};

var parameters = L.Util.extend(defaultParameters);
var URL = owsrootUrl + L.Util.getParamString(parameters);

var WFSLayer = null;
var ajax = $.ajax({
    url : URL,
    dataType : 'jsonp',
    jsonpCallback : 'getJson',
    success : function (response) {
        WFSLayer = L.geoJson(response, {
            style: function (feature) {
                return {
                    stroke: true,
                    fillColor: '#99FF99',
                    //fillOpacity: 80,
                    opacity: 80,
                    color: '#228822'
                };
            },
            onEachFeature: function (feature, layer) {

				if (feature.properties && feature.properties.PRCL_NOMBRE) {                
                	popupOptions = {
                        	maxWidth: '480px', 
                        	minWidth: '480px',
                			width: '400px' 
        			};
                	//layer.bindPopup(feature.properties.PRCL_NOMBRE, popupOptions);
                	/*
                	layer.bindPopup(
                        	"Nombre:" + feature.properties.PRCL_NOMBRE
                        	+ "<br/>Área:" + feature.properties.PRCL_AREA
                        	+ "<br/>Descripción:<br/>" + feature.properties.PRCL_DESCRIPCION
                        	+ "<br/>Observaciones:<br/>" + feature.properties.PRCL_AREA
                        	, popupOptions);
                	*/

                	var iframe = '<iframe class="iframe_etiqueta_visor" src="Parcela?accion=visualizar_etiqueta&PRCL_CONSECUTIVO='+feature.properties.PRCL_CONSECUTIVO+'"></iframe>';
                	layer.bindPopup(iframe, popupOptions);
                	
				}
                
                /*
            	return L.marker(latlng, {
            		icon: getIcon(feature.properties.type,'unhighlight')
            		});
        		*/
            }
        }).addTo(map);
        map.fitBounds(WFSLayer.getBounds());
    }
});


map.doubleClickZoom.disable();

</script>
   
</body>
</html>