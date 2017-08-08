<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="co.gov.ideamredd.admif.Auxiliar" %>
<%
//co.gov.ideamredd.admif.Auxiliar aux = new co.gov.ideamredd.admif.Auxiliar();
LenguajeI18N L = new LenguajeI18N();
ResourceBundle msj = null;
String yo = "busqueda_parcelas.";

String idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "es");

/*
String usuario = Auxiliar.nz(request.getParameter("usuario"), "");
String idioma = Auxiliar.nz(request.getParameter("idioma"), "es");
*/
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
%>

<% out.print(doctype); %>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<title>
<% try { out.print(msj.getString(yo+"Encontrar_Parcelas")); } catch (MissingResourceException e) { out.print("Encontrar Individuos"); } %>
</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<link rel='stylesheet' type='text/css' media='all' href='js/jscalendar/calendar-win2k-cold-1.css' title='win2k-cold-1' />
 
<script type='text/javascript' src='js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='js/jscalendar/calendar-setup.js'></script>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type='text/javascript' src='js/ajaxLoader.js'></script>
<script type='text/javascript' src='js/auxiliares.js'></script>

<script src="js/smartpaginator/smartpaginator.js" type="text/javascript"></script>
<link href="js/smartpaginator/smartpaginator.css" rel="stylesheet" type="text/css" />


<script type="text/javascript">

$(document).ready(function() {
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-0) + 'px';
    <% } %>
});

window.onresize = function(event) {
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-0) + 'px';
    <% } %>
};

function crearAJAX()
{
   var AJAX=null;
   if (window.XMLHttpRequest) 
   { 
       AJAX = new XMLHttpRequest();
   } 
   else if (window.ActiveXObject) 
   { 
       AJAX = new ActiveXObject("Microsoft.XMLHTTP");
   }
   return AJAX;
}

var ajax = crearAJAX();
var ocupado = false;

var timerID = 0;
var tStart  = null;
var countdown = 2;
var tiempo_respuesta = 0.0;

function esNumero(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
}
	
function redondear(valor,posiciones){
	var resultado = Math.round(valor*Math.pow(10,posiciones))/Math.pow(10,posiciones);
	return resultado;
}

function puntear()
{
	document.getElementById('resultados').innerHTML += " . ";
}

function UpdateTimer() 
{
	if (tStart == null) return;
	 
	if(timerID) 
	{
		clearTimeout(timerID);
		clockID = 0;
	}
	  
	if(!tStart)
	{
		tStart = new Date();
	}
 
	var tDate = new Date();
	var tDiff = tDate.getTime() - tStart.getTime();
	tDate.setTime(tDiff);
 
	if (countdown == 0) 
	{
		tStart = null;
		puntear();
		Start();
	}

	countdown--;
	tiempo_respuesta = redondear(tiempo_respuesta + 0.1, 1);
	
	document.getElementById('tiempo_respuesta').innerHTML = '<% try { out.print(msj.getString("General.Tiempo_de_Respuesta")); } catch (MissingResourceException e) { out.print("Tiempo de Respuesta" + ".."); } %>: ' + (tiempo_respuesta * 1.0) + ' s.';
	timerID = setTimeout('UpdateTimer()', 100);
}

function Start() 
{
	tStart = new Date();
	Reset();
	timerID = setTimeout('UpdateTimer()', 100);
}

function Stop() 
{
	tiempo_respuesta = 0.0;
	if(timerID) 
	{
		clearTimeout(timerID);
		timerID = 1;
	}
	tStart = null;
}

function Reset() 
{
  countdown = 2;
}

function exportar(f) 
{
	var INDV_ID_IMPORTACION = f.INDV_ID_IMPORTACION.value;
	var f_desde = f.f_desde.value;
	var f_hasta = f.f_hasta.value;
	var f_especie = f.especie.value;

	//if (INDV_ID_IMPORTACION.length==0 && f_desde.length==0 && f_hasta==0 && f_especie==0) return;
	
	if (INDV_ID_IMPORTACION.length>0) if (!esNumero(INDV_ID_IMPORTACION)) return;
	//if (f_especie.length>0) if (f_especie.length < 3) return;
		
	if (INDV_ID_IMPORTACION == 'null' || INDV_ID_IMPORTACION == undefined) INDV_ID_IMPORTACION = "";
	if (f_desde == 'null' || f_desde == undefined) f_desde = "";
	if (f_hasta == 'null' || f_hasta == undefined) f_hasta = "";
	if (f_especie == 'null' || f_especie == undefined) f_especie = "";

	var url;
	
	url = 'Individuo?accion=exportar_parcelas&INDV_ID_IMPORTACION=' + INDV_ID_IMPORTACION + '&f_desde=' + f_desde + '&f_hasta=' + f_hasta + '&f_especie=' + f_especie;
	window.open(url);
}

function concatenarOpciones(s) {
	var c = '';
	var s = document.getElementById(s); 
	
	if (s) {
		var i=0;

		for (i=0; i < s.options.length; i++) {
			if (s.options[i].selected) {
				c += s.options[i].value + ','; 
			}
		}
		
		if (i>0) {
			c = c.slice(0, -1);
		}
	}
		
	return c;
}

function buscar(f, exportar) 
{
	/*
	seleccionarTodasLasOpciones('departamentos_seleccionados');
	seleccionarTodasLasOpciones('municipios_seleccionados');
	*/
	
	var PRCL_NOMBRE = f.PRCL_NOMBRE.value;
	var PRCL_CONSECUTIVO = f.PRCL_CONSECUTIVO.value;
	var PRCL_PLOT = f.PRCL_PLOT.value;
	var PRCL_FECHAFIN_APROXIMACION = f.PRCL_FECHAFIN_APROXIMACION.value;
	var PRCL_CONS_PAIS = f.PRCL_CONS_PAIS.value;

		
	if (ocupado && false)
	{
		alert('Por favor espere, estoy procesando su solicitud...');
		return;
	}
	ocupado = true;
	
	if (PRCL_CONSECUTIVO == 'null' || PRCL_CONSECUTIVO == undefined) PRCL_CONSECUTIVO = "";
	if (PRCL_PLOT == 'null' || PRCL_PLOT == undefined) PRCL_PLOT = "";
	if (PRCL_NOMBRE == 'null' || PRCL_NOMBRE == undefined) PRCL_NOMBRE = "";
	if (PRCL_FECHAFIN_APROXIMACION == 'null' || PRCL_FECHAFIN_APROXIMACION == undefined) PRCL_FECHAFIN_APROXIMACION = "";
	if (PRCL_CONS_PAIS == 'null' || PRCL_CONS_PAIS == undefined) PRCL_CONS_PAIS = "";

	var departamentos_seleccionados = concatenarOpciones('departamentos_seleccionados');
	var municipios_seleccionados = concatenarOpciones('municipios_seleccionados');

	/*
	if (PRCL_NOMBRE.length==0 && PRCL_CONSECUTIVO.length==0 && PRCL_PLOT.length==0 && PRCL_FECHAFIN_APROXIMACION==0 && PRCL_CONS_PAIS.length==0 && departamentos_seleccionados.length==0 && municipios_seleccionados==0) {
		var url_visor = 'visor_parcelas_leaflet.jsp';
	    document.getElementById('iframe_visor_parcelas').src = url_visor;
	    document.getElementById('resultados').innerHTML = '';
	    return;
	}
	*/
	
	if (PRCL_CONSECUTIVO.length>0) if (!esNumero(PRCL_CONSECUTIVO)) return;
	//if (PRCL_NOMBRE.length>0) if (PRCL_NOMBRE.length < 3) return;
	
	var url;

	var accion = "";

	if (exportar) {
		accion = "exportar_parcelas";
	}
	else {
		accion = "encontrar";
	}

	var idioma = f.idioma.value;

	var parametros_visor = 'PRCL_CONSECUTIVO=' + PRCL_CONSECUTIVO + '&PRCL_PLOT=' + PRCL_PLOT + '&PRCL_NOMBRE=' + PRCL_NOMBRE + '&PRCL_FECHAFIN_APROXIMACION=' + PRCL_FECHAFIN_APROXIMACION + "&PRCL_CONS_PAIS=" + PRCL_CONS_PAIS + "&departamentos_seleccionados=" + departamentos_seleccionados + "&municipios_seleccionados=" + municipios_seleccionados;
	
	url = 'Parcela?accion=' + accion + '&PRCL_CONSECUTIVO=' + PRCL_CONSECUTIVO + '&PRCL_PLOT=' + PRCL_PLOT + '&PRCL_NOMBRE=' + PRCL_NOMBRE + '&PRCL_FECHAFIN_APROXIMACION=' + PRCL_FECHAFIN_APROXIMACION + "&PRCL_CONS_PAIS=" + PRCL_CONS_PAIS + "&departamentos_seleccionados=" + departamentos_seleccionados + "&municipios_seleccionados=" + municipios_seleccionados;

	if (exportar) {
		window.open(url);
	}
	else {
	    //document.getElementById('resultados').innerHTML = "Obteniendo registros (" + url + "). Un momento por favor...";
	    //alert(url);
	    //ajax.open('post', url);
	    ajax.open('get', url);
	    ajax.onreadystatechange = listarRegistros;
	    ajax.send(null);
	    Start();

	    var url_visor = 'visor_parcelas_leaflet.jsp?'+parametros_visor;
	    document.getElementById('iframe_visor_parcelas').src = url_visor;
	    document.getElementById('div_url_visor').innerHTML = url_visor; 
	}
}

function listarRegistros() 
{
    if(ajax.readyState == 4)
    {
        var response = ajax.responseText;
        var a_response = response.split('==!!==');
        var resultados = a_response[0];
        var n_resultados = a_response[1];
        
        document.getElementById('resultados').innerHTML = resultados;

        $('#paginas').smartpaginator({ 
            totalrecords: n_resultados, 
            recordsperpage: 3, 
            datacontainer: 'contenedor-resultados',
            dataelement: 'span',
            length: 3, 
            next: '>', 
            prev: '<', 
            first: '<<', 
            last: '>>', 
            go: '>X',
            theme: 'black', 
            controlsalways: true, 
            onchange: function (newPage) {
            	$('#r').html('Page # ' + newPage);
            }
        });
        
	    ocupado = false;
    	Stop();

    	document.f.reset();
    }
    else
    {	
        document.getElementById('resultados').innerHTML += " - ";
        ocupado = true;
	    Start();
    }
}

function validar(f)
{
	return true;
}

function someter(f)
{
	var f = f;

	seleccionarTodasLasOpciones('departamentos_seleccionados');
	seleccionarTodasLasOpciones('municipios_seleccionados');

    if (validar(f))
    {
    	f.submit();
    }

    deseleccionarTodasLasOpciones('departamentos_seleccionados');
    deseleccionarTodasLasOpciones('municipios_seleccionados');
}

function consultarMunicipios() {
	/*
	seleccionarTodasLasOpciones('departamentos_seleccionados');
	ajaxLoader('Auxiliar?accion=ajax_municipios','municipios_posibles','div_feedback_municipios','','departamentos_seleccionados','','No se encontraron municipios.')
	*/	
	ajaxLoader('Auxiliar?accion=ajax_municipios&opcionVacia=true','municipios_seleccionados','div_feedback_municipios','','departamentos_seleccionados','','No se encontraron municipios.')	
}

function registrarParcela(f) {
	location.href = 'Parcela?accion=detalle_parcela';
}

</script>

<!-- Put these into the <head> -->
<link rel="stylesheet" href="js/responsive-nav/responsive-nav.css">
<script src="js/responsive-nav/responsive-nav.js"></script>

</head>
<body id="body">

<%=co.gov.ideamredd.admif.UI.getHeader() %>

<%
co.gov.ideamredd.admif.Sec sec = new co.gov.ideamredd.admif.Sec();

String menu = "";
String id_usuario = "";

try {
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "");
	menu = sec.generarMenu(id_usuario, "/AdmIF/Parcela?accion=busqueda_parcelas", idioma);
}
catch (Exception e) {
	menu = "..." + e.toString();
}

%>
<div class="menu"><%=menu %></div>

<%
String datos_sesion = "";
try {
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
<% try { out.print(msj.getString("General.Administracion_de_inventarios_forestales")); } catch (MissingResourceException e) { out.print("Administración de Inventarios Forestales"); } %>
</p>

<h1>
<% try { out.print(msj.getString("General.Visor")); } catch (MissingResourceException e) { out.print("Visor de Parcelas"); } %>
</h1>
<div class="iframe_visor_de_parcelas">
<iframe class="iframe_visor_de_parcelas" id="iframe_visor_parcelas" src="visor_parcelas_leaflet.jsp"></iframe>
</div>

<h1>
<% try { out.print(msj.getString(yo+"Formulario_de_busqueda_de_parcelas")); } catch (MissingResourceException e) { out.print("Formulario de búsqueda de parcelas"); } %>
</h1>

<form name=f action='Parcela' method='POST'>				

<div class="form">				

<div class=campo>
<div class="etiqueta">
<% try { out.print(msj.getString(yo+"Nombre")); } catch (MissingResourceException e) { out.print("Nombre"); } %>
</div>
<input type=text name="PRCL_NOMBRE" value='' />
</div>

<div class=campo>
<div class="etiqueta">
<% try { out.print(msj.getString(yo+"PRCL_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Consecutivo de Parcela"); } %>
</div>
<input type=text name="PRCL_CONSECUTIVO" value='' />
</div>

<div class=campo>
<div class="etiqueta">
<% try { out.print(msj.getString(yo+"PRCL_PLOT")); } catch (MissingResourceException e) { out.print("Código Plot"); } %>
</div>
<input type=text name="PRCL_PLOT" value='' />
</div>

<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString("PRCL_FECHAFIN_APROXIMACION")); } catch (MissingResourceException e) { out.print("Fecha final establecimiento"); } 
%>
</div>
<input type=text name="PRCL_FECHAFIN_APROXIMACION" id="PRCL_FECHAFIN_APROXIMACION" value='' disabled />
<input type=button id="b_PRCL_FECHAFIN_APROXIMACION" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha"); } %>' class="btn btn-default" />
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_FECHAFIN_APROXIMACION',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_FECHAFIN_APROXIMACION',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<!--
<div class=campo>
<div class="etiqueta">
<% 
//try { out.print(msj.getString(yo+"Pais")); } catch (MissingResourceException e) { out.print("País"); } 
%>
</div>
<select class="form-control" name="PRCL_CONS_PAIS" id="PRCL_CONS_PAIS" onchange="ajaxLoader('Auxiliar?accion=ajax_departamentos','departamentos_posibles','div_feedback_departamentos','','PRCL_CONS_PAIS','','No se encontraron departamentos.');seleccionarTodasLasOpciones('departamentos_seleccionados');quitarOpcionesSeleccionadas('departamentos_seleccionados');seleccionarTodasLasOpciones('municipios_posibles');quitarOpcionesSeleccionadas('municipios_posibles');seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');" >
<% 
//out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_pais"), "").toString()); 
%>
</select>
</div>

<div class=campo>
<div class="etiqueta">
<% 
//try { out.print(msj.getString(yo+"Departamentos")); } catch (MissingResourceException e) { out.print("Departamentos"); } 
%>
</div>
<div class="select_izquierdo">
<select size="4" multiple class="form-control" name="departamentos_posibles" id="departamentos_posibles" >
<% 
//out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_departamentos"), "").toString()); 
%>
</select>
</div>
<div class="botonesquitaryponer">
<input type="button" id="b_agregar_todos_los_departamentos" value="++" onclick="seleccionarTodasLasOpciones('departamentos_posibles');copiarOpcionesSeleccionadas('departamentos_posibles', 'departamentos_seleccionados');seleccionarTodasLasOpciones('municipios_posibles');quitarOpcionesSeleccionadas('municipios_posibles');consultarMunicipios();" />
<input type="button" id="b_agregar_departamentos_seleccionados" value="+" onclick="copiarOpcionesSeleccionadas('departamentos_posibles', 'departamentos_seleccionados');seleccionarTodasLasOpciones('municipios_posibles');quitarOpcionesSeleccionadas('municipios_posibles');seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');consultarMunicipios();" />
<input type="button" id="b_quitar_departamentos_seleccionados" value="-" onclick="quitarOpcionesSeleccionadas('departamentos_seleccionados');seleccionarTodasLasOpciones('municipios_posibles');quitarOpcionesSeleccionadas('municipios_posibles');seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');consultarMunicipios();" />
<input type="button" id="b_quitar_todos_departamentos" value="--" onclick="seleccionarTodasLasOpciones('departamentos_seleccionados');quitarOpcionesSeleccionadas('departamentos_seleccionados');seleccionarTodasLasOpciones('municipios_posibles');quitarOpcionesSeleccionadas('municipios_posibles');seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');consultarMunicipios();" />
</div>
<div class="select_derecho">
<select size="4" multiple class="form-control" name="departamentos_seleccionados" id="departamentos_seleccionados" >
</select>
</div>
<div id="div_feedback_departamentos"></div>
</div>

<div class=campo>
<div class="etiqueta">
<% 
//try { out.print(msj.getString(yo+"Municipios")); } catch (MissingResourceException e) { out.print("Municipios"); } 
%>
</div>
<div class="select_izquierdo">
<select size="4" multiple class="form-control" name="municipios_posibles" id="municipios_posibles" >
<% 
//out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_departamentos"), "").toString()); 
%>
</select>
</div>
<div class="botonesquitaryponer">
<input type="button" id="b_agregar_todos_los_municipios" value="++" onclick="seleccionarTodasLasOpciones('municipios_posibles');copiarOpcionesSeleccionadas('municipios_posibles', 'municipios_seleccionados');" />
<input type="button" id="b_agregar_municipios_seleccionados" value="+" onclick="copiarOpcionesSeleccionadas('municipios_posibles', 'municipios_seleccionados');" />
<input type="button" id="b_quitar_municipios_seleccionados" value="-" onclick="quitarOpcionesSeleccionadas('municipios_seleccionados');" />
<input type="button" id="b_quitar_todos_municipios" value="--" onclick="seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');" />
</div>
<div class="select_derecho">
<select size="4" multiple class="form-control" name="municipios_seleccionados" id="municipios_seleccionados" >
</select>
</div>
<div id="div_feedback_municipios"></div>
</div>
-->


<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString(yo+"Pais")); } catch (MissingResourceException e) { out.print("País"); } 
%>
</div>
<select class="form-control" name="PRCL_CONS_PAIS" id="PRCL_CONS_PAIS" onchange="ajaxLoader('Auxiliar?accion=ajax_departamentos&opcionVacia=true','departamentos_seleccionados','div_feedback_departamentos','','PRCL_CONS_PAIS','','No se encontraron departamentos.');seleccionarTodasLasOpciones('departamentos_seleccionados');quitarOpcionesSeleccionadas('departamentos_seleccionados');seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_pais"), "").toString()); 
%>
</select>
</div>


<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString(yo+"Departamento")); } catch (MissingResourceException e) { out.print("Departamento"); } 
%>
</div>
<div class="select_derecho">
<select class="form-control" name="departamentos_seleccionados" id="departamentos_seleccionados" onchange="seleccionarTodasLasOpciones('municipios_seleccionados');quitarOpcionesSeleccionadas('municipios_seleccionados');consultarMunicipios();">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_departamentos"), "").toString()); 
%>
</select>
</div>
<div id="div_feedback_departamentos"></div>
</div>

<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString(yo+"Municipio")); } catch (MissingResourceException e) { out.print("Municipio"); } 
%>
</div>
<div class="select_derecho">
<select class="form-control" name="municipios_seleccionados" id="municipios_seleccionados" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_municipios"), "").toString()); 
%>
</select>
</div>
<div id="div_feedback_municipios"></div>
</div>

<!-- 
<div class=campo>
<div class="etiqueta">
<% 
//try { out.print(msj.getString(yo+"Tipo_Inventario")); } catch (MissingResourceException e) { out.print("Tipo de Inventario"); } 
%>
</div>
<select class="form-control" name="TPIN_CONSECUTIVO" id="TPIN_CONSECUTIVO" >
<% 
//out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_tipoinventario"), "").toString()); 
%>
</select>
</div>
-->

</div>

<div class="botones">
<input type='button' value='<% try { out.print(msj.getString(yo+"Buscar_Parcelas")); } catch (MissingResourceException e) { out.print("Buscar Parcelas"); } %>' class="btn btn-default" onclick='javascript:buscar(this.form, false);' />

<%
String usuarioAceptoLicencia = Auxiliar.nzObjStr(request.getAttribute("usuarioAceptoLicencia"), "");
String indicacionLicencia = Auxiliar.nzObjStr(request.getAttribute("indicacionLicencia"), "");
if (!usuarioAceptoLicencia.equals("1")) { 
%>
	<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=indicacionLicencia %></div>
<% } else { %>
	<input type='button' value='<% try { out.print(msj.getString(yo+"Exportar_Parcelas")); } catch (MissingResourceException e) { out.print("Exportar Parcelas a Excel"); } %>' class="btn btn-default" onclick='javascript:buscar(this.form, true);' />
<% } %>	
<!-- 
<input type='button' value='
<% 
//try { out.print(msj.getString(yo+"Exportar_Parcelas")); } catch (MissingResourceException e) { out.print("Exportar Parcelas"); } 
%>' class='btn btn-default' onclick='javascript:exportar(this.form);' />
-->
<% if (Auxiliar.tieneAlgo(id_usuario)) { %>
<input type='button' value='<% try { out.print(msj.getString(yo+"Registrar_Parcelas")); } catch (MissingResourceException e) { out.print("Registrar Parcelas"); } %>' class='btn btn-default' onclick='javascript:registrarParcela(this.form);' />
<% } %>
</div>

<input type='hidden' name='idioma' value='<% out.print(idioma); %>' />
</form>

<div id="div_url_visor" style="color: #999; width: 100%; border: 1px solid white; display:none;"></div>

<div id="resultados"></div>
<div id="tiempo_respuesta"></div>
<div id="paginas" style="margin: auto;"></div>


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