import java.awt.*;

public class Projectile {
    int x, y, width = 6, height = 6, velocidad = 8;
    boolean activo = true;

    public Projectile(int x, int y) { this.x = x; this.y = y; }

    public void update() {
        y -= velocidad;
        if (y < 0) activo = false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }
}

class EnemyProjectile {
    int x, y, width = 6, height = 6;
    double dx, dy;
    boolean activo = true;

    public EnemyProjectile(int x, int y, int objetivoX, int objetivoY) {
        double angulo = Math.atan2(objetivoY - y, objetivoX - x);
        dx = Math.cos(angulo) * 4;
        dy = Math.sin(angulo) * 4;
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += dx;
        y += dy;
        if (x < 0 || x > 800 || y < 0 || y > 600) activo = false;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
    }
}