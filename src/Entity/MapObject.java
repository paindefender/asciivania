package Entity;

import CharMap.CharMap;
import Main.GameWindow;

import java.awt.*;

public abstract class MapObject {
    // map and scrolling
    CharMap charMap;
    double xmap;
    double ymap;

    // object position and instantaneous speed
    double x;
    double y;
    double dx;
    double dy;

    int width;
    int height;
    int cwidth;
    int cheight;

    // collision detection
    int currRow;
    int currCol;
    double xdest;
    double ydest;
    double xtemp;
    double ytemp;
    boolean topLeft;
    boolean topRight;
    boolean bottomLeft;
    boolean bottomRight;

    boolean left;
    boolean right;
    boolean up;
    boolean down;
    boolean jumping;
    boolean falling;

    double maxSpeed;
    double moveSpeed;
    double stopSpeed;
    double fallSpeed;
    double maxFallSpeed;
    double jumpStart;
    double stopJumpSpeed;

    public MapObject(CharMap charMap){
        this.charMap = charMap;
    }

    public boolean intersects(MapObject m) {
        Rectangle b1 = getBox();
        Rectangle b2 = m.getBox();
        return b1.intersects(b2);
    }

    public Rectangle getBox(){
        return new Rectangle((int)x - cwidth/2, (int)y - cheight/2, cwidth, cheight);
    }

    public void checkCorners(double x, double y) {
        int left = (int)(x - cwidth/2) / GameWindow.CHARSCALE;
        int right = (int)(x + cwidth/2 - 1) / GameWindow.CHARSCALE;
        int top = (int)(y - cheight/2) / GameWindow.CHARSCALE;
        int bottom = (int)(y + cheight/2 - 1) / GameWindow.CHARSCALE;

        if (top < 0 || bottom >= 24 || left < 0 || right >= 80) {
            topLeft = topRight = bottomRight = bottomLeft = false;
            return;
        }

        char tl = charMap.getChar(top, left);
        char tr = charMap.getChar(top, right);
        char bl = charMap.getChar(bottom, left);
        char br = charMap.getChar(bottom, right);

        topLeft = tl == CharMap.SOLIDCHAR;
        topRight = tr == CharMap.SOLIDCHAR;
        bottomLeft = bl == CharMap.SOLIDCHAR;
        bottomRight = br == CharMap.SOLIDCHAR;
    }

    public void checkMapCollision(int rowAdjust, int colAdjust) { // trial and error hack
        currCol = (int)x / GameWindow.CHARSCALE;
        currRow = (int)y / GameWindow.CHARSCALE;

        xdest = x + dx;
        ydest = y + dy;

        xtemp = x;
        ytemp = y;

        checkCorners(x, ydest);
        if(dy < 0) {
            if(topLeft || topRight) {
                dy = 0;
                ytemp = (currRow - rowAdjust) * GameWindow.CHARSCALE + cheight / 2;
            }
            else {
                ytemp += dy;
            }
        }
        if(dy > 0) {
            if(bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1 + rowAdjust) * GameWindow.CHARSCALE - cheight / 2;
            }
            else {
                ytemp += dy;
            }
        }

        checkCorners(xdest, y);
        if(dx < 0) {
            if(topLeft || bottomLeft) {
                dx = 0;
                xtemp = (currCol - colAdjust) * GameWindow.CHARSCALE + cwidth / 2;
            }
            else {
                xtemp += dx;
            }
        }
        if(dx > 0) {
            if(topRight || bottomRight) {
                dx = 0;
                xtemp = (currCol + 1 + colAdjust) * GameWindow.CHARSCALE - cwidth / 2;
            }
            else {
                xtemp += dx;
            }
        }

        if(!falling) {
            checkCorners(x, ydest + 1);
            if(!bottomLeft && !bottomRight) {
                falling = true;
            }
        }
    }

    // Getters & Setters

    public int getX(){
        return (int)this.x;
    }

    public int getY(){
        return (int)this.y;
    }

    public int getCWidth(){
        return this.cwidth;
    }

    public int getCHeight(){
        return this.cheight;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setMapPosition(){
        this.xmap = this.charMap.getX();
        this.ymap = this.charMap.getY();
    }

    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean notOnScreen() {
        return x + xmap + width < 0 || x + xmap - width > GameWindow.WIDTH ||
                y + ymap + height < 0 || y + ymap - height > GameWindow.HEIGHT;
    }
}
