public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "♗" : "♝";
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        if (Math.abs(endRow - startRow) != Math.abs(endCol - startCol)) return false;

        int rowStep = Integer.compare(endRow, startRow);
        int colStep = Integer.compare(endCol, startCol);
        int r = startRow + rowStep, c = startCol + colStep;

        while (r != endRow && c != endCol) {
            if (board[r][c] != null) return false;
            r += rowStep;
            c += colStep;
        }

        Piece target = board[endRow][endCol];
        return target == null || target.isWhite() != isWhite;
    }
}
