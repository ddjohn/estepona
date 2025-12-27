package gibraltar.compass;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;

import gibraltar.Main;
import gibraltar.iface.IMyMainListener;
import gibraltar.logs.DLog;

public class MyWest extends JPanel implements IDeviceChangeListener, ListSelectionListener, ListDataListener, IMyMainListener {
	private static final long serialVersionUID = 1L;
	private static final String TAG = DLog.forTag(MyWest.class);
	
	private DefaultListModel<IDevice> model;
	private Main main;

	public MyWest(Main main) {
		DLog.method(TAG, "MyWest()");

		this.main = main;
		main.register(this);
		
		@SuppressWarnings("deprecation")
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge();

		adb.addDeviceChangeListener(this);
		
		TitledBorder border = BorderFactory.createTitledBorder("Toolbar");
		border.setTitleFont(new Font("Arial", Font.PLAIN, 32));
		setFont(new Font("Arial", Font.PLAIN, 48));
		setBorder(border);

		model = new DefaultListModel<>();

		JList<IDevice> list = new JList<IDevice>(model);
		list.setFont(new Font("Arial", Font.PLAIN, 40));

		setPreferredSize(new Dimension(400, 300));
		add(list);
		
		list.getSelectionModel().addListSelectionListener(this);
		model.addListDataListener(this);

		DLog.info(TAG,  "Load initial device list...");
		for(IDevice device : adb.getDevices()) {
			model.addElement(device);
		}	
	}
	
	@Override
	public void deviceConnected(IDevice device) {
		DLog.method(TAG, "deviceConnected(): " + device);	
		
		model.addElement(device);
	}

	@Override
	public void deviceDisconnected(IDevice device) {
		DLog.method(TAG, "deviceDisconnected(): " + device);		

		model.removeElement(device);
	}

	@Override
	public void deviceChanged(IDevice device, int changeMask) {
		DLog.method(TAG, "deviceChanged(): " + device + changeMask);	

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

	@Override
	public void valueChanged(ListSelectionEvent event) {
		DLog.method(TAG, "valueChanged(): " + event);	
		
        ListSelectionModel lsm = (ListSelectionModel)event.getSource();
		IDevice device =  model.get(lsm.getSelectedIndices()[0]);
		
		DLog.info(TAG, "Device selected: " + device);
		main.setDevice(device); 	
	}

	@Override
	public void contentsChanged(ListDataEvent event) {
		DLog.method(TAG, "contentsChanged(): " + event);	
		
	}

	@Override
	public void intervalAdded(ListDataEvent event) {
		DLog.method(TAG, "intervalAdded(): " + event);	
		
	}

	@Override
	public void intervalRemoved(ListDataEvent event) {
		DLog.method(TAG, "intervalRemoved(): " + event);	
		
	}

	@Override
	public void deviceChange(IDevice device) {
		DLog.method(TAG, "deviceChange(): " + device);		
	}
}
