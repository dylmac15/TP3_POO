package ca.csf.pobj.tp3.activity;

public class CeasarCipher {

    private CipherKey cipherKey;

    public CeasarCipher(CipherKey cipherKey) {
        this.cipherKey = cipherKey;
    }

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
