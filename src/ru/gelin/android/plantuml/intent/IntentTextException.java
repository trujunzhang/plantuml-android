package ru.gelin.android.plantuml.intent;

public class IntentTextException extends IntentException {

    public IntentTextException() {
        super();
    }

    public IntentTextException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IntentTextException(String detailMessage) {
        super(detailMessage);
    }

    public IntentTextException(Throwable throwable) {
        super(throwable);
    }

}
