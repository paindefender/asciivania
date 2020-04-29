package GameState;

import Main.GameWindow;

import java.awt.*;

public class WinState extends GameState{
    public WinState (GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0, GameWindow.WIDTH, GameWindow.HEIGHT);
        g.setColor(Color.BLACK);
        g.drawString("YOU ARE WIN!", 34 * GameWindow.CHARSCALE, 10 * GameWindow.CHARSCALE);
    }

    @Override
    public void keyPressed(int k) {
        gsm.setState(gsm.MENUSTATE);
    }

    @Override
    public void keyReleased(int k) {

    }
}
