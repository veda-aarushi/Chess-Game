import javax.swing.*;
import java.awt.*;

public class Chess {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Java Chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 800);
            frame.setResizable(false);

            String[] levels = {"Easy", "Medium", "Hard"};
            JComboBox<String> difficultyBox = new JComboBox<>(levels);
            difficultyBox.setSelectedIndex(1); // Default to Medium

            ChessBoard board = new ChessBoard(difficultyBox.getSelectedIndex() + 1);
            JScrollPane historyScroll = new JScrollPane(board.getHistoryArea());

            JButton restartBtn = new JButton("Restart");

            JPanel top = new JPanel(new BorderLayout());
            top.add(restartBtn, BorderLayout.WEST);
            top.add(difficultyBox, BorderLayout.EAST);
            top.add(historyScroll, BorderLayout.CENTER);

            JPanel timerPanel = new JPanel(new GridLayout(1, 2));
            timerPanel.add(board.getWhiteTimerLabel());
            timerPanel.add(board.getBlackTimerLabel());

            frame.add(top, BorderLayout.NORTH);
            frame.add(board, BorderLayout.CENTER);
            frame.add(timerPanel, BorderLayout.SOUTH);

            restartBtn.addActionListener(e -> {
                frame.getContentPane().removeAll();
                ChessBoard newBoard = new ChessBoard(difficultyBox.getSelectedIndex() + 1);
                JScrollPane newScroll = new JScrollPane(newBoard.getHistoryArea());

                JPanel newTop = new JPanel(new BorderLayout());
                newTop.add(restartBtn, BorderLayout.WEST);
                newTop.add(difficultyBox, BorderLayout.EAST);
                newTop.add(newScroll, BorderLayout.CENTER);

                JPanel newTimerPanel = new JPanel(new GridLayout(1, 2));
                newTimerPanel.add(newBoard.getWhiteTimerLabel());
                newTimerPanel.add(newBoard.getBlackTimerLabel());

                frame.add(newTop, BorderLayout.NORTH);
                frame.add(newBoard, BorderLayout.CENTER);
                frame.add(newTimerPanel, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
            });

            frame.setVisible(true);
        });
    }
}
