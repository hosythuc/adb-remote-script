package common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {
    @Before
    public void before() {

    }

    @Test
    public void testRunningJarPath() {
        String path = Utils.getRunningJarPath();
        Assert.assertEquals("D:\\MYPROJECTS\\AdbRemoteScript\\target", path);
    }
}
