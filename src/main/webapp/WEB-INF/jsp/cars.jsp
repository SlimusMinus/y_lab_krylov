<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cars</title>
    <jsp:useBean id="car" class="org.example.model.Car" scope="request"/>
</head>
<body>
<a href="cars?action=edit">Add Car</a>
<br><br>
<table border="1">
    <tr>
        <th>Brand</th>
        <th>Model</th>
        <th>Year</th>
        <th>Price</th>
        <th>Condition</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${cars}" var="car">
        <tr>
            <td>${car.brand}</td>
            <td>${car.model}</td>
            <td>${car.year}</td>
            <td>${car.price}</td>
            <td>${car.condition}</td>
            <td><a href="cars?action=delete&id=${car.id}">Delete</a></td>
            <td><a href="cars?action=edit&id=${car.id}">Update</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
