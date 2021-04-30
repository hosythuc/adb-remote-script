package controllers;

import common.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class BuilderTest {
    private Builder builder;
    @Before
    public void before() {
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
            Helpper helpper = new Helpper(dest);
            builder = new Builder(helpper);
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testBuildDevicesCommand() {
        builder.buildDevicesCommand();
    }
}
