package Snake;
import javax.swing.SwingUtilities;
public final class SnakeRulesTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SnakePanel game=new SnakePanel(); game.startGame(); game.timer.stop();
            if(game.x.length!=400) throw new AssertionError("Full board capacity");
            for(int i=1;i<game.bodyParts;i++) if(game.x[i]==game.x[0]&&game.y[i]==game.y[0]) throw new AssertionError("Overlapping spawn");
            game.x[0]=SnakePanel.GameWidth; game.checkCollision();
            if(!game.dead||game.running) throw new AssertionError("Right edge collision");
            game.restartGame();game.timer.stop();
            game.fruitX=game.x[0];game.fruitY=game.y[0];game.checkApple();
            if(game.bodyParts!=4||game.Score!=1) throw new AssertionError("Growth and score");
            for(int i=0;i<game.bodyParts;i++) if(game.fruitX==game.x[i]&&game.fruitY==game.y[i]) throw new AssertionError("Food inside snake");
            game.removeNotify();
            System.out.println("Snake regression checks passed");
        });
    }
}
