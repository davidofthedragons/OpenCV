package tests;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class CaptureTest1 extends JFrame {
	
	VideoCapture cap;
	VideoCapture capUSB;
	BufferedImage imgr;
	BufferedImage imgl;
	int fps = 50;
	
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JPanel right = new JPanel() {
		public void paint(Graphics g) {
			g.drawImage(imgr, 0, 0, null);
		}
	};
	JPanel left = new JPanel() {
		public void paint(Graphics g) {
			g.drawImage(imgl, 0, 0, null);
		}
	};
	
	public CaptureTest1() {
		super("Capture Test 1");
		setSize(648*2, 488);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cap = new VideoCapture(1);
		capUSB = new VideoCapture(0);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!cap.isOpened() || !capUSB.isOpened()) {
			System.out.println("Could not find camera");
			System.exit(1);
		}
		System.out.println("Found camera " + cap.toString());
		
		splitPane.setRightComponent(right);
		splitPane.setLeftComponent(left);
		add(splitPane);
		
		
		Thread capture = new Thread(new Runnable() {
			public void run() {
				while(true) {
					loop();
					try {
						Thread.sleep(1000/fps);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		capture.start();
		
		setVisible(true);
	}
	
	
	public void loop() {
		Mat frame = new Mat();
		cap.retrieve(frame);
		//Highgui.imwrite("im1.jpg", frame);
		MatOfByte tempMat = new MatOfByte();
		Highgui.imencode(".jpg", frame, tempMat);
		byte[] byteArray = tempMat.toArray();
		imgr = null;
		InputStream in = new ByteArrayInputStream(byteArray);
		try {
			imgr = ImageIO.read(in);
			right.repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame = new Mat();
		capUSB.retrieve(frame);
		//Highgui.imwrite("im1.jpg", frame);
		tempMat = new MatOfByte();
		Highgui.imencode(".jpg", frame, tempMat);
		byteArray = tempMat.toArray();
		imgl = null;
		in = new ByteArrayInputStream(byteArray);
		try {
			imgl = ImageIO.read(in);
			left.repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new CaptureTest1();
	}
	
	

}
