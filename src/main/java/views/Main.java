package views;

import common.Utils;
import controllers.Helpper;
import models.Deivce;
import widget.DeviceMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main extends JFrame {
    private DeviceMenu deviceMenu;
    private Helpper helpper;
    private DeviceScreen deviceScreen;

    public Main() throws HeadlessException {
        super();
        setSize(300, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        helpper = new Helpper(loadAdbExecutable());
        deviceScreen = new DeviceScreen(helpper);

        add(deviceScreen);

        initMenu();
       // pack();

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                helpper.release();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private void initMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        deviceMenu = new DeviceMenu("Devices");
        jMenuBar.add(deviceMenu);
        setJMenuBar(jMenuBar);
        JMenuItem noneMenuItem = new JRadioButtonMenuItem("None");
        deviceMenu.add(noneMenuItem);
        helpper.setOnAttachedDevicesChangedListener(new Helpper.OnAttachedDevicesChangedListener() {
            @Override
            public void onAttachedDevicesChanged(List<Deivce> devices) {
                for(int i = 0 ; i<devices.size(); i++) {
                    JMenuItem menuItem = new JMenuItem(devices.get(i).getReadableName());
                    Deivce deivce = devices.get(i);
                    menuItem.addActionListener(e -> setCurrentDevice(deivce));
                    System.out.println(devices.get(i).getReadableName());
                    deviceMenu.add(menuItem);
                }
            }
        });
    }

    private void setCurrentDevice(Deivce deivce) {
        if (deviceScreen.isRendering()) {
            deviceScreen.stopUpdate();
        }
        helpper.setCurrentDevice(deivce);
        deviceScreen.startUpdate();
    }



    private File loadAdbExecutable() {
        URL adbFile = getClass().getResource("/adb_2701_win");
        File file = new File(Utils.getRunningJarPath() + File.separator + "Temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        file.deleteOnExit();
        File dest = new File(file, "adb");
        dest.deleteOnExit();
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            Utils.createTempExecutable(adbFile, dest);
            return dest;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
