package libraries.retrofit;


import libraries.retrofit.enums.RestErrorType;

public class RestError
{
    private RestErrorType restErrorType = RestErrorType.NONE;
    private Throwable throwable;
    private String message;

    public RestErrorType getRestErrorType()
    {
        return restErrorType;
    }

    public void setRestErrorType(RestErrorType restErrorType)
    {
        this.restErrorType = restErrorType;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
