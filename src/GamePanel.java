import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    Player player;
    ArrayList<Projectile> proyectiles = new ArrayList<>();
    ArrayList<Enemy> enemigos = new ArrayList<>();
    boolean gameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        player = new Player(400, 300);

        // Crear algunos enemigos
        enemigos.add(new Enemy(100, 100));
        enemigos.add(new Enemy(700, 500));
        enemigos.add(new Enemy(400, 100));

        timer = new Timer(16, this); // ~60 FPS
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar jugador
        player.draw(g);

        // Dibujar proyectiles
        for (Projectile p : proyectiles)
            p.draw(g);

        // Dibujar enemigos
        for (Enemy en : enemigos)
            en.draw(g);

        // Mostrar vida
        g.setColor(Color.WHITE);
        g.drawString("Vida: " + player.vida, 20, 20);

        // Mensaje de Game Over
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 250, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Presiona R para reiniciar", 300, 340);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.update();

            // Actualizar proyectiles
            for (Projectile p : proyectiles)
                p.update();

            // Actualizar enemigos y detectar colisiones
            for (Enemy en : enemigos) {
                en.update(player);

                if (en.colisiona(player)) {
                    player.recibirDaño(1);
                    System.out.println("¡El enemigo te atacó!");
                }
            }

            // Verificar si el jugador murió
            if (player.vida <= 0) {
                gameOver = true;
                timer.stop();
                System.out.println("¡GAME OVER!");
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (!gameOver) {
            if (code == KeyEvent.VK_LEFT) player.left = true;
            if (code == KeyEvent.VK_RIGHT) player.right = true;
            if (code == KeyEvent.VK_UP) player.up = true;
            if (code == KeyEvent.VK_DOWN) player.down = true;
            if (code == KeyEvent.VK_SPACE)
                proyectiles.add(player.shoot());
        } else {
            // Reiniciar cuando esté en Game Over
            if (code == KeyEvent.VK_R)
                reiniciarJuego();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) player.left = false;
        if (code == KeyEvent.VK_RIGHT) player.right = false;
        if (code == KeyEvent.VK_UP) player.up = false;
        if (code == KeyEvent.VK_DOWN) player.down = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // 🔁 Método para reiniciar el juego
    public void reiniciarJuego() {
        player = new Player(400, 300);
        enemigos.clear();
        proyectiles.clear();

        enemigos.add(new Enemy(100, 100));
        enemigos.add(new Enemy(700, 500));
        enemigos.add(new Enemy(400, 100));

        gameOver = false;
        timer.start();
    }
}