package gibraltar.subbuttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.UIManager;

import gibraltar.DLog;

public abstract class MyButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static String TAG = DLog.forTag(MyButton.class);

	public MyButton(String title, String iconName) {
		super(title);

		this.addActionListener(this);
		this.setIcon(UIManager.getIcon(iconName));
	}

	public abstract void action();
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		DLog.method(TAG, "actionPerformed(): " + ae);
		
	    action();	
	}
}

/*
UIManager.getIcon("FileView.directoryIcon");
UIManager.getIcon("FileView.fileIcon");
UIManager.getIcon("FileView.computerIcon");
UIManager.getIcon("FileView.hardDriveIcon");
UIManager.getIcon("FileView.floppyDriveIcon");

UIManager.getIcon("FileChooser.newFolderIcon");
UIManager.getIcon("FileChooser.upFolderIcon");
UIManager.getIcon("FileChooser.homeFolderIcon");
UIManager.getIcon("FileChooser.detailsViewIcon");
UIManager.getIcon("FileChooser.listViewIcon");

UIManager.getIcon("Tree.collapsedIcon");
UIManager.getIcon("Tree.openIcon");
UIManager.getIcon("Tree.leafIcon");
UIManager.getIcon("Tree.closedIcon");

UIManager.getIcon("OptionPane.questionIcon");
UIManager.getIcon("OptionPane.errorIcon");
UIManager.getIcon("OptionPane.informationIcon");
UIManager.getIcon("OptionPane.warningIcon");
*/