<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.mbc.dao.CargaDatosInicialHome"%>  
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%@page import="co.gov.ideamredd.mbc.entities.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>     
<%@page import="co.gov.ideamredd.util.Util"%> 
<%@page import="co.gov.ideamredd.mbc.dao.ConsultaBosqueCifras"%>
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
	
	ArrayList<InfoCifras> cifrasBosqNoBosqAreasHidro= 
	ConsultaBosqueCifras.consultarBosqNoBosqAreasHidro();
	
	ArrayList<Double> cifrasBNBConsolidado = 
	ConsultaBosqueCifras.consultarPorcentCifrasConsolidado();
	
	ArrayList<Double> cifrasBNBConsolidadoHa = 
			ConsultaBosqueCifras.consultarCifrasConsolidado();
	
	ArrayList<InformacionReporteBiomasa> infoBiomasaCons = 
			ConsultaBosqueCifras.consultarInfoConsolidadoBiomasa();
	
	ArrayList<InformacionReporteCobertura> infoCoberturaCons=
	ConsultaBosqueCifras.consultarInfoCoberturaConsolidado();
	
	ArrayList<InformacionReporteBiomasaAreaHidro> infoBiomasaCarbonoAH = 
			ConsultaBosqueCifras.consultarInfoBiomasa();
	
	ArrayList<InformacionReporteCobertura> infoCoberturaAH = 
			ConsultaBosqueCifras.consultarInfoCoberturaAH();
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
	
	
	
// 	Funciones para el pop up de datos de Bosque en cifras
	function popUpReferenciasOpen() {
		var coords=getAbsoluteElementPosition(document.getElementById("divisionBosque"));
		
		document.getElementById("popUpReferencias").style.display = "block";
		document.getElementById("popUpReferencias").style.left= coords.left+210 + "px";
		document.getElementById("popUpReferencias").style.top= coords.top+200 + "px";
	}

	function popUpReferenciasClose() {
		document.getElementById("popUpReferencias").style.display = "none";
	}
//***********************************************************************	




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

	var cifrasBNBConsolidado = new Array(<%=cifrasBNBConsolidado.size()%>);
	var nombresBNBConsolidado = new Array(<%=cifrasBNBConsolidado.size()%>);

    var cifrasTipoBosqConsolidado = new Array(6);

<%for(int i=0;i<cifrasBNBConsolidado.size();i++)
{%>
		cifrasBNBConsolidado[<%=i%>] =<%=cifrasBNBConsolidado.get(i)%>;
<%}%>

<%for(int i=0;i<6;i++)
{%>
        cifrasTipoBosqConsolidado[<%=i%>] =<%=infoBiomasaCons.get(i).getArea()%>;
<%}%>

	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});

	google.setOnLoadCallback(drawGraficaBarrasConsolidado);
	google.setOnLoadCallback(drawGraficaPieConsolidado);
	google.setOnLoadCallback(drawGraficaCoberturaConsolidado);
	
//***************************************************GRAFICOS CONSOLIDADO**********************************

	function drawGraficaBarrasConsolidado() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo Bosque', 'Area', { role: 'style' }],
				['<%=infoBiomasaCons.get(0).getNombreTipoBosque()%>',cifrasTipoBosqConsolidado[0],'color: #B4EB91'], 
				['<%=infoBiomasaCons.get(1).getNombreTipoBosque()%>',cifrasTipoBosqConsolidado[1],'color: #3BB578'],
				['<%=infoBiomasaCons.get(2).getNombreTipoBosque()%>',cifrasTipoBosqConsolidado[2],'color: #B4EB91'],
				['<%=infoBiomasaCons.get(3).getNombreTipoBosque()%>',cifrasTipoBosqConsolidado[3],'color: #3BB578'],
				['<%=infoBiomasaCons.get(4).getNombreTipoBosque()%>',cifrasTipoBosqConsolidado[4],'color: #B4EB91'],
				['<%=infoBiomasaCons.get(5).getNombreTipoBosque()%>',cifrasTipoBosqConsolidado[5],'color: #3BB578']]);
		options = {
			title : 'Tipo de Bosque Consolidado',
			width : 330,
			height : 300,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divTipoBosque'));
		chart.draw(data, options);
	}
	
	function drawGraficaCoberturaConsolidado() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Area (ha)', { role: 'style' }],
				['Deforestacion',<%=infoCoberturaCons.get(0).getDeforestacion()%>,'color: #B4EB91'], 
				['Regeneracion',<%=infoCoberturaCons.get(0).getReforestacion()%>,'color: #3BB578']]);
		options = {
			title : 'Deforestacion Consolidado',
			width : 330,
			height : 100,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGraficaDeforest'));
		chart.draw(data, options);
	}

	function drawGraficaPieConsolidado() {
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area (ha)' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', cifrasBNBConsolidado[0] ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', cifrasBNBConsolidado[1] ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', cifrasBNBConsolidado[2] ] 
		]);

		options = {
			title : '<%=msj.getString("bosqueEnCifras.bosque")%> <%=msj.getString("bosqueEnCifras.consolidado").toLowerCase()%>',
			width : 270,
			height : 270,
			backgroundColor : '#188923',
			legend : {
				position : 'top',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
		    titleTextStyle:{color:"#FFFFFF"},
		    slices: {
			    0: {color: '#B4EB91'}, 
			    1: {color: '#3BB578'},
			    2: {color: '#54B58E'}}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGraficaBNB'));
		chart.draw(data, options);
	}
	
	function drawDatosConsolidado() {
		
		document.getElementById('h1CarbonoTotal').innerHTML='Carbono Total(t): <%=infoBiomasaCons.get(0).getC().longValue()%>';
		document.getElementById('h1CarbonoPromedio').innerHTML='Carbono promedio(t): <%=infoBiomasaCons.get(0).getCarbono().longValue()%>';
		document.getElementById('h1CO2').innerHTML='CO2(t): <%=infoBiomasaCons.get(0).getCO2().longValue()%>';
		
		document.getElementById('datosBosque').innerHTML='Bosque: <%=cifrasBNBConsolidadoHa.get(0).longValue()%> ha';
		document.getElementById('datosNoBosque').innerHTML='No Bosque: <%=cifrasBNBConsolidadoHa.get(1).longValue()%> ha';
		document.getElementById('datosSinInfo').innerHTML='Sin Informacion: <%=cifrasBNBConsolidadoHa.get(2).longValue()%> ha';
		
		document.getElementById('datosTipoBosque1').innerHTML='<%=infoBiomasaCons.get(0).getNombreTipoBosque()%>: <%=infoBiomasaCons.get(0).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque2').innerHTML='<%=infoBiomasaCons.get(1).getNombreTipoBosque()%>: <%=infoBiomasaCons.get(1).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque3').innerHTML='<%=infoBiomasaCons.get(2).getNombreTipoBosque()%>: <%=infoBiomasaCons.get(2).getArea().longValue()%> ha';
		
		document.getElementById('datosDeforestacion').innerHTML='Deforestacion: <%=infoCoberturaCons.get(0).getDeforestacion()%> ha';
		document.getElementById('datosRegeneracion').innerHTML='Regeneracion: <%=infoCoberturaCons.get(0).getReforestacion()%> ha';
		
		document.getElementById('datosCarbono').innerHTML='Carbono Total: <%=infoBiomasaCons.get(0).getC().longValue()%> t';
		
		document.getElementById('bcImagenGraficas').src='../img/bcConsolidado.png';
		document.getElementById('bcImagenDatos').src='../img/bcConsolidado.png';
	}
	
	
	
	//***************************************************GRAFICOS AMAZONAS**********************************

	function drawGraficaBarrasAmazonas() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo Bosque', 'Area', { role: 'style' }],
				['<%=infoBiomasaCarbonoAH.get(0).bosques.get(0).getNombre()%>',<%=infoBiomasaCarbonoAH.get(0).bosques.get(0).getArea().longValue()%>,'color: #B4EB91'], 
				['<%=infoBiomasaCarbonoAH.get(0).bosques.get(1).getNombre()%>',<%=infoBiomasaCarbonoAH.get(0).bosques.get(1).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(0).bosques.get(2).getNombre()%>',<%=infoBiomasaCarbonoAH.get(0).bosques.get(2).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(0).bosques.get(3).getNombre()%>',<%=infoBiomasaCarbonoAH.get(0).bosques.get(3).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(0).bosques.get(4).getNombre()%>',<%=infoBiomasaCarbonoAH.get(0).bosques.get(4).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(0).bosques.get(5).getNombre()%>',<%=infoBiomasaCarbonoAH.get(0).bosques.get(5).getArea().longValue()%>,'color: #3BB578']]);
		options = {
			title : 'Tipo de Bosque Amazonas',
			width : 330,
			height : 300,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divTipoBosque'));
		chart.draw(data, options);
	}
	
	function drawGraficaCoberturaAmazonas() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Area (ha)', { role: 'style' }],
				['Deforestacion',<%=infoCoberturaAH.get(0).getDeforestacion()%>,'color: #B4EB91'], 
				['Regeneracion',<%=infoCoberturaAH.get(0).getReforestacion()%>,'color: #3BB578']]);
		options = {
			title : 'Deforestacion Amazonas',
			width : 330,
			height : 100,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGraficaDeforest'));
		chart.draw(data, options);
	}

	function drawGraficaPieAmazonas() {
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area (ha)' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(0).getBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(0).getNoBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', <%=cifrasBosqNoBosqAreasHidro.get(0).getSinInfo().longValue()%> ] 
		]);

		options = {
			title : 'Bosque Amazonas',
			width : 270,
			height : 270,
			backgroundColor : '#188923',
			legend : {
				position : 'top',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
		    titleTextStyle:{color:"#FFFFFF"},
		    slices: {
			    0: {color: '#B4EB91'}, 
			    1: {color: '#3BB578'},
			    2: {color: '#54B58E'}}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGraficaBNB'));
		chart.draw(data, options);
	}
	
	function drawDatosAmazonas() {
		
		document.getElementById('h1CarbonoTotal').innerHTML='Carbono Total(t): <%=infoBiomasaCarbonoAH.get(0).C.longValue()%>';
		document.getElementById('h1CarbonoPromedio').innerHTML='Carbono promedio(t): <%=infoBiomasaCarbonoAH.get(0).Cj.longValue()%>';
		document.getElementById('h1CO2').innerHTML='CO2(t): <%=infoBiomasaCarbonoAH.get(0).CO2.longValue()%>';
		
		document.getElementById('datosBosque').innerHTML='Bosque: <%=cifrasBosqNoBosqAreasHidro.get(0).getBosque().longValue()%> ha';
		document.getElementById('datosNoBosque').innerHTML='No Bosque: <%=cifrasBosqNoBosqAreasHidro.get(0).getNoBosque().longValue()%> ha';
		document.getElementById('datosSinInfo').innerHTML='Sin Informacion: <%=cifrasBosqNoBosqAreasHidro.get(0).getSinInfo().longValue()%> ha';
		
		document.getElementById('datosTipoBosque1').innerHTML='<%=infoBiomasaCarbonoAH.get(0).bosques.get(0).getNombre()%>: <%=infoBiomasaCarbonoAH.get(0).bosques.get(0).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque2').innerHTML='<%=infoBiomasaCarbonoAH.get(0).bosques.get(1).getNombre()%>: <%=infoBiomasaCarbonoAH.get(0).bosques.get(1).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque3').innerHTML='<%=infoBiomasaCarbonoAH.get(0).bosques.get(2).getNombre()%>: <%=infoBiomasaCarbonoAH.get(0).bosques.get(2).getArea().longValue()%> ha';
		
		document.getElementById('datosDeforestacion').innerHTML='Deforestacion: <%=infoCoberturaAH.get(0).getDeforestacion()%> ha';
		document.getElementById('datosRegeneracion').innerHTML='Regeneracion: <%=infoCoberturaAH.get(0).getReforestacion()%> ha';
		
		document.getElementById('datosCarbono').innerHTML='Carbono Total: <%=infoBiomasaCarbonoAH.get(0).C.longValue()%> t';
		
		document.getElementById('bcImagenGraficas').src='../img/bcAmazonas.png';
		document.getElementById('bcImagenDatos').src='../img/bcAmazonas.png';
	}
	
	//***************************************************GRAFICOS CARIBE**********************************

	function drawGraficaBarrasCaribe() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo Bosque', 'Area', { role: 'style' }],
				['<%=infoBiomasaCarbonoAH.get(1).bosques.get(0).getNombre()%>',<%=infoBiomasaCarbonoAH.get(1).bosques.get(0).getArea().longValue()%>,'color: #B4EB91'], 
				['<%=infoBiomasaCarbonoAH.get(1).bosques.get(1).getNombre()%>',<%=infoBiomasaCarbonoAH.get(1).bosques.get(1).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(1).bosques.get(2).getNombre()%>',<%=infoBiomasaCarbonoAH.get(1).bosques.get(2).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(1).bosques.get(3).getNombre()%>',<%=infoBiomasaCarbonoAH.get(1).bosques.get(3).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(1).bosques.get(4).getNombre()%>',<%=infoBiomasaCarbonoAH.get(1).bosques.get(4).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(1).bosques.get(5).getNombre()%>',<%=infoBiomasaCarbonoAH.get(1).bosques.get(5).getArea().longValue()%>,'color: #3BB578']]);
		options = {
			title : 'Tipo de Bosque Caribe',
			width : 330,
			height : 300,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divTipoBosque'));
		chart.draw(data, options);
	}
	
	function drawGraficaCoberturaCaribe() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Area (ha)', { role: 'style' }],
				['Deforestacion',<%=infoCoberturaAH.get(1).getDeforestacion()%>,'color: #B4EB91'], 
				['Regeneracion',<%=infoCoberturaAH.get(1).getReforestacion()%>,'color: #3BB578']]);
		options = {
			title : 'Deforestacion Caribe',
			width : 330,
			height : 100,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGraficaDeforest'));
		chart.draw(data, options);
	}

	function drawGraficaPieCaribe() {
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area (ha)' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(1).getBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(1).getNoBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', <%=cifrasBosqNoBosqAreasHidro.get(1).getSinInfo().longValue()%> ] 
		]);

		options = {
			title : 'Bosque Caribe',
			width : 270,
			height : 270,
			backgroundColor : '#188923',
			legend : {
				position : 'top',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
		    titleTextStyle:{color:"#FFFFFF"},
		    slices: {
			    0: {color: '#B4EB91'}, 
			    1: {color: '#3BB578'},
			    2: {color: '#54B58E'}}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGraficaBNB'));
		chart.draw(data, options);
	}
	
	function drawDatosCaribe() {
		
		document.getElementById('h1CarbonoTotal').innerHTML='Carbono Total(t): <%=infoBiomasaCarbonoAH.get(1).C.longValue()%>';
		document.getElementById('h1CarbonoPromedio').innerHTML='Carbono promedio(t): <%=infoBiomasaCarbonoAH.get(1).Cj.longValue()%>';
		document.getElementById('h1CO2').innerHTML='CO2(t): <%=infoBiomasaCarbonoAH.get(1).CO2.longValue()%>';
		
		document.getElementById('datosBosque').innerHTML='Bosque: <%=cifrasBosqNoBosqAreasHidro.get(1).getBosque().longValue()%> ha';
		document.getElementById('datosNoBosque').innerHTML='No Bosque: <%=cifrasBosqNoBosqAreasHidro.get(1).getNoBosque().longValue()%> ha';
		document.getElementById('datosSinInfo').innerHTML='Sin Informacion: <%=cifrasBosqNoBosqAreasHidro.get(1).getSinInfo().longValue()%> ha';
		
		document.getElementById('datosTipoBosque1').innerHTML='<%=infoBiomasaCarbonoAH.get(1).bosques.get(0).getNombre()%>: <%=infoBiomasaCarbonoAH.get(1).bosques.get(0).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque2').innerHTML='<%=infoBiomasaCarbonoAH.get(1).bosques.get(1).getNombre()%>: <%=infoBiomasaCarbonoAH.get(1).bosques.get(1).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque3').innerHTML='<%=infoBiomasaCarbonoAH.get(1).bosques.get(2).getNombre()%>: <%=infoBiomasaCarbonoAH.get(1).bosques.get(2).getArea().longValue()%> ha';
		
		document.getElementById('datosDeforestacion').innerHTML='Deforestacion: <%=infoCoberturaAH.get(1).getDeforestacion()%> ha';
		document.getElementById('datosRegeneracion').innerHTML='Regeneracion: <%=infoCoberturaAH.get(1).getReforestacion()%> ha';
		
		document.getElementById('datosCarbono').innerHTML='Carbono Total: <%=infoBiomasaCarbonoAH.get(1).C.longValue()%> t';
		
		document.getElementById('bcImagenGraficas').src='../img/bcCaribe.png';
		document.getElementById('bcImagenDatos').src='../img/bcCaribe.png';
	}
	
	
	
	//***************************************************GRAFICOS ORINOCO**********************************

	function drawGraficaBarrasOrinoco() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo Bosque', 'Area', { role: 'style' }],
				['<%=infoBiomasaCarbonoAH.get(2).bosques.get(0).getNombre()%>',<%=infoBiomasaCarbonoAH.get(2).bosques.get(0).getArea().longValue()%>,'color: #B4EB91'], 
				['<%=infoBiomasaCarbonoAH.get(2).bosques.get(1).getNombre()%>',<%=infoBiomasaCarbonoAH.get(2).bosques.get(1).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(2).bosques.get(2).getNombre()%>',<%=infoBiomasaCarbonoAH.get(2).bosques.get(2).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(2).bosques.get(3).getNombre()%>',<%=infoBiomasaCarbonoAH.get(2).bosques.get(3).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(2).bosques.get(4).getNombre()%>',<%=infoBiomasaCarbonoAH.get(2).bosques.get(4).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(2).bosques.get(5).getNombre()%>',<%=infoBiomasaCarbonoAH.get(2).bosques.get(5).getArea().longValue()%>,'color: #3BB578']]);
		options = {
			title : 'Tipo de Bosque Orinoco',
			width : 330,
			height : 300,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divTipoBosque'));
		chart.draw(data, options);
	}
	
	function drawGraficaCoberturaOrinoco() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Area (ha)', { role: 'style' }],
				['Deforestacion',<%=infoCoberturaAH.get(2).getDeforestacion()%>,'color: #B4EB91'], 
				['Regeneracion',<%=infoCoberturaAH.get(2).getReforestacion()%>,'color: #3BB578']]);
		options = {
			title : 'Deforestacion Orinoco',
			width : 330,
			height : 100,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGraficaDeforest'));
		chart.draw(data, options);
	}

	function drawGraficaPieOrinoco() {
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area (ha)' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(2).getBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(2).getNoBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', <%=cifrasBosqNoBosqAreasHidro.get(2).getSinInfo().longValue()%> ] 
		]);

		options = {
			title : 'Bosque Orinoco',
			width : 270,
			height : 270,
			backgroundColor : '#188923',
			legend : {
				position : 'top',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
		    titleTextStyle:{color:"#FFFFFF"},
		    slices: {
			    0: {color: '#B4EB91'}, 
			    1: {color: '#3BB578'},
			    2: {color: '#54B58E'}}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGraficaBNB'));
		chart.draw(data, options);
	}
	
	function drawDatosOrinoco() {
		
		document.getElementById('h1CarbonoTotal').innerHTML='Carbono Total(t): <%=infoBiomasaCarbonoAH.get(2).C.longValue()%>';
		document.getElementById('h1CarbonoPromedio').innerHTML='Carbono promedio(t): <%=infoBiomasaCarbonoAH.get(2).Cj.longValue()%>';
		document.getElementById('h1CO2').innerHTML='CO2(t): <%=infoBiomasaCarbonoAH.get(2).CO2.longValue()%>';
		
		document.getElementById('datosBosque').innerHTML='Bosque: <%=cifrasBosqNoBosqAreasHidro.get(2).getBosque().longValue()%> ha';
		document.getElementById('datosNoBosque').innerHTML='No Bosque: <%=cifrasBosqNoBosqAreasHidro.get(2).getNoBosque().longValue()%> ha';
		document.getElementById('datosSinInfo').innerHTML='Sin Informacion: <%=cifrasBosqNoBosqAreasHidro.get(2).getSinInfo().longValue()%> ha';
		
		document.getElementById('datosTipoBosque1').innerHTML='<%=infoBiomasaCarbonoAH.get(2).bosques.get(0).getNombre()%>: <%=infoBiomasaCarbonoAH.get(2).bosques.get(0).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque2').innerHTML='<%=infoBiomasaCarbonoAH.get(2).bosques.get(1).getNombre()%>: <%=infoBiomasaCarbonoAH.get(2).bosques.get(1).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque3').innerHTML='<%=infoBiomasaCarbonoAH.get(2).bosques.get(2).getNombre()%>: <%=infoBiomasaCarbonoAH.get(2).bosques.get(2).getArea().longValue()%> ha';
		
		document.getElementById('datosDeforestacion').innerHTML='Deforestacion: <%=infoCoberturaAH.get(2).getDeforestacion()%> ha';
		document.getElementById('datosRegeneracion').innerHTML='Regeneracion: <%=infoCoberturaAH.get(2).getReforestacion()%> ha';
		
		document.getElementById('datosCarbono').innerHTML='Carbono Total: <%=infoBiomasaCarbonoAH.get(2).C.longValue()%> t';
		
		document.getElementById('bcImagenGraficas').src='../img/bcOrinoquia.png';
		document.getElementById('bcImagenDatos').src='../img/bcOrinoquia.png';
	}
	
	
	//***************************************************GRAFICOS MAGDALENA CAUCA**********************************

	function drawGraficaBarrasMagCauca() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo Bosque', 'Area', { role: 'style' }],
				['<%=infoBiomasaCarbonoAH.get(3).bosques.get(0).getNombre()%>',<%=infoBiomasaCarbonoAH.get(3).bosques.get(0).getArea().longValue()%>,'color: #B4EB91'], 
				['<%=infoBiomasaCarbonoAH.get(3).bosques.get(1).getNombre()%>',<%=infoBiomasaCarbonoAH.get(3).bosques.get(1).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(3).bosques.get(2).getNombre()%>',<%=infoBiomasaCarbonoAH.get(3).bosques.get(2).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(3).bosques.get(3).getNombre()%>',<%=infoBiomasaCarbonoAH.get(3).bosques.get(3).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(3).bosques.get(4).getNombre()%>',<%=infoBiomasaCarbonoAH.get(3).bosques.get(4).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(3).bosques.get(5).getNombre()%>',<%=infoBiomasaCarbonoAH.get(3).bosques.get(5).getArea().longValue()%>,'color: #3BB578']]);
		options = {
			title : 'Tipo de Bosque MagCauca',
			width : 330,
			height : 300,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divTipoBosque'));
		chart.draw(data, options);
	}
	
	function drawGraficaCoberturaMagCauca() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Area (ha)', { role: 'style' }],
				['Deforestacion',<%=infoCoberturaAH.get(3).getDeforestacion()%>,'color: #B4EB91'], 
				['Regeneracion',<%=infoCoberturaAH.get(3).getReforestacion()%>,'color: #3BB578']]);
		options = {
			title : 'Deforestacion MagCauca',
			width : 330,
			height : 100,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGraficaDeforest'));
		chart.draw(data, options);
	}

	function drawGraficaPieMagCauca() {
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area (ha)' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(3).getBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(3).getNoBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', <%=cifrasBosqNoBosqAreasHidro.get(3).getSinInfo().longValue()%> ] 
		]);

		options = {
			title : 'Bosque MagCauca',
			width : 270,
			height : 270,
			backgroundColor : '#188923',
			legend : {
				position : 'top',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
		    titleTextStyle:{color:"#FFFFFF"},
		    slices: {
			    0: {color: '#B4EB91'}, 
			    1: {color: '#3BB578'},
			    2: {color: '#54B58E'}}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGraficaBNB'));
		chart.draw(data, options);
	}
	
	function drawDatosMagCauca() {
		
		document.getElementById('h1CarbonoTotal').innerHTML='Carbono Total(t): <%=infoBiomasaCarbonoAH.get(3).C.longValue()%>';
		document.getElementById('h1CarbonoPromedio').innerHTML='Carbono promedio(t): <%=infoBiomasaCarbonoAH.get(3).Cj.longValue()%>';
		document.getElementById('h1CO2').innerHTML='CO2(t): <%=infoBiomasaCarbonoAH.get(3).CO2.longValue()%>';
		
		document.getElementById('datosBosque').innerHTML='Bosque: <%=cifrasBosqNoBosqAreasHidro.get(3).getBosque().longValue()%> ha';
		document.getElementById('datosNoBosque').innerHTML='No Bosque: <%=cifrasBosqNoBosqAreasHidro.get(3).getNoBosque().longValue()%> ha';
		document.getElementById('datosSinInfo').innerHTML='Sin Informacion: <%=cifrasBosqNoBosqAreasHidro.get(3).getSinInfo().longValue()%> ha';
		
		document.getElementById('datosTipoBosque1').innerHTML='<%=infoBiomasaCarbonoAH.get(3).bosques.get(0).getNombre()%>: <%=infoBiomasaCarbonoAH.get(3).bosques.get(0).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque2').innerHTML='<%=infoBiomasaCarbonoAH.get(3).bosques.get(1).getNombre()%>: <%=infoBiomasaCarbonoAH.get(3).bosques.get(1).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque3').innerHTML='<%=infoBiomasaCarbonoAH.get(3).bosques.get(2).getNombre()%>: <%=infoBiomasaCarbonoAH.get(3).bosques.get(2).getArea().longValue()%> ha';
		
		document.getElementById('datosDeforestacion').innerHTML='Deforestacion: <%=infoCoberturaAH.get(3).getDeforestacion()%> ha';
		document.getElementById('datosRegeneracion').innerHTML='Regeneracion: <%=infoCoberturaAH.get(3).getReforestacion()%> ha';
		
		document.getElementById('datosCarbono').innerHTML='Carbono Total: <%=infoBiomasaCarbonoAH.get(3).C.longValue()%> t';
		
		document.getElementById('bcImagenGraficas').src='../img/bcMagCauca.png';
		document.getElementById('bcImagenDatos').src='../img/bcMagCauca.png';
	}
	
	
	//***************************************************GRAFICOS PACIFICO**********************************

	function drawGraficaBarrasPacifico() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo Bosque', 'Area', { role: 'style' }],
				['<%=infoBiomasaCarbonoAH.get(4).bosques.get(0).getNombre()%>',<%=infoBiomasaCarbonoAH.get(4).bosques.get(0).getArea().longValue()%>,'color: #B4EB91'], 
				['<%=infoBiomasaCarbonoAH.get(4).bosques.get(1).getNombre()%>',<%=infoBiomasaCarbonoAH.get(4).bosques.get(1).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(4).bosques.get(2).getNombre()%>',<%=infoBiomasaCarbonoAH.get(4).bosques.get(2).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(4).bosques.get(3).getNombre()%>',<%=infoBiomasaCarbonoAH.get(4).bosques.get(3).getArea().longValue()%>,'color: #3BB578'],
				['<%=infoBiomasaCarbonoAH.get(4).bosques.get(4).getNombre()%>',<%=infoBiomasaCarbonoAH.get(4).bosques.get(4).getArea().longValue()%>,'color: #B4EB91'],
				['<%=infoBiomasaCarbonoAH.get(4).bosques.get(5).getNombre()%>',<%=infoBiomasaCarbonoAH.get(4).bosques.get(5).getArea().longValue()%>,'color: #3BB578']]);
		options = {
			title : 'Tipo de Bosque Pacifico',
			width : 330,
			height : 300,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divTipoBosque'));
		chart.draw(data, options);
	}
	
	function drawGraficaCoberturaPacifico() {
		data = google.visualization.arrayToDataTable([
				[ 'Tipo', 'Area (ha)', { role: 'style' }],
				['Deforestacion',<%=infoCoberturaAH.get(4).getDeforestacion()%>,'color: #B4EB91'], 
				['Regeneracion',<%=infoCoberturaAH.get(4).getReforestacion()%>,'color: #3BB578']]);
		options = {
			title : 'Deforestacion Pacifico',
			width : 330,
			height : 100,
			backgroundColor : '#188923',
			legend : {
				position : 'none',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
			hAxis : {
				title : 'Area (ha)',
				titleTextStyle : {
					color : 'white'
				}
			},
			titleTextStyle:{color:"#FFFFFF"}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGraficaDeforest'));
		chart.draw(data, options);
	}

	function drawGraficaPiePacifico() {
		data = google.visualization.arrayToDataTable([ [ 'Tipo', 'Area (ha)' ],
				[ '<%=msj.getString("bosqueEnCifras.bosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(4).getBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.noBosque")%>', <%=cifrasBosqNoBosqAreasHidro.get(4).getNoBosque().longValue()%> ],
				[ '<%=msj.getString("bosqueEnCifras.sinInfo")%>', <%=cifrasBosqNoBosqAreasHidro.get(4).getSinInfo().longValue()%> ] 
		]);

		options = {
			title : 'Bosque Pacifico',
			width : 270,
			height : 270,
			backgroundColor : '#188923',
			legend : {
				position : 'top',
				maxLines : 3,
				textStyle : {color:"#FFFFFF"}
			},
		    titleTextStyle:{color:"#FFFFFF"},
		    slices: {
			    0: {color: '#B4EB91'}, 
			    1: {color: '#3BB578'},
			    2: {color: '#54B58E'}}
		};

		chart = new google.visualization.PieChart(document
				.getElementById('divGraficaBNB'));
		chart.draw(data, options);
	}
	
	function drawDatosPacifico() {
		
		document.getElementById('h1CarbonoTotal').innerHTML='Carbono Total(t): <%=infoBiomasaCarbonoAH.get(4).C.longValue()%>';
		document.getElementById('h1CarbonoPromedio').innerHTML='Carbono promedio(t): <%=infoBiomasaCarbonoAH.get(4).Cj.longValue()%>';
		document.getElementById('h1CO2').innerHTML='CO2(t): <%=infoBiomasaCarbonoAH.get(4).CO2.longValue()%>';
		
		document.getElementById('datosBosque').innerHTML='Bosque: <%=cifrasBosqNoBosqAreasHidro.get(4).getBosque().longValue()%> ha';
		document.getElementById('datosNoBosque').innerHTML='No Bosque: <%=cifrasBosqNoBosqAreasHidro.get(4).getNoBosque().longValue()%> ha';
		document.getElementById('datosSinInfo').innerHTML='Sin Informacion: <%=cifrasBosqNoBosqAreasHidro.get(4).getSinInfo().longValue()%> ha';
		
		document.getElementById('datosTipoBosque1').innerHTML='<%=infoBiomasaCarbonoAH.get(4).bosques.get(0).getNombre()%>: <%=infoBiomasaCarbonoAH.get(4).bosques.get(0).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque2').innerHTML='<%=infoBiomasaCarbonoAH.get(4).bosques.get(1).getNombre()%>: <%=infoBiomasaCarbonoAH.get(4).bosques.get(1).getArea().longValue()%> ha';
		document.getElementById('datosTipoBosque3').innerHTML='<%=infoBiomasaCarbonoAH.get(4).bosques.get(2).getNombre()%>: <%=infoBiomasaCarbonoAH.get(4).bosques.get(2).getArea().longValue()%> ha';
		
		document.getElementById('datosDeforestacion').innerHTML='Deforestacion: <%=infoCoberturaAH.get(4).getDeforestacion()%> ha';
		document.getElementById('datosRegeneracion').innerHTML='Regeneracion: <%=infoCoberturaAH.get(4).getReforestacion()%> ha';
		
		document.getElementById('datosCarbono').innerHTML='Carbono Total: <%=infoBiomasaCarbonoAH.get(4).C.longValue()%> t';
		
		document.getElementById('bcImagenGraficas').src='../img/bcPacifico.png';
		document.getElementById('bcImagenDatos').src='../img/bcPacifico.png';
	}
	
	
	
	
//*******************************************FUNCIONES ACTIVACION DE PESTAÑAS Y GRAFICAS**********************************	


	var cifrasTabla=1;
	function graficasConsolidado() {
// 		drawGraficaBarrasConsolidado();
// 		drawGraficaPieConsolidado();
// 		document.getElementById('imagenBosque').src = '../img/croquisColombia.jpg';
// 		document.getElementById('btnConsolidado').style.backgroundColor='#698E1A'; 
// 		document.getElementById('btnCARs').style.border='0px';
// 		document.getElementById('btnDepartamentos').style.border='0px';
// 		cifrasTabla=1;
// 		efecto(document.getElementById('bosqueCifrasGraficas'));
		document.getElementById('btnConsolidado').style.backgroundColor='#698E1A';
		document.getElementById('btnAmazonas').style.backgroundColor='#4B660F';
		document.getElementById('btnCaribe').style.backgroundColor='#4B660F';
		document.getElementById('btnOrinoco').style.backgroundColor='#4B660F';
		document.getElementById('btnMagCauca').style.backgroundColor='#4B660F';
		document.getElementById('btnPacifico').style.backgroundColor='#4B660F'; 

		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
    	
    	drawGraficaBarrasConsolidado();
    	drawGraficaCoberturaConsolidado();
    	drawGraficaPieConsolidado();
    	drawDatosConsolidado();

		document.getElementById('nombreAreahidro').innerHTML= "DATOS GENERALES";
	}
	
	function graficasAmazonas() {
		document.getElementById('btnConsolidado').style.backgroundColor='#4B660F';
		document.getElementById('btnAmazonas').style.backgroundColor='#698E1A';
		document.getElementById('btnCaribe').style.backgroundColor='#4B660F';
		document.getElementById('btnOrinoco').style.backgroundColor='#4B660F';
		document.getElementById('btnMagCauca').style.backgroundColor='#4B660F';
		document.getElementById('btnPacifico').style.backgroundColor='#4B660F'; 

		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
    	
    	drawGraficaBarrasAmazonas();
    	drawGraficaCoberturaAmazonas();
    	drawGraficaPieAmazonas();
    	drawDatosAmazonas();

		document.getElementById('nombreAreahidro').innerHTML= "DATOS AMAZONAS";
	}
	function graficasCaribe() {
		document.getElementById('btnConsolidado').style.backgroundColor='#4B660F';
		document.getElementById('btnAmazonas').style.backgroundColor='#4B660F';
		document.getElementById('btnCaribe').style.backgroundColor='#698E1A';
		document.getElementById('btnOrinoco').style.backgroundColor='#4B660F';
		document.getElementById('btnMagCauca').style.backgroundColor='#4B660F';
		document.getElementById('btnPacifico').style.backgroundColor='#4B660F'; 
		
		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
    	
    	drawGraficaBarrasCaribe();
    	drawGraficaCoberturaCaribe();
    	drawGraficaPieCaribe();
    	drawDatosCaribe();

		document.getElementById('nombreAreahidro').innerHTML= "DATOS CARIBE";
	}
	function graficasOrinoco() {
		document.getElementById('btnConsolidado').style.backgroundColor='#4B660F';
		document.getElementById('btnAmazonas').style.backgroundColor='#4B660F';
		document.getElementById('btnCaribe').style.backgroundColor='#4B660F';
		document.getElementById('btnOrinoco').style.backgroundColor='#698E1A';
		document.getElementById('btnMagCauca').style.backgroundColor='#4B660F';
		document.getElementById('btnPacifico').style.backgroundColor='#4B660F'; 
		
		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
    	
    	drawGraficaBarrasOrinoco();
    	drawGraficaCoberturaOrinoco();
    	drawGraficaPieOrinoco();
    	drawDatosOrinoco();

		document.getElementById('nombreAreahidro').innerHTML= "DATOS ORINOCO";
	}
	function graficasMagCauca() {
		document.getElementById('btnConsolidado').style.backgroundColor='#4B660F';
		document.getElementById('btnAmazonas').style.backgroundColor='#4B660F';
		document.getElementById('btnCaribe').style.backgroundColor='#4B660F';
		document.getElementById('btnOrinoco').style.backgroundColor='#4B660F';
		document.getElementById('btnMagCauca').style.backgroundColor='#698E1A';
		document.getElementById('btnPacifico').style.backgroundColor='#4B660F'; 
		
		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
    	
    	drawGraficaBarrasMagCauca();
    	drawGraficaCoberturaMagCauca();
    	drawGraficaPieMagCauca();
    	drawDatosMagCauca();

		document.getElementById('nombreAreahidro').innerHTML= "DATOS MAGDALENA CAUCA";
	}
	function graficasPacifico() {
		document.getElementById('btnConsolidado').style.backgroundColor='#4B660F';
		document.getElementById('btnAmazonas').style.backgroundColor='#4B660F';
		document.getElementById('btnCaribe').style.backgroundColor='#4B660F';
		document.getElementById('btnOrinoco').style.backgroundColor='#4B660F';
		document.getElementById('btnMagCauca').style.backgroundColor='#4B660F';
		document.getElementById('btnPacifico').style.backgroundColor='#698E1A'; 

		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
    	
    	drawGraficaBarrasPacifico();
    	drawGraficaCoberturaPacifico();
    	drawGraficaPiePacifico();
    	drawDatosPacifico();

		document.getElementById('nombreAreahidro').innerHTML= "DATOS PACIFICO";
	}
	
	function verDatos(){
		document.getElementById('bosqueCifrasDatos').style.display='block';
    	document.getElementById('bosqueCifrasGraficas').style.display='none';
	}

	function verGraficas(){
		document.getElementById('bosqueCifrasDatos').style.display='none';
    	document.getElementById('bosqueCifrasGraficas').style.display='block';
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
								<%=msj.getString("bosqueEnCifras.bosqueEnCifras")%></h2>
							<div id="divisionBosque" style="text-align: center;background-color: #4B660F">
							<table width="99%" style="border: 0px solid #BBBBBB;">
								<tbody>
								<tr>
								<td style="border: 0px solid #BBBBBB; width:300px ">
								<h1 id="nombreAreahidro" style="color: #FFFFFF" >DATOS GENERALES</h1>
								</td>
								<td style="border: 0px solid #BBBBBB; color: #FFFFFF">
									<a id="btnConsolidado"  style="color: #FFFFFF; background-color:#698E1A; cursor: pointer; font-size: 20px" onclick="graficasConsolidado()">&nbsp;Todas&nbsp;</a>
									<a id="btnAmazonas" style="color: #FFFFFF; cursor: pointer; font-size: 20px" onclick="graficasAmazonas()">&nbsp;Amazonas&nbsp;</a>
									<a id="btnCaribe" style="color: #FFFFFF; cursor: pointer; font-size: 20px" onclick="graficasCaribe()">&nbsp;Caribe&nbsp;</a>
									<a id="btnOrinoco" style="color: #FFFFFF; cursor: pointer; font-size: 20px" onclick="graficasOrinoco()">&nbsp;Orinoco&nbsp;</a>
									<a id="btnMagCauca" style="color: #FFFFFF; cursor: pointer; font-size: 20px" onclick="graficasMagCauca()">&nbsp;Mag.Cauca&nbsp;</a>
									<a id="btnPacifico" style="color: #FFFFFF; cursor: pointer; font-size: 20px" onclick="graficasPacifico()">&nbsp;Pacifico&nbsp;</a>
								</td>
								</tr>
								</tbody>
							</table>
							</div>
							

							
							<div id="bosqueCifrasGraficas" style="display: block ; text-align: center; background-image: url('../img/fondoBosqueC.jpg');">
								<table width="99%" style="padding:0px; border: 0px solid #BBBBBB;">
									<tbody>
									<tr>
											<td colspan="2" rowspan="5" style="width:250px;height:300px; border: 0px solid #BBBBBB;">
												<img style="width:240px;height:290px;" id="bcImagenGraficas" alt="Consolidado" src="../img/bcConsolidado.png">
											</td>
											<td rowspan="4" style="width:280px; border: 0px solid #BBBBBB;">
												<div id="divGraficaBNB"></div>
											</td>
											<td rowspan="2" style="border: 0px solid #BBBBBB;">
												<div id="divGraficaDeforest"></div>
											</td>
									</tr>
									<tr></tr>
									<tr>
											<td rowspan="4" style="border: 0px solid #BBBBBB;">
												<div id="divTipoBosque"></div>
											</td>
									</tr>
									<tr></tr>
									<tr>
											<td rowspan="2" style="width:280px; border: 0px solid #BBBBBB;">
											<div id="divDatosCarbono" style="background-color: #188923;">
												<h2 id="h1CarbonoTotal" style="color: #FFFFFF" >Carbono Total(t): <%=infoBiomasaCons.get(0).getC()%></h2>
												<h2 id="h1CarbonoPromedio" style="color: #FFFFFF" >Carbono promedio(t): <%=infoBiomasaCons.get(0).getCarbono()%></h2>
												<h2 id="h1CO2" style="color: #FFFFFF" >CO2(t): <%=infoBiomasaCons.get(0).getCO2()%></h2>
											</div>
											</td>
									</tr>
									<tr>
											<td style="width:125px; height:20px; border: 0px solid #BBBBBB;">
												<input type="button" value="Referencias" onclick="popUpReferenciasOpen()">
											</td>
											<td style="width:125px; height:20px; border: 0px solid #BBBBBB;">
												<input type="button" value="Ver Datos" onclick="verDatos()">
											</td>
									</tr>
									</tbody>
								</table>
							</div>
							
							<div id="bosqueCifrasDatos" style="display: none ; text-align: center; background-image: url('../img/fondoBosqueC.jpg');">
								<table width="99%" style="padding:0px; border: 0px solid #BBBBBB;">
									<tbody>
									<tr>
											<td colspan="2" style="width:250px;height:300px; border: 0px solid #BBBBBB;">
												<img style="width:240px;height:290px;" id="bcImagenDatos" alt="Consolidado" src="../img/bcConsolidado.png">
											</td>
											
											<td rowspan="2" style="height:450px; width:600px; border: 0px solid #BBBBBB;">
												<div style="height:400px; background-color: #188923; color: #FFFFFF;text-align: left;padding: 10px">
												<h2 style="font-style: italic;">Bosque</h2>
												  <ul>
												    <li id="datosBosque">Bosque: <%=cifrasBNBConsolidadoHa.get(0).longValue()%> ha</li>
												    <li id="datosNoBosque">No Bosque: <%=cifrasBNBConsolidadoHa.get(1).longValue()%> ha</li>
												    <li id="datosSinInfo">Sin Informacion: <%=cifrasBNBConsolidadoHa.get(2).longValue()%> ha</li>
												  </ul>
												<h2 style="font-style: italic;">Tipo de Bosque</h2>
												  <ul>
												    <li id="datosTipoBosque1"><%=infoBiomasaCons.get(0).getNombreTipoBosque()%>: <%=infoBiomasaCons.get(0).getArea().longValue()%> ha</li>
												    <li id="datosTipoBosque2"><%=infoBiomasaCons.get(1).getNombreTipoBosque()%>: <%=infoBiomasaCons.get(1).getArea().longValue()%> ha</li>
												    <li id="datosTipoBosque3"><%=infoBiomasaCons.get(2).getNombreTipoBosque()%>: <%=infoBiomasaCons.get(2).getArea().longValue()%> ha</li>
												  </ul>
												<h2 style="font-style: italic;">Deforestacion</h2>
												  <ul>
												    <li id="datosDeforestacion">Deforestacion: <%=infoCoberturaCons.get(0).getDeforestacion().longValue()%> ha</li>
												    <li id="datosRegeneracion">Regeneracion: <%=infoCoberturaCons.get(0).getReforestacion().longValue()%> ha</li>
												  </ul>
												<h2 style="font-style: italic;">Carbono</h2>
												  <ul>
												    <li id="datosCarbono">Carbono Total: <%=infoBiomasaCons.get(0).getC()%> t</li>
												  </ul>
												</div>
											</td>
									</tr>
									<tr>
											<td style="width:125px; height:20px; border: 0px solid #BBBBBB;">
												<input type="button" value="Referencias" onclick="popUpReferenciasOpen()">
											</td>
											<td style="width:125px; height:20px; border: 0px solid #BBBBBB;">
												<input type="button" value="Ver Graficas" onclick="verGraficas()">
											</td>
									</tr>
									</tbody>
								</table>
							</div>

							
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
											<p>Las &aacute;reas n&uacute;cleo de deforestaci&oacute;n en Colombia, en las cuales se concentra la mayor p&eacute;rdida de bosque se
											 localizan en los Caquet&aacute;-Putumayo, Meta-Guaviare y el eje San Jos&eacute; del Guaviare-Calamar.
											Se evidencia otro foco activo, en Antioquia, en los municipios de Segovia, Turbo, Ituango, Anor&iacute;, 
											y El Bagre, siendo las zonas de mayor afectaci&oacute;n con una p&eacute;rdida de bosque entre el 45 y el 75%.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>MonitoreoBC-WEB/pub/alertasDeforestacion.jsp">Ver más</a>
											</div>
										</div>
									</div>

									<div id="block-carbono" class="blockx245 block-vermas block">
										<div class="content">
											<h2>Visor Geografico</h2>
											<img src="../img/Visor.jpg">
											<p>Conozca dónde se localizan los Bosques de Colombia y observe los análisis de la información de los últimos 23 años 
											en los cuales se evidencia una desconexión de la masa boscosa de la Amazonia y el bosque natural de la cordillera oriental.
											 Con nuestro Visor geográfico, puede explorar las áreas de Deforestación, en las cuales se observa 
											cómo el 57% de la deforestación nacional se localiza en la región de la Amazonía, mientras que un 22% se reportó en la región andina.</p>
											<div class="ver-mas">
												<a href="<%=basePath2%>AdmIF/Parcela?accion=busqueda_parcelas&usuario=0&idioma=es">Ver más</a>
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
	
	
	<div id="popUpReferencias"
		style="z-index: 3; position: absolute; width: 500px; height: 150px; background: none repeat scroll 0 0 #B4EB91; display: none; border: 3px solid #3BB578;">
		
		<div style="background: none repeat scroll 0 0 #3BB578;">
			<label for="exampleInputEmail1" onclick="popUpReferenciasClose()"
				style="text-align: right; color: white; font-weight: bold; cursor: pointer;">
				<%=msj.getString("home.popAyuda.cerrar")%></label></div>
				
		<div style="background: none repeat scroll 0 0 #3BB578;">
			<label for="exampleInputEmail1"
				style="text-align: center; color: white; font-weight: bold;">
				Referencias</label>
		</div>
		
		<div style="height: 120px">
		<label>**Galindo. et al., IDEAM 2011.</label>
		<label>**Phillips et al. IDEAM 2011.</label>
		<label>**Cabrera et. al, IDEAM, 2011.</label>
		</div>
		
	</div>
	
</body>
</html>