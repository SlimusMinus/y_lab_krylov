<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Cars</title>
  <jsp:useBean id="user" class="org.example.model.User" scope="request"/>
</head>
<body>

<table border="1">
  <tr>
    <th>login</th>
    <th>password</th>
    <th>name</th>
    <th>age</th>
    <th>city</th>
    <th></th>
    <th></th>
  </tr>
  <c:forEach items="${users}" var="user">
    <tr>
      <td>${user.login}</td>
      <td>${user.password}</td>
      <td>${user.name}</td>
      <td>${user.age}</td>
      <td>${user.city}</td>
      <td><a href="cars?action=delete&id=${user.userId}">Delete</a></td>
      <td><a href="cars?action=edit&id=${user.userId}">Update</a></td>
    </tr>
  </c:forEach>
</table>

</body>
</html>
