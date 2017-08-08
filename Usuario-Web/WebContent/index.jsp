
<%@page language="java" contentType="text/html;  
    charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";

	request.getSession().invalidate();
	response.sendRedirect(basePath + "reg/home.jsp");
%>
