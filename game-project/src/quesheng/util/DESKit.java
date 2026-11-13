/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESKit {
    static String key = "nihao985";

    public static String Encrypt(String content) {
        byte[] encrypted = DESKit.DES_CBC_Encrypt(content.getBytes(), key.getBytes());
        return DESKit.byteToHexString(encrypted);
    }

    public static String Decrypt(String content) {
        byte[] decrypted = DESKit.DES_CBC_Decrypt(DESKit.hexStringToBytes(content), key.getBytes());
        return new String(decrypted);
    }

    private static byte[] DES_CBC_Encrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(1, (Key)key, new IvParameterSpec(keySpec.getKey()));
            byte[] result = cipher.doFinal(content);
            return result;
        }
        catch (Exception e) {
            System.out.println("exception:" + e.toString());
            return null;
        }
    }

    private static byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, (Key)key, new IvParameterSpec(keyBytes));
            byte[] result = cipher.doFinal(content);
            return result;
        }
        catch (Exception e) {
            System.out.println("exception:" + e.toString());
            return null;
        }
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; ++i) {
            int pos = i * 2;
            d[i] = (byte)(DESKit.toByte(hexChars[pos]) << 4 | DESKit.toByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            String sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private static byte toByte(char c) {
        byte b = (byte)"0123456789ABCDEF".indexOf(c);
        return b;
    }
}

