package ru.otus.software_architect.eshop.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.otus.software_architect.eshop.service.TransportService;
import ru.otus.software_architect.eshop.util.GsonUtil;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RestClientFactory {
    private final static int CONNECTION_TIME_OUT_SEC = 60;
    private final static int READ_TIME_OUT_SEC = 60;
    //private final static int WRITE_TIME_OUT_SEC = 60;
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static GsonBuilder gsonBuilder = GsonUtil.gsonbuilder;
    private static Gson gson;
    private static OkHttpClient.Builder httpClient;
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    static {
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT_SEC, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT_SEC, TimeUnit.SECONDS);
        // image must be array
        httpClient.addInterceptor(httpLoggingInterceptor);
        // all requests has same headers
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Content-Type", JSON_CONTENT_TYPE).build();
                return chain.proceed(request);
            }
        });

        gson = gsonBuilder.create();
    }

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiHelper.API_BASE_URL_NOTIFY)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build());

    private static Retrofit retrofit = builder.build();

    public static <T> T createRestClient(Class<T> restClientClass) {
        retrofit = builder.build();
        return retrofit.create(restClientClass);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
