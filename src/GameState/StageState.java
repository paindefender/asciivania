package GameState;

import CharMap.CharMap;
import Entity.Exit;
import Entity.Player;
import Entity.Skeleton;
import Main.GameWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class StageState extends GameState{

    private CharMap charMap;
    private Player player;
    private Exit exit;
    private ArrayList<Skeleton> skeletons;
    private String lvlPath;

    public StageState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void init() {
        //═════☼
        charMap = new CharMap();
        this.lvlPath = gsm.lvlPath;
        charMap.loadMap(lvlPath);
        int playerX = charMap.getPlayerX();
        int playerY = charMap.getPlayerY();
        System.out.println(Integer.toString(playerX) + ", " + Integer.toString(playerY));
        player = new Player(charMap);
        player.setPosition(playerX * GameWindow.CHARSCALE + player.getCWidth()/2,playerY * GameWindow.CHARSCALE + player.getCHeight()/2);

        int exitX = charMap.getExitX();
        int exitY = charMap.getExitY();
        exit = new Exit(charMap);
        exit.setPosition(exitX * GameWindow.CHARSCALE + exit.getCWidth()/2, exitY * GameWindow.CHARSCALE + exit.getCHeight()/2);
        skeletons = charMap.getSkeletons();
        for (Skeleton skeleton : skeletons) {
            skeleton.setPlayerTarget(player);
            skeleton.setRight(true);
        }
////        System.out.println(player.getBox());
//        Skeleton arthur = new Skeleton(charMap);
//        arthur.setPlayerTarget(player);
//        arthur.setPosition(60*16,3*16);
//        arthur.setRight(true);
//        skeletons.add(arthur);
    }

    @Override
    public void update() {
        player.update();
        for (int i = 0; i < skeletons.size(); i++){
            skeletons.get(i).update();
            player.checkAxeDamage(skeletons.get(i).getAxes());
            if (skeletons.get(i).isDead()) {
                skeletons.remove(i);
                i--;
            }
        }
        player.checkSkeletonHits(skeletons);
        if (player.isDead()){
            gsm.setState(gsm.LOSESTATE);
        }
        if (exit.getBox().intersects(player.getBox())){
            gsm.setState(gsm.WINSTATE);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, GameWindow.WIDTH, GameWindow.HEIGHT);
        g.setColor(Color.GRAY);
        charMap.draw(g);
        player.draw(g);
        for (Skeleton skeleton : skeletons) {
            skeleton.draw(g);
        }
        exit.draw(g);
        String HUD = constructHUD(player);
        charMap.drawStringNewline(g,HUD,0,GameWindow.CHARSCALE * 24);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_LEFT) player.setLeft(true);
        if (k == KeyEvent.VK_RIGHT) player.setRight(true);
        if (k == KeyEvent.VK_DOWN) player.setDown(true);
        if (k == KeyEvent.VK_UP) player.setUp(true);
        if (k == KeyEvent.VK_Z) player.setJumping(true);
        if (k == KeyEvent.VK_X) player.setAxing(true);
    }

    @Override
    public void keyReleased(int k) {
        if (k == KeyEvent.VK_LEFT) player.setLeft(false);
        if (k == KeyEvent.VK_RIGHT) player.setRight(false);
        if (k == KeyEvent.VK_DOWN) player.setDown(false);
        if (k == KeyEvent.VK_UP) player.setUp(false);
        if (k == KeyEvent.VK_Z) player.setJumping(false);
        if (k == KeyEvent.VK_X) player.setAxing(false);
    }

    public String getLvlPath() {
        return lvlPath;
    }

    public String constructHUD(Player player) {
        char health = '█';
        char bHealth = '░';
        String currentStage = "Stage: " + lvlPath;
        String hp = "Health: ";
        String enemyHp = "";
        int i;
        for (i = 0; i < player.getHealth(); i++){
            hp += health;
        }
        for (;i < player.getMaxHealth(); i++){
            hp += bHealth;
        }
        if (player.getLastEnemy() != null) {
            enemyHp = "Enemy:  ";
            for (i = 0; i < player.getLastEnemy().getHealth(); i++) {
                enemyHp += health;
            }
            for (; i < player.getLastEnemy().getMaxHealth(); i++) {
                enemyHp += bHealth;
            }
        }
        return currentStage + '\n' + hp + '\n' + enemyHp;
    }
}
