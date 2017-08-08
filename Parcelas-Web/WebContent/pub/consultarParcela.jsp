<%@page import="co.gov.ideamredd.dao.CargaParcelasConsulta"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicialParcelas"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="co.gov.ideamredd.entities.Depto"%>
<%@page import="co.gov.ideamredd.entities.Municipios"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>  
<%@page import="java.util.ArrayList"%> 
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />
<script type="text/javascript" src="../js/manejo-listas.js"></script>
<script type="text/javascript" src="../js/download.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar parcelas</title>
</head>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";

String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	ArrayList<Noticias> noticias = CargaDatosInicialParcelas.getNoticias(); 
// 	String parametros =  Util.desencriptar(request.getParameter("id"));
// 	String [] p = parametros.split(";");
	ArrayList<Noticias> eventos = CargaDatosInicialParcelas.getEventos();
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
	}else
	if(i18n.getLenguaje() == null)
	{
		i18n.setLenguaje("es");
		i18n.setPais("CO");
	}
	ResourceBundle msj = i18n.obtenerMensajeIdioma();
	
//Parte1: para que el log in del usuario salga bien	
    Usuario usuario =null;
	//usuario.getIdUsuario();
	try{
	if(request.getParameter("id")!=null)
	{
	usuario = UtilWeb.consultarUsuarioPorDoc(Integer
	.valueOf(Util.desencriptar(request.getParameter("id"))));
	
	request.getSession().setAttribute("usuarioAux", usuario);
	}
	}catch(Exception e){
		usuario=null;
	}
//Parte1: hasta aqui fin
	
	request.getSession().setAttribute("i18n", msj);
%>
<script type="text/javascript">

<%-- <%System.out.println("Usuario: "+Util.desencriptar(request.getParameter("id")));%> --%>

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

  var reporte=0;
  var municipio;
  var municipios = new Array();
  var depto;
  var deptos = new Array();
  var parFiltradas = new Array();
  var parcelas = new Array();
  var codigos = new Array();
  var parce; 
  var parametros;
  var codigos;
  var ind;
  var idForm = 0;
  var idActual;
  var titulos = new Array(10);
  var paramReporte;
  var parFiltradasR = new Array();
  var parcelasR = new Array();
  titulos[0] = 'Código';
  titulos[1] = 'Nombre';
  titulos[2] = 'Fecha';
  titulos[3] = '';
  titulos[4] = '';
  titulos[5] = '';
  titulos[6] = 'CAR';
  titulos[7] = 'Fuente generadora';
  titulos[8] = 'Tipo de bosque';
  titulos[9] = 'Tipo de inventario';

$(document).ready(function () {
	cargarParcelas();
	idActual=1;
	resultadosContactos(parcelas.length, 'resultados','consultas');
	cargarDatos(parcelas,'consultas',3,titulos,null,0,5);
	crearPaginas(parcelas.length,'consultas',1);
    $('#genReporte').click(function(event) {
        if(reporte==2){
        	alert('No hay elementos para generar el reporte');
        }else if(reporte==0){
        	for(k=0;k<parcelas.length;k++){
        		parcelasR[k]=parcelas[k].replace(/,/g,";");
        	}
        	$('#idsParcela').val(parcelasR); 
        	$('#paramReporte').val($('#codigo:checked').val()+";"+$('#nombre:checked').val()+";"+$('#codigoCampo:checked').val()+";"+$('#tipoParcela:checked').val()+";"+$('#fecha:checked').val()+";"+$('#aprov:checked').val()+";"+$('#tipoInventario:checked').val()
        	+";"+$('#descripcion:checked').val()+";"+$('#proposito:checked').val()+";"+$('#observaciones:checked').val()+";"+$('#archivo:checked').val()+";"+$('#geometria:checked').val()+";"+$('#puntos:checked').val()+";"+$('#pais:checked').val()
        	+";"+$('#departamento:checked').val()+";"+$('#municipio:checked').val()+";"+$('#fgda:checked').val()+";"+$('#propietario:checked').val()+";"+$('#custodio:checked').val()+";"+$('#otrosConts:checked').val()+";"+$('#biomasa:checked').val()+";"+$('#individuos:checked').val());
        	$.post('<%=basePath%>exportarReporte',{p:$('#idsParcela').val(),param:$('#paramReporte').val()},function(filename) { 
        		$.fileDownload(filename)
        	    .done(function () { alert('File download a success!'); })
        	    .fail(function () { alert('File download failed!'); });
            });
        }else{
        	for(k=0;k<parFiltradas.length;k++){
        		parFiltradasR[k]=parFiltradas[k].replace(/,/g,";");
        	}
        	$('#idsParcela').val(parFiltradasR); 
        	$('#paramReporte').val($('#codigo:checked').val()+";"+$('#nombre:checked').val()+";"+$('#codigoCampo:checked').val()+";"+$('#tipoParcela:checked').val()+";"+$('#fecha:checked').val()+";"+$('#aprov:checked').val()+";"+$('#tipoInventario:checked').val()
                	+";"+$('#descripcion:checked').val()+";"+$('#proposito:checked').val()+";"+$('#observaciones:checked').val()+";"+$('#archivo:checked').val()+";"+$('#geometria:checked').val()+";"+$('#puntos:checked').val()+";"+$('#pais:checked').val()
                	+";"+$('#departamento:checked').val()+";"+$('#municipio:checked').val()+";"+$('#fgda:checked').val()+";"+$('#propietario:checked').val()+";"+$('#custodio:checked').val()+";"+$('#otrosConts:checked').val()+";"+$('#biomasa:checked').val()+";"+$('#individuos:checked').val());
        	$.post('<%=basePath%>exportarReporte',{p:$('#idsParcela').val(),param:$('#paramReporte').val()},function(filename) { 
        		document.location=filename;
            });
        }
    	
    });
});

function busquedaVacia(resultados, consultas){
	var cont = document.getElementById(resultados);
	var cons = document.getElementById(consultas);
	if (cons != null)
		cons.parentNode.removeChild(cons);
	cons = document.createElement('div');
	cons.id = consultas;
	cons.className = 'resultados-busqueda';
	var div= document.createElement('div');
	div.className = 'item-busqueda item-busqueda-odd';
	var span1 = document.createElement('span');
	span1.id="columna";
	span1.className= "column column1";
	var p = document.createElement('p');
	var strong = document.createElement('strong');
	strong.innerText = '<%=msj.getString("consulta.vacia")%>';
	strong.innerHTML = '<%=msj.getString("consulta.vacia")%>';
	p.appendChild(strong);
	span1.appendChild(p);
	div.appendChild(span1);
	cons.appendChild(div);
	cont.appendChild(cons);
}

function cargarMunicipios(){
	<%ArrayList<Municipios> municipios = CargaDatosInicialParcelas.getArrayMunicipios();
			for (int i = 0; i < municipios.size(); i++) {
				Municipios municipio = (Municipios) municipios.get(i);
				String m = municipio.getConsecutivo() + ","
						+ municipio.getNombre() + ","
						+ municipio.getDepartamento();%>     
					   municipio = '<%=m%>';
	                municipios[<%=i%>]=municipio;
	<%}%>
			return municipios;
	} 

	function cargarParcelas(){
		<%ArrayList<String> a = CargaParcelasConsulta.cargarResumenParcelaConsulta();
		for (int i = 0; i < a.size(); i++) {%>
			parcelas[<%=i%>] = '<%=a.get(i)%>';
			codigos[<%=i%>] = '<%=CargaParcelasConsulta.codigos.get(i)%>';
		<%}%>
		}

	function cargarDepto(){
		<%ArrayList<Depto> d1 = CargaDatosInicialParcelas.getArrayDeptos();
		  String de="";
				for (int i = 0; i < d1.size(); i++) {
					Depto depto1 = (Depto) d1.get(i);
					de = depto1.getConsecutivo() + ","
							+ depto1.getNombre();%>	
					depto = '<%=de%>';
				deptos[<%=i%>] = depto;
		<%}%>
		return deptos;
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
		prin.appendChild(cont);
	}

	function crearPaginas(tamanho, contenedor, id) {
		var i;
		var cont = document.getElementById(contenedor);
		var paginador = document.getElementById('paginador');
		if (paginador != null)
			paginador.parentNode.removeChild(paginador);
		paginador = document.createElement('div');
		paginador.id = 'paginador';
		paginador.className = "pager";
		var ul = document.createElement('ul');
		var numeroHojas = Math.ceil(tamanho / 5);

		var liInicial = document.createElement('li');
		var aInicial = document.createElement('a');
		var liAnterior = document.createElement('li');
		var aAnterior = document.createElement('a');
		var liSiguiente = document.createElement('li');
		var aSiguiente = document.createElement('a');
		var liFinal = document.createElement('li');
		var aFinal = document.createElement('a');

		liInicial.className = "item-pager-controls item-pager-first";
		liInicial.onclick = function() {
			paginar((1));
		};
		liAnterior.className = "item-pager-controls item-pager-previous";
		liAnterior.onclick = function() {
			if (idActual > 1) {
				paginar(idActual - 1);
			}
		};
		liSiguiente.className = "item-pager-controls item-pager-next";
		liSiguiente.onclick = function() {
			if (idActual < numeroHojas) {
				paginar(idActual + 1);
			}
		};
		liFinal.className = "item-pager-controls item-pager-last";
		liFinal.onclick = function() {
			paginar((numeroHojas));
		};

		liInicial.appendChild(aInicial);
		liAnterior.appendChild(aAnterior);
		liSiguiente.appendChild(aSiguiente);
		liFinal.appendChild(aFinal);

		ul.appendChild(liInicial);
		ul.appendChild(liAnterior);
		for (i = 0; i < numeroHojas; i++) {
			crearBoton(i, id, ul);
		}
		ul.appendChild(liSiguiente);
		ul.appendChild(liFinal);

		paginador.appendChild(ul);
		cont.appendChild(paginador);
	}

	function paginar(boton) {
		idActual = boton;
		var indiceFinal = boton * 5;
		var indiceInicial = indiceFinal - 5;
		resultadosContactos(parcelas.length, 'resultados', 'consultas');
		cargarDatos(parcelas, 'consultas', 3, titulos, null, indiceInicial,
				indiceFinal);
		crearPaginas(parcelas.length, 'consultas', boton);
	}

	function crearColumnas(numColumnas, contResults) {
		var i;
		for (i = 0; i < numColumnas; i++) {
			var span = document.createElement('span');
			span.id = "columna" + (i + 1);
			span.class = "column column" + (i + 1);
			contResults.appendChild(span);
		}
	}

	function crearBoton(indice, id, ul) {
		var li = document.createElement('li');
		if (indice == (id - 1)) {
			li.className = "item-pager active";
			li.innerText = indice + 1;
			li.innerHTML = indice + 1;
		} else {
			li.className = "item-pager";
			var a = document.createElement('a');
			a.id = indice + 1;
			a.innerText = indice + 1;
			a.innerHTML = indice + 1;
			a.onclick = function() {
				paginar((indice + 1));
			};
			li.appendChild(a);
		}
		ul.appendChild(li);
	}

	function cargarDatos(parc, contenedor, numColumnas, titulos, permisos,
			indiceInicial, indiceFinal) {
		var i, j, conte;
		var cont = document.getElementById(contenedor);
		for (i = indiceInicial; i < parc.length; i++) {
			if (i < indiceFinal) {
				var span1, span2, span3;
				var indiceColumna = 1;
// 				var codigoProyecto;
				var div = document.createElement('div');
				if (i % 2 == 0)
					div.className = 'item-busqueda item-busqueda-odd';
				else
					div.className = 'item-busqueda item-busqueda-even';
				conte = parc[i].split(",");
				span1 = document.createElement('span');
				span1.id = "columna" + indiceColumna;
				span1.className = "column column" + indiceColumna;
				indiceColumna++;
				span2 = document.createElement('span');
				span2.id = "columna" + indiceColumna;
				span2.className = "column column" + indiceColumna;
				for (j = 0; j < conte.length; j++) {
					if (j == 0)
						codigoProyecto = conte[j];
					if (j < 5) {
						var p = document.createElement('p');
						var strong = document.createElement('strong');
						if (j != 3) {
							strong.innerText = titulos[j] + ": " + conte[j];
							strong.innerHTML = titulos[j] + ": " + conte[j];
						} else {//esto no en el general
							strong.innerText = conte[j] + "," + conte[j + 1]
									+ "," + conte[j + 2];
							strong.innerHTML = conte[j] + "," + conte[j + 1]
									+ "," + conte[j + 2];
							j = 5;
						}
						p.appendChild(strong);
						span1.appendChild(p);
						div.appendChild(span1);
					} else {
						var p = document.createElement('p');
						var strong = document.createElement('strong');
						strong.innerText = titulos[j] + ": " + conte[j];
						strong.innerHTML = titulos[j] + ": " + conte[j];
						p.appendChild(strong);
						span2.appendChild(p);
						div.appendChild(span2);
					}
				}
				if (numColumnas == 3) {
					var k;
					indiceColumna++;
					span3 = document.createElement('span');
					span3.id = "columna" + indiceColumna;
					span3.className = "column column" + indiceColumna;
					// 				for(k=0;k<permisos.length;k++){}Cuando se tengan los permisos 
					adicionarBoton('detallar', span3, 'Detallar', 1, conte[0]);
					adicionarBoton('modificar', span3, 'Modificar', 2, conte[0]);
					
					<%if(usuario!=null)
					{%>
					adicionarBoton('individuos', span3, 'Individuos', 5, conte[0]);
					adicionarBoton('importacion', span3, 'Importación', 6, conte[0]);
					<%}%>
					// 					adicionarBoton('exportar', span3, 'Exportar', 3, conte[0]);
					// 					adicionarBoton('individuos', span3, 'Individuos', 4, conte[0]);
					div.appendChild(span3);
				}
				cont.appendChild(div);
			}
		}
	}

	function adicionarBoton(idBoton, contenedor, valor, opc, cod) {
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
				enviar(opc, cod);
			}, true);
		} else {
			boton.attachEvent('onclick', function() {
				enviar(opc, cod);
			});
		}
		formulario.appendChild(hiddenIdParcela);
		formulario.appendChild(boton);
		contenedor.appendChild(formulario);
	}

	function enviar(opc, id) {
		var idParcela = document.getElementById('parcela_hidden');
		idParcela.value = id;
		var dir = document.getElementById('dir');
		dir.value = opc;
		<%if(usuario!=null){%>
		var user = document.getElementById('id_usuario');
		user.value = '<%=usuario.getIdUsuario() %>';
		<%}%>
		var lenguaje = document.getElementById('lenguaje');
		lenguaje.value = '<%=i18n.getLenguaje() %>';
		var form = document.getElementById('consultaParcela');
		form.submit();
	}

	function filtraParcelas() {
		parFiltradas.length = 0;
		parce = new Array();
		var control;
		parametros = new Array();
		codi = new Array();
		ind = 0;

		var cod = document.getElementById('codigo');
		var nom = document.getElementById('nombre');
		var pai = document.getElementById('pais');
		var tin = document.getElementById('tipoInventario');
		var dep = document.getElementById('deptoInicial');
		var mun = document.getElementById('municipioInicial');
		var car = document.getElementById('CAR');
		var fgd = document.getElementById('FGDA');
		var tbo = document.getElementById('tipoBosque');
		var fin = document.getElementById('fInicial');
		var ffi = document.getElementById('ffin');

		obtenerParametros(cod, 0);
		obtenerParametros(nom, 1);
		obtenerParametros(pai, 3);
		obtenerParametros(dep, 4);
		obtenerParametros(mun, 5);
		obtenerParametros(car, 6);
		obtenerParametros(fgd, 7);
		obtenerParametros(tbo, 8);
		obtenerParametros(tin, 9);

		if (parametros.length != 0 || fin.value != '' || ffi.value != '') {
			for (control = 0; control < parametros.length; control++) {
				if (parFiltradas.length == 0) {
					parFiltradas = filtro(parametros[control], parcelas,
							codi[control], codigos);
				} else {
					parFiltradas = filtro(parametros[control], parFiltradas,
							codi[control], parce);
				}
			}
			if (fin.value != '' && ffi.value != '') {
				if (parFiltradas.length == 0) {
					parFiltradas = filtroFechas(fin.value, ffi.value, parcelas,
							11, 12);
				} else {
					parFiltradas = filtroFechas(fin.value, ffi.value,
							parFiltradas, 11, 12);
				}
			}
			if (parFiltradas.length != 0) {
				reporte=1;
				resultadosContactos(parFiltradas.length, 'resultados',
						'consultas');
				cargarDatos(parFiltradas, 'consultas', 3, titulos, null, 0, 5);
				crearPaginas(parFiltradas.length, 'consultas', 1);
			} else {
				reporte=2;
				busquedaVacia('resultados', 'consultas');
			}
		} else {
			reporte=0;
			resultadosContactos(parcelas.length, 'resultados', 'consultas');
			cargarDatos(parcelas, 'consultas', 3, titulos, null, 0, 5);
			crearPaginas(parcelas.length, 'consultas', 1);
		}
	}

	function obtenerParametros(obj, cod) {
		var indice;
		if (obj.value != "") {
			if (obj.type == 'select-one') {
				if (obj.value != "-1") {
					for (indice = 0; indice < obj.options.length; indice++) {
						if (obj.options[indice].selected) {
							parametros[ind] = obj.value;
							codi[ind] = cod;
							ind++;
						}
					}
				}
			} else {
				parametros[ind] = obj.value;
				codi[ind] = cod;
				ind++;
			}
		}
	}

	function filtroFechas(fecha1, fecha2, arreglo, indiceInicial, indiceFinal) {
		var i;
		var p;
		var index = 0;
		var fi = fecha1.split("/");
		var ff = fecha2.split("/");
		var parcels = new Array();
		for (i = 0; i < arreglo.length; i++) {
			p = arreglo[i].split(';');
			var f1 = p[indiceInicial].split("-");
			var f2 = p[indiceFinal].split("-");
			var myDate1 = new Date(f1[0], f1[1], f1[2].substring(0, 2));
			var myDate2 = new Date(f2[0], f2[1], f2[2].substring(0, 2));
			var startDate = new Date(fi[2], fi[1], fi[0]);
			var endDate = new Date(ff[2], ff[1], ff[0]);
			if ((startDate < myDate1 && myDate1 < endDate)
					&& (startDate < myDate2 && myDate2 < endDate)) {
				parcels[index] = arreglo[i];
				index++;
			}
		}
		// 	if(parcels.length==0)
		// 		parcels = arreglo;
		return parcels;
	}

	function filtro(param, arreglo, indice, cods) {
		var i;
		var index = 0;
		var parcels = new Array();
		var parCodigos = new Array();
		for (i = 0; i < arreglo.length; i++) {
			var p = arreglo[i].split(';');
			c = cods[i].split(',');
			if (indice == 1 || indice == 7) {
				if ((c[indice].toUpperCase().search(param.toUpperCase())) >= 0) {
					parcels[index] = arreglo[i];
					parCodigos[index] = c[i];
					index++;
				}
			} else {
				if (param == c[indice]) {
					parcels[index] = arreglo[i];
					parCodigos[index] = cods[i];
					index++;
				}
			}
		}
		return parcels;
	}
	

	function exportarReporte() {
		window.scrollTo(0, 0);
		document.getElementById("fondoBloqueo").style.display = "block";
		document.getElementById("popup-exportarParcelas").style.display = "block";
	}

	function cerrarReporte() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popup-exportarParcelas").style.display = "none";
	}
	
	function enviarReporte(){
		var par="";
		var ops;
		for(k=0;k<parcelas.length;k++){
    		parcelasR[k]=parcelas[k].replace(/,/g,";");
    		if(par==""){
    			par=parcelasR[k];
    		}else{
    			par=par+","+parcelasR[k];
    		}
    	}
    	document.getElementById("idsParcela").value=par;
    	ops=$('#codigo:checked').val()+";"+$('#nombre:checked').val()+";"+$('#codigoCampo:checked').val()+";"+$('#tipoParcela:checked').val()+";"+$('#fecha:checked').val()+";"+$('#aprov:checked').val()+";"+$('#tipoInventario:checked').val()
    	+";"+$('#descripcion:checked').val()+";"+$('#proposito:checked').val()+";"+$('#observaciones:checked').val()+";"+$('#archivo:checked').val()+";"+$('#geometria:checked').val()+";"+$('#puntos:checked').val()+";"+$('#pais:checked').val()
    	+";"+$('#departamento:checked').val()+";"+$('#municipio:checked').val()+";"+$('#fgda:checked').val()+";"+$('#propietario:checked').val()+";"+$('#custodio:checked').val()+";"+$('#otrosConts:checked').val()+";"+$('#biomasa:checked').val()+";"+$('#individuos:checked').val();
		document.getElementById("paramReporte").value=ops;
    	document.getElementById("exportarReporte").action="<%=basePath%>exportarReporte";
		document.getElementById("exportarReporte").method="post";
		document.getElementById("exportarReporte").submit();
	}
	
	function registrarParcela(){
		window.location = '<%=basePath%>reg/registrarParcela.jsp';
	}
	
    
</script>
<body onload="cargarMunicipios();cargarDepto();"
	class='sidebar-first front'>
<%-- 	<h1><%=usuario.getIdUsuario() %></h1>  --%>
	<div id="fondoBloqueo"
		style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; display: none;">
		<!--Sin contenido -->
	</div>
	<div id="popup-exportarParcelas"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 700px; display: none;">
		<form id="exportarReporte" name="reporte">
		<div id="cboxContent">
			<div id="cboxTitle"><%=msj.getString("consulta.exporta.titulo")%></div>
			<div id="cboxLoadedContent">
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("consulta.exporta.selDatos")%></label>
					<input type="hidden" id="idsParcela" name="idsParcela">
					<input type="hidden" id="paramReporte" name="paramReporte">
					
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">*Organizaci&oacute;n: </label> <input
						type="checkbox" class="form-control" onclick="todos(this);"><%=msj.getString("consulta.exporta.selTodos")%>
				</div>
				<div class="form-group">
					<input type=checkbox class="form-control" name="codigo" id="codigo"><%=msj.getString("consulta.parcela.codigo")%>
				</div>
				<div class="form-group">
					<input type=checkbox class="form-control" name="nombre" id="nombre"><%=msj.getString("consulta.parcela.nombre")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="codigoCampo" id="codigoCampo"><%=msj.getString("consulta.exporta.campo")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="tipoParcela" id="tipoParcela"><%=msj.getString("consulta.parcela.tipo")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="fecha" id="fecha"><%=msj.getString("consulta.parcela.fechaEstablecimiento")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="aprov" id="aprov"><%=msj.getString("consulta.parcela.aprov")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="tipoInventario" id="tipoInventario"><%=msj.getString("consulta.parcela.inventario")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="descripcion" id="descripcion"><%=msj.getString("consulta.parcela.descripcion")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="proposito" id="proposito"><%=msj.getString("consulta.parcela.proposito")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="observaciones" id="observaciones"><%=msj.getString("consulta.parcela.observaciones")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="archivo" id="archivo"><%=msj.getString("consulta.parcela.archivo")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="geometria" id="geometria"><%=msj.getString("detalle.geometria")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="puntos" id="puntos"><%=msj.getString("consulta.exporta.valGeom")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="pais" id="pais"><%=msj.getString("consulta.proyecto.pais")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="departamento" id="departamento"><%=msj.getString("consulta.proyecto.depto")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="municipio" id="municipio"><%=msj.getString("consulta.proyecto.mcipio")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="fgda" id="fgda"><%=msj.getString("consulta.parcela.fgda")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="propietario" id="propietario"><%=msj.getString("consulta.exporta.propietario")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="custodio" id="custodio"><%=msj.getString("consulta.exporta.custodio")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="otrosConts" id="otrosConts"><%=msj.getString("consulta.exporta.otros")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="biomasa" id="biomasa"><%=msj.getString("consulta.exporta.bio")%>
				</div>
				<div>
					<input type=checkbox class="form-control" name="individuos" id="individuos"><%=msj.getString("consulta.exporta.ind")%>
				</div>
			</div>
			<br> <input type="button" value="Volver"
					onclick="cerrarReporte()">
			<br> <input type="button" id="genReporte" value="Aceptar" onclick="enviarReporte();"> 
		</div>
		</form>
	</div>


	<form method="post" action="<%=basePath%>redireccion"
		id="consultaParcela">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina"> <input type="hidden" name="parcela_hidden" id="parcela_hidden" /> 
		<input type="hidden" name="dir" id="dir" />
		<input type="hidden" name="id_usuario" id="id_usuario" />
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
							<!--Parte2:Desde aqui se copia para que aparezca bien el ligin del usuario  -->
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
						<%if(usuario!=null){%>	
						 <div id="form-loguin-header"  role="form">
                            <div class="form-group">
                              <label for="exampleInputEmail1"><%=msj.getString("home.registrado.bienvenido") %></label>
                            </div>

                            <div class="form-group">
                              <label for="exampleInputPassword1"><%=usuario.getNombre()%></label>
                            </div>

                            <div class="form-group">
                            <label for="exampleInputEmail1"><a href="<%=basePath2%>Usuario-Web/reg/modificarUsuario.jsp?id=<%=Util.encriptar(usuario.getIdentificacion())%>">
                            <%=msj.getString("home.registrado.modificar")%></a></label>
                            </div>
                            <div class="form-group">
                            <label for="exampleInputEmail1"><a href="<%=basePath2%>MonitoreoBiomasaCarbono/limpiarSesionServlet"><%=msj.getString("home.registrado.cerrar")%></a></label>
                            </div>
                          </div>
                          <ul class="social-menu item-list">
										<li class="menu-item facebook"><a></a></li>
										<li class="menu-item en"><a onclick="lenguaje(2);"></a></li>
										<li class="menu-item es"><a onclick="lenguaje(1)"></a></li>
						  </ul>
                          <%}else{%>
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
												onclick="enviarForms()"></input>
										</div>

									</div>

								</div>
								<ul class="social-menu item-list">
										<li id="icoAyuda" class="menu-item help first"
											onclick="popUpAyudaOpen()"
											style="margin-right: 10px; cursor: pointer;"><a></a></li>
										
										<li class="menu-item en" style="cursor: pointer;"><a
											onclick="lenguaje(2);"></a></li>
										<li class="menu-item es" style="cursor: pointer;"><a
											onclick="lenguaje(1)"></a></li>
									</ul>
                          <%}%>
						<!--Parte2:Hasta aqui se copia para que aparezca bien el ligin del usuario  -->	
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
											<li class="item-noticia first"><img src="../img/"
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
											<span class="item-breadcrumb separator"><a href="#">Lorem</a></span>
											<span class="item-breadcrumb separator">></span> <span
												class="item-breadcrumb active">Ipsum</span>
										</div>
									</div>
									<!-- precontent -->

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
												<h3><%=msj.getString("titulos.datosBasicos")%></h3>
												<div id="block-datos-basicos">
													<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.codigo")%></label>
															<input type="text" class="form-control" id="codigo"
																name="codigo" placeholder="Parcela_1"
																title="Codigo asignado para la parcela">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.nombre")%></label>
															<input type="text" class="form-control" id="nombre"
																name="nombre" placeholder="Parcela_1"
																title="Nombre con el que se registro la parcela">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.fgda")%></label>
															<input type="text" class="form-control" id="FGDA"
																name="FGDA" placeholder="Parcela_1"
																title="Nombre de la Fuente Generadora de Datos Ambientales designada para la parcela">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.inventario")%></label>
															<div class="select-wrapper">
																<select id="tipoInventario" name="tipoInventario"
																	title="Tipo de inventario aplicado a la parcela">
																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaDatosInicialParcelas.getTipoInventario()%>
																</select>
															</div>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.pais")%></label>
															<div class="select-wrapper">
																<select name="pais" id="pais"
																	title="País donde figura registrada la parcela"
																	onchange="cargarDeptos(deptos,deptoInicial);eliminarlista(municipioInicial);eliminarlista(municipioFinal);">
																	<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaDatosInicialParcelas.getPaises()%>
																</select>
															</div>
														</div>


														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.car")%></label>
															<div class="select-wrapper">
																<select name="CAR" id="CAR">
																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaDatosInicialParcelas.getCAR()%>
																</select>
															</div>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.depto")%></label>
															<div class="select-wrapper">
																<select name="deptoInicial" id="deptoInicial" size="7"
																	multiple="multiple" style="width: 300px;"
																	onchange="eliminarlista(municipioInicial);completarLista(deptoInicial, municipioInicial);"
																	title="Departamento(s) donde se encuentra ubicada la parcela">
																</select>
															</div>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.mcipio")%></label>
															<div class="select-wrapper">
																<select name="municipioInicial" id="municipioInicial"
																	size="7" multiple="multiple" style="width: 300px;"
																	title="Municipios disponibles para la busqueda">
																</select>
															</div>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.bosque")%></label>
															<div class="select-wrapper">
																<select id="tipoBosque" name="tipoBosque">
																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaDatosInicialParcelas.getTipoBosque()%>
																</select>
															</div>
														</div>
														<h3 class="item-clear-both"><%=msj.getString("consulta.parcela.tiempo")%></h3>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaInicio")%>:</label>
															<input type="text" id="fInicial" name="fInicial">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaFin")%>:</label>
															<input type="text" id="ffin" name="ffin">
														</div>
														<div class="form-actions">
															<input type="button"
																value="<%=msj.getString("consulta.proyecto.filtro")%>"
																onclick="filtraParcelas();"> <input
																type="button"
																value="<%=msj.getString("consulta.proyecto.exportar")%>"
																onclick="exportarReporte();">
																<input
																type="button"
																value="Registrar"
																onclick="registrarParcela();">
														</div>
													</div>

													<div id="resultados">
														<div id="consultas"></div>
														<div id="paginador" class="pager"></div>
													</div>
													<!-- fin resultados -->
												</div>
												<!-- block datos basicos -->
											</div>
											<!--  accordeon -->
										</div>
									</div>
									<!-- block resultados busqueda -->
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
</body>
</html>