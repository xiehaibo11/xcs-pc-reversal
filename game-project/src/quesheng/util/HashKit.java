/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class HashKit {
    public static final long FNV_OFFSET_BASIS_64 = -3750763034362895579L;
    public static final long FNV_PRIME_64 = 1099511628211L;
    private static final SecureRandom random = new SecureRandom();
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
    private static final char[] CHAR_ARRAY = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static long fnv1a64(String key) {
        long hash = -3750763034362895579L;
        int size = key.length();
        for (int i = 0; i < size; ++i) {
            hash ^= (long)key.charAt(i);
            hash *= 1099511628211L;
        }
        return hash;
    }

    public static String md5(String srcStr) {
        return HashKit.hash("MD5", srcStr);
    }

    public static String sha1(String srcStr) {
        return HashKit.hash("SHA-1", srcStr);
    }

    public static String sha256(String srcStr) {
        return HashKit.hash("SHA-256", srcStr);
    }

    public static String sha384(String srcStr) {
        return HashKit.hash("SHA-384", srcStr);
    }

    public static String sha512(String srcStr) {
        return HashKit.hash("SHA-512", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            return HashKit.toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            ret.append(HEX_DIGITS[bytes[i] >> 4 & 0xF]);
            ret.append(HEX_DIGITS[bytes[i] & 0xF]);
        }
        return ret.toString();
    }

    public static String generateSalt(int saltLength) {
        StringBuilder salt = new StringBuilder(saltLength);
        for (int i = 0; i < saltLength; ++i) {
            salt.append(CHAR_ARRAY[random.nextInt(CHAR_ARRAY.length)]);
        }
        return salt.toString();
    }

    public static String generateSaltForSha256() {
        return HashKit.generateSalt(32);
    }

    public static String generateSaltForSha512() {
        return HashKit.generateSalt(64);
    }

    public static boolean slowEquals(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; ++i) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}

