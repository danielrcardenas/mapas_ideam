<%@page import="co.gov.ideamredd.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.entities.Rol"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consultar Roles</title>
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
	var roles = new Array();
	function cargarRoles() {
<%ArrayList<Rol> roles = CargaDatosInicial.cargarRoles();
		for(int i=0;i<roles.size();i++){
			Rol rol = roles.get(i);%>
	roles[<%=i%>] = '<%=rol.getConsecutivo()%>'+ ";" +'<%=rol.getNombre()%>'+ ";" +'<%=rol.getDescripcion()%>';
<%}%>
	}

	function mostrarResultados() {
		divResultados = document.getElementById('resultados');
		var contenedor = document.getElementById('divRol');
		if (contenedor != null){
			divResultados.removeChild(contenedor);
			contenedor.innerHTML="";
		}
		contenedor =  document.createElement("div");
		contenedor.id = 'divRol';
		crearResultados(roles, contenedor);
		divResultados.appendChild(contenedor);
	}

	function crearResultados(arreglo, div){
		var i;
		var rol = new Array();
		for (i = 0; i < arreglo.length; i++) {
			rol = arreglo[i].split(';');
			var divRol = document.createElement("div");
			var divInfo = document.createElement("div");
			var radioInput = document.createElement('input');
			radioInput.type = 'radio';
			radioInput.name = 'rol';
			radioInput.value = rol[0];
			var lblCons = document.createElement("label");
			lblCons.innerHTML = 'Consecutivo: ';
			var cons = document.createElement("label");
			cons.innerHTML = rol[0];
			var lblNom = document.createElement("label");
			lblNom.innerHTML = 'Nombre: ';
			var nom = document.createElement("label");
			nom.innerHTML = rol[1];
			var lblDes = document.createElement("label");
			lblDes.innerHTML = 'Descripción: ';
			var des = document.createElement("label");
			des.innerHTML = rol[2];
			divInfo.appendChild(radioInput);
			divInfo.appendChild(lblCons);
			divInfo.appendChild(cons);
			divInfo.appendChild(lblNom);
			divInfo.appendChild(nom);
			divInfo.appendChild(lblDes);
			divInfo.appendChild(des);
			divRol.appendChild(divInfo);
			div.appendChild(divRol);
		}
	}

	function modificar(){
		var i;
		for (i=0;i<document.roles.rol.length;i++){
			if (document.roles.rol[i].checked){
				var url = '<%=basePath%>modificarRol.jsp?id='+ document.roles.rol[i].value;
				location.href = url;
			}
		}
	}

	function permisos(){
		var i;
		for (i=0;i<document.roles.rol.length;i++){
			if (document.roles.rol[i].checked){
				var url = '<%=basePath%>permisosRol.jsp?id='+ document.roles.rol[i].value;
				location.href = url;
			}
		}
	}

	$(function() {
		$('#popup-eliminar').modalPopLite({
			openButton : '#eliminar',
			closeButton : '#cancelar',
			isModal : true
		});
	});
	var filtrado;
	var cont;
	function filtrar() {
		var i;
		filtrado = new Array();
		cont=0;
		var rol = new Array();
		var valor = document.getElementById('buscartxt').value;
		divResultados = document.getElementById('resultados');
		for (i = 0; i < roles.length; i++) {
			rol = roles[i].split(';');
			if ((valor.search(rol[0])) > -1 ||(rol[1].indexOf(valor)) > -1 || (rol[2].indexOf(valor)) > -1){
				filtrado[cont]=roles[i];
				cont++;
			}
		}
		var contenedor = document.getElementById('divRol');
		if (contenedor != null){
			divResultados.removeChild(contenedor);
			contenedor.innerHTML="";
		}
		contenedor =  document.createElement("div");
		contenedor.id = 'divRol';
		crearResultados(filtrado, contenedor);
		divResultados.appendChild(contenedor);
	}

</script>
</head>
<body onload="cargarRoles();mostrarResultados();">
	<form id="roles" name="roles" action="<%=basePath%>eliminarRol"
		method="post">
		<div id="header"></div>
		<div id="left"></div>
		<div id="body">
			<div id="filtro">
				<div>
					<div id="Buscar">
						<div>
							<input type="text" id="buscartxt" name="buscartxt">
						</div>
						<div>
							<input type="button" id="buscarbtn" name="buscarbtn"
								value="Buscar" onclick="filtrar();">
						</div>
					</div>
				</div>
				<div>
					<input type="button" id="crearbtn" name="crearbtn"
						value="Crear Rol"
						onclick="location.href='<%=basePath%>crearRol.jsp'">
				</div>
			</div>
			<div id="resultados"></div>
			<div id="acciones">
				<div>
					<input type="button" value="Permisos" onclick="permisos();">
				</div>
				<div>
					<input type="button" value="Modificar" onclick="modificar();">
				</div>
				<div>
					<input id="eliminar" name="eliminar" type="button" value="Eliminar">
				</div>
			</div>
		</div>
		<div id="popup-eliminar">
			<div>
				<label>¿Está seguro de eliminar el rol?</label>
			</div>
			<div>
				<div>
					<input type="submit" value="Aceptar">
				</div>
				<div>
					<input id="cancelar" name="cancelar" type="button" value="Cancelar">
				</div>
			</div>
		</div>
	</form>
</body>
</html>