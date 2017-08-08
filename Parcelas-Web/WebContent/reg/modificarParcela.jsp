<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="co.gov.ideamredd.dao.Constantes"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.entities.TipoInventario"%>
<%@page import="co.gov.ideamredd.entities.Proposito"%>
<%@page import="co.gov.ideamredd.entities.Temporalidad"%>
<%@page import="co.gov.ideamredd.entities.Pais"%>
<%@page import="co.gov.ideamredd.entities.ContactoParcela"%>
<%@page import="co.gov.ideamredd.entities.CAR"%>
<%@page import="co.gov.ideamredd.entities.Municipios"%>
<%@page import="co.gov.ideamredd.entities.Depto"%>
<%@page import="co.gov.ideamredd.entities.TipoBosque"%>
<%@page import="co.gov.ideamredd.dao.ObtenerParcelaConsulta"%>
<%@page import="co.gov.ideamredd.entities.Parcela"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="co.gov.ideamredd.entities.Organizacion"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicialParcelas"%>
<%@page import="co.gov.ideamredd.entities.Contacto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.dao.TabsLicenciasDescarga"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<title>Modificar Parcela</title>
<%
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/"; 
	ArrayList<Noticias> noticias = CargaDatosInicialParcelas.getNoticias(); 
	ArrayList<Noticias> eventos = CargaDatosInicialParcelas.getEventos();
	ResourceBundle msj= (ResourceBundle)request.getSession().getAttribute("i18n");
	String[] licencias = TabsLicenciasDescarga.getLicenciasDescargaUsuarios(0, 1);
	String patron = "dd/MM/yyyy";
	SimpleDateFormat formato = new SimpleDateFormat(patron);
	String idParcela = (String)request.getSession().getAttribute("parcela");
	Parcela parcela = ObtenerParcelaConsulta.obtenerParcela(Integer.valueOf(idParcela));
	String[] geometria = ObtenerParcelaConsulta.consultarGeoParcela(Integer.valueOf(idParcela)); 
// 	if(geometria[0].equals(Constantes.punto))
// 		geometria[0]="Punto";
// 	else if(geometria[0].equals(Constantes.linea))
// 		geometria[0]="Linea";
// 	else if(geometria[0].equals(Constantes.poligono))
// 		geometria[0]="Poligono";
	ArrayList<ContactoParcela> contactos = ObtenerParcelaConsulta.consultaContactosParcelaId(Integer.valueOf(idParcela));
	ArrayList<Object> cont = ObtenerParcelaConsulta.obtenerContactosParcela(contactos);
	Organizacion ideam = CargaDatosInicialParcelas.getDatosIdeam();
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
<script type="text/javascript" src="../js/manejo-listas.js"></script>
<script type="text/javascript" src="../js/seleccion.js"></script>
<script type="text/javascript" src="../js/contactos.js"></script>
<script type="text/javascript" src="../js/paginador.js"></script>
<script type="text/javascript">
$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });
$(function() {
    $( "#fInicial" ).datepicker(); 
  });

var titulos = new Array(3);
titulos[0] = 'Codigo';
titulos[1] = 'Nombre';
titulos[2] = 'Correo';
titulos[3] = 'Telefono';

//-------------------------------------------------------------------------------------
var contact;
var contacts = new Array();

function cargarContacto(){
<%ArrayList<Contacto> contac = CargaDatosInicialParcelas
			.getArrayContactos();
 	String con="";
	for (int i = 0; i < contac.size(); i++) {
		Contacto c1 = (Contacto) contac.get(i); 
		con = c1.getConsecutivo() + ","
				+ c1.getNombre()+","+c1.getCorreo()+","+c1.getMovil();%>	
		contacts[<%=i%>] = '<%=con%>';
	<%}%>
	return contacts;
}

var org;
var orgs = new Array();

function cargarOrganizacion(){
	<%ArrayList<Organizacion> organ = CargaDatosInicialParcelas
			.getArrayOrganizacion();
 	String or="";
	for (int i = 0; i < organ.size(); i++) {
		Organizacion o1 = (Organizacion) organ.get(i);
		or = o1.getConsecutivo() + ","
				+ o1.getNombre()+","+o1.getCorreo()+","+o1.getTelefono();%>	
		orgs[<%=i%>] = '<%=or%>';
	<%}%>
	return orgs;
}


var IDEAM = 1;
var inv = false;
var colEnc = false;
var bri = false;
var sup = false;
var idActual;
function agregaContacto(control) {

	if (control.value == 4) {
		if (!inv) {
			document.getElementById('Inv').style = 'block';
			inv = true;
			document.getElementById('isInvestigador').value = 1;
		}
	} else if (control.value == 5 || control.value == 6) {
		if (!colEnc) {
			document.getElementById('ColEnc').style = 'block';
			colEnc = true;
			document.getElementById('isColeccion').value = 1;
		}
	} else if (control.value == 7) {
		if (!bri) {
			document.getElementById('Bri').style = 'block';
			bri = true;
			document.getElementById('isBrigadista').value = 1;
		}
	} else if (control.value == 8) {
		if (!sup) {
			document.getElementById('Sup').style = 'block';
			sup = true;
			document.getElementById('isSupervisor').value = 1;
		}
	}
}

function enviar() {
	retornarDatos();
	document.getElementById('modificarParcela').action='<%=basePath%>modificarparcela';
	document.getElementById('modificarParcela').submit();
}

function limpiarFormulario(formulario) {
	var i;
	for (i = 0; i < document.getElementById(formulario).elements.length; i++) {
		if ((document.getElementById(formulario)[i].type == 'checkbox')) {
			if (document.getElementById(formulario)[i].options[indice].value == 0) {
				document.getElementById(combo).selectedIndex = indice;
			}
		} else if ((document.getElementById(formulario)[i].type == 'text')) {
			document.getElementById(formulario)[i].value = '';
		} else if ((document.getElementById(formulario)[i].type == 'textarea')) {
			document.getElementById(formulario)[i].value = '';
		} else if ((document.getElementById(formulario)[i].type == 'file')) {
			document.getElementById(formulario)[i].value = '';
		} else if ((document.getElementById(formulario)[i].type == 'select-one')) {
			if (document.getElementById(formulario)[i].selectedIndex != 0) {
				document.getElementById(formulario)[i].selectedIndex = 0;
			}
		}
	}
}

function ocultarContacto(divContacto) {
	if (document.getElementById(divContacto).style.display == 'block') {
		document.getElementById(divContacto).style.display = 'none';
	}
}

function cargarDatosParcela(){
	document.getElementById('consecutivoParcela').value='<%=parcela.getConsecutivo()%>';
	document.getElementById('usoinfo').value='<%=parcela.getInventarioPublico()%>';
	document.getElementById('tipolicencia').value='<%=parcela.getPublica()%>';
	cargarArchivoZIP();
	//creaGeometria();
	cargarGeometria();
	cargarTemporalidad();
	cargarAprovechamientoParcela();
	cargarTipoInvParcela();
	cargarPropositoParcela();
	cargarContactosParcela();
<%if(parcela.getNombreImagen()!=""){%>
		document.getElementById('datosArchivo').style.display='block';
<%}%>
}

var opcion = 0;
var cont=0;
function creaGeometria() {
	var contenedor = document.getElementById("puntos");
	var j;
		puntos[cont]='<%=geometria[1]%>'; 
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

function cargarContactosParcela(){
<%for(int i=0;i<contactos.size();i++){
		if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionFGDA)){%>
// 			document.getElementById('divContacto').style.display='block';
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseFGDA').value='<%=Constantes.personaNatural%>';
				document.getElementById('consFGDA').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomFGDA').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telFGDA').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corFGDA').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseFGDA').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consFGDA').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomFGDA').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telFGDA').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corFGDA').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		} else if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionPropietario)){%>
// 			document.getElementById('divContPro').style.display='block';
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseprop').value='<%=Constantes.personaNatural%>';
				document.getElementById('consprop').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomprop').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telprop').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corprop').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseprop').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consprop').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomprop').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telprop').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corprop').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		}else if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionCustodio)){%>
// 			document.getElementById('divContactoCus').style.display='block';
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('clasecus').value='<%=Constantes.personaNatural%>';
				document.getElementById('conscus').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomcus').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telcus').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corcus').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('clasecus').value='<%=Constantes.personaJuridica%>';
				document.getElementById('conscus').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomcus').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telcus').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corcus').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		}else if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionInv)){%>
			document.getElementById('divNomInv').style.display='block';
			document.getElementById('divTelInv').style.display='block';
			document.getElementById('divCorInv').style.display='block';
			document.getElementById('eventoInv').style.display='block';
			document.getElementById('isInv').value	= 1;
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseInv').value='<%=Constantes.personaNatural%>';
				document.getElementById('consInv').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomInv').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telInv').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corInv').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseInv').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consInv').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomInv').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telInv').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corInv').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		}else if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionCol)){%>
			document.getElementById('divNomCol').style.display='block';
			document.getElementById('divTelCol').style.display='block';
			document.getElementById('divCorCol').style.display='block';
			document.getElementById('divNomEnc').style.display='block';
			document.getElementById('divTelEnc').style.display='block';
			document.getElementById('divCorEnc').style.display='block';
			document.getElementById('eventoCol').style.display='block';
			document.getElementById('eventoEnv').style.display='block';
			document.getElementById('isCol').value	= 1;
			document.getElementById('isEnc').value	= 1;
// 			adicionarBoton('divContactoCol', 'ColEnc', 'isColeccion');
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseCol').value='<%=Constantes.personaNatural%>';
				document.getElementById('consCol').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomCol').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telCol').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corCol').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseCol').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consCol').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomCol').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telCol').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corCol').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
			i++;
			if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseEnc').value='<%=Constantes.personaNatural%>';
				document.getElementById('consEnc').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomEnc').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telEnc').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corEnc').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseEnc').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consEnc').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomEnc').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telEnc').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corEnc').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		}else if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionBri)){%>
			document.getElementById('divNomBri').style.display='block';
			document.getElementById('divTelBri').style.display='block';
			document.getElementById('divCorBri').style.display='block';
			document.getElementById('eventoBri').style.display='block';
			document.getElementById('isBri').value	= 1;
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseBri').value='<%=Constantes.personaNatural%>';
				document.getElementById('consBri').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomBri').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telBri').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corBri').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseBri').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consBri').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomBri').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telBri').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corBri').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		}else if(contactos.get(i).getIdClase()==Integer.valueOf(Constantes.opcionSup)){%>
			document.getElementById('divNomSup').style.display='block';
			document.getElementById('divTelSup').style.display='block';
			document.getElementById('divCorSup').style.display='block';
			document.getElementById('eventoSup').style.display='block';
			document.getElementById('isSup').value	= 1;
<%if(cont.get(i) instanceof Contacto){%>
				document.getElementById('claseSup').value='<%=Constantes.personaNatural%>';
				document.getElementById('consSup').value='<%=((Contacto) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomSup').value='<%=((Contacto) cont.get(i)).getNombre()%>';
				document.getElementById('telSup').value='<%=((Contacto) cont.get(i)).getMovil()%>';
				document.getElementById('corSup').value='<%=((Contacto) cont.get(i)).getCorreo()%>';
<%}else{%>
				document.getElementById('claseSup').value='<%=Constantes.personaJuridica%>';
				document.getElementById('consSup').value='<%=((Organizacion) cont.get(i)).getConsecutivo()%>';
				document.getElementById('nomSup').value='<%=((Organizacion) cont.get(i)).getNombre()%>';
				document.getElementById('telSup').value='<%=((Organizacion) cont.get(i)).getTelefono()%>';
				document.getElementById('corSup').value='<%=((Organizacion) cont.get(i)).getCorreo()%>';
<%}
		}
	}%>
}

function agregarArchivo(opc){
	if(opc.value==1)
		document.getElementById("popup-file").style.display="block";
	else
		document.getElementById("popup-file").style.display="none";
}

function cargarTemporalidad(){
	var txtTemporalidad = document.getElementById('temporalidad');
	var i;
	for (i = 0; i < txtTemporalidad.options.length; i++) {
		if (txtTemporalidad.options[i].value == <%=parcela.getTemporalidad()%>) {
			txtTemporalidad.options[i].selected = 'selected';
		}
	}
}

function cargarGeometria(){
	var txtGeometria = document.getElementById('geometria');
	var i;
	for (i = 0; i < txtGeometria.options.length; i++) {
		if (txtGeometria.options[i].value == <%=geometria[0]%>) {
			txtGeometria.options[i].selected = 'selected';
		}
	}
		opcion = <%=geometria[0]%>;
		coord('<%=geometria[1]%>');
		valorArea('<%=parcela.getArea().toString()%>');

		seleccion();
<%String[] c =geometria[1].split(";");
		for(int i=0;i<c.length;i++){
		String[] xy = c[i].split(",");%>
	crear(<%=xy[0]%>,<%=xy[1]%>);
<%}%>
	}

	function cargarArchivoZIP() {
<%if(!parcela.getNombreImagen().equals("")){%>
	document.getElementById("datosArchivo").style.display = "block";
<%}%>
	}

	function cargarAprovechamientoParcela() {
		var txtSelectedValuesObj = document.getElementById('aprov');
		var i;
		for (i = 0; i < txtSelectedValuesObj.options.length; i++) {
			if (txtSelectedValuesObj.options[i].value ==
<%=parcela.getAprovechamiento()%>
	) {
				txtSelectedValuesObj.options[i].selected = 'selected';
			}
		}
	}

	function cargarTipoInvParcela() {
		var txtSelectedValuesObj = document.getElementById('tipoinv');
		var i;
		for (i = 0; i < txtSelectedValuesObj.options.length; i++) {
			if (txtSelectedValuesObj.options[i].value ==
<%=parcela.getInventario()%>
	) {
				txtSelectedValuesObj.options[i].selected = 'selected';
			}
		}
	}

	function cargarPropositoParcela() {
		var txtSelectedValuesObj = document.getElementById('proposito');
		var i;
		for (i = 0; i < txtSelectedValuesObj.options.length; i++) {
			if (txtSelectedValuesObj.options[i].value ==
<%=parcela.getProposito()%>
	) {
				txtSelectedValuesObj.options[i].selected = 'selected';
			}
		}
	}

	function adicionarBoton(contenedor, clave, bandera) {
		var clave1 = '';
		if (clave == 'ColEnc') {
			clave = 'Col';
			clave1 = 'Enc';
		}
		var cont = document.getElementById(contenedor);
		var boton = document.createElement('input');
		boton.type = "button";
		boton.id = 'boton' + clave;
		boton.name = 'boton' + clave;
		boton.value = 'Eliminar';
		if (boton.addEventListener) {
			boton
					.addEventListener(
							"click",
							function() {
								document.getElementById('cons' + clave).value = '';
								document.getElementById('nom' + clave).value = '';
								document.getElementById('tel' + clave).value = '';
								document.getElementById('cor' + clave).value = '';
								if (clave1 != '') {
									document.getElementById(clave + clave1).style.display = 'none';
									document.getElementById('cons' + clave1).value = '';
									document.getElementById('nom' + clave1).value = '';
									document.getElementById('tel' + clave1).value = '';
									document.getElementById('cor' + clave1).value = '';
									document.getElementById('divContactoEnc').style.display = 'none';
								} else
									document.getElementById(clave).style.display = 'none';
								document.getElementById(contenedor).style.display = 'none';
								document.getElementById(bandera).value = '';
							}, true);
		} else {
			boton
					.attachEvent(
							'onclick',
							function() {
								document.getElementById('cons' + clave).value = '';
								document.getElementById('nom' + clave).value = '';
								document.getElementById('tel' + clave).value = '';
								document.getElementById('cor' + clave).value = '';
								if (clave1 != '') {
									document.getElementById(clave + clave1).style.display = 'none';
									document.getElementById('cons' + clave1).value = '';
									document.getElementById('nom' + clave1).value = '';
									document.getElementById('tel' + clave1).value = '';
									document.getElementById('cor' + clave1).value = '';
									document.getElementById('divContactoEnc').style.display = 'none';
								} else
									document.getElementById(clave).style.display = 'none';
								document.getElementById(contenedor).style.display = 'none';
								document.getElementById(bandera).value = '';
							});
		}
		cont.appendChild(boton);
		if (clave1 != '') {
			var cont1 = document.getElementById('divContactoEnc');
			var boton1 = document.createElement('input');
			boton1.type = "button";
			boton1.id = 'boton' + clave1;
			boton1.name = 'boton' + clave1;
			boton1.value = 'Eliminar';
			if (boton1.addEventListener) {
				boton1
						.addEventListener(
								"click",
								function() {
									document.getElementById('cons' + clave1).value = '';
									document.getElementById('nom' + clave1).value = '';
									document.getElementById('tel' + clave1).value = '';
									document.getElementById('cor' + clave1).value = '';
									document.getElementById('divContactoEnc').style.display = 'none';
								}, true);
			} else {
				boton1
						.attachEvent(
								'onclick',
								function() {
									document.getElementById('cons' + clave1).value = '';
									document.getElementById('nom' + clave1).value = '';
									document.getElementById('tel' + clave1).value = '';
									document.getElementById('cor' + clave1).value = '';
									document.getElementById('divContactoEnc').style.display = 'none';
								});
			}
			cont1.appendChild(boton1);
		}

	}
	
	function obtenerNombreArchivo() {
		document.getElementById('datosArchivo').style.display = 'block';
		document.getElementById('nomArchivo').value = document
				.getElementById('archivo').value;
	}
	function registrarContacto(tipoCont) {
		window.scrollTo(0, 0);
		document.getElementById("fondoBloqueo").style.display = "block";
		document.getElementById("popup-registrarContacto").style.display = "block";
		document.getElementById("tipoContactoRegistro").value=tipoCont;
	}

	function cerrarRegistroContacto() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popup-registrarContacto").style.display = "none";
	}


	function adicionarDatoContacto(sel,clase, cons, nom,tel,cor, tipoCont){
		if(sel.value==1){

		}else if(sel.value==2){
			document.getElementById(clase).value='<%=Constantes.personaJuridica%>';
			document.getElementById(cons).value=IDEAM;
			document.getElementById(nom).value='<%=ideam.getNombre()%>';
			document.getElementById(tel).value='<%=ideam.getTelefono()%>';
			document.getElementById(cor).value='<%=ideam.getCorreo()%>';
		}else if(sel.value==3){
			registrarContacto(tipoCont);
		}else if(sel.value==4){
			buscarContacto(tipoCont);
		}
	}

	function buscarContacto(tipoCont) {
		window.scrollTo(0, 0);
		document.getElementById("fondoBloqueo").style.display = "block";
		document.getElementById("popup-buscarContacto").style.display = "block";
		document.getElementById("tipoContactoBusqueda").value=tipoCont;
		idActual=1;
		resultadosContactos(contacts.length, 'resultados','consultas');
		cargarDatos(contacts,'consultas',2,titulos,null,0,5);
		crearPaginas(contacts.length,'consultas',1);
	}

	function cerrarBusquedaContacto() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popup-buscarContacto").style.display = "none";
	}

	function registroContacto(){
		var tipo = document.getElementById("tipoContactoRegistro").value;
		var pref;
		if(tipo==1)
			pref='FGDA';
		else if(tipo==2)
			pref='prop';
		else if(tipo==3)
			pref='cus';
		else if(tipo==4)
			pref='Inv';
		else if(tipo==5)
			pref='Bri';
		else if(tipo==6)
			pref='Sup';
		else if(tipo==7)
			pref='Col';
		else if(tipo==8)
			pref='Enc';
		
		document.getElementById("registro"+pref).value=1;
		document.getElementById("clase"+pref).value=document.getElementById("selContactoRegistro").value;
		if(tipo>3){
			document.getElementById('divNom'+pref).style.display='block';
			document.getElementById('divTel'+pref).style.display='block';
			document.getElementById('divCor'+pref).style.display='block';
			document.getElementById('evento'+pref).style.display='block';
			document.getElementById('is'+pref).value	= 1;
		}
		if(document.getElementById("selContactoRegistro").value==<%=Constantes.personaJuridica%>){
			document.getElementById("nomOrg"+pref).value=document.getElementById("nomOrg").value;
			document.getElementById("nom"+pref).value=document.getElementById("nomOrg").value;
			document.getElementById("secOrg"+pref).value=document.getElementById("sector").value;
			document.getElementById("telOrg"+pref).value=document.getElementById("telOrg").value;
			document.getElementById("tel"+pref).value=document.getElementById("telOrg").value;
			document.getElementById("dirOrg"+pref).value=document.getElementById("dir").value;
			document.getElementById("corOrg"+pref).value=document.getElementById("corCont").value;
			document.getElementById("cor"+pref).value=document.getElementById("corCont").value;
			document.getElementById("paiOrg"+pref).value=document.getElementById("paisCont").value;
		}else{
			document.getElementById("nomCon"+pref).value=document.getElementById("nomCon").value;
			document.getElementById("nom"+pref).value=document.getElementById("nomOrg").value;
			document.getElementById("fijCon"+pref).value=document.getElementById("fijoCon").value;
			document.getElementById("celCon"+pref).value=document.getElementById("telCon").value;
			document.getElementById("tel"+pref).value=document.getElementById("telOrg").value;
			document.getElementById("corCon"+pref).value=document.getElementById("corCont").value;
			document.getElementById("cor"+pref).value=document.getElementById("corCont").value;
			document.getElementById("paiCon"+pref).value=document.getElementById("paisCont").value;
		}
		
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popup-registrarContacto").style.display = "none";
	}

	function seleccionarContacto(){
		var tipo = document.getElementById("tipoContactoBusqueda").value;
		var pref;
		if(tipo==1)
			pref='FGDA';
		else if(tipo==2)
			pref='prop';
		else if(tipo==3)
			pref='cus';
		else if(tipo==4)
			pref='Inv';
		else if(tipo==5)
			pref='Bri';
		else if(tipo==6)
			pref='Sup';
		else if(tipo==7)
			pref='Col';
		else if(tipo==8)
			pref='Enc';
		
		var i;
		
		document.getElementById("registro"+pref).value=2;
		document.getElementById("clase"+pref).value=document.getElementById("selContactoBusqueda").value;
		var radios = document.getElementsByTagName('input');
		var idSeleccionado;
		for (var i = 0; i < radios.length; i++) {
		    if (radios[i].type === 'radio' && radios[i].checked) {
		    	idSeleccionado = radios[i].value;  
		    	break;     
		    }
		}
		if(document.getElementById("selContactoBusqueda").value==<%=Constantes.personaJuridica%>){
			for(i=0;i<orgs.length;i++){
				contacto = orgs[i].split(",");
				if(contacto[0]==idSeleccionado){
					document.getElementById("nom"+pref).style.display='block';
					document.getElementById("tel"+pref).style.display='block';
					document.getElementById("cor"+pref).style.display='block';
					document.getElementById("nom"+pref).value=contacto[1];
					document.getElementById("tel"+pref).value=contacto[2];
					document.getElementById("cor"+pref).value=contacto[3];
					document.getElementById("cons"+pref).value=contacto[0];
					break;
				}
			}
		}else{
			for(i=0;i<contacts.length;i++){
				contacto = contacts[i].split(",");
				if(contacto[0]==idSeleccionado){
					document.getElementById("nom"+pref).style.display='block';
					document.getElementById("tel"+pref).style.display='block';
					document.getElementById("cor"+pref).style.display='block';
					document.getElementById("nom"+pref).value=contacto[1];
					document.getElementById("tel"+pref).value=contacto[2];
					document.getElementById("cor"+pref).value=contacto[3];
					document.getElementById("cons"+pref).value=contacto[0];
					break;
				}
			}
		}
		if(tipo>3){
			document.getElementById('divNom'+pref).style.display='block';
			document.getElementById('divTel'+pref).style.display='block';
			document.getElementById('divCor'+pref).style.display='block';
			document.getElementById("evento"+pref).style.display='block';
			document.getElementById('is'+pref).value	= 1;
		}
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popup-buscarContacto").style.display = "none";
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

	function cambiarContactos(tipoContacto){
		if(tipoContacto.value==1){
			resultadosContactos(contacts.length, 'resultados','consultas');
			cargarDatos(contacts,'consultas',2,titulos,null,0,5);
			crearPaginas(contacts.length,'consultas',1);
		}else{
			resultadosContactos(orgs.length, 'resultados','consultas');
			cargarDatos(orgs,'consultas',2,titulos,null,0,5);
			crearPaginas(orgs.length,'consultas',1);
		}
	}

	function eliminarContacto(pref){
		document.getElementById("nom"+pref).value="";
		document.getElementById("tel"+pref).value="";
		document.getElementById("cor"+pref).value="";
		document.getElementById("cons"+pref).value="";
		document.getElementById('is'+pref).value = 0;
		document.getElementById('divNom'+pref).style.display='none';
		document.getElementById('divTel'+pref).style.display='none';
		document.getElementById('divCor'+pref).style.display='none';
		document.getElementById("evento"+pref).style.display='none';
	}
</script>
</head>
<body 
	onload="cargarContacto();cargarOrganizacion();cargarDatosParcela();"
	 class='sidebarlast front'>
	<div id="fondoBloqueo"
		style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; display: none;">
		<!--Sin contenido -->
	</div>
	<div id="popup-registrarContacto"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 700px; display: none;">
		<div id="cboxContent">
			<div id="cboxTitle">Registrar contacto</div>
			<div id="cboxLoadedContent">
				<div class="form-group">
					<label for="exampleInputEmail1">Tipo de contacto:</label>
					<input type="hidden" id="tipoContactoRegistro">
					<div class="select-wrapper">
						<select name="selContactoRegistro" id="selContactoRegistro"
							onchange="mostrarFormulario(this,'orga','perso','misc');"
							title="Tipo de persona al cual pertence el contacto">
							<option selected="selected" value="0">Seleccione</option>
							<%=CargaDatosInicialParcelas.getTipoPersona()%>
						</select>
					</div>
				</div>

				<div id="orga" style="display: none;">
					<div class="form-group">
						<label for="exampleInputEmail1">*Organizaci&oacute;n: </label> <input
							type="text" id="nomOrg" name="nomOrg" class="form-control"
							title="Nombre de la organización fuente generadora de los datos de la parcela">
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Sector:</label>
						<div class="select-wrapper">
							<select name="sector" id="sector" name="sector"
								title="Sector al que pertenece la fuente generadora de datos">
								<option value="0">Seleccione</option>
								<%=CargaDatosInicialParcelas.consultarSector()%>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Tel&eacute;fono:</label> <input
							type="text" id="telOrg" name="telOrg" class="form-control"
							title="Número de teléfono principal  del contacto en la fuente generadora de datos">
					</div>
					<div>
						<div>
							<label>Direcci&oacute;n:</label>
						</div>
						<div>
							<textarea rows="3" cols="40" id="dir" name="dir"
								title="Dirección de la fuente generadora de datos"></textarea>
						</div>
					</div>
				</div>
				<div id="perso" style="display: none;">
					<div>
						<div>
							<label>*Nombre: </label>
						</div>
						<div>
							<input type="text" id="nomCon" name="nomCon"
								title="Nombre completo de la fuente generadora de los datos de la parcela">
						</div>
					</div>
					<div>
						<div>
							<label>Tel&eacute;fono:</label>
						</div>
						<div>
							<input type="text" id="fijoCon" name="fijoCon"
								title="Número de teléfono principal  del contacto en la fuente generadora de datos">
						</div>
					</div>
					<div>
						<div>
							<label>Celular:</label>
						</div>
						<div>
							<input type="text" id="telCon" name="telCon"
								title="Número de celular  del contacto en la fuente generadora de datos">
						</div>
					</div>
				</div>
				<div id="misc" style="display: none;">
					<div>
						<div>
							<label>Correo electr&oacute;nico:</label>
						</div>
						<div>
							<input type="text" id="corCont" name="corCont"
								title="Correo electrónico del contacto en la fuente generadora de datos">
						</div>
					</div>
					<div>
						<div>
							<label>Pa&iacute;s:</label>
						</div>
						<div class="select-wrapper">
							<select name="paisCont" id="paisCont"
								title="País en el que se ubica la fuente generadora de datos">
								<option value="0">Seleccione</option>
								<%=CargaDatosInicialParcelas.getPaises()%>
							</select>
						</div>
					</div>
					<br> <input type="button" value="Aceptar"
						onclick="registroContacto()">
				</div>
				<br> <input type="button" value="Volver"
					onclick="cerrarRegistroContacto()">
			</div>
		</div>
	</div>
	<div id="popup-buscarContacto"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 700px; display: none;">
		<div id="cboxContent">
			<div id="cboxTitle">Buscar Contacto</div>
			<div id="cboxLoadedContent">
				<div>
					<div>
					<input type="hidden" id="tipoContactoBusqueda">
						<label>Tipo de contacto:</label>
					</div>
					<div class="select-wrapper">
						<select id="selContactoBusqueda" name="selContactoBusqueda"
							title="Tipo de persona al cual pertence el contacto"
							onchange="cambiarContactos(this);">
							<%=CargaDatosInicialParcelas.getTipoPersona()%>
						</select>
					</div>
				</div>
				<div>
					<div>
						<label>Nombre del contacto:</label>
					</div>
					<div>
						<input type="text" id="nomContacto"
							title="Nombre del contacto a buscar">
					</div>
				</div>
				<div>
					<div>
						<label>Correo electronico del contacto:</label>
					</div>
					<div>
						<input type="text" id="corContacto"
							title="Correo electronico del contacto a buscar">
					</div>
				</div>
				<div>
					<input id="filtro" type="button" value="Buscar"
						onclick="filtrarContacto('nomContacto','corContacto','tipoContacto');">
				</div>
				<div id="resultados">
					<div id="consultas"></div>
					<div id="paginador" class="pager"></div>
				</div>
				<div>
					<input
						id="okBusCon" type="button" value="Seleccionar"
						onclick="seleccionarContacto()">
				</div>
				<br> <input type="button" value="Volver"
					onclick="cerrarBusquedaContacto();">
			</div>
		</div>
	</div>
	<form id="modificarParcela" method="post" enctype="multipart/form-data">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina"> <input type="hidden"
			id="consecutivoParcela" name="consecutivoParcela"> <input
			type="hidden" id="usoinfo" name="usoinfo"> <input
			type="hidden" id="tipolicencia" name="tipolicencia">
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

									<div class="content">
										<div class="content">
											<div id="accordion">
												<h3>Datos Básicos</h3>
												<div class="form-datos-parcela form-columnx2" role="form">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.codigo")%>:</label>
														<input type="text" class="form-control"
															value="<%=parcela.getConsecutivo()%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.nombre")%>:</label>
														<input type="text" class="form-control" id="nombreParcela" name="nombreParcela"
															id="exampleInputEmail1" title=""
															value="<%=parcela.getNombre()%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.tipo")%>:</label>
														<div class="select-wrapper">
															<select id="temporalidad" name="temporalidad"
																title="Registre si se evidencia algun aprovechamiento forestal ">
																<%=CargaDatosInicialParcelas.getTemporalidad()%>
															</select>
														</div>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.aprov")%>:</label>
														<div class="select-wrapper">
															<select id="aprov" name="aprov"
																title="Registre si se evidencia algun aprovechamiento forestal ">
																<option value="0">No</option>
																<option value="1">Si</option>
															</select>
														</div>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.inventario")%>:</label>
														<div class="select-wrapper">
															<select id="tipoinv" name="tipoinv"
																title="Tipo de inventario forestal que se maneja en la parcela">
																<%=CargaDatosInicialParcelas.getTipoInventario()%>
															</select>
														</div>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.fechaEstablecimiento")%>:</label>
														<input type="text" id="fInicial" name="fInicial"
															value="<%=formato.format(Util.convertToDate(parcela
					.getFechaEstablecimiento()))%>">
													</div>
													<div class="form-group item-textarea">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.descripcion")%>:</label>
														<textarea id="descparcela" name="descparcela" title="" rows="2"
															cols="8"><%=parcela.getDescripcion()%></textarea>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.proposito")%>:</label>
														<div class="select-wrapper">
															<select id="proposito" name="proposito"
																title="Proposito especifico designado a la parcela">
																<%=CargaDatosInicialParcelas.getPropositoParcela()%>
															</select>
														</div>
													</div>
													<div class="form-group item-textarea">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.observaciones")%>:</label>
														<textarea id="observaciones" name="observaciones" title="" rows="2"
															cols="8"><%=parcela.getObservaciones()%></textarea>
													</div>
													<div class="form-group">
														<label for="exampleInputFile"><%=msj.getString("modificar.parcela.archivo")%>:</label>
														<div class="select-wrapper">
															<select id="tieneImagen" name="tieneImagen"
																title="Archivo Zip con imagenes de la parcela"
																onchange="agregarArchivo(this);">
																<option id="no" selected="selected" value="0">No</option>
																<option id="si" value="1">Si</option>
															</select>
														</div>
													</div>

													<div id="datosArchivo" style="display: none;"
														class="form-group">
														<label for="exampleInputFile"><%=msj.getString("consulta.parcela.archivo")%>:</label>
														<a class="ico-zip"><%=parcela.getNombreImagen() == "" ? "Imagenes de la parcela"
					: "sin"%></a>
													</div>
													<div id="popup-file" style="display: none;"
														class="form-group">
														<label for="exampleInputFile"><%=msj.getString("consulta.parcela.archivo")%>:</label>
														<input type="file" id="archivo" name="archivo" />
													</div>
												</div>
												<h3><%=msj.getString("titulos.localizacion")%></h3>
												<div class="form-localizacion" role="form">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.geometria")%>:</label>
														<div class="select-wrapper">
															<input type="hidden" name="valor" id="valor">
															<select name="geometria" id="geometria"
																onChange="seleccion();"
																title="Tipo de geometria que describe la forma de la parcela">
																<option value="0">Seleccione...</option>
																<option value="1">Punto</option>
																<option value="3">Polígono</option>
															</select>
														</div>
													</div>
													<div id="punto" class="form-group" style="display: none;"></div>
													<div id="poligono" class="form-group"
														style="display: none;">
														<label for="exampleInputFile">Largo(Mts.):</label> <input
															type="text" id="largo" name="largo" value=""
															class="form-control"
															title="Medida de largo promedio de la parcela (en metros)" />
														<label for="exampleInputFile">Ancho(Mts.):</label> <input
															type="text" id="ancho" name="ancho" value=""
															class="form-control"
															title="Medida del ancho promedio de la parcela (en metros)" />
														<div class="form-actions">
															<input type="button" value="Crear"
																onclick="crear(null,null)" />
														</div>
													</div>
													<div id="visualizar" class="form-actions"
														style="display: none">
														<input type="button" value="Visualizar"
															onclick="enviar(1);"
															title="Vista previa del poligono generado que describe la forma de la parcela">
													</div>
												</div>
												<h3><%=msj.getString("titulos.contactos")%></h3>
												<div class="form-localizacion form-columnx2" role="form">
													<div id="fgda" class="form-group">
														<input type="hidden" id="consFGDA" name="consFGDA">
														<input type="hidden" id="claseFGDA" name="claseFGDA">
														
														<input type="hidden" id="registroFGDA" name="registroFGDA">
														<input type="hidden" id="nomOrgFGDA" name="nomOrgFGDA">
														<input type="hidden" id="secOrgFGDA" name="secOrgFGDA">
														<input type="hidden" id="telOrgFGDA" name="telOrgFGDA">
														<input type="hidden" id="dirOrgFGDA" name="dirOrgFGDA">
														<input type="hidden" id="corOrgFGDA" name="corOrgFGDA">
														<input type="hidden" id="paiOrgFGDA" name="paiOrgFGDA">
														<input type="hidden" id="nomConFGDA" name="nomConFGDA">
														<input type="hidden" id="fijConFGDA" name="fijConFGDA">
														<input type="hidden" id="celConFGDA" name="celConFGDA">
														<input type="hidden" id="corConFGDA" name="corConFGDA">
														<input type="hidden" id="paiConFGDA" name="paiConFGDA">
														
														<label for="exampleInputFile"><%=msj.getString("consulta.parcela.fgda")%>:</label>
														<div class="select-wrapper">
															<select name="selfgda" id="selfgda" style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseFGDA', 'consFGDA','nomFGDA','telFGDA','corFGDA',1)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regFGDA" value="3">Registrar
																	Contacto</option>
																<option id="busFGDA" value="4">Buscar Contacto</option>
															</select>
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomFGDA"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telFGDA"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corFGDA"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
													</div>
													<div id="propietario" class="form-group">
														<label for="exampleInputFile">Propietario de los
															datos:</label>
														<div class="select-wrapper">
															<select name="selContPro" id="selContPro"
																style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseprop', 'consprop','nomprop','telprop','corprop',2)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regContactoPro" value="3">Registrar
																	Contacto</option>
																<option id="busContactoPro" value="4">Buscar
																	Contacto</option>
															</select>
														</div>
														<input type="hidden" id="consprop" name="consprop">
														<input type="hidden" id="claseprop" name="claseprop">
														
														<input type="hidden" id="registroprop" name="registroprop">
														<input type="hidden" id="nomOrgprop" name="nomOrgprop">
														<input type="hidden" id="secOrgprop" name="secOrgprop">
														<input type="hidden" id="telOrgprop" name="telOrgprop">
														<input type="hidden" id="dirOrgprop" name="dirOrgprop">
														<input type="hidden" id="corOrgprop" name="corOrgprop">
														<input type="hidden" id="paiOrgprop" name="paiOrgprop">
														<input type="hidden" id="nomConprop" name="nomConprop">
														<input type="hidden" id="fijConprop" name="fijConprop">
														<input type="hidden" id="celConprop" name="celConprop">
														<input type="hidden" id="corConprop" name="corConprop">
														<input type="hidden" id="paiConprop" name="paiConprop">
														
														<div class="form-group">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomprop"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telprop"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corprop"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
													</div>
													<div id="custodio" class="form-group">
														<label for="exampleInputFile">Custodio de los
															datos:</label>
														<div class="select-wrapper">
															<select name="selCustodio" id="selCustodio"
																style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'clasecus', 'conscus','nomcus','telcus','corcus',3)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regContactoPro" value="3">Registrar
																	Contacto</option>
																<option id="busContactoPro" value="4">Buscar
																	Contacto</option>
															</select>
														</div>
														<input type="hidden" id="clasecus" name="clasecus">
														<input type="hidden" id="conscus" name="conscus">
														
														<input type="hidden" id="registrocus" name="registrocus">
														<input type="hidden" id="nomOrgcus" name="nomOrgcus">
														<input type="hidden" id="secOrgcus" name="secOrgcus">
														<input type="hidden" id="telOrgcus" name="telOrgcus">
														<input type="hidden" id="dirOrgcus" name="dirOrgcus">
														<input type="hidden" id="corOrgcus" name="corOrgcus">
														<input type="hidden" id="paiOrgcus" name="paiOrgcus">
														<input type="hidden" id="nomConcus" name="nomConcus">
														<input type="hidden" id="fijConcus" name="fijConcus">
														<input type="hidden" id="celConcus" name="celConcus">
														<input type="hidden" id="corConcus" name="corConcus">
														<input type="hidden" id="paiConcus" name="paiConcus">
														
														<div class="form-group">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomcus"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telcus"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corcus"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
													</div>
												</div>
												<h3><%=msj.getString("titulos.contactos")%></h3>
												<div class="form-localizacion form-columnx2" role="form">
													<div id="investigador" class="form-group">
														<input type="hidden" name="isInv" id="isInv" /> 
														<input type="hidden" id="consInv" name="consInv"> 
														<input type="hidden" id="claseInv" name="claseInv">
														
														<input type="hidden" id="registroInv" name="registroInv">
														<input type="hidden" id="nomOrgInv" name="nomOrgInv">
														<input type="hidden" id="secOrgInv" name="secOrgInv">
														<input type="hidden" id="telOrgInv" name="telOrgInv">
														<input type="hidden" id="dirOrgInv" name="dirOrgInv">
														<input type="hidden" id="corOrgInv" name="corOrgInv">
														<input type="hidden" id="paiOrgInv" name="paiOrgInv">
														<input type="hidden" id="nomConInv" name="nomConInv">
														<input type="hidden" id="fijConInv" name="fijConInv">
														<input type="hidden" id="celConInv" name="celConInv">
														<input type="hidden" id="corConInv" name="corConInv">
														<input type="hidden" id="paiConInv" name="paiConInv">
														 
														<label for="exampleInputFile"><%=msj.getString("consulta.parcela.fgda")%>:</label>
														<div class="select-wrapper">
															<select name="selInv" id="selInv" style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseInv', 'consInv','nomInv','telInv','corInv',4)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regFGDA" value="3">Registrar
																	Contacto</option>
																<option id="busFGDA" value="4">Buscar Contacto</option>
															</select>
														</div>
														<div class="form-group" style="display: none"
															id="divNomInv">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomInv"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divTelInv">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telInv"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divCorInv">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corInv"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
														<div class="form-actions" style="display: none" id="eventoInv">
															<input type="button" value="eliminar"
																onclick="eliminarContacto('Inv')">
														</div>
													</div>
													<div id="brigadista" class="form-group">
														<input type="hidden" name="isBri" id="isBri" />
														
														<input type="hidden" id="registroBri" name="registroBri">
														<input type="hidden" id="nomOrgBri" name="nomOrgBri">
														<input type="hidden" id="secOrgBri" name="secOrgBri">
														<input type="hidden" id="telOrgBri" name="telOrgBri">
														<input type="hidden" id="dirOrgBri" name="dirOrgBri">
														<input type="hidden" id="corOrgBri" name="corOrgBri">
														<input type="hidden" id="paiOrgBri" name="paiOrgBri">
														<input type="hidden" id="nomConBri" name="nomConBri">
														<input type="hidden" id="fijConBri" name="fijConBri">
														<input type="hidden" id="celConBri" name="celConBri">
														<input type="hidden" id="corConBri" name="corConBri">
														<input type="hidden" id="paiConBri" name="paiConBri">
														
														<label for="exampleInputFile">Propietario de los
															datos:</label>
														<div class="select-wrapper">
															<select name="selBri" id="selBri" style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseBri', 'consBri','nomBri','telBri','corBri',5)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regBri" value="3">Registrar
																	Contacto</option>
																<option id="busBri" value="4">Buscar Contacto</option>
															</select>
														</div>
														<input type="hidden" id="consBri" name="consBri">
														<input type="hidden" id="claseBri" name="claseBri">
														<div class="form-group" style="display: none"
															id="divNomBri">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomBri"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divTelBri">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telBri"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divCorBri">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corBri"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
														<div class="form-actions" style="display: none" id="eventoBri">
															<input type="button" value="eliminar"
																onclick="eliminarContacto('Bri')">
														</div>
													</div>
													<div id="supervisor" class="form-group">
														<input type="hidden" name="isSup" id="isSup" />
														
														<input type="hidden" id="registroSup" name="registroSup">
														<input type="hidden" id="nomOrgSup" name="nomOrgSup">
														<input type="hidden" id="secOrgSup" name="secOrgSup">
														<input type="hidden" id="telOrgSup" name="telOrgSup">
														<input type="hidden" id="dirOrgSup" name="dirOrgSup">
														<input type="hidden" id="corOrgSup" name="corOrgSup">
														<input type="hidden" id="paiOrgSup" name="paiOrgSup">
														<input type="hidden" id="nomConSup" name="nomConSup">
														<input type="hidden" id="fijConSup" name="fijConSup">
														<input type="hidden" id="celConSup" name="celConSup">
														<input type="hidden" id="corConSup" name="corConSup">
														<input type="hidden" id="paiConSup" name="paiConSup">
														
														<label for="exampleInputFile">Custodio de los
															datos:</label>
														<div class="select-wrapper">
															<select name="selSup" id="selSup" style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseSup', 'consSup','nomSup','telSup','corSup',6)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regContactoPro" value="3">Registrar
																	Contacto</option>
																<option id="busContactoPro" value="4">Buscar
																	Contacto</option>
															</select>
														</div>
														<input type="hidden" id="consSup" name="consSup">
														<input type="hidden" id="claseSup" name="claseSup">
														<div class="form-group" style="display: none"
															id="divNomSup">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomSup"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divTelSup">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telSup"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divCorSup">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corSup"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
														<div class="form-actions" style="display: none" id="eventoSup">
															<input type="button" value="eliminar"
																onclick="eliminarContacto('Sup')">
														</div>
													</div>
													<div id="Col" class="form-group">
														<input type="hidden" name="isCol" id="isCol" />
														
														<input type="hidden" id="registroCol" name="registroCol">
														<input type="hidden" id="nomOrgCol" name="nomOrgCol">
														<input type="hidden" id="secOrgCol" name="secOrgCol">
														<input type="hidden" id="telOrgCol" name="telOrgCol">
														<input type="hidden" id="dirOrgCol" name="dirOrgCol">
														<input type="hidden" id="corOrgCol" name="corOrgCol">
														<input type="hidden" id="paiOrgCol" name="paiOrgCol">
														<input type="hidden" id="nomConCol" name="nomConCol">
														<input type="hidden" id="fijConCol" name="fijConCol">
														<input type="hidden" id="celConCol" name="celConCol">
														<input type="hidden" id="corConCol" name="corConCol">
														<input type="hidden" id="paiConCol" name="paiConCol">
														
														<label for="exampleInputFile">Custodio de los
															datos:</label>
														<div class="select-wrapper">
															<select name="selCol" id="selCol" style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseCol', 'consCol','nomCol','telCol','corCol',7)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option id="regContactoPro" value="3">Registrar
																	Contacto</option>
																<option id="busContactoPro" value="4">Buscar
																	Contacto</option>
															</select>
														</div>
														<input type="hidden" id="consCol" name="consCol">
														<input type="hidden" id="claseCol" name="claseCol">
														<div class="form-group" style="display: none"
															id="divNomCol">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomCol"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divTelCol">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telCol"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divCorCol">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corCol"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
														<div class="form-actions" style="display: none" id="eventoCol">
															<input type="button" value="eliminar"
																onclick="eliminarContacto('Col');eliminarContacto('Enc');">
														</div>
													</div>
													<div id="Enc" class="form-group">
														<label for="exampleInputFile">Custodio de los
															datos:</label>
														<div class="select-wrapper">
															<select name="selEnc" id="selEnc" style="width: 300px;"
																title="Opciones disponibles para la selección del contacto"
																onchange="adicionarDatoContacto(this,'claseEnc', 'consEnc','nomEnc','telEnc','corEnc',8)">
																<option value="0">Seleccione</option>
																<option value="1">Usuario Actual</option>
																<option value="2">IDEAM</option>
																<option value="3">Registrar Contacto</option>
																<option value="4">Buscar Contacto</option>
															</select>
														</div>
														<input type="hidden" name="isEnc" id="isEnc" />
														<input type="hidden" id="consEnc" name="consEnc">
														<input type="hidden" id="claseEnc" name="claseEnc">
														
														<input type="hidden" id="registroEnc" name="registroEnc">
														<input type="hidden" id="nomOrgEnc" name="nomOrgEnc">
														<input type="hidden" id="secOrgEnc" name="secOrgEnc">
														<input type="hidden" id="telOrgEnc" name="telOrgEnc">
														<input type="hidden" id="dirOrgEnc" name="dirOrgEnc">
														<input type="hidden" id="corOrgEnc" name="corOrgEnc">
														<input type="hidden" id="paiOrgEnc" name="paiOrgEnc">
														<input type="hidden" id="nomConEnc" name="nomConEnc">
														<input type="hidden" id="fijConEnc" name="fijConEnc">
														<input type="hidden" id="celConEnc" name="celConEnc">
														<input type="hidden" id="corConEnc" name="corConEnc">
														<input type="hidden" id="paiConEnc" name="paiConEnc">
														
														<div class="form-group" style="display: none"
															id="divNomEnc">
															<label for="exampleInputFile">Nombre:</label> <input
																type="text" readonly="readonly" id="nomEnc"
																title="Nombre del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divTelEnc">
															<label for="exampleInputFile">Telefono: </label> <input
																type="text" readonly="readonly" id="telEnc"
																title="Telefono del contacto" class="form-control">
														</div>
														<div class="form-group" style="display: none"
															id="divCorEnc">
															<label for="exampleInputFile">Correo Electronico:
															</label> <input type="text" readonly="readonly" id="corEnc"
																title="Correo electronico del contacto"
																class="form-control">
														</div>
														<div class="form-actions" style="display: none" id="eventoEnc">
															<input type="button" value="eliminar"
																onclick="eliminarContacto('Col');eliminarContacto('Enc');">
														</div>
													</div>
												</div>
											</div>
											<div class="form-actions">
												<input type="button" value="regresar"
													onclick="javascript:history.back()">
												<input type="button" value="Modificar"
													onclick="enviar()">
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