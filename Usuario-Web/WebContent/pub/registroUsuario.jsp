<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.ui.dao.TabsLicenciasDescarga"%>
<%@page import="co.gov.ideamredd.entities.Municipios"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<%@page import="co.gov.ideamredd.ui.entities.Usuario"%>
<%@page import="nl.captcha.Captcha"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.ui.entities.Noticias"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<meta charset="utf-8" />
<title>Monitoreo de Bosques y Carbono</title>
<%
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/";

    String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

    LenguajeI18N i18n = new LenguajeI18N(); 
	if(request.getParameter("idiom") != null )
	{
		i18n.setLenguaje(request.getParameter("idiom"));
		i18n.setPais(request.getParameter("pais"));
	}else
	if(request.getSession().getAttribute("i18nAuxLeng") != null )
	{
		i18n.setLenguaje((String)request.getSession().getAttribute("i18nAuxLeng"));
		i18n.setPais((String)request.getSession().getAttribute("i18nAuxPais"));
	}else
	if(i18n.getLenguaje() == null)
	{
		i18n.setLenguaje("es");
		i18n.setPais("CO");
	}
	ResourceBundle msj = i18n.obtenerMensajeIdioma();
    
	ArrayList<Noticias> noticias = CargaDatosInicialHome.getNoticiasHome();
	ArrayList<Noticias> eventos = CargaDatosInicialHome.getEventosHome();
	request.getSession().setAttribute("noticia", noticias);
	request.getSession().setAttribute("eventos", eventos);
	
		String licencias = TabsLicenciasDescarga.getLicenciasDescargaUsuarios(basePath,2,msj);  
		int numeroLicencias= TabsLicenciasDescarga.getNumeroLicencias();
		String formDescargaLic= TabsLicenciasDescarga.getFormDescargaLic(basePath);
		
		String nombresLicencias = TabsLicenciasDescarga.getNombresLicencias();
		String idsLicencias = TabsLicenciasDescarga.getIdsLicencias(); 
		String[] idsArrayLicencias = TabsLicenciasDescarga.getIdsArrayLicencias();
		 
		String errorCaptcha;
		String errorRegistro;
		try{
	errorCaptcha = (String)session.getAttribute("errorCaptcha");
	errorRegistro = (String)session.getAttribute("errorRegistro");
		}catch(Exception e)
		{
	errorCaptcha="No";
	errorRegistro="No";
		}

	String path = request.getContextPath();
	ArrayList<Municipios> municipios = CargaDatosSelect.getArrayMunicipios();
	Usuario u = null;
	if(session.getAttribute("usr_tmp")!=null)
	{
		u = (Usuario)session.getAttribute("usr_tmp");
	}
	
%>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="../jquerymobile/jquery.mobile-1.4.2.min.js"></script>
<script src="../jquerymobile/popup.js"></script>
<script src="../js/slippry.min.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="../css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/html.css"
	media="all" />
<script type="text/javascript" src="../custom/datum-validation.js"></script>
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

// function funcionEnter(event)
// {
// 	var navegador = navigator.appName;
// 	if(navegador=="Microsoft Internet Explorer")
// 	{
// 	   if(window.event.keyCode==13)
// 	   {
// 	   }
// 	}else{
// 	   if(event.which==13)
// 	   {
// 	   }
// 	   return;
// 	}
// 	return;
// }


$(document).ready(function() {
	var navegador = navigator.appName;
   if(navegador=="Microsoft Internet Explorer")
   {
    $("form").keypress(function(e) {
        if (e.keyCode == 13) {
            return false;
        }
    });
   }else{
	   $("form").keypress(function(e) {
	        if (e.which == 13) {
	            return false;
	        }
	    });
   }
});

        function reloadCaptcha(){
            var d = new Date();
            $("#captcha_image").attr("src", "<%=basePath%>stickyCaptchaServlet?"+d.getTime());
        }

		function validar(){

		 var seleccionado = document.getElementById("tipopersonaAux").selectedIndex;
		 var passed = true;
		 var i, j, m = 0;
		 var auxLicSelected="";

		 if(seleccionado=="0")
         {
			 <%for(int i=0;i<idsArrayLicencias.length;i++)
			  {%>
				  if(document.getElementById('licCheck'+<%=idsArrayLicencias[i]%>).checked)
			      {
					 auxLicSelected=auxLicSelected+document.getElementById('licCheck'+
					                <%=idsArrayLicencias[i]%>).value;
					 if((<%=i%>+1)!=<%=idsArrayLicencias.length%>)
					 {
						 auxLicSelected=auxLicSelected+",";
					 }
				  }
			  <%}%>
		  document.getElementById("licSeleccionadas").value=auxLicSelected;
		 
		  var mensaje = "Los siguientes campos son obligatorios:\n";
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
		  }else if(!valideMail(document.getElementById("emailAux").value)){
			mensaje = mensaje + "- Correo electronico invalido";
			passed = false;
		  }
		  if( document.getElementById("captchaAux").value == "") {
		  	mensaje = mensaje + "- Imagen de ValidaciÃ³n\n";
		  	passed = false;
		  }
		  if( document.getElementById("passwordAux").value != document.getElementById("valideClaveAux").value ) {
			  	mensaje = mensaje + "Las contraseÃ±as no coinciden\nPor favor corrija el error.";
			  	passed = false;
		  }
		  
         }
		 if(seleccionado=="1")
         {
			 <%for(int i=0;i<idsArrayLicencias.length;i++)
			  {%>
				  if(document.getElementById('licCheck'+<%=idsArrayLicencias[i]%>).checked)
			      {
					 auxLicSelected=auxLicSelected+document.getElementById('licCheck'+
					                <%=idsArrayLicencias[i]%>).value;
					 if((<%=i%>+1)!=<%=idsArrayLicencias.length%>)
					 {
						 auxLicSelected=auxLicSelected+",";
					 }
				  }
			  <%}%> 
		  document.getElementById("licSeleccionadas").value=auxLicSelected;
		 
		  var mensaje = "Los siguientes campos son obligatorios:\n";
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
		  if( document.getElementById("valideClaveAux").value == "") {
		  	mensaje = mensaje + "- Reingreso Clave\n";
		  	passed = false;
		  	if( document.getElementById("passwordAux").value != document.getElementById("passwordAux").value ) {
		  	mensaje = mensaje + "+ Claves no coincidentes.\n";
		  	passed = false;
		  	}
		  }
		  if( document.getElementById("emailAux").value == "") {
		  	mensaje = mensaje + "- Correo ElectrÃ³nico\n";
		  	passed = false;
		  }else if(!valideMail(document.getElementById("emailAux").value)){
			mensaje = mensaje + "- Correo electronico invalido";
			passed = false;
		  }
		  if( document.getElementById("captchaAux").value == "") {
		  	mensaje = mensaje + "- Imagen de ValidaciÃ³n\n";
		  	passed = false;
		  }
		  if( document.getElementById("passwordAux").value != document.getElementById("valideClaveAux").value ) {
			  	mensaje = mensaje + "Las contraseÃ±as no coinciden\nPor favor corrija el error.";
			  	passed = false;
		  }
         }

		 if(!passed){
			    alert('<%=msj.getString("registroUsuario.alert.errores")%>');
			  }

		 document.getElementById("tipopersona").value=document.getElementById("tipopersonaAux").value;
			document.getElementById("nombre").value=document.getElementById("nombreAux").value;
			document.getElementById("tipodoc").value=document.getElementById("tipodocAux").value;
			document.getElementById("documento").value=document.getElementById("documentoAux").value;
			document.getElementById("password").value=document.getElementById("passwordAux").value;
			document.getElementById("valideClave").value=document.getElementById("valideClaveAux").value;
			document.getElementById("pais").value=document.getElementById("paisAux").value;
			document.getElementById("dpto").value=document.getElementById("dptoAux").value;
			document.getElementById("mcpio").value=document.getElementById("mcpioAux").value;
			document.getElementById("dir").value=document.getElementById("dirAux").value;
			document.getElementById("tel").value=document.getElementById("telAux").value;
			document.getElementById("cel").value=document.getElementById("celAux").value;
			document.getElementById("email").value=document.getElementById("emailAux").value;
			document.getElementById("captcha").value=document.getElementById("captchaAux").value;
		 
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
		 <%for (int i = 0; i < municipios.size(); i++) {
				Municipios municipio = (Municipios) municipios.get(i);
				String m = municipio.getConsecutivo() + ","
						+ municipio.getNombre() + ","
						+ municipio.getDepartamento();%>
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
			document.getElementById('tipoDoc'+<%=u.getTipoIdentificacion()%>).selected= true;
			document.getElementById("tipoPOp"+<%=u.getTipoPersona()%>).selected=true;
// 			activarCampos();
			document.getElementById("pais"+<%=u.getPais()%>).selected=true;
			document.getElementById("depto"+<%=u.getDepto()%>).selected=true;
			cargarMunicipiosDepto();
			document.getElementById("muni"+<%=u.getMunicipio()%>).selected=true;
			<%
			if(u.getLicencias() != null)
			for(int i=0;i< u.getLicencias().length;i++)
			{%>
				document.getElementById("licCheck"+<%=u.getLicencias()[i]%>).checked=true;
			<%}%>
		<%}%>
		
		document.getElementById("logName").value="";
		document.getElementById("logPassword").value="";
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
	 
    $(function() {
        $( "#accordion" ).accordion({
          heightStyle: "content"
        });
      });
      $(function() {
        $( "#datepicker" ).datepicker();
      });
    
    function lenguaje(id){
		if(id==1){
			document.getElementById('lenguaje').value="ES";
		}else{
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
		document.getElementById("j_password").value = pass;

		document.getElementById("formRegistra").submit();
		document.getElementById("j_security_check").submit();
	}

	function popUpAyudaAux() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		
		document.getElementById("popUpAyuda").style.left= coords.left-135 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+15 + "px";
	}

	function popUpAyudaOpen() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		
		document.getElementById("popUpAyuda").style.left= coords.left-135 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+15 + "px";
		document.getElementById("popUpAyuda").style.display = "block";
	}

	function popUpAyudaClose() {
		document.getElementById("popUpAyuda").style.display = "none";
	}

	function takeCoordenadas(event) {
		mouseX = event.clientX;
		mouseY = event.clientY;
	}

	function enviarFormRegistro() {
		if(validar())
		{
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
	
</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);" onload="postEdicion()" onmouseover="popUpAyudaAux()">
	<form id="home" action="<%=basePath%>idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
		<div id="page"
			style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 1000px;">
			<div id="header">

				<div id="header-first" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">

							<div id="block-logo" class="block">
								<div class="content">
									<a href="<%=basePath2%>MonitoreoBiomasaCarbono/home.jsp"><img
										src="../img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-images-header" class="block">
								<div class="content">
									<a href="http://www.minambiente.gov.co/web/index.html"><img
										src="../img/img-min.png" alt=""></a> <a
										href="http://wsp.presidencia.gov.co/portal/Paginas/default.aspx"><img
										src="../img/img-prosperidad.png" alt=""></a> <a
										href="http://www.moore.org/"><img
										src="../img/img-moore.png" alt=""></a> <a
										href="http://www.patrimonionatural.org.co/"><img
										src="../img/img-patrimonio.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-top-menu" class="block block-menu">

								<div class="content">
									<div id="form-loguin-header" role="form">
										<div class="form-group">
											<label for="exampleInputEmail1"><%=msj.getString("home.usuario")%></label>
											<input type="text" class="form-control" id="logName"
												name="logName" placeholder="">
										</div>

										<div class="form-group">
											<label for="exampleInputPassword1"><%=msj.getString("home.pass")%></label>
											<input type="password" class="form-control" id="logPassword"
												name="logPassword" placeholder="">
										</div>

										<div class="form-actions">
											<input type="button" class="btn btn-default"
												value="<%=msj.getString("home.ir")%>"
												onclick="enviarForms()"></input>
										</div>

									</div>
									<ul class="social-menu item-list">
										<li id="icoAyuda" class="menu-item help first" onclick="popUpAyudaOpen()"
											style="margin-right: 10px; cursor: pointer;"><a></a></li>
										<!-- Aca estaba lo de twitter -->
										<li class="menu-item facebook" style="cursor: pointer;"><a></a></li>
										<li class="menu-item en" style="cursor: pointer;"><a
											onclick="lenguaje(2);"></a></li>
										<li class="menu-item es" style="cursor: pointer;"><a
											onclick="lenguaje(1)"></a></li>
									</ul>

								</div>
								<!-- /.content -->
							</div>
							<!--/.block -->

						</div>
						<!-- /.section-inner-->
					</div>
					<!--/.section -->
				</div>
				<!-- /.section-wrapper-->


				<div id="header-second" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
							<div id="block-main-menu" class="block block-menu">
								<!-- Desde aqui copiar para menu verde cuando el usuario no esta en Log in -->
								<div class="content">
									<ul class="main-menu item-list">
										<li class="menu-item home expanded"><a><%=msj.getString("home.carbono")%></a>
											<ul class="menu">
												<li class="first leaf"><a href="<%=basePath2%>Proyectos-Web/pub/consultaProyecto.jsp"><%=msj.getString("home.carbono.actividades")%></a></li>
												<li class="leaf"><a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteCarbono.jsp"><%=msj.getString("home.carbono.reportes")%></a></li>
<%-- 												<li class="leaf"><a href="<%=basePath2%>/Parcelas-Web/pub/consultarParcela.jsp?id=<%=Util.encriptar(i18n.getLenguaje()+";null")%>"><%=msj.getString("home.carbono.estimacion")%></a></li> --%>
											</ul></li>
										<li class="menu-item about-us expanded"><a><%=msj.getString("home.bosque")%></a>
											<ul class="menu"> 
<%-- 												<li class="first leaf"><a><%=msj.getString("home.bosques.ifn")%></a></li> --%>
												<li class="leaf"><a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteBosques.jsp"><%=msj.getString("home.bosques.cuantificacion")%></a></li>
<%-- 												<li class="leaf"><a href="<%=basePath2%>/Parcelas-Web/pub/consultarParcela.jsp?id=<%=Util.encriptar(i18n.getLenguaje()+";null")%>"><%=msj.getString("home.bosques.alertas")%></a></li> --%>
<%-- 												<li class="leaf"><a href="<%=basePath2%>/Parcelas-Web/pub/consultarParcela.jsp?id=<%=Util.encriptar(i18n.getLenguaje()+";null")%>"><%=msj.getString("home.bosques.ifc")%></a></li> --%>
											</ul>
										</li>
										
										
										<li class="menu-item about-us expanded"><a><%=msj.getString("home.noticiasEventos")%></a>
<!-- 										    <ul class="menu">  -->
<!-- 										        <li class="menu-item noticia"> -->
<%-- 										        <a href="<%=basePath2%>MonitoreoBiomasaCarbono/verNoticiasYEventos.jsp">Consultar Noticias y Eventos</a></li> --%>
<!-- 											    <li class="menu-item noticia"><a -->
<%-- 											    href="<%=basePath2%>MonitoreoBiomasaCarbono/reg/crearNoticia.jsp">Crear Noticias o Eventos</a></li> --%>
<!-- 									        </ul> -->
										</li>
											
										<li class="menu-item services">
										<a href="<%=basePath2%>Usuario-Web/extra/documentacion.jsp" ><%=msj.getString("home.documentacion")%></a>
										</li>
											
										<li class="menu-item contact-us"><a href="<%=basePath2%>MonitoreoBiomasaCarbono/pub/bosqueEnCifras.jsp"><%=msj.getString("home.bosqueCifras")%></a></li>
										<li class="menu-item work-oferts expanded"><a><%=msj.getString("home.visor")%></a>
											<ul class="menu">
												<li class="first leaf"><a><%=msj.getString("home.visor.otro")%></a></li>
												<li class="leaf"><a><%=msj.getString("home.visor.catalogo")%></a></li>
											</ul>
										</li>
									</ul>
								</div>
						<!-- Fin menu verde cuando el usuario no esta en Log in -->
								<!-- /.content -->
							</div>
							<!--/.block -->
						</div>
						<!-- /.section-inner-->
					</div>
					<!--/.section -->
				</div>
				<!-- /.section-wrapper header second-->

			</div>
			<!-- /#header -->

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">

							<div id="sidebar-first">
								<div class="section-inner clearfix">

									<div id="menu-sidebar" class="block-gray menu-sidebar block">
										<ul>
											<li class="item-menu first"><a href="<%=basePath2%>Usuario-Web/extra/presentacion.jsp"><%=msj.getString("pagina.Presentacion")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/aspectosGenerales.jsp" ><%=msj.getString("pagina.AGenerales")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/normatividad.jsp" ><%=msj.getString("pagina.Normatividad")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/enlacesRel.jsp" ><%=msj.getString("pagina.SitiosInteres")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/documentacion.jsp" ><%=msj.getString("pagina.Documentacion")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/protocolos.jsp" ><%=msj.getString("pagina.Protocolos")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/glosario.jsp" ><%=msj.getString("pagina.Glosario")%></a></li>
											<li class="item-menu last"><a href="<%=basePath2%>MonitoreoBiomasaCarbono/pub/bosqueEnCifras.jsp">Bosque en Cifras</a></li>
										</ul>
									</div>

									<div id="block-noticias" class="block-gray block">
										
											<h2><%=msj.getString("home.noticias")%></h2>
											
									</div>


								</div>
								<!-- /.section-inner-->
							</div>
							<!-- /.sidebar-wrapper-->

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

															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.nombre")%>:<span
																class="obligatorio">*</span></label>

															<%
																if (errorCaptcha != "Yes" && errorRegistro != "Yes") {
															%>
															<input type="text" class="select-wrapper" name="nombreAux"
																id="nombreAux" style="width: 230px">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper" name="nombreAux"
																id="nombreAux" value="<%=u.getNombre()%>"
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
															<input type="text" class="select-wrapper"
																onkeypress="return valideValNum(event)" name="documentoAux"
																id="documentoAux" style="width: 230px">
															<%
																}
															%>
															<%
																if (errorCaptcha == "Yes" || errorRegistro == "Yes") {
															%>
															<input type="text" class="select-wrapper"
																onkeypress="return valideValNum(event)" name="documentoAux"
																id="documentoAux" value="<%=u.getIdentificacion()%>"
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
																<select class="form-control" name="paisAux" id="paisAux">
																	<option value="-1">Seleccionar</option>
																	<%=CargaDatosSelect.getPaises()%>
																</select>
															</div>

															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.depto")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="dptoAux" id="dptoAux"
																	onchange="cargarMunicipiosDepto()">
																	<option value="0">Seleccionar</option>
																	<%=CargaDatosSelect.getDepartamentos()%>
																</select>
															</div>

															<label for="exampleInputEmail1"><%=msj.getString("registroUsuario.muni")%>:</label>
															<div class="select-wrapper" style="width: 250px">
																<select class="form-control" name="mcpioAux" id="mcpioAux">
																	<option value="0">Seleccionar</option>
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
																id="dirAux" value="<%=u.getDireccion()%>"
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
																id="telAux" value="<%=u.getTelefonoOficina()%>"
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
																value="<%if (u.getCelular() != null) {out.print(u.getCelular());}%>"
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
																id="emailAux" value="<%=u.getCorreoElectronico()%>"
																style="width: 230px" onchange="verificarMail(this)">
															<%
																}
															%>


															<label for="exampleInputEmail1"> <%=msj.getString("registroUsuario.imagen")%>
															:<span class="obligatorio">*</span>
															</label>
															<div>
																<img id="captcha_image"
																	src="<%=basePath%>stickyCaptchaServlet" width="200"
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
													onclick="location.href='<%=basePath%>limpiarSesionServlet';">
											</div>
										</div>
									</div>

								</div>
								<!-- content-inner -->
							</div>
							<!-- /.content-->

						</div>
						<!-- /.section-inner-->
					</div>
					<!--/.section -->
				</div>
				<!-- /.section-wrapper-->

			</div>
			<!--/.main -->


			<!-- FOOTER -->
			<div id="postscript" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix">

						<div class="links-interes">
							<img src="../img/gobierno.png" />
							<div class="menu-ministerios menu-postscript">
								<ul>
									<li><a class="vicepresidencia"
										href="http://www.vicepresidencia.gov.co/Paginas/Vicepresidencia-Colombia.aspx">Vicepresidencia</a></li>
									<li><a class="min-justicia"
										href="http://www.minjusticia.gov.co/">MinJusticia</a></li>
									<li><a class="min-defensa"
										href="http://www.mindefensa.gov.co/irj/portal/Mindefensa">MinDefensa</a></li>
									<li><a class="min-interior"
										href="http://www.mininterior.gov.co/">MinInterior</a></li>
									<li><a class="min-relaciones"
										href="http://www.cancilleria.gov.co/">MinRelaciones</a></li>
									<li><a class="min-hacienda"
										href="http://www.minhacienda.gov.co/HomeMinhacienda">MinHacienda</a></li>
									<li><a class="min-minas"
										href="http://www.minminas.gov.co/mme/">MinMinas</a></li>
									<li><a class="min-comercio"
										href="http://www.mincit.gov.co/">MinComercio</a></li>
									<li><a class="min-tic"
										href="http://www.mintic.gov.co/portal/604/w3-channel.html">MinTIC</a></li>
									<li><a class="min-cultura"
										href="http://www.mincultura.gov.co/Paginas/default.aspx">MinCultura</a></li>
									<li><a class="min-agricultura"
										href="https://www.minagricultura.gov.co/Paginas/inicio.aspx">MinAgricultura</a></li>
									<li><a class="min-ambiente"
										href="http://www.minambiente.gov.co/web/index.html">MinAmbiente</a></li>
									<li><a class="min-transporte"
										href="https://www.mintransporte.gov.co/">MinTransporte</a></li>
									<li><a class="min-vivienda"
										href="http://www.minvivienda.gov.co/SitePages/Ministerio%20de%20Vivienda.aspx">MinVivienda</a></li>
									<li><a class="min-educacion"
										href="http://www.mineducacion.gov.co/1621/w3-channel.html">MinEducación</a></li>
									<li><a class="min-trabajo"
										href="http://www.mintrabajo.gov.co/">MinTrabajo</a></li>
									<li><a class="min-salud"
										href="http://www.minsalud.gov.co/Paginas/default.aspx">MinSalud</a></li>
								</ul>
							</div>
<!-- 							<div class="menu-servicios menu-postscript"> -->
<!-- 								<h3>Servicios de Cuidadanía</h3> -->
<!-- 								<ul> -->
<!-- 									<li><a >Visitas Casa de Nariño</a></li> -->
<!-- 									<li><a >Datos de contacto</a></li> -->
<!-- 									<li><a >Escríbale al Presidente</a></li> -->
<!-- 									<li><a >PSQR</a></li> -->
<!-- 									<li><a >Colombia Compra Eficiente</a></li> -->
<!-- 									<li><a >Avisos Convocatoria Pública</a></li> -->
<!-- 									<li><a >Notificaciones por Aviso</a></li> -->
<!-- 									<li><a >Notificaciones Judiciales</a></li> -->
<!-- 									<li><a >Proveedores</a></li> -->
<!-- 								</ul> -->
<!-- 							</div> -->
<!-- 							<div class="sistema-web-presidencia select-postscript"> -->
<!-- 								<h3>Sistema Web Presidencia</h3> -->
<!-- 								<div class="select-wrapper"> -->
<!-- 									<select> -->
<!-- 										<option>1</option> -->
<!-- 									</select> -->
<!-- 								</div> -->
<!-- 								<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 									<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 										class="form-submit"> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<div class="dependecias-presidencia select-postscript"> -->
<!-- 								<h3>Dependencias Presidencia</h3> -->
<!-- 								<div class="select-wrapper"> -->
<!-- 									<select> -->
<!-- 										<option>1</option> -->
<!-- 									</select> -->
<!-- 								</div> -->
<!-- 								<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 									<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 										class="form-submit"> -->
<!-- 								</div> -->
<!-- 							</div> -->
						</div>

					</div>
					<!-- /.section-inner-->
				</div>
				<!--/.section -->
			</div>
			<!-- /.section-wrapper-->


			<div id="footer" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix">

						<div class="menu-footer">
							<ul>
								<li><a href="<%=basePath2%>MonitoreoBiomasaCarbono" ><%=msj.getString("home.home")%></a></li>
								<li><a ><%=msj.getString("home.mapa")%></a></li>
								<li><a href="<%=basePath2%>Usuario-Web/extra/documentacion.jsp"><%=msj.getString("home.documentacion")%></a></li>
								<li><a href="<%=basePath2%>Usuario-Web/extra/protocolos.jsp"><%=msj.getString("home.protocolos")%></a></li>
								<li><a href="<%=basePath2%>Usuario-Web/extra/enlacesRel.jsp"><%=msj.getString("home.links")%></a></li>
							</ul>
						</div>
						<div class="copyriht">
							<p>
								@2013 IDEAM. Adscrito al Ministerio de Ambiente y Desarrollo
								Sostenible de Colombia. Sistema Nacional Ambiental. <a >atencionalciudadano@ideam.gov.co</a>
							</p>
							<p>Carrera 10 No. 20-30 Bogotá DC. - PBX:(571)3527160 - Línea
								nacional 018000110012 - Pronóstico y Alertas (571) 3527180</p>
							<p>Horario de Atención: Lunes a Viernes 8:00 AM a 4:00 PM</p>
						</div>

					</div>
					<!-- /.section-inner-->
				</div>
				<!--/.section -->
			</div>
			<!-- /.section-wrapper-->

		</div>
		<!--/.page -->
	</form>
	<form method="post"
		action="<%=basePath2%>MonitoreoBiomasaCarbono/j_security_check"
		name="j_security_check" id="j_security_check">
		<input type="hidden" name="j_username" id="j_username" /> <br> <input
			type="hidden" name="j_password" id="j_password" />
	</form>
	<form method="post" action="<%=basePath%>registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /> <input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>
	<div id="popUpAyuda"
		style="z-index: 3; position: absolute; width: 135px; height:80px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;"><label for="exampleInputEmail1" style="text-align: center; color: white; font-weight: bold;">
		<%=msj.getString("home.popAyuda.titulo")%></label>
		</div>
       <label for="exampleInputEmail1"><a href="<%=basePath2%>Usuario-Web/pub/registroUsuario.jsp">
       *<%=msj.getString("home.popAyuda.registrarse")%></a></label>
       <label for="exampleInputEmail1" style="text-align: left;"><a href="<%=basePath2%>Usuario-Web/pub/recordarClave.jsp?idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
       *<%=msj.getString("home.popAyuda.recordar")%></a></label>
       <label for="exampleInputEmail1" onclick="popUpAyudaClose()" 
       style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
     </div>
	<form action="<%=basePath%>registrarUsuarioServlet" method="post" 
	name="formRegistro" id="formRegistro">
		<input type="hidden" value="" name="licSeleccionadas"
			id="licSeleccionadas">
		<input type="hidden"
			value="<%=nombresLicencias%>" name="nombresLicencias"
			id="nombresLicencias"> 
		<input type="hidden"
			value="<%=idsLicencias%>" name="idsLicencias" id="idsLicencias">
		<input type="hidden" value="" name="tipopersona"
			id="tipopersona">	
		<input type="hidden" value="" name="nombre"
			id="nombre">
		<input type="hidden" value="" name="tipodoc"
			id="tipodoc">
		<input type="hidden" value="" name="documento"
			id="documento">
		<input type="hidden" value="" name="password"
			id="password">
		<input type="hidden" value="" name="valideClave"
			id="valideClave">
		<input type="hidden" value="" name="pais"
			id="pais">
		<input type="hidden" value="" name="dpto"
			id="dpto">
	    <input type="hidden" value="" name="mcpio"
			id="mcpio">
	    <input type="hidden" value="" name="dir"
			id="dir">
		<input type="hidden" value="" name="tel"
			id="tel">
		<input type="hidden" value="" name="cel"
			id="cel">
		<input type="hidden" value="" name="email"
			id="email">
		<input type="hidden" value="" name="captcha"
			id="captcha">
	</form>
	<%
		out.print(formDescargaLic);
	%>
</body>
</html>







