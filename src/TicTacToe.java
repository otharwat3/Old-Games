import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe implements ActionListener {

    JFrame frame;
    JPanel titlePanel, buttonPanel;
    JLabel textField;
    JButton[] buttons;
    JButton restartButton;
    boolean player1Turn;

    TicTacToe() {
        frame = new JFrame();
        titlePanel = new JPanel();
        buttonPanel = new JPanel();
        textField = new JLabel();
        buttons = new JButton[9];
        restartButton = new JButton("Restart");
        player1Turn = true;

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 640);
        frame.getContentPane().setBackground(arcade.Theme.BACKGROUND);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Tic-Tac-Toe · Two players");
        frame.setLocationRelativeTo(null);

        textField.setBackground(arcade.Theme.SURFACE);
        textField.setForeground(arcade.Theme.ACCENT);
        textField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setText("Tic-Tac-Toe");
        textField.setOpaque(true);

        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBounds(0, 0, 800, 100);

        buttonPanel.setLayout(new GridLayout(3, 3));
        buttonPanel.setBackground(arcade.Theme.BACKGROUND);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttonPanel.add(buttons[i]);
            buttons[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        restartButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        restartButton.setFocusable(false);
        restartButton.addActionListener(this);

        titlePanel.add(textField);
        titlePanel.add(restartButton, BorderLayout.EAST);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonPanel);

        firstTurn();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            restartGame();
        } else {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == buttons[i]) {
                    if (buttons[i].getText().equals("")) {
                        if (player1Turn) {
                            buttons[i].setForeground(new Color(255, 115, 143));
                            buttons[i].setText("X");
                            textField.setText("O turn");
                            player1Turn = false;
                        } else {
                            buttons[i].setForeground(new Color(165, 139, 250));
                            buttons[i].setText("O");
                            textField.setText("X turn");
                            player1Turn = true;
                        }
                        check();
                    }
                }
            }
        }
    }

    public void firstTurn() {
        if (Math.random() < 0.5) {
            player1Turn = true;
            textField.setText("X turn");
        } else {
            player1Turn = false;
            textField.setText("O turn");
        }
    }

    public void check() {
        // Check for X win conditions
        if (buttons[0].getText().equals("X") && buttons[1].getText().equals("X") && buttons[2].getText().equals("X")) {
            xWins(0, 1, 2);
        } else if (buttons[3].getText().equals("X") && buttons[4].getText().equals("X") && buttons[5].getText().equals("X")) {
            xWins(3, 4, 5);
        } else if (buttons[6].getText().equals("X") && buttons[7].getText().equals("X") && buttons[8].getText().equals("X")) {
            xWins(6, 7, 8);
        } else if (buttons[0].getText().equals("X") && buttons[3].getText().equals("X") && buttons[6].getText().equals("X")) {
            xWins(0, 3, 6);
        } else if (buttons[1].getText().equals("X") && buttons[4].getText().equals("X") && buttons[7].getText().equals("X")) {
            xWins(1, 4, 7);
        } else if (buttons[2].getText().equals("X") && buttons[5].getText().equals("X") && buttons[8].getText().equals("X")) {
            xWins(2, 5, 8);
        } else if (buttons[0].getText().equals("X") && buttons[4].getText().equals("X") && buttons[8].getText().equals("X")) {
            xWins(0, 4, 8);
        } else if (buttons[2].getText().equals("X") && buttons[4].getText().equals("X") && buttons[6].getText().equals("X")) {
            xWins(2, 4, 6);
        }
        // Check for O win conditions
        else if (buttons[0].getText().equals("O") && buttons[1].getText().equals("O") && buttons[2].getText().equals("O")) {
            oWins(0, 1, 2);
        } else if (buttons[3].getText().equals("O") && buttons[4].getText().equals("O") && buttons[5].getText().equals("O")) {
            oWins(3, 4, 5);
        } else if (buttons[6].getText().equals("O") && buttons[7].getText().equals("O") && buttons[8].getText().equals("O")) {
            oWins(6, 7, 8);
        } else if (buttons[0].getText().equals("O") && buttons[3].getText().equals("O") && buttons[6].getText().equals("O")) {
            oWins(0, 3, 6);
        } else if (buttons[1].getText().equals("O") && buttons[4].getText().equals("O") && buttons[7].getText().equals("O")) {
            oWins(1, 4, 7);
        } else if (buttons[2].getText().equals("O") && buttons[5].getText().equals("O") && buttons[8].getText().equals("O")) {
            oWins(2, 5, 8);
        } else if (buttons[0].getText().equals("O") && buttons[4].getText().equals("O") && buttons[8].getText().equals("O")) {
            oWins(0, 4, 8);
        } else if (buttons[2].getText().equals("O") && buttons[4].getText().equals("O") && buttons[6].getText().equals("O")) {
            oWins(2, 4, 6);
        }
        // Check for a tie
        else if (!buttons[0].getText().equals("") && !buttons[1].getText().equals("") && !buttons[2].getText().equals("") &&
                !buttons[3].getText().equals("") && !buttons[4].getText().equals("") && !buttons[5].getText().equals("") &&
                !buttons[6].getText().equals("") && !buttons[7].getText().equals("") && !buttons[8].getText().equals("")) {
            tie();
        }
    }

    public void xWins(int a, int b, int c) {
        buttons[a].setBackground(new Color(55, 95, 45));
        buttons[b].setBackground(new Color(55, 95, 45));
        buttons[c].setBackground(new Color(55, 95, 45));

        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(false);
        }

        textField.setText("X wins");
        restartButton.setEnabled(true);
    }

    public void oWins(int a, int b, int c) {
        buttons[a].setBackground(new Color(55, 95, 45));
        buttons[b].setBackground(new Color(55, 95, 45));
        buttons[c].setBackground(new Color(55, 95, 45));

        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(false);
        }

        textField.setText("O wins");
        restartButton.setEnabled(true);
    }

    public void tie() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setBackground(new Color(100, 80, 35));
            buttons[i].setEnabled(false);
        }

        textField.setText("It's a tie");
        restartButton.setEnabled(true);
    }

    public void restartGame() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(true);
            buttons[i].setText("");
            buttons[i].setBackground(null);
        }

        restartButton.setEnabled(false);
        firstTurn();
    }
}