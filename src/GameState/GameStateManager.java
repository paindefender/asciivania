package GameState;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class GameStateManager {
    private ArrayList<GameState> gameStates;
    private int currentState;
    public String lvlPath;

    public static final int MENUSTATE = 0;
    public static final int SELECTORSTATE = 1;
    public static final int STAGESTATE = 2;
    public static final int LOSESTATE = 3;
    public static final int WINSTATE = 4;

    public GameStateManager() {
        gameStates = new ArrayList<GameState>();

        currentState = MENUSTATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new LevelSelectorState(this));
        gameStates.add(new StageState(this));
        gameStates.add(new LoseState(this));
        gameStates.add(new WinState(this));
    }

    public void setState(int state) {
        gameStates.get(state).init();
        currentState = state;
    }

    public void update() {
        gameStates.get(currentState).update();
    }

    public void draw(Graphics2D g) {
        gameStates.get(currentState).draw(g);
    }

    public void keyPressed(int k){ gameStates.get(currentState).keyPressed(k); }

    public void keyReleased(int k){
        gameStates.get(currentState).keyReleased(k);
    }

//    // from stackoverflow
//    public void playSound(){
//        try {
//            File yourFile;
//            AudioInputStream stream;
//            AudioFormat format;
//            DataLine.Info info;
////            Clip clip;
//
//            yourFile = new File(getClass().getResource("/sounds/Dead.wav").getFile());
//
////            stream = AudioSystem.getAudioInputStream(yourFile);
////            format = stream.getFormat();
////            info = new DataLine.Info(Clip.class, format);
////            clip = (Clip) AudioSystem.getLine(info);
////            clip.open(stream);
////            clip.start();
//            Clip clip = AudioSystem.getClip();
//            clip.open(AudioSystem.getAudioInputStream(yourFile));
//            clip.start();
//            Thread.sleep(5000);
//        }
//        catch (Exception e) {
//            //whatevers
//        }
//    }
}
