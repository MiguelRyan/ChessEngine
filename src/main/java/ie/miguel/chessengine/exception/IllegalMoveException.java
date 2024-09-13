package ie.miguel.chessengine.exception;

public class IllegalMoveException extends RuntimeException{
    public IllegalMoveException(String message){
        super(message);
    }
}
