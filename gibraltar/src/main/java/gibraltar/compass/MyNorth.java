package gibraltar.compass;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IClientChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.clientmanager.DeviceClientManager;
import com.android.ddmlib.clientmanager.DeviceClientManagerListener;

import gibraltar.Main;
import gibraltar.components.MyFontCheckBox;
import gibraltar.components.MyFontTextField;
import gibraltar.iface.IMyMainListener;
import gibraltar.logs.DLog;

public class MyNorth extends JPanel implements IClientChangeListener, IDeviceChangeListener, IDebugBridgeChangeListener, IMyMainListener {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyNorth.class);
	
	private JCheckBox isConnected;
	AndroidDebugBridge adb;
	private MyFontCheckBox isManaged;
	JTextField deviceName;
	private MyFontTextField adbVersion;
	
	public MyNorth(Main main) {
		DLog.method(TAG, "MyNorth()");
		
		main.register(this);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		isConnected = new MyFontCheckBox("Connected...");
		isManaged = new MyFontCheckBox("Managed...");
		deviceName = new MyFontTextField("device:<none>");
		//deviceName.setPreferredSize(new Dimension(200, 32));
		//deviceName.setSize(new Dimension(200, 32));
		adbVersion = new MyFontTextField("adb:<unknown>");

		add(isConnected);
		add(isManaged);
		add(deviceName);
		add(adbVersion);
		
		adb = AndroidDebugBridge.createBridge();
		adb.addClientChangeListener(this);
		adb.addDebugBridgeChangeListener(this);
		adb.addDeviceChangeListener(this);
	}

	private void updateStats() {
		DLog.info(TAG, "Update stats...");
		
		isConnected.setSelected(adb.isConnected());
		isManaged.setSelected(adb.isUserManagedAdbMode());
		adbVersion.setText("adb:" + adb.getCurrentAdbVersion());
	}
	
	@Override
	public void bridgeChanged(AndroidDebugBridge bridge) {
		updateStats();
	}

	@Override
	public void deviceConnected(IDevice device) {
		updateStats();
	
	}

	@Override
	public void deviceDisconnected(IDevice device) {
		updateStats();
	}

	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		updateStats();
	}

	@Override
	public void clientChanged(Client client, int changeMask) {
		updateStats();	
	}

	@Override
	public void deviceChange(IDevice device) {
		deviceName.setText("device:" + device.toString());
	}
}
