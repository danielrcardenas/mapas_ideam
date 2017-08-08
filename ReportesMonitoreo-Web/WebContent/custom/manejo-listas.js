function moverElementoSeleccionado(listaInicial, listaFinal) {
	var i;
	for (i = 0; i < listaInicial.options.length; i++) {
		if (listaInicial.options[i].selected) {
			opcion = document.createElement('option');
			texto = document.createTextNode(listaInicial.options[i].text);
			opcion.appendChild(texto);
			opcion.value = listaInicial.options[i].value;
			listaFinal.appendChild(opcion);
			listaInicial.removeChild(listaInicial.options[i]);
		}
	}
}

function moverTodosElementos(listaInicial, listaFinal) {
	var i = 0;
	while (i < listaInicial.options.length) {
		opcion = document.createElement('option');
		texto = document.createTextNode(listaInicial.options[i].text);
		opcion.appendChild(texto);
		opcion.value = listaInicial.options[i].value;
		listaFinal.appendChild(opcion);
		listaInicial.removeChild(listaInicial.options[i]);
	}
}
function completarLista(listaInicial, listaFinal) {
	var i, j, k;
	municipios = cargarMunicipios();
	for (i = 0; i < listaInicial.options.length; i++) {
		for (j = 0; j < municipios.length; j++) {
			var m = municipios[j].split(",");
			if (m[2] < 10)
				m[2] = '0' + m[2];
			if (m[0].length < 5)
				var d = m[0].substring(0, 1);
			else
				var d = m[0].substring(0, 2);
			if (d == listaInicial.options[i].value) {
				tmp = listaFinal.options.length;
				existe = false;
				if (tmp != 0) {
					for (k = 0; k < tmp; k++) {
						if (m[0] == listaFinal.options[k].value) {
							existe = true;
						}
					}
					if (!existe) {
						opcion = document.createElement('option');
						texto = document.createTextNode(m[1]);
						opcion.appendChild(texto);
						opcion.value = m[0];
						listaFinal.appendChild(opcion);
					}
				} else {
					opcion = document.createElement('option');
					texto = document.createTextNode(m[1]);
					opcion.appendChild(texto);
					opcion.value = m[0];
					listaFinal.appendChild(opcion);
				}
			}
		}
	}
}

function completarListaCar(listaInicial, listaFinal) {
	var i, j, k, l;
	cd = cargarCarDepto();
	car = cargarCar();
	for (i = 0; i < listaInicial.options.length; i++) {
		for (j = 0; j < cd.length; j++) {
			var m = cd[j].split(",");
			for (k = 0; k < car.length; k++) {
				var c = car[k].split(",");
				if (m[1] == c[0]) {
					m[3] = c[1];
				}
			}
			if (m[2] == listaInicial.options[i].value) {
				tmp = listaFinal.options.length;
				existe = false;
				if (tmp != 0) {
					for (l = 0; l < tmp; l++) {
						if (m[1] == listaFinal.options[l].value)
							existe = true;
					}
					if (!existe) {
						opcion = document.createElement('option');
						texto = document.createTextNode(m[3]);
						opcion.appendChild(texto);
						opcion.value = m[1];
						listaFinal.appendChild(opcion);
					}
				} else {
					opcion = document.createElement('option');
					texto = document.createTextNode(m[3]);
					opcion.appendChild(texto);
					opcion.value = m[1];
					listaFinal.appendChild(opcion);
				}
			}
		}
	}
}

function eliminarElementoLista(listaInicial, listaFinal1, listaFinal2) {
	var i, j = 0, k = 0;
	for (i = 0; i < listaInicial.options.length; i++) {
		if (listaInicial.options[i].selected) {
			while (j != listaFinal1.options.length) {
				if (listaFinal1.options[j].value.substring(0, 2) == listaInicial.options[i].value) {
					listaFinal1.removeChild(listaFinal1.options[j]);
				} else if ('0' + listaFinal1.options[j].value.substring(0, 1) == listaInicial.options[i].value) {
					listaFinal1.removeChild(listaFinal1.options[j]);
				} else
					j++;
			}
			while (k != listaFinal2.options.length) {
				if (listaFinal2.options[k].value.substring(0, 2) == listaInicial.options[i].value) {
					listaFinal2.removeChild(listaFinal2.options[k]);
				} else if ('0' + listaFinal2.options[j].value.substring(1, 2) == listaInicial.options[i].value) {
					listaFinal2.removeChild(listaFinal2.options[j]);
				} else
					k++;
			}
		}
	}
}

function eliminarlista(lista) {
	var i = 0;
	while (i < lista.options.length) {
		lista.removeChild(lista.options[i]);
	}
}
function validarPeriodo(periodos, per){
	var i=0;
	var ingresar=false;
	if(periodos.length>0)
		while(i<periodos.length) {
			if(per==periodos[i]){				
				ingresar=false;
				break;
			}else
				ingresar=true;
			i++;
		}
	else
		ingresar=true;
	return ingresar;
}

function completarListaPeriodos(lista1, lista2, listaFinal) {
	var i, j, k, index=0;
	var periodo = new Array();
	eliminarlista(listaFinal);
	periodos = cargarPeriodos();
	for (i = 0; i < periodos.length; i++) {
		var p = periodos[i].split(",");
		if (p[1] == lista1.value && (p[2] == lista2.value || lista2.value == 4)) {
			if(validarPeriodo(periodo,p[3])){
				opcion = document.createElement('option');
				texto = document.createTextNode(p[3]);
				opcion.appendChild(texto);
				opcion.value = p[0] + '-' + p[3];
				periodo[index]= p[3];
				index++;
				listaFinal.appendChild(opcion);
			}
		}
	}
}

function precarga(presel) {
	var i, j, k;
	for (i = 0; i < presel.length; i++) {
		var sel = document.getElementById(presel[i][0]);
		if (sel.type.match("multi")) {
			var tmp = presel[i][1].split(',');
			for (j = 0; j < tmp.length; j++) {
				for (k = 0; k < sel.options.length; k++) {
					if (sel.options[k].value == tmp[j]) {
						sel.options[k].selected = 'selected';
					}
				}
			}
		} else {
			for (j = 0; j < sel.options.length; j++) {
				if (sel.options[j].value == presel[i][1]) {
					sel.options[j].selected = 'selected';
				}
			}
		}
	}
}

function cargarDeptos(deptos, lista) {
	var i, j;
	var f=false;
	var pais = document.getElementById('pais');
	for (j = 0; j < pais.options.length; j++) {
		if (pais.options[j].selected && pais.options[j].value==57) {
			for (i = 0; i < deptos.length; i++) {
				var d = deptos[i].split(",");
				opcion = document.createElement('option');
				texto = document.createTextNode(d[1]);
				opcion.appendChild(texto);
				opcion.value = d[0];
				lista.appendChild(opcion);
			}
			f=true;
		} else if(!f){
			for (i = 0; i < lista.options.length; i) {
				aBorrar = lista.options[i];
   				aBorrar.parentNode.removeChild(aBorrar);
			}
		}
	}
}