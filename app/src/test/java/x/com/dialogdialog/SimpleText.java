package x.com.dialogdialog;


import java.io.File;

public class SimpleText {

    public static void main(String[] args) {

        String dirPath = "/Users/xrxing/Downloads/output";
        File dirFile = new File(dirPath);
        File[] files = dirFile.listFiles();
        for (File f : files) {
            String name = f.getName();
            if (!name.endsWith(".apk")) {
                continue;
            }
            if (!name.contains("_sign")) {
                continue;
            }
            if (name.contains("jiagu")) {
                f.delete();
                continue;
            }
            int i1 = name.indexOf("_");
            int i2 = name.indexOf("_", i1 + 1);
            int i3 = name.indexOf("_", i2 + 1);

            name = name.substring(i3 + 1);
            name = name.replace("_sign", "");

            if (name == null || name.equals("")) {
                f.delete();
            } else {
                File file = new File(dirFile, name);
                if (file.exists()) {
                    file.delete();
                }
                f.renameTo(file);
            }
        }
    }
}
