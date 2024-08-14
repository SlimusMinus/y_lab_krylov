<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit car</title>
    <jsp:useBean id="car" class="org.example.model.Car" scope="request"/>
</head>
<body>
    <form method="post" action="cars">
        <input type="hidden" id="id" name="id" value="${car.id}">
        <div>
            <label for="brand">Brand</label>
            <input type="text" id="brand" name="brand" value="${car.brand}">
            <br><br>
        </div>
        <div>
            <label for="model">Model</label>
            <input type="text" id="model" name="model" value="${car.model}">
            <br><br>
        </div>
        <div>
            <label for="year">Year</label>
            <input type="number" id="year" name="year" value="${car.year}">
            <br><br>
        </div>
        <div>
            <label for="price">Price</label>
            <input type="number" id="price" name="price" value="${car.price}">
            <br><br>
        </div>

        <div>
            <label for="condition">Condition</label>
            <input type="text" id="condition" name="condition" value="${car.condition}">
            <br><br>
        </div>
        <input type="submit" value="Save">
        <input type="button" value="Cancel" onclick="window.history.back()">
    </form>
</body>
</html>
