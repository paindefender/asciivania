package Entity;

import CharMap.CharMap;
import Main.GameWindow;

import javax.lang.model.type.NullType;
import java.awt.*;
import java.util.ArrayList;

public class Skeleton extends MapObject {

    int health;
    int maxHealth;
    boolean dead;
    int damage;

    ArrayList<Axe> axes;
    int axeDamage;
    long axeTimeout;
    long lastAxeTime;
    Player target;

    public Skeleton(CharMap charMap){
        super(charMap);
        health = maxHealth = 8;
        cheight = GameWindow.CHARSCALE * 3;
        cwidth = GameWindow.CHARSCALE;
        moveSpeed = GameWindow.CHARSCALE * 0.10;
        maxSpeed = GameWindow.CHARSCALE * 0.16;
        stopSpeed = GameWindow.CHARSCALE * 0.12;
        fallSpeed = GameWindow.CHARSCALE * 0.02;
        maxFallSpeed = GameWindow.CHARSCALE * 1.03;

        axes = new ArrayList<>();
        axeDamage = 2;
        axeTimeout = 1000;
        lastAxeTime = 0;

        this.target = target;
    }

    public void setPlayerTarget(Player target) {
        this.target = target;
    }
    public boolean isDead() {return dead;}
    public int getDamage() {return damage;}
    public void hit(int damage){
        if (dead) return;
        health -= damage;
        if (health < 0) dead = true;

    }

    public void checkCorners(double x, double y) {
        int left = (int)(x - cwidth/2) / GameWindow.CHARSCALE;
        int right = (int)(x + cwidth/2 - 1) / GameWindow.CHARSCALE;
        int bottom = (int)(y + cheight/2 - 1) / GameWindow.CHARSCALE;

        if (bottom >= 24 || left < 0 || right >= 80) {
            topLeft = topRight = bottomRight = bottomLeft = false;
            return;
        }

        char bl = charMap.getChar(bottom, left);
        char br = charMap.getChar(bottom, right);

        bottomLeft = bl == CharMap.SOLIDCHAR;
        bottomRight = br == CharMap.SOLIDCHAR;
    }

    public void getNextPosition() {
        if (!bottomLeft) {dx = 0; right = true; left = false;}
        else if (!bottomRight) {dx = 0; left = true; right = false;}
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

        if (falling) {
            dy += fallSpeed;
            dy = Math.min(dy, maxFallSpeed);
        }
    }
    public void update() {
        getNextPosition();
        checkMapCollision(1,0);
        setPosition(xtemp, ytemp);

        if (right && dx == 0) {
            right = false;
            left = true;
        } else if (left && dx == 0) {
            left = false;
            right = true;
        }
//        System.out.println(target.getBox());
        if (target.getX() - x < 400){ // If target is near
            if (System.currentTimeMillis() - lastAxeTime > axeTimeout) {
                Axe axe = new Axe(charMap, target.getX() - x > 0);
                axe.setPosition(x, y);
                axes.add(axe);
                axe.setMaxSpeed(400/(target.getX() - x)/(target.getX() - x));
                axe.setMoveSpeed(400/(target.getX() - x)/(target.getX() - x));
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
    }
    public void draw(Graphics2D g) {
        Color cTemp = g.getColor();
        g.setColor(Color.WHITE);
        CharMap.drawStringNewline(g, "☺\n%\n%", (int)(x - cwidth/2), (int)(y + charMap.getY() - cheight/2));
        g.setColor(cTemp);
//        Rectangle hitbox = getBox();
//        Color cTemp = g.getColor();
//        g.setColor(Color.RED);
//        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
//        g.setColor(cTemp);

        cTemp = g.getColor();
        g.setColor(Color.RED);
        for (Axe axe : axes) {
            axe.draw(g);
        }
        g.setColor(cTemp);
    }

    public ArrayList<Axe> getAxes() {
        return axes;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
