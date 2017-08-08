<%@page import="co.gov.ideamredd.mbc.dao.CargaImagenesUsuario"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
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
<html class='no-js'>
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta charset="utf-8" />
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
 	
	ArrayList<String> imagenesUsuarios = CargaImagenesUsuario.cargaImagenesUsuarioAceptadas();

%>

<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="js/slippry.min.js"></script> 
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="js/logIn.js"></script>
<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/<% out.print(estilo); %>" media="all" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>
<script>


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
		inicializarNavegador();
	});
	 
   
</script>
</head>
<body class='sidebarlast front'  
	onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">

			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									

			<div id="preface" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix">

						<div class="slide">
						
							<ul id="slippry-demo">
							<%for(int x=0; x<imagenesUsuarios.size(); x++){%>
								<li><a href="#slide<%=x+1%>"><img src="/MonitoreoBC-WEB/imagenServlet?nomImagenParam=<%=imagenesUsuarios.get(x).split("::")[1]%>"></a>
									<span class="caption"><h3><%=imagenesUsuarios.get(x).split("::")[0]%></h3>
										<p><%=imagenesUsuarios.get(x).split("::")[2]%></p></span></li>
	
							<%}%>
							</ul>
							
						</div>

					</div>
				</div>
			</div>

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
											<div style="margin: 5px;padding: 5px;width:325px;">
											<%int cont=0;
											  for(cont=0;cont<eventos.size();cont++){
												  if(cont>=3){break;}
											  %>
											  <table style="border: 0px;">
											  <tr style="border: 0px;">
											  	  <td colspan="2" style="border: 0px;margin: 0px;padding: 0px;font-size: 14px;text-align: justify;font-weight: bold;">
											  	  	<%=eventos.get(cont).getNombre()%>
											  	  </td>
											  </tr>
											  <tr>
											  	<td rowspan="2" style="border: 0px;margin: 0px;padding: 0px;width:70px;height: 70px">
											  	  <div>
											  	  <% if(eventos.get(cont).getPathImagen().equals("nulo")) {%>
											  	  		<img style="width:70px;height: 70px" src="/MonitoreoBC-WEB/img/listaNoticias.jpg">
											  	  <%}else{ %>
											  	       <img style="width: 70px;height: 70px" src="/MonitoreoBC-WEB/imagenNoticiaServlet?nomImagenParam=<%=eventos.get(cont).getPathImagen()%>">
											  	  <%} %>
											  	  </div>
											  	</td>
											  	<td style="border: 0px;">
											  	  <div>
											  	  <p style="font-size: 13px;text-align: justify;">
											  	  <%
											  	  if(eventos.get(cont).getDescripcion().length()>80)
											  	  {
											  	  		out.print(eventos.get(cont).getDescripcion().substring(0,80));
											  	  }else{
											  		    out.print(eventos.get(cont).getDescripcion());
											  	  }
											  	  %>
											  	  ... <a href="/MonitoreoBC-WEB/pub/verNoticiaEvento.jsp?idNoticia=<%=eventos.get(cont).getConsecutivo()%>"> Ver mas.</a>
											  	  </p>
											  	  </div>
											  	</td>
											  </tr>
											  </table><hr>
											<%}%>
											<a href="/MonitoreoBC-WEB/pub/consultaEventos.jsp"">Mas Eventos...</a>
											</div>
										</div>

										<div id="block-noticias">
											<h2><%=msj.getString("home.noticias")%></h2>
											<div style="margin: 5px;padding: 5px;width:325px;">
											
											<%
											  for(cont=0;cont<noticias.size();cont++){
												  if(cont>=3){break;}
											  %>
											  <table style="border: 0px;">
											  <tr style="border: 0px;">
											  	  <td colspan="2" style="border: 0px;margin: 0px;padding: 0px;font-size: 14px;text-align: justify;font-weight: bold;">
											  	  	<%=noticias.get(cont).getNombre()%>
											  	  </td>
											  </tr>
											  <tr>
											  	<td rowspan="2" style="border: 0px;margin: 0px;padding: 0px;width:70px;height: 70px">
											  	  <div>
											  	  <% if(noticias.get(cont).getPathImagen().equals("nulo")) {%>
											  	  		<img style="width:70px;height: 70px" src="/MonitoreoBC-WEB/img/listaNoticias.jpg">
											  	  <%}else{ %>
											  	       <img style="width: 70px;height: 70px" src="/MonitoreoBC-WEB/imagenNoticiaServlet?nomImagenParam=<%=noticias.get(cont).getPathImagen()%>">
											  	  <%} %>
											  	  </div>
											  	</td>
											  	<td style="border: 0px;">
											  	  <div>
											  	  <p style="font-size: 13px;text-align: justify;">
											  	  <%
											  	  if(noticias.get(cont).getDescripcion().length()>80)
											  	  {
											  	  		out.print(noticias.get(cont).getDescripcion().substring(0,80));
											  	  }else{
											  		    out.print(noticias.get(cont).getDescripcion());
											  	  }
											  	  %>
											  	  ... <a href="/MonitoreoBC-WEB/pub/verNoticiaEvento.jsp?idNoticia=<%=noticias.get(cont).getConsecutivo()%>"> Ver mas.</a>
											  	  </p>
											  	  </div>
											  	</td>
											  </tr>
											  </table><hr>
											<%}%>
											<a href="/MonitoreoBC-WEB/pub/consultaNoticias.jsp">Mas Noticias...</a>
											</div>
										</div>
										
									</div>


									<div id="block-bosques" class=" blockx245 block-vermas block">
										<div class="content">
											<h2><%=msj.getString("home.bosque")%></h2>
											<img src="img/Bosque.jpg">
											<p>En el año 2013, la deforestaci&oacute;n estimada fue de 120.933 has, lo cual 
											equivale a 1.8 veces el &aacute;rea de una metr&oacute;poli colombiana como la ciudad de Cali.</p>
											<div class="ver-mas">
												<a href="/MonitoreoBC-WEB/pub/consultarReporteBosques.jsp">Ver m&aacute;s</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2><%=msj.getString("home.carbono")%></h2>
											<img src="img/Carbono.jpg">
											<p>En 2012 los bosques de Colombia almacenaban 23.001.314.006 t CO2 
											en la biomasa a&eacute;rea, que equivale al CO2 que se 
											necesita para llenar 112.000.000 de edificios 
											como la Torre Colpatria la cual tiene 63 pisos.</p>
											<div class="ver-mas">
												<a href="/MonitoreoBC-WEB/pub/consultarReporteCarbono.jsp">Ver m&aacute;s</a>
											</div>
										</div>
									</div>

																	</div>
							</div>

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
												<a href="/MonitoreoBC-WEB/pub/alertasDeforestacion.jsp">Ver m&aacute;s</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2>Propuesta de nivel de referencia de las emisiones forestales por deforestaci&oacute;n en la Amazon&iacute;a de Colombia</h2>
											<img src="img/Visor.png">
											<p>Colombia presenta la primera versi&oacute;n del nivel de referencia de emisiones forestales (NREF), como parte de la  adopci&oacute;n 
											de las medidas mencionadas en el par&aacute;grafo 70 de la decisi&oacute;n 1/CP.16 (Convenci&oacute;n Marco de las Naciones Unidas Contra el Cambio 
											Clim&aacute;tico - CMNUCC, 2011), para que sea incluida en el proceso de evaluaci&oacute;n t&eacute;cnica en el contexto de pagos basados en resultados 
											de la reducci&oacute;n de emisiones por deforestaci&oacute;n y degradaci&oacute;n y la conservaci&oacute;n, manejo forestal sostenible y mejora de los contenidos 
											de carbono en los pa&iacute;ses en desarrollo (REDD +). Colombia resalta que la presentaci&oacute;n del NREF y sus anexos t&eacute;cnicos,  es voluntaria y 
											tiene como prop&oacute;sito exclusivo obtener pagos por las acciones de REDD+.</p>
											<div class="ver-mas">
												<a href="/MonitoreoBC-WEB/extra/propReferencia.jsp">Ver m&aacute;s</a>
											</div>
										</div>
									</div>

									

								</div>
									</div>
							<!-- /.sidebar-wrapper-->


						</div>
					</div>
				</div>

			</div>

			<%=UI.getFooter(msj).replace("../img", "img")%>									

		</div>
	</form>
	
</body>
</html>