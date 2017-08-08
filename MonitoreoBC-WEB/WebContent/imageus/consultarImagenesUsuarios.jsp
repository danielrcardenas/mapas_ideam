<%@page import="co.gov.ideamredd.web.admin.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.admin.entities.Depto"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.web.admin.dao.ConsultaWebUsuario"%>
<%@page import="co.gov.ideamredd.web.admin.dao.CargaDatosSelect"%>
<%@page import="co.gov.ideamredd.admin.entities.Municipios"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
<%@page import="co.gov.ideamredd.imgusuarios.entities.ImagenUsuario"%>
<%@page import="co.gov.ideamredd.web.imgusuarios.dao.ConsultarImagenesUsuario"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta charset="utf-8" />
<title>Sistema de Monitoreo de Biomasa y Carbono</title>
<%
	HttpSession sesion = request.getSession(false); if (!request.isRequestedSessionIdValid() || sesion == null) { response.sendRedirect("/MonitoreoBC-WEB"); return;}ResourceBundle msj = (ResourceBundle)sesion.getAttribute("i18n");

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

ArrayList<ImagenUsuario> imagenes = ConsultarImagenesUsuario.consultarImagenes();

%>

<script type="text/javascript">
 var PuedeProcesarImagen=-1;
</script>

<%
	if(usuario != null)
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 108))
	{
%>

<%
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 109))
{
%>
<script type="text/javascript">
	PuedeProcesarImagen =1;
	</script>
<%
	}else{
%>
<script type="text/javascript">
	PuedeProcesarImagen =0;
	</script>
<%
	}
%>


<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>

<script>


	$(document).ready(function() {
		inicializarNavegador();
	});

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

        <%String direccionImagen="";%>
		var auxDatosImagenes;
		
		var nombreImagenActiva;
		var idImagenActiva;
		var nomImagenActiva;
		var usuarioImagenActiva;
		
		var datImagenes= Array(<%=imagenes.size()%>);
		var documentoLista= Array(<%=imagenes.size()%>);
		
		var listaImagenes= Array(<%=imagenes.size()%>);
		
		var contadorImagenes=0;

		<%for(int i=0;i<imagenes.size();i++)
		{%>
		
		documentoLista[<%=i%>]='<%=imagenes.get(i).getIdImagen()%>';
		
		listaImagenes[<%=i%>]='<%=imagenes.get(i).getIdImagen()%>'+";"+
							  '<%=imagenes.get(i).getIdUsuario()%>'+";"+
							  '<%=imagenes.get(i).getNombre()%>'+";"+
							  '<%=imagenes.get(i).getFechaSubida()%>'+";"+
							  'Procesando'+";"+
							  
							  '<%if(imagenes.get(i).getLicencia()==1)
							  {out.print("Publicar y Descargar");}
							  else{out.print("Solo Publicar");}%>'+";"+
							  
							  '<%=imagenes.get(i).getDescripcion()%>'+";"+
							  '<%=imagenes.get(i).getAutor()%>'+";"+
							  '<%=imagenes.get(i).getLatitud()%>'+";"+
							  '<%=imagenes.get(i).getLongitud()%>'+";"+
							  '<%=imagenes.get(i).getPath()%>';

		auxDatosImagenes = "";
		auxDatosImagenes = '<%=imagenes.get(i).getNombre()%>' + ";" +
						   '<%=imagenes.get(i).getFechaSubida().toString()%>' + ";" +
						   '<%=imagenes.get(i).getIdUsuario()%>' + ";" +
						   
						   '<%if(imagenes.get(i).getLicencia()==1)
							  {out.print("Publicar y Descargar");}
							  else{out.print("Solo Publicar");}%>';
							  
		datImagenes[<%=i%>]= auxDatosImagenes;
	                       
	    <%}%>             

		function todos(chkbox) {
			for ( var i = 0; i < document.forms[0].elements.length; i++) {
				var elemento = document.forms[0].elements[i];
				if (elemento.type == "checkbox")
					elemento.checked = chkbox.checked;
			}
		}

		function detallar(idImagen) {
			window.scrollTo(0,0);
			document.getElementById("fondoBloqueo").style.display = "block";
			document.getElementById("popUpDetallar").style.display = "block";
			var i=0;
				for (i = 0; i < listaImagenes.length; i++) {
					if(listaImagenes[i].split(";")[0]==idImagen)
					{
						document.getElementById("detIdImagen").innerHTML=listaImagenes[i].split(";")[0];
						document.getElementById("detIdUsuario").innerHTML=listaImagenes[i].split(";")[1];
						document.getElementById("detNombre").innerHTML=listaImagenes[i].split(";")[2];
						document.getElementById("detFechaCarga").innerHTML=listaImagenes[i].split(";")[3];
						document.getElementById("detEstado").innerHTML=listaImagenes[i].split(";")[4];
						document.getElementById("detLicencia").innerHTML=listaImagenes[i].split(";")[5];
						document.getElementById("detDescripcion").innerHTML=listaImagenes[i].split(";")[6];
						document.getElementById("detAutor").innerHTML=listaImagenes[i].split(";")[7];
						document.getElementById("detLatitud").innerHTML=listaImagenes[i].split(";")[8];
						document.getElementById("detLongitud").innerHTML=listaImagenes[i].split(";")[9];
						document.getElementById("detImagen"+listaImagenes[i].split(";")[0]).style.display="block";
						nombreImagenActiva="detImagen"+listaImagenes[i].split(";")[0];
						idImagenActiva=listaImagenes[i].split(";")[0];
						nomImagenActiva=listaImagenes[i].split(";")[2];
						usuarioImagenActiva=listaImagenes[i].split(";")[1];
					}
				}
		}

		function desactivaDetallar() {
			document.getElementById("fondoBloqueo").style.display = "none";
			document.getElementById("popUpDetallar").style.display = "none";
			document.getElementById(nombreImagenActiva).style.display = "none";
		}

		function guardaAsignarRol(docUsuario) {
			var idUsAux = document.getElementById("userHid"+docUsuario).value;
			var selectedRol = document.getElementById("selectRol").selectedIndex+1;

			document.getElementById("idRolCambioRol").value = selectedRol;
			document.getElementById("idUsuarioCambioRol").value = idUsAux;

			document.getElementById("formCambioRol").submit();		
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
	titulos[0] = '<b>Nombre imagen</b>';
	titulos[1] = '<b>Fecha subida</b>';
	titulos[2] = '<b>Usuario</b>';
	titulos[3] = '<b>Licencia</b>';
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
		if(datImagenes != null)
		if(datImagenes.length!=0){//cambiar para los filtros 
		resultadosContactos(datImagenes.length, 'resultados','consultas');
		cargarDatos(datImagenes,'consultas',3,titulos,null,0,5);
		crearPaginas(datImagenes.length,'consultas',1);
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

	function crearPaginas(tamanho, contenedor, id) {
		var i;
		var cont = document.getElementById(contenedor);
		var paginador = document.getElementById('paginador');
		if (paginador != null) paginador.parentNode.removeChild(paginador);
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
		var indiceFinal;
		var indiceInicial;
		contadorImagenes = 5 * (boton - 1);
		idActual = boton;
		indiceFinal = boton * 5;
		indiceInicial = indiceFinal - 5;
		resultadosContactos(datImagenes.length, 'resultados', 'consultas');
		cargarDatos(datImagenes, 'consultas', 3, titulos, null, indiceInicial, indiceFinal);
		crearPaginas(datImagenes.length, 'consultas', boton);
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
		}
		else {
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

	function cargarDatos(parc, contenedor, numColumnas, titulos, permisos, indiceInicial, indiceFinal) {
		var i, j, conte;
		var cont = document.getElementById(contenedor);
		for (i = indiceInicial; i < parc.length; i++) {
			if (i < indiceFinal) {
				var span1, span2, span3;
				var indiceColumna = 1;
				var div = document.createElement('div');
				if (i % 2 == 0)
					div.className = 'item-busqueda item-busqueda-odd';
				else div.className = 'item-busqueda item-busqueda-even';
				conte = parc[i].split(";");
				span1 = document.createElement('span');
				span1.id = "columna" + indiceColumna;
				span1.className = "column column" + indiceColumna;
				indiceColumna++;
				span2 = document.createElement('span');
				span2.id = "columna" + indiceColumna;
				span2.className = "column column" + indiceColumna;
				for (j = 0; j < conte.length; j++) {
					if (j < 5) {
						var p = document.createElement('p');
						var strong = document.createElement('strong');
						if (j != 4) {
							strong.innerText = titulos[j] + ": " + conte[j];
							strong.innerHTML = titulos[j] + ": " + conte[j];
						}
						else {//esto no en el general
							strong.innerText = conte[j] + "," + conte[j + 1] + "," + conte[j + 2];
							strong.innerHTML = conte[j] + "," + conte[j + 1] + "," + conte[j + 2];
							j = 6;
						}
						p.appendChild(strong);
						span1.appendChild(p);
						div.appendChild(span1);
					}
					else {
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
					adicionarBotonProcesar('Procesar', span3, 'Procesar', 1, contadorImagenes);
					div.appendChild(span3);
				}
				cont.appendChild(div);
			}
		}
	}

	function adicionarBotonProcesar(idBoton, contenedor, valor, indiceimax) {
		var formulario = document.createElement("div");
		formulario.className = "form-actions";
		var boton = document.createElement('input');
		boton.type = "button";
		boton.className = "btn btn-default";
		boton.value = idBoton;

		boton.id = documentoLista[contadorImagenes].toString();
		boton.name = documentoLista[contadorImagenes].toString();

		if (PuedeProcesarImagen == 0) {
			boton.style.display = "none";
		}

		boton.onclick = function() {
			detallar(boton.id);
		};

		formulario.appendChild(boton);
		contenedor.appendChild(formulario);
		contadorImagenes++;
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

		if (esActivo == "1")
			boton.onclick = function() {
				desactivarUs(boton.id);
			};
		else boton.onclick = function() {
			activarUs(boton.id);
		};

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
		hidden1.type = "hidden";
		hidden2.type = "hidden";
		hidden1.value = userId[indice];
		hidden2.value = userRol[indice];
		hidden1.id = "userHid" + documentoLista[indice];
		hidden2.id = "rolHid" + documentoLista[indice];
		hidden1.name = "userHid" + documentoLista[indice];
		hidden2.name = "rolHid" + documentoLista[indice];

		boton.id = documentoLista[indice].toString();
		boton.name = documentoLista[indice].toString();

		if (valor == "Detallar") {
			boton.onclick = function() {
				detallar(boton.id);
			};
		}
		else {
			boton.onclick = function() {
				asignarRol(boton.id);
			};
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
			if ((startDate < myDate1 && myDate1 < endDate) && (startDate < myDate2 && myDate2 < endDate)) {
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
			}
			else {
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
		var coords = getAbsoluteElementPosition(document.getElementById("icoUsuarios"));

		document.getElementById("popUpUsuarios").style.left = coords.left + "px";
		document.getElementById("popUpUsuarios").style.top = coords.top + 15 + "px";
	}

	function popUpUsuariosOpen() {
		var coords = getAbsoluteElementPosition(document.getElementById("icoUsuarios"));

		document.getElementById("popUpUsuarios").style.left = coords.left + "px";
		document.getElementById("popUpUsuarios").style.top = coords.top + 15 + "px";
		document.getElementById("popUpUsuarios").style.display = "block";
	}

	function popUpUsuariosClose() {
		document.getElementById("popUpUsuarios").style.display = "none";
	}

	function formConsultarUsuario() {
		document.getElementById("documento").value = document.getElementById("documentoAux").value;
		document.getElementById("tipodoc").value = document.getElementById("tipodocAux").value;
		document.getElementById("nombre").value = document.getElementById("nombreAux").value;

		document.getElementById("formConsulta").submit();
	}

	function formProcesar(seleccion) {
		document.getElementById("hidImagenId").value = idImagenActiva;
		document.getElementById("hidSeleccion").value = seleccion;
		document.getElementById("hidNombreImagen").value = nomImagenActiva;
		document.getElementById("hidUsuarioId").value = usuarioImagenActiva;

		document.getElementById("formProcesarImagen").submit();
	}
</script>
</head>
<body class='sidebarlast front' onMouseMove="takeCoordenadas(event);" onmouseover="popUpUsuariosAux()">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idiomaServlet" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">


							<div id="content">

								<div class="content-inner">

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
											<div id="accordion">
												<h3>Consultar Imagenes de Usuarios</h3>
												<div id="block-datos-basicos">

													<div id="resultados">
														<div id="consultas"></div>
														<div id="paginador" class="pager"></div>
													</div>
												</div>
											</div>

										</div>
									</div>
								</div>




							</div>

							<%=UI.getSidebar(usuario, sesion, msj, diccionarioPermisos, i18n) %>									

						</div>
					</div>
				</div>

			</div>

			<%=UI.getFooter(msj) %>									

		</div>
	</form>

	<div id="popUpDetallar" style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 81%; display: none;">
		<div id="cboxContent">
			<div id="cboxTitle">DETALLES</div>
			<div id="cboxLoadedContent">
				<div class="form-group">
					<label for="exampleInputEmail1">ID IMAGEN: </label> <label style="margin-left: 30px" id="detIdImagen" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">ID USUARIO: </label> <label style="margin-left: 30px" id="detIdUsuario" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">NOMBRE: </label> <label style="margin-left: 30px" id="detNombre" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">FECHA DE SUBIDA: </label> <label style="margin-left: 30px" id="detFechaCarga" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">ESTADO: </label> <label style="margin-left: 30px" id="detEstado" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">LICENCIA: </label> <label style="margin-left: 30px" id="detLicencia" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">AUTOR: </label> <label style="margin-left: 30px" id="detAutor" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">LATITUD: </label> <label style="margin-left: 30px" id="detLatitud" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">LONGITUD: </label> <label style="margin-left: 30px" id="detLongitud" for="exampleInputEmail1"></label>
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">DESCRIPCION: </label>
					<p style="resize: none; font-size: 15px;" id="detDescripcion"></p>
				</div>

				<div class="form-group">
					<label for="exampleInputEmail1">IMAGEN: </label>
					<%
						for (int i = 0; i < imagenes.size(); i++) {
					%>
					<img name="detImagen<%=imagenes.get(i).getIdImagen()%>" id="detImagen<%=imagenes.get(i).getIdImagen()%>" style="width: 600px; display: none" src="/MonitoreoBC-WEB/imagenServlet?nomImagenParam=<%=imagenes.get(i).getPath()%>">
					<%
						}
					%>
				</div>

				<input type="button" value="Aceptar" onclick="alert('La imagen aparecera en el visor de imagenes de usuario, se le notificará a este la aceptación.');formProcesar('acepta')"> <input type="button" value="Rechazar" onclick="alert('La imagen sera archivada y no se mostrara, se le notificará al usuario que revise la imagen.');formProcesar('rechaza')"> <input type="button" value="<%=msj.getString("volver")%>" onclick="desactivaDetallar()">
			</div>
		</div>
	</div>


	<div id="fondoBloqueo" style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; top: -20; left: -20; display: none;">
		<!--Sin contenido -->
	</div>

	<form id="formActivacion" action="/MonitoreoBC-WEB/activacionUsuarioServletAdmin" method="post">
		<input type="hidden" id="idUsuario" name="idUsuario"> <input type="hidden" id="tipoOperacion" name="tipoOperacion">
	</form>
	<form id="formCambioRol" action="/MonitoreoBC-WEB/modificarRolUsuarioServletAdmin" method="post">
		<input type="hidden" id="idUsuarioCambioRol" name="idUsuario"> <input type="hidden" id="idRolCambioRol" name="idRol">
	</form>

	<form id="formProcesarImagen" action="/MonitoreoBC-WEB/procesarImagenServlet" method="post">
		<input type="hidden" id="hidImagenId" name="hidImagenId"> <input type="hidden" id="hidSeleccion" name="hidSeleccion"> <input type="hidden" id="hidNombreImagen" name="hidNombreImagen"> <input type="hidden" id="hidUsuarioId" name="hidUsuarioId">
	</form>

</body>
</html>
<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>