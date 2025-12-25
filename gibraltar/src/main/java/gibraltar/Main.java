package gibraltar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import com.android.ddmlib.AndroidDebugBridge;
import gibraltar.logs.DLog;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(Main.class);
	
	public Main() {
		DLog.method(TAG, "Main2()");
		
		setLayout(new BorderLayout());
		
		MyCenter center;
		add(center = new MyCenter(this), BorderLayout.CENTER);
		
		MySouth south;
		add(south = new MySouth(), BorderLayout.SOUTH);
		
		add(new MyWest(center, south), BorderLayout.WEST);
		
		add(new MyFontButton("EAST"), BorderLayout.EAST);
		add(new MyFontButton("NORTH"), BorderLayout.NORTH);
		
		setTitle("ADB screencopy");
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);	
	}
	
	public static void main(String[] args) throws InterruptedException {
		AndroidDebugBridge.init(false);
		
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge();
	
		while(!adb.hasInitialDeviceList()) {
			System.err.println("Retry...");
			Thread.sleep(1000);
		}

		System.out.println("connected=" + adb.isConnected());
		
		new Main();
	}
}
