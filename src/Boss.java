import java.awt.*;

public class Boss {
    int x, y, width = 80, height = 80, vida = 100, speed = 1;
    long ultimoDisparo = 0;

    public Boss(int x, int y, int cantidad) {
        this.x = x;
        this.y = y;
        this.vida = 100 * cantidad;
    }

    public void update(Player player) {
        if (player.getX() > x) x += speed;
        if (player.getX() < x) x -= speed;
        if (player.getY() > y) y += speed;
        if (player.getY() < y) y -= speed;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > 800 - width) x = 800 - width;
        if (y > 600 - height) y = 600 - height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, width, height);
        g.setColor(Color.RED);
        g.drawString("BOSS HP: " + vida, x, y - 10);
    }

    public boolean colisiona(Player player) {
        return new Rectangle(x, y, width, height).intersects(
                new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
        );
    }

    public boolean colisiona(Projectile p) {
        return new Rectangle(x, y, width, height).intersects(new Rectangle(p.x, p.y, p.width, p.height));
    }

    public boolean puedeDisparar() {
        return System.currentTimeMillis() - ultimoDisparo > 1000;
    }

    public EnemyProjectile shoot(Player player) {
        ultimoDisparo = System.currentTimeMillis();
        return new EnemyProjectile(x + width / 2, y + height / 2, player.getX(), player.getY());
    }
}