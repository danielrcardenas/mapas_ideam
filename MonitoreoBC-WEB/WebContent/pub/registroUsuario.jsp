<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.TabsLicenciasDescarga"%>
<%@page import="co.gov.ideamredd.usuario.entities.Municipios"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<%@page import="nl.captcha.Captcha"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.util.Util"%> 
<%@page import="co.gov.ideamredd.web.ui.UI"%>
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
<html class='no-js'>
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta charset="utf-8" />
<%

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

LenguajeI18N i18n = (LenguajeI18N)sesion.getAttribute("i18nAux"); 

String licencias = TabsLicenciasDescarga.getLicenciasDescargaUsuarios(2, msj);
int numeroLicencias = TabsLicenciasDescarga.getNumeroLicencias();
String formDescargaLic = TabsLicenciasDescarga.getFormDescargaLic();

String nombresLicencias = TabsLicenciasDescarga.getNombresLicencias();
String idsLicencias = TabsLicenciasDescarga.getIdsLicencias();
String[] idsArrayLicencias = TabsLicenciasDescarga.getIdsArrayLicencias();

String errorCaptcha;
String errorRegistro;
try {
	errorCaptcha = (String) session.getAttribute("errorCaptcha");
	errorRegistro = (String) session.getAttribute("errorRegistro");
}
catch (Exception e) {
	errorCaptcha = "No";
	errorRegistro = "No";
}

String path = request.getContextPath();
ArrayList<Municipios> municipios = CargaDatosSelect.getArrayMunicipios();
Usuario usuario = null;
if (session.getAttribute("usr_tmp") != null) {
	usuario = (Usuario) session.getAttribute("usr_tmp");
}

Map<Integer, String> diccionarioPermisos = null;
if (usuario != null) {
	diccionarioPermisos = ControlPermisos.consultaPermisos(usuario.getRolId());
}
	
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

<script type='text/javascript'>
var municipio;
var municipios = new Array(<%=municipios.size()%>);

<%if(errorCaptcha=="Yes")
{%>
	alert('<%=msj.getString("registroUsuario.alert.malCaptcha")%>'); 
<%}%>
<%if(errorRegistro=="Yes")
{%>
	alert('<%=msj.getString("registroUsuario.alert.idExiste")%>');
<%}%>

$(document).ready(function() {
	inicializarNavegador();
});

        function reloadCaptcha(){
            var d = new Date();
            $("#captcha_image").attr("src", "/MonitoreoBC-WEB/stickyCaptchaServlet?"+d.getTime());
        }

function validar(){

	var seleccionado = document.getElementById("tipopersonaAux").selectedIndex;
	var passed = true;
	var i, j, m = 0;
	var auxLicSelected="";

	if(seleccionado=="0") {
	<%	for (int i=0;i<idsArrayLicencias.length;i++) { %>
			if(document.getElementById('licCheck'+<%=idsArrayLicencias[i]%>).checked) {
				auxLicSelected=auxLicSelected+document.getElementById('licCheck'+ <%=idsArrayLicencias[i]%>).value;

				if ((<%=i%>+1) != <%=idsArrayLicencias.length%> ) {
					auxLicSelected=auxLicSelected+",";
				}
			}
	<%	}	%>

		document.getElementById("licSeleccionadas").value=auxLicSelected;
		 
		var mensaje = "Los siguientes campos son obligatorios:\n";

		if (document.getElementById("loginAux").value == "" ) {
			mensaje = mensaje + "- Login\n";
			passed = false;
		}
		
		if( document.getElementById("nombreAux").value == "" ) {
			mensaje = mensaje + "- Nombre\n";
			passed = false;
		}
		if( document.getElementById("documentoAux").value == "" ) {
			mensaje = mensaje + "- Nro. Documento\n";
			passed = false;
		}
		if( document.getElementById("tipodocAux").value  < 1 ) {
			mensaje = mensaje + "- Tipo de Identificacion\n";
			passed = false;
		}
		if( document.getElementById("passwordAux").value == "" ) {
			mensaje = mensaje + "- Clave\n";
			passed = false;
		}
		if( document.getElementById("valideClaveAux").value == "") {
			mensaje = mensaje + "- Reingreso Clave\n";
			passed = false;
			if( document.getElementById("passwordAux").value != document.getElementById("passwordAux").value ) {
				mensaje = mensaje + "+ Claves no coincidentes.\n";
				passed = false;
			}
		}
		if( document.getElementById("emailAux").value == "") {
			mensaje = mensaje + "- Correo Electronico\n";
			passed = false;
		}
		else {
			if(!valideMail(document.getElementById("emailAux").value)){
				mensaje = mensaje + "- Correo electronico invalido";
				passed = false;
			}
			var emailAux = document.getElementById("emailAux").value;
			var emailAux_verificacion = document.getElementById("emailAux_verificacion").value;

			if (emailAux != emailAux_verificacion) {
				mensaje = mensaje + "- el Correo Electronico de verificación no coincide\n";
				alert("El Correo electronico de verificación no coincide\n");
				passed = false;				  
			} 
		}
		if( document.getElementById("captchaAux").value == "") {
			mensaje = mensaje + "- Imagen de Validación\n";
			passed = false;
		}
		if( document.getElementById("passwordAux").value != document.getElementById("valideClaveAux").value ) {
			mensaje = mensaje + "Las contraseñas no coinciden\nPor favor corrija el error.";
			passed = false;
		} 
	}
	if(seleccionado=="1") {
	<%	
		for (int i=0; i<idsArrayLicencias.length; i++) {%>
			if (document.getElementById('licCheck'+<%=idsArrayLicencias[i]%>).checked) {
				auxLicSelected=auxLicSelected+document.getElementById('licCheck'+ <%=idsArrayLicencias[i]%>).value;
				if (( <%=i%>+1) != <%=idsArrayLicencias.length%> ) {
					auxLicSelected=auxLicSelected+",";
				}
			}
<%		}%> 
		document.getElementById("licSeleccionadas").value=auxLicSelected;

		var mensaje = "Los siguientes campos son obligatorios:\n";

		if (document.getElementById("loginAux").value == "" ) {
			mensaje = mensaje + "- Login\n";
			passed = false;
		}
		
		if( document.getElementById("nombreAux").value == "" ) {
			mensaje = mensaje + "- Nombre\n";
			passed = false;
		}
		if( document.getElementById("documentoAux").value == "" ) {
			mensaje = mensaje + "- Nro. Documento\n";
			passed = false;
		}
		if( document.getElementById("passwordAux").value == "" ) {
			mensaje = mensaje + "- Clave\n";
			passed = false;
		}
		if ( document.getElementById("valideClaveAux").value == "") {
			mensaje = mensaje + "- Reingreso Clave\n";
			passed = false;
			if (document.getElementById("passwordAux").value != document.getElementById("passwordAux").value ) {
				mensaje = mensaje + "+ Claves no coincidentes.\n";
				passed = false;
			}
		}
		if ( document.getElementById("emailAux").value == "") {
			mensaje = mensaje + "- Correo Electrónico\n";
			passed = false;
		}
		else if (!valideMail(document.getElementById("emailAux").value)){
			mensaje = mensaje + "- Correo electronico invalido";
			passed = false;
		}
		if ( document.getElementById("captchaAux").value == "") {
			mensaje = mensaje + "- Imagen de Validación\n";
			passed = false;
		}
		if ( document.getElementById("passwordAux").value != document.getElementById("valideClaveAux").value ) {
			mensaje = mensaje + "Las contraseñas no coinciden\nPor favor corrija el error.";
			passed = false;
		}
	}
	
	if(!passed){
		alert('<%=msj.getString("registroUsuario.alert.errores")%>');
	}

	var tipopersona = document.getElementById("tipopersonaAux").value;
	var login = document.getElementById("loginAux").value;
	var nombre = document.getElementById("nombreAux").value;
	var tipodoc = document.getElementById("tipodocAux").value;
	var documento = document.getElementById("documentoAux").value;
	var password = document.getElementById("passwordAux").value;
	var valideClave = document.getElementById("valideClaveAux").value;
	var pais = document.getElementById("paisAux").value;
	var dpto = document.getElementById("dptoAux").value;
	var mcpio = document.getElementById("mcpioAux").value;
	var dir = document.getElementById("dirAux").value;
	var tel = document.getElementById("telAux").value;
	var cel = document.getElementById("celAux").value;
	var email = document.getElementById("emailAux").value;
	var organizacion = document.getElementById("organizacionAux").value;
	var cargo = document.getElementById("cargoAux").value;
	var captcha = document.getElementById("captchaAux").value;

	document.getElementById("tipopersona").value = tipopersona;
	document.getElementById("nombre").value = nombre;
	document.getElementById("login").value = login;
	document.getElementById("tipodoc").value = tipodoc;
	document.getElementById("documento").value = documento;
	document.getElementById("password").value = password;
	document.getElementById("valideClave").value = valideClave;
	document.getElementById("pais").value = pais;
	document.getElementById("dpto").value = dpto;
	document.getElementById("mcpio").value = mcpio;
	document.getElementById("dir").value = dir;
	document.getElementById("tel").value = tel;
	document.getElementById("cel").value = cel;
	document.getElementById("correoe").value = email;
	document.getElementById("organizacion").value = organizacion;
	document.getElementById("cargo").value = cargo;
	document.getElementById("captcha").value = captcha;
	 
	return passed;
}
		
function selEnvio(){
	var sel1 = document.getElementById('actividades');
	var count = 0;
	for(i=0;i<sel1.options.length;i++){
		if(sel1.options[i].selected == true)
			count++;
	}
	return count;
}   
		
function cargarMunicipios(){
<%	for (int i = 0; i < municipios.size(); i++) {
		Municipios municipio = (Municipios) municipios.get(i);
		String m = municipio.getConsecutivo() + "," + municipio.getNombre() + "," + municipio.getDepartamento();%>
		municipio = '<%=m%>';
		municipios[<%=i%>] = municipio;
<%	}%>
	return municipios;
}

	function cargarMunicipiosDepto() {
		var txtSelectedValuesObj = document.getElementById('mcpioAux');
		var selectedArray = new Array();
		var selObj = document.getElementById('dptoAux');
		var i, j, m = 0;
		municipios = cargarMunicipios();
		if (txtSelectedValuesObj.options.length != 0) {
			while (m != txtSelectedValuesObj.options.length) {
				txtSelectedValuesObj.remove(m);
			}
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
						texto = document.createTextNode(m[1]);
						opcion.appendChild(texto);
						opcion.id='muni'+m[0];
						opcion.value = m[0];
						txtSelectedValuesObj.appendChild(opcion);
					}
			}
		}
	}

	function verificarMail(mail) {
		if (mail.value != null)
			var str = mail.value.substring(mail.value.indexOf('@') + 1);
		if (str == 'ideam.gov.co') {
			var listas = document.getElementsByTagName('li');
			for (i = 0; i < listas.length; i++) {
				if (listas[i].className == 'nav-three') {
					listas[i].style.display = 'block';
				}
			}
		} else {
			var listas = document.getElementsByTagName('li');
			for (i = 0; i < listas.length; i++) {
				if (listas[i].className == 'nav-three') {
					listas[i].style.display = 'none';
				}
			}
		}
	}

	function descargarLic(nombreLic) {
		document.getElementById("licenciaDescarga").value = nombreLic;
		document.getElementById("formDescLicencia").submit();
	}

	function postEdicion(){
		document.getElementById("passwordAux").value="";
		document.getElementById("valideClaveAux").value="";
		<%if(errorCaptcha=="Yes" || errorRegistro=="Yes")
		{%>
			document.getElementById('tipoDoc'+<%=usuario.getTipoIdentificacion()%>).selected= true;
			document.getElementById("tipoPOp"+<%=usuario.getTipoPersona()%>).selected=true;
// 			activarCampos();
			document.getElementById("pais"+<%=usuario.getPais()%>).selected=true;
			<%if(usuario.getPais() == 57){%>
			document.getElementById("depto"+<%=usuario.getDepto()%>).selected=true;
			cargarMunicipiosDepto();
			document.getElementById("muni"+<%=usuario.getMunicipio()%>).selected=true;
			<%}else{%>
			document.getElementById("mcpioAux").style.display="none";
		 	document.getElementById("dptoAux").style.display="none";
		 	document.getElementById("mcpioLab").style.display="none";
		 	document.getElementById("dptoLab").style.display="none";
			<%}%>
			
			<%
			if(usuario.getLicencias() != null)
			for(int i=0;i< usuario.getLicencias().length;i++)
			{%>
				document.getElementById("licCheck"+<%=usuario.getLicencias()[i]%>).checked=true;
			<%}%>
		<%}%>
		
		document.getElementById("logName").value="";
		document.getElementById("logPassword").value="";
	}
</script>
<script>

	 
$(function() {
	$( "#accordion" ).accordion({
		heightStyle: "content"
	});
});

$(function() {
	$( "#datepicker" ).datepicker();
});
    
function enviarFormRegistro() {
	if(validar()) {
		document.getElementById("formRegistro").submit();
	}
}
	
function activaDesactivaMunicipioDepto(pais){
	if(pais.value != 57){
		$("#mcpioAux").val('0');
		$("#dptoAux").val('0');
		document.getElementById("mcpioAux").style.display="none";
		document.getElementById("dptoAux").style.display="none";
		document.getElementById("mcpioLab").style.display="none";
		document.getElementById("dptoLab").style.display="none";
	}
	else {
		document.getElementById("mcpioAux").style.display="block";
		document.getElementById("dptoAux").style.display="block";
		document.getElementById("mcpioLab").style.display="block";
		document.getElementById("dptoLab").style.display="block";
	}
}

function valideLogin() {
	
}
	
</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);"   onmouseover="popUpAyudaAux()">
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
						

							<div id="content">
								<div class="content-inner">

									<div id="block-accordeon-registro-parcela" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("registroUsuario.registro")%></h3>
												<div id="form-datos-parcela">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.tipoPersona")%>:<span
																class="obligatorio">*</span></label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="tipopersonaAux"
																	id="tipopersonaAux">
																	<%=CargaDatosSelect.getTipoPersona()%>
																</select>
															</div>

															<label for="exampleInputEmail1"><%=msj.getString("nombre_completo") %>:<span class="obligatorio">*</span></label>

															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="nombreAux" id="nombreAux" style="width: 230px">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="nombreAux" id="nombreAux" value="<%=usuario.getNombre()%>" style="width: 230px">
															<%
																}
															%>

															<label for="exampleInputEmail1"><%=msj.getString("organizacion") %>:<span class="obligatorio">*</span></label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="organizacionAux"
																id="organizacionAux" style="width: 230px">
															<%
																}
															%>

															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="organizacionAux"
																id="organizacionAux" value="<%=usuario.getOrganizacion()%>"
																style="width: 230px">
															<%
																}
															%>

															<label for="exampleInputEmail1"><%=msj.getString("cargo") %>:<span class="obligatorio">*</span></label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="cargoAux"
																id="cargoAux" style="width: 230px">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="cargoAux"
																id="cargoAux" value="<%=usuario.getCargo()%>"
																style="width: 230px">
															<%
																}
															%>

															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.tipoIdenti")%>
															:<span class="obligatorio">*</span>
															</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="tipodocAux" id="tipodocAux">
																	<%=CargaDatosSelect.getTipoDocumento()%>
																</select>
															</div>

															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.numeroIdenti")%>:
															<span class="obligatorio">*</span>
															</label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
																<input type="text" class="select-wrapper" onkeypress="return valideValNum(event)" name="documentoAux" id="documentoAux" style="width: 230px">
															<%
																}
															%>
															
															<label for="exampleInputEmail1"><%=msj.getString("login")%> (Usuario para ingresar a la aplicacion):
															<span class="obligatorio">*</span>
															</label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
																<input type="text" class="select-wrapper" onkeypress="valideLogin()" name="loginAux" id="loginAux" style="width: 230px">
															<%
																}
															%>
															
															
															
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper"
																onkeypress="return valideValNum(event)" name="documentoAux"
																id="documentoAux" value="<%=usuario.getIdentificacion()%>"
																style="width: 230px">
															<%
																}
															%>

															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.clave")%>:<span
																class="obligatorio">*</span></label> <input type="password"
																class="select-wrapper" name="passwordAux" id="passwordAux"
																style="width: 230px"> <label
																for="exampleInputEmail1"><%=msj.getString("registroUsuario.reClave")%>:<span
																class="obligatorio">*</span></label> <input type="password"
																class="select-wrapper" name="valideClaveAux"
																id="valideClaveAux" style="width: 230px"> <label
																for="exampleInputEmail1"><%=msj.getString("registroUsuario.pais")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="paisAux" id="paisAux" onchange="activaDesactivaMunicipioDepto(this)">
																	<option value="-1">Seleccionar</option>
																	<%=CargaDatosSelect.getPaises()%>
																</select>
															</div>

															<label id="dptoLab" for="exampleInputEmail1"><%=msj.getString("registroUsuario.depto")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="dptoAux" id="dptoAux"
																	onchange="cargarMunicipiosDepto()">
																	<option id="dptoSel" value="0">Seleccionar</option>
																	<%=CargaDatosSelect.getDepartamentos()%>
																</select>
															</div>

															<label id="mcpioLab" for="exampleInputEmail1"><%=msj.getString("registroUsuario.muni")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="mcpioAux" id="mcpioAux">
																	<option id="mcpioSel" value="0">Seleccionar</option>
																</select>
															</div>


															<label for="exampleInputEmail1">
																<%=msj.getString("registroUsuario.direccion")%>:<span class="obligatorio">*</span>
															</label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="dirAux"
																id="dirAux" style="width: 230px">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="dirAux"
																id="dirAux" value="<%=usuario.getDireccion()%>"
																style="width: 230px">
															<%
																}
															%>

															<label for="exampleInputEmail1"> <%=msj.getString("registroUsuario.tel")%>:<span
																class="obligatorio">*</span>
															</label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="telAux"
																id="telAux" style="width: 230px">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="telAux"
																id="telAux" value="<%=usuario.getTelefonoOficina()%>"
																style="width: 230px">
															<%
																}
															%>

															<label for="exampleInputEmail1"> <%=msj.getString("registroUsuario.cel")%>:</label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="celAux"
																id="celAux" style="width: 230px"
																onkeypress="return valideValNum(event)">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="celAux"
																id="celAux"
																value="<%if (usuario.getCelular() != null) {out.print(usuario.getCelular());}%>"
																style="width: 230px"
																onkeypress="return valideValNum(event)">
															<%
																}
															%>


															<label for="exampleInputEmail1"> <%=msj.getString("registroUsuario.correo")%>
															:<span class="obligatorio">*</span>
															</label>
															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="emailAux"
																id="emailAux" style="width: 230px"
																onchange="verificarMail(this)">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="emailAux"
																id="emailAux" value="<%=usuario.getCorreoElectronico()%>"
																style="width: 230px" onchange="verificarMail(this)">
															<%
																}
															%>

															<label for="exampleInputEmail1"> <%=msj.getString("registroUsuario.emailverificacion")%> 
															:<span class="obligatorio">*</span>
															</label>
															<input type="text" class="select-wrapper" name="emailAux_verificacion"
																id="emailAux_verificacion" style="width: 230px">


															<label for="exampleInputEmail1"> <%=msj.getString("registroUsuario.imagen")%>
															:<span class="obligatorio">*</span>
															</label>
															<div>
																<img id="captcha_image"
																	src="/MonitoreoBC-WEB/stickyCaptchaServlet" width="200"
																	height="60" /> <img src="../img/reload.png"
																	onclick="reloadCaptcha()" alt="reload"
																	style="cursor: pointer;" />
															</div>
															<input type="text" type="text" class="select-wrapper"
																name="captchaAux" id="captchaAux" style="width: 230px;">
														</div>
													<!--formulario de datos de usuario -->
												</div>
												<%
													out.print(licencias);
												%>
											</div>
											<div class="form-actions">
												<input class="btn btn-default btn-ir" type="button"
													style="margin-left: 15px" value="<%=msj.getString("registroUsuario.registrar")%>" class="boton"
													onclick="enviarFormRegistro()"> <input
													class="btn btn-default btn-default" type="button"
													value="<%=msj.getString("registroUsuario.cancelar")%>" class="boton"
													onclick="location.href='/MonitoreoBC-WEB';">
											</div>
										</div>
									</div>

								</div>
							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
							</div>

						</div>
					</div>
				</div>

			</div>

					<%=UI.getFooter(msj) %>									

		</div>
	</form>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										

	<form action="/MonitoreoBC-WEB/registrarUsuarioServlet" method="post" name="formRegistro" id="formRegistro">
		<input type="hidden" value="" name="licSeleccionadas" id="licSeleccionadas">
		<input type="hidden" value="<%=nombresLicencias%>" name="nombresLicencias" id="nombresLicencias"> 
		<input type="hidden" value="<%=idsLicencias%>" name="idsLicencias" id="idsLicencias">
		<input type="hidden" value="" name="tipopersona" id="tipopersona">	
		<input type="hidden" value="" name="nombre" id="nombre">
		<input type="hidden" value="" name="login" id="login">
		<input type="hidden" value="" name="organizacion" id="organizacion">
		<input type="hidden" value="" name="cargo" id="cargo">
		<input type="hidden" value="" name="tipodoc" id="tipodoc">
		<input type="hidden" value="" name="documento" id="documento">
		<input type="hidden" value="" name="password" id="password">
		<input type="hidden" value="" name="valideClave" id="valideClave">
		<input type="hidden" value="" name="pais" id="pais">
		<input type="hidden" value="" name="dpto" id="dpto">
	    <input type="hidden" value="" name="mcpio" id="mcpio">
	    <input type="hidden" value="" name="dir" id="dir">
		<input type="hidden" value="" name="tel" id="tel">
		<input type="hidden" value="" name="cel" id="cel">
		<input type="hidden" value="" name="correoe" id="correoe"> 
		<input type="hidden" value="" name="captcha" id="captcha">
	</form>
	<%
		out.print(formDescargaLic);
	%>
	
	<script type="text/javascript">
		postEdicion();
	</script>
	
</body>
</html>
