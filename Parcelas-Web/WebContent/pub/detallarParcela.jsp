<%@page import="java.net.URI"%>
<%@page import="java.awt.Desktop"%>   
<%@page import="java.net.URL"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="co.gov.ideamredd.entities.MetadataView"%>
<%@page import="co.gov.ideamredd.dao.Constantes"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicialParcelas"%>
<%@page import="co.gov.ideamredd.entities.Noticias"%>
<%@page import="java.text.SimpleDateFormat"%>
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
<%@page import="co.gov.ideamredd.entities.Parcela"%>
<%@page import="co.gov.ideamredd.dao.ObtenerParcelaConsulta"%>
<%@page import="co.gov.ideamredd.entities.Organizacion"%>
<%@page import="co.gov.ideamredd.entities.Contacto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.dao.TabsLicenciasDescarga"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class='no-js'>
<head>
<title>Detallar Parcela</title>
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
	String urlDescarga = basePath+"pub/descarga.jsp?a="+Util.encriptar(parcela.getRutaImagen()+parcela.getNombre()+"/"+parcela.getNombreImagen()+";"+parcela.getNombreImagen());
	ArrayList<TipoBosque> bosques = ObtenerParcelaConsulta.consultaTipoBosqueParcela(Integer.valueOf(idParcela));
	ArrayList<Depto> deptos = ObtenerParcelaConsulta.consultaDeptoParcela(Integer.valueOf(idParcela));
	ArrayList<Municipios> municipios = ObtenerParcelaConsulta.consultaMunicipioParcela(Integer.valueOf(idParcela));
	ArrayList<CAR> cars = ObtenerParcelaConsulta.consultaCarParcela(Integer.valueOf(idParcela));
	ArrayList<ContactoParcela> contactos = ObtenerParcelaConsulta.consultaContactosParcelaId(Integer.valueOf(idParcela));
	ArrayList<Object> cont = ObtenerParcelaConsulta.obtenerContactosParcela(contactos);
	MetadataView meta = ObtenerParcelaConsulta.consultaInfoMetadata(Integer.valueOf(idParcela));
	ArrayList<String> metadatoInfo = ObtenerParcelaConsulta.consultarDatosMetadatos();
	Pais pais = ObtenerParcelaConsulta.consultarPaisParcela(Integer.valueOf(idParcela));
	Temporalidad temp = ObtenerParcelaConsulta.consultarTemporalidadParcela(parcela.getTemporalidad());
	ArrayList<String> fisiografia = ObtenerParcelaConsulta.consultaFisiografiaParcela(Integer.valueOf(idParcela));
	Proposito proposito =  ObtenerParcelaConsulta.consultarPropositoParcela(parcela.getProposito());
	TipoInventario tipoInventario = ObtenerParcelaConsulta.consultarTipoInventarioParcela(parcela.getInventario());
	String[] geometria = ObtenerParcelaConsulta.consultarGeoParcela(Integer.valueOf(idParcela)); 
	String[] listaImagenes = ObtenerParcelaConsulta.copiarImagenCarrusel(parcela.getRutaImagen()+parcela.getNombre()+"/");	
	if(geometria[0].equals(Constantes.punto))
		geometria[0]="Punto";
	else if(geometria[0].equals(Constantes.linea))
		geometria[0]="Linea";
	else if(geometria[0].equals(Constantes.poligono))
		geometria[0]="Poligono";
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
<link rel="stylesheet" href="../css/slippry.css" />
<script type="text/javascript" src="../js/datum-validation.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="../js/slippry.min.js"></script>

<script type="text/javascript">
$(function() {
    $( "#accordion" ).accordion({
      heightStyle: "content"
    });
  });

$(function(){
    $('#slippry-demo').slippry();
   });

var puntos = new Array();
var cont=0;
var doc = new Array();
var indice=0;
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

var indicesCont =new Array();
var i =0;
var datos = new Array();
function cargarContactos() {
	var nom;
	var tel;
	var cor;
	var mov;
	var pais;
	var dir;
	var sec;
	var consecutivoCont;
<%for(int i=0;i<cont.size();i++){ 
		if(cont.get(i) instanceof Contacto){%>
			consecutivoCont='<%=((Contacto)cont.get(i)).getConsecutivo()%>'
			nom='<%=((Contacto)cont.get(i)).getNombre()%>';
			tel='<%=((Contacto)cont.get(i)).getTelefono()%>';
			cor='<%=((Contacto)cont.get(i)).getCorreo()%>';
			mov='<%=((Contacto)cont.get(i)).getMovil()%>';
			pais='<%=(ObtenerParcelaConsulta.consultarPaisPersona(((Contacto)cont.get(i)).getPais())).getNombre()%>';
			datos[<%=i%>]=nom+","+tel+","+cor+","+mov+","+pais+","+consecutivoCont;
<%}else{%>
			consecutivoCont='<%=((Organizacion)cont.get(i)).getConsecutivo()%>';
			nom='<%=((Organizacion)cont.get(i)).getNombre()%>';
			tel='<%=((Organizacion)cont.get(i)).getTelefono()%>';
			cor='<%=((Organizacion)cont.get(i)).getCorreo()%>';
			dir='<%=((Organizacion)cont.get(i)).getDireccion()%>';
			pais='<%=(ObtenerParcelaConsulta.consultarPaisPersona(((Organizacion)cont.get(i)).getPais())).getNombre()%>'; 
			sec='<%=(ObtenerParcelaConsulta.consultarSectorOrganizacion(((Organizacion)cont.get(i)).getSector())).getDescripcion()%>';
		datos[<%=i%>] = nom + "," + tel + "," + cor + "," + dir + "," + pais + "," + sec + "," + consecutivoCont;
<%}
		
if(contactos.get(i).getIdClase()==1){%>
	crearInfoContacto('fgda', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==2){%>
	crearInfoContacto('propietario', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==3){%>
	crearInfoContacto('custodio', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==4){%>
	crearInfoContacto('investigador', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==5){%>
	crearInfoContacto('coleccion', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==6){%>
	crearInfoContacto('encargado', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==7){%>
	crearInfoContacto('brigadista', nom, tel, cor, consecutivoCont, datos);
<%}else if(contactos.get(i).getIdClase()==8){%>
	crearInfoContacto('supervisor', nom, tel, cor, consecutivoCont, datos);
<%}
	}%>
	}

	function crearInfoContacto(div, nombre, tel, correo, consecutivoCont, datos) {

		var contenedor = document.getElementById(div);
		var divNombre = document.createElement('div');
		divNombre.id = 'divNombre';
		var labNombre = document.createElement('label');
		labNombre.innerText = 'Nombre:';
		labNombre.innerHTML = 'Nombre:';
		divNombre.appendChild(labNombre);
		var nombreC = document.createElement('input');
		nombreC.type = 'text';
		nombreC.readonly = 'readonly';
		nombreC.id = 'nombre';
		nombreC.title = 'Nombre del contacto';
		nombreC.value = nombre;
		divNombre.appendChild(nombreC);

		var divTel = document.createElement('div');
		divTel.id = 'divTel';
		var labTel = document.createElement('label');
		labTel.innerText = 'Telefono:';
		labTel.innerHTML = 'Telefono:';
		divTel.appendChild(labTel);
		var telC = document.createElement('input');
		telC.type = 'text';
		telC.readonly = 'readonly';
		telC.id = 'telefono';
		telC.title = 'Telefono del contacto';
		telC.value = tel;
		divTel.appendChild(telC);

		var divCorreo = document.createElement('div');
		divCorreo.id = 'divCorreo';
		var labCorreo = document.createElement('label');
		labCorreo.innerText = 'Correo:';
		labCorreo.innerHTML = 'Correo:';
		divCorreo.appendChild(labCorreo);
		var correoC = document.createElement('input');
		correoC.type = 'text';
		correoC.readonly = 'readonly';
		correoC.id = 'correo';
		correoC.title = 'Correo del contacto';
		correoC.value = correo;
		divCorreo.appendChild(correoC);

		//		var popdetalle = document.getElementById('popup-detalleContacto' + i);
		
		var boton = document.createElement('input');
		boton.type = "button";
		boton.id = 'botonDetallar' + i;
		boton.name = 'botonDetallar' + i;
		boton.onclick = function(){ detallar(datos, consecutivoCont); };
		indicesCont[i] = 'botonDetallar' + i;
		i++;
		boton.value = 'Detallar';

		contenedor.appendChild(divNombre);
		contenedor.appendChild(divTel);
		contenedor.appendChild(divCorreo);
		contenedor.appendChild(boton);
	}

	function detallar(datos, consecutivoCont) {
		window.scrollTo(0,0);
		var j;
		document.getElementById("fondoBloqueo").style.display = "block";
		
		
		for (j = 0; j < datos.length; j++) {
			var d = datos[j].split(',');
			if (d.length == 6) {
				if (d[5] == consecutivoCont) {
					document.getElementById("popUpContacto").style.display = "block";
					document.getElementById("detContato").value='<%=msj.getString("contacto.natural")%>';
					document.getElementById("detNombreCont").value=d[0];
					document.getElementById("detTelefono").value=d[1];
					document.getElementById("detCorreo").value=d[2];
					document.getElementById("detCelular").value=d[3];
					document.getElementById("detPaisCont").value=d[4];
					break;
				}
			}else{
				if (d[6] == consecutivoCont) {
					document.getElementById("popUpOrg").style.display = "block";
					document.getElementById("detTipoPersona").value='<%=msj.getString("contacto.juridica")%>';
					document.getElementById("detNombreOrg").value = d[0];
					document.getElementById("detTel").value = d[1];
					document.getElementById("detCorreoOrg").value = d[2];
					document.getElementById("detDir").value = d[3];
					document.getElementById("detSector").value = d[5];
					document.getElementById("detPais").value = d[4];
					break;
				}
			}
		}
	}

	function desactivaDetallar() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popUpContacto").style.display = "none";
		document.getElementById("popUpOrg").style.display = "none";
	}

	var mouseX = 0;
	var mouseY = 0;

	function coordenadas(event) {
		x = event.clientX;
		y = event.clientY;

		document.getElementById("x").value = x;
		document.getElementById("y").value = y;
	}

	function takeCoordenadas(event) {
		mouseX = event.clientX;  
		mouseY = event.clientY;
	}

	function descargarArchivo(){
		window.open('<%=urlDescarga%>');
	}
</script>
</head>
<body onload="creaGeometria();cargarContactos();"
	onMouseMove="takeCoordenadas(event);" class='sidebarlast front'>
	<div id="fondoBloqueo"
		style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; display: none;">
		<!--Sin contenido -->
	</div>
	<div id="popUpContacto"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 700px; display: none;">
		<div id="cboxContent">
			<div id="cboxTitle"><%=msj.getString("titulos.contactos")%></div>
			<div id="cboxLoadedContent">
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.tipo")%>:
					</label> <input type="text" style="width: 300px" id="detContato"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.nombre")%>:
					</label> <input type="text" style="width: 300px" id="detNombreCont"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.telefono")%>:
					</label> <input type="text" style="width: 300px" id="detTelefono"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.correo")%>:
					</label> <input type="text" style="width: 300px" id="detCorreo"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.cel")%>:
					</label> <input type="text" style="width: 300px" id="detCelular"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.pais")%>:
					</label> <input type="text" style="width: 300px" id="detPaisCont"
						readonly="readonly">
				</div>
				<br> <input type="button" value="Volver"
					onclick="desactivaDetallar()">
			</div>
		</div>
	</div>

	<div id="popUpOrg"
		style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 700px; display: none;">
		<div id="cboxContent">
			<div id="cboxTitle"><%=msj.getString("titulos.organizacion")%></div>
			<div id="cboxLoadedContent">
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.tipo")%>:
					</label> <input type="text" style="width: 300px" id="detTipoPersona"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.nombre")%>:
					</label> <input type="text" style="width: 300px" id="detNombreOrg"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.telefono")%>:
					</label> <input type="text" style="width: 300px" id="detTel"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.correo")%>:
					</label> <input type="text" style="width: 300px" id="detCorreoOrg"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("organizacion.direccion")%>:
					</label> <input type="text" style="width: 300px" id="detDir"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("organizacion.sector")%>:
					</label> <input type="text" style="width: 300px" id="detSector"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1"><%=msj.getString("contacto.pais")%>:
					</label> <input type="text" style="width: 300px" id="detPais"
						readonly="readonly">
				</div>
				<br> <input type="button" value="Volver"
					onclick="desactivaDetallar()">
			</div>
		</div>
	</div>
	<form id="home">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
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
															readonly="readonly" value="<%=parcela.getConsecutivo()%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.nombre")%>:</label>
														<input type="text" class="form-control"
															id="exampleInputEmail1" readonly="readonly" title=""
															value="<%=parcela.getNombre()%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.tipo")%>:</label>
														<input type="text" class="form-control"
															id="exampleInputEmail1" readonly="readonly" title=""
															value="<%=temp.getNombre()%>" />
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.fechaEstablecimiento")%>:</label>
														<input type="text" class="form-control"
															id="exampleInputEmail1" readonly="readonly" title=""
															value="<%=formato.format(Util.convertToDate(parcela
					.getFechaEstablecimiento()))%>">
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.aprov")%>:</label>
														<input type="text" class="form-control"
															id="exampleInputEmail1" readonly="readonly" title=""
															value="<%=parcela.getAprovechamiento() == "0" ? msj
					.getString("opcion.no") : msj.getString("opcion.si")%>" />
													</div>

													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.inventario")%>:</label>
														<input type="text" readonly="readonly" title=""
															class="form-control"
															value="<%=tipoInventario.getNombre()%>" />
													</div>
													<div class="form-group item-textarea">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.descripcion")%>:</label>
														<textarea id="areapro" name="areapro" readonly="readonly"
															title="" rows="2" cols="8"><%=parcela.getDescripcion()%></textarea>
													</div>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.proposito")%>:</label>
														<input type="text" readonly="readonly" title=""
															class="form-control" value="<%=proposito.getNombre()%>" />
													</div>
													<%
														if (!parcela.getNombreImagen().equals("")) {
													%>
													<div class="form-group">
														<label for="exampleInputFile"><%=msj.getString("consulta.parcela.archivo")%>:</label>
														<input type="button" class="ico-zip"
															onclick="descargarArchivo();"
															value="<%=parcela.getNombreImagen()%>">
													</div>
													<%
														} else {
													%>
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.proposito")%>:</label>
														<label for="exampleInputEmail1"><%=msj.getString("parcela.detalle.sin.archivo")%></label>
													</div>
													<%
														}
													%>
													<div class="form-group item-textarea">
														<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.observaciones")%>:</label>
														<textarea id="areapro" name="areapro" readonly="readonly"
															title="" rows="2" cols="8"><%=parcela.getObservaciones()%></textarea>
													</div>
												</div>
												<h3><%=msj.getString("titulos.ubicacion")%></h3>
												<div>
													<div class="form-localizacion" role="form">
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.pais")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control" value="<%=pais.getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.depto")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control"
																value="<%=deptos.get(0).getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.mcipio")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=municipios.get(0).getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.car")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control"
																value="<%=cars.get(0).getNombre()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.proyecto.bosque")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control"
																value="<%=bosques.get(0).getTipoBosque()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.altitud")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control"
																value="<%=bosques.get(0).getAltitud()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.temperatura")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control"
																value="<%=bosques.get(0).getTemperatura()%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("detalle.precipitacion")%>:</label>
															<input type="text" readonly="readonly" id="paispro"
																class="form-control"
																value="<%=bosques.get(0).getPrecipitacion()%>">
														</div>
													</div>

												</div>
												<h3><%=msj.getString("titulos.localizacion")%></h3>
												<div class="form-localizacion" role="form">
													<div class="form-group">
														<label for="exampleInputEmail1"><%=msj.getString("detalle.geometria")%>:</label>
														<input type="text" readonly="readonly" id="paispro"
															class="form-control" value="<%=geometria[0]%>">
													</div>
													<div class="form-group">
														<div id="puntos"></div>
													</div>
												</div>
												<h3><%=msj.getString("titulos.contactos")%></h3>
												<!-- 												<div > -->
												<div id="detalleContactos" class="form-localizacion"
													role="form">
													<!-- 														<div class="form-group"> -->
													<div id="fgda" class="form-group"></div>
													<div id="propietario" class="form-group"></div>
													<div id="custodio" class="form-group"></div>
													<div id="investigador" class="form-group"></div>
													<div id="coleccion" class="form-group"></div>
													<div id="encargado" class="form-group"></div>
													<div id="brigadista" class="form-group"></div>
													<div id="supervisor" class="form-group"></div>
													<!-- 														</div> -->

													<!-- 													</div> -->
												</div>
												<h3><%=msj.getString("metadato.titulo")%></h3>
												<div id="metadato" class="form-localizacion form-columnx2"
													role="form">
													<fieldset>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.nombre")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(0)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.org")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(1)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.cargo")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(2)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.tels")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(3)%>">
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(4)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.dir")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(5)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.ciudad")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(6)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.pais")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(9)%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.mail")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=metadatoInfo.get(10)%>">
														</div>
													</fieldset>
													<fieldset>
														<%
															String[] infoPar = meta.getInfoParcela().split(";");
														%>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.nombre")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=infoPar[0].split("/")[1]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.observaciones")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=infoPar[3]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.descripcion")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=infoPar[2]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("consulta.parcela.proposito")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=infoPar[1]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.fecha")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=meta.getInfoFecha()%>">
														</div>
													</fieldset>
													<%
														String[] coordenadas = meta.getInfoCoord().split(";");
													%>
													<fieldset>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.maximo.x")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=coordenadas[0]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.minimo.x")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=coordenadas[1]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.maximo.y")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=coordenadas[2]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.minimo.x")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=coordenadas[3]%>">
														</div>
													</fieldset>
													<%
														String[] propietarioDatos = meta.getInfoCont1().split(";");
													%>
													<fieldset>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.nombre")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=propietarioDatos[0]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.telefono")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=propietarioDatos[1]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.ciudad")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=propietarioDatos[2]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.pais")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=propietarioDatos[3]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.correo")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=propietarioDatos[4]%>">
														</div>
													</fieldset>
													<%
														String[] custodioDatos = meta.getInfoCont2().split(";");
													%>
													<fieldset>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.nombre")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=custodioDatos[0]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.telefono")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=custodioDatos[1]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.ciudad")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=custodioDatos[2]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.pais")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=custodioDatos[3]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.correo")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=custodioDatos[4]%>">
														</div>
													</fieldset>
													<%
														if (meta.getInfoCont3() != null) {
															String[] investigadorPrincipal = meta.getInfoCont1().split(";");
													%>
													<fieldset>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.nombre")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=investigadorPrincipal[0]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.telefono")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=investigadorPrincipal[1]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.responsable.ciudad")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=investigadorPrincipal[2]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.pais")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=investigadorPrincipal[3]%>">
														</div>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("contacto.correo")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control"
																value="<%=investigadorPrincipal[4]%>">
														</div>
													</fieldset>
													<%
														}
													%>
													<fieldset>
														<%
															String[] keywords = meta.getInfoKey().split(";");
															for (int keyw = 0; keyw < keywords.length; keyw++) {
														%>
														<div class="form-group">
															<label for="exampleInputEmail1"><%=msj.getString("metadato.palabra.clave")%>:</label>
															<input type="text" readonly="readonly" id="munpro"
																class="form-control" value="<%=keywords[keyw]%>">
														</div>
														<%
															}
														%>
													</fieldset>
												</div>
												<h3>Imagenes de la parcela</h3>
												<div id="metadato" class="form-localizacion" style="overflow-x:hidden;overflow-y:hidden">
													<div class="slide">
														<ul id="slippry-demo">
														<%for(int x=0;x<listaImagenes.length;x++){ %>
															<li><a href="#slide<%=x+1 %>"><img src="../carrusel/<%=listaImagenes[x] %>"
																	alt=""></a></li> 
														<%} %>
														</ul>
													</div>
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