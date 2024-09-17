package ie.miguel.chessengine;

import ie.miguel.chessengine.board.Board;
import ie.miguel.chessengine.exception.IllegalMoveException;
import ie.miguel.chessengine.board.Move;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveTestUtils {
    public static void testLegalStraight(PieceType piece){
        // These tests should pass for pieces that are allowed to move in straight lines (Rook and Queen).
        Board board = new Board(piece);
        board.clearBoard();

        board.placePiece(piece, 35);
        Move right = new Move(piece, 35, 36);
        assertDoesNotThrow(() -> board.makeMove(right), right + " MOVE ONE RIGHT");

        Move twoRight = new Move(piece, 36, 38);
        assertDoesNotThrow(() -> board.makeMove(twoRight), twoRight + " MOVE TWO RIGHT");

        Move left = new Move(piece, 38, 37);
        assertDoesNotThrow(() -> board.makeMove(left), left + " MOVE ONE LEFT");

        Move twoLeft = new Move(piece, 37, 35);
        assertDoesNotThrow(() -> board.makeMove(twoLeft), twoLeft + " MOVE TWO LEFT");

        Move up = new Move(piece, 35, 43);
        assertDoesNotThrow(() -> board.makeMove(up), up + " MOVE UP");

        Move downTwo = new Move(piece, 43, 27);
        assertDoesNotThrow(() -> board.makeMove(downTwo), downTwo + " MOVE DOWN TWO");

        Move upTwo = new Move(piece, 27, 43);
        assertDoesNotThrow(() -> board.makeMove(upTwo), upTwo + " MOVE UP TWO");

        Move down = new Move(piece, 43, 35);
        assertDoesNotThrow(() -> board.makeMove(down), down + " MOVE TWO");

        // Ensure that the piece isn't jumping over other pieces.

        // Surround piece to check it doesn't jump over pieces.
        board.placePiece(PieceType.WHITE_PAWN, 34);
        board.placePiece(PieceType.WHITE_PAWN, 36);
        board.placePiece(PieceType.WHITE_PAWN, 27);
        board.placePiece(PieceType.WHITE_PAWN, 43);

        Move downTwo2 = new Move(piece, 35, 19);
        Move upTwo2 = new Move(piece, 35, 51);
        Move rightTwo2 = new Move(piece, 35, 37);
        Move leftTwo2 = new Move(piece, 35, 33);

        assertThrows(IllegalMoveException.class, () -> board.makeMove(upTwo2), upTwo2 + " JUMPING OVER PIECE MOVING UP");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downTwo2), downTwo2 + " JUMPING OVER PIECE MOVING DOWN");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(rightTwo2), rightTwo2 + " JUMPING OVER PIECE MOVING RIGHT");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(leftTwo2), leftTwo2 + " JUMPING OVER PIECE MOVING LEFT");
    }

    public static void testIllegalStraight(PieceType piece){
        // These tests should pass for pieces that do not move in straight lines.
        Board board = new Board(piece);
        board.clearBoard();

        board.placePiece(piece, 32);

        Move twoRight = new Move(piece, 33, 35);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoRight), twoRight + " MOVE TWO RIGHT");

        Move twoLeft = new Move(piece, 34, 32);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoLeft), twoLeft + " MOVE TWO LEFT");

        Move downTwo = new Move(piece, 40, 24);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downTwo), downTwo + " MOVE DOWN TWO");

        Move upTwo = new Move(piece, 24, 40);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(upTwo), upTwo + " MOVE UP TWO");
    }

    public static void testLegalDiagonal(PieceType piece){
        // These tests should pass for pieces that are allowed to move diagonally.
        Board board = new Board(piece);
        board.clearBoard();

        board.placePiece(piece, 43);
        Move upRight = new Move(piece, 43, 61);
        assertDoesNotThrow(() -> board.makeMove(upRight), upRight + " MOVE UP RIGHT");

        Move downRight = new Move(piece, 61, 43);
        assertDoesNotThrow(() -> board.makeMove(downRight), downRight + " MOVE DOWN RIGHT");

        Move upLeft = new Move(piece, 43, 57);
        assertDoesNotThrow(() -> board.makeMove(upLeft), upLeft + " MOVE UP LEFT");

        Move downLeft = new Move(piece, 57, 43);
        assertDoesNotThrow(() -> board.makeMove(downLeft), downLeft + " MOVE DOWN LEFT");

        board.removePiece(piece, 43);
        board.placePiece(piece, 35);
        // Avoid jumping over pieces.
        board.placePiece(PieceType.WHITE_PAWN, 42);
        board.placePiece(PieceType.WHITE_PAWN, 44);
        board.placePiece(PieceType.WHITE_PAWN, 26);
        board.placePiece(PieceType.WHITE_PAWN, 28);

        Move upRight2 = new Move(piece, 35, 53);
        Move downRight2 = new Move(piece, 35, 21);
        Move upLeft2 = new Move(piece, 35, 49);
        Move downLeft2 = new Move(piece, 35, 17);

        assertThrows(IllegalMoveException.class, () -> board.makeMove(upRight2), upRight2 + " MOVE UP RIGHT JUMPING OVER PIECE");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(upLeft2), upLeft2 + " MOVE UP LEFT JUMPING OVER PIECE");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downLeft2), downLeft2 + " MOVE DOWN LEFT JUMPING OVER PIECE");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downRight2), downRight2 + " MOVE DOWN RIGHT JUMPING OVER PIECE");
    }

    public static void testIllegalDiagonal(PieceType piece){
        // These tests should pass for pieces that are not supposed to move diagonally.
        Board board = new Board(piece);
        board.clearBoard();

        board.placePiece(piece, 16);
        Move upRight = new Move(piece, 16, 43);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(upRight), upRight + " MOVE UP RIGHT");

        Move downRight = new Move(piece, 43, 22);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downRight), downRight + " MOVE DOWN RIGHT");

        Move upLeft = new Move(piece, 22, 43);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(upLeft), upLeft + " MOVE UP LEFT");

        Move downLeft = new Move(piece, 43, 16);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downLeft), downLeft + " MOVE DOWN LEFT");
    }

    public static void testKnightMoves(PieceType knight){
        if (!(knight == PieceType.WHITE_KNIGHT || knight == PieceType.BLACK_KNIGHT)){
            throw new IllegalArgumentException("Non-King being passed into King tests.");
        }

        // Normal Knight with 8 valid positions.
        List<Integer> validPositionsFrom35 = Arrays.asList(18, 20, 25, 29, 41, 45, 50, 52);
        testBasedOnValid(knight, validPositionsFrom35,35);

        // Knight in corner with only 2 valid positions.
        List<Integer> validPositionsFrom0 = Arrays.asList(10, 17);
        testBasedOnValid(knight, validPositionsFrom0, 0);

        // Knight on edge with only 4 valid positions.
        List<Integer> validPositionsFrom39 = Arrays.asList(54, 45, 29, 22);
        testBasedOnValid(knight, validPositionsFrom39, 39);

        // Knight on edge with only 3 valid positions.
        List<Integer> validPositionsFrom15 = Arrays.asList(5, 21, 30);
        testBasedOnValid(knight, validPositionsFrom15, 15);
    }

    private static void testBasedOnValid(PieceType piece, List<Integer> validPositions, int position) {
        // This function takes a piece, an array of where it is allowed to go, and the staring position.
        // It then checks that the piece can ONLY move to legal positions and not move to illegal positions.
        Board board = new Board(piece);
        board.clearBoard();
        board.placePiece(piece, position);

        for (int i = 0; i <= 63; i++){
            Move testMove = new Move(piece, position, i);
            if (validPositions.contains(i)){
                assertDoesNotThrow(() -> board.makeMove(testMove), testMove + " NORMAL MOVE");
                Move reverseMove = new Move(piece, i, position);
                assertDoesNotThrow(() -> board.makeMove(reverseMove), reverseMove + " NORMAL MOVE");
            } else {
                assertThrows(IllegalMoveException.class, () -> board.makeMove(testMove), testMove + " MAKING ILLEGAL MOVE");
            }
        }
        board.removePiece(piece, position);
    }

    public static void kingTest(PieceType king){
        // TODO: Checks Captures and Castling.
        if (!(king == PieceType.WHITE_KING || king == PieceType.BLACK_KING)){
            throw new IllegalArgumentException("Non-King being passed into King tests.");
        }
        Board board = new Board(king);
        board.clearBoard();
        board.placePiece(king, 35);

        List<Integer> legalFrom35 = Arrays.asList(26, 27, 28, 34, 36, 42, 43, 44);
        List<Integer> legalFrom00 = Arrays.asList(1, 8, 9);
        List<Integer> legalFrom56 = Arrays.asList(48, 49, 57);
        List<Integer> legalFrom63 = Arrays.asList(54, 55, 62);
        List<Integer> legalFrom07 = Arrays.asList(6, 14, 15);
        List<Integer> legalFrom31 = Arrays.asList(22, 23, 30, 38, 39);

        testBasedOnValid(king, legalFrom35,35);
        testBasedOnValid(king, legalFrom00,0);
        testBasedOnValid(king, legalFrom56,56);
        testBasedOnValid(king, legalFrom63,63);
        testBasedOnValid(king, legalFrom07,07);
        testBasedOnValid(king, legalFrom31,31);

        // Make sure king can't move into own piece
        PieceType knight;
        if (king == PieceType.WHITE_KING){
            knight = PieceType.WHITE_KNIGHT;
        } else {
            knight = PieceType.BLACK_KNIGHT;
        }

        // Surround the king with knight of same colour shouldn't be able to move now.
        for (int i: legalFrom35){
            board.placePiece(knight, i);
        }

        for (int i: legalFrom35){
            Move move = new Move(king, 35, i);
            assertThrows(IllegalMoveException.class, () -> board.makeMove(move), move + " KING MOVES INTO SAME COLOURED PIECE");
        }
    }
}
