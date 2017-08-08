var map;
var untiled;
var tiled;
var pureCoverage = false;
// pink tile avoidance
OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
// make OL compute scale according to WMS spec
OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;

function init(){
    // if this is just a coverage or a group of them, disable a few items,
    // and default to jpeg format
    format = 'image/png';
    if(pureCoverage) {
        document.getElementById('filterType').disabled = true;
        document.getElementById('filter').disabled = true;
        document.getElementById('antialiasSelector').disabled = true;
        document.getElementById('updateFilterButton').disabled = true;
        document.getElementById('resetFilterButton').disabled = true;
        document.getElementById('jpeg').selected = true;
        format = "image/jpeg";
    }

    var bounds = new OpenLayers.Bounds(
        -78.581864, -4.049999,
        -67.338611, 11.288397
    );
    var options = {
        controls: [],
        maxExtent: bounds,
        maxResolution: 0.059915609375,
        projection: "EPSG:4326",
        units: 'degrees'
    };
    map = new OpenLayers.Map('map', options);

	// Define the map layer
	// Here we use a predefined layer that will be kept up to date with URL changes
	//osmLayer = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
	
    /*
	function get_osm_url (bounds) {
		var res = this.map.getResolution();
		var x = Math.round ((bounds.left
			- this.maxExtent.left) 
			/ (res * this.tileSize.w));
		var y = Math.round ((this.maxExtent.top - bounds.top)
			/ (res * this.tileSize.h));
    		var z = this.map.getZoom();
    		var path = z + "/" + x + "/" + y + "." + this.type; 
    		var url = this.url;
    		if (url instanceof Array) {
        			url = this.selectUrl(path, url);
    		}
		return url + path;
	}
	osmLayer = new OpenLayers.Layer.TMS("OpenStreetMap (mapnic)",
			["http://tile.openstreetmap.org/mapnik/"],
			{type:"png", getURL: get_osm_url} );
	*/
    
    var osmLayer = new OpenLayers.Layer.OSM("OpenStreetMap");
    aliasproj = new OpenLayers.Projection("EPSG:4326");
    osmLayer.projection = aliasproj; 
    
    // setup tiled layer
    tiled = new OpenLayers.Layer.WMS(
        "OracleAmazon:PARCELAS_ANTIGUAS - Tiled", "http://54.172.131.5:8080/geoserver/OracleAmazon/wms",
        {
            "LAYERS": 'OracleAmazon:PARCELAS_ANTIGUAS',
            "STYLES": '',
            cql_filter: 'TIPO=\'Permanente\'',
            format: format
        },
        {
            buffer: 0,
            displayOutsideMaxExtent: true,
            isBaseLayer: true,
            yx : {'EPSG:4326' : true}
        } 
    );

    // setup single tiled layer
    untiled = new OpenLayers.Layer.WMS(
        "OracleAmazon:PARCELAS_ANTIGUAS - Untiled", "http://54.172.131.5:8080/geoserver/OracleAmazon/wms",
        {
            "LAYERS": 'OracleAmazon:PARCELAS_ANTIGUAS',
            "STYLES": '',
            cql_filter: 'TIPO=\'Permanente\'',
            format: format
        },
        {
           singleTile: true, 
           ratio: 1, 
           isBaseLayer: true,
           yx : {'EPSG:4326' : true}
        } 
    );

    map.addLayers([osmLayer, untiled, tiled]);

    // build up all controls
    map.addControl(new OpenLayers.Control.PanZoomBar({
        position: new OpenLayers.Pixel(2, 15)
    }));
    map.addControl(new OpenLayers.Control.Navigation());
    map.addControl(new OpenLayers.Control.Scale($('scale')));
    map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
    map.zoomToExtent(bounds);
    
    // wire up the option button
    var options = document.getElementById("options");
    options.onclick = toggleControlPanel;
    
    // support GetFeatureInfo
    map.events.register('click', map, function (e) {
        document.getElementById('nodelist').innerHTML = "Loading... please wait...";
        var params = {
            REQUEST: "GetFeatureInfo",
            EXCEPTIONS: "application/vnd.ogc.se_xml",
            BBOX: map.getExtent().toBBOX(),
            SERVICE: "WMS",
            INFO_FORMAT: 'text/html',
            QUERY_LAYERS: map.layers[0].params.LAYERS,
            FEATURE_COUNT: 50,
            "Layers": 'OracleAmazon:PARCELAS_ANTIGUAS',
            WIDTH: map.size.w,
            HEIGHT: map.size.h,
            format: format,
            styles: map.layers[0].params.STYLES,
            srs: map.layers[0].params.SRS};
        
        // handle the wms 1.3 vs wms 1.1 madness
        if(map.layers[0].params.VERSION == "1.3.0") {
            params.version = "1.3.0";
            params.j = parseInt(e.xy.x);
            params.i = parseInt(e.xy.y);
        } else {
            params.version = "1.1.1";
            params.x = parseInt(e.xy.x);
            params.y = parseInt(e.xy.y);
        }
            
        // merge filters
        if(map.layers[0].params.CQL_FILTER != null) {
            params.cql_filter = map.layers[0].params.CQL_FILTER;
        } 
        if(map.layers[0].params.FILTER != null) {
            params.filter = map.layers[0].params.FILTER;
        }
        if(map.layers[0].params.FEATUREID) {
            params.featureid = map.layers[0].params.FEATUREID;
        }
        OpenLayers.loadURL("http://54.172.131.5:8080/geoserver/OracleAmazon/wms", params, this, setHTML, setHTML);
        OpenLayers.Event.stop(e);
    });
}

// sets the HTML provided into the nodelist element
function setHTML(response){
    document.getElementById('nodelist').innerHTML = response.responseText;
};

// shows/hide the control panel
function toggleControlPanel(event){
    var toolbar = document.getElementById("toolbar");
    if (toolbar.style.display == "none") {
        toolbar.style.display = "block";
    }
    else {
        toolbar.style.display = "none";
    }
    event.stopPropagation();
    map.updateSize()
}

// Tiling mode, can be 'tiled' or 'untiled'
function setTileMode(tilingMode){
    if (tilingMode == 'tiled') {
        untiled.setVisibility(false);
        tiled.setVisibility(true);
        map.setBaseLayer(tiled);
    }
    else {
        untiled.setVisibility(true);
        tiled.setVisibility(false);
        map.setBaseLayer(untiled);
    }
}

// Transition effect, can be null or 'resize'
function setTransitionMode(transitionEffect){
    if (transitionEffect === 'resize') {
        tiled.transitionEffect = transitionEffect;
        untiled.transitionEffect = transitionEffect;
    }
    else {
        tiled.transitionEffect = null;
        untiled.transitionEffect = null;
    }
}

// changes the current tile format
function setImageFormat(mime){
    // we may be switching format on setup
    if(tiled == null)
      return;
      
    tiled.mergeNewParams({
        format: mime
    });
    untiled.mergeNewParams({
        format: mime
    });
    /*
    var paletteSelector = document.getElementById('paletteSelector')
    if (mime == 'image/jpeg') {
        paletteSelector.selectedIndex = 0;
        setPalette('');
        paletteSelector.disabled = true;
    }
    else {
        paletteSelector.disabled = false;
    }
    */
}

// sets the chosen style
function setStyle(style){
    // we may be switching style on setup
    if(tiled == null)
      return;
      
    tiled.mergeNewParams({
        styles: style
    });
    untiled.mergeNewParams({
        styles: style
    });
}

// sets the chosen WMS version
function setWMSVersion(wmsVersion){
    // we may be switching style on setup
    if(wmsVersion == null)
      return;
      
    if(wmsVersion == "1.3.0") {
       origin = map.maxExtent.bottom + ',' + map.maxExtent.left;
    } else {
       origin = map.maxExtent.left + ',' + map.maxExtent.bottom;
    }
      
    tiled.mergeNewParams({
        version: wmsVersion,
        tilesOrigin : origin
    });
    untiled.mergeNewParams({
        version: wmsVersion
    });
}

function setAntialiasMode(mode){
    tiled.mergeNewParams({
        format_options: 'antialias:' + mode
    });
    untiled.mergeNewParams({
        format_options: 'antialias:' + mode
    });
}

function setPalette(mode){
    if (mode == '') {
        tiled.mergeNewParams({
            palette: null
        });
        untiled.mergeNewParams({
            palette: null
        });
    }
    else {
        tiled.mergeNewParams({
            palette: mode
        });
        untiled.mergeNewParams({
            palette: mode
        });
    }
}

function setWidth(size){
    var mapDiv = document.getElementById('map');
    var wrapper = document.getElementById('wrapper');
    
    if (size == "auto") {
        // reset back to the default value
        mapDiv.style.width = null;
        wrapper.style.width = null;
    }
    else {
        mapDiv.style.width = size + "px";
        wrapper.style.width = size + "px";
    }
    // notify OL that we changed the size of the map div
    map.updateSize();
}

function setHeight(size){
    var mapDiv = document.getElementById('map');
    
    if (size == "auto") {
        // reset back to the default value
        mapDiv.style.height = null;
    }
    else {
        mapDiv.style.height = size + "px";
    }
    // notify OL that we changed the size of the map div
    map.updateSize();
}

function updateFilter(){
    if(pureCoverage)
      return;

    var filterType = document.getElementById('filterType').value;
    var filter = document.getElementById('filter').value;
    
    // by default, reset all filters
    var filterParams = {
        filter: null,
        cql_filter: null,
        featureId: null
    };
    if (OpenLayers.String.trim(filter) != "") {
        if (filterType == "cql") 
            filterParams["cql_filter"] = filter;
        if (filterType == "ogc") 
            filterParams["filter"] = filter;
        if (filterType == "fid") 
            filterParams["featureId"] = filter;
    }
    // merge the new filter definitions
    mergeNewParams(filterParams);
}

function resetFilter() {
    if(pureCoverage)
      return;

    document.getElementById('filter').value = "";
    updateFilter();
}

function mergeNewParams(params){
    tiled.mergeNewParams(params);
    untiled.mergeNewParams(params);
}