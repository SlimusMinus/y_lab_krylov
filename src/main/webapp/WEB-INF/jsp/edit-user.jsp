<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Edit user</title>
  <jsp:useBean id="user" class="org.example.model.User" scope="request"/>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<form method="post" action="users">
  <input type="hidden" id="id" name="id" value="${user.userId}">
  <div>
    <label for="login">login</label>
    <input type="text" id="login" name="login" value="${user.login}">
    <br><br>
  </div>
  <div>
    <label for="password">password</label>
    <input type="text" id="password" name="password" value="${user.password}">
    <br><br>
  </div>
  <div>
    <label for="name">name</label>
    <input type="text" id="name" name="name" value="${user.name}">
    <br><br>
  </div>
  <div>
    <label for="age">age</label>
    <input type="number" id="age" name="age" value="${user.age}">
    <br><br>
  </div>
  <div>
    <label for="city">city</label>
    <input type="text" id="city" name="city" value="${user.city}">
    <br><br>
  </div>
  <input type="submit" value="Save">
  <input type="button" value="Cancel" onclick="window.history.back()">
</form>
</body>
</html>
