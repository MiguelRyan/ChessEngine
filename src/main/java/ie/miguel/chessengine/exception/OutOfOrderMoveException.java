package ie.miguel.chessengine.exception;

public class OutOfOrderMoveException extends RuntimeException {
    public OutOfOrderMoveException(String message) {
        super(message);
    }
}
