import java.awt.*;

public class Projectile {
    int x, y, speed = 10;

    public Projectile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed; // se mueve hacia arriba
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, 10, 20);
    }
}