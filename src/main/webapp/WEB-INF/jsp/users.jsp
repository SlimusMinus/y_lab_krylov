<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Users</title>
  <jsp:useBean id="user" class="org.example.model.User" scope="request"/>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<table border="1">
  <tr>
    <th>№</th>
    <th>name</th>
    <th>age</th>
    <th>city</th>
    <th></th>
    <th></th>
  </tr>
  <c:forEach items="${users}" var="user" varStatus="status">
    <tr>
      <td>${status.index + 1}</td>
      <td>${user.name}</td>
      <td>${user.age}</td>
      <td>${user.city}</td>
      <td><a href="users?action=update&id=${user.userId}">Update</a></td>
    </tr>
  </c:forEach>
</table>

</body>
</html>
