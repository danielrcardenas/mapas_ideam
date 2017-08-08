function popUpAyudaAux(document) {
	var coords = getAbsoluteElementPosition(document,document.getElementById("icoAyuda"));

	document.getElementById("popUpAyuda").style.left = coords.left - 135 + "px";
	document.getElementById("popUpAyuda").style.top = coords.top + 15 + "px";
}

function popUpAyudaOpen(document) {
	var coords = getAbsoluteElementPosition(document,document.getElementById("icoAyuda"));

	document.getElementById("popUpAyuda").style.left = coords.left - 135 + "px";
	document.getElementById("popUpAyuda").style.top = coords.top + 15 + "px";
	document.getElementById("popUpAyuda").style.display = "block";
}

function popUpAyudaClose(document) {
	document.getElementById("popUpAyuda").style.display = "none";
}

function getAbsoluteElementPosition(document,element) {
	if (typeof element == "string")
		element = document.getElementById(element);

	if (!element)
		return {
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

function postEdit(document) {
	document.getElementById("logName").value = "";
	document.getElementById("logPassword").value = "";
}

function enviarForms(document) {
	var nombre = document.getElementById("logName").value;
	var pass = document.getElementById("logPassword").value;

	document.getElementById("hidUsername").value = nombre;
	document.getElementById("hidPassword").value = pass;
	document.getElementById("j_username").value = nombre;
	document.getElementById("j_password").value = pass;

	document.getElementById("formRegistra").submit();
	document.getElementById("j_security_check").submit();
}