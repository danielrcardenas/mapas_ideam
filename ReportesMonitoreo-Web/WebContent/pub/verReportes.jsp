<%@ page language="java" import="java.util.*" import="java.math.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="co.gov.ideamredd.ui.dao.CargaDatosInicialHome"%>
<%@page import="co.gov.ideamredd.ui.entities.Noticias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.util.UbicacionActual"%>
<%@page import="co.gov.ideamredd.reportes.LecturaArchivo"%> 
<%@page import="co.gov.ideamredd.lenguaje.LenguajeI18N"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>
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
<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700'
	rel='stylesheet' type='text/css'>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="../jquerymobile/jquery.mobile-1.4.2.min.js"></script>
<script src="../jquerymobile/popup.js"></script>
<script src="../js/slippry.min.js"></script>
<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/content.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/html.css"
	media="all" />
<script type="text/javascript" src="../js/GBiomasa.js"></script>
<script type="text/javascript" src="../js/GBosque.js"></script>
<script type="text/javascript" src="../js/GCobertura.js"></script>
<script type="text/javascript" src="../js/GDeforestacion.js"></script>
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>


<script>

google.load("visualization", "1", {
	packages : [ "corechart" ]
});

  var mouseX=0;
  var mouseY=0;

    function coordenadas(event) {
	  x=event.clientX;
	  y=event.clientY;
	   
	  document.getElementById("x").value = x;
	  document.getElementById("y").value = y;
	   
	}
	 
    $(function(){
    	var tabs = $( "#tabs" ).tabs();
        tabs.find( ".ui-tabs-nav" ).sortable({
          axis: "x",
          stop: function() {
            tabs.tabs( "refresh" );
          }
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

	function postEdit(){
		document.getElementById("logName").value="";
		document.getElementById("logPassword").value="";
	}

</script>
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
	
	function descargarReporte(tipoArchivo)
	{
		document.getElementById("tipoArchivo").value = tipoArchivo;
		document.getElementById("formDescargaDoc").submit();
	}
	
</script>
</head>
<body class='sidebarlast front' onload="postEdit()" onMouseMove="takeCoordenadas(event);" onmouseover="popUpAyudaAux()">
	<form id="home" action="<%=basePath%>idiomaServlet" method="post">
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
										<li id="icoAyuda" class="menu-item help first" onclick="popUpAyudaOpen()"
											style="margin-right: 10px; cursor: pointer;"><a></a></li>
										<!-- Aca estaba lo de twitter -->
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

					<div id="block-resultados-reporte" class="block-gray  block">
                        <div class="content">
                          <h2>RESULTADOS DE REPORTE</h2>
                          <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" id="tabs">
                            <ul role="tablist" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                              <li aria-selected="true" aria-labelledby="ui-id-1" aria-controls="tabs-1" tabindex="0" role="tab" class="tab-tabla ui-state-default ui-corner-top ui-tabs-active ui-state-active"><a id="ui-id-1" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-1">Ver tabla</a></li>
                              <li aria-selected="false" aria-labelledby="ui-id-2" aria-controls="tabs-2" tabindex="-1" role="tab" class="tab-graficas ui-state-default ui-corner-top"><a id="ui-id-2" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-2">Ver gráficas</a></li>
                              <li aria-selected="false" aria-labelledby="ui-id-3" aria-controls="tabs-3" tabindex="-1" role="tab" class="tab-mapa ui-state-default ui-corner-top"><a id="ui-id-3" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#tabs-3">Ver mapas</a></li>
                            </ul>
                            <div aria-hidden="false" aria-expanded="true" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-1" id="tabs-1">

                              <div class="info-tabla" style="height:600px; width:630px; max-height:600px; max-width: 630px; overflow: auto;">
                              <table class="tblSalida" border="1">
                              <%=reporte[1]%>
                              </table>

                              </div>
                              
                              <div class="block-dark-gray block-aclaracion">
                                <h3>Aclaración</h3>
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
                              <div class="form-actions">
                                  <input class="btn btn-default btn-back" onclick="javascript:history.back(1);" value="Regresar" type="button">
                                  <input class="btn btn-default" value="Descargar XLSX" onclick="descargarReporte('xlsx')" type="button">
                                  <input class="btn btn-default" value="Descargar PDF" onclick="descargarReporte('pdf')" type="button">
                                  
                              </div>
                            </div>

                            <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-2" id="tabs-2">

                              <div class="info-graficas">
                              
                              </div>
                              <div class="graficas block-dark-gray" style="width: 575px">
                                <%
				if (tipoDivision != "ConsolidadoNacional") {
					out.println("<div id=\"divGraficas\">"
							+ "<div id=\"divSelect\">");

					if (tipoGrafica == "Biomasa" || tipoGrafica == "Deforestacion") {
						if (tipoGrafica == "Biomasa")
						{
						out.println("<h3>"
								+ "Selecciona Area Hidrografica: "
								+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
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
								+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
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
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ periodosS + "</select>" + "</h3>");
							}
							if (tipoGrafica == "Cobertura") {
								out.println("<h3>"
										+ "Selecciona la CAR: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
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
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ periodosS + "</select>" + "</h3>");
							}
							if (tipoGrafica == "Cobertura") {
								out.println("<h3>"
										+ "Selecciona el Departamento: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
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
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGBosque()\">"
										+ periodosS + "</select>" + "</h3>");
							}
							if (tipoGrafica == "Cobertura") {
								out.println("<h3>"
										+ "Selecciona el Area Hidrografica: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
										+ divisionesS + "</select>" + "</h3>");
								out.println("<h3>"
										+ "Selecciona el Periodo: "
										+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionPeriodoGrafica\" id=\"opcionPeriodoGrafica\""
										+ "onchange=\"cambiarGCobertura()\">"
										+ periodosS + "</select>" + "</h3>");
							}
						}
					}

					out.println("</div>"
							+ "<div style=\"display:none;\""
							+ "id=\"divGBarras\"></div>"
							+ "<div style=\"display:none;\""
							+ "id=\"divGTortas\"></div>"
							+ "<div style=\"display: none;\""
							+ "id=\"divGTendencias\"></div>"
							+ "<div style=\"display:none; overflow: auto; width: 550px; height: 350px;\""
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
							+ "<div style=\"display:none;\" id=\"divGMapas\"></div>"
							 + "</div>");
				} else {
					String periodS = "";
					out.println("<div id=\"divGraficas\">"
							+ "<div id=\"divSelect\">");

					if (tipoGrafica == "Biomasa" || tipoGrafica=="Deforestacion") {
						if (tipoGrafica == "Biomasa"){
						out.println("<h3>"
								+ "Selecciona Periodo: "
								+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
								+ "onchange=\"cambiarGBiomasa()\">"
								+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
								+ "<option id=\"bioGOP1\" value=\"1\">"
								+ PeriodoBiomasa + "</option>" + "</select>"
								+ "</h3>");
						}
						if (tipoGrafica == "Deforestacion"){
							out.println("<h3>"
									+ "Selecciona Periodo: "
									+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
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
									+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
									+ "onchange=\"cambiarGBosque()\">"
									+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
									+ periodS + "</select>" + "</h3>");
						}
						if (tipoGrafica == "Cobertura") {
							out.println("<h3>"
									+ "Selecciona Periodo: "
									+ "<select class=\"select-wrapper\" style=\"width: 300px\" name=\"opcionGrafica\" id=\"opcionGrafica\""
									+ "onchange=\"cambiarGCobertura()\">"
									+ "<option id=\"bioGOP0\" value=\"0\">Seleccione</option>"
									+ periodS + "</select>" + "</h3>");
						}

					}

					out.println("</div>"
							+ "<div id=\"divGBarras\"></div>"
							+ "<div id=\"divGTortas\"></div>"
							+ "<div style=\"display: none;\" id=\"divGTendencias\"></div>"
							+ "<div style=\"display:none; overflow: auto; width: 550px; height: 350px;\""
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
							+ "<div style=\"display:none;\" id=\"divGMapas\"></div>"
							 + "</div>");
				}
			%>
                              </div>
                              <div class="block-dark-gray block-aclaracion">
                                <h3>Aclaración</h3>
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
                              <div class="form-actions">
                                  <input class="btn btn-default btn-back" onclick="javascript:history.back(1);" value="Regresar" type="button">
                                  <input class="btn btn-default" value="Descargar XLSX" onclick="descargarReporte('xlsx')" type="button">
                                  <input class="btn btn-default" value="Descargar PDF" onclick="descargarReporte('pdf')" type="button">
                              </div>  

                            </div>
                            <div aria-hidden="true" aria-expanded="false" style="display: none;" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-3" id="tabs-3">
                                 
                            
                            <div class="block-dark-gray block-aclaracion">
                                <h3>Aclaración</h3>
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
                              <div class="form-actions">
                                  <input class="btn btn-default btn-back" onclick="javascript:history.back(1);" value="Regresar" type="button">
                                  <input class="btn btn-default" value="Descargar XLSX" onclick="descargarReporte('xlsx')" type="button">
                                  <input class="btn btn-default" value="Descargar PDF" onclick="descargarReporte('pdf')" type="button">
                              </div>
                            </div>
                          </div>
 
                        </div>
                      </div>

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
	<form method="post"
		action="<%=basePath2%>MonitoreoBiomasaCarbono/j_security_check"
		name="j_security_check" id="j_security_check">
		<input type="hidden" name="j_username" id="j_username" /> <br> <input
			type="hidden" name="j_password" id="j_password" />
	</form>
	<form method="post" action="<%=basePath%>registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /> <input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>
	<div id="popUpAyuda"
		style="z-index: 3; position: absolute; width: 135px; height:80px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;">
		<div style="background: none repeat scroll 0 0 #D66A10;"><label for="exampleInputEmail1" style="text-align: center; color: white; font-weight: bold;">
		<%=msj.getString("home.popAyuda.titulo")%></label>
		</div>
       <label for="exampleInputEmail1"><a href="<%=basePath2%>Usuario-Web/pub/registroUsuario.jsp">
       *<%=msj.getString("home.popAyuda.registrarse")%></a></label>
       <label for="exampleInputEmail1" style="text-align: left;"><a href="<%=basePath2%>Usuario-Web/pub/recordarClave.jsp?idiom=<%=i18n.getLenguaje()%>&pais=<%=i18n.getPais()%>">
       *<%=msj.getString("home.popAyuda.recordar")%></a></label>
       <label for="exampleInputEmail1" onclick="popUpAyudaClose()" 
       style="text-align: left; cursor: pointer; color: #C36003;"><%=msj.getString("home.popAyuda.cerrar")%></label>
     </div>
	<form action="<%=basePath%>recordarClaveUsuarioServlet" method="post"
		onsubmit="return validar();" name="formRecordarClave"
		id="formRecordarClave">
		<input type="hidden" name="email" id="email" size="30"
			style="width: 250px">
	</form>
	
	
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
				</form>
	
	
</body>
</html>

