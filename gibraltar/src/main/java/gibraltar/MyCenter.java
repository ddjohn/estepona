package gibraltar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import gibraltar.Main.IMain;
import gibraltar.logs.DLog;

public class MyCenter extends JPanel implements MouseListener, Runnable, IMain {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyCenter.class);

	private IDevice device = null;
	private BufferedImage image;

	public MyCenter(Main main) {
		DLog.method(TAG, "MyCenter()");
		
		main.register(this);
		
		addMouseListener(this);
		new Thread(this).start();
	}

	@Override
	public void run() {
		DLog.method(TAG, "run()");
		
		RawImage screenshot;
		while(true) {
			try {
				Thread.sleep(100);
				if(device != null) {
					screenshot = device.getScreenshot();
					image = screenshot.asBufferedImage();
					repaint();
					DLog.debug(TAG, "Redraw...");
				}
			} catch (TimeoutException | AdbCommandRejectedException | IOException | InterruptedException e) {
				DLog.error(TAG,  "run", e);
			}		
		}		
	}
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
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
	public void deviceChange(IDevice device) {
		DLog.method(TAG, "deviceChange(): " + device);
		
		this.device = device;
		
		try {
			image = device.getScreenshot().asBufferedImage();
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			setSize(new Dimension(image.getWidth(), image.getHeight()));
		} catch (TimeoutException | AdbCommandRejectedException | IOException e) {
			DLog.error(TAG, "screenshot", e);
			this.setPreferredSize(new Dimension(800, 600));
		}
	}
}
