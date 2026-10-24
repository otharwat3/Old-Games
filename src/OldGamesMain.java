import javax.swing.SwingUtilities;
import arcade.Theme;

public final class OldGamesMain {
    private OldGamesMain() {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { Theme.install(); new OldGames(); });
    }
}
