function areaHidroGBiomasa(nombresAreasHidro, nombresTiposBosque, datosBiomasa,
		periodo, document) {
	var data = null;
	var options = null;
	var chart = null;

	var seleccionado = document.getElementById("opcionGrafica").selectedIndex-1;

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
					nombresTiposBosque[10], nombresTiposBosque[11],
					nombresTiposBosque[12], nombresTiposBosque[13],
					nombresTiposBosque[14] ],
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
					datosBiomasa[seleccionado][11][0],
					datosBiomasa[seleccionado][12][0],
					datosBiomasa[seleccionado][13][0],
					datosBiomasa[seleccionado][14][0] ] ]);
	options = {
		title : 'Biomasa ' + nombresAreasHidro[seleccionado],
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
			title : 'BA(t)',
			titleTextStyle : {
				color : 'black'
			}
		}
	};

	chart = new google.visualization.BarChart(document
			.getElementById('divGBarras'));
	chart.draw(data, options);

	// ************Grafica Torta Biomasa***************
	data = google.visualization.arrayToDataTable([ [ 'Periodo', 'BA(t)' ],
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
			[ nombresTiposBosque[11], datosBiomasa[seleccionado][11][0] ],
			[ nombresTiposBosque[12], datosBiomasa[seleccionado][12][0] ],
			[ nombresTiposBosque[13], datosBiomasa[seleccionado][13][0] ],
			[ nombresTiposBosque[14], datosBiomasa[seleccionado][14][0] ] ]);

	options = {
		title : 'Biomasa ' + nombresAreasHidro[seleccionado],
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
	document.getElementById('infoTB0').innerHTML = "Bosque muy seco Tropical BA(t): "
			+ datosBiomasa[seleccionado][0][0];
	document.getElementById('infoTB1').innerHTML = "Bosque seco Tropical BA(t): "
			+ datosBiomasa[seleccionado][1][0];
	document.getElementById('infoTB2').innerHTML = "Bosque humedo Tropical BA(t): "
			+ datosBiomasa[seleccionado][2][0];
	document.getElementById('infoTB3').innerHTML = "Bosque humedo Pre-montano BA(t): "
			+ datosBiomasa[seleccionado][3][0];
	document.getElementById('infoTB4').innerHTML = "Bosque muy humedo Pre-montano BA(t): "
			+ datosBiomasa[seleccionado][4][0];
	document.getElementById('infoTB5').innerHTML = "Bosque humedo Montano-bajo BA(t): "
			+ datosBiomasa[seleccionado][5][0];
	document.getElementById('infoTB6').innerHTML = "Bosque muy humedo Montano-bajo BA(t): "
			+ datosBiomasa[seleccionado][6][0];
	document.getElementById('infoTB7').innerHTML = "Bosque seco Pre-montano BA(t): "
			+ datosBiomasa[seleccionado][7][0];
	document.getElementById('infoTB8').innerHTML = "Bosque muy humedo Montano BA(t): "
			+ datosBiomasa[seleccionado][8][0];
	document.getElementById('infoTB9').innerHTML = "Bosque pluvial Montano BA(t): "
			+ datosBiomasa[seleccionado][9][0];
	document.getElementById('infoTB10').innerHTML = "Bosque pluvial Pre-montano BA(t): "
			+ datosBiomasa[seleccionado][10][0];
	document.getElementById('infoTB11').innerHTML = "Bosque seco Montano-bajo BA(t): "
			+ datosBiomasa[seleccionado][11][0];
	document.getElementById('infoTB12').innerHTML = "Bosque humedo Montano BA(t): "
			+ datosBiomasa[seleccionado][12][0];
	document.getElementById('infoTB13').innerHTML = "Bosque pluvial Montano-bajo BA(t): "
			+ datosBiomasa[seleccionado][13][0];
	document.getElementById('infoTB14').innerHTML = "Bosque pluvial Tropical BA(t): "
			+ datosBiomasa[seleccionado][14][0];

}


function consolidadoGBiomasa(nombresAreasHidro, nombresTiposBosque, datosBiomasa,
		periodo, document) {
	var data = null;
	var options = null;
	var chart = null;
	
	document.getElementById('divGBarras').style.display = 'block';
	document.getElementById('divGTortas').style.display = 'block';
	document.getElementById('divGInformacion').style.display = 'block';
	document.getElementById('divGMapas').style.display = 'block';
	
	var i=0;
	for(i=0;i<2;i++)
	{
	
	// **************Grafica de Barras Biomasa*********************
	data = google.visualization.arrayToDataTable([
			[ 'Periodo', nombresTiposBosque[0], nombresTiposBosque[1],
					nombresTiposBosque[2], nombresTiposBosque[3],
					nombresTiposBosque[4], nombresTiposBosque[5],
					nombresTiposBosque[6], nombresTiposBosque[7],
					nombresTiposBosque[8], nombresTiposBosque[9],
					nombresTiposBosque[10], nombresTiposBosque[11],
					nombresTiposBosque[12], nombresTiposBosque[13],
					nombresTiposBosque[14] ],
			[ periodo, datosBiomasa[0][0],
					datosBiomasa[1][0],
					datosBiomasa[2][0],
					datosBiomasa[3][0],
					datosBiomasa[4][0],
					datosBiomasa[5][0],
					datosBiomasa[6][0],
					datosBiomasa[7][0],
					datosBiomasa[8][0],
					datosBiomasa[9][0],
					datosBiomasa[10][0],
					datosBiomasa[11][0],
					datosBiomasa[12][0],
					datosBiomasa[13][0],
					datosBiomasa[14][0] ] ]);
	options = {
		title : 'Biomasa Consolidado Nacional',
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
			title : 'BA(t)',
			titleTextStyle : {
				color : 'black'
			}
		}
	};

	chart = new google.visualization.BarChart(document
			.getElementById('divGBarras'));
	chart.draw(data, options);

	// ************Grafica Torta Biomasa***************
	data = google.visualization.arrayToDataTable([ [ 'Periodo', 'BA(t)' ],
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
			[ nombresTiposBosque[11], datosBiomasa[11][0] ],
			[ nombresTiposBosque[12], datosBiomasa[12][0] ],
			[ nombresTiposBosque[13], datosBiomasa[13][0] ],
			[ nombresTiposBosque[14], datosBiomasa[14][0] ] ]);

	options = {
		title : 'Biomasa Consolidado Nacional',
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
	document.getElementById('infoTB0').innerHTML = "Bosque muy seco Tropical BA(t): "
			+ datosBiomasa[0][0];
	document.getElementById('infoTB1').innerHTML = "Bosque seco Tropical BA(t): "
			+ datosBiomasa[1][0];
	document.getElementById('infoTB2').innerHTML = "Bosque humedo Tropical BA(t): "
			+ datosBiomasa[2][0];
	document.getElementById('infoTB3').innerHTML = "Bosque humedo Pre-montano BA(t): "
			+ datosBiomasa[3][0];
	document.getElementById('infoTB4').innerHTML = "Bosque muy humedo Pre-montano BA(t): "
			+ datosBiomasa[4][0];
	document.getElementById('infoTB5').innerHTML = "Bosque humedo Montano-bajo BA(t): "
			+ datosBiomasa[5][0];
	document.getElementById('infoTB6').innerHTML = "Bosque muy humedo Montano-bajo BA(t): "
			+ datosBiomasa[6][0];
	document.getElementById('infoTB7').innerHTML = "Bosque seco Pre-montano BA(t): "
			+ datosBiomasa[7][0];
	document.getElementById('infoTB8').innerHTML = "Bosque muy humedo Montano BA(t): "
			+ datosBiomasa[8][0];
	document.getElementById('infoTB9').innerHTML = "Bosque pluvial Montano BA(t): "
			+ datosBiomasa[9][0];
	document.getElementById('infoTB10').innerHTML = "Bosque pluvial Pre-montano BA(t): "
			+ datosBiomasa[10][0];
	document.getElementById('infoTB11').innerHTML = "Bosque seco Montano-bajo BA(t): "
			+ datosBiomasa[11][0];
	document.getElementById('infoTB12').innerHTML = "Bosque humedo Montano BA(t): "
			+ datosBiomasa[12][0];
	document.getElementById('infoTB13').innerHTML = "Bosque pluvial Montano-bajo BA(t): "
			+ datosBiomasa[13][0];
	document.getElementById('infoTB14').innerHTML = "Bosque pluvial Tropical BA(t): "
			+ datosBiomasa[14][0];

	}

}
