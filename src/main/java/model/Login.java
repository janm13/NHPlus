package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Represents a login entry for a user in the system.
 * Stores the user's username, password hash, and permissions.
 */
public class Login {

    private long uid; // User ID
    private long cid; // Caregiver ID
    private String username; // Username
    private String passwordHash; // Password hash
    private int permissions; // User permissions

    /**
     * Constructs a Login object with the given parameters.
     *
     * @param uid           The user ID.
     * @param cid           The caregiver ID.
     * @param username      The username.
     * @param passwordHash  The password hash.
     * @param permissions   The user permissions.
     */
    public Login(long uid, long cid, String username, String passwordHash, int permissions) {
        this.uid = uid;
        this.cid = cid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.permissions = permissions;
    }

    /**
     * Constructs a Login object with the given parameters.
     * Generates the password hash based on the provided password.
     *
     * @param cid           The caregiver ID.
     * @param username      The username.
     * @param upass         The password.
     * @param permissions   The user permissions.
     */
    public Login(long cid, String username, String upass, int permissions) {
        this.cid = cid;
        this.username = username;
        this.passwordHash = generatePasswordHash(upass);
        this.permissions = permissions;
    }

    /**
     * Returns the user ID.
     *
     * @return The user ID.
     */
    public long getUid() {
        return this.uid;
    }

    /**
     * Returns the caregiver ID.
     *
     * @return The caregiver ID.
     */
    public long getCid() {
        return this.cid;
    }

    /**
     * Returns the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the password hash.
     *
     * @return The password hash.
     */
    public String getPasswordHash() {
        return this.passwordHash;
    }

    /**
     * Returns the user permissions.
     *
     * @return The user permissions.
     */
    public int getPermissions() {
        return this.permissions;
    }

    /**
     * Sets the user permissions.
     *
     * @param permissions The new user permissions.
     */
    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    /**
     * Generates a password hash based on the provided password.
     *
     * @param password The password to generate the hash for.
     * @return The generated password hash.
     */
    private static String generatePasswordHash(String password) {
        int iterations = 10000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] hash;
        try {
            hash = skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    /**
     * Validates the provided password against the stored password hash.
     *
     * @param originalPassword The original password.
     * @param storedPassword   The stored password hash.
     * @return True if the passwords match, false otherwise.
     */
    public static boolean validatePassword(String originalPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt;
        byte[] hash;
        try {
            salt = fromHex(parts[1]);
            hash = fromHex(parts[2]);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] testHash;
        try {
            testHash = skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param hex The hexadecimal string.
     * @return The byte array.
     * @throws NoSuchAlgorithmException if the SHA1 algorithm is not available.
     */
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * Generates a random salt.
     *
     * @return The salt as a byte array.
     */
    private static byte[] getSalt() {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param array The byte array.
     * @return The hexadecimal string.
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}