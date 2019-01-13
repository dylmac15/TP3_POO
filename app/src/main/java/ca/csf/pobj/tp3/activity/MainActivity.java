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

public class MainActivity extends AppCompatActivity {

    private static final String STATE_INPUT_VALUE = "STATE_INPUT_VALUE";
    private static final String STATE_OUTPUT_VALUE = "STATE_OUTPUT_VALUE";
    private static final String STATE_CURRENT_KEY_TEXT_VALUE = "STATE_CURRENT_KEY_VALUE";

    private static final int KEY_LENGTH = 5;
    private static final int MIN_RANDOM_VALUE = 0;
    private static final int MAX_RANDOM_VALUE = (int) Math.pow(10, KEY_LENGTH);
    //BEN_CORRECTION : Devrait être private. x2.
    public static final String STATE_CIPHER_KEY = "STATE_CIPHER_KEY";
    public static final String STATE_DIALOG = "STATE_DIALOG";


    private View rootView;
    private EditText inputEditText;
    private TextView outputTextView;
    private TextView currentKeyTextView;
    private ProgressBar progressBar;
    private CaesarCipher caesarCipher;
    private CipherKey cipherKey;
    private int currentKey; //BEN_REVIEW : Duplication d'information. Aussi disponible dans "cipherKey".
    private boolean keepStateDialog;


    public void outputCypherKeyFound(CipherKey cipherKey) {
        this.cipherKey = cipherKey;
        hideProgressBar();
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

        if (savedInstanceState == null){
            currentKey = randomKey();
            //BEN_CORRECTION : Duplication de code (voir fetchSubstitutionCypherKey).
            String formattedKey = String.format(getResources().getString(R.string.text_current_key), currentKey);
            currentKeyTextView.setText(formattedKey);
            this.fetchSubstitutionCypherKey(currentKey);
        } else {
            cipherKey = savedInstanceState.getParcelable(STATE_CIPHER_KEY);
            //noinspection ConstantConditions guaranteed not to be null
            currentKey = cipherKey.getId();
        }
    }

    //BEN_CORRECTION : Nommage pas clair (voir mensonger).
    private void changeStateDialog(){
        this.keepStateDialog = false;
    }

    private int randomKey() {
        Random random = new Random();
        return random.nextInt(MAX_RANDOM_VALUE - MIN_RANDOM_VALUE) + MIN_RANDOM_VALUE;
    }

    private void showKeyPickerDialog(int key) {
        KeyPickerDialog.make(this, KEY_LENGTH)
                .setKey(key)
                .setConfirmAction(this::fetchSubstitutionCypherKey)
                .setCancelAction(this::changeStateDialog)
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
        keepStateDialog = false;
        FetchCipherKeyTask.run(key,
                this::outputCypherKeyFound,
                this::showServerError,
                this::showConnectionError
        );
        showProgressBar();
        currentKey = key;

        String currentKey = String.format(getResources().getString(R.string.text_current_key), key);

        currentKeyTextView.setText(currentKey);
    }

    private void putTextInClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_encrypted_text), text));
    }

    public void onKeySelectButtonClicked(View view) {
        this.showKeyPickerDialog(currentKey);
        keepStateDialog = true;
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputEditText.setText(savedInstanceState.getCharSequence(STATE_INPUT_VALUE));
        outputTextView.setText(savedInstanceState.getCharSequence(STATE_OUTPUT_VALUE));
        currentKeyTextView.setText(savedInstanceState.getCharSequence(STATE_CURRENT_KEY_TEXT_VALUE));
        keepStateDialog = savedInstanceState.getBoolean(STATE_DIALOG);
        cipherKey = savedInstanceState.getParcelable(STATE_CIPHER_KEY);
        if (keepStateDialog){
            //noinspection ConstantConditions guaranteed not to be null
            //BEN_CORRECITON : Pas tout à fait vrai. Si l'application démarre sans "Internet".
            showKeyPickerDialog(cipherKey.getId());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(STATE_INPUT_VALUE, inputEditText.getText());
        outState.putCharSequence(STATE_OUTPUT_VALUE, outputTextView.getText());
        outState.putCharSequence(STATE_CURRENT_KEY_TEXT_VALUE, currentKeyTextView.getText());
        outState.putBoolean(STATE_DIALOG, keepStateDialog);
        outState.putParcelable(STATE_CIPHER_KEY, cipherKey);
    }
}

