<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="co.gov.ideamredd.dao.IndividuoDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	IndividuoDAO individuoDAO = new IndividuoDAO();

	ArrayList<String> parametros = new ArrayList<String>();

	parametros.add( request.getParameter("especieAux")==null?"":request.getParameter("especieAux")) ;
	parametros.add( request.getParameter("parcelaAux")==null?"":request.getParameter("parcelaAux"));
	
	parametros.add( request.getParameter("deptoAux")==null?"":request.getParameter("deptoAux"));
	parametros.add( request.getParameter("tipoBosqueAux")==null?"":request.getParameter("tipoBosqueAux"));
	
	
	
	
	JSONArray jsonArray = new JSONArray();
	JSONObject json = new JSONObject();
	List<String> especies = new ArrayList<String>();
	
	especies = individuoDAO.consultarEspecies(parametros);
	
	
	for (String especieFiltro : especies) {
		json.put("name", especieFiltro);
		json.put("value", especieFiltro);
		jsonArray.add(json);
	}

	out.println(jsonArray);
%>