package com.plantuml.client;

public class PlantUMLClientException extends Exception {

    public PlantUMLClientException() {
        super();
    }

    public PlantUMLClientException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PlantUMLClientException(String detailMessage) {
        super(detailMessage);
    }

    public PlantUMLClientException(Throwable throwable) {
        super(throwable);
    }

}
