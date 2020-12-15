J'ai créé tout le code de ce projet mais l'idée est une copie du projet Hermès de Grimille.

Mercure est un gestionnaire de bots Discord en Java qui implémente un système de plugins personnalisables grâce à une API qui s'inspire de celle de Spigot

Pour utiliser Mercure, mettez le JAR dans un dossier et lancez le depuis un terminal grâce à cette commande:
`
java -jar Mercure.jar
`

ou alors cette commande pour afficher les messages de debug:
`
java -jar Mercure.jar debug
`


Comment créer un plugin Mercure:

- Ajouter le JAR de mercure aux dépendances de votre projet

- Créer un fichier plugin.yml sur le modèle suivant:
    ```
    name: [name]
    version: [version]
    author: [author]
    description: [plugin description]
    main: [main class path]
    ```

- Créer une class principale implémentant MercurePlugin

- Pour enregistrer un event:
  - Créer une class implémentant Listener
  - Depuis la class principale, enregistrez cette class avec ```getPluginManager().getRegisterManager().registerEvents()```
  - Dans la listener, ajouter une fonction par évennement avec l'annotation @EventHandler et l'évennement JDA en paramètre

- Pour enregistrer une commande:
  - Ajouter ceci au plugin.yml:
      ```
      commands:
        [name]:
          description: [description]
          permissions:
            - ["user" for discord user]
            - ["console" for console]
            - ["all" for users and console]
            - ["role:ROLE_ID" make the user needs the role]
          format:
            - "[list formats for every argument]"
          aliases: []
      ```

   - Créer une class implémentant CommandExecutor
   - Depuis la class principale, enregistrez cette class avec ```getPluginManager().getRegisterManager().registerCommand()```
