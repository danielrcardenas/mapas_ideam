<%@ page language="java" import="java.util.*" import="java.math.*"
	pageEncoding="ISO-8859-1"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<%@page import="co.gov.ideamredd.reportes.LecturaArchivo"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/";

	String reporte[] = (String[]) request.getSession().getAttribute(
	"reporte");
	String tipoGrafica = (String) request.getSession().getAttribute(
	"tipoGrafica");
	String tipoDivision = (String) request.getSession().getAttribute(
	"tipoDivision");
	
	//*******************Datos Generales**************
	String[] nombresCARs=null;
	String[] nombresDepartamentos=null;
	String[] nombresAreasHidro=null;
	
	//*******************Datos Para Graficas de Biomasa
	ArrayList<BigDecimal[][]> datosBiomasa=null;
	ArrayList<Double[][]> datosBiomasa2=null;
	ArrayList<BigDecimal[]> datosBiomasaConsolidado=null;
	ArrayList<Double[]> datosBiomasaConsolidado2=null;
	String[] nombresTiposBosque=null;
	String PeriodoBiomasa=null;
	
	//*******************Datos Para Graficas de Bosque
	ArrayList<Double[][][]> datosBosque=null;
	ArrayList<Double[][]> datosBosqueConsolidado=null;
	String[] PeriodosBosque=null;
	
	if(tipoGrafica.equals("Biomasa") || tipoGrafica.equals("Deforestacion")){  
		if(tipoGrafica.equals("Biomasa"))
		{
	datosBiomasa = (ArrayList<BigDecimal[][]>)request.getSession().getAttribute(
	"listaDatosWeb");
	datosBiomasaConsolidado = (ArrayList<BigDecimal[]>)request.getSession().getAttribute(
	"listaDatosConsolidadoWeb");
		}
		if(tipoGrafica.equals("Deforestacion"))
		{
	datosBiomasa2 = (ArrayList<Double[][]>)request.getSession().getAttribute(
	"listaDatosWeb");
	datosBiomasaConsolidado2 = (ArrayList<Double[]>)request.getSession().getAttribute(
	"listaDatosConsolidadoWeb");
		}
	PeriodoBiomasa=(String)request.getSession().getAttribute(
	"PeriodoBiomasa");
	nombresAreasHidro= (String[])request.getSession().getAttribute(
	"nombresAreasHidro");
	nombresTiposBosque= (String[])request.getSession().getAttribute(
	"nombresTiposBosque");
		
	}else
	if(tipoGrafica.equals("Bosque") || tipoGrafica.equals("Cobertura")){
		datosBosque = (ArrayList<Double[][][]>)request.getSession().getAttribute(
		"listaDatosWeb");
		datosBosqueConsolidado = (ArrayList<Double[][]>)request.getSession().getAttribute(
		"listaDatosConsolidadoWeb");
		PeriodosBosque=(String[])request.getSession().getAttribute(
		"PeriodosBosque");
		nombresCARs= (String[])request.getSession().getAttribute(
		"nombresCARs");
		nombresDepartamentos= (String[])request.getSession().getAttribute(
		"nombresDepartamentos");
		nombresAreasHidro= (String[])request.getSession().getAttribute(
		"nombresAreasHidro");
	}
%>

<link rel="stylesheet" href="../css/ideam/datum-custom.css"
	type="text/css"></link>
<script type="text/javascript" src="../js/GBiomasa.js"></script>
<script type="text/javascript" src="../js/GBosque.js"></script>
<script type="text/javascript" src="../js/GCobertura.js"></script>
<script type="text/javascript" src="../js/GDeforestacion.js"></script>
<script type="text/javascript">

    //Variables globales
    var i=0,j=0,k=0;
    var tipoDivision="";
    tipoDivision='<%=tipoDivision%>';
    <%if(tipoGrafica=="Biomasa" || tipoGrafica=="Deforestacion"){%>
	var nombresAreasHidro = new Array(<%=nombresAreasHidro.length%>);
	<%}%>
    <%if(tipoGrafica=="Bosque" || tipoGrafica=="Cobertura"){%>
    var nombresCARs = new Array(<%=nombresCARs.length%>);
	var nombresDepartamentos = new Array(<%=nombresDepartamentos.length%>);
	var nombresAreasHidro = new Array(<%=nombresAreasHidro.length%>);
	<%}%>
	
   //*******************Variables para graficas Biomasa******************
    <%if(tipoGrafica=="Biomasa" || tipoGrafica=="Deforestacion")
	{%>
	var nombresTiposBosque = new Array(<%=nombresTiposBosque.length%>);
	var datosBiomasa=new Array(<%=nombresAreasHidro.length%>);
	var datosBiomasaConsolidado=new Array(<%=nombresTiposBosque.length%>);
	var periodo="";
	<%}%>
	
	//*******************Variables para graficas Bosque******************
	<%if(tipoGrafica=="Bosque" || tipoGrafica=="Cobertura")
	{%>
	var datosBosque=new Array(<%=PeriodosBosque.length%>);
    var datosBosqueConsolidado=new Array(<%=PeriodosBosque.length%>);
	var periodo=new Array(<%=PeriodosBosque.length%>);
	<%}%>

    //***********Procesado de datos Biomasa********
	<%if(tipoGrafica=="Biomasa" || tipoGrafica=="Deforestacion")
	{%>
	 <%if(tipoDivision=="AreasHidrograficas")
	   {%>
	
		for(i=0;i<datosBiomasa.length;i++)
		{
			<%if(tipoGrafica=="Biomasa")
			{%>
			datosBiomasa[i]=new Array(15);
			<%}%>
			<%if(tipoGrafica=="Deforestacion")
			{%>
			datosBiomasa[i]=new Array(12);
			<%}%>
		}
		for(i=0;i<datosBiomasa.length;i++)
		{
			for(j=0;j<datosBiomasa[0].length;j++)
			{
				<%if(tipoGrafica=="Biomasa")
				{%>
				datosBiomasa[i][j]=new Array(2);
				<%}%>
				<%if(tipoGrafica=="Deforestacion")
				{%>
				datosBiomasa[i][j]=new Array(1);
				<%}%>	
			}
		}
		<%if(tipoGrafica=="Biomasa")
		{%>
		<%for(int cont=0;cont<nombresAreasHidro.length;cont++){%> 
		nombresAreasHidro[<%=cont%>]='<%=nombresAreasHidro[cont]%>';
		<%}%>
		<%for(int cont=0;cont<nombresTiposBosque.length;cont++){%> 
		nombresTiposBosque[<%=cont%>]='<%=nombresTiposBosque[cont]%>';
		<%}%>
		<%for(int i=0;i<datosBiomasa.size();i++){%> 
		<%for(int j=0;j<datosBiomasa.get(0).length;j++){%> 
		<%for(int k=0;k<datosBiomasa.get(0)[0].length;k++){%> 
		datosBiomasa[<%=i%>][<%=j%>][<%=k%>]=<%=datosBiomasa.get(i)[j][k]%>;
		<%}%>
		<%}%>
		<%}%>
		<%}%>
		<%if(tipoGrafica=="Deforestacion")
		{%>
		<%for(int cont=0;cont<nombresAreasHidro.length;cont++){%> 
		nombresAreasHidro[<%=cont%>]='<%=nombresAreasHidro[cont]%>';
		<%}%>
		<%for(int cont=0;cont<nombresTiposBosque.length;cont++){%> 
		nombresTiposBosque[<%=cont%>]='<%=nombresTiposBosque[cont]%>';
		<%}%>
		<%for(int i=0;i<datosBiomasa2.size();i++){%> 
		<%for(int j=0;j<datosBiomasa2.get(0).length;j++){%> 
		<%for(int k=0;k<datosBiomasa2.get(0)[0].length;k++){%> 
		datosBiomasa[<%=i%>][<%=j%>][<%=k%>]=<%=datosBiomasa2.get(i)[j][k]%>;
		<%}%>
		<%}%>
		<%}%>
		<%}%>
	  <%}%>
	  <%if(tipoDivision=="ConsolidadoNacional")
	   {%>

	   <%if(tipoGrafica=="Biomasa")
		{%>
		for(i=0;i<datosBiomasaConsolidado.length;i++)
		{
			datosBiomasaConsolidado[i]=new Array(2);
		}
		<%for(int cont=0;cont<nombresAreasHidro.length;cont++){%> 
		nombresAreasHidro[<%=cont%>]='<%=nombresAreasHidro[cont]%>';
		<%}%>
		<%for(int cont=0;cont<nombresTiposBosque.length;cont++){%> 
		nombresTiposBosque[<%=cont%>]='<%=nombresTiposBosque[cont]%>';
		<%}%>
		<%for(int i=0;i<datosBiomasaConsolidado.size();i++){%> 
		<%for(int j=0;j<datosBiomasaConsolidado.get(0).length;j++){%> 
		datosBiomasaConsolidado[<%=i%>][<%=j%>]=<%=datosBiomasaConsolidado.get(i)[j]%>;
		<%}%>
		<%}%>
		<%}%>
		<%if(tipoGrafica=="Deforestacion")
		{%>
		for(i=0;i<datosBiomasaConsolidado.length;i++)
		{
			datosBiomasaConsolidado[i]=new Array(1);
		}
		<%for(int cont=0;cont<nombresAreasHidro.length;cont++){%> 
		nombresAreasHidro[<%=cont%>]='<%=nombresAreasHidro[cont]%>';
		<%}%>
		<%for(int cont=0;cont<nombresTiposBosque.length;cont++){%> 
		nombresTiposBosque[<%=cont%>]='<%=nombresTiposBosque[cont]%>';
		<%}%>
		<%for(int i=0;i<datosBiomasaConsolidado2.size();i++){%> 
		<%for(int j=0;j<datosBiomasaConsolidado2.get(0).length;j++){%> 
		datosBiomasaConsolidado[<%=i%>][<%=j%>]=<%=datosBiomasaConsolidado2.get(i)[j]%>;
		<%}%>
		<%}%>
		<%}%>
	  <%}%>
	<%}%>

	//***********Procesado de datos Bosque********
	<%if(tipoGrafica=="Bosque" || tipoGrafica=="Cobertura")
	{%>
	 <%for(int cont=0;cont<PeriodosBosque.length;cont++){%> 
	 periodo[<%=cont%>]='<%=PeriodosBosque[cont]%>';
     <%}%>
	 <%if(tipoDivision=="CARs" || 
	 tipoDivision=="Departamentos" ||
	 tipoDivision=="AreasHidrograficas")
	   {%>
	
		for(i=0;i<datosBosque.length;i++)
		{
			if(tipoDivision=="CARs")
			{
				datosBosque[i]=new Array(<%=nombresCARs.length%>);
			}else
			if(tipoDivision=="Departamentos")
			{
				datosBosque[i]=new Array(<%=nombresDepartamentos.length%>);
			}else
			if(tipoDivision=="AreasHidrograficas")
			{
				datosBosque[i]=new Array(<%=nombresAreasHidro.length%>);
			}
		}
		for(i=0;i<datosBosque.length;i++)
		{
			for(j=0;j<datosBosque[0].length;j++)
			{
				<%if(tipoGrafica=="Bosque")
				{%>
				datosBosque[i][j]=new Array(3);
				<%}else%>
				<%if(tipoGrafica=="Cobertura")
				{%>
				datosBosque[i][j]=new Array(5);
				<%}%>
			}
		}
		for(i=0;i<datosBosque.length;i++)
		{
			for(j=0;j<datosBosque[0].length;j++)
			{
				for(k=0;k<datosBosque[0][0].length;k++)
				{
					datosBosque[i][j][k]=new Array(1);
				}
			}
		}
		<%for(int cont=0;cont<nombresCARs.length;cont++){%> 
		nombresCARs[<%=cont%>]='<%=nombresCARs[cont]%>';
		<%}%>
		<%for(int cont=0;cont<nombresDepartamentos.length;cont++){%> 
		nombresDepartamentos[<%=cont%>]='<%=nombresDepartamentos[cont]%>';
		<%}%>
		<%for(int cont=0;cont<nombresAreasHidro.length;cont++){%> 
		nombresAreasHidro[<%=cont%>]='<%=nombresAreasHidro[cont]%>';
		<%}%>
		<%for(int i=0;i<datosBosque.size();i++){%> 
		<%for(int j=0;j<datosBosque.get(0).length;j++){%> 
		<%for(int k=0;k<datosBosque.get(0)[0].length;k++){%> 
		datosBosque[<%=i%>][<%=j%>][<%=k%>][0]=<%=datosBosque.get(i)[j][k][0]%>;
		<%}%>
		<%}%>
		<%}%>
	  <%}%>
	  <%if(tipoDivision=="ConsolidadoNacional")
 	  {%> 
		for(i=0;i<datosBosqueConsolidado.length;i++)
		{
			<%if(tipoGrafica=="Bosque")
			{%>
			datosBosqueConsolidado[i]=new Array(3);
			<%}else%>
			<%if(tipoGrafica=="Cobertura")
			{%>
			datosBosqueConsolidado[i]=new Array(5);
			<%}%>
		}
		for(i=0;i<datosBosqueConsolidado.length;i++)
		{
			for(j=0;j<datosBosqueConsolidado[0].length;j++)
			{
				datosBosqueConsolidado[i][j]=new Array(1);
			}
		}
		<%for(int i=0;i<datosBosqueConsolidado.size();i++){%> 
		<%for(int j=0;j<datosBosqueConsolidado.get(0).length;j++){%> 
		datosBosqueConsolidado[<%=i%>][<%=j%>][0]=<%=datosBosqueConsolidado.get(i)[j][0]%>;
		<%}%>
		<%}%>
	  <%}%>
	<%}%>
	
	function cambiarGBiomasa()
	{
		if(tipoDivision=="AreasHidrograficas")
		{
			areaHidroGBiomasa(nombresAreasHidro, nombresTiposBosque, datosBiomasa,
				'<%=PeriodoBiomasa%>', document);
		}
		if(tipoDivision=="ConsolidadoNacional")
		{
			consolidadoGBiomasa(nombresAreasHidro, nombresTiposBosque, datosBiomasaConsolidado,
					'<%=PeriodoBiomasa%>', document);
		}
	}

	function cambiarGBosque() {
		if (tipoDivision != "ConsolidadoNacional") {
			if (tipoDivision == "CARs") {
				datosGBosque(nombresCARs, datosBosque, periodo, document);
			} else if (tipoDivision == "Departamentos") {
				datosGBosque(nombresDepartamentos, datosBosque, periodo,
						document);
			} else if (tipoDivision == "AreasHidrograficas") {
				datosGBosque(nombresAreasHidro, datosBosque, periodo, document);
			}
		} else {
			datosConsolidadoGBosque(datosBosqueConsolidado, periodo, document);
		}
	}

	function cambiarGCobertura() {
		if (tipoDivision != "ConsolidadoNacional") {
			if (tipoDivision == "CARs") {
				datosGCobertura(nombresCARs, datosBosque, periodo, document);
			} else if (tipoDivision == "Departamentos") {
				datosGCobertura(nombresDepartamentos, datosBosque, periodo,
						document);
			} else if (tipoDivision == "AreasHidrograficas") {
				datosGCobertura(nombresAreasHidro, datosBosque, periodo,
						document);
			}
		} else {
			datosConsolidadoGCobertura(datosBosqueConsolidado, periodo,
					document);
		}
	}

	function cambiarGDeforestacion()
	{
		if(tipoDivision=="AreasHidrograficas")
		{
			areaHidroGDeforestacion(nombresAreasHidro, nombresTiposBosque, datosBiomasa,
				'<%=PeriodoBiomasa%>', document);
		}
		if(tipoDivision=="ConsolidadoNacional")
		{
			consolidadoGDeforestacion(nombresAreasHidro, nombresTiposBosque, datosBiomasaConsolidado,
					'<%=PeriodoBiomasa%>', document);
		}
	}
	
</script>
<script type="text/javascript">
	function cambiarTab(botonNombre) {

		var tabTablas = document.getElementById("divTablas");
		var tabGraficas = document.getElementById("divGraficas");
		var tabMapas = document.getElementById("divMapas");

		if (botonNombre == "btnTablas") {
			tabTablas.style.display = 'block';
			tabGraficas.style.display = 'none';
			tabMapas.style.display = 'none';
		} else if (botonNombre == "btnGraficas") {
			tabTablas.style.display = 'none';
			tabGraficas.style.display = 'block';
			tabMapas.style.display = 'none';
		} else if (botonNombre == "btnMapas") {
			tabTablas.style.display = 'none';
			tabGraficas.style.display = 'none';
			tabMapas.style.display = 'block';
		}

	}
	
	function descargarReporte(tipoArchivo)
	{
		document.getElementById("tipoArchivo").value = tipoArchivo;
		document.getElementById("formDescargaDoc").submit();
	}
</script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});
	google.load('visualization', '1', {
		'packages' : [ 'geomap' ]
	});

	google.setOnLoadCallback(drawGeoChart);

	function drawGeoChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Department', 'i' ], [ 'CO-DC', 5 ], [ 'CO-AMA ', 100 ],
				[ 'CO-ANT', 20 ], [ 'CO-ARA', 20 ], [ 'CO-ATL ', 20 ],
				[ 'CO-BOL ', 20 ], [ 'CO-BOY ', 20 ], [ 'CO-CAL ', 20 ],
				[ 'CO-CAQ ', 20 ], [ 'CO-CAS ', 20 ], [ 'CO-CAU ', 20 ],
				[ 'CO-CES ', 20 ], [ 'CO-COR ', 20 ], [ 'CO-CUN ', 20 ],
				[ 'CO-CHO ', 20 ], [ 'CO-GUA ', 20 ], [ 'CO-GUV ', 20 ],
				[ 'CO-HUI ', 20 ], [ 'CO-LAG ', 20 ], [ 'CO-MAG ', 20 ],
				[ 'CO-MET', 20 ], [ 'CO-NAR ', 20 ], [ 'CO-NSA', 20 ],
				[ 'CO-PUT', 20 ], [ 'CO-QUI ', 20 ], [ 'CO-RIS ', 20 ],
				[ 'CO-SAP ', 20 ], [ 'CO-SAN', 20 ], [ 'CO-SUC ', 20 ],
				[ 'CO-TOL', 20 ], [ 'CO-VAC', 20 ], [ 'CO-VAU', 20 ],
				[ 'CO-VID', 20 ] ]);

		var geomap = new google.visualization.GeoMap(document
				.getElementById('divGMapas'));
		geomap.draw(data, {
			showLegend : false,
			region : 'CO',
			width : 627,
			height : 418,
			showZoomOut : false,
			tooltip : {
				trigger : 'none'
			}
		});
	}
</script>
</head>

<body style="background-color: #FFFFFF">
	<div id="divPrinsipal" style="width: 1256px; margin: 0 auto;">
		<div id="divBanner" style="float: top; margin: 0 auto;"></div>
		<div id="divMenuI" style="float: left; margin: 0 auto;"></div>
		<div id="divContenido" style="float: right; width: 1256px;">
			<h3 align="center">RESULTADOS DE REPORTES REDD</h3>
			<fieldset style="margin: 10px; padding: 3px;">
				<%-- 			<form action="<%=basePath%>regresarAConsultarRepoServlet" method="post"> --%>
				<!-- 				<input type="submit" value="VOLVER" /> -->
				<!-- 			</form> -->
				<input type="button" value="VOLVER" class="boton"
					onclick="location.href='<%=basePath%>pub/consultarReporte.jsp';" />
				<form action="<%=basePath%>descargaDocumentosServlet" method="post" id="formDescargaDoc">
					<input type="hidden" value="<%=reporte[2]%>" name="proyecto" id="proyecto"></input>  
						<input type="hidden" value="<%=reporte[0]%>" name="path" id="path"></input> 
						<input type="hidden" name="tipoArchivo" id="tipoArchivo"></input> 
						<input type="hidden" value="<%=request.getSession().getAttribute("PDFtiporeporte")%>" 
						name="PDFtiporeporte" id="PDFtiporeporte"></input> 
						<input type="hidden" value="<%=request.getSession().getAttribute("PDFdivision")%>" 
					    name="PDFdivision" id="PDFdivision"></input> 
					    <input type="hidden" value="<%=request.getSession().getAttribute("variosPeriodos")%>" 
					    name="variosPeriodos" id="variosPeriodos"></input>
					    <input type="hidden" value="<%=request.getSession().getAttribute("PDFperiodos")%>" 
					    name="PDFperiodos" id="PDFperiodos"></input>
					    <input type="hidden" value="<%=request.getSession().getAttribute("PDFperiodo")%>" 
					    name="PDFperiodo" id="PDFperiodo"></input>
						<input type="button" value="DESCARGAR XLSX" onclick="descargarReporte('xlsx')" class="boton"></input>
						<input type="button" value="DESCARGAR PDF" onclick="descargarReporte('pdf')" class="boton"></input>
				</form>
				<br /> <br /> <br /> <input type="button" name="btnTablas"
					id="btnTablas" onclick="cambiarTab(this.name)" value="ver Tabla">
				<input type="button" name="btnGraficas" id="btnGraficas"
					onclick="cambiarTab(this.name)" value="ver Graficas"> <input
					type="button" name="btnMapas" id="btnMapas"
					onclick="cambiarTab(this.name)" value="ver Mapas">
			</fieldset>
			<div id="divAclaracion"
				style="float: left; width: 1200px; padding: 20px; border: 1px groove black; margin: 10px;">
				<h3>Aclaraci&oacute;n</h3>
				<p>Los reportes de los valores de &aacute;reas de bosque,
					deforestaci&oacute;n o biomasa pueden variar respecto a los
					publicados en el documento "Memoria T&eacute;cnica de la
					Cuantificaci&oacute;n de la Deforestaci&oacute;n Hist&oacute;rica
					Nacional Escalas Gruesa y Fina " (Cabrera et al, IDEAM, 2011)
					debido a los cambios de proyección de los mapas originales. Los
					mapas de Cobertura de Bosque, Cambio de la Cobertura de Bosque y
					biomasa reportados en la Memoria técnica se generaron en el Sistema
					de Referencia por Coordenadas (SRC) WGS84 proyecci&oacute;n UTM
					Zona 18 Norte. Para su publicación en el portal, estos mapas se
					migraron al SRC Marco Geoc&eacute;ntrico Nacional (MAGNA) oficial
					para Rep&uacute;blica de Colombia.</p>
				<p>Debido a que los par&aacute;metros Latitud Central, Longitud
					Central, Falso Norte y Falso Este en las dos proyecciones son
					diferentes, el cambio de Sistema de Referencia genera variaciones
					en las caracter&iacute;sticas espaciales de los mapas
					(principalmente tamaño de pixel y alg&uacute;n desplazamiento
					m&iacute;nimo) y por ende cambios en los valores de las
					&aacute;reas.</p>
			</div>
			<div id="divTablas"
				style="margin: 0 auto; overflow: auto; height: 600px; width: 1256px; float: left;">
				<table width="99%" align="center" class="tblSalida">
					<%=reporte[1]%>
				</table>
			</div>
			<%
				if (tipoDivision != "ConsolidadoNacional") {
					out.println("<div id=\"divGraficas\""
							+ "style=\"display: none; overflow: auto; margin: 0 auto; float: left;\">"
							+ "<div id=\"divSelect\">");

					if (tipoGrafica == "Biomasa" || tipoGrafica == "Deforestacion") {
						if (tipoGrafica == "Biomasa")
						{
						out.println("<h3>"
								+ "Selecciona Area Hidrografica: "
								+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
								+ "onchange=\"cambiarGBiomasa()\">"
								+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
								+ "<option id=\"bioGOP1\" value=\"1\">Caribe</option>"
								+ "<option id=\"bioGOP2\" value=\"2\">Magdalena Cauca</option>"
								+ "<option id=\"bioGOP3\" value=\"3\">Orinoco</option>"
								+ "<option id=\"bioGOP4\" value=\"4\">Amazonas</option>"
								+ "<option id=\"bioGOP5\" value=\"5\">Pacifico</option>"
								+ "</select>" + "</h3>");
						}
						if (tipoGrafica == "Deforestacion")
						{
						out.println("<h3>"
								+ "Selecciona Area Hidrografica: "
								+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
								+ "onchange=\"cambiarGDeforestacion()\">"
								+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
								+ "<option id=\"bioGOP1\" value=\"1\">Caribe</option>"
								+ "<option id=\"bioGOP2\" value=\"2\">Magdalena Cauca</option>"
								+ "<option id=\"bioGOP3\" value=\"3\">Orinoco</option>"
								+ "<option id=\"bioGOP4\" value=\"4\">Amazonas</option>"
								+ "<option id=\"bioGOP5\" value=\"5\">Pacifico</option>"
								+ "</select>" + "</h3>");
						}
					} else if (tipoGrafica == "Bosque"
							|| tipoGrafica == "Cobertura") {
						String divisionesS = "";
						String periodosS = "";
						if (tipoDivision == "CARs") {
							divisionesS = "";
							periodosS = "";

							for (int x = 0; x < nombresCARs.length; x++) {
								divisionesS = divisionesS + "<option id=\"bosGOP"
										+ (x + 1) + "\" value=\"" + (x + 1) + "\">"
										+ nombresCARs[x] + "</option>";
							}
							for (int x = 0; x < PeriodosBosque.length; x++) {
								periodosS = periodosS + "<option id=\"opGOP" + x
										+ "\" value=\"" + x + "\">"
										+ PeriodosBosque[x] + "</option>";
							}

							if (tipoGrafica == "Bosque") {
								out.println("<h3>"
										+ "Selecciona la CAR: "
										+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ periodosS + "</select>" + "</h3>");
							}
							if (tipoGrafica == "Cobertura") {
								out.println("<h3>"
										+ "Selecciona la CAR: "
										+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ periodosS + "</select>" + "</h3>");
							}
						} else if (tipoDivision == "Departamentos") {
							divisionesS = "";
							periodosS = "";

							for (int x = 0; x < nombresDepartamentos.length; x++) {
								divisionesS = divisionesS + "<option id=\"bosGOP"
										+ (x + 1) + "\" value=\"" + (x + 1) + "\">"
										+ nombresDepartamentos[x] + "</option>";
							}
							for (int x = 0; x < PeriodosBosque.length; x++) {
								periodosS = periodosS + "<option id=\"opGOP" + x
										+ "\" value=\"" + x + "\">"
										+ PeriodosBosque[x] + "</option>";
							}

							if (tipoGrafica == "Bosque") {
								out.println("<h3>"
										+ "Selecciona el Departamento: "
										+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ periodosS + "</select>" + "</h3>");
							}
							if (tipoGrafica == "Cobertura") {
								out.println("<h3>"
										+ "Selecciona el Departamento: "
										+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ periodosS + "</select>" + "</h3>");
							}
						} else if (tipoDivision == "AreasHidrograficas") {
							divisionesS = "";
							periodosS = "";

							for (int x = 0; x < nombresAreasHidro.length; x++) {
								divisionesS = divisionesS + "<option id=\"bosGOP"
										+ (x + 1) + "\" value=\"" + (x + 1) + "\">"
										+ nombresAreasHidro[x] + "</option>";
							}
							for (int x = 0; x < PeriodosBosque.length; x++) {
								periodosS = periodosS + "<option id=\"opGOP" + x
										+ "\" value=\"" + x + "\">"
										+ PeriodosBosque[x] + "</option>";
							}

							if (tipoGrafica == "Bosque") {
								out.println("<h3>"
										+ "Selecciona el Area Hidrografica: "
										+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ periodosS + "</select>" + "</h3>");
							}
							if (tipoGrafica == "Cobertura") {
								out.println("<h3>"
										+ "Selecciona el Area Hidrografica: "
										+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ periodosS + "</select>" + "</h3>");
							}
						}
					}

					out.println("</div>"
							+ "<div style=\"display:none; float: left; width: 436px; height: 418px;\""
							+ "id=\"divGBarras\"></div>"
							+ "<div style=\"display:none; float: left; width: 418px; height: 418px;\""
							+ "id=\"divGTortas\"></div>"
							+ "<div style=\"display:none; float: left; overflow: auto; width: 400px; height: 380px;\""
							+ "id=\"divGInformacion\" >");
					if (tipoGrafica == "Biomasa") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"infoTB0\"></p>"
								+ "<p id=\"infoTB1\"></p>"
								+ "<p id=\"infoTB2\"></p>"
								+ "<p id=\"infoTB3\"></p>"
								+ "<p id=\"infoTB4\"></p>"
								+ "<p id=\"infoTB5\"></p>"
								+ "<p id=\"infoTB6\"></p>"
								+ "<p id=\"infoTB7\"></p>"
								+ "<p id=\"infoTB8\"></p>"
								+ "<p id=\"infoTB9\"></p>"
								+ "<p id=\"infoTB10\"></p>"
								+ "<p id=\"infoTB11\"></p>"
								+ "<p id=\"infoTB12\"></p>"
								+ "<p id=\"infoTB13\"></p>"
								+ "<p id=\"infoTB14\"></p>");
					} else if (tipoGrafica == "Bosque") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"infoBosque\"></p>"
								+ "<p id=\"infoNoBosque\"></p>"
								+ "<p id=\"infoSinInfo\"></p>");
					} else if (tipoGrafica == "Cobertura") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"info1\"></p>" + "<p id=\"info2\"></p>"
								+ "<p id=\"info3\"></p>" + "<p id=\"info4\"></p>"
								+ "<p id=\"info5\"></p>");
					}else
					if (tipoGrafica == "Deforestacion") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"infoTB0\"></p>"
								+ "<p id=\"infoTB1\"></p>"
								+ "<p id=\"infoTB2\"></p>"
								+ "<p id=\"infoTB3\"></p>"
								+ "<p id=\"infoTB4\"></p>"
								+ "<p id=\"infoTB5\"></p>"
								+ "<p id=\"infoTB6\"></p>"
								+ "<p id=\"infoTB7\"></p>"
								+ "<p id=\"infoTB8\"></p>"
								+ "<p id=\"infoTB9\"></p>"
								+ "<p id=\"infoTB10\"></p>"
								+ "<p id=\"infoTB11\"></p>");
					}
					out.println("</div>"
							+ "<div style=\"display:none; float: left; width: 627px; height: 418px;\" id=\"divGMapas\"></div>"
							+ "<div style=\"display: none;float: left; width: 627px; height: 418px;\""
							+ "id=\"divGTendencias\"></div>" + "</div>");
				} else {
					String periodS = "";
					out.println("<div id=\"divGraficas\""
							+ "style=\"display: none; overflow: auto; margin: 0 auto; float: left;\">"
							+ "<div id=\"divSelect\">");

					if (tipoGrafica == "Biomasa" || tipoGrafica=="Deforestacion") {
						if (tipoGrafica == "Biomasa"){
						out.println("<h3>"
								+ "Selecciona Periodo: "
								+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
								+ "onchange=\"cambiarGBiomasa()\">"
								+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
								+ "<option id=\"bioGOP1\" value=\"1\">"
								+ PeriodoBiomasa + "</option>" + "</select>"
								+ "</h3>");
						}
						if (tipoGrafica == "Deforestacion"){
							out.println("<h3>"
									+ "Selecciona Periodo: "
									+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
									+ "onchange=\"cambiarGDeforestacion()\">"
									+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
									+ "<option id=\"bioGOP1\" value=\"1\">"
									+ PeriodoBiomasa + "</option>" + "</select>"
									+ "</h3>");
							}
					}
					if (tipoGrafica == "Bosque" || tipoGrafica == "Cobertura") {

						for (int x = 0; x < PeriodosBosque.length; x++) {
							periodS = periodS + "<option id=\"opGOP" + (x + 1)
									+ "\" value=\"" + (x + 1) + "\">"
									+ PeriodosBosque[x] + "</option>";
						}

						if (tipoGrafica == "Bosque") {
							out.println("<h3>"
									+ "Selecciona Periodo: "
									+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
									+ "onchange=\"cambiarGBosque()\">"
									+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
									+ periodS + "</select>" + "</h3>");
						}
						if (tipoGrafica == "Cobertura") {
							out.println("<h3>"
									+ "Selecciona Periodo: "
									+ "<select name=\"opcionGrafica\" id=\"opcionGrafica\""
									+ "onchange=\"cambiarGCobertura()\">"
									+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
									+ periodS + "</select>" + "</h3>");
						}

					}

					out.println("</div>"
							+ "<div style=\"float: left; width: 436px; height: 418px;\""
							+ "id=\"divGBarras\"></div>"
							+ "<div style=\"float: left; width: 418px; height: 418px;\""
							+ "id=\"divGTortas\"></div>"
							+ "<div style=\"display:none; float: left; overflow: auto; width: 400px; height: 380px;\""
							+ "id=\"divGInformacion\" >");
					if (tipoGrafica == "Biomasa") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"infoTB0\"></p>"
								+ "<p id=\"infoTB1\"></p>"
								+ "<p id=\"infoTB2\"></p>"
								+ "<p id=\"infoTB3\"></p>"
								+ "<p id=\"infoTB4\"></p>"
								+ "<p id=\"infoTB5\"></p>"
								+ "<p id=\"infoTB6\"></p>"
								+ "<p id=\"infoTB7\"></p>"
								+ "<p id=\"infoTB8\"></p>"
								+ "<p id=\"infoTB9\"></p>"
								+ "<p id=\"infoTB10\"></p>"
								+ "<p id=\"infoTB11\"></p>"
								+ "<p id=\"infoTB12\"></p>"
								+ "<p id=\"infoTB13\"></p>"
								+ "<p id=\"infoTB14\"></p>");
					} else if (tipoGrafica == "Bosque") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"infoBosque\"></p>"
								+ "<p id=\"infoNoBosque\"></p>"
								+ "<p id=\"infoSinInfo\"></p>");
					} else if (tipoGrafica == "Cobertura") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"info1\"></p>" + "<p id=\"info2\"></p>"
								+ "<p id=\"info3\"></p>" + "<p id=\"info4\"></p>"
								+ "<p id=\"info5\"></p>");
					}else
					if (tipoGrafica == "Deforestacion") {
						out.println("<h3>Informacion</h3>"
								+ "<p id=\"infoArea\"></p>"
								+ "<p id=\"infoPeriodo\"></p>"
								+ "<p id=\"infoTB0\"></p>"
								+ "<p id=\"infoTB1\"></p>"
								+ "<p id=\"infoTB2\"></p>"
								+ "<p id=\"infoTB3\"></p>"
								+ "<p id=\"infoTB4\"></p>"
								+ "<p id=\"infoTB5\"></p>"
								+ "<p id=\"infoTB6\"></p>"
								+ "<p id=\"infoTB7\"></p>"
								+ "<p id=\"infoTB8\"></p>"
								+ "<p id=\"infoTB9\"></p>"
								+ "<p id=\"infoTB10\"></p>"
								+ "<p id=\"infoTB11\"></p>");
					}
					out.println("</div>"
							+ "<div style=\"display:none; float: left; width: 627px; height: 418px;\" id=\"divGMapas\"></div>"
							+ "<div style=\"display: none;float: left; width: 627px; height: 418px;\""
							+ "id=\"divGTendencias\"></div>" + "</div>");
				}
			%>
			<div id="divMapas" style="display: none;">
				<h1>MAPAS</h1>
				<h1>En construccion..</h1>
			</div>
		</div>
	</div>

</body>
</html>