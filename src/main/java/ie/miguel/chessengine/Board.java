package ie.miguel.chessengine;

import java.util.EnumMap;
import java.util.HashSet;

public class Board {
    public static void main(String[] args) {
        Board board = new Board();
        board.printBoard();
        board.movePiece(9, 17, PieceType.WHITE_PAWN);
        board.printBoard();
    }

    // Bitboards for the starting position where a1 = 0, and h8 = 63.
    EnumMap<PieceType, Long> pieceBitBoards;
    boolean whiteToMove = true;

    public Board() {
        this.pieceBitBoards = new EnumMap<>(PieceType.class);
        pieceBitBoards.put(PieceType.WHITE_PAWN, 0xff00L);
        pieceBitBoards.put(PieceType.WHITE_ROOK, 0x81L);
        pieceBitBoards.put(PieceType.WHITE_KNIGHT, 0x42L);
        pieceBitBoards.put(PieceType.WHITE_BISHOP, 0x24L);
        pieceBitBoards.put(PieceType.WHITE_QUEEN, 0x8L);
        pieceBitBoards.put(PieceType.WHITE_KING, 0x10L);

        pieceBitBoards.put(PieceType.BLACK_PAWN, 0xff000000000000L);
        pieceBitBoards.put(PieceType.BLACK_ROOK, 0x8100000000000000L);
        pieceBitBoards.put(PieceType.BLACK_KNIGHT, 0x4200000000000000L);
        pieceBitBoards.put(PieceType.BLACK_BISHOP, 0x2400000000000000L);
        pieceBitBoards.put(PieceType.BLACK_QUEEN, 0x800000000000000L);
        pieceBitBoards.put(PieceType.BLACK_KING, 0x1000000000000000L);
    }

    public void printBoard() {
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file; // Square index from 0 to 63

                // Shift 1 to the square index position and check with each bitboard using & operator
                long mask = 1L << square;
                char piece = '.';

                // Check for white pieces
                if ((pieceBitBoards.get(PieceType.WHITE_PAWN) & mask) != 0) piece = 'P';
                else if ((pieceBitBoards.get(PieceType.WHITE_ROOK) & mask) != 0) piece = 'R';
                else if ((pieceBitBoards.get(PieceType.WHITE_KNIGHT) & mask) != 0) piece = 'N';
                else if ((pieceBitBoards.get(PieceType.WHITE_BISHOP) & mask) != 0) piece = 'B';
                else if ((pieceBitBoards.get(PieceType.WHITE_QUEEN) & mask) != 0) piece = 'Q';
                else if ((pieceBitBoards.get(PieceType.WHITE_KING) & mask) != 0) piece = 'K';

                // Check for black pieces
                if ((pieceBitBoards.get(PieceType.BLACK_PAWN) & mask) != 0) piece = 'p';
                else if ((pieceBitBoards.get(PieceType.BLACK_ROOK) & mask) != 0) piece = 'r';
                else if ((pieceBitBoards.get(PieceType.BLACK_KNIGHT) & mask) != 0) piece = 'n';
                else if ((pieceBitBoards.get(PieceType.BLACK_BISHOP) & mask) != 0) piece = 'b';
                else if ((pieceBitBoards.get(PieceType.BLACK_QUEEN) & mask) != 0) piece = 'q';
                else if ((pieceBitBoards.get(PieceType.BLACK_KING) & mask) != 0) piece = 'k';

                System.out.print(piece + " ");
            }
            System.out.println();
        }
    }



    public void movePiece(long from, long to, PieceType piece){
        long fromMask = 1L << from;
        long toMask = 1L << to;

        // This checks if the piece is in the from place.
        long bitboard = pieceBitBoards.get(piece);
        if ((bitboard & fromMask) != 0) {
            // Remove the piece from the from location.
            bitboard &= ~ fromMask;

            // Moves the piece to the to location.
            bitboard |= toMask;

            // Updates the piece board.
            pieceBitBoards.put(piece, bitboard);
        }
    }

    public HashSet<Move> getPossibleMoves(PieceType piece){
        switch (piece){
            case WHITE_PAWN:

        }
        return null;
    }

    public boolean isCheck(){
        return true;
    }

    public boolean isCheckmate(){
        return true;
    }
}
