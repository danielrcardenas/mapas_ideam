var mouseX = 0;
var mouseY = 0;

function coordenadas(event) {
	x = event.clientX;
	y = event.clientY;

	document.getElementById("x").value = x;
	document.getElementById("y").value = y;
}

function actividadUusuario(ruta, usuarioId) {
	$.ajax({
		type : "GET",
		url : ruta,
		data : {
			usuario : usuarioId
		}, // serializes the form's elements.
		success : function(data) {
			// data No retorna nada
		}
	});
}

function postEdit() {
	if (document.getElementById("logName") && document.getElementById("logPassword")) {
		document.getElementById("logName").value = "";
		document.getElementById("logPassword").value = "";
	}
}

function enviarForms() {
	var nombre = document.getElementById("logName").value;
	var pass = document.getElementById("logPassword").value;
	
	if (nombre == '' || pass == '') {
		alert("Por favor especifique usuario y clave / Please specify username and password");
		return;
	}

	document.getElementById("hidUsername").value = nombre;
	document.getElementById("hidPassword").value = pass;
	document.getElementById("j_username").value = nombre;
	document.getElementById("j_password").value = pass;
	actividadUusuario('/MonitoreoBC-WEB/registrarAccesoServlet', nombre);

	document.getElementById("formRegistra").submit();
	document.getElementById("j_security_check").submit();
}

function lenguaje(id, uri) {
	if (id == 1) {
		document.getElementById('lenguaje').value = "ES";
	}
	else {
		document.getElementById('lenguaje').value = "EN";
	}
	document.getElementById('pagina').value = uri;
	if (document.getElementById('home')) {
		document.getElementById('home').submit();
		return;
	}
}

function getAbsoluteElementPosition(element) {
	if (typeof element == "string") element = document.getElementById(element);

	if (!element) return {
		top : 0,
		left : 0
	};

	var y = 0;
	var x = 0;
	while (element.offsetParent) {
		x += element.offsetLeft;
		y += element.offsetTop;
		element = element.offsetParent;
	}
	return {
		top : y,
		left : x
	};
}

function popUpAyudaAux() {
	var coords = getAbsoluteElementPosition(document.getElementById("icoAyuda"));
	document.getElementById("popUpAyuda").style.left = coords.left - 135 + "px";
	document.getElementById("popUpAyuda").style.top = coords.top + 15 + "px";
}

function popUpAyudaOpen() {
	var coords = getAbsoluteElementPosition(document.getElementById("icoAyuda"));

	document.getElementById("popUpAyuda").style.left = coords.left - 135 + "px";
	document.getElementById("popUpAyuda").style.top = coords.top + 15 + "px";
	document.getElementById("popUpAyuda").style.display = "block";
}

function popUpAyudaClose() {
	document.getElementById("popUpAyuda").style.display = "none";
}

function limpiarDiv(divid) {
	var div = document.getElementById(divid);
	if (div) {
		div.innerHTML = '';
	}
}

function inicializarNavegador() {
	var navegador = navigator.appName;

	if (navegador == "Microsoft Internet Explorer") {
		$("form").keypress(function(e) {
			if (e.keyCode == 13) {
				return false;
			}
		});
	}
	else {
		$("form").keypress(function(e) {
			if (e.which == 13) {
				return false;
			}
		});
	}
}

function takeCoordenadas(event) {
	mouseX = event.clientX;
	mouseY = event.clientY;
}

function enviarFormRC() {
	document.getElementById("email").value = document.getElementById("auxEmail").value;
	if (validar()) {
		document.getElementById("formRecordarClave").submit();
	}
}

function irAHome() {
	document.location.href = "/MonitoreoBC-WEB";
}

function mostrarLoading() {
	document.getElementById("capaLoading").style.display = "block";
}

function ocultarLoading() {
	document.getElementById("capaLoading").style.display = "none";
}

(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-103071412-1', 'auto');
ga('send', 'pageview');

