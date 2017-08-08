<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="co.gov.ideamredd.admif.Auxiliar" %>
<%
//co.gov.ideamredd.admif.Auxiliar aux = new co.gov.ideamredd.admif.Auxiliar();
LenguajeI18N L = new LenguajeI18N();
ResourceBundle msj = null;
String yo = "registro_archivo.";
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
<% try { out.print(msj.getString(yo+"Registro_de_Archivos_para_Cargue_Masivo")); } catch (MissingResourceException e) { out.print("Registro de Archivos para Cargue Masivo" + ".."); } %> 
</title>

<script type="text/javascript" src="js/ajax.js"></script>
<script type="text/javascript" src="js/ajaxLoader.js"></script>

<script type="text/javascript">
function visibilidadDivElegirIndividuo(s)
{
	if (s.value == 'IMAGENES')
	{
		document.getElementById('div_elegir_individuo').style.display = 'block';
	}
	else
	{
		document.getElementById('div_elegir_individuo').style.display = 'none';
	}
}

function buscarIndividuo()
{
	var f_consecutivo = document.getElementById('f_consecutivo').value;
	var f_placa = document.getElementById('f_placa').value;
	var f_nombre_comun = document.getElementById('f_nombre_comun').value;
	var f_especie = document.getElementById('f_especie').value;
	
	ajax('Individuo','f_indv_consecutivo','div_mensaje_elegir_individuo','?accion=opciones_individuo&f_consecutivo='+f_consecutivo+'&f_placa='+f_placa+'&f_nombre_comun='+f_nombre_comun+'&f_especie='+f_especie,'','','No se encontraron individuos con estos filtros.');
}

function enviar(f) {
	if (f.f_archivo.value == '') {
		alert('Por favor, especifique el archivo / Please, specify the file');
		return;
	}
	
	if (f.f_descripcion.value == '') {
		alert('Por favor, especifique la descripción / Please, specify the description');
		return;
	}
	
	if (f.f_tipo.value == '') {
		alert('Por favor, especifique el tipo del archivo / Please, specify the type of the file');
		return;
	}

	if (f.f_respuestacaptcha.value == '') {
		alert('Por favor, especifique el código de verificación / Please, specify the catpcha code');
		return;
	}

	f.submit();	
}

</script>

<!-- Put these into the <head> -->
<link rel="stylesheet" href="js/responsive-nav/responsive-nav.css">
<script src="js/responsive-nav/responsive-nav.js"></script>

</head>
<body>


<% 
String PRCL_CONSECUTIVO = Auxiliar.nzObjStr(request.getParameter("PRCL_CONSECUTIVO"), "");
String INDV_CONSECUTIVO = Auxiliar.nzObjStr(request.getParameter("INDV_CONSECUTIVO"), "");

if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO) && !Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
%>

<%=co.gov.ideamredd.admif.UI.getHeader() %>

<% } %>

<%
co.gov.ideamredd.admif.Sec sec = new co.gov.ideamredd.admif.Sec();

String menu = "";
try {
	String id_usuario = "";
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "es");
	if (!id_usuario.equals("")) {
		menu = sec.generarMenu(id_usuario, "registro_archivo.jsp", idioma);
	}
}
catch (Exception e) {
	menu = "..." + e.toString();
}

%>

<% 
if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO) && !Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
%>	
<div class="menu"><%=menu %></div>
<% } %>


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
if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO) && !Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
%>	
<h1>
<% try { out.print(msj.getString(yo+"Formulario_para_Cargar_Archivos")); } catch (MissingResourceException e) { out.print("Formulario para Cargar Archivos" + ".."); } %> 
</h1>
<% } %>

<% 
String opciones_tipo_archivo = "";

if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO) && !Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
	opciones_tipo_archivo = "<option value='' selected>";
	try { opciones_tipo_archivo += msj.getString("General.Seleccionar"); } catch (MissingResourceException e) { opciones_tipo_archivo += "Seleccionar" + ".."; }
	opciones_tipo_archivo += "</option>";  

	opciones_tipo_archivo += "<option value='PARCELAS'>";
	try { opciones_tipo_archivo += msj.getString("General.Archivo_de_Parcelas"); } catch (MissingResourceException e) { opciones_tipo_archivo += "Archivo de Parcelas" + ".."; }
	opciones_tipo_archivo += "</option>";

	opciones_tipo_archivo += "<option value='IMAGENES_PARCELAS'>";
	try { opciones_tipo_archivo += msj.getString("General.Archivo_de_Imagenes_de_Parcelas"); } catch (MissingResourceException e) { opciones_tipo_archivo += "Archivo de Imágenes de Parcelas" + ".."; }
	opciones_tipo_archivo += "</option>";
}


String individuos_selected = "";

if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	individuos_selected = " selected ";
}

if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
	opciones_tipo_archivo += "<option value='INDIVIDUOS' "+individuos_selected+">";
	try { opciones_tipo_archivo += msj.getString("General.Archivo_de_Individuos"); } catch (MissingResourceException e) { opciones_tipo_archivo += "Archivo de Tallos" + ".."; }
	opciones_tipo_archivo += "</option>"; 
}

if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
	String tallos_selected = "";
	
	if (Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
		tallos_selected = " selected ";
	}
	
	opciones_tipo_archivo += "<option value='TALLOS' "+tallos_selected+" >";
	try { opciones_tipo_archivo += msj.getString("General.Archivo_de_Tallos"); } catch (MissingResourceException e) { opciones_tipo_archivo += "Archivo de Tallos" + ".."; }
	opciones_tipo_archivo += "</option>"; 
}

if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO) && !Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
	opciones_tipo_archivo += "<option value='IMAGENES_INDIVIDUOS'>";
	try { opciones_tipo_archivo += msj.getString("General.Archivo_de_Imagenes_de_Individuos"); } catch (MissingResourceException e) { opciones_tipo_archivo += "Archivo de Imágenes de Individuos" + ".."; }
	opciones_tipo_archivo += "</option>";
}

%>

<%!String r = "";%> 
<% if (request.getAttribute("retorno") != null) { r = request.getAttribute("retorno").toString(); } else { r=null; } %>  

<% if (r == null) { %> 

<form name=f action='CargueMasivo?accion=subir_archivo' method='post' enctype='multipart/form-data'>				

<div class=form>				

<div class=campo>
<div class=etiqueta>
<% try { out.print(msj.getString("General.Descripcion")); } catch (MissingResourceException e) { out.print("Descripción" + ".."); } %> 
</div>
<textarea name='f_descripcion' cols='36' rows='3'></textarea>
</div>

<div class=campo>
<div class=etiqueta>
<% try { out.print(msj.getString("General.Tipo")); } catch (MissingResourceException e) { out.print("Tipo" + ".."); } %> 
</div>
<select name='f_tipo'><%= opciones_tipo_archivo %></select>
</div>

<div class=campo>
<div class=etiqueta>
<% try { out.print(msj.getString(yo+"Archivo_a_subir")); } catch (MissingResourceException e) { out.print("Archivo a subir" + ".."); } %> 
</div>
<input type='file' name='f_archivo' />
</div>

<div class=campo>
<div class=etiqueta>
<% try { out.print(msj.getString(yo+"Introduzca_el_Codigo_de_la_Imagen")); } catch (MissingResourceException e) { out.print("Introduzca el Código de la Imagen" + ".."); } %> 
</div>
<img src='/AdmIF/simpleCaptcha.png' border='1' />
<br />
<br />
<input type='text' name='f_respuestacaptcha' value='' />
</div>

</div>

<div class="botones">
<input type='button' value='<% try { out.print(msj.getString(yo+"Enviar_Archivo")); } catch (MissingResourceException e) { out.print("Enviar Archivo" + ".."); } %>' class="btn btn-default" onclick='enviar(this.form)' />
</div>

<input type='hidden' name='accion' value='subir_archivo' />
<input type='hidden' name='PRCL_CONSECUTIVO' value='<%= PRCL_CONSECUTIVO %>' />
<input type='hidden' name='INDV_CONSECUTIVO' value='<%= INDV_CONSECUTIVO %>' />
</form>

<% } else { %>
<%= r%>
<% } %>

<hr noshade/>

<% 
if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO) && !Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
%>	
<div class=opcionmenu>
<a class=boton href='Parcela?accion=plantilla_parcelas' target="_blank">
<% try { out.print(msj.getString("General.Descargar_Plantilla_Parcelas")); } catch (MissingResourceException e) { out.print("Descargar Plantilla de Archivo de Importación de Parcelas" + ".."); } %> 
</a>
</div>

<div style="clear: both;"></div>
<% } %>

<% 
if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
%>	
<div class=opcionmenu>
<a class=boton href='Individuo?accion=plantilla_individuos' target="_blank">
<% try { out.print(msj.getString("General.Descargar_Plantilla_Individuos")); } catch (MissingResourceException e) { out.print("Descargar Plantilla de Archivo de Importación de Individuos" + ".."); } %> 
</a>
</div>
<% } %>

<% 
if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
%>	
<div style="clear: both;"></div>

<div class=opcionmenu>
<a class=boton href='Tallo?accion=plantilla_tallos' target="_blank">
<% try { out.print(msj.getString("General.Descargar_Plantilla_Tallos")); } catch (MissingResourceException e) { out.print("Descargar Plantilla de Archivo de Importación de Tallos" + ".."); } %> 
</a>
</div>
<% } %>

</div>
<!-- content-inner -->
</div>
<!-- /.content-->


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