<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Root page</title>
</head>
<body>

<div align = "center">
	
	<form action = "initialize">
		<input type = "submit" value = "Initialize the Database"/>
	</form>
	<a href="login.jsp"target ="_self" > logout</a><br><br> 

<h1>List all users</h1>
    <div align="center">
        <p><%= request.getAttribute("list of users") %></p>
	</div>

<h1>List all Requests</h1>
    <div align="center">
        <p><%= request.getAttribute("list of requests") %></p>
	</div>	
	
<h1>List all Trees</h1>
    <div align="center">
        <p><%= request.getAttribute("list of trees") %></p>
	</div>
	
<h1>History of all chats</h1>
    <div align="center">
        <p><%= request.getAttribute("History of Chats") %></p>
	</div>	
	
	
	
	</div>

</body>
</html>