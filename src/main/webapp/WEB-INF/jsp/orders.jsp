<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cars</title>
    <jsp:useBean id="order" class="org.example.model.Order" scope="request"/>
</head>
<body>
<a href="orders?action=edit">Add Order</a>
<br><br>
<table border="1">
    <tr>
        <th>ID</th>
        <th>userId</th>
        <th>carId</th>
        <th>date</th>
        <th>status</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${orders}" var="order">
        <tr>
            <td>${order.orderId}</td>
            <td>${order.userId}</td>
            <td>${order.carId}</td>
            <td>${order.date}</td>
            <td>${order.status}</td>
            <td><a href="orders?action=canceled&id=${order.orderId}">Отменить заказ</a></td>
            <td><a href="orders?action=changeStatus&id=${order.orderId}">Изменить статус заказа</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
