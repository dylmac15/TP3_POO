package ca.csf.pobj.tp3.activity;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import ca.csf.pobj.tp3.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchCipherKeyTask extends AsyncTask<Integer, Void, CipherKey> {

    private static final String URL = "https://m1t2.csfpwmjv.tk/api/key/%d";
    private final List<Listener> listeners = new ArrayList();
    private int errorMessage = MainActivity.NO_ERROR;

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    protected void onPreExecute() {
        for (Listener listener : listeners) {
            listener.showProgressBar();
        }
    }

    @Override
    protected CipherKey doInBackground(Integer... integers) {
        if (integers.length != 1) {
            throw new IllegalArgumentException("You must provide one word.");
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
                return mapper.readValue(responseBody, CipherKey.class);
            } else {
                errorMessage = MainActivity.ERROR_ONE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = MainActivity.ERROR_TWO;
        }
        return null;
    }

    @Override
    protected void onPostExecute(CipherKey cipherKey) {
        for (Listener listener : listeners) {
            if (errorMessage == MainActivity.ERROR_ONE) {
                listener.showErrorMessage(MainActivity.ERROR_ONE);
            } else if (errorMessage == MainActivity.ERROR_TWO) {
                listener.showErrorMessage(MainActivity.ERROR_TWO);
            } else {
                listener.outputCypherKeyFound(cipherKey);

            }
            listener.hideProgressBar();
        }

    }

    public interface Listener {
        void outputCypherKeyFound(CipherKey cipherKey);

        void showProgressBar();

        void hideProgressBar();

        void showErrorMessage(int error);
    }
}
