<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Date"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<%
HttpSession sesion = request.getSession(false); 
if (!request.isRequestedSessionIdValid() || sesion == null) { 
	response.sendRedirect("/MonitoreoBC-WEB"); 
	return;
}
sesion.setMaxInactiveInterval(10);
ResourceBundle msj = (ResourceBundle)sesion.getAttribute("i18n");

long timeout = (long) sesion.getMaxInactiveInterval();
long lastaccess = sesion.getLastAccessedTime();
Date vencimiento_sesion = new Date(lastaccess + timeout*10);
Date now = new Date();

try {
	if (msj == null) {
		if (vencimiento_sesion.before(now) || true) {
			sesion = request.getSession(true);
		    LenguajeI18N i18n = new LenguajeI18N();
			i18n.setLenguaje("es");
			i18n.setPais("CO");
			msj = i18n.obtenerMensajeIdioma();
			sesion.setAttribute("i18n", msj); 
			sesion.setAttribute("i18nAux", i18n);
		}
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
	
sesion.setAttribute("usuarioSesion", usuario);

DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String str_vencimiento_sesion = dateFormat.format(vencimiento_sesion);

%>
<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script> -->

<script src="js/jquery.min.js"></script>
<script src="js/slippry.min.js"></script> 
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="js/logIn.js"></script>
<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/<% out.print(estilo); %>" media="all" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>


</head>

<body>
<div id="container">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1000; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
		<div id="header" style="z-index: 1001;">
	
			<div id="header-first" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix">
	
						<div id="block-logo" class="block">
							<div class="content">
								<a href="/MonitoreoBC-WEB/reg/indexLogOn.jsp"><img src="img/logo.png" alt=""></a>
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
									<!--<li class="menu-item facebook" style="cursor: pointer;"><a></a></li>-->
									<li class="menu-item en" style="cursor: pointer;"><a
										onclick="lenguaje(2, '<%=request.getRequestURI()%>');"></a></li>
									<li class="menu-item es" style="cursor: pointer;"><a
										onclick="lenguaje(1, '<%=request.getRequestURI()%>')"></a></li>
								</ul>
	
							</div>
							<!-- /.content -->
						</div>
						<!--/.block -->
	
					</div>
				</div>
			</div>
			<!-- /.section-wrapper-->
	
	
			<div id="header-second" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix">
						<div id="block-main-menu" class="block block-menu">
						<!-- 							MENU VERDE -->
						
							<div class="content">
								<ul class="main-menu item-list">
									<li class="menu-item home expanded"><a><%=msj.getString("home.carbono")%></a>
										<ul class="menu">
											<li class="first leaf"><a onclick="cargarPagina('/MonitoreoBC-WEB/proyectos/consultaProyecto.jsp');" href="javascript:void(0);"><%=msj.getString("home.carbono.actividades")%></a></li>
											<!-- <li class="first leaf"><a href="javascript:content.load('/MonitoreoBC-WEB/proyectos/consultaProyecto.jsp');"><%=msj.getString("home.carbono.actividades")%></a></li> -->
											<li class="leaf"><a href="/MonitoreoBC-WEB/pub/consultarReporteCarbono.jsp"><%=msj.getString("home.carbono.reportes")%></a></li>
											<!-- Enlace para consulta de Parcelas -->
										</ul></li>                        
									<li class="menu-item about-us expanded"><a><%=msj.getString("home.bosque")%></a>
										<ul class="menu">
											<li class="leaf"><a href="/MonitoreoBC-WEB/pub/consultarReporteBosques.jsp"><%=msj.getString("home.bosques.cuantificacion")%></a></li>
											<li class="leaf"><a href="/MonitoreoBC-WEB/pub/alertasDeforestacion.jsp"><%=msj.getString("home.bosques.alertas")%></a></li>
											<li class="leaf"><a href="/MonitoreoBC-WEB/indicadores/reporteInventarios.jsp">Reporte Inventarios</a></li>
											<li class="leaf"><a href="/MonitoreoBC-WEB/indicadores/reporteInventariosParcela.jsp">Reporte Inventarios de Parcela</a></li>
											<li class="leaf"><a href="/MonitoreoBC-WEB/indicadores/calculoEstructuraComposicion.jsp">Calculo de estructura y composicion</a></li> 
											<li class="leaf"><a href="/MonitoreoBC-WEB/indicadores/consultaIndicadoresSimilitud.jsp">Consulta Indicadores de Similitud</a></li>
											<%if(usuario != null){%><li class="leaf"><a href="/AdmIF/Parcela?accion=busqueda_parcelas&usuario=<%=Util.encriptar(usuario.getIdentificacion())%>&idioma=<%=i18n.getLenguaje()%>">Inventarios forestales</a></li><%}%>
										</ul>
									</li>
									
									
									<li class="menu-item about-us expanded"><a><%=msj.getString("home.noticiasEventos")%></a>
										    <ul class="menu"> 
									        <li class="menu-item noticia"><a 
									        href="/MonitoreoBC-WEB/pub/consultaNoticias.jsp">Noticias</a></li>
											    <li class="menu-item noticia"><a
											    href="/MonitoreoBC-WEB/pub/consultaEventos.jsp">Eventos</a></li>
											    
											    <%if(usuario != null)
												if(ControlPermisos.tienePermiso(diccionarioPermisos, 141))
												{
											%>
											    	<li class="menu-item noticia"><a
										    		href="/MonitoreoBC-WEB/noticias/crearNoticiasEventos.jsp">Crear Noticias y Eventos</a></li>
										    <%} %>
										    
								        </ul> 
									</li>
										
									<li class="menu-item services">
									<a href="/MonitoreoBC-WEB/extra/documentacion.jsp" ><%=msj.getString("home.documentacion")%></a>  
									</li>
									
									
									<%if(usuario == null){ %>
										<li class="menu-item contact-us"><a href="/MonitoreoBC-WEB/pub/bosqueEnCifras.jsp"><%=msj.getString("home.bosqueCifras")%></a></li>
									<%}else{%>
										<li class="menu-item work-oferts expanded"><a>Menu Usuario</a>
											<ul class="menu">
												<li class="first leaf"><a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp">Datos Personales</a></li>
													
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 107)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/reg/imagenUsuario.jsp">Registrar Imagen de Usuario</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 108)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/imageus/consultarImagenesUsuarios.jsp">Consultar Imagenes de Usuarios</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 84)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/registraLicenciaUso.jsp">Registrar Licencia de Uso</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 85)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/consultaLicenciasUso.jsp">Consultar Licencias de Uso</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 80)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/registraRol.jsp">Registrar Rol</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 79)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/consultaRoles.jsp">Consultar Roles</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 71)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/consultarUsuarios.jsp">Consultar Usuarios</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 143)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/generarReportesUsuarios.jsp">Generar Reporte de Usuarios</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 162)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/admin/cargarDatosAlertasTempranas.jsp">Cargar Datos Alertas Tempranas</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 75)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/descarga/ConsultarDescargaImagenes.jsp">Descargas de Usuarios</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 144)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/descarga/ConsultarInventarioImagenes.jsp">Inventario de Imagenes</a></li><%}%><%if(ControlPermisos.tienePermiso(diccionarioPermisos, 261)){%><li class="leaf"><a href="/MonitoreoBC-WEB/admin/consultaReportes.jsp">Administrar Reportes</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 99)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/parametrizacion/editarRutasConfiguracion.jsp">Parametros del sistema</a></li>
												<%} %>
												<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 145)){%>
													<li class="leaf"><a href="/MonitoreoBC-WEB/borrarArchivosServlet">Borrar Temporales</a></li>
												<%} %>
											</ul>
										</li>
									<%} %>
									
									
									<li class="menu-item work-oferts expanded"><a><%=msj.getString("home.visor")%></a>
										<ul class="menu">
											<li class="first leaf"><a href="http://seinekan.ideam.gov.co/SMBYC/ApolloPro.aspx">Visor Apollo</a></li>
											<li class="leaf"><a href="/AdmIF/Parcela?accion=busqueda_parcelas&idioma=es">Visor de Parcelas</a></li>
											<li class="leaf"><a href="/MonitoreoBC-WEB/imagenesUsuarios.jsp">Visor de imagenes de usuario</a></li>
										</ul>
									</li>
									
								</ul>
							</div>
							
	<!-- 								FIN MENU VERDE -->
							<!-- /.content -->
						</div>
						<!--/.block -->
					</div>
				</div>
			</div>
			<!-- /.section-wrapper header second-->
	
		</div>
		<!-- /#header -->
	
	<div id="loading" style="text-align: center; display: none;">
		<img src="/MonitoreoBC-WEB/img/loading1.gif">
	</div>
	
	<div id="content"></div>

<script>
	var mouseX=0;
	var mouseY=0;
	var loading = $("#loading");
	var content = $("#content");
  

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
		if(navegador=="Microsoft Internet Explorer") {
			$("form").keypress(function(e) {
				if (e.keyCode == 13) {
				    return false;
				}
			});
		}
		else {
			$("form").keypress(function(e) {
				if (e.which == 13) {
					return false;
				}
			});
		}

		cargarPagina('index.jsp');
	});


	function cargarPagina(pagina) {
		showLoading();
		content.load(pagina, hideLoading);
	}
	   
	//show loading bar
	function showLoading(){
		loading.css({visibility:"visible"}).css({opacity:"1"}).css({display:"block"});
	}

	//hide loading bar
	function hideLoading(){
		//loading.fadeTo(1000, 0);
		loading.css({display:"none"});
	};
    
    
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


</script>
	
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
							<li><a class="min-educacion" href="http://www.mineducacion.gov.co/1621/w3-channel.html">MinEducaci&oacute;n</a></li>
							<li><a class="min-trabajo" href="http://www.mintrabajo.gov.co/">MinTrabajo</a></li>
							<li><a class="min-salud" href="http://www.minsalud.gov.co/Paginas/default.aspx">MinSalud</a></li>
						</ul>
					</div>
	<!-- 							<div class="menu-servicios menu-postscript"> -->
	<!-- 								<h3>Servicios de Cuidadan&iacute;a</h3> -->
	<!-- 								<ul> -->
	<!-- 									<li><a href="">Visitas Casa de Nariño</a></li> -->
	<!-- 									<li><a href="">Datos de contacto</a></li> -->
	<!-- 									<li><a href="">Escr&iacute;bale al Presidente</a></li> -->
	<!-- 									<li><a href="">PSQR</a></li> -->
	<!-- 									<li><a href="">Colombia Compra Eficiente</a></li> -->
	<!-- 									<li><a href="">Avisos Convocatoria P&eacute;blica</a></li> -->
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
						<li><a href="/MonitoreoBC-WEB/reg/indexLogOn.jsp" ><%=msj.getString("home.home")%></a></li>
						<li><a href="/MonitoreoBC-WEB/extra/mapaDelSitio.jsp"><%=msj.getString("home.mapa")%></a></li>
						<li><a href="/MonitoreoBC-WEB/extra/documentacion.jsp"><%=msj.getString("home.documentacion")%></a></li>
						<li><a href="/MonitoreoBC-WEB/extra/protocolos.jsp"><%=msj.getString("home.protocolos")%></a></li>
						<li><a href="/MonitoreoBC-WEB/extra/enlacesRel.jsp"><%=msj.getString("home.links")%></a></li>
					</ul>
				</div>
				<div class="copyriht">
					<p><%=msj.getString("pie.ideam") %></p>
					<p><%=msj.getString("pie.contacto") %></p>
					<p><%=msj.getString("pie.atencion") %></p>
				</div>
	
				<div id="session_timeout">Su sesión vence: <%=str_vencimiento_sesion %></div>
				
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
<form method="post" action="/MonitoreoBC-WEB/registrarAccesoServlet"
	name="formRegistra" id="formRegistra" target="deathFrame">
	<input type="hidden" name="hidUsername" id="hidUsername" /> <input
		type="hidden" name="hidPassword" id="hidPassword" />
</form>

<script type="text/javascript">
	postEdit();
</script>
</div>
</body>
</html>