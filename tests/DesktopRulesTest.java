import java.awt.Color;
import java.lang.reflect.Field;
import javax.swing.SwingUtilities;

/** Headless regressions against the real Swing Tetris board. */
public final class DesktopRulesTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                Tetris game = new Tetris(); game.init();
                for (int i=0;i<100;i++) game.move(-1);
                for (int i=0;i<100;i++) game.move(1);
                for (int i=0;i<1000;i++) { game.rotate(1); game.drop(); }
                game.init();
                Field field=Tetris.class.getDeclaredField("well"); field.setAccessible(true);
                Color[][] board=(Color[][])field.get(game);
                for(int y=20;y<=21;y++) for(int x=1;x<=10;x++) board[x][y]=Color.cyan;
                game.clearRows();
                if(game.score!=300) throw new AssertionError("Two rows should score 300");
                for(int x=1;x<=10;x++) if(board[x][0]!=Color.black) throw new AssertionError("Top row must clear");
                System.out.println("Desktop regression checks passed");
            } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
        });
    }
}
