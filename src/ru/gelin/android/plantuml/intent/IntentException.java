package ru.gelin.android.plantuml.intent;

public class IntentException extends Exception {

    public IntentException() {
        super();
    }

    public IntentException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IntentException(String detailMessage) {
        super(detailMessage);
    }

    public IntentException(Throwable throwable) {
        super(throwable);
    }

}
