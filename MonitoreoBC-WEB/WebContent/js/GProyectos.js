var gWidth = "550";
var gHeight = "550";
var gBarChartWidth = "70%";
var gBarChartHeight = "40%";
var gPieChartWidth = "90%";
var gPieChartHeight = "40%";
var div_barras = 'divGBarras';
var div_pie = 'divGTortas';

function conteoProyecto(conteo, dimension1, dimension2) {
	this.conteo = conteo;
	this.dimension1 = dimension1;
	this.dimension2 = dimension2;
}

function graficarConteoProyectos(conteoProyectos, tipo, agregacion) {
	var data = null;
	var options = null;
	var chart = null;
	
	
	// **************Grafica de Barras Biomasa*********************
	var a_tabla = new Array();	
	a_tabla.push(['', 'Biomasa Aérea', 'Carbono']);
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

