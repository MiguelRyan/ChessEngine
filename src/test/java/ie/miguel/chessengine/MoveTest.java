package ie.miguel.chessengine;

import ie.miguel.chessengine.board.Board;
import ie.miguel.chessengine.exception.IllegalMoveException;
import ie.miguel.chessengine.exception.OutOfOrderMoveException;
import ie.miguel.chessengine.board.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ie.miguel.chessengine.MoveTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

// TODO: Tests for capturing, moving off edge, not moving if king is in check.
class MoveTest {
    Board board;
    @BeforeEach
    void setUp() {
        this.board = new Board();
    }

    @Test
    public void testNotSelfMove(){
        // Tests that all pieces cannot move to themselves.
        for (PieceType piece : PieceType.values()) {
            // TODO: Instead of initilizing a new board it would be better to jsut remove the piece and then continue.
            Board board = new Board(piece);
            board.clearBoard();
            board.placePiece(piece, 32);
            Move nonMoveMove = new Move(piece, 32, 32);
            assertThrows(IllegalMoveException.class, () -> board.makeMove(nonMoveMove), piece + " ABLE TO MOVE TO OWN POSITION");
        }
    }

    @Test
    public void testPieceDoesNotMoveIntoCheckmate(){
        for (PieceType piece : PieceType.values()) {
            if (piece == PieceType.WHITE_KING || piece == PieceType.BLACK_KING) {
                continue;
            }
            char colour = String.valueOf(piece).charAt(0);
            PieceType enemy = colour == 'W' ? PieceType.BLACK_ROOK : PieceType.WHITE_ROOK;
            PieceType king = colour == 'W' ? PieceType.WHITE_KING : PieceType.BLACK_KING;

            Board board = new Board(piece);
            board.clearBoard();

            board.placePiece(king, 35);
            board.placePiece(piece, 34);
            board.placePiece(enemy, 33);

            // Check that the piece cannot move.
            for (int i = 0; i <= 63; i++) {
                if (i == 33) continue; // This is when the piece is able to capture.
                Move move = new Move(piece, 34, i);
                assertThrows(IllegalMoveException.class, () -> board.makeMove(move), move + " allows king to be captured.");
            }
        }
    }

    @Test
    public void testCannotMoveOutOfTurn(){
        Move whiteMove = new Move(PieceType.WHITE_PAWN, 8, 16);
        Move blackMove = new Move(PieceType.BLACK_PAWN, 48, 40);

        assertThrows(OutOfOrderMoveException.class, () -> board.makeMove(blackMove));
        assertDoesNotThrow(() -> board.makeMove(whiteMove));
        assertThrows(OutOfOrderMoveException.class, () -> board.makeMove(whiteMove));
        assertDoesNotThrow(() -> board.makeMove(blackMove));
    }

    @Test
    public void testWhitePawns(){
        // TODO: Tests for En Passant and Promotions. Capturing around the edge (i.e. 24 -> 39 for white).
        // TODO: Refactor this and conjoin with Black tests.
        // Beginning Moves.
        Board board = new Board(PieceType.WHITE_PAWN);
        Move oneStep = new Move(PieceType.WHITE_PAWN, 8, 16);
        Move twoStep = new Move(PieceType.WHITE_PAWN, 9, 25);
        assertDoesNotThrow(() -> board.makeMove(oneStep), oneStep + " FIRST ONE STEP");
        assertDoesNotThrow(() -> board.makeMove(twoStep), twoStep + " FIRST TWO STEP");

        board.placePiece(PieceType.WHITE_PAWN, 22);
        Move twoStepHop = new Move(PieceType.WHITE_PAWN, 14, 30);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoStepHop), twoStepHop + " HOPPING OVER PIECE ON FIRST MOVE");

        // Illegal Moves.
        Move twoStepAfterOne = new Move(PieceType.WHITE_PAWN, 16, 32);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoStepAfterOne), twoStepAfterOne + " TWO STEP AFTER ONE STEP");

        Move sideways = new Move(PieceType.WHITE_PAWN, 10, 11);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(sideways), sideways + " SIDEWAYS MOVE");

        Move backwards = new Move(PieceType.WHITE_PAWN, 12, 4);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(backwards), backwards + " BACKWARDS MOVE");

        // Captures.
        Move takeSelf = new Move(PieceType.WHITE_PAWN, 16, 25);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeSelf), takeSelf + " TAKING OWN PAWN");

        board.placePiece(PieceType.BLACK_PAWN, 32);
        board.placePiece(PieceType.BLACK_PAWN, 33);
        board.placePiece(PieceType.BLACK_PAWN, 34);

        Move takeEnemyRight = new Move(PieceType.WHITE_PAWN, 25, 34);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyRight), takeEnemyRight + " TAKING ENEMY ON RIGHT");

        board.placePiece(PieceType.WHITE_PAWN, 25);

        Move takeEnemyLeft = new Move(PieceType.WHITE_PAWN, 25, 32);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyLeft), takeEnemyLeft + " TAKING ENEMY ON LEFT");

        board.placePiece(PieceType.WHITE_PAWN, 25);

        Move takeEnemyInfront = new Move(PieceType.WHITE_PAWN, 25, 33);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeEnemyInfront), takeEnemyInfront + " TAKING ENEMY IN FRONT");
    }

    @Test
    public void testBlackPawns(){
        // TODO: Same as white pawns.
        // Beginning Moves.
        Board board = new Board(PieceType.BLACK_PAWN);
        Move oneStep = new Move(PieceType.BLACK_PAWN, 48, 40);
        Move twoStep = new Move(PieceType.BLACK_PAWN, 49, 33);
        assertDoesNotThrow(() -> board.makeMove(oneStep), oneStep + " FIRST ONE STEP");
        assertDoesNotThrow(() -> board.makeMove(twoStep), twoStep + " FIRST TWO STEP");

        board.placePiece(PieceType.BLACK_PAWN, 44);
        Move twoStepHop = new Move(PieceType.BLACK_PAWN, 52, 36);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoStepHop), twoStepHop + " HOPPING OVER PIECE ON FIRST MOVE");

        // Illegal Moves.
        Move toSelf = new Move(PieceType.BLACK_PAWN, 50, 50);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(toSelf));

        Move twoStepAfterOne = new Move(PieceType.BLACK_PAWN, 40, 24);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(twoStepAfterOne), twoStepAfterOne + " TWO STEP AFTER ONE STEP");

        Move sideways = new Move(PieceType.BLACK_PAWN, 50, 51);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(sideways), sideways + " SIDEWAYS MOVE");

        Move backwards = new Move(PieceType.BLACK_PAWN, 50, 58);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(backwards), backwards + " BACKWARDS MOVE");

        // Captures.
        Move takeSelf = new Move(PieceType.BLACK_PAWN, 54, 47);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeSelf), takeSelf + " TAKING OWN PAWN");

        board.placePiece(PieceType.WHITE_PAWN, 24);
        board.placePiece(PieceType.WHITE_PAWN, 25);
        board.placePiece(PieceType.WHITE_PAWN, 26);

        Move takeEnemyRight = new Move(PieceType.BLACK_PAWN, 33, 26);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyRight), takeEnemyRight + " TAKING ENEMY ON RIGHT");

        board.placePiece(PieceType.BLACK_PAWN, 33);

        Move takeEnemyLeft = new Move(PieceType.BLACK_PAWN, 33, 24);
        assertDoesNotThrow(() -> board.makeMove(takeEnemyLeft), takeEnemyLeft + " TAKING ENEMY ON LEFT");

        board.placePiece(PieceType.BLACK_PAWN, 33);

        Move takeEnemyInfront = new Move(PieceType.BLACK_PAWN, 33, 25);
        assertThrows(IllegalMoveException.class, () -> board.makeMove(takeEnemyInfront), takeEnemyInfront + " TAKING ENEMY IN FRONT");
    }

    @Test
    public void testKnights(){
        testKnightMoves(PieceType.WHITE_KNIGHT);
        testKnightMoves(PieceType.BLACK_KNIGHT);
    }

    @Test
    public void testBishops(){
        // TODO: Capturing and moving off edge
        testLegalDiagonal(PieceType.WHITE_BISHOP);
        testLegalDiagonal(PieceType.BLACK_BISHOP);
        testIllegalStraight(PieceType.WHITE_BISHOP);
        testIllegalStraight(PieceType.BLACK_BISHOP);
    }

    @Test
    public void testRooks(){
        // TODO: Capturing, Castling, and moving off edge
        testLegalStraight(PieceType.WHITE_ROOK);
        testLegalStraight(PieceType.BLACK_ROOK);
        testIllegalDiagonal(PieceType.WHITE_ROOK);
        testIllegalDiagonal(PieceType.BLACK_ROOK);
    }

    @Test
    public void testQueens(){
        // TODO: Capturing and moving off edge
        testLegalStraight(PieceType.WHITE_QUEEN);
        testLegalStraight(PieceType.BLACK_QUEEN);
        testLegalDiagonal(PieceType.WHITE_QUEEN);
        testLegalDiagonal(PieceType.BLACK_QUEEN);
    }

    @Test
    public void testKings(){
        // TODO: Checks, Captures, Castling
        kingTest(PieceType.WHITE_KING);
        kingTest(PieceType.BLACK_KING);
    }
}