package exception;

/**
 * custom exception class used in enitre application
 */
public class MovieReviewException extends Throwable {

    private String message;
    public MovieReviewException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
