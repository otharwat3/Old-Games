package Pong;

import Pong.Ball;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GamePanel extends JPanel {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * 0.5555);
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    private final Timer loop;
    Image image;
    Graphics graphics;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    GamePanel(){
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        setBackground(new Color(12, 15, 24));
        loop = new Timer(16, e -> { move(); checkCollision(); repaint(); });
        addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { paddle1.yVelocity = 0; paddle2.yVelocity = 0; }
        });
    }
    public void newBall(){
        //random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), (GAME_HEIGHT/2)-(BALL_DIAMETER/2), BALL_DIAMETER, BALL_DIAMETER);
    }
    public void newPaddles(){
        paddle1 = new Paddle(0,(GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2),PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2),PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }
    @Override protected void paintComponent(Graphics g) { super.paintComponent(g); draw(g); }
    @Override public void addNotify() { super.addNotify(); loop.start(); requestFocusInWindow(); }
    @Override public void removeNotify() { loop.stop(); super.removeNotify(); }
    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision(){
        // stops ball of top & bottom
        if (ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT-BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);
        }
        //bounces
        if (ball.intersects(paddle1)){
            ball.xVelocity= Math.abs(ball.xVelocity);
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2)){
            ball.xVelocity= Math.abs(ball.xVelocity);
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(-ball.yVelocity);
        }
        // stops paddles at window edges
        if (paddle1.y <= 0)
            paddle1.y = 0;
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;

        if (paddle2.y <= 0)
            paddle2.y = 0;
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        // give points and create new balls
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
        }
        if (ball.x >= GAME_WIDTH-BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
        }
    }
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
