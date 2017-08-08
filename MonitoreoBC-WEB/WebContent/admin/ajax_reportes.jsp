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
<%@page import="co.gov.ideamredd.reportes.dao.ConsultaReportes"%>
<%@page import="co.gov.ideamredd.reportes.entities.*"%>
<%
Integer id_reporte = null;
String str_id_reporte = Auxiliar.nzObjStr(request.getParameter("id_reporte"), "");
if (Auxiliar.tieneAlgo(str_id_reporte)) id_reporte = Integer.parseInt(str_id_reporte);

Integer tipoReporte = null;
String str_tipoReporte = Auxiliar.nzObjStr(request.getParameter("tipoReporte"), "");
if (Auxiliar.tieneAlgo(str_tipoReporte)) tipoReporte = Integer.parseInt(str_tipoReporte);

Integer divisionTerritorio = null;
String str_divisionTerritorio = Auxiliar.nzObjStr(request.getParameter("divisionTerritorio"), "");
if (Auxiliar.tieneAlgo(str_divisionTerritorio)) divisionTerritorio = Integer.parseInt(str_divisionTerritorio);

String nombreDivisionTerritorial = Auxiliar.nzObjStr(request.getParameter("nombreDivisionTerritorial"), "");
if (!Auxiliar.tieneAlgo(nombreDivisionTerritorial)) nombreDivisionTerritorial = null;

String fechaInicial = Auxiliar.nzObjStr(request.getParameter("fechaInicial"), "");
if (!Auxiliar.tieneAlgo(fechaInicial)) fechaInicial = null;

String fechaFinal = Auxiliar.nzObjStr(request.getParameter("fechaFinal"), "");
if (!Auxiliar.tieneAlgo(fechaFinal)) fechaFinal = null;

String identImagen = Auxiliar.nzObjStr(request.getParameter("identImagen"), "");
if (!Auxiliar.tieneAlgo(identImagen)) identImagen = null;

Integer periodoUno = null;
String str_periodoUno = Auxiliar.nzObjStr(request.getParameter("periodoUno"), "");
if (Auxiliar.tieneAlgo(str_periodoUno)) periodoUno = Integer.parseInt(str_periodoUno);

Integer periodoDos = null;
String str_periodoDos = Auxiliar.nzObjStr(request.getParameter("periodoDos"), "");
if (Auxiliar.tieneAlgo(str_periodoDos)) periodoDos = Integer.parseInt(str_periodoDos);

Integer publicado = null;
String str_publicado = Auxiliar.nzObjStr(request.getParameter("publicado"), "");
if (Auxiliar.tieneAlgo(str_publicado)) publicado = Integer.parseInt(str_publicado);


ArrayList<Reporte> reportes = ConsultaReportes.consultarReportesGeoprocesoAdmin(
		id_reporte, 
		tipoReporte, 
		divisionTerritorio, 
		nombreDivisionTerritorial, 
		fechaInicial, 
		fechaFinal, 
		identImagen, 
		periodoUno, 
		periodoDos, 
		publicado);

Reporte reporte = new Reporte();

String r_id_reporte = ""; 
String r_id_tiporeporte = ""; 
String r_fechageneracion = ""; 
String r_divisionterritorio = ""; 
String r_periodouno = ""; 
String r_periododos = ""; 
String r_identimagen = "";
String r_publicado = "";

out.print("{");
out.print("\"reportes\":[");
if (reportes != null) {
	if (reportes.size() > 0) {
		for (int r=0; r<reportes.size(); r++) {
			reporte = reportes.get(r);
			
			r_id_reporte = String.valueOf(reporte.getId());
			r_id_tiporeporte = String.valueOf(reporte.getIdTipoReporte());
			String nombreTipoReporte = ConexionBD.obtenerDato("select tprp_nombre from red_tiporeporte where tprp_consecutivo="+r_id_tiporeporte, "", null);
			r_fechageneracion = String.valueOf(reporte.getFechaGeneracion());
			r_divisionterritorio = String.valueOf(reporte.getIdDivision());
			String nombreDivisionTerritorio = ConexionBD.obtenerDato("select dvtr_nombre from red_divisionterritorio where dvtr_consecutivo="+r_divisionterritorio, "", null);
			r_periodouno = String.valueOf(reporte.getPeriodoUno());
			r_periododos = String.valueOf(reporte.getPeriodoDos());
			r_identimagen = Auxiliar.nzObjStr(reporte.getIdentImagen(), "NA");
			r_publicado = reporte.getPublicado() ? "1" : "0";
			ArrayList<String> a_divisiones = ConsultaReportes.consultarNombresDivisionesTerritoriales(r_id_reporte);
			String str_divisiones = "";
			
			for (int d=0; d<a_divisiones.size(); d++) {
				if (d > 0) {
					str_divisiones += ", ";
				}
				str_divisiones += a_divisiones.get(d);
			}
			
			if (r>0) {
				out.print(",");
			}
			out.print("\n{\"id_reporte\":\""+r_id_reporte+"\",\"id_tiporeporte\":\""+r_id_tiporeporte+"\",\"nombretiporeporte\":\""+nombreTipoReporte+"\",\"fechageneracion\":\""+r_fechageneracion+"\",\"divisionterritorio\":\""+r_divisionterritorio+"\",\"nombredivisionterritorio\":\""+nombreDivisionTerritorio+"\",\"periodouno\":\""+r_periodouno+"\",\"periododos\":\""+r_periododos+"\",\"identimagen\":\""+r_identimagen+"\",\"publicado\":\""+r_publicado+"\",\"divisiones\":\""+str_divisiones+"\"}");
		}
	}
}
out.print("]");
out.print("}");
%>