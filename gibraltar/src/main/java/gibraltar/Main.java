package gibraltar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import gibraltar.logs.DLog;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(Main.class);
	
	private List<IMain> listeners = new ArrayList<IMain>();

	public Main() {
		DLog.method(TAG, "Main()");
		
		setLayout(new BorderLayout());
		
		add(new MyCenter(this), BorderLayout.CENTER);
		add(new MySouth(this), BorderLayout.SOUTH);
		add(new MyWest(this), BorderLayout.WEST);
		add(new MyFontButton("EAST"), BorderLayout.EAST);
		add(new MyFontButton("NORTH"), BorderLayout.NORTH);
		
		setTitle("ADB screencopy");
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);	
	}
	
	public static void main(String[] args) throws InterruptedException {
		DLog.method(TAG, "main()");

		DLog.init(false);
		
		AndroidDebugBridge.init(false);
		
		@SuppressWarnings("deprecation")
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge();
	
		while(!adb.hasInitialDeviceList()) {
			System.err.println("Retry...");
			Thread.sleep(1000);
		}

		System.out.println("connected=" + adb.isConnected());
		
		new Main();
	}

	public interface IMain {
		void deviceChange(IDevice device);
	}
	
	public void register(IMain main) {
		DLog.method(TAG, "register(): " + main);

		listeners.add(main);
	}

	public void setDevice(IDevice device) {
		DLog.method(TAG, "setDevice(): " + device);

		DLog.debug(TAG, "Updating " + listeners.size() + " subscribers...");
		for(IMain listener : listeners) {
			listener.deviceChange(device);
		}
		
		setLocationRelativeTo(null);
		pack();
	}
}
