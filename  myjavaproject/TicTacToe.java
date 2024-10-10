import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private JButton restartButton;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 3)); // Increased rows for the restart button

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].addActionListener(this);
                add(buttons[row][col]);
            }
        }

        // Restart button
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver)
            return;

        JButton buttonClicked = (JButton) e.getSource();
        if (buttonClicked.getText().equals("")) {
            buttonClicked.setText(String.valueOf(currentPlayer));
            if (checkForWin()) {
                JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!");
                gameOver = true;
                return;
            }
            if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "It's a draw!");
                gameOver = true;
                return;
            }
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch player
            if (currentPlayer == 'O') {
                computerMove(); // Call computer move
            }
        }
    }

    private void computerMove() {
        // Get the best move for the computer
        int[] bestMove = findBestMove();
        if (bestMove != null) {
            buttons[bestMove[0]][bestMove[1]].setText(String.valueOf(currentPlayer));
            if (checkForWin()) {
                JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!");
                gameOver = true;
            }
            if (isBoardFull() && !gameOver) {
                JOptionPane.showMessageDialog(this, "It's a draw!");
                gameOver = true;
            }
            currentPlayer = 'X'; // Switch back to player X
        }
    }

    private int[] findBestMove() {
        int bestValue = -1000; // Start with a very low value
        int[] bestMove = new int[] { -1, -1 }; // Initialize best move

        // Evaluate each cell
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    buttons[row][col].setText(String.valueOf(currentPlayer));
                    int moveValue = minimax(0, false); // Call minimax for evaluation
                    buttons[row][col].setText(""); // Undo move
                    if (moveValue > bestValue) {
                        bestMove[0] = row; // Update best move
                        bestMove[1] = col;
                        bestValue = moveValue; // Update best value
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMax) {
        if (checkForWin()) {
            return isMax ? -10 : 10; // Return score based on win condition
        }
        if (isBoardFull()) {
            return 0; // Draw
        }

        if (isMax) {
            int best = -1000; // Maximizing player (computer)
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (buttons[row][col].getText().equals("")) {
                        buttons[row][col].setText(String.valueOf(currentPlayer)); // Place 'O'
                        best = Math.max(best, minimax(depth + 1, !isMax)); // Recur
                        buttons[row][col].setText(""); // Undo move
                    }
                }
            }
            return best; // Return best value found
        } else {
            int best = 1000; // Minimizing player (human)
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (buttons[row][col].getText().equals("")) {
                        buttons[row][col].setText(String.valueOf('X')); // Place 'X'
                        best = Math.min(best, minimax(depth + 1, !isMax)); // Recur
                        buttons[row][col].setText(""); // Undo move
                    }
                }
            }
            return best; // Return best value found
        }
    }

    private boolean checkForWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[i][1].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[i][2].getText().equals(String.valueOf(currentPlayer))) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (buttons[0][i].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[1][i].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[2][i].getText().equals(String.valueOf(currentPlayer))) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[2][2].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }
        if (buttons[0][2].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[2][0].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void restartGame() {
        currentPlayer = 'X';
        gameOver = false;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe game = new TicTacToe();
            game.setVisible(true);
        });
    }
}
