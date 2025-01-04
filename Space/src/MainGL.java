import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

public class MainGL extends GLCanvas implements GLEventListener, KeyListener, MouseListener {

    private enum GameState {
        MENU, PLAYING, GAME_OVER
    }

    private GameState gameState = GameState.MENU;

    private ArrayList<GraphicalObject> objects;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> alienProjectiles;
    private ArrayList<Bunker> bunkers;
    private PlayerShip player;
    private ArrayList<Alien> aliens;
    private boolean aliensMovingRight = true;
    private float alienSpeed = 0.3f;
    private long lastAlienMoveTime = System.currentTimeMillis();
    private int alienMoveInterval = 700; // Milliseconds between alien moves
    private int level = 1;

    private int playerLives = 3; // Vies initiales du joueur
    private int score = 0; // Score initial

    private float windowLimit = 7.5f; // Limites de l'écran
    private float playerLimit = 7.0f; // Limite ajustée pour le joueur
    private long lastAlienShootTime = System.currentTimeMillis();
    private int alienShootInterval = 2000; // Milliseconds entre les tirs des aliens
    private long lastPlayerShootTime = System.currentTimeMillis();
    private int playerShootInterval = 250; // Milliseconds entre les tirs du joueur

    private float aspectRatio = 1.0f; // Rapport largeur/hauteur de la fenêtre
    private GLUT glut = new GLUT();

    public MainGL() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.objects = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.alienProjectiles = new ArrayList<>();
        this.bunkers = new ArrayList<>();
        this.aliens = new ArrayList<>();
    }

    public void init(GLAutoDrawable drawable) {
        resetGame();
    }

    private void resetGame() {
        score = 0; // Réinitialise le score pour la nouvelle partie
        playerLives = 3;
        level = 1;
        objects.clear();
        projectiles.clear();
        alienProjectiles.clear();
        bunkers.clear();
        player = new PlayerShip(0, -3, 0, 0, 0, 0, 0, 0, 1, 0.5f);
        objects.add(player);
        initializeAliens();
        initializeBunkers();
    }

    private void initializeAliens() {
        aliens.clear();
        objects.removeIf(obj -> obj instanceof Alien);
        for (int j = 3; j <= 5; j++) {
            for (int i = -5; i <= 6; i++) {
                Alien alien = new Alien(i * 0.8f, j, 0, 0, 0, 0, 0, 1, 0, 0.6f);
                aliens.add(alien);
                objects.add(alien);
            }
        }
        alienSpeed = 1f + (level - 1) * 0.1f;
        alienMoveInterval = Math.max(300, 700 - (level - 1) * 50);
    }

    private void initializeBunkers() {
        float bunkerSpacing = 4.0f; // Espacement entre les bunkers
        for (int i = -1; i <= 1; i++) {
            Bunker bunker = new Bunker(i * bunkerSpacing, -2.5f, 0, 1f);
            bunkers.add(bunker);
            objects.add(bunker); // Ajouter à la liste des objets graphiques
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        final GLU glu = new GLU();
        aspectRatio = (float) width / height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspectRatio, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        switch (gameState) {
            case MENU:
                renderMenu(gl);
                break;
            case PLAYING:
                renderGame(gl);
                break;
            case GAME_OVER:
                renderGameOver(gl);
                break;
        }
    }

    private void renderMenu(GL2 gl) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-8, 8, -6, 6, -1, 1); // Vue orthogonale pour le menu
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Affichage du titre "SPACE INVADERS" en grand
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Vert rétro pour le texte principal
        gl.glTranslatef(-6f, 2f, 0); // Position centrée
        gl.glScalef(0.01f, 0.01f, 1f); // Mise à l'échelle pour agrandir la police
        for (char c : "SPACE INVADERS".toCharArray()) {
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
        gl.glPopMatrix();

        // Affichage du bouton "CLIQUE ICI POUR JOUER"
        gl.glPushMatrix();
        float time = (System.currentTimeMillis() % 1000) / 1000.0f; // Animation de couleur
        gl.glColor3f(1.0f, time, 0.0f); // Couleur qui varie avec le temps
        gl.glTranslatef(-5f, -2f, 0); // Position centrée sous le titre
        gl.glScalef(0.007f, 0.007f, 1f); // Mise à l'échelle pour une police légèrement plus petite
        for (char c : "CLIQUE POUR JOUER".toCharArray()) {
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
        gl.glPopMatrix();
    }



    private void renderGame(GL2 gl) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        final GLU glu = new GLU();
        glu.gluPerspective(45.0, aspectRatio, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -10);

        for (GraphicalObject obj : objects) {
            obj.display(gl);
        }

        renderHUD(gl);

        updateGame();

        if (playerLives <= 0) {
            gameState = GameState.GAME_OVER;
        }
    }

    private void renderHUD(GL2 gl) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(-aspectRatio * 8, aspectRatio * 8, -6, 6, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glColor3f(1.0f, 1.0f, 1.0f);

        // Affichage des vies
        gl.glRasterPos2f(-7.5f, 5.5f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Lives: " + playerLives);

        // Affichage du score
        gl.glRasterPos2f(5f, 5.5f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Score: " + score);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void renderGameOver(GL2 gl) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-8, 8, -6, 6, -1, 1); // Vue orthogonale pour le menu
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        // Affichage du titre "GAME OVER" en grand et épais
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Rouge rétro pour le texte principal
        gl.glTranslatef(-5.5f, 2f, 0); // Position centrée
        gl.glScalef(0.01f, 0.01f, 1f); // Mise à l'échelle pour agrandir la police
        for (char c : "GAME OVER".toCharArray()) {
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
        gl.glPopMatrix();

        // Affichage du score final
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 1.0f, 0.0f); // Jaune pour le score
        gl.glTranslatef(-6f, 0f, 0); // Position centrée sous le titre
        gl.glScalef(0.007f, 0.007f, 1f); // Taille légèrement réduite
        for (char c : ("SCORE: " + score).toCharArray()) {
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
        gl.glPopMatrix();

        // Affichage du bouton "REJOUER"
        gl.glPushMatrix();
        float time = (System.currentTimeMillis() % 1000) / 1000.0f; // Animation de couleur
        gl.glColor3f(0.0f, time, 1.0f); // Bleu qui varie avec le temps
        gl.glTranslatef(-4.5f, -2f, 0); // Position centrée sous le score
        gl.glScalef(0.007f, 0.007f, 1f); // Taille similaire au score
        for (char c : "REJOUER".toCharArray()) {
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
        gl.glPopMatrix();

        // Affichage du bouton "QUITTER"
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Rouge pour le bouton quitter
        gl.glTranslatef(-4.5f, -4f, 0); // Position centrée sous le bouton rejouer
        gl.glScalef(0.007f, 0.007f, 1f); // Taille similaire au score
        gl.glPopMatrix();
    }


    private void updateGame() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAlienMoveTime > alienMoveInterval) {
            boolean changeDirection = false;

            for (Alien alien : aliens) {
                float nextX = alien.getX() + (aliensMovingRight ? alienSpeed : -alienSpeed);
                if (nextX > windowLimit || nextX < -windowLimit) {
                    changeDirection = true;
                    break;
                }
            }

            for (Alien alien : aliens) {
                if (changeDirection) {
                    alien.translate(0, -0.5f, 0);
                    if (alien.getY() <= player.getY()) {
                        gameState = GameState.GAME_OVER;
                    }
                } else {
                    alien.translate(aliensMovingRight ? alienSpeed : -alienSpeed, 0, 0);
                }
            }
            if (changeDirection) {
                aliensMovingRight = !aliensMovingRight;
            }
            lastAlienMoveTime = currentTime;
        }

        if (currentTime - lastAlienShootTime > alienShootInterval) {
            for (Alien alien : aliens) {
                if (Math.random() < 0.1) {
                    Projectile projectile = new Projectile(alien.getX(), alien.getY() - 0.5f, 0, 0, 0, 0, 1, 0, 0, 0.2f);
                    alienProjectiles.add(projectile);
                    objects.add(projectile);
                }
            }
            lastAlienShootTime = currentTime;
        }

        moveProjectiles();
        checkCollisions();
    }

    private void moveProjectiles() {
        ArrayList<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            projectile.translate(0, 0.05f, 0);
            if (projectile.getY() > windowLimit) {
                toRemove.add(projectile);
            }
        }
        projectiles.removeAll(toRemove);
        objects.removeAll(toRemove);

        ArrayList<Projectile> alienProjectilesToRemove = new ArrayList<>();
        for (Projectile projectile : alienProjectiles) {
            projectile.translate(0, -0.05f, 0);
            if (projectile.getY() < -windowLimit) {
                alienProjectilesToRemove.add(projectile);
            }
            for (Bunker bunker : bunkers) {
                if (bunker.handleCollision(projectile.getX(), projectile.getY())) {
                    alienProjectilesToRemove.add(projectile);
                    break;
                }
            }
            if (Math.abs(projectile.getX() - player.getX()) < 0.5f &&
                    Math.abs(projectile.getY() - player.getY()) < 0.5f) {
                alienProjectilesToRemove.add(projectile);
                playerLives--;
            }
        }
        alienProjectiles.removeAll(alienProjectilesToRemove);
        objects.removeAll(alienProjectilesToRemove);
    }

    private void checkCollisions() {
        ArrayList<Alien> aliensToRemove = new ArrayList<>();
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            for (Alien alien : aliens) {
                if (Math.abs(projectile.getX() - alien.getX()) < 0.5f &&
                        Math.abs(projectile.getY() - alien.getY()) < 0.5f) {
                    aliensToRemove.add(alien);
                    projectilesToRemove.add(projectile);
                    score++;
                }
            }
            for (Bunker bunker : bunkers) {
                if (bunker.handleCollision(projectile.getX(), projectile.getY())) {
                    projectilesToRemove.add(projectile);
                    break;
                }
            }
        }

        aliens.removeAll(aliensToRemove);
        objects.removeAll(aliensToRemove);
        projectiles.removeAll(projectilesToRemove);
        objects.removeAll(projectilesToRemove);

        if (aliens.isEmpty()) {
            level++;
            initializeAliens();
        }
    }

    public void dispose(GLAutoDrawable drawable) {}

    public void keyPressed(KeyEvent e) {
        if (gameState == GameState.PLAYING) {
            if (System.currentTimeMillis() - lastPlayerShootTime > playerShootInterval) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (player.getX() - 0.5f > -playerLimit) {
                            player.translate(-0.5f, 0, 0);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (player.getX() + 0.5f < playerLimit) {
                            player.translate(0.5f, 0, 0);
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        Projectile projectile = new Projectile(player.getX(), player.getY() + 0.5f, 0, 0, 0, 0, 1, 1, 0, 0.2f);
                        projectiles.add(projectile);
                        objects.add(projectile);
                        lastPlayerShootTime = System.currentTimeMillis();
                        break;
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void mouseClicked(MouseEvent e) {
        if (gameState == GameState.MENU) {
            gameState = GameState.PLAYING;
            resetGame();
        } else if (gameState == GameState.GAME_OVER) {
            gameState = GameState.MENU;
        }
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        GLCanvas canvas = new MainGL();
        canvas.setPreferredSize(new Dimension(800, 600));
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setTitle("Space Invaders - Java OpenGL");
        frame.pack();
        frame.setVisible(true);
        final Animator animator = new Animator(canvas);
        animator.start();
    }
}