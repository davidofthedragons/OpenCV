package tests;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;

import utils.Utils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class FilterTest extends JFrame implements ChangeListener {
	
	VideoCapture cam = new VideoCapture(0);
	Mat frame = new Mat();
	Mat hsv = new Mat();
	Mat thresh = new Mat();
	BufferedImage img;
	
	Mat erodeElement= new Mat();
	Mat dilateElement = new Mat();
	
	int hmin=0, hmax=255, smin=0, smax=255, vmin=0, vmax=255;
	
	JSlider hmins = new JSlider(hmin, hmax);
	JSlider hmaxs = new JSlider(hmin, hmax);
	JSlider smins = new JSlider(smin, hmax);
	JSlider smaxs = new JSlider(smin, hmax);
	JSlider vmins = new JSlider(vmin, hmax);
	JSlider vmaxs = new JSlider(vmin, hmax);
	JSlider erode = new JSlider(0, 30);
	JSlider dilate = new JSlider(0, 30);
	
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
					Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);
					Core.inRange(hsv, new Scalar(hmin, smin, vmin), new Scalar(hmax, smax, vmax), thresh);
					if(erode.getValue()>0) Imgproc.erode(thresh, thresh, erodeElement);
					if(dilate.getValue()>0) Imgproc.dilate(thresh, thresh, dilateElement);
					
					img = Utils.matToBI(thresh);
					repaint();
				}
			}
		});
		update.start();
		
		setVisible(true);
		
		JFrame sliderFrame = new JFrame("Edit constraints");
		sliderFrame.setSize(640, 480);
		sliderFrame.setLayout(new GridLayout(6, 1));
		hmins.addChangeListener(this);
		hmins.setValue(0);
		hmaxs.addChangeListener(this);
		hmaxs.setValue(255);
		smins.addChangeListener(this);
		smins.setValue(0);
		smaxs.addChangeListener(this);
		smaxs.setValue(255);
		vmins.addChangeListener(this);
		vmins.setValue(0);
		vmaxs.addChangeListener(this);
		vmaxs.setValue(255);
		erode.addChangeListener(this);
		erode.setValue(0);
		dilate.addChangeListener(this);
		dilate.setValue(0);
		sliderFrame.add(hmins);
		sliderFrame.add(hmaxs);
		sliderFrame.add(smins);
		sliderFrame.add(smaxs);
		sliderFrame.add(vmins);
		sliderFrame.add(vmaxs);
		sliderFrame.add(erode);
		sliderFrame.add(dilate);
		JButton saveButton = new JButton("Save Configuration");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showSaveDialog(FilterTest.this);
				if(result == JFileChooser.CANCEL_OPTION) return;
				try {
					saveConfig(fileChooser.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		sliderFrame.add(saveButton);
		JButton loadButton = new JButton("Load Configuration"); 
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(FilterTest.this);
				if(result == JFileChooser.CANCEL_OPTION) return;
				try {
					loadConfig(fileChooser.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		sliderFrame.add(loadButton);
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hmin = 0; hmins.setValue(hmin);
				hmax = 255; hmaxs.setValue(hmax);
				smin = 0; smins.setValue(smin);
				smax = 255; smaxs.setValue(smax);
				vmin = 0; vmins.setValue(vmin);
				vmax = 255; vmaxs.setValue(vmax);
				erode.setValue(0);
				dilate.setValue(0);
			}
		});
		sliderFrame.add(resetButton);
		sliderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sliderFrame.setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	
	
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new FilterTest();
	}
	
	public void saveConfig(File file) throws IOException {
		if(!file.exists()) file.createNewFile();
		PrintWriter writer = new PrintWriter(file);
		writer.write(Integer.toString(hmin) + "\n");
		writer.write(Integer.toString(hmax) + "\n");
		writer.write(Integer.toString(smin) + "\n");
		writer.write(Integer.toString(smax) + "\n");
		writer.write(Integer.toString(vmin) + "\n");
		writer.write(Integer.toString(vmax) + "\n");
		writer.write(Integer.toString(erode.getValue()) + "\n");
		writer.write(Integer.toString(dilate.getValue()) + "\n");
		writer.flush();
		writer.close();
	}
	
	public void loadConfig(File file) throws IOException {
		Scanner scanner = new Scanner(file);
		hmin = Integer.parseInt(scanner.nextLine());
		hmins.setValue((int)hmin);
		hmax = Integer.parseInt(scanner.nextLine());
		hmaxs.setValue((int)hmax);
		smin = Integer.parseInt(scanner.nextLine());
		smins.setValue((int)smin);
		smax = Integer.parseInt(scanner.nextLine());
		smaxs.setValue((int)smax);
		vmin = Integer.parseInt(scanner.nextLine());
		vmins.setValue((int)vmin);
		vmax = Integer.parseInt(scanner.nextLine());
		vmaxs.setValue((int)vmax);
		erode.setValue(Integer.parseInt(scanner.nextLine()));
		dilate.setValue(Integer.parseInt(scanner.nextLine()));
		scanner.close();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		//System.out.println("Fire stateChanged()");
		
		if(e.getSource().equals(hmins)) hmin = hmins.getValue();
		else if(e.getSource().equals(hmaxs)) hmax = hmaxs.getValue();
		else if(e.getSource().equals(smins)) smin = smins.getValue();
		else if(e.getSource().equals(smaxs)) smax = smaxs.getValue();
		else if(e.getSource().equals(vmins)) vmin = vmins.getValue();
		else if(e.getSource().equals(vmaxs)) vmax = vmaxs.getValue();
		else if(e.getSource().equals(erode)) {
			if(erode.getValue()>0) erodeElement = Imgproc.getStructuringElement(
					Imgproc.MORPH_RECT, new Size(erode.getValue(), erode.getValue())); 
			else erodeElement = new Mat();
		}
		else if(e.getSource().equals(dilate)) {
			if(dilate.getValue()>0)dilateElement = Imgproc.getStructuringElement(
					Imgproc.MORPH_RECT, new Size(dilate.getValue(), dilate.getValue()));
			else dilateElement = new Mat();
		}
		
	}
}
