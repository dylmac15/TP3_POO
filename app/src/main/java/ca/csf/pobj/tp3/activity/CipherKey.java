package ca.csf.pobj.tp3.activity;

public class CipherKey {

    private static int id;
    private static String inputCharacters;
    private static String outputCharacters;

    public CipherKey(int id, String inputCharacters, String outputCharacters) {
        CipherKey.id = id;
        CipherKey.inputCharacters = inputCharacters;
        CipherKey.outputCharacters = outputCharacters;
    }

}
