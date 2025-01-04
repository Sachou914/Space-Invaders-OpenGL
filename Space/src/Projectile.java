import com.jogamp.opengl.GL2;class Projectile extends GraphicalObject {
    public Projectile(float pX, float pY, float pZ, float angX, float angY, float angZ, float r, float g, float b, float scale) {
        super(pX, pY, pZ, angX, angY, angZ, r, g, b, scale);
    }

    public void displayNormalized(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.1f, 0.1f, 0f);
        gl.glVertex3f(0.1f, 0.1f, 0f);
        gl.glVertex3f(0.1f, -0.1f, 0f);
        gl.glVertex3f(-0.1f, -0.1f, 0f);
        gl.glEnd();
    }
}
