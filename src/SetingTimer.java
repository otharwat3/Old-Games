import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Validated countdown; restarting replaces the previous timer. */
public class SetingTimer extends JFrame {
    private final JLabel status = new JLabel("Set a break. Come back fresh.", SwingConstants.CENTER);
    private final JSpinner minutes = new JSpinner(new SpinnerNumberModel(15, 1, 1440, 1));
    private final Timer timer;
    private long deadline;
    public SetingTimer() {
        super("Session timer"); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel root = new JPanel(new GridLayout(3, 1, 12, 16)); root.setBorder(new EmptyBorder(24, 32, 24, 32));
        root.add(status); root.add(minutes); JButton start = new JButton("Start / restart countdown"); root.add(start);
        timer = new Timer(250, e -> {
            long seconds = Math.max(0, (deadline - System.nanoTime() + 999999999L) / 1000000000L);
            status.setText(String.format("%02d:%02d remaining", seconds / 60, seconds % 60));
            if (seconds == 0) { ((Timer)e.getSource()).stop(); JOptionPane.showMessageDialog(this, "Time for a break!"); }
        });
        start.addActionListener(e -> {
            try { minutes.commitEdit(); }
            catch (java.text.ParseException invalid) { status.setText("Enter 1–1440 whole minutes."); return; }
            deadline = System.nanoTime() + ((Number)minutes.getValue()).longValue() * 60000000000L; timer.restart();
        });
        setContentPane(root); setSize(420, 240); setLocationRelativeTo(null); setVisible(true);
    }
    @Override public void dispose() { timer.stop(); super.dispose(); }
}
