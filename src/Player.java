import java.awt.*;

public class Player {
    int x, y;
    int speed = 5;
    int width = 30, height = 30;
    int vida = 100;

    // Controles de movimiento
    boolean up, down, left, right;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);

        // Barra de vida
        g.setColor(Color.WHITE);
        g.drawRect(x - 2, y - 10, width + 4, 6);
        g.setColor(Color.GREEN);
        g.fillRect(x - 1, y - 9, vida * (width + 2) / 100, 5);
    }

    public Projectile shoot() {
        return new Projectile(x + width / 2 - 5, y);
    }

    // Llamar a este método cuando un enemigo te toque
    public void recibirDaño(int cantidad) {
        vida -= cantidad;
        if (vida < 0) vida = 0;
    }
}