package Main;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        JFrame window = new JFrame("asciivania: symphony of blood");
        window.setContentPane(new GameWindow());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
