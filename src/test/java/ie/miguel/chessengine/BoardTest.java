package ie.miguel.chessengine;

import ie.miguel.chessengine.board.Board;
import ie.miguel.chessengine.board.Move;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static ie.miguel.chessengine.board.BoardUtils.findKingPosition;
import static ie.miguel.chessengine.board.BoardUtils.getPieceLocations;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void testFindKing(){
        Board board = new Board();
        assertEquals(4, findKingPosition(PieceType.WHITE_KING, board));
        assertEquals(60, findKingPosition(PieceType.BLACK_KING, board));

        board.clearBoard();
        board.placePiece(PieceType.WHITE_KING, 10);
        board.placePiece(PieceType.BLACK_KING, 15);
        assertEquals(10, findKingPosition(PieceType.WHITE_KING, board));
        assertEquals(15, findKingPosition(PieceType.BLACK_KING, board));
    }

    @Test
    public void testRemovePiece(){
        Board board = new Board();
        board.removePiece(PieceType.WHITE_ROOK, 0);
        board.removePiece(PieceType.BLACK_ROOK, 56);

        assertNull(board.squareIsOccupied(0));
        assertNull(board.squareIsOccupied(56));

        assertThrows(IllegalArgumentException.class, () -> board.removePiece(PieceType.WHITE_ROOK, 0));
        assertThrows(IllegalArgumentException.class, () -> board.removePiece(PieceType.BLACK_ROOK, 56));
        assertThrows(IllegalArgumentException.class, () -> board.removePiece(PieceType.WHITE_ROOK, 1));
        assertThrows(IllegalArgumentException.class, () -> board.removePiece(PieceType.BLACK_ROOK, 2));

        assertThrows(IllegalArgumentException.class, () -> board.removePiece(PieceType.WHITE_ROOK, -1));
        assertThrows(IllegalArgumentException.class, () -> board.removePiece(PieceType.WHITE_ROOK, 78));
    }

    @Test
    public void testPlacePiece(){
        Board board = new Board();
        board.clearBoard();

        board.placePiece(PieceType.WHITE_KING, 10);
        assertEquals(PieceType.WHITE_KING, board.squareIsOccupied(10));

        board.placePiece(PieceType.BLACK_KING, 15);
        assertEquals(PieceType.BLACK_KING, board.squareIsOccupied(15));

        assertThrows(IllegalArgumentException.class, () -> board.placePiece(PieceType.WHITE_KING, 10));
        assertThrows(IllegalArgumentException.class, () -> board.placePiece(PieceType.BLACK_KING, 10));

        assertThrows(IllegalArgumentException.class, () -> board.placePiece(PieceType.WHITE_ROOK, -10));
        assertThrows(IllegalArgumentException.class, () -> board.placePiece(PieceType.WHITE_ROOK, 78));
    }

    @Test
    public void testMakeMove(){
        Board board = new Board();
        board.clearBoard();
        board.placePiece(PieceType.WHITE_KING, 10);
        Move move = new Move(PieceType.WHITE_KING, 10, 11);
        board.makeMove(move);

        assertEquals(null, board.squareIsOccupied(10), "makeMove is not clearing piece from FROM when moving.");
        assertEquals(PieceType.WHITE_KING, board.squareIsOccupied(11), "makeMove is not adding piece to TO when moving.");
    }

    @Test
    public void testGetPieceLocations(){
        Board board = new Board();
        board.clearBoard();
        HashSet<Integer> places = new HashSet<>(Arrays.asList(0, 2, 4, 5, 20, 26, 34, 40, 43, 45, 63));
        for (int x: places){
            board.placePiece(PieceType.WHITE_PAWN, x);
        }
        HashSet<Integer> test = getPieceLocations(board.getPieceBitBoards().get(PieceType.WHITE_PAWN));
        assertEquals(places, test);
    }
}