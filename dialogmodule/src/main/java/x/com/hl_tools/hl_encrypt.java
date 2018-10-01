package x.com.hl_tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
