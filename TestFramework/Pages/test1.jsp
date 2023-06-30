<%@page import="java.util.Vector"%>

<%
    Vector<String> listes = (Vector<String>)request.getAttribute("donnees");
    int[] numbers = (int[])request.getAttribute("numbers");
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
    <p>Les donnees: </p>
    <ul>
        <% if(listes != null) {%>
            <li>Nom: <% out.println(listes.get(1)); %></li>
            <li>Prenoms: <% out.println(listes.get(2)); %></li>
            <li>Age: <% out.println(listes.get(3)); %></li>
            <li>Autres Somme: <% out.println(listes.get(4)); %></li>
        <% } %>
    </ul>

    <% if(numbers != null) { %>
        <p>Les nombres caches sont: </p>
        <ul>
            <% for(int i=0; i<numbers.length; i++){ %>
                <li><% out.println(numbers[i]); %></li>
            <% } %>
        </ul>
    <% } %>

    <p><a href="http://localhost:8080/YourFramework/getThis">Retour</a></p>

</body>
</html>