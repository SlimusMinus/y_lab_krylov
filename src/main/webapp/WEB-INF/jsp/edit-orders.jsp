<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit car</title>
    <jsp:useBean id="order" class="org.example.model.Order" scope="request"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<form method="post" action="orders">
    <input type="hidden" id="id" name="id" value="${order.orderId}">
    <div>
        <label for="userId">userId</label>
        <input type="number" id="userId" name="userId" value="${order.userId}">
        <br><br>
    </div>
    <div>
        <label for="carId">carId</label>
        <input type="number" id="carId" name="carId" value="${order.carId}">
        <br><br>
    </div>
    <div>
        <label for="date">date</label>
        <input type="date" id="date" name="date" value="${order.date}">
        <br><br>
    </div>
    <div>
        <label for="status">status</label>
        <input type="text" id="status" name="status" value="${order.status}">
        <br><br>
    </div>
    <input type="submit" value="Save">
    <input type="button" value="Cancel" onclick="window.history.back()">
</form>
</body>
</html>
