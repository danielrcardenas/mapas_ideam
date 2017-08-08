<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="co.gov.ideamredd.admif.Auxiliar" %>
<%
//co.gov.ideamredd.admif.Auxiliar aux = new co.gov.ideamredd.admif.Auxiliar();
LenguajeI18N L = new LenguajeI18N();
ResourceBundle msj = null;
String yo = "busqueda_parcelas.";
String idioma = Auxiliar.nz(request.getParameter("idioma"), "es");

if (idioma.equals("es")) {
	L.setLenguaje("es");
	L.setPais("CO");
}
else {
	L.setLenguaje("en");
	L.setPais("US");
}
msj = L.obtenerMensajeIdioma();
%>
<%
String ua=request.getHeader("User-Agent").toLowerCase();
boolean es_movil = ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-");
String doctype = "";
String estilo = "estilos.css";

if(es_movil) { 
	doctype = " <!DOCTYPE html PUBLIC '-//WAPFORUM//DTD XHTML Mobile 1.0//EN' 'http://www.wapforum.org/DTD/xhtml-mobile10.dtd' >"; 
	estilo = "estilos_movil.css";
} 
else {
	doctype = " <!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd' >";
	estilo = "estilos.css";
} 
out.print(doctype); 
%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<title>
<% try { out.print(msj.getString(yo+"Visor_Parcelas")); } catch (MissingResourceException e) { out.print("Visor de Parcelas" + ".."); } %>
</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

      <style type="text/css">
            /* Toolbar styles */
            #toolbar {
                position: relative;
                padding-bottom: 0.5em;
                display: none;
            }
            
            #toolbar ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            
            #toolbar ul li {
                float: left;
                padding-right: 1em;
                padding-bottom: 0.5em;
            }
            
            #toolbar ul li a {
                font-weight: bold;
                font-size: smaller;
                vertical-align: middle;
                color: black;
                text-decoration: none;
            }

            #toolbar ul li a:hover {
                text-decoration: underline;
            }
            
            #toolbar ul li * {
                vertical-align: middle;
            }

            /* The map and the location bar */
            #map {
                clear: both;
                position: relative;
                width: 100%;
                height: 512px;
                border: 1px solid #ccc;
            }
            
            #wrapper {
                width: 377px;
            }
            
            #location {
                float: right;
            }
            
            #options {
                position: absolute;
                left: 13px;
                top: 7px;
                z-index: 3000;
            }

            /* Styles used by the default GetFeatureInfo output, added to make IE happy */
            table.featureInfo, table.featureInfo td, table.featureInfo th {
                border: 1px solid #ddd;
                border-collapse: collapse;
                margin: 0;
                padding: 0;
                font-size: 90%;
                padding: .2em .1em;
            }
            
            table.featureInfo th {
                padding: .2em .2em;
                font-weight: bold;
                background: #eee;
            }
            
            table.featureInfo td {
                background: #fff;
            }
            
            table.featureInfo tr.odd td {
                background: #eee;
            }
            
            table.featureInfo caption {
                text-align: left;
                font-size: 100%;
                font-weight: bold;
                padding: .2em .2em;
            }
        </style>

<!-- 
<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
-->

<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type='text/javascript' src='js/ajaxLoader.js'></script>
<!--<script type='text/javascript' src='js/OpenLayers3.js'></script>-->
<!--<script src="http://54.172.131.5:8080/geoserver/openlayers/OpenLayers.js" type="text/javascript"></script>-->
<!--<script src="http://54.172.131.5:8080/geoserver/openlayers/OpenLayers3.js" type="text/javascript"></script>-->
<script src="http://www.openlayers.org/api/OpenLayers.js"></script>
<!--<script src="http://www.openstreetmap.org/openlayers/OpenStreetMap.js"></script>-->
<!--<script src='http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6.1'></script>-->

<script type="text/javascript">
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
        //format = "image/jpeg";
        //format = "image/png";
        format = "image/gif";
    }

    var bounds = new OpenLayers.Bounds(
        -78.584364, -4.0505,
        -67.338111, 11.293397
    );
    var options = {
        controls: [],
        maxExtent: bounds,
        maxResolution: 0.05993709765625,
        //projection: "EPSG:4326",
        units: 'degrees'
    };
    map = new OpenLayers.Map('map', options);

    alert('Versión de OpenLayers:' + OpenLayers.VERSION_NUMBER);
    
    //var osmLayer = new OpenLayers.Layer.OSM("OpenStreetMap");
    /*
    aliasproj = new OpenLayers.Projection("EPSG:4326");
    osmLayer.projection = aliasproj;
    */

    //var osmLayer = new OpenLayers.Layer.OSM();

	var str_viewparams = "";
    
	var PRCL_NOMBRE = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_NOMBRE"),"") %>';
	var w_PRCL_NOMBRE = '';
	if (PRCL_NOMBRE.length > 0) {
		//w_PRCL_NOMBRE = " AND LOWER(PRCL_NOMBRE) LIKE '%25"+PRCL_NOMBRE+"%25' ";
		w_PRCL_NOMBRE = " AND LOWER(PRCL_NOMBRE) LIKE '%"+PRCL_NOMBRE+"%' ";
		str_viewparams += 'w_PRCL_NOMBRE:'+w_PRCL_NOMBRE+';'; 
	}

	var PRCL_CONSECUTIVO = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_CONSECUTIVO"),"") %>';
	var w_PRCL_CONSECUTIVO = '';
	if (PRCL_CONSECUTIVO.length > 0) {
		w_PRCL_CONSECUTIVO = " AND PRCL_CONSECUTIVO IN ("+PRCL_CONSECUTIVO+") ";
		str_viewparams += 'w_PRCL_CONSECUTIVO:'+w_PRCL_CONSECUTIVO+';'; 
	}
	
	var PRCL_CONS_PAIS = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_CONS_PAIS"),"") %>';
	var w_PRCL_CONS_PAIS = '';
	if (PRCL_CONS_PAIS.length > 0) {
		w_PRCL_CONS_PAIS = " AND PRCL_CONS_PAIS IN ("+PRCL_CONS_PAIS+") ";
		str_viewparams += 'w_PRCL_CONS_PAIS:'+w_PRCL_CONS_PAIS+';'; 
	}

	var departamentos_seleccionados = '<%= Auxiliar.nzObjStr(request.getParameter("departamentos_seleccionados"),"") %>';
	var w_departamentos_seleccionados = '';
	if (departamentos_seleccionados.length > 0) {
		w_departamentos_seleccionados = " AND PRCL_CONSECUTIVO IN (SELECT DPPR_CONS_PARCELA FROM RED_DEPTO_PARCELA WHERE DPPR_CONS_DEPTO IN (" + departamentos_seleccionados + ")) ";
		str_viewparams += 'w_departamentos_seleccionados:'+w_departamentos_seleccionados+';'; 
	}

	var municipios_seleccionados = '<%= Auxiliar.nzObjStr(request.getParameter("municipios_seleccionados"),"") %>';
	var w_municipios_seleccionados = '';
	if (municipios_seleccionados.length > 0) {
		w_municipios_seleccionados = " AND PRCL_CONSECUTIVO IN (SELECT MNPR_PARCELA FROM RED_MUNICIPIO_PARCELA WHERE MNPR_MUNICIPIO IN (" + municipios_seleccionados + ")) ";
		str_viewparams += 'w_municipios_seleccionados:'+w_municipios_seleccionados+';'; 
	}

	var PRCL_FECHAESTABPARCE = '<%= Auxiliar.nzObjStr(request.getParameter("PRCL_FECHAESTABPARCE"),"") %>';
	var w_PRCL_FECHAESTABPARCE = '';
	if (PRCL_FECHAESTABPARCE.length > 0) {
		w_PRCL_FECHAESTABPARCE = " AND PRCL_FECHAESTABPARCE >= TO_DATE('"+PRCL_FECHAESTABPARCE+"', 'YYYY-MM-DD HH24:MI:SS') ";
		str_viewparams += 'w_PRCL_FECHAESTABPARCE:'+w_PRCL_FECHAESTABPARCE+';'; 
	}

	if (str_viewparams.length>0) {
		str_viewparams = str_viewparams.slice(0,-1);
		document.getElementById('viewparams').innerHTML = 'Filtro actual:['+str_viewparams+']';
	}
	
    // setup tiled layer
    tiled = new OpenLayers.Layer.WMS(
        "OracleAmazon:C_RED_PARCELA_PARAMETRIZADA - Tiled", "http://54.172.131.5:8080/geoserver/OracleAmazon/wms",
        {
            "LAYERS": 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',
            "STYLES": '',
            viewparams: str_viewparams, 
            transparent: 'true',
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
        "OracleAmazon:C_RED_PARCELA_PARAMETRIZADA - Untiled", "http://54.172.131.5:8080/geoserver/OracleAmazon/wms",
        {
            "LAYERS": 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',
            "STYLES": '',
            viewparams: str_viewparams, 
            transparent: 'true',
            format: format
        },
        {
           singleTile: true, 
           ratio: 1, 
           isBaseLayer: true,
           yx : {'EPSG:4326' : true}
        } 
    );

    //map.addLayers([osmLayer, untiled, tiled]);
    //map.addLayers([tiled, osmLayer]);
    //map.addLayers([untiled, osmLayer]);
    //map.addLayers([untiled, tiled]);
    //map.addLayers([osmLayer]);
    
    //map.addLayer(new OpenLayers.Layer.OSM());
    map.addLayer(tiled);

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
            "Layers": 'OracleAmazon:C_RED_PARCELA_PARAMETRIZADA',
            WIDTH: map.size.w,
            HEIGHT: map.size.h,
            viewparams: str_viewparams, 
            format: format,
            styles: map.layers[0].params.STYLES,
            srs: map.layers[0].params.SRS
           };
        
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
</script>

</head>
<body onload="init()">


<div>
     <div id="toolbar" style="display: none;">
            <ul>
                <li>
                    <a>WMS version:</a>
                    <select id="wmsVersionSelector" onchange="setWMSVersion(value)">
                        <option value="1.1.1">1.1.1</option>
                        <option value="1.3.0">1.3.0</option>
                    </select>
                </li>
                <li>
                    <a>Tiling:</a>
                    <select id="tilingModeSelector" onchange="setTileMode(value)">
                        <option value="untiled">Single tile</option>
                        <option value="tiled">Tiled</option>
                    </select>
                </li>
                <li>
                    <a>Transition effect:</a>
                    <select id="transitionEffectSelector" onchange="setTransitionMode(value)">
                        <option value="">None</option>
                        <option value="resize">Resize</option>
                    </select>
                </li>
                <li>
                    <a>Antialias:</a>
                    <select id="antialiasSelector" onchange="setAntialiasMode(value)">
                        <option value="full">Full</option>
                        <option value="text">Text only</option>
                        <option value="none">Disabled</option>
                    </select>
                </li>
                <li>
                    <a>Format:</a>
                    <select id="imageFormatSelector" onchange="setImageFormat(value)">
                        <option value="image/png">PNG 24bit</option>
                        <option value="image/png8">PNG 8bit</option>
                        <option value="image/gif">GIF</option>
                        <option id="jpeg" value="image/jpeg">JPEG</option>
                    </select>
                </li>
                <li>
                    <a>Styles:</a>
                    <select id="imageFormatSelector" onchange="setStyle(value)">
                        <option value="">Default</option>
                    </select>
                </li>
                <!-- Commented out for the moment, some code needs to be extended in 
                     order to list the available palettes
                <li>
                    <a>Palette:</a>
                    <select id="paletteSelector" onchange="setPalette(value)">
                        <option value="">None</option>
                        <option value="safe">Web safe</option>
                    </select>
                </li>
                -->
                <li>
                    <a>Width/Height:</a>
                    <select id="widthSelector" onchange="setWidth(value)">
                        <!--
                        These values come from a statistics of the viewable area given a certain screen area
                        (but have been adapted a litte, simplified numbers, added some resolutions for wide screen)
                        You can find them here: http://www.evolt.org/article/Real_World_Browser_Size_Stats_Part_II/20/2297/
                        --><option value="auto">Auto</option>
                        <option value="600">600</option>
                        <option value="750">750</option>
                        <option value="950">950</option>
                        <option value="1000">1000</option>
                        <option value="1200">1200</option>
                        <option value="1400">1400</option>
                        <option value="1600">1600</option>
                        <option value="1900">1900</option>
                    </select>
                    <select id="heigthSelector" onchange="setHeight(value)">
                        <option value="auto">Auto</option>
                        <option value="300">300</option>
                        <option value="400">400</option>
                        <option value="500">500</option>
                        <option value="600">600</option>
                        <option value="700">700</option>
                        <option value="800">800</option>
                        <option value="900">900</option>
                        <option value="1000">1000</option>
                    </select>
                </li>
                <li>
                    <a>Filter:</a>
                    <select id="filterType">
                        <option value="cql">CQL</option>
                        <option value="ogc">OGC</option>
                        <option value="fid">FeatureID</option>
                    </select>
                    <input type="text" size="80" id="filter"/>
                    <img id="updateFilterButton" src="http://54.172.131.5:8080/geoserver/openlayers/img/east-mini.png" onClick="updateFilter()" title="Apply filter"/>
                    <img id="resetFilterButton" src="http://54.172.131.5:8080/geoserver/openlayers/img/cancel.png" onClick="resetFilter()" title="Reset filter"/>
                </li>
            </ul>
        </div>

<div id="map"><img id="options" title="Toggle options toolbar" src="http://54.172.131.5:8080/geoserver/options.png"/></div>

<div id="wrapper">
    <div id="location">Ubicación</div>
    <div id="scale">
    </div>
</div>
<div id="nodelist">
    <em>Clic en el mapa para más información</em>
</div>
<div id="viewparams" style="color: #999; width: 100%; height: 40px; border: 1px solid white;"></div>
        

</div>

</body>
</html>