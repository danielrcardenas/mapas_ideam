function datosGCobertura(nombresDivisiones, datosCobertura, periodo, document) {
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
	data = google.visualization
			.arrayToDataTable([
					[ 'Periodo', 'Bosque Estable', 'No Bosque Estable',
							'Deforestacion', 'Regeneracion',
							'Sin Informacion Estable' ],
					[
							periodo[periodoSeleccionado],
							datosCobertura[periodoSeleccionado][seleccionado][0][0],
							datosCobertura[periodoSeleccionado][seleccionado][1][0],
							datosCobertura[periodoSeleccionado][seleccionado][2][0],
							datosCobertura[periodoSeleccionado][seleccionado][3][0],
							datosCobertura[periodoSeleccionado][seleccionado][4][0] ] ]);
	options = {
		title : 'Coberura ' + nombresDivisiones[seleccionado],
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
							'Bosque Estable',
							datosCobertura[periodoSeleccionado][seleccionado][0][0] ],
					[
							'No Bosque Estable',
							datosCobertura[periodoSeleccionado][seleccionado][1][0] ],
					[
							'Deforestacion',
							datosCobertura[periodoSeleccionado][seleccionado][2][0] ],
					[
							'Regeneracion',
							datosCobertura[periodoSeleccionado][seleccionado][3][0] ],
					[
							'Sin Informacion Estable',
							datosCobertura[periodoSeleccionado][seleccionado][4][0] ] ]);

	options = {
		title : 'Coberura ' + nombresDivisiones[seleccionado],
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
			datosTendencias[x] = new Array(6);
		}

		for (x = 0; x < datosTendencias.length; x++) {
			if (x == 0) {
				datosTendencias[x][0] = 'x';
				datosTendencias[x][1] = 'Bosque Estable';
				datosTendencias[x][2] = 'No Bosque Estable';
				datosTendencias[x][3] = 'Deforestacion';
				datosTendencias[x][4] = 'Regeneracion';
				datosTendencias[x][5] = 'Sin Informacion Estable';
			} else {
				datosTendencias[x][0] = periodo[x - 1];
				datosTendencias[x][1] = datosCobertura[x - 1][seleccionado][0][0];
				datosTendencias[x][2] = datosCobertura[x - 1][seleccionado][1][0];
				datosTendencias[x][3] = datosCobertura[x - 1][seleccionado][2][0];
				datosTendencias[x][4] = datosCobertura[x - 1][seleccionado][3][0];
				datosTendencias[x][5] = datosCobertura[x - 1][seleccionado][4][0];
			}
		}

		data = google.visualization.arrayToDataTable(datosTendencias);

		// Create and draw the visualization.
		new google.visualization.LineChart(document
				.getElementById('divGTendencias')).draw(data, {
			curveType : "function",
			title : "Coberura",
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
	document.getElementById('info1').innerHTML = "Bosque Estable (Area%): "
			+ datosCobertura[periodoSeleccionado][seleccionado][0][0];
	document.getElementById('info2').innerHTML = "No Bosque Estable (Area%): "
			+ datosCobertura[periodoSeleccionado][seleccionado][1][0];
	document.getElementById('info3').innerHTML = "Deforestacion (Area%): "
			+ datosCobertura[periodoSeleccionado][seleccionado][2][0];
	document.getElementById('info4').innerHTML = "Regeneracion (Area%): "
			+ datosCobertura[periodoSeleccionado][seleccionado][3][0];
	document.getElementById('info5').innerHTML = "Sin Informacion Estable (Area%): "
			+ datosCobertura[periodoSeleccionado][seleccionado][4][0];

}

function datosConsolidadoGCobertura(datosCobertura, periodo, document) {
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
	data = google.visualization
			.arrayToDataTable([
					[ 'Periodo', 'Bosque Estable', 'No Bosque Estable',
							'Deforestacion', 'Regeneracion',
							'Sin Informacion Estable' ],
					[ periodo[seleccionado],
							datosCobertura[seleccionado][0][0],
							datosCobertura[seleccionado][1][0],
							datosCobertura[seleccionado][2][0],
							datosCobertura[seleccionado][3][0],
							datosCobertura[seleccionado][4][0] ] ]);
	options = {
		title : 'Coberura Consolidado Nacional ',
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
					[ 'Bosque Estable', datosCobertura[seleccionado][0][0] ],
					[ 'No Bosque Estable', datosCobertura[seleccionado][1][0] ],
					[ 'Deforestacion', datosCobertura[seleccionado][2][0] ],
					[ 'Regeneracion', datosCobertura[seleccionado][3][0] ],
					[ 'Sin Informacion Estable',
							datosCobertura[seleccionado][4][0] ] ]);

	options = {
		title : 'Coberura Consolidado Nacional',
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
			datosTendencias[x] = new Array(6);
		}

		for (x = 0; x < datosTendencias.length; x++) {
			if (x == 0) {
				datosTendencias[x][0] = 'x';
				datosTendencias[x][1] = 'Bosque Estable';
				datosTendencias[x][2] = 'No Bosque Estable';
				datosTendencias[x][3] = 'Deforestacion';
				datosTendencias[x][4] = 'Regeneracion';
				datosTendencias[x][5] = 'Sin Informacion Estable';
			} else {
				datosTendencias[x][0] = periodo[x - 1];
				datosTendencias[x][1] = datosCobertura[x - 1][0][0];
				datosTendencias[x][2] = datosCobertura[x - 1][1][0];
				datosTendencias[x][3] = datosCobertura[x - 1][2][0];
				datosTendencias[x][4] = datosCobertura[x - 1][3][0];
				datosTendencias[x][5] = datosCobertura[x - 1][4][0];
			}
		}

		data = google.visualization.arrayToDataTable(datosTendencias);

		// Create and draw the visualization.
		new google.visualization.LineChart(document
				.getElementById('divGTendencias')).draw(data, {
			curveType : "function",
			title : "Coberura",
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
	document.getElementById('info1').innerHTML = "Bosque Estable (Area%): "
			+ datosCobertura[seleccionado][0][0];
	document.getElementById('info2').innerHTML = "No Bosque Estable (Area%): "
			+ datosCobertura[seleccionado][1][0];
	document.getElementById('info3').innerHTML = "Deforestacion (Area%): "
			+ datosCobertura[seleccionado][2][0];
	document.getElementById('info4').innerHTML = "Regeneracion (Area%): "
			+ datosCobertura[seleccionado][3][0];
	document.getElementById('info5').innerHTML = "Sin Informacion Estable (Area%): "
			+ datosCobertura[seleccionado][4][0];

}
