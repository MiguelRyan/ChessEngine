package ie.miguel.chessengine;

public class moveCheck {
    public static boolean isLegalMove(Move move, Board board){
        PieceType piece = move.getPiece();

        if (piece == PieceType.WHITE_PAWN || piece == PieceType.BLACK_PAWN){
            return isLegalPawnMove(move, board);
        }

        return false;
    }

    private static boolean isLegalPawnMove(Move move, Board board){
        int sign = 1;
        int pawnRankStart;
        int pawnRankEnd;

        // Reverses the direction based on the colour of the pawn.
        if (move.getPiece() == PieceType.WHITE_PAWN){
            pawnRankStart = 8;
            pawnRankEnd = 15;
        } else if (move.getPiece() == PieceType.BLACK_PAWN) {
            sign *= -1;
            pawnRankStart = 48;
            pawnRankEnd = 55;
        } else {
            throw new IllegalArgumentException("Invalid move");
        }

        long fromSquare = move.getFromSquare();
        long toSquare = move.getToSquare();

        // Normal Move
        if (fromSquare + (8 * sign) == toSquare && !board.squareIsOccupied(toSquare)){
            return true;
        }

        // Double Jump
        if (fromSquare + (16 * sign) == toSquare && fromSquare >= pawnRankStart && fromSquare <= pawnRankEnd && !board.squareIsOccupied(toSquare)){
            return true;
        }

        return false;
    }
}
