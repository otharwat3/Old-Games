package Snake;/*
This class consists UI, Control, Logic & everything about Game.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener {
    static final int GameWidth = 700;
    static final int GameHeight = 700;
    static final int UNIT_Size = 35;
    static final int delay = 150;
    final int[] x = new int[GameWidth / UNIT_Size * (GameHeight / UNIT_Size)];
    final int[] y = new int[GameWidth / UNIT_Size * (GameHeight / UNIT_Size)];
    int bodyParts = 3;
    int fruitX, fruitY;
    char direction = 'R';
    boolean running;
    boolean turnQueued;
    int Score = 0;
    boolean dead = false;
    Timer timer;

    SnakePanel() {
        setPreferredSize(new Dimension(GameWidth, GameHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        setLayout(null);
        setBackground(new Color(230, 230, 250));
    }

    public void startGame() {
        if (timer != null) timer.stop();
        for (int i=0;i<bodyParts;i++) { x[i]=(bodyParts-i)*UNIT_Size; y[i]=UNIT_Size*3; }
        newfruit();
        dead = false;
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void restartGame() {
        timer.stop();
        Score = 0;
        bodyParts = 3;
        Arrays.fill(x, 0);
        Arrays.fill(y, 0);
        direction = 'R'; turnQueued = false;
        startGame();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;
        for (int i = 0; i < GameWidth / UNIT_Size + 1; i++) {
            for (int j = 0; j < GameHeight / UNIT_Size; j++) {
                if ((i + j) % 2 == 0)
                    gd.setColor(new Color(19,25,35));
                else
                    gd.setColor(new Color(23,30,41));
                gd.fillRect(i * UNIT_Size, j * UNIT_Size, UNIT_Size, UNIT_Size);
            }
        }
        gd.setColor(Color.black);
        gd.setStroke(new BasicStroke(3));
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                gd.setColor(new Color(184,246,89)); gd.fillRoundRect(x[i]+2,y[i]+2,31,31,8,8);
            } else {
                gd.setColor(new Color(103,177,65)); gd.fillRoundRect(x[i]+2,y[i]+2,31,31,8,8);
            }
        }
        gd.setColor(new Color(255,115,143)); gd.fillOval(fruitX+5,fruitY+5,25,25);
        if (running && !dead){
            gd.setColor(new Color(184,246,89));
            gd.setFont(new Font(Font.SERIF, Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            gd.drawString("Score: " + Score, (GameWidth - metrics.stringWidth("Score:10")) / 2, 30);

        }
        if (!running && !dead) {
            Start(g);
        }
        if (!running && dead) {
            Gameover(g);
        }
    }

    public void move() {
        turnQueued = false;
        for (int i = bodyParts - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'R' -> x[0] = x[0] + UNIT_Size;
            case 'L' -> x[0] = x[0] - UNIT_Size;
            case 'U' -> y[0] = y[0] - UNIT_Size;
            case 'D' -> y[0] = y[0] + UNIT_Size;
        }

    }

    public void newfruit() {
        Random random = new Random();
        boolean occupied;
        do {
            fruitX = random.nextInt(GameWidth / UNIT_Size) * UNIT_Size;
            fruitY = random.nextInt(GameHeight / UNIT_Size) * UNIT_Size;
            occupied = false;
            for (int i=0;i<bodyParts;i++) if (x[i]==fruitX && y[i]==fruitY) occupied=true;
        } while (occupied);
    }

    public void checkApple() {
        if (x[0] == fruitX && y[0] == fruitY) {
            if (bodyParts < x.length) {
                x[bodyParts] = x[bodyParts-1]; y[bodyParts] = y[bodyParts-1]; bodyParts++;
            }
            Score++;
            if (bodyParts == x.length) { running = false; dead = true; timer.stop(); }
            else newfruit();
        }

    }

    public void checkCollision() {
        for (int i = bodyParts - 1; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                dead = true;
            }
            if (x[0] >= GameWidth) {
                running = false;
                dead = true;
            }
            if (x[0] < 0) {
                running = false;
                dead = true;
            }
            if (y[0] < 0) {
                running = false;
                dead = true;
            }
            if (y[0] >= GameHeight) {
                running = false;
                dead = true;
            }
            if (!running) {
                timer.stop();
            }
        }
    }

    public void Gameover(Graphics g) {
        g.setColor(new Color(255,115,143));
        g.setFont(new Font("Georgia", Font.BOLD, 40));
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString("Press Space To Restart!", (GameWidth - fontMetrics.stringWidth("Press Space To Restart!")) / 2, (int) ((GameHeight - fontMetrics.stringWidth("Game Over")) / 1.5));
        g.drawString("Game Over!", (GameWidth - fontMetrics.stringWidth("Game Over")) / 2, ((GameHeight - fontMetrics.stringWidth("Game Over")) / 2));
    }
    public void Start(Graphics g) {
        g.setColor(new Color(184,246,89));
        g.setFont(new Font("Georgia", Font.BOLD, 40));
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString("Press Space To Start!", (GameWidth - fontMetrics.stringWidth("Press Space To Restart!")) / 2, (int) ((GameHeight - fontMetrics.stringWidth("Game Over")) / 1.5));
        g.drawString("Welcome!", (GameWidth - fontMetrics.stringWidth("Game Over")) / 2, ((GameHeight - fontMetrics.stringWidth("Game Over")) / 2));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            if (running) checkApple();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_SPACE && (!running || turnQueued)) return;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> { if (direction != 'R') { direction = 'L'; turnQueued = true; } }
                case KeyEvent.VK_RIGHT -> { if (direction != 'L') { direction = 'R'; turnQueued = true; } }
                case KeyEvent.VK_UP -> { if (direction != 'D') { direction = 'U'; turnQueued = true; } }
                case KeyEvent.VK_DOWN -> { if (direction != 'U') { direction = 'D'; turnQueued = true; } }
                case KeyEvent.VK_SPACE -> { if (!running) { if (dead) restartGame(); else startGame(); } }
            }
        }
    }
    @Override public void removeNotify() { if (timer != null) timer.stop(); super.removeNotify(); }
}
