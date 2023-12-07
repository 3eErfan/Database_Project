<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Activity page</title>
<style>
    .container {
        display: flex;
        justify-content: center;
        align-items: flex-start;
    }
    .column {
        margin: 10px 100px;
    }
</style>
</head>

<body>
    <center><h2>Welcome David!</h2> 
    <a href="login.jsp" target="_self">logout</a><br><br> 
    </center>

    <div class="container">
        <div class="column">
            <p><%= request.getAttribute("requests") %></p>
        </div>
        <div class="column">  
            <h3>Big Clients</h3>
            <p><%= request.getAttribute("Big") %></p>   
            <h3>Easy Clients</h3>
            <p><%= request.getAttribute("Easy") %></p>
            <h3>Prospective Clients</h3>
            <p><%= request.getAttribute("Prospective") %></p>
            <h3>Good Clients</h3>
            <p><%= request.getAttribute("Good") %></p>
            <h3>Highest Tree</h3>
            <p><%= request.getAttribute("Highest Tree") %></p>    
        </div>
    </div>
</body>
</html>
