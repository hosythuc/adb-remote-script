package controllers;

import common.Utils;
import models.Deivce;

import javax.swing.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Handler implements Runnable{
    private Helpper helpper;
    private Thread thread;
    private List<Deivce> deivceList;

    public Handler(Helpper helpper) {
        this.helpper = helpper;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            final List<Deivce> devices = new LinkedList<>();

            Builder commandBuilder = helpper.getBuilder();
            byte[] data = Command.exec(commandBuilder.buildDevicesCommand());
            if (data != null) {
                String[] lines = new String(data).split("\\n");
                for (String line : lines) {
                    if (line.startsWith("*") || line.startsWith("List") || Utils.isEmpty(line)) // line.startsWith("adb
                        // server")
                        continue;
                    System.out.println(line);
                    devices.add(new Deivce(line));
                }
            }

            Collections.sort(devices);
            if (!Utils.equalsOrder(devices, deivceList)) {
                deivceList = devices;
                System.out.println(devices.toArray().toString());
                SwingUtilities.invokeLater(() -> helpper.OnDevicesChanged(devices));
            }

            Utils.sleep(500);
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }
    public void stop() {
        thread.interrupt();
        thread = null;
    }
}
