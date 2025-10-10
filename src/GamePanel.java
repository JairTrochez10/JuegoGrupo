import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.io.File;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    Timer timer;
    Player player;
    ArrayList<Projectile> proyectiles = new ArrayList<>();
    ArrayList<Enemy> enemigos = new ArrayList<>();
    ArrayList<EnemyProjectile> disparosEnemigos = new ArrayList<>();

    boolean gameOver = false;
    boolean enMenu = true;
    String dificultad = "";
    int nivel = 1, velocidadBaseEnemigos = 2;
    int enemigosPorNivel = 5, enemigosMuertos = 0;
    int puntaje = 0;
    Point mousePos = new Point(0,0);

    Rectangle btnNormal = new Rectangle(300, 300, 200, 50);
    Rectangle btnHard = new Rectangle(300, 380, 200, 50);
    Rectangle btnSalir = new Rectangle(300, 460, 200, 50);

    boolean bossActivo = false;
    Enemy boss, boss2;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        timer = new Timer(16, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (enMenu) { drawMenu(g); return; }

        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());

        player.draw(g);
        for (Projectile p : proyectiles) p.draw(g);
        for (EnemyProjectile ep : disparosEnemigos) ep.draw(g);
        for (Enemy en : enemigos) en.draw(g);

        if (bossActivo && boss != null) boss.draw(g);
        if (bossActivo && boss2 != null) boss2.draw(g);

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.WHITE);
        g.drawString("Nivel: " + nivel, 20, 30);
        g.drawString("Vida: " + player.getVida(), 150, 30);
        g.drawString("Munición: " + player.getMunicion(), 300, 30);
        g.drawString("Puntaje: " + puntaje, 450, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 250, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Presiona R para reiniciar", 300, 340);
        }
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 800, 600);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Mi Juego Shooter", 180, 150);

        g.setColor(btnNormal.contains(mousePos)? Color.LIGHT_GRAY: Color.WHITE);
        g.fillRect(btnNormal.x, btnNormal.y, btnNormal.width, btnNormal.height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Normal", btnNormal.x+60, btnNormal.y+35);

        g.setColor(btnHard.contains(mousePos)? Color.LIGHT_GRAY: Color.WHITE);
        g.fillRect(btnHard.x, btnHard.y, btnHard.width, btnHard.height);
        g.setColor(Color.BLACK);
        g.drawString("Hard", btnHard.x+70, btnHard.y+35);

        g.setColor(btnSalir.contains(mousePos)? Color.RED: Color.WHITE);
        g.fillRect(btnSalir.x, btnSalir.y, btnSalir.width, btnSalir.height);
        g.setColor(Color.BLACK);
        g.drawString("Salir", btnSalir.x+70, btnSalir.y+35);
    }

    private void spawnEnemies() {
        int cantidad = dificultad.equals("Hard") ? 2 : 1;
        for (int i = 0; i < cantidad; i++) {
            int intentos = 0;
            while (intentos < 50) {
                int x = (int)(Math.random()*770);
                int y = (int)(Math.random()*570);
                Enemy nuevo = new Enemy(x, y);
                boolean colisiona = false;
                for (Enemy en : enemigos) if (nuevo.colisiona(en)) colisiona = true;
                if (!colisiona) {
                    nuevo.setSpeed(velocidadBaseEnemigos);
                    enemigos.add(nuevo);
                    break;
                }
                intentos++;
            }
        }
    }

    private void reproducirSonido(String archivo) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(archivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void iniciarJuego(String dif) {
        enMenu = false;
        dificultad = dif;
        player = new Player(400, 500);
        enemigos.clear();
        proyectiles.clear();
        disparosEnemigos.clear();
        nivel = 1;
        puntaje = 0;
        enemigosMuertos = 0;
        velocidadBaseEnemigos = 2;
        bossActivo = false;
        boss = null;
        boss2 = null;
        spawnEnemies();
        timer.start();
    }

    public void reiniciarJuego() {
        gameOver = false;
        enMenu = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !enMenu) {
            player.update();
            for (Projectile p: proyectiles) p.update();
            for (EnemyProjectile ep: disparosEnemigos) ep.update();

            for (Enemy en : new ArrayList<>(enemigos)) {
                en.update(player);
                if (en.puedeDisparar()) disparosEnemigos.add(en.shoot(player));
                if (en.colisiona(player)) player.recibirDaño(5);
            }

            // Disparo del jugador a enemigos normales
            for (Projectile p : new ArrayList<>(proyectiles)) {
                for (Enemy en : new ArrayList<>(enemigos)) {
                    if (en.colisiona(p)) {
                        p.activo = false;
                        en.vida--;
                        if (en.vida <= 0) {
                            enemigos.remove(en);
                            enemigosMuertos++;
                            puntaje += 10;
                            reproducirSonido("muerte.wav");
                        }
                    }
                }
            }

            // Disparo a bosses
            if (bossActivo) {
                if (boss != null) {
                    boss.update(player);
                    for (Projectile p: new ArrayList<>(proyectiles)) {
                        if (boss.colisiona(p)) {
                            p.activo = false;
                            boss.vida--;
                            if (boss.vida <= 0) {
                                boss = null;
                                bossActivo = false;
                                gameOver = true;
                                timer.stop();
                            }
                        }
                    }
                }
                if (boss2 != null) {
                    boss2.update(player);
                    for (Projectile p: new ArrayList<>(proyectiles)) {
                        if (boss2.colisiona(p)) {
                            p.activo = false;
                            boss2.vida--;
                            if (boss2.vida <= 0) {
                                boss2 = null;
                                bossActivo = false;
                                gameOver = true;
                                timer.stop();
                            }
                        }
                    }
                }
            }

            // Disparo de enemigos al jugador
            for (EnemyProjectile ep : new ArrayList<>(disparosEnemigos)) {
                if (new Rectangle(ep.x, ep.y, ep.width, ep.height)
                        .intersects(new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight()))) {
                    player.recibirDaño(5);
                    ep.activo = false;
                }
            }

            proyectiles.removeIf(p -> !p.activo);
            disparosEnemigos.removeIf(ep -> !ep.activo);

            // Subir de nivel si mató todos los enemigos normales
            if (!bossActivo && enemigosMuertos >= enemigosPorNivel) {
                nivel++;
                player.recargarMunicion(10);
                player.speed += player.speed / 2;
                velocidadBaseEnemigos++;
                enemigosMuertos = 0;
                enemigos.clear();

                if (dificultad.equals("Normal") && nivel == 10) {
                    bossActivo = true;
                    boss = new Enemy(400,100);
                    boss.size = 80;
                    boss.vida = 20;
                    boss.setSpeed(1);
                } else if (dificultad.equals("Hard") && nivel == 10) {
                    bossActivo = true;
                    boss = new Enemy(250,100);
                    boss.size = 80;
                    boss.vida = 20;
                    boss.setSpeed(2);
                    boss2 = new Enemy(550,100);
                    boss2.size = 80;
                    boss2.vida = 20;
                    boss2.setSpeed(2);
                }

                if (!bossActivo) spawnEnemies();
            }

            if (player.getVida() <= 0) {
                gameOver = true;
                timer.stop();
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (!gameOver && !enMenu) {
            if (code == KeyEvent.VK_LEFT) player.left = true;
            if (code == KeyEvent.VK_RIGHT) player.right = true;
            if (code == KeyEvent.VK_UP) player.up = true;
            if (code == KeyEvent.VK_DOWN) player.down = true;
            if (code == KeyEvent.VK_SPACE) {
                Projectile p = player.shoot();
                if (p != null) {
                    proyectiles.add(p);
                    reproducirSonido("disparo.wav");
                }
            }
        } else if (gameOver && code == KeyEvent.VK_R) reiniciarJuego();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) player.left = false;
        if (code == KeyEvent.VK_RIGHT) player.right = false;
        if (code == KeyEvent.VK_UP) player.up = false;
        if (code == KeyEvent.VK_DOWN) player.down = false;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {
        if (enMenu) {
            if (btnNormal.contains(e.getPoint())) iniciarJuego("Normal");
            else if (btnHard.contains(e.getPoint())) iniciarJuego("Hard");
            else if (btnSalir.contains(e.getPoint())) System.exit(0);
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) { mousePos = e.getPoint(); }
    @Override public void mouseDragged(MouseEvent e) {}
}