function areaHidroGDeforestacion(nombresAreasHidro, nombresTiposBosque, datosBiomasa,
		periodo, document) {
	var data = null;
	var options = null;
	var chart = null;

	var seleccionado = document.getElementById("opcionGrafica").selectedIndex - 1;

	document.getElementById('divGBarras').style.display = 'block';
	document.getElementById('divGTortas').style.display = 'block';
	document.getElementById('divGInformacion').style.display = 'block';
	document.getElementById('divGMapas').style.display = 'block';

	// **************Grafica de Barras Biomasa*********************
	data = google.visualization.arrayToDataTable([
			[ 'Periodo', nombresTiposBosque[0], nombresTiposBosque[1],
					nombresTiposBosque[2], nombresTiposBosque[3],
					nombresTiposBosque[4], nombresTiposBosque[5],
					nombresTiposBosque[6], nombresTiposBosque[7],
					nombresTiposBosque[8], nombresTiposBosque[9],
					nombresTiposBosque[10], nombresTiposBosque[11] ],
			[ periodo, datosBiomasa[seleccionado][0][0],
					datosBiomasa[seleccionado][1][0],
					datosBiomasa[seleccionado][2][0],
					datosBiomasa[seleccionado][3][0],
					datosBiomasa[seleccionado][4][0],
					datosBiomasa[seleccionado][5][0],
					datosBiomasa[seleccionado][6][0],
					datosBiomasa[seleccionado][7][0],
					datosBiomasa[seleccionado][8][0],
					datosBiomasa[seleccionado][9][0],
					datosBiomasa[seleccionado][10][0],
					datosBiomasa[seleccionado][11][0] ] ]);
	options = {
		title : 'Deforestacion/Reforestacion '
				+ nombresAreasHidro[seleccionado],
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
			title : 'Area%',
			titleTextStyle : {
				color : 'black'
			}
		}
	};

	chart = new google.visualization.BarChart(document
			.getElementById('divGBarras'));
	chart.draw(data, options);

	// ************Grafica Torta Biomasa***************
	data = google.visualization.arrayToDataTable([ [ 'Periodo', 'Area%' ],
			[ nombresTiposBosque[0], datosBiomasa[seleccionado][0][0] ],
			[ nombresTiposBosque[1], datosBiomasa[seleccionado][1][0] ],
			[ nombresTiposBosque[2], datosBiomasa[seleccionado][2][0] ],
			[ nombresTiposBosque[3], datosBiomasa[seleccionado][3][0] ],
			[ nombresTiposBosque[4], datosBiomasa[seleccionado][4][0] ],
			[ nombresTiposBosque[5], datosBiomasa[seleccionado][5][0] ],
			[ nombresTiposBosque[6], datosBiomasa[seleccionado][6][0] ],
			[ nombresTiposBosque[7], datosBiomasa[seleccionado][7][0] ],
			[ nombresTiposBosque[8], datosBiomasa[seleccionado][8][0] ],
			[ nombresTiposBosque[9], datosBiomasa[seleccionado][9][0] ],
			[ nombresTiposBosque[10], datosBiomasa[seleccionado][10][0] ],
			[ nombresTiposBosque[11], datosBiomasa[seleccionado][11][0] ] ]);

	options = {
		title : 'Deforestacion/Reforestacion '
				+ nombresAreasHidro[seleccionado],
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

	document.getElementById('infoArea').innerHTML = nombresAreasHidro[seleccionado];
	document.getElementById('infoPeriodo').innerHTML = "Periodo:" + periodo;
	document.getElementById('infoTB0').innerHTML = "Areas urbanizadas: "
			+ datosBiomasa[seleccionado][0][0];
	document.getElementById('infoTB1').innerHTML = "Cultivos transitorios: "
			+ datosBiomasa[seleccionado][1][0];
	document.getElementById('infoTB2').innerHTML = "Cultivos permanentes: "
			+ datosBiomasa[seleccionado][2][0];
	document.getElementById('infoTB3').innerHTML = "Pastos: "
			+ datosBiomasa[seleccionado][3][0];
	document.getElementById('infoTB4').innerHTML = "Plantacion forestal: "
			+ datosBiomasa[seleccionado][4][0];
	document.getElementById('infoTB5').innerHTML = "Areas con vegetacion herbacea: "
			+ datosBiomasa[seleccionado][5][0];
	document.getElementById('infoTB6').innerHTML = "Areas con vegetacion arbustiva: "
			+ datosBiomasa[seleccionado][6][0];
	document.getElementById('infoTB7').innerHTML = "Vegetacion secundaria o en trancision: "
			+ datosBiomasa[seleccionado][7][0];
	document.getElementById('infoTB8').innerHTML = "Zonas quemadas: "
			+ datosBiomasa[seleccionado][8][0];
	document.getElementById('infoTB9').innerHTML = "Otras areas sin vegetacion: "
			+ datosBiomasa[seleccionado][9][0];
	document.getElementById('infoTB10').innerHTML = "Vegetacion acuatica: "
			+ datosBiomasa[seleccionado][10][0];
	document.getElementById('infoTB11').innerHTML = "Superficies de agua: "
			+ datosBiomasa[seleccionado][11][0];

}

function consolidadoGDeforestacion(nombresAreasHidro, nombresTiposBosque,
		datosBiomasa, periodo, document) {
	var data = null;
	var options = null;
	var chart = null;
	
	document.getElementById('divGBarras').style.display = 'block';
	document.getElementById('divGTortas').style.display = 'block';
	document.getElementById('divGInformacion').style.display = 'block';
	document.getElementById('divGMapas').style.display = 'block';

	var i = 0;
	for (i = 0; i < 2; i++) {

		// **************Grafica de Barras Biomasa*********************
		data = google.visualization.arrayToDataTable([
				[ 'Periodo', nombresTiposBosque[0], nombresTiposBosque[1],
						nombresTiposBosque[2], nombresTiposBosque[3],
						nombresTiposBosque[4], nombresTiposBosque[5],
						nombresTiposBosque[6], nombresTiposBosque[7],
						nombresTiposBosque[8], nombresTiposBosque[9],
						nombresTiposBosque[10], nombresTiposBosque[11] ],
				[ periodo, datosBiomasa[0][0], datosBiomasa[1][0],
						datosBiomasa[2][0], datosBiomasa[3][0],
						datosBiomasa[4][0], datosBiomasa[5][0],
						datosBiomasa[6][0], datosBiomasa[7][0],
						datosBiomasa[8][0], datosBiomasa[9][0],
						datosBiomasa[10][0], datosBiomasa[11][0] ] ]);
		options = {
			title : 'Deforestacion/Reforestacion Consolidado Nacional',
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
				title : 'Area%',
				titleTextStyle : {
					color : 'black'
				}
			}
		};

		chart = new google.visualization.BarChart(document
				.getElementById('divGBarras'));
		chart.draw(data, options);

		// ************Grafica Torta Biomasa***************
		data = google.visualization.arrayToDataTable([ [ 'Periodo', 'Area%' ],
				[ nombresTiposBosque[0], datosBiomasa[0][0] ],
				[ nombresTiposBosque[1], datosBiomasa[1][0] ],
				[ nombresTiposBosque[2], datosBiomasa[2][0] ],
				[ nombresTiposBosque[3], datosBiomasa[3][0] ],
				[ nombresTiposBosque[4], datosBiomasa[4][0] ],
				[ nombresTiposBosque[5], datosBiomasa[5][0] ],
				[ nombresTiposBosque[6], datosBiomasa[6][0] ],
				[ nombresTiposBosque[7], datosBiomasa[7][0] ],
				[ nombresTiposBosque[8], datosBiomasa[8][0] ],
				[ nombresTiposBosque[9], datosBiomasa[9][0] ],
				[ nombresTiposBosque[10], datosBiomasa[10][0] ],
				[ nombresTiposBosque[11], datosBiomasa[11][0] ] ]);

		options = {
			title : 'Deforestacion/Reforestacion Consolidado Nacional',
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

		document.getElementById('infoArea').innerHTML = "Consolidado Nacional";
		document.getElementById('infoPeriodo').innerHTML = "Periodo:" + periodo;
		document.getElementById('infoTB0').innerHTML = "Areas urbanizadas%: "
				+ datosBiomasa[0][0];
		document.getElementById('infoTB1').innerHTML = "Cultivos transitorios%: "
				+ datosBiomasa[1][0];
		document.getElementById('infoTB2').innerHTML = "Cultivos permanentes%: "
				+ datosBiomasa[2][0];
		document.getElementById('infoTB3').innerHTML = "Pastos%: "
				+ datosBiomasa[3][0];
		document.getElementById('infoTB4').innerHTML = "Plantacion forestal%: "
				+ datosBiomasa[4][0];
		document.getElementById('infoTB5').innerHTML = "Areas con vegetacion herbacea%: "
				+ datosBiomasa[5][0];
		document.getElementById('infoTB6').innerHTML = "Areas con vegetacion arbustiva%: "
				+ datosBiomasa[6][0];
		document.getElementById('infoTB7').innerHTML = "Vegetacion secundaria%: "
				+ datosBiomasa[7][0];
		document.getElementById('infoTB8').innerHTML = "Zonas quemadas%: "
				+ datosBiomasa[8][0];
		document.getElementById('infoTB9').innerHTML = "Otras areas sin vegetacion%: "
				+ datosBiomasa[9][0];
		document.getElementById('infoTB10').innerHTML = "Vegetacion acuatica%: "
				+ datosBiomasa[10][0];
		document.getElementById('infoTB11').innerHTML = "Superficies de agua%: "
				+ datosBiomasa[11][0];

	}

}
