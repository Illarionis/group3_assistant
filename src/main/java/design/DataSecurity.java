package design;

/**
 * Defines a functionality to protect data.
 **/
public abstract class DataSecurity {
    /**
     * Decrypts a string that has been encrypted by this security system.
     *
     * @param s The encrypted string that should be decrypted.
     * @return The decrypted version of the encrypted string.
     **/
    protected abstract String decrypt(String s);

    /**
     * Encrypts a non-encrypted string.
     *
     * @param s A non-encrypted string that be encrypted by this security system.
     * @return The encrypted version of the non-encrypted string.
     **/
    protected abstract String encrypt(String s);
}
