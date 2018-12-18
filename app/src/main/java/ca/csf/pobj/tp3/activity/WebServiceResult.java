package ca.csf.pobj.tp3.activity;

public final class WebServiceResult<T> {

    private T result;
    private boolean isServerError;
    private boolean isConnectivityError;

    public static <T> WebServiceResult<T> ok(T result) {
        return new WebServiceResult<>(result, false, false);
    }

    public static <T> WebServiceResult<T> serverError() {
        return new WebServiceResult<>(null, true, false);
    }

    public static <T> WebServiceResult<T> connectivityError() {
        return new WebServiceResult<>(null, false, true);
    }

    private WebServiceResult(T result,
                             boolean isServerError,
                             boolean isConnectivityError) {
        this.result = result;
        this.isServerError = isServerError;
        this.isConnectivityError = isConnectivityError;
    }

    public T getResult() {
        return result;
    }

    public boolean isServerError() {
        return isServerError;
    }

    public boolean isConnectivityError() {
        return isConnectivityError;
    }

}
