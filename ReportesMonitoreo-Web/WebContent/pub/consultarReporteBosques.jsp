<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.ui.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.entities.Reportes"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosSelect"%>
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
%>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="../jquerymobile/jquery.mobile-1.4.2.min.js"></script>
<script src="../jquerymobile/popup.js"></script>
<script src="../js/slippry.min.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"
	rel="stylesheet" />

<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/html.css"
	media="all" />
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>


<script>
  var mouseX=0;
  var mouseY=0;

  <%if (errorRegistro == "Yes") {%>
	alert("El correo electronico no existe\n"
			+ "Por favor cambielo e intente nuevamente.");
<%}%>
	
<%if (errorRol == "Yes") {%>
	alert("El rol actual del usuario no permite cambiar la contraseña de esta manera,\n"
			+ "Comuniquese a las lineas de atencion para mas informacion.");
<%}%>

    function coordenadas(event) {
	  x=event.clientX;
	  y=event.clientY;
	   
	  document.getElementById("x").value = x;
	  document.getElementById("y").value = y;
	   
	}

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

	function popUpAyudaAux() {
		var coords = getAbsoluteElementPosition(document
				.getElementById("icoAyuda"));

		document.getElementById("popUpAyuda").style.left = coords.left - 135
				+ "px";
		document.getElementById("popUpAyuda").style.top = coords.top + 15
				+ "px";
	}

	function popUpAyudaOpen() {
		var coords = getAbsoluteElementPosition(document
				.getElementById("icoAyuda"));

		document.getElementById("popUpAyuda").style.left = coords.left - 135
				+ "px";
		document.getElementById("popUpAyuda").style.top = coords.top + 15
				+ "px";
		document.getElementById("popUpAyuda").style.display = "block";
	}

	function popUpAyudaClose() {
		document.getElementById("popUpAyuda").style.display = "none";
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

	function postEdit() {
		document.getElementById("logName").value = "";
		document.getElementById("logPassword").value = "";
	}
</script>
<script type='text/javascript'>
	var reporte;
	var reportes = new Array();

	function cargarPeriodos() {
<%ArrayList<Reportes> reportes = CargaDatosSelect.getPeriodos(); 
				for (int i = 0; i < reportes.size(); i++) {
					Reportes reporte = (Reportes) reportes.get(i);
					String r = reporte.getConsecutivo() + ","
							+ reporte.getTipoReporte() + ","
							+ reporte.getDivision()+","
							+ reporte.getPeriodoUno();
					if(reporte.getPeriodoDos()>0)
						r+="-"+reporte.getPeriodoDos();%>
						reporte ='<%=r%>';
		reportes[
<%=i%>
	] = reporte;
<%}%>
	return reportes;
	}

	function valoresTipoReporte() {
		var sel = document.getElementById("divterritorioAux");
		if (document.getElementById("treporteAux").value > 4) {
			sel.disabled = false;
			sel.value = -1;
			sel.options[1].style.display = 'none';
			sel.options[2].style.display = 'none';
			document.getElementById('l1').style.display = 'block';
			document.getElementById("periodo1Aux").style.display = 'block';
			document.getElementById("periodoIniAux").style.display = 'none';
			document.getElementById("periodoFinAux").style.display = 'none';
			document.getElementById('l3').style.display = 'none';
			document.getElementById("b1").style.display = 'none';
			document.getElementById("b2").style.display = 'none';
			document.getElementById("b3").style.display = 'none';
			document.getElementById("b4").style.display = 'none';
		} else if (document.getElementById("treporteAux").value > 0
				&& document.getElementById("treporteAux").value < 5) {
			sel.disabled = false;
			sel.value = -1;
			sel.options[1].style.display = 'block';
			sel.options[2].style.display = 'block';
			document.getElementById('l1').style.display = 'none';
			document.getElementById("periodo1Aux").style.display = 'none';
			document.getElementById("periodoIniAux").style.display = 'block';
			document.getElementById("periodoFinAux").style.display = 'block';
			document.getElementById('l3').style.display = 'block';
			document.getElementById("b1").style.display = 'block';
			document.getElementById("b2").style.display = 'block';
			document.getElementById("b3").style.display = 'block';
			document.getElementById("b4").style.display = 'block';
		} else if (document.getElementById("treporteAux").value < 0) {
			sel.value = -1;
			sel.disabled = true;
			sel.options[1].style.display = 'block';
			sel.options[2].style.display = 'block';
			document.getElementById("periodoIniAux").style.display = 'none';
			document.getElementById("periodoFinAux").style.display = 'none';
			document.getElementById('l1').style.display = 'none';
			document.getElementById('l3').style.display = 'none';
			document.getElementById("periodo1Aux").style.display = 'none';
			document.getElementById("b1").style.display = 'none';
			document.getElementById("b2").style.display = 'none';
			document.getElementById("b3").style.display = 'none';
			document.getElementById("b4").style.display = 'none';
		} else {
			sel.disabled = false;
			sel.options[1].style.display = 'block';
			sel.options[2].style.display = 'block';
			document.getElementById("periodoIniAux").style.display = 'none';
			document.getElementById("periodoFinAux").style.display = 'none';
			document.getElementById('l1').style.display = 'none';
			document.getElementById('l3').style.display = 'none';
			document.getElementById("periodo1Aux").style.display = 'none';
			document.getElementById("b1").style.display = 'none';
			document.getElementById("b2").style.display = 'none';
			document.getElementById("b3").style.display = 'none';
			document.getElementById("b4").style.display = 'none';
		}
		sel.value = -1;
	}

	function selEnvio() {
		var sel = document.getElementById('periodoFinAux');
		var j = 0;
		for (j = 0; j < sel.options.length; j++) {
			sel.options[j].selected = true;
		}
	}

	function validarConsultaReporte() {
		selEnvio();
		var passed = true;
		var mensaje = "Los siguientes campos son obligatorios:";
		if (document.getElementById("treporteAux").value < 1) {
			mensaje = mensaje + "- Tipo de Reporte";
			passed = false;
		} else if (document.getElementById("divterritorioAux").value < 1) {
			mensaje = mensaje + "- División territorial";
			passed = false;
		} else if (document.getElementById("periodoFinAux").length == 0
				&& document.getElementById("treporteAux").value > 0
				&& document.getElementById("treporteAux").value < 5) {
			mensaje = "Se necesita minimo un dato de periodo para la busqueda.\n";
			passed = false;
		}
		if (!passed) {
			alert(mensaje);
		}
		return passed;
	}
	
	
	function enviarFormularioReporte()
	{
		document.getElementById("treporte").value= "";
		document.getElementById("divterritorio").value= "";
		document.getElementById("periodo1").value= "";
		document.getElementById("periodoFin").value="";
		
		var b=0;
		if(validarConsultaReporte()==true)
		{
		document.getElementById("treporte").value= document.getElementById("treporteAux").value;
		document.getElementById("divterritorio").value= document.getElementById("divterritorioAux").value;
		document.getElementById("periodo1").value= document.getElementById("periodo1Aux").value;
		for(b=0;b<document.getElementById("periodoFinAux").length;b++)
		{
		document.getElementById("periodoFin").value=document.getElementById("periodoFin").value+","+ document.getElementById("periodoFinAux")[b].value;
		}
		
		var valorcito= document.getElementById("periodo1").value;
		
		if((document.getElementById("periodo1").value=="-1" 
			|| valorcito=="") &&
		   document.getElementById("periodoFinAux").length==0)
	    {
			setTimeout(function(){document.location.href = '<%=basePath2%>'+"ReportesMonitoreo-Web/pub/consultarReporteBosques.jsp"},500);
<%-- 			location.href = '<%=basePath2%>'+"ReportesMonitoreo-Web/pub/consultarReporteBosques.jsp"; --%>
			return;
		}
		
		document.getElementById("formConsultaReporte").submit();
		}
	}
</script>
</head>
<body class='sidebarlast front' onload="postEdit()"
	onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">
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
										<li id="icoAyuda" class="menu-item help first"
											onclick="popUpAyudaOpen()"
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

							<div id="content" style="width: 700px">
								<div class="content-inner">

									<div id="block-consulta-reportes" class="block-gray block">

										<h2>Consulta de reportes</h2>
<!-- 										<form class="form-columnx2 form-consulta-reportes" role="form"> -->
											<div class="form-group"
												style="padding-left: 20px; padding-right: 20px">
												<label for="exampleInputEmail1"> Tipo de reporte: </label>
												<div class="select-wrapper" style="max-width: 400px;">
													<select name="treporteAux" id="treporteAux"
														onchange="eliminarlista(periodoFinAux);eliminarlista(periodoIniAux);valoresTipoReporte()">
														<option value="-1">Seleccionar</option>
														<%=CargaDatosSelect.getTipoReporte("bosques")%>
													</select>
												</div>
											</div>
											<div class="form-group"
												style="padding-left: 20px; padding-right: 20px">
												<label for="exampleInputEmail1"> Division del
													territorio </label>
												<div class="select-wrapper" style="max-width: 400px;">
													<select name="divterritorioAux" id="divterritorioAux"
														disabled="disabled"
														onchange="eliminarlista(periodoFinAux);eliminarlista(periodoIniAux);completarListaPeriodos(treporteAux,divterritorioAux,periodoIniAux);eliminarlista(periodo1Aux);completarListaPeriodos(treporteAux,divterritorioAux,periodo1Aux)">
														<option value="-1">Seleccionar</option>
														<%=CargaDatosSelect.getDivisionTerritorio()%>
													</select>
												</div>
											</div>
											<div class="form-group item-periodos"
												style="padding-left: 20px; padding-right: 20px">

												<label id="l1" for="exampleInputEmail1"
													style="display: none;">Periodos:</label> 
													<span
													class="item-periodo-left" style="max-width: 300px"> <select class="select-wrapper" name="periodo1Aux"
													id="periodo1Aux" size="1" style="width: 200px; display: none;"
													title="Año inicial para el periodo del reporte">
														<option value="-1">Seleccionar</option>
												</select>
												</span> 
												
												<label id="l3" for="exampleInputEmail1"
													style="display: none; ">Periodos:</label> 
													<span
													class="item-periodo-left"> 
													<select class="select-wrapper" name="periodoIniAux"
													id="periodoIniAux" multiple="multiple"
													style="display: none; height: 150px; width:260px">
												</select>
												</span>
												
												 <span class="pager item-periodo-middle" >
													<ul>
													
													<li class="item-pager-controls item-pager-last" 
													onclick="moverTodosElementos(periodoIniAux, periodoFinAux);"
													id="b1" style="display: none;"><a></a></li>
															
													<li class="item-pager-controls item-pager-next"
														onclick="moverElementoSeleccionado(periodoIniAux, periodoFinAux);"
														id="b2" style="display: none;"><a></a></li>
													
													<li class="item-pager-controls item-pager-previous" 
													onclick="moverElementoSeleccionado(periodoFinAux,periodoIniAux);"
													id="b3" style="display: none;"><a></a></li>
															
													<li class="item-pager-controls item-pager-first" 
													onclick="moverTodosElementos(periodoFinAux, periodoIniAux);"
													id="b4" style="display: none;"><a></a></li>
														
													</ul>
												</span> 
												<span class="item-periodo-right"> 
												<select class="select-wrapper" name="periodoFinAux" id="periodoFinAux"
													multiple="multiple" style="display: none;height: 150px; width:260px">
													</select>
												</span>
											</div>
											<div class="form-actions">
												<input class="btn btn-default btn-ir" type="button"
													value="Consultar" onclick="enviarFormularioReporte()"></input>
													<input type="button"
												value=" <%=msj.getString("recordarClave.Atras")%> "
												class="btn btn-default"
												onclick="javascript:history.back(1);">
											</div>
<!-- 										</form> -->

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
<!-- 									<li><a href="">Visitas Casa de Nariño</a></li> -->
<!-- 									<li><a href="">Datos de contacto</a></li> -->
<!-- 									<li><a href="">Escríbale al Presidente</a></li> -->
<!-- 									<li><a href="">PSQR</a></li> -->
<!-- 									<li><a href="">Colombia Compra Eficiente</a></li> -->
<!-- 									<li><a href="">Avisos Convocatoria Pública</a></li> -->
<!-- 									<li><a href="">Notificaciones por Aviso</a></li> -->
<!-- 									<li><a href="">Notificaciones Judiciales</a></li> -->
<!-- 									<li><a href="">Proveedores</a></li> -->
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
								Sostenible de Colombia. Sistema Nacional Ambiental. <a href="">atencionalciudadano@ideam.gov.co</a>
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
		style="z-index: 3; position: absolute; width: 135px; height: 80px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;">
			<label for="exampleInputEmail1"
				style="text-align: center; color: white; font-weight: bold;">
				<%=msj.getString("home.popAyuda.titulo")%></label>
		</div>
		<label for="exampleInputEmail1"><a
			href="<%=basePath2%>Usuario-Web/pub/registroUsuario.jsp"> *<%=msj.getString("home.popAyuda.registrarse")%></a></label>
		<label for="exampleInputEmail1" style="text-align: left;"><a
			href="<%=basePath2%>Usuario-Web/pub/recordarClave.jsp?idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
				*<%=msj.getString("home.popAyuda.recordar")%></a></label> <label
			for="exampleInputEmail1" onclick="popUpAyudaClose()"
			style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
	</div>
	<form action="<%=basePath%>recordarClaveUsuarioServlet" method="post"
		onsubmit="return validar();" name="formRecordarClave"
		id="formRecordarClave">
		<input type="hidden" name="email" id="email" size="30"
			style="width: 250px">
	</form>
	
	<form action="<%=basePath%>consultarReporteServlet" method="post" name="formConsultaReporte" id="formConsultaReporte">
			<input type="hidden" name="treporte" id="treporte" value="">
			<input type="hidden" name="divterritorio" id="divterritorio" value="">
			<input type="hidden" name="periodo1" id="periodo1" value="">
			<input type="hidden" name="periodoFin" id="periodoFin" value="">
	</form>
</body>
</html>

