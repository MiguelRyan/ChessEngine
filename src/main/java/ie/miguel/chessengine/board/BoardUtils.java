package ie.miguel.chessengine.board;

import ie.miguel.chessengine.PieceType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BoardUtils {
    public final static List<PieceType> whitePieces = Arrays.asList(PieceType.WHITE_PAWN, PieceType.WHITE_BISHOP, PieceType.WHITE_ROOK, PieceType.WHITE_KNIGHT, PieceType.WHITE_KING, PieceType.WHITE_QUEEN);
    public final static List<PieceType> blackPieces = Arrays.asList(PieceType.BLACK_PAWN, PieceType.BLACK_KNIGHT, PieceType.BLACK_KING, PieceType.BLACK_QUEEN, PieceType.BLACK_ROOK, PieceType.BLACK_BISHOP);

    public static HashSet<Integer> getPieceLocations(long bitboard){
        HashSet<Integer> locations = new HashSet<>();

        while (bitboard != 0) {
            int pieceLocation = Long.numberOfTrailingZeros(bitboard);
            locations.add(pieceLocation);
            bitboard &= (bitboard - 1);
        }

        return locations;
    }

    public static int findKingPosition(PieceType king, Board board){
        if (!(king == PieceType.WHITE_KING || king == PieceType.BLACK_KING)){
            throw new IllegalArgumentException("Non-King being passed to find king position.");
        }
        long bitboard = board.getPieceBitBoards().get(king);

        return Long.numberOfTrailingZeros(bitboard);
    }
}
