<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/createmessages.css">
<link rel="stylesheet" type="text/css" href="css/header.css">
<title></title>
</head>
<body>
	<%
		String toppanel; 
		if (request.getSession().getAttribute("MasterUser") == null) toppanel = "/toppanel-loggedout.jsp";
		else toppanel = "/toppanel-loggedin.jsp";
	%>
	<div id="centerpanel">
	<form action="SendNote" method="post">
		<input type="hidden"  name="getter" value='<%= request.getParameter("getter") %>'>
		<textarea type="text" cols="60" rows="10" name="note" id="nt" Placeholder="Message"></textarea>
		<input type="submit" id="buttn" value="Send">
	</form>
	</div>
	
	<div id="toppanel">
		<jsp:include page='<%= toppanel %>' />
	</div>
	
</body>
</html>