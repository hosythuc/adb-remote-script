package widget;

import javax.swing.*;

public class DeviceMenu extends JMenu {
    private ButtonGroup mButtonGroup;

    public DeviceMenu(String s) {
        super(s);
        mButtonGroup = new ButtonGroup();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        mButtonGroup = new ButtonGroup();
    }

    public void setMenuItem(JRadioButtonMenuItem menuItem) {
        super.add(menuItem);
        mButtonGroup.add(menuItem);
    }
}
