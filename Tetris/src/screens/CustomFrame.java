package screens;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class CustomFrame extends JFrame{
    public CustomFrame(String title) throws HeadlessException {
        super(title);
        frameInit();
    }
	public void dispose() {
        TetrisOpeningScreen.openScreen();
		super.dispose();
	}
}
