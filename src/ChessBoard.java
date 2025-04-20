import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JPanel {
    public static final int SIZE = 8;
    private Square[][] board = new Square[SIZE][SIZE];
    private Square selectedSquare = null;
    private boolean whiteTurn = true;

    public ChessBoard() {
        this.setLayout(new GridLayout(SIZE, SIZE));
        boolean isWhite = false;

        for (int row = 0; row < SIZE; row++) {
            isWhite = !isWhite;
            for (int col = 0; col < SIZE; col++) {
                Square square = new Square(row, col, isWhite);
                board[row][col] = square;
                square.addActionListener(e -> handleClick(square));
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

    private void handleClick(Square clicked) {
        Piece clickedPiece = clicked.getPiece();

        if (selectedSquare == null) {
            if (clickedPiece != null && clickedPiece.isWhite() == whiteTurn) {
                selectedSquare = clicked;
                clicked.setBackground(Color.YELLOW);
            }
        } else {
            if (clicked == selectedSquare) {
                resetSquareColor(clicked);
                selectedSquare = null;
                return;
            }

            Piece selectedPiece = selectedSquare.getPiece();
            int startRow = selectedSquare.getRow();
            int startCol = selectedSquare.getCol();
            int endRow = clicked.getRow();
            int endCol = clicked.getCol();

            if (selectedPiece.isValidMove(startRow, startCol, endRow, endCol, getPieceMatrix())) {
                clicked.setPiece(selectedPiece);
                selectedSquare.setPiece(null);
                resetSquareColor(selectedSquare);
                resetSquareColor(clicked);
                whiteTurn = !whiteTurn;

                if (isKingInCheck(!whiteTurn)) {
                    if (isCheckmate(!whiteTurn)) {
                        JOptionPane.showMessageDialog(this, (whiteTurn ? "White" : "Black") + " wins by checkmate!");
                        disableBoard();
                    } else {
                        JOptionPane.showMessageDialog(this, (whiteTurn ? "Black" : "White") + " is in check!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid move!");
                resetSquareColor(selectedSquare);
            }

            selectedSquare = null;
        }
    }

    private void resetSquareColor(Square square) {
        int r = square.getRow();
        int c = square.getCol();
        square.setBackground((r + c) % 2 == 0 ? Color.WHITE : Color.GRAY);
    }

    private Piece[][] getPieceMatrix() {
        Piece[][] matrix = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = board[i][j].getPiece();
            }
        }
        return matrix;
    }

    private Square findKing(boolean isWhite) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece p = board[i][j].getPiece();
                if (p instanceof King && p.isWhite() == isWhite) {
                    return board[i][j];
                }
            }
        }
        return null;
    }

    private boolean isKingInCheck(boolean isWhiteKing) {
        Square kingSquare = findKing(isWhiteKing);
        if (kingSquare == null) return false;

        int kingRow = kingSquare.getRow();
        int kingCol = kingSquare.getCol();

        Piece[][] matrix = getPieceMatrix();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece attacker = matrix[i][j];
                if (attacker != null && attacker.isWhite() != isWhiteKing) {
                    if (attacker.isValidMove(i, j, kingRow, kingCol, matrix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(boolean defendingWhite) {
        Piece[][] matrix = getPieceMatrix();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Piece p = matrix[i][j];
                if (p != null && p.isWhite() == defendingWhite) {
                    for (int x = 0; x < SIZE; x++) {
                        for (int y = 0; y < SIZE; y++) {
                            if (p.isValidMove(i, j, x, y, matrix)) {
                                Piece backup = matrix[x][y];
                                matrix[x][y] = p;
                                matrix[i][j] = null;

                                boolean stillInCheck = isKingInCheck(defendingWhite);

                                matrix[i][j] = p;
                                matrix[x][y] = backup;

                                if (!stillInCheck) return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void disableBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].setEnabled(false);
            }
        }
    }
}
