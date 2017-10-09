package tech.lapsa.epayment.ws.rs.app;

public class WrongArgumentException extends Exception {
    private static final long serialVersionUID = -5521345061525397270L;

    public WrongArgumentException() {
	super();
    }

    public WrongArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public WrongArgumentException(String message, Throwable cause) {
	super(message, cause);
    }

    public WrongArgumentException(String message) {
	super(message);
    }

    public WrongArgumentException(Throwable cause) {
	super(cause);
    }
}
