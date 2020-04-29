package Entity;

import CharMap.CharMap;
import Main.GameWindow;

import java.awt.*;

public class Axe extends MapObject {

    private boolean hit;

    public Axe(CharMap charMap, boolean right){
        super(charMap);
        maxSpeed = GameWindow.CHARSCALE * 0.25;
        jumpStart = GameWindow.CHARSCALE * -0.55;
        stopJumpSpeed = GameWindow.CHARSCALE * 0.3;
        maxFallSpeed = GameWindow.CHARSCALE * 1.03;
        fallSpeed = GameWindow.CHARSCALE * 0.019;
        moveSpeed = maxSpeed;
        dx = right?moveSpeed:-moveSpeed;
        jumping = true;
        cwidth = GameWindow.CHARSCALE * 1;
        cheight = GameWindow.CHARSCALE * 1;
    }

    private void getNextPosition() { // copied from Player.java
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
    }

    public void update(){
        getNextPosition();
        checkMapCollision(0,0);
        setPosition(xtemp, ytemp);
        if (dx == 0 || dy == 0) setHit(true);
    }

    public void setMaxSpeed(double d){
        this.maxSpeed = d;
    }

    public void setMoveSpeed(double d){
        this.moveSpeed = d;
    }

    public void draw(Graphics2D g){
        g.drawString("§", (int)(x - cwidth/2), (int)(y + cheight/2));


//        Rectangle hitbox = getBox();
//        Color cTemp = g.getColor();
//        g.setColor(Color.GREEN);
//        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
//        g.setColor(cTemp);
    }

    public void setHit(boolean hit){
        this.hit = hit;
    }
    public boolean getHit(){
        return hit;
    }
}
