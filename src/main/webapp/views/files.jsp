<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="filewebapp.models.FileModel" %>
<%@ page import="filewebapp.models.DirectoryContentModel" %>
<html>
<head>
    <title>Files</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../styles.css">
    <style>
        html {
            font-family: Arial;
        }
        a{
            color: darkcyan;
        }
        img {
            height: 16px;
            width: 16px;
        }
        .row-light {
            background: ghostwhite;
        }
        .row-dark {
            background: gainsboro;
        }
    </style>
</head>
<body>
<%
    DirectoryContentModel directoryContent = (DirectoryContentModel) request.getAttribute("directoryContent");
%>
<a href="/logout">Выйти</a>
<p><%=directoryContent.dateGenerated%></p>
<h2><%=directoryContent.path%></h2>
<hr>
<%
    if (directoryContent.previousPath != null)
        out.print(String.format("<a href=\"/files?path=%s\"><-- Назад</a>", directoryContent.previousPath));
%>
<table>
    <tr>
        <th>Имя</th>
        <th>Размер</th>
        <th>Дата последнего изменения</th>
    </tr>
    <%
        boolean hasDarkColor = false;
        for (FileModel fileModel : directoryContent.files) {
    %>
    <tr class="<%= hasDarkColor ? "row-dark" : "row-light"%>">
        <td>
            <img src="https://www.flaticon.com/svg/static/icons/svg/<%= fileModel.isDirectory ? "3628/3628828.svg" : "3628/3628808.svg"%>"/>
            <a href="/files?path=<%=fileModel.path%>">
                <%=fileModel.name%>
            </a>
        </td>
        <td><%= fileModel.isDirectory ? "" : fileModel.sizeInBytes + "B"%></td>
        <td><%= fileModel.lastModifiedDate%></td>
    </tr>
    <%
            hasDarkColor = !hasDarkColor;
        }
    %>
</table>
</body>
</html>
