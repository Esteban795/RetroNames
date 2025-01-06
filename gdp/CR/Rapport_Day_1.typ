= Rapport Jour 1 CodingWeek
== Objectifs (fonctionnalités souhaitées)
- permettre à des joueurs de créer une partie en choisissant différents paramètres (taille de la grille, thématique de la liste de mots, nombre de joueurs, temps de réflexion limité ou non, etc.)
- permettre de sauvegarder/recharger une partie ;
- permettre d'éditer de nouvelles listes/thématiques de cartes "Nom de Code", ou de sélectionner/composer des jeux de cartes à partir des cartes existantes.



== Gestion carte secrète pour les SpyMaster
Generation d'un QR Code -> il faudrait un site et que le QR Code renvoie à cet URL... \
On aimerait trouver une autre solution. \
On y réfléchit et on prendra une décision plus tard.

== Affichage in game
- nom de chaque joueur 
- a chaque tour, afficher la couleur de l'équipe dont c'est le tour

== Gestion des decks
- Un JSON par deck


== Road Map 
Road Map faites au jour 1.\
Road Map sujette à modification au fur et à mesure de l'avancement (ajout de fonctionnalités, suppression de fonctionnalités, mauvaise estimation du temps, etc.) 

=== Jour 1
- Mise en place Gradle — 2 — 1h
- Diagramme de classes — 1 — 2h
- Gestionnaire de Scène — 3 — 3h
- Scènes :
  - Menu (play, settings, edit decks, Quit) Front&Back — 1 — 30min
  - Play (new game, load game) Front&Back — 0 — 1h — 10min
  - Interface jeu (grille principale) Front — 1 — 1h
  - Save&Load Game Front&Back — 3 — 4h

- Top Bar — 1 — 30min

- Logique du jeu (essentiellement Backend) :
  - Équipes et joueurs — 1 — 40min
  - Génération de la grille Back — 2 — 1h
  - Génération de la grille Front — 1 — 1h

- Génération QR code Carte secrète — 3 — 2h30 
- Génération QR code à partir de la grille — 1 — 20min

=== Jour 2

- Scène :
  - Edit Decks (ajout, suppression, création) Front — 1 — 1h
  - Edit Decks (ajout, suppression, création) Back — 2 — 1h
  - New Game (création d'équipe, sélection rôle, interface jeu) Front — 1 — 1h
  - New Game (création d'équipe, sélection rôle, interface jeu) Back — 2 — 1h
  - Interface jeu (grille principale) Back — 1 — 1h
  - Écran fin de partie — 1 — 1h

=== Jour 3

- Finir fonctionnalités j1 et j2
- Embellir le jeu (CSS)

=== Jour 4

- Mode Duo


- Mode de jeu Blitz
- Image à la place des mots
- Statistiques

=== Jour X
  - Settings
- Jeu :
  - Affichage équipe

== Todo day 1.1
- RoadMap — Esteban & Baptiste ✅
- Mise en place Gradle — Tom ✅
- Diagramme de classes — Esteban & Baptiste ✅
- Réflexion sur le QR Code — Maelan ✅
- Implementation de la génération du QR Code — Maelan ✅ (reste à la lier au jeu plus tard)
- Gestionnaire de Scène — Esteban ✅
- Menu principal — Baptiste ✅ (manque quelques links)
- Menu Play — Baptiste ✅ (Front ok, manque le back ❌)
- Menu Edit Decks ❌
- Save & Load game — Tom ✅
- Top Bar — Baptiste ✅ (mais abandonné pour le moment, pas d'utité) 
- Logique du jeu (Backend) :
  - Creation des classes — Tom ✅
  - Logique / algorithme du jeu — ?? ❌
- Jeu display de la grille — Esteban ❌
