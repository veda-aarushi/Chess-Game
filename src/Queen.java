public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "♕" : "♛";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        Rook fakeRook = new Rook(isWhite);
        Bishop fakeBishop = new Bishop(isWhite);
        return fakeRook.isValidMove(startRow, startCol, endRow, endCol, board) ||
                fakeBishop.isValidMove(startRow, startCol, endRow, endCol, board);
    }
}
