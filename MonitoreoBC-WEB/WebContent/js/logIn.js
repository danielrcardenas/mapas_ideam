function enviarForms() {
		var nombre = document.getElementById("logName").value;
		var pass = document.getElementById("logPassword").value;

		document.getElementById("hidUsername").value = nombre;
		document.getElementById("hidPassword").value = pass;
		document.getElementById("j_username").value = nombre;
		document.getElementById("j_password").value = pass; actividadUusuario('<%=basePath%>registrarAccesoServlet',nombre);

		document.getElementById("formRegistra").submit();
		document.getElementById("j_security_check").submit();
	}

function postEdit() {
	document.getElementById("logName").value = "";
	document.getElementById("logPassword").value = "";
}
