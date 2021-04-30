package models;

import common.Utils;

public class Deivce implements Comparable<Deivce> {
    private String serial;
    private String product;
    private String model;
    private String device;
    private boolean available;

    public  Deivce(String line) {
        String[] array = line.split(" ");
        serial = array[0];

        for (int i = 1; i < array.length; i++) {
            if (!Utils.isEmpty(array[i])) {
                available = "device".equals(array[i]);
                break;
            }
        }

        for (String s : array) {
            if (s.startsWith("product:"))
                product = s.substring("product:".length());
            else if (s.startsWith("model:"))
                model = s.substring("model:".length());
            else if (s.startsWith("device:"))
                device = s.substring("device:".length());
        }

        if (!available)
            model = serial.substring(0, Math.min(serial.length(), 4)) + "... (UNAVAILABLE)";
    }

    public String getReadableName() {
        return model;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Deivce)
            return serial.equals(((Deivce) object).serial) && available == ((Deivce) object).available;
        return false;
    }

    @Override
    public int compareTo(Deivce device) {
        return serial.compareTo(device.serial);
    }

    @Override
    public String toString() {
        return available ? model : "unavailable" + " [" + serial + "]";
    }

    public String getSerial() {
        return serial;
    }

}
