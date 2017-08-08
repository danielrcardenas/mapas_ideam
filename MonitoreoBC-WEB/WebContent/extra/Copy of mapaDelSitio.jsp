<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.util.Util"%>
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
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
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
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>


<script type="text/javascript" src="../custom/manejo-listas.js"></script>

<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

<script>


    $(document).ready(function() {
		inicializarNavegador();
	});
    


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
	function descargaDocumento(nombre,tipo){
		document.getElementById("hidNomDoc").value=nombre;
		document.getElementById("hidTipoDocRef").value=tipo;
		document.getElementById("formDescargaDocs").submit();
	}

</script>


  
</head>
<body class='sidebarlast front'   onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										
	
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">

			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja">Mapa del Sitio</h2>
							
							<div id="content">
								<div class="content-inner">

								

									<div >
									
										<div class="tab">
										  <%if(usuario == null){ %> 
											<button class="tablinks" onclick="openSite(event, 'tab_default_1')">Usuarios</button>
										  <%}%>
										  <%if(usuario != null)
												if(ControlPermisos.tienePermiso(diccionarioPermisos, 141))
												{
													%> <button class="tablinks" onclick="openSite(event, 'tab_default_2')"><%=msj.getString("home.noticiasEventos")%></button>
									  	  <%} %>
										  <button class="tablinks" onclick="openSite(event, 'tab_default_3')"><%=msj.getString("home.cifras")%></button>
										  <button class="tablinks" onclick="openSite(event, 'tab_default_4')"><%=msj.getString("home.bosques.alertas")%></button>
										  <%if(usuario != null){ %>
										  	<button class="tablinks" onclick="openSite(event, 'tab_default_5')"><%=msj.getString("menu_usuario")%></button>
										  <%} %>	
										  <button class="tablinks" onclick="openSite(event, 'tab_default_6')"><%=msj.getString("home.visor")%></button>
										  <button class="tablinks" onclick="openSite(event, 'tab_default_7')"><%=msj.getString("home.links")%></button>
										  <button class="tablinks" onclick="openSite(event, 'tab_default_8')"><%=msj.getString("socios")%></button>
										</div>
									
									
											<div class="tabcontent" id="tab_default_1">
													<ul>
														<li ><a>Usuarios</a>
															<ul >
																<li ><a href="/MonitoreoBC-WEB/pub/registroUsuario.jsp"><%=msj.getString("home.popAyuda.registrarse")%></a></li>
															</ul>
															<ul>
																<li ><a href="/MonitoreoBC-WEB/pub/recordarClave.jsp"><%=msj.getString("home.popAyuda.recordar")%></a></li>
																
															</ul>
														</li>
													</ul>	
												</div>
						                        <div class="tabcontent" id="tab_default_2">
							                        <li ><a><%=msj.getString("home.noticiasEventos")%></a>
			 										    <ul > 
													        <li ><a href="/MonitoreoBC-WEB/pub/consultaNoticias.jsp"><%=msj.getString("home.noticias")%></a></li>
			 											</ul>
														<ul>    
			 											    <li ><a href="/MonitoreoBC-WEB/pub/consultaEventos.jsp"><%=msj.getString("home.eventos")%></a></li>
			 											</ul>
														<ul>    
			 											    <%if(usuario != null)
																if(ControlPermisos.tienePermiso(diccionarioPermisos, 141))
																{
															%>
			 											    	<li ><a href="/MonitoreoBC-WEB/noticias/crearNoticiasEventos.jsp"><%=msj.getString("home.crear_not_evt")%></a></li>
														    <%} %>
														    
												        </ul> 
													</li>
						                        </div>
						                        <div class="tabcontent" id="tab_default_3">
							                        <li ><a><%=msj.getString("home.cifras")%></a>
														<ul >
														    <li ><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=1'><%=msj.getString("home.bosques.bosquenobosque") %></a></li>
					            						</ul>
														<ul>	
					            							<li ><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=3'><%=msj.getString("home.bosques.cambiocobertura")%></a></li>
					            						</ul>
														<ul>	
					            							<li ><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=5'><%=msj.getString("home.bosques.deforestacion")%></a></li>
					                                    </ul>
														<ul>   
					                                        <%if(ControlPermisos.tienePermiso(diccionarioPermisos, 143)){%>
																	<li ><a href="/MonitoreoBC-WEB/admin/reportes.jsp"><%=msj.getString("Administrar_Reportes")%></a></li><%} %>
														</ul>
													</li> 
						                        </div>
						                        <div class="tabcontent"  id="tab_default_4">
							                        <li ><a><%=msj.getString("home.bosques.alertas")%></a>
														<ul >
														    <li ><a href="/MonitoreoBC-WEB/pub/elBosqueEnCifras.jsp"><%=msj.getString("home.bosqueCifras")%></a></li>
														</ul>
														<ul>	
															<li ><a href="/MonitoreoBC-WEB/pub/alertasDeforestacion.jsp"><%=msj.getString("home.bosques.alertas")%></a></li>
														</ul>
															
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 162)){%><ul><li ><a href="/MonitoreoBC-WEB/admin/cargarDatosAlertasTempranas.jsp">Cargar Datos Alertas Tempranas</a></li></ul>
																<%} %>
														
													</li>
						                        </div>
						                        <div class="tabcontent"  id="tab_default_5">
							                        <li ><a><%=msj.getString("menu_usuario")%></a>
														<ul >
															<li ><a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp"><%=msj.getString("datos_personales")%></a></li>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 85)){%>
																<li ><a href="/MonitoreoBC-WEB/admin/consultaLicenciasUso.jsp"><%=msj.getString("consultar_licencias_de_uso")%></a></li>
															<%} %>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 80)){%>
																<li ><a href="/MonitoreoBC-WEB/admin/registraRol.jsp"><%=msj.getString("registrar_rol")%></a></li>
															<%} %>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 79)){%>
																<li ><a href="/MonitoreoBC-WEB/admin/consultaRoles.jsp"><%=msj.getString("consultar_roles")%></a></li>
															<%} %>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 71)){%>
																<li ><a href="/MonitoreoBC-WEB/admin/consultarUsuarios.jsp"><%= msj.getString("Consultar_Usuarios") %></a></li>
															<%} %>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 75)){%>
																<li ><a href="/MonitoreoBC-WEB/descarga/ConsultarDescargaImagenes.jsp"><%=msj.getString("Descargas_de_Usuarios")%></a></li>
															<%} %>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 99)){%>
																<li ><a href="/MonitoreoBC-WEB/parametrizacion/editarRutasConfiguracion.jsp"><%=msj.getString("Parametros_del_sistema")%></a></li>
															<%} %>
														</ul>
													    <ul> 	
															<%if(ControlPermisos.tienePermiso(diccionarioPermisos, 145)){%>
																<li ><a href="/MonitoreoBC-WEB/borrarArchivosServlet"><%= msj.getString("logOn.admin.borrarTemporales") %></a></li>
															<%} %>
														</ul>
													</li>
											
						                        </div>
						                        <div class="tabcontent"  id="tab_default_6">
							                        <li ><a><%=msj.getString("home.visor")%></a>
														<ul >
															<li ><a href="http://seinekan.ideam.gov.co/SMBYC/ApolloPro.aspx" target='_blank'><%=msj.getString("etiqueta_link_visor_apollo")%></a></li>
														</ul>
														<ul> 	
															<li ><a href='http://visor.ideam.gov.co:8530/geovisor/#!/profiles/5/no' target='_blank'><%=msj.getString("etiqueta_link_visor_ideam")%></a></li>
			
														</ul>
													</li>
						                        </div>
						                        <div class="tabcontent"  id="tab_default_7">
							                        <li ><a><%=msj.getString("home.links")%></a>
														<ul>
														    <li ><a href="/MonitoreoBC-WEB/extra/documentacion.jsp" ><%=msj.getString("pagina.Documentacion")%></a></li>
														</ul>
														<ul>	
															<li ><a href="/MonitoreoBC-WEB/extra/presentacion.jsp"><%=msj.getString("pagina.Presentacion")%></a></li>
														</ul>
														<ul>	
															<li ><a href="/MonitoreoBC-WEB/extra/enlacesRel.jsp" ><%=msj.getString("pagina.SitiosInteres")%></a></li>
														</ul>
														<ul>	
															<li ><a href="/MonitoreoBC-WEB/extra/protocolos.jsp" ><%=msj.getString("pagina.Protocolos")%></a></li>
														</ul>
														<ul>	
															<li ><a href="/MonitoreoBC-WEB/extra/glosario.jsp" ><%=msj.getString("pagina.Glosario")%></a></li>
															
														</ul>
													</li>
										
						                        </div>
						                        <div class="div_alertas_tempranas"  id="tab_default_8">
						                        	<li ><a href="/MonitoreoBC-WEB">Inicio</a>
						                        	<li ><a href='/MonitoreoBC-WEB/extra/socios.jsp'><%= msj.getString("socios") %></a></li>
						                        </div>
						                    
									
									     
									    </div>
									
										<div class="form-actions">
										<input type="button"
												value=" <%=msj.getString("recordarClave.Atras")%> "
												class="btn btn-default"
												onclick="javascript:history.back(1);">
										</div>
									</div>
								</div>
							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
							</div>

						</div>
					</div>
				</div>

			<%=UI.getFooter(msj) %>									

		</div>
	</form>
<script>
function openSite(evt, cityName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}
</script>
</body>
</html>

