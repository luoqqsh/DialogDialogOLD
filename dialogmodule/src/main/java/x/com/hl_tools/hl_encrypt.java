package x.com.hl_tools;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密
 */
public class hl_encrypt {
    /**
     * MD5算法
     */
    public static String Md5(String plainText) {
        StringBuffer buf = null;
        if ("".equals(plainText)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (buf.toString().substring(8, 24)).toUpperCase();
    }

    /**
     * DES加密
     *
     * @param encryptString 明文
     * @param keyStr        解密key
     * @param ivStr         偏移量
     * @return 密文
     */
    public static String encrypt(String encryptString, String keyStr, String ivStr) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
        DESKeySpec dks = new DESKeySpec(keyStr.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return new String(Base64.encode(cipher.doFinal(encryptString.getBytes()), Base64.NO_WRAP));
    }

    /**
     * DES解密
     *
     * @param decryptString 密文
     * @param keyStr        加密key
     * @param ivStr         加密偏移量
     * @return 明文
     */
    public static String decrypt(String decryptString, String keyStr, String ivStr) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
//        SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DES");
        DESKeySpec dks = new DESKeySpec(keyStr.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.decode(decryptString, Base64.NO_WRAP)));
    }
}
