package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class Utils {
	public static BufferedImage matToBI(Mat m) {
		MatOfByte tempMat = new MatOfByte();
		Highgui.imencode(".jpg", m, tempMat);
		byte[] byteArray = tempMat.toArray();
		InputStream in = new ByteArrayInputStream(byteArray);
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
