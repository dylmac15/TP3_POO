package ca.csf.pobj.tp3.activity;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchCipherKeyTask extends AsyncTask<Integer, Void, WebServiceResult<CipherKey>> {

    private static final String URL = "https://m1t2.csfpwmjv.tk/api/key/%d";

    private final Consumer<CipherKey> onSuccess;
    private final Runnable onServerError;
    private final Runnable onConnectivityError;

    public static void run(int keyNumber,
                           Consumer<CipherKey> onSuccess,
                           Runnable onServerError,
                           Runnable onConnectivityError) {
        new FetchCipherKeyTask(onSuccess,
                onServerError,
                onConnectivityError
        ).execute(keyNumber);
    }


    private FetchCipherKeyTask(Consumer<CipherKey> onSuccess,
                               Runnable onServerError,
                               Runnable onConnectivityError) {
        this.onSuccess = onSuccess;
        this.onServerError = onServerError;
        this.onConnectivityError = onConnectivityError;
    }

    @Override
    protected WebServiceResult<CipherKey> doInBackground(Integer... integers) {
        if (integers.length != 1) {
            throw new IllegalArgumentException("You must provide one number.");
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.format(URL, integers[0]))
                .build();

        Call call = client.newCall(request);

        try {
            SystemClock.sleep(2000);
            Response response = call.execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                ObjectMapper mapper = new ObjectMapper();

                CipherKey result = mapper.readValue(
                        responseBody,
                        mapper.getTypeFactory().constructType(CipherKey.class)
                );

                return WebServiceResult.ok(result);
            } else {
                return WebServiceResult.serverError();
            }
        } catch (JsonParseException | JsonMappingException e) {
            return WebServiceResult.serverError();
        } catch (IOException e) {
            return WebServiceResult.connectivityError();
        }
    }

    @Override
    protected void onPostExecute(WebServiceResult<CipherKey> result) {

        if (result.isServerError()) {
            onServerError.run();
        } else if (result.isConnectivityError()) {
            onConnectivityError.run();
        } else {
            onSuccess.accept(result.getResult());
        }
    }
}

