package tests;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;

import javax.swing.*;


public class FilterTest extends JFrame {
	
	VideoCapture cam = new VideoCapture(0);
	Mat frame;
	
	int fps = 20;
	
	public FilterTest() {
		super("Filter Test");
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if(!cam.isOpened()) {
			System.out.println("Camera not loaded");
			System.exit(1);
		}
		Thread update = new Thread(new Runnable() {
			public void run() {
				while(true) {
					cam.retrieve(frame);
					
				}
			}
		});
		update.start();
		
		setVisible(true);
	}
	
	
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new FilterTest();
	}
}
