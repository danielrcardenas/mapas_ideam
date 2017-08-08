<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.CargaDatosSelect"%>   
<%@page import="co.gov.ideamredd.web.usuario.dao.TabsLicenciasDescarga"%> 
<%@page import="co.gov.ideamredd.usuario.entities.Municipios"%> 
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%> 
<%@page import="co.gov.ideamredd.util.Util"%> 
<%@page import="co.gov.ideamredd.web.usuario.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.CargaDatosInicialHome"%>   
<%@page import="nl.captcha.Captcha"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>  
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
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
 	
	
	String licencias = TabsLicenciasDescarga.getLicenciasDescargaUsuarios(2,msj); 
	int numeroLicencias= TabsLicenciasDescarga.getNumeroLicencias();
	String formDescargaLic= TabsLicenciasDescarga.getFormDescargaLic();
	
	String nombresLicencias = TabsLicenciasDescarga.getNombresLicencias();
	String idsLicencias = TabsLicenciasDescarga.getIdsLicencias(); 
	String[] idsArrayLicencias = TabsLicenciasDescarga.getIdsArrayLicencias();
	
	
	
	ArrayList<Integer> listaLic = 
			(ArrayList<Integer>)ConsultaWebUsuario.
			consultarLicenciasUsuario(usuario.getIdUsuario());
	
	request.getSession().setAttribute("usr_seq", usuario.getIdUsuario());
	request.getSession().setAttribute("tipPersona", usuario.getTipoPersona());
	request.getSession().setAttribute("documento", usuario.getIdentificacion());
	request.getSession().setAttribute("tipodoc", usuario.getTipoIdentificacion());
	request.getSession().setAttribute("usuario", usuario);
	request.getSession().setAttribute("login", usuario.getLogin());
	
	String errorRegistro;
	try{
		errorRegistro = (String)session.getAttribute("errorRegistro");
		session.setAttribute("errorRegistro","No");
	}catch(Exception e)
	{
		errorRegistro="No";
	}
	
	String path = request.getContextPath();
	ArrayList<Municipios> municipios = CargaDatosSelect.getArrayMunicipios();
	
	
%>

<%
if(usuario != null)
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 70)) {
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script type="text/javascript" src="../js/general.js"></script>


<script type='text/javascript'>
var municipio;
var municipios = new Array(<%=municipios.size()%>);

<%if(errorRegistro=="Yes")
{%>
	alert('<%=msj.getString("modificaUsuario.alert.idExiste")%>');
<%}%>

function reloadCaptcha(){
	var d = new Date();
	$("#captcha_image").attr("src", "/MonitoreoBC-WEB/stickyCaptchaServlet?"+d.getTime());
}

$(document).ready(function() {
	inicializarNavegador();
});

function validar(){
	var passed = true;
	var i, j, m=0;
	var auxLicSelected="";

	<%for(int i=0;i<idsArrayLicencias.length;i++) {%>
		if(document.getElementById('licCheck'+<%=idsArrayLicencias[i]%>).checked) {
			auxLicSelected=auxLicSelected+document.getElementById('licCheck'+ <%=idsArrayLicencias[i]%>).value;
			if((<%=i%>+1)!=<%=idsArrayLicencias.length%>) {
				auxLicSelected=auxLicSelected+",";
			}
		}
	<%}%>

		document.getElementById("licSeleccionadas").value=auxLicSelected;
	
		var mensaje = "Los siguientes campos son obligatorios:\n";
		if (document.getElementById("loginAux").value == "" ) {
			mensaje = mensaje + "- Login\n";
			passed = false;
		}

		if (document.getElementById("nombreAux").value == "" ) {
			mensaje = mensaje + "- Nombre\n";
			passed = false;
		}

		if (document.getElementById("passwordAux").value != "") {
			if( document.getElementById("passwordAux").value != document.getElementById("valideClaveAux").value ) {
				mensaje = mensaje + "+ Claves no coincidentes.\n";
				passed = false;
			}
		}

		if (document.getElementById("emailAux").value == "") {
			mensaje = mensaje + "- Correo Electronico\n";
			passed = false;
		}
		else if (!valideMail(document.getElementById("emailAux").value)) {
			mensaje = mensaje + "- Correo electr�nico inv�lido";
			passed = false;
		}

		if(!passed){
			alert('<%=msj.getString("registroUsuario.alert.errores")%>');
		}
	 
		document.getElementById("login").value = document.getElementById("loginAux").value;
		document.getElementById("nombre").value = document.getElementById("nombreAux").value;
		document.getElementById("password").value = document.getElementById("passwordAux").value;
		document.getElementById("valideClave").value = document.getElementById("valideClaveAux").value;
		document.getElementById("pais").value = document.getElementById("paisAux").value;
		document.getElementById("dpto").value = document.getElementById("dptoAux").value;
		document.getElementById("mcpio").value = document.getElementById("mcpioAux").value;
		document.getElementById("dir").value = document.getElementById("dirAux").value;
		document.getElementById("tel").value = document.getElementById("telAux").value;
		document.getElementById("cel").value = document.getElementById("celAux").value;
		document.getElementById("emailUsuario").value = document.getElementById("emailAux").value;

		return passed;
	}
	
	function selEnvio() {
		var sel1 = document.getElementById('actividades');
		var count = 0;
		for (i=0;i<sel1.options.length;i++) {
			if(sel1.options[i].selected == true) count++;
		}
		return count;
	}   
	
	function cargarMunicipios() {
	<%for (int i = 0; i < municipios.size(); i++) {
		Municipios municipio = (Municipios) municipios.get(i);
		String m = municipio.getConsecutivo() + "," + municipio.getNombre() + "," + municipio.getDepartamento();
		%>
		municipio = '<%=m%>';
		municipios[<%=i%>] = municipio;
	<%}%>
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

				if (selObj.options[i].selected) {
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
		} 
		else {
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
		document.getElementById('tipoDoc'+<%=usuario.getTipoIdentificacion()%>).selected= true;
        document.getElementById('tipodoc').disabled = true;
        document.getElementById('documento').disabled = true;
        document.getElementById("password").value="";
		document.getElementById("valideClave").value="";
		document.getElementById("pais"+<%=usuario.getPais()%>).selected=true;
		document.getElementById("pais"+<%=usuario.getPais()%>).selected=true;

		<%if(usuario.getPais() == 57) {%>
			document.getElementById("depto"+<%=usuario.getDepto()%>).selected=true;
			cargarMunicipiosDepto();
			document.getElementById("muni"+<%=usuario.getMunicipio()%>).selected=true;
		<%} else {%>
			document.getElementById("mcpioAux").style.display="none";
		 	document.getElementById("dptoAux").style.display="none";
		 	document.getElementById("mcpioLab").style.display="none";
		 	document.getElementById("dptoLab").style.display="none";
		<%}%>

		<%if(listaLic.size() != 0)
			for(int i=0;i<listaLic.size();i++) {%>
				document.getElementById("licCheck"+<%=listaLic.get(i)%>).checked=true;
		<%}%>
		
	}
</script>

<script>
	var mouseX=0;
	var mouseY=0;

	function coordenadas(event) {
		x=event.clientX;
		y=event.clientY;
		 
		document.getElementById("x").value = x;
		document.getElementById("y").value = y;
	}
	 
	$(function() {$( "#accordion" ).accordion({heightStyle: "content"});});
	$(function() {$( "#datepicker" ).datepicker();});
    
	function lenguaje(id) {
		if (id==1) {
			document.getElementById('lenguaje').value="ES";
		}
		else {
			document.getElementById('lenguaje').value="EN";
		}
		document.getElementById('pagina').value="<%=request.getRequestURI()%>";
		document.getElementById('home').submit();
	}

	function enviarForms() {
		var nombre = document.getElementById("logName").value;
		var pass = document.getElementById("logPassword").value;
		
		document.getElementById("hidUsername").value = nombre;
		document.getElementById("hidPassword").value = pass;
		document.getElementById("j_username").value = nombre;
		document.getElementById("j_password").value = pass; actividadUusuario('/MonitoreoBC-WEB/registrarAccesoServlet',nombre);
		
		document.getElementById("formRegistra").submit();
		document.getElementById("j_security_check").submit();
	}

	function popUpAyudaAux() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		
		document.getElementById("popUpAyuda").style.left= coords.left+10 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+10 + "px";
	}

	function popUpAyudaOpen() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		
		document.getElementById("popUpAyuda").style.left= coords.left+10 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+10 + "px";
		document.getElementById("popUpAyuda").style.display = "block";
	}	function enviarFormRegistro() {
		if(validar()) {
			document.getElementById("formRegistro").submit();
		}
	}

	function getAbsoluteElementPosition(element) {
		if (typeof element == "string")
			element = document.getElementById(element);
		  
		if (!element) return { top:0,left:0 };
		
		var y = 0;
		var x = 0;
		while (element.offsetParent) {
			x += element.offsetLeft;
			y += element.offsetTop;
			element = element.offsetParent;
		}
		return {top:y,left:x};
	}
	
	function activaDesactivaMunicipioDepto(pais) {
		if(pais.value != 57) {
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
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja"><%=msj.getString("Modificar_Usuario") %></h2>

							<div id="content">
								<div class="content-inner">

									<div id="block-accordeon-registro-parcela" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("modificaUsuario.modifica")%></h3>
												<div id="form-datos-parcela">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.tipoPersona")%>:<span
																class="obligatorio">*</span></label>
															<%
															if(usuario.getTipoPersona()==1)
															{%>
															<input type="text" class="select-wrapper"
															style="width: 230px" value="Natural" disabled="disabled">
															<%}
															if(usuario.getTipoPersona()==2)
															{%>
															<input type="text" class="select-wrapper"
															style="width: 230px" value="Juridica" disabled="disabled">
															<%}%>

															<label for="exampleInputEmail1"><%=msj.getString("login")%>:<span
																class="obligatorio">*</span></label> 
															<input type="text" class="select-wrapper" name="loginAux"
																id="loginAux" value="<%=usuario.getLogin()%>"
																style="width: 230px">
															<label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.nombre")%>:<span
																class="obligatorio">*</span></label>
															<input type="text" class="select-wrapper" name="nombreAux"
																id="nombreAux" value="<%=usuario.getNombre()%>"
																style="width: 230px">
															<label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.tipoIdenti")%>
															:<span class="obligatorio">*</span>
															</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="tipodocAux" id="tipodocAux" disabled="disabled">
																	<%=CargaDatosSelect.getTipoDocumento()%>
																</select>
															</div>

															<label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.numeroIdenti")%>:
															<span class="obligatorio">*</span>
															</label>
															<input type="text" class="select-wrapper"
																onkeypress="return valideValNum(event)" name="documentoAux"
																id="documentoAux" value="<%=usuario.getIdentificacion()%>"
																style="width: 230px" disabled="disabled">

															
															<label
																for="exampleInputEmail1"><%=msj.getString("modificaUsuario.pais")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="paisAux" id="paisAux" onchange="activaDesactivaMunicipioDepto(this)">
																	<option value="-1"><%=msj.getString("seleccionar") %></option>
																	<%=CargaDatosSelect.getPaises()%>
																</select>
															</div>

															<label id="dptoLab" for="exampleInputEmail1"><%=msj.getString("modificaUsuario.depto")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="dptoAux" id="dptoAux"
																	onchange="cargarMunicipiosDepto()">
																	<option value="0"><%=msj.getString("seleccionar") %></option>
																	<%=CargaDatosSelect.getDepartamentos()%>
																</select>
															</div>

															<label id="mcpioLab" for="exampleInputEmail1"><%=msj.getString("modificaUsuario.muni")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="mcpioAux" id="mcpioAux">
																	<option value="0"><%=msj.getString("seleccionar") %></option>
																</select>
															</div>


															<label for="exampleInputEmail1">
																<%=msj.getString("modificaUsuario.direccion")%>:<span class="obligatorio">*</span>
															</label>
															<input type="text" class="select-wrapper" name="dirAux"
																id="dirAux" value="<%=usuario.getDireccion()%>"
																style="width: 230px">

															<label for="exampleInputEmail1"> <%=msj.getString("modificaUsuario.tel")%>:<span
																class="obligatorio">*</span>
															</label>
															<input type="text" class="select-wrapper" name="telAux"
																id="telAux" value="<%=usuario.getTelefonoOficina()%>"
																style="width: 230px">

															<label for="exampleInputEmail1"> <%=msj.getString("modificaUsuario.cel")%>:</label>
															<input type="text" class="select-wrapper" name="celAux"
																id="celAux"
																value="<%if (usuario.getCelular() != null) {out.print(usuario.getCelular());}%>"
																style="width: 230px"
																onkeypress="return valideValNum(event)">

															<label for="exampleInputEmail1"> <%=msj.getString("modificaUsuario.correo")%>
															:<span class="obligatorio">*</span>
															</label>
															<input type="text" class="select-wrapper" name="emailAux"
																id="emailAux" value="<%=usuario.getCorreoElectronico()%>"
																style="width: 230px" onchange="verificarMail(this)">
														</div>
													<!--formulario de datos de usuario -->
												</div>
												<h3><%=msj.getString("modificaUsuario.cambiaClave")%></h3>
											<div id="form-datos-parcela">
											<label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.clave")%>:<span
																class="obligatorio">*</span></label> 
															<input type="password"
																class="select-wrapper" name="passwordAux" id="passwordAux"
																style="width: 230px"> 
															<label
																for="exampleInputEmail1"><%=msj.getString("modificaUsuario.reClave")%>:<span
																class="obligatorio">*</span></label> 
															<input type="password"
																class="select-wrapper" name="valideClaveAux"
																id="valideClaveAux" style="width: 230px"> 
											</div>
												<% if(ControlPermisos.tienePermiso(diccionarioPermisos, 87))
												{
													out.print(licencias);
												}
												%>
											</div>
											<div class="form-actions">
												<input class="btn btn-default btn-ir" type="button"
													style="margin-left: 15px" value="<%=msj.getString("modificaUsuario.guardar")%>" class="boton"
													onclick="enviarFormRegistro()"> <input
													class="btn btn-default btn-default" type="button"
													value="<%=msj.getString("modificaUsuario.cancelar")%>" class="boton"
													onclick="javascript:history.back(1);">
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

	<form action="/MonitoreoBC-WEB/actualizarUsuarioServlet" method="post" 
	name="formRegistro" id="formRegistro">
		<input type="hidden" value="" name="licSeleccionadas" id="licSeleccionadas">
		<input type="hidden" value="<%=nombresLicencias%>" name="nombresLicencias" id="nombresLicencias"> 
		<input type="hidden" value="<%=idsLicencias%>" name="idsLicencias" id="idsLicencias">
		<input type="hidden" value="" name="tipopersona" id="tipopersona">	
		<input type="hidden" value="" name="login" id="login">
		<input type="hidden" value="" name="nombre" id="nombre">
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
		<input type="hidden" value="" name="emailUsuario" id="emailUsuario">
	</form>
	<%
		out.print(formDescargaLic);
	%>
		
	<script type="text/javascript">
		postEdicion();
	</script>
	
</body>
</html> 


<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>