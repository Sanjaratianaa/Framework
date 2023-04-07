<%
    String[] listes = (String[])request.getAttribute("fruits");
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
    <h4>Hellooooooooooo</h4>
    <p>Tonga zaaaaaa</p>
    <p>Les listes de fruits: </p>
    <ul>
        <% for(int i=0; i<listes.length; i++) {%>
            <li><% out.println(listes[i]); %></li>
        <% } %>
    </ul>
</body>
</html>