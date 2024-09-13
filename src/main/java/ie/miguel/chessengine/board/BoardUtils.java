package ie.miguel.chessengine.board;

import ie.miguel.chessengine.move.Move;
import ie.miguel.chessengine.PieceType;

import java.util.HashSet;

import static ie.miguel.chessengine.move.moveUtils.generateAllMoves;

public class BoardUtils {

    public static boolean isCheck(Board board){
        PieceType king;
        long bitboard;
        // If it is whites turn we want to know if the white king is in check.
        king = board.isWhiteToMove() ? PieceType.WHITE_KING : PieceType.BLACK_KING;
        int kingLocation = findKingPosition(king, board);

        HashSet<Move> possibleMoves = generateAllMoves(board);
        for (Move move : possibleMoves) {
            if (move.toSquare() == kingLocation) {
                return true;
            }
        }

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

    public static int findKingPosition(PieceType king, Board board){
        if (!(king == PieceType.WHITE_KING || king == PieceType.BLACK_KING)){
            throw new IllegalArgumentException("Non-King being passed to find king position.");
        }
        long bitboard = board.getPieceBitBoards().get(king);

        return Long.numberOfTrailingZeros(bitboard);
    }
}
