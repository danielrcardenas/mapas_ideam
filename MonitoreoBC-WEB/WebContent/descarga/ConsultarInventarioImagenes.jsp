<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>  
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.web.descarga.dao.*"%> 
<%@page import="co.gov.ideamredd.web.descarga.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.web.usuario.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

String id_usuario = "";
if (usuario != null) {
	id_usuario = String.valueOf(usuario.getIdUsuario());
}

String nombreLicencia = "IMAGENES";

%>


<%if(usuario != null)
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 144))
	{
%>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar Inventario de Imágenes</title>
<link rel="stylesheet" type="text/css" href="../css/jsDatePick_ltr.min.css" />
<script type="text/javascript" src="../js/jquery.1.4.2.js"></script>
<script type="text/javascript" src="../js/jsDatePick.jquery.min.1.3.js"></script>
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />

<link rel='stylesheet' type='text/css' media='all' href='../js/jscalendar/calendar-green.css' title='green' /> 
<script type='text/javascript' src='../js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='../js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='../js/jscalendar/calendar-setup.js'></script>
<script src="/MonitoreoBC-WEB/js/general.js"></script>
<script src="/MonitoreoBC-WEB/js/encodecode.js"></script>

<script type="text/javascript">
var datasetFiltradosLista="";
var datasetFiltradosListaEncab="";
var datasetsFiltrados = new Array();
var datasets = new Array();
var datasetTemp;
var titulos = new Array(13);
var posParametros;
var parametros;
var paramsTiposImgMap;
var paramsRes;
var paramsTipoDato;
var codigos;
var idActual;
titulos[0] = '<%=msj.getString("Identificador")%>';
titulos[1] = '<%=msj.getString("nombre")%>';
titulos[2] = '<%=msj.getString("formato")%>';
titulos[3] = '<%=msj.getString("fecha_de_adquisicion")%>';
titulos[4] = '<%=msj.getString("fecha_de_registro")%>';
titulos[5] = '<%=msj.getString("proyeccion")%>';
titulos[6] = '<%=msj.getString("numero_de_bandas")%>';
titulos[7] = '<%=msj.getString("detalle")%>';
titulos[8] = '<%=msj.getString("tipo_de_dato")%>';
titulos[9] = '<%=msj.getString("resolucion")%>';
titulos[10] = '<%=msj.getString("nivel_de_georreferenciacion")%>';
titulos[11] = '<%=msj.getString("tipo_de_imagen_o_mapa")%>';
titulos[12] = '<%=msj.getString("link_metadato")%>';
	   
$(document).ready(function () {
	cargarDatasets();
	cargarEncabezados();
	console.log("datasets cargados:", datasets.length);
	idActual=1;
	resultadosContactos(datasets.length, 'resultados','consultas');
	cargarDatos(datasets,'consultas',2,titulos,null,0,5);
	crearPaginas(datasets.length,'consultas',1);

	inicializarNavegador();

		$("#tiposImgMap").mousedown(function(e){
		    e.preventDefault();
		    var select = this;
		    var scroll = select .scrollTop;
		    e.target.selected = !e.target.selected;
		    setTimeout(function(){select.scrollTop = scroll;}, 0);
		    $(select).focus();
		   	$(select).trigger("change");
		}).mousemove(function(e){e.preventDefault()});		
		   
		$("#resolucion").mousedown(function(e){
		    e.preventDefault();
		    var select = this;
		    var scroll = select .scrollTop;
		    e.target.selected = !e.target.selected;
		    setTimeout(function(){select.scrollTop = scroll;}, 0);
		    $(select).focus();
		   	$(select).trigger("change");
		}).mousemove(function(e){e.preventDefault()});		
		   
		$("#tipoDato").mousedown(function(e){
		    e.preventDefault();
		    var select = this;
		    var scroll = select .scrollTop;
		    e.target.selected = !e.target.selected;
		    setTimeout(function(){select.scrollTop = scroll;}, 0);
		    $(select).focus();
		   	$(select).trigger("change");
		}).mousemove(function(e){e.preventDefault()});		
		   
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
		results.innerText = tamanho +" "+ '<%=msj.getString("consultaUsuarios.resultados")%>';
		results.innerHTML = tamanho +" "+ '<%=msj.getString("consultaUsuarios.resultados")%>';
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
		
		if(datasetsFiltrados && datasetsFiltrados.length) {
			resultadosContactos(datasetsFiltrados.length, 'resultados','consultas');
			cargarDatos(datasetsFiltrados,'consultas',2,titulos,null,indiceInicial,indiceFinal);
			crearPaginas(datasetsFiltrados.length,'consultas',boton);
		}
		else {
			resultadosContactos(datasets.length, 'resultados','consultas');
			cargarDatos(datasets,'consultas',2,titulos,null,indiceInicial,indiceFinal);
			crearPaginas(datasets.length,'consultas',boton);			
		}

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
		for (var j = 0; j < 13; j++) {
			var titulo = titulos[j];
			datasetFiltradosListaEncab = datasetFiltradosListaEncab + titulo + ",";
		}
		document.getElementById("dataSetsListaEncab").value=datasetFiltradosListaEncab; 
	}
	
	function filtraDatasets(){
		cargarDatasets();
		datasetsFiltrados.length=0;
		var cla = document.getElementById('palabraClave');
		var tim = document.getElementById('tiposImgMap');
		var res = document.getElementById('resolucion');
		var tda = document.getElementById('tipoDato');
		var fin = document.getElementById('fInicial');
		var ffi = document.getElementById('fFinal');
		//var control;
		//Obtener parametros
		parametros = new Array();
		
		paramsTiposImgMap = new Array();
		paramsRes = new Array();
		paramsTipoDato = new Array();
		
		codi = new Array();
		posParametros=0;

		var palabraClave= cla.value;
		obtenerParametrostim(tim,$('#tiposImgMap option:selected'));
		obtenerParametrosres(res, $('#resolucion option:selected'));
		obtenerParametrostda(tda,$('#tipoDato option:selected'));
		
		datasetsFiltrados=datasets;
		//if(palabraClave.length>0 && paramsTiposImgMap.length>0 && paramsRes.length>0 && paramsTipoDato.length>0){
			
		//	}
		//else{ 
			if(palabraClave.length>0){ 
				datasetsFiltrados=filtrarPalabraClave(datasets,palabraClave,1);
			}
			if(paramsTiposImgMap.length>0){
				datasetsFiltrados=filtrarListas(datasetsFiltrados,paramsTiposImgMap,11);
			}
			if(paramsRes.length>0){
				datasetsFiltrados=filtrarListas(datasetsFiltrados,paramsRes,9);
			}
			if(paramsTipoDato.length>0){
				datasetsFiltrados=filtrarListas(datasetsFiltrados,paramsTipoDato,8);
			}

			if (fin.value != '' || ffi.value != '') {					
				var dateObj = new Date();
				var month = dateObj.getUTCMonth() + 1; //months from 1-12
				var day = dateObj.getUTCDate();
				var year = dateObj.getUTCFullYear();
	
				var fecha_hoy = year + "" + pad(month) + "" + pad(day);
	
				var fecha_ini = fin.value.replaceAll("-", "");
				var fecha_fin = ffi.value.replaceAll("-", "");
	
				if (fecha_ini != '' && fecha_fin != '') {
					if (fecha_ini*1.0 > fecha_fin*1.0) {
						alert('La fecha inicial no puede ser posterior a la fecha final');
						return;
					}
				}
				
				if (fecha_ini*1.0 > fecha_hoy*1.0) {
					alert('La fecha inicial no puede ser posterior a la fecha de hoy');
					return;
				}
				
				if (fecha_fin*1.0 > fecha_hoy*1.0) {
					alert('La fecha final no puede ser posterior a la fecha de hoy');
					return;
				}
				
				datasetsFiltrados = filtroFechas(fin.value, ffi.value, datasetsFiltrados, 4);
			}
			idActual=1;
			resultadosContactos(datasetsFiltrados.length, 'resultados','consultas');
			cargarDatos(datasetsFiltrados,'consultas',2,titulos,null,0,5);
			crearPaginas(datasetsFiltrados.length,'consultas',1);
	//	resultadosContactos(datasetsFiltrados,'resultados', 'trDataset');
	//	$('#black').smartpaginator({ totalrecords: datasetsFiltrados.length, recordsperpage: 8, datacontainer: 'resultados', dataelement: 'div', initval: 0, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', theme: 'black' });
		
	//	$('#red').smartpaginator({ totalrecords: 32, recordsperpage: 4, length: 4, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', theme: 'red', controlsalways: true, onchange: function (newPage) {
	  //      $('#r'+ newPage);
	//    }
//	    });

	}

	String.prototype.replaceAll = function(search, replacement) {
	    var target = this;
	    return target.split(search).join(replacement);
	};

	function pad(n) {
	    return (n < 10) ? ("0" + n) : n;
	}
		
	function obtenerParametrostim(obj, selectedOpt){
		
		if(obj.value.length > 0){
			for(var j=0;j<selectedOpt.length;j++){
				paramsTiposImgMap[j]=selectedOpt[j].value;
			}
		}
	}
	function obtenerParametrosres(obj, selectedOpt){
		
		if(obj.value.length > 0){
			for(var j=0;j<selectedOpt.length;j++){
				paramsRes[j]=selectedOpt[j].value;
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
		var posFiltrados = 0;
		var i;
		var p;
		var fi = fecha1.split("-");
		var ff = fecha2.split("-");
		
		for(i=0; i<datasets.length; i++){
			p = datasets[i].split(',');
			var f = p[indice].split("/");
			var myDate = new Date(f[2], f[1]-1, f[0]);
 			var startDate = myDate;
 			if (fecha1 != '') startDate = new Date(fi[0], fi[1]-1, fi[2]);
 			var endDate = myDate; 
 			if (fecha2 != '') endDate = new Date(ff[0], ff[1]-1, ff[2]);

 			if (startDate <= myDate && myDate <= endDate) {
				datasetsTemp[posFiltrados] = datasets[i];
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
	
	function filtrarPalabraClave(datasets,palabra,posArreglo){
		var datasetsTemp = new Array();
		var posFiltrados;
		posFiltrados=0;
		var conte;
		for (var i = 0; i < datasets.length; i++) {
			conte = datasets[i].split(",");
			if((new RegExp(palabra)).test(conte[1])){
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
				// if((new RegExp(paramsLista[j])).test(conte[posArreglo])){
				if(paramsLista[j] == conte[posArreglo]) {
					datasetsTemp[posFiltrados]=datasets[i];
					posFiltrados++;
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

	function cargarDatasets(){
		
		<%ArrayList<String> DS = CargaDatosInicial.getDatasetsString();
		for (int i = 0; i < DS.size(); i++) {%>
			datasets[<%=i%>] = '<%=DS.get(i)%>';
		<%}%>

	}
	
	
</script>
</head>
<body class='sidebar-first front'>
		<div id="page">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja"><%=msj.getString("Inventario_de_Imagenes") %></h2>

							<div id="content">
								<div class="content-inner">

										<div class="block-gray block">
										<form id="generarReporte" name="generarGeporte" action="/MonitoreoBC-WEB/generarReporteServlet" method="post" onsubmit="validarRepDatasets()">
												<h2><%=msj.getString("formulario_de_consulta")%></h2>
												<div id="block-datos-basicos" class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
												<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.palabraclave")%></label>
															<input type="text" class="form-control" id="palabraClave"
																name="palabraClave" title="<%=msj.getString("descargas.palabraclaveTitle")%>" placeholder="imagen_1">
														</div>
														<div class="form-group"><label></label> </div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.tipoImagenMapa")%></label>
															<div class="select-wrapper">
																<select name="tiposImgMap" id="tiposImgMap" size="7"
																	multiple="multiple" style="width: 300px;"
																	title="<%=msj.getString("descargas.tipoImagenMapaTitle")%>">
																	<%ArrayList<String> tiposImgMap = CargaDatosInicial.getTipoImgMap();
																    int numtiposImgMap=0;
																	for (int i = 0; i < tiposImgMap.size(); i++) {
																		%><option value="<%=tiposImgMap.get(i)%>"><%=tiposImgMap.get(i)%></option><%
																		numtiposImgMap++;
																	}
																	%>
																</select>
															</div>
														</div>
															<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.resolucion")%></label>
															<div class="select-wrapper">
																<select name="resolucion" id="resolucion"
																	size="7" multiple="multiple" style="width: 300px;"
																	title="<%=msj.getString("descargas.resolucionTitle")%>">
																	<%ArrayList<String> resolucion = CargaDatosInicial.getResolucion();
																    int numresolucion=0;
																	for (int i = 0; i < resolucion.size(); i++) {
																		%><option value="<%=resolucion.get(i)%>"><%=resolucion.get(i)%></option><%
																		numresolucion++;
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
<%-- 															<input id="fInicial" name="fInicial" onclick="return noKeyData(event);" title="<%=msj.getString("descargas.fechaInicialTitle")%>" value=""> --%>
															<input id="fInicial" name="fInicial" title="<%=msj.getString("descargas.fechaInicialTitle")%>" value="" readonly>
															<script type='text/javascript'>
															Calendar.setup({
																inputField     :    'fInicial',				// id of the input field
																ifFormat       :    '%Y-%m-%d',	// format of the input field
																showsTime      :    false,					// will display a time selector
																button         :    'fInicial',				// trigger for the calendar (button ID)
																singleClick    :    false,					// double-click mode
																step           :    1						// show all years in drop-down boxes (instead of every other year as default)
															});
															</script>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("descargas.fechaFinal")%>:</label> 
<%-- 															<input id="fFinal" name="fFinal" onclick="return noKeyData(event);" title="<%=msj.getString("descargas.fechaFinalTitle")%>" value=""> --%>
															<input id="fFinal" name="fFinal" title="<%=msj.getString("descargas.fechaFinalTitle")%>" value="" readonly>
															<script type='text/javascript'>
															Calendar.setup({
																inputField     :    'fFinal',				// id of the input field
																ifFormat       :    '%Y-%m-%d',	// format of the input field
																showsTime      :    false,					// will display a time selector
																button         :    'fFinal',				// trigger for the calendar (button ID)
																singleClick    :    false,					// double-click mode
																step           :    1						// show all years in drop-down boxes (instead of every other year as default)
															});
															</script>
														</div>

														<div class="form-actions">
														
														<input type="hidden" value="" name="dataSetsLista" id="dataSetsLista">
													    <input type="hidden" value="" name="dataSetsListaEncab" id="dataSetsListaEncab">
														<input type="button" onclick="filtraDatasets();" value="<%=msj.getString("descargas.filtrar")%>" title="<%=msj.getString("descargas.filtrar")%>">
														<% if (!ConsultaWebUsuario.usuarioAceptoLicencia(Integer.parseInt(id_usuario), nombreLicencia)) { %>
															<div style="margin-top: 5px; border:1px solid white; border-radius: 3px; font-size: 13px;"><%=msj.getString("nota_licencia_descarga_recurso_prefijo")%> "<%=nombreLicencia %>" <%=msj.getString("nota_licencia_descarga_recurso_mediofijo")%> <a href="/MonitoreoBC-WEB/reg/modificarUsuario.jsp"><%=msj.getString("nota_licencia_descarga_recurso_postfijo")%></a></div>
														<% } else { %>
														<input id="btnReporte" name="btnReporte" type="submit" value="<%=msj.getString("descargas.descargarXLS")%>" class="btn btn-default" title="<%=msj.getString("descargas.descargarXLSTitle")%>">
														<input id="btnReportepdf" name="btnReportepdf" type="submit" value="<%=msj.getString("descargas.descargarPDF")%>" class="btn btn-default" title="<%=msj.getString("descargas.descargarPDFTitle")%>">
														<% } %>
														
														<input id="btnCancelar" name="btnCancelar" type="button" value="<%=msj.getString("parametrizacion.cancelar")%>"
															onclick="location.href='/MonitoreoBC-WEB/descarga/ConsultarInventarioImagenes.jsp';">
				
														</div>
													</div>
													</div>
											</form>
												</div>

									<div id="resultados"> 
										<div id="consultas">
										</div>
										<div id="paginador" class="pager">														
										</div>
									</div>
													
												
								</div>
							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									
							
							</div>
						</div>
							</div>
							
					<%=UI.getFooter(msj) %>									

				</div>
			</div>
			</div>
	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		</form>
		
	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										
	
</body>
</html> 


<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>