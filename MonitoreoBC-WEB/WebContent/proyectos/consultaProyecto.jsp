<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.web.proyecto.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Depto"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Municipios"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.web.proyecto.dao.CargaInicialDatosProyectos"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.ConsultaWebUsuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@page import="co.gov.ideamredd.mbc.conexion.ConexionBD"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.Auxiliar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
Auxiliar aux = new Auxiliar();
ConexionBD dbREDD = new ConexionBD();

String atribucion_defecto = "Map data &copy; <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors. <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Proyectos/Projects © <a href='http://www.ideam.gov.co'>IDEAM</a> et al."; 

String wms_base = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_base'", "http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", null);
String wms_atribucion = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wms_atribucion'", atribucion_defecto, null);

String wfs_proyectos_url = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_proyectos_url'", "http://52.4.164.82:8080/geoserver/OracleAmazon/wfs", null);
String wfs_proyectos_parametro_version = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_proyectos_parametro_version'", "1.0.0", null);
String wfs_proyectos_parametro_typeName = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_proyectos_parametro_typeName'", "OracleAmazon:C_PROYECTOS", null);
String wfs_proyectos_parametro_srs = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='wfs_proyectos_parametro_srs'", "EPSG:3116", null);


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

String id_usuario = "";
if (usuario != null) {
	id_usuario = String.valueOf(usuario.getIdUsuario());
}

String nombreLicencia = "PROYECTOS REDD";


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
%>

<% out.print(doctype); %>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>

<link rel="stylesheet" href="../js/leaflet/leaflet.css" />
<script type="text/javascript" src="../js/leaflet/leaflet-src.js"></script>

<link rel='stylesheet' type='text/css' media='all' href='../js/jscalendar/calendar-green.css' title='green' /> 
<script type='text/javascript' src='../js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='../js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='../js/jscalendar/calendar-setup.js'></script>

<script type="text/javascript">

var map;

var wms_base;
var wms_atribucion;

var wfs_proyectos_url;
var wfs_proyectos_parametro_version;
var wfs_proyectos_parametro_typeName;
var wfs_proyectos_parametro_srs;

wms_base = '<%=wms_base %>';
wms_atribucion = '<%=wms_atribucion %>';

wfs_proyectos_url = '<%=wfs_proyectos_url %>';
wfs_proyectos_parametro_version = '<%=wfs_proyectos_parametro_version %>';
wfs_proyectos_parametro_typeName = '<%=wfs_proyectos_parametro_typeName %>';
wfs_proyectos_parametro_srs = '<%=wfs_proyectos_parametro_srs %>';

var str_viewparams = "";

var PRYC_NOMBRE = '';
var w_PRYC_NOMBRE = '';
var PRYC_CONSECUTIVO = '';
var w_PRYC_CONSECUTIVO = '';
var departamentos_seleccionados = ''; 
var w_departamentos_seleccionados = '';
var municipios_seleccionados = ''; 
var w_municipios_seleccionados = '';
var cars_seleccionadas = ''; 
var w_CARS = '';
var actividades_seleccionadas = ''; 
var w_ACTIVIDADES = '';
var PRYC_FECHA_INICIO = ''; 
var w_PRYC_FECHA_INICIO = '';
var PRYC_FECHA_FIN = ''; 
var w_PRYC_FECHA_FIN = '';
var PRYC_TIPO_BOSQUE = ''; 
var w_PRYC_TIPO_BOSQUE = '';
var PRYC_CONS_PAIS = '';
var w_PRYC_CONS_PAIS = '';
var PRYC_CONS_ESTADO = '';
var w_PRYC_CONS_ESTADO = '';
var PRYC_PROPIETARIO = '';
var w_PRYC_PROPIETARIO = '';


var CX = '';
var CY = '';
var S = '';
var W = '';
var N = '';
var E = '';

var WFSLayer = null;

function initializeMap() {

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
	
	map = L.map('map').setView([dCY, dCX], 5);
		
	// MAPA BASE
	L.tileLayer(wms_base, {attribution: wms_atribucion}).addTo(map);
	
	map.doubleClickZoom.disable();

}

function filtrarProyectosEnMapa(
		PRYC_NOMBRE,
		PRYC_CONSECUTIVO,
		departamentos_seleccionados,
		municipios_seleccionados,
		cars_seleccionadas,
		actividades_seleccionadas,
		PRYC_FECHA_INICIO,
		PRYC_FECHA_FIN,
		PRYC_TIPO_BOSQUE,
		PRYC_CONS_PAIS,
		PRYC_CONS_ESTADO,
		PRYC_PROPIETARIO
		) {
	str_viewparams = "";
	
	w_PRYC_NOMBRE = '';
	if (PRYC_NOMBRE.length > 0) {
		w_PRYC_NOMBRE = " AND LOWER(PRYC_NOMBRE) LIKE '%"+PRYC_NOMBRE.toLowerCase()+"%' ";
		str_viewparams += 'w_PRYC_NOMBRE:'+w_PRYC_NOMBRE+';'; 
	}
	
	w_PRYC_CONSECUTIVO = '';
	if (PRYC_CONSECUTIVO.length > 0) {
		w_PRYC_CONSECUTIVO = " AND PRYC_CONSECUTIVO IN ("+PRYC_CONSECUTIVO+") ";
		str_viewparams += 'w_PRYC_CONSECUTIVO:'+w_PRYC_CONSECUTIVO+';'; 
	}
	
	w_departamentos_seleccionados = '';
	if (departamentos_seleccionados.length > 0) {
		w_departamentos_seleccionados = " AND PRYC_CONSECUTIVO IN (SELECT DPPR_CONS_PROYECTO FROM RED_DEPTO_PROYECTO WHERE DPPR_CONS_DEPARTAM IN (" + departamentos_seleccionados + ")) ";
		str_viewparams += 'w_DEPARTAMENTOS:'+w_departamentos_seleccionados+';'; 
	}
	
	w_municipios_seleccionados = '';
	if (municipios_seleccionados.length > 0) {
		w_municipios_seleccionados = " AND PRYC_CONSECUTIVO IN (SELECT MNPR_CONS_proyecto FROM RED_MUNICIPIO_proyecto WHERE MNPR_CONS_MUNICIPIO IN (" + municipios_seleccionados + ")) ";
		str_viewparams += 'w_MUNICIPIOS:'+w_municipios_seleccionados+';'; 
	}
	
	w_CARS = '';
	if (cars_seleccionadas.length > 0) {
		w_CARS = " AND PRYC_CONSECUTIVO IN (SELECT CRPR_CONS_PROYECTO FROM RED_CAR_proyecto WHERE CRPR_CONS_CAR IN (" + cars_seleccionadas + ")) ";
		str_viewparams += 'w_CARS:'+w_CARS+';'; 
	}
	
	w_ACTIVIDADES = '';
	if (actividades_seleccionadas.length > 0) {
		w_ACTIVIDADES = " AND PRYC_CONSECUTIVO IN (SELECT ACPR_CONS_PROYECTO FROM RED_ACTIVIDAD_proyecto WHERE ACPR_CONS_ACTIVIDAD IN (" + actividades_seleccionadas + ")) ";
		str_viewparams += 'w_ACTIVIDADES:'+w_ACTIVIDADES+';'; 
	}
	
	w_PRYC_FECHA_INICIO = '';
	if (PRYC_FECHA_INICIO.length > 0) {
		w_PRYC_FECHA_INICIO = " AND TO_DATE(PRYC_FECHA_INICIO\\,\\'YYYY-MM-DD\\') >= TO_DATE(\\'"+PRYC_FECHA_INICIO+"\\'\\,\\'YYYY-MM-DD\\') ";
		str_viewparams += 'w_PRYC_FECHA_INICIO:'+w_PRYC_FECHA_INICIO+';'; 
	}
	
	w_PRYC_FECHA_FIN = '';
	if (PRYC_FECHA_FIN.length > 0) {
		w_PRYC_FECHA_FIN = " AND TO_DATE(PRYC_FECHA_FIN\\,\\'YYYY-MM-DD\\') <= TO_DATE(\\'"+PRYC_FECHA_FIN+"\\'\\,\\'YYYY-MM-DD\\') ";
		str_viewparams += 'w_PRYC_FECHA_FIN:'+w_PRYC_FECHA_FIN+';'; 
	}
	
	w_PRYC_TIPO_BOSQUE = '';
	if (PRYC_TIPO_BOSQUE.length > 0) {
		w_PRYC_TIPO_BOSQUE = " AND PRYC_TIPO_BOSQUE = "+PRYC_TIPO_BOSQUE+" ";
		str_viewparams += 'w_PRYC_TIPO_BOSQUE:'+w_PRYC_TIPO_BOSQUE+';'; 
	}
	
	w_PRYC_CONS_PAIS = '';
	if (PRYC_CONS_PAIS.length > 0) {
		w_PRYC_CONS_PAIS = " AND PRYC_CONS_PAIS = "+PRYC_CONS_PAIS+" ";
		str_viewparams += 'w_PRYC_CONS_PAIS:'+w_PRYC_CONS_PAIS+';'; 
	}
	
	w_PRYC_CONS_ESTADO = '';
	if (PRYC_CONS_ESTADO.length > 0) {
		w_PRYC_CONS_ESTADO = " AND PRYC_CONS_ESTADO = "+PRYC_CONS_ESTADO+" ";
		str_viewparams += 'w_PRYC_CONS_ESTADO:'+w_PRYC_CONS_ESTADO+';'; 
	}
	
	w_PRYC_PROPIETARIO = '';
	if (PRYC_PROPIETARIO.length > 0) {
		w_PRYC_PROPIETARIO = " AND PRYC_PROPIETARIO = "+PRYC_PROPIETARIO+" ";
		str_viewparams += 'w_PRYC_PROPIETARIO:'+w_PRYC_PROPIETARIO+';'; 
	}
	
	if (str_viewparams.length>0) {
		str_viewparams = str_viewparams.slice(0,-1);
		document.getElementById('viewparams').innerHTML = 'Filtro actual:['+str_viewparams+']';
	}

	var defaultParameters = {
		    service : 'WFS',
		    version : wfs_proyectos_parametro_version,
		    request : 'GetFeature',
		    typeName : wfs_proyectos_parametro_typeName,
		    outputFormat : 'text/javascript',
		    format_options : 'callback:getJson',
			viewparams: str_viewparams, 
		    srs : wfs_proyectos_parametro_srs
		};
		
		var parameters = L.Util.extend(defaultParameters);
		var URL = wfs_proyectos_url + L.Util.getParamString(parameters);

		if (WFSLayer != null) {
			map.removeLayer(WFSLayer);
		}
		
		var ajax = $.ajax({
		    url : URL,
		    dataType : 'jsonp',
		    jsonpCallback : 'getJson',
		    success : function (response) {
		        WFSLayer = L.geoJson(response, {
		            style: function (feature) {
		                return {
		                    stroke: true,
		                    fillColor: '#FFFFFF',
		                    fillOpacity: .5,
		                    opacity: 1,
		                    weight: 5,
		                    color: '#FF0101'
		                };
		            },
		            onEachFeature: function (feature, layer) {
		
						if (feature.properties) {                
		                	popupOptions = {
		                			width: '300',
		                			height: '100' 
		        			};

							var titulo_detalle = '<%=msj.getString("consulta.proyecto.detalle")%>';
							var titulo_nombre = '<%=msj.getString("consulta.proyecto.nombre")%>';
							var titulo_descripcion_area = '<%=msj.getString("consulta.proyecto.area")%>';
							var titulo_actividades = '<%=msj.getString("consulta.proyecto.actividades")%>';
							var titulo_tipobosque = '<%=msj.getString("consulta.proyecto.bosque")%>';
							var titulo_metodologia = '<%=msj.getString("consulta.proyecto.metodologia")%>';
							var titulo_tenencia = '<%=msj.getString("consulta.proyecto.propietario")%>';
							var titulo_estado = '<%=msj.getString("consulta.proyecto.estado")%>';
		                	
		                	layer.bindPopup(
		    	                	"<div class='etiqueta_mapa_proyecto'>"
		    	                	+ "<h4>"+titulo_detalle+"</h4>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_nombre+"</b>:" + feature.properties.PRYC_NOMBRE + "</p>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_descripcion_area+"</b>:" + feature.properties.PRYC_DES_AREA + "</p>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_actividades+"</b>:" + feature.properties.ACTIVIDADES + "</p>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_tipobosque+"</b>:" + feature.properties.TIPOBOSQUE + "</p>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_metodologia+"</b>:" + feature.properties.METODOLOGIA + "</p>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_tenencia+"</b>:" + feature.properties.USUARIO + "</p>"
		                        	+ "<p style='dato_mapa_proyecto'><b>"+titulo_estado+"</b>:" + feature.properties.ESTADO + "</p>"
		                        	+ "</div>"
		                        	, popupOptions);
						}
		            }
		        }).addTo(map);
		        map.fitBounds(WFSLayer.getBounds());
		        map.setZoom(Math.max(map.getZoom()-2, 5));
		    }
		});
		
		
	
}

</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar Proyectos</title>
</head>
<script type="text/javascript">

var id_usuario = '<%=id_usuario %>';

$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });
//   $(function() {
//     $( "#fInicial" ).datepicker({
//         dateFormat: "yy-mm-dd"
//   	}); 
//   });
//   $(function() {
//     $( "#ffin" ).datepicker({
//         dateFormat: "yy-mm-dd"
//   	});
//   });


var municipio;
var municipios = new Array();
var depto;
var deptos = new Array();
var proyectos = new Array();
var proyectos1 = new Array();
var codigos = new Array();
var titulos = new Array(11);
titulos[0] = '<%=msj.getString("consulta.proyecto.codigo")%>';
titulos[1] = '<%=msj.getString("consulta.proyecto.nombre")%>';
titulos[2] = '<%=msj.getString("consulta.proyecto.area")%>';
titulos[3] = '<%=msj.getString("consulta.proyecto.localizacion")%>';
titulos[4] = '<%=msj.getString("consulta.proyecto.pais")%>';
titulos[5] = '<%=msj.getString("consulta.proyecto.municipios")%>';
titulos[6] = '<%=msj.getString("consulta.proyecto.departamentos")%>';
titulos[7] = '<%=msj.getString("consulta.proyecto.bosque")%>';
titulos[8] = '<%=msj.getString("consulta.proyecto.cars")%>';
titulos[9] = '<%=msj.getString("consulta.proyecto.estado")%>';
titulos[10] = '<%=msj.getString("consulta.proyecto.propietario")%>';
titulos[11] = '<%=msj.getString("consulta.proyecto.fechaInicio")%>';
titulos[12] = '<%=msj.getString("consulta.proyecto.fechaFin")%>';
titulos[13] = '<%=msj.getString("consulta.proyecto.actividades")%>';


var parFiltradas = new Array();
var parce; 
var parametros;
var codigos;
var ind;
var idForm = 0;
var idActual;

$(document).ready(function () {
	var navegador = navigator.appName;
	
	if (navegador=="Microsoft Internet Explorer") {
		$("form").keypress(function(e) {
		    if (e.keyCode == 13) {
		        return false;
		    }
		});
	}
	else {
		$("form").keypress(function(e) {
			if (e.which == 13) {
			    return false;
			}
		});
	}
	
	cargarProyectos();
	idActual=1;
	resultadosContactos(proyectos.length, 'resultados','consultas');
	cargarDatos(proyectos,'consultas',3,titulos,null,0,5);
	crearPaginas(proyectos.length,'consultas',1);

	if (id_usuario != '') {
		$("#chk_solomisproyectos").prop('checked', true);
		$("#div_solomisproyectos").show();
	}
	else {
		$("#chk_solomisproyectos").prop('checked', false);
		$("#div_solomisproyectos").hide();
	}
	
	filtraParcelas();

	$("#deptoInicial").mousedown(function(e){
	    e.preventDefault();
	    var select = this;
	    var scroll = select .scrollTop;
	    e.target.selected = !e.target.selected;
	    setTimeout(function(){select.scrollTop = scroll;}, 0);
	    $(select).focus();
	   	$(select).trigger("change");
	}).mousemove(function(e){e.preventDefault()});		
		
	$("#municipioInicial").mousedown(function(e){
	    e.preventDefault();
	    var select = this;
	    var scroll = select .scrollTop;
	    e.target.selected = !e.target.selected;
	    setTimeout(function(){select.scrollTop = scroll;}, 0);
	    $(select).focus();
	   	$(select).trigger("change");
	}).mousemove(function(e){e.preventDefault()});		
		
	$("#CAR").mousedown(function(e){
	    e.preventDefault();
	    var select = this;
	    var scroll = select .scrollTop;
	    e.target.selected = !e.target.selected;
	    setTimeout(function(){select.scrollTop = scroll;}, 0);
	    $(select).focus();
	   	$(select).trigger("change");
	}).mousemove(function(e){e.preventDefault()});

	$("#actividades").mousedown(function(e){
	    e.preventDefault();
	    var select = this;
	    var scroll = select .scrollTop;
	    e.target.selected = !e.target.selected;
	    setTimeout(function(){select.scrollTop = scroll;}, 0);
	    $(select).focus();
	   	$(select).trigger("change");
	}).mousemove(function(e){e.preventDefault()});

	initializeMap();

});

function busquedaVacia(resultados, consultas){
	var cont = document.getElementById(resultados);
	var cons = document.getElementById(consultas);
	if (cons != null)
		cons.parentNode.removeChild(cons);
	cons = document.createElement('div');
	cons.id = consultas;
	cons.className = 'resultados-busqueda';
	var div= document.createElement('div');
	div.className = 'item-busqueda item-busqueda-odd';
	var span1 = document.createElement('span');
	span1.id="columna";
	span1.className= "column column1";
	var p = document.createElement('p');
	var strong = document.createElement('strong');
	strong.innerText = '<%=msj.getString("consulta.vacia")%>';
	strong.innerHTML = '<%=msj.getString("consulta.vacia")%>';
	p.appendChild(strong);
	span1.appendChild(p);
	div.appendChild(span1);
	cons.appendChild(div);
	cont.appendChild(cons);
}

	function resultadosContactos(tamanho, principal, contenedor) {
		var cont;
		var prin = document.getElementById(principal);
		cont = document.getElementById(contenedor);
		if (cont != null) {
			cont.parentNode.removeChild(cont);
		}
		cont = document.createElement('div');
		cont.id = contenedor;
		cont.className = 'resultados-busqueda';
		var results = document.createElement('h3');
		results.innerText = '<%=msj.getString("consulta.resultados1")%> ' + tamanho + ' <%=msj.getString("consulta.resultados2")%>';
		results.innerHTML = '<%=msj.getString("consulta.resultados1")%> '+ tamanho + ' <%=msj.getString("consulta.resultados2")%>';
		cont.appendChild(results);
		prin.appendChild(cont);
	}
	
	function crearPaginas(tamanho,contenedor, id) {
		var i;
		var cont = document.getElementById(contenedor);
		var paginador = document.getElementById('paginador');
		if (paginador != null) {
			paginador.parentNode.removeChild(paginador);
		}
		paginador = document.createElement('div');
		paginador.id='paginador';
		paginador.className = "pager";
		var ul = document.createElement('ul');
		var numeroHojas = Math.ceil(tamanho/5);
		
		var liInicial = document.createElement('li');
		var aInicial = document.createElement('a');
		var liAnterior = document.createElement('li');
		var aAnterior = document.createElement('a');
		var liSiguiente = document.createElement('li');
		var aSiguiente = document.createElement('a');
		var liFinal = document.createElement('li');
		var aFinal = document.createElement('a');
		
		liInicial.className="item-pager-controls item-pager-first"; 
		liInicial.onclick=function() {paginar((1)); };
		liAnterior.className="item-pager-controls item-pager-previous";
		liAnterior.onclick=function() {if(idActual>1){paginar(idActual-1);}};
		liSiguiente.className="item-pager-controls item-pager-next"; 
		liSiguiente.onclick=function() {if(idActual<numeroHojas){paginar(idActual+1);}};
		liFinal.className="item-pager-controls item-pager-last"; 
		liFinal.onclick=function() {paginar((numeroHojas)); };
		
		liInicial.appendChild(aInicial);
		liAnterior.appendChild(aAnterior);
		liSiguiente.appendChild(aSiguiente);
		liFinal.appendChild(aFinal);
		
		ul.appendChild(liInicial);
		ul.appendChild(liAnterior);
		for(i=0;i<numeroHojas;i++) {
			crearBoton(i, id, ul);
		}
		ul.appendChild(liSiguiente);
		ul.appendChild(liFinal);
		
		paginador.appendChild(ul);
		cont.appendChild(paginador);
	}
	
	function paginar(boton){
		idActual=boton;
		var indiceFinal=boton*5;
		var indiceInicial=indiceFinal-5;
		resultadosContactos(proyectos.length, 'resultados','consultas');
		cargarDatos(proyectos,'consultas',3,titulos,null,indiceInicial,indiceFinal);
		crearPaginas(proyectos.length,'consultas',boton);
	}

	function crearColumnas(numColumnas,contResults){
		var i;
		for (i=0;i<numColumnas;i++) {
			var span = document.createElement('span');
			span.id="columna"+(i+1);
			span.class= "column column"+(i+1);
			contResults.appendChild(span);
		}
	}
	
	function crearBoton(indice, id, ul){
		var li = document.createElement('li');
		if (indice==(id-1)) {
			li.className="item-pager active";
			li.innerText = indice+1;
			li.innerHTML = indice+1;
		}
		else {
			li.className="item-pager";
			var a = document.createElement('a');
			a.id=indice+1;
			a.innerText = indice+1;
			a.innerHTML = indice+1;
			a.onclick=function() {paginar((indice+1)); };
			li.appendChild(a);
		}
		ul.appendChild(li);
	}
	
	function cargarDatos(parc,contenedor, numColumnas, titulos, permisos, indiceInicial, indiceFinal){
		var i, j, conte;
		var cont = document.getElementById(contenedor);
		for (i=indiceInicial; i < parc.length; i++) {
			if(i<indiceFinal) {
				var span1, span2, span3;
				var indiceColumna=1;
				var codigoProyecto;
				var div = document.createElement('div');
				if(i%2==0)
					div.className='item-busqueda item-busqueda-odd';
				else
					div.className='item-busqueda item-busqueda-even';
				conte = parc[i].split(";");
				span1 = document.createElement('span');
				span1.id="columna"+indiceColumna;
				span1.className= "column column"+indiceColumna;
				indiceColumna++;
				span2 = document.createElement('span');
				span2.id="columna"+indiceColumna;
				span2.className= "column column"+indiceColumna;
				for (j = 0; j < conte.length; j++) {
					if (conte[j] != '' && conte[j] != 'null') {
						if (j==0) {
							codigoProyecto = conte[j];
						}
						var p = document.createElement('p');
						var strong = document.createElement('div');
						strong.innerText = "<b>" + titulos[j] + ":</b> " + conte[j];
						strong.innerHTML = "<b>" + titulos[j] + ":</b> " + conte[j];
						p.appendChild(strong);
						if (j<8) {
							span1.appendChild(p);
							div.appendChild(span1);
						}
						else {
							span2.appendChild(p);
							div.appendChild(span2);
						}
					}
				}
				
				if(numColumnas==3) {
					var k;
					indiceColumna++;
					span3 = document.createElement('span');
					span3.id="columna"+indiceColumna;
					span3.className= "column column"+indiceColumna;
	// 				for(k=0;k<permisos.length;k++){}Cuando se tengan los permisos 
					adicionarBoton('detallar', span3, 'Detallar', 1, codigoProyecto);
// 					adicionarBoton('modificar', span3, 'Modificar', 2, codigoProyecto);
// 					adicionarBoton('exportar', span3, 'Exportar', 3, codigoProyecto);
// 					adicionarBoton('individuos', span3, 'Individuos', 4, codigoProyecto);
					div.appendChild(span3);
				}
				cont.appendChild(div);
			}
		}
	}
	
	function adicionarBoton(idBoton, contenedor, valor, opc, cod) {
		var formulario = document.createElement("div");
		formulario.className = "form-actions";
		var hiddenIdParcela = document.createElement("input");
		hiddenIdParcela.type = 'hidden';
		hiddenIdParcela.id = 'idProyecto';
		hiddenIdParcela.name = 'idProyecto';
		hiddenIdParcela.value = valor;
		
		var boton = document.createElement('input');
		boton.type = "button";
		boton.id = 'boton' + idBoton;
		boton.name = 'boton' + idBoton;
		boton.className = "btn btn-default";
		boton.value = idBoton;
		if (boton.addEventListener) {
			boton.addEventListener("click", function() {
				enviar(opc,cod);
			}, true);
		} 
		else {
			boton.attachEvent('onclick', function() {
				enviar(opc,cod);
			});
		}
		formulario.appendChild(hiddenIdParcela);
		formulario.appendChild(boton);
		contenedor.appendChild(formulario);
	}

	function enviar(opc, id) {
		var idParcela = document.getElementById('proyecto_hidden');
		idParcela.value = id;
		var usuario= document.getElementById("usuario");
		usuario.value='<%if(usuario!=null) {out.print(usuario.getIdentificacion());}%>';
		var dir = document.getElementById('dir');
		dir.value = opc;
		var form = document.getElementById('consultaProyecto');
		form.submit();
	}
	

	var stringParametros;
	var indicesFiltro;
	
	function filtraParcelas() {
		var f_codigo = document.getElementById('codigo').value;
		var f_nombre = document.getElementById('nombre').value;

		PRYC_TIPO_BOSQUE = '';
		var f_tipoBosque = document.getElementById('tipoBosque').value;
		if (f_tipoBosque == '-1') PRYC_TIPO_BOSQUE = '';
		else PRYC_TIPO_BOSQUE = f_tipoBosque;

		PRYC_CONS_PAIS = '';
		var f_pais = document.getElementById('pais').value;
		if (f_pais == '-1') PRYC_CONS_PAIS = '';
		else PRYC_CONS_PAIS = f_pais;
		
		PRYC_CONS_ESTADO = '';
		var f_estado = document.getElementById('estado').value;
		if (f_estado == '-1') PRYC_CONS_ESTADO = '';
		else PRYC_CONS_ESTADO = f_estado;
		
		var id_usuario = '<%=id_usuario %>';
		
		var f_fInicial = document.getElementById('fInicial').value.replaceAll("-", "");
		var f_ffin = document.getElementById('ffin').value.replaceAll("-", "");
		
		departamentos_seleccionados = '';
		var a_departamentos = new Array();
		var f_deptoInicial = document.getElementById('deptoInicial');
		var j=0;
		for (var i=0; i<f_deptoInicial.options.length; i++) {
			if (f_deptoInicial.options[i].selected) {
				a_departamentos.push(f_deptoInicial.options[i].value);
				if (j > 0) departamentos_seleccionados += '\\,';
				departamentos_seleccionados += f_deptoInicial.options[i].value;
				j++;
			}
		}

		municipios_seleccionados = '';
		var a_municipios = new Array();
		var f_municipioInicial = document.getElementById('municipioInicial');
		var j=0;
		for (var i=0; i<f_municipioInicial.options.length; i++) {
			if (f_municipioInicial.options[i].selected) {
				a_municipios.push(f_municipioInicial.options[i].value);
				if (j > 0) municipios_seleccionados += '\\,';
				municipios_seleccionados += f_municipioInicial.options[i].value;
				j++;
			}
		}

		cars_seleccionadas = '';
		var a_car = new Array();
		var f_CAR = document.getElementById('CAR');
		var j=0;
		for (var i=0; i<f_CAR.options.length; i++) {
			if (f_CAR.options[i].selected) {
				a_car.push(f_CAR.options[i].value);
				if (j > 0) cars_seleccionadas += '\\,';
				cars_seleccionadas += f_CAR.options[i].value;
				j++;
			}
		}

		actividades_seleccionadas = '';
		var f_a_ids_actividades = new Array();
		var f_actividades = document.getElementById('actividades');
		var j=0;
		for (var i=0; i<f_actividades.options.length; i++) {
			if (f_actividades.options[i].selected) {
				f_a_ids_actividades.push(f_actividades.options[i].value);
				if (j > 0) actividades_seleccionadas += '\\,';
				actividades_seleccionadas += f_actividades.options[i].value;
				j++;
			}
		}

		filtrarProyectosEnMapa(
				f_nombre,
				f_codigo,
				departamentos_seleccionados,
				municipios_seleccionados,
				cars_seleccionadas,
				actividades_seleccionadas,
				document.getElementById('fInicial').value,
				document.getElementById('ffin').value,
				PRYC_TIPO_BOSQUE,
				PRYC_CONS_PAIS,
				PRYC_CONS_ESTADO,
				id_usuario
				);				

		
		parFiltradas.length = 0;
		parce = new Array();
		var control;
		parametros = new Array();
		codi = new Array();
		ind = 0;

		stringParametros="";
		indicesFiltro="";
		
		var cod = document.getElementById('codigo');
		obtenerParametros(cod);
		
		var nom = document.getElementById('nombre');
		obtenerParametros(nom);

		stringParametros=stringParametros+"-1;-1;";

		var pais = document.getElementById('pais');
		obtenerParametrosSelect(pais);

		var mun = document.getElementById('municipioInicial');
		obtenerParametrosSelect(mun);

		var dep = document.getElementById('deptoInicial');
		obtenerParametrosSelect(dep);
		
		var tibo = document.getElementById('tipoBosque');
		obtenerParametrosSelect(tibo);

		var car = document.getElementById('CAR');
		obtenerParametrosSelect(car);
		
		var est = document.getElementById('estado');
		
		if (id_usuario != '') {
			if (est.value == '-1') {
				est.value = '1';
			}
			stringParametros+=est.value+';'
		}
		else {
			obtenerParametrosSelect(est);
		}

		if (id_usuario != '') {
			stringParametros+=id_usuario+';'
		}
		else {
			stringParametros=stringParametros+"-1;";
		}
		
		var fin = document.getElementById('fInicial');
		obtenerParametros(fin);
		
		var ffi = document.getElementById('ffin');
		stringParametros=stringParametros+ffi.value;


		if (fin.value != '' && ffi.value != '') {
			if (fin.value.trim() > ffi.value.trim()) {
				alert('La fecha inicial no puede ser posterior a la final.');
				return;
			}
		}
		
  		var act = document.getElementById('actividades');
  		obtenerParametrosSelect(act);

		indicesFiltro=obtenerIndicesFiltro();

		if(indicesFiltro != "") {
			parFiltradas=filtro(proyectos,codigos, indicesFiltro, stringParametros);
			
		 	if(parFiltradas.length!=0) {//cambiar para los filtros 
				resultadosContactos(parFiltradas.length, 'resultados','consultas');
				cargarDatos(parFiltradas,'consultas',3,titulos,null,0,5);
				crearPaginas(parFiltradas.length,'consultas',1);
		 	}
		 	else {
		 		busquedaVacia('resultados','consultas');
		 	}
		}
		else {
			resultadosContactos(proyectos.length, 'resultados','consultas');
			cargarDatos(proyectos,'consultas',3,titulos,null,0,5);
			crearPaginas(proyectos.length,'consultas',1);
		}
		
		//alert("Filtro cargado.");
	}

	function obtenerIndicesFiltro(){
		var result="";
		var auxParam=stringParametros.split(";");
		var i;
		
		for (i=0;i<auxParam.length;i++) {
			if (auxParam[i] != "" && auxParam[i] !="-1") {
				result=result+i+";";
			}
		}
		result=result.substring(0,result.length-1);
		return result.split(";");
	}

	function obtenerParametros(obj) {
		stringParametros=stringParametros+obj.value+";";
	}

	function obtenerParametrosSelect(obj) {
		var i;
		if (obj) {
			for (i = 0; i < obj.options.length; i++) {
				if (obj.options[i].selected) {
					stringParametros = stringParametros+obj.options[i].value+";";
				}
			}
		}
	}

	function filtroFechas(fechaMenor, fechaMayor) {
		var fi = fechaMenor.substring(0,10).split("-");
		var ff = fechaMayor.substring(0,10).split("-");
		var resultado=0;
		var myDate1 = new Date(fi[0], fi[1], fi[2]);
		var myDate2 = new Date(ff[0], ff[1], ff[2]);
			
		if (myDate1<=myDate2) {
			resultado = 1;
		}
		
		return resultado;
	}

	function filtro(proyectos, codigos) {
		var proyectos_filtrados = new Array();
		
		var a_proyecto;
		var a_codigo;
		
		var pryc_consecutivo; 
		var pryc_nombre; 
		var pryc_area; 
		var forma; 
		var pryc_cons_pais; 
		var nombre_pais; 
		var nombres_departamentos; 
		var ids_departamentos; 
		var nombres_actividades; 
		var ids_actividades; 
		var ids_municipios; 
		var nombres_municipios; 
		var tpbs_descripcion; 
		var id_tipobosque; 
		var nombres_cars; 
		var ids_cars; 
		var espr_nombre; 
		var pryc_cons_estado; 
		var usr_nombre; 
		var usr_consecutivo; 
		var pryc_fecha_inicio; 
		var pryc_fecha_fin; 

		var f_codigo = document.getElementById('codigo').value;
		var f_nombre = document.getElementById('nombre').value;
		var f_tipoBosque = document.getElementById('tipoBosque').value;
		var f_pais = document.getElementById('pais').value;
		var f_estado = document.getElementById('estado').value;
		var id_usuario = '<%=id_usuario %>';
		var f_fInicial = document.getElementById('fInicial').value.replaceAll("-", "");
		var f_ffin = document.getElementById('ffin').value.replaceAll("-", "");
		
		departamentos_seleccionados = '';
		var a_departamentos = new Array();
		var f_deptoInicial = document.getElementById('deptoInicial');
		for (var i=0; i<f_deptoInicial.options.length; i++) {
			if (f_deptoInicial.options[i].selected) {
				a_departamentos.push(f_deptoInicial.options[i].value);
				if (i > 0) departamentos_seleccionados += ',';
				departamentos_seleccionados += f_deptoInicial.options[i].value;
			}
		}

		municipios_seleccionados = '';
		var a_municipios = new Array();
		var f_municipioInicial = document.getElementById('municipioInicial');
		for (var i=0; i<f_municipioInicial.options.length; i++) {
			if (f_municipioInicial.options[i].selected) {
				a_municipios.push(f_municipioInicial.options[i].value);
				if (i > 0) municipios_seleccionados += ',';
				municipios_seleccionados += f_municipioInicial.options[i].value;
			}
		}

		cars_seleccionadas = '';
		var a_car = new Array();
		var f_CAR = document.getElementById('CAR');
		for (var i=0; i<f_CAR.options.length; i++) {
			if (f_CAR.options[i].selected) {
				a_car.push(f_CAR.options[i].value);
				if (i > 0) cars_seleccionadas += ',';
				cars_seleccionadas += f_CAR.options[i].value;
			}
		}

		actividades_seleccionadas = '';
		var f_a_ids_actividades = new Array();
		var f_actividades = document.getElementById('actividades');
		for (var i=0; i<f_actividades.options.length; i++) {
			if (f_actividades.options[i].selected) {
				f_a_ids_actividades.push(f_actividades.options[i].value);
				if (i > 0) actividades_seleccionadas += ',';
				actividades_seleccionadas += f_actividades.options[i].value;
			}
		}

		var a_ids_departamentos = new Array();
		var ids_departamentos = ""; 
		var a_ids_municipios = new Array();
		var ids_municipios = ""; 
		var a_ids_cars = new Array();
		var ids_cars = ""; 
		var a_ids_actividades = new Array();
		var ids_actividades = ""; 

		var k = 0;

		var incluir = true;

		loop_proyectos:
		for (var p=0; p<proyectos.length; p++) {
			incluir = true;
			
			a_proyecto = proyectos[p].split(";");
			
			pryc_consecutivo = a_proyecto[0]; 
			pryc_nombre = a_proyecto[1]; 
			pryc_area = a_proyecto[2]; 
			forma = a_proyecto[3]; 
			nombre_pais = a_proyecto[4]; 
			nombres_municipios = a_proyecto[5]; 
			nombres_departamentos = a_proyecto[6]; 
			tpbs_descripcion = a_proyecto[7]; 
			nombres_cars = a_proyecto[8]; 
			espr_nombre = a_proyecto[9]; 
			usr_nombre = a_proyecto[10]; 
			pryc_fecha_inicio = a_proyecto[11].replaceAll("-", ""); 
			pryc_fecha_fin = a_proyecto[12].replaceAll("-", "");
			nombres_actividades = a_proyecto[13]; 

			a_codigo = codigos[p].split(";");

			pryc_cons_pais = a_codigo[4]; 

			var ids_municipios = a_codigo[5];
			a_ids_municipios = ids_municipios.split("-=-");

			var ids_departamentos = a_codigo[6];
			a_ids_departamentos = ids_departamentos.split("-=-");

			id_tipobosque = a_codigo[7]; 

			var ids_cars = a_codigo[8];
			a_ids_cars = ids_cars.split("-=-");
			
			pryc_cons_estado = a_codigo[9]; 
			usr_consecutivo = a_codigo[10];

			var ids_actividades = a_codigo[13];
			a_ids_actividades = ids_actividades.split("-=-");


			// FILTRAR

			
			// FILTRAR POR CÓDIGO
			if (f_codigo != '') {
				if (f_codigo == pryc_consecutivo) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}
				
			// FILTRAR POR NOMBRE
			if (f_nombre != '') {
				if (pryc_nombre.toLowerCase().indexOf(f_nombre.toLowerCase()) != -1) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}
			
			// FILTRAR POR TIPO DE BOSQUE
			if (f_tipoBosque != '' && f_tipoBosque != '-1') {
				if (f_tipoBosque == id_tipobosque) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}
						
			// FILTRAR POR PAIS
			if (f_pais != '' && f_pais != '-1') {
				if (f_pais == pryc_cons_pais) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}
			
			// FILTRAR POR ESTADO
			if (f_estado != '' && f_estado != '-1') {
				if (f_estado == pryc_cons_estado) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}
			
			// FILTRAR POR ID DE USUARIO
			var chk_solomisproyectos = document.getElementById('chk_solomisproyectos');
			
			if (chk_solomisproyectos.checked) {
				if (id_usuario != '' && usr_consecutivo != '-1') {
					if (id_usuario == usr_consecutivo) {
						incluir = incluir && true;
					}
					else {
						incluir = false;
					}
				}
			}
			else {
				incluir = incluir && true;
			}
			
			// FILTRAR POR FECHA DE INICIO
			if (f_fInicial != '') {
				if (f_fInicial * 1.0 <= pryc_fecha_inicio * 1.0) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}
			
			// FILTRAR POR FECHA DE FINALIZACION
			if (f_ffin != '') {
				if (f_ffin * 1.0 >= pryc_fecha_fin * 1.0) {
					incluir = incluir && true;
				}
				else {
					incluir = false;
				}
			}

			// FILTRAR POR DEPARTAMENTO
			loop_f_a_ids_departamentos:
			for (var i=0; i<f_deptoInicial.options.length; i++) {
				if (f_deptoInicial.options[i].selected) {
					loop_a_ids_departamentos:
					for (var j=0; j<a_ids_departamentos.length; j++) {
						var id_departamento_buscada = f_deptoInicial.options[i].value;
						var id_departamento_encontrada = a_ids_departamentos[j];  
						if (id_departamento_encontrada == id_departamento_buscada) {
							incluir = incluir && true;
							break loop_f_a_ids_departamentos;
						}
						else {
							incluir = false;
						}
					}
				}
			}

			// FILTRAR POR municipio
			loop_f_a_ids_municipios:
			for (var i=0; i<f_municipioInicial.options.length; i++) {
				if (f_municipioInicial.options[i].selected) {
					loop_a_ids_municipios:
					for (var j=0; j<a_ids_municipios.length; j++) {
						var id_municipio_buscada = f_municipioInicial.options[i].value;
						var id_municipio_encontrada = a_ids_municipios[j];  
						if (id_municipio_encontrada == id_municipio_buscada) {
							incluir = incluir && true;
							break loop_f_a_ids_municipios;
						}
						else {
							incluir = false;
						}
					}
				}
			}

			// FILTRAR POR car
			loop_f_a_ids_cars:
			for (var i=0; i<f_CAR.options.length; i++) {
				if (f_CAR.options[i].selected) {
					loop_a_ids_cars:
					for (var j=0; j<a_ids_cars.length; j++) {
						var id_car_buscada = f_CAR.options[i].value;
						var id_car_encontrada = a_ids_cars[j];  
						if (id_car_encontrada == id_car_buscada) {
							incluir = incluir && true;
							break loop_f_a_ids_cars;
						}
						else {
							incluir = false;
						}
					}
				}
			}

			// FILTRAR POR TIPO DE ACTIVIDAD REDD
			loop_f_a_ids_actividades:
			for (var i=0; i<f_actividades.options.length; i++) {
				if (f_actividades.options[i].selected) {
					loop_a_ids_actividades:
					for (var j=0; j<a_ids_actividades.length; j++) {
						var id_actividad_buscada = f_actividades.options[i].value;
						var id_actividad_encontrada = a_ids_actividades[j];  
						if (id_actividad_encontrada == id_actividad_buscada) {
							incluir = incluir && true;
							break loop_f_a_ids_actividades;
						}
						else {
							incluir = false;
						}
					}
				}
			}

			
			if (incluir) {
				if (!estaEn(proyectos_filtrados, proyectos[p])) {
					proyectos_filtrados[k] = proyectos[p]; k++;
				}
			}
			
		}

		return proyectos_filtrados;
		
	}

	String.prototype.replaceAll = function(search, replacement) {
	    var target = this;
	    return target.split(search).join(replacement);
	};

	function estaEn(a, v) {
		for (var i=0; i<a.length; i++) {
			if (v == a[i]) {
				return true;
			} 
		}
		return false;
	}
	
	function filtro_malo(proyectos,codigos, indicesFiltro, stringParametros) {
		var i,j,indice=0;
		var verdaderos=0;
		var auxProyecto;
		var resultados=new Array();
		var result=0;
		for (i = 0; i < codigos.length; i++) {
			
			auxProyecto=codigos[i].split(";");
			
			for(j=0;j<indicesFiltro.length;j++)
			{
				var val1=auxProyecto[indicesFiltro[j]].toLowerCase();
				var val2=stringParametros.split(";")[indicesFiltro[j]].toLowerCase();

				if(indicesFiltro[j]>=11 && indicesFiltro[j]<=12)
				{
					if(indicesFiltro[j]==11)
					{
						result=filtroFechas(val2,val1);
						if(result==1)
						{
							verdaderos++;
						}
					}
					if(indicesFiltro[j]==12)
					{
						result=filtroFechas(val1,val2);
						if(result==1)
						{
							verdaderos++;
						}
					}
					
				}else{
					if(val1==val2)
					{
						verdaderos++;
					}
				}
			}
			
			if(verdaderos==indicesFiltro.length)
			{
				resultados[indice]=proyectos[i];
				indice++;
			}
			verdaderos=0;
		}

		return resultados;
	}

	function cargarMunicipiosDepto() {
		var txtSelectedValuesObj = document.getElementById('municipioInicial');
		var selectedArray = new Array();
		var selObj = document.getElementById('deptoInicial');
		var i, j, m = 0;
		municipios = cargarMunicipios();
		if (txtSelectedValuesObj.options.length != 0) {
			while (m != txtSelectedValuesObj.options.length) {
				txtSelectedValuesObj.remove(m);
			}
			/*
			var option = document.createElement("option");
			option.text = "Seleccione";
			option.value= "-1";
			txtSelectedValuesObj.add(option);
			*/
		}
		for (i = 0; i < selObj.options.length; i++) {
			for (j = 0; j < municipios.length; j++) {
				var m = municipios[j].split(",");
				if (m[0].length < 5)
					var d = m[0].substring(0, 1);
				else
					var d = m[0].substring(0, 2);
				if (selObj.options[i].selected)
					if (d == selObj.options[i].value) {
						opcion = document.createElement('option');
						texto = document.createTextNode(m[1] + ' (' +selObj.options[i].text + ')');
						opcion.appendChild(texto);
						opcion.id='muni'+m[0];
						opcion.value = m[0];
						txtSelectedValuesObj.appendChild(opcion);
					}
			}
		}
	}
	
	function cargarProyectos() {
	<%
		CargaInicialDatosProyectos.cargarProyectoConsulta();
		ArrayList<String> a = CargaInicialDatosProyectos.getProyectos();
		for (int i = 0; i < a.size(); i++) {%>
			proyectos[<%=i%>] = '<%=a.get(i)%>';
			codigos[<%=i%>] = '<%=CargaInicialDatosProyectos.getCodigos().get(i)%>';
		<%}%>
	}

	function cargarMunicipios() {
		<%
		ArrayList<Municipios> municipios = CargaInicialDatosProyectos.getArrayMunicipios();
		for (int i = 0; i < municipios.size(); i++) {
			Municipios municipio = (Municipios) municipios.get(i);
			String m = municipio.getConsecutivo() + "," + municipio.getNombre() + "," + municipio.getDepartamento();%>     
			municipio = '<%=m%>';
			municipios[<%=i%>]=municipio;
		<%}%>
		return municipios;
	} 

	function cargarDepto(){
	<%
		ArrayList<Depto> d1 = CargaInicialDatosProyectos.getArrayDeptos();
		String de="";
		for (int i = 0; i < d1.size(); i++) {
			Depto depto1 = (Depto) d1.get(i);
			de = depto1.getConsecutivo() + "," + depto1.getNombre();%>	
			depto = '<%=de%>';
			deptos[<%=i%>] = depto;
		<%}%>
		return deptos;
	}

	
</script>
<body class='sidebar-first front'>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form method="post" action="/MonitoreoBC-WEB/redireccion"
		id="consultaProyecto">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<input type="hidden" name="proyecto_hidden" id="proyecto_hidden" /> 
		<input type="hidden" name="dir" id="dir" />
		<input type="hidden" name="usuario" id="usuario" />
	</form>
	
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page">
				
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja"><%=msj.getString("consultaproyecto.1")%></h2>
							
							<div id="content">
								<div class="content-inner">
									
									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
											
												<h3><%=msj.getString("formulario_de_consulta")%></h3>
												
												<div id="block-datos-basicos">
													<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.codigo")%></label>
															<input type="text" class="form-control" id="codigo"
																name="codigo" >
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.nombre")%></label>
															<input type="text" class="form-control" id="nombre"
																name="nombre">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.bosque")%></label>
															<div class="select-wrapper">
																<select id="tipoBosque" name="tipoBosque">
																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaInicialDatosProyectos.getTipoBosque()%>
																</select>
															</div> 
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.pais")%></label>
															<div class="select-wrapper">
																<select name="pais" id="pais"
																	title="País donde figura registrada la parcela"
																	onchange="cargarDepto(deptos,deptoInicial);eliminarlista(municipioInicial);eliminarlista(municipioFinal);">
																	<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaInicialDatosProyectos.getPaises()%>
																</select>
															</div>
														</div>												
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.depto")%></label>
															<div class="select-wrapper">
																<select multiple name="deptoInicial" id="deptoInicial"
																	onchange="cargarMunicipiosDepto()"
																	title="Departamento(s) donde se encuentra ubicada la parcela">
																	<%=CargaDatosSelect.getDepartamentos()%>
																</select>
															</div>
														</div>
														
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.mcipio")%></label>
															<div class="select-wrapper">
																<select multiple name="municipioInicial" id="municipioInicial"
																	title="Municipios disponibles para la busqueda">
																</select>
															</div>
														</div>
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.car")%></label>
															<div class="select-wrapper">
																<select multiple name="CAR" id="CAR">
																	<%=CargaInicialDatosProyectos.getCAR()%>
																</select>
															</div>
														</div>
 														<div class="form-group"> 
 															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.actividades")%></label> 
 															<div class="select-wrapper"> 
 																<select multiple name="actividades" id="actividades"> 
 																	<!-- <option value="-1" selected="selected"><%--//msj.getString("crea.noticias.seleccion")--%></option> --> 
 																	<%=CargaInicialDatosProyectos.getActividad()%> 
 																</select> 
 															</div> 
 														</div> 
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.estado")%></label>
															<div class="select-wrapper">
																<select name="estado" id="estado">
																<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaInicialDatosProyectos.getEstado()%>
																</select>
															</div>
														</div>
														<div id="div_solomisproyectos" style="display: none;" class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.solomisproyectos")%></label>
															<input type="checkbox" name="chk_solomisproyectos" id="chk_solomisproyectos" unchecked>
														</div>
														
														<h4 class="item-clear-both"><%=msj.getString("rango_fechas_iniciacion") %></h4>
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaInicio") %>:</label>
															<input type="text" id="fInicial" name="fInicial">
															<script type='text/javascript'>
															Calendar.setup({
																inputField     :    'fInicial',				// id of the input field
																ifFormat       :    '%Y-%m-%d',	// format of the input field
																showsTime      :    false,					// will display a time selector
																button         :    'fInicial',				// trigger for the calendar (button ID)
																singleClick    :    false,					// double-click mode
																step           :    1						// show all years in drop-down boxes (instead of every other year as default)
															});
															</script>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaFin")%>:</label> 
															<input type="text" id="ffin" name="ffin">
															<script type='text/javascript'>
															Calendar.setup({
																inputField     :    'ffin',				// id of the input field
																ifFormat       :    '%Y-%m-%d',	// format of the input field
																showsTime      :    false,					// will display a time selector
																button         :    'ffin',				// trigger for the calendar (button ID)
																singleClick    :    false,					// double-click mode
																step           :    1						// show all years in drop-down boxes (instead of every other year as default)
															});
															</script>
														</div>
														
														<div class="form-actions">
															<input type="button"
																value="<%=msj.getString("consulta.proyecto.filtro")%>"
																onclick="filtraParcelas();">
															<input type="button" value="<%=msj.getString("sh_consulta.proyecto.regresar")%>"
																	onclick="javascript:history.back()">
																<%	if(usuario != null) {
																		if(ControlPermisos.tienePermiso(diccionarioPermisos, 8)) {
																			if (!ConsultaWebUsuario.usuarioAceptoLicencia(Integer.parseInt(id_usuario), nombreLicencia)) {
																				%>
																				<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=msj.getString("nota_licencia_descarga_recurso_prefijo")%> "<%=nombreLicencia %>" <%=msj.getString("nota_licencia_descarga_recurso_mediofijo")%> <a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp"><%=msj.getString("nota_licencia_descarga_recurso_postfijo")%></a></div>
																				<%
																			}
																			else {
																	%>
															<input type="button" value="Exportar"
																	onclick="window.location='/MonitoreoBC-WEB/proyectos/exportarProyecto.jsp';">
																	<%
																			}
																		}
																			
																	}
																	%>
														</div>
													</div>
						
													<h3 class="titulo_naranja"><%=msj.getString("consulta.proyecto.mapa") %></h3>
													
													<div class="map_container">
														<div id="map" class="mapa_proyectos"></div>
													</div>
																						
													<div id="viewparams" style="color: #999; width: 100%; height: 40px; border: 1px solid white; display: none;"></div>

													<h3 class="titulo_naranja"><%=msj.getString("registros_encontrados") %></h3>

													<div id="resultados">
														<div id="consultas"></div>
														<div id="paginador" class="pager"></div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
		
								<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>			
														
							</div>
						</div>
							</div>
							
					
					<%=UI.getFooter(msj) %>									
					
				</div>
			</div>
			</div>
	</form>
	
</body>
</html>