package ru.otus.software_architect.eshop.api;

import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ErrorUtils {
    private static Logger logger = LogManager.getLogger(ErrorUtils.class);

    public static ErrorResponse parseError(Response<?> response) {
        logger.error("parseError: Error response(RAW):"
                + "\ncode = " + response.code()
                + "\nmessage = " + response.message()
                + "\nraw = " + response.raw()
        );
        Retrofit retrofit = RestClientFactory.getRetrofit();
        Converter<ResponseBody, ErrorResponse> converter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse errorResponse;
        try {
            ResponseBody responseBody = response.errorBody();
            errorResponse = converter.convert(responseBody);
        } catch (IOException e) {
            logger.error("parseError: " + e.getMessage()
                    + "\nError deserialize errorBody from JSON ---> use HTTP error code/message");
            errorResponse = new ErrorResponse();
            errorResponse.setCode(response.code());
            errorResponse.setMessage(response.message());
            return errorResponse;
        }
        return errorResponse;
    }
}
