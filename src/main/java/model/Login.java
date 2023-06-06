package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Login {

    private long uid;
    private long cid;
    private String username;
    private String passwordHash;
    private int permissions;

    public Login(long uid, long cid, String username, String passwordHash, int permissions) {
        this.uid = uid;
        this.cid = cid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.permissions = permissions;
    }

    public Login(long cid, String username, String upass, int permissions) {
        this.cid = cid;
        this.username = username;
        this.passwordHash = generatePasswordHash(upass);
        this.permissions = permissions;
    }

    public long getUid() {
        return this.uid;
    }

    public long getCid() {
        return this.cid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public int getPermissions() { return this.permissions; }

     public void setPermissions(int permissions) { this.permissions = permissions; }

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
        byte[] testHash = new byte[0];
        try {
            testHash = skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

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
