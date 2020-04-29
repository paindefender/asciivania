package GameState;

import Main.GameWindow;
import CharMap.CharMap;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LevelSelectorState extends GameState{
    private Font font;

    private int currentOption = 0;
    private ArrayList<String> options;

    public LevelSelectorState(GameStateManager gsm){
        this.gsm = gsm;

    }

    @Override
    public void init() {
        options = new ArrayList<>();
        try {
            for (String s : getResourceListing(getClass(), "maps")){
                options.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, GameWindow.WIDTH, GameWindow.HEIGHT);

        // options
        for (int i = 0; i < options.size(); i++) {
            if (i == currentOption) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawString(options.get(i), 5 * GameWindow.CHARSCALE, 5 * GameWindow.CHARSCALE + i * GameWindow.CHARSCALE * 2 );
        }
    }

    private void select() {
        gsm.lvlPath =  "/maps/" + options.get(currentOption);
        System.out.println(gsm.lvlPath);
        gsm.setState(gsm.STAGESTATE);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentOption--;
            if (currentOption == -1) currentOption = options.size() - 1;
        }
        if (k == KeyEvent.VK_DOWN) {
            currentOption++;
            if (currentOption == options.size()) currentOption = 0;
        }
    }
    public void keyReleased(int k) {

    }

    /** http://www.uofr.net/~greg/java/get-resource-listing.html
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException
     * @throws IOException
     */
    String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException, UnsupportedEncodingException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
            String me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    String entry = name.substring(path.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
    }
}
