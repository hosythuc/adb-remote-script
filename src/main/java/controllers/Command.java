package controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class Command {
    interface Callback {
        void callback(byte[] data);
    }

    static void execAsync(List<String> command, Callback callback) {
        new Thread(()->{
           byte[] data = exec(command);
           if (callback != null) {
               callback.callback(data);
           }
        }).start();
    }

    static  byte[] exec(List<String> command) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process;
        try {
            process = builder.start();
           // process = Runtime.getRuntime().exec("adb devices");
            InputStream inputStream = process.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int read;
            byte[] dataChuck = new byte[16384];
            while ((read = inputStream.read(dataChuck, 0, dataChuck.length)) != -1) {
                buffer.write(dataChuck, 0, read);
            }
            buffer.flush();
            byte[] data = buffer.toByteArray();
            buffer.close();
            inputStream.close();
            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
