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
<%@page import="co.gov.ideamredd.mbc.conexion.Parametro"%>
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
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 141))
	{
%>
<title>Sistema de Monitoreo de Biomasa y Carbono</title>


<script type="text/javascript" src="../custom/manejo-listas.js"></script>

<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script type="text/javascript" src="../custom/datum-validation.js"></script>

<script>


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

  	
	function validarDatosForm(){
		var resultado=true;
		
		var nombreNoticia=document.getElementById("nombreNoticia").value;
		var tipoNoticia=document.getElementById("tipoNoticia").value;
		var descripcion=document.getElementById("descripcion").value;
		descripcion=descripcion.replace(/\n/g, "<br />")
		.replace(/á/g, "&aacute;").replace(/é/g, "&eacute;")
		.replace(/í/g, "&iacute;").replace(/ó/g, "&oacute;")
		.replace(/ú/g, "&uacute;").replace(/ñ/g, "&ntilde;");
		
		nombreNoticia=nombreNoticia.replace(/á/g, "&aacute;")
		.replace(/é/g, "&eacute;").replace(/í/g, "&iacute;")
		.replace(/ó/g, "&oacute;").replace(/ú/g, "&uacute;")
		.replace(/ñ/g, "&ntilde;");
		
		
		var mensaje="";
		
		if(nombreNoticia == ""){resultado=false;mensaje=mensaje+"-Nombre de la Noticia\n";}
		if(tipoNoticia == "-1"){resultado=false;mensaje=mensaje+"-Tipo de Noticia\n";}
		if(descripcion == ""){resultado=false; mensaje=mensaje+"-Descripcion\n";}
		
		if (!resultado) {
			alert("Los campos marcados con * son obligatorios.\nComplete los siguientes campos:\n"+mensaje.substring(0,mensaje.length-1));
		}else{
			document.getElementById("descripcion").value=descripcion;
			document.getElementById("nombreNoticia").value=nombreNoticia;
		}
		
		return resultado;
	}
	
	function seleccionarTab(idTab,boton){
		document.getElementById('divCrear').style.display='none';
		document.getElementById('divCrear').style.border='hidden';
		document.getElementById('divModificar').style.display='none';
		document.getElementById('divModificar').style.border='hidden';
		document.getElementById('divEliminar').style.display='none';
		document.getElementById('divEliminar').style.border='hidden';
				
		document.getElementById(idTab).style.display='block';
		//boton.style.border='solid';
	}

	var JSONNotEvt;
	function ModificarNotEv(idTab,id_noticia){
		<%
		if(!ControlPermisos.tienePermiso(diccionarioPermisos, 94))
		{
		%>
			alert('No tiene privilegios para eliminar noticias o eventos. Por favor comuníquese con el administrador del sistema.');
			return;
		<%
		} 
		%>

		var div_estado = document.getElementById('divModificar');
		var formData = {'id_noticia':id_noticia}; 
		$.ajax({
			type: 'POST',
			data: formData,
			url: "../admin/ajax_consultarNotEvt.jsp",
			success: function(data, textStatus, jqXHR){
				div_estado.innerHTML = "";
				JSONNotEvt = JSON.parse(data);
	            modificarNoticias();
	        },
	    	error: function (jqXHR, result, errorThrown) {
	    		div_estado.innerHTML = jqXHR.responseText;
	        	consultando = false;
	    	}	
	    });
				
	}

    function modificarNoticias(){
    	
    	var etiqueta_nombre = '<%=msj.getString("crea.noticias.nombre")%>';
    	var etiqueta_tipo = '<%=msj.getString("crea.noticias.seleccionar")%>';
    	var etiqueta_descripcion = '<%=msj.getString("crea.noticias.descripcion")%>';
    	var etiqueta_imagen = '<%=msj.getString("crea.noticias.imagen")%>';
    	var etiqueta_guardar = '<%=msj.getString("crea.noticia.crear") %>';
    	var tituloForma = '<%=msj.getString("crearnoticiaseventos.40") %>';
    	var imagencargada = '<%=msj.getString("imagenusuario.53") %>';
    	var idusuario = '<%=usuario.getIdentificacion()%>';
    	var html = "";
    	var carpetaImagenes = '<%=Parametro.getParametro("ruta.imagenes.noticias")%>';
		

    	html += "<form method='POST' enctype='multipart/form-data' action='/MonitoreoBC-WEB/ActualizarNoticiasServlet'"; 
    	html += "	id='formActNotEvt' name='formActNotEvt' onsubmit='return validarDatosNotEvt()'>";
    	html += "<div class='block-gray block'>";
    	html += "<h2>"+tituloForma+"</h2>";
    	html += "<br>";
    	html += "<div style='margin-left: 20px'>";
    	html += "<label for='nombreNotEvt' >"+etiqueta_nombre+"<span class='obligatorio'>*</span></label>";
    	html += "<input type='text' name='nombreNotEvt' id='nombreNotEvt' style='width: 280px' value='"+JSONNotEvt.nombre+"'>";
    	html += "<br><br>";
    	html += "<label for='tipoNotEvt' >"+etiqueta_tipo+"<span class='obligatorio'>*</span></label>";
    	html += "<div class='select-wrapper' style='width: 300px'>";
    	html += "<select class='form-control' name='tipoNotEvt' id='tipoNotEvt'>";
	    	if(JSONNotEvt.tipo==1){
	        	html += "	<option value='-1'>Seleccionar</option>";
	    	    html += "	<option value='1' selected>Noticia</option>";
	    	    html += "	<option value='2'>Evento</option>";
	    	}else{
	    		html += "	<option value='-1'>Seleccionar</option>";
	        	html += "	<option value='1'>Noticia</option>";
	        	html += "	<option value='2' selected>Evento</option>";
	       	}  
    	html += "</select>";
    	html += "</div>";
    	html += "<br>";   	
    	html += "<label for='descpNotEvt'>"+etiqueta_descripcion+"<span class='obligatorio'>*</span></label>";
    	html += "<textarea style='resize: none; font-size: 15px; width: 500px; height: 100px;' name='descpNotEvt' id='descpNotEvt'>"+JSONNotEvt.descripcion+"</textarea>";
    	html += "<br><br>";
    	html += "<p >"+imagencargada+"</p>";
    	html += "<img style='width:100px;height: 100px' src='/MonitoreoBC-WEB/imagenNoticiaServlet?nomImagenParam="+JSONNotEvt.imagen+"'>"; 
    	html += "<input type='hidden' name='imagenvieja' id='imagenvieja' value='"+JSONNotEvt.imagen+"'>";	 
    	html += "<label for='imgNotEvt'>"+etiqueta_imagen+" (JPG, PNG):</label>";
    	html += "<input type='file' name='imgNotEvt' id='imgNotEvt' class='btn btn-default' >";  
    	html += "<input type='hidden' name='idnotevt' id='idnotevt' value='"+JSONNotEvt.consecutivo+"'>";
    	html += "<input type='hidden' id='idUsuarioNotEvt' name='idUsuarioNotEvt' value="+idusuario+">";	
    	html += "<br><br>";
    	html += "</div>";	
    	html += "</div>";
    	html += "<div class='form-actions'>";
		html += "<input type='submit' value='"+etiqueta_guardar+"'>";
    	html += "</div>";	
    	html += "</form>";

    	var div_estado = document.getElementById('divModificar');
    	div_estado.innerHTML = html;

    	document.getElementById('divCrear').style.display='none';
		document.getElementById('divCrear').style.border='hidden';
		document.getElementById('divModificar').style.display='none';
		document.getElementById('divModificar').style.border='hidden';
		document.getElementById('divEliminar').style.display='none';
		document.getElementById('divEliminar').style.border='hidden';
				
		document.getElementById('divModificar').style.display='block';
    }    

    function validarDatosNotEvt(){
		var resultado=true;
		
		var nombreNoticia=document.getElementById("nombreNotEvt").value;
		var tipoNoticia=document.getElementById("tipoNotEvt").value;
		var descripcion=document.getElementById("descpNotEvt").value;
		descripcion=descripcion.replace(/\n/g, "<br />")
		.replace(/á/g, "&aacute;").replace(/é/g, "&eacute;")
		.replace(/í/g, "&iacute;").replace(/ó/g, "&oacute;")
		.replace(/ú/g, "&uacute;").replace(/ñ/g, "&ntilde;");
		
		nombreNoticia=nombreNoticia.replace(/á/g, "&aacute;")
		.replace(/é/g, "&eacute;").replace(/í/g, "&iacute;")
		.replace(/ó/g, "&oacute;").replace(/ú/g, "&uacute;")
		.replace(/ñ/g, "&ntilde;");
		
		
		var mensaje="";
		
		if(nombreNoticia == ""){resultado=false;mensaje=mensaje+"-Nombre de la Noticia\n";}
		if(tipoNoticia == "-1"){resultado=false;mensaje=mensaje+"-Tipo de Noticia\n";}
		if(descripcion == ""){resultado=false; mensaje=mensaje+"-Descripcion\n";}
		
		if (!resultado) {
			alert("Los campos marcados con * son obligatorios.\nComplete los siguientes campos:\n"+mensaje.substring(0,mensaje.length-1));
		}else{
			document.getElementById("descpNotEvt").value=descripcion;
			document.getElementById("nombreNotEvt").value=nombreNoticia;
		}
		
		return resultado;
	}
    

	
	function eliminarNotEv(id_consecutivo){
		<%
		if(!ControlPermisos.tienePermiso(diccionarioPermisos, 94))
		{
		%>
			alert('No tiene privilegios para eliminar noticias o eventos. Por favor comuníquese con el administrador del sistema.');
			return;
		<%
		} 
		%>

		var r = confirm("¿Desea eliminar el registro con consecutivo: "+id_consecutivo+"?");
		if (r == true) {
			
			var respuesta = "";
			
			eliminando = true;
			
			var formData = {'id_consecutivo':id_consecutivo}; 
			$.ajax({
				type: 'POST',
				data: formData,
				url: "../admin/ajax_eliminar_noticia.jsp",
				success: function(data, textStatus, jqXHR){
					respuesta = data.replace(/[\n\r]/g, '');
					//if (data.length > 0) respuesta = data.substr(data.length - 1);
					if (respuesta == "1") {
			        	alert("Noticia/Evento fue eliminado con éxito");
					}
					else {
						alert("Noticia/Evento NO fue eliminado... consecutivo: " + id_consecutivo);
					}
		        	eliminando = false;
		        },
		    	error: function (jqXHR, result, errorThrown) {
		    		alert("No fue posible eliminar la noticia/evento: " + jqXHR.responseText);
		        	eliminando = false;
		        }
		    });
		}
		// refreshes the table 
         location.reload();
 		
	}


</script>
</head>
<body class='sidebarlast front'   onMouseMove="" onmouseover="">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja"><%=msj.getString("crearnoticiaseventos.1") %></h2>

							<div id="content">
								<div class="content-inner">

									   <div class="form-actions">
											<input id="btnCrear" name="btnCrear" type="button" value="<%=msj.getString("crearnoticiaseventos.35")%>" onclick="seleccionarTab('divCrear',this);">
										    <input id="btnModif" name="btnModif" type="button" value="<%=msj.getString("crearnoticiaseventos.37")%>" onclick="seleccionarTab('divEliminar',this);">
										    <input id="btnCancelar" name="btnCancelar" type="button" value="<%=msj.getString("parametrizacion.cancelar")%>" onclick="location.href='/MonitoreoBC-WEB';">

										</div>
									
									    	
										<div id="divEliminar"  class="div_noticias_eventos">
										 <div class="block-gray block" style="overflow-x:auto;">		
											<h2><%=msj.getString("crearnoticiaseventos.39") %></h2>
											<form>
													
												<table id="tablaNotEv" class="tableNotEv">
												    <thead id="theadNotEv">
													    <tr>
													        <th scope="col" id="tiponot">Tipo</th>
													        <th scope="col" id="fechanot">Fecha</th>
													        <th scope="col" id="titulonot" >Titulo</th>
													        <th scope="col" id="modnot">Modificar</th>
													        <th scope="col" id="elemnot">Eliminar</th>
													    </tr> 
												    </thead>
												    <tfoot id="tfootNotEv"> 
												        <tr> 
												           <td colspan="5">Información sobre noticias y eventos en SMBYC</td>
												        </tr> 
												    </tfoot> 
												    <tbody> 
												    	<%ArrayList<Noticias> noticias1 = CargaNoticiasYEventos.cargaNoticias();
														    int numNoticias=0;
															for (int i = 0; i < noticias1.size(); i++) {
																Noticias rutaNot = (Noticias) noticias1.get(i);
																%>
																<tr >
																<td headers="tiponot"><%=msj.getString("crearnoticiaseventos.32")%></td>
														        <td headers="fechanot"><%=rutaNot.getFecha()%></td>
														        <td headers="titulonot"><%=rutaNot.getNombre().toLowerCase()%></td>
														        <td headers="modnot" style="text-align: center; cursor: pointer;"><a onclick="ModificarNotEv('divModificar',<%=rutaNot.getConsecutivo()%>);"><img src="../img/updateNotEv.png"/></a></td>
														        <td headers="elemnot" style="text-align: center; cursor: pointer;"><a onclick="eliminarNotEv(<%=rutaNot.getConsecutivo()%>);"><img src="../img/eliminarNotEv.png"/></a></td>													
																</tr>
																<% numNoticias++;
																
															}
														%>
												     
												     
												    	<%ArrayList<Noticias> eventos1 = CargaNoticiasYEventos.cargaEventos();
														    int numEventos=0;
															for (int i = 0; i < eventos1.size(); i++) {
																Noticias rutaNot = (Noticias) eventos1.get(i);
																%>
																<tr>
																<td headers="tipoevt"><%=msj.getString("crearnoticiaseventos.33")%></td>
														        <td headers="fechaevt"><%=rutaNot.getFecha()%></td>
														        <td headers="tituloevt"><%=rutaNot.getNombre().toLowerCase()%></td>
														        <td headers="modevt" style="text-align: center; cursor: pointer;"><a onclick="ModificarNotEv('divModificar',<%=rutaNot.getConsecutivo()%>);"><img src="../img/editarNotEv.png"/></a></td>
														        <td headers="elemevt" style="text-align: center; cursor: pointer;"><a onclick="eliminarNotEv(<%=rutaNot.getConsecutivo()%>);"><img src="../img/eliminarNotEv.png"/></a></td>													
																</tr>
																<% numEventos++;
															}
														%>
												     
												    </tbody>
												 </table>
												 <div>
												    <input type="hidden" name="consecutivo" value="consecutivo">
												 </div>	
											</form>	
										  </div>	
										</div>
										
										<div id="divCrear" style="display: none;" class="div_noticias_eventos">
											<div class="block-gray block">									
												<h2><%=msj.getString("crearnoticiaseventos.38") %></h2>
		                                        <br><br>
												<form method="POST" enctype="multipart/form-data" action="/MonitoreoBC-WEB/crearNoticiasServlet" 
													id="formImagenUsuario" name="formImagenUsuario" onsubmit="return validarDatosForm()">
													
													<div style="margin-left: 20px">
													<input type="hidden" id="idUsuario" name="idUsuario" value="<%=usuario.getIdentificacion()%>">
													
													<label for="exampleInputEmail1">Nombre:<span class="obligatorio">*</span></label>
													<input type="text" name="nombreNoticia" id="nombreNoticia" style="width: 280px">
													<br><br>
													
													<label for="exampleInputEmail1">Tipo:<span class="obligatorio">*</span></label>
													<div class="select-wrapper" style="width: 300px">
														<select class="form-control" name="tipoNoticia" id="tipoNoticia">
															<option value="-1">Seleccionar</option>
															<option value="1">Noticia</option>
															<option value="2">Evento</option>
														</select>
													</div>
													<br>
													
													<label for="exampleInputEmail1">Descripcion:<span class="obligatorio">*</span></label>
													<textarea style="resize: none; font-size: 15px; width: 500px; height: 100px;" name="descripcion" id="descripcion"></textarea>
													<br><br>
													
													<label for="exampleInputEmail1">Desea adjutar una imagen?(JPG, PNG):</label>
													<input type="file" name="imagenNoticia" id="imagenNoticia" class="btn btn-default">
													
													</div>
													<br>
													<div class="form-actions">
													<input type="submit" value="Crear">
													
													</div>
												</form>
											</div>
										</div>
										
										
										<div id="divModificar" style="display: none;" class="div_noticias_eventos">
											

										</div>
									  
								
								</div>
								
								
								
								
							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
							</div>

						</div>
					</div>
				</div>

			</div>

			<%=UI.getFooter(msj) %>									

		</div>
	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										
</body>
</html> 


<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>