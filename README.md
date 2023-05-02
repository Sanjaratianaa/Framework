# Framework
- Mettre le fichier .sh dans votre home. (home/<Your pc-name>/)
- Remplacer le chemin de tomcat par le chemin de la place ou vous avez placer le votre.
- Remplacer aussi le chemin de yourFramework par le chemin du votre

Concernant l'utilisation du framework:
-> Chaque classe sera annoter par @ModelTable
-> A chaque fois que vous voulez utiliser ou applez une fonction, vous mettez une annotation @Url(value = <la valeur que vous voulez>)
-> Pour afficher des donnees venant de votre framework, vous devez d'abord appeler la fonction addItem(<nom>,<data>) dans modelview avec le nom que vous voulez l'associer avec.
-> Quand vous allez appeler les donnees, ce sera ave request.getAttribute(<nom>) et vous le caster avec le type dont vous l'aviez assigner.
-> La value de l'url sera le lien pour appeler le modelView que vous voulez
-> Les pages .jsp doivent etre stocker dans un dossier Pages que vous allez creer au meme niveaux que votre code
-> Les classes que vous allez creer doivent etre dans le package etu1947.framework.models;
-> Cette fonction retourne une ModelView.

Les packages pour annotations:
- etu1947.framework.annotations pour les annotations
- etu1947.framework.utile pour la classe ModelView