package CharMap;

import Entity.MapObjectCreator;
import Entity.Skeleton;
import Main.GameWindow;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import Entity.MapObject;

public class CharMap {
    private double x;
    private double y;

    private char[][] map;
    private String bgMap = "";

//    private ArrayList<MapObject> entities;
    private ArrayList<Skeleton> skeletons;

    // special characters
    public static final char SOLIDCHAR = '#'; // solid platform
    public static final char PLAYERCHAR = '@'; // player
    public static final char STAIRCHAR = '^'; // stairs (WIP)
    public static final char BREAKABLECHAR = '?'; // breakable (WIP)
    public static final char SKELETONCHAR = '%'; // skeleton (WIP)
    public static final char EXITRIGHTCHAR = '→'; // exit
    public static Character tokens[] = {
            PLAYERCHAR,
            STAIRCHAR,
            BREAKABLECHAR,
            SKELETONCHAR,
            EXITRIGHTCHAR,
    };
    private final Set<Character> tokenSet = new HashSet<>(Arrays.asList(tokens));

    private int playerX;
    private int playerY;

    private int exitX;
    private int exitY;

    public CharMap() {
//        this.tileSize = tileSize;
//
//        numRowsToDraw = GameWindow.HEIGHT / tileSize + 2;
//        numColsToDraw = GameWindow.WIDTH / tileSize + 2;
//        y = GameWindow.CHARSCALE * 4;
//        tween = 0.07;
        skeletons = new ArrayList<>();
    }

    public void loadMap(String s) {
        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            map = new char[24][80];

            for (int row = 0; row < 24; row++) {
                String temp = br.readLine();
                String[] tokens = temp.split("");
                for (int col = 0; col < 80; col++) {
                    map[row][col] = tokens[col].charAt(0);
                }
            }

            // remove special tokens from map, leave static objects only
            boolean playerFound = false;
            boolean exitFound = false;
            for (int row = 0; row < 24; row++) {
                for (int col = 0; col < 80; col++) {
                    char currentChar = map[row][col];
                    if (tokenSet.contains(currentChar)){ // special token detected!
                        MapObjectCreator moc = new MapObjectCreator();
                        if (!playerFound && currentChar == PLAYERCHAR) {
                            playerFound = true;
                            playerX = col;
                            playerY = row;
                        } else if (currentChar == SKELETONCHAR){
                            Skeleton newSkeleton = new Skeleton(this);
                            newSkeleton.setPosition(col * GameWindow.CHARSCALE + newSkeleton.getCWidth()/2, row * GameWindow.CHARSCALE + newSkeleton.getCHeight()/2);
                            skeletons.add(newSkeleton);
//                        entities.add(moc.getMapObject(currentChar)); // Create an entity based on current char.
                        } else if (!exitFound && currentChar == EXITRIGHTCHAR){
                            exitFound = true;
                            exitX = col;
                            exitY = row;
                        }
                        map[row][col] = ' '; // replace it by empty background
                    };
                }
            }

            // collapse map to single string(for ease of printing as background)
            for (int row = 0; row < 24; row++) {
                bgMap += String.copyValueOf(map[row]) + '\n';
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawStringNewline(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    public void draw(Graphics2D g){
        Color cTemp = g.getColor();
        g.setColor(Color.DARK_GRAY);
        drawStringNewline(g, bgMap, 0, (int)y);
        g.setColor(cTemp);
    }

    public char getChar(int row, int col) {
        return map[row][col];
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }
    public ArrayList<Skeleton> getSkeletons(){
        return skeletons;
    }
    public int getExitX(){
        return exitX;
    }
    public int getExitY(){
        return exitY;
    }
}
