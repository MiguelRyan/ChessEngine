package ie.miguel.chessengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;
    @BeforeEach
    void setUp() {
        this.board = new Board();
    }

    @Test
    public void testWhitePawns(){
        // TODO: Tests for En Passant and Promotions. Capturing around the edge (i.e. 24 -> 39 for white).
        // Beginning Moves.
        Move oneStep = new Move(PieceType.WHITE_PAWN, 8, 16);
        Move twoStep = new Move(PieceType.WHITE_PAWN, 9, 25);
        assertDoesNotThrow(() -> board.makeMove(oneStep), oneStep + " FIRST ONE STEP");
        assertDoesNotThrow(() -> board.makeMove(twoStep), twoStep + " FIRST TWO STEP");

        // Illegal Moves.
        Move toSelf = new Move(PieceType.WHITE_PAWN, 15, 15);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(toSelf));

        Move twoStepAfterOne = new Move(PieceType.WHITE_PAWN, 16, 32);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoStepAfterOne), twoStepAfterOne + " TWO STEP AFTER ONE STEP");

        Move sideways = new Move(PieceType.WHITE_PAWN, 10, 11);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(sideways), sideways + " SIDEWAYS MOVE");

        Move backwards = new Move(PieceType.WHITE_PAWN, 12, 4);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(backwards), backwards + " BACKWARDS MOVE");

        board.placeNewPiece(PieceType.WHITE_PAWN, 23);
        Move moveOnToSelf = new Move(PieceType.WHITE_PAWN, 15, 23);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(moveOnToSelf), moveOnToSelf + " MOVING ONTO SELF");

        // Captures.
        Move takeSelf = new Move(PieceType.WHITE_PAWN, 16, 25);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeSelf), takeSelf + " TAKING OWN PAWN");

        board.placeNewPiece(PieceType.BLACK_PAWN, 32);
        board.placeNewPiece(PieceType.BLACK_PAWN, 33);
        board.placeNewPiece(PieceType.BLACK_PAWN, 34);

        Move takeEnemyRight = new Move(PieceType.WHITE_PAWN, 25, 34);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyRight), takeEnemyRight + " TAKING ENEMY ON RIGHT");

        Move takeEnemyLeft = new Move(PieceType.WHITE_PAWN, 25, 32);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyLeft), takeEnemyLeft + " TAKING ENEMY ON LEFT");

        Move takeEnemyInfront = new Move(PieceType.WHITE_PAWN, 25, 33);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeEnemyInfront), takeEnemyInfront + " TAKING ENEMY IN FRONT");
    }

    @Test
    public void testBlackPawns(){
        // TODO: Same as white pawns.
        // Beginning Moves.
        Move oneStep = new Move(PieceType.BLACK_PAWN, 48, 40);
        Move twoStep = new Move(PieceType.BLACK_PAWN, 49, 33);
        assertDoesNotThrow(() -> board.makeMove(oneStep), oneStep + " FIRST ONE STEP");
        assertDoesNotThrow(() -> board.makeMove(twoStep), twoStep + " FIRST TWO STEP");

        // Illegal Moves.
        Move toSelf = new Move(PieceType.BLACK_PAWN, 50, 50);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(toSelf));

        Move twoStepAfterOne = new Move(PieceType.BLACK_PAWN, 40, 24);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoStepAfterOne), twoStepAfterOne + " TWO STEP AFTER ONE STEP");

        Move sideways = new Move(PieceType.BLACK_PAWN, 50, 51);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(sideways), sideways + " SIDEWAYS MOVE");

        Move backwards = new Move(PieceType.BLACK_PAWN, 50, 58);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(backwards), backwards + " BACKWARDS MOVE");

        board.placeNewPiece(PieceType.BLACK_PAWN, 47);
        Move moveOnToSelf = new Move(PieceType.BLACK_PAWN, 55, 47);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(moveOnToSelf), moveOnToSelf + " MOVING ONTO SELF");


        // Captures.
        Move takeSelf = new Move(PieceType.BLACK_PAWN, 16, 25);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeSelf), takeSelf + " TAKING OWN PAWN");

        board.placeNewPiece(PieceType.WHITE_PAWN, 24);
        board.placeNewPiece(PieceType.WHITE_PAWN, 25);
        board.placeNewPiece(PieceType.WHITE_PAWN, 26);

        Move takeEnemyRight = new Move(PieceType.BLACK_PAWN, 33, 26);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyRight), takeEnemyRight + " TAKING ENEMY ON RIGHT");

        Move takeEnemyLeft = new Move(PieceType.BLACK_PAWN, 33, 24);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyLeft), takeEnemyLeft + " TAKING ENEMY ON LEFT");

        Move takeEnemyInfront = new Move(PieceType.BLACK_PAWN, 33, 25);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeEnemyInfront), takeEnemyInfront + " TAKING ENEMY IN FRONT");
    }
}