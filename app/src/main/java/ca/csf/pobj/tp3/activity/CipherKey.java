package ca.csf.pobj.tp3.activity;

public class CipherKey {

    private final int id;
    private final String inputCharacters;
    private final String outputCharacters;

    public int getId() {
        return id;
    }

    public String getInputCharacters() {
        return inputCharacters;
    }

    public String getOutputCharacters() {
        return outputCharacters;
    }

    public CipherKey(int id, String inputCharacters, String outputCharacters) {
        this.id = id;
        this.inputCharacters = inputCharacters;
        this.outputCharacters = outputCharacters;
    }

}
