
function cargarDatos(parc,contenedor, numColumnas, titulos, permisos, indiceInicial, indiceFinal){
	var i, j, conte,k;
	var cont = document.getElementById(contenedor);
	for (i=indiceInicial; i < parc.length; i++) {
		if(i<indiceFinal){
			var span1, span2, span3;
			var indiceColumna=1;
			var div = document.createElement('div');
			if(i%2==0)
				div.className='item-busqueda item-busqueda-odd';
			else
				div.className='item-busqueda item-busqueda-even';
			conte = parc[i].split(",");
			span1 = document.createElement('span');
			span1.id="columna"+indiceColumna;
			span1.className= "column column"+indiceColumna;
			indiceColumna++;
			span2 = document.createElement('span');
			span2.id="columna"+indiceColumna;
			span2.className= "column column"+indiceColumna;
			for (j = 0; j < conte.length; j++) {
				if(j==0){
					codigoProyecto = conte[j];
					var p = document.createElement('p');
					var radio = document.createElement('input');
					radio.type='radio';
					radio.name='codigo';
					radio.id='codigo';
					radio.value = conte[j];
					radio.innerText = titulos[j]+": "+ conte[j];
					radio.innerHTML = titulos[j]+": "+conte[j];
					p.appendChild(radio);
					span1.appendChild(p);
					div.appendChild(span1);
				}else if(j<5){
					var p = document.createElement('p');
					var strong = document.createElement('strong');
// 					if(j!=4){
						strong.innerText = titulos[j]+": "+ conte[j];
						strong.innerHTML = titulos[j]+": "+conte[j];
// 					}else{//esto no en el general
// 						strong.innerText = conte[j]+","+conte[j+1]+","+conte[j+2];
// 						strong.innerHTML = conte[j]+","+conte[j+1]+","+conte[j+2];
// 						j=6;
// 					}
					p.appendChild(strong);
					span1.appendChild(p);
					div.appendChild(span1);
				}else{
					var p = document.createElement('p');
					var strong = document.createElement('strong');
					strong.innerText = titulos[j]+": "+ conte[j];
					strong.innerHTML = titulos[j]+": "+conte[j];
					p.appendChild(strong);
					span2.appendChild(p);
					div.appendChild(span2);
				}
			}
			if(numColumnas==3){
				var k;
				indiceColumna++;
				span3 = document.createElement('span');
				span3.id="columna"+indiceColumna;
				span3.className= "column column"+indiceColumna;
// 				for(k=0;k<permisos.length;k++){}Cuando se tengan los permisos 
				adicionarBoton('<%=msj.getString("consulta.biomasa.define.estado")%>', span3, 'definirEstado', 1, consecutivo);
//					adicionarBoton('modificar', span3, 'Modificar', 2, codigoProyecto);
//					adicionarBoton('exportar', span3, 'Exportar', 3, codigoProyecto);
//					adicionarBoton('individuos', span3, 'Individuos', 4, codigoProyecto);
				div.appendChild(span3);
			}
			cont.appendChild(div);
		}
	}
}

function adicionarBoton(idBoton, contenedor, valor, opc, id) {
	var formulario = document.createElement("div");
	formulario.className = "form-actions";
	var hiddenIdParcela = document.createElement("input");
	hiddenIdParcela.type = 'hidden';
	hiddenIdParcela.id = 'idProyecto';
	hiddenIdParcela.name = 'idProyecto';
	hiddenIdParcela.value = valor;
	var boton = document.createElement('input');
	boton.type = "button";
	boton.id = 'boton' + idBoton;
	boton.name = 'boton' + idBoton;
	boton.className = "btn btn-default";
	boton.value = idBoton;
	if (boton.addEventListener) {
		boton.addEventListener("click", function() {
			enviar(opc,id);
		}, true);
	} else {
		boton.attachEvent('onclick', function() {
			enviar(opc,id);
		});
	}
	formulario.appendChild(hiddenIdParcela);
	formulario.appendChild(boton);
	contenedor.appendChild(formulario);
}

function paginar(boton){
	idActual=boton;
	var indiceFinal=boton*5;
	var indiceInicial=indiceFinal-5;
	resultadosContactos(contacts.length, 'resultados','consultas');
	cargarDatos(contacts,'consultas',2,titulos,null,indiceInicial,indiceFinal);
	crearPaginas(contacts.length,'consultas',boton);
}

function crearColumnas(numColumnas,contResults){
	var i;
	for(i=0;i<numColumnas;i++){
		var span = document.createElement('span');
		span.id="columna"+(i+1);
		span.class= "column column"+(i+1);
		contResults.appendChild(span);
	}
}

function crearBoton(indice, id, ul){
	var li = document.createElement('li');
	if(indice==(id-1)){
		li.className="item-pager active";
		li.innerText = indice+1;
		li.innerHTML = indice+1;
	}else{
		li.className="item-pager";
		var a = document.createElement('a');
		a.id=indice+1;
		a.innerText = indice+1;
		a.innerHTML = indice+1;
		a.onclick=function() {paginar((indice+1)); };
		li.appendChild(a);
	}
	ul.appendChild(li);
}

function crearPaginas(tamanho,contenedor, id){
	var i;
	var cont = document.getElementById(contenedor);
	var paginador = document.getElementById('paginador');
	if (paginador != null)
		paginador.parentNode.removeChild(paginador);
	paginador = document.createElement('div');
	paginador.id='paginador';
	paginador.className = "pager";
	var ul = document.createElement('ul');
	var numeroHojas = Math.ceil(tamanho/5);
	
	var liInicial = document.createElement('li');
	var aInicial = document.createElement('a');
	var liAnterior = document.createElement('li');
	var aAnterior = document.createElement('a');
	var liSiguiente = document.createElement('li');
	var aSiguiente = document.createElement('a');
	var liFinal = document.createElement('li');
	var aFinal = document.createElement('a');
	
	liInicial.className="item-pager-controls item-pager-first"; 
	liInicial.onclick=function() {paginar((1)); };
	liAnterior.className="item-pager-controls item-pager-previous";
	liAnterior.onclick=function() {if(idActual>1){paginar(idActual-1);}};
	liSiguiente.className="item-pager-controls item-pager-next"; 
	liSiguiente.onclick=function() {if(idActual<numeroHojas){paginar(idActual+1);}};
	liFinal.className="item-pager-controls item-pager-last"; 
	liFinal.onclick=function() {paginar((numeroHojas)); };
	
	liInicial.appendChild(aInicial);
	liAnterior.appendChild(aAnterior);
	liSiguiente.appendChild(aSiguiente);
	liFinal.appendChild(aFinal);
	
	ul.appendChild(liInicial);
	ul.appendChild(liAnterior);
	for(i=0;i<numeroHojas;i++){
		crearBoton(i, id, ul);
	}
	ul.appendChild(liSiguiente);
	ul.appendChild(liFinal);
	
	paginador.appendChild(ul);
	cont.appendChild(paginador);
}
