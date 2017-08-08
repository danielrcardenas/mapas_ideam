<%@page import="co.gov.ideamredd.ui.dao.CargaDatosInicialHome"%> 
<%@page import="co.gov.ideamredd.entities.Depto"%>
<%@page import="co.gov.ideamredd.ui.entities.Noticias"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.ui.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosSelect"%> 
<%@page import="co.gov.ideamredd.entities.Municipios"%> 
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<meta charset="utf-8" />
<title>Monitoreo de Bosques y Carbono</title>
<%
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/";

    String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	ArrayList<Noticias> noticias = CargaDatosInicialHome.getNoticiasHome(); 
	ArrayList<Noticias> eventos = CargaDatosInicialHome.getEventosHome();
	request.getSession().setAttribute("noticia", noticias);
	request.getSession().setAttribute("eventos", eventos);
	
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
	
	String errorRegistro, errorRol;
	try {
		errorRegistro = (String) session.getAttribute("errorRegistro");
		session.setAttribute("errorRegistro", "No");
	} catch (Exception e) {
		errorRegistro = "No";
	}
	try {
		errorRol = (String) session.getAttribute("errorRol");
		session.setAttribute("errorRol", "No");
	} catch (Exception e) {
		errorRol = "No";
	}
	
	Usuario usuario =null;
	if(request.getParameter("id")!=null)
	{
	usuario = UtilWeb.consultarUsuarioPorDoc(Integer
	.valueOf(Util.desencriptar(request.getParameter("id"))));
	
	request.getSession().setAttribute("usuarioAux", usuario);
	}else{
		usuario = (Usuario)request.getSession().getAttribute("usuarioAux");
	}
	
	ArrayList<co.gov.ideamredd.entities.Usuario> usuarios = (ArrayList<co.gov.ideamredd.entities.Usuario>) session.getAttribute("usuarios");
	String roles = (String) CargaDatosSelect.rolesUsuario();
%>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/html.css"
	media="all" />
<script type="text/javascript" src="../js/datum-validation.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<!-- <link rel="stylesheet" -->
<!-- 	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"> -->
<script>

  var mouseX=0;
  var mouseY=0;

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

    function coordenadas(event) {
	  x=event.clientX;
	  y=event.clientY;
	   
	  document.getElementById("x").value = x;
	  document.getElementById("y").value = y;
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

	function enviarForms() {
		var nombre = document.getElementById("logName").value;
		var pass = document.getElementById("logPassword").value;

		document.getElementById("hidUsername").value = nombre;
		document.getElementById("hidPassword").value = pass;
		document.getElementById("j_username").value = nombre;
		document.getElementById("j_password").value = pass;

		document.getElementById("formRegistra").submit();
		document.getElementById("j_security_check").submit();
	}

	function takeCoordenadas(event) {
		mouseX = event.clientX;
		mouseY = event.clientY;
	}

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

	function enviarFormRC() {
		document.getElementById("email").value = document
				.getElementById("auxEmail").value;
		if (validar()) {
			document.getElementById("formRecordarClave").submit();
		}
	}

	function getAbsoluteElementPosition(element) {
		  if (typeof element == "string")
		    element = document.getElementById(element);
		    
		  if (!element) return { top:0,left:0 };
		  
		  var y = 0;
		  var x = 0;
		  while (element.offsetParent) {
		    x += element.offsetLeft;
		    y += element.offsetTop;
		    element = element.offsetParent;
		  }
		  return {top:y,left:x};
	}

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

//*************************USUARIOS**************************************
        var tipoPersonaLista = null;
	    var nombreLista = null;
	    var tipoIdenLista = null;
		var documentoLista = null;
		var paisLista = null;
		var departamentoLista = null;
		var ciudadLista = null;
		var direccionLista = null;
		var telefonoLista = null;
		var movilLista = null;
		var emailLista = null;
		var licenciaLista = null;
        var datUsuarios = null;
        var esActivo=null;
		
	<%try{%>
		
	<%if(usuarios != null)
	   {%>
	   tipoPersonaLista = Array(<%=usuarios.size()%>);
	   nombreLista = Array(<%=usuarios.size()%>);
	   tipoIdenLista = Array(<%=usuarios.size()%>);
	   documentoLista = Array(<%=usuarios.size()%>);
	   paisLista = Array(<%=usuarios.size()%>);
	   departamentoLista = Array(<%=usuarios.size()%>);
	   ciudadLista = Array(<%=usuarios.size()%>);
	   direccionLista = Array(<%=usuarios.size()%>);
	   telefonoLista = Array(<%=usuarios.size()%>);
	   movilLista = Array(<%=usuarios.size()%>);
	   emailLista = Array(<%=usuarios.size()%>);
	   licenciaLista = Array(<%=usuarios.size()%>);
	   datUsuarios = Array(<%=usuarios.size()%>);
	   esActivo= Array(<%=usuarios.size()%>);
	   userId= Array(<%=usuarios.size()%>);
	   userRol= Array(<%=usuarios.size()%>);
	   
	<%for(int i=0;i<usuarios.size();i++)
		{%>
		tipoPersonaLista[<%=i%>] = '<%=usuarios.get(i).getNombreTipoPersona()%>'; 
	    nombreLista[<%=i%>] = '<%=usuarios.get(i).getNombre()%>';
	    tipoIdenLista[<%=i%>] = '<%=usuarios.get(i).getNombreTipoIdenti()%>';
		documentoLista[<%=i%>] = '<%=usuarios.get(i).getIdentificacion()%>';
		paisLista[<%=i%>] = '<%if(usuarios.get(i).getPais() != null)
		                    {out.print(usuarios.get(i).getNombrePais());}
		                    else{out.print("No registra");}%>';
		departamentoLista[<%=i%>] = '<%if(usuarios.get(i).getDepto() != null)
	                                 {out.print(usuarios.get(i).getNombreDepto());}
	                                 else{out.print("No registra");}%>';
		ciudadLista[<%=i%>] = '<%if(usuarios.get(i).getMunicipio() != null)
	                             {out.print(usuarios.get(i).getNombreCiudad());}
	                             else{out.print("No registra");}%>';
		direccionLista[<%=i%>] = '<%=usuarios.get(i).getDireccion()%>';
		telefonoLista[<%=i%>] = '<%=usuarios.get(i).getTelefonoOficina()%>';
		movilLista[<%=i%>] = '<%if(usuarios.get(i).getCelular() != null)
	                            {out.print(usuarios.get(i).getCelular());}
	                            else{out.print("No registra");}%>';
		emailLista[<%=i%>] = '<%=usuarios.get(i).getCorreoElectronico()%>';
		licenciaLista[<%=i%>] = '<%if(usuarios.get(i).getNombresLicencias()!= "")
	                            {out.print(usuarios.get(i).getNombresLicencias());}
	                            else{out.print("Ninguna");}%>';
	    esActivo[<%=i%>] = '<%=usuarios.get(i).getActivo()%>';
	    userId[<%=i%>] = '<%=usuarios.get(i).getIdUsuario()%>';

	    auxDatosUsuarios = "";
	    auxDatosUsuarios = documentoLista[<%=i%>] + ";" +
	                       tipoIdenLista[<%=i%>] + ";" +
	                       nombreLista[<%=i%>] + ";" +
	                      '<%=ConsultaWebUsuario.consultarUltimoIngreso(usuarios.get(i).getIdUsuario())%>';
	                       datUsuarios[<%=i%>]= auxDatosUsuarios;
	    userRol[<%=i%>]= '<%=usuarios.get(i).getRolId()%>';
		
		<%}%>
	 <%}%>
	 <%}catch(Exception e){
			//control variables nulas
	}%>
	

		function todos(chkbox) {
			for ( var i = 0; i < document.forms[0].elements.length; i++) {
				var elemento = document.forms[0].elements[i];
				if (elemento.type == "checkbox")
					elemento.checked = chkbox.checked;
			}
		}

		function detallar(docUsuario) {
			window.scrollTo(0,0);
			document.getElementById("fondoBloqueo").style.display = "block";
			document.getElementById("popUpDetallar").style.display = "block";
			
			if (documentoLista != null) {
				for (i = 0; i < documentoLista.length; i++) {
					if(documentoLista[i]==docUsuario)
					{
						document.getElementById("detTipoPersona").value=tipoPersonaLista[i];
						document.getElementById("detNombre").value=nombreLista[i];
						document.getElementById("detTipoIdentificacion").value=tipoIdenLista[i];
						document.getElementById("detNumeroIdentificacion").value=documentoLista[i];
						document.getElementById("detPais").value=paisLista[i];
						document.getElementById("detDepartamento").value=departamentoLista[i];
						document.getElementById("detCiudad").value=ciudadLista[i];
						document.getElementById("detDireccion").value=direccionLista[i];
						document.getElementById("detTelefono").value=telefonoLista[i];
						document.getElementById("detMovil").value=movilLista[i];
						document.getElementById("detEmail").value=emailLista[i];
						document.getElementById("detLicencias").value=licenciaLista[i];
						break;
					}
				}
			}
		}

		function desactivaDetallar() {
			document.getElementById("fondoBloqueo").style.display = "none";
			document.getElementById("popUpDetallar").style.display = "none";
		}

		function asignarRol(docUsuario) {
			window.scrollTo(0,0);
			document.getElementById("fondoBloqueo").style.display = "block";
			document.getElementById("popUpAsignarRol").style.display = "block";
			document.getElementById("guardaRol").title=docUsuario;	
			
			if (documentoLista != null) {
				for (i = 0; i < documentoLista.length; i++) {
					if(documentoLista[i]==docUsuario)
					{
						document.getElementById("asrolNombre").value=nombreLista[i];
						break;
					}
				}
			}

			var aux1 = document.getElementById("rolHid"+docUsuario).value;
			document.getElementById("chkRol"+aux1).selected=true;
		}

		function guardaAsignarRol(docUsuario) {
			var idUsAux = document.getElementById("userHid"+docUsuario).value;
			var selectedRol = document.getElementById("selectRol").selectedIndex+1;

			document.getElementById("idRolCambioRol").value = selectedRol;
			document.getElementById("idUsuarioCambioRol").value = idUsAux;

			document.getElementById("formCambioRol").submit();		
		}

		function desactivaAsignarRol() {
			document.getElementById("fondoBloqueo").style.display = "none";
			document.getElementById("popUpAsignarRol").style.display = "none";
		}

		function activarUs(idUsuario) {
			document.getElementById("idUsuario").value=idUsuario;
			document.getElementById("tipoOperacion").value=1;
			document.getElementById("formActivacion").submit();
		}

		function desactivarUs(idUsuario) {
			document.getElementById("idUsuario").value=idUsuario;
			document.getElementById("tipoOperacion").value=0;
			document.getElementById("formActivacion").submit();
		}

		function validarModificar() {
			var isChecked = false;
			if (document.getElementById("selusuarios").checked)
				isChecked = true;
			if (isChecked)
				document.forms[2].submit();
			else
				alert('Seleccione un resultado para modificar');
		}
		function validarDesactivar() {
			var isChecked = false;
			if (document.getElementById("selusuarios").checked)
				isChecked = true;
			if (isChecked)
				document.forms[3].submit();
			else
				alert('Seleccione un resultado para desactivar/activar');
		}


	  //************************************

	var municipio;
	var municipios = new Array();
	var depto;
	var deptos = new Array();
	var codigos = new Array();
	var titulos = new Array(11);
	titulos[0] = '<%=msj.getString("consultaUsuarios.q.identificacion")%>';
	titulos[1] = '<%=msj.getString("consultaUsuarios.q.tipoIdenti")%>';
	titulos[2] = '<%=msj.getString("consultaUsuarios.q.nombre")%>';
	titulos[3] = '<%=msj.getString("consultaUsuarios.q.ultimoIngreso")%>';
	titulos[4] = '';
	titulos[5] = '';
	titulos[6] = '';
	titulos[7] = '';
	titulos[8] = '';
	titulos[9] = '';
	titulos[10] = '';
	titulos[11] = '';
	titulos[12] = '';

	var parFiltradas = new Array();
	var parce; 
	var parametros;
	var codigos;
	var ind;
	var idForm = 0;
	var idActual;

	$(document).ready(function () {
		//cargarProyectos();
		idActual=1;
		if(datUsuarios != null)
		if(datUsuarios.length!=0){//cambiar para los filtros 
		resultadosContactos(datUsuarios.length, 'resultados','consultas');
		cargarDatos(datUsuarios,'consultas',3,titulos,null,0,5);
		crearPaginas(datUsuarios.length,'consultas',1);
		}else{
			busquedaVacia('resultados','consultas');
		}
	});

	function busquedaVacia(resultados, consultas){
		var cont = document.getElementById(resultados);
		var cons = document.getElementById(consultas);
//	 	if (cont != null)
//	 		cont.parentNode.removeChild(cont);
//	 	cont = document.createElement('div');
//	 	cont.id = resultados;
//	 	cont.className = 'resultados-busqueda';
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
		strong.innerText = '<%=msj.getString("consultaUsuarios.noResultados")%>';
		strong.innerHTML = '<%=msj.getString("consultaUsuarios.noResultados")%>';
		p.appendChild(strong);
		span1.appendChild(p);
		div.appendChild(span1);
		cons.appendChild(div);
		cont.appendChild(cons);
	}

		function resultadosContactos(tamanho, principal, contenedor) {
			var cont;
//	 		var indiceColumna = 1;
//	 		var span;
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
			resultadosContactos(datUsuarios.length, 'resultados','consultas');
			cargarDatos(datUsuarios,'consultas',3,titulos,null,indiceInicial,indiceFinal);
			crearPaginas(datUsuarios.length,'consultas',boton);
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
						if(j<5){
							var p = document.createElement('p');
							var strong = document.createElement('strong');
							if(j!=4){
								strong.innerText = titulos[j]+": "+ conte[j];
								strong.innerHTML = titulos[j]+": "+conte[j];
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
						adicionarBoton3('<%=msj.getString("consultaUsuarios.detallar")%>',
								 span3, '<%=msj.getString("consultaUsuarios.detallar")%>', 1, i);
						if(userRol[i]!="7")
						adicionarBoton3('<%=msj.getString("consultaUsuarios.asignaRol")%>',
								 span3, '<%=msj.getString("consultaUsuarios.asignaRol")%>', 2, i);
						
						if(esActivo[i]=="1")
						{
						if(userRol[i]!="7")
						adicionarBoton2('<%=msj.getString("consultaUsuarios.desactivar")%>', 
								span3, '<%=msj.getString("consultaUsuarios.desactivar")%>', 3, i, esActivo[i]);
						}
						else{
						if(userRol[i]!="7")
						adicionarBoton2('<%=msj.getString("consultaUsuarios.activar")%>',
								 span3, '<%=msj.getString("consultaUsuarios.activar")%>', 3, i, esActivo[i]);
						}
						
						div.appendChild(span3);
					}
					cont.appendChild(div);
				}
			}
		}
		
		function adicionarBoton(idBoton, contenedor, valor, opc) {
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
			formulario.appendChild(hiddenIdParcela);
			formulario.appendChild(boton);
			contenedor.appendChild(formulario);
		}

		function adicionarBoton2(idBoton, contenedor, valor, opc, indice, esActivo) {
			var formulario = document.createElement("div");
			formulario.className = "form-actions";
			var boton = document.createElement('input');
			boton.type = "button";
			boton.id = 'boton' + idBoton;
			boton.name = 'boton' + idBoton;
			boton.className = "btn btn-default";
			boton.value = idBoton;

			boton.id = userId[indice].toString();
			boton.name = userId[indice].toString();
			
			if(esActivo=="1")
				boton.onclick = function(){ desactivarUs(boton.id); };
			else
				boton.onclick = function(){ activarUs(boton.id); };
			
			formulario.appendChild(boton);
			contenedor.appendChild(formulario);
		}

		function adicionarBoton3(idBoton, contenedor, valor, opc, indice) {
			var formulario = document.createElement("div");
			formulario.className = "form-actions";
			var boton = document.createElement('input');
			var hidden1 = document.createElement('input');
			var hidden2 = document.createElement('input');
			boton.type = "button";
			boton.className = "btn btn-default";
			boton.value = idBoton;
			hidden1.type= "hidden";
			hidden2.type= "hidden";
			hidden1.value= userId[indice];
			hidden2.value= userRol[indice];
			hidden1.id= "userHid" +  documentoLista[indice];
			hidden2.id= "rolHid" + documentoLista[indice];
			hidden1.name= "userHid" + documentoLista[indice];
			hidden2.name= "rolHid" + documentoLista[indice];

			boton.id = documentoLista[indice].toString();
			boton.name = documentoLista[indice].toString();
			
			if(valor=="Detallar")
			{
				boton.onclick = function(){ detallar(boton.id); };
			}
			else
			{
				boton.onclick = function(){ asignarRol(boton.id); }; 
			}

			formulario.appendChild(hidden1);
			formulario.appendChild(hidden2);
			formulario.appendChild(boton);
			contenedor.appendChild(formulario);
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
				c = cods[i].split(';');
				if (indice == 1 || indice == 7) {
					if ((p[indice].search(param)) > 0) {
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

		function borrarArchivos() {
			document.getElementById('formBorrar').submit();
		}

		function popUpUsuariosAux() {
			var coords=getAbsoluteElementPosition(document.getElementById("icoUsuarios"));
			
			document.getElementById("popUpUsuarios").style.left= coords.left + "px";
			document.getElementById("popUpUsuarios").style.top= coords.top + 15 + "px";
		}

		function popUpUsuariosOpen() {
			var coords=getAbsoluteElementPosition(document.getElementById("icoUsuarios"));
			
			document.getElementById("popUpUsuarios").style.left= coords.left + "px";
			document.getElementById("popUpUsuarios").style.top= coords.top + 15 + "px";
			document.getElementById("popUpUsuarios").style.display = "block";
		}

		function popUpUsuariosClose() {
			document.getElementById("popUpUsuarios").style.display = "none";
		}

		function formConsultarUsuario(){
			document.getElementById("documento").value=document.getElementById("documentoAux").value;
			document.getElementById("tipodoc").value=document.getElementById("tipodocAux").value;
			document.getElementById("nombre").value=document.getElementById("nombreAux").value;
			
			document.getElementById("formConsulta").submit();
		}

</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);" onmouseover="popUpUsuariosAux()">
	<form id="home" action="<%=basePath%>idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
		<div id="page"
			style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 1000px;">
			<div id="header">
<input type="hidden">
				<div id="header-first" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">

							<div id="block-logo" class="block">
								<div class="content">
									<a href="<%=basePath2%>MonitoreoBiomasaCarbono/home.jsp"><img
										src="../img/logo.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-images-header" class="block">
								<div class="content">
									<a href="http://www.minambiente.gov.co/web/index.html"><img
										src="../img/img-min.png" alt=""></a> <a
										href="http://wsp.presidencia.gov.co/portal/Paginas/default.aspx"><img
										src="../img/img-prosperidad.png" alt=""></a> <a
										href="http://www.moore.org/"><img
										src="../img/img-moore.png" alt=""></a> <a
										href="http://www.patrimonionatural.org.co/"><img
										src="../img/img-patrimonio.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-top-menu" class="block block-menu">

								<div class="content">
									<div id="form-loguin-header"  role="form">
                            <div class="form-group">
                              <label for="exampleInputEmail1"><%=msj.getString("home.registrado.bienvenido") %></label>
                            </div>

                            <div class="form-group">
                              <label for="exampleInputPassword1"><%=usuario.getNombre()%></label>
                            </div>

                            <div class="form-group">
                            <div class="form-group">
                            <label id="icoUsuarios" for="exampleInputEmail1" onclick="popUpUsuariosOpen()"
                                 style="margin-right: 10px; cursor: pointer; margin-left: 5px"><a><%=msj.getString("logOn.admin.Usuarios")%>▼ </a></label>
                            </div>
                            </div>
                            <div class="form-group">
                            <label for="exampleInputEmail1"><a href="<%=basePath2%>MonitoreoBiomasaCarbono/limpiarSesionServlet">
                            <%=msj.getString("home.registrado.cerrar")%></a></label>
                            </div>
                          </div>
                          <ul class="social-menu item-list">
                            <!-- Aca estaba lo de twitter -->
                            <li class="menu-item facebook"><a></a></li>
                            <li class="menu-item en"><a onclick="lenguaje(2);"></a></li>
                            <li class="menu-item es"><a onclick="lenguaje(1)"></a></li>
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

									<div id="block-noticias" class="block-gray block">
										
											<h2><%=msj.getString("home.noticias")%></h2>
											
									</div>


								</div>
								<!-- /.section-inner-->
							</div>
							<!-- /.sidebar-wrapper-->

							<div id="content">
								
								
								
								
								<div class="content-inner">

									<div id="block-accordeon-resultados-busqueda" class="block" style="width: 700px;">
										<div class="content">
												<div id="accordion">
												<h3><%=msj.getString("consultaUsuarios.consultaUsuarios")%></h3>
												<div id="block-datos-basicos">
													<div class="form-datos-parcela form-columnx2" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1">
 															<%=msj.getString("consultaUsuarios.NoIdentificacion")%>
 															</label>
															<input type="text" class="form-control" name="documentoAux" id="documentoAux"
															 onkeypress="return valideValNum(event)">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1">
															<%=msj.getString("consultaUsuarios.Nombre")%>
														</label>
															<input type="text" name="nombreAux" id="nombreAux" class="form-control" >
														</div>
														<div class="form-group">
														<label for="exampleInputEmail1">
														<%=msj.getString("consultaUsuarios.TipoIdenti")%>
														</label>
															<div class="select-wrapper">
																<select name="tipodocAux" id="tipodocAux">
																	<%=CargaDatosSelect.getTipoDocumento()%>
																</select>
															</div>
														</div>
														<div class="form-group">
															<br>
														</div>
<!-- 														<h3 class="item-clear-both">Proyectos establecidas en -->
<!-- 															el periodo de tiempo:</h3> -->
<!-- 														<div class="form-group"> -->
<!-- 															<label for="exampleInputEmail1">Fecha inicial:</label> <input -->
<!-- 																type="text" id="datepicker" name="fInicial"> -->
<!-- 														</div> -->
<!-- 														<div class="form-group"> -->
<!-- 															<label for="exampleInputEmail1">Fecha final:</label> <input -->
<!-- 																type="text" id="datepicker" name="ffin"> -->
<!-- 														</div> -->
														<div class="form-actions">
															<input type="button" class="btn btn-default btn-default"
																value="<%=msj.getString("consultaUsuarios.consultar")%>"
																onclick="formConsultarUsuario()">
																<input type="button"
												value="<%=msj.getString("consultaUsuarios.volver")%>"
												class="btn btn-default"
												onclick="javascript:history.back(1);">
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
						<!-- /.section-inner-->
					</div>
					<!--/.section -->
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
									<li><a class="vicepresidencia"
										href="http://www.vicepresidencia.gov.co/Paginas/Vicepresidencia-Colombia.aspx">Vicepresidencia</a></li>
									<li><a class="min-justicia"
										href="http://www.minjusticia.gov.co/">MinJusticia</a></li>
									<li><a class="min-defensa"
										href="http://www.mindefensa.gov.co/irj/portal/Mindefensa">MinDefensa</a></li>
									<li><a class="min-interior"
										href="http://www.mininterior.gov.co/">MinInterior</a></li>
									<li><a class="min-relaciones"
										href="http://www.cancilleria.gov.co/">MinRelaciones</a></li>
									<li><a class="min-hacienda"
										href="http://www.minhacienda.gov.co/HomeMinhacienda">MinHacienda</a></li>
									<li><a class="min-minas"
										href="http://www.minminas.gov.co/mme/">MinMinas</a></li>
									<li><a class="min-comercio"
										href="http://www.mincit.gov.co/">MinComercio</a></li>
									<li><a class="min-tic"
										href="http://www.mintic.gov.co/portal/604/w3-channel.html">MinTIC</a></li>
									<li><a class="min-cultura"
										href="http://www.mincultura.gov.co/Paginas/default.aspx">MinCultura</a></li>
									<li><a class="min-agricultura"
										href="https://www.minagricultura.gov.co/Paginas/inicio.aspx">MinAgricultura</a></li>
									<li><a class="min-ambiente"
										href="http://www.minambiente.gov.co/web/index.html">MinAmbiente</a></li>
									<li><a class="min-transporte"
										href="https://www.mintransporte.gov.co/">MinTransporte</a></li>
									<li><a class="min-vivienda"
										href="http://www.minvivienda.gov.co/SitePages/Ministerio%20de%20Vivienda.aspx">MinVivienda</a></li>
									<li><a class="min-educacion"
										href="http://www.mineducacion.gov.co/1621/w3-channel.html">MinEducación</a></li>
									<li><a class="min-trabajo"
										href="http://www.mintrabajo.gov.co/">MinTrabajo</a></li>
									<li><a class="min-salud"
										href="http://www.minsalud.gov.co/Paginas/default.aspx">MinSalud</a></li>
								</ul>
							</div>
<!-- 							<div class="menu-servicios menu-postscript"> -->
<!-- 								<h3>Servicios de Cuidadanía</h3> -->
<!-- 								<ul> -->
<!-- 									<li><a >Visitas Casa de Nariño</a></li> -->
<!-- 									<li><a >Datos de contacto</a></li> -->
<!-- 									<li><a >Escríbale al Presidente</a></li> -->
<!-- 									<li><a >PSQR</a></li> -->
<!-- 									<li><a >Colombia Compra Eficiente</a></li> -->
<!-- 									<li><a >Avisos Convocatoria Pública</a></li> -->
<!-- 									<li><a >Notificaciones por Aviso</a></li> -->
<!-- 									<li><a >Notificaciones Judiciales</a></li> -->
<!-- 									<li><a >Proveedores</a></li> -->
<!-- 								</ul> -->
<!-- 							</div> -->
<!-- 							<div class="sistema-web-presidencia select-postscript"> -->
<!-- 								<h3>Sistema Web Presidencia</h3> -->
<!-- 								<div class="select-wrapper"> -->
<!-- 									<select> -->
<!-- 										<option>1</option> -->
<!-- 									</select> -->
<!-- 								</div> -->
<!-- 								<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 									<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 										class="form-submit"> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<div class="dependecias-presidencia select-postscript"> -->
<!-- 								<h3>Dependencias Presidencia</h3> -->
<!-- 								<div class="select-wrapper"> -->
<!-- 									<select> -->
<!-- 										<option>1</option> -->
<!-- 									</select> -->
<!-- 								</div> -->
<!-- 								<div class="form-actions form-wrapper" id="edit-actions"> -->
<!-- 									<input type="submit" id="edit-submit" name="op" value="Ir" -->
<!-- 										class="form-submit"> -->
<!-- 								</div> -->
<!-- 							</div> -->
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
								Sostenible de Colombia. Sistema Nacional Ambiental. <a >atencionalciudadano@ideam.gov.co</a>
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
	<form action="<%=basePath%>consultarUsuarioServlet" method="post"
			name="formConsulta" id="formConsulta">
			<input type="hidden" name="documento" id="documento">
		    <input type="hidden" name="tipodoc" id="tipodoc">
		    <input type="hidden" name="nombre" id="nombre">
	</form>

	<form method="post" action="<%=basePath%>registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /> <input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>
	
	<div id="popUpUsuarios"
		style="z-index: 3; position: absolute; width: 140px; height:100px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;"><label for="exampleInputEmail1" style="text-align: center; color: white; font-weight: bold;">
		<%=msj.getString("logOn.admin.Usuarios")%></label>
		</div>
       <label for="exampleInputEmail1"><a href="<%=basePath2%>Usuarios_Web/admin/consultarUsuarios.jsp?id=<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
       *<%=msj.getString("logOn.admin.consultarUsuarios")%></a></label>
       <label for="exampleInputEmail1" style="text-align: left;"><a href="<%=basePath2%>Usuarios_Web/admin/generarReportesUsuarios.jsp?<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
       *<%=msj.getString("logOn.admin.reporteUsuarios")%></a></label>
       <label for="exampleInputEmail1" style="text-align: left;"><a href="<%=basePath%>borrarArchivosServlet">
       *<%=msj.getString("logOn.admin.borrarTemporales")%></a></label>
       <label for="exampleInputEmail1" onclick="popUpUsuariosClose()" 
       style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
     </div>
     
     <div id="popUpDetallar"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; 
		right: 0; width: 700px; display: none;">
		<div id="cboxContent">
		<div id="cboxTitle">DETALLES</div>
		<div id="cboxLoadedContent">
		<div class="form-group"><label for="exampleInputEmail1">TIPO DE PERSONA: </label>
		<input type="text" style="width: 300px" id="detTipoPersona" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">NOMBRE: </label>
		<input type="text" style="width: 300px" id="detNombre" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">TIPO IDENTIFICACION: </label>
		<input type="text" style="width: 300px" id="detTipoIdentificacion" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">NUMERO IDENTIFICACION: </label>
		<input type="text" style="width: 300px" id="detNumeroIdentificacion" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">PAIS: </label>
		<input type="text" style="width: 300px" id="detPais" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">DEPARTAMENTO: </label>
		<input type="text" style="width: 300px" id="detDepartamento" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">CIUDAD: </label>
		<input type="text" style="width: 300px" id="detCiudad" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">DIRECCION: </label>
		<input type="text" style="width: 300px" id="detDireccion" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">TELEFONO: </label>
		<input type="text" style="width: 300px" id="detTelefono" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">CELULAR: </label>
		<input type="text" style="width: 300px" id="detMovil" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">CORREO ELECTRONICO: </label>
		<input type="text" style="width: 300px" id="detEmail" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1">LICENCIAS: </label>
		<input type="text" style="width: 300px" id="detLicencias" disabled="disabled"></div><br>
		<input type="button" value="Volver" onclick="desactivaDetallar()">
		</div>
		</div>
	</div>
	
	<div id="popUpAsignarRol"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; 
		right: 0; width: 700px; display: none; ">
		<div id="cboxContent">
		<div id="cboxTitle">ASIGNAR ROL</div>
		<div id="cboxLoadedContent">
		<div class="form-group"><label for="exampleInputEmail1">Seleccione el rol para el usuario: </label>
		<input type="text" style="width: 300px" id="asrolNombre" disabled="disabled"></div>
		<label>Rol: </label>
		<div class="form-group">
		<div class="select-wrapper" style="width: 320px">
		<select id="selectRol" name="selectRol">
		<%=roles%>
		</select></div></div><br>
		<input type="button" id="guardaRol" name="guardaRol"  value="Guardar" 
		title="" onclick="guardaAsignarRol(this.title)">
		<input type="button" value="Cancelar" onclick="desactivaAsignarRol()">
		</div>
		</div>
	</div>

	<div id="fondoBloqueo"
		style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8);
		 width: 1000%; height: 1000%; top: -20; left: -20; display: none;">
         <!--Sin contenido -->
	</div>
	
		<form id="formActivacion" action="<%=basePath%>activacionUsuarioServlet" method="post">
		<input type="hidden" id="idUsuario" name="idUsuario">
		<input type="hidden" id="tipoOperacion" name="tipoOperacion">
		</form>
		<form id="formCambioRol" action="<%=basePath%>modificarRolUsuarioServlet" method="post">
		<input type="hidden" id="idUsuarioCambioRol" name="idUsuario">
		<input type="hidden" id="idRolCambioRol" name="idRol">
		</form>
</body>
</html>