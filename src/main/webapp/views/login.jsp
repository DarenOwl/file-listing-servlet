<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../styles.css">

    <style>
        .auth-container {
            margin: 0 auto;
            width: min-content;
            text-align: center;
            background: darkcyan;
            padding: 10px;
        }
        .text-light {
            color: ghostwhite;
        }
        .text-error {
            color: orange;
        }
    </style>
</head>

<body>
<div class="content">
    <div class="auth-container">
        <form action="login" method="post">
            <input type="text" name="login" placeholder="Login">
            <input type="password" name="password" placeholder="Password"/>
            <input class="text-dark" type="submit" value="войти">
        </form>
        <p class="test-error">
            <%
                String errors = (String)request.getAttribute("errors");
                if (errors != null)
                    out.print(errors);
            %>
        </p>
        <a class="text-light" href="register">
            Зарегистрироваться
        </a>

    </div>
</div>
</body>
</html>


