<%@page import="co.gov.ideamredd.ui.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.entities.Depto"%>
<%@page import="co.gov.ideamredd.ui.entities.Noticias"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.ui.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.entities.Municipios"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

	ArrayList<Noticias> noticias = CargaDatosInicialHome.getNoticiasHome(); 
	ArrayList<Noticias> eventos = CargaDatosInicialHome.getEventosHome();
	request.getSession().setAttribute("noticia", noticias);
	request.getSession().setAttribute("eventos", eventos);
	
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
	}
	else
		if(i18n.getLenguaje() == null)
		{
	i18n.setLenguaje("es");
	i18n.setPais("CO");
		}
	ResourceBundle msj = i18n.obtenerMensajeIdioma();
	
	String errorRegistro, errorRol;
	try {
		errorRegistro = (String) session.getAttribute("errorRegistro");
		session.setAttribute("errorRegistro", "No");
	} catch (Exception e) {
		errorRegistro = "No";
	}
	try {
		errorRol = (String) session.getAttribute("errorRol");
		session.setAttribute("errorRol", "No");
	} catch (Exception e) {
		errorRol = "No";
	}
	
	Usuario usuario =null;
	if(request.getParameter("id")!=null)
	{
	usuario = UtilWeb.consultarUsuarioPorDoc(Integer
	.valueOf(Util.desencriptar(request.getParameter("id"))));
	
	request.getSession().setAttribute("usuarioAux", usuario);
	}else{
		usuario = (Usuario)request.getSession().getAttribute("usuarioAux");
	}
	
	ArrayList tipopersona = null;
	ArrayList departamento = null;
	ArrayList totales = null;

	String fechaIni="",fechaFin="";

	try{
		fechaIni = (String)session.getAttribute("fechaIni");
		fechaFin = (String)session.getAttribute("fechaFin");
		tipopersona = (ArrayList)session.getAttribute("estadistica1");
		departamento = (ArrayList)session.getAttribute("estadistica2");
		totales = (ArrayList)session.getAttribute("estadistica3");
	}catch(Exception e){
		//Control variables nulas
	}
%>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="../jquerymobile/jquery.mobile-1.4.2.min.js"></script>
<script src="../jquerymobile/popup.js"></script>
<script src="../js/slippry.min.js"></script>
<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/html.css"
	media="all" />
<script type="text/javascript" src="../js/datum-validation.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script>
  $(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });
  $(function() {
    $( "#finiAux" ).datepicker();
  });
  $(function() {
	$( "#ffinAux" ).datepicker();
   });


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
	 
    $(function(){
     $('#slippry-demo').slippry();
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

	function takeCoordenadas(event) {
		mouseX = event.clientX;
		mouseY = event.clientY;
	}

	function validar() {
		var passed = true;
		var mensaje = "Los siguientes campos son obligatorios:\n";
		if (document.getElementById("email").value == "") {
			mensaje = mensaje + "- Correo electronico";
			passed = false;
		} else if (!valideMail(document.getElementById("email").value)) {
			mensaje = "El Correo electronico ingresado no es valido";
			passed = false;
		}
		if (!passed) {
			alert(mensaje);
		}
		return passed;
	}

	function enviarFormRC() {
		document.getElementById("email").value = document
				.getElementById("auxEmail").value;
		if (validar()) {
			document.getElementById("formRecordarClave").submit();
		}
	}

	function getAbsoluteElementPosition(element) {
		if (typeof element == "string")
			element = document.getElementById(element);

		if (!element)
			return {
				top : 0,
				left : 0
			};

		var y = 0;
		var x = 0;
		while (element.offsetParent) {
			x += element.offsetLeft;
			y += element.offsetTop;
			element = element.offsetParent;
		}
		return {
			top : y,
			left : x
		};
	}

	//*************************USUARIOS**************************************
	var tipoPersonaLista = null;
	var nombreLista = null;
	var tipoIdenLista = null;
	var documentoLista = null;
	var paisLista = null;
	var departamentoLista = null;
	var ciudadLista = null;
	var direccionLista = null;
	var telefonoLista = null;
	var movilLista = null;
	var emailLista = null;
	var licenciaLista = null;
	var datUsuarios = null;
	var esActivo = null;

	function todos(chkbox) {
		for ( var i = 0; i < document.forms[0].elements.length; i++) {
			var elemento = document.forms[0].elements[i];
			if (elemento.type == "checkbox")
				elemento.checked = chkbox.checked;
		}
	}

	function detallar(docUsuario) {
		window.scrollTo(0, 0);
		document.getElementById("fondoBloqueo").style.display = "block";
		document.getElementById("popUpDetallar").style.display = "block";

		if (documentoLista != null) {
			for (i = 0; i < documentoLista.length; i++) {
				if (documentoLista[i] == docUsuario) {
					document.getElementById("detTipoPersona").value = tipoPersonaLista[i];
					document.getElementById("detNombre").value = nombreLista[i];
					document.getElementById("detTipoIdentificacion").value = tipoIdenLista[i];
					document.getElementById("detNumeroIdentificacion").value = documentoLista[i];
					document.getElementById("detPais").value = paisLista[i];
					document.getElementById("detDepartamento").value = departamentoLista[i];
					document.getElementById("detCiudad").value = ciudadLista[i];
					document.getElementById("detDireccion").value = direccionLista[i];
					document.getElementById("detTelefono").value = telefonoLista[i];
					document.getElementById("detMovil").value = movilLista[i];
					document.getElementById("detEmail").value = emailLista[i];
					document.getElementById("detLicencias").value = licenciaLista[i];
					break;
				}
			}
		}
	}

	function desactivaDetallar() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popUpDetallar").style.display = "none";
	}

	function asignarRol(docUsuario) {
		window.scrollTo(0, 0);
		document.getElementById("fondoBloqueo").style.display = "block";
		document.getElementById("popUpAsignarRol").style.display = "block";
		document.getElementById("guardaRol").title = docUsuario;

		if (documentoLista != null) {
			for (i = 0; i < documentoLista.length; i++) {
				if (documentoLista[i] == docUsuario) {
					document.getElementById("asrolNombre").value = nombreLista[i];
					break;
				}
			}
		}

		var aux1 = document.getElementById("rolHid" + docUsuario).value;
		document.getElementById("chkRol" + aux1).selected = true;
	}

	function guardaAsignarRol(docUsuario) {
		var idUsAux = document.getElementById("userHid" + docUsuario).value;
		var selectedRol = document.getElementById("selectRol").selectedIndex + 1;

		document.getElementById("idRolCambioRol").value = selectedRol;
		document.getElementById("idUsuarioCambioRol").value = idUsAux;

		document.getElementById("formCambioRol").submit();
	}

	function desactivaAsignarRol() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popUpAsignarRol").style.display = "none";
	}

	function activarUs(idUsuario) {
		document.getElementById("idUsuario").value = idUsuario;
		document.getElementById("tipoOperacion").value = 1;
		document.getElementById("formActivacion").submit();
	}

	function desactivarUs(idUsuario) {
		document.getElementById("idUsuario").value = idUsuario;
		document.getElementById("tipoOperacion").value = 0;
		document.getElementById("formActivacion").submit();
	}

	function validarModificar() {
		var isChecked = false;
		if (document.getElementById("selusuarios").checked)
			isChecked = true;
		if (isChecked)
			document.forms[2].submit();
		else
			alert('Seleccione un resultado para modificar');
	}
	function validarDesactivar() {
		var isChecked = false;
		if (document.getElementById("selusuarios").checked)
			isChecked = true;
		if (isChecked)
			document.forms[3].submit();
		else
			alert('Seleccione un resultado para desactivar/activar');
	}

	var parFiltradas = new Array();
	var parce;
	var parametros;
	var codigos;
	var ind;
	var idForm = 0;
	var idActual;

	$(document).ready(
			function() {
				//cargarProyectos();
				idActual = 1;
				if (datUsuarios != null)
					if (datUsuarios.length != 0) {//cambiar para los filtros 
						resultadosContactos(datUsuarios.length, 'resultados',
								'consultas');
						cargarDatos(datUsuarios, 'consultas', 3, titulos, null,
								0, 5);
						crearPaginas(datUsuarios.length, 'consultas', 1);
					} else {
						busquedaVacia('resultados', 'consultas');
					}
			});

	function borrarArchivos() {
		document.getElementById('formBorrar').submit();
	}

	function popUpUsuariosAux() {
		var coords = getAbsoluteElementPosition(document
				.getElementById("icoUsuarios"));

		document.getElementById("popUpUsuarios").style.left = coords.left
				+ "px";
		document.getElementById("popUpUsuarios").style.top = coords.top + 15
				+ "px";
	}

	function popUpUsuariosOpen() {
		var coords = getAbsoluteElementPosition(document
				.getElementById("icoUsuarios"));

		document.getElementById("popUpUsuarios").style.left = coords.left
				+ "px";
		document.getElementById("popUpUsuarios").style.top = coords.top + 15
				+ "px";
		document.getElementById("popUpUsuarios").style.display = "block";
	}

	function popUpUsuariosClose() {
		document.getElementById("popUpUsuarios").style.display = "none";
	}

	function formConsultarUsuario() {
		document.getElementById("documento").value = document
				.getElementById("documentoAux").value;
		document.getElementById("tipodoc").value = document
				.getElementById("tipodocAux").value;
		document.getElementById("nombre").value = document
				.getElementById("nombreAux").value;

		document.getElementById("formConsulta").submit();
	}

	function descargaXLS() {
		document.getElementById("tipoDescarga").value = "XLS";
		document.getElementById("formDescarga").submit();
	}
	function descargaPDF() {
		document.getElementById("tipoDescarga").value = "PDF";
		document.getElementById("formDescarga").submit();
	}

	function validarFechas() {
		document.getElementById("fini").value = document.getElementById("finiAux").value;
		document.getElementById("ffin").value = document.getElementById("ffinAux").value;

		var fechaAux=new Array(3);
		if (document.getElementById("fini").value != '') {
			fechaAux=(document.getElementById("fini").value).split('/');
			document.getElementById("fini").value=fechaAux[2]+"/"+fechaAux[0]+"/"+fechaAux[1];
		}
		if (document.getElementById("ffin").value != '') {
			fechaAux=(document.getElementById("ffin").value).split('/');
			document.getElementById("ffin").value=fechaAux[2]+"/"+fechaAux[0]+"/"+fechaAux[1];
		}
		
		if (document.getElementById("fini").value == ''
				&& document.getElementById("ffin").value != '') {
			alert("Por favor, especifique una fecha inicial");
			return false;
		}
		document.getElementById("formFechas").submit();
		return true;
	}
</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);">
	<form id="home" action="<%=basePath%>idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
		<div id="page"
			style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 1000px;">
			<div id="header">
				<input type="hidden">
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
											<label for="exampleInputEmail1"><%=msj.getString("home.registrado.bienvenido")%></label>
										</div>

										<div class="form-group">
											<label for="exampleInputPassword1"><%=usuario.getNombre()%></label>
										</div>

										<div class="form-group">
											<div class="form-group">
												<label id="icoUsuarios" for="exampleInputEmail1" onclick="popUpUsuariosOpen()"
                                 style="margin-right: 10px; cursor: pointer; margin-left: 5px"><a><%=msj.getString("logOn.admin.Usuarios")%>▼ </a></label>
                            </div>
										</div>
										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath2%>MonitoreoBiomasaCarbono/limpiarSesionServlet">
													<%=msj.getString("home.registrado.cerrar")%></a></label>
										</div>
									</div>
									<ul class="social-menu item-list">
										<!-- Aca estaba lo de twitter -->
										<li class="menu-item facebook"><a></a></li>
										<li class="menu-item en"><a onclick="lenguaje(2);"></a></li>
										<li class="menu-item es"><a onclick="lenguaje(1)"></a></li>
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


							<div id="content" style="width: 700px;">
								<div class="content-inner" style="font-size: 14px">

									<div id="block-accordeon-registro-parcela" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("reportesUsuarios.reporteUsuarios")%></h3>
												<div id="form-datos-parcela">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("reportesUsuarios.fechaIni")%>:</label>
														<input type="text" id="finiAux" style="width: 300px" 
														onkeypress="return noKeyData(event);">
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("reportesUsuarios.fechaFin")%>:</label> <input
															type="text" id="ffinAux" style="width: 300px" 
															onkeypress="return noKeyData(event);">
													</div>
													<div class="form-actions">
														<input type="button" class="btn btn-default"
															value="<%=msj.getString("reportesUsuarios.Consultar")%>" onclick="validarFechas()"
															style="font-size: 13px; margin-left: -16px"> <input
															type="button"
															value="<%=msj.getString("consultaUsuarios.volver")%>"
															style="font-size: 13px;" class="btn btn-default"
															onclick="javascript:history.back(1);">
													</div>
													<div></div>
													<div>
														<%
															try {
																if (tipopersona != null) {
																	if ((Integer) totales.get(0) > 0) {
														%>
														<input type="button" value="<%=msj.getString("reportesUsuarios.descarga")%> XLSX"
															onclick="descargaXLS()"> <input type="button"
															value="<%=msj.getString("reportesUsuarios.descarga")%> PDF" onclick="descargaPDF()">
														<%
															}
														%>
														<div class="list-wrap" style="overflow: auto;">
															<%
																if (fechaIni != null) {
																			if (!fechaIni.equals("")) {
															%>
															<h3 align="left">
																<%=msj.getString("reportesUsuarios.desde")%>:&nbsp;&nbsp;&nbsp;<%=fechaIni%></h3>
															<%
																}
																		}
																		if (fechaFin != null) {
																			if (!fechaFin.equals("")) {
															%>
															<h3 align="left">
																<%=msj.getString("reportesUsuarios.hasta")%>:&nbsp;&nbsp;&nbsp;<%=fechaFin%></h3>
															<%
																}
																		}
															%>
															<h3 align="left">
																<%=msj.getString("reportesUsuarios.totalUsuarios")%>:&nbsp;&nbsp;&nbsp;<%=totales.get(0)%></h3>
															<h3 align="left">
																<%=msj.getString("reportesUsuarios.totalPubUsuarios")%>:&nbsp;&nbsp;&nbsp;<%=totales.get(1)%></h3>
															<br />
															<table width="99%" border="1">
																<thead>
																	<tr>
																		<th colspan="2" class="odd"><%=msj.getString("reportesUsuarios.usuariosPorTipoPersona")%></th>
																	</tr>
																	<tr >
																		<th class="even"><%=msj.getString("reportesUsuarios.tipoPersona")%></th>
																		<th class="even"><%=msj.getString("reportesUsuarios.cantidadUsuarios")%></th>
																	</tr>
																</thead>
																<tbody>

																	<%
																		for (int i = 0; i < tipopersona.size(); i++) {
																					String[] tp = (String[]) tipopersona.get(i);
																	%>
																	<tr  class="even">
																		<td><%=tp[1]%></td>
																		<td><%=tp[0]%></td>
																	</tr>
																	<%
																		}
																				if (tipopersona.size() < 1) {
																	%>
																	<tr>
																		<td colspan="2" class="odd"><%=msj.getString("consultaUsuarios.noResultados")%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>
															<br />
															<table width="99%" border="1">
																<thead>
																	<tr>
																		<th colspan="2" class="odd"><%=msj.getString("reportesUsuarios.usuariosPorDepto")%></th>
																	</tr>
																	<tr>
																		<th class="even"><%=msj.getString("reportesUsuarios.departamento")%></th>
																		<th class="even"><%=msj.getString("reportesUsuarios.cantidadUsuarios")%></th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (int i = 0; i < departamento.size(); i++) {
																					String[] depto = (String[]) departamento.get(i);
																	%>
																	<tr  class="even">
																		<td><%=depto[1]%></td>
																		<td><%=depto[0]%></td>
																	</tr>
																	<%
																		}
																				if (departamento.size() < 1) {
																	%>
																	<tr>
																	<td colspan="2"><%=msj.getString("consultaUsuarios.noResultados")%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>
														</div>
														<%
															}
															} catch (Exception e) {
																//control vars no inicializadas
															}
														%>
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
<!-- 									<div class="menu-servicios menu-postscript"> -->
<!-- 										<h3>Servicios de Cuidadanía</h3> -->
<!-- 										<ul> -->
<!-- 											<li><a >Visitas Casa de Nariño</a></li> -->
<!-- 											<li><a >Datos de contacto</a></li> -->
<!-- 											<li><a >Escríbale al Presidente</a></li> -->
<!-- 											<li><a >PSQR</a></li> -->
<!-- 											<li><a >Colombia Compra Eficiente</a></li> -->
<!-- 											<li><a >Avisos Convocatoria Pública</a></li> -->
<!-- 											<li><a >Notificaciones por Aviso</a></li> -->
<!-- 											<li><a >Notificaciones Judiciales</a></li> -->
<!-- 											<li><a >Proveedores</a></li> -->
<!-- 										</ul> -->
<!-- 									</div> -->
<!-- 									<div class="sistema-web-presidencia select-postscript"> -->
<!-- 										<h3>Sistema Web Presidencia</h3> -->
<!-- 										<div class="select-wrapper"> -->
<!-- 											<select> -->
<!-- 												<option>1</option> -->
<!-- 											</select> -->
<!-- 										</div> -->
<!-- 										<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 											<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 												class="form-submit"> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<div class="dependecias-presidencia select-postscript"> -->
<!-- 										<h3>Dependencias Presidencia</h3> -->
<!-- 										<div class="select-wrapper"> -->
<!-- 											<select> -->
<!-- 												<option>1</option> -->
<!-- 											</select> -->
<!-- 										</div> -->
<!-- 										<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 											<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 												class="form-submit"> -->
<!-- 										</div> -->
<!-- 									</div> -->
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
									<p>Carrera 10 No. 20-30 Bogotá DC. - PBX:(571)3527160 -
										Línea nacional 018000110012 - Pronóstico y Alertas (571)
										3527180</p>
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
				</div>
	</form>
	<form action="<%=basePath%>consultarUsuarioServlet" method="post"
		name="formConsulta" id="formConsulta">
		<input type="hidden" name="documento" id="documento"> <input
			type="hidden" name="tipodoc" id="tipodoc"> <input
			type="hidden" name="nombre" id="nombre">
	</form>

	<form method="post" action="<%=basePath%>registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /> <input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>

	<div id="popUpUsuarios"
		style="z-index: 3; position: absolute; width: 140px; height:100px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;"><label for="exampleInputEmail1" style="text-align: center; color: white; font-weight: bold;">
		<%=msj.getString("logOn.admin.Usuarios")%></label>
		</div>
       <label for="exampleInputEmail1"><a href="<%=basePath2%>Usuarios_Web/admin/consultarUsuarios.jsp?id=<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
       *<%=msj.getString("logOn.admin.consultarUsuarios")%></a></label>
       <label for="exampleInputEmail1" style="text-align: left;"><a href="<%=basePath2%>Usuarios_Web/admin/generarReportesUsuarios.jsp?<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
       *<%=msj.getString("logOn.admin.reporteUsuarios")%></a></label>
       <label for="exampleInputEmail1" style="text-align: left;"><a href="<%=basePath%>borrarArchivosServlet">
       *<%=msj.getString("logOn.admin.borrarTemporales")%></a></label>
       <label for="exampleInputEmail1" onclick="popUpUsuariosClose()" 
       style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
     </div>

	<div id="fondoBloqueo"
		style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; top: -20; left: -20; display: none;">
		<!--Sin contenido -->
	</div>
	
	<form action="<%=basePath%>consultarEstadisticasUsuarioServlet" method="post"
			name="registro" id="formFechas">
			<input type="hidden" id="fini" name="fini" />
		    <input type="hidden" id="ffin" name="ffin"/>
	</form>
	
	
	<form action="<%=basePath%>descargaEstadisticasServlet" method="post" id="formDescarga">
			<input type="hidden" id="tipoDescarga" name="tipoDescarga" value=""> 
	</form>
	
	<form id="formActivacion"
		action="<%=basePath%>activacionUsuarioServlet" method="post">
		<input type="hidden" id="idUsuario" name="idUsuario"> <input
			type="hidden" id="tipoOperacion" name="tipoOperacion">
	</form>
	<form id="formCambioRol"
		action="<%=basePath%>modificarRolUsuarioServlet" method="post">
		<input type="hidden" id="idUsuarioCambioRol" name="idUsuario">
		<input type="hidden" id="idRolCambioRol" name="idRol">
	</form>
</body>
</html>