<%
    String[] listes = (String[])request.getAttribute("fruits");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <title>Document</title>
</head>
<body>

    <form method="post" action="checkLogin.do">
        <h5>Bienvenueee</h5>
        <p>Nom: <input type="text" name="nom"></p>
        <p>Mot de passe: <input type="text" name="mdp"></p>
        <input type="submit" value="valider">
    </form>

</body>
</html>