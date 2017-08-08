<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="co.gov.ideamredd.dao.ParcelaDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	
	ParcelaDAO parcela = new ParcelaDAO();

	String parcelaAux = request.getParameter("parcelaAux");
	String depto= request.getParameter("deptoAux")==null?"":request.getParameter("deptoAux");
	String  tBosque= request.getParameter("tipoBosqueAux")==null?"":request.getParameter("tipoBosqueAux");
	JSONArray jsonArray = new JSONArray();
	JSONObject json = new JSONObject();

	List<String> parcelas = parcela.consultarParcela(parcelaAux,depto,tBosque);

	for (String especieFiltro : parcelas) {
		json.put("name", especieFiltro);
		json.put("value", especieFiltro);
		jsonArray.add(json);
	}

	out.println(jsonArray);
%>