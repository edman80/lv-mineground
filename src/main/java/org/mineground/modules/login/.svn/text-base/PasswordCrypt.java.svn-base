package org.mineground.modules.login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.mineground.core.MinegroundProperties;

/**
 *
 * @file PasswordCrypt.java (2012)
 * @author Daniel Koenen
 * 
 */
public class PasswordCrypt {
    private static final SecureRandom RandomGenerator = new SecureRandom();
    
    /**
     * Creates a random salt for us. This will be used to salt encrypted or
     * hashed things, such as passwords.
     *
     * @param length The length of the salt we want.
     * @return A random string with the given length.
     * @throws NoSuchAlgorithmException
     */
    public static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[40];
        RandomGenerator.nextBytes(bytes);

        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        
        byte[] digest = sha1.digest(bytes);
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).substring(0, length);
    }
    
    /**
     * Returns the hashed password with the given salt.
     *
     * @param password The password to hash.
     * @param salt The salt to use.
     * @return The hashed and salted password.
     * @throws NoSuchAlgorithmException
     */
    public static String getPasswordHash(String password, String salt) throws NoSuchAlgorithmException {
        return getSha256(getSha256(password) + salt);
    }

    /**
     * Creates an unsalted hash for the given input with the given algorithm.
     *
     * @param algorithm The algorithm to use, see {@link MessageDigest.getInstance()}.
     * @param input The string to hash.
     * @return The hashed input.
     * @throws NoSuchAlgorithmException
     */
    private static String getUnsaltedHash(String algorithm, String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(input.getBytes(MinegroundProperties.DefaultCharset));
        byte[] digest = messageDigest.digest();

        return String.format(MinegroundProperties.DefaultLocale, "%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }

    /**
     * Convenience method to hash with the SHA-256 algorithm.
     *
     * @param message The message to hash.
     * @return The hashed message.
     * @throws NoSuchAlgorithmException
     */
    private static String getSha256(String message) throws NoSuchAlgorithmException {
        return getUnsaltedHash("SHA-256", message);
    }
}
