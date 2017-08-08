var opc;
var datos;
var map;
function init() {

	// Create map
	map = new ERDAS.maps.Map("myWorldMap");
	var st = new ERDAS.services.ServiceTool();
	st.withWMSService(ERDAS.misc.conf("service.wcs.world.url"), {
		success : function(wmsService) {

			var ld = wmsService.createLayerDescriptor("worldelevation");
			map.addLayerDescriptor(ld);

			var ld2 = wmsService.createLayerDescriptor("worldelevation", {
				styling : "color"
			});
			ld2.setOpacity(0.65);
			map.addLayerDescriptor(ld2);
		},
		failure : function() {

			alert("Se se pudo conectar con el servicio WMS");
		}
	});

	// Create navigation bar
	var navBar = new ERDAS.ui.navigation.NavBar("myNavBar", map);

	navBar.createDefaultButtons();

	//Create info bar
	var navInfo = new ERDAS.ui.navigation.NavInfo("navInfo", map);
	navInfo.createDefaultFields();
	new ERDAS.ui.navigation.SRSSelector(navInfo);
	new ERDAS.ui.navigation.MapDimensions(navInfo);
	dibujar();
	
}

function dibujar(){
	var d = datos.split(",");
	switch(opc){
	case '1':
		var point = new ERDAS.geom.Point (paserFloat(d[0]),paserFloat(d[1]), 4326);
		map.showGeometry(point);
	break;
	case '2':
		var lineString = new ERDAS.geom.LineString (d, 4326);
		map.showGeometry(lineString);
	break;
	case '3':
		var ring = new ERDAS.geom.LinearRing (d, 4326);
		var polygon = new ERDAS.geom.Polygon (epsgId);
		polygon.pushChild (ring);
		map.showGeometry(polygon);
	break;	
	}
	
}

function opcion(seleccion){
	opc=seleccion;
}

function valores(val){
	datos=val;
	
} 
