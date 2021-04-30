import views.Main;

import java.awt.*;

public class Application implements Runnable{

    public static void main(String... args) {
        EventQueue.invokeLater(new Application());
    }
    @Override
    public void run() {
        new Main();
    }
}
