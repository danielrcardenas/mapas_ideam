var opt;
var strUser;
var contactos = new Array();
var isBusqueda = 0;

$(function() {
	$('#popup-registrarFGDA').modalPopLite({
		openButton : '#regFGDA',
		closeButton : '#canRegCon, #regCon',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarFGDA').modalPopLite({
		openButton : '#busFGDA',
		closeButton : '#canBusCon, #okBusCon',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarPro').modalPopLite({
		openButton : '#regContactoPro',
		closeButton : '#canRegPro, #regConPro',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarPro').modalPopLite({
		openButton : '#busContactoPro',
		closeButton : '#canBusPro,#okBusPro',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarCustodio').modalPopLite({
		openButton : '#regCustodio',
		closeButton : '#canRegCus, #regConCus',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarCustodio').modalPopLite({
		openButton : '#busCustodio',
		closeButton : '#canBusCus,#okBusCus',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarInv').modalPopLite({
		openButton : '#regInv',
		closeButton : '#canRegInv, #regConInv',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarInv').modalPopLite({
		openButton : '#busInv',
		closeButton : '#canBusConInv, #okBusConInv',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarCol').modalPopLite({
		openButton : '#regCol',
		closeButton : '#canRegCol, #regConCol',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarCol').modalPopLite({
		openButton : '#busCol',
		closeButton : '#canBusConCol, #okBusConCol',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarEnc').modalPopLite({
		openButton : '#regEnc',
		closeButton : '#canRegEnc, #regConEnc',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarEnc').modalPopLite({
		openButton : '#busEnc',
		closeButton : '#canBusConEnc, #okBusConEnc',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarBri').modalPopLite({
		openButton : '#regBri',
		closeButton : '#canRegBri, #regConBri',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarBri').modalPopLite({
		openButton : '#busBri',
		closeButton : '#canBusConBri, #okBusConBri',
		isModal : true
	});
});

$(function() {
	$('#popup-registrarSup').modalPopLite({
		openButton : '#regSup',
		closeButton : '#canRegSup, #regConSup',
		isModal : true
	});
});

$(function() {
	$('#popup-buscarSup').modalPopLite({
		openButton : '#busSup',
		closeButton : '#canBusConSup, #okBusConSup',
		isModal : true
	});
});

$(document).ready(function() {
	new JsDatePick({
		useMode : 2,
		target : "festablecimiento",
		dateFormat : "%d/%m/%Y"

	});
});

$(function() {
	$('#popup-file').modalPopLite({
		openButton : '#si',
		closeButton : '#close-btn, ',
		isModal : true
	});
});


function mostrarFormulario(control, organizacion, persona, misc) {
	isBusqueda = 0;
	if (control.value == 0) {
		document.getElementById(organizacion).style.display = 'none';
		document.getElementById(persona).style.display = 'none';
		document.getElementById(misc).style.display = 'none';
	} else if (control.value == 1) {
		document.getElementById(organizacion).style.display = 'none';
		document.getElementById(persona).style.display = 'block';
		document.getElementById(misc).style.display = 'block';
	} else {
		document.getElementById(organizacion).style.display = 'block';
		document.getElementById(persona).style.display = 'none';
		document.getElementById(misc).style.display = 'block';
	}

}

function resultadosContactos(principal, divCont, tipo) {
	isBusqueda = 0;
	limpiarChecks();
	var cont, contenedor, i;
	cont = document.getElementById(principal);
	contenedor = document.getElementById(divCont);
	var e = document.getElementById(tipo);
	strUser = e.options[e.selectedIndex].value;

	if (contenedor != null){
		cont.removeChild(contenedor);
		contenedor.innerHTML="";
	}

	if (strUser == 1) {
		contenedor = document.createElement('div');
		contenedor.id = divCont;
		for (i = 0; i < contacts.length; i++) {

			conte = contacts[i].split(",");
			var erInput = document.createElement('INPUT');
			erInput.setAttribute("type", "checkbox");
			erInput.setAttribute("value", i);
			erInput.setAttribute("id", i);
			contenedor.appendChild(erInput);
			ele = document.createElement('label');
			ele.innerHTML = conte[0];
			contenedor.appendChild(ele);
			ele = document.createElement('label');
			ele.innerHTML = conte[1];
			contenedor.appendChild(ele);
			ele = document.createElement('label');
			ele.innerHTML = conte[2];
			br = document.createElement('br');
			contenedor.appendChild(ele);
			contenedor.appendChild(br);
		}
	} else if (strUser == 2) {
		contenedor = document.createElement('div');
		contenedor.id = divCont;
		for (i = 0; i < orgs.length; i++) {
			var erInput = document.createElement('INPUT');
			erInput.setAttribute("type", "checkbox");
			erInput.setAttribute("value", i);
			erInput.setAttribute("id", i);
			conte = orgs[i].split(",");
			contenedor.appendChild(erInput);
			ele = document.createElement('label');
			ele.innerHTML = conte[0];
			contenedor.appendChild(ele);
			ele = document.createElement('label');
			ele.innerHTML = conte[1];
			contenedor.appendChild(ele);
			ele = document.createElement('label');
			ele.innerHTML = conte[2];
			br = document.createElement('br');
			contenedor.appendChild(ele);
			contenedor.appendChild(br);
		}
	}
	if (contenedor.innerHTML!="")
		cont.appendChild(contenedor);
}

function seleccionar(divIdContacto, divCont, cont) {
	var i;
	var c;
	for (i = 0; i < document.getElementById('formparcela').elements.length; i++) {
		if ((document.getElementById('formparcela')[i].type == 'checkbox')) {
			if (document.getElementById('formparcela')[i].checked) {
				document.getElementById(divCont).style.display = 'block';
				if (strUser == 1) {
					if (isBusqueda==0)
						c = contacts[document.getElementById('formparcela')[i].id]
								.split(",");
					else
						c = contactos[document.getElementById('formparcela')[i].id]
								.split(",");
					document.getElementById(divIdContacto).value = c[0];
					document.getElementById('nom' + cont).value = c[1];
					document.getElementById('tel' + cont).value = c[2];
					document.getElementById('cor' + cont).value = c[3];
				} else {
					if (isBusqueda==0)
						c = orgs[document.getElementById('formparcela')[i].id]
								.split(",");
					else
						c = contactos[document.getElementById('formparcela')[i].id]
								.split(",");
					document.getElementById(divIdContacto).value = c[0];
					document.getElementById('nom' + cont).value = c[1];
					document.getElementById('tel' + cont).value = c[2];
					document.getElementById('cor' + cont).value = c[3];
				}
			}
		}
	}
}

function registrarDatos(nomOrg, telOrg, nomCon, telCor, correo, div, opc, cont) {
	var e = document.getElementById(opc);
	var strUser = e.options[e.selectedIndex].value;
	document.getElementById(div).style.display = 'block';
	if (strUser == 1) {
		document.getElementById('nom' + cont).value = document
				.getElementById(nomCon).value;
		document.getElementById('tel' + cont).value = document
				.getElementById(telCor).value;
	} else {
		document.getElementById('nom' + cont).value = document
				.getElementById(nomOrg).value;
		document.getElementById('tel' + cont).value = document
				.getElementById(telOrg).value;
	}
	document.getElementById('cor' + cont).value = document
			.getElementById(correo).value;
}

function buscarContacto(nom, cor, divRes, tipoCont, divCont) {
	var i, cont = 0;
	var contacto;
	contactos = [];
	var nombre = document.getElementById(nom).value;
	var correo = document.getElementById(cor).value;
	var e = document.getElementById(tipoCont);
	var strUser = e.options[e.selectedIndex].value;
	if (nombre != "") {
		var n = '.[' + nombre + '].';
		if (correo != "") {
			if (strUser == 1) {
				for (i = 0; i < contacts.length; i++) {
					contacto = contacts[i].split(",");
					if ((contacto[1].search(n) != -1)
							&& (contacto[2].search(correo) != -1)) {
						contactos[cont] = contacts[i];
						cont++;
					}
				}
			} else {
				for (i = 0; i < orgs.length; i++) {
					contacto = orgs[i].split(",");
					if ((contacto[1].search(n) != -1)
							&& (contacto[2].search(correo) != -1)) {
						contactos[cont] = orgs[i];
						cont++;
					}
				}
			}
		} else {
			if (strUser == 1) {
				for (i = 0; i < contacts.length; i++) {
					contacto = contacts[i].split(",");
					if (contacto[1].search(n) != -1) {
						contactos[cont] = contacts[i];
						cont++;
					}
				}
			} else {
				for (i = 0; i < orgs.length; i++) {
					contacto = orgs[i].split(",");
					if (contacto[1].search(n) != -1) {
						contactos[cont] = orgs[i];
						cont++;
					}
				}
			}
		}
	} else {
		if (strUser == 1) {
			for (i = 0; i < contacts.length; i++) {
				contacto = contacts[i].split(",");
				if (contacto[2].search(correo) != -1) {
					contactos[cont] = contacts[i];
					cont++;
				}
			}
		} else {
			for (i = 0; i < orgs.length; i++) {
				contacto = orgs[i].split(",");
				if (contacto[2].search(correo) != -1) {
					contactos[cont] = orgs[i];
					cont++;
				}
			}
		}
	}
	resBusqueda(contactos, divRes, divCont);
}

function resBusqueda(contacts, divRes, divCont) {
	var i;
	var cont = document.getElementById(divRes);
	var contenedor = document.getElementById(divCont);
	isBusqueda = 1;
	if (contenedor != null)
		cont.removeChild(contenedor);
	contenedor = document.createElement('div');
	contenedor.id = divCont;
	for (i = 0; i < contacts.length; i++) {
		conte = contacts[i].split(",");
		var erInput = document.createElement('INPUT');
		erInput.setAttribute("type", "checkbox");
		erInput.setAttribute("value", i);
		erInput.setAttribute("id", i);
		contenedor.appendChild(erInput);
		ele = document.createElement('label');
		ele.innerHTML = conte[0];
		contenedor.appendChild(ele);
		ele = document.createElement('label');
		ele.innerHTML = conte[1];
		contenedor.appendChild(ele);
		ele = document.createElement('label');
		ele.innerHTML = conte[2];
		br = document.createElement('br');
		contenedor.appendChild(ele);
		contenedor.appendChild(br);
	}
	cont.appendChild(contenedor);
}

function limpiarChecks(){
	var i;
	for (i = 0; i < document.getElementById('formparcela').elements.length; i++) {
		if ((document.getElementById('formparcela')[i].type == 'checkbox')) {
			if (document.getElementById('formparcela')[i].checked) {
				document.getElementById('formparcela')[i].checked=0;
			}
		}
	}
}


var inv = false;
var colEnc = false;
var bri = false;
var sup = false;
function agregaContacto(control) {

	if (control.value == 4) {
		if (!inv) {
			document.getElementById('Inv').style = 'block';
			inv = true;
			document.getElementById('isInvestigador').value = 1;
		}
	} else if (control.value == 5 || control.value == 6) {
		if (!colEnc) {
			document.getElementById('ColEnc').style = 'block';
			colEnc = true;
			document.getElementById('isColeccion').value = 1;
		}
	} else if (control.value == 7) {
		if (!bri) {
			document.getElementById('Bri').style = 'block';
			bri = true;
			document.getElementById('isBrigadista').value = 1;
		}
	} else if (control.value == 8) {
		if (!sup) {
			document.getElementById('Sup').style = 'block';
			sup = true;
			document.getElementById('isSupervisor').value = 1;
		}
	}
}

function cargaArchivo(control, carga) {
	if (control.value == 1) {
		document.getElementById(carga).disabled = false;
	} else {
		document.getElementById(carga).disabled = true;
	}
}

function limpiarFormulario(formulario) {
	var i;
	for (i = 0; i < document.getElementById(formulario).elements.length; i++) {
		if ((document.getElementById(formulario)[i].type == 'checkbox')) {
			if (document.getElementById(formulario)[i].options[indice].value == 0) {
				document.getElementById(combo).selectedIndex = indice;
			}
		} else if ((document.getElementById(formulario)[i].type == 'text')) {
			document.getElementById(formulario)[i].value = '';
		} else if ((document.getElementById(formulario)[i].type == 'textarea')) {
			document.getElementById(formulario)[i].value = '';
		} else if ((document.getElementById(formulario)[i].type == 'file')) {
			document.getElementById(formulario)[i].value = '';
		} else if ((document.getElementById(formulario)[i].type == 'select-one')) {
			if (document.getElementById(formulario)[i].selectedIndex != 0) {
				document.getElementById(formulario)[i].selectedIndex = 0;
			}
		}
	}
	document.getElementById('isInvestigador').value="";	
	document.getElementById('isColeccion').value="";
	document.getElementById('isBrigadista').value="";
	document.getElementById('isSupervisor').value="";
}

function ocultarContacto(divContacto) {
	if (document.getElementById(divContacto).style.display == 'block') {
		document.getElementById(divContacto).style.display = 'none';
	}
}

function obtenerNombreArchivo() {
	document.getElementById('datosArchivo').style.display = 'block';
	document.getElementById('nomArchivo').value = document
			.getElementById('archivo').value;
}
