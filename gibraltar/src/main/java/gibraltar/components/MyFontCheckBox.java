package gibraltar.components;

import java.awt.Font;

import javax.swing.JCheckBox;

public class MyFontCheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;

	public MyFontCheckBox(String title) {
		super(title);
		
		setFont(new Font("Arial", Font.PLAIN, 40));
	}
}
