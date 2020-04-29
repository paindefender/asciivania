package Entity;

import CharMap.CharMap;
import Main.GameWindow;

import java.awt.*;

public class Exit extends MapObject{
    public Exit(CharMap charMap){
        super(charMap);
        cwidth = 1 * GameWindow.CHARSCALE;
        cheight = 4 * GameWindow.CHARSCALE;
    }

    public void update(){

    }

    @Override
    public Rectangle getBox(){
        return new Rectangle((int)x - cwidth, (int)y - cheight/2, cwidth, cheight);
    }

    public void draw(Graphics2D g){
        Color cTemp = g.getColor();
        g.setColor(Color.PINK);
        CharMap.drawStringNewline(g, "→\n→\n→\n→", (int)(x - cwidth/2), (int)(y + charMap.getY() - cheight/2));
        g.setColor(cTemp);

//        Rectangle hitbox = getBox();
//        cTemp = g.getColor();
//        g.setColor(Color.GREEN);
//        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
//        g.setColor(cTemp);
    }
}
