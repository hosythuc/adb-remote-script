package controllers;

import models.Deivce;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Helpper {
    public static final int INVALID_DEVICE_INDEX = -1;

    private String adbPath;
    private Builder builder;
    private Handler handler;

    private Deivce currentDevice;

    private OnAttachedDevicesChangedListener listener;

    public Helpper(File adbFile) {
        adbPath = adbFile.getAbsolutePath();
        builder = new Builder(this);
        Runtime.getRuntime().addShutdownHook(new Thread(()->Command.execAsync(builder.buildKillServerCommand(), null)));
        handler = new Handler(this);
        handler.start();
    }

    public String getAdbPath() {
        return adbPath;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void OnDevicesChanged(List<Deivce> devices) {
        if (listener != null) {
            listener.onAttachedDevicesChanged(devices);
        }
    }

    public void setOnAttachedDevicesChangedListener(OnAttachedDevicesChangedListener listener) {
        this.listener = listener;
    }

    public interface OnAttachedDevicesChangedListener {
        void onAttachedDevicesChanged(List<Deivce> devices);
    }

    public void release() {
        handler.stop();
    }

    public Deivce getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Deivce device) {
        currentDevice = device;
    }

    public BufferedImage getSreenshot() throws IOException {
        if(currentDevice == null) {
            return null;
        }
        byte[] data = Command.exec(builder.buildScreencapCommand());
        if (data == null) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }

}
