package Snake;

import javax.swing.*;
import javax.swing.JFrame;

public class SnakeFrame extends JFrame{
    public SnakeFrame() {
        this.add(new SnakePanel());
        this.setSize(750, 750);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setTitle("Snake · Arrows move · Space starts / restarts");
        this.setLocationRelativeTo(null);
    }
}
