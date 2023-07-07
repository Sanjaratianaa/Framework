# Framework
- Mettre le fichier .sh dans votre home. (home/<Your pc-name>/)
- Remplacer le chemin de tomcat par le chemin de la place ou vous avez placer le votre.
- Remplacer aussi le chemin de yourFramework par le chemin du votre

Concernant l'utilisation du framework:
-> Chaque classe sera annoter par @ModelTable
-> A chaque fois que vous voulez utiliser ou applez une fonction, vous mettez une annotation @Url(value = <la valeur que vous voulez>.do)
-> Pour afficher des donnees venant de votre framework, vous devez d'abord appeler la fonction addItem(<nom>,<data>) dans modelview avec le nom que vous voulez l'associer avec.
-> Quand vous allez appeler les donnees dans l'affichage, ce sera avec request.getAttribute(<nom>) et vous le caster avec le type dont vous l'aviez assigner.
-> La value de l'url sera le lien pour appeler le modelView que vous voulez
-> Les pages .jsp doivent etre stocker dans un dossier Pages que vous allez creer au meme niveaux que votre code
-> Les classes que vous allez creer doivent etre dans le package etu1947.framework.models;
-> Cette fonction retourne une ModelView.

Les packages pour annotations:
- etu1947.framework.annotations pour les annotations
- etu1947.framework.utile pour la classe ModelView

Concernanat les authentifications:
>> Si vous voulez que l'utilisateur ne peut acceder a une fonction qu'apres login, vous ajouter @Auth( Profil = "")
>> SI vous voulez une personne specifique , c'est exemple: @Auth( Profil = "admin")
Pour les connexions (si besoin d'etre authetifier):
	- model.addSession("isConnected",<objet>)
	- model.addSession("profil",<profil-voulue>)
	>> autres sessions voules
		- model.addSession("<nom>","<valeur>")

Consernant les classes:
- le commencement des attributs devraient tous etre en majuscule: si vous voulez y acceder, pareil dans la page.jsp
Exemple:
Class Personne{
	String Nom;
	
	public void setNom(String nom){
		this.Nom = nom;
	}
	public String getNom(){
		return this.Nom;
	}
}
Dans le Jsp:
<input type="text" name="Nom" placeholder="your name please!">

Si votre fonction prend des parametres, faites comme suit:
>> public ModelView Insertion(@ParameterNames({"un","deux","numbers[]"}) double un,double deux, int[] numbers)
on ajoute les noms des arguments(meme ecriture) dans @ParameterNames({}), si c'est un tableau ajouter un crochet a la fin.
Dans l'affichage c'est la meme chose que l'input preciser ci-dessus

Concernant les Json:
>> Si vous voulez juste transformez les datas d'un modelView, vous ajouter avant de retourner: model.setIsJson(true)
>> Si vous voulez trnasformez le resultat de n'importe quel objet en Json: vous devez ajouter l'annotation @RestAPI sur la fonction

Concernant les Sessions:
>> Si vous voulez supprimer toutes les sessions en meme temps: vous faites comme suit:
ModelView model = new ModelView("deconnexion.jsp");
model.setInvalidateSession(true);

>> Si vous voulez supprimer des sessions specifiques:
	- Vous creer une List<String> et ajouter dedans toutes les noms des sessions que vous voulez supprimer
	- Vous passez cette List dans le ModelView avec model.setRemoveSession(<la liste>)	
	
>> SI vous voulez acceder a des sessions via votre applications:
	- ajouter l'annotation @Session sur le ModelView(fonction)
	- creer une attribut HashMap<String, Object> SessionsAffichage pour la classe qui en a besoin.
