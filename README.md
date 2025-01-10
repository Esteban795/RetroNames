# CodingWeek 2025
**TELECOM Nancy - 06/01 au 10/01**

# RetroNames

Groupe : 
- BLOCH Tom
- MANDJUKOWSKI Esteban
- SIBELLAS Baptiste
- TIGER Maelan

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