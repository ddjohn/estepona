package gibraltar;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

public class MyDevice extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyDevice.class);
	
	@SuppressWarnings("unused")
	private IDevice device;

	public MyDevice(IDevice device) throws TimeoutException, AdbCommandRejectedException, IOException {
		super("");
		DLog.method(TAG, "MyDevice()");
		
		this.device = device;
				
		setLayout(new BorderLayout());

		RawImage screenshot = device.getScreenshot();
		setTitle(device.getSerialNumber() + " (" + screenshot.width + "x" + screenshot.height + ")");
		
        add(new MyMainDisplayArea(device, screenshot.asBufferedImage()), BorderLayout.CENTER);
        add(new MyBottomPanel(device), BorderLayout.SOUTH);
        
        pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
