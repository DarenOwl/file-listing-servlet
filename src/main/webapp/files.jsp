<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="filewebapp.models.FileModel" %>
<%@ page import="filewebapp.models.DirectoryContentModel" %>
<html>
<head>
    <title>Files</title>
    <meta charset="UTF-8">
</head>
<body>
<%
    DirectoryContentModel directoryContent = (DirectoryContentModel) request.getAttribute("directoryContent");
%>
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
        for (FileModel fileModel : directoryContent.files) {
    %>
    <tr>
        <td>
            <img src="img/<%= fileModel.isDirectory ? "folder.png" : "file.png"%>"/>
            <a href="/files?path=<%=fileModel.path%>">
                <%=fileModel.name%>
            </a>
        </td>
        <td><%= fileModel.isDirectory ? "" : fileModel.sizeInBytes + "B"%></td>
        <td><%= fileModel.lastModifiedDate%></td>
    </tr>
    <%}%>
</table>
</body>
</html>
