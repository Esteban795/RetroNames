package linguacrypt.exception;

public class CorruptedSaveException extends RuntimeException {
    public CorruptedSaveException(String message) {
        super(message);
    }
}