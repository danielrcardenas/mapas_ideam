<%@page import="co.gov.ideamredd.entities.EstadoBiomasa"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.entities.BiomasaYCarbono"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicialBiomasa"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="co.gov.ideamredd.dao.PruebaR"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<title>Consulta de biomasa</title>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";
	ArrayList<Noticias> noticias = CargaDatosInicialBiomasa.getNoticias();
	String parametros =  Util.desencriptar(request.getParameter("id"));
	String [] p = parametros.split(";");
	ArrayList<BiomasaYCarbono> biomasas = CargaDatosInicialBiomasa.getBiomasaYCarbono(p[0]);
	String nombreParcela = CargaDatosInicialBiomasa.obtenerNombreParcela(p[0]);
	request.getSession().setAttribute("idParcela", p[0]);
	ArrayList<Noticias> eventos = CargaDatosInicialBiomasa.getEventos(); 
	LenguajeI18N i18n = new LenguajeI18N();
	ResourceBundle msj=null;
	if(p[1].equals("1")){
		i18n.setLenguaje("es");
		i18n.setPais("CO");
		msj = i18n.obtenerMensajeIdioma();
	}
	request.getSession().setAttribute("i18n", msj);
%>

<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />

<script type="text/javascript">

var biomasa;
var biomasas = new Array();
var consecutivo;
var titulos = new Array(6);
titulos[0] = '<%=msj.getString("consulta.parcela.codigo")%>';
titulos[1] = '<%=msj.getString("consulta.parcela.nombre")%>';
titulos[2] = '<%=msj.getString("consulta.fecha.generacion")%>';
titulos[3] = '<%=msj.getString("consulta.biomasa")%>';
titulos[4] = '<%=msj.getString("consulta.carbono")%>';
titulos[5] = '<%=msj.getString("estado.biomasa")%>';

$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });
$(function() {
	$( "#fInicial" ).datepicker(); 
	});
$(function() {
    $( "#ffin" ).datepicker(); 
});

$(document).ready(function () {
	cargarBiomasas();
	idActual=1;
	resultadosContactos(biomasas.length, 'resultados','consultas');
	cargarDatos(biomasas,'consultas',3,titulos,null,0,5);
	crearPaginas(biomasas.length,'consultas',1);
});

function cargarBiomasas(){
	<%for (int i = 0; i < biomasas.size(); i++) {
		BiomasaYCarbono bio = biomasas.get(i);
		EstadoBiomasa b = CargaDatosInicialBiomasa
				.obtenerEstadoBiomasa(bio.getEstado());
		String estado = CargaDatosInicialBiomasa.estadoBiomasa(b);
		String bi = bio.getIdParcela()+";"+nombreParcela+";"+bio.getFechaInicio().toString().substring(0,11)+";"+bio.getBiomasa()+";"+bio.getCarbono()+";"+estado;%>     
		consecutivo = '<%=bio.getConsecutivo()%>';
		biomasa = '<%=bi%>';
		biomasas[<%=i%>]=biomasa;
		
	<%}%>
	return biomasas;
} 

function resultadosContactos(tamanho, principal, contenedor) {
	var cont;
	var prin = document.getElementById(principal);
	cont = document.getElementById(contenedor);
	if (cont != null)
		cont.parentNode.removeChild(cont);
	cont = document.createElement('div');
	cont.id = contenedor;
	cont.className = 'resultados-busqueda';
	var results = document.createElement('h3');
	results.innerText = '<%=msj.getString("consulta.resultados1")%> ' + tamanho + ' <%=msj.getString("consulta.resultados2")%>';
	results.innerHTML = '<%=msj.getString("consulta.resultados1")%> '+ tamanho + ' <%=msj.getString("consulta.resultados2")%>';
	cont.appendChild(results);
	adicionarBoton('<%=msj.getString("registrar.biomasa")%>', cont, 'registrarBiomasa', 2, <%=p[0]%>);
	prin.appendChild(cont);
}

function cargarDatos(parc,contenedor, numColumnas, titulos, permisos, indiceInicial, indiceFinal){
	var i, j, conte;
	var cont = document.getElementById(contenedor);
	for (i=indiceInicial; i < parc.length; i++) {
		if(i<indiceFinal){
			var span1, span2, span3;
			var indiceColumna=1;
			var div = document.createElement('div');
			if(i%2==0)
				div.className='item-busqueda item-busqueda-odd';
			else
				div.className='item-busqueda item-busqueda-even';
			conte = parc[i].split(";");
			span1 = document.createElement('span');
			span1.id="columna"+indiceColumna;
			span1.className= "column column"+indiceColumna;
			indiceColumna++;
			span2 = document.createElement('span');
			span2.id="columna"+indiceColumna;
			span2.className= "column column"+indiceColumna;
			for (j = 0; j < conte.length; j++) {
				if(j==0)
					codigoProyecto = conte[j];
				if(j<5){
					var p = document.createElement('p');
					var strong = document.createElement('strong');
// 					if(j!=4){
						strong.innerText = titulos[j]+": "+ conte[j];
						strong.innerHTML = titulos[j]+": "+conte[j];
// 					}else{//esto no en el general
// 						strong.innerText = conte[j]+","+conte[j+1]+","+conte[j+2];
// 						strong.innerHTML = conte[j]+","+conte[j+1]+","+conte[j+2];
// 						j=6;
// 					}
					p.appendChild(strong);
					span1.appendChild(p);
					div.appendChild(span1);
				}else{
					var p = document.createElement('p');
					var strong = document.createElement('strong');
					strong.innerText = titulos[j]+": "+ conte[j];
					strong.innerHTML = titulos[j]+": "+conte[j];
					p.appendChild(strong);
					span2.appendChild(p);
					div.appendChild(span2);
				}
			}
			if(numColumnas==3){
				var k;
				indiceColumna++;
				span3 = document.createElement('span');
				span3.id="columna"+indiceColumna;
				span3.className= "column column"+indiceColumna;
// 				for(k=0;k<permisos.length;k++){}Cuando se tengan los permisos 
				adicionarBoton('<%=msj.getString("consulta.biomasa.define.estado")%>', span3, 'definirEstado', 1, consecutivo);
//					adicionarBoton('modificar', span3, 'Modificar', 2, codigoProyecto);
//					adicionarBoton('exportar', span3, 'Exportar', 3, codigoProyecto);
//					adicionarBoton('individuos', span3, 'Individuos', 4, codigoProyecto);
				div.appendChild(span3);
			}
			cont.appendChild(div);
		}
	}
}

function adicionarBoton(idBoton, contenedor, valor, opc, id) {
	var formulario = document.createElement("div");
	formulario.className = "form-actions";
	var hiddenIdParcela = document.createElement("input");
	hiddenIdParcela.type = 'hidden';
	hiddenIdParcela.id = 'idProyecto';
	hiddenIdParcela.name = 'idProyecto';
	hiddenIdParcela.value = valor;
	var boton = document.createElement('input');
	boton.type = "button";
	boton.id = 'boton' + idBoton;
	boton.name = 'boton' + idBoton;
	boton.className = "btn btn-default";
	boton.value = idBoton;
	if (boton.addEventListener) {
		boton.addEventListener("click", function() {
			enviar(opc,id);
		}, true);
	} else {
		boton.attachEvent('onclick', function() {
			enviar(opc,id);
		});
	}
	formulario.appendChild(hiddenIdParcela);
	formulario.appendChild(boton);
	contenedor.appendChild(formulario);
}

function paginar(boton){
	idActual=boton;
	var indiceFinal=boton*5;
	var indiceInicial=indiceFinal-5;
	resultadosContactos(proyectos.length, 'resultados','consultas');
	cargarDatos(proyectos,'consultas',3,titulos,null,indiceInicial,indiceFinal);
	crearPaginas(proyectos.length,'consultas',boton);
}

function crearColumnas(numColumnas,contResults){
	var i;
	for(i=0;i<numColumnas;i++){
		var span = document.createElement('span');
		span.id="columna"+(i+1);
		span.class= "column column"+(i+1);
		contResults.appendChild(span);
	}
}

function crearBoton(indice, id, ul){
	var li = document.createElement('li');
	if(indice==(id-1)){
		li.className="item-pager active";
		li.innerText = indice+1;
		li.innerHTML = indice+1;
	}else{
		li.className="item-pager";
		var a = document.createElement('a');
		a.id=indice+1;
		a.innerText = indice+1;
		a.innerHTML = indice+1;
		a.onclick=function() {paginar((indice+1)); };
		li.appendChild(a);
	}
	ul.appendChild(li);
}

function crearPaginas(tamanho,contenedor, id){
	var i;
	var cont = document.getElementById(contenedor);
	var paginador = document.getElementById('paginador');
	if (paginador != null)
		paginador.parentNode.removeChild(paginador);
	paginador = document.createElement('div');
	paginador.id='paginador';
	paginador.className = "pager";
	var ul = document.createElement('ul');
	var numeroHojas = Math.ceil(tamanho/5);
	
	var liInicial = document.createElement('li');
	var aInicial = document.createElement('a');
	var liAnterior = document.createElement('li');
	var aAnterior = document.createElement('a');
	var liSiguiente = document.createElement('li');
	var aSiguiente = document.createElement('a');
	var liFinal = document.createElement('li');
	var aFinal = document.createElement('a');
	
	liInicial.className="item-pager-controls item-pager-first"; 
	liInicial.onclick=function() {paginar((1)); };
	liAnterior.className="item-pager-controls item-pager-previous";
	liAnterior.onclick=function() {if(idActual>1){paginar(idActual-1);}};
	liSiguiente.className="item-pager-controls item-pager-next"; 
	liSiguiente.onclick=function() {if(idActual<numeroHojas){paginar(idActual+1);}};
	liFinal.className="item-pager-controls item-pager-last"; 
	liFinal.onclick=function() {paginar((numeroHojas)); };
	
	liInicial.appendChild(aInicial);
	liAnterior.appendChild(aAnterior);
	liSiguiente.appendChild(aSiguiente);
	liFinal.appendChild(aFinal);
	
	ul.appendChild(liInicial);
	ul.appendChild(liAnterior);
	for(i=0;i<numeroHojas;i++){
		crearBoton(i, id, ul);
	}
	ul.appendChild(liSiguiente);
	ul.appendChild(liFinal);
	
	paginador.appendChild(ul);
	cont.appendChild(paginador);
}

	function volver() {
		history.back(-1);
	}
	
	function lenguaje(id){
		if(id==1){
			document.getElementById('lenguaje').value="ES";
		}else{
			document.getElementById('lenguaje').value="EN";
		}
		document.getElementById('pagina').value="<%=request.getRequestURI()%>";
		document.getElementById('home').submit();
	}

	function definirEstado(id){
		window.location ='<%=basePath%>reg/definirEstadoBiomasa.jsp?bio=' + id;
	}
	
	function enviar(opc,id) {
		var idBiomasa = document.getElementById('biomasa_hidden');
		idBiomasa.value = id;
		var dir = document.getElementById('dir');
		dir.value = opc;
		var form = document.getElementById('consultarBiomasa');
		form.submit();
	}

</script>
</head>
<body>
	<form id="consultarBiomasa" action="<%=basePath%>redireccion" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina"> 
		<input type="hidden" name="biomasa_hidden" id="biomasa_hidden" /> 
		<input type="hidden" name="dir" id="dir" />
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
										href="/"><img src="img/img-prosperidad.png" alt=""></a>
									<a href="/"><img src="img/img-moore.png" alt=""></a> <a
										href="/"><img src="img/img-patrimonio.png" alt=""></a>
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
											<li class="item-noticia first"><img src="img/"
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
											<span class="item-breadcrumb separator">></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>
									</div>

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("descargas.filtrar")%></h3>
													<div id="resultados">
														<div id="consultas">

														</div>
														<div id="paginador" class="pager">
														
														</div>
													</div>
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
	<%out.println(PruebaR.prueba());%>
</body>
</html>