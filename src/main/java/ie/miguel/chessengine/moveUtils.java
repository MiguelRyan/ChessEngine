package ie.miguel.chessengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class moveUtils {
    public static boolean isLegalMove(Move move, Board board){
        PieceType piece = move.piece();
        if (move.fromSquare() == move.toSquare()) return false;

        if (piece == PieceType.WHITE_PAWN || piece == PieceType.BLACK_PAWN){
            return isLegalPawnMove(move, board);
        }
        if (piece == PieceType.WHITE_KNIGHT || piece == PieceType.BLACK_KNIGHT){
            return isLegalKnightMove(move);
        }
        if (piece == PieceType.WHITE_BISHOP || piece == PieceType.BLACK_BISHOP){
            return isLegalBishopMove(move);
        }
        if (piece == PieceType.WHITE_ROOK || piece == PieceType.BLACK_ROOK){
            return isLegalRookMove(move);
        }
        if (piece == PieceType.WHITE_QUEEN || piece == PieceType.BLACK_QUEEN){
            return isLegalRookMove(move) || isLegalBishopMove(move);
        }
        if (piece == PieceType.WHITE_KING || piece == PieceType.BLACK_KING){
            return isLegalKingMove(move, board);
        }

        return false;
    }

    private static boolean isLegalPawnMove(Move move, Board board){
        // TODO: En passant.
        int sign = 1;
        int pawnRankStart;
        int pawnRankEnd;

        // Reverses the direction based on the colour of the pawn.
        if (move.piece() == PieceType.WHITE_PAWN){
            pawnRankStart = 8;
            pawnRankEnd = 15;
        } else if (move.piece() == PieceType.BLACK_PAWN) {
            sign *= -1;
            pawnRankStart = 48;
            pawnRankEnd = 55;
        } else {
            throw new IllegalMoveException("Invalid move: " + move);
        }

        int fromSquare = move.fromSquare();
        int toSquare = move.toSquare();

        PieceType pieceAtToLocation = board.squareIsOccupied(toSquare);
        // Normal Move
        if (fromSquare + (8 * sign) == toSquare && pieceAtToLocation == null){
            return true;
        }

        // Double Jump
        if (fromSquare + (16 * sign) == toSquare && fromSquare >= pawnRankStart && fromSquare <= pawnRankEnd
                && pieceAtToLocation == null && board.squareIsOccupied(fromSquare + (8 * sign)) == null) {
            return true;
        }

        // Capture
        if ((fromSquare + (7 * sign) == toSquare || fromSquare + (9 * sign) == toSquare)
                && pieceAtToLocation != null && isEnemy(move, pieceAtToLocation)){
            return true;
        }

        return false;
    }

    private static boolean isLegalKnightMove(Move move){
        int from = move.fromSquare();
        int to = move.toSquare();

        List<Integer> validDifferences = Arrays.asList(-17, -15, -10, -6, 6, 10, 15, 17);
        for (int x: validDifferences){
            if (from + x == to){
                return true;
            }
        }

        return false;
    }

    private static boolean isLegalBishopMove(Move move){
        return isDiagonalMove(move);
    }

    private static boolean isLegalRookMove(Move move){
        return isStraightMove(move);
    }

    private static boolean isLegalKingMove(Move move, Board board){
        int from = move.fromSquare();
        int to = move.toSquare();

        List<Integer> valid = Arrays.asList(from - 7, from - 8, from - 9, from - 1, from + 1, from + 7, from + 8, from + 9);

        if (valid.contains(to) && board.squareIsOccupied(to) == null){
            return true;
        }
        throw new IllegalMoveException("Invalid move: " + move);
    }

    private static boolean isEnemy(Move move, PieceType enemy){
        PieceType piece = move.piece();
        String pieceString = String.valueOf(piece);
        String enemyString = String.valueOf(enemy);

        return enemyString.charAt(0) != pieceString.charAt(0);
    }

    private static boolean isStraightMove(Move move){
        int fromSquare = move.fromSquare();
        int toSquare = move.toSquare();

        return fromSquare / 8 == toSquare / 8 || fromSquare % 8 == toSquare % 8;
    }

    private static boolean isDiagonalMove(Move move){
        int rankDifference = (move.fromSquare() / 8) - (move.toSquare() / 8);
        int fileDifference = (move.fromSquare() % 8) - (move.toSquare() % 8);

        return Math.abs(rankDifference) == Math.abs(fileDifference);
    }
}
