package ie.miguel.chessengine;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveTestUtils {
    public static void testLegalStraight(PieceType piece){
        // These tests should pass for pieces that are allowed to move in straight lines (Rook and Queen).
        Board board = new Board();
        board.clearBoard();

        board.placeNewPiece(piece, 32);
        Move right = new Move(piece, 32, 33);
        assertDoesNotThrow(() -> board.makeMove(right), right + " MOVE ONE RIGHT");

        Move twoRight = new Move(piece, 33, 35);
        assertDoesNotThrow(() -> board.makeMove(twoRight), twoRight + " MOVE TWO RIGHT");

        Move left = new Move(piece, 35, 34);
        assertDoesNotThrow(() -> board.makeMove(left), left + " MOVE ONE LEFT");

        Move twoLeft = new Move(piece, 34, 32);
        assertDoesNotThrow(() -> board.makeMove(twoLeft), twoLeft + " MOVE TWO LEFT");

        Move up = new Move(piece, 32, 40);
        assertDoesNotThrow(() -> board.makeMove(up), up + " MOVE UP");

        Move downTwo = new Move(piece, 40, 24);
        assertDoesNotThrow(() -> board.makeMove(downTwo), downTwo + " MOVE DOWN TWO");

        Move upTwo = new Move(piece, 24, 40);
        assertDoesNotThrow(() -> board.makeMove(upTwo), upTwo + " MOVE UP TWO");

        Move down = new Move(piece, 40, 32);
        assertDoesNotThrow(() -> board.makeMove(down), down + " MOVE TWO");

        // Ensure that the piece isn't jumping over other pieces.
        board.makeMove(twoRight); // Piece is now at 34

        // Surround piece to check it doesn't jump over pieces.
        board.placeNewPiece(PieceType.WHITE_PAWN, 33);
        board.placeNewPiece(PieceType.WHITE_PAWN, 35);
        board.placeNewPiece(PieceType.WHITE_PAWN, 42);
        board.placeNewPiece(PieceType.WHITE_PAWN, 26);

        assertThrows(IllegalArgumentException.class, () -> board.makeMove(upTwo), upTwo + " JUMPING OVER PIECE MOVING UP");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downTwo), downTwo + " JUMPING OVER PIECE MOVING DOWN");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoRight), twoRight + " JUMPING OVER PIECE MOVING RIGHT");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoLeft), twoLeft + " JUMPING OVER PIECE MOVING LEFT");
    }

    public static void testIllegalStraight(PieceType piece){
        // These tests should pass for pieces that do not move in straight lines.
        Board board = new Board();
        board.clearBoard();

        board.placeNewPiece(piece, 32);

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
        Board board = new Board();
        board.clearBoard();

        board.placeNewPiece(piece, 43);
        Move upRight = new Move(piece, 43, 61);
        assertDoesNotThrow(() -> board.makeMove(upRight), upRight + " MOVE UP RIGHT");

        Move downRight = new Move(piece, 61, 43);
        assertDoesNotThrow(() -> board.makeMove(downRight), downRight + " MOVE DOWN RIGHT");

        Move upLeft = new Move(piece, 43, 57);
        assertDoesNotThrow(() -> board.makeMove(upLeft), upLeft + " MOVE UP LEFT");

        Move downLeft = new Move(piece, 57, 43);
        assertDoesNotThrow(() -> board.makeMove(downLeft), downLeft + " MOVE DOWN LEFT");

        // Avoid jumping over pieces.
        board.placeNewPiece(PieceType.WHITE_PAWN, 34);
        board.placeNewPiece(PieceType.WHITE_PAWN, 36);
        board.placeNewPiece(PieceType.WHITE_PAWN, 50);
        board.placeNewPiece(PieceType.WHITE_PAWN, 52);

        assertThrows(IllegalMoveException.class, () -> board.makeMove(upRight), upRight + " MOVE UP RIGHT JUMPING OVER PIECE");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(upLeft), upLeft + " MOVE UP LEFT JUMPING OVER PIECE");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downLeft), downLeft + " MOVE DOWN LEFT JUMPING OVER PIECE");
        assertThrows(IllegalMoveException.class, () -> board.makeMove(downRight), downRight + " MOVE DOWN RIGHT JUMPING OVER PIECE");
    }

    public static void testIllegalDiagonal(PieceType piece){
        // These tests should pass for pieces that are not supposed to move diagonally.
        Board board = new Board();
        board.clearBoard();

        board.placeNewPiece(piece, 16);
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
        Board board = new Board();
        board.clearBoard();

        board.placeNewPiece(knight, 35);
        List<Integer> validPositionsFrom35 = Arrays.asList(18, 20, 25, 29, 41, 45, 50, 52);

        for (int i = 0; i <= 63; i++){
            Move testMove = new Move(knight, 35, i);
            if (validPositionsFrom35.contains(i)){
                assertDoesNotThrow(() -> board.makeMove(testMove), testMove + " NORMAL KNIGHT MOVE");
                Move reverseMove = new Move(knight, i, 35);
                assertDoesNotThrow(() -> board.makeMove(reverseMove), reverseMove + " NORMAL KNIGHT MOVE");
            } else {
                assertThrows(IllegalMoveException.class, () -> board.makeMove(testMove), testMove + " KNIGHT MAKING ILLEGAL MOVE");
            }
        }
    }

    public static void kingTest(PieceType king){
        // TODO: Checks Captures and Castling.
        if (!(king == PieceType.WHITE_KING || king == PieceType.BLACK_KING)){
            throw new IllegalArgumentException("Non-King being passed into King tests.");
        }
        Board board = new Board();
        board.clearBoard();
        board.placeNewPiece(king, 35);
        List<Integer> legalFrom35 = Arrays.asList(26, 27, 28, 34, 36, 42, 43, 44);

        // Basic Movement when nothing in the way.
        for (int i = 0; i <= 63; i++){
            Move move = new Move(king, 35, i);
            if (!legalFrom35.contains(i)) {
                assertThrows(IllegalMoveException.class, () -> board.makeMove(move), move + " KING MOVE MORE THAN 1 AWAY.");
            } else {
                assertDoesNotThrow(() -> board.makeMove(move), move + " MOVING 1 FAILS");
            }
        }

        // Make sure king can't move into own piece
        PieceType knight;
        if (king == PieceType.WHITE_KING){
            knight = PieceType.WHITE_KNIGHT;
        } else {
            knight = PieceType.BLACK_KNIGHT;
        }

        // Surround the king with knight of same colour shouldn't be able to move now.
        for (int i: legalFrom35){
            board.placeNewPiece(knight, i);
        }

        for (int i: legalFrom35){
            Move move = new Move(king, 35, i);
            assertThrows(IllegalMoveException.class, () -> board.makeMove(move), move + " KING MOVES INTO SAME COLOURED PIECE");
        }

        // Cannot Move off the edge.
        List<Integer> legalFrom00 = Arrays.asList(1, 8, 9);
        for (int i = 0; i <= 63; i++){
            Move move = new Move(king, 0, i);
            if (legalFrom00.contains(i)) {
                assertDoesNotThrow(() -> board.makeMove(move), move + " KING CANNOT MOVE WHEN IN CORNER");
            } else {
                assertThrows(IllegalMoveException.class, () -> board.makeMove(move), move +" KING MOVES OFF EDGE WHEN IN CORNER");
            }
        }
    }
}
