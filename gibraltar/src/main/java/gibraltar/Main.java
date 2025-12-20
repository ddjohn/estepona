package gibraltar;

import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IClientChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

public class Main implements IClientChangeListener, IDebugBridgeChangeListener, IDeviceChangeListener {	
	private static final String TAG = DLog.forTag(Main.class);
	
	private AndroidDebugBridge adb;
	
	public Main(String title) throws InterruptedException {		
		AndroidDebugBridge.init(false);
		
		AndroidDebugBridge.addClientChangeListener(this);
		AndroidDebugBridge.addDebugBridgeChangeListener(this);
		AndroidDebugBridge.addDeviceChangeListener(this);
		
		adb = AndroidDebugBridge.createBridge();
		
		System.out.println("connected=" + adb.isConnected());
		System.out.println("address=" + adb.getSocketAddress());
		System.out.println("bridge=" + adb.getBridge());
		
	
		while(!adb.hasInitialDeviceList()) {
			System.err.println("Retry...");
			Thread.sleep(1000);
		}

		System.out.println("connected=" + adb.isConnected());
	}

	public static void main(String[] args) throws InterruptedException, TimeoutException, AdbCommandRejectedException, IOException, ShellCommandUnresponsiveException {
		
		Main main = new Main("temp");
		for(IDevice device : main.getDevices()) {
			System.out.println("device=" + device);
	     
			System.out.println("propset=" + device.arePropertiesSet());
			System.out.println("serial=" + device.getSerialNumber());
			System.out.println("battery=" + device.getBatteryLevel());
			System.out.println("count=" + device.getPropertyCount());
			System.out.println("prop=" + device.getProperties());
			
			System.out.println("state=" + device.getState());
			System.out.println("sync=" + device.getSyncService());
			System.out.println("avd=" + device.getAvdName());
			System.out.println("file=" + device.getFileListingService());
			System.out.println("clients=" + device.getClients());
			for(Client client : device.getClients()) {
				System.out.println("client=" + client);
			}

		}
		
		main.close();

	}

	private IDevice[] getDevices() {
		return adb.getDevices();
	}

	private void close() {
		//AndroidDebugBridge.disconnectBridge();
		AndroidDebugBridge.terminate();
	}

	@Override
	public void clientChanged(Client client, int changeMask) {
		System.err.println("clientChanged(): " + client + changeMask);	
	}

	@Override
	public void bridgeChanged(AndroidDebugBridge bridge) {
		System.err.println("bridgeChanged(): " + bridge);			
	}

	@Override
	public void deviceConnected(IDevice device) {
		System.err.println("deviceConnected(): " + device);	
		
		try {
			MyDevice myDevice = new MyDevice(device);
		} catch (TimeoutException | AdbCommandRejectedException | IOException e) {
			DLog.error(TAG,  "Trying to connect...", e);
		}
	}

	@Override
	public void deviceDisconnected(IDevice device) {
		System.err.println("deviceDisconnected(): " + device);			
	}

	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		System.err.println("deviceChanged(): " + device + changeMask);	
		if(changeMask % IDevice.CHANGE_BUILD_INFO != 0) {
			System.err.println("deviceChanged(): change build info");	
		}
		if(changeMask % IDevice.CHANGE_CLIENT_LIST != 0) {
			System.err.println("deviceChanged(): change client list");	
		}
		if(changeMask % IDevice.CHANGE_STATE != 0) {
			System.err.println("deviceChanged(): change state");	
		}
	}
}
