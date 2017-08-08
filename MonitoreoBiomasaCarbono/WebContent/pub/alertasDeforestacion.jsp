<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>  
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>    
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.dao.ConsultaAlertasDeforestacion"%>
<%@page import="co.gov.ideamredd.mbc.entities.InfoCifras"%>
<%@page import="java.text.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
<title>Sistema de monitoreo de bosques y carbono</title>
<%
    DecimalFormat df = new DecimalFormat("#0.00");

	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort() 
	+ request.getContextPath() + "/";

    String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	ArrayList<Noticias> noticias = CargaDatosInicialHome.getNoticiasHome(); 
	ArrayList<Noticias> eventos = CargaDatosInicialHome.getEventosHome();
	request.getSession().setAttribute("noticia", noticias);
	request.getSession().setAttribute("eventos", eventos);
	ResourceBundle msj = (ResourceBundle)request.getSession().getAttribute("i18n");
	
	Usuario usuario = null;
	LenguajeI18N i18n = (LenguajeI18N)request.getSession().getAttribute("i18nAux");
	if(request.getUserPrincipal() !=null)
	{
		usuario = UtilWeb.consultarUsuarioPorDoc(Integer
		.valueOf(request.getUserPrincipal().getName()));
		usuario.setRolNombre(UtilWeb.consultarRolesUsuarioPorDoc(Integer 
		.valueOf(request.getUserPrincipal().getName())));
	}
	
	ArrayList<InfoCifras> cifrasDeptos = 
			ConsultaAlertasDeforestacion.consultarCifrasDepartamentos();

	ArrayList<InfoCifras> cifrasCARs = 
			ConsultaAlertasDeforestacion.consultarCifrasCARs();

	ArrayList<Double> cifrasConsolidado = 
			ConsultaAlertasDeforestacion.consultarCifrasConsolidado();
%>
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="../js/slippry.min.js"></script>
<link rel="stylesheet" href="../css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/html.css"
	media="all" />

<script>
    $(function(){
     $('#slippry-demo').slippry();
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
    
	function lenguaje(id){
		if(id==1){
			document.getElementById('lenguaje').value="ES";
		}else{
			document.getElementById('lenguaje').value="EN";
		}
		document.getElementById('pagina').value="<%=request.getRequestURI()%>";
		document.getElementById('home').submit();
	}

	function borrarArchivos() {
		document.getElementById('formBorrar').submit();
	}

	function popUpUsuariosAux() {
		var coords = getAbsoluteElementPosition(document
				.getElementById("icoUsuarios"));

		document.getElementById("popUpUsuarios").style.left = coords.left
				+ "px";
		document.getElementById("popUpUsuarios").style.top = coords.top + 15
				+ "px";
	}

	function popUpUsuariosOpen() {
		var coords = getAbsoluteElementPosition(document
				.getElementById("icoUsuarios"));

		document.getElementById("popUpUsuarios").style.left = coords.left
				+ "px";
		document.getElementById("popUpUsuarios").style.top = coords.top + 15
				+ "px";
		document.getElementById("popUpUsuarios").style.display = "block";
	}

	function popUpUsuariosClose() {
		document.getElementById("popUpUsuarios").style.display = "none";
	}
	function getAbsoluteElementPosition(element) {
		if (typeof element == "string")
			element = document.getElementById(element);

		if (!element)
			return {
				top : 0,
				left : 0
			};

		var y = 0;
		var x = 0;
		while (element.offsetParent) {
			x += element.offsetLeft;
			y += element.offsetTop;
			element = element.offsetParent;
		}
		return {
			top : y,
			left : x
		};
	}
	function popUpAyudaAux() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		document.getElementById("popUpAyuda").style.left= coords.left-135 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+15 + "px";
	}

	function popUpAyudaOpen() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		
		document.getElementById("popUpAyuda").style.left= coords.left-135 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+15 + "px";
		document.getElementById("popUpAyuda").style.display = "block";
	}

	function popUpAyudaClose() {
		document.getElementById("popUpAyuda").style.display = "none";
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
</script>
<script>
function setOp(obj, value) {
    obj.style.opacity = value/100;
    obj.style.MozOpacity = value/100;
    obj.style.KhtmlOpacity = value/100;
    obj.style.filter = 'alpha(opacity=' + value+ ')';
    obj.style.zoom=1;//necesario para Explorer
} 
function efecto(o){
    for (var c=0 ; c < 100; c++) 
        (function(c){
             setTimeout(function(){setOp(o,c);} , 6*c);
        })(c);
} 
</script> 
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	var cifrasDeptos = new Array(<%=cifrasDeptos.size()%>);
	var cifrasCARs = new Array(<%=cifrasCARs.size()%>);
	var nombresDeptos = new Array(<%=cifrasDeptos.size()%>);
	var nombresCARs = new Array(<%=cifrasCARs.size()%>);
	var cifrasConsolidado = new Array(<%=cifrasConsolidado.size()%>);
	
<%for(int i=0;i<cifrasDeptos.size();i++)
{%>
cifrasDeptos[<%=i%>]=new Array(3);
nombresDeptos[<%=i%>]='<%=cifrasDeptos.get(i).getNombre()%>';
<%}%>
<%for(int i=0;i<cifrasCARs.size();i++)
{%>
cifrasCARs[<%=i%>]=new Array(3);
nombresCARs[<%=i%>]='<%=cifrasCARs.get(i).getNombre()%>';
<%}%>
	
<%for(int i=0;i<cifrasConsolidado.size();i++)
{%>
	cifrasConsolidado[
<%=i%>
	] =
<%=cifrasConsolidado.get(i)%>
	;
<%}%>
	
<%for(int i=0;i<cifrasDeptos.size();i++)
{%>
	cifrasDeptos[
<%=i%>
	][0] =
<%=cifrasDeptos.get(i).getBosque()%>
	;
	cifrasDeptos[
<%=i%>
	][1] =
<%=cifrasDeptos.get(i).getNoBosque()%>
	;
	cifrasDeptos[
<%=i%>
	][2] =
<%=cifrasDeptos.get(i).getSinInfo()%>
	;
<%} 
for(int i=0;i<cifrasCARs.size();i++)
{%>
	cifrasCARs[
<%=i%>
	][0] =
<%=cifrasCARs.get(i).getBosque()%>
	;
	cifrasCARs[
<%=i%>
	][1] =
<%=cifrasCARs.get(i).getNoBosque()%>
	;
	cifrasCARs[
<%=i%>
	][2] =
<%=cifrasCARs.get(i).getSinInfo()%>
	;
<%}%>
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});

	google.setOnLoadCallback(drawGraficaBarrasConsolidado);
	google.setOnLoadCallback(drawGraficaPieConsolidado);

	function drawGraficaBarrasConsolidado() {
		// **************Grafica de Barras Bosque********************
		data = google.visualization.arrayToDataTable([
				[ '<%=msj.getString("bosqueEnCifras.consolidado")%>', '<%=msj.getString("bosqueEnCifras.bosque")%>', 
				'<%=msj.getString("bosqueEnCifras.noBosque")%>', 
				'<%=msj.getString("bosqueEnCifras.sinInfo")%>' ],
				[ '', cifrasConsolidado[0], cifrasConsolidado[1],
						cifrasConsolidado[2] ] ]);
		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> <%=msj.getString("bosqueEnCifras.consolidado").toLowerCase()%>',
			width : 330,
			height : 330,
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '<%=msj.getString("bosqueEnCifras.consolidado")%>',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : 'Area %',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGBarras'));
		chart.draw(data, options);
	}

	function drawGraficaPieConsolidado() {
		// ************Grafica Torta Bosque***************
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area %' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', cifrasConsolidado[0] ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', cifrasConsolidado[1] ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', cifrasConsolidado[2] ] ]);

		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> <%=msj.getString("bosqueEnCifras.consolidado").toLowerCase()%>',
			width : 330,
			height : 330,
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGPie'));
		chart.draw(data, options);
	}

	function drawGraficaBarrasCARs() {
		// **************Grafica de Barras Bosque********************
		data = google.visualization.arrayToDataTable([
				[ 'CAR', '<%=msj.getString("bosqueEnCifras.bosque")%>',
					'<%=msj.getString("bosqueEnCifras.noBosque")%>',
					'<%=msj.getString("bosqueEnCifras.sinInfo")%>' ],
				[ nombresCARs[0], cifrasCARs[0][0], cifrasCARs[0][1],
						cifrasCARs[0][2] ],
				[ nombresCARs[1], cifrasCARs[1][0], cifrasCARs[1][1],
						cifrasCARs[1][2] ],
				[ nombresCARs[2], cifrasCARs[2][0], cifrasCARs[2][1],
						cifrasCARs[2][2] ],
				[ nombresCARs[3], cifrasCARs[3][0], cifrasCARs[3][1],
						cifrasCARs[3][2] ],
				[ nombresCARs[4], cifrasCARs[4][0], cifrasCARs[4][1],
						cifrasCARs[5][2] ],
				[ nombresCARs[5], cifrasCARs[5][0], cifrasCARs[5][1],
						cifrasCARs[5][2] ] ]);
		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> CARs',
			width : 330,
			height : 330,
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : 'CAR',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGBarras'));
		chart.draw(data, options);
	}

	function drawGraficaPieCARs() {
		// ************Grafica Torta Bosque***************
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area %' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', cifrasConsolidado[0] ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', cifrasConsolidado[1] ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', cifrasConsolidado[2] ] ]);

		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> Total CARs',
			width : 330,
			height : 330,
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGPie'));
		chart.draw(data, options);
	}

	function drawGraficaBarrasDepartamentos() {
		// **************Grafica de Barras Bosque********************
		data = google.visualization.arrayToDataTable([
				[ 'Departamento', '<%=msj.getString("bosqueEnCifras.bosque")%>',
					 '<%=msj.getString("bosqueEnCifras.noBosque")%>',
					  '<%=msj.getString("bosqueEnCifras.sinInfo")%>' ],
				[ nombresDeptos[0], cifrasDeptos[0][0], cifrasDeptos[0][1],
						cifrasDeptos[0][2] ],
				[ nombresDeptos[1], cifrasDeptos[1][0], cifrasDeptos[1][1],
						cifrasDeptos[1][2] ],
				[ nombresDeptos[2], cifrasDeptos[2][0], cifrasDeptos[2][1],
						cifrasDeptos[2][2] ],
				[ nombresDeptos[3], cifrasDeptos[3][0], cifrasDeptos[3][1],
						cifrasDeptos[3][2] ],
				[ nombresDeptos[4], cifrasDeptos[4][0], cifrasDeptos[4][1],
						cifrasDeptos[5][2] ],
				[ nombresDeptos[5], cifrasDeptos[5][0], cifrasDeptos[5][1],
						cifrasDeptos[5][2] ] ]);
		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> <%=msj.getString("bosqueEnCifras.departamentos").toLowerCase()%>',
			width : 330,
			height : 330,
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : 'CAR',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGBarras'));
		chart.draw(data, options);
	}

	function drawGraficaPieDepartamentos() {
		// ************Grafica Torta Bosque***************
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area %' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', cifrasConsolidado[0] ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', cifrasConsolidado[1] ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', cifrasConsolidado[2] ] ]);

		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> Total <%=msj.getString("bosqueEnCifras.departamentos").toLowerCase()%>',
			width : 330,
			height : 330,
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGPie'));
		chart.draw(data, options);
	}

	//     Funciones para despliegue de graficas
	var cifrasTabla=1;
	function graficasConsolidado() {
		drawGraficaBarrasConsolidado();
		drawGraficaPieConsolidado();
		document.getElementById('imagenBosque').src = '../img/croquisColombia.jpg';
		document.getElementById('btnConsolidado').style.border='solid'; 
		document.getElementById('btnCARs').style.border='0px';
		document.getElementById('btnDepartamentos').style.border='0px';
		cifrasTabla=1;
		efecto(document.getElementById('bosqueCifrasGraficas'));
	}
	function graficasCARs() {
		drawGraficaBarrasCARs();
		drawGraficaPieCARs();
		document.getElementById('imagenBosque').src = '../img/carsColombia.jpg';
		document.getElementById('btnCARs').style.border='solid';
		document.getElementById('btnConsolidado').style.border='0px';
		document.getElementById('btnDepartamentos').style.border='0px';
		cifrasTabla=2;
		efecto(document.getElementById('bosqueCifrasGraficas'));
	}
	function graficasDepartamentos() {
		drawGraficaBarrasDepartamentos();
		drawGraficaPieDepartamentos();
		document.getElementById('imagenBosque').src = '../img/deptosColombia.jpg';
		document.getElementById('btnDepartamentos').style.border='solid';
		document.getElementById('btnCARs').style.border='0px';
		document.getElementById('btnConsolidado').style.border='0px';
		cifrasTabla=3;
		efecto(document.getElementById('bosqueCifrasGraficas'));
	}

	function verTablas(){
		
		document.getElementById('bosqueCifrasGraficas').style.display='none';
		document.getElementById('divisionBosque').style.display='none';
		switch(cifrasTabla)
		{
		    case 1:
		    	document.getElementById('bosqueCifrasTablaConsolidado').style.display='block';
			    break;
		    case 2:
		    	document.getElementById('bosqueCifrasTablaCARs').style.display='block';
				break;
		    case 3:
		    	document.getElementById('bosqueCifrasTablaDeptos').style.display='block';
				break;
		}
	}

	function verGraficas(){
		document.getElementById('bosqueCifrasTablaConsolidado').style.display='none';
    	document.getElementById('bosqueCifrasTablaCARs').style.display='none';
    	document.getElementById('bosqueCifrasTablaDeptos').style.display='none';
		document.getElementById('bosqueCifrasGraficas').style.display='block';
		document.getElementById('divisionBosque').style.display='block';
	}

function btnCambioCapa(){

	    if(document.getElementById('bosqueCifrasGraficas').style.display=='block')
		{
		document.getElementById('bosqueCifrasGraficas').style.display='none';
		document.getElementById('divisionBosque').style.display='none';
		switch(cifrasTabla)
		{
		    case 1:
		    	document.getElementById('bosqueCifrasTablaConsolidado').style.display='block';
			    break;
		    case 2:
		    	document.getElementById('bosqueCifrasTablaCARs').style.display='block';
				break;
		    case 3:
		    	document.getElementById('bosqueCifrasTablaDeptos').style.display='block';
				break;
		}
		document.getElementById('btnCambio').value='Ver Graficas';
		}else{
		document.getElementById('bosqueCifrasTablaConsolidado').style.display='none';
    	document.getElementById('bosqueCifrasTablaCARs').style.display='none';
    	document.getElementById('bosqueCifrasTablaDeptos').style.display='none';
		document.getElementById('bosqueCifrasGraficas').style.display='block';
		document.getElementById('divisionBosque').style.display='block';
		document.getElementById('btnCambio').value='Ver Tablas';
		}
}

function descargaDocumento(nombre){
	document.getElementById("hidNomDoc").value=nombre;
	document.getElementById("formDescargaDocs").submit();
}
</script>
</head>
<body class='sidebarlast front' onmouseover="popUpUsuariosAux()">
	<form id="home" action="<%=basePath%>idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> <input
			type="hidden" name="pagina" id="pagina">
		<div id="page"
			style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; width: 1000px;">
			<div id="header">

				<div id="header-first" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">

							<div id="block-logo" class="block">
								<div class="content">
									<a href="<%=basePath%>home.jsp"><img src="../img/logo.png"
										alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->

							<div id="block-images-header" class="block">
								<div class="content">
									<a href=""><img src="../img/img-min.png" alt=""></a> <a
										><img src="../img/img-prosperidad.png" alt=""></a>
									<a ><img src="../img/img-moore.png" alt=""></a> <a
										><img src="../img/img-patrimonio.png" alt=""></a>
								</div>
								<!-- /.content -->
							</div>
							<!-- /.block -->


							<%
								if (usuario != null) {
							%>
							<div id="block-top-menu" class="block block-menu">

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
												href="<%=basePath2%>Usuario-Web/reg/modificarUsuario.jsp?
                            id=<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
													<%=msj.getString("home.registrado.modificar")%></a></label>
										</div>
										<%
											}
										%>
										<%
											if (usuario.getRolNombre().contains("ADMINISTRADOR_GENERAL")) {
										%>
										<div class="form-group">
											<label id="icoUsuarios" for="exampleInputEmail1"
												onclick="popUpUsuariosOpen()"
												style="margin-right: 10px; cursor: pointer;"><a><%=msj.getString("logOn.admin.Usuarios")%>▼
											</a></label>
										</div>
										<%
											}
										%>

										<div class="form-group">
											<label for="exampleInputEmail1"><a
												href="<%=basePath%>limpiarSesionServlet"><%=msj.getString("home.registrado.cerrar")%></a></label>
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
							<%
								} else {
							%>
							<div id="block-top-menu" class="block block-menu">

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
									<ul class="social-menu item-list">
										<li id="icoAyuda" class="menu-item help first"
											onclick="popUpAyudaOpen()"
											style="margin-right: 4px; cursor: pointer;"><a></a></li>
										<li class="menu-item facebook" style="cursor: pointer;"><a></a></li>
										<li class="menu-item en" style="cursor: pointer;"><a
											onclick="lenguaje(2);"></a></li>
										<li class="menu-item es" style="cursor: pointer;"><a
											onclick="lenguaje(1)"></a></li>
									</ul>

								</div>
								<!-- /.content -->
							</div>
							<!--/.block -->
							<%
								}
							%>


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
<%-- 										        <a href="<%=basePath2%>MonitoreoBC-WEB/verNoticiasYEventos.jsp">Consultar Noticias y Eventos</a></li> --%>
<!-- 											    <li class="menu-item noticia"><a -->
<%-- 											    href="<%=basePath2%>MonitoreoBC-WEB/reg/crearNoticia.jsp">Crear Noticias o Eventos</a></li> --%>
<!-- 									        </ul> -->
										</li>
											
										<li class="menu-item services">
										<a href="<%=basePath2%>Usuario-Web/extra/documentacion.jsp" ><%=msj.getString("home.documentacion")%></a>
										</li>
											
										<li class="menu-item contact-us"><a href="<%=basePath2%>MonitoreoBC-WEB/pub/bosqueEnCifras.jsp"><%=msj.getString("home.bosqueCifras")%></a></li>
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

			<div id="preface" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix"
						style="background: #EEEEEE; border: 1px solid #D3D3D3; box-shadow: 2px 2px 2px #D3D3D3; margin-top: 20px; overflow: hidden; padding: 20px; position: relative;">

						<div style="margin-top: 30px; background: #EEEEEE;">
							<h2
								style="marmargin-top: 15px; background-color: #D66A10; color: white; text-align: center; font-weight: bold; font-size: 25px">
								ALERTAS TEMPRANAS DE DEFORESTACION</h2>
							<div id="divisionBosque" style="text-align: center;">
								<h3>
									<a id="btnConsolidado"  style="cursor: pointer; border:solid; font-size: 20px" onclick="graficasConsolidado()">&nbsp;<%=msj.getString("bosqueEnCifras.consolidado")%>&nbsp;</a>&nbsp;&nbsp;&nbsp; 
									<a id="btnCARs" style="cursor: pointer; font-size: 20px" onclick="graficasCARs()">&nbsp;<%=msj.getString("bosqueEnCifras.cars")%>&nbsp;</a> &nbsp;&nbsp;&nbsp;
									<a id="btnDepartamentos" style="cursor: pointer; font-size: 20px" onclick="graficasDepartamentos()">&nbsp;<%=msj.getString("bosqueEnCifras.departamentos")%>&nbsp;</a>
								</h3>
							</div>
							<div id="bosqueCifrasGraficas" onclick="verTablas()" style="display: block;">
							
							    <div style="width: 300px; float:left ;">
									<img id="imagenBosque" width="250px" height="300px"
										src="../img/croquisColombia.jpg">
								</div>
								<div style="width: 300px; float: left;" id="divGBarras"></div>
								<div style="width: 300px; float: right;" id="divGPie"></div>
								
							</div>
							
							<div id="bosqueCifrasTablaCARs" onclick="verGraficas()" style="display: none;height: 420px">
								<table width="99%" border="1">
									<thead>
									    <tr>
									        <th colspan="4" style="text-align: center;">
									        <%=msj.getString("bosqueEnCifras.becCAR")%>
									        </th>
									    </tr>
										<tr>
											<th class="even"><%=msj.getString("bosqueEnCifras.car")%></th>
											<th class="odd"><%=msj.getString("bosqueEnCifras.bosque")%> (ha)</th>
											<th class="even"><%=msj.getString("bosqueEnCifras.noBosque")%> (ha)</th>
											<th class="odd"><%=msj.getString("bosqueEnCifras.sinInfo")%> (ha)</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (int y = 0; y < 8; y++) {
										%>
										<tr class="even">
											<td><%=cifrasCARs.get(y).getNombre()%></td>
											<td><%=df.format(cifrasCARs.get(y).getBosque())%></td>
											<td><%=df.format(cifrasCARs.get(y).getNoBosque())%></td>
											<td><%=df.format(cifrasCARs.get(y).getSinInfo())%></td>
										</tr>
										<%
											}
										%>
									</tbody>
								</table>
							</div>
							<div id="bosqueCifrasTablaDeptos" onclick="verGraficas()" style="display: none; height: 420px">
								<table width="99%" border="1">
									<thead>
									    <tr>
									        <th colspan="4" style="text-align: center;">
									        <%=msj.getString("bosqueEnCifras.becDepartamento")%>
									        </th>
									    </tr>
										<tr>
											<th class="even"><%=msj.getString("bosqueEnCifras.departamento")%></th>
											<th class="odd"><%=msj.getString("bosqueEnCifras.bosque")%> (ha)</th>
											<th class="even"><%=msj.getString("bosqueEnCifras.noBosque")%> (ha)</th>
											<th class="odd"><%=msj.getString("bosqueEnCifras.sinInfo")%> (ha)</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (int y = 0; y < 8; y++) {
										%>
										<tr class="even">
											<td><%=cifrasDeptos.get(y).getNombre()%></td>
											<td><%=df.format(cifrasDeptos.get(y).getBosque())%></td>
											<td><%=df.format(cifrasDeptos.get(y).getNoBosque())%></td>
											<td><%=df.format(cifrasDeptos.get(y).getSinInfo())%></td>
										</tr>
										<%
											}
										%>
									</tbody>
								</table>
							</div>
							<div id="bosqueCifrasTablaConsolidado" onclick="verGraficas()" style="display: none; height: 400px">
								<table width="99%" border="1">
									<thead>
									    <tr>
									        <th colspan="3" style="text-align: center;">
									        <%=msj.getString("bosqueEnCifras.becConsolidado")%>
									        </th>
									    </tr>
										<tr>
											<th class="odd"><%=msj.getString("bosqueEnCifras.bosque")%> (ha)</th>
											<th class="even"><%=msj.getString("bosqueEnCifras.noBosque")%> (ha)</th>
											<th class="odd"><%=msj.getString("bosqueEnCifras.sinInfo")%> (ha)</th>
										</tr>
									</thead>
									<tbody>
										<tr class="even">
											<td><%=df.format(cifrasConsolidado.get(0))%></td>
											<td><%=df.format(cifrasConsolidado.get(1))%></td>
											<td><%=df.format(cifrasConsolidado.get(2))%></td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<input id="btnCambio" name="btnCambio" type="button" value="Ver Datos" onclick="btnCambioCapa()">
							<input id="btnDescarga" name="btnDescarga" type="button" value="Descargar" onclick="descargaDocumento('AT_DEFORESTACION')">
							
						</div>

					</div>
					<!-- /.section-inner-->
				</div>
				<!--/.section -->
			</div>
			<!-- /.section-wrapper preface-->

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix">

							<div id="content">
								<div class="content-inner">

									<div id="block-home" class="block-gray blockx345 block">
										<div id="block-video">
											<iframe width="310" height="210" src="//www.youtube.com/embed/DhhmVENqKv4?" 
											frameborder="0" allowfullscreen></iframe>
										</div>

										<div id="block-eventos">
											<h2><%=msj.getString("home.eventos")%></h2>
											
										</div>

										<div id="block-noticias">
											<h2><%=msj.getString("home.noticias")%></h2>
											
										</div>
									</div>


									<div id="block-bosques" class=" blockx245 block-vermas block">
										<div class="content">
											<h2><%=msj.getString("home.bosque")%></h2>
											<img src="../img/Bosque.jpg">
											<p>En el año 2013, la deforestación estimada fue de 120.933 has, lo cual 
											equivale a 1.8 veces el área de una metrópoli colombiana como la ciudad de Cali.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteBosques.jsp">Ver más</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2><%=msj.getString("home.carbono")%></h2>
											<img src="../img/Carbono.jpg">
											<p>En 2012 los bosques de Colombia almacenaban 23.001.314.006 t CO2 
											en la biomasa aérea, que equivale al CO2 que se 
											necesita para llenar 112.000.000 de edificios 
											como la Torre Colpatria la cual tiene 63 pisos.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>/ReportesMonitoreo-Web/pub/consultarReporteCarbono.jsp">Ver más</a>
											</div>
										</div>
									</div>

									



								</div>
								<!-- content-inner -->
							</div>
							<!-- /.content-->

							<div id="sidebarlast">
								<div class="section-inner clearfix">

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2>Alertas Tempranas de Deforestaci&oacute;n</h2>
											<img src="../img/Alertas.jpg">
											<p>
											Las &aacute;reas n&uacute;cleo de deforestaci&oacute;n en Colombia, en las cuales se concentra la mayor p&eacute;rdida de bosque se
											 localizan en los Caquet&aacute;-Putumayo, Meta-Guaviare y el eje San Jos&eacute; del Guaviare-Calamar.
											Se evidencia otro foco activo, en Antioquia, en los municipios de Segovia, Turbo, Ituango, Anor&iacute;, 
											y El Bagre, siendo las zonas de mayor afectaci&oacute;n con una p&eacute;rdida de bosque entre el 45 y el 75%.
											</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>MonitoreoBC-WEB/pub/alertasDeforestacion.jsp">Ver más</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2>Visor Geografico</h2>
											<img src="../img/Visor.jpg">
											<p>Conozca dónde se localiza los Bosques de Colombia y observe los análisis de la información de los últimos 23 años 
											en los cuales se evidencia una desconexión de la masa boscosa de la Amazonia y el bosque natural de la cordillera oriental.
											 Con nuestro Visor geográfico, puede explorar las áreas de Deforestación, en las cuales se observa 
											cómo el 57% de la deforestación nacional se localiza en la región de la Amazonía, mientras que un 22% se reportó en la región andina.</p>
											<div class="ver-mas">
												<a>Ver más</a>
											</div>
										</div>
									</div>

									

								</div>
								<!-- /.section-inner-->
							</div>
							<!-- /.sidebar-wrapper-->


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
<!-- 							<div class="menu-servicios menu-postscript"> -->
<!-- 								<h3>Servicios de Cuidadanía</h3> -->
<!-- 								<ul> -->
<!-- 									<li><a href="">Visitas Casa de Nariño</a></li> -->
<!-- 									<li><a href="">Datos de contacto</a></li> -->
<!-- 									<li><a href="">Escríbale al Presidente</a></li> -->
<!-- 									<li><a href="">PSQR</a></li> -->
<!-- 									<li><a href="">Colombia Compra Eficiente</a></li> -->
<!-- 									<li><a href="">Avisos Convocatoria Pública</a></li> -->
<!-- 									<li><a href="">Notificaciones por Aviso</a></li> -->
<!-- 									<li><a href="">Notificaciones Judiciales</a></li> -->
<!-- 									<li><a href="">Proveedores</a></li> -->
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
								<li><a href="<%=basePath2%>MonitoreoBC-WEB" ><%=msj.getString("home.home")%></a></li>
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

	<div id="popUpUsuarios"
		style="z-index: 3; position: absolute; width: 140px; height: 100px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;">
			<label for="exampleInputEmail1"
				style="text-align: center; color: white; font-weight: bold;">
				<%=msj.getString("logOn.admin.Usuarios")%></label>
		</div>
		<%
			if (usuario != null) {
		%>
		<label for="exampleInputEmail1"><a
			href="<%=basePath2%>Usuarios_Web/admin/consultarUsuarios.jsp?id=<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
				*<%=msj.getString("logOn.admin.consultarUsuarios")%></a></label> <label
			for="exampleInputEmail1" style="text-align: left;"><a
			href="<%=basePath2%>Usuarios_Web/admin/generarReportesUsuarios.jsp?id=<%=Util.encriptar(usuario.getIdentificacion())%>&idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
				*<%=msj.getString("logOn.admin.reporteUsuarios")%></a></label> <label
			for="exampleInputEmail1" style="text-align: left;"><a
			href="<%=basePath%>borrarArchivosServlet"> *<%=msj.getString("logOn.admin.borrarTemporales")%></a></label>
		<label for="exampleInputEmail1" onclick="popUpUsuariosClose()"
			style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
		<%
			}
		%>
	</div>

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
			for="exampleInputEmail1" onclick="popUpAyudaClose()"
			style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
	</div>
	
	<form method="post" action="<%=basePath%>descargaDocumentosServlet"
		name="formDescargaDocs" id="formDescargaDocs">
		<input type="hidden" name="hidNomDoc" id="hidNomDoc" />
	</form>
	
</body>
</html>