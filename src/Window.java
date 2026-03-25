import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JWindow;
import javax.swing.Timer;
import java.io.File;
import java.io.IOException;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

public class Window extends JWindow implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener{
	
	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	public Robot robot;
	public static boolean activated;
	public volatile int x1, x2, y1, y2;
	public volatile boolean pressed;
	
	public Window() throws AWTException, NativeHookException {
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(this);
		GlobalScreen.addNativeMouseListener(this);
		GlobalScreen.addNativeMouseMotionListener(this);

		x1 = -10;
		x2 = -10;
		y1 = -10;
		y2 = -10;
		
		robot = new Robot();
		activated = false;
		pressed = false;
		setBounds(0, 0, WIDTH, HEIGHT);
		setBackground(new Color(0, 0, 0, 0));
		setAlwaysOnTop(true);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		
		new Timer(16, e -> {
			if (activated) repaint();
		}).start();
	}
	
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		String txt = NativeKeyEvent.getKeyText(e.getKeyCode());
		//System.out.println(txt);
		if (txt.equalsIgnoreCase("Escape")) {
			System.out.println("Exiting...");
			System.exit(0);
		}
		
		if (txt.equalsIgnoreCase("Alt")) {
			activated = !activated;
		}
	}
	
	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		if (activated) {
			x1 = e.getX();
			y1 = e.getY();
		}
		pressed = true;
	}
	
	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		if (activated) {
			x2 = e.getX();
			y2 = e.getY();
		}
	}
	
	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if (activated) {
			try {
				takeSS(x1, y1, x2, y2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			x1 = -10;
			x2 = -10;
			y1 = -10;
			y2 = -10;
			activated = false;
		}
		pressed = false;
	}
	
	public void takeSS(int x1, int y1, int x2, int y2) throws IOException {
		if (x1 == -10 || x2 == -10 || y1 == -10 || y2 == -10) return;
		setVisible(false);
		String date = String.valueOf(System.currentTimeMillis());
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}
		BufferedImage img = robot.createScreenCapture(new Rectangle(x1, y1, x2 - x1, y2 - y1));
		ImageIO.write(img, "png", new File("screenshots\\" + date + ".png"));
		System.out.println("Saved " + date + ".png");
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		requestFocus();
		
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		g2.setComposite(AlphaComposite.SrcOver);
		
		if (x1 == -10 || x2 == -10 || y1 == -10 || y2 == -10) return;
		
		g2.setColor(new Color(11, 125, 216, 90));
		g2.drawRect(x1, y1, x2 - x1, y2 - y1);
		g2.setColor(new Color(185, 213, 241, 90));
		g2.fillRect(x1, y1, x2 - x1, y2 - y1);
		//System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
	}


	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {}
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {}
	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {}

}