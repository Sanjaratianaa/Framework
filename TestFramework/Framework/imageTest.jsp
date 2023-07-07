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

    <form method="post" action="getImage.do" enctype="multipart/form-data">
        <p>Choisis une image:</p>
        <p>Image: <input type="file" name="Fichier"></p>
        <input type="submit" value="valider">
    </form>

</body>
</html>