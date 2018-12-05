package ca.csf.pobj.tp3.activity;

import android.net.sip.SipSession;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchCypherKeyTask extends AsyncTask<Integer, Void, CypherKey> {

    private static final String URL = "https://m1t2.csfpwmjv.tk/api/key/%d";

    private final List<Listener> listeners = new ArrayList();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    protected CypherKey doInBackground(Integer... integers) {
        if (integers.length != 1)
            throw new IllegalArgumentException("You must provide one word.");

        OkHttpClient client = new OkHttpClient();


        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    public interface Listener {
        void outputCypherKeyFound(CypherKey cypherKey);
    }
}
