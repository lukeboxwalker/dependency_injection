package dependency_injection;

public class ObjectCreationException extends RuntimeException {
    private static final long serialVersionUID = 19892738959823L;

    public ObjectCreationException(String message) {
        super(message);
    }

    public ObjectCreationException(Throwable cause) {
        super(cause);
    }
}
