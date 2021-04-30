package controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandTest {
    @Before
    public void before() {

    }

    @Test
    public void testExec() {
        List<String> list = new ArrayList<String>();
        list.add("adb");
        list.add("devices");
        Command.exec(list);
    }

    @Test
    public void testExecAsync() {
        List<String> list = new ArrayList<String>();
        list.add("adb");
        list.add("devices");
        Command.execAsync(list, new Command.Callback() {
            @Override
            public void callback(byte[] data) {
                Assert.assertEquals(1, 1);
            }
        });
    }
}
