package cz.jiripinkas.jsitemapgenerator;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Ping response reacts to response of Ping call.
 * If operation failed, *failure() methods will be called.
 * If operation succeeds, *success() method will be called.
 */
public class PingResponse {

    private boolean operationFailed;

    private RuntimeException exception;

    public PingResponse(boolean operationFailed, RuntimeException exception) {
        this.operationFailed = operationFailed;
        this.exception = exception;
    }

    public PingResponse(boolean operationFailed) {
        this.operationFailed = operationFailed;
    }

    /**
     * On success call Runnable
     *
     * @param runnable Runnable to call
     * @return Current PingResponse object, which can be used to define what happens on failure.
     * On failure exception should be either catched, or re-thrown.
     */
    public PingResponse callOnSuccess(Runnable runnable) {
        if(!operationFailed) {
            runnable.run();
        }
        return this;
    }

    /**
     * Throw user-defined exception on failure
     *
     * @param function Function, input: original exception, output: user-defined exception
     */
    public void throwOnFailure(UnaryOperator<RuntimeException> function) {
        if(operationFailed) {
            throw function.apply(exception);
        }
    }

    /**
     * Throw original exception on failure
     */
    public void throwOnFailure() {
        if(operationFailed) {
            throw exception;
        }
    }

    /**
     * On failure call consumer.
     *
     * @param consumer Consumer to call
     */
    public void catchOnFailure(Consumer<RuntimeException> consumer) {
        if(operationFailed) {
            consumer.accept(exception);
        }
    }

}
