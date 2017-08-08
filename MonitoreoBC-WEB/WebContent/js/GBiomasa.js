// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
var gWidth = "550";
var gHeight = "550";
var gBarChartWidth = "70%";
var gBarChartHeight = "40%";
var gPieChartWidth = "90%";
var gPieChartHeight = "40%";
var div_barras = 'divGBarras';
var div_pie = 'divGTortas';

function registroBC(nombre, biomasa, carbono) {
	this.nombre = nombre;
	this.biomasa = biomasa;
	this.carbono = carbono;
}
function areaHidroGBiomasa(a_registrosBC, periodo, document) {
	var data = null;
	var options = null;
	var chart = null;
	
	document.getElementById('tabs-2').style.display = 'block';
	document.getElementById(div_barras).style.display = 'block';
	document.getElementById(div_pie).style.display = 'block';
	//document.getElementById('divGInformacion').style.display = 'block';
	
	// **************Grafica de Barras Biomasa*********************
	var a_tablaBC = new Array();	
	a_tablaBC.push(['Área Hidrográfica', 'Biomasa Aérea', 'Carbono']);
	for (var i=0; i<a_registrosBC.length; i++) {
		var r = a_registrosBC[i];
		if (r) {
			a_tablaBC.push([r.nombre, r.biomasa, r.carbono]);
		}	
	}
	
	data = google.visualization.arrayToDataTable(a_tablaBC);

	options = {
			title : 'Biomasa Aérea y Carbono por Área Hidrográfica (Toneladas)',
			width : gWidth,
			height : gHeight,
            chartArea: {  width: gBarChartWidth, height: gBarChartHeight },
			backgroundColor: 'DFDFDF',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : 'Área Hidrográfica',
				titleTextStyle : {
					color : 'black'
				},
		        direction: 1, 
		        slantedText: true, 
		        slantedTextAngle: 90 
			},
			seriesType: 'bars'
	};

	chart = new google.visualization.ComboChart(document.getElementById(div_barras));
	chart.draw(data, options);
	
	// ************Grafica Torta Biomasa***************

	var a_tablaBC = new Array();

	a_tablaBC.push(['Area Hidrográfica', 'Biomasa Aerea']);
	for (var i=0; i<a_registrosBC.length; i++) {
		var r = a_registrosBC[i];
		if (r) {
			a_tablaBC.push([r.nombre, r.biomasa]);
		}	
	}

	data = google.visualization.arrayToDataTable(a_tablaBC);
	
	options = {
			title : 'Distribución de la Biomasa Aérea y el Carbono por Área Hidrográfica',
			width : gWidth,
			height : gHeight,
            chartArea: {  width: gPieChartWidth, height: gPieChartHeight },
			backgroundColor: 'DFDFDF',
			legend : {
				position : 'top',
				maxLines : 3
			}
	};
	
	chart = new google.visualization.PieChart(document.getElementById(div_pie));
	chart.draw(data, options);
	
	document.getElementById('infoPeriodo').innerHTML = "Periodo:" + periodo;
	
}

function tipoBosqueGBiomasa(a_registrosBC, periodo, document) {
	var data = null;
	var options = null;
	var chart = null;
	
	document.getElementById('tabs-2').style.display = 'block';
	document.getElementById(div_barras).style.display = 'block';
	document.getElementById(div_pie).style.display = 'block';
	//document.getElementById('divGInformacion').style.display = 'block';
	
	// **************Grafica de Barras Biomasa*********************
	var a_tablaBC = new Array();	
	a_tablaBC.push(['Tipo de Bosque', 'Biomasa Aérea', 'Carbono']);
	for (var i=0; i<a_registrosBC.length; i++) {
		var r = a_registrosBC[i];
		if (r) {
			a_tablaBC.push([r.nombre, r.biomasa, r.carbono]);
		}	
	}
	
	data = google.visualization.arrayToDataTable(a_tablaBC);

	options = {
			title : 'Biomasa Aérea y Carbono por Tipo de Bosque (Toneladas)',
			width : gWidth,
			height : gHeight,
            chartArea: {  width: gBarChartWidth, height: gBarChartHeight },
			backgroundColor: 'DFDFDF',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : 'Tipo de Bosque',
				titleTextStyle : {
					color : 'black'
				},
		        direction: 1, 
		        slantedText: true, 
		        slantedTextAngle: 90 
			},
			seriesType: 'bars'
	};

	chart = new google.visualization.ComboChart(document.getElementById(div_barras));
	chart.draw(data, options);
	
	// ************Grafica Torta Biomasa***************

	var a_tablaBC = new Array();

	a_tablaBC.push(['Tipo de Bosque', 'Biomasa Aerea']);
	for (var i=0; i<a_registrosBC.length; i++) {
		var r = a_registrosBC[i];
		if (r) {
			a_tablaBC.push([r.nombre, r.biomasa]);
		}	
	}

	data = google.visualization.arrayToDataTable(a_tablaBC);
	
	options = {
			title : 'Distribución de la Biomasa Aérea y el Carbono por Tipo de Bosque',
			width : gWidth,
			height : gHeight,
            chartArea: {  width: gPieChartWidth, height: gPieChartHeight },
			backgroundColor: 'DFDFDF',
			legend : {
				position : 'top',
				maxLines : 3
			}
	};
	
	chart = new google.visualization.PieChart(document.getElementById(div_pie));
	chart.draw(data, options);
	
	document.getElementById('infoPeriodo').innerHTML = "Periodo:" + periodo;
	
}

function consolidadoGBiomasa(a_registrosBC, periodo, document) {
	var data = null;
	var options = null;
	var chart = null;
	
	document.getElementById('tabs-2').style.display = 'block';
	document.getElementById(div_barras).style.display = 'block';
	document.getElementById(div_pie).style.display = 'block';
	//document.getElementById('divGInformacion').style.display = 'block';
	
	// **************Grafica de Barras Biomasa*********************
	var a_tablaBC = new Array();	
	a_tablaBC.push(['Total Nacional', 'Biomasa Aérea', 'Carbono']);
	for (var i=0; i<a_registrosBC.length; i++) {
		var r = a_registrosBC[i];
		if (r) {
			a_tablaBC.push([r.nombre, r.biomasa, r.carbono]);
		}	
	}
	
	data = google.visualization.arrayToDataTable(a_tablaBC);
	
	options = {
			title : 'Biomasa Aérea y Carbono Total Nacional (Toneladas)',
			width : gWidth,
			height : gHeight,
            chartArea: {  width: gBarChartWidth, height: gBarChartHeight },
			backgroundColor: 'DFDFDF',
			legend : {
				position : 'top',
				maxLines : 3
			},
			vAxis : {
				title : '',
				titleTextStyle : {
					color : 'black'
				}
			},
			hAxis : {
				title : '',
				titleTextStyle : {
					color : 'black'
				},
		        direction: 1, 
		        slantedText: true, 
		        slantedTextAngle: 90 
			},
			seriesType: 'bars'
	};
	
	chart = new google.visualization.ComboChart(document.getElementById(div_barras));
	chart.draw(data, options);
	
	// ************Grafica Torta Biomasa***************
	
	var a_tablaBC = new Array();
	
	a_tablaBC.push(['Total Nacional', 'Biomasa Aerea']);
	for (var i=0; i<a_registrosBC.length; i++) {
		var r = a_registrosBC[i];
		if (r) {
			a_tablaBC.push([r.nombre, r.biomasa]);
		}	
	}
	
	data = google.visualization.arrayToDataTable(a_tablaBC);
	
	options = {
			title : 'Distribución de la Biomasa Aérea y el Carbono total Nacional',
			width : gWidth,
			height : gHeight,
            chartArea: {  width: gPieChartWidth, height: gPieChartHeight },
			backgroundColor: 'DFDFDF',
			legend : {
				position : 'top',
				maxLines : 3
			}
	};
	
	chart = new google.visualization.PieChart(document.getElementById(div_pie));
	chart.draw(data, options);
	
	document.getElementById('infoPeriodo').innerHTML = "Periodo:" + periodo;
	
}

