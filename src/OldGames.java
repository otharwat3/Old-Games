import Pong.GameFrame;
import Snake.SnakeFrame;
import arcade.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

/** Desktop library. Each launch owns its window and releases its timer on disposal. */
public class OldGames {
    private final JFrame frame = new JFrame("Old Games / Arcade Collection");
    private final List<JButton> games = new ArrayList<>();
    public OldGames() {
        JPanel root = new JPanel(new BorderLayout(24, 24));
        root.setBackground(Theme.BACKGROUND);
        root.setBorder(new EmptyBorder(32, 36, 32, 36));
        JLabel heading = new JLabel("<html><font color='#b8f659' size='3'>INSERT COIN · STAY AWHILE</font><br>OLD GAMES<br><font size='4'>Five classics. One more round.</font></html>");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        root.add(heading, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 2, 16, 16));
        grid.setOpaque(false);
        addGame(grid, "01  TETRIS", "Stack blocks · Arrows / Space", this::tetris);
        addGame(grid, "02  PONG", "Two players · W S / ↑ ↓", GameFrame::new);
        addGame(grid, "03  SNAKE", "Chase the next apple · Arrows / Space", SnakeFrame::new);
        addGame(grid, "04  TIC-TAC-TOE", "Two players · Three in a row", () -> new TicTacToe().frame);
        addGame(grid, "05  ROCK PAPER SCISSORS", "You vs. the machine", () -> {
            RockPaperScissor game = new RockPaperScissor();
            game.setup(); game.setSize(640, 520); return game;
        });
        JButton random = new JButton("SURPRISE ME  ↗");
        random.addActionListener(e -> {
            List<JButton> available = new ArrayList<>();
            for (JButton button : games) if (button.isEnabled()) available.add(button);
            if (!available.isEmpty()) available.get(new Random().nextInt(available.size())).doClick();
        });
        grid.add(random); root.add(grid);
        JPanel footer = new JPanel(new BorderLayout(12, 12)); footer.setOpaque(false);
        String[] objectives = {"Clear four lines in Tetris.", "Play Tic-Tac-Toe with a friend.", "Score ten points in Pong.", "Eat ten apples in Snake.", "Win three rounds of Rock Paper Scissors."};
        footer.add(new JLabel("DAILY QUEST / " + objectives[Math.floorMod(LocalDate.now().toEpochDay(), objectives.length)]));
        JButton timer = new JButton("Session timer");
        timer.addActionListener(e -> new SetingTimer()); footer.add(timer, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);
        frame.setContentPane(root); frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 620)); frame.setSize(960, 720);
        frame.setLocationRelativeTo(null); frame.setVisible(true);
    }
    private void addGame(JPanel grid, String title, String subtitle, Supplier<JFrame> launch) {
        JButton button = new JButton("<html><font size='5'>" + title + "</font><br><br>" + subtitle + "</html>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(52, 61, 80)), new EmptyBorder(20, 20, 20, 20)));
        button.setToolTipText(subtitle); games.add(button); grid.add(button);
        button.addActionListener(e -> {
            JFrame window = launch.get(); window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setLocationRelativeTo(frame);
            window.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent event) { button.setEnabled(true); }
            });
            button.setEnabled(false); window.setVisible(true);
        });
    }
    private JFrame tetris() {
        JFrame window = new JFrame("Tetris · Arrows move / rotate · Space drops · R restarts");
        Tetris game = new Tetris(); game.init(); window.add(game);
        window.setSize(330, 650); window.setResizable(false);
        javax.swing.Timer loop = new javax.swing.Timer(650, e -> game.drop()); loop.start();
        window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { loop.stop(); }
        });
        window.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> game.move(-1);
                    case KeyEvent.VK_RIGHT -> game.move(1);
                    case KeyEvent.VK_UP -> game.rotate(1);
                    case KeyEvent.VK_DOWN, KeyEvent.VK_SPACE -> game.drop();
                    case KeyEvent.VK_R -> game.init();
                }
            }
        });
        return window;
    }
}
