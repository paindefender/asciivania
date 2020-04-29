package Main;

import GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GameWindow extends JPanel implements Runnable, KeyListener {
    // Dimensions to simulate 80x24 terminal window
    public static final int CHARWIDTH = 80;
    public static final int CHARHEIGHT = 24;
    public static final int CHARSCALE = 16; // basically font size
    public static final int WIDTH = CHARWIDTH * CHARSCALE;
    public static final int HEIGHT = CHARHEIGHT * CHARSCALE + 4 * CHARSCALE; // 4 additional rows for hud

    // game loop setup
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;

    private GameStateManager gsm;



    public GameWindow() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        gsm.keyPressed(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }

    private void init(){
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;

        gsm = new GameStateManager();
    }

    public void run() {
        init();

        long start;
        long elapsed;
        long wait;

        while (running) {
            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            // fps lock
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            if(wait<0) wait=0; //quick hack
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        gsm.update();
    }
    private void draw() {
        gsm.draw(g);
    }
    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0,0, WIDTH, HEIGHT, null);
        g2.dispose();
    }

}
