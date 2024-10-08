package ie.miguel.chessengine.board;

import ie.miguel.chessengine.exception.IllegalMoveException;
import ie.miguel.chessengine.PieceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class legalMoveUtils {
    static boolean isLegalPawnMove(Move move, Board board){
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

    static boolean isLegalKnightMove(Move move){
        int from = move.fromSquare();
        int to = move.toSquare();

        // Get the row and column for both the from and to positions
        int fromRow = from / 8;
        int fromCol = from % 8;

        // The possible knight moves
        List<int[]> knightMoves = Arrays.asList(
                new int[] {-2, -1}, new int[] {-2, 1},  // Up-left, Up-right
                new int[] {2, -1}, new int[] {2, 1},    // Down-left, Down-right
                new int[] {-1, -2}, new int[] {-1, 2},  // Left-up, Right-up
                new int[] {1, -2}, new int[] {1, 2}     // Left-down, Right-down
        );

        // Check if any of the possible knight moves matches the destination
        for (int[] moveOffset : knightMoves) {
            int newRow = fromRow + moveOffset[0];
            int newCol = fromCol + moveOffset[1];

            // Check if the move stays within the bounds of the board
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                int possibleSquare = newRow * 8 + newCol;
                if (possibleSquare == to) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean isLegalBishopMove(Move move, Board board){
        return isDiagonalMove(move) && locationsInDiagonalPath(move, board).isEmpty();
    }

    static boolean isLegalRookMove(Move move, Board board){
        return isStraightMove(move) && locationsInStraightPath(move, board).isEmpty();
    }

    static boolean isLegalKingMove(Move move, Board board){
        int from = move.fromSquare();
        int to = move.toSquare();
        int rank = from / 8;
        int file = from % 8;

        List<Integer> valid = new ArrayList<>();

        // Valid relative locations
        //  7, 8, 9,
        // -1, K  1,
        // -9, -8, -7

        // TODO: There must be a more elegant solution to this.
        // Similar to how knight moves are checked.
        if (rank > 0){
            if (file > 0) valid.add(from - 9);
            if (file < 7) valid.add(from - 7);
            valid.add(from - 8);
        }
        if (rank < 8){
            if (file > 0) valid.add(from + 7);
            if (file < 7) valid.add(from + 9);
            valid.add(from + 8);
        }
        if (file > 0) valid.add(from - 1);
        if (file < 7) valid.add(from + 1);

        if (valid.contains(to) && board.squareIsOccupied(to) == null){
            return true;
        }
        return false;
    }

    private static boolean isEnemy(Move move, PieceType enemy){
        PieceType piece = move.piece();
        String pieceString = String.valueOf(piece);
        String enemyString = String.valueOf(enemy);

        return enemyString.charAt(0) != pieceString.charAt(0);
    }

    static boolean isStraightMove(Move move){
        int fromSquare = move.fromSquare();
        int toSquare = move.toSquare();

        return fromSquare / 8 == toSquare / 8 || fromSquare % 8 == toSquare % 8;
    }

    static boolean isDiagonalMove(Move move){
        int rankDifference = (move.fromSquare() / 8) - (move.toSquare() / 8);
        int fileDifference = (move.fromSquare() % 8) - (move.toSquare() % 8);

        return Math.abs(rankDifference) == Math.abs(fileDifference);
    }

    static HashSet<Integer> locationsInStraightPath(Move move, Board board){
        //TODO: I think this should throw the error in the else but that causes issues with testing,
        //TODO: when the diagonals are tested. This could also be done a lot more elegantly.
        HashSet<Integer> locations = new HashSet<>();
        int start = move.fromSquare();
        int end = move.toSquare();
        int startRank = start / 8;
        int startFile = start % 8;
        int endRank = end / 8;
        int endFile = end % 8;

        if (start > end){
            int temp = start;
            start = end;
            end = temp;
        }

        if (startRank == endRank){
            // Horizontal Move.
            for (int i = start + 1; i < end; i++){
                if (board.squareIsOccupied(i) != null){
                    locations.add(i);
                }
            }
        } else if (startFile == endFile){
            // Vertical Move.
            for (int i = start + 8; i < end; i+=8){
                if (board.squareIsOccupied(i) != null){
                    locations.add(i);
                }
            }

        } else {
            //throw new IllegalArgumentException("Non-straight path passed: " + start + " to " + end);
        }

        return locations;
    }

    static HashSet<Integer> locationsInDiagonalPath(Move move, Board board){
        int start = Math.min(move.fromSquare(), move.toSquare());
        int end = Math.max(move.fromSquare(), move.toSquare());

        int endAdjusted = end - start;
        int amount = endAdjusted % 9 == 0 ? 9 : 7;


        HashSet<Integer> locations = new HashSet<>();
        for (int i = start + amount; i < end && i >= 0; i += amount){
            if (board.squareIsOccupied(i) != null){
                locations.add(i);
            }
        }

        return locations;
    }
}
