import com.jogamp.opengl.GL2;

public class Block {

    private float posX, posY, posZ;
    private float size; // Taille du bloc
    private boolean destroyed; // Indique si le bloc est détruit

    public Block(float x, float y, float z, float size) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.size = size;
        this.destroyed = false;
    }

    public void display(GL2 gl) {
        if (destroyed) {
            return; // Ne rien afficher si le bloc est détruit
        }

        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.3f, 0.8f, 0.3f); // Couleur verte
        gl.glVertex3f(posX - size / 2, posY - size / 2, posZ);
        gl.glVertex3f(posX + size / 2, posY - size / 2, posZ);
        gl.glVertex3f(posX + size / 2, posY + size / 2, posZ);
        gl.glVertex3f(posX - size / 2, posY + size / 2, posZ);
        gl.glEnd();
    }

    public boolean isHit(float x, float y) {
        if (destroyed) {
            return false; // Pas de collision si le bloc est déjà détruit
        }

        // Vérifie si les coordonnées (x, y) sont dans les limites du bloc
        if (x >= posX - size / 2 && x <= posX + size / 2 &&
                y >= posY - size / 2 && y <= posY + size / 2) {
            destroyed = true; // Marque le bloc comme détruit
            return true;
        }
        return false;
    }
}
