package ie.miguel.chessengine;

public class Move {
    PieceType piece;
    long fromSquare;
    long toSquare;

    public Move(PieceType piece, long fromSquare, long toSquare) {
        if (fromSquare < 0 || fromSquare > 63 || toSquare < 0 || toSquare > 63) {
            throw new IllegalArgumentException("Move must be between 0 and 63");
        }
        this.piece = piece;
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    public long getFromSquare() {
        return fromSquare;
    }

    public long getToSquare() {
        return toSquare;
    }

    public PieceType getPiece() {
        return piece;
    }
    @Override
    public String toString() {
        String from = String.valueOf((char) ((fromSquare % 8) + 'a')) + ((fromSquare / 8) + 1);
        String to = String.valueOf((char) ((toSquare % 8) + 'a')) + ((toSquare / 8) + 1);
        return piece + " from " + from + " to " + to;
    }
}
