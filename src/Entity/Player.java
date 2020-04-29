package Entity;

import CharMap.CharMap;
import Main.GameWindow;

import java.awt.*;
import java.util.ArrayList;

public class Player extends MapObject {
    private int health;
    private int maxHealth;

    private boolean dead;
    private boolean stunned;
    private long stunTime;
    private boolean facingRight;

    // Whip
    private boolean whipping;
    private int whipDamage;
    private int whipRange;

    // Axe
    private boolean axing;
    private int axeDamage;
    private long axeTimeout;
    private long lastAxeTime;
    private ArrayList<Axe> axes;
    private Skeleton lastEnemy = null;

    // states
    private static final int NEUTRAL = 0;
    private static final int JUMPING = 1;
    private static final int ATTACKING = 2;

    public Player(CharMap charMap){
        super(charMap);

        x = 25;
        y = 50;

        width = GameWindow.CHARSCALE * 1;
        height = GameWindow.CHARSCALE * 3;
        cwidth = GameWindow.CHARSCALE * 1;
        cheight = GameWindow.CHARSCALE * 3;

        health = maxHealth = 16;
        moveSpeed = GameWindow.CHARSCALE * 0.10;
        maxSpeed = GameWindow.CHARSCALE * 0.16;
        stopSpeed = GameWindow.CHARSCALE * 0.12;
        fallSpeed = GameWindow.CHARSCALE * 0.02;
        maxFallSpeed = GameWindow.CHARSCALE * 1.03;
        jumpStart = GameWindow.CHARSCALE * -0.44;
        stopJumpSpeed = GameWindow.CHARSCALE * 0.3;

        whipDamage = 2;
        whipRange = GameWindow.CHARSCALE * 3;

        axes = new ArrayList<>();
        axeDamage = 2;
        axeTimeout = 1000;
        lastAxeTime = 0;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getWhipDamage() {
        return whipDamage;
    }

    public int getWhipRange() {
        return whipRange;
    }

    public void setWhipping(boolean whipping){
        this.whipping = whipping;
    }

    public void setAxing(boolean b){
        if (b == false) {
            lastAxeTime = 0; // now you can throw an axe for each time you press the key
        }
        this.axing = b;
    }

    public void hit(int damage){
        if (dead) return;
        health -= damage;
        if (health < 0) dead = true;

    }

    public boolean isDead() {
        return dead;
    }

    public void checkSkeletonHits(ArrayList<Skeleton> skeletons){
        for (Axe axe : axes){
            for(Skeleton skeleton: skeletons) {
                if (axe.intersects(skeleton)) {
                    skeleton.hit(axeDamage);
                    lastEnemy = skeleton;
                    axe.setHit(true);
                }
            }
        }
    }

    public void checkAxeDamage(ArrayList<Axe> badAxes){
        for (Axe axe : badAxes){
            if (this.intersects(axe)){
                this.hit(axeDamage);
                System.out.println(health);
                axe.setHit(true);
            }
        }
    }

    private void getNextPosition() {
        if (left) {
            dx -= moveSpeed;
            dx = Math.max(dx, -maxSpeed);
        } else if (right) {
            dx += moveSpeed;
            dx = Math.min(dx, maxSpeed);
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                dx = Math.max(dx, 0);
            } else if (dx < 0) {
                dx += stopSpeed;
                dx = Math.min(dx, 0);
            }
        }

        if (jumping && !falling){
            dy = jumpStart;
            falling = true;
        }

        if (falling) {
            dy += fallSpeed;

            if (dy > 0) jumping = false;
            dy = Math.min(dy, maxFallSpeed);
        }

        if ((whipping || axing) && !(jumping || falling)) {
            dx = 0;
        }
    }

    public void update(){
        getNextPosition();
        checkMapCollision(1,0);
        setPosition(xtemp, ytemp);

//        System.out.println(Double.toString(x) + ", " + Double.toString(y));

        if (axing) {
            if (System.currentTimeMillis() - lastAxeTime > axeTimeout) {
                Axe axe = new Axe(charMap, facingRight);
                axe.setPosition(x, y);
                axes.add(axe);
                lastAxeTime = System.currentTimeMillis();
            }
        }

        for (int i = 0; i < axes.size(); i++) {
            axes.get(i).update();
            if (axes.get(i).getHit()){
                axes.remove(i);
                i--;
            }
        }

        if(right) facingRight = true;
        if(left) facingRight = false;


    }

    public void draw(Graphics2D g){
        setMapPosition();

//        if (stunned) {
//            long elapsed = (System.nanoTime() - stunTime) / 1000000;
//        }
        Color cTemp = g.getColor();
        g.setColor(Color.YELLOW);
        CharMap.drawStringNewline(g, "☻\n@\n@", (int)(x - cwidth/2), (int)(y + charMap.getY() - cheight/2));
        g.setColor(cTemp);
//        Rectangle hitbox = getBox();
//        Color cTemp = g.getColor();
//        g.setColor(Color.GREEN);
//        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
//        g.setColor(cTemp);


        cTemp = g.getColor();
        g.setColor(Color.ORANGE);
        for (Axe axe : axes) {
            axe.draw(g);
        }
        g.setColor(cTemp);
    }

    public Skeleton getLastEnemy() {
        return lastEnemy;
    }
}
