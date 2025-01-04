import com.jogamp.opengl.GL2;

public class Bunker extends GraphicalObject {

    private float width;
    private float height;
    private int health; // Santé du bunker, chaque collision réduit la santé

    public Bunker(float posX, float posY, float posZ, float scale) {
        super(posX, posY, posZ, 0, 0, 0, 0.5f, 0.5f, 0.5f, scale);
        this.width = 1.0f; // Largeur du bunker
        this.height = 0.5f; // Hauteur du bunker
        this.health = 3; // Santé initiale du bunker (par exemple, 3 collisions pour détruire)
    }

    @Override
    public void displayNormalized(GL2 gl) {
        if (health > 0) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(0.3f, 0.8f, 0.3f); // Couleur verte pour représenter un bunker intact
            gl.glVertex3f(-width / 2, -height / 2, 0);
            gl.glVertex3f(width / 2, -height / 2, 0);
            gl.glVertex3f(width / 2, height / 2, 0);
            gl.glVertex3f(-width / 2, height / 2, 0);
            gl.glEnd();
        }
    }

    public boolean handleCollision(float x, float y) {
        if (health <= 0) {
            return false; // Le bunker est déjà détruit
        }

        // Vérification de collision avec les coordonnées du projectile
        if (x >= this.getX() - width / 2 && x <= this.getX() + width / 2 &&
                y >= this.getY() - height / 2 && y <= this.getY() + height / 2) {
            health--; // Réduire la santé du bunker
            if (health <= 0) {
                System.out.println("Bunker détruit à la position : " + this.getX());
            }
            return true;
        }
        return false;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
