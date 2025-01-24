# CodingWeek 2025
**TELECOM Nancy - 06/01 au 10/01**

# RetroNames

Groupe : 
- BLOCH Tom
- MANDJUKOWSKI Esteban
- SIBELLAS Baptiste
- TIGER Maelan


# Introduction 

RetroNames est un Codenames version Windows 95. On peut y retrouver deux options typiques de cette époque : 
- l'effet oeil de poisson (Fisheye Effect) de l'écran
- l'effet "scanlines".

Une vidéo de démonstration est disponible sur youtube : 

https://youtu.be/LNb_meM7E-c
## Remarque

Etant donné que JavaFX n'est plus dans la librairie standard de Java depuis un certain temps, il est maintenant nécessaire de linker les modules JavaFX lors de l'exécution du .jar

## Lancer le projet 
En étant à la racine du projet,
```bash
$ ./gradlew run
```

## Compiler le jar
```bash
$ ./gradlew jar
```
Le jar se trouve alors dans build/libs/

## Exécuter le jar 
```bash
$ java -jar --module-path <path_to_your_javafx_repo>/lib --add-modules=javafx.base,javafx.controls,javafx.fxml <path_to_your_app>.jar
```

Si vous avez fait `./gradlew jar` juste avant, il suffit de faire : 
```bash
$ java -jar --module-path  <path_to_your_javafx_repo>/lib --add-modules=javafx.base,javafx.controls,javafx.fxml  build/libs/grp24.jar
```

## Astuces et raccourcis
Dans le menu pour choisir son équipe, il est possible de presser entrée pour ajouter le pseudo actuel ou de presser <kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>X</kbd>  pour directement ajouter 4 joueurs par défaut, et <kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>Enter</kbd> pour tenter de valider. Attention à bien choisir le deck avant de valider cependant.
De même, durant la partie, il est possible de presser <kbd>Enter</kbd>  en ayant le champ indice ou le menu déroulant avec le nombre de tentatives sélectionné pour valider l'indice.