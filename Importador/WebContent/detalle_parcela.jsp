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
String yo = "detalle_parcela.";
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

//es_movil = true;

if (es_movil) { 
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
<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
<link rel='stylesheet' type='text/css' media='all' href='js/jscalendar/calendar-win2k-cold-1.css' title='win2k-cold-1' />

<script type='text/javascript' src='js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='js/jscalendar/calendar-setup.js'></script>

<title>
<% try { out.print(msj.getString(yo+"Detalle_de_Parcela")); } catch (MissingResourceException e) { out.print("Detalle de Parcela" + ":"); } %>
</title>

<script type='text/javascript' src='js/ajax.js'></script>
<script type='text/javascript' src='js/ajaxLoader.js'></script>
<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>


<script type='text/javascript'>
<%
String PRCL_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("PRCL_CONSECUTIVO"), "").toString();
%>

var polyline_campamento;
var polyline_parcela;


var usuario = '<% out.print(request.getParameter("usuario"));%>';
var idioma = '<% out.print(request.getParameter("idioma"));%>';

$(document).ready(function(){
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-30) + 'px';
    <% } %>

    if ('<%=PRCL_CONSECUTIVO%>' != '' && false) {
		document.body.style.backgroundImage = 'url("http://media1.s-nbcnews.com/i/newscms/2014_08/198926/140220-deforestation-jsw-1212p_44a17716d0a0e28154dc2907d07152c1.jpg")';
		document.body.style.imageRepeat = 'no-repeat';
		document.body.style.backgroundAttachment = 'fixed';
   	}
});

function nueva(f) {
	url = 'Parcela?accion=detalle_parcela';
	location.href = url;
}

function editar(f) {
	if (validar(f))	{
		f.accion.value = 'guardar';
		f.submit();
	}
	else {
		alert('<% try { out.print(msj.getString("AVISO_VALIDACION.REVISAR_FORMULARIO")); } catch (MissingResourceException e) { out.print("Por favor especifique el id de conglomerado" + ":"); } %>');
	}
}

function validar(f) {
	var go = true;

	if (f.PRCL_ID_UPM.value == '' || !esEntero(f.PRCL_ID_UPM.value)) {
		document.getElementById('PRCL_ID_UPM_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.ID_UPM")); } catch (MissingResourceException e) { out.print("Por favor especifique el id de conglomerado" + ":"); } %>';
		document.getElementById('PRCL_ID_UPM_MSG').style.display = 'block';
		f.PRCL_ID_UPM.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_ID_UPM.className = 'valido';
		document.getElementById('PRCL_ID_UPM_MSG').style.display = 'none';
	}
	
	if (f.PRCL_PLOT.value != '') {
		if (!esEntero(f.PRCL_PLOT.value)) {
			document.getElementById('PRCL_PLOT_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_PLOT")); } catch (MissingResourceException e) { out.print("El id de plot debe ser un número entero" + ":"); } %>';
			document.getElementById('PRCL_PLOT_MSG').style.display = 'block';
			f.PRCL_PLOT.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_PLOT.className = 'valido';
			document.getElementById('PRCL_PLOT_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_AREA.value != '') {
		if (!esArea(f.PRCL_AREA.value)) {
			document.getElementById('PRCL_AREA_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_AREA")); } catch (MissingResourceException e) { out.print("El área debe ser un número decimal positivo" + ":"); } %>';
			document.getElementById('PRCL_AREA_MSG').style.display = 'block';
			f.PRCL_AREA.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_AREA.className = 'valido';
			document.getElementById('PRCL_AREA_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_LATITUD.value == '' || !esLatitud(f.PRCL_LATITUD.value)) {
		document.getElementById('PRCL_LATITUD_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_LATITUD")); } catch (MissingResourceException e) { out.print("Por favor especifique la latitud del jalón principal de la parcela" + ":"); } %>';
		document.getElementById('PRCL_LATITUD_MSG').style.display = 'block';
		f.PRCL_LATITUD.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_LATITUD.className = 'valido';
		document.getElementById('PRCL_LATITUD_MSG').style.display = 'none';
	}
	
	if (f.PRCL_LONGITUD.value == '' || !esLongitud(f.PRCL_LONGITUD.value)) {
		document.getElementById('PRCL_LONGITUD_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_LONGITUD")); } catch (MissingResourceException e) { out.print("Por favor especifique la longitud del jalón principal de la parcela" + ":"); } %>';
		document.getElementById('PRCL_LONGITUD_MSG').style.display = 'block';
		f.PRCL_LONGITUD.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_LONGITUD.className = 'valido';
		document.getElementById('PRCL_LONGITUD_MSG').style.display = 'none';
	}
	
	if (f.PRCL_ALTITUD.value == '' || !esAltitud(f.PRCL_ALTITUD.value)) {
		document.getElementById('PRCL_ALTITUD_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_ALTITUD")); } catch (MissingResourceException e) { out.print("Por favor especifique la altitud del jalón principal de la parcela" + ":"); } %>';
		document.getElementById('PRCL_ALTITUD_MSG').style.display = 'block';
		f.PRCL_ALTITUD.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_ALTITUD.className = 'valido';
		document.getElementById('PRCL_ALTITUD_MSG').style.display = 'none';
	}
	
	if (f.PRCL_NOMBRE.value == '') {
		document.getElementById('PRCL_NOMBRE_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_NOMBRE")); } catch (MissingResourceException e) { out.print("Por favor especifique el nombre de la parcela" + ":"); } %>';
		document.getElementById('PRCL_NOMBRE_MSG').style.display = 'block';
		f.PRCL_NOMBRE.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_NOMBRE.className = 'valido';
		document.getElementById('PRCL_NOMBRE_MSG').style.display = 'none';
	}
	
	if (f.PRCL_FECHAINI_APROXIMACION.value == '') {
		document.getElementById('PRCL_FECHAINI_APROXIMACION_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_FECHAINI_APROXIMACION")); } catch (MissingResourceException e) { out.print("Por favor especifique la fecha inicial de aproximación a la parcela" + ":"); } %>';
		document.getElementById('PRCL_FECHAINI_APROXIMACION_MSG').style.display = 'block';
		f.PRCL_FECHAINI_APROXIMACION.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_FECHAINI_APROXIMACION.className = 'valido';
		document.getElementById('PRCL_FECHAINI_APROXIMACION_MSG').style.display = 'none';
	}
	
	if (f.PRCL_FECHAFIN_APROXIMACION.value == '') {
		document.getElementById('PRCL_FECHAFIN_APROXIMACION_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_FECHAFIN_APROXIMACION")); } catch (MissingResourceException e) { out.print("Por favor especifique la fecha final de aproximación a la parcela" + ":"); } %>';
		document.getElementById('PRCL_FECHAFIN_APROXIMACION_MSG').style.display = 'block';
		f.PRCL_FECHAFIN_APROXIMACION.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_FECHAFIN_APROXIMACION.className = 'valido';
		document.getElementById('PRCL_FECHAFIN_APROXIMACION_MSG').style.display = 'none';
	}

	
	if (f.PRCL_FECHAINI_APROXIMACION.value != '' && f.PRCL_FECHAFIN_APROXIMACION.value != '') {
		if (f.PRCL_FECHAINI_APROXIMACION.value > f.PRCL_FECHAFIN_APROXIMACION.value) {
			document.getElementById('PRCL_FECHAINI_APROXIMACION_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_FECHAS_APROXIMACION")); } catch (MissingResourceException e) { out.print("La fecha inicial de establecimiento no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_FECHAINI_APROXIMACION_MSG').style.display = 'block';
			f.PRCL_FECHAINI_APROXIMACION.className = 'invalido';
			document.getElementById('PRCL_FECHAFIN_APROXIMACION_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_FECHAS_APROXIMACION")); } catch (MissingResourceException e) { out.print("La fecha inicial de establecimiento no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_FECHAFIN_APROXIMACION_MSG').style.display = 'block';
			f.PRCL_FECHAFIN_APROXIMACION.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_FECHAINI_APROXIMACION.className = 'valido';
			f.PRCL_FECHAFIN_APROXIMACION.className = 'valido';
			document.getElementById('PRCL_FECHAINI_APROXIMACION_MSG').style.display = 'none';
			document.getElementById('PRCL_FECHAFIN_APROXIMACION_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_FECHAINI_LOCALIZACION.value == '') {
		document.getElementById('PRCL_FECHAINI_LOCALIZACION_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_FECHAINI_LOCALIZACION")); } catch (MissingResourceException e) { out.print("Por favor especifique la fecha inicial de localización a la parcela" + ":"); } %>';
		document.getElementById('PRCL_FECHAINI_LOCALIZACION_MSG').style.display = 'block';
		f.PRCL_FECHAINI_LOCALIZACION.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_FECHAINI_LOCALIZACION.className = 'valido';
		document.getElementById('PRCL_FECHAINI_LOCALIZACION_MSG').style.display = 'none';
	}
	
	if (f.PRCL_FECHAFIN_LOCALIZACION.value == '') {
		document.getElementById('PRCL_FECHAFIN_LOCALIZACION_MSG').innerHTML = '<% try { out.print(msj.getString("PRCL_FECHAFIN_LOCALIZACION")); } catch (MissingResourceException e) { out.print("Por favor especifique la fecha final de localización a la parcela" + ":"); } %>';
		document.getElementById('PRCL_FECHAFIN_LOCALIZACION_MSG').style.display = 'block';
		f.PRCL_FECHAFIN_LOCALIZACION.className = 'invalido';
		go = false;
	}
	else {
		f.PRCL_FECHAFIN_LOCALIZACION.className = 'valido';
		document.getElementById('PRCL_FECHAFIN_LOCALIZACION_MSG').style.display = 'none';
	}
	
	if (f.PRCL_FECHAINI_LOCALIZACION.value != '' && f.PRCL_FECHAFIN_LOCALIZACION.value != '') {
		if (f.PRCL_FECHAINI_LOCALIZACION.value > f.PRCL_FECHAFIN_LOCALIZACION.value) {
			document.getElementById('PRCL_FECHAINI_LOCALIZACION_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_FECHAS_LOCALIZACION")); } catch (MissingResourceException e) { out.print("La fecha inicial de localización no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_FECHAINI_LOCALIZACION_MSG').style.display = 'block';
			f.PRCL_FECHAINI_LOCALIZACION.className = 'invalido';
			document.getElementById('PRCL_FECHAFIN_LOCALIZACION_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_FECHAS_LOCALIZACION")); } catch (MissingResourceException e) { out.print("La fecha inicial de localización no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_FECHAFIN_LOCALIZACION_MSG').style.display = 'block';
			f.PRCL_FECHAFIN_LOCALIZACION.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_FECHAINI_LOCALIZACION.className = 'valido';
			document.getElementById('PRCL_FECHAINI_LOCALIZACION_MSG').style.display = 'none';
			f.PRCL_FECHAFIN_LOCALIZACION.className = 'valido';
			document.getElementById('PRCL_FECHAFIN_LOCALIZACION_MSG').style.display = 'none';
		}
	}
	
	f.PRCL_DISTANCIA_POBLADO.className = 'valido';
	document.getElementById('PRCL_DISTANCIA_POBLADO_MSG').style.display = 'none';
	f.PRCL_MEDIOACCESO_POBLADO.className = 'valido';
	document.getElementById('PRCL_MEDIOACCESO_POBLADO_MSG').style.display = 'none';
	f.PRCL_TPOBLADO_H.className = 'valido';
	document.getElementById('PRCL_TPOBLADO_H_MSG').style.display = 'none';
	f.PRCL_TPOBLADO_M.className = 'valido';
	document.getElementById('PRCL_TPOBLADO_M_MSG').style.display = 'none';

	if (f.PRCL_TPOBLADO_M.value != '' || f.PRCL_DISTANCIA_POBLADO.value != '' || f.PRCL_TPOBLADO_H.value != '' || f.PRCL_TPOBLADO_M.value != '' || f.PRCL_MEDIOACCESO_POBLADO.value != '') {
		if (!esDistancia(f.PRCL_DISTANCIA_POBLADO.value)) {
			document.getElementById('PRCL_DISTANCIA_POBLADO_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.DISTANCIA_INVALIDA")); } catch (MissingResourceException e) { out.print("La distancia hasta el poblado debe ser un decimal positivo." + ":"); } %>';
			document.getElementById('PRCL_DISTANCIA_POBLADO_MSG').style.display = 'block';
			f.PRCL_DISTANCIA_POBLADO.className = 'invalido';
			go = false;
		}
			
		if (f.PRCL_MEDIOACCESO_POBLADO.value == '') {
			document.getElementById('PRCL_MEDIOACCESO_POBLADO_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_MEDIOACCESO_POBLADO")); } catch (MissingResourceException e) { out.print("Especifique el medio de acceso al poblado." + ":"); } %>';
			document.getElementById('PRCL_MEDIOACCESO_POBLADO_MSG').style.display = 'block';
			f.PRCL_MEDIOACCESO_POBLADO.className = 'invalido';
			go = false;
		}
		
		if (!esEnteroMayorOIgualACero(f.PRCL_TPOBLADO_H.value)) {
			document.getElementById('PRCL_TPOBLADO_H_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.HORAS_INVALIDAS")); } catch (MissingResourceException e) { out.print("Número de horas inválido." + ":"); } %>';
			document.getElementById('PRCL_TPOBLADO_H_MSG').style.display = 'block';
			f.PRCL_TPOBLADO_H.className = 'invalido';
			go = false;
		}
		
		if (!esMinuto(f.PRCL_TPOBLADO_M.value)) {
			document.getElementById('PRCL_TPOBLADO_M_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.MINUTOS_INVALIDOS")); } catch (MissingResourceException e) { out.print("Número de minutos inválido." + ":"); } %>';
			document.getElementById('PRCL_TPOBLADO_M_MSG').style.display = 'block';
			f.PRCL_TPOBLADO_M.className = 'invalido';
			go = false;
		}
	}
			
	f.PRCL_DISTANCIA_CAMPAMENTO.className = 'valido';
	document.getElementById('PRCL_DISTANCIA_CAMPAMENTO_MSG').style.display = 'none';
	f.PRCL_MEDIOACCESO_CAMPAMENTO.className = 'valido';
	document.getElementById('PRCL_MEDIOACCESO_CAMPAMENTO_MSG').style.display = 'none';
	f.PRCL_TCAMPAMENTO_H.className = 'valido';
	document.getElementById('PRCL_TCAMPAMENTO_H_MSG').style.display = 'none';
	f.PRCL_TCAMPAMENTO_M.className = 'valido';
	document.getElementById('PRCL_TCAMPAMENTO_M_MSG').style.display = 'none';
	f.PRCL_DISTANCIA_JALON.className = 'valido';
	document.getElementById('PRCL_DISTANCIA_JALON_MSG').style.display = 'none';

	if (f.PRCL_TCAMPAMENTO_M.value != '' || f.PRCL_DISTANCIA_CAMPAMENTO.value != '' || f.PRCL_TCAMPAMENTO_H.value != '' || f.PRCL_TCAMPAMENTO_H.value != '' || f.PRCL_MEDIOACCESO_CAMPAMENTO.value != '') {
		if (!esDistancia(f.PRCL_DISTANCIA_CAMPAMENTO.value)) {
			document.getElementById('PRCL_DISTANCIA_CAMPAMENTO_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.DISTANCIA_INVALIDA")); } catch (MissingResourceException e) { out.print("La distancia hasta el campamento debe ser un decimal positivo." + ":"); } %>';
			document.getElementById('PRCL_DISTANCIA_CAMPAMENTO_MSG').style.display = 'block';
			f.PRCL_DISTANCIA_CAMPAMENTO.className = 'invalido';
			go = false;
		}
		
		if (f.PRCL_MEDIOACCESO_CAMPAMENTO.value == '') {
			document.getElementById('PRCL_MEDIOACCESO_CAMPAMENTO_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.PRCL_MEDIOACCESO_CAMPAMENTO")); } catch (MissingResourceException e) { out.print("Especifique el medio de acceso al poblado." + ":"); } %>';
			document.getElementById('PRCL_MEDIOACCESO_CAMPAMENTO_MSG').style.display = 'block';
			f.PRCL_MEDIOACCESO_CAMPAMENTO.className = 'invalido';
			go = false;
		}
		
		if (!esEnteroMayorOIgualACero(f.PRCL_TCAMPAMENTO_H.value)) {
			document.getElementById('PRCL_TCAMPAMENTO_H_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.HORAS_INVALIDAS")); } catch (MissingResourceException e) { out.print("Número de horas inválido." + ":"); } %>';
			document.getElementById('PRCL_TCAMPAMENTO_H_MSG').style.display = 'block';
			f.PRCL_TCAMPAMENTO_H.className = 'invalido';
			go = false;
		}
			
		if (!esMinuto(f.PRCL_TCAMPAMENTO_M.value)) {
			document.getElementById('PRCL_TCAMPAMENTO_M_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.MINUTOS_INVALIDOS")); } catch (MissingResourceException e) { out.print("Número de minutos inválido." + ":"); } %>';
			document.getElementById('PRCL_TCAMPAMENTO_M_MSG').style.display = 'block';
			f.PRCL_TCAMPAMENTO_M.className = 'invalido';
			go = false;
		}
	}
	
	f.PRCL_DISTANCIA_JALON.className = 'valido';
	document.getElementById('PRCL_DISTANCIA_JALON_MSG').style.display = 'none';
	f.PRCL_TJALON_H.className = 'valido';
	document.getElementById('PRCL_TJALON_H_MSG').style.display = 'none';
	f.PRCL_TJALON_M.className = 'valido';
	document.getElementById('PRCL_TJALON_M_MSG').style.display = 'none';
	f.PRCL_DISTANCIA_CAMPAMENTOS.className = 'valido';
	document.getElementById('PRCL_DISTANCIA_CAMPAMENTOS_MSG').style.display = 'none';
	if (f.PRCL_DISTANCIA_JALON.value != '' || f.PRCL_MEDIOACCESO_JALON.value != '' || f.PRCL_TJALON_H.value != '' || f.PRCL_TJALON_M.value != '' || f.PRCL_DISTANCIA_CAMPAMENTOS.value != '') {
		if (!esDistancia(f.PRCL_DISTANCIA_JALON.value)) {
			document.getElementById('PRCL_DISTANCIA_JALON_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.DISTANCIA_INVALIDA")); } catch (MissingResourceException e) { out.print("Distancia inválida." + ":"); } %>';
			document.getElementById('PRCL_DISTANCIA_JALON_MSG').style.display = 'block';
			f.PRCL_DISTANCIA_JALON.className = 'invalido';
			go = false;
		}
		
		if (!esEnteroMayorOIgualACero(f.PRCL_TJALON_H.value)) {
			document.getElementById('PRCL_TJALON_H_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.HORAS_INVALIDAS")); } catch (MissingResourceException e) { out.print("Número de horas inválido." + ":"); } %>';
			document.getElementById('PRCL_TJALON_H_MSG').style.display = 'block';
			f.PRCL_TJALON_H.className = 'invalido';
			go = false;
		}
		
		if (!esMinuto(f.PRCL_TJALON_M.value)) {
			document.getElementById('PRCL_TJALON_M_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.MINUTOS_INVALIDOS")); } catch (MissingResourceException e) { out.print("Número de minutos inválido." + ":"); } %>';
			document.getElementById('PRCL_TJALON_M_MSG').style.display = 'block';
			f.PRCL_TJALON_M.className = 'invalido';
			go = false;
		}
		
		if (!esDistancia(f.PRCL_DISTANCIA_CAMPAMENTOS.value)) {
			document.getElementById('PRCL_DISTANCIA_CAMPAMENTOS_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.DISTANCIA_INVALIDA")); } catch (MissingResourceException e) { out.print("Distancia inválida." + ":"); } %>';
			document.getElementById('PRCL_DISTANCIA_CAMPAMENTOS_MSG').style.display = 'block';
			f.PRCL_DISTANCIA_CAMPAMENTOS.className = 'invalido';
			go = false;
		}
	}	

	if (f.PRCL_SPF1_FECHAINI.value != '' && f.PRCL_SPF1_FECHAFIN.value != '') {
		if (f.PRCL_SPF1_FECHAINI.value > f.PRCL_SPF1_FECHAFIN.value) {
			document.getElementById('PRCL_SPF1_FECHAINI_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF1")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF1 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF1_FECHAINI_MSG').style.display = 'block';
			f.PRCL_SPF1_FECHAINI.className = 'invalido';
			document.getElementById('PRCL_SPF1_FECHAFIN_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF1")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF1 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF1_FECHAFIN_MSG').style.display = 'block';
			f.PRCL_SPF1_FECHAFIN.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_SPF1_FECHAINI.className = 'valido';
			f.PRCL_SPF1_FECHAFIN.className = 'valido';
			document.getElementById('PRCL_SPF1_FECHAINI_MSG').style.display = 'none';
			document.getElementById('PRCL_SPF1_FECHAFIN_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_SPF2_FECHAINI.value != '' && f.PRCL_SPF2_FECHAFIN.value != '') {
		if (f.PRCL_SPF2_FECHAINI.value > f.PRCL_SPF2_FECHAFIN.value) {
			document.getElementById('PRCL_SPF2_FECHAINI_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF2")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF2 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF2_FECHAINI_MSG').style.display = 'block';
			f.PRCL_SPF2_FECHAINI.className = 'invalido';
			document.getElementById('PRCL_SPF2_FECHAFIN_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF2")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF2 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF2_FECHAFIN_MSG').style.display = 'block';
			f.PRCL_SPF2_FECHAFIN.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_SPF2_FECHAINI.className = 'valido';
			f.PRCL_SPF2_FECHAFIN.className = 'valido';
			document.getElementById('PRCL_SPF2_FECHAINI_MSG').style.display = 'none';
			document.getElementById('PRCL_SPF2_FECHAFIN_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_SPF3_FECHAINI.value != '' && f.PRCL_SPF3_FECHAFIN.value != '') {
		if (f.PRCL_SPF3_FECHAINI.value > f.PRCL_SPF3_FECHAFIN.value) {
			document.getElementById('PRCL_SPF3_FECHAINI_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF3")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF3 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF3_FECHAINI_MSG').style.display = 'block';
			f.PRCL_SPF3_FECHAINI.className = 'invalido';
			document.getElementById('PRCL_SPF3_FECHAFIN_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF3")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF3 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF3_FECHAFIN_MSG').style.display = 'block';
			f.PRCL_SPF3_FECHAFIN.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_SPF3_FECHAINI.className = 'valido';
			f.PRCL_SPF3_FECHAFIN.className = 'valido';
			document.getElementById('PRCL_SPF3_FECHAINI_MSG').style.display = 'none';
			document.getElementById('PRCL_SPF3_FECHAFIN_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_SPF4_FECHAINI.value != '' && f.PRCL_SPF4_FECHAFIN.value != '') {
		if (f.PRCL_SPF4_FECHAINI.value > f.PRCL_SPF4_FECHAFIN.value) {
			document.getElementById('PRCL_SPF4_FECHAINI_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF4")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF4 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF4_FECHAINI_MSG').style.display = 'block';
			f.PRCL_SPF4_FECHAINI.className = 'invalido';
			document.getElementById('PRCL_SPF4_FECHAFIN_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF4")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF4 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF4_FECHAFIN_MSG').style.display = 'block';
			f.PRCL_SPF4_FECHAFIN.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_SPF4_FECHAINI.className = 'valido';
			f.PRCL_SPF4_FECHAFIN.className = 'valido';
			document.getElementById('PRCL_SPF4_FECHAINI_MSG').style.display = 'none';
			document.getElementById('PRCL_SPF4_FECHAFIN_MSG').style.display = 'none';
		}
	}
	
	if (f.PRCL_SPF5_FECHAINI.value != '' && f.PRCL_SPF5_FECHAFIN.value != '') {
		if (f.PRCL_SPF5_FECHAINI.value > f.PRCL_SPF5_FECHAFIN.value) {
			document.getElementById('PRCL_SPF5_FECHAINI_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF5")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF5 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF5_FECHAINI_MSG').style.display = 'block';
			f.PRCL_SPF5_FECHAINI.className = 'invalido';
			document.getElementById('PRCL_SPF5_FECHAFIN_MSG').innerHTML = '<% try { out.print(msj.getString("AVISO_VALIDACION.FECHAS_SPF5")); } catch (MissingResourceException e) { out.print("La fecha inicial de SPF5 no puede ser posterior a la fecha final" + ":"); } %>';
			document.getElementById('PRCL_SPF5_FECHAFIN_MSG').style.display = 'block';
			f.PRCL_SPF5_FECHAFIN.className = 'invalido';
			go = false;
		}
		else {
			f.PRCL_SPF5_FECHAINI.className = 'valido';
			f.PRCL_SPF5_FECHAFIN.className = 'valido';
			document.getElementById('PRCL_SPF5_FECHAINI_MSG').style.display = 'none';
			document.getElementById('PRCL_SPF5_FECHAFIN_MSG').style.display = 'none';
		}
	}
	
	
	
	return go;
}

function eliminar(f) {
	if (confirm('<% try { out.print(msj.getString(yo+"Confirma_la_eliminacion_del_elemento_seleccionado")); } catch (MissingResourceException e) { out.print("Confirma la eliminación del elemento seleccionado" + ":"); } %>')) {
		f.accion.value = 'eliminar';
		f.submit();
	}
}

function exportar_a_excel(f) {
	url = 'Individuo?accion=exportar&PRCL_CONSECUTIVO=' + f.PRCL_CONSECUTIVO.value;
	window.open(url);	
}

function exportar_a_pdf(f) {
	url = 'Individuo?accion=exportar_individuo_pdf&PRCL_CONSECUTIVO=' + f.PRCL_CONSECUTIVO.value;
	window.open(url);	
}

function coordenadas(f) {
	var url = '';
	if (f.PRCL_CONSECUTIVO.value != '') {
		url = 'Parcela?accion=coordenadas&control=&PRCL_CONSECUTIVO='+f.PRCL_CONSECUTIVO.value;
		window.open(url); 
	}
	else {
		alert('Antes debe crear la parcela');	
	}
}

function contactos(f) {
	var url = '';
	if (f.PRCL_CONSECUTIVO.value != '') {
		url = 'Parcela?accion=contactos_parcela&control=&PRCL_CONSECUTIVO='+f.PRCL_CONSECUTIVO.value;
		window.open(url); 
	}
	else {
		alert('Antes debe crear la parcela');	
	}
}

function consultarMunicipios() {
	ajaxLoader('Auxiliar?accion=ajax_municipios&opcionVacia=true','PRCL_MUNICIPIO','div_feedback_municipios','','departamentos_seleccionados','','No se encontraron municipios.');	
}

var jalon;

function success(position)
{
	var PRCL_LATITUD = document.getElementById('PRCL_LATITUD');
	var PRCL_LONGITUD = document.getElementById('PRCL_LONGITUD');
	var PRCL_ALTITUD = document.getElementById('PRCL_ALTITUD');

	var lat = position.coords.latitude;
	var lon = position.coords.longitude;
	var alt = position.coords.altitude;

	if (!esNumero(lat)) lat = 0;
	if (!esNumero(lon)) lon = 0;
	if (!esNumero(alt)) alt = 0;
	
	PRCL_LATITUD.value = lat;
	PRCL_LONGITUD.value = lon;
	PRCL_ALTITUD.value = alt;

	validar(document.f);

	jalon = L.icon({
	    iconUrl: 'mapas_parcelas/images/jalon-icon.png',
	    shadowUrl: 'mapas_parcelas/images/jalon-shadow.png',

	    iconSize:     [10, 20], // size of the icon
	    shadowSize:   [20, 20], // size of the shadow
	    iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location
	    shadowAnchor: [5, 20],  // the same for the shadow
	    popupAnchor:  [0, -20] // point from which the popup should open relative to the iconAnchor
	});
	
	L.marker([lat, lon], {icon: jalon}).addTo(map).bindPopup("GPS").openPopup();
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
	var PRCL_LATITUD = document.getElementById('PRCL_LATITUD');
	var PRCL_LONGITUD = document.getElementById('PRCL_LONGITUD');
	var PRCL_ALTITUD = document.getElementById('PRCL_ALTITUD');
		
	if (!esNumero(lat)) lat = 0;
	if (!esNumero(lon)) lon = 0;
	if (!esNumero(alt)) alt = 0;

	PRCL_LATITUD.value = lat;
	PRCL_LONGITUD.value = lon;
	PRCL_ALTITUD.value = alt;

	jalon = L.icon({
	    iconUrl: 'mapas_parcelas/images/jalon-icon.png',
	    shadowUrl: 'mapas_parcelas/images/jalon-shadow.png',

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

function agregarTrackLog(lat, lon) {
	var select_tipotrack = document.getElementById('tipotrack');
	var tipotrack = select_tipotrack.value;

	if (tipotrack == '') {
		alert('Elija el tipo de tracklog');
		select_tipotrack.focus();
	}		
	
	var tb_tracklog_campamento = document.getElementById('PRCL_TRACKLOG_CAMPAMENTO');
	var tb_tracklog_parcela = document.getElementById('PRCL_TRACKLOG_PARCELA');
	var separador = '_';
	
	if (tipotrack == 'CAMPAMENTO') {
		if (tb_tracklog_campamento.value != '') {
			tb_tracklog_campamento.value += separador;
		}
		
		tb_tracklog_campamento.value += lat+','+lon;

		trackear('CAMPAMENTO', tb_tracklog_campamento.value);
	}	

	if (tipotrack == 'PARCELA') {
		if (tb_tracklog_parcela.value != '') {
			tb_tracklog_parcela.value += separador;
		}
		
		tb_tracklog_parcela.value += lat+','+lon;

		trackear('PARCELA', tb_tracklog_parcela.value);
	}	
}

function inicializar() {
	var tb_tracklog_campamento = document.getElementById('PRCL_TRACKLOG_CAMPAMENTO');
	var tb_tracklog_parcela = document.getElementById('PRCL_TRACKLOG_PARCELA');
	
	trackear('CAMPAMENTO', tb_tracklog_campamento.value);
	trackear('PARCELA', tb_tracklog_parcela.value);
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
<body onload='inicializar();'>

<%=co.gov.ideamredd.admif.UI.getHeader() %>

<%
co.gov.ideamredd.admif.Sec sec = new co.gov.ideamredd.admif.Sec();

String menu = "";
try {
	String id_usuario = "";
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "es");
	if (!id_usuario.equals("")) {
		menu = sec.generarMenu(id_usuario, "detalle_parcela.jsp", idioma);
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

<p class="titulo-derecho">
<% try { out.print(msj.getString("General.Administracion_de_inventarios_forestales")); } catch (MissingResourceException e) { out.print("Administración de Inventarios Forestales"); } %>
</p>

<div id="div_titulo__detalle_de_parcela"  onclick="javascript:DIVisibilidad('div__form');">
<h1>
<% try { out.print(msj.getString(yo+"Detalle_de_Parcela")); } catch (MissingResourceException e) { out.print("Detalle de Parcela" + ":"); } %>
</h1>
</div>
<%
String retorno = Auxiliar.nzObjStr(request.getAttribute("retorno"), "").toString().trim(); 

if (!retorno.equals("")) {
	out.println(retorno); 
}
%>

<div id="div__form" class="form">

<form name="f" action='Parcela' method='post'>

<div id="div_subtitulo__datos_basicos" class="div_subseccion" style="clear: both;" onclick="javascript:DIVisibilidad('div__datos_basicos');">
<h3>
<% try { out.print(msj.getString(yo+"Datos_Basicos")); } catch (MissingResourceException e) { out.print("Datos Básicos de la Parcela" + ":"); } %>
</h3>
</div>

<div id="div__datos_basicos" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Consecutivo Proyecto REDD" + ":"); } %>
</div>

<input type=hidden name="PRCL_CONSECUTIVO" value="<%= PRCL_CONSECUTIVO %>" />
<%= PRCL_CONSECUTIVO %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_USR_DILIGENCIA_F1")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia la aproximación" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_USR_DILIGENCIA_F1" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_USR_DILIGENCIA_F1"), "").toString() %>"  onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_USR_DILIGENCIA_F1_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_USR_DILIGENCIA_F2")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia la localización" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_USR_DILIGENCIA_F2" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_USR_DILIGENCIA_F2"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_USR_DILIGENCIA_F2_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_NOMBRE")); } catch (MissingResourceException e) { out.print("Nombre del predio de la parcela" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_NOMBRE" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_NOMBRE"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_NOMBRE_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_DESCRIPCION")); } catch (MissingResourceException e) { out.print("Descripción" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_DESCRIPCION"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_DESCRIPCION"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_OBSERVACIONES")); } catch (MissingResourceException e) { out.print("Observaciones" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_OBSERVACIONES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_OBSERVACIONES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_ID_UPM")); } catch (MissingResourceException e) { out.print("Id Conglomerado" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_ID_UPM" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_ID_UPM"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_ID_UPM_MSG"></div>
</div>

</div><div id="div_subtitulo__acceso_poblado" style="clear: both;" onclick="javascript:DIVisibilidad('div__acceso_poblado');">
<h3>
<% try { out.print(msj.getString(yo+"Acceso_Poblado")); } catch (MissingResourceException e) { out.print("Acceso al Caserío" + ":"); } %>
</h3>
</div>

<div id="div__acceso_poblado" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_DISTANCIA_POBLADO")); } catch (MissingResourceException e) { out.print("Distancia (km) desde la capital del departamento al caserío más cercano a la parcela." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_DISTANCIA_POBLADO" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_DISTANCIA_POBLADO"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_DISTANCIA_POBLADO_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_MEDIOACCESO_POBLADO")); } catch (MissingResourceException e) { out.print("Medio de acceso desde la capital del departamento al caserío más cercano a la parcela." + ":"); } %>
</div>
<select class="form-control" name="PRCL_MEDIOACCESO_POBLADO" onblur="validar(this.form)">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_medioacceso_poblado"), "").toString()); 
%>
</select>
<div class="div_mensaje_dato_invalido" id="PRCL_MEDIOACCESO_POBLADO_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TPOBLADO_H")); } catch (MissingResourceException e) { out.print("Tiempo de viaje (horas) desde la capital del departamento al caserío más cercano a la parcela." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_TPOBLADO_H" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TPOBLADO_H"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_TPOBLADO_H_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TPOBLADO_M")); } catch (MissingResourceException e) { out.print("Tiempo de viaje (minutos) desde la capital del departamento al caserío más cercano a la parcela." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_TPOBLADO_M" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TPOBLADO_M"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_TPOBLADO_M_MSG"></div>
</div>

</div>
<div id="div_subtitulo__acceso_campamento" style="clear: both;" onclick="javascript:DIVisibilidad('div__acceso_campamento');">
<h3>
<% try { out.print(msj.getString(yo+"Acceso_Campamento")); } catch (MissingResourceException e) { out.print("Acceso al Campamento" + ":"); } %>
</h3>
</div>

<div id="div__acceso_campamento" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_FECHAINI_APROXIMACION")); } catch (MissingResourceException e) { out.print("Fecha inicial de aproximación a la parcela." + ":"); } %>
</div>
<input type=text name="PRCL_FECHAINI_APROXIMACION" id="PRCL_FECHAINI_APROXIMACION" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_FECHAINI_APROXIMACION"), "").toString() %>' onchange="validar(this.form)"/>
<input type=button id="b_PRCL_FECHAINI_APROXIMACION" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_FECHAINI_APROXIMACION_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_FECHAINI_APROXIMACION',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_FECHAINI_APROXIMACION',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_FECHAFIN_APROXIMACION")); } catch (MissingResourceException e) { out.print("Fecha final de aproximación la parcela" + ":"); } %>
</div>
<input type=text name="PRCL_FECHAFIN_APROXIMACION" id="PRCL_FECHAFIN_APROXIMACION" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_FECHAFIN_APROXIMACION"), "").toString() %>' onchange="validar(this.form)"/>
<input type=button id="b_PRCL_FECHAFIN_APROXIMACION" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_FECHAFIN_APROXIMACION_MSG"></div>
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


<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_DISTANCIA_CAMPAMENTO")); } catch (MissingResourceException e) { out.print("Distancia (km) desde el caserío más cercano a la parcela al campamento." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_DISTANCIA_CAMPAMENTO" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_DISTANCIA_CAMPAMENTO"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_DISTANCIA_CAMPAMENTO_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_MEDIOACCESO_CAMPAMENTO")); } catch (MissingResourceException e) { out.print("Medio de acceso desde el caserío más cercano a la parcela al campamento." + ":"); } %>
</div>
<select class="form-control" name="PRCL_MEDIOACCESO_CAMPAMENTO" onblur="validar(this.form)">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_medioacceso_campamento"), "").toString()); 
%>
</select>
<div class="div_mensaje_dato_invalido" id="PRCL_MEDIOACCESO_CAMPAMENTO_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TCAMPAMENTO_H")); } catch (MissingResourceException e) { out.print("Tiempo de viaje (horas) desde el caserío más cercano a la parcela al campamento." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_TCAMPAMENTO_H" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TCAMPAMENTO_H"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_TCAMPAMENTO_H_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TCAMPAMENTO_M")); } catch (MissingResourceException e) { out.print("Tiempo de viaje (minutos) desde el caserío más cercano a la parcela al campamento." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_TCAMPAMENTO_M" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TCAMPAMENTO_M"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_TCAMPAMENTO_M_MSG"></div>
</div>

</div>

<div id="div_subtitulo__acceso_jalon" style="clear: both;" onclick="javascript:DIVisibilidad('div__acceso_jalon');">
<h3>
<% try { out.print(msj.getString(yo+"Acceso_Jalon")); } catch (MissingResourceException e) { out.print("Acceso al Jalón" + ":"); } %>
</h3>
</div>

<div id="div__acceso_jalon" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_FECHAINI_LOCALIZACION")); } catch (MissingResourceException e) { out.print("Fecha inicial de localización de la parcela" + ":"); } %>
</div>
<input type=text name="PRCL_FECHAINI_LOCALIZACION" id="PRCL_FECHAINI_LOCALIZACION" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_FECHAINI_LOCALIZACION"), "").toString() %>' onchange="validar(this.form)"/>
<input type=button id="b_PRCL_FECHAINI_LOCALIZACION" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_FECHAINI_LOCALIZACION_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_FECHAINI_LOCALIZACION',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_FECHAINI_LOCALIZACION',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_FECHAFIN_LOCALIZACION")); } catch (MissingResourceException e) { out.print("Fecha final de localización de la parcela" + ":"); } %>
</div>
<input type=text name="PRCL_FECHAFIN_LOCALIZACION" id="PRCL_FECHAFIN_LOCALIZACION" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_FECHAFIN_LOCALIZACION"), "").toString() %>' onchange="validar(this.form)"/>
<input type=button id="b_PRCL_FECHAFIN_LOCALIZACION" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_FECHAFIN_LOCALIZACION_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_FECHAFIN_LOCALIZACION',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_FECHAFIN_LOCALIZACION',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_DISTANCIA_JALON")); } catch (MissingResourceException e) { out.print("Distancia (km) desde el campamento al jalón de la parcela." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_DISTANCIA_JALON" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_DISTANCIA_JALON"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_DISTANCIA_JALON_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_MEDIOACCESO_JALON")); } catch (MissingResourceException e) { out.print("Medio de acceso desde el campamento al jalón de la parcela." + ":"); } %>
</div>
<select class="form-control" name="PRCL_MEDIOACCESO_JALON" onblur="validar(this.form)">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_medioacceso_jalon"), "").toString()); 
%>
</select>
<div class="div_mensaje_dato_invalido" id="PRCL_MEDIOACCESO_CAMPAMENTO_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TJALON_H")); } catch (MissingResourceException e) { out.print("Tiempo de viaje (horas) desde el campamento al jalón de la parcela." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_TJALON_H" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TJALON_H"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_TJALON_H_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TJALON_M")); } catch (MissingResourceException e) { out.print("Tiempo de viaje (minutos) desde el campamento al jalón de la parcela." + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_TJALON_M" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TJALON_M"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_TJALON_M_MSG"></div>
</div><div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_DISTANCIA_CAMPAMENTOS")); } catch (MissingResourceException e) { out.print("Distancia (km) entre campamentos" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_DISTANCIA_CAMPAMENTOS" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_DISTANCIA_CAMPAMENTOS"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_DISTANCIA_CAMPAMENTOS_MSG"></div>
</div>

</div>


<!-- DATOS GEOGRÁFICOS -->
<div id="div_subtitulo__datos_geograficos" style="clear: both;" onclick="javascript:DIVisibilidad('div__datos_geograficos');">
<h3>
<% try { out.print(msj.getString(yo+"Datos_Geograficos")); } catch (MissingResourceException e) { out.print("Datos Geográficos" + ":"); } %>
</h3>
</div>

<div id="div__datos_geograficos" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("General.PNN")); } catch (MissingResourceException e) { out.print("Parque Nacional Natural" + ":"); } %>
</div>
<%
String PNN = Auxiliar.nzObjStr(request.getAttribute("PNN"), "").toString();
%>
<%= PNN %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("General.TIPOBOSQUE")); } catch (MissingResourceException e) { out.print("Tipo de Bosque (Holdridge)" + ":"); } %>
</div>
<%
String TIPOBOSQUE = Auxiliar.nzObjStr(request.getAttribute("TIPOBOSQUE"), "").toString();
%>
<%= TIPOBOSQUE %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("General.CAR")); } catch (MissingResourceException e) { out.print("Corporación Autónoma Regional" + ":"); } %>
</div>
<%
String CAR = Auxiliar.nzObjStr(request.getAttribute("CAR"), "").toString();
%>
<%= CAR %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("General.RESGUARDOINDIGENA")); } catch (MissingResourceException e) { out.print("Resguardo Indigena" + ":"); } %>
</div>
<%
String RESGUARDOINDIGENA = Auxiliar.nzObjStr(request.getAttribute("RESGUARDOINDIGENA"), "").toString();
%>
<%= RESGUARDOINDIGENA %>
</div>

<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString("General.Pais")); } catch (MissingResourceException e) { out.print("País" + ":"); } 
%>
</div>
<select class="form-control" name="PRCL_CONS_PAIS" id="PRCL_CONS_PAIS" onchange="ajaxLoader('Auxiliar?accion=ajax_departamentos&opcionVacia=true','departamentos_seleccionados','div_feedback_departamentos','','PRCL_CONS_PAIS','','No se encontraron departamentos.');seleccionarTodasLasOpciones('departamentos_seleccionados');quitarOpcionesSeleccionadas('departamentos_seleccionados');seleccionarTodasLasOpciones('PRCL_MUNICIPIO');quitarOpcionesSeleccionadas('PRCL_MUNICIPIO');" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_pais"), "").toString()); 
%>
</select>
</div>

<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString("General.Departamento")); } catch (MissingResourceException e) { out.print("Departamento" + ":"); } 
%>
</div>
<div class="select_derecho">
<select class="form-control" name="PRCL_DEPARTAMENTO" id="departamentos_seleccionados" onchange="seleccionarTodasLasOpciones('PRCL_MUNICIPIO');quitarOpcionesSeleccionadas('PRCL_MUNICIPIO');consultarMunicipios();">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_departamento"), "").toString()); 
%>
</select>
</div>
<div id="div_feedback_departamentos"></div>
</div>

<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString("General.Municipio")); } catch (MissingResourceException e) { out.print("Municipio" + ":"); } 
%>
</div>
<div class="select_derecho">
<select class="form-control" name="PRCL_MUNICIPIO" id="PRCL_MUNICIPIO" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_municipio"), "").toString()); 
%>
</select>
</div>
<div id="div_feedback_municipios"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_LATITUD")); } catch (MissingResourceException e) { out.print("Latitud" + " "); } %>
</div>
<input class="form-control" type=text name="PRCL_LATITUD" id="PRCL_LATITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_LATITUD"), "").toString() %>" onchange="validar(this.form)"/>
<% if (es_movil) { %>
	<input type="button" value="GPS" onclick="gps(this.form)" />
<% } %>
<div class="div_mensaje_dato_invalido" id="PRCL_LATITUD_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_LONGITUD")); } catch (MissingResourceException e) { out.print("Longitud" + " "); } %>
</div>
<input class="form-control" type=text name="PRCL_LONGITUD" id="PRCL_LONGITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_LONGITUD"), "").toString() %>" onchange="validar(this.form)"/>
<% if (es_movil) { %>
	<input type="button" value="GPS" onclick="gps(this.form)" />
<% } %>
<div class="div_mensaje_dato_invalido" id="PRCL_LONGITUD_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_ALTITUD")); } catch (MissingResourceException e) { out.print("Altitud" + " "); } %>
</div>
<input class="form-control" type=text name="PRCL_ALTITUD" id="PRCL_ALTITUD" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_ALTITUD"), "").toString() %>" onchange="validar(this.form)"/>
<% if (es_movil) { %>
	<input type="button" value="GPS" onclick="gps(this.form)" />
<% } %>
<div class="div_mensaje_dato_invalido" id="PRCL_ALTITUD_MSG"></div>
</div>

</div>

<!-- SPF1 -->
<div id="div_subtitulo__SPF1" style="clear: both;" onclick="javascript:DIVisibilidad('div__SPF1');">
<h3>
<% try { out.print(msj.getString("General.SPF1")); } catch (MissingResourceException e) { out.print("Sub-parcela Fustal (SPF) 1" + ":"); } %>
</h3>
</div>

<div id="div__SPF1" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_DILIGENCIA")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia SPF1" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_SPF1_DILIGENCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_DILIGENCIA"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_FECHAINI")); } catch (MissingResourceException e) { out.print("Fecha inicial de levantamiento de SPF1" + ":"); } %>
</div>
<input type=text name="PRCL_SPF1_FECHAINI" id="PRCL_SPF1_FECHAINI" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_FECHAINI"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF1_FECHAINI" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF1_FECHAINI_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF1_FECHAINI',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF1_FECHAINI',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_FECHAFIN")); } catch (MissingResourceException e) { out.print("Fecha final de levantamiento de SPF1" + ":"); } %>
</div>
<input type=text name="PRCL_SPF1_FECHAFIN" id="PRCL_SPF1_FECHAFIN" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_FECHAFIN"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF1_FECHAFIN" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF1_FECHAFIN_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF1_FECHAFIN',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF1_FECHAFIN',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_POSIBLE")); } catch (MissingResourceException e) { out.print("Fue posible establecer SPF1?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_SPF1_POSIBLE" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_posible_SPF1"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_JUSTIFICACION_NO")); } catch (MissingResourceException e) { out.print("Justificación por la cual no fue posible establecer SPF1" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF1_JUSTIFICACION_NO"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_JUSTIFICACION_NO"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_OBS_FUSTALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela fustal en SPF1" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF1_OBS_FUSTALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_OBS_FUSTALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_OBS_LATIZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela latizal en SPF1" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF1_OBS_LATIZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_OBS_LATIZALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF1_OBS_BRINZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela brinzal en SPF1" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF1_OBS_BRINZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF1_OBS_BRINZALES"), "").toString() %></textarea>
</div>

</div>
<!-- SPF2 -->
<div id="div_subtitulo__SPF2" style="clear: both;" onclick="javascript:DIVisibilidad('div__SPF2');">
<h3>
<% try { out.print(msj.getString("General.SPF2")); } catch (MissingResourceException e) { out.print("Sub-parcela Fustal (SPF) 2" + ":"); } %>
</h3>
</div>

<div id="div__SPF2" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_DILIGENCIA")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia SPF2" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_SPF2_DILIGENCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_DILIGENCIA"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_FECHAINI")); } catch (MissingResourceException e) { out.print("Fecha inicial de levantamiento de SPF2" + ":"); } %>
</div>
<input type=text name="PRCL_SPF2_FECHAINI" id="PRCL_SPF2_FECHAINI" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_FECHAINI"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF2_FECHAINI" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF2_FECHAINI_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF2_FECHAINI',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF2_FECHAINI',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_FECHAFIN")); } catch (MissingResourceException e) { out.print("Fecha final de levantamiento de SPF2" + ":"); } %>
</div>
<input type=text name="PRCL_SPF2_FECHAFIN" id="PRCL_SPF2_FECHAFIN" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_FECHAFIN"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF2_FECHAFIN" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF2_FECHAFIN_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF2_FECHAFIN',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF2_FECHAFIN',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_POSIBLE")); } catch (MissingResourceException e) { out.print("Fue posible establecer SPF2?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_SPF2_POSIBLE" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_posible_SPF2"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_JUSTIFICACION_NO")); } catch (MissingResourceException e) { out.print("Justificación por la cual no fue posible establecer SPF2" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF2_JUSTIFICACION_NO"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_JUSTIFICACION_NO"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_OBS_FUSTALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela fustal en SPF2" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF2_OBS_FUSTALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_OBS_FUSTALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_OBS_LATIZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela latizal en SPF2" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF2_OBS_LATIZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_OBS_LATIZALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF2_OBS_BRINZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela brinzal en SPF2" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF2_OBS_BRINZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF2_OBS_BRINZALES"), "").toString() %></textarea>
</div>

</div>
<!-- SPF3 -->
<div id="div_subtitulo__SPF3" style="clear: both;" onclick="javascript:DIVisibilidad('div__SPF3');">
<h3>
<% try { out.print(msj.getString("General.SPF3")); } catch (MissingResourceException e) { out.print("Sub-parcela Fustal (SPF) 3" + ":"); } %>
</h3>
</div>

<div id="div__SPF3" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_DILIGENCIA")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia SPF3" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_SPF3_DILIGENCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_DILIGENCIA"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_FECHAINI")); } catch (MissingResourceException e) { out.print("Fecha inicial de levantamiento de SPF3" + ":"); } %>
</div>
<input type=text name="PRCL_SPF3_FECHAINI" id="PRCL_SPF3_FECHAINI" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_FECHAINI"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF3_FECHAINI" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF3_FECHAINI_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF3_FECHAINI',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF3_FECHAINI',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_FECHAFIN")); } catch (MissingResourceException e) { out.print("Fecha final de levantamiento de SPF3" + ":"); } %>
</div>
<input type=text name="PRCL_SPF3_FECHAFIN" id="PRCL_SPF3_FECHAFIN" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_FECHAFIN"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF3_FECHAFIN" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF3_FECHAFIN_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF3_FECHAFIN',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF3_FECHAFIN',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_POSIBLE")); } catch (MissingResourceException e) { out.print("Fue posible establecer SPF3?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_SPF3_POSIBLE" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_posible_SPF3"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_JUSTIFICACION_NO")); } catch (MissingResourceException e) { out.print("Justificación por la cual no fue posible establecer SPF3" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF3_JUSTIFICACION_NO"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_JUSTIFICACION_NO"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_OBS_FUSTALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela fustal en SPF3" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF3_OBS_FUSTALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_OBS_FUSTALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_OBS_LATIZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela latizal en SPF3" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF3_OBS_LATIZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_OBS_LATIZALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF3_OBS_BRINZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela brinzal en SPF3" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF3_OBS_BRINZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF3_OBS_BRINZALES"), "").toString() %></textarea>
</div>

</div>
<!-- SPF4 -->
<div id="div_subtitulo__SPF4" style="clear: both;" onclick="javascript:DIVisibilidad('div__SPF4');">
<h3>
<% try { out.print(msj.getString("General.SPF4")); } catch (MissingResourceException e) { out.print("Sub-parcela Fustal (SPF) 4" + ":"); } %>
</h3>
</div>

<div id="div__SPF4" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_DILIGENCIA")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia SPF4" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_SPF4_DILIGENCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_DILIGENCIA"), "").toString() %>" />
</div>


<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_FECHAINI")); } catch (MissingResourceException e) { out.print("Fecha inicial de levantamiento de SPF4" + ":"); } %>
</div>
<input type=text name="PRCL_SPF4_FECHAINI" id="PRCL_SPF4_FECHAINI" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_FECHAINI"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF4_FECHAINI" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF4_FECHAINI_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF4_FECHAINI',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF4_FECHAINI',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_FECHAFIN")); } catch (MissingResourceException e) { out.print("Fecha final de levantamiento de SPF4" + ":"); } %>
</div>
<input type=text name="PRCL_SPF4_FECHAFIN" id="PRCL_SPF4_FECHAFIN" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_FECHAFIN"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF4_FECHAFIN" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF4_FECHAFIN_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF4_FECHAFIN',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF4_FECHAFIN',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_POSIBLE")); } catch (MissingResourceException e) { out.print("Fue posible establecer SPF4?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_SPF4_POSIBLE" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_posible_SPF4"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_JUSTIFICACION_NO")); } catch (MissingResourceException e) { out.print("Justificación por la cual no fue posible establecer SPF4" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF4_JUSTIFICACION_NO"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_JUSTIFICACION_NO"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_OBS_FUSTALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela fustal en SPF4" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF4_OBS_FUSTALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_OBS_FUSTALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_OBS_LATIZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela latizal en SPF4" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF4_OBS_LATIZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_OBS_LATIZALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF4_OBS_BRINZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela brinzal en SPF4" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF4_OBS_BRINZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF4_OBS_BRINZALES"), "").toString() %></textarea>
</div>

</div>
<!-- SPF5 -->
<div id="div_subtitulo__SPF5" style="clear: both;" onclick="javascript:DIVisibilidad('div__SPF5');">
<h3>
<% try { out.print(msj.getString("General.SPF5")); } catch (MissingResourceException e) { out.print("Sub-parcela Fustal (SPF) 5" + ":"); } %>
</h3>
</div>

<div id="div__SPF5" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_DILIGENCIA")); } catch (MissingResourceException e) { out.print("Cédula de ciudadanía de quien diligencia SPF5" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_SPF5_DILIGENCIA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_DILIGENCIA"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_FECHAINI")); } catch (MissingResourceException e) { out.print("Fecha inicial de levantamiento de SPF5" + ":"); } %>
</div>
<input type=text name="PRCL_SPF5_FECHAINI" id="PRCL_SPF5_FECHAINI" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_FECHAINI"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF5_FECHAINI" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF5_FECHAINI_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF5_FECHAINI',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF5_FECHAINI',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_FECHAFIN")); } catch (MissingResourceException e) { out.print("Fecha final de levantamiento de SPF5" + ":"); } %>
</div>
<input type=text name="PRCL_SPF5_FECHAFIN" id="PRCL_SPF5_FECHAFIN" value='<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_FECHAFIN"), "").toString() %>' onchange="validar(this.form)" />
<input type=button id="b_PRCL_SPF5_FECHAFIN" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ":"); } %>' class="btn btn-default" />
<div class="div_mensaje_dato_invalido" id="PRCL_SPF5_FECHAFIN_MSG"></div>
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'PRCL_SPF5_FECHAFIN',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_PRCL_SPF5_FECHAFIN',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_POSIBLE")); } catch (MissingResourceException e) { out.print("Fue posible establecer SPF5?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_SPF5_POSIBLE" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_posible_SPF5"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_JUSTIFICACION_NO")); } catch (MissingResourceException e) { out.print("Justificación por la cual no fue posible establecer SPF5" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF5_JUSTIFICACION_NO"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_JUSTIFICACION_NO"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_OBS_FUSTALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela fustal en SPF5" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF5_OBS_FUSTALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_OBS_FUSTALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_OBS_LATIZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela latizal en SPF5" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF5_OBS_LATIZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_OBS_LATIZALES"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_SPF5_OBS_BRINZALES")); } catch (MissingResourceException e) { out.print("Observaciones sobre la parcela brinzal en SPF5" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 name="PRCL_SPF5_OBS_BRINZALES"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_SPF5_OBS_BRINZALES"), "").toString() %></textarea>
</div>

</div>

<div id="div_subtitulo__campos_importacion" style="clear: both;" onclick="javascript:DIVisibilidad('div__campos_importacion');">
<h3>
<% try { out.print(msj.getString(yo+"CAMPOS_IMPORTACION")); } catch (MissingResourceException e) { out.print("Otros Datos de Importación" + ":"); } %>
</h3>
</div>

<div id="div__campos_importacion" style="display: none;">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_PLOT")); } catch (MissingResourceException e) { out.print("Código plot" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_PLOT" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_PLOT"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_PLOT_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_AREA")); } catch (MissingResourceException e) { out.print("Área de la Parcela en Hectáreas" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_AREA" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_AREA"), "").toString() %>" onchange="validar(this.form)"/>
<div class="div_mensaje_dato_invalido" id="PRCL_AREA_MSG"></div>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_INCLUIR")); } catch (MissingResourceException e) { out.print("Incluir Parcela en Cálculos" + ":"); } %>
</div>
<select class="form-control" name="PRCL_INCLUIR" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_incluir"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TEMPORALIDAD")); } catch (MissingResourceException e) { out.print("Temporalidad (tipo) de Parcela" + ":"); } %>
</div>
<select class="form-control" name="PRCL_TEMPORALIDAD" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_temporalidad"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_PUBLICA")); } catch (MissingResourceException e) { out.print("Parcela es Pública" + ":"); } %>
</div>
<select class="form-control" name="PRCL_PUBLICA" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_publica"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_HAB")); } catch (MissingResourceException e) { out.print("Se registró una disminución >20% de la biomasa aérea al excluir individuos no-arbóreos?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_HAB" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_hab"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_DAP")); } catch (MissingResourceException e) { out.print("La parcela presenta una distribución diamétrica anómala?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_DAP" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_dap"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_GPS")); } catch (MissingResourceException e) { out.print("La diferencia entre la altitud reportada e interpolada es mayor o igual a 100 m.s.n.m.?" + ":"); } %>
</div>
<select class="form-control" name="PRCL_GPS" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_gps"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_EQ")); } catch (MissingResourceException e) { out.print("Ecuación Alométrica a Utilizar" + ":"); } %>
</div>
<select class="form-control" name="PRCL_EQ" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_eq"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_AUTORCUSTODIOINFO")); } catch (MissingResourceException e) { out.print("Número de Documento del Autor y Custodio de la Información" + ":"); } %>
</div>
<input class="form-control" type=text name="PRCL_AUTORCUSTODIOINFO" value="<%= Auxiliar.nzObjStr(request.getAttribute("PRCL_AUTORCUSTODIOINFO"), "").toString() %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("General.TIPOBOSQUE")); } catch (MissingResourceException e) { out.print("Tipo de Bosque (Estrato)" + ":"); } %>
</div>
<select class="form-control" name="PRCL_TIPOBOSQUE" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_tipobosque"), "").toString()); 
%>
</select>
</div>

</div><div id="div_subtitulo__tracklogs" style="clear: both;" onclick="javascript:DIVisibilidad('div__tracklogs');">
<h3>
<% try { out.print(msj.getString(yo+"TRACKLOGS")); } catch (MissingResourceException e) { out.print("TrackLogs" + ":"); } %>
</h3>
</div>

<div id="div__tracklogs" style="display: none;">

<div class="campo">
<div class="etiqueta"><% try { out.print(msj.getString(yo+"Doble_clic_agrega_coordenadas_a")); } catch (MissingResourceException e) { out.print("Doble clic agrega coordenada a" + ":"); } %>:</div>
<select id='tipotrack'>
<option value=''></option>
<option value='CAMPAMENTO'>Tracklog hacia el campamento</option>
<option value='PARCELA'>Tracklog hacia la parcela</option>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TRACKLOG_CAMPAMENTO")); } catch (MissingResourceException e) { out.print("Tracklog desde el caserio o corregimiento más cercano al campamento" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 id="PRCL_TRACKLOG_CAMPAMENTO" name="PRCL_TRACKLOG_CAMPAMENTO"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TRACKLOG_CAMPAMENTO"), "").toString() %></textarea>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("PRCL_TRACKLOG_PARCELA")); } catch (MissingResourceException e) { out.print("Tracklog desde el campamento a la parcela" + ":"); } %>
</div>
<textarea class="form-control" cols=36 rows=3 id="PRCL_TRACKLOG_PARCELA" name="PRCL_TRACKLOG_PARCELA"><%= Auxiliar.nzObjStr(request.getAttribute("PRCL_TRACKLOG_PARCELA"), "").toString() %></textarea>
</div>

</div>


<div class="botones" style="clear: both;">

<!-- 
<input type="button" onclick='javascript:coordenadas(this.form);' value='
<% 
// try { out.print(msj.getString("General.Editar_Coordenadas")); } catch (MissingResourceException e) { out.print("Editar Coordenadas" + ":"); } 
%>' class="btn btn-default" />
<input type="button" onclick='javascript:contactos(this.form);' value='
<% 
// try { out.print(msj.getString("General.Editar_Contactos")); } catch (MissingResourceException e) { out.print("Editar Contactos" + ":"); } 
%>' class="btn btn-default" />
-->
<input type="button" onclick='javascript:editar(this.form);' value='<% try { out.print(msj.getString("General.Guardar")); } catch (MissingResourceException e) { out.print("Guardar" + ":"); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:eliminar(this.form);' value='<% try { out.print(msj.getString("General.Eliminar")); } catch (MissingResourceException e) { out.print("Eliminar" + ":"); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:nueva(this.form);' value='<% try { out.print(msj.getString("General.Nueva")); } catch (MissingResourceException e) { out.print("Nueva" + ":"); } %>' class="btn btn-default" />
<!-- 
<input type="button" onclick='javascript:exportar_a_excel(this.form);' value='<% try { out.print(msj.getString("General.Exportar_a_Excel")); } catch (MissingResourceException e) { out.print("Exportar a Excel" + ":"); } %>' class="btn btn-default" />
-->

</div>

<input type="hidden" name="accion" value="" />

<div style="display: inline-block;">
<div class="opcionmenu">
<a class=boton href='Individuo?accion=buscar&PRCL_CONSECUTIVO=<%=PRCL_CONSECUTIVO %>' target='_blank'>
<% try { out.print(msj.getString("General.Individuos")); } catch (MissingResourceException e) { out.print("Individuos de esta Parcela" + ":"); } %> 
</a>
</div>
</div>

<div style="display: inline-block;">
<div class="opcionmenu">
<% 
//out.print(Auxiliar.nzObjStr(request.getAttribute("enlace_pdf_parcela"), "").toString()); 
%>
<%
String usuarioAceptoLicencia = Auxiliar.nzObjStr(request.getAttribute("usuarioAceptoLicencia"), "");
String indicacionLicencia = Auxiliar.nzObjStr(request.getAttribute("indicacionLicencia"), "");
if (!usuarioAceptoLicencia.equals("1")) { 
%>
	<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=indicacionLicencia %></div>
<% } else { %>
<a class=boton href='Parcela?accion=exportar_pdf&PRCL_CONSECUTIVO=<%=PRCL_CONSECUTIVO %>' target='_blank'>
<% try { out.print(msj.getString("General.EXPORTAR_PARCELA_PDF")); } catch (MissingResourceException e) { out.print("Exportar a PDF" + ":"); } %> 
</a>
<% } %>
</div>
</div>

<div style="display: inline-block;">
<div class="opcionmenu">
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("enlace_metadato_parcela"), "").toString()); 
%>
</div>
</div>

<div style="display: inline-block;">
<div class="opcionmenu">
<a class=boton href="Parcela?accion=visualizar_metadato&PRCL_CONSECUTIVO=<%=PRCL_CONSECUTIVO %>" target=_blank><% try { out.print(msj.getString("General.Visualizar_Metadato")); } catch (MissingResourceException e) { out.print("Visualizar Metadato" + ".."); } %></a>
</div>
</div>

</form>

<div id="div_titulo__visor" onclick="javascript:DIVisibilidad('div__visor');">
<h4>
<% try { out.print(msj.getString("General.Visor")); } catch (MissingResourceException e) { out.print("Visor" + ".."); } %>
</h4>
</div>

<div id="div__visor">
<div id="div_coordenadas"></div>
<div id="map" style="width: 100%; height: 400px"></div>
</div>

</div>


<!--<script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>-->

<script type="text/javascript" src="js/leaflet.js"></script>

<script type="text/javascript">

var a_coordenadas = Array();

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

var map = L.map('map', {maxZoom:30, minZoom: 5}).setView([PRCL_LATITUD, PRCL_LONGITUD], 16);

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

/*
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

if (db_PRCL_LATITUD != '' && db_PRCL_LONGITUD != '') {
	a_coordenadas = [db_PRCL_LATITUD, db_PRCL_LONGITUD];
	jalon = L.icon({
	    iconUrl: 'mapas_parcelas/images/jalon-icon.png',
	    shadowUrl: 'mapas_parcelas/images/jalon-shadow.png',

	    iconSize:     [10, 20], // size of the icon
	    shadowSize:   [20, 20], // size of the shadow
	    iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location
	    shadowAnchor: [5, 20],  // the same for the shadow
	    popupAnchor:  [0, -20] // point from which the popup should open relative to the iconAnchor
	});

	L.marker(a_coordenadas, {icon: jalon}).addTo(map);
}

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

function eventoTrackLog(e) {
	agregarTrackLog(e.latlng.lat, e.latlng.lng);
}
map.on('dblclick', eventoTrackLog);

var factor = 0.0000045;


//var bounds = [[PRCL_LATITUD+(95*factor), PRCL_LONGITUD-(95*factor)], [PRCL_LATITUD-(95*factor), PRCL_LONGITUD+(95*factor)]];
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


<%
String track_campamento = Auxiliar.nzObjStr(request.getAttribute("track_campamento"), "").toString();
String track_parcela = Auxiliar.nzObjStr(request.getAttribute("track_parcela"), "").toString();
%>

var track_campamento = [<%= track_campamento %>];
var track_parcela = [<%= track_parcela %>];

var polyline_campamento = L.polyline(track_campamento, {color: 'red'}).addTo(map);
var polyline_parcela = L.polyline(track_parcela, {color: 'blue'}).addTo(map);

if (track_campamento.length > 2) {
	polyline_campamento = L.polyline(track_campamento, {color: 'red'}).addTo(map);
}
if (track_parcela.length > 2) {
	polyline_parcela = L.polyline(track_parcela, {color: 'blue'}).addTo(map);
}


function trackear(tipo, tracklog) {
	var a_tracklog = tracklog.split('_');

	var n_tracklog = a_tracklog.length;
	
	var i = 0;

	var lat = 0;
	var lon = 0;	

	var a_latlon = new Array();
	
	for (i=0; i<n_tracklog; i++) {
		var a_coordenadas = a_tracklog[i].split(',');

		if (a_coordenadas.length == 2) {
			lat = a_coordenadas[0] * 1.0;
			lon = a_coordenadas[1] * 1.0;
			a_latlon[i] = L.latLng(lat, lon);	
		}		
	} 

	if (tipo == 'CAMPAMENTO') {
		if (!polyline_campamento) {
			polyline_campamento = L.polyline(track_campamento, {color: 'red'}).addTo(map);
		}
		polyline_campamento.setLatLngs(a_latlon);
	}

	if (tipo == 'PARCELA') {
		if (!polyline_parcela) {
			polyline_parcela = L.polyline(track_parcela, {color: 'blue'}).addTo(map);
		}
		polyline_parcela.setLatLngs(a_latlon);
	}
} 


<%
String str_individuos = Auxiliar.nzObjStr(request.getAttribute("str_individuos"), "").toString();
%>

var str_individuos = '<%= str_individuos %>';

var a_individuos = str_individuos.split('_');

var n_individuos = a_individuos.length;

var i = 0;

for (i=0; i<n_individuos; i++) {
	a_individuo = a_individuos[i].split(':=:');

	if (a_individuo.length == 4) {
		numero_arbol = a_individuo[0];
		INDV_CONSECUTIVO = a_individuo[1];
		lat = a_individuo[2];
		lon = a_individuo[3];
	
		etiqueta_individuo = "<a href='Individuo?accion=detalle_individuo&INDV_PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+"&INDV_CONSECUTIVO="+INDV_CONSECUTIVO+"' target=_blank>"+numero_arbol+"</a>";
		
		if (lat != '' && lon != '') {
			a_coordenadas = [lat, lon];
			arbolito = L.icon({
			    iconUrl: 'mapas_individuos/images/marker-icon.png',
			    shadowUrl: 'mapas_individuos/images/marker-shadow.png',
		
			    iconSize:     [10, 20], // size of the icon
			    shadowSize:   [20, 20], // size of the shadow
			    iconAnchor:   [5, 20], // point of the icon which will correspond to marker's location
			    shadowAnchor: [5, 20],  // the same for the shadow
			    popupAnchor:  [0, -20] // point from which the popup should open relative to the iconAnchor
			});
		
			L.marker(a_coordenadas, {icon: arbolito}).addTo(map).bindPopup(etiqueta_individuo);
		}
	}
}

var usuario = '<% out.print(request.getParameter("usuario"));%>'; 
var idioma = '<% out.print(request.getParameter("idioma"));%>'; 
var PRCL_CONSECUTIVO = '<% out.print(request.getParameter("PRCL_CONSECUTIVO"));%>'; 

</script><div>

<% 
if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	String url_imagenes = "ImagenesParcela?accion=imagenes_parcela&control=&PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_imagenes"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_imagenes');">
<h1>
<% try { out.print(msj.getString(yo+"Imagenes_de_Parcela")); } catch (MissingResourceException e) { out.print("Fotos de la Parcela" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_imagenes" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_imagenes" id="iframe_imagenes" src="<%= url_imagenes %>"></iframe>
</div>
<% } %>

<%
if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	String url_carguemasivo = "registro_archivo.jsp?&PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_carguemasivo"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_carguemasivo');">
<h1>
<% try { out.print(msj.getString(yo+"IMPORTACION_DE_INDIVIDUOS")); } catch (MissingResourceException e) { out.print("Importación de Individuos" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_carguemasivo" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_carguemasivo" id="iframe_carguemasivo" src="<%= url_carguemasivo %>"></iframe>
</div>
<% } %>

<%
if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	String url_biomasacarbono = "BiomasaCarbonoParcela?accion=&BMCR_CONS_PARCELA=" + PRCL_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_biomasacarbono"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_biomasacarbono');">
<h1>
<% try { out.print(msj.getString(yo+"BiomasaCarbono_de_Parcela")); } catch (MissingResourceException e) { out.print("Biomasa-Carbono de Parcela" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_biomasacarbono" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_biomasacarbono" id="iframe_biomasacarbono" src="<%= url_biomasacarbono %>"></iframe>
</div>
<% } %>

<%
if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	String url_contactos = "Contacto?accion=&CNPR_CONS_PARCELA=" + PRCL_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_contactos"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_contactos');">
<h1>
<% try { out.print(msj.getString(yo+"Contactos_de_Parcela")); } catch (MissingResourceException e) { out.print("Contactos de Parcela" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_contactos" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_contactos" id="iframe_contactos" src="<%= url_contactos %>"></iframe>
</div>
<% } %>

<%
if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	String url_cobertura = "Cobertura?accion=&CBRT_PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO; 
%>
<div id="div_titulo__iframe_subformulario_cobertura"  onclick="javascript:DIVisibilidad('div__iframe_subformulario_cobertura');">
<h1>
<% try { out.print(msj.getString(yo+"Porcentajes_de_Cobertura")); } catch (MissingResourceException e) { out.print("Porcentajes de Cobertura" + ":"); } %>
</h1>
</div>
<div id="div__iframe_subformulario_cobertura" class="div_iframe_subformulario" style="display: none;">
<iframe class="iframe_subformulario_cobertura" id="iframe_cobertura" src="<%= url_cobertura %>"></iframe>
</div>
<% } %>


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