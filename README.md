# Space Invaders Java (OpenGL)

## **Description**
Un jeu de type Space Invaders développé en Java avec la librairie OpenGL. 
L'objectif principal est de survivre le plus longtemps possible tout en marquant le plus de points.

### **Caractéristiques principales**
- **Déplacement du joueur** : Déplacez-vous avec les flèches gauche et droite.
- **Attaque** : Tirez avec la touche `ESPACE` pour éliminer les aliens.
- **Ennemis** :
  - Les aliens se rapprochent du joueur progressivement.
  - Ils tirent des projectiles qui peuvent blesser le joueur.
- **Défense** :
  - 3 boucliers destructibles protègent le joueur.
  - Cachez-vous derrière les boucliers pour éviter les tirs adverses.
  - Les boucliers peuvent être détruits par les tirs ennemis ou vos propres projectiles.
- **Progression** :
  - Les aliens accélèrent à chaque vague.
  - Plus vous progressez, plus le défi augmente.
- **Conditions de défaite** :
  - Perdre toutes vos 3 vies en recevant des tirs ennemis.
  - Laisser les aliens atteindre votre position.
- **Objectif** :
  - Faire le meilleur score possible en éliminant un maximum d'aliens.

---

## **Installation**

### **Prérequis**
- **Java Development Kit (JDK)** version 8 ou supérieure
- **OpenGL** (via [LWJGL](https://www.lwjgl.org/))

### **Étapes d'installation**
1. Clonez ce dépôt sur votre machine :
   ```bash
   git clone <URL-du-repository>
   ```
2. Importez le projet dans votre IDE préféré (Eclipse, IntelliJ IDEA, etc.).
3. Ajoutez les dépendances nécessaires pour OpenGL (LWJGL).
4. Compilez et exécutez le projet.

---

## **Commandes du jeu**

| **Commande**         | **Action**                          |
|-----------------------|--------------------------------------|
| Flèche gauche        | Déplacement à gauche                |
| Flèche droite        | Déplacement à droite                |
| Espace               | Tirer un projectile                 |

---

## **Gameplay**

1. **Début de la partie** :
   - Le joueur commence avec 3 vies et 3 boucliers.
   - Les aliens se déplacent latéralement et descendent progressivement.
2. **Objectif** :
   - Éliminez les aliens pour accumuler des points.
   - Survivez le plus longtemps possible.
3. **Difficulté croissante** :
   - À chaque vague, les aliens se déplacent plus vite.


### Sacha CLEMENT 
