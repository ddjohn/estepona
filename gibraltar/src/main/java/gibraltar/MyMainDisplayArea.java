package gibraltar;

import javax.swing.*;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MyMainDisplayArea extends JPanel implements MouseListener, Runnable {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyMainDisplayArea.class);
	
	private BufferedImage image;
	private IDevice device;

    public MyMainDisplayArea(IDevice device, BufferedImage image) {
    	this.device = device;
        this.image = image;
        
        addMouseListener(this);
        new Thread(this).start();
    }

    public void updateImage(BufferedImage img) {
        this.image = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

	@Override
	public void mouseClicked(MouseEvent me) {
		DLog.method(TAG, "mouseClicked(): " + me);		
		
		int x = me.getX();
	    int y = me.getY();

	    
	    tap(x, y);
	}

    public void tap(int x, int y) {
        String cmd = String.format("input tap %d %d", x, y);
        try {
			device.executeShellCommand(cmd, new NullOutputReceiver());
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			DLog.error(TAG,  "tap", e);
		}
    }
    
	@Override
	public void mousePressed(MouseEvent me) {}

	@Override
	public void mouseReleased(MouseEvent me) {}

	@Override
	public void mouseEntered(MouseEvent me) {}
	@Override
	public void mouseExited(MouseEvent me) {}

	@Override
	public void run() {
		RawImage screenshot;
		while(true) {
			try {
				Thread.sleep(100);
				screenshot = device.getScreenshot();
				updateImage(screenshot.asBufferedImage());
				DLog.info(TAG, "Redraw...");
			} catch (TimeoutException | AdbCommandRejectedException | IOException | InterruptedException e) {
				DLog.error(TAG,  "run", e);
			}		
		}	
	}
}
