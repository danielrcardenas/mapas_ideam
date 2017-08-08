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
Integer id_reporte = null;
String str_id_reporte = Auxiliar.nzObjStr(request.getParameter("id_reporte"), "");
if (Auxiliar.tieneAlgo(str_id_reporte)) {
	id_reporte = Integer.parseInt(str_id_reporte);
	String estado = ConexionBD.obtenerDato("select rprt_publicado from red_reportes where rprt_consecutivo=" + id_reporte, "", null);

	if (estado.equals("1")) {
		respuesta = "El reporte no se puede eliminar porque ya está publicado.";
	}
	else {
		sql = "delete from red_reportes where rprt_consecutivo="+id_reporte;
		ok = ConexionBD.ejecutarSQL(sql, null);
	}
} 

if (ok) out.print("1");
else out.print(respuesta);

%>