<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicialBiomasa"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Definir estado de biomasa</title>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";
	ArrayList<Noticias> noticias = CargaDatosInicialBiomasa.getNoticias();
	ArrayList<Noticias> eventos = CargaDatosInicialBiomasa.getEventos();
	String id = Util.desencriptar(request.getParameter("bio"));
	String[] valores = CargaDatosInicialBiomasa.configurarEstado(Integer.valueOf(id));
	ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
%>

<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />

<script type="text/javascript">
	function volver() {
		history.back(-1);
	}
	
	function lenguaje(id){
		if(id==1){
			document.getElementById('lenguaje').value="ES";
		}else{
			document.getElementById('lenguaje').value="EN";
		}
		document.getElementById('pagina').value="<%=request.getRequestURI()%>";
		document.getElementById('home').submit();
	}

	function cargaDatos() {
		var ver = <%=valores[0]%>;
		var atp = <%=valores[1]%>;
		var inc = <%=valores[2]%>;
		var i, j, k;
		for (i = 0; i < document.getElementById('verificado').options.length; i++) {
			if (document.getElementById('verificado').options[i].value == ver) {
				document.getElementById('verificado').options[i].selected = 'selected';
			}
		}
		for (j = 0; j < document.getElementById('atipico').options.length; j++) {
			if (document.getElementById('atipico').options[j].value == atp) {
				document.getElementById('atipico').options[j].selected = 'selected';
			}
		}
		for (k = 0; k < document.getElementById('incluido').options.length; k++) {
			if (document.getElementById('incluido').options[k].value == inc) {
				document.getElementById('incluido').options[k].selected = 'selected';
			}
		}
	}

	function actualizarEstado(){
		document.getElementById('home').action='<%=basePath%>estadobiomasa';
		document.getElementById('home').submit();
	}
</script>
</head>
<body onload="cargaDatos();">
	<form id="home" action="<%=basePath%>idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina"> <input type="hidden"
			name="idBiomasa" id="idBiomasa" value="<%=id%>">
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
										<%
											for (int j = 0; j < eventos.size(); j++) {
												Noticias e = eventos.get(j);
										%>
										<ul>
											<li class="">
												<h3><%=e.getFecha().toString().startsWith("00") ? e
						.getFecha().toString().replace("00", "20") : e
						.getFecha()%></h3>
												<p>
													<strong><%=e.getHora()%></strong>
													<%=e.getDescripcion().length() > 50 ? e.getDescripcion()
						.substring(0, 50) + "..." : e.getDescripcion()%></p>
												<p>
													<a href="<%=basePath%>detallarEvento.jsp?e=<%=j%>"><%=msj.getString("home.verMas")%></a>
												</p>
											</li>
										</ul>
										<%
											}
										%>
									</div>
									<div id="block-noticias" class="block-gray block">
										<h2><%=msj.getString("home.noticias")%></h2>
										<ul>
											<%
												for (int i = 0; i < noticias.size(); i++) {
													Noticias n = noticias.get(i);
											%>
											<li class="item-noticia first"><img src="../img/"
												<%=n.getPathImagen()%>>
												<h3><%=n.getFecha()%></h3>
												<p><%=n.getDescripcion().length() > 50 ? n.getDescripcion()
						.substring(0, 50) + "..." : n.getDescripcion()%>
												</p>
												<p>
													<a href="<%=basePath%>detallarNoticia.jsp?i=<%=i%>"><%=msj.getString("home.verMas")%></a>
												</p></li>
											<%
												}
											%>
										</ul>
									</div>
								</div>
								<!-- content-inner -->
							</div>

							<div id="content">
								<div class="content-inner">

									<div class="pre-content">
										<div class="breadcrumb">
											<span class="item-breadcrumb separator home"><a
												href="#">Home</a></span> <span class="item-breadcrumb separator">></span>
											<span class="item-breadcrumb separator">></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>
									</div>

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("estado.biomasa")%></h3>
												<div class="form-localizacion" role="form">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("estado.biomasa.verificado")%></label>
														<div class="select-wrapper">
															<select id="verificado" name="verificado">
																<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																<option value="0"><%=msj.getString("opcion.no")%></option>
																<option value="1"><%=msj.getString("opcion.si")%></option>
															</select>
														</div>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("estado.biomasa.atipico")%></label>
														<div class="select-wrapper">
															<select id="atipico" name="atipico">
																<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																<option value="0"><%=msj.getString("opcion.no")%></option>
																<option value="1"><%=msj.getString("opcion.si")%></option>
															</select>
														</div>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("estado.biomasa.incluido")%></label>
														<div class="select-wrapper">
															<select id="incluido" name="incluido">
																<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																<option value="0"><%=msj.getString("opcion.no")%></option>
																<option value="1"><%=msj.getString("opcion.si")%></option>
															</select>
														</div>
													</div>
													<div class="form-actions">
														<input type="button" onclick="actualizarEstado();" value="Registrar">
													</div>
												</div>
											</div>
										</div>
									</div>

								</div>
							</div>
							<!-- /.content-->
						</div>
						<!-- /.section-wrapper-->
					</div>
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
							<div class="menu-servicios menu-postscript">
								<h3>Servicios de Cuidadanía</h3>
								<ul>
									<li><a href="">Visitas Casa de Nariño</a></li>
									<li><a href="">Datos de contacto</a></li>
									<li><a href="">Escríbale al Presidente</a></li>
									<li><a href="">PSQR</a></li>
									<li><a href="">Colombia Compra Eficiente</a></li>
									<li><a href="">Avisos Convocatoria Pública</a></li>
									<li><a href="">Notificaciones por Aviso</a></li>
									<li><a href="">Notificaciones Judiciales</a></li>
									<li><a href="">Proveedores</a></li>
								</ul>
							</div>
							<div class="sistema-web-presidencia select-postscript">
								<h3>Sistema Web Presidencia</h3>
								<div class="select-wrapper">
									<select>
										<option>1</option>
									</select>
								</div>
								<div class="form-actions form-wrapper" id="edit-actions">
									<input type="submit" id="edit-submit" name="op" value="Ir"
										class="form-submit">
								</div>
							</div>
							<div class="dependecias-presidencia select-postscript">
								<h3>Dependencias Presidencia</h3>
								<div class="select-wrapper">
									<select>
										<option>1</option>
									</select>
								</div>
								<div class="form-actions form-wrapper" id="edit-actions">
									<input type="submit" id="edit-submit" name="op" value="Ir"
										class="form-submit">
								</div>
							</div>
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
								<li><a href=""><%=msj.getString("home.home")%></a></li>
								<li><a href=""><%=msj.getString("home.mapa")%></a></li>
								<li><a href=""><%=msj.getString("home.documentacion")%></a></li>
								<li><a href=""><%=msj.getString("home.protocolos")%></a></li>
								<li><a href=""><%=msj.getString("home.links")%></a></li>
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
		</div>
		<!--/.page -->
		</div>
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