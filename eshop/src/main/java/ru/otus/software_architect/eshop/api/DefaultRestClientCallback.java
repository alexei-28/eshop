package ru.otus.software_architect.eshop.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultRestClientCallback<T> implements Callback<T> {
    private static Logger logger = LogManager.getLogger(DefaultRestClientCallback.class);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        logger.info("onResponse: ");
        // Returns true if code() is in the range [200..300)
        boolean isSuccessful = response.isSuccessful();
        logger.info("onResponse: isSuccessful = " + isSuccessful);
        // An HTTP response may still indicate an application-level failure such as a 404 or 500.
        // Call Response.isSuccessful() to determine if the response indicates success.
        if (isSuccessful) { // (status 200-299)
            logger.info("onResponse: Success: HTTP code [200..300)");
            onSuccess(response);
        } else { // (status 400-599)
            logger.info("onResponse: ERROR response: HTTP code [400..599)");
            ErrorResponse errorResponse = ErrorUtils.parseError(response);
            int code = errorResponse.getCode();
            if (code == 401) {
                logger.warn("onResponse: -> change to custom error message");
                errorResponse.setMessage("Invalid tokent");
            } else {
                logger.warn("onResponse: -> change to custom general error message");
                errorResponse.setMessage("Service unavailable");
            }
            logger.error("onResponse: ---> apiErrorResponse:\n" + errorResponse);
            //Exception ex = new Exception(apiErrorResponse.getMessage());
            onError(errorResponse);
        }
    }

    // Invoked when a network exception occurred talking to the server
    // or when an unexpected exception occurred creating the request or processing the response.
    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        logger.error("onFailure: Error: " + throwable.getMessage(), throwable);
        ErrorResponse errorResponse = new ErrorResponse();
        logger.warn("onFailure: -> change to custom general error message");
        errorResponse.setMessage("Service unavailable");
        onError(errorResponse);
    }


    // Callback methods (client can override methods if need)
    public void onSuccess(Response<T> response) {
        //Debug.d(TAG, "onSuccess: DEAULT IMPLEMENTATION IS EMPTY");
        logger.debug("onSuccess: DEFAULT_IMPLEMENTATION ---> notify subscribers");
        onDefault();
    }

    public void onError(ErrorResponse errorResponse) {
        //Debug.d(TAG, "onError: DEAULT IMPLEMENTATION IS EMPTY");
        logger.debug("onError: DEFAULT_IMPLEMENTATION ---> notify subscribers");
        onDefault();
    }

    private void onDefault() {
        //NetworkRequestStateEvent networkRequestStateEvent = NetworkRequestStateEvent.FINISH;
        logger.warn("onDefault:");
        //EventBus.getDefault().postSticky(networkRequestStateEvent);
    }


}
