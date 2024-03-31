<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
            crossorigin="anonymous"></script>
</head>
<body>
<div align="center">
    <c:if test="${admin}">
        <h1>Secret page for admins only!</h1>
    </c:if>
    <c:if test="${moder}">
        <h1>Secret page for admins only!</h1>
    </c:if>

    <p>Click to go back: <a href="/">back</a></p>

    <p>Click to logout: <a href="/logout">LOGOUT</a></p>


    <c:if test="${admin}">
        <button type="button" id="add_user">Add</button>
        <button type="button" id="delete_user">Delete</button>
        <button type="button" class="status_user" data-status="false">Block Account</button>
        <button type="button" class="status_user" data-status="true">Unlock Account</button>
    </c:if>

    <table border="1">
        <c:forEach items="${users}" var="user">
            <tr>
                <c:if test="${admin}">
                    <td><input type="checkbox" name="toDelete" value="${user.id}" id="check_${user.id}"></td>
                </c:if>
                <td><c:out value="${user.login}"/></td>
                <td><c:out value="${user.role}"/></td>
                <td><c:out value="${user.status}"/></td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>

    $('#add_user').click(function(){
        window.location.href = "/register";
    });

    $('#delete_user').click(function(){
        var data = { 'toDelete' : []};

        $(":checked").each(function() {
            data['toDelete'].push($(this).val());
        });
        $.post("/delete", data, function(data, status) {
            window.location.reload();
        });
    });

    $('.status_user').click(function(){
        let status = $(this).data('status');
        var data = { 'userStatus' : [],
            'status':status
        };

        $("input[name='toDelete']:checked").each(function() {
            data['userStatus'].push($(this).val());
        });

        $.post("/status", data, function(data, status) {
            window.location.reload();
        });
    });
</script>

</body>
</html>
