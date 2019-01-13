package ca.csf.pobj.tp3.activity;

public class CaesarCipher {

    private CipherKey cipherKey;

    public CaesarCipher(CipherKey cipherKey) {
        this.cipherKey = cipherKey;
    }

    //BEN_CORRECTION : Duplication de code (voir méthode decrypt).
    public StringBuilder encrypt(String string) {
        StringBuilder stringBuilder = new StringBuilder();


        for (Character stringCharacter : string.toCharArray()) {
            for (Character character : cipherKey.getInputCharacters().toCharArray()) {
                if (stringCharacter.equals(character)) {
                    stringBuilder.append(cipherKey.getOutputCharacters().charAt(cipherKey.getInputCharacters().indexOf(character)));
                }
            }
        }
        return stringBuilder;
    }

    //BEN_CORRECTION : Duplication de code (voir méthode encrypt).
    public StringBuilder decrypt(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Character stringCharacter : string.toCharArray()) {
            for (Character character : cipherKey.getOutputCharacters().toCharArray()) {
                if (stringCharacter.equals(character)) {
                    stringBuilder.append(cipherKey.getInputCharacters().charAt(cipherKey.getOutputCharacters().indexOf(character)));
                }
            }
        }
        return stringBuilder;
    }
}
