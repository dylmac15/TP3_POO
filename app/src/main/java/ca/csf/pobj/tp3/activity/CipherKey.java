package ca.csf.pobj.tp3.activity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CipherKey implements Parcelable {

    private int id;
    private String outputCharacters;
    private String inputCharacters;


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

    // region Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(outputCharacters);
        dest.writeString(inputCharacters);
    }

    private CipherKey(Parcel in){
        id = in.readInt();
        outputCharacters = in.readString();
        inputCharacters = in.readString();
    }

    public static final Parcelable.Creator<CipherKey> CREATOR
            = new Creator<CipherKey>() {
        @Override
        public CipherKey createFromParcel(Parcel source) {
            return new CipherKey(source);
        }

        @Override
        public CipherKey[] newArray(int size) {
            return new CipherKey[size];
        }
    };
    // endregion
}
