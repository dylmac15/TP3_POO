package ca.csf.pobj.tp3.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import ca.csf.pobj.tp3.R;
import ca.csf.pobj.tp3.utils.view.CharactersFilter;
import ca.csf.pobj.tp3.utils.view.KeyPickerDialog;

public class MainActivity extends AppCompatActivity implements FetchCipherKeyTask.Listener {

    private static final int KEY_LENGTH = 5;
    private static final int MAX_KEY_VALUE = (int) Math.pow(10, KEY_LENGTH) - 1;
    private static final int MIN_RANDOM_VALUE = 0;
    private static final int MAX_RANDOM_VALUE = (int) Math.pow(10, KEY_LENGTH);
    private View rootView;
    private EditText inputEditText;
    private TextView outputTextView;
    private TextView currentKeyTextView;
    private ProgressBar progressBar;
    private CaesarCipher caesarCipher;
    private CipherKey cipherKey;
    private int currentKey;


    public void outputCypherKeyFound(CipherKey cipherKey) {
        this.cipherKey = cipherKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootView);
        progressBar = findViewById(R.id.progressbar);
        inputEditText = findViewById(R.id.input_edittext);
        inputEditText.setFilters(new InputFilter[]{new CharactersFilter()});
        outputTextView = findViewById(R.id.output_textview);
        currentKeyTextView = findViewById(R.id.current_key_textview);
        currentKey = randomKey();
        String formattedKey = String.format(getResources().getString(R.string.text_current_key), currentKey);
        currentKeyTextView.setText(formattedKey);
        this.fetchSubstitutionCypherKey(currentKey);
    }

    private int randomKey() {
        Random random = new Random();
        return random.nextInt(MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) + MIN_RANDOM_VALUE;
    }

    private void showKeyPickerDialog(int key) {
        KeyPickerDialog.make(this, KEY_LENGTH)
                .setKey(key)
                .setConfirmAction(this::fetchSubstitutionCypherKey)
                .show();
    }

    private void showCopiedToClipboardMessage() {
        Snackbar.make(rootView, R.string.text_copied_output, Snackbar.LENGTH_SHORT).show();
    }

    private void showConnectionError() {
        Snackbar.make(rootView, R.string.text_connectivity_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.text_activate_wifi, (view) -> showWifiSettings())
                .show();
    }

    private void showServerError() {
        Snackbar.make(rootView, R.string.text_server_error, Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }


    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showWifiSettings() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

    private void fetchSubstitutionCypherKey(int key) {
        FetchCipherKeyTask task = new FetchCipherKeyTask();
        task.execute(key);
        currentKey = key;
        task.addListener(this);
        //TODO on create pas faire squia en dessous
        String currentKey = String.format(getResources().getString(R.string.text_current_key), key);
        currentKeyTextView.setText(currentKey);
    }

    @SuppressWarnings("ConstantConditions")
    private void putTextInClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_encrypted_text), text));
    }

    public void onKeySelectButtonClicked(View view) {
        this.showKeyPickerDialog(currentKey);
    }

    public void onEncryptButtonClicked(View view) {
        caesarCipher = new CaesarCipher(cipherKey);
        outputTextView.setText(caesarCipher.encrypt(inputEditText.getText().toString()));
    }

    public void onDecryptButtonClicked(View view) {
        caesarCipher = new CaesarCipher(cipherKey);
        outputTextView.setText(caesarCipher.decrypt(inputEditText.getText().toString()));
    }

    public void onCopyButtonClicked(View view) {
        this.putTextInClipboard(outputTextView.getText().toString());
        this.showCopiedToClipboardMessage();
    }

}
