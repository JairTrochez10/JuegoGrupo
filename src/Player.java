import java.awt.*;

public class Player {
    int x, y, width = 30, height = 30;
    int vida = 100;
    int speed = 5;
    int municion = 20;

    boolean up, down, left, right;

    public Player(int x, int y) { this.x = x; this.y = y; }

    public void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;

        if (x < 0) x = 0;
        if (x > 770) x = 770;
        if (y < 0) y = 0;
        if (y > 570) y = 570;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        int barraAncho = width + 20;
        int barraAlto = 10;
        int vidaActual = vida * barraAncho / 100;
        if (vida > 60) g.setColor(Color.GREEN);
        else if (vida > 30) g.setColor(Color.ORANGE);
        else g.setColor((System.currentTimeMillis()/200)%2==0 ? Color.RED : Color.BLACK);

        g.fillRect(x - 10, y - 20, vidaActual, barraAlto);
        g.setColor(Color.WHITE);
        g.drawRect(x - 10, y - 20, barraAncho, barraAlto);

        g.setColor((System.currentTimeMillis()/200)%2==0 && municion > 0 ? Color.YELLOW : Color.WHITE);
        g.drawString("Munición: " + municion, x - 10, y - 30);
    }

    public Projectile shoot() {
        if (municion <= 0) return null;
        municion--;
        return new Projectile(x + width / 2 - 5, y);
    }

    public void recibirDaño(int cantidad) {
        vida -= cantidad;
        if (vida < 0) vida = 0;
    }

    public void recargarMunicion(int cant) { municion += cant; }

    public int getVida() { return vida; }
    public int getMunicion() { return municion; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}