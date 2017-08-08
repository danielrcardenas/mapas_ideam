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
<%@page import="co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos"%>
<%@page import="co.gov.ideamredd.mbc.entities.Noticias"%>
<%
Integer id_noticia = null;
String str_id_noticia = Auxiliar.nzObjStr(request.getParameter("id_noticia"), "");
if (Auxiliar.tieneAlgo(str_id_noticia)) id_noticia = Integer.parseInt(str_id_noticia);

Noticias n = CargaNoticiasYEventos.consultarNoticiaEvento(id_noticia);

out.print("{\"descripcion\":\""+n.getDescripcion()+"\",\"nombre\":\""+n.getNombre()+"\",\"imagen\":\""+n.getPathImagen()+"\",\"consecutivo\":"+n.getConsecutivo()+",\"tipo\":"+n.getTipo()+"}");

%>