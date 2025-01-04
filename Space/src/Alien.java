import com.jogamp.opengl.GL2;

class Alien extends GraphicalObject {
    public Alien(float pX, float pY, float pZ, float angX, float angY, float angZ, float r, float g, float b, float scale) {
        super(pX, pY, pZ, angX, angY, angZ, r, g, b, scale);
    }

    public void displayNormalized(GL2 gl) {
        gl.glPushMatrix();
        gl.glScalef(0.5f, 0.5f, 1f); // Ajuster l'échelle globale des aliens

        // Couleur de l'alien
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Vert

        // Partie principale (corps)
        drawPixelBlock(gl, -3, 0, 7, 1); // Rectangle central

        // Tête
        drawPixelBlock(gl, -1, 1, 3, 2); // Partie supérieure

        // Bras
        drawPixelBlock(gl, -4, 0, 1, 2); // Bras gauche
        drawPixelBlock(gl, 4, 0, 1, 2); // Bras droit

        // Jambes
        drawPixelBlock(gl, -3, -1, 1, 1); // Jambe gauche
        drawPixelBlock(gl, 3, -1, 1, 1); // Jambe droite

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
