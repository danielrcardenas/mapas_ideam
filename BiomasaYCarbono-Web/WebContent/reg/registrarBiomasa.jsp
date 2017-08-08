<%@page import="co.gov.ideamredd.dao.CargaDatosInicialBiomasa"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar Proyectos</title>
</head>
<%
	String basePath = request.getScheme() + "://" 
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";
	String idParcela = Util.desencriptar(request.getParameter("id"));
	ArrayList<Noticias> noticias = CargaDatosInicialBiomasa.getNoticias();
	ArrayList<Noticias> eventos = CargaDatosInicialBiomasa.getEventos();
	ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
%>
<script type="text/javascript">
	$(function() {
	    $( "#accordion" ).accordion({
	      heightStyle: "content"
	    });
	  });
	$(function() {
	    $( "#fInicial" ).datepicker(); 
	});
	$(function() {
	    $( "#fIni" ).datepicker(); 
	});
	$(function() {
	    $( "#ffin" ).datepicker(); 
	});	

	function lenguaje(id){
		if(id==1){
			document.getElementById('lenguaje').value="ES";
		}else{
			document.getElementById('lenguaje').value="EN";
		}
		document.getElementById('pagina').value='<%=request.getRequestURI()%>'; 
		document.getElementById('home').action='<%=basePath%>idioma';
		document.getElementById('home').submit();
	}

	function registrarBiomasa() {
		var seleccion = document.getElementById("metodo");
		if (seleccion.value == 1) {
			document.getElementById("bioma").style.display = 'block';
			document.getElementById("fgenera").style.display = 'block';
			document.getElementById("metod").style.display = 'block';
			document.getElementById("nomMet").style.display = 'none';
			document.getElementById("descMet").style.display = 'none';
			document.getElementById("ecuaMet").style.display = 'none';
			document.getElementById("arcMet").style.display = 'none';
			document.getElementById("fecInigene").style.display = 'none';
			document.getElementById("fecFingene").style.display = 'none';
			document.getElementById("tFechas").style.display = 'none';
		} else if (seleccion.value == -1) {
			document.getElementById("bioma").style.display = 'none';
			document.getElementById("fgenera").style.display = 'none';
			document.getElementById("metod").style.display = 'none';
			document.getElementById("nomMet").style.display = 'none';
			document.getElementById("descMet").style.display = 'none';
			document.getElementById("ecuaMet").style.display = 'none';
			document.getElementById("arcMet").style.display = 'none';
			document.getElementById("fecInigene").style.display = 'none';
			document.getElementById("fecFingene").style.display = 'none';
			document.getElementById("tFechas").style.display = 'none';
		} else {
			document.getElementById("bioma").style.display = 'none';
			document.getElementById("fgenera").style.display = 'none';
			document.getElementById("metod").style.display = 'none';
			document.getElementById("nomMet").style.display = 'none';
			document.getElementById("descMet").style.display = 'none';
			document.getElementById("ecuaMet").style.display = 'none';
			document.getElementById("arcMet").style.display = 'none';
			document.getElementById("tFechas").style.display = 'block';
			document.getElementById("fecInigene").style.display = 'block';
			document.getElementById("fecFingene").style.display = 'block';
		}
	}

	function tipoMetodologia() {
		if (document.getElementById("metodologia").value == 2) {
			document.getElementById("nomMet").style.display = 'block';
			document.getElementById("descMet").style.display = 'block';
			document.getElementById("ecuaMet").style.display = 'block';
			document.getElementById("arcMet").style.display = 'block';
		} else {
			document.getElementById("nomMet").style.display = 'none';
			document.getElementById("descMet").style.display = 'none';
			document.getElementById("ecuaMet").style.display = 'none';
			document.getElementById("arcMet").style.display = 'none';
		}
	}

	function registroBiomasa(){
		document.getElementById('registroBiomasa').action='<%=basePath%>registrarbiomasa';
		document.getElementById('registroBiomasa').submit();
	}
</script>
<body class='sidebar-first front'>
	<form method="post" action="<%=basePath%>redireccion"
		id="registroBiomasa">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina"> <input type="hidden"
			name="idParcela" id="idParcela" value="<%=idParcela %>" /> <input type="hidden"
			name="dir" id="dir" />
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
											<span class="item-breadcrumb separator"><a href="#">Lorem</a></span>
											<span class="item-breadcrumb separator">></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>
									</div>
									<!-- precontent -->

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("registrar.biomasa")%></h3>
												<div id="block-datos-basicos">
													<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("registrar.biomasa.opciones")%></label>
															<div class="select-wrapper">
																<select id="metodo" name="metodo"
																	onchange="registrarBiomasa();"
																	title="Opciones para el registro de datos de Biomasa de la(s) parcela(s).">
																	<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<option value="1"><%=msj.getString("registrar.biomasa.registrar")%></option>
																	<option value="2"><%=msj.getString("registrar.biomasa.calcular")%></option>
																</select>
															</div>
														</div>
													</div>
													<div class="form-datos-parcela form-columnx2" role="form">
														<div id="fgenera" class="form-group" style="display: none;">
															<label for="exampleInputEmail1" id="lfechaGeneracion"><%=msj.getString("consulta.fecha.generacion")%></label>
															<input type="text" id="fInicial" name="fInicial" class="form-control">
														</div>
														<div class="form-group" id="bioma" style="display: none;">
															<label id="lbiomasa" for="exampleInputEmail1"><%=msj.getString("registrar.biomasa.aerea")%></label>
															<input name="biomasa" id="biomasa" class="form-control"
																title="Valor calcolado de Biomasa para la(s) parcela(s).">
														</div>
														<div class="form-group" id="metod" style="display: none;">
															<label for="exampleInputEmail1" id="lmetodologia"><%=msj.getString("detalle.proyecto.metodologia")%></label>
															<div class="select-wrapper">
																<select id="metodologia" name="metodologia" onchange="tipoMetodologia()"
																	title="Tipo de metodología implementada en la obtención del valor de Biomasa">
																	<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<option value="1"><%=msj.getString("metodologia.ideam")%></option>
																	<option value="2"><%=msj.getString("metodologia.otra")%></option>
																</select>
															</div>
														</div>
														<div class="form-group" id="nomMet" style="display: none;">
															<label for="exampleInputEmail1" id="lnombre"><%=msj.getString("detalle.proyecto.nombreMetodologia")%></label>
															<input name="nombre" id="nombre" class="form-control"
																title="Nombre de la metodología implementada.">
														</div>
														<div class="form-group item-textarea" id="descMet" style="display: none;">
															<label for="exampleInputEmail1" id="ldescripcion"><%=msj.getString("detalle.proyecto.descripcionMetodologia")%></label>
															<textarea name="descripcion" id="descripcion"
																title="Descripcion breve de la metodologia implementada"></textarea>
														</div>
														<div class="form-group" id="ecuaMet" style="display: none;">
															<label for="exampleInputEmail1" id="lecuacion"><%=msj.getString("detalle.proyecto.ecuacionMetodologia")%></label>
															<input name="ecuacion" id="ecuacion" class="form-control"
																title="Ecuación empleada en la metodologia para el cálculo del valor de la biomasa.">
														</div>
														<div class="form-group" id="arcMet" style="display: none;">
															<label for="exampleInputEmail1" id="lfile"><%=msj.getString("detalle.proyecto.archivoMetodolgia")%></label>
															<input type="file" id="archivo" name="archivo"
																style="display: none;" class="form-control" />
														</div>
														<h3 id="tFechas" style="display: none;" class="item-clear-both"><%=msj.getString("consulta.proyecto.tiempo") %></h3>
														<div class="form-group" id="fecInigene" style="display: none;">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaInicio") %>:</label> <input
																type="text" id="fIni" name="fIni">
														</div>
														<div class="form-group" id="fecFingene" style="display: none;">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaFin")%>:</label> <input
																type="text" id="ffin" name="ffin">
														</div>
														<div class="form-actions">
															<input type="button"
																value="<%=msj.getString("crea.noticia.crear")%>"
																onclick="registroBiomasa();"> 
														</div>
													</div>
												</div>
												<!-- fin resultados -->
											</div>
											<!-- block datos basicos -->
										</div>
										<!--  accordeon -->
									</div>
								</div>
								<!-- block resultados busqueda -->
							</div>
							<!-- content-inner -->
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