package gibraltar.compass;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

import gibraltar.Main;
import gibraltar.helpers.MyAdbHelper;
import gibraltar.helpers.MyUtils;
import gibraltar.iface.IMyMainListener;
import gibraltar.logs.DLog;

public class MyCenter extends JPanel implements MouseListener, KeyListener, Runnable, IMyMainListener {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyCenter.class);

	private IDevice device = null;
	private BufferedImage image;

	public MyCenter(Main main) {
		DLog.method(TAG, "MyCenter()");
		
		main.register(this);
	
		try {
			DLog.info(TAG, "=> " + getClass().getClassLoader().getResourceAsStream("adb.png"));
			DLog.info(TAG, "=> " + getClass().getResourceAsStream("/resources/adb.png"));
			DLog.info(TAG, "=> " + getClass().getClassLoader().getResource("adb.png"));
			DLog.info(TAG, "=> " + getClass().getResource("/resources/adb.png"));

			image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("adb.png"));
			if(image == null) {
				//ImageIcon imageIcon = new ImageIcon(getClass().getResourceAsStream("/resources/adb.png"));
				//ImageIcon imageIcon = new ImageIcon(getClass().getResource("/resources/adb.png"));
				//image = (BufferedImage)imageIcon.getImage();
			}
			
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			setSize(new Dimension(image.getWidth(), image.getHeight()));
		} catch (IOException e) {
			DLog.error(TAG, "adb.png", e);
		}
		
		setFocusable(true);
		addMouseListener(this);
		addKeyListener(this);
		
		setFocusable(true);
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("A"), "action");
		setFocusable(true);

		getActionMap().put("action",new AbstractAction() {
            private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("A is pressed");
                //now it works
            }
        });
		setFocusable(true);
		
		new Thread(this).start();
	}

	@Override
	public void run() {
		DLog.method(TAG, "run()");
		
		RawImage screenshot;
		while(true) {
			MyUtils.sleep(100);
			
			if(device != null) {
				try {
					screenshot = device.getScreenshot();
					image = screenshot.asBufferedImage();
				} catch (TimeoutException | AdbCommandRejectedException | IOException e) {
					DLog.error(TAG, "screenshot", e);
				}
				
				DLog.debug(TAG, "Redraw...");
				repaint();
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
	    
	    MyAdbHelper.tap(device, x, y);
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

	@Override
	public void keyPressed(KeyEvent ke) {
		DLog.method(TAG, "keyPressed(): " + ke);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		DLog.method(TAG, "keyReleased(): " + ke);
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		DLog.method(TAG, "keyTyped(): " + ke);
	}
}
