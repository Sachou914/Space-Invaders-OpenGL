import com.jogamp.opengl.GL2;

class PlayerShip extends GraphicalObject {
    public PlayerShip(float pX, float pY, float pZ, float angX, float angY, float angZ, float r, float g, float b, float scale) {
        super(pX, pY, pZ, angX, angY, angZ, r, g, b, scale);
    }

    public void displayNormalized(GL2 gl) {
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 1f); // Ajuster l'échelle globale du vaisseau

        // Couleur du vaisseau
        gl.glColor3f(0.0f, 1.0f, 1.0f); // Cyan

        // Partie principale (corps)
        drawPixelBlock(gl, -3, -1, 7, 2); // Base rectangulaire

        // Canon
        drawPixelBlock(gl, -1, 1, 3, 1); // Petit rectangle au centre

        // Extensions latérales
        drawPixelBlock(gl, -4, 0, 1, 1); // Extension gauche
        drawPixelBlock(gl, 4, 0, 1, 1); // Extension droite

        gl.glPopMatrix();
    }

    private void drawPixelBlock(GL2 gl, int x, int y, int width, int height) {
        float pixelSize = 0.1f; // Taille d'un "pixel"
        float startX = x * pixelSize;
        float startY = y * pixelSize;
        float endX = (x + width) * pixelSize;
        float endY = (y + height) * pixelSize;

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(startX, startY, 0);
        gl.glVertex3f(endX, startY, 0);
        gl.glVertex3f(endX, endY, 0);
        gl.glVertex3f(startX, endY, 0);
        gl.glEnd();
    }
}
