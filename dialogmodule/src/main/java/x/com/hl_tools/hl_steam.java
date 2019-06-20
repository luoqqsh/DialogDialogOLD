package x.com.hl_tools;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class hl_steam {
    /**
     * hl_sharedP中使用
     */
    public static String objectToSteam(Object obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(obj);
        return new String(Base64.encode(os.toByteArray(), Base64.DEFAULT));
    }

    public static <T> T streamToObject(String str) throws IOException, ClassNotFoundException {
        byte[] base64 = Base64.decode(str.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream is = new ByteArrayInputStream(base64);
        ObjectInputStream bis = new ObjectInputStream(is);
        return (T) bis.readObject();
    }
}
