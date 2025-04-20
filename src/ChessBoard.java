import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int SIZE = 8;

    public ChessBoard() {
        this.setLayout(new GridLayout(SIZE, SIZE));
        boolean isWhite = false;

        for (int row = 0; row < SIZE; row++) {
            isWhite = !isWhite;
            for (int col = 0; col < SIZE; col++) {
                Square square = new Square(row, col, isWhite);
                this.add(square);
                isWhite = !isWhite;
            }
        }
    }
}
