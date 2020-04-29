package GameState;

import Main.GameWindow;
import CharMap.CharMap;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;

public class MenuState extends GameState{
    private Font font;
    private String header =
            " ▄▄▄       ██████ ▄████▄  ██▓██▓██▒   █▓▄▄▄      ███▄    █ ██▓▄▄▄\n" +
            "▒████▄   ▒██    ▒▒██▀ ▀█ ▓██▓██▓██░   █▒████▄    ██ ▀█   █▓██▒████▄\n" +
            "▒██  ▀█▄ ░ ▓██▄  ▒▓█    ▄▒██▒██▒▓██  █▒▒██  ▀█▄ ▓██  ▀█ ██▒██▒██  ▀█▄\n" +
            "░██▄▄▄▄██  ▒   ██▒▓▓▄ ▄██░██░██░ ▒██ █░░██▄▄▄▄██▓██▒  ▐▌██░██░██▄▄▄▄██\n" +
            " ▓█   ▓██▒██████▒▒ ▓███▀ ░██░██░  ▒▀█░  ▓█   ▓██▒██░   ▓██░██░▓█   ▓██▒\n" +
            " ▒▒   ▓▒█▒ ▒▓▒ ▒ ░ ░▒ ▒  ░▓ ░▓    ░ ▐░  ▒▒   ▓▒█░ ▒░   ▒ ▒░▓  ▒▒   ▓▒█░\n" +
            "  ▒   ▒▒ ░ ░▒  ░ ░ ░  ▒   ▒ ░▒ ░  ░ ░░   ▒   ▒▒ ░ ░░   ░ ▒░▒ ░ ▒   ▒▒ ░\n" +
            "  ░   ▒  ░  ░  ░ ░        ▒ ░▒ ░    ░░   ░   ▒     ░   ░ ░ ▒ ░ ░   ▒\n" +
            "      ░  ░     ░ ░ ░      ░  ░       ░       ░  ░        ░ ░       ░  ░\n" +
            "                 ░                  ░";

    private int currentOption = 0;
    private String[] options = {"Start", "Quit"};

    public MenuState(GameStateManager gsm){
        this.gsm = gsm;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, MenuState.class.getResourceAsStream("/fonts/Px437_ATI_8x8.ttf"));//new FileInputStream("/fonts/Px437_ATI_8x8.ttf"));
            font = font.deriveFont((float) GameWindow.CHARSCALE);
        } catch (Exception e) {
            System.out.println(new File(".").getAbsolutePath());
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, GameWindow.WIDTH, GameWindow.HEIGHT);
        // draw header
        g.setColor(new Color(255,0,0));
        g.setFont(font);
        CharMap.drawStringNewline(g, header,5 * GameWindow.CHARSCALE,3 * GameWindow.CHARSCALE);

        // options
        for (int i = 0; i < options.length; i++) {
            if (i == currentOption) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawString(options[i], 9 * GameWindow.CHARSCALE, 18 * GameWindow.CHARSCALE + i * GameWindow.CHARSCALE * 2 );
        }
    }

    private void select() {
        if( currentOption == 0 ){
            gsm.setState(gsm.SELECTORSTATE);
        }
        if( currentOption == 1 ){
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentOption--;
            if (currentOption == -1) currentOption = options.length - 1;
        }
        if (k == KeyEvent.VK_DOWN) {
            currentOption++;
            if (currentOption == options.length) currentOption = 0;
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
