function ir(destino){

	var dir="";
	
	switch(destino){
		case 0:
			dir = 'inicio.jsp';	
			break;
		case 1:
			dir = 'pages/registrarProyecto.jsp';
			break;
		case 2:
			dir = 'limpiardatossesion?dir=pages/consultarProyecto.jsp';
			break;
		case 3:
			dir = 'pages/modificarProyecto.jsp';
			break;
		case 4:
			dir = 'limpiardatossesion?dir=pages/consultarProyecto.jsp';
			break;
		case 5:
			dir = 'pages/registrarParcela.jsp';
			break;
		case 6:
			dir = 'limpiardatossesion?dir=pages/consultarParcela.jsp';
			break;
		case 7:
			dir = 'pages/modificarParcela.jsp';
			break;
		case 8:
			dir = 'limpiardatossesion?dir=consultarProyectoPublico.jsp';
			break;
		case 9:
			dir = 'limpiardatossesion?dir=consultarParcelaPublica.jsp';
			break;
		case 10:
			dir = 'limpiardatossesion?dir=consultarReporte.jsp';
			break;
		case 11:
			dir = 'limpiardatossesion?dir=pages/ConsultaReportesGenerados.jsp';
			break;
		case 12:
			dir = 'registroUsuario.jsp';
			break;
		case 13:
			dir = 'limpiardatossesion?dir=pages/admin/consultarUsuarios.jsp';
			break;
		case 14:
			dir = 'pages/modificarUsuario.jsp';
			break;
		case 15:
			dir = 'pages/admin/configuracionReportes.jsp';
			break;
		case 16:
			dir = 'recordarClave.jsp';
			break;
		case 17:
			dir = 'limpiardatossesion?dir=consultarBiomasa.jsp';
			break;
		case 18:
			dir = 'pages/registrarBiomasa.jsp';
			break;
		case 19:
			dir = 'pages/modificarUsuario.jsp';
			break;
		case 20:
			dir = 'pages/admin/generarReportesUsuarios.jsp';
			break;
		case 21:
			dir = 'limpiardatossesion?dir=pages/admin/consultarDescargasUsuario.jsp';
			break;
		case 22:
			dir = 'sitemap.html';
			break;
		case 23:
			dir = 'documentacion.jsp';
			break;
		case 24:
			dir = 'pages/admin/adminLicencias.jsp';
			break;
		case 25:
			dir = 'pages/admin/adicionarUsuario.jsp';
			break;
		case 26:
			dir = 'http://trianea.ideam.gov.co/redd-client';
			break;
		case 27:
			dir = 'http://geoapps.ideam.gov.co:8080/geovisor/index.jsf';
			break;
		case 28:
			dir = 'http://bacata.ideam.gov.co/geonetwork/srv/es/main.home';
			break;
		case 29:
			dir = 'http://trianea.ideam.gov.co/erdas-apollo/catalog';
			break;
	}
	var actual = "";
	var base = "";
	if(window.frames[0]==undefined)
		actual = location.href;
	else
		actual = window.frames[0].location.href;		
	base = actual.toString().substring(0,actual.toString().lastIndexOf('/')+1);
	if(actual.match('/pages/admin')){
		base = base.substring(0,base.toString().length-12);
	}else if(actual.match('/pages')){
		base = base.substring(0,base.toString().length-6);
	}
	if(window.frames[0]==undefined)
		location.href =base;
	else
		window.frames[0].location.href =base+dir;
} 

function verVisor(){
	
	var visor = document.getElementById('myWrapper');
	
	if(visor.style.zIndex<0){
		visor.style.zIndex = 9999;
	}else{
		visor.style.zIndex = -999;
	}
}

//	var selObj = document.getElementById('selSeaShells');
//               var i;
//               var count = 0;
//               for (i = 0; i < selObj.options.length; i++) {
//                       if (selObj.options[i].selected) {
//                               selectedArray[count] = selObj.options[i].value;
//                               opcion = document.createElement('option');
//                               texto = document.createTextNode(selObj.options[i].value);
//                               opcion.appendChild(texto);
//                               txtSelectedValuesObj.appendChild(opcion);
//
//                               count++;
//                       }
//               }