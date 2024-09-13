package ie.miguel.chessengine;

import java.util.HashSet;

public class BoardUtils {
    public static boolean isCheck(Board board){
        PieceType king;
        // If it is whites turn we want to know if the white king is in check.
        king = board.isWhiteToMove() ? PieceType.WHITE_KING : PieceType.BLACK_KING;
        board.findKingPosition(king);

        return false;
    }

    public static HashSet<Integer> getPieceLocations(long bitboard){
        HashSet<Integer> locations = new HashSet<>();

        while (bitboard != 0) {
            int pieceLocation = Long.numberOfTrailingZeros(bitboard);
            locations.add(pieceLocation);
            bitboard &= (bitboard - 1);
        }

        return locations;
    }
}
