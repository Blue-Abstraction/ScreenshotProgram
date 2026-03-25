import java.awt.AWTException;
import javax.swing.SwingUtilities;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
	
	public static void main(String[] args) throws AWTException, NativeHookException {
		SwingUtilities.invokeLater(() -> {
			try {
				new Window();
			} catch (AWTException e) {
				e.printStackTrace();
			} catch (NativeHookException e) {
				e.printStackTrace();
			}
		});
	}
	// hello bozo
}