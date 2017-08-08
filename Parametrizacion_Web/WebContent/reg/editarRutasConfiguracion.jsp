<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.entities.Rutas"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";

String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
	ArrayList<Noticias> noticias = CargaDatosInicial.getNoticias(); 
	ArrayList<Noticias> eventos = CargaDatosInicial.getEventos();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Editar Rutas de Configuración</title>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet"	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />

<script type="text/javascript">
	function lenguaje(id){
		if(id==1){
			document.getElementById('lenguaje').value="ES";
		}else{
			document.getElementById('lenguaje').value="EN";
		}
		document.getElementById('pagina').value="<%=request.getRequestURI()%>"; 
		document.getElementById('home').submit();
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
</script>
</head>
<body class='sidebar-first front'>
		<div id="page">
			<div id="header">
				<div id="header-first" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
							<div id="block-logo" class="block">
								<div class="content">
									<a href=""><img src="../img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->
							<div id="block-images-header" class="block">
								<div class="content">
									<a href=""><img src="../img/img-min.png" alt=""></a> <a
										href="/"><img src="../img/img-prosperidad.png" alt=""></a>
									<a href="/"><img src="../img/img-moore.png" alt=""></a> <a
										href="/"><img src="../img/img-patrimonio.png" alt=""></a>
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
											<label for="exampleInputPassword1">AAAAAA</label>
										</div>

										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath%>detallarNoticia.jsp?"><%=msj.getString("home.registrado.modificar")%></a></label>
										</div>
										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath%>detallarNoticia.jsp?"><%=msj.getString("home.registrado.cerrar")%></a></label>
										</div>
									</div>
									<ul class="social-menu item-list">
										<li class="menu-item twitter first"><a href="/"></a></li>
										<li class="menu-item facebook"><a></a></li>
										<li class="menu-item en"><a onclick="lenguaje(2);"></a></li>
										<li class="menu-item es"><a onclick="lenguaje(1);"></a></li>
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
								<div class="content">
									<ul class="main-menu item-list">
										<li class="menu-item home active"><a href="/"><%=msj.getString("home.carbono")%></a></li>
										<li class="menu-item about-us"><a href="/"><%=msj.getString("home.bosque")%></a></li>
										<li class="menu-item noticia"><a
											href="<%=basePath%>verNoticiasYEventos.jsp"><%=msj.getString("home.noticiasEventos")%></a></li>
										<li class="menu-item services"><a href="/"><%=msj.getString("home.documentacion")%></a></li>
										<li class="menu-item contact-us"><a href="/"><%=msj.getString("home.bosqueCifras")%></a></li>
										<li class="menu-item work-oferts"><a href="/"><%=msj.getString("home.visor")%></a></li>
										<li class="menu-item sic last"><a href="/"><%=msj.getString("home.especies")%></a></li>
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
											<li class="item-menu first"><a href="">Presentación</a></li>
											<li class="item-menu"><a href="">Aspectos Generales
													Colombia</a></li>
											<li class="item-menu"><a href="">Normatividad</a></li>
											<li class="item-menu"><a href="">Sitios de Interés</a></li>
											<li class="item-menu"><a href="">Docuemntación</a></li>
											<li class="item-menu"><a href="">Protocolos</a></li>
											<li class="item-menu"><a href="">Glosario</a></li>
											<li class="item-menu last"><a href="">Banco de datos</a></li>
										</ul>
									</div>
									<div id="block-eventos" class="block-gray block">
										<h2><%=msj.getString("home.eventos")%></h2>
										
									</div>
									<div id="block-noticias" class="block-gray block">
										<h2><%=msj.getString("home.noticias")%></h2>
										
									</div>
								</div>
								<!-- content-inner -->
							</div>

							<div id="content">
								<div class="content-inner">


									<div class="pre-content">

										<div class="breadcrumb">
											<span class="item-breadcrumb separator home"><a
												href="#">Home</a></span> <span class="item-breadcrumb separator"></span>
											<span class="item-breadcrumb separator"><a href="#">Lorem</a></span>
											<span class="item-breadcrumb separator"></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>

										<div class="menu-user">
											<ul class="items-menu">
												<li class="item-menu first">Usuario Activo</li>
												<li class="item-menu last active"><a href="">Cerrar
														Sesión</a></li>
											</ul>
										</div>

									</div>
									<!-- precontent -->
									<form id="editarParametros" name="editarParametros" action="<%=basePath%>editarParametrosServlet" method="post">
												<div class="form-datos-parcela form-columnx1" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("parametrizacion.advertencia")%></label>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"></label>
														</div>
														<div class="form-group">
															<%ArrayList<Rutas> rutasB = CargaDatosInicial.getArrayRutasApollo();
														    int numRutas=0;
															for (int i = 0; i < rutasB.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB.get(i);
																  %><%=rutaApollo.getNombre()%>: <br><input type="text" class="form-control" title="<%=rutaApollo.getDescripcion()%>" id="textRuta<%=i%>" name="textRuta<%=i%>" value="<%=rutaApollo.getRuta()%>"><br><%
																numRutas++;
															}
															%>
															
														</div>
														
		
														<div class="form-actions">
														
														<input type="hidden" value="<%=numRutas%>" name="numRutas" id="numRutas">
														<input class="btn btn-default" id="btnModificar" name="btnModificar" type="submit" value="<%=msj.getString("parametrizacion.almacenar")%>" > 
														<input id="btnCancelar" name="btnCancelar" type="button" value="<%=msj.getString("parametrizacion.cancelar")%>" onclick="location.href='<%=basePath%>reg/home.jsp';">
				
														</div>
													</div>
						
													</form>
													<!-- fin resultados -->
												
								</div>
								<!-- content-inner -->
							</div>
							<!-- /.content-->
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
											<li><a class="vicepresidencia" href="">Vicepresidencia</a></li>
											<li><a class="min-justicia" href="">MinJusticia</a></li>
											<li><a class="min-defensa" href="">MinDefensa</a></li>
											<li><a class="min-interior" href="">MinInterior</a></li>
											<li><a class="min-relaciones" href="">MinRelaciones</a></li>
											<li><a class="min-hacienda" href="">MinHacienda</a></li>
											<li><a class="min-minas" href="">MinMinas</a></li>
											<li><a class="min-comercio" href="">MinComercio</a></li>
											<li><a class="min-tic" href="">MinTIC</a></li>
											<li><a class="min-cultura" href="">MinCultura</a></li>
											<li><a class="min-agricultura" href="">MinAgricultura</a></li>
											<li><a class="min-ambiente" href="">MinAmbiente</a></li>
											<li><a class="min-transporte" href="">MinTransporte</a></li>
											<li><a class="min-vivienda" href="">MinVivienda</a></li>
											<li><a class="min-educacion" href="">MinEducación</a></li>
											<li><a class="min-trabajo" href="">MinTrabajo</a></li>
											<li><a class="min-salud" href="">MinSalud</a></li>
										</ul>
									</div>
<!-- 									<div class="menu-servicios menu-postscript"> -->
<!-- 										<h3>Servicios de Cuidadanía</h3> -->
<!-- 										<ul> -->
<!-- 											<li><a href="">Visitas Casa de Nariño</a></li> -->
<!-- 											<li><a href="">Datos de contacto</a></li> -->
<!-- 											<li><a href="">Escríbale al Presidente</a></li> -->
<!-- 											<li><a href="">PSQR</a></li> -->
<!-- 											<li><a href="">Colombia Compra Eficiente</a></li> -->
<!-- 											<li><a href="">Avisos Convocatoria Pública</a></li> -->
<!-- 											<li><a href="">Notificaciones por Aviso</a></li> -->
<!-- 											<li><a href="">Notificaciones Judiciales</a></li> -->
<!-- 											<li><a href="">Proveedores</a></li> -->
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
										Sostenible de Colombia. Sistema Nacional Ambiental. <a href="">atencionalciudadano@ideam.gov.co</a>
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
			</div>
			<!--/.page -->
		</div>
		
		
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