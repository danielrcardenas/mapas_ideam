<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.entities.Reportes"%>

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
<meta charset="utf-8" />
<title>Inventarios Forestales</title>
<%
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/";

    String basePath2 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort() + "/";

	
%>
 <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
   <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="../js/slippry.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/estilos.css"
	media="all" />
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>

<link rel='stylesheet' type='text/css' media='all' href='../js/jscalendar/calendar-green.css' title='green' /> 
<script type='text/javascript' src='../js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='../js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='../js/jscalendar/calendar-setup.js'></script>

<script type="text/javascript">

	function mostrarLoading() {
		document.getElementById("capaLoading").style.display="block";
	}
	
	function ocultarLoading() {
		document.getElementById("capaLoading").style.display="none";
	}
	function verificarCampos() {
		mostrarLoading();
		var passed = true;
		console.log('validando campos');
		var mensaje = "Los siguientes campos son obligatorios/The following fields are mandatory:\n";		
																
		if (document.getElementById("especieAux").value == "") {
			mensaje = mensaje + "- Especie/Species";
			passed = false;
		}
		if (document.getElementById("parcelaAux").value == "") {
			mensaje = mensaje + "- Parcela/Plot";
			passed = false;
		}
		if (document.getElementById("fechaIniAux").value == "") {
			mensaje = mensaje +"- Fecha inicio/Start Date";
			passed = false;
		}
		if (document.getElementById("fechaFinAux").value == "") {
			mensaje = mensaje +"- Fecha final/End Date";
			passed = false;
		}		
		
		var fechaIniAux = document.getElementById("fechaIniAux").value; 
		var ini_anio = fechaIniAux.substring(6, 10); 
		var ini_mes = fechaIniAux.substring(3, 5); 
		var ini_dia = fechaIniAux.substring(0, 2); 
		var desde_numerico = (ini_anio + '' + ini_mes + '' + ini_dia) * 1.0;

		var fechaFinAux = document.getElementById("fechaFinAux").value; 
		var fin_anio = fechaFinAux.substring(6, 10); 
		var fin_mes = fechaFinAux.substring(3, 5); 
		var fin_dia = fechaFinAux.substring(0, 2); 
		var hasta_numerico = (fin_anio + '' + fin_mes + '' + fin_dia) * 1.0;
		
		if (desde_numerico > hasta_numerico) {
			mensaje += '- Rango de fechas no válido/Invalid date range';
			passed = false;
		}
			
		if (!passed) {
				alert(mensaje);
				ocultarLoading();
		}
	
		return passed;
	}

</script>
</head>
<body>
	
		<div id="page"
			style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			
			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja">Reporte de Inventario de Parcelas/Plot Inventory Report</h2>

							<script>
								$(function() {
									
								    function split( val ) {
								        return val.split( /,\s*/ );
								    }
								    
								    function extractLast( term ) {
								        return split( term ).pop();
								    }
								    $("#parcelaAux")
									.bind( "keydown", function( event ) {
							        if ( event.keyCode === $.ui.keyCode.TAB &&
							                $( this ).autocomplete( "instance" ).menu.active ) {
							              event.preventDefault();
							            }
							        })
									.autocomplete({
								    source: function(request, response) {
									    $.ajax({
										    url: "parcelas.jsp?parcelaAux="+extractLast($("#parcelaAux").val()),
										    type: "POST",
										    dataType: "json",
										    data: { name: request.term},
										    success: function( data ) {
										        response( $.map( data, function( item ) {
										        return {
										            label: item.name,
										            value: item.value,
										        }
										        }));
										    },
										    error: function (error) {
										       
										    }
									    });
								    },
								    focus: function(){
								    	return false;
								    },
								    select: function(event, ui){
								    	var terms = split(this.value);
								    	terms.pop();
								    	terms.push(ui.item.value);
								    	terms.push("");
								    	this.value = terms.join(",");
								    	return false;
								    },
								    minLength: 2
							    });
								    
									$("#especieAux")
										.bind( "keydown", function( event ) {
								        if ( event.keyCode === $.ui.keyCode.TAB &&
								                $( this ).autocomplete( "instance" ).menu.active ) {
								              event.preventDefault();
								            }
								        })
										.autocomplete({
									    source: function(request, response) {
										    $.ajax({
											    url: "especies.jsp?especieAux="+extractLast($("#especieAux").val()) + "&parcelaAux="+extractLast($("#parcelaAux").val()),
											    type: "POST",
											    dataType: "json",
											    data: { name: request.term},
											    success: function( data ) {
											        response( $.map( data, function( item ) {
											        return {
											            label: item.name,
											            value: item.value,
											        }
											        }));
											    },
											    error: function (error) {
											       alert('error: ' + error);
											    }
										    });
									    },
									    focus: function(){
									    	return false;
									    },
									    select: function(event, ui){
									    	var terms = split(this.value);
									    	terms.pop();
									    	terms.push(ui.item.value);
									    	terms.push("");
									    	this.value = terms.join(",");
									    	return false;
									    },
									    minLength: 3
								    });
								});							
							</script>
							<div id="content">
								<div class="content-inner">

									<div id="block-consulta-reportes" class="block-gray block">

										<h2>Búsqueda/Search</h2>
											<div class="form-group"
												style="padding-left: 20px; padding-right: 20px">
												<label for="exampleInputEmail1">Parcelas/Plots: </label>
												<div class="select-wrapper" style="max-width: 400px;">
													<input name="parcelaAux" id="parcelaAux" type="text" placeholder="Ingrese minimo 2 caracteres/At least 2 characters" />														
												</div>
											</div>
											<div class="form-group"
												style="padding-left: 20px; padding-right: 20px">
												<label for="exampleInputEmail1">Especie/Species: </label>
												<div class="select-wrapper" style="max-width: 400px;">
													<input name="especieAux" id="especieAux" type="text" placeholder="Ingrese minimo 3 caracteres/At least 3 characters" />														
												</div>
											</div>
											
				                            
											<div class="form-group item-periodos"
												style="padding-left: 20px; padding-right: 20px">

												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px"> 
													<label id="l3" for="fechaIniAux" >Fecha inicial/Start Date:</label> 
													<input type="text" class="select-wrapper" name="fechaIniAux" id="fechaIniAux" style="width:260px"/>
													<script type='text/javascript'>
													Calendar.setup({
														inputField     :    'fechaIniAux',				// id of the input field
														ifFormat       :    '%d/%m/%Y',	// format of the input field
														showsTime      :    false,					// will display a time selector
														button         :    'fechaIniAux',				// trigger for the calendar (button ID)
														singleClick    :    false,					// double-click mode
														step           :    1						// show all years in drop-down boxes (instead of every other year as default)
													});
													</script>
												</span>
	
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label id="l3b" for="fechaFinAux" >Fecha final/End Date:</label> 
													<input type="text" class="select-wrapper" name="fechaFinAux" id="fechaFinAux" style="width:260px"/>
													<script type='text/javascript'>
													Calendar.setup({
														inputField     :    'fechaFinAux',				// id of the input field
														ifFormat       :    '%d/%m/%Y',	// format of the input field
														showsTime      :    false,					// will display a time selector
														button         :    'fechaFinAux',				// trigger for the calendar (button ID)
														singleClick    :    false,					// double-click mode
														step           :    1						// show all years in drop-down boxes (instead of every other year as default)
													});
													</script>
												</span>
											</div>
											<div class="form-actions">
												<input class="btn btn-default btn-ir" type="button" id="enviarConsulta"
													value="Consultar/Query"></input>
											</div>
											<script>
											$(function() {
												$( document ).tooltip();
												
												var todaysDate = new Date();
												
// 								                $("#fechaIniAux").datepicker({
// 								                    dateFormat: 'dd/mm/yy',
// 								                    maxDate: todaysDate
// 								                }, $.datepicker.regional["es"]).on("input change", function (e) {
// 													console.log("Date changed: ", e.target.value);
// 													var minDate = $('#fechaIniAux').datepicker('getDate');
// 													$("#fechaFinAux").datepicker("option", "minDate", minDate);
// 												});
												
// 								                $("#fechaFinAux").datepicker({
// 								                    dateFormat: 'dd/mm/yy',
// 								                    maxDate: todaysDate
// 								                },$.datepicker.regional["es"]);
											    
											    $("#enviarConsulta").click(function() {
											    	if(verificarCampos() == false) {
											    		return;
											    	}
											    	
													document.getElementById("especie").value= "";
													document.getElementById("parcela").value= "";
													document.getElementById("fechaIni").value= "";
													document.getElementById("fechaFin").value="";
													
													document.getElementById("especie").value = document.getElementById("especieAux").value;
													document.getElementById("parcela").value = document.getElementById("parcelaAux").value;
													document.getElementById("fechaIni").value = document.getElementById("fechaIniAux").value;
													document.getElementById("fechaFin").value = document.getElementById("fechaFinAux").value;
													
													var data = {
														especie: document.getElementById("especieAux").value,
														parcela: document.getElementById("parcelaAux").value,
														fechaIni: document.getElementById("fechaIniAux").value,
														fechaFin: document.getElementById("fechaFinAux").value
													}
													
													if(false)//revisar validacion de fechas
												    {
														setTimeout(function(){document.location.href = '<%=basePath2%>'+"ReportesInventario-Web/pub/consultarReporteInventarios.jsp"},500);
														return;
													}
													
													var url = "<%=basePath%>consultarReporteServlet";
													
													$.ajax({
														type: "POST",
														url: url,
														data: data,
														success: function(data){
															ocultarLoading();
															$("#div_resultado").html(data);
															if(!String(data).substring(0,20).includes("No hay datos")){
																$("#btn_descargar_pdf").show();
																$("#btn_descargar_xls").show();
															}
														}
													});			
													
												});
											});
											</script>
									</div>
									<div id="capaLoading" class='centrar' style="display: none;">
										<img src="<%=basePath%>img/loading1.gif">
									</div>
									<div id="div_resultado" style="max-height: 750px; overflow: auto;">
									</div>
									<a id="btn_descargar_pdf" href="<%=basePath%>descargaPdfServlet" style="display: none;" >PDF</a>
									<a id="btn_descargar_xls" href="<%=basePath%>descargaXlsServlet" style="display: none;" >Excel</a>
								</div>
							</div>

						</div>
					</div>
				</div>

			</div>


			

			

		</div>
	<form action="<%=basePath%>consultarReporteServlet" method="post" name="formConsultaReporte" id="formConsultaReporte" onsubmit= "verificarCampos();">
			<input type="hidden" name="treporte" id="treporte" value="">
			<input type="hidden" name="especie" id="especie" value="">
			<input type="hidden" name="parcela" id="parcela" value="">
			<input type="hidden" name="fechaIni" id="fechaIni" value="">
			<input type="hidden" name="fechaFin" id="fechaFin" value="">
	</form>
</body>
<style>
	.ui-autocomplete{
		background-color: #CCC;
	}
</style>
</html>

