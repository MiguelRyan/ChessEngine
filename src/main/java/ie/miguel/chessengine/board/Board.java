package ie.miguel.chessengine.board;

import ie.miguel.chessengine.exception.IllegalMoveException;
import ie.miguel.chessengine.move.Move;
import ie.miguel.chessengine.exception.OutOfOrderMoveException;
import ie.miguel.chessengine.PieceType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;

import static ie.miguel.chessengine.move.moveUtils.*;

// TODO: MAJOR MAJOR REFACTORING: There is a lot of code smell in the form of classes that have unclear goals. moveUtils specifically.
public class Board {
    public final static List<PieceType> whitePieces = Arrays.asList(PieceType.WHITE_PAWN, PieceType.WHITE_BISHOP, PieceType.WHITE_ROOK, PieceType.WHITE_KNIGHT, PieceType.WHITE_KING, PieceType.WHITE_QUEEN);
    public final static List<PieceType> blackPieces = Arrays.asList(PieceType.BLACK_PAWN, PieceType.BLACK_KNIGHT, PieceType.BLACK_KING, PieceType.BLACK_QUEEN, PieceType.BLACK_ROOK, PieceType.BLACK_BISHOP);

    // Bitboards for the starting position where a1 = 0, and h8 = 63.
    private EnumMap<PieceType, Long> pieceBitBoards;
    private boolean whiteToMove = true;
    private boolean moveChanges = true;

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

    public Board(Board old){
        this.pieceBitBoards = new EnumMap<>(old.pieceBitBoards);
        this.whiteToMove = old.whiteToMove;
    }

    public Board(PieceType piece) {
        // Used for testing to ensure that the same piece can move over and over again.
        // Pass the piece in and that colour will always be the one to move.
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
        moveChanges = false;
        whiteToMove = whitePieces.contains(piece);
    }

    public void clearBoard(){
        pieceBitBoards.replaceAll((t, v) -> 0L);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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

                sb.append(piece).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void makeMove(Move move){
        List<PieceType> piecesOfCurrentPlayer = whiteToMove ? whitePieces : blackPieces;
        if (!piecesOfCurrentPlayer.contains(move.piece())){
            throw new OutOfOrderMoveException("Wrong player moved " + move);
        }
        if (!isLegalMove(move, this)){
            throw new IllegalMoveException("Illegal move: " + move);
        }

        PieceType movingPiece = squareIsOccupied(move.fromSquare());

        if (movingPiece != move.piece()){
            throw new IllegalMoveException("Attempted to move " + move.piece() + " from " + move.fromSquare() + " but " + movingPiece + " is present instead.");
        }

        PieceType toCapture = squareIsOccupied(move.toSquare());
        if (toCapture != null){
            removePiece(toCapture, move.toSquare());
        }
        placePiece(move.piece(), move.toSquare());
        removePiece(move.piece(), move.fromSquare());

        // For the testing board.
        if (moveChanges) {
            whiteToMove = !whiteToMove;
        }
    }

    public PieceType squareIsOccupied(int location){
        if (location < 0 || location > 63){
            throw new IllegalArgumentException("Location must be between 0 and 63");
        }

        long mask = 1L << location;

        for (PieceType piece: pieceBitBoards.keySet()){
            long bitboard = pieceBitBoards.get(piece);
            if ((bitboard & mask) != 0) return piece;
        }

        return null;
    }

    private PieceType capturePiece(Move move){
        // Returns the piece that was captured.
        PieceType capturedPiece = squareIsOccupied(move.toSquare());
        if (capturedPiece == null){
            throw new IllegalArgumentException("Attempting to capture at: " + move.toSquare() + " but no piece is present to capture.");
        }

        removePiece(capturedPiece, move.toSquare());
        placePiece(move.piece(), move.toSquare());

        return capturedPiece;
    }

    public void removePiece(PieceType piece, int location){
        if (piece != squareIsOccupied(location)){
            throw new IllegalArgumentException("Attempting to remove " + piece + " from " + location + " but "
                    + squareIsOccupied(location) + " is at location.");
        }

        long bitboard = pieceBitBoards.get(piece);
        long locationMask = 1L << location;
        bitboard = bitboard ^ locationMask;
        pieceBitBoards.put(piece, bitboard);
    }

    public void placePiece(PieceType piece, int location){
        if (squareIsOccupied(location) != null){
            throw new IllegalArgumentException("Attempted to place " + piece + " at " + location + " but a piece is already there.");
        }
        long bitboard = pieceBitBoards.get(piece);
        long mask = 1L << location;
        bitboard |= mask;
        pieceBitBoards.put(piece, bitboard);
    }

    public boolean isWhiteToMove(){
        return whiteToMove;
    }

    public boolean isCheck(){
        HashSet<Move> validMoves= generateAllMoves(this);
        System.out.println("TESTSETSET");
        System.out.println(validMoves);
        return true;
    }

    public boolean isCheckmate(){
        return true;
    }

    public EnumMap<PieceType, Long> getPieceBitBoards() {
        return pieceBitBoards;
    }

}