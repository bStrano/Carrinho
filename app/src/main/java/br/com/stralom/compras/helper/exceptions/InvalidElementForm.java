package br.com.stralom.compras.helper.exceptions;

public class InvalidElementForm extends Exception {
    private int errorCode;

    public InvalidElementForm(String message) {
        super(message);
    }

    public InvalidElementForm(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
