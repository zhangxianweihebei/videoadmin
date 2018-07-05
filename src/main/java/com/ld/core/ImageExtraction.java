package com.ld.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 图像提取
 */
public class ImageExtraction {

	private static String tmpPath = "d:/tmp/";
	private static String forMat = "PNG";

	Java2DFrameConverter converter = new Java2DFrameConverter();
	ImageView[] imageView;
	/**
	 * @Title:ImageExtraction
	 * @Description:TODO
	 */
	public ImageExtraction(ImageView... imageView) {
		this.imageView = imageView;
	}



	/**
	 * 获取视频截图
	 * @param sourceFile 源文件
	 * @param num  数量
	 * @return
	 * @throws IOException 
	 */
	public List<File> getScreenshots(File sourceFile,int... num) throws Exception {
		FFmpegFrameGrabber ff = getFFmpegFrameGrabber(sourceFile);
		List<File> images = new ArrayList<>();
		int frameNum = ff.getLengthInFrames();//获取视频帧数
		int k=0;
		for (int i=0;i<frameNum;i++){
			if(k == num.length) {
				break;
			}
			for (int j = 0; j < num.length; j++) {
				int p = num[j];
				if(p == i) {
					File createImage = createImage(ff.grabImage());
					images.add(createImage);
					imageView[j].setImage(new Image(new FileInputStream(createImage)));
					++k;
				}
			}
			ff.grab();

		}
		ff.stop();
		return images;
	}



	File image = null;
	public File createImage(Frame f){
		BufferedImage bi = converter.getBufferedImage(f);
		try {
			image = new File(tmpPath+new Date().getTime()+"."+forMat);
			ImageIO.write(bi, forMat,image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}


	/**
	 * 获取一个文件处理对象
	 * @param sourceFile
	 * @return
	 */
	public FFmpegFrameGrabber getFFmpegFrameGrabber(File sourceFile){
		FFmpegFrameGrabber ff = new FFmpegFrameGrabber(sourceFile);
		asynchronousExe(ff);
		return ff;
	}

	/**
	 * 异步处理读取
	 */
	public void asynchronousExe(FFmpegFrameGrabber ff){
		try {
			ff.start();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
class ThreadImage implements Runnable{
	FFmpegFrameGrabber ff;
	int i;
	List<File> images;
	int[] num;
	public ThreadImage(FFmpegFrameGrabber ff,int i,List<File> images,int... num) {
		this.ff = ff;
		this.i = i;
		this.images = images;
		this.num = num;
	}
	@Override
	public void run() {
		Frame f = null;
		try {
			f = ff.grabImage();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if(f != null){
			for(int j:num){
				if(j == i && f != null){
					images.add(new ImageExtraction().createImage(f));
				}
			}
		}
	}
}
