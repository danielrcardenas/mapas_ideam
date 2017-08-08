<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.entities.Dataset"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";

String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
	ArrayList<Noticias> noticias = CargaDatosInicial.getNoticias(); 
	ArrayList<Noticias> eventos = CargaDatosInicial.getEventos();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar Inventario de Imágenes</title>

<link rel="stylesheet" type="text/css" href="../css/jsDatePick_ltr.min.css" />
<script type="text/javascript" src="../js/jquery.1.4.2.js"></script>
<script type="text/javascript" src="../js/jsDatePick.jquery.min.1.3.js"></script>
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />

<script type="text/javascript">
var datasetFiltradosLista="";
var datasetFiltradosListaEncab="";
var datasetsFiltrados = new Array();
var datasets = new Array();
var datasetTemp;
var titulos = new Array(13);
var posParametros;
var parametros;
var paramsTiposID;
var paramsTipoDato;
var codigos;
var idActual;
titulos[0] = 'Nombre Usuario';
titulos[1] = 'Tipo ID';
titulos[2] = 'Número ID';
titulos[3] = 'Nombre imagen';
titulos[4] = 'Tipo imágen';
titulos[5] = 'Fecha descarga';
	   
$(document).ready(function () {
	cargarDatasets();
	cargarEncabezados();
	//resultadosContactos(datasets,'resultados', 'trDataset');
	idActual=1;
	resultadosContactos(datasets.length, 'resultados','consultas');
	cargarDatos(datasets,'consultas',2,titulos,null,0,5);
	crearPaginas(datasets.length,'consultas',1);
	
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
		document.getElementById('pagina').value="<%=request.getRequestURI()%>"; 
		document.getElementById('home').submit();
	}
	$(document).ready(function() {
		new JsDatePick({
			useMode : 2,
			target : "fInicial",
			dateFormat : "%d/%m/%Y"

		});
	});

	$(document).ready(function() {
		new JsDatePick({
			useMode : 2,
			target : "fFinal",
			dateFormat : "%d/%m/%Y"

		});
	});
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
		results.innerText = 'Se encontraron' + tamanho + ' resultado(s) para esta búsqueda';
		results.innerHTML = 'Se encontraron'+ tamanho + ' resultado(s) para esta búsqueda';
		cont.appendChild(results);
		//crearColumnas(numColumnas,cont);
		prin.appendChild(cont);
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
	
	function paginar(boton){
		idActual=boton;
		var indiceFinal=boton*5;
		var indiceInicial=indiceFinal-5;
		resultadosContactos(datasets.length, 'resultados','consultas');
		cargarDatos(datasets,'consultas',2,titulos,null,indiceInicial,indiceFinal);
		crearPaginas(datasets.length,'consultas',boton);
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
	

	function cargarDatos(parc,contenedor, numColumnas, titulos, permisos, indiceInicial, indiceFinal){
		var i, j, conte;
		var cont = document.getElementById(contenedor);
		datasetsFiltradosLista="";
		
		for (i=indiceInicial; i < parc.length; i++) {
			if(i<indiceFinal){
				var span1, span2;
				var indiceColumna=1;
				var div = document.createElement('div');
				if(i%2==0)
					div.className='item-busqueda item-busqueda-odd';
				else
					div.className='item-busqueda item-busqueda-even';
				datasetsFiltradosLista = datasetsFiltradosLista+parc[i]+",";
				conte = parc[i].split(",");
				span1 = document.createElement('span');
				span1.id="columna"+indiceColumna;
				span1.className= "column column"+indiceColumna;
				indiceColumna++;
				span2 = document.createElement('span');
				span2.id="columna"+indiceColumna;
				span2.className= "column column"+indiceColumna;
				for (j = 0; j < conte.length; j++) {
					if(j<5){
						var p = document.createElement('p');
						var strong = document.createElement('strong');
						strong.innerText = titulos[j]+": "+ conte[j];
						strong.innerHTML = titulos[j]+": "+conte[j];
						
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

				cont.appendChild(div);
			}
		}
		
		document.getElementById("dataSetsLista").value=datasetsFiltradosLista;
	}
	
	

	function cargarEncabezados(){
		for (var j = 0; j < 6; j++) {
			datasetFiltradosListaEncab = datasetFiltradosListaEncab + titulos[j] + ",";
		}
		document.getElementById("dataSetsListaEncab").value=datasetFiltradosListaEncab; 
	}
	function cargarDatasets(){
		
		<%ArrayList<String> DS = CargaDatosInicial.getDatasetsDescargadosString();
		for (int i = 0; i < DS.size(); i++) {%>
			datasets[<%=i%>] = '<%=DS.get(i)%>';
		<%}%>

		}
	
	
	function filtraDatasets(){
		datasetsFiltrados.length=0;
		var nom = document.getElementById('nomUsr');
		var nid = document.getElementById('numID');
		var tid = document.getElementById('tipoDocUsr');
		var tda = document.getElementById('tipoDato');
		
		var fin = document.getElementById('fInicial');
		var ffi = document.getElementById('fFinal');
		//var control;
		//Obtener parametros
		parametros = new Array();
		
		paramsTiposID = new Array();
		paramsTipoDato = new Array();
		
		codi = new Array();
		posParametros=0;

		var nomUsuario= nom.value;
		var numID= nid.value;
		obtenerParametrostid(tid,$('#tipoDocUsr option:selected'));
		obtenerParametrostda(tda,$('#tipoDato option:selected'));
		
		datasetsFiltrados=datasets;
		//if(palabraClave.length>0 && paramsTiposImgMap.length>0 && paramsRes.length>0 && paramsTipoDato.length>0){
			
		//	}
		//else{ 
			if(nomUsuario.length>0){ 
				datasetsFiltrados=filtrarNomNumID(datasets,nomUsuario,0);
			}
			if(numID.length>0){ 
				datasetsFiltrados=filtrarNomNumID(datasets,numID,2);
			}
			if(paramsTiposID.length>0){
				datasetsFiltrados=filtrarListas(datasetsFiltrados,paramsTiposID,1);
			}
			if(paramsTipoDato.length>0){
				datasetsFiltrados=filtrarListas(datasetsFiltrados,paramsTipoDato,4);
			}
		
			if(fin.value != '' && ffi.value != ''){
				datasetsFiltrados = filtroFechas(fin.value, ffi.value, datasetsFiltrados, 5);
			}
			idActual=1;
			resultadosContactos(datasetsFiltrados.length, 'resultados','consultas');
			cargarDatos(datasetsFiltrados,'consultas',2,titulos,null,0,5);
			crearPaginas(datasetsFiltrados.length,'consultas',1);
	//	resultadosContactos(datasetsFiltrados,'resultados', 'trDataset');
		//$('#black').smartpaginator({ totalrecords: datasetsFiltrados.length, recordsperpage: 8, datacontainer: 'resultados', dataelement: 'div', initval: 0, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', theme: 'black' });
		
		//$('#red').smartpaginator({ totalrecords: 32, recordsperpage: 4, length: 4, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', theme: 'red', controlsalways: true, onchange: function (newPage) {
	      //  $('#r'+ newPage);
	    //}
	    //});

	}

	function obtenerParametrostid(obj, selectedOpt){
		if(obj.value.length > 0){
			for(var j=0;j<selectedOpt.length;j++){
				paramsTiposID[j]=selectedOpt[j].value;
			}
		}
	}

	function obtenerParametrostda(obj,selectedOpt){
		if(obj.value.length > 0){
			for(var j=0;j<selectedOpt.length;j++){
				paramsTipoDato[j]=selectedOpt[j].value;
			}
		}
	}
	
		
	function filtroFechas(fecha1, fecha2, datasets, indice){
		var datasetsTemp = new Array();
		var posFiltrados;
		posFiltrados=0;
		var i;
		var p;
		var fi=fecha1.split("/");
		var ff=fecha2.split("/");
		for(i=0;i<datasets.length;i++){
			p = datasets[i].split(',');
			var f = p[indice].split("/");
			var myDate = new Date(f[2], f[1], f[0]);
			var startDate = new Date(fi[2], fi[1], fi[0]);
			var endDate = new Date(ff[2], ff[1], ff[0]);
			if (startDate < myDate && myDate < endDate) {
				datasetsTemp[posFiltrados]=datasets[i];
				 posFiltrados++;
			}
		}
		return datasetsTemp;
	}
	function filtro(param, arreglo, indice){
		var i;
		var index=0;
		var parcels = new Array();
		if(indice){
			
		}else{
		for(i=0;i<arreglo.length;i++){
			var p = arreglo[i].split(',');
			 if((p[indice].search(param))!= -1){
					parcels[index] = arreglo[i];
					index++;
			}
		}
		}
		return parcels;
	}
	
	function filtrarNomNumID(datasets,palabra,posArreglo){
		var datasetsTemp = new Array();
		var posFiltrados;
		posFiltrados=0;
		var conte;
		for (var i = 0; i < datasets.length; i++) {
			conte = datasets[i].split(",");
			if((new RegExp(palabra)).test(conte[posArreglo])){
				datasetsTemp[posFiltrados]=datasets[i];
				posFiltrados++;
			}
		}
	return datasetsTemp;
	}
	
	function filtrarListas(datasets,paramsLista,posArreglo){
		var datasetsTemp = new Array();
		var posFiltrados;
		posFiltrados=0;
		var conte;
		for (var i = 0; i < datasets.length; i++) {
			conte = datasets[i].split(",");
			for (var j=0; j<paramsLista.length;j++){
				if(paramsLista[j]==conte[posArreglo]){
					datasetsTemp[posFiltrados]=datasets[i];
					posFiltrados++;
					//if((new RegExp(paramsLista[j])).test(conte[posArreglo])){
					//datasetsTemp[posFiltrados]=datasets[i];
					//posFiltrados++;
				}
			}
		}
	return datasetsTemp;
	}
	function validarRepDatasets(){
		//Pobla la variable dataSetsLista
		datasetsFiltradosLista="";
		if (datasetsFiltrados.length==0){
			for (var i = 0; i < datasets .length; i++) {
			 	datasetsFiltradosLista = datasetsFiltradosLista+datasets[i]+",";
				}
			
		}else{
			for (var i = 0; i < datasetsFiltrados.length; i++) {
			 	datasetsFiltradosLista = datasetsFiltradosLista+datasetsFiltrados[i]+",";
				}
		
		}
		document.getElementById("dataSetsLista").value=datasetsFiltradosLista;
	}
	
</script>
</head>
<body class='sidebar-first front'>
		<div id="page">
			<div id="header">
				<div id="header-first" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">
							<div id="block-logo" class="block">
								<div class="content">
									<a href=""><img src="../img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->
							<div id="block-images-header" class="block">
								<div class="content">
									<a href=""><img src="../img/img-min.png" alt=""></a> <a
										href="/"><img src="../img/img-prosperidad.png" alt=""></a>
									<a href="/"><img src="../img/img-moore.png" alt=""></a> <a
										href="/"><img src="../img/img-patrimonio.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->
							<div id="block-top-menu" class="block block-menu">
								<div class="content">
									<div id="form-loguin-header" role="form">
										<div class="form-group">
											<label for="exampleInputEmail1"><%=msj.getString("home.registrado.bienvenido")%></label>
										</div>

										<div class="form-group">
											<label for="exampleInputPassword1">AAAAAA</label>
										</div>

										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath%>detallarNoticia.jsp?"><%=msj.getString("home.registrado.modificar")%></a></label>
										</div>
										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath%>detallarNoticia.jsp?"><%=msj.getString("home.registrado.cerrar")%></a></label>
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
												href="#">Home</a></span> <span class="item-breadcrumb separator"></span>
											<span class="item-breadcrumb separator"><a href="#">Lorem</a></span>
											<span class="item-breadcrumb separator"></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>

										<div class="menu-user">
											<ul class="items-menu">
												<li class="item-menu first">Usuario Activo</li>
												<li class="item-menu last active"><a href="">Cerrar
														Sesión</a></li>
											</ul>
										</div>

									</div>
									<!-- precontent -->
									<form id="generarReporteDescarga" name="generarGeporteDescarga" action="<%=basePath%>generarReporteDescargasServlet" method="post" onsubmit="validarRepDatasets()">
												<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.nomUsr")%></label>
															<input type="text" class="form-control" id="nomUsr"
																name="nomUsr" title="<%=msj.getString("descargas.nomUsrTitle")%>" placeholder="usuario_1">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.numDocUsr")%></label>
															<input type="text" class="form-control" id="numID"
																name="numID" onkeypress="return valideValNum(event)" title="<%=msj.getString("descargas.numDocUsrTitle")%>" placeholder="12345">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.tipoDocUsr")%></label>
															<div class="select-wrapper">
																<select name="tipoDocUsr" id="tipoDocUsr" size="7"
																	multiple="multiple" style="width: 300px;"
																	title="<%=msj.getString("descargas.tipoDocUsTitler")%>">
																	<%ArrayList<String> tiposDoc = CargaDatosInicial.getTiposDocumento();
																    int numtiposDoc=0;
																	for (int i = 0; i < tiposDoc.size(); i++) {
																		%><option value="<%=tiposDoc.get(i)%>"><%=tiposDoc.get(i)%></option><%
																				numtiposDoc++;
																	}
																	%>
																</select>
															</div>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.tipoDato")%></label>
															<div class="select-wrapper">
																<select name="tipoDato" id="tipoDato"
																	size="7" multiple="multiple" style="width: 300px;"
																	title="<%=msj.getString("descargas.tipoDatoTitle")%>">
																<%ArrayList<String> tipoDato = CargaDatosInicial.getTipoDato();
															    int numtipoDato=0;
																for (int i = 0; i < tipoDato.size(); i++) {
																	%><option value="<%=tipoDato.get(i)%>"><%=tipoDato.get(i)%></option><%
																	numtipoDato++;
																}
																%>
																</select>
															</div>
														</div>
														
														<h3 class="item-clear-both"><%=msj.getString("descargas.periodo")%>:</h3>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.fechaInicial")%>:</label> 
															<input id="fInicial" name="fInicial"
																onclick="return noKeyData(event);"
																title="<%=msj.getString("descargas.fechaInicialTitle")%>"
																value="">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.fechaFinal")%>:</label> 
															<input id="fFinal" name="fFinal"
																onclick="return noKeyData(event);"
																title="<%=msj.getString("descargas.fechaFinalTitle")%>" 
																value="">
														</div>

														<div class="form-actions">
														
														<input type="hidden" value="" name="dataSetsLista" id="dataSetsLista">
													    <input type="hidden" value="" name="dataSetsListaEncab" id="dataSetsListaEncab">
														<input type="button" onclick="filtraDatasets();" value="<%=msj.getString("descargas.filtrar")%>" title="<%=msj.getString("descargas.filtrar")%>">
														<input id="btnReporte" name="btnReporte" type="submit" value="<%=msj.getString("descargas.descargarXLS")%>" class="btn btn-default" title="<%=msj.getString("descargas.descargarXLSTitle")%>">
														<input id="btnReportepdf" name="btnReportepdf" type="submit" value="<%=msj.getString("descargas.descargarPDF")%>" class="btn btn-default" title="<%=msj.getString("descargas.descargarPDFTitle")%>">
														<input
														    id="btnCancelar" name="btnCancelar" type="button" value="<%=msj.getString("parametrizacion.cancelar")%>"
															onclick="location.href='<%=basePath%>reg/home.jsp';">
				
														</div>
													</div>

													<div id="resultados"> 
														<div id="consultas">

														</div>
														<div id="paginador" class="pager">
														
														</div>
													</div>
													
													</form>
													<!-- fin resultados -->
												
								</div>
								<!-- content-inner -->
							</div>
							<!-- /.content-->
						</div>
						<!-- /.section-wrapper-->
					</div>
					<!--/.main -->
					<!-- FOOTER -->
					<div id="postscript" class="section-wrapper">
						<div class="section">
							<div class="section-inner clearfix">
								<div class="links-interes">
									<img src="../img/gobierno.png" />
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
<!-- 									<div class="menu-servicios menu-postscript"> -->
<!-- 										<h3>Servicios de Cuidadanía</h3> -->
<!-- 										<ul> -->
<!-- 											<li><a href="">Visitas Casa de Nariño</a></li> -->
<!-- 											<li><a href="">Datos de contacto</a></li> -->
<!-- 											<li><a href="">Escríbale al Presidente</a></li> -->
<!-- 											<li><a href="">PSQR</a></li> -->
<!-- 											<li><a href="">Colombia Compra Eficiente</a></li> -->
<!-- 											<li><a href="">Avisos Convocatoria Pública</a></li> -->
<!-- 											<li><a href="">Notificaciones por Aviso</a></li> -->
<!-- 											<li><a href="">Notificaciones Judiciales</a></li> -->
<!-- 											<li><a href="">Proveedores</a></li> -->
<!-- 										</ul> -->
<!-- 									</div> -->
<!-- 									<div class="sistema-web-presidencia select-postscript"> -->
<!-- 										<h3>Sistema Web Presidencia</h3> -->
<!-- 										<div class="select-wrapper"> -->
<!-- 											<select> -->
<!-- 												<option>1</option> -->
<!-- 											</select> -->
<!-- 										</div> -->
<!-- 										<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 											<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 												class="form-submit"> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<div class="dependecias-presidencia select-postscript"> -->
<!-- 										<h3>Dependencias Presidencia</h3> -->
<!-- 										<div class="select-wrapper"> -->
<!-- 											<select> -->
<!-- 												<option>1</option> -->
<!-- 											</select> -->
<!-- 										</div> -->
<!-- 										<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 											<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 												class="form-submit"> -->
<!-- 										</div> -->
<!-- 									</div> -->
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
									<p>Carrera 10 No. 20-30 Bogotá DC. - PBX:(571)3527160 -
										Línea nacional 018000110012 - Pronóstico y Alertas (571)
										3527180</p>
									<p>Horario de Atención: Lunes a Viernes 8:00 AM a 4:00 PM</p>
								</div>
							</div>
							<!-- /.section-inner-->
						</div>
						<!--/.section -->
					</div>
					<!-- /.section-wrapper-->
				</div>
			</div>
			<!--/.page -->
		</div>
		
		
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