package controllers;

import common.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HelpperTest {
    private Helpper helpper;
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
            helpper = new Helpper(dest);
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
       //
    }

    @Test
    public void testAdbPath() {
        Assert.assertEquals(helpper.getAdbPath(),"D:\\MYPROJECTS\\AdbRemoteScript\\target\\Temp\\adb");
    }

    @Test
    public void test(){
        assert true;
    }
}
