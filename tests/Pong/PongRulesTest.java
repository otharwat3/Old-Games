package Pong;
import javax.swing.SwingUtilities;
public final class PongRulesTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel game=new GamePanel();
            game.paddle1.y=-20;game.paddle2.y=1000;game.checkCollision();
            if(game.paddle1.y!=0||game.paddle2.y!=GamePanel.GAME_HEIGHT-GamePanel.PADDLE_HEIGHT) throw new AssertionError("Paddle bounds");
            game.ball.x=-1;game.checkCollision();
            if(game.score.player2!=1) throw new AssertionError("Left goal scoring");
            game.ball.x=GamePanel.GAME_WIDTH;game.checkCollision();
            if(game.score.player1!=1) throw new AssertionError("Right goal scoring");
            game.removeNotify();
            System.out.println("Pong regression checks passed");
        });
    }
}
