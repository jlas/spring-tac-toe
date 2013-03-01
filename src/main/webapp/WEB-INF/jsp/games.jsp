<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
</head>
<body>
<c:forEach var="game" items="${it.games}" varStatus="s">
${game.id}:${game.name}<br/>
</c:forEach>
</form>
</body>
</html>
