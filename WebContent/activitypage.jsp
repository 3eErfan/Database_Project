<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html>
<html>
<head>
    <title>Activity Page</title>
</head>
<body>


    <div align="center">
    <h1>History of Requests</h1>
        <p><%= request.getAttribute("histoty of requests") %></p>
	
	
	<a href="login.jsp"target ="_self" > logout</a><br><br> 
	</div>	
	
</body>
</html>
