package ie.miguel.chessengine;

import java.util.EnumMap;
import java.util.HashSet;

import static ie.miguel.chessengine.BoardUtils.getPieceLocations;
import static ie.miguel.chessengine.moveUtils.isLegalMove;

public class Board {
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board.generateMovesForPiece(PieceType.WHITE_PAWN));
        System.out.println(board.generateMovesForPiece(PieceType.WHITE_PAWN).size());
        System.out.println(board.generateMovesForPiece(PieceType.WHITE_KNIGHT));
        System.out.println(board.generateMovesForPiece(PieceType.WHITE_KNIGHT).size());
    }
    // Bitboards for the starting position where a1 = 0, and h8 = 63.
    EnumMap<PieceType, Long> pieceBitBoards;
    private boolean whiteToMove = true;

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
    }

    public HashSet<Move> generateAllPossibleMoves(){
        // My thinking is we need to generate all possible moves even ones that result in check.
        // Then we can use this to see if any of the moves could take the king.
        // If so those moves are removed from the set.
        HashSet<Move> possibleMoves = new HashSet<>();


        return possibleMoves;
    }

    public HashSet<Move> generateMovesForPiece(PieceType piece) {
        HashSet<Move> possibleMoves = new HashSet<>();
        long bitboard = pieceBitBoards.get(piece);
        HashSet<Integer> pieceLocations = getPieceLocations(bitboard);
        for (int location: pieceLocations){
            for (int i = 0; i <= 63; i++){
                Move move = new Move(piece, location, i);
                if (isLegalMove(move, this)) {
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves;
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

    public long findKingPosition(PieceType king){
        if (!(king == PieceType.WHITE_KING || king == PieceType.BLACK_KING)){
            throw new IllegalArgumentException("Non-King being passed to find king position.");
        }

        long kingBitboard = pieceBitBoards.get(king);
        return Long.numberOfTrailingZeros(kingBitboard);
    }

    public void simulateMove(Move move){
        Board simulationBoard = new Board(this);
        simulationBoard.makeMove(move);
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
        return true;
    }

    public boolean isCheckmate(){
        return true;
    }
}
