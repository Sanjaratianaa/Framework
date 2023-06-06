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
    <p>Les listes des fruits: </p>
    <ul>
        <% //for(int i=0; i<listes.length; i++) {%>
            <li><% //out.println(listes[i]); %></li>
        <% //} %>
    </ul>

    <form method="get" action="InsertionPersonne" enctype="multipart/form-data">
        <input type="hidden" name="idPersonne" value="0">
        <p>Nom: <input type="text" name="Nom"></p>
        <p>Prenom: <input type="text" name="Prenom"></p>
        <p>Age: <input type="text" name="Age"></p>

        <p>Premier Nombre: <input type="number" name="un"></p>
        <p>Second Nombre: <input type="number" name="deux"></p>

        <p>Check1: <input type="checkbox" name="numbers[]" value="2"></p>
        <p>Check2: <input type="checkbox" name="numbers[]" value="4"></p>
        <p>Check3: <input type="checkbox" name="numbers[]" value="6"></p>

        <input type="submit" value="valider">
        
    </form>

    <p><a href="http://localhost:8080/YourFramework/insertImage">Inserer une image</a></p>

</body>
</html>