/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package connectfourgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFourGame extends JFrame {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int WINNING_COUNT = 4;
    private final JButton[][] boardButtons = new JButton[ROWS][COLS];
    private boolean playerOneTurn = true; // true for player one (red), false for player two (yellow)
    private final JPanel boardPanel = new JPanel();

    public ConnectFourGame() {
        setTitle("Connect Four");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel.setLayout(new GridLayout(ROWS, COLS));
        initializeBoard();
        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(100, 100));
                button.setBackground(Color.LIGHT_GRAY);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                button.addActionListener(new ButtonClickListener(row, col));
                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final int row;
        private final int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Find the lowest empty row in the column
            for (int r = ROWS - 1; r >= 0; r--) {
                if (boardButtons[r][col].getBackground() == Color.LIGHT_GRAY) {
                    boardButtons[r][col].setBackground(playerOneTurn ? Color.RED : Color.YELLOW);
                    if (checkForWin(r, col)) {
                        JOptionPane.showMessageDialog(ConnectFourGame.this, 
                            "Player " + (playerOneTurn ? "One (Red)" : "Two (Yellow)") + " wins!");
                        resetBoard();
                    } else if (isBoardFull()) {
                        JOptionPane.showMessageDialog(ConnectFourGame.this, "It's a draw!");
                        resetBoard();
                    }
                    playerOneTurn = !playerOneTurn;
                    break;
                }
            }
        }
    }

    private boolean checkForWin(int row, int col) {
        Color color = boardButtons[row][col].getBackground();
        return checkDirection(row, col, color, 1, 0) || // Horizontal
               checkDirection(row, col, color, 0, 1) || // Vertical
               checkDirection(row, col, color, 1, 1) || // Diagonal \
               checkDirection(row, col, color, 1, -1);  // Diagonal /
    }

    private boolean checkDirection(int row, int col, Color color, int deltaRow, int deltaCol) {
        int count = 1;
        count += countInDirection(row, col, color, deltaRow, deltaCol);
        count += countInDirection(row, col, color, -deltaRow, -deltaCol);
        return count >= WINNING_COUNT;
    }

    private int countInDirection(int row, int col, Color color, int deltaRow, int deltaCol) {
        int count = 0;
        int r = row + deltaRow;
        int c = col + deltaCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && boardButtons[r][c].getBackground() == color) {
            count++;
            r += deltaRow;
            c += deltaCol;
        }
        return count;
    }

    private boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (boardButtons[0][col].getBackground() == Color.LIGHT_GRAY) {
                return false;
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boardButtons[row][col].setBackground(Color.LIGHT_GRAY);
            }
        }
        playerOneTurn = true; // Player one starts the new game
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnectFourGame());
    }
}

