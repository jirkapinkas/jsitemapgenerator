package cz.jiripinkas.jsitemapgenerator.exception;

public class WebmasterToolsException extends RuntimeException {

    public WebmasterToolsException(String message, Throwable ex) {
        super(message, ex);
    }

    public WebmasterToolsException(String message) {
        super(message);
    }

    public WebmasterToolsException(Throwable cause) {
        super(cause);
    }
}
