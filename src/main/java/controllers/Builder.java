package controllers;

import java.util.LinkedList;
import java.util.List;

public class Builder {
    private Helpper helpper;

    public Builder(Helpper helpper) {
        this.helpper = helpper;
    }

    public List<String> buildAdbCommand() {
        List<String> command = new LinkedList<>();
        command.add(helpper.getAdbPath());
        return command;
    }

    public List<String> buildDevicesCommand() {
        List<String> command = buildAdbCommand();
        command.add("devices");
        command.add("-l");
        return command;
    }

    public List<String> buildKillServerCommand() {
        List<String> command = buildAdbCommand();
        command.add("kill-server");
        command.add("-l");
        return command;
    }

    public List<String> buildScreencapCommand() {
        List<String> command = buildDeviceSpecificCommand();
        command.add("exec-out");
        command.add("screencap");
        command.add("-p");
        return command;
    }

    public List<String> buildDeviceSpecificCommand() {
        List<String> command = buildAdbCommand();
        command.add("-s");
        command.add(helpper.getCurrentDevice().getSerial());
        return command;
    }
}
