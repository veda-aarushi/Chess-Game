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
            difficultyBox.setSelectedIndex(1); // Default: Medium

            ChessBoard board = new ChessBoard(difficultyBox.getSelectedIndex() + 1);
            JScrollPane historyScroll = new JScrollPane(board.getHistoryArea());

            JButton restartBtn = new JButton("Restart");
            restartBtn.addActionListener(e -> {
                frame.getContentPane().removeAll();
                ChessBoard newBoard = new ChessBoard(difficultyBox.getSelectedIndex() + 1);
                JScrollPane newScroll = new JScrollPane(newBoard.getHistoryArea());

                JPanel top = new JPanel(new BorderLayout());
                top.add(restartBtn, BorderLayout.WEST);
                top.add(difficultyBox, BorderLayout.EAST);
                top.add(newScroll, BorderLayout.CENTER);

                frame.add(top, BorderLayout.NORTH);
                frame.add(newBoard, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            });

            JPanel top = new JPanel(new BorderLayout());
            top.add(restartBtn, BorderLayout.WEST);
            top.add(difficultyBox, BorderLayout.EAST);
            top.add(historyScroll, BorderLayout.CENTER);

            frame.add(top, BorderLayout.NORTH);
            frame.add(board, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
