package org.cynic.exception;

public class CounterApiException extends IllegalArgumentException {
    private static final long serialVersionUID = 8787002642950587992L;

    private Object[] values;

    public CounterApiException(String message, Object... values) {
        super(message);
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }

    public CounterApiException withCause(Throwable cause) {
        initCause(cause);

        return this;
    }
}
