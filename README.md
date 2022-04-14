# Ouga Boggle

Un jeu de lettres inédit, pour les moments d'ennui !

[![pipeline status](https://gaufre.informatique.univ-paris-diderot.fr/crisan/ouga-boggle/badges/main/pipeline.svg)](https://gaufre.informatique.univ-paris-diderot.fr/crisan/ouga-boggle/commits/main)

## Description

Ce logiciel est un jeu de lettres, se déroulant sur une grille. Le but est de
trouver le plus de mots possibles dans la grille. Il peut être joué seul ou à
plusieurs, lentement ou contre la montre, en s'amusant ou en pleurant.

## Règles

Une liste complète des règles peut être trouvée [ici](https://www.boggle.fr/regles.php)

## Fonctionnalités

Voici une liste des fonctionnalités clefs de notre version :

- Un minuteur (qui peut être désactivé)
- Validation des mots
- Validation des déplacements
- Entrée clavier + entrée souris
- Multijoueur illimité

## Développement

Lancer un serveur :

```bash
./mvnw exec:java -Dexec.args="serveur"
```

Lancer un client :

```bash
./mvnw exec:java -Dexec.args="client"
```

## Développeurs

- Claire Chenillet
- Emilie Lin
- Florian Gaie
- Phillipe Hinault
- Bogdan Crisan
