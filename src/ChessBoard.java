import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int SIZE = 8;
    private Square[][] board = new Square[SIZE][SIZE];

    public ChessBoard() {
        this.setLayout(new GridLayout(SIZE, SIZE));
        boolean isWhite = false;

        for (int row = 0; row < SIZE; row++) {
            isWhite = !isWhite;
            for (int col = 0; col < SIZE; col++) {
                Square square = new Square(row, col, isWhite);
                board[row][col] = square;
                this.add(square);
                isWhite = !isWhite;
            }
        }
        initializePieces();
    }

    private void initializePieces() {
        for (int col = 0; col < SIZE; col++) {
            board[1][col].setPiece(new Pawn(false));
            board[6][col].setPiece(new Pawn(true));
        }

        board[0][0].setPiece(new Rook(false));
        board[0][7].setPiece(new Rook(false));
        board[7][0].setPiece(new Rook(true));
        board[7][7].setPiece(new Rook(true));

        board[0][1].setPiece(new Knight(false));
        board[0][6].setPiece(new Knight(false));
        board[7][1].setPiece(new Knight(true));
        board[7][6].setPiece(new Knight(true));

        board[0][2].setPiece(new Bishop(false));
        board[0][5].setPiece(new Bishop(false));
        board[7][2].setPiece(new Bishop(true));
        board[7][5].setPiece(new Bishop(true));

        board[0][3].setPiece(new Queen(false));
        board[0][4].setPiece(new King(false));
        board[7][3].setPiece(new Queen(true));
        board[7][4].setPiece(new King(true));
    }
}