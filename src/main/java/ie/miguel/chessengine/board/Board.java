package ie.miguel.chessengine.board;

import ie.miguel.chessengine.exception.IllegalMoveException;
import ie.miguel.chessengine.exception.OutOfOrderMoveException;
import ie.miguel.chessengine.PieceType;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;

import static ie.miguel.chessengine.board.BoardUtils.*;
import static ie.miguel.chessengine.board.legalMoveUtils.*;

// TODO: MAJOR MAJOR REFACTORING: There is a lot of code smell in the form of classes that have unclear goals. moveUtils specifically.
public class Board {
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
        this.moveChanges = old.moveChanges;
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

    public void makeMove(Move move){
        List<PieceType> piecesOfCurrentPlayer = whiteToMove ? whitePieces : blackPieces;
        if (!piecesOfCurrentPlayer.contains(move.piece())){
            throw new OutOfOrderMoveException("Wrong player moved " + move);
        }

        if (!isLegalMove(move)){
            throw new IllegalMoveException("Illegal move: " + move);
        }

        if (leavesKingInCheck(move)){
            throw new IllegalMoveException("Move allows king to be captured: " + move);
        }

        PieceType movingPiece = squareIsOccupied(move.fromSquare());

        if (movingPiece != move.piece()){
            throw new IllegalMoveException("Attempted to move " + move.piece() + " from " + move.fromSquare() + " but " + movingPiece + " is present instead.");
        }

        PieceType toCapture = squareIsOccupied(move.toSquare());
        if (piecesOfCurrentPlayer.contains(toCapture)){
            throw new IllegalMoveException("Attempted to take own piece: " + move);
        }

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

    private boolean isLegalMove(Move move){
        PieceType piece = move.piece();
        if (move.fromSquare() == move.toSquare()) return false;

        if (piece == PieceType.WHITE_PAWN || piece == PieceType.BLACK_PAWN){
            return isLegalPawnMove(move, this);
        }
        if (piece == PieceType.WHITE_KNIGHT || piece == PieceType.BLACK_KNIGHT){
            return isLegalKnightMove(move);
        }
        if (piece == PieceType.WHITE_BISHOP || piece == PieceType.BLACK_BISHOP){
            return isLegalBishopMove(move, this);
        }
        if (piece == PieceType.WHITE_ROOK || piece == PieceType.BLACK_ROOK){
            return isLegalRookMove(move, this);
        }
        if (piece == PieceType.WHITE_QUEEN || piece == PieceType.BLACK_QUEEN){
            return isLegalRookMove(move, this) || isLegalBishopMove(move, this);
        }
        if (piece == PieceType.WHITE_KING || piece == PieceType.BLACK_KING){
            return isLegalKingMove(move, this);
        }

        return false;
    }

    private boolean leavesKingInCheck(Move move){
        Board simulationBoard = new Board(this);
        PieceType toCapture = simulationBoard.squareIsOccupied(move.toSquare());

        if (toCapture != null){
            simulationBoard.removePiece(toCapture, move.toSquare());
        }
        simulationBoard.placePiece(move.piece(), move.toSquare());
        simulationBoard.removePiece(move.piece(), move.fromSquare());
        PieceType king = simulationBoard.whiteToMove ? PieceType.WHITE_KING : PieceType.BLACK_KING;
        int kingPosition = findKingPosition(king, simulationBoard);

        for (Move enemyMove: simulationBoard.generateAllMoves()){
            if (enemyMove.toSquare() == kingPosition) return true;
        }

        return false;
    }

    private HashSet<Move> generateMovesForPiece(PieceType piece) {
        HashSet<Move> possibleMoves = new HashSet<>();

        long bitboard = getPieceBitBoards().get(piece);
        HashSet<Integer> pieceLocations = getPieceLocations(bitboard);
        for (int location: pieceLocations){
            for (int i = 0; i <= 63; i++){
                Move move = new Move(piece, location, i);
                if (isLegalMove(move)) {
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves;
    }

    public HashSet<Move> generateAllMoves(){
        List<PieceType> enemyPieces = isWhiteToMove() ? blackPieces : whitePieces;
        HashSet<Move> possibleMoves = new HashSet<>();

        for (PieceType piece: enemyPieces){
            possibleMoves.addAll(generateMovesForPiece(piece));
        }

        return possibleMoves;
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

    public EnumMap<PieceType, Long> getPieceBitBoards() {
        return pieceBitBoards;
    }
}