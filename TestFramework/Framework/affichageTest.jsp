<%@page import="etu1947.framework.utile.Fichiers"%>
<%@page import="java.util.Vector"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.nio.charset.StandardCharsets"%>

<%
    Vector<Object> all = (Vector<Object>)request.getAttribute("images");
    Fichiers fichier = (Fichiers)all.get(0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h3>Pour les images: </h3>
    <p>Nom Image: <% out.println(fichier.getNomFichier()); %></p>
    <% 
        byte[] byteArray = fichier.getBytesFichier();
        String text = new String(byteArray, StandardCharsets.UTF_8);
    %>
    <p><% out.println(text); %></p>

    <p><a href="http://localhost:8080/YourFramework/getThis.do">Retour</a></p>

</body>
</html>