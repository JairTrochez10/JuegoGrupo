import java.awt.*;

public class Enemy {
    int x, y, size = 30;
    int vida = 3;
    int speed = 2;

    public Enemy(int x, int y) { this.x = x; this.y = y; }

    public void setSpeed(int s) { speed = s; }

    public void update(Player player) {
        if (player.getX() > x) x += speed;
        if (player.getX() < x) x -= speed;
        if (player.getY() > y) y += speed;
        if (player.getY() < y) y -= speed;

        if (x < 0) x = 0;
        if (x > 770) x = 770;
        if (y < 0) y = 0;
        if (y > 570) y = 570;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, size, size);
        g.setColor(Color.RED);
        g.fillRect(x, y - 5, vida * size / 3, 4);
        g.setColor(Color.WHITE);
        g.drawRect(x, y - 5, size, 4);
    }

    public boolean colisiona(Player player) {
        return new Rectangle(x, y, size, size)
                .intersects(new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
    }

    public boolean colisiona(Projectile p) {
        return new Rectangle(x, y, size, size)
                .intersects(new Rectangle(p.x, p.y, p.width, p.height));
    }

    public boolean colisiona(EnemyProjectile ep) {
        return new Rectangle(x, y, size, size)
                .intersects(new Rectangle(ep.x, ep.y, ep.width, ep.height));
    }

    public boolean colisiona(Enemy otro) {
        return new Rectangle(x, y, size, size)
                .intersects(new Rectangle(otro.x, otro.y, otro.size, otro.size));
    }

    public boolean puedeDisparar() {
        return Math.random() < 0.01;
    }

    public EnemyProjectile shoot(Player player) {
        return new EnemyProjectile(x + size/2, y + size/2, player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2);
    }
}