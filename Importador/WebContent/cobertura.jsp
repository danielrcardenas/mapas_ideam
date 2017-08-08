<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="co.gov.ideamredd.admif.Auxiliar" %>
<%
//co.gov.ideamredd.admif.Auxiliar aux = new co.gov.ideamredd.admif.Auxiliar();
LenguajeI18N L = new LenguajeI18N();
ResourceBundle msj = null;
String yo = "cobertura.";
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

String CBRT_PRCL_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("CBRT_PRCL_CONSECUTIVO"), "").toString();

%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<title>
<% try { out.print(msj.getString(yo+"Detalle_de_Cobertura")); } catch (MissingResourceException e) { out.print("Detalle de Coberturas" + " "); } %>
</title>

<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/ajax.js'></script>
<script type='text/javascript' src='js/ajax_opciones.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>

<script src="js/smartpaginator/smartpaginator.js" type="text/javascript"></script>
<link href="js/smartpaginator/smartpaginator.css" rel="stylesheet" type="text/css" />

<%
String n_registros = Auxiliar.nzObjStr(request.getAttribute("n_registros"), "").toString();
%>
<script type='text/javascript'>

$(document).ready(function(){
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-30) + 'px';
    <% } %>
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

function nueva(f) {
	url = 'Cobertura?accion=detalle&CBRT_PRCL_CONSECUTIVO=<%=CBRT_PRCL_CONSECUTIVO%>';
	location.href = url;
}

function editar(f) {
	if (validar(f))	{
		f.submit();
	}
}

function validar(f) {
	if (f.CBRT_PRCL_CONSECUTIVO.value == '') {
		alert('<% try { out.print(msj.getString("Por_favor_especifique_CBRT_PRCL_CONSECUTIVO")); } catch (MissingResourceException e) { out.print(" el código del individuo " + " "); } %>.');
		f.PRCL_CONSECUTIVO.focus();
		return false;
	}
	
	if (f.CBRT_TPCB_CONSECUTIVO.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_CBRT_TPCB_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Por favor especifique el tipo de cobertura" + " "); } %>.');
		f.CBRT_TPCB_CONSECUTIVO.focus();
		return false;
	}
	
	if (f.CBRT_TPER_ID.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_CBRT_TPER_ID")); } catch (MissingResourceException e) { out.print("Por favor especifique el tipo de perturbación" + " "); } %>.');
		f.CBRT_TPER_ID.focus();
		return false;
	}
	
	if (f.CBRT_SVPR_CONSECUTIVO.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_CBRT_SVPR_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Por favor especifique la severidad de perturbación" + " "); } %>.');
		f.CBRT_SVPR_CONSECUTIVO.focus();
		return false;
	}
	
	if (f.CBRT_SUBPARCELA.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_CBRT_SUBPARCELA")); } catch (MissingResourceException e) { out.print("Por favor especifique el número de subparcela" + " "); } %>.');
		f.CBRT_SUBPARCELA.focus();
		return false;
	}
	
	if (f.CBRT_PORCENTAJE.value == '') {
		alert('<% try { out.print(msj.getString(yo+"Por_favor_especifique_PORCENTAJE")); } catch (MissingResourceException e) { out.print("Por favor especifique el porcentaje" + " "); } %>.');
		f.CBRT_PORCENTAJE.focus();
		return false;
	}
	
	return true;
}

function eliminar(f) {
	if (confirm('<% try { out.print(msj.getString(yo+"Confirma_la_eliminacion_del_elemento_seleccionado")); } catch (MissingResourceException e) { out.print("Confirma la eliminación del elemento seleccionado" + " "); } %>')) {
		f.accion.value = 'eliminar';
		f.submit();
	}
}

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

<div id="content">
<div class="content-inner">

<% 
out.println(request.getAttribute("retorno")); 
%>

<form action='Cobertura' method='post'>

<div class="form">

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Consecutivo Cobertura" + " "); } %>
</div>
<%
String CBRT_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("CBRT_CONSECUTIVO"), "").toString();
%>
<input type=hidden name="CBRT_CONSECUTIVO" value="<%= CBRT_CONSECUTIVO %>" />
<%= CBRT_CONSECUTIVO %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_PRCL_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Consecutivo Parcela" + " "); } %>
</div>
<input type="hidden" name="CBRT_PRCL_CONSECUTIVO" value="<%=CBRT_PRCL_CONSECUTIVO%>" />
<%= CBRT_PRCL_CONSECUTIVO %>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_SUBPARCELA")); } catch (MissingResourceException e) { out.print("Subparcela" + " "); } %>
</div>
<select class="form-control" name="CBRT_SUBPARCELA" >
<%
String CBRT_SUBPARCELA = Auxiliar.nzObjStr(request.getAttribute("CBRT_SUBPARCELA"), "").toString();
%>
<option value="<%=CBRT_SUBPARCELA%>" selected><%=CBRT_SUBPARCELA%></option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
<option value="4">4</option>
<option value="5">5</option>
</select>
</div>


<div style="clear: both;"></div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_TPER_ID")); } catch (MissingResourceException e) { out.print("Tipo de Perturbación" + " "); } %>
</div>
<select class="form-control" name="CBRT_TPER_ID" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_tipoperturbacion"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_SVPR_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Severidad de la Perturbación" + " "); } %>
</div>
<select class="form-control" name="CBRT_SVPR_CONSECUTIVO" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_severidad"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_TPCB_CONSECUTIVO")); } catch (MissingResourceException e) { out.print("Tipo de Cobertura" + " "); } %>
</div>
<select class="form-control" name="CBRT_TPCB_CONSECUTIVO" >
<% 
out.print(Auxiliar.nzObjStr(request.getAttribute("opciones_tipocobertura"), "").toString()); 
%>
</select>
</div>

<div class="campo">
<div class="etiqueta">
<% try { out.print(msj.getString("CBRT_PORCENTAJE")); } catch (MissingResourceException e) { out.print("Porcentaje Parcela Bajo Cobertura (0 <= x <= 1)" + " "); } %>
</div>
<input class="form-control" type=text name="CBRT_PORCENTAJE" value="<%= Auxiliar.nzObjStr(request.getAttribute("CBRT_PORCENTAJE"), "").toString() %>" />
</div>


</div>

<div class="botones">
<input type="button" onclick='javascript:editar(this.form);' value='<% try { out.print(msj.getString("General.Guardar")); } catch (MissingResourceException e) { out.print("Guardar" + " "); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:eliminar(this.form);' value='<% try { out.print(msj.getString("General.Eliminar")); } catch (MissingResourceException e) { out.print("Eliminar" + " "); } %>' class="btn btn-default" />
<input type="button" onclick='javascript:nueva(this.form);' value='<% try { out.print(msj.getString("General.Nueva")); } catch (MissingResourceException e) { out.print("Registrar Nueva Cobertura" + ".."); } %>' class="btn btn-default" />
</div>

<input type="hidden" name="accion" value="guardar" />
</form>

<div class='tabla_resultados'>
<% out.println(request.getAttribute("tabla_registros")); %> 
</div>

<div id="paginas" style="margin: auto;"></div>

</div>
</div>

</body>
</html>