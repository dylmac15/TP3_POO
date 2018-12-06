package ca.csf.pobj.tp3.activity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CipherKey {

    public int id;
    public String outputCharacters;
    public String inputCharacters;


    @JsonCreator
    public CipherKey(
            @JsonProperty("id") int id,
            @JsonProperty("outputCharacters") String outputCharacters,
            @JsonProperty("inputCharacters") String inputCharacters) {
        this.id = id;
        this.outputCharacters = outputCharacters;
        this.inputCharacters = inputCharacters;
    }

    public int getId() {
        return id;
    }

    public String getInputCharacters() {
        return inputCharacters;
    }

    public String getOutputCharacters() {
        return outputCharacters;
    }
}
