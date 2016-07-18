package pl.eduweb.podcastplayer.api;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Autor on 2016-07-18.
 */
public class ErrorConverter {

    private final Retrofit retrofit;

    public ErrorConverter(Retrofit retrofit) {

        this.retrofit = retrofit;
    }

    public ErrorResponse convert(ResponseBody responseBody) {
        Converter<ResponseBody, ErrorResponse> converter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[]{});
        try {
            return converter.convert(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
