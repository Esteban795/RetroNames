# CodingWeek 2025
**TELECOM Nancy - 06/01 au 10/01**


## Compiler le jar
```bash
$ ./gradlew jar
```
Le jar se trouve alors dans build/libs/

\
Ou bien d'une autre manière.

## Exécuter le jar 
```bash
$ java -jar --module-path <path_to_your_javafx_repo>/lib --add-modules=javafx.base,javafx.controls,javafx.fxml <path_to_your_app>.jar
```