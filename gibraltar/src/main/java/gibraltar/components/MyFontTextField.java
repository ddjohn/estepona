package gibraltar.components;

import java.awt.Font;

import javax.swing.JTextField;

public class MyFontTextField extends JTextField {
	private static final long serialVersionUID = 1L;


	public MyFontTextField(String text) {
		super(text);

		setFont(new Font("Arial", Font.PLAIN, 40));
	}

}
