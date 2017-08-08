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
<html class='no-js'>
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta charset="utf-8" />
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
	
	ArrayList<co.gov.ideamredd.util.entities.Usuario> usuarios = (ArrayList<co.gov.ideamredd.util.entities.Usuario>) session.getAttribute("usuarios");
	String roles = (String) CargaDatosSelect.rolesUsuario();
	
%>

<script>
	
	var PuedeCambiarRol = -1;
	var PuedeDetallarUsuario = -1;
	
</script>

<%if(usuario != null)
	if(ControlPermisos.tienePermiso(diccionarioPermisos, 71))
	{
%>

<%
if(ControlPermisos.tienePermiso(diccionarioPermisos, 74))
	{
%>
	<script>
	PuedeCambiarRol = 1;
	</script>
<%}else{ %>
	<script>
	PuedeCambiarRol = 0;
    </script>
<%} %>

<%
if(ControlPermisos.tienePermiso(diccionarioPermisos, 72))
	{
%>
	<script>
	PuedeDetallarUsuario = 1;
	</script>
<%}else{ %>
	<script>
	PuedeDetallarUsuario = 0;
    </script>
<%} %>

<title>Sistema de Monitoreo de Biomasa y Carbono</title>

<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="../css/estilos.css" />
<script type="text/javascript" src="../js/general.js"></script>


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

		function formConsultarUsuario(){
			var documento = document.getElementById("documentoAux").value;
			document.getElementById("documento").value=documento;

			var tipodoc = document.getElementById("tipodocAux").value;
			document.getElementById("tipodoc").value=tipodoc;

			var nombre = document.getElementById("nombreAux").value;
			document.getElementById("nombre").value=nombre;

			var rol = document.getElementById("rolAux").value;
			document.getElementById("rol").value=rol;

			var activo = "0";
			activo = document.getElementById("activoAux").checked ? "1" : "0";
			document.getElementById("activo").value=activo;

			var tipo = document.getElementById("tipoAux").value;
			document.getElementById("tipo").value=tipo;
			
			document.getElementById("formConsulta").submit();
		}

		var municipio;
		var municipios = new Array();
		var depto;
		var deptos = new Array();
		var codigos = new Array();
		var titulos = new Array(11);
		titulos[0] = '<%=msj.getString("consultaUsuarios.q.identificacion")%>';
		titulos[1] = '<%=msj.getString("consultaUsuarios.q.tipoIdenti")%>';
		titulos[2] = '<%=msj.getString("consultaUsuarios.q.nombre")%>';
		titulos[3] = '<%=msj.getString("email")%>';
		titulos[4] = '<%=msj.getString("rol")%>';
		titulos[5] = '<%=msj.getString("activo")%>';
		titulos[6] = '<%=msj.getString("tipo")%>';
		titulos[7] = '<%=msj.getString("consultaUsuarios.q.ultimoIngreso")%>';
		titulos[8] = '';
		titulos[9] = '';
		titulos[10] = '';
		titulos[11] = '';
		titulos[12] = '';
			
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
							var p2 = document.createElement('p2');
							p2.innerText = titulos[j]+": <b>"+ conte[j] + "</b>";
							p2.innerHTML = titulos[j]+": <b>"+ conte[j] + "</b>";
							p.appendChild(p2);
							span1.appendChild(p);
							div.appendChild(span1);
						}else{
							var p = document.createElement('p');
							var p2 = document.createElement('p2');
							p2.innerText = titulos[j]+": <b>" + conte[j] + "</b>";
							p2.innerHTML = titulos[j]+": <b>" + conte[j] + "</b>";
							p.appendChild(p2);
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
						
						if(PuedeDetallarUsuario==1){
						adicionarBoton3('<%=msj.getString("consultaUsuarios.detallar")%>',
								 span3, 'Detallar', 1, i);
						}
						
						if(PuedeCambiarRol==1){
							//if(userRol[i]!="7")
							adicionarBoton3('<%=msj.getString("consultaUsuarios.asignaRol")%>',
								 	span3, 'Asignar_Rol', 2, i);
						}
						
						if(esActivo[i]=="1")
						{
						//if(userRol[i]!="7")
						adicionarBoton2('<%=msj.getString("consultaUsuarios.desactivar")%>', 
								span3, 'Desactivar', 3, i, esActivo[i]);
						}
						else{
						//if(userRol[i]!="7")
						adicionarBoton2('<%=msj.getString("consultaUsuarios.activar")%>',
								 span3, '<%=msj.getString("consultaUsuarios.activar")%>', 3, i, esActivo[i]);
						}
						
						div.appendChild(span3);
					}
					cont.appendChild(div);
				}
			}
		}
		


        
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
	   userNombreRol= Array(<%=usuarios.size()%>);
	   
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
	    var str_esActivo = 'No';
	    if (esActivo[<%=i%>] == '1') {
		    str_esActivo = 'Sí/Yes';
		}
	    userId[<%=i%>] = '<%=usuarios.get(i).getIdUsuario()%>';
	    userRol[<%=i%>]= '<%=usuarios.get(i).getRolId()%>';
	    userNombreRol[<%=i%>]= '<%=usuarios.get(i).getRolNombre()%>';

	    auxDatosUsuarios = "";
	    auxDatosUsuarios = documentoLista[<%=i%>] + ";" +
	                       tipoIdenLista[<%=i%>] + ";" +
	                       nombreLista[<%=i%>] + ";" +
	                       emailLista[<%=i%>] + ";" +
	                       userNombreRol[<%=i%>] + ";" +
	                       str_esActivo + ";" +
	                       tipoPersonaLista[<%=i%>] + ";" +
	                      '<%=ConsultaWebUsuario.consultarUltimoIngreso(usuarios.get(i).getIdUsuario())%>';
	                       datUsuarios[<%=i%>]= auxDatosUsuarios;
		
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
			var selectedRol = document.getElementById("selectRol").value;

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
		if(datUsuarios != null) {
			if(datUsuarios.length!=0){//cambiar para los filtros 
				resultadosContactos(datUsuarios.length, 'resultados','consultas');
				cargarDatos(datUsuarios,'consultas',3,titulos,null,0,5);
				crearPaginas(datUsuarios.length,'consultas',1);
			}
			else{
				busquedaVacia('resultados','consultas');
			}
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
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja"><%=msj.getString("Administracion_de_Usuarios") %></h2>

							<div id="content">
								<div class="content-inner">

									<div id="block-accordeon-resultados-busqueda" class="block">
										<div class="content">
												<div id="accordion">
												<h3><%=msj.getString("consultaUsuarios.consultaUsuarios")%></h3>
												<div id="block-datos-basicos">
													<div class="form-datos-parcela form-columnx2" role="form">

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
															<label for="exampleInputEmail1">
 															<%=msj.getString("consultaUsuarios.NoIdentificacion")%>
 															</label>
															<input type="text" class="form-control" name="documentoAux" id="documentoAux"
															 onkeypress="return valideValNum(event)">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1">
															<%=msj.getString("buscar_nombre")%>
														</label>
															<input type="text" name="nombreAux" id="nombreAux" class="form-control" >
														</div>
														
														<div class="form-group">
														<label for="exampleInputEmail1">
														<%=msj.getString("rol")%>
														</label>
															<div class="select-wrapper">
																<select name="rolAux" id="rolAux">
																	<option value="-1" selected></option>
																	<%=CargaDatosSelect.getRoles()%>
																</select>
															</div>
														</div>
														
														<div class="form-group">
														<label for="exampleInputEmail1">
														<%=msj.getString("activo")%>
														</label>
															<div class="select-wrapper">
																<input type="checkbox" value="1" checked id="activoAux" name="activoAux" />
															</div>
														</div>

														<div class="form-group">
														<label for="exampleInputEmail1">
														<%=msj.getString("tipo")%>
														</label>
															<div class="select-wrapper">
																<select name="tipoAux" id="tipoAux">
																	<option value="-1" selected></option>
																	<option value="1"><%=msj.getString("natural")%></option>
																	<option value="2"><%=msj.getString("juridica")%></option>
																</select>
															</div>
														</div>
																												
														<div class="form-group">
															<br>
														</div>
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

			</div>

			<%=UI.getFooter(msj) %>									

		</div>
	</form>
	
	<form action="/MonitoreoBC-WEB/consultarUsuarioServletAdmin" method="post" name="formConsulta" id="formConsulta">
			<input type="hidden" name="documento" id="documento">
		    <input type="hidden" name="tipodoc" id="tipodoc">
		    <input type="hidden" name="nombre" id="nombre">
		    <input type="hidden" name="rol" id="rol">
		    <input type="hidden" name="activo" id="activo">
		    <input type="hidden" name="tipo" id="tipo">
	</form>

     <div id="popUpDetallar" style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 81%; display: none;">
		<div id="cboxContent">
		<div id="cboxTitle"><%=msj.getString("detalles") %></div>
		<div id="cboxLoadedContent">
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.tipoPersona")%>: </label>
		<input type="text" style="width: 300px" id="detTipoPersona" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("nombre_completo") %>: </label>
		<input type="text" style="width: 300px" id="detNombre" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.tipoIdenti")%>: </label>
		<input type="text" style="width: 300px" id="detTipoIdentificacion" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.numeroIdenti")%>: </label>
		<input type="text" style="width: 300px" id="detNumeroIdentificacion" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.pais")%>: </label>
		<input type="text" style="width: 300px" id="detPais" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.depto")%>: </label>
		<input type="text" style="width: 300px" id="detDepartamento" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.muni")%>: </label>
		<input type="text" style="width: 300px" id="detCiudad" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.direccion")%>: </label>
		<input type="text" style="width: 300px" id="detDireccion" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.tel")%>: </label>
		<input type="text" style="width: 300px" id="detTelefono" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.cel")%>: </label>
		<input type="text" style="width: 300px" id="detMovil" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("modificaUsuario.correo")%>: </label>
		<input type="text" style="width: 300px" id="detEmail" disabled="disabled"></div>
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("licencias") %>: </label>
		<input type="text" style="width: 300px" id="detLicencias" disabled="disabled"></div><br>
		<input type="button" value="<%=msj.getString("volver")%>" onclick="desactivaDetallar()">
		</div>
		</div>
	</div>
	
	<div id="popUpAsignarRol" style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 81%; display: none; ">
		<div id="cboxContent">
		<div id="cboxTitle"><%=msj.getString("asignar_rol") %></div>
		<div id="cboxLoadedContent">
		<div class="form-group"><label for="exampleInputEmail1"><%=msj.getString("seleccionar") %>: </label>
		<input type="text" style="width: 300px" id="asrolNombre" disabled="disabled"></div>
		<label><%=msj.getString("rol") %>: </label>
		<div class="form-group">
		<div class="select-wrapper" style="width: 320px">
		<select id="selectRol" name="selectRol">
		<%=roles%>
		</select></div></div><br>
		<input type="button" id="guardaRol" name="guardaRol"  value="<%=msj.getString("guardar") %>" 
		title="" onclick="guardaAsignarRol(this.title)">
		<input type="button" value="<%=msj.getString("cancelar") %>" onclick="desactivaAsignarRol()">
		</div>
		</div>
	</div>

		<form id="formActivacion" action="/MonitoreoBC-WEB/activacionUsuarioServletAdmin" method="post">
		<input type="hidden" id="idUsuario" name="idUsuario">
		<input type="hidden" id="tipoOperacion" name="tipoOperacion">
		</form>
		<form id="formCambioRol" action="/MonitoreoBC-WEB/modificarRolUsuarioServletAdmin" method="post">
		<input type="hidden" id="idUsuarioCambioRol" name="idUsuario">
		<input type="hidden" id="idRolCambioRol" name="idRol">
		</form>
</body>
</html> 

<%}else{%>

	<%=UI.getPaginaPermisoDenegado(msj)%>										

<%} %>