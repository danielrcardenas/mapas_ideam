<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Crear Rol</title>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<link href="css/PopUp.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/popup.min.js"></script>
<script type="text/javascript">
	$(function() {
		$('#popup-guardar').modalPopLite({
			openButton : '#guardar',
			closeButton : '#cancelar',
			isModal : true
		});
	});

	function enviar(permisos){
		if(permisos==1){
			document.getElementById("permisos").value='1';
		}else{
			document.getElementById("permisos").value='0';
		}
		document.crearRol.submit();
	}
</script>
</head>
<body>
	<form id="crearRol" name="crearRol" action="<%=basePath%>registrarRol"
		method="post">
		<div id="header"></div>
		<div id="left"></div>
		<div id="body">
			<div>
				<div>
					<div>
						<label>Nombre</label>
					</div>
					<div>
						<input type="text" id="nombre" name="nombre">
					</div>
				</div>
				<div>
					<div>
						<label>Descripción</label>
					</div>
					<div>
						<textarea rows="5" cols="20" id="descripcion" name="descripcion"></textarea>
					</div>
				</div>
			</div>
			<div>
				<div>
					<input type="button" id="guardar" name="guardar" value="Guardar">
					<input type="hidden" id="permisos" name="permisos"> 
				</div>
				<div>
					<input type="button" value="Cancelar"
						onclick="javascript:window.history.go(-1);">
				</div>
			</div>
			<div id="popup-guardar">
				<div>
					<label>¿Desea asignar los permisos al rol creado?</label>
				</div>
				<div>
					<div>
						<input type="button" value="Si" onclick="enviar(1);">
					</div>
					<div>
						<input type="button" value="No" onclick="enviar(2);">
					</div>
					<div>
						<input id="cancelar" name="cancelar" type="button"
							value="Cancelar">
					</div>
				</div>
			</div>
		</div>
		<div id="footer"></div>
	</form>
</body>
</html>