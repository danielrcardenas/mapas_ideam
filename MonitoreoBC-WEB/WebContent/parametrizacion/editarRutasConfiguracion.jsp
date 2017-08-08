<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.web.parametrizacion.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>  
<%@page import="co.gov.ideamredd.parametrizacion.entities.Rutas"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
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

<%if(usuario != null)
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 99))
	{
%>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Parámetros del Sistema</title>
<link rel="stylesheet"	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />

<script src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/general.js"></script>


<script>
function almacenarParametro(p) {
	$("#div_mensaje_parametro_"+p).html("...");
	$.ajax({
		type: 'POST',
		url: "almacenarParametro.jsp",
		data: {
			'PRTR_CONSECUTIVO':p,
			'PRTR_RUTA':document.getElementById("parametro_"+p).value
		}, 
		success: function(result){
        $("#div_mensaje_parametro_"+p).html(result.trim());
        $("#div_mensaje_parametro_"+p).show();
    }});
}														
</script>

<script type="text/javascript">
	$(document).ready(function() {
		inicializarNavegador();
	});
</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);" onmouseover="popUpUsuariosAux()">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
	</form>
	<div id="page">
	   <%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
		<div id="main" class="wrapper">
			<div id="main-inner" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix fondoformulario">
						
						<h2 class="titulo_naranja"><%=msj.getString("administracion_de_parametros") %></h2>

							<div id="content">
								<div class="content-inner">
									<!-- precontent -->
									<form id="editarParametros" name="editarParametros" action="/MonitoreoBC-WEB/editarParametrosServlet" method="post">
										<div class="block-gray block"   style="overflow-x:auto;">
											<h2><%=msj.getString("listado_de_parametros") %></h2>
											
											
											
												 
												    <div class="parametros">
														<div id="menuParametros">
														    <br>
														    <ul>
														    
														        <li class="has-sub"><a href="#URL"> <%=msj.getString("parametro.url")%></a>
															    <ul >
															        <%ArrayList<Rutas> rutasB4 = CargaDatosInicial.getArrayRutasApollo_Descrp("[URL]");
																    int numRutas4=0;
																	for (int i = 0; i < rutasB4.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB4.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas4++;
																	}
																	%>
																</ul>
																</li>
																
															    
															    <li class="has-sub"><a href="#Ruta"> <%=msj.getString("parametro.Ruta")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasB9 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Ruta]");
																    int numRutas9=0;
																	for (int i = 0; i < rutasB9.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB9.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas9++;
																	}
																	%>
																</ul>
																</li>
																
																
																<li class="has-sub"><a href="#Correo"> <%=msj.getString("contacto.correo")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasB12 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Correo]");
																    int numRutas12=0;
																	for (int i = 0; i < rutasB12.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB12.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas12++;
																	}
																	%>
																</ul>
															    </li>
															    
															    
																<li class="has-sub"><a href="#Contenidos"> <%=msj.getString("parametro.contenido")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasB3 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Contenido]");
																    int numRutas3=0;
																	for (int i = 0; i < rutasB3.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB3.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas3++;
																	}
																	%>
																</ul>
																</li>
																
																<li class="has-sub"><a href="#ContenidosER"> <%=msj.getString("enlacesrel.1")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasB31 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Contenido ER]");
																    int numRutas31=0;
																	for (int i = 0; i < rutasB31.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB31.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas31++;
																	}
																	%>
																</ul>
																</li>
																
																<li class="has-sub"><a href="#ContenidosINFO"> <%=msj.getString("alertasdeforestacion.44")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasB32 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Contenido INFO]");
																    int numRutas32=0;
																	for (int i = 0; i < rutasB32.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB32.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas32++;
																	}
																	%>
																</ul>
																</li>
															
															    <li class="has-sub"><a href="#Geo"> <%=msj.getString("parametro.Geoproceso")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasB01 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Geop]");
																    int numRutas01=0;
																	for (int i = 0; i < rutasB01.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasB01.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutas01++;
																	}
																	%>
																</ul>
																</li>
																
																<li class="has-sub"><a href="#ldap"> <%=msj.getString("parametro.ldap")%> </a>
															    <ul >
															        <%ArrayList<Rutas> rutasc40 = CargaDatosInicial.getArrayRutasApollo_Descrp("[LDAP]");
																    int numRutasC40=0;
																	for (int i = 0; i < rutasc40.size(); i++) {
																		Rutas rutaApollo = (Rutas) rutasc40.get(i);
																		
																		%>
																		<li ><a href="#lista_<%=rutaApollo.getConsecutivo()%>" ><%=rutaApollo.getNombre()%></a></li>
																															
																		<% numRutasC40++;
																	}
																	%>
																</ul>
																</li>
															</ul>
														</div>
													   </div>
												 
												 
												 
											 
											
												<div class="form-datos-parametros form-columnx1" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("parametrizacion.advertencia")%></label>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"></label>
														</div>
														
													
													   <div >
													   <h2 class="titulo_naranja" id="Contenidos"><%=msj.getString("parametro.contenido") %></h2>
												             <%ArrayList<Rutas> rutasB = CargaDatosInicial.getArrayRutasApollo_Descrp("[Contenido]");
														    int numRutas=0;
															for (int i = 0; i < rutasB.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas++;
															}
															%>
														
														</div>
														
														<div >
														<h2 class="titulo_naranja" id="ContenidosER"><%=msj.getString("enlacesrel.1") %></h2>
												             <%ArrayList<Rutas> rutasB33 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Contenido ER]");
														    int numRutas33=0;
															for (int i = 0; i < rutasB33.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB33.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas33++;
															}
															%>
														
														</div>
														
														<div >
														<h2 class="titulo_naranja" id="ContenidosINFO"><%=msj.getString("alertasdeforestacion.44") %></h2>
												             <%ArrayList<Rutas> rutasB34 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Contenido INFO]");
														    int numRutas34=0;
															for (int i = 0; i < rutasB34.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB34.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas34++;
															}
															%>
														
														</div>
														
														<div >
														<h2 class="titulo_naranja" id="URL" ><%=msj.getString("parametro.url") %></h2>
												        	<%ArrayList<Rutas> rutasB6 = CargaDatosInicial.getArrayRutasApollo_Descrp("[URL]");
														    int numRutas6=0;
															for (int i = 0; i < rutasB6.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB6.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas6++;
															}
															%>
															
														
														</div>
														
														<div  >
														<h2 class="titulo_naranja" id="Ruta" > <%=msj.getString("parametro.Ruta") %></h2>
												            <%ArrayList<Rutas> rutasB11 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Ruta]");
														    int numRutas11=0;
															for (int i = 0; i < rutasB11.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB11.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas11++;
															}
															%>
																											
														</div>	
														
														
														
														
														<div >
														<h2 class="titulo_naranja" id="Geo" ><%=msj.getString("parametro.Geoproceso") %></h2>
												        	<%ArrayList<Rutas> rutasB7 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Geop]");
														    int numRutas7=0;
															for (int i = 0; i < rutasB7.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB7.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas7++;
															}
															%>
															
														
														</div>
														
														
														<div>
														<h2 class="titulo_naranja" id="Correo" ><%=msj.getString("contacto.correo") %></h2>
												        	<%ArrayList<Rutas> rutasB13 = CargaDatosInicial.getArrayRutasApollo_Descrp("[Correo]");
														    int numRutas13=0;
															for (int i = 0; i < rutasB13.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasB13.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutas13++;
															}
															%>
																											
														</div>
														
														
														<div>
														<h2 class="titulo_naranja" id="ldap" ><%=msj.getString("parametro.ldap") %></h2>
												        	<%ArrayList<Rutas> rutasd13 = CargaDatosInicial.getArrayRutasApollo_Descrp("[LDAP]");
														    int numRutasd13=0;
															for (int i = 0; i < rutasd13.size(); i++) {
																Rutas rutaApollo = (Rutas) rutasd13.get(i);
																out.print("\n<div id=lista_"+rutaApollo.getConsecutivo() +" class='parametro'>");
																out.print("<b>"+rutaApollo.getNombre()+"</b>:<br>");
																out.print(rutaApollo.getDescripcion()+":<br>");
																%>
																<textarea type="text" rows="3" class="form-control" style="height: 50px !important !important;" title="<%=rutaApollo.getDescripcion()%>" id="parametro_<%=rutaApollo.getConsecutivo()%>" name="parametro_<%=rutaApollo.getConsecutivo()%>"><%=rutaApollo.getRuta()%></textarea>
																<br>
																<input class="btn btn-default" 
																id="b_<%=rutaApollo.getConsecutivo()%>" 
																name="b_parametro_<%=rutaApollo.getConsecutivo()%>" 
																type="button" 
																onclick="javascript:almacenarParametro(<%=rutaApollo.getConsecutivo()%>);" 
																value="<%=msj.getString("parametrizacion.almacenar")%>" >
																<div id="div_mensaje_parametro_<%=rutaApollo.getConsecutivo()%>" class="div_mensajes"></div>
																</div> 														
																<% numRutasd13++;
															}
															%>
																											
														</div>
														
														
														
														<div class="form-actions">
															<input type="hidden" value="<%=numRutas%>" name="numRutas" id="numRutas">
														    <input id="btnCancelar" name="btnCancelar" type="button" value="<%=msj.getString("parametrizacion.cancelar")%>" onclick="location.href='/MonitoreoBC-WEB';">
				
														</div>
														
													</div>
														
						</div>
					</form>
												
								
						
						
						
					
					</div>
						<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
					</div>
					</div>	
						
					</div>		
					<%=UI.getFooter(msj) %>									
				</div>
			</div>
		</div>
		
		
	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										
	
</body>
</html> 


<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>