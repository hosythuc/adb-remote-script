package common;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Utils {
    public static boolean isEmpty(String str) {
        for (Character c : str.toCharArray())
            if (Character.isLetterOrDigit(c))
                return false;
        return true;
    }

    public static void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void executeOnUiThread(Runnable r) {
        SwingUtilities.invokeLater(r);
    }

    public static <T> boolean equalsOrder(List<T> c1, List<T> c2) {
        if (c1 == null || c2 == null || (c1.size() != c2.size()))
            return false;
        for (int i = 0; i < c1.size(); i++)
            if (!c1.get(i).equals(c2.get(i)))
                return false;
        return true;
    }

    public static String getRunningJarPath() {
        try {
            String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File classFile = new File(path);
            return classFile.getParent();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createTempExecutable(URL source, File dest) throws IOException {
        InputStream is = source.openStream();
        OutputStream os = new FileOutputStream(dest);
        byte[] b = new byte[2048];
        int length;
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);

        }

        is.close();
        os.close();
    }
}
