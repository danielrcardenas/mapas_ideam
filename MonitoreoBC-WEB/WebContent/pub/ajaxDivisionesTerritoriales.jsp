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
<%@page import="co.gov.ideamredd.reportes.dao.ConsultaReportes"%>
<%@page import="co.gov.ideamredd.reportes.entities.*"%>
<%
Integer divisionTerritorio = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("divisionTerritorio"), "-1"));
Integer tipoReporte = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("tipoReporte"), "-1"));
tipoReporte = 3;
ArrayList<String> nombresDivisionesTerritoriales = ConsultaReportes.consultarDivisionesTerritoriales(divisionTerritorio, tipoReporte);

String nombreDivisionTerritorial = ""; 

out.print("{");
out.print("\"divisiones\":[");
if (nombresDivisionesTerritoriales != null) {
	if (nombresDivisionesTerritoriales.size() > 0) {
		for (int d=0; d<nombresDivisionesTerritoriales.size(); d++) {
			nombreDivisionTerritorial = nombresDivisionesTerritoriales.get(d);

			if (d>0) {
				out.print(",");
			}
			out.print("\n{\"nombre\":\""+nombreDivisionTerritorial+"\"}");
		}
	}
}
out.print("]");
out.print("}");
%>