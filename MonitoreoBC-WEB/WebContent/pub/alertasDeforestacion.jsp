<%@page import="co.gov.ideamredd.mbc.entities.DatosAlertas"%>
<%@page import="co.gov.ideamredd.mbc.entities.ReporteAlertas"%>
<%@page import="co.gov.ideamredd.mbc.entities.DatosExcelAlertas"%>
<%@page import="co.gov.ideamredd.mbc.entities.EntidadAlertas"%>
<%@page import="co.gov.ideamredd.mbc.entities.Nucleo"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargarDatosAlertasTempranas"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>  
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>    
<%@page import="co.gov.ideamredd.util.Util"%>
<%-- <%@page import="co.gov.ideamredd.mbc.dao.ConsultaAlertasDeforestacion"%> --%>
<%-- <%@page import="co.gov.ideamredd.mbc.entities.InfoCifras"%> --%>
<%@page import="java.text.*"%>
<%@page import="co.gov.ideamredd.web.ui.UI"%>
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
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<% if (es_movil) { %>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<% } %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
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
 	
DecimalFormat df2 = new DecimalFormat("#0.00");
DecimalFormat df0 = new DecimalFormat("#0");
DecimalFormat df1 = new DecimalFormat("#0.0");

CargarDatosAlertasTempranas cdat= new CargarDatosAlertasTempranas();

ArrayList<ReporteAlertas> reportesHistoricos= cdat.obtenerReportesHistorico();
DatosAlertas datosAlertas = cdat.obtenerDatosAlertas();
DatosExcelAlertas datosExcelAlertas= cdat.obtenerDatosExcel();

ArrayList<EntidadAlertas> departamentos = new ArrayList<EntidadAlertas>();
ArrayList<EntidadAlertas> regiones = new ArrayList<EntidadAlertas>();
ArrayList<EntidadAlertas> autoridades = new ArrayList<EntidadAlertas>();
ArrayList<Nucleo> nucleos = new ArrayList<Nucleo>();

if (datosExcelAlertas != null) {
	departamentos = datosExcelAlertas.getDepartamentos();
	regiones = datosExcelAlertas.getRegiones();
	autoridades = datosExcelAlertas.getAutoridades();
	nucleos = datosExcelAlertas.getNucleos();
}
	
%>
<title><%=msj.getString("alertasdeforestacion.29")%></title>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="../js/alertasTempranas.js"></script>
<script src="../js/slippry.min.js"></script> 
<link rel="stylesheet" href="../css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/estilos.css"
	media="all" />
<script src="/MonitoreoBC-WEB/js/general.js"></script>
<script>

    $(document).ready(function() {
		inicializarNavegador();
	});
    

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
	function popUpAyudaAux() {
		var coords=getAbsoluteElementPosition(document.getElementById("icoAyuda"));
		document.getElementById("popUpAyuda").style.left= coords.left-135 + "px";
		document.getElementById("popUpAyuda").style.top= coords.top+15 + "px";
	}</script>
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

	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});
	
	
	function drawGraficaDepartamentos(idDiv) {
<%
double max_porcentaje = 0d;
%>
		// **************Grafica de Barras Bosque********************
		var data = google.visualization.arrayToDataTable([
	                                                  ['<%=msj.getString("departamento")%>','% <%=msj.getString("nacional")%> AT-D', {role: 'style'}],
	                                                  <%
	                                                  max_porcentaje = 0d;
	                                                  for (int i=0; i<departamentos.size(); i++) {
	                                                	  double porcentaje = departamentos.get(i).getPorcentaje(); 
	                                                	  if (porcentaje > max_porcentaje) {
	                                                		  max_porcentaje = porcentaje;
	                                                	  }
	                                                  }	   
	                                                  
	                                                  for (int i=0; i<departamentos.size(); i++) {
                                               	  			double porcentaje = departamentos.get(i).getPorcentaje();
                                               	  			double porcentaje_normalizado = porcentaje / Math.max(max_porcentaje,.1);
															int c = (int) Math.min(255,
																	Math.max(
																	0, 255-Math.floor(porcentaje_normalizado*255)
																	)
																	);
															int g = Math.min(255, c+20);
															int b = c;
															out.println("['" + departamentos.get(i).getNombre() + "'," + df1.format(departamentos.get(i).getPorcentaje()).toString().replace(",", ".") + ", 'rgb(255, "+g+", "+b+")'],");
	                                                  }
	                                                  %>	                                                  
	                                               ]);
		options = {
			title : '<%=msj.getString("alertas_por_departamentos")%>',
			width : 500,
			height : 1000,
			colors: ['red'],
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '<%=msj.getString("departamento")%>',
				titleTextStyle : {
					color : 'black'
				},
				//textPosition: "in",
		        textStyle: {
		        	fontSize:9
		        }
			},
			hAxis : {
				title : '% <%=msj.getString("nacional")%> AT-D',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document.getElementById(idDiv));
		chart.draw(data, options);
	}
	
	function drawGraficaRegiones(idDiv) {
		// **************Grafica de Barras Bosque********************
		var data = google.visualization.arrayToDataTable([
	                                                  ['<%=msj.getString("region_natural")%>','% <%=msj.getString("nacional")%> AT-D', {role: 'style'}],
	                                                  <%
	                                                  max_porcentaje = 0d;
	                                                  for (int i=0; i<regiones.size(); i++) {
	                                                	  double porcentaje = regiones.get(i).getPorcentaje(); 
	                                                	  if (porcentaje > max_porcentaje) {
	                                                		  max_porcentaje = porcentaje;
	                                                	  }
	                                                  }	   
	                                                  
	                                                  for (int i=0; i<regiones.size(); i++) {
                                             	  			double porcentaje = regiones.get(i).getPorcentaje();
                                             	  			double porcentaje_normalizado = porcentaje / Math.max(max_porcentaje,.1);
															int c = (int) Math.min(255,
																	Math.max(
																	0, 255-Math.floor(porcentaje_normalizado*255)
																	)
																	);
															int g = Math.min(255, c+20);
															int b = c;
															out.println("['" + regiones.get(i).getNombre() + "'," + df1.format(regiones.get(i).getPorcentaje()).toString().replace(",", ".") + ", 'rgb(255, "+g+", "+b+")'],");
	                                                  }
	                                                  %>	                                                  	                                                  
	                                               ]);
		options = {
			title : '<%=msj.getString("alertas_por_regiones_naturales")%>',
			width : 500,
			height : 250,
			colors: ['red'],
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '<%=msj.getString("region_natural")%>',
				titleTextStyle : {
					color : 'black'
				},
				//textPosition: "in",
		        textStyle: {
		        	fontSize:9
		        }
			},
			hAxis : {
				title : '% <%=msj.getString("nacional")%> AT-D',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document.getElementById(idDiv));
		chart.draw(data, options);
	}
	
	function drawGraficaAutoridades(idDiv) {
		// **************Grafica de Barras Bosque********************
		var data = google.visualization.arrayToDataTable([
													  ['<%=msj.getString("autoridad_ambiental")%>','% <%=msj.getString("nacional")%> AT-D', {role: 'style'}],
	                                                  <%
	                                                  max_porcentaje = 0d;
	                                                  for (int i=0; i<autoridades.size(); i++) {
	                                                	  double porcentaje = autoridades.get(i).getPorcentaje(); 
	                                                	  if (porcentaje > max_porcentaje) {
	                                                		  max_porcentaje = porcentaje;
	                                                	  }
	                                                  }	   
	                                                  
	                                                  for (int i=0; i<autoridades.size(); i++) {
	                                           	  			double porcentaje = autoridades.get(i).getPorcentaje();
	                                           	  			double porcentaje_normalizado = porcentaje / Math.max(max_porcentaje,.1);
															int c = (int) Math.min(255,
																	Math.max(
																	0, 255-Math.floor(porcentaje_normalizado*255)
																	)
																	);
															int g = Math.min(255, c+20);
															int b = c;
															out.println("['" + autoridades.get(i).getNombre() + "'," + df1.format(autoridades.get(i).getPorcentaje()).toString().replace(",", ".") + ", 'rgb(255, "+g+", "+b+")'],");
	                                                  }
	                                                  %>	                                                  	                                                  													  
	                                               ]);
		options = {
			title : '<%=msj.getString("alertas_por_autoridad_ambiental")%>',
			width : 500,
			height : 1500,
			colors: ['red'],
			backgroundColor : '#EEEEEE',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '<%=msj.getString("autoridad_ambiental")%>',
				titleTextStyle : {
					color : 'black'
				},
				//textPosition: "in",
		        textStyle: {
		        	fontSize:9
		        }
			},
			hAxis : {
				title : '% <%=msj.getString("nacional")%> AT-D',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document
				.getElementById(idDiv));
		chart.draw(data, options);
	}
	
	function activaVerPDF(nombrePDF) {
		window.scrollTo(0,0);
		document.getElementById("fondoBloqueo").style.display = "block";
		document.getElementById("popUpPDF").style.display = "block";
		
		document.getElementById('mapaCompuesto').style.display='none';
		document.getElementById('mapaDepartamentos').style.display='none';
		document.getElementById('mapaRegionesNaturales').style.display='none';
		
		document.getElementById(nombrePDF).style.display='block';
	}
	
	function desactivaVerPDF() {
		document.getElementById("fondoBloqueo").style.display = "none";
		document.getElementById("popUpPDF").style.display = "none";
	}

function descargaBoletin(nombre){
	document.getElementById("hidNomBoletin").value=nombre;
	document.getElementById("formDescargaBoletin").submit();
}

function getPDFActual(){
	return document.getElementById('hidArchivoPDF').value;
}
</script>
</head>
<body class='sidebarlast front' onmouseover="popUpUsuariosAux()">

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>

	<form id="home" action="/MonitoreoBC-WEB/idioma" method="post">
		<input type="hidden" name="lenguaje" id="lenguaje"> 
		<input type="hidden" name="pagina" id="pagina">
		<div id="page" style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">

			<%=UI.getHeader(usuario, sesion, msj, diccionarioPermisos, i18n, request.getRequestURI()) %>									

			<div id="preface" class="section-wrapper">
			<div id="preface" class="section-wrapper">
				<div class="section">
					<div class="section-inner clearfix fondoformulario">

						<h2 class="titulo_naranja"><%=msj.getString("alertas_tempranas_de_deforestacion") %></h2>

						<div id="logos" class="logos">
							<div class="logo" style="width: 32%; text-align: center !important; padding-left: 0; padding-right: 0;">
							<a href="/MonitoreoBC-WEB/reg/indexLogOn.jsp">
							<img src="../img/logos/smbyc.jpg" alt=""class="logo">
							</a>
							</div>
							<div class="logo" style="width: 32%; text-align: center !important; padding-left: 0; padding-right: 0;">
							<a href="http://www.ideam.gov.co/">
							<img src="../img/logos/ideam.jpg" alt=""class="logo">
							</a>
							</div>
							<div class="logo" style="width: 32%; text-align: center !important; padding-left: 0; padding-right: 0;">
							<a href="http://www.minambiente.gov.co/">
							<img src="../img/logos/minambiente.jpg" alt=""class="logo">
							</a>		
							</div>				
						</div>
						

						<div style="margin-top: 30px; background: #EEEEEE;">
<%-- 								<p><%=msj.getString("alertasdeforestacion.8")%>--<%=msj.getString("alertasdeforestacion.3")%></p> --%>
							<div id="divSeleccionReporte" style="text-align: center;">
								<table style="border-style:hidden;text-align: center;height: 400px">
									<tr>
									<td>
									 	<input type="button" style="cursor: pointer;width: 200px;height: 35px;font-size: 15px" value="<%=msj.getString("reporte_actual") %>" onclick="irAReportesActual(document)">
									 </td>
									 <td>
									 	<input type="button" style="cursor: pointer;width: 200px;height: 35px;font-size: 15px" value="<%=msj.getString("reportes_anteriores") %>" onclick="irAReportesAnteriores(document)">
									 </td>
									</tr>
								</table>
							</div>
							
							<div id="divReportesAnteriores" style="display: none;" >
							<div style="text-align: left;height: 400px !important; overflow: auto;">
								<table style="border-style:hidden;text-align: center;">
								<%for (int z=0;z<reportesHistoricos.size();z++) { %>
									<tr>
										<td style="cursor: pointer;font-size: 15px" 
										onclick="descargaBoletin('<%=reportesHistoricos.get(z).getNombre()%>')"><%=msj.getString("alertas_tempranas_de_deforestacion") %> - <%=msj.getString("boletin") %> <%=reportesHistoricos.get(z).getPeriodo()%></td>
									</tr>
								<%}%>
								</table>
								
							</div>
							<div style="margin-top: 50px;text-align: left;">
								<label style="font-size: 14px"><%=msj.getString("alertasdeforestacion.36")%>:</label>
								<label style="font-size: 14px"><%=msj.getString("proximas_publicaciones") %>:</label>
								<input type="button" style="cursor: pointer;" value="<%=msj.getString("alertasdeforestacion.34")%>" onclick="menuAlertas(document)">
							</div>
							</div>
								
							
							<div id="divReporteActual" style="text-align: center;display: none;">
							
								<div class="menu_alertas_tempranas">
									<a id="btnGeneralidades"  style="cursor: pointer; border:solid;" onclick="seleccionarTab('divGeneralidades',this);">&nbsp;<%=msj.getString("alertasdeforestacion.49") %>&nbsp;</a> 
									<a id="btnDepartamentos" style="cursor: pointer;" onclick="seleccionarTab('divDepartamentos',this);drawGraficaDepartamentos('graficaDepartamentos')">&nbsp;<%=msj.getString("alertasdeforestacion.50") %>&nbsp;</a>
									<a id="btnRegionesNaturales" style="cursor: pointer;" onclick="seleccionarTab('divRegionesNaturales',this);drawGraficaRegiones('graficaRegiones')">&nbsp;<%=msj.getString("alertasdeforestacion.51") %>&nbsp;</a>
									<a id="btnAutoridades"  style="cursor: pointer;" onclick="seleccionarTab('divAutoridades',this);drawGraficaAutoridades('graficaAutoridades')">&nbsp;<%=msj.getString("alertasdeforestacion.52") %>&nbsp;</a> 
									<a id="btnPersistencia" style="cursor: pointer;" onclick="seleccionarTab('divPersistencia',this)">&nbsp;<%=msj.getString("alertasdeforestacion.53") %>&nbsp;</a>
									<a id="btnConcentracion" style="cursor: pointer;" onclick="seleccionarTab('divConcentracion',this)">&nbsp;<%=msj.getString("alertasdeforestacion.54") %>&nbsp;</a>
									<a id="btnNucleos" style="cursor: pointer;" onclick="seleccionarTab('divNucleos',this)">&nbsp;<%=msj.getString("alertasdeforestacion.55") %>&nbsp;</a>
								</div>
								
								<div id="divGeneralidades" class="div_alertas_tempranas">
								
									<div style="font-size: 17px; text-align:justify; overflow:auto; width: 100%; margin-top: 10px; padding: 10px;">
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.38")%>:</label><label><%=datosAlertas.getNombre()%></label><br>
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.39") %>:</label><label><%=datosAlertas.getPeriodo()%></label><br>
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.40") %>:</label><label><%=datosAlertas.getNumero()%></label><br>
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.41")%>:</label><label><%=datosAlertas.getFecha()%></label><br>
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.42") %>:</label><label><%=datosAlertas.getDescripcion()%></label><br>
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.43") %>:</label><label><%=datosAlertas.getDireccionContacto()%></label><br>
										<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.44") %>:</label><label><%=datosAlertas.getInformacionComplementaria()%></label><br>
									</div>
									<input type="button" style="cursor: pointer;width: 250px;height: 35px;font-size: 15px" value="<%=msj.getString("descargar_boletin_oficial") %>" onclick="descargaBoletin('Boletin<%=datosAlertas.getNumero()%>.pdf')">
									
								</div>
								<div id="divDepartamentos" style="display: none;" class="div_alertas_tempranas">
								
									<table style="border-style: hidden;" class="tabla_alertas_tempranas">
										<tr>
											<td width="430px" class="td_alertas_tempranas">
												<div style="overflow: auto; width: 400px;">
												<table>
												<!-- <thead style="background-color: #31D398">-->
												<thead>
												 <tr>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;"><%=msj.getString("departamento") %></td>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;">% <%=msj.getString("nacional") %> AT-D</td>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;">% <%=msj.getString("nacional_acumulado") %> AT-D</td>
												 </tr>
												</thead>
												<!-- <tbody style="background-color: #97FFC0">-->
												<tbody style="background-color: white;">
												<%
                                                max_porcentaje = 0d;
                                                for (int i=0; i<departamentos.size(); i++) {
                                              	  double porcentaje = departamentos.get(i).getPorcentaje(); 
                                              	  if (porcentaje > max_porcentaje) {
                                              		  max_porcentaje = porcentaje;
                                              	  }
                                                }	   
                                                
                                                for (int dat=0; dat<departamentos.size(); dat++) {
                                       	  			double porcentaje = departamentos.get(dat).getPorcentaje();
                                       	  			double porcentaje_normalizado = porcentaje / Math.max(max_porcentaje,.1);
													int c = (int) Math.min(255,
															Math.max(
															0, 255-Math.floor(porcentaje_normalizado*255)
															)
															);
													int g = Math.min(255, c+20);
													int b = c;
													%>
													<tr style="background-color: rgba(255, <%=g%>, <%=b%>, 1.0); color: black !important;">
												 		<td style="color: black !important;"><%=departamentos.get(dat).getNombre()%></td>
												 		<td style="color: black !important;"><%=df1.format(departamentos.get(dat).getPorcentaje())%></td>
												 		<td style="color: black !important;"><%=df0.format(departamentos.get(dat).getPorcentajeAcumulado())%></td>
												 	</tr>
												<%
												}
												%>
												</tbody>
												</table>
												</div>
											</td>
											<td class="td_alertas_tempranas">
												<div class="descripcion_alertas_tempranas">
													<label style="font-weight: bold;"><%=msj.getString("descripcion") %>:</label><p><%=datosAlertas.getDesccripcionDepartamentos()%></p>
												</div>
												<div id="graficaDepartamentos">
													
												</div>
											</td>
										</tr>
									</table>
								
								</div>
								<div id="divRegionesNaturales" style="display: none;" class="div_alertas_tempranas">
								
									<table style="border-style: hidden;" class="tabla_alertas_tempranas">
										<tr>
											<td width="430px" class="td_alertas_tempranas">
												<div style="overflow: auto; width: 400px;">
												<table>
												<thead style="background-color: #31D398">
												 <tr>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;"><%=msj.getString("region_natural") %></td>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;">% <%=msj.getString("nacional") %> AT-D</td>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;">% <%=msj.getString("nacional_acumulado") %> AT-D</td>
												 </tr>
												</thead>
												<tbody style="background-color: white;">
													<%
	                                                max_porcentaje = 0d;
	                                                for (int i=0; i<regiones.size(); i++) {
	                                              	  double porcentaje = regiones.get(i).getPorcentaje(); 
	                                              	  if (porcentaje > max_porcentaje) {
	                                              		  max_porcentaje = porcentaje;
	                                              	  }
	                                                }	   
	                                                
	                                                for (int dat=0; dat<regiones.size(); dat++) {
	                                       	  			double porcentaje = regiones.get(dat).getPorcentaje();
	                                       	  			double porcentaje_normalizado = porcentaje / Math.max(max_porcentaje,.1);
														int c = (int) Math.min(255,
																Math.max(
																0, 255-Math.floor(porcentaje_normalizado*255)
																)
																);
														int g = Math.min(255, c+20);
														int b = c;
													%>
													<tr style="background-color: rgba(255, <%=g%>, <%=b%>, 1.0); color: black !important;">
												 		<td style="color: black !important;"><%=regiones.get(dat).getNombre()%></td>
												 		<td style="color: black !important;"><%=df1.format(regiones.get(dat).getPorcentaje())%></td>
												 		<td style="color: black !important;"><%=df0.format(regiones.get(dat).getPorcentajeAcumulado())%></td>
												 	</tr>
												<%}%>
												</tbody>
												</table>
											    </div>
											</td>
											<td class="td_alertas_tempranas">
												<div class="descripcion_alertas_tempranas">
													<label style="font-weight: bold;"><%=msj.getString("alertasdeforestacion.45")%>:</label><p><%=datosAlertas.getDescripcionRegiones()%></p>
												</div>
												<div id="graficaRegiones">
													
												</div>
											</td>
										</tr>
									</table>
								
								</div>
								<div id="divAutoridades" style="display: none;" class="div_alertas_tempranas">
								
									<table style="border-style: hidden;" class="tabla_alertas_tempranas">
										<tr>
											<td width="430px" class="td_alertas_tempranas">
												<div style="overflow: auto; width: 400px;">
												<table>
												<thead style="background-color: #31D398">
												 <tr>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;"><%=msj.getString("autoridad_ambiental")%></td>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;">% <%=msj.getString("nacional")%> AT-D</td>
												 	<td style="background-color: #aaa; color: black; font-weight: bold;">% <%=msj.getString("nacional_acumulado")%> AT-D</td>
												 </tr>
												</thead>
												<tbody style="background-color: white;">
													<%
	                                                max_porcentaje = 0d;
	                                                for (int i=0; i<autoridades.size(); i++) {
	                                              	  double porcentaje = autoridades.get(i).getPorcentaje(); 
	                                              	  if (porcentaje > max_porcentaje) {
	                                              		  max_porcentaje = porcentaje;
	                                              	  }
	                                                }	   
	                                                
	                                                for (int dat=0; dat<autoridades.size(); dat++) {
	                                       	  			double porcentaje = autoridades.get(dat).getPorcentaje();
	                                       	  			double porcentaje_normalizado = porcentaje / Math.max(max_porcentaje,.1);
														int c = (int) Math.min(255,
																Math.max(
																0, 255-Math.floor(porcentaje_normalizado*255)
																)
																);
														int g = Math.min(255, c+20);
														int b = c;
													%>
													<tr style="background-color: rgba(255, <%=g%>, <%=b%>, 1.0); color: black !important;">
												 		<td style="color: black !important;"><%=autoridades.get(dat).getNombre()%></td>
												 		<td style="color: black !important;"><%=df1.format(autoridades.get(dat).getPorcentaje())%></td>
												 		<td style="color: black !important;"><%=df0.format(autoridades.get(dat).getPorcentajeAcumulado())%></td>
												 	</tr>
												<%}%>
												</tbody>
												</table>
												</div>
											</td>
											<td class="td_alertas_tempranas">
												<div class="descripcion_alertas_tempranas">
													<label style="font-weight: bold;"><%=msj.getString("descripcion") %>:</label><p><%=datosAlertas.getDescripcionAutoridades()%></p>
												</div>
												<div id="graficaAutoridades">
													
												</div>
											</td>
										</tr>
									</table>
								
								</div>
								
								<div id="divPersistencia" style="display: none;">
								
									<div class="descripcion">
										<p><%=datosAlertas.getDescripcionPersistencia()%></p>
									</div>
									
									<object id="mapaPersistencia" data="/MonitoreoBC-WEB/descargaBoletinesAlertasServlet?nomDoc=mapa_persistencia.pdf" type="application/pdf" width="800px" height="1280px"></object>
															
								</div>
								
								<div id="divConcentracion" style="display: none;">
								
									<div class="descripcion">
										<p><%=datosAlertas.getDescripcionConcentracion()%></p>
									</div>
									
									<object id="mapaNucleos" data="/MonitoreoBC-WEB/descargaBoletinesAlertasServlet?nomDoc=mapa_compuesto.pdf" type="application/pdf" width="800px" height="1280px"></object>
										
								</div>
								
								<div id="divNucleos" style="display: none; width: 100%;">
								<script>
								function toggleDisplay(divid) {
									var div = document.getElementById(divid);
									if (div.style.display == 'block') {
										div.style.display = 'none';
									}
									else {
										div.style.display = 'block';
									}
								}
								</script>
									<div class="descripcion">
										<p><%=datosAlertas.getDescripcionNucleos()%></p>
									</div>
									
									<div class="nucleos">
										<%for(int dat=0;dat<nucleos.size();dat++){
											int i_nucleo = dat+1;
										%>
										<div class="nucleo">
											<div class="nucleo_boton" onclick="javascript:toggleDisplay('div_nucleo_<%=i_nucleo%>');">
												<p class="numero_nucleo"><%=nucleos.get(dat).getNumero()%></p>
										 		<p class="nombre_nucleo"><%=nucleos.get(dat).getNombre()%></p>
									 		</div>
									 		<div id="div_nucleo_<%=i_nucleo %>" style="display: none;">
										 		<p class="descripcion_nucleo"><%=nucleos.get(dat).getDescripcion()%></p>
										 		<div class="imagen_nucleo">
													<object id="imagenNucleo_<%=i_nucleo %>" class="imagen_nucleo" data="/MonitoreoBC-WEB/descargaImagenesAlertasServlet?nomDoc=imagen_nucleo<%=i_nucleo %>.jpg" type="image/jpg"></object>
										 		</div>
									 		</div>
									 	</div>
									<%}%>
									</div>
									
								</div>
								<div style="margin-top: 50px;text-align: left;">
								<p style="font-size: 12px; margin: 10px; border-radius: 5px; border: none; font-style: italic;">Creditos:<%=datosAlertas.getCreditos()%></p>
								<p style="font-size: 12px; margin: 10px; border-radius: 5px; border: none; font-style: italic;">Proximas publicaciones:<%=datosAlertas.getProximasPublicaciones()%></p>
								<input type="button" style="cursor: pointer;" value="<%=msj.getString("volver")%>" onclick="menuAlertas(document)">
								</div>
							</div>
							
						</div>

						<div id="logos" class="logos">
							<div class="logo">
							<a href="http://www.bancomundial.org">
							<img src="../img/logos/banco_mundial.jpg" alt=""class="logo">
							</a>
							</div>
							<div class="logo">
							<a href="http://www.sinchi.org.co/index.php/gef">
							<img src="../img/logos/corazon_de_amazonia.jpg" alt=""class="logo">
							</a> 
							</div>
							<div class="logo">
							<a href="http://www.patrimonionatural.org.co/">
							<img src="../img/logos/patrimonio_natural.jpg" alt=""class="logo">
							</a>
							</div>
							<div class="logo">
							<a href="http://www.co.undp.org/content/colombia/es/home/operations/projects/environment_and_energy/onu-redd---fortalecimiento-de-capacidades-nacionales-para-redd--.html">
							<img src="../img/logos/onu-redd.jpg" alt=""class="logo">
							</a>
							</div>
							<div class="logo">
							<a href="http://www.ecopetrol.com.co">
							<img src="../img/logos/ecopetrol.jpg" alt=""class="logo">
							</a>
							</div>
							<div class="logo">
							<a href="https://www.thegef.org/gef/">
							<img src="../img/logos/global_environment_facility.jpg" alt=""class="logo">
							</a>
							</div>
							<span class="stretch"></span>
						</div>

					</div>
				</div>
			</div>

			<%=UI.getFooter(msj) %>									

			</div>
			<!-- /.section-wrapper-->


		</div>
	</form>

	<%=UI.getCommonForms(usuario, sesion, msj, diccionarioPermisos, i18n)%>										
	
	<div id="popUpPDF" style="z-index: 3; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0; display: none;" >
		<div id="cboxContent">
		<div id="cboxTitle"><%=msj.getString("visor_pdf")%></div>
		
		<object style="display: none;" id="mapaCompuesto" data="/MonitoreoBC-WEB/descargaBoletinesAlertasServlet?nomDoc=mapa_compuesto.pdf" type="application/pdf" width="990px" height="1280px"></object>
		<object style="display: none;" id="mapaDepartamentos" data="/MonitoreoBC-WEB/descargaBoletinesAlertasServlet?nomDoc=mapa_departamentos.pdf" type="application/pdf" width="990px" height="1280px"></object>
		<object style="display: none;" id="mapaRegionesNaturales" data="/MonitoreoBC-WEB/descargaBoletinesAlertasServlet?nomDoc=mapa_regiones_naturales.pdf" type="application/pdf" width="990px" height="1280px"></object>
		
		<input type="button" value="<%=msj.getString("volver")%>" onclick="desactivaVerPDF()">
		
		</div>
	</div>

	<div id="fondoBloqueo" style="z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; top: -20; left: -20; display: none;">
         <!--Sin contenido -->
	</div>
	
	<form method="post" action="/MonitoreoBC-WEB/descargaBoletinesAlertasServlet" name="formDescargaBoletin" id="formDescargaBoletin">
		<input type="hidden" name="hidNomBoletin" id="hidNomBoletin" />
	</form>
	
</body>
</html>