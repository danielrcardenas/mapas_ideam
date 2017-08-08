<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
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
<% try { out.print(msj.getString(yo+"BiomasaCarbono_de_Parcela")); } catch (MissingResourceException e) { out.print("Biomasa-Carbono de Parcela" + ".."); } %>
</title>

<link rel='stylesheet' type='text/css' media='all' href='js/jscalendar/calendar-win2k-cold-1.css' title='win2k-cold-1' />
 
<script type='text/javascript' src='js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='js/jscalendar/calendar-setup.js'></script>

<script type='text/javascript' src='js/jquery.min.js'></script>
<script src="js/smartpaginator/smartpaginator.js" type="text/javascript"></script>
<link href="js/smartpaginator/smartpaginator.css" rel="stylesheet" type="text/css" />


<%
String BMCR_CONS_PARCELA = Auxiliar.nzObjStr(request.getAttribute("BMCR_CONS_PARCELA"), "").toString();

String BMCR_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("BMCR_CONSECUTIVO"), "").toString();
String BMCR_BIOMASA = Auxiliar.nzObjStr(request.getAttribute("BMCR_BIOMASA"), "").toString();
String BMCR_CARBONO = Auxiliar.nzObjStr(request.getAttribute("BMCR_CARBONO"), "").toString();
String BMCR_AREABASALPROME = Auxiliar.nzObjStr(request.getAttribute("BMCR_AREABASALPROME"), "").toString();
String BMCR_AREABASALTOTAL = Auxiliar.nzObjStr(request.getAttribute("BMCR_AREABASALTOTAL"), "").toString();
String BMCR_VOLUMENTOTAL = Auxiliar.nzObjStr(request.getAttribute("BMCR_VOLUMENTOTAL"), "").toString();
String BMCR_FECHA_INICIO = Auxiliar.nzObjStr(request.getAttribute("BMCR_FECHA_INICIO"), "").toString();

String n_registros = Auxiliar.nzObjStr(request.getAttribute("n_registros"), "").toString();

%>

<script type='text/javascript'>

function nueva(f) {
	url = 'BiomasaCarbonoParcela?accion=&BMCR_CONS_PARCELA=<%= BMCR_CONS_PARCELA %>';
	location.href = url;
}

function editar(f) {
	if (validar(f))	{
		if (f.BMCR_CONS_PARCELA.value == '') {
			alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_consecutivo_parcela")); } catch (MissingResourceException e) { out.print("Por favor especifique el consecutivo de la parcela" + ".."); } %>.');
		}
		else {
			f.accion.value = 'guardar';
			f.submit();
		}
	}
}

function validar(f) {
	var go = true;

	if (f.BMCR_CONS_PARCELA.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_consecutivo_parcela")); } catch (MissingResourceException e) { out.print("Por favor especifique el consecutivo de la parcela" + ".."); } %>.');
		f.BMCR_CONS_PARCELA.focus();
		go = false;
	}

	if (f.calcular.checked == false) {
		if (f.BMCR_BIOMASA.value == '') {
			alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_BIOMASA")); } catch (MissingResourceException e) { out.print("Por favor especifique el valor de biomasa" + ".."); } %>.');
			f.BMCR_BIOMASA.focus();
			go = false;
		}
		
		if (f.BMCR_CARBONO.value == '') {
			alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_CARBONO")); } catch (MissingResourceException e) { out.print("Por favor especifique el valor de carbono" + ".."); } %>.');
			f.BMCR_CARBONO.focus();
			go = false;
		}
		
		if (f.BMCR_AREABASALPROME.value == '') {
			alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_AREABASALPROME")); } catch (MissingResourceException e) { out.print("Por favor especifique el área basal promedio" + ".."); } %>.');
			f.BMCR_AREABASALPROME.focus();
			go = false;
		}
		
		if (f.BMCR_AREABASALTOTAL.value == '') {
			alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_AREABASALTOTAL")); } catch (MissingResourceException e) { out.print("Por favor especifique el área basal total" + ".."); } %>.');
			f.BMCR_AREABASALTOTAL.focus();
			go = false;
		}
		
		if (f.BMCR_VOLUMENTOTAL.value == '') {
			alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_VOLUMENTOTAL")); } catch (MissingResourceException e) { out.print("Por favor especifique el volumen total" + ".."); } %>.');
			f.BMCR_VOLUMENTOTAL.focus();
			go = false;
		}
	}
		
	if (f.BMCR_TIPOGENERA.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_TIPOGENERA")); } catch (MissingResourceException e) { out.print("Por favor especifique el tipo de generación" + ".."); } %>.');
		f.BMCR_TIPOGENERA.focus();
		go = false;
	}

	/*
	if (f.BMCR_FECHA_INICIO.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_BMCR_FECHA_INICIO")); } catch (MissingResourceException e) { out.print("Por favor especifique el inicio de vigencia de los valores" + ".."); } %>.');
		f.BMCR_FECHA_INICIO.focus();
		go = false;
	}
	*/
	
	return go;
}

function eliminar(f) {
	if (f.BMCR_CONSECUTIVO.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_consecutivo_parcela")); } catch (MissingResourceException e) { out.print("Por favor especifique el consecutivo de la parcela" + ".."); } %>.');
	}
	else {
		if (confirm('<% try { out.print(msj.getString(yo+"Confirma_la_eliminacion_del_elemento_seleccionado")); } catch (MissingResourceException e) { out.print("Confirma la eliminación del elemento seleccionado" + ".."); } %>')) {
			f.accion.value = 'eliminar';
			f.submit();
		}
	}
}

function alertarCalcular(checked) {
	if (checked) {
		alert('<% try { out.print(msj.getString(yo+"ALERTA_CALCULAR_BIOMASA")); } catch (MissingResourceException e) { out.print("Al calcular la biomasa todos los datos de biomasa especificados en este formulario serán descartados" + ".."); } %>.');
	}
}

$(document).ready(function() {
    $('#paginas').smartpaginator({ 
        totalrecords: <%= n_registros %>, 
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
});
</script>

</head>
<body class="iframe">

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

<% 
out.println(request.getAttribute("retorno")); 
%>

<form action='BiomasaCarbonoParcela' method='post'>

<div class="form">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Id. del registro" + ".."); } %>
</div>
<input type=hidden name="BMCR_CONSECUTIVO" value="<%= BMCR_CONSECUTIVO %>" />
<%= BMCR_CONSECUTIVO %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_BIOMASA")); } catch (MissingResourceException e) { out.print("Biomasa" + ".."); } %>
</div>
<input type=text name="BMCR_BIOMASA" value="<%= BMCR_BIOMASA %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_CARBONO")); } catch (MissingResourceException e) { out.print("Carbono" + ".."); } %>
</div>
<input type=text name="BMCR_CARBONO" value="<%= BMCR_CARBONO %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_AREABASALPROME")); } catch (MissingResourceException e) { out.print("Área basal promedio" + ".."); } %>
</div>
<input type=text name="BMCR_AREABASALPROME" value="<%= BMCR_AREABASALPROME %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_AREABASALTOTAL")); } catch (MissingResourceException e) { out.print("Área basal total" + ".."); } %>
</div>
<input type=text name="BMCR_AREABASALTOTAL" value="<%= BMCR_AREABASALTOTAL %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_VOLUMENTOTAL")); } catch (MissingResourceException e) { out.print("Volumen total" + ".."); } %>
</div>
<input type=text name="BMCR_VOLUMENTOTAL" value="<%= BMCR_VOLUMENTOTAL %>" />
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_CONS_METODOLOGI")); } catch (MissingResourceException e) { out.print("Metodología" + ".."); } %>
</div>
<select class="form-control" name="BMCR_CONS_METODOLOGI" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_metodologia"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_TIPOGENERA")); } catch (MissingResourceException e) { out.print("Tipo de generación" + ".."); } %>
</div>
<select class="form-control" name="BMCR_TIPOGENERA" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_tipogeneracion"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("BMCR_CONS_ESTADOBIOM")); } catch (MissingResourceException e) { out.print("Estado" + ".."); } %>
</div>
<select class="form-control" name="BMCR_CONS_ESTADOBIOM" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_estadobiomasa"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("General.calcular")); } catch (MissingResourceException e) { out.print("Calcular y Registrar" + ".."); } %>
</div>
<input type=checkbox name="calcular" value="1" unchecked onchange="alertarCalcular(this.checked);" />
</div>

<div class=campo>
<div class="etiqueta">
<% 
try { out.print(msj.getString("BMCR_FECHA_INICIO")); } catch (MissingResourceException e) { out.print("Vigente desde" + ".."); } 
%>
</div>
<input type=text name="BMCR_FECHA_INICIO" id="BMCR_FECHA_INICIO" value='<%= BMCR_FECHA_INICIO %>' />
<input type=button id="b_BMCR_FECHA_INICIO" value='<% try { out.print(msj.getString("General.Elegir_Fecha")); } catch (MissingResourceException e) { out.print("Elegir Fecha" + ".."); } %>' class="btn btn-default" />
<script type='text/javascript'>
Calendar.setup({
	inputField     :    'BMCR_FECHA_INICIO',				// id of the input field
	ifFormat       :    '%Y-%m-%d',	// format of the input field
	showsTime      :    false,					// will display a time selector
	button         :    'b_BMCR_FECHA_INICIO',				// trigger for the calendar (button ID)
	singleClick    :    false,					// double-click mode
	step           :    1						// show all years in drop-down boxes (instead of every other year as default)
});
</script>
</div>

</div>

<div class="botones">
<input type="button" onclick='javascript:editar(this.form);' value='<% try { out.print(msj.getString("General.Guardar")); } catch (MissingResourceException e) { out.print("Guardar" + ".."); } %>' class="btn btn-default" />
<% if (Auxiliar.tieneAlgo(BMCR_CONSECUTIVO)) { %>
<input type="button" onclick='javascript:eliminar(this.form);' value='<% try { out.print(msj.getString("General.Eliminar")); } catch (MissingResourceException e) { out.print("Eliminar" + ".."); } %>' class="btn btn-default" />
<% } %>
<input type="button" onclick='javascript:nueva(this.form);' value='<% try { out.print(msj.getString("General.Nueva")); } catch (MissingResourceException e) { out.print("Nueva" + ".."); } %>' class="btn btn-default" />
</div>

<input type="hidden" name="BMCR_CONS_PARCELA" value="<%=BMCR_CONS_PARCELA %>" />
<input type="hidden" name="accion" value="" />
</form>

<div>

<h4>
<% try { out.print(msj.getString("General.Registros_BMC")); } catch (MissingResourceException e) { out.print("Registros de Biomasa y Carbono" + ".."); } %>
</h4>

<div class="tabla_registros" id="div_tabla_registros">

<% 
out.print(request.getAttribute("tabla_registros")); 
%>

</div>
<div id="paginas" style="margin: auto;"></div>


</div>

</body>
</html>