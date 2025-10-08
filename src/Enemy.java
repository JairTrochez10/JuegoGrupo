import java.awt.*;

public class Enemy {
    int x, y;
    int speed = 2; // velocidad de movimiento
    int size = 30;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Persigue al jugador
    public void update(Player player) {
        if (player.x > x) x += speed;
        if (player.x < x) x -= speed;
        if (player.y > y) y += speed;
        if (player.y < y) y -= speed;
    }

    // Dibuja el enemigo
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, size, size);
    }

    // Detecta colisión con el jugador
    public boolean colisiona(Player player) {
        Rectangle r1 = new Rectangle(x, y, size, size);
        Rectangle r2 = new Rectangle(player.x, player.y, 30, 30);
        return r1.intersects(r2);
    }
}
