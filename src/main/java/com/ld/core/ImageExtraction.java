package com.ld.core;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 图像提取
 */
public class ImageExtraction {

    private static String tmpPath = "d:/tmp/";
    private static String forMat = "PNG";

    Java2DFrameConverter converter = new Java2DFrameConverter();


    /**
     * 获取视频截图
     * @param sourceFile 源文件
     * @param num  数量
     * @return
     */
    public List<File> getScreenshots(String sourceFile,int... num) throws FrameGrabber.Exception {
        File file = new File(sourceFile);
        FFmpegFrameGrabber ff = getFFmpegFrameGrabber(file);
        int frameNum = ff.getLengthInFrames();//获取视频帧数
        System.out.println(new Date()+"总帧数:"+frameNum);
        List<File> images = new ArrayList<>();
        for (int i=0;i<frameNum;i++){
            Frame f =  ff.grabImage();
            if(f != null){
                for(int j:num){
                    if(j == i){
                        images.add(createImage(f));
                    }
                }
            }
            if(images.size() == num.length){
                System.out.println("结束");
                break;
            }
        }
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
