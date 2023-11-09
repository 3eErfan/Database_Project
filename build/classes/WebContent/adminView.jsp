<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Activity page</title>
</head>

<center><h2>Welcome David!</h2> 
<a href="login.jsp"target ="_self" > logout</a><br><br> 
</center>

 
	<body>
	
    <div align="center">
    <h2>Accepted Requests</h2>
        <p><%= request.getAttribute("Accepted requests") %></p>
    <h2>Open Requests</h2>
        <p><%= request.getAttribute("Open requests") %></p>
    <h2>Quote ready Requests</h2>
        <p><%= request.getAttribute("Quote requests") %></p>        
	<h2>Done Requests</h2>
        <p><%= request.getAttribute("Done requests") %></p>
	<h2>Rejected Requests</h2>
        <p><%= request.getAttribute("Rejected requests") %></p>
	
	</div>	
		 
	</body>
</html>