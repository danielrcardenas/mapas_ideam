<%@page import="co.gov.ideamredd.dao.Constantes"%>
<%@page import="co.gov.ideamredd.entities.DocumentosAsociados"%>
<%@page import="co.gov.ideamredd.entities.ActividadRedd"%>
<%@page import="co.gov.ideamredd.entities.Tenencia"%>
<%@page import="co.gov.ideamredd.entities.Metodologia"%>
<%@page import="co.gov.ideamredd.entities.CAR"%>
<%@page import="co.gov.ideamredd.entities.TipoBosque"%>
<%@page import="co.gov.ideamredd.entities.Pais"%>
<%@page import="co.gov.ideamredd.entities.Municipios"%>
<%@page import="co.gov.ideamredd.entities.Depto"%>
<%@page import="co.gov.ideamredd.dao.ConsultaProyecto"%>
<%@page import="co.gov.ideamredd.entities.Proyecto"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.dao.CargaInicialDatosProyectos"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>  
<title>Detallar Proyecto</title>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";

String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	String idProyecto = Util.desencriptar(request.getParameter("id"));
	ArrayList<Noticias> noticias = CargaInicialDatosProyectos.getNoticias(); 
	ArrayList<Noticias> eventos = CargaInicialDatosProyectos.getEventos();
	
	LenguajeI18N i18n = new LenguajeI18N();
	if(request.getParameter("idiom") != null )
	{
		i18n.setLenguaje(request.getParameter("idiom"));
		i18n.setPais(request.getParameter("pais"));
	}else
	if(request.getSession().getAttribute("i18nAuxLeng") != null )
	{
		i18n.setLenguaje((String)request.getSession().getAttribute("i18nAuxLeng"));
		i18n.setPais((String)request.getSession().getAttribute("i18nAuxPais"));
	}
	else
		if(i18n.getLenguaje() == null)
		{
	i18n.setLenguaje("es");
	i18n.setPais("CO");
		}
	ResourceBundle msj = i18n.obtenerMensajeIdioma();
	
	Usuario usuario =null;
	if(request.getParameter("us")!=null)
	{
		usuario = UtilWeb.consultarUsuarioPorDoc(Integer
			.valueOf(Util.desencriptar(request.getParameter("us"))));
	}
	
	Proyecto proyecto = ConsultaProyecto.consultarProyectoId(Integer.valueOf(idProyecto));
// 	String[] geometria = ConsultaProyecto.consultarGeoProyecto(Integer.valueOf(idProyecto));
// 	if(geometria[0].equals(Constantes.punto))
// 		geometria[0]="Punto";
// 	else if(geometria[0].equals(Constantes.linea))
// 		geometria[0]="Linea";
// 	else if(geometria[0].equals(Constantes.poligono))
// 		geometria[0]="Poligono";
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

<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="../js/popUpUsuarios.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />

<script type="text/javascript">
$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });

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

var puntos = new Array();
var cont=0;
var doc = new Array();
var indice=0;
function creaGeometria() {
// 	var contenedor = document.getElementById("puntos");
// 	var j;
<%-- 		puntos[cont]='<%=geometria[1]%>';  --%>
// 		var p = puntos[cont].split(",");
// 		for(j=0;j<p.length;j+=2){
// 			var divPuntos = document.createElement('div');
// 			contenedor.appendChild(divPuntos);
// 			var labelX = document.createElement('label');
// 			labelX.innerText = 'Punto X:';
// 			labelX.innerHTML = 'Punto X:';
// 			divPuntos.appendChild(labelX);
// 			var X = document.createElement('input');
// 			X.type = 'text';
// 			X.readonly = 'readonly';
// 			X.id = 'CoordX';
// 			X.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
// 			X.value = p[j];
// 			divPuntos.appendChild(X);
// 			var labelY = document.createElement('label');
// 			labelY.innerText = 'Punto Y:';
// 			labelY.innerHTML = 'Punto Y:';
// 			divPuntos.appendChild(labelY);
// 			var Y = document.createElement('input');
// 			Y.type = 'text';
// 			Y.readonly = 'readonly';
// 			Y.id = 'CoordY';
// 			Y.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
// 			Y.value = p[j+1];
// 			divPuntos.appendChild(Y);
// 		}
		
	
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
<body onload="creaGeometria();cargaDocumentos();" class='sidebar-first front'>
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
									<a href="<%=basePath2%>MonitoreoBiomasaCarbono/home.jsp"><img src="../img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-images-header" class="block">
								<div class="content">
									<a href="http://www.minambiente.gov.co/">
									<img src="../img/img-min.png" alt="">
									</a> 
									<a href="http://wsp.presidencia.gov.co/portal/Paginas/default.aspx">
									<img src="../img/img-prosperidad.png" alt="">
									</a>
									<a href="http://www.moore.org/">
									<img src="../img/img-moore.png" alt="">
									</a> 
									<a href="http://www.patrimonionatural.org.co/">
									<img src="../img/img-patrimonio.png" alt="">
									</a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->
							<div id="block-top-menu" class="block block-menu">
<%-- 								<%out.println(usuario);%> --%>
								<%if(usuario==null){%>
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
												onclick="enviarForms(document)"></input>
										</div>

									</div>
									<ul class="social-menu item-list">
										<li id="icoAyuda" class="menu-item help first"
											onclick="popUpAyudaOpen(document)"
											style="margin-right: 10px; cursor: pointer;"><a></a></li>
										<li class="menu-item facebook" style="cursor: pointer;"><a></a></li>
										<li class="menu-item en" style="cursor: pointer;"><a
											onclick="lenguaje(2);"></a></li>
										<li class="menu-item es" style="cursor: pointer;"><a
											onclick="lenguaje(1)"></a></li>
									</ul>

								</div>
								<!-- /.content -->
								<%}else{ %>
								<div class="content">
									<div id="form-loguin-header" role="form">
										<div class="form-group">
											<label for="exampleInputEmail1"><%=msj.getString("home.registrado.bienvenido")%></label>
										</div>

										<div class="form-group">
											<label for="exampleInputPassword1"><%=usuario.getNombre()%></label>
										</div>
										<%
											if (!usuario.getRolNombre().contains("ADMINISTRADOR_GENERAL")) {
										%>
										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath2%>Usuario-Web/reg/modificarUsuario.jsp?id=<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
													<%=msj.getString("home.registrado.modificar")%></a></label>
										</div>
										<%
											}
										%>

										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath2%>MonitoreoBiomasaCarbono/limpiarSesionServlet"><%=msj.getString("home.registrado.cerrar")%></a></label>
										</div>
									</div>
									<ul class="social-menu item-list">
										<!-- Aca estaba lo de twitter -->
										<li class="menu-item facebook"><a></a></li>
										<li class="menu-item en"><a onclick="lenguaje(2);"></a></li>
										<li class="menu-item es"><a onclick="lenguaje(1)"></a></li>
									</ul>

								</div>
								<%} %>
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
											<li class="item-menu first"><a href="<%=basePath2%>Usuario-Web/extra/presentacion.jsp"><%=msj.getString("pagina.Presentacion")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/aspectosGenerales.jsp" ><%=msj.getString("pagina.AGenerales")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/normatividad.jsp" ><%=msj.getString("pagina.Normatividad")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/enlacesRel.jsp" ><%=msj.getString("pagina.SitiosInteres")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/documentacion.jsp" ><%=msj.getString("pagina.Documentacion")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/protocolos.jsp" ><%=msj.getString("pagina.Protocolos")%></a></li>
											<li class="item-menu"><a href="<%=basePath2%>Usuario-Web/extra/glosario.jsp" ><%=msj.getString("pagina.Glosario")%></a></li>
											<li class="item-menu last"><a href="<%=basePath2%>MonitoreoBiomasaCarbono/pub/bosqueEnCifras.jsp">Bosque en Cifras</a></li>
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
															<input type="text" readonly="readonly" id="paispro" class="form-control" value ="">
<%-- 																value="<%=geometria[0]%>"> --%>
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
<!-- 						<div class="menu-servicios menu-postscript"> -->
<!-- 							<h3>Servicios de Cuidadanía</h3> -->
<!-- 							<ul> -->
<!-- 								<li><a href="">Visitas Casa de Nariño</a></li> -->
<!-- 								<li><a href="">Datos de contacto</a></li> -->
<!-- 								<li><a href="">Escríbale al Presidente</a></li> -->
<!-- 								<li><a href="">PSQR</a></li> -->
<!-- 								<li><a href="">Colombia Compra Eficiente</a></li> -->
<!-- 								<li><a href="">Avisos Convocatoria Pública</a></li> -->
<!-- 								<li><a href="">Notificaciones por Aviso</a></li> -->
<!-- 								<li><a href="">Notificaciones Judiciales</a></li> -->
<!-- 								<li><a href="">Proveedores</a></li> -->
<!-- 							</ul> -->
<!-- 						</div> -->
<!-- 						<div class="sistema-web-presidencia select-postscript"> -->
<!-- 							<h3>Sistema Web Presidencia</h3> -->
<!-- 							<div class="select-wrapper"> -->
<!-- 								<select> -->
<!-- 									<option>1</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 								<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 									class="form-submit"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 						<div class="dependecias-presidencia select-postscript"> -->
<!-- 							<h3>Dependencias Presidencia</h3> -->
<!-- 							<div class="select-wrapper"> -->
<!-- 								<select> -->
<!-- 									<option>1</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 								<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 									class="form-submit"> -->
<!-- 							</div> -->
<!-- 						</div> -->
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
		<!--/.page -->
	</form>
	<form method="post"
		action="<%=basePath2%>MonitoreoBiomasaCarbono/j_security_check"
		name="j_security_check" id="j_security_check">
		<input type="hidden" name="j_username" id="j_username" /> <br> <input
			type="hidden" name="j_password" id="j_password" />
	</form>
	<form method="post" action="<%=basePath2%>MonitoreoBiomasaCarbono/registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /> <input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>
	<div id="popUpAyuda"
		style="z-index: 3; position: absolute; width: 135px; height: 80px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;">
			<label for="exampleInputEmail1"
				style="text-align: center; color: white; font-weight: bold;">
				<%=msj.getString("home.popAyuda.titulo")%></label>
		</div>
		<label for="exampleInputEmail1"><a
			href="<%=basePath2%>Usuario-Web/pub/registroUsuario.jsp"> *<%=msj.getString("home.popAyuda.registrarse")%></a></label>
		<label for="exampleInputEmail1" style="text-align: left;"><a
			href="<%=basePath2%>Usuario-Web/pub/recordarClave.jsp?idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
				*<%=msj.getString("home.popAyuda.recordar")%></a></label> <label
			for="exampleInputEmail1" onclick="popUpAyudaClose(document)"
			style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
	</div>
</body>
</html>