import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class RockPaperScissor extends JFrame {
    private final JLabel result = new JLabel("Choose your move", SwingConstants.CENTER);
    private final JLabel score = new JLabel("YOU  0 : 0  CPU", SwingConstants.CENTER);
    private int playerScore, computerScore;
    public void setup() {
        setTitle("Rock Paper Scissors"); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel root = new JPanel(new BorderLayout(20, 40)); root.setBorder(new EmptyBorder(40, 24, 40, 24));
        score.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28)); root.add(score, BorderLayout.NORTH);
        root.add(result); JPanel choices = new JPanel(new GridLayout(1, 3, 12, 12));
        for (String choice : new String[]{"Rock", "Paper", "Scissors"}) {
            JButton button = new JButton(choice); button.addActionListener(e -> calculate(choice)); choices.add(button);
        }
        root.add(choices, BorderLayout.SOUTH); setContentPane(root);
    }
    public void calculate(String player) {
        String[] choices = {"Rock", "Paper", "Scissors"};
        int computer = ThreadLocalRandom.current().nextInt(3), selected = java.util.Arrays.asList(choices).indexOf(player);
        if (selected < 0) throw new IllegalArgumentException("Unknown move: " + player);
        int outcome = Math.floorMod(selected - computer, 3);
        if (outcome == 1) playerScore++; else if (outcome == 2) computerScore++;
        result.setText("CPU chose " + choices[computer] + " · " + (outcome == 0 ? "Draw" : outcome == 1 ? "You win!" : "CPU wins"));
        score.setText("YOU  " + playerScore + " : " + computerScore + "  CPU");
    }
}
