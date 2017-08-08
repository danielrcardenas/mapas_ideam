<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Monitoreo de Bosques y Carbono</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<script type='text/javascript'>
	function enviarForms() {
		document.getElementById("hidUsername").value = document
				.getElementById("j_username").value;
		document.getElementById("hidPassword").value = document
				.getElementById("j_password").value;
		document.getElementById("formRegistra").submit();
		document.getElementById("j_security_check").submit();
	}
</script>
</head>
<body>
	<h2>Log In Usuario</h2>
	<form method="post" action="j_security_check" name="j_security_check"
		id="j_security_check">
		<label>Usuario</label> <input type="text" name="j_username"
			id="j_username" /> <br> <label>Contrase&ntilde;a</label> <input
			type="password" name="j_password" id="j_password" />
	</form>
	<form method="post" action="<%=basePath%>registrarAccesoServlet"
		name="formRegistra" id="formRegistra" target="deathFrame">
		<input type="hidden" name="hidUsername" id="hidUsername" /><input
			type="hidden" name="hidPassword" id="hidPassword" />
	</form>
	<input type="button" value="Ingresar" onclick="enviarForms()"><br>
	<input type="button" value="Registrar"
		onclick="location.href='<%=basePath%>pub/registroUsuario.jsp';">
	<input type="button" value="Recordar Clave"
		onclick="location.href='<%=basePath%>pub/recordarClave.jsp';">
</body>
</html>