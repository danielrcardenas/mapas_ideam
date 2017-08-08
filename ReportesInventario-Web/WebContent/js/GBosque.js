function datosGBosque(nombresDivisiones, datosBosque, periodo, document) {
	var data = null;
	var options = null;
	var chart = null;

	var seleccionado = document.getElementById("opcionGrafica").selectedIndex - 1;
	var periodoSeleccionado = document.getElementById("opcionPeriodoGrafica").selectedIndex;

	document.getElementById('divGBarras').style.display = 'block';
	document.getElementById('divGTortas').style.display = 'block';
	document.getElementById('divGInformacion').style.display = 'block';
	document.getElementById('divGMapas').style.display = 'block';
	document.getElementById('divGTendencias').style.display = 'block';

	// **************Grafica de Barras Bosque*********************
	data = google.visualization.arrayToDataTable([
			[ 'Periodo', 'Bosque', 'No Bosque', 'Sin Informacion' ],
			[ periodo[periodoSeleccionado],
					datosBosque[periodoSeleccionado][seleccionado][0][0],
					datosBosque[periodoSeleccionado][seleccionado][1][0],
					datosBosque[periodoSeleccionado][seleccionado][2][0] ] ]);
	options = {
		title : 'Bosque ' + nombresDivisiones[seleccionado],
		width : 550,
		height : 550,
		backgroundColor: 'DFDFDF',
		legend : {
			position : 'top',
			maxLines : 3
		},
		vAxis : {
			title : 'Periodo',
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

	// ************Grafica Torta Biomasa***************
	data = google.visualization
			.arrayToDataTable([
					[ 'Periodo', 'Area %' ],
					[
							'Bosque',
							datosBosque[periodoSeleccionado][seleccionado][0][0] ],
					[
							'No Bosque',
							datosBosque[periodoSeleccionado][seleccionado][1][0] ],
					[
							'Sin Informacion',
							datosBosque[periodoSeleccionado][seleccionado][2][0] ] ]);

	options = {
		title : 'Bosque ' + nombresDivisiones[seleccionado],
		width : 550,
		height : 550,
		backgroundColor: 'DFDFDF',
		legend : {
			position : 'top',
			maxLines : 3
		}
	};
	
	chart = new google.visualization.PieChart(document
			.getElementById('divGTortas'));
	chart.draw(data, options);

	// *************Grafica Tendencias***********
	if (periodo.length > 1) {
		var datosTendencias = new Array(periodo.length + 1);
		var x = 0;
		for (x = 0; x < datosTendencias.length; x++) {
			datosTendencias[x] = new Array(4);
		}

		for (x = 0; x < datosTendencias.length; x++) {
			if (x == 0) {
				datosTendencias[x][0] = 'x';
				datosTendencias[x][1] = 'Bosque';
				datosTendencias[x][2] = 'No Bosque';
				datosTendencias[x][3] = 'Sin Informacion';
			} else {
				datosTendencias[x][0] = periodo[x-1];
				datosTendencias[x][1] = datosBosque[x-1][seleccionado][0][0];
				datosTendencias[x][2] = datosBosque[x-1][seleccionado][1][0];
				datosTendencias[x][3] = datosBosque[x-1][seleccionado][2][0];
			}
		}

		data = google.visualization.arrayToDataTable(datosTendencias);

		// Create and draw the visualization.
		new google.visualization.LineChart(document
				.getElementById('divGTendencias')).draw(data, {
			curveType : "function",
			title : "Bosque",
			width : 550,
			height : 550,
			backgroundColor: 'DFDFDF',
			vAxis : {
				maxValue : 10
			}
		});
	}

	document.getElementById('infoArea').innerHTML = nombresDivisiones[seleccionado];
	document.getElementById('infoPeriodo').innerHTML = "Periodo:"
			+ periodo[periodoSeleccionado];
	document.getElementById('infoBosque').innerHTML = "Bosque (Area%): "
			+ datosBosque[periodoSeleccionado][seleccionado][0][0];
	document.getElementById('infoNoBosque').innerHTML = "No Bosque (Area%): "
			+ datosBosque[periodoSeleccionado][seleccionado][1][0];
	document.getElementById('infoSinInfo').innerHTML = "Sin Informacion (Area%): "
			+ datosBosque[periodoSeleccionado][seleccionado][2][0];

}

function datosConsolidadoGBosque(datosBosque, periodo, document) {
	var data = null;
	var options = null;
	var chart = null;

	var seleccionado = document.getElementById("opcionGrafica").selectedIndex - 1;

	document.getElementById('divGBarras').style.display = 'block';
	document.getElementById('divGTortas').style.display = 'block';
	document.getElementById('divGInformacion').style.display = 'block';
	document.getElementById('divGMapas').style.display = 'block';
	document.getElementById('divGTendencias').style.display = 'block';

	// **************Grafica de Barras Bosque*********************
	data = google.visualization.arrayToDataTable([
			[ 'Periodo', 'Bosque', 'No Bosque', 'Sin Informacion' ],
			[ periodo[seleccionado], datosBosque[seleccionado][0][0],
					datosBosque[seleccionado][1][0],
					datosBosque[seleccionado][2][0] ] ]);
	options = {
		title : 'Bosque Consolidado Nacional ',
		width : 550,
		height : 550,
		backgroundColor: 'DFDFDF',
		legend : {
			position : 'top',
			maxLines : 3
		},
		vAxis : {
			title : 'Periodo',
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

	// ************Grafica Torta Biomasa***************
	data = google.visualization.arrayToDataTable([ [ 'Periodo', 'Area %' ],
			[ 'Bosque', datosBosque[seleccionado][0][0] ],
			[ 'No Bosque', datosBosque[seleccionado][1][0] ],
			[ 'Sin Informacion', datosBosque[seleccionado][2][0] ] ]);

	options = {
		title : 'Bosque Consolidado Nacional',
		width : 550,
		height : 550,
		backgroundColor: 'DFDFDF',
		legend : {
			position : 'top',
			maxLines : 3
		}
	};

	chart = new google.visualization.PieChart(document
			.getElementById('divGTortas'));
	chart.draw(data, options);
	
	// *************Grafica Tendencias***********
	if (periodo.length > 1) {
		var datosTendencias = new Array(periodo.length + 1);
		var x = 0;
		for (x = 0; x < datosTendencias.length; x++) {
			datosTendencias[x] = new Array(4);
		}

		for (x = 0; x < datosTendencias.length; x++) {
			if (x == 0) {
				datosTendencias[x][0] = 'x';
				datosTendencias[x][1] = 'Bosque';
				datosTendencias[x][2] = 'No Bosque';
				datosTendencias[x][3] = 'Sin Informacion';
			} else {
				datosTendencias[x][0] = periodo[x-1];
				datosTendencias[x][1] = datosBosque[x-1][0][0];
				datosTendencias[x][2] = datosBosque[x-1][1][0];
				datosTendencias[x][3] = datosBosque[x-1][2][0];
			}
		}

		data = google.visualization.arrayToDataTable(datosTendencias);

		// Create and draw the visualization.
		new google.visualization.LineChart(document
				.getElementById('divGTendencias')).draw(data, {
			curveType : "function",
			title : "Bosque",
			width : 550,
			height : 550,
			backgroundColor: 'DFDFDF',
			vAxis : {
				maxValue : 10
			}
		});
	}

	document.getElementById('infoArea').innerHTML = "Consolidado Nacional";
	document.getElementById('infoPeriodo').innerHTML = "Periodo:"
			+ periodo[seleccionado];
	document.getElementById('infoBosque').innerHTML = "Bosque (Area%): "
			+ datosBosque[seleccionado][0][0];
	document.getElementById('infoNoBosque').innerHTML = "No Bosque (Area%): "
			+ datosBosque[seleccionado][1][0];
	document.getElementById('infoSinInfo').innerHTML = "Sin Informacion (Area%): "
			+ datosBosque[seleccionado][2][0];

}
