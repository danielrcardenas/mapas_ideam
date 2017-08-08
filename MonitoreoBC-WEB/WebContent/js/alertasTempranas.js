// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
function irAReportesAnteriores(document){
	document.getElementById('divSeleccionReporte').style.display='none';
	document.getElementById('divReportesAnteriores').style.display='block';
}

function menuAlertas(document){
	document.getElementById('divSeleccionReporte').style.display='block';
	document.getElementById('divReportesAnteriores').style.display='none';
	document.getElementById('divReporteActual').style.display='none';
}

function irAReportesActual(document){
	document.getElementById('divSeleccionReporte').style.display='none';
	document.getElementById('divReporteActual').style.display='block';
}

function seleccionarTab(idTab,boton){
	document.getElementById('divGeneralidades').style.display='none';
	document.getElementById('btnGeneralidades').style.border='hidden';
	document.getElementById('divDepartamentos').style.display='none';
	document.getElementById('btnDepartamentos').style.border='hidden';
	document.getElementById('divRegionesNaturales').style.display='none';
	document.getElementById('btnRegionesNaturales').style.border='hidden';
	document.getElementById('divAutoridades').style.display='none';
	document.getElementById('btnAutoridades').style.border='hidden';
	document.getElementById('divPersistencia').style.display='none';
	document.getElementById('btnPersistencia').style.border='hidden';
	document.getElementById('divConcentracion').style.display='none';
	document.getElementById('btnConcentracion').style.border='hidden';
	document.getElementById('divNucleos').style.display='none';
	document.getElementById('btnNucleos').style.border='hidden';
	
	document.getElementById(idTab).style.display='block';
	boton.style.border='solid';
}