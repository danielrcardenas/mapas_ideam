<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="co.gov.ideamredd.dao.TipoBosqueDAO"%>
<%@page import="co.gov.ideamredd.dao.DepartamentoDAO"%>
<%@page import="co.gov.ideamredd.rserver.conexion.RserverConexion"%>

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

<!-- <link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700' rel='stylesheet' type='text/css'> -->
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<!--  <script src="../js/slippry.min.js"></script>-->
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css" rel="stylesheet" />

<link rel="stylesheet" href="css/slippry.css" />
<link type="text/css" rel="stylesheet" href="../css/layout.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="../css/menu.css"
	media="all" />
<link type="text/css" rel="stylesheet" href="/MonitoreoBC-WEB/css/estilos.css"
	media="all" />
<script type="text/javascript" src="../custom/datum-validation.js"></script>
<script type="text/javascript" src="../custom/manejo-listas.js"></script>
<!-- <script src="../js/datepicker_es.js"></script> -->

<link rel='stylesheet' type='text/css' media='all' href='../js/jscalendar/calendar-green.css' title='green' /> 
<script type='text/javascript' src='../js/jscalendar/calendar.js'></script>
<script type='text/javascript' src='../js/jscalendar/lang/calendar-en.js'></script>
<script type='text/javascript' src='../js/jscalendar/calendar-setup.js'></script>

<%
TipoBosqueDAO tb = new TipoBosqueDAO();
String tipoBosque = tb.getTipoBosque();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";

	    String basePath2 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + "/";
DepartamentoDAO dt = new DepartamentoDAO();
String departamentos = dt.getDepartamentos();

%>
<script type="text/javascript">

	function mostrarLoading() {
		document.getElementById("capaLoading").style.display="block";
	}
	
	function ocultarLoading() {
		document.getElementById("capaLoading").style.display="none";
	}
function verificarCampos() {
	var passed = true;
	console.log('llegue');
	mostrarLoading();

	var mensaje = "Los siguientes campos son obligatorios/The following fields are mandatory:\n";

	if (document.getElementById("deptosAux1").value == "") {
		mensaje = mensaje +"- Departamento 1/Department 1 \n";
		passed = false;
	}
	if (document.getElementById("tBosqueAux1").value == "") {
		mensaje = mensaje +"- Tipo de Bosque 1/Forest type 1 \n";
		passed = false;
	}
	if (document.getElementById("tBosqueAux2").value == "") {
		mensaje = mensaje +"- Tipo de Bosque 2/Forest type 2 \n";
		passed = false;
	}
	if (document.getElementById("fechaIniAux1").value == "") {
		mensaje = mensaje +"- Fecha inicial 1/Start date 1 \n";
		passed = false;
	}
	if (document.getElementById("fechaFinAux1").value == "") {
		mensaje = mensaje +"- Fecha final 1/End date 1 \n";
		passed = false;
	}
	
	
	if (document.getElementById("deptosAux2").value == "") {
		mensaje = mensaje +"- Departamento 2/Department 2 \n";
		passed = false;
	}
	
	if (document.getElementById("fechaIniAux2").value == "") {
		mensaje = mensaje +"- Fecha inicial 2/Start date 2 \n";
		passed = false;
	}
	if (document.getElementById("fechaFinAux2").value == "") {
		mensaje = mensaje +"- Fecha final 2/End date 2 \n";
		passed = false;
	}		

	var fechaIniAux1 = document.getElementById("fechaIniAux1").value; 
	var ini_anio1 = fechaIniAux1.substring(6, 10); 
	var ini_mes1 = fechaIniAux1.substring(3, 5); 
	var ini_dia1 = fechaIniAux1.substring(0, 2); 
	var desde_numerico1 = (ini_anio1 + '' + ini_mes1 + '' + ini_dia1) * 1.0;

	var fechaFinAux1 = document.getElementById("fechaFinAux1").value; 
	var fin_anio1 = fechaFinAux1.substring(6, 10); 
	var fin_mes1 = fechaFinAux1.substring(3, 5); 
	var fin_dia1 = fechaFinAux1.substring(0, 2); 
	var hasta_numerico1 = (fin_anio1 + '' + fin_mes1 + '' + fin_dia1) * 1.0;
	
	if (desde_numerico1 > hasta_numerico1) {
		mensaje += '- Rango de fechas iniciales no válido/Invalid date range';
		passed = false;
	}
		
	var fechaIniAux2 = document.getElementById("fechaIniAux2").value; 
	var ini_anio2 = fechaIniAux2.substring(6, 20); 
	var ini_mes2 = fechaIniAux2.substring(3, 5); 
	var ini_dia2 = fechaIniAux2.substring(0, 2); 
	var desde_numerico2 = (ini_anio2 + '' + ini_mes2 + '' + ini_dia2) * 2.0;

	var fechaFinAux2 = document.getElementById("fechaFinAux2").value; 
	var fin_anio2 = fechaFinAux2.substring(6, 20); 
	var fin_mes2 = fechaFinAux2.substring(3, 5); 
	var fin_dia2 = fechaFinAux2.substring(0, 2); 
	var hasta_numerico2 = (fin_anio2 + '' + fin_mes2 + '' + fin_dia2) * 2.0;
	
	if (desde_numerico2 > hasta_numerico2) {
		mensaje += '- Rango de fechas finales no válido/Invalid date range';
		passed = false;
	}
		
	
	if (!passed) {
		ocultarLoading();
		alert(mensaje);
	}
	return passed;
}
</script>
</head>
<body >
	
		<div id="page"
			style="z-index: 1; position: absolute; margin-left: auto; margin-right: auto; left: 0; right: 0;">
			

			<div id="main" class="wrapper">
				<div id="main-inner" class="section-wrapper">
					<div class="section">
						<div class="section-inner clearfix fondoformulario">
						
							<h2 class="titulo_naranja">Reporte de Similitud de Especies/Species Similarity Report</h2>

							
							<script>
								$(function() {
									
								    function split( val ) {
								        return val.split( /,\s*/ );
								    }
								    
								    function extractLast( term ) {
								        return split( term ).pop();
								    }
									
									$("#especieAux1")
										.bind( "keydown", function( event ) {
								        if ( event.keyCode === $.ui.keyCode.TAB &&
								                $( this ).autocomplete( "instance" ).menu.active ) {
								              event.preventDefault();
								            }
								        })
										.autocomplete({
									    source: function(request, response) {
										    $.ajax({
											    url: "especies.jsp?especieAux="+extractLast($("#especieAux1").val()) + "\&parcelaAux="+$("#parcelaAux1").val() + "\&deptoAux="+$("#deptosAux1").val()+ "\&tipoBosqueAux="+$("#tBosqueAux1").val(),
											    type: "POST",
											    dataType: "json",
											    data: { name: request.term},
											    success: function( data ) {
											        response( $.map( data, function( item ) {
											        return {
											            label: item.name,
											            value: item.value,
											        };
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
									$("#especieAux2")
									.bind( "keydown", function( event ) {
							        if ( event.keyCode === $.ui.keyCode.TAB &&
							                $( this ).autocomplete( "instance" ).menu.active ) {
							              event.preventDefault();
							            }
							        })
									.autocomplete({
								    source: function(request, response) {
									    $.ajax({
										    url: "especies.jsp?especieAux="+extractLast($("#especieAux2").val()) + "\&parcelaAux="+$("#parcelaAux2").val() + "\&deptoAux="+$("#deptosAux2").val()+ "\&tipoBosqueAux="+$("#tBosqueAux2").val(),
										    type: "POST",
										    dataType: "json",
										    data: { name: request.term},
										    success: function( data ) {
										        response( $.map( data, function( item ) {
										        return {
										            label: item.name,
										            value: item.value,
										        };
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
								    $("#parcelaAux2")
									.bind( "keydown", function( event ) {
							        if ( event.keyCode === $.ui.keyCode.TAB &&
							                $( this ).autocomplete( "instance" ).menu.active ) {
							              event.preventDefault();
							            }
							        })
									.autocomplete({
								    source: function(request, response) {
									    $.ajax({
										    url: "parcelas.jsp?parcelaAux="+extractLast($("#parcelaAux2").val())+ "\&deptoAux="+$("#deptosAux2").val()+ "\&tipoBosqueAux="+$("#tBosqueAux2").val(),
										    type: "POST",
										    dataType: "json",
										    data: { name: request.term},
										    success: function( data ) {
										        response( $.map( data, function( item ) {
										        return {
										            label: item.name,
										            value: item.value,
										        };
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
								    minLength: 1
							    });

								    $("#parcelaAux1")
									.bind( "keydown", function( event ) {
							        if ( event.keyCode === $.ui.keyCode.TAB &&
							                $( this ).autocomplete( "instance" ).menu.active ) {
							              event.preventDefault();
							            }
							        })
									.autocomplete({
								    source: function(request, response) {
									    $.ajax({
										    url: "parcelas.jsp?parcelaAux="+extractLast($("#parcelaAux1").val())+ "\&deptoAux="+$("#deptosAux1").val()+ "\&tipoBosqueAux="+$("#tBosqueAux1").val(),
										    type: "POST",
										    dataType: "json",
										    data: { name: request.term},
										    success: function( data ) {
										        response( $.map( data, function( item ) {
										        return {
										            label: item.name,
										            value: item.value,
										        };
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
								    minLength: 1
							    });
								    
								});							
							</script>
							<div id="content">
								<div class="content-inner">

									<div id="block-consulta-reportes" class="block-gray block">
											<div class="form-group" style="padding-left: 20px; padding-right: 20px">
												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px;"> 
													<label >Departamentos 1/Departments 1: </label>
													<select class="select-wrapper" name="deptosAux1" id="deptosAux1">
													<option value="">Seleccionar/Select</option>
													<%=departamentos%>	</select>												
												</span>
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label >Departamentos 2/Departments 2: </label>
													<select class="select-wrapper" name="deptosAux2" id="deptosAux2">
													<option value="">Seleccionar/Select</option>
													<%=departamentos%>	</select>												
												</span>
												<br/>
												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px"> 
													<label >Tipos de Bosque 1/Forest Types 1: </label>
													<select class="select-wrapper" name="tBosqueAux1" id="tBosqueAux1">
													<option value="">Seleccionar/Select</option>
													<%=tipoBosque%>	</select>	
												</span>
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label >Tipos de Bosque 2/Forest Types 2: </label>
													<select class="select-wrapper" name="tBosqueAux2" id="tBosqueAux2">
													<option value="">Seleccionar/Select</option>
													<%=tipoBosque%>	</select>												
												</span>
												<br/>
												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px"> 
													<label for="exampleInputEmail1">Parcelas 1/Plots 1: </label>
													<input name="parcelaAux1" id="parcelaAux1" type="text" />														
												</span>
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label for="exampleInputEmail1">Parcelas 2/Plots 2: </label>
													<input name="parcelaAux2" id="parcelaAux2" type="text" />														
												</span>
												<br/>
												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px"> 
													<label for="exampleInputEmail1">Especies Lugar 1/Species Place 1: </label>
													<input name="especieAux1" id="especieAux1" type="text" title="Ingrese minimo 3 caracteres/At least 3 characters" required />														
												</span>
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label for="exampleInputEmail1">Especies Lugar 2/Species Place 2: </label>
													<input name="especieAux2" id="especieAux2" type="text" title="Ingrese minimo 3 caracteres/At least 3 characters" required />														
												</span>
												<br/>
												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px"> 
													<label id="l3" for="fechaIniAux1" >Fecha inicial 1/Start Date 1:</label> 
													<input type="text" class="select-wrapper" name="fechaIniAux1"  id="fechaIniAux1"/>
													<script type='text/javascript'>
													Calendar.setup({
														inputField     :    'fechaIniAux1',				// id of the input field
														ifFormat       :    '%d/%m/%Y',	// format of the input field
														showsTime      :    false,					// will display a time selector
														button         :    'fechaIniAux1',				// trigger for the calendar (button ID)
														singleClick    :    false,					// double-click mode
														step           :    1						// show all years in drop-down boxes (instead of every other year as default)
													});
													</script>
												</span>
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label id="l3b" for="fechaIniAux2" >Fecha inicial 2/Start Date 2:</label> 
													<input type="text" class="select-wrapper" name="fechaIniAux2" id="fechaIniAux2"/>
													<script type='text/javascript'>
													Calendar.setup({
														inputField     :    'fechaIniAux2',				// id of the input field
														ifFormat       :    '%d/%m/%Y',	// format of the input field
														showsTime      :    false,					// will display a time selector
														button         :    'fechaIniAux2',				// trigger for the calendar (button ID)
														singleClick    :    false,					// double-click mode
														step           :    1						// show all years in drop-down boxes (instead of every other year as default)
													});
													</script>
												</span>
												<br/>
												<span class="item-periodo-left" style="padding-left: 20px; padding-right: 20px"> 
													<label id="l3" for="fechaFinAux1" >Fecha final 1/End Date 1:</label> 
													<input type="text" class="select-wrapper" name="fechaFinAux1" id="fechaFinAux1"/>
													<script type='text/javascript'>
													Calendar.setup({
														inputField     :    'fechaFinAux1',				// id of the input field
														ifFormat       :    '%d/%m/%Y',	// format of the input field
														showsTime      :    false,					// will display a time selector
														button         :    'fechaFinAux1',				// trigger for the calendar (button ID)
														singleClick    :    false,					// double-click mode
														step           :    1						// show all years in drop-down boxes (instead of every other year as default)
													});
													</script>
												</span>
												<span class="item-periodo-right" style="padding-left: 20px; padding-right: 20px"> 
													<label id="l3b" for="fechaFinAux2" >Fecha final 2/End Date 2:</label> 
													<input type="text" class="select-wrapper" name="fechaFinAux2" id="fechaFinAux2"/>
													<script type='text/javascript'>
													Calendar.setup({
														inputField     :    'fechaFinAux2',				// id of the input field
														ifFormat       :    '%d/%m/%Y',	// format of the input field
														showsTime      :    false,					// will display a time selector
														button         :    'fechaFinAux2',				// trigger for the calendar (button ID)
														singleClick    :    false,					// double-click mode
														step           :    1						// show all years in drop-down boxes (instead of every other year as default)
													});
													</script>
												</span>
											</div>
											<!-- Tipo de Bosques -->
											<div class="form-actions">
												<input class="btn btn-default btn-ir" type="button" id="enviarConsulta"
													value="Consultar/Query"></input>
											</div>
											
											
											<script>
											  $(function() {
											  	$( document ).tooltip();

											  	var todaysDate = new Date();
// 									                $("#fechaIniAux1").datepicker({
									                  
// 									                    dateFormat: 'dd/mm/yy',
// 									                    maxDate: todaysDate
// 									                }, $.datepicker.regional["es"]).on("input change", function (e) {
//                     var minDate = $('#fechaIniAux1').datepicker('getDate');
//                     $("#fechaFinAux1").datepicker("option", "minDate", minDate);
//                 });
// 									                $("#fechaFinAux1").datepicker({
									                    
// 									                    dateFormat: 'dd/mm/yy',
// 									                    maxDate: todaysDate
// 									                },$.datepicker.regional["es"]);
// 									                $("#fechaIniAux2").datepicker({
									                    
// 									                    dateFormat: 'dd/mm/yy',
// 									                    maxDate: todaysDate
// 									                }, $.datepicker.regional["es"]).on("input change", function (e) {
//                     console.log("Date changed: ", e.target.value);
//                     var minDate = $('#fechaIniAux2').datepicker('getDate');
//                     $("#fechaFinAux2").datepicker("option", "minDate", minDate);
//                 });
// 									                $("#fechaFinAux2").datepicker({
									                    
// 									                    dateFormat: 'dd/mm/yy',
// 									                    maxDate: todaysDate
// 									                },$.datepicker.regional["es"]);
												    
												
												    
												    $("#enviarConsulta").click(function(){
												    	
														
												    	if(verificarCampos()==false){
												    		return;
												    	}
												    	document.getElementById("especie1").value= "";
														document.getElementById("depto1").value= "";
														document.getElementById("tBosque1").value= "";
														document.getElementById("fechaIni1").value= "";
														document.getElementById("fechaFin1").value="";
														document.getElementById("parcela1").value="";

														document.getElementById("especie2").value= "";
														document.getElementById("depto2").value= "";
														document.getElementById("tBosque2").value= "";
														document.getElementById("fechaIni2").value= "";
														document.getElementById("fechaFin2").value="";
														document.getElementById("parcela2").value="";
														
														document.getElementById("especie1").value = document.getElementById("especieAux1").value;
														document.getElementById("tBosque1").value = document.getElementById("tBosqueAux1").value;
														document.getElementById("depto1").value = document.getElementById("deptosAux1").value;
														document.getElementById("fechaIni1").value = document.getElementById("fechaIniAux1").value;
														document.getElementById("fechaFin1").value = document.getElementById("fechaFinAux1").value;
														document.getElementById("parcela1").value = document.getElementById("parcelaAux1").value;

														document.getElementById("especie2").value = document.getElementById("especieAux2").value;
														document.getElementById("tBosque2").value = document.getElementById("tBosqueAux2").value;
														document.getElementById("depto2").value = document.getElementById("deptosAux2").value;
														document.getElementById("fechaIni2").value = document.getElementById("fechaIniAux2").value;
														document.getElementById("fechaFin2").value = document.getElementById("fechaFinAux2").value;
														document.getElementById("parcela2").value = document.getElementById("parcelaAux1").value;
														
														var data = {
																tBosque1: document.getElementById("tBosqueAux1").value,
																deptosAux1: document.getElementById("deptosAux1").value,
																fechaIni1: document.getElementById("fechaIniAux1").value,
																fechaFin1: document.getElementById("fechaFinAux1").value,
																parcela1:document.getElementById("parcelaAux1").value,
																especie1:document.getElementById("especieAux1").value,
																tBosque2: document.getElementById("tBosqueAux2").value,
																deptosAux2: document.getElementById("deptosAux2").value,
																fechaIni2: document.getElementById("fechaIniAux2").value,
																fechaFin2: document.getElementById("fechaFinAux2").value,
																parcela2: document.getElementById("parcelaAux2").value,
																especie2: document.getElementById("especieAux2").value
														};
														
														
														
														var url = "<%=basePath%>consultarReporteSimilitudServlet";
														
														$.ajax({
															type: "POST",
															url: url,
															data: data,
															success: function(data){
																ocultarLoading();
																$("#div_resultado").html(data);
																
															}
														});			
														
												    });
												    
												  });
											  
											</script>
									</div>
									<div id="capaLoading" class='centrar' style="display: none;">
										<img src="<%=basePath%>img/loading1.gif">
									</div>
									<div id="div_resultado" style="height: 480px; overflow: auto;">
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
	
	<form action="<%=basePath%>consultarReporteSimilitudServlet" method="post" name="formConsultaReporte" id="formConsultaReporte" onsubmit= "verificarCampos();">
			<input type="hidden" name="tBosque1" id="tBosque1" value="">
			<input type="hidden" name="especie1" id="especie1" value="">
			<input type="hidden" name="depto1" id="depto1" value="">
			<input type="hidden" name="parcela1" id="parcela1" value="">
			<input type="hidden" name="fechaIni1" id="fechaIni1" value="">
			<input type="hidden" name="fechaFin1" id="fechaFin1" value="">
			<input type="hidden" name="tBosque2" id="tBosque2" value="">
			<input type="hidden" name="especie2" id="especie2" value="">
			<input type="hidden" name="depto2" id="depto2" value="">
			<input type="hidden" name="parcela2" id="parcela2" value="">
			<input type="hidden" name="fechaIni2" id="fechaIni2" value="">
			<input type="hidden" name="fechaFin2" id="fechaFin2" value="">
	</form>
</body>
<style>
	.ui-autocomplete{
		background-color: #CCC;
	}
</style>
</html>

