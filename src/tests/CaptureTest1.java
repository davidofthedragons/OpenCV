package tests;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class CaptureTest1 extends JFrame {
	
	VideoCapture cap;
	BufferedImage img;
	int fps = 50;
	
	public CaptureTest1() {
		super("Capture Test 1");
		setSize(648, 488);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cap = new VideoCapture(0);
		if(!cap.isOpened()) {
			System.out.println("Could not find camera");
			System.exit(1);
		}
		System.out.println("Found camera " + cap.toString());
		
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
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	
	public void loop() {
		Mat frame = new Mat();
		cap.retrieve(frame);
		//Highgui.imwrite("im1.jpg", frame);
		MatOfByte tempMat = new MatOfByte();
		Highgui.imencode(".jpg", frame, tempMat);
		byte[] byteArray = tempMat.toArray();
		img = null;
		InputStream in = new ByteArrayInputStream(byteArray);
		try {
			img = ImageIO.read(in);
			repaint();
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
