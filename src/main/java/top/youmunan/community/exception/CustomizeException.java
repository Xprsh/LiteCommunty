package top.youmunan.community.exception;


public class CustomizeException extends RuntimeException{
    private String errorMsg;

    public CustomizeException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
