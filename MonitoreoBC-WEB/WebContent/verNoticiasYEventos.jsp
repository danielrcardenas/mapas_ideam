<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
ArrayList<Noticias> events = CargaNoticiasYEventos.cargaEventos();
ArrayList<Noticias> eventos = CargaDatosInicialHome.getEventosHome(); 

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
 	
    
    if(request.getUserPrincipal() !=null)
	{
		usuario = UtilWeb.consultarUsuarioPorLogin(request.getUserPrincipal().getName());
		usuario.setRolNombre(UtilWeb.consultarRolesUsuarioPorLogin(request.getUserPrincipal().getName()));
	}
    
%>
<title>Detalle Noticia</title>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="js/logIn.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
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
	
</script>
</head>
<body class='sidebar-first front'>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()).replace("../img", "img") %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
						

							<div id="content">
								<div class="content-inner">

									<div class="content">
										<div id="eventos">
											<div>
												<h3><%=msj.getString("home.eventos")%></h3>
											</div>
											<div class="form-datos-parcela form-columnx2">
												<%
													for (int j = 0; j < events.size(); j++) {
														Noticias e = events.get(j);
												%>
												<div class="form-group">
													<div>
														<label><%=msj.getString("crea.noticias.nombre")%></label>
													</div>
													<div>
														<input type="text" readonly="readonly"
															class="form-control" value="<%=e.getNombre()%>" />
													</div>
												</div>
												<div class="form-group">
													<div>
														<label><%=msj.getString("crea.noticias.fecha")%></label>
													</div>
													<div>
														<input type="text" readonly="readonly"
															class="form-control" value="<%=e.getFecha()%>" />
													</div>
												</div>
												<div class="form-group">
													<div>
														<label><%=msj.getString("crea.noticias.hora")%></label>
													</div>
													<div>
														<input type="text" readonly="readonly"
															class="form-control" value="<%=e.getHora()%>" />
													</div>
												</div>
												<div class="form-group">
													<div>
														<label><%=msj.getString("crea.noticias.lugar")%></label>
													</div>
													<div>
														<input type="text" readonly="readonly"
															class="form-control" value="<%=e.getLugar()%>" />
													</div>
												</div>
												<div class="form-actions">
													<a href="/MonitoreoBC-WEB/detallarEvento.jsp?i=<%=j%>"><%=msj.getString("home.verMas")%></a>
												</div>
												<%
													}
												%>
											</div>
										</div>
										<div id="noticias">
											<div>
												<label><%=msj.getString("home.noticias")%></label>
											</div>
											<div>
												<%
													for (int i = 0; i < noticias.size(); i++) {
														Noticias n = noticias.get(i);
												%>
												<div class="form-group">
													<img src="<%=n.getPathImagen()%>">
												</div>
												<div class="form-group"></div>
												<div class="form-group">
													<div>
														<label><%=msj.getString("crea.noticias.nombre")%></label>
													</div>
													<div>
														<input type="text" readonly="readonly"
															class="form-control" value="<%=n.getNombre()%>" />
													</div>
												</div>
												<div class="form-group">
													<div>
														<label><%=msj.getString("crea.noticias.fecha")%></label>
													</div>
													<div>
														<input type="text" readonly="readonly"
															class="form-control" value="<%=n.getFecha()%>" />
													</div>
												</div>
												<div class="form-actions">
													<a href="/MonitoreoBC-WEB/detallarNoticia.jsp?i=<%=i%>"><%=msj.getString("home.verMas")%></a>
												</div>
												<%
													}
												%>
											</div>
										</div>
									</div>
								</div>
							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n).replace("../img", "img") %>									
							</div>
						</div>
					</div>
					<!-- /.section-->
				</div>
			</div>

			<%=UI.getFooter(msj).replace("../img", "img")%>									

		</div>
	</form>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										

</body>
</html>