<%@page import="co.gov.ideamredd.mbc.conexion.ConexionBD"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="co.gov.ideamredd.mbc.dao.ControlPermisos"%>
<%@page import="co.gov.ideamredd.util.entities.Usuario"%>
<%@page import="co.gov.ideamredd.util.UtilWeb"%>
<%@page import="co.gov.ideamredd.util.Util"%>
<%@page import="co.gov.ideamredd.mbc.auxiliares.*"%>

<%
boolean ok = false;
String respuesta = "";
String sql = "";
Integer id_consecutivo = null;
String str_id_consecutivo = Auxiliar.nzObjStr(request.getParameter("id_consecutivo"), "");
if (Auxiliar.tieneAlgo(str_id_consecutivo)) {
	id_consecutivo = Integer.parseInt(str_id_consecutivo);
	sql = "DELETE FROM RED_NOTICIAS WHERE NTCS_CONSECUTIVO ="+id_consecutivo;
	ok = ConexionBD.ejecutarSQL(sql, null);
	
} 

if (ok) out.print("1");
else out.print(respuesta);

%>