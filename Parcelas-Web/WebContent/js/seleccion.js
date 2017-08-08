/* Add your JavaScript code here */
var opcion;
var valores=null;
var area;
var valArea=null;
var inputX =null;
var inputY=null;
num = 0;
var divPunto;
var divPoligono;
var divVisualizar;

function seleccion() {
	opcion = document.getElementById('geometria').value;
	divPunto = document.getElementById('punto');
	divPoligono = document.getElementById('poligono');
	divVisualizar = document.getElementById('visualizar');

	switch (opcion) {
	case '0':
		divPunto.style.display = 'none';
		divPoligono.style.display = 'none';
		divVisualizar.style.display = 'none';
		break;
	case '1':
		if (divPunto.style.display == 'none') {
			divPunto.style.display = 'block';
		}
		if (divPoligono.style.display == 'block') {
			divPoligono.style.display = 'none';
		}
		if (divVisualizar.style.display == 'none') {
			divVisualizar.style.display = 'block';
			//document.getElementById('localizacion').style.height='auto';
		}

		if (inputX == null) {
			labelX = document.createElement('label');
			labelX.innerText = 'Punto X:';
			labelX.innerHTML = 'Punto X:';
			divPunto.appendChild(labelX);

			inputX = document.createElement('input');
			inputX.type = 'text';
			inputX.name = 'inputX';
			inputX.id = 'inputX';
			inputX.title = 'Valor de la coordenada de longitud en grados decimal ej: -75,6897458 y en WGS84';
			if(valores!=null&&valores[0]!="null"){
				inputX.value=valores[0];
			}
			divPunto.appendChild(inputX);

			labelY = document.createElement('label');
			labelY.innerText = 'Punto Y:';
			labelY.innerHTML = 'Punto Y:';
			divPunto.appendChild(labelY);

			inputY = document.createElement('input');
			inputY.type = 'text';
			inputY.name = 'inputY';
			inputY.id = 'inputY';
			inputY.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
			if(valores!=null&&valores[1]!=null){
				inputY.value=valores[1];
			}
			divPunto.appendChild(inputY);

			labelArea = document.createElement('label');
			labelArea.innerText = '�rea(ha):';
			labelArea.innerHTML = '�rea(ha):';
			divPunto.appendChild(labelArea);

			area = document.createElement('input');
			area.type = 'text';
			area.name = 'area';
			area.id = 'area';
			area.title = 'Valor estimado de �rea de la parcela, en hect�reas';
			if(valArea !=null){
				area.value=valArea;
			}
			divPunto.appendChild(area);
		} else {
			inputX.value = '';
			inputY.value = '';
		}
		break;
	case '2':
		if (num != 0) {
			borrarDivPoligono()
			num = 0;
		}
		if (divPunto.style.display == 'block') {
			divPunto.style.display = 'none';
		}
		if (divPoligono.style.display == 'none') {
			divPoligono.style.display = 'block';
		}
		if (divVisualizar.style.display == 'none') {
			divVisualizar.style.display = 'block';
		}
		break;
	case '3':
		if (num != 0) {
			borrarDivPoligono()
			num = 0;
		}
		if (divPunto.style.display == 'block') {
			divPunto.style.display = 'none';
		}
		if (divPoligono.style.display == 'none') {
			divPoligono.style.display = 'block';
		}
		if (divVisualizar.style.display == 'none') {
			divVisualizar.style.display = 'block';
		}
		break;
	}

}

function crear(valX,valY) {
	contenedor = document.createElement('div');
	contenedor.id = 'div' + num;
	divPoligono.appendChild(contenedor);

	ele = document.createElement('label');
	ele.innerText = 'Punto X:';
	ele.innerHTML = 'Punto X:';
	contenedor.appendChild(ele);

	ele = document.createElement('input');
	ele.type = 'text';
	ele.name = 'puntoX' + num;
	ele.id = 'puntoX' + num;
	ele.title = 'Valor de la coordenada de longitud en grados decimal ej: -75,6897458 y en WGS84';
	if(valX!=null){
		ele.value=valX;
	}
	contenedor.appendChild(ele);

	ele = document.createElement('label');
	ele.innerText = 'Punto Y:';
	ele.innerHTML = 'Punto Y:';
	contenedor.appendChild(ele);

	ele = document.createElement('input');
	ele.type = 'text';
	ele.name = 'puntoY' + num;
	ele.id = 'puntoY' + num;
	ele.title = 'Valor de la coordenada de latitud en grados decimal ej: 4,8654587 y en WGS84';
	if(valY!=null){
		ele.value=valY;
	}
	contenedor.appendChild(ele);

	ele = document.createElement('input');
	ele.type = 'button';
	ele.value = 'Borrar';
	ele.name = 'div' + num;
	ele.onclick = function() {
		borrar(this.name)
	}
	contenedor.appendChild(ele);
	num++;
}

function borrar(obj) {
	divPoligono.removeChild(document.getElementById(obj));
	num--;
}

function borrarDivPoligono() {
	var i = 0;
	for (i = 0; i < num; i++) {
		divPoligono.removeChild(document.getElementById('div' + i));
	}
}

function valideExtend(puntoX, puntoY) {
	if ((puntoX < -66.8 && puntoX > -81.85)
			&& (puntoY > -4.71 && puntoY < 12.75))
		return true;
	else
		return false;
}

function retornarDatos() {
	var j = 0;
	var i;
	var valores = '';
	var er = /^-?[0-9]+(\.[0-9]*)?$/;
	var aux;
	opcion = document.getElementById('geometria').value;
	if (opcion != 1) {
		if (num == 0) {
			alert('Ingrese por lo menos un valor');
		} else {
			var listas = document.getElementsByTagName('div');
			for(j=0;j<listas.length;j++){
				if(listas[j].id.length<5)
				if(listas[j].id.substring(0,3)=='div'){
					i=listas[j].id.substring(3);
					if (document.getElementById('puntoX' + i).value.length != 0
						&& document.getElementById('puntoY' + i).value.length != 0) {
					if (er.test(document.getElementById('puntoX' + i).value)
							&& er.test(document.getElementById('puntoY' + i).value)) {
						if (valideExtend(
								document.getElementById('puntoX' + i).value,
								document.getElementById('puntoY' + i).value))
							if (valores == '') {
								valores = document.getElementById('puntoX' + i).value
										+ ","
										+ document.getElementById('puntoY' + i).value;
							} else {
								valores += ","
										+ document.getElementById('puntoX' + i).value
										+ ","
										+ document.getElementById('puntoY' + i).value;
							}
						else {
							alert('la coordenada X: '
									+ document.getElementById('puntoX' + i).value
									+ ', Y: '
									+ document.getElementById('puntoY' + i).value
									+ ' esta por fuera del extend de Colombia');
							valores = '';
							break;
						}
					} else {
						alert('Existen valores no validos en los campos');
						valores = '';
						break;
					}
				} else {
					alert('Existen campos vacios, ingrese un valor');
					valores = '';
					break;
				}
				}
			}
//			for (i = 0; i < num; i++) {
//				
//			}
		}
	} else {
		if (document.getElementById('inputX').value.length != 0
				&& document.getElementById('inputY').value.length != 0) {
			if (er.test(document.getElementById('inputX').value)
					&& er.test(document.getElementById('inputY').value)) {
				if (valideExtend(document.getElementById('inputX').value,
						document.getElementById('inputY').value))
					valores = document.getElementById('inputX').value + ","
							+ document.getElementById('inputY').value;
				else
					alert('Coordenadas por fuera del Extend de Colombia');
			} else {
				alert('Existen valores no validos en los campos');
			}
		} else {
			alert('Existen campos vacios, ingrese un valor');
		}
	}
	if (valores != '') {
		document.getElementById('valor').value = valores;
		return true;
	} else {
		return false;
	}

}

function tipoSeleccion(o){
	opcion=o;
} 
function coord(v){
	valores=v.split(",");
	return valores;
}
function valorArea(a){
	valArea=a;
}
function valNum(x){
	num=x;
}