<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
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
	ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
	LenguajeI18N i18n = (LenguajeI18N)request.getSession().getAttribute("i18nAux");
%>

<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="../jquerymobile/jquery.mobile-1.4.2.min.js"></script>
<script src="../jquerymobile/popup.js"></script>
<script src="js/slippry.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="js/logIn.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="css/layout.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/menu.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="css/html.css" media="all" />

<script>
  var mouseX=0;
  var mouseY=0;

<%--   <%try{ --%>
// 	  msj.getString("home.popAyuda.titulo");
<%--   }catch(Exception e){%> --%>
<%--   			setTimeout(function(){document.location.href = '<%=basePath2%>'+"MonitoreoBiomasaCarbono/home.jsp"},500); --%>
<%--   <%}%> --%>

$(function(){
    $('#slippry-demo').slippry();
   });

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

</script>
</head>
<body class='sidebarlast front' onload="postEdit()"
	onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">
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
	<form id="home" action="<%=basePath%>idioma" method="post">
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
									<a href="<%=basePath2%>MonitoreoBiomasaCarbono/home.jsp"><img src="img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-images-header" class="block">
								<div class="content">
									<a href="http://www.minambiente.gov.co/">
									<img src="img/img-min.png" alt="">
									</a> 
									<a href="http://wsp.presidencia.gov.co/portal/Paginas/default.aspx">
									<img src="img/img-prosperidad.png" alt="">
									</a>
									<a href="http://www.moore.org/">
									<img src="img/img-moore.png" alt="">
									</a> 
									<a href="http://www.patrimonionatural.org.co/">
									<img src="img/img-patrimonio.png" alt="">
									</a>
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
 												<li class="first leaf"><a><%=msj.getString("home.bosques.ifn")%></a></li>
												<li class="leaf"><a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteBosques.jsp"><%=msj.getString("home.bosques.cuantificacion")%></a></li>
<%-- 												<li class="leaf"><a href="<%=basePath2%>/Parcelas-Web/pub/consultarParcela.jsp?id=<%=Util.encriptar(i18n.getLenguaje()+";null")%>"><%=msj.getString("home.bosques.alertas")%></a></li> --%>
<%-- 												<li class="leaf"><a href="<%=basePath2%>/Parcelas-Web/pub/consultarParcela.jsp?id=<%=Util.encriptar(i18n.getLenguaje()+";null")%>"><%=msj.getString("home.bosques.ifc")%></a></li> --%>
											</ul>
										</li>
										
										
										<li class="menu-item about-us expanded"><a><%=msj.getString("home.noticiasEventos")%></a>
<!--  										    <ul class="menu"> -->
<!-- 										        <li class="menu-item noticia"> -->
<%--  										        <a href="<%=basePath2%>MonitoreoBiomasaCarbono/verNoticiasYEventos.jsp">Consultar Noticias y Eventos</a></li> --%>
<!-- 											    <li class="menu-item noticia"> -->
											    
<%-- 											    <a href="<%=basePath2%>MonitoreoBiomasaCarbono/reg/crearNoticia.jsp">Crear Noticias o Eventos</a></li> --%>
<!--  									        </ul> -->
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

			<div id="preface" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix">

						<div class="slide">
							<ul id="slippry-demo">
<!-- 								<li><a href="#slide1"><img src="img/cinta0.jpg" alt=""></a> -->
<!-- 									<span class="caption"><h3></h3> -->
<!-- 										<p> </p></span></li> -->
								<li><a href="#slide2"><img src="img/cinta1.jpg" alt=""></a>
									<span class="caption"><h3> </h3>
										<p> </p></span></li>
								<li><a href="#slide3"><img src="img/cinta2.jpg" alt=""></a>
									<span class="caption"><h3> </h3>
										<p> </p></span></li>
								<li><a href="#slide1"><img src="img/cinta3.jpg" alt=""></a>
									<span class="caption"><h3></h3>
										<p> </p></span></li>
								<li><a href="#slide2"><img src="img/cinta4.jpg" alt=""></a>
									<span class="caption"><h3> </h3>
										<p> </p></span></li>
								<li><a href="#slide3"><img src="img/cinta5.jpg" alt=""></a>
									<span class="caption"><h3> </h3>
										<p> </p></span></li>
								<li><a href="#slide3"><img src="img/cinta6.jpg" alt=""></a>
									<span class="caption"><h3> </h3>
										<p> </p></span></li>
							</ul>
						</div>

					</div>
					<!-- /.section-inner-->
				</div>
				<!--/.section -->
			</div>
			<!-- /.section-wrapper preface-->

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">

							<div id="content">
								<div class="content-inner">

									<div id="block-home" class="block-gray blockx345 block">
										<div id="block-video">
											<iframe width="310" height="210" src="//www.youtube.com/embed/DhhmVENqKv4?version=3&loop=1&playlist=DhhmVENqKv4" 
											frameborder="0" allowfullscreen></iframe>
										</div>

										<div id="block-eventos">
											<h2><%=msj.getString("home.eventos")%></h2>
											<ul>
											<li> <a href="http://puu.sh/cwZJO/54805b7362.jpg" target="_blank">**Usted es nuestro invitado especial</a></li>
											<li>**Invitación al evento de cierre</li>
<li>**Capacitación Codechocó (Octubre 20 – 31 de 2014)
CDMB  (Noviembre 2014)</li>
<li>**Primer semestre 2015 se lanzará el boletín de alertas tempranas  del segundo semestre de 2014</li>
<li>**En el segundo semestre de 2015 se lanzará la tasa anual de deforestación calculada para el año 2014</li>
											</ul>
										</div>

										<div id="block-noticias">
											<h2><%=msj.getString("home.noticias")%></h2>
											<ul>
											<li>**El 31 de octubre se realiza la presentación de resultados del Sistema de Monitoreo de Bosques y carbono en Colombia</li>
<li>**El 31 de octubre de 2014 se lanzará el boletín de alertas tempranas  del primer semestre de 2014</li>
<li>**Colombia participará en la COP 20 de Cambio Climático</li>
<li>**Colombia participará del 14-16 de Noviembre en la cumbre ministerial de GEO</li>
											</ul>
										</div>
									</div>


									<div id="block-bosques" class=" blockx245 block-vermas block">
										<div class="content">
											<h2><%=msj.getString("home.bosque")%></h2>
											<img src="img/Bosque.jpg">
											<p>En el año 2013, la deforestación estimada fue de 120.933 has, lo cual 
											equivale a 1.8 veces el área de una metrópoli colombiana como la ciudad de Cali.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteBosques.jsp">Ver más</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2><%=msj.getString("home.carbono")%></h2>
											<img src="img/Carbono.jpg">
											<p>En 2012 los bosques de Colombia almacenaban 23.001.314.006 t CO2 
											en la biomasa aérea, que equivale al CO2 que se 
											necesita para llenar 112.000.000 de edificios 
											como la Torre Colpatria la cual tiene 63 pisos.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteCarbono.jsp">Ver más</a>
											</div>
										</div>
									</div>

									



								</div>
								<!-- content-inner -->
							</div>
							<!-- /.content-->

							<div id="sidebarlast">
								<div class="section-inner clearfix">

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2>Alertas Tempranas de Deforestaci&oacute;n</h2>
											<img src="img/Alertas.jpg">
											<p>Las &aacute;reas n&uacute;cleo de deforestaci&oacute;n en Colombia, en las cuales se concentra la mayor p&eacute;rdida de bosque se
											 localizan en los Caquet&aacute;-Putumayo, Meta-Guaviare y el eje San Jos&eacute; del Guaviare-Calamar.
											Se evidencia otro foco activo, en Antioquia, en los municipios de Segovia, Turbo, Ituango, Anor&iacute;, 
											y El Bagre, siendo las zonas de mayor afectaci&oacute;n con una p&eacute;rdida de bosque entre el 45 y el 75%.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>MonitoreoBiomasaCarbono/pub/alertasDeforestacion.jsp">Ver más</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2>Visor Geografico</h2>
											<img src="img/Visor.png">
											<p>Conozca dónde se localizan los Bosques de Colombia y observe los análisis de la información de los últimos 23 años 
											en los cuales se evidencia una desconexión de la masa boscosa de la Amazonia y el bosque natural de la cordillera oriental.
											 Con nuestro Visor geográfico, puede explorar las áreas de Deforestación, en las cuales se observa 
											cómo el 57% de la deforestación nacional se localiza en la región de la Amazonía, mientras que un 22% se reportó en la región andina.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>AdmIF/Parcela?accion=busqueda_parcelas&usuario=0&idioma=es">Ver más</a>
											</div>
										</div>
									</div>

									

								</div>
								<!-- /.section-inner-->
							</div>
							<!-- /.sidebar-wrapper-->


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
							<img src="img/gobierno.png" />
							<div class="menu-ministerios menu-postscript">
								<ul>
									<li><a class="vicepresidencia" href="http://www.vicepresidencia.gov.co/Paginas/Vicepresidencia-Colombia.aspx">Vicepresidencia</a></li>
									<li><a class="min-justicia" href="http://www.minjusticia.gov.co/">MinJusticia</a></li>
									<li><a class="min-defensa" href="http://www.mindefensa.gov.co/">MinDefensa</a></li>
									<li><a class="min-interior" href="http://www.mij.gov.co/">MinInterior</a></li>
									<li><a class="min-relaciones" href="http://www.cancilleria.gov.co/">MinRelaciones</a></li>
									<li><a class="min-hacienda" href="http://www.minhacienda.gov.co/">MinHacienda</a></li>
									<li><a class="min-minas" href="http://www.minminas.gov.co/mme/">MinMinas</a></li>
									<li><a class="min-comercio" href="http://www.mincit.gov.co/">MinComercio</a></li>
									<li><a class="min-tic" href="http://www.mintic.gov.co/">MinTIC</a></li>
									<li><a class="min-cultura" href="http://www.mincultura.gov.co/Paginas/default.aspx">MinCultura</a></li>
									<li><a class="min-agricultura" href="https://www.minagricultura.gov.co/Paginas/inicio.aspx">MinAgricultura</a></li>
									<li><a class="min-ambiente" href="http://www.minambiente.gov.co/web/index.html">MinAmbiente</a></li>
									<li><a class="min-transporte" href="https://www.mintransporte.gov.co/">MinTransporte</a></li>
									<li><a class="min-vivienda" href="http://www.minvivienda.gov.co/SitePages/Ministerio%20de%20Vivienda.aspx">MinVivienda</a></li>
									<li><a class="min-educacion" href="http://www.mineducacion.gov.co/1621/w3-channel.html">MinEducación</a></li>
									<li><a class="min-trabajo" href="http://www.mintrabajo.gov.co/">MinTrabajo</a></li>
									<li><a class="min-salud" href="http://www.minsalud.gov.co/Paginas/default.aspx">MinSalud</a></li>
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
							<p><%=msj.getString("pie.ideam") %></p>
							<p><%=msj.getString("pie.contacto") %></p>
							<p><%=msj.getString("pie.atencion") %></p>
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
	<form method="post" action="j_security_check" name="j_security_check"
		id="j_security_check">
		<input type="hidden" name="j_username" id="j_username" /> <br> <input
			type="hidden" name="j_password" id="j_password" />
	</form>
	<form method="post" action="<%=basePath%>registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /> <input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>
</body>
</html>