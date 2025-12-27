package gibraltar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import gibraltar.compass.MyCenter;
import gibraltar.compass.MyNorth;
import gibraltar.compass.MySouth;
import gibraltar.compass.MyWest;
import gibraltar.helpers.MyUtils;
import gibraltar.iface.IMyMainListener;
import gibraltar.logs.DLog;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(Main.class);
	
	private List<IMyMainListener> listeners = new ArrayList<IMyMainListener>();

	public Main() {
		DLog.method(TAG, "Main()");
		
		MyUtils.lookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		setLayout(new BorderLayout());
		
		getContentPane().add(new MyCenter(this), BorderLayout.CENTER);
		getContentPane().add(new MySouth(this), BorderLayout.SOUTH);
		getContentPane().add(new MyWest(this), BorderLayout.WEST);
		getContentPane().add(new MyEast(this), BorderLayout.EAST);
		getContentPane().add(new MyNorth(this), BorderLayout.NORTH);
		
		setTitle("ADB screencopy");
		setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("adb.png")));
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);	
	}
	
	public static void main(String[] args) throws InterruptedException {
		DLog.init(args.length == 0);

		DLog.method(TAG, "main()");

		AndroidDebugBridge.init(false);
		
		@SuppressWarnings("deprecation")
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge();
	
		while(!adb.hasInitialDeviceList()) {
			DLog.debug(TAG, "Retry...");
			Thread.sleep(1000);
		}

		DLog.info(TAG, "connected=" + adb.isConnected());
		
		new Main();
	}

	public void register(IMyMainListener main) {
		DLog.method(TAG, "register(): " + main);

		listeners.add(main);
	}

	public void setDevice(IDevice device) {
		DLog.method(TAG, "setDevice(): " + device);

		DLog.debug(TAG, "Updating " + listeners.size() + " subscribers...");
		for(IMyMainListener listener : listeners) {
			listener.deviceChange(device);
		}
		
		setLocationRelativeTo(null);
		pack();
	}
}
