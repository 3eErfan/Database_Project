<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Request Details</title>
</head>
<body>

<div align = "center">
<h1>Request Details</h1>
	
<h2>Trees Info</h2>
    <div align="center">
        <p><%= request.getAttribute("trees info") %></p>
	</div>
	
<h2>History of chats</h2>
    <div align="center">
        <p><%= request.getAttribute("History of Chats") %></p>
	</div>	
	
	<div align="center">
        <p><%= request.getAttribute("msgBox") %></p>
	</div>	
	
	</div>

</body>
</html>