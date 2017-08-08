<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Monitoreo de Bosques y Carbono</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
</head>
<frameset rows="1,*" border="0" bordercolor="white" style="background: white">
 <frame name="deathFrame" src="<%=basePath%>closePag.jsp">
 <frame name="allFrame" src="<%=basePath%>index.jsp">
</frameset>
</html>