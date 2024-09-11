package ie.miguel.chessengine;

public class IllegalMoveException extends RuntimeException{
    public IllegalMoveException(String message){
        super(message);
    }
}
