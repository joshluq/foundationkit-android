package es.joshluq.foundationkit.provider

/**
 * Interface for encryption providers. Implement this interface to provide data encryption and
 * decryption services.
 */
interface EncryptionProvider : Provider {

    /**
     * Encrypts the given data.
     *
     * @param data The string data to be encrypted.
     * @return The encrypted string.
     */
    fun encrypt(data: String): String

    /**
     * Decrypts the given data.
     *
     * @param data The encrypted string data to be decrypted.
     * @return The decrypted string.
     */
    fun decrypt(data: String): String
}
