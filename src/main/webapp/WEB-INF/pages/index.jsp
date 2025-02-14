<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
    <div align="center">
        <c:choose>
            <c:when test="${status}">
                <h1>Your login is: ${login}, your roles are:</h1>
                <c:forEach var="s" items="${roles}">
                    <h3><c:out value="${s}" /></h3>
                </c:forEach>

                <c:if test="${admin}">
                    <p><a href="/admin">Click</a> for admin page</p>
                </c:if>

                <c:if test="${moder}">
                    <p><a href="/admin">Click</a> for moder page</p>
                </c:if>

                <form action="/update" method="POST">
                    E-mail:<br/><input type="text" name="email" value="${email}" /><br/>
                    Phone:<br/><input type="text" name="phone" value="${phone}" /><br/>
                    Address:<br/><input type="text" name="address" value="${address}" /><br/>
                    <input type="submit" value="Update" />
                </form>
            </c:when>
            <c:otherwise>
                <h1>Your login is: ${login}. Your Account has blocked. </h1>
                <br />
            </c:otherwise>
        </c:choose>


        <p>Click to logout: <a href="/logout">LOGOUT</a></p>
    </div>
</body>
</html>
