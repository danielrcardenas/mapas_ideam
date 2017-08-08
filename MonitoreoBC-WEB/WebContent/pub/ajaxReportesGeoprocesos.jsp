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
String nombreDivisionTerritorial = Auxiliar.nzObjStr(request.getParameter("nombreDivisionTerritorial"), "-1");
Integer tipoReporte = Integer.parseInt(Auxiliar.nzObjStr(request.getParameter("tipoReporte"), "-1"));

ArrayList<Reporte> reportes = ConsultaReportes.consultarReportesGeoproceso(divisionTerritorio, tipoReporte, nombreDivisionTerritorial);

Reporte reporte = new Reporte();

String id_reporte = ""; 
String fechageneracion = ""; 
String divisionterritorio = ""; 
String periodouno = ""; 
String periododos = ""; 
String identimagen = "";

out.print("{");
out.print("\"reportes\":[");
if (reportes != null) {
	if (reportes.size() > 0) {
		for (int r=0; r<reportes.size(); r++) {
			reporte = reportes.get(r);
			id_reporte = String.valueOf(reporte.getId());
			fechageneracion = String.valueOf(reporte.getFechaGeneracion());
			divisionterritorio = String.valueOf(reporte.getIdDivision());
			periodouno = String.valueOf(reporte.getPeriodoUno());
			periododos = String.valueOf(reporte.getPeriodoDos());
			identimagen = reporte.getIdentImagen();

			if (r>0) {
				out.print(",");
			}
			out.print("\n{\"id_reporte\":\""+id_reporte+"\",\"fechageneracion\":\""+fechageneracion+"\",\"divisionterritorio\":\""+divisionterritorio+"\",\"periodouno\":\""+periodouno+"\",\"periododos\":\""+periododos+"\",\"identimagen\":\""+identimagen+"\"}");
		}
	}
}
out.print("]");
out.print("}");
%>