<%@page import="co.gov.ideamredd.web.proyecto.dao.Constantes"%>
<%@page import="co.gov.ideamredd.proyecto.entities.DocumentosAsociados"%>
<%@page import="co.gov.ideamredd.proyecto.entities.ActividadRedd"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Tenencia"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Metodologia"%>
<%@page import="co.gov.ideamredd.proyecto.entities.CAR"%>
<%@page import="co.gov.ideamredd.proyecto.entities.TipoBosque"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Pais"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Municipios"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Depto"%>
<%@page import="co.gov.ideamredd.proyecto.dao.ConsultaProyecto"%>
<%@page import="co.gov.ideamredd.proyecto.entities.Proyecto"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.web.proyecto.dao.CargaInicialDatosProyectos"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
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
<html class='no-js'> 
<head>  
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

	String idProyecto = Util.desencriptar(request.getSession().getAttribute("idProyecto").toString());
	Proyecto proyecto = ConsultaProyecto.consultarProyectoId(Integer.valueOf(idProyecto));
	String[] geometria = ConsultaProyecto.consultarGeoProyecto(Integer.valueOf(idProyecto));
	if(geometria[0].equals(Constantes.punto))
		geometria[0]="Punto";
	else if(geometria[0].equals(Constantes.linea))
		geometria[0]="Linea";
	else if(geometria[0].equals(Constantes.poligono))
		geometria[0]="Poligono";
	Depto depto = ConsultaProyecto.consultarDepto(Integer.valueOf(idProyecto));
	Municipios municipios = ConsultaProyecto.consultarMcipio(Integer.valueOf(idProyecto));
	Pais pais = ConsultaProyecto.consultarPais(Integer.valueOf(idProyecto));
	TipoBosque bosque = ConsultaProyecto.consultarTipoBosque(Integer.valueOf(idProyecto));
	CAR car = ConsultaProyecto.consultarCAR(Integer.valueOf(idProyecto));
	Metodologia metodologia = ConsultaProyecto.consultarMetodologia(Integer.valueOf(idProyecto));
	Tenencia tenencia = ConsultaProyecto.consultarTenencia(Integer.valueOf(idProyecto));
	ArrayList<ActividadRedd> actividad = ConsultaProyecto.consultarActividadProyecto(Integer.valueOf(idProyecto));
	String act ="";
	for(int i=0;i<actividad.size();i++){
		ActividadRedd actividadRedd = actividad.get(i);
		if(act.length()==0)
	act=actividadRedd.getNombre();
		else
	act+=";"+actividadRedd.getNombre();
	}
	ArrayList<DocumentosAsociados> documentos = ConsultaProyecto.consultarDocumentosProyecto(Integer.valueOf(idProyecto));
%>
<title>Detallar Proyecto</title>

<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

<script type="text/javascript">
$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });

$(document).ready(function() {
	inicializarNavegador();
});

var puntos = new Array();
var cont=0;
var doc = new Array();
var indice=0;
function creaGeometria() {
	var contenedor = document.getElementById("puntos");
	var j;
		puntos[cont]='<%=geometria[1]%>'; 
		//alert(puntos.toString());
		var p = puntos[cont].split(",");
		for(j=0;j<p.length;j+=2){
			var divPuntos = document.createElement('div');
			contenedor.appendChild(divPuntos);
			var labelX = document.createElement('label');
			labelX.innerText = 'Punto X:';
			labelX.innerHTML = 'Punto X:';
			divPuntos.appendChild(labelX);
			var X = document.createElement('input');
			X.type = 'text';
			X.readonly = 'readonly';
			X.id = 'CoordX';
			X.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			X.value = p[j];
			divPuntos.appendChild(X);
			var labelY = document.createElement('label');
			labelY.innerText = 'Punto Y:';
			labelY.innerHTML = 'Punto Y:';
			divPuntos.appendChild(labelY);
			var Y = document.createElement('input');
			Y.type = 'text';
			Y.readonly = 'readonly';
			Y.id = 'CoordY';
			Y.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			Y.value = p[j+1];
			divPuntos.appendChild(Y);
		}
		
	
}

function cargaDocumentos() {
	var contenedor = document.getElementById("documentos");
	var i;
	<%for(int i=0;i<documentos.size();i++){
		DocumentosAsociados docs = documentos.get(i);%>
		doc[indice]='<%=docs.getUn_NombreDocumento()+";"+docs.getUn_NombreTipoDoc()+";"+docs.getUna_FechaIngresoDocumento().toString().substring(0,10)+";"+docs.getEs_Publico()%>';
		indice++;
<%}%>
	for (i = 0; i < doc.length; i++) {
			var d = doc[i].split(";");
			var labelNombre = document.createElement('div');
			contenedor.appendChild(labelNombre);
			var labelX = document.createElement('label');
			labelX.innerText = 'Nombre Documeto:';
			labelX.innerHTML = 'Nombre Documeto:';
			labelNombre.appendChild(labelX);

			var textNombre = document.createElement('div');
			contenedor.appendChild(textNombre);
			var X = document.createElement('input');
			X.type = 'text';
			X.readonly = 'readonly';
			X.id = 'CoordX';
			X.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			X.value = d[0];
			textNombre.appendChild(X);

			var labelTipo = document.createElement('div');
			contenedor.appendChild(labelTipo);
			var labelY = document.createElement('label');
			labelY.innerText = 'Tipo Documento:';
			labelY.innerHTML = 'Tipo Documento:';
			labelTipo.appendChild(labelY);

			var textTipo = document.createElement('div');
			contenedor.appendChild(textTipo);
			var Y = document.createElement('input');
			Y.type = 'text';
			Y.readonly = 'readonly';
			Y.id = 'CoordY';
			Y.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			Y.value = d[1];
			textTipo.appendChild(Y);

			var labelFecha = document.createElement('div');
			contenedor.appendChild(labelFecha);
			var labelY = document.createElement('label');
			labelY.innerText = 'Fecha de ingreso:';
			labelY.innerHTML = 'Fecha de ingreso:';
			labelFecha.appendChild(labelY);

			var textFecha = document.createElement('div');
			contenedor.appendChild(textFecha);
			var Y = document.createElement('input');
			Y.type = 'text';
			Y.readonly = 'readonly';
			Y.id = 'CoordY';
			Y.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			Y.value = d[2];
			textFecha.appendChild(Y);

			var labelPublico = document.createElement('div');
			contenedor.appendChild(labelPublico);
			var labelY = document.createElement('label');
			labelY.innerText = 'Publico:';
			labelY.innerHTML = 'Publico:';
			labelPublico.appendChild(labelY);

			var textPublico = document.createElement('div');
			contenedor.appendChild(textPublico);
			var Y = document.createElement('input');
			Y.type = 'text';
			Y.readonly = 'readonly';
			Y.id = 'CoordY';
			Y.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			if (d[4] == '0') {
				Y.value = 'No';
			} else {
				Y.value = 'Si';
			}
			textPublico.appendChild(Y);
		}
	}
</script>
</head>
<body class='sidebar-first front'>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
						

							<div id="content">
								<div class="content-inner">

<!-- 									<div class="pre-content"> -->
<!-- 										<div class="breadcrumb"> -->
<!-- 											<span class="item-breadcrumb separator home"><a -->
<!-- 												href="#">Home</a></span> <span class="item-breadcrumb separator">></span> -->
<!-- 											<span class="item-breadcrumb separator">></span> <span -->
<!-- 												class="item-breadcrumb active">Ipsum</span> -->
<!-- 										</div> -->
<!-- 									</div> -->

									<div class="content">

										<div class="content">
											<div id="accordion">
												<h3>Detalles del proyecto</h3>
												<div class="form-datos-parcela form-columnx2" role="form">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.codigo")%>:</label>
														<input type="text" class="form-control"
															readonly="readonly" value="<%=proyecto.getConsecutivo()%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.nombre")%>:</label> 
														<input type="text" class="form-control" id="exampleInputEmail1" readonly="readonly"
															title="" value="<%=proyecto.getNombre()%>" />
													</div>
													<div class="form-group item-textarea">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.area")%>:</label> 
														<textarea id="areapro" name="areapro" readonly="readonly"
															title="" rows="2" cols="8"><%=proyecto.getDescripcionArea()%></textarea>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.tenencia")%>:</label> 
														<input type="text" class="form-control" id="exampleInputEmail1" readonly="readonly"
															title="" value="<%=tenencia.getDescripcion()%>">
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.actividades")%>:</label> 
														<input type="text" class="form-control" id="exampleInputEmail1" readonly="readonly"
															title="" value="<%=act%>"/>
													</div>
													
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaInicio")%>:</label> 
														<input id="fechaIni" name="fechaIni" type="text"
															readonly="readonly" title="" class="form-control"
															value="<%=((String)proyecto.getFechaInicio().toString()).substring(0,11)%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaFin")%>:</label> 
														<input id="fechaFin" name="fechaFin" type="text"
															readonly="readonly" title="" class="form-control"
															value="<%=(proyecto.getFechaFin().toString()).substring(0,11)%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.co2")%>:</label> 
														<input name="copro" id="copro" type="text" readonly="readonly" class="form-control"
															title="" value="<%=proyecto.getCo2Reducir()%>">
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.tasa")%>:</label> 
														<input name="taspro" id="taspro" type="text" readonly="readonly" class="form-control"
															title="" value="<%=proyecto.getTasaDeforestar()%>">
													</div>													
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.nombreMetodologia")%>:</label> 
														<input name="metpro" id="metpro" type="text" readonly="readonly" class="form-control"
															title="" value="<%=metodologia.getMetodologiaNombre()%>">
													</div>
													<%if(metodologia.getMetodologiaId()!=Constantes.ideam){%>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.descripcionMetodologia")%>:</label> 
														<input name="metpro" id="metpro" type="text" readonly="readonly" class="form-control"
															title="" value="<%=metodologia.getDescripcion()%>">
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.ecuacionMetodologia")%>:</label> 
														<input name="metpro" id="metpro" type="text" readonly="readonly" class="form-control"
															title="" value="<%=metodologia.getMetodologiaEcuacion()%>">
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.proyecto.archivoMetodolgia")%>:</label> 
														<input name="metpro" id="metpro" type="text" readonly="readonly" class="form-control"
															title="" value="<%=metodologia.getMetodologiaDirArchivo()%>">
													</div>	
												<%} %>
												</div>
												<h3><%=msj.getString("titulos.ubicacion")%></h3>
												<div>
													<div class="form-localizacion" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.pais")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=pais.getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.depto")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=depto.getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.mcipio")%>:</label>
															<input type="text" readonly="readonly" id="munpro" class="form-control"
																value="<%=municipios.getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.car")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=car.getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.bosque")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=bosque.getTipoBosque()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.altitud")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=bosque.getAltitud()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.temperatura")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=bosque.getTemperatura()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.precipitacion")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control"
																value="<%=bosque.getPrecipitacion()%>">
														</div>
													</div>

												</div>
												<h3><%=msj.getString("titulos.localizacion")%></h3>
												<div class="form-localizacion" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.geometria")%>:</label>
															<input type="text" readonly="readonly" id="paispro" class="form-control" value="<%=geometria[0]%>" />
														</div>
														<div class="form-group">
														<div id="puntos"></div>
														</div>
												</div>
												<h3><%=msj.getString("titulos.documentos")%></h3>
												<div class="form-localizacion" role="form">
													<div id="documentos"></div>
												</div>
											</div>
											<div class="form-actions">
												<input type="button" value="regresar"
													onclick="javascript:history.back()">
											</div>
											
											
											<script type="text/javascript">
												creaGeometria();
												cargaDocumentos();
											</script>


										</div>
									</div>

								</div>
							</div>
							<!-- content-inner -->
							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
						</div>
						<!-- /.content-->
					</div>
				</div>
				<!-- /.section-->
			</div>
			<!-- /.section-wrapper-->
		</div>
		<!--/.main -->

		<%=UI.getFooter(msj) %>									

		</div>
	</form>

</body>
</html>