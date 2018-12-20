package com.yinsin.simter.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageHelper {
	private static ImageHelper imageHelper = null;

	public static ImageHelper getImageHelper() {
		if (imageHelper == null) {
			imageHelper = new ImageHelper();
		}
		return imageHelper;
	}

	public static void scaleImage(String sourceImagePath, String destinationPath, int width, int height, String format) {
		File file = new File(sourceImagePath);
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(file);

			Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = outputImage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();

			ImageIO.write(outputImage, format, new File(destinationPath));
		} catch (IOException e) {
			System.out.println("scaleImage方法压缩图片时出错了");
			e.printStackTrace();
		}

	}

	/***
	 * 将图片缩放到指定的高度或者宽度
	 * 
	 * @param sourceImagePath
	 *            图片源地址
	 * @param destinationPath
	 *            压缩完图片的地址
	 * @param width
	 *            缩放后的宽度
	 * @param height
	 *            缩放后的高度
	 * @param auto
	 *            是否自动保持图片的原高宽比例
	 * @param format
	 *            图图片格式 例如 jpg
	 */

	public static void scaleImageWithParams(String sourceImagePath, String destinationPath, int width, int height,
			boolean auto, String format) {

		try {
			File file = new File(sourceImagePath);
			BufferedImage bufferedImage = null;
			bufferedImage = ImageIO.read(file);
			if (auto) {
				ArrayList<Integer> paramsArrayList = getAutoWidthAndHeight(bufferedImage, width, height);
				width = paramsArrayList.get(0);
				height = paramsArrayList.get(1);
				System.out.println("自动调整比例，width=" + width + " height=" + height);
			}

			Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = outputImage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();
			ImageIO.write(outputImage, format, new File(destinationPath));
		} catch (Exception e) {
			System.out.println("scaleImageWithParams方法压缩图片时出错了");
			e.printStackTrace();
		}

	}

	private static int parseDoubleToInt(double sourceDouble) {
		int result = 0;
		result = (int) sourceDouble;
		return result;
	}

	private static ArrayList<Integer> getAutoWidthAndHeight(BufferedImage bufferedImage, int width_scale,
			int height_scale) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		double scale_w = getDot2Decimal(width_scale, width);

		System.out.println("getAutoWidthAndHeight width=" + width + "scale_w=" + scale_w);
		double scale_h = getDot2Decimal(height_scale, height);
		if (scale_w < scale_h) {
			arrayList.add(parseDoubleToInt(scale_w * width));
			arrayList.add(parseDoubleToInt(scale_w * height));
		} else {
			arrayList.add(parseDoubleToInt(scale_h * width));
			arrayList.add(parseDoubleToInt(scale_h * height));
		}
		return arrayList;

	}

	public static double getDot2Decimal(int a, int b) {

		BigDecimal bigDecimal_1 = new BigDecimal(a);
		BigDecimal bigDecimal_2 = new BigDecimal(b);
		BigDecimal bigDecimal_result = bigDecimal_1.divide(bigDecimal_2, new MathContext(4));
		Double double1 = new Double(bigDecimal_result.toString());
		System.out.println("相除后的double为：" + double1);
		return double1;
	}
}
