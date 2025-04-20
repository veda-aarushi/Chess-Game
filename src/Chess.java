import javax.swing.*;

public class Chess {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Java Chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setResizable(false);

            ChessBoard board = new ChessBoard();
            frame.add(board);
            frame.setVisible(true);
        });
    }
}
