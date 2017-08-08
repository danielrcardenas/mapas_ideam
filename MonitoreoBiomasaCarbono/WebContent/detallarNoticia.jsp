<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%> 
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<meta charset="utf-8" />
<title>Detalle Noticia</title>
<%
	String basePath = request.getScheme() + "://"
        + request.getServerName() + ":" + request.getServerPort()
        + request.getContextPath() + "/";
    String indice = request.getParameter("i");
    ArrayList<Noticias> noticias = (ArrayList<Noticias>) request.getSession()
    .getAttribute("noticia");
    Noticias n1 = noticias.get(Integer.valueOf(indice));
    ArrayList<Noticias> eventos = CargaDatosInicialHome.getEventosHome(); 
    ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
    String basePath2 = request.getScheme() + "://"
    		+ request.getServerName() + ":" + request.getServerPort() + "/";
    Usuario usuario = (Usuario)request.getSession().getAttribute("usuarioSesion");
    LenguajeI18N i18n = (LenguajeI18N)request.getSession().getAttribute("i18nAux");
%>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="css/html.css" media="all" />
<script type="text/javascript">
    function volver() {
        history.back(-1);
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
		document.getElementById('pagina').value="<%=request.getRequestURI()%>?i=<%=indice%>";
		document.getElementById('home').submit();
	}
</script>
</head>
<body class='sidebar-first front'>
	<form id="home" action="<%=basePath%>idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
		<div id="page">
			<div id="header">
				<div id="header-first" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
							<div id="block-logo" class="block">
								<div class="content">
									<a href=""><img src="img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->
							<div id="block-images-header" class="block">
								<div class="content">
									<a href=""><img src="img/img-min.png" alt=""></a> <a
										><img src="img/img-prosperidad.png" alt=""></a>
									<a ><img src="img/img-moore.png" alt=""></a> <a
										><img src="img/img-patrimonio.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->
							<div id="block-top-menu" class="block block-menu">
								<div class="content">
									<div id="form-loguin-header" role="form">
										<div class="form-group">
											<label for="exampleInputEmail1">Usuario</label> <input
												type="text" class="form-control" id="logName" name="logName"
												placeholder="">
										</div>
										<div class="form-group">
											<label for="exampleInputPassword1">Contraseña</label> <input
												type="password" class="form-control" id="logPassword"
												name="logPassword" placeholder="">
										</div>
										<div class="form-actions">
											<input type="button" class="btn btn-default" value="Ir"
												onclick="enviarForms()"></input>
										</div>
									</div>
									<ul class="social-menu item-list">
										<!-- Aca estaba lo de twitter -->
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
												href="#">Home</a></span> <span class="item-breadcrumb separator">></span>
											<span class="item-breadcrumb separator">></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>
									</div>

									<div class="content">
										<div>
											<h3><%=msj.getString("home.noticias")%></h3>
										</div>
										<div class="form-datos-parcela form-columnx2">
											<div class="form-group">
												<img src="<%=n1.getPathImagen()%>">
											</div>
											<div class="form-group"></div>
											<div class="form-group">
												<div>
													<label><%=msj.getString("crea.noticias.nombre")%></label>
												</div>
												<div>
													<input type="text" readonly="readonly" class="form-control"
														value="<%=n1.getNombre()%>" />
												</div>
											</div>
											<div class="form-group">
												<div>
													<label><%=msj.getString("crea.noticias.fecha")%></label>
												</div>
												<div>
													<input type="text" readonly="readonly" class="form-control"
														value="<%=n1.getFecha()%>" />
												</div>
											</div>
											<div class="form-group item-textarea">
												<div>
													<label><%=msj.getString("crea.noticias.descripcion")%></label>
												</div>
												<div>
													<textarea><%=n1.getDescripcion()%></textarea>
												</div>
											</div>
											<div class="form-actions">
												<a href="javascript:volver()"><%=msj.getString("noticias.volver")%></a>
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
					<!-- /.section-->
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