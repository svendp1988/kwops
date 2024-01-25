package pxl.kwops.api.models;

public class ErrorModel {

    private final String message;
    private final int statusCode;

    public ErrorModel(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
