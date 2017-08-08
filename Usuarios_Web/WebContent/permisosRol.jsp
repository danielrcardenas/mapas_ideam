<%@page import="co.gov.ideamredd.entities.PermisosRol"%>
<%@page import="co.gov.ideamredd.entities.Permisos"%>
<%@page import="co.gov.ideamredd.dao.CargaDatosInicial"%>
<%@page import="co.gov.ideamredd.entities.Modulos"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Permisos del rol</title>
<link rel="stylesheet" href="css/accordion_style.css" type="text/css" />
<script type="text/javascript" src="js/jquery.1.4.2.js"></script>
<script type="text/javascript" src="js/accordion.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<%
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";
	String idRol = request.getParameter("id");
	ArrayList<Modulos> modulos = CargaDatosInicial.consultarModulos();
	ArrayList<Permisos> permisos = CargaDatosInicial.consultarPermisos();
	ArrayList<PermisosRol> permisosRol = CargaDatosInicial.consultarPermisosRol(Integer.valueOf(idRol));
%>
</head>
<body>
	<form id="permisosRol" action="<%=basePath%>permisosRol" method="post">
		<div id="accordion">
			<dl class="accordion" id="slider">
			</dl>
		</div>
		<div id=acciones>
			<div>
				<input type="hidden" name="idRol" id="idRol" value="<%=idRol%>">
				<input type="submit" value="Aceptar">
			</div>
			<div>
				<input type="button" value="Cancelar" onclick="javascript:window.history.go(-1);">
			</div>
		</div>
	</form>
	<script type="text/javascript">
	
	var modulos = new Array();
	var permisos = new Array();
	var permisosRol = new Array();
	function cargarModulos(){
<%for(int i=0;i<modulos.size();i++){
			Modulos m = modulos.get(i);%>
			modulos[<%=i%>] = '<%=m.getIdModulo()%>'+";"+'<%=m.getDescripcion()%>';
<%}%>
	}
	
	function cargarPermisos(){
<%for(int i=0;i<permisos.size();i++){
			Permisos p = permisos.get(i);%>
			permisos[<%=i%>] = '<%=p.getIdPermiso()%>'+";"+'<%=p.getDescripcion()%>'+";"+'<%=p.getIdModulo()%>';
<%}%>		
	}
	
	function cargarPermisosRol(){
<%for(int i=0;i<permisosRol.size();i++){
			PermisosRol p = permisosRol.get(i);%>
			permisosRol[<%=i%>] = '<%=p.getIdRol()%>'+";"+'<%=p.getIdPermiso()%>';
	<%}%>
		}

		function limpiarChecks() {
			var i, j;
			for (i = 0; i < document.getElementById('permisosRol').elements.length; i++) {
				if ((document.getElementById('permisosRol')[i].type == 'checkbox')) {
					for (j = 0; j < permisosRol.length; j++) {
						var pr = permisosRol[j].split(';');
						if (document.getElementById('permisosRol')[i].id == pr[1]) {
							document.getElementById('permisosRol')[i].checked = 1;
							break;
						}
					}
				}
			}
		}
		function cargarAcordeones() {
			var dlAcordeon = document.getElementById('slider');
			var i, j, k;
			cargarModulos();
			cargarPermisos();
			cargarPermisosRol();
			for (i = 0; i < modulos.length; i++) {
				var m = modulos[i].split(';');
				var dt = document.createElement('dt');
				dt.innerHTML = m[1];
				var dd = document.createElement('dd');
				for (j = 0; j < permisos.length; j++) {
					var p = permisos[j].split(';');
					if (m[0] == p[2]) {
						var divCheckbox = document.createElement('div');
						var checkbox = document.createElement('input');
						checkbox.type = 'checkbox';
						checkbox.name = 'permisos';
						checkbox.value = p[0];
						checkbox.id = p[0];
						var lbcCheck = document.createElement('label');
						lbcCheck.innerHTML = p[1];
						divCheckbox.appendChild(checkbox);
						divCheckbox.appendChild(lbcCheck);
						dd.appendChild(divCheckbox);
					}
				}
				dlAcordeon.appendChild(dt);
				dlAcordeon.appendChild(dd);
			}
		}

		cargarAcordeones();
		limpiarChecks();

		var slider1 = new accordion.slider("slider1");
		slider1.init("slider");
	</script>
</body>
</html>