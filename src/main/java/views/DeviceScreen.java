package views;

import common.Utils;
import controllers.Helpper;

import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DeviceScreen extends JComponent {
    private Helpper helpper;
    private Ellipse2D ellipse;

    private static final long MIN_SCREEN_REFRESH_INTERVAL = 250;

    private static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(720, 1080);

    private volatile BufferedImage bufferedImage;
    private volatile Thread mUpdateThread;
    public DeviceScreen(Helpper helpper) {
        super();
        this.helpper = helpper;

        ellipse = new Ellipse2D.Double(0, 0, 400, 300);
        mUpdateThread = new UpdateThread();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bufferedImage != null) {
            double dRatio = bufferedImage.getWidth() / (double) bufferedImage.getHeight();
            double fRatio = getWidth() / (double) getHeight();
            if (!Double.valueOf(dRatio).equals(fRatio))
                setScale(0.5);

            g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);

        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            String text = "Nothing signal";
            g.setColor(Color.WHITE);
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
        }
    }

    public void setScale(double scale) {
        Dimension d;
        if (bufferedImage == null)
            d = new Dimension((int) (DEFAULT_SCREEN_SIZE.getWidth() * scale),
                    (int) (DEFAULT_SCREEN_SIZE.getHeight() * scale));
        else
            d = new Dimension((int) (bufferedImage.getWidth() * scale), (int) (bufferedImage.getHeight() * scale));

        if (!getPreferredSize().equals(d)) {
            setPreferredSize(d);
            ((JFrame) getTopLevelAncestor()).pack();
        }
    }

    public void startUpdate() {
        if (isRendering()) {
            throw new IllegalStateException("Already started");
        }
        mUpdateThread = new UpdateThread();
        mUpdateThread.start();
    }

    public void stopUpdate() {
        if (!isRendering()) {
            throw new IllegalStateException("Already stopped");
        }
        mUpdateThread.interrupt();
        mUpdateThread = null;
        bufferedImage = null;
        repaint();
    }

    public boolean isRendering() {
        return mUpdateThread != null && !mUpdateThread.isInterrupted();
    }

    class UpdateThread extends Thread {
        @Override
        public void run() {
            super.run();
            long previousFrameTime = System.currentTimeMillis();

            while (!Thread.interrupted()) {
                long currentFrameTime = System.currentTimeMillis();
                long dT = currentFrameTime - previousFrameTime;
                previousFrameTime = currentFrameTime;

                if (dT < MIN_SCREEN_REFRESH_INTERVAL)
                    Utils.sleep(MIN_SCREEN_REFRESH_INTERVAL - dT);

                if (Thread.interrupted())
                    break;

                try {
                    bufferedImage = helpper.getSreenshot();

                    long t2 = System.currentTimeMillis();
                    postFpsCount((t2 - currentFrameTime));

                    if (Thread.interrupted())
                        bufferedImage = null;

                    if (bufferedImage == null)
                        abort();
                    else
                        repaintPanel();

                } catch (IIOException e) {
                    e.printStackTrace();
                   // Logger.w("Corrupted data received from device, skipping one frame");
                    // Screencap is partially corrupted for that frame, let's try at the next one
                } catch (IOException e) {
                    e.printStackTrace();
                    abort();
                }
            }
        }
        private void abort() {
            interrupt();
            mUpdateThread = null;
            bufferedImage = null;

            Utils.executeOnUiThread(() -> {
              //  Logger.e("Unable to retrieve screen capture, aborting rendering...");
                repaint();
            });
        }

        private void repaintPanel() {
            Utils.executeOnUiThread(DeviceScreen.this::repaint);
        }

        private void postFpsCount(final double dt) {
           // Utils.executeOnUiThread(() -> ((MainFrame) getTopLevelAncestor()).getTitleManger().postFpsCount(1d / dt));
        }

    }
}
