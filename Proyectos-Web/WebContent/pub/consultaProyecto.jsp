<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.entities.Depto"%>
<%@page import="co.gov.ideamredd.entities.Municipios"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="co.gov.ideamredd.dao.CargaInicialDatosProyectos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="../js/popUpUsuarios.js"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="../css/content.css" />
<link type="text/css" rel="stylesheet" href="../css/html.css" />
<script type="text/javascript" src="../js/datum-validation.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar Proyectos</title>
</head>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/";

String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	ArrayList<Noticias> noticias = CargaInicialDatosProyectos.getNoticias(); 
// 	String parametros =  Util.desencriptar(request.getParameter("id"));
// 	String [] p = parametros.split(";"); 
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
	if(request.getParameter("id")!=null)
	{
		usuario = UtilWeb.consultarUsuarioPorDoc(Integer
			.valueOf(Util.desencriptar(request.getParameter("id"))));
	}
	
%>
<script type="text/javascript">

$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });
  $(function() {
    $( "#fInicial" ).datepicker({
        dateFormat: "yy-mm-dd"
  	}); 
  });
  $(function() {
    $( "#ffin" ).datepicker({
        dateFormat: "yy-mm-dd"
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

var municipio;
var municipios = new Array();
var depto;
var deptos = new Array();
var proyectos = new Array();
var proyectos1 = new Array();
var codigos = new Array();
var titulos = new Array(11);
titulos[0] = '<%=msj.getString("consulta.proyecto.codigo")%>';
titulos[1] = '<%=msj.getString("consulta.proyecto.nombre")%>';
titulos[2] = '<%=msj.getString("consulta.proyecto.area")%>';
titulos[3] = '<%=msj.getString("consulta.proyecto.localizacion")%>';
titulos[4] = '';
titulos[5] = '';
titulos[6] = '';
titulos[7] = '<%=msj.getString("consulta.proyecto.bosque")%>';
titulos[8] = '<%=msj.getString("consulta.proyecto.car")%>';
titulos[9] = '<%=msj.getString("consulta.proyecto.estado")%>';
titulos[10] = '<%=msj.getString("consulta.proyecto.propietario")%>';
titulos[11] = '<%=msj.getString("consulta.proyecto.fechaInicio")%>';
titulos[12] = '<%=msj.getString("consulta.proyecto.fechaFin")%>';


var parFiltradas = new Array();
var parce; 
var parametros;
var codigos;
var ind;
var idForm = 0;
var idActual;

$(document).ready(function () {
	cargarProyectos();
	idActual=1;
	resultadosContactos(proyectos.length, 'resultados','consultas');
	cargarDatos(proyectos,'consultas',3,titulos,null,0,5);
	crearPaginas(proyectos.length,'consultas',1);
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

function cargarProyectos(){
	<%CargaInicialDatosProyectos.cargarProyectoConsulta();
	ArrayList<String> a = CargaInicialDatosProyectos.getProyectos();
	for (int i = 0; i < a.size(); i++) {%>
		proyectos[<%=i%>] = '<%=a.get(i)%>';
		codigos[<%=i%>] = '<%=CargaInicialDatosProyectos.getCodigos().get(i)%>';
<%}%>
	}

function cargarMunicipios(){
	<%ArrayList<Municipios> municipios = CargaInicialDatosProyectos.getArrayMunicipios();
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

function cargarDepto(){
	<%ArrayList<Depto> d1 = CargaInicialDatosProyectos.getArrayDeptos();
	  String de="";
		for (int i = 0; i < d1.size(); i++) {
			Depto depto1 = (Depto) d1.get(i);
			de = depto1.getConsecutivo() + ","
					+ depto1.getNombre();%>	
			depto = '<%=de%>';
		deptos[
<%=i%>
	] = depto;
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
	
	function cargarDatos(parc,contenedor, numColumnas, titulos, permisos, indiceInicial, indiceFinal){
		var i, j, conte;
		var cont = document.getElementById(contenedor);
		for (i=indiceInicial; i < parc.length; i++) {
			if(i<indiceFinal){
				var span1, span2, span3;
				var indiceColumna=1;
				var codigoProyecto;
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
						if(j!=4){
							strong.innerText = "<b>"+titulos[j]+":</b> "+ conte[j];
							strong.innerHTML = "<b>"+titulos[j]+":</b> "+conte[j];
						}else{//esto no en el general
							strong.innerText = conte[j]+","+conte[j+1]+","+conte[j+2];
							strong.innerHTML = conte[j]+","+conte[j+1]+","+conte[j+2];
							j=6;
						}
						p.appendChild(strong);
						span1.appendChild(p);
						div.appendChild(span1);
					}else{
						var p = document.createElement('p');
						var strong = document.createElement('strong');
						strong.innerText = "<b>"+titulos[j]+":</b> "+ conte[j];
						strong.innerHTML = "<b>"+titulos[j]+":</b> "+conte[j];
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
					adicionarBoton('detallar', span3, 'Detallar', 1, codigoProyecto);
// 					adicionarBoton('modificar', span3, 'Modificar', 2, codigoProyecto);
// 					adicionarBoton('exportar', span3, 'Exportar', 3, codigoProyecto);
// 					adicionarBoton('individuos', span3, 'Individuos', 4, codigoProyecto);
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
				enviar(opc,cod);
			}, true);
		} else {
			boton.attachEvent('onclick', function() {
				enviar(opc,cod);
			});
		}
		formulario.appendChild(hiddenIdParcela);
		formulario.appendChild(boton);
		contenedor.appendChild(formulario);
	}

	function enviar(opc, id) {
		var idParcela = document.getElementById('proyecto_hidden');
		idParcela.value = id;
		var usuario= document.getElementById("usuario");
		usuario.value='<%if(usuario!=null){out.print(usuario.getIdentificacion());}%>';
		var dir = document.getElementById('dir');
		dir.value = opc;
		var form = document.getElementById('consultaProyecto');
		form.submit();
	}
	
	var stringParametros;
	var indicesFiltro;
	function filtraParcelas() {
		parFiltradas.length = 0;
		parce = new Array();
		var control;
		parametros = new Array();
		codi = new Array();
		ind = 0;

		stringParametros="";
		indicesFiltro="";
		
		var cod = document.getElementById('codigo');
		obtenerParametros(cod);
		
		var nom = document.getElementById('nombre');
		obtenerParametros(nom);

		stringParametros=stringParametros+"-1;-1;";

		var pais = document.getElementById('pais');
		obtenerParametrosSelect(pais);

		var mun = document.getElementById('municipioInicial');
		obtenerParametrosSelect(mun);

		var dep = document.getElementById('deptoInicial');
		obtenerParametrosSelect(dep);
		
		var tibo = document.getElementById('tipoBosque');
		obtenerParametrosSelect(tibo);

		var car = document.getElementById('CAR');
		obtenerParametrosSelect(car);
		
		var est = document.getElementById('estado');
		obtenerParametrosSelect(est);

		stringParametros=stringParametros+"-1;";
		
		var fin = document.getElementById('fInicial');
		obtenerParametros(fin);
		
		var ffi = document.getElementById('ffin');
		stringParametros=stringParametros+ffi.value;
		
//  		var act = document.getElementById('actividades');
//  		obtenerParametrosSelect(act);

		indicesFiltro=obtenerIndicesFiltro();

		if(indicesFiltro != "")
		{
			parFiltradas=filtro(proyectos,codigos, indicesFiltro, stringParametros);
			
		 	if(parFiltradas.length!=0){//cambiar para los filtros 
				resultadosContactos(parFiltradas.length, 'resultados','consultas');
				cargarDatos(parFiltradas,'consultas',3,titulos,null,0,5);
				crearPaginas(parFiltradas.length,'consultas',1);
		 	}else{
		 		busquedaVacia('resultados','consultas');
		 	}
		}else{
			resultadosContactos(proyectos.length, 'resultados','consultas');
			cargarDatos(proyectos,'consultas',3,titulos,null,0,5);
			crearPaginas(proyectos.length,'consultas',1);
		}
	}

	function obtenerIndicesFiltro(){
		var result="";
		var auxParam=stringParametros.split(";");
		var i;
		for(i=0;i<auxParam.length;i++){
			if(auxParam[i] != "" && auxParam[i] !="-1")
			{
				result=result+i+";";
			}
		}
		result=result.substring(0,result.length-1);
		return result.split(";");
	}

	function obtenerParametros(obj) {

		stringParametros=stringParametros+obj.value+";";
		
	}

	function obtenerParametrosSelect(obj) {
		var i;
		for (i = 0; i < obj.options.length; i++)
			{
				if (obj.options[i].selected) 
				{
					stringParametros = stringParametros+obj.options[i].value+";";
				}
			}
	}

	function filtroFechas(fechaMenor, fechaMayor) {
		var fi = fechaMenor.substring(0,10).split("-");
		var ff = fechaMayor.substring(0,10).split("-");
		var resultado=0;
		var myDate1 = new Date(fi[0], fi[1], fi[2]);
		var myDate2 = new Date(ff[0], ff[1], ff[2]);
			
		if (myDate1<myDate2)
			resultado = 1;
		
		return resultado;
	}

	function filtro(proyectos,codigos, indicesFiltro, stringParametros) {
		var i,j,indice=0;
		var verdaderos=0;
		var auxProyecto;
		var resultados=new Array();
		var result=0;
		for (i = 0; i < codigos.length; i++) {
			
			auxProyecto=codigos[i].split(";");
			
			for(j=0;j<indicesFiltro.length;j++)
			{
				var val1=auxProyecto[indicesFiltro[j]].toLowerCase();
				var val2=stringParametros.split(";")[indicesFiltro[j]].toLowerCase();

				if(indicesFiltro[j]>=11 && indicesFiltro[j]<=12)
				{
					if(indicesFiltro[j]==11)
					{
						result=filtroFechas(val2,val1);
						if(result==1)
						{
							verdaderos++;
						}
					}
					if(indicesFiltro[j]==12)
					{
						result=filtroFechas(val1,val2);
						if(result==1)
						{
							verdaderos++;
						}
					}
					
				}else{
					if(val1==val2)
					{
						verdaderos++;
					}
				}
			}
			
			if(verdaderos==indicesFiltro.length)
			{
				resultados[indice]=proyectos[i];
				indice++;
			}
			verdaderos=0;
		}

		return resultados;
	}

	function cargarMunicipiosDepto() {
		var txtSelectedValuesObj = document.getElementById('municipioInicial');
		var selectedArray = new Array();
		var selObj = document.getElementById('deptoInicial');
		var i, j, m = 0;
		municipios = cargarMunicipios();
		if (txtSelectedValuesObj.options.length != 0) {
			while (m != txtSelectedValuesObj.options.length) {
				txtSelectedValuesObj.remove(m);
			}
			var option = document.createElement("option");
			option.text = "Seleccione";
			option.value= "-1";
			txtSelectedValuesObj.add(option);
		}
		for (i = 0; i < selObj.options.length; i++) {
			for (j = 0; j < municipios.length; j++) {
				var m = municipios[j].split(",");
				if (m[0].length < 5)
					var d = m[0].substring(0, 1);
				else
					var d = m[0].substring(0, 2);
				if (selObj.options[i].selected)
					if (d == selObj.options[i].value) {
						opcion = document.createElement('option');
						texto = document.createTextNode(m[1]);
						opcion.appendChild(texto);
						opcion.id='muni'+m[0];
						opcion.value = m[0];
						txtSelectedValuesObj.appendChild(opcion);
					}
			}
		}
	}
</script>
<body class='sidebar-first front' onload="postEdit(document)" onmouseover="popUpAyudaAux(document)">
	<form method="post" action="<%=basePath%>redireccion"
		id="consultaProyecto">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<input type="hidden" name="proyecto_hidden" id="proyecto_hidden" /> 
		<input type="hidden" name="dir" id="dir" />
		<input type="hidden" name="usuario" id="usuario" />
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
<!-- 											<span class="item-breadcrumb separator"><a href="#">Lorem</a></span> -->
<!-- 											<span class="item-breadcrumb separator">></span> <span -->
<!-- 												class="item-breadcrumb active">Ipsum</span> -->
<!-- 										</div> -->
<!-- 									</div> -->
									<!-- precontent -->

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
												<h3>Consulta de Proyectos REDD</h3>
												<div id="block-datos-basicos">
													<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.codigo")%></label>
															<input type="text" class="form-control" id="codigo"
																name="codigo" >
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.nombre")%></label>
															<input type="text" class="form-control" id="nombre"
																name="nombre">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.bosque")%></label>
															<div class="select-wrapper">
																<select id="tipoBosque" name="tipoBosque">
																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaInicialDatosProyectos.getTipoBosque()%>
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
																	<%=CargaInicialDatosProyectos.getPaises()%>
																</select>
															</div>
														</div>												
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.depto")%></label>
															<div class="select-wrapper">
																<select name="deptoInicial" id="deptoInicial"
																	onchange="cargarMunicipiosDepto()"
																	title="Departamento(s) donde se encuentra ubicada la parcela">
																	<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaDatosSelect.getDepartamentos()%>
																</select>
															</div>
														</div>
														
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.mcipio")%></label>
															<div class="select-wrapper">
																<select name="municipioInicial" id="municipioInicial"
																	title="Municipios disponibles para la busqueda">
																	<option value="-1"><%=msj.getString("crea.noticias.seleccion")%></option>
																</select>
															</div>
														</div>
														
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.car")%></label>
															<div class="select-wrapper">
																<select name="CAR" id="CAR">
																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaInicialDatosProyectos.getCAR()%>
																</select>
															</div>
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.estado")%></label>
															<div class="select-wrapper">
																<select name="estado" id="estado">
																<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option>
																	<%=CargaInicialDatosProyectos.getEstado()%>
																</select>
															</div>
														</div>
<!-- 														<div class="form-group"> -->
<%-- 															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.actividades")%></label> --%>
<!-- 															<div class="select-wrapper"> -->
<!-- 																<select name="actividades" id="actividades"> -->
<%-- 																	<option value="-1" selected="selected"><%=msj.getString("crea.noticias.seleccion")%></option> --%>
<%-- 																	<%=CargaInicialDatosProyectos.getActividad()%> --%>
<!-- 																</select> -->
<!-- 															</div> -->
<!-- 														</div> -->
														<h3 class="item-clear-both">Proyectos establecidos en el periodo de tiempo:</h3>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaInicio") %>:</label> <input
																type="text" id="fInicial" name="fInicial">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.fechaFin")%>:</label> <input
																type="text" id="ffin" name="ffin">
														</div>
														
														<div class="form-actions">
															<input type="button"
																value="<%=msj.getString("consulta.proyecto.filtro")%>"
																onclick="filtraParcelas();">
															<input type="button" value="Regresar"
																	onclick="javascript:history.back()">
<!-- 															<input type="button" -->
<!-- 																value="Reportes" -->
<!-- 																onclick="enviar(4,0);"> -->
														</div>
													</div>

													<div id="resultados">
														<div id="consultas">

														</div>
														<div id="paginador" class="pager">
														
														</div>
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