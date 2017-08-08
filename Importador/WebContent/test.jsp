<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> <!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd' >
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<title>
Visor de Parcelas..
</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />

<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type='text/javascript' src='js/auxiliares.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>

<script type='text/javascript'>

$(document).ready(function(){
	
});
</script>

</head>

<body>


<div id="map" style="width: 100%; height: 400px"></div>

<!--<script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>-->

<script type="text/javascript" src="js/leaflet.js"></script>

<script type="text/javascript">var map = L.map('map').setView([5, -73], 5);

var myAtribucion = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
'Imagery © <a href="http://mapbox.com">Mapbox</a>, ' +
'Parcelas/Plots © <a href="http://www.ideam.gov.co">IDEAM</a> y/o Colaboradores'; 

L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
	attribution: myAtribucion,
	id: 'examples.map-i875mjb7'
}).addTo(map);


map.doubleClickZoom.disable();

</script>
   
</body>
</html>