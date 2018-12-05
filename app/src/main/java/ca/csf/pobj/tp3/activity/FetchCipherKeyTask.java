package ca.csf.pobj.tp3.activity;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class FetchCipherKeyTask extends AsyncTask<Integer, Void, CipherKey> {

    private static final String URL = "https://m1t2.csfpwmjv.tk/api/key/%d";

    private final List<Listener> listeners = new ArrayList();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    protected CipherKey doInBackground(Integer... integers) {
        if (integers.length != 1)
            throw new IllegalArgumentException("You must provide one word.");

        OkHttpClient client = new OkHttpClient();


        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    public interface Listener {
        void outputCypherKeyFound(CipherKey cipherKey);
    }
}
