import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class TicTacToe {
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JButton[][] board = new JButton[4][4];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer, userSign, computerSign;
    boolean isUserTurn, gameOver = false;
    int turns = 0;
 
    TicTacToe() {
        setupUI();
        userSign = chooseSign();
        computerSign = userSign.equals(playerX) ? playerO : playerX;
        currentPlayer = userSign;
        isUserTurn = true;
        updateTextLabel();
    }
 
    void setupUI() {
        frame.setSize(600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
 
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(textLabel, BorderLayout.NORTH);
 
        JPanel boardPanel = new JPanel(new GridLayout(4, 4));
        frame.add(boardPanel);
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);
                tile.addActionListener(this::tileClicked);
                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);
    }
 
    String chooseSign() {
        Object[] options = {playerX, playerO};
        return (String) JOptionPane.showInputDialog(frame, "Choose your sign:", "Sign Selection",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
 
    void tileClicked(ActionEvent e) {
        if (gameOver || !isUserTurn) return;
        JButton tile = (JButton) e.getSource();
        if (tile.getText().equals("")) {
            tile.setText(currentPlayer);
            turns++;
            checkWinner();
            if (!gameOver) {
                switchPlayer();
                isUserTurn = false;
                computerMove();
            }
        }
    }
 
    void switchPlayer() {
        currentPlayer = currentPlayer.equals(userSign) ? computerSign : userSign;
        updateTextLabel();
    }
 
    void updateTextLabel() {
        textLabel.setText((isUserTurn ? "Your turn" : "Computer's turn") + " (" + currentPlayer + ")");
    }
 
    void computerMove() {
        if (!gameOver) {
            int[] move = minimax(4, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (move[1] != -1) {
                board[move[1]][move[2]].setText(computerSign);
                turns++;
                checkWinner();
                if (!gameOver) {
                    switchPlayer();
                    isUserTurn = true;
                }
            }
        }
    }
 
    int[] minimax(int depth, int alpha, int beta, boolean isMaximizing) {
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {bestScore, -1, -1};
 
        if (depth == 0 || gameOver) {
            bestScore = evaluateBoard();
            return new int[]{bestScore, -1, -1};
        }
 
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c].getText().equals("")) {
                    board[r][c].setText(isMaximizing ? computerSign : userSign);
                    turns++;
                    int score = minimax(depth - 1, alpha, beta, !isMaximizing)[0];
                    board[r][c].setText("");
                    turns--;
 
                    if (isMaximizing) {
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[]{bestScore, r, c};
                        }
                        alpha = Math.max(alpha, bestScore);
                    } else {
                        if (score < bestScore) {
                            bestScore = score;
                            bestMove = new int[]{bestScore, r, c};
                        }
                        beta = Math.min(beta, bestScore);
                    }
 
                    if (beta <= alpha) break;
                }
            }
        }
        return bestMove;
    }
 
    int evaluateBoard() {
        if (checkWinnerMinimax(computerSign)) return 1;
        if (checkWinnerMinimax(userSign)) return -1;
        if (turns == 16) return 0;
        return 0;
    }
 
    boolean checkWinnerMinimax(String sign) {
        for (int r = 0; r < 4; r++) {
            if (board[r][0].getText().equals(sign) && board[r][1].getText().equals(sign) &&
                board[r][2].getText().equals(sign) && board[r][3].getText().equals(sign)) return true;
        }
        for (int c = 0; c < 4; c++) {
            if (board[0][c].getText().equals(sign) && board[1][c].getText().equals(sign) &&
                board[2][c].getText().equals(sign) && board[3][c].getText().equals(sign)) return true;
        }
        if (board[0][0].getText().equals(sign) && board[1][1].getText().equals(sign) &&
            board[2][2].getText().equals(sign) && board[3][3].getText().equals(sign)) return true;
        if (board[0][3].getText().equals(sign) && board[1][2].getText().equals(sign) &&
            board[2][1].getText().equals(sign) && board[3][0].getText().equals(sign)) return true;
        return false;
    }
 
    void checkWinner() {
        if (checkWinnerMinimax(currentPlayer)) {
            gameOver = true;
            textLabel.setText((currentPlayer.equals(userSign) ? "You win!" : "Computer wins!"));
        } else if (turns == 16) {
            gameOver = true;
            textLabel.setText("It's a tie!");
        }
    }
 
    public static void main(String[] args) {
        new TicTacToe();
    }
}